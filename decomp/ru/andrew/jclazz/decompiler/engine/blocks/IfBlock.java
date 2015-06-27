package ru.andrew.jclazz.decompiler.engine.blocks;

import ru.andrew.jclazz.decompiler.engine.ops.*;

import java.util.*;

public class IfBlock extends Block
{
    private List andConditions = new ArrayList();

    private Else elseBlock;

    private boolean isNegConditions = false;

    public IfBlock(Block parent)
    {
        super(parent);
    }

    public void setElseBlock(Else elseBlock)
    {
        this.elseBlock = elseBlock;
        // Remove last goto
        if (getLastOperation() instanceof GoToView)
        {
            removeLastOperation();
        }
    }

    public Else getElseBlock()
    {
        return elseBlock;
    }

    private boolean isElseIf = false;   // For printing only

    public void setElseIf(boolean elseIf)
    {
        isElseIf = elseIf;
    }

    public long getStartByte()
    {
        if (andConditions != null && andConditions.size() > 0 && ((List) andConditions.get(0)).size() > 0)
        {
            Condition cond = (Condition) ((List) andConditions.get(0)).get(0);
            return cond.getStartByte();
        }
        return super.getStartByte();
    }

    public String getSource()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(isElseIf ? "else " : "").append("if ");
        if (isNegConditions) sb.append("(!");
        if (andConditions.size() > 1) sb.append("(");
        for (Iterator i = andConditions.iterator(); i.hasNext();)
        {
            List orConditions = (List) i.next();
            if (orConditions.size() > 1) sb.append("(");
            for (Iterator j = orConditions.iterator(); j.hasNext();)
            {
                Condition cond = (Condition) j.next();
                if (j.hasNext() && orConditions.size() > 1) cond.setNeedReverseOperation(false);
                sb.append("(" + cond.str() + ")");
                if (j.hasNext()) sb.append(" || ");
            }
            if (orConditions.size() > 1) sb.append(")");
            if (i.hasNext()) sb.append(" && ");
        }
        if (andConditions.size() > 1) sb.append(")");
        if (isNegConditions) sb.append(")");
        sb.append(NL).append(super.getSource());
        return sb.toString();
    }

    public void addAndConditions(List ops)
    {
        List orConditions = new ArrayList();
        List newOps = new ArrayList();
        Iterator i = ops.iterator();
        while (i.hasNext())
        {
            OperationView ci = (OperationView) i.next();
            newOps.add(ci);
            if (ci instanceof IfView)
            {
                orConditions.add(new Condition((IfView) ci, this, new ArrayList(newOps)));
                newOps.clear();
            }
        }

        andConditions.add(orConditions);
    }

    public void analyze(Block block)
    {
        for (Iterator i = andConditions.iterator(); i.hasNext();)
        {
            List orConditions = (List) i.next();
            for (Iterator j = orConditions.iterator(); j.hasNext();)
            {
                Condition cond = (Condition) j.next();
                cond.analyze(block);
            }
        }

        // Can be break in back loop
        List firstOrConditions = (List) andConditions.get(0);
        long target = ((Condition) firstOrConditions.get(firstOrConditions.size() - 1)).getIfOperation().getTargetOperation();
        long start = ((Condition) firstOrConditions.get(firstOrConditions.size() - 1)).getIfOperation().getStartByte();
        if (block.getLastOperation().getStartByte() < target && block instanceof Loop)
        {
            Block par = block;
            do
            {
                par = par.getParent();
                if (par == null) break;
            }
            while (par.getOperationByStartByte(target) == null);
            
            if (par != null && par.getOperationPriorTo(target) instanceof Loop)
            {
                GoToView got = new FakeGoToView(start, target);
                got.setBreak(true);
                ops.add(ops.size(), got);
                isNegConditions = true;
            }
        }
    }
}