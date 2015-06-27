package ru.andrew.jclazz.code.codeitems.blocks;

import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.code.codeitems.*;

import java.io.*;
import java.util.*;

public class Loop extends Block
{
    private List andConditions = new ArrayList();

    private boolean printPrecondition;   //WHILE(){} vs DO{}WHILE()
    private boolean isBackLoop;          // If conditions at the end of block then true

    private long begin_pc;

    // For-style specific variables
    private boolean isInForStyle = false;
    private long incPartStartByte;

    public Loop(Block parent, boolean isBackLoop)
    {
        super(parent);
        this.isBackLoop = isBackLoop;
        this.printPrecondition = !isBackLoop;
    }

    public void setPrintPrecondition(boolean precondition)
    {
        this.printPrecondition = precondition;
    }

    public void postCreate()
    {
        // Remove last goto
        if (!isBackLoop && (getLastOperation() instanceof GoTo))
        {
            // TODO bug for inner loops
            //GoTo lastGoto = (GoTo) removeLastOperation();
            GoTo lastGoto = (GoTo) getLastOperation();
            lastGoto.setLoop(this);
            begin_pc = lastGoto.getTargetOperation();
        }
        else
        {
            begin_pc = getFirstOperation().getStartByte();
        }
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
                Condition cond = new Condition((If) ci, this, new ArrayList(newOps));
                cond.setNeedReverseOperation(!isBackLoop);
                orConditions.add(cond);
                newOps.clear();
            }
        }
        andConditions.add(orConditions);
    }

    public void addAndConditions2(List conditions)
    {
        andConditions.addAll(conditions);
    }

    public List getConditions()
    {
        return andConditions;
    }

    public boolean isBackLoop()
    {
        return isBackLoop;
    }

    public void setIncrementalPartStartOperation(long incPartStartByte)
    {
        this.incPartStartByte = incPartStartByte;
        isInForStyle = true;
    }

    public long getBeginPc()
    {
        return begin_pc;
    }

    public void print(PrintWriter pw, String init_indent)
    {
        if (isInForStyle)
        {
            pw.print(init_indent + "for (; ");
            for (Iterator i = andConditions.iterator(); i.hasNext();)
            {
                List orConditions = (List) i.next();
                if (orConditions.size() > 1) pw.print("(");
                for (Iterator j = orConditions.iterator(); j.hasNext();)
                {
                    Condition cond = (Condition) j.next();
                    if (j.hasNext() && orConditions.size() > 1) cond.setNeedReverseOperation(false);
                    pw.print(cond.str());
                    if (j.hasNext()) pw.print(" || ");
                }
                if (orConditions.size() > 1) pw.print(")");
                if (i.hasNext()) pw.print(" && ");
            }
            pw.print(";");
            for (Iterator i = ops.iterator(); i.hasNext();)
            {
                CodeItem citem = (CodeItem) i.next();
                if (citem.getStartByte() >= incPartStartByte)
                {
                    String opStr = ((Operation) citem).str();
                    if (!"".equals(opStr)) pw.print(" " + opStr);
                }
            }
            pw.println(")");

            pw.println(init_indent + "{");
            for (Iterator i = ops.iterator(); i.hasNext();)
            {
                CodeItem citem = (CodeItem) i.next();
                if (citem.getStartByte() >= incPartStartByte) break;
                citem.print(pw, init_indent + "    ");
            }
            pw.println(init_indent + "}");
            return;
        }
        if (!printPrecondition)
        {
            pw.println(init_indent + "do");
            super.print(pw, init_indent);
        }
        pw.print(init_indent + "while ");
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
                if (j.hasNext()) pw.print(isBackLoop ? " && " : " || ");
            }
            if (orConditions.size() > 1) pw.print(")");
            if (i.hasNext()) pw.print(isBackLoop ? " || " : " && ");
        }
        if (andConditions.size() > 1) pw.print(")");
        pw.println(printPrecondition ? "" : ";");
        if (printPrecondition)
        {
            super.print(pw, init_indent);
        }
    }

    public void postanalyze(Block block)
    {
        if (isBackLoop)
        {
            // Checking if last operation is Nop for correct seekEnd
            if (!(getLastOperation() instanceof Nop))
            {
                addOperation(size(), new Nop(-1));
            }
            this.seekEnd();
        }

        // All other conditions are analyzed with themselves
        for (Iterator i = andConditions.iterator(); i.hasNext();)
        {
            List orConditions = (List) i.next();
            for (Iterator j = orConditions.iterator(); j.hasNext();)
            {
                Condition cond = (Condition) j.next();
                cond.analyze(isBackLoop ? this : block);
            }
        }
    }
}
