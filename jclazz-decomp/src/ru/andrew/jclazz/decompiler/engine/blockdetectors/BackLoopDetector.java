package ru.andrew.jclazz.decompiler.engine.blockdetectors;

import java.util.Map.Entry;
import ru.andrew.jclazz.decompiler.engine.Converter.TransferPoint;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

import java.util.*;

public class BackLoopDetector implements Detector
{
    public void analyze(Block block)
    {
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            if (!(citem instanceof IfView)) continue;

            IfView ifCond = (IfView) citem;
            if (!ifCond.isForwardBranch())
            {
                createBackLoop(block, ifCond);
            }
        }
    }

    private void createBackLoop(Block block, IfView ifCond)
    {
        CodeItem preLoop = block.getOperationPriorTo(ifCond.getTargetOperation());
        boolean printCondition = false;
        if (preLoop != null && preLoop instanceof GoToView &&
            ((GoToView) preLoop).isForwardBranch() &&
            ((GoToView) preLoop).getTargetOperation() < ifCond.getStartByte())
        {
            printCondition = true;
            block.removeOperation(preLoop.getStartByte());
            // TODO set label for conditions
        }

        Loop loop = new Loop(block, true);
        loop.setPrintPrecondition(printCondition);

        //try-catch inside loop with break in try block results in loop block inside catch
        boolean isLoopCreated = false;
        if (block instanceof Catch)
        {
            Try tryBlock = (Try) block.getParent().getOperationPriorTo(block.getStartByte());
            if (tryBlock.getStartByte() >= ifCond.getTargetOperation())
            {
                block.getParent().createSubBlock(ifCond.getTargetOperation(), ifCond.getStartByte(), loop);
                isLoopCreated = true;
            }
        }

        if (!isLoopCreated)
        {
            block.createSubBlock(ifCond.getTargetOperation(), ifCond.getStartByte(), loop);
        }

        List firstConditions = block.createSubBlock(ifCond.getStartByte(), ifCond.getStartByte() + 1, null);
        loop.addAndConditions(firstConditions); // TODO smth with multiple conditions
    }

    public Loop collapseBackLoops(Block block)
    {
        if (!(block instanceof Loop) || (block.size() > 2) || (block.size() == 0) || !(block.getFirstOperation() instanceof Loop)) return null;
        if ((block.size() == 2) && !(block.getLastOperation() instanceof NopView)) return null;

        Loop loop = ((Loop) block.getFirstOperation());
        if (!loop.isBackLoop()) return null;
        loop.addAndConditions2(((Loop) block).getConditions());
        loop.setParent(block.getParent());
        return loop;
    }
    
    // NG

    public void analyze_2(Block topBlock, Converter conv)
    {
        for (Iterator i = conv.getTransferPoints().iterator(); i.hasNext();)
        {
            Converter.TransferPoint tp = (TransferPoint) i.next();
            if (!tp.isFwd() && checkBackLoop(tp, conv, topBlock))
            {
                createBackLoop_2(tp, topBlock);

                conv.getTransferPoints().remove(tp);
                ((Collection) conv.getTransfers().get(new Long(tp.getTargetByte()))).remove(tp);
            }
        }
    }

    private boolean checkBackLoop(TransferPoint loop, Converter conv, Block topBlock)
    {
        long begin = loop.getTargetByte();
        long end = loop.getStartByte();

        // Check there are no incoming transitions from outside
        for (Iterator it = conv.getTransfers().entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Entry) it.next();
            long target = ((Long) entry.getKey()).longValue();
            if (target >= begin && target <= end)
            {
                Collection points = (Collection) entry.getValue();
                for (Iterator pit = points.iterator(); pit.hasNext();)
                {
                    TransferPoint tp = (TransferPoint) pit.next();
                    long point = tp.getStartByte();
                    if (point < begin || point > end)
                    {
                        return false;
                    }
                }
            }
        }

        // Check there are no outgoing transitions outside
        for (Iterator it = conv.getTransferPoints().iterator(); it.hasNext();)
        {
            TransferPoint tp = (TransferPoint) it.next();
            long point = tp.getStartByte();
            if (point >= begin && point <= end)
            {
                long target = tp.getTargetByte();
                if (target < begin)
                {
                    return false;
                }
                else if (target > end)
                {
                    long after_byte = topBlock.getOperationAfterRecursivly(end).getStartByte();
                    if (after_byte != target)
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void createBackLoop_2(TransferPoint tp, Block topBlock)
    {
        IfView ifCond = (IfView) topBlock.getOperationByStartByteRecursivly(tp.getStartByte());
        Block block = (Block) topBlock.getBlockForOperationRecursivly(tp.getStartByte());

        CodeItem preLoop = block.getOperationPriorTo(ifCond.getTargetOperation());
        boolean printCondition = false;
        if (preLoop != null && preLoop instanceof GoToView &&
            ((GoToView) preLoop).isForwardBranch() &&
            ((GoToView) preLoop).getTargetOperation() < ifCond.getStartByte())
        {
            printCondition = true;
            block.removeOperation(preLoop.getStartByte());
            // TODO set label for conditions
        }

        Loop loop = new Loop(block, true);
        loop.setPrintPrecondition(printCondition);

        block.createSubBlock(ifCond.getTargetOperation(), ifCond.getStartByte(), loop);

        List firstConditions = block.createSubBlock(ifCond.getStartByte(), ifCond.getStartByte() + 1, null);
        loop.addAndConditions(firstConditions); // TODO smth with multiple conditions
    }
}
