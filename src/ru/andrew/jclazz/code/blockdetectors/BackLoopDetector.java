package ru.andrew.jclazz.code.blockdetectors;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.codeitems.ops.*;

import java.util.*;

public class BackLoopDetector implements Detector
{
    public void analyze(Block block)
    {
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            if (!(citem instanceof If)) continue;
            If ifCond = (If) citem;

            if (!ifCond.isForwardBranch())
            {
                createBackLoop(block, ifCond);
            }
        }
    }

    private void createBackLoop(Block block, If ifCond)
    {
        CodeItem preLoop = block.getOperationPriorTo(ifCond.getTargetOperation());
        boolean printCondition = false;
        if (preLoop != null && preLoop instanceof GoTo &&
            ((GoTo) preLoop).isForwardBranch() &&
            ((GoTo) preLoop).getTargetOperation() < ifCond.getStartByte())
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

    public Loop collapseBackLoops(Block block)
    {
        if (!(block instanceof Loop) || (block.size() > 2) || (block.size() == 0) || !(block.getFirstOperation() instanceof Loop)) return null;
        if ((block.size() == 2) && !(block.getLastOperation() instanceof Nop)) return null;

        Loop loop = ((Loop) block.getFirstOperation());
        if (!loop.isBackLoop()) return null;
        loop.addAndConditions2(((Loop) block).getConditions());
        loop.setParent(block.getParent());
        return loop;
    }
}
