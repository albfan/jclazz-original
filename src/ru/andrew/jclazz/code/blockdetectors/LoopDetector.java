package ru.andrew.jclazz.code.blockdetectors;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.codeitems.ops.*;

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

            if (!ifCond.isForwardBranch())
            {
                createBackLoop(block, ifCond);
                continue;
            }

            CodeItem priorTarget = block.getOperationPriorTo(ifCond.getTargetOperation());

            if (priorTarget != null && priorTarget instanceof If)
            {
                continue;
            }
            if ((priorTarget == null) || (!(priorTarget instanceof GoTo)))
            {
                continue;
            }

            GoTo priorTargetGoTo = (GoTo) priorTarget;

            if (!priorTargetGoTo.isForwardBranch() && !isIfContinue(block, priorTargetGoTo))
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

    private void createBackLoop(Block block, If ifCond)
    {
        BranchCondition branchCond = new BranchCondition(ifCond);
        block.replaceCurrentOperation(branchCond);

        CodeItem preLoop = block.getOperationPriorTo(ifCond.getTargetOperation());
        boolean isPreCondition = false;
        if (preLoop != null && preLoop instanceof GoTo &&
                ((GoTo) preLoop).isForwardBranch() &&
                ((GoTo) preLoop).getTargetOperation() < ifCond.getStartByte())
        {
            isPreCondition = true;
            block.removeOperation(preLoop.getStartByte());
        }

        Loop loop = new Loop(block, isPreCondition);
        branchCond.setConditionBlock(loop, isPreCondition);
        block.createSubBlock(ifCond.getTargetOperation(), ifCond.getStartByte() + 1, loop);
    }

    private void createForwardLoop(Block block, If ifCond)
    {
        BranchCondition branchCond = new BranchCondition(ifCond);
        block.replaceCurrentOperation(branchCond);

        Loop loop = new Loop(block, true);
        branchCond.setConditionBlock(loop);
        block.createSubBlock(ifCond.getStartByte() + 1, ifCond.getTargetOperation(), loop);
    }
}
