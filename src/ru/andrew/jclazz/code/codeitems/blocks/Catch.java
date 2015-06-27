package ru.andrew.jclazz.code.codeitems.blocks;

import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.*;

import java.io.*;

public class Catch extends Block
{
    private CONSTANT_Class exceptionClassInfo;
    private long handler_pc;

    private LocalVariable lv;

    public Catch(long handler_pc, CONSTANT_Class cl_info, Block parent)
    {
        super(parent);
        this.handler_pc = handler_pc;
        exceptionClassInfo = cl_info;
    }

    public long getStartByte()
    {
        return handler_pc;
    }

    public void postProcess()
    {
        // Adding Push for first POP operation
        if (getFirstOperation() instanceof Pop)
        {
            Pop pop = (Pop) removeFirstOperation();
            if (exceptionClassInfo != null)
            {
                lv = getLocalVariable(pop.getLocalVariableNumber(), exceptionClassInfo.getFullyQualifiedName(), true);
            }
        }

        // Removing last goto operation for catch blocks
        if (getLastOperation() instanceof GoTo)
        {
            if (!checkLastGoTo((GoTo) getLastOperation()))
            {
                removeLastOperation();
            }
        }

        // Removing last PUSH and THROW operations for finally blocks
        if (exceptionClassInfo == null)
        {
            if (getLastOperation() instanceof Throw)
            {
                removeLastOperation();
                if (getLastOperation() instanceof Push)
                {
                    removeLastOperation();
                }
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

    public String getExceptionType()
    {
        return exceptionClassInfo != null ? exceptionClassInfo.getFullyQualifiedName() : null;
    }

    public void print(PrintWriter pw, String init_indent)
    {
        if (exceptionClassInfo != null)
        {
            pw.println(init_indent + "catch (" + alias(exceptionClassInfo.getFullyQualifiedName()) + " " + (lv != null ? lv.getName() : "") + ")");
        }
        else
        {
            pw.println(init_indent + "finally");
        }
        super.print(pw, init_indent);
    }
    
}
