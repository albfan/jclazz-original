package ru.andrew.jclazz.code.codeitems.blocks;

import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.code.codeitems.*;

import java.io.*;
import java.util.*;

public class Loop extends Block
{
    private boolean precondition;   //WHILE(){} vs DO{}WHILE() 
    private String ifCondition;

    private long begin_pc;

    // For-style specific variables
    private boolean isInForStyle = false;
    private long incPartStartByte;

    public Loop(Block parent, boolean precondition)
    {
        super(parent);
        this.precondition = precondition;
    }

    public void postCreate()
    {
        // Remove last goto
        if (precondition && (getLastOperation() instanceof GoTo))
        {
            GoTo lastGoto = (GoTo) removeLastOperation();
            begin_pc = lastGoto.getTargetOperation();
        }
        else
        {
            begin_pc = getFirstOperation().getStartByte();
        }
    }

    public void setIfCondition(String ifCondition)
    {
        this.ifCondition = ifCondition;
    }

    public boolean isPrecondition()
    {
        return precondition;
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
            pw.print(init_indent + "for (; " + ifCondition + ";");
            for (Iterator i = ops.iterator(); i.hasNext();)
            {
                CodeItem citem = (CodeItem) i.next();
                if (citem.getStartByte() >= incPartStartByte)
                {
                    pw.print(" " + ((Operation) citem).str());
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
        if (!precondition)
        {
            pw.println(init_indent + "do");
            super.print(pw, init_indent);
        }
        pw.println(init_indent + "while (" + ifCondition + ")" + (precondition ? "" : ";"));
        if (precondition)
        {
            super.print(pw, init_indent);
        }
    }

}
