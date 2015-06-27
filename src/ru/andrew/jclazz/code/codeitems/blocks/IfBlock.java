package ru.andrew.jclazz.code.codeitems.blocks;

import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.code.codeitems.*;

import java.io.*;
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
        if (getLastOperation() instanceof GoTo)
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

    public void print(PrintWriter pw, String init_indent)
    {
        pw.print(init_indent + (isElseIf ? "else " : "") + "if ");
        if (isNegConditions) pw.print("(!");
        if (andConditions.size() > 1) pw.print("(");
        for (Iterator i = andConditions.iterator(); i.hasNext();)
        {
            List orConditions = (List) i.next();
            if (orConditions.size() > 1) pw.print("(");
            for (Iterator j = orConditions.iterator(); j.hasNext();)
            {
                Condition cond = (Condition) j.next();
                if (j.hasNext() && orConditions.size() > 1) cond.setNeedReverseOperation(false);
                pw.print("(" + cond.str() + ")");
                if (j.hasNext()) pw.print(" || ");
            }
            if (orConditions.size() > 1) pw.print(")");
            if (i.hasNext()) pw.print(" && ");
        }
        if (andConditions.size() > 1) pw.print(")");
        if (isNegConditions) pw.print(")");
        pw.println();
        super.print(pw, init_indent);
    }

    public void addAndConditions(List ops)
    {
        List orConditions = new ArrayList();
        List newOps = new ArrayList();
        Iterator i = ops.iterator();
        while (i.hasNext())
        {
            CodeItem ci = (CodeItem) i.next();
            newOps.add(ci);
            if (ci instanceof If)
            {
                orConditions.add(new Condition((If) ci, this, new ArrayList(newOps)));
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
                GoTo got = new GoTo(start, target);
                got.setBreak(true);
                ops.add(ops.size(), got);
                isNegConditions = true;
            }
        }
    }
}
