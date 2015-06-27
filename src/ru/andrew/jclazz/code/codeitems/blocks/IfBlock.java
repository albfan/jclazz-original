package ru.andrew.jclazz.code.codeitems.blocks;

import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.code.codeitems.*;

import java.io.*;
import java.util.*;

public class IfBlock extends Block
{
    private Condition firstCondition;
    private List andConditions = new ArrayList();

    private Else elseBlock;

    public IfBlock(Block parent, Operation ifOp)
    {
        super(parent);
        this.firstCondition = new Condition(ifOp, this, null);
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
        pw.println();
        super.print(pw, init_indent);
    }

    public void addAndConditions(List ops, boolean isFirstAnd)
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
                orConditions.add(new Condition((Operation) ci, this, new ArrayList(newOps)));
                newOps.clear();
            }
        }

        if (isFirstAnd)
        {
            orConditions.add(0, firstCondition);
        }
        else if (andConditions.isEmpty())
        {
            List firstAndConditions = new ArrayList(1);
            firstAndConditions.add(firstCondition);
            andConditions.add(firstAndConditions);
        }
        andConditions.add(orConditions);
    }

    public void analyze(Block block)
    {
        firstCondition.analyze(block);

        boolean skipFirst = true;
        // All other conditions are analyzed with themselves
        for (Iterator i = andConditions.iterator(); i.hasNext();)
        {
            List orConditions = (List) i.next();
            for (Iterator j = orConditions.iterator(); j.hasNext();)
            {
                Condition cond = (Condition) j.next();
                if (skipFirst)
                {
                    skipFirst = false;
                    continue;
                }
                cond.analyze(cond);
            }
        }

        if (andConditions.isEmpty())
        {
            List firstAndConditions = new ArrayList(1);
            firstAndConditions.add(firstCondition);
            andConditions.add(firstAndConditions);
        }
    }
}
