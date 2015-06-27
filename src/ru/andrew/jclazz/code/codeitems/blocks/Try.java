package ru.andrew.jclazz.code.codeitems.blocks;

import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.attributes.*;

import java.io.*;
import java.util.*;

public class Try extends Block
{
    public static boolean DEBUG = false;

    private List catchBlocks = new ArrayList();
    private List finallyExceptions = new ArrayList();

    private int goto_cmd = -1;

    private long start_pc;
    private long end_pc;

    public Try(long start_pc, long end_pc, Block parent)
    {
        super(parent);
        this.start_pc = start_pc;
        this.end_pc = end_pc;
    }

    public void addCatchBlock(Catch _catch)
    {
        catchBlocks.add(_catch);
    }

    public List getCatchBlocks()
    {
        return catchBlocks;
    }

    public void setFinallyException(List excTable)
    {
        finallyExceptions = excTable;
    }

    public long getStartPC()
    {
        return start_pc;
    }

    public long getEndPC()
    {
        return end_pc;
    }

    public int getRetrieveOperation()
    {
        return goto_cmd;
    }

    public void print(PrintWriter pw, String init_indent)
    {
        pw.println(init_indent + "try");
        super.print(pw, init_indent);
    }

    public void postCreate()
    {
        CodeItem gop = getLastOperation();
        if (gop instanceof GoTo)
        {
            goto_cmd = (int) ((GoTo) gop).getTargetOperation();
        }
        else
        {
            goto_cmd = Integer.MAX_VALUE;
        }
    }

    public void postProcess()
    {
        // Checking dublicated finally block in try block
        if (end_pc < getLastOperation().getStartByte())
        {
            truncate(end_pc);
        }

        // Checking dublicated finally block in catch blocks
        Iterator i = finallyExceptions.iterator();
        while (i.hasNext())
        {
            Code.ExceptionTable et = (Code.ExceptionTable) i.next();

            for (Iterator cit = catchBlocks.iterator(); cit.hasNext();)
            {
                Catch _catch = (Catch) cit.next();

                if (et.end_pc > _catch.getFirstOperation().getStartByte() &&
                        et.end_pc < _catch.getLastOperation().getStartByte() &&
                        et.handler_pc > _catch.getLastOperation().getStartByte())
                {
                    _catch.truncate(et.end_pc);
                }
            }
        }

        // Checking last goto: break or continue
        if (getLastOperation() instanceof GoTo)
        {
            if (!checkLastGoTo((GoTo) getLastOperation()))
            {
                removeLastOperation();
            }
        }
    }

    private boolean checkLastGoTo(GoTo oper)
    {
        Block ff_block = this;
        CodeItem ff_oper;
        do
        {
            ff_oper = ff_block.getOperationByStartByte(oper.getTargetOperation());
            if (ff_oper != null) break;
            ff_block = ff_block.getParent();
        }
        while (ff_block != null);
        if (ff_oper == null) return false;

        CodeItem prevFF = ff_block.getOperationPriorTo(ff_oper.getStartByte());
        if (prevFF instanceof Loop)
        {
            oper.setBreak(true);
            return true;
        }
        if (ff_block instanceof Loop)
        {
            Loop ff_loop = (Loop) ff_block;
            if (!ff_loop.isBackLoop() && (ff_loop.getBeginPc() == oper.getTargetOperation()))
            {
                oper.setContinue(true);
                return true;
            }
            else if (ff_loop.isBackLoop())
            {
                if (!(prevFF instanceof Catch))
                {
                    oper.setContinue(true);
                    return true;
                }
            }
        }

        return false;
    }
}
