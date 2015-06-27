package ru.andrew.jclazz.code.blockdetectors;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.codeitems.ops.*;

import java.util.*;

public class LoopDetector implements Detector
{
    public void analyze(Block block)
    {
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            if (!(citem instanceof If)) continue;
            If ifCond = (If) citem;

            CodeItem priorTarget = block.getOperationPriorTo(ifCond.getTargetOperation());

            if (priorTarget != null && priorTarget instanceof If)
            {
                priorTarget = block.getOperationPriorTo(((If) priorTarget).getTargetOperation());
            }

            // Inner loop
            if (priorTarget == null) priorTarget = block.getLastOperation();   // TODO better detection of target operation

            if ((priorTarget == null) || (!(priorTarget instanceof GoTo)))
            {
                continue;
            }

            GoTo priorTargetGoTo = (GoTo) priorTarget;

            if (!priorTargetGoTo.isForwardBranch() && (priorTargetGoTo.getLoop() != null || !isIfContinue(block, priorTargetGoTo)))
            {
                createForwardLoop(block, ifCond);
                continue;
            }
        }
    }

    private boolean isIfContinue(Block block, GoTo priorTarget)
    {
        // priorTarget - backward goto, but this is not loop
        // Case: if - continue for while (...) {...} block
        Block loop = block;
        while (loop != null)
        {
            if ((loop instanceof Loop) && (((Loop) loop).getBeginPc() == priorTarget.getTargetOperation()))
            {
                // This is "if - continue" case
                return true;
            }
            loop = loop.getParent();
        }
        return false;
    }

    private boolean isCompoundForwardLoop(Block block, If ifCond)
    {
        if (ifCond.getTargetOperation() <= block.getLastOperation().getStartByte())
        {
            return false;
        }
        if (!(block instanceof Loop))
        {
            return false;
        }

        CodeItem next = block.getParent().getOperationAfter(block.getStartByte());
        return next.getStartByte() == ifCond.getTargetOperation();
    }

    private void createForwardLoop(Block block, If firstIf)
    {
        CodeItem firstPriorOp = block.getOperationPriorTo(firstIf.getTargetOperation());
        If ifCond = firstPriorOp instanceof If ? (If) firstPriorOp : firstIf;

        if (isCompoundForwardLoop(block, ifCond))
        {
            List conditions = block.createSubBlock(0, ifCond.getStartByte() + 1, null);
            ((Loop) block).addAndConditions(conditions);
            return;
        }

        Loop loop = new Loop(block, false);
        block.createSubBlock(ifCond.getStartByte() + 1, ifCond.getTargetOperation(), loop);
        List firstConditions = block.createSubBlock(firstIf.getStartByte(), ifCond.getStartByte() + 1, null);
        loop.addAndConditions(firstConditions);
    }
}
