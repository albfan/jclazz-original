package ru.andrew.jclazz.code.codeitems.blocks;

import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.codeitems.ops.*;

import java.io.*;

public class Else extends Block
{
    public Else(Block parent)
    {
        super(parent);
    }

    public void print(PrintWriter pw, String init_indent)
    {
        if ( ((size() == 1) && (getFirstOperation() instanceof IfBlock)) ||
             ((size() == 2) && (getFirstOperation() instanceof IfBlock) && (getLastOperation() instanceof Else)) )
        {
            ((IfBlock) getFirstOperation()).setElseIf(true);
            getFirstOperation().print(pw, init_indent);
            if (size() == 2)
            {
                getLastOperation().print(pw, init_indent);
            }
        }
        else
        {
            pw.println(init_indent + "else");
            super.print(pw, init_indent);
        }
    }

    public void postCreate()
    {
        // Last "goto" in else block can be "break" or "continue" if enclosed in loop
        if (!(getLastOperation() instanceof GoTo)) return;

        long target_byte = ((GoTo) getLastOperation()).getTargetOperation();
        // Checking if last goto forwards to the begining of loop block, thus it is "continue"
        if (!((GoTo) getLastOperation()).isForwardBranch())
        {
            Block prevBlock = getParent();
            while (prevBlock != null && !(prevBlock instanceof Loop))
            {
                prevBlock = prevBlock.getParent();
            }
            if (prevBlock != null)
            {
                if (((Loop) prevBlock).getBeginPc() == target_byte)
                {
                    ((GoTo) getLastOperation()).setContinue(true);
                    return;
                }
            }
        }

        // Trying to find target operation for "break"
        Block prevBlock = getParent();
        while (prevBlock != null)
        {
            CodeItem ff_oper = prevBlock.getOperationByStartByte(target_byte);
            if (ff_oper != null)
            {
                CodeItem prev_ff = prevBlock.getOperationPriorTo(target_byte);
                if (prev_ff instanceof Loop) // Found target operation after loop
                {
                    ((GoTo) getLastOperation()).setBreak(true); // Set goto to break
                    return;
                }
            }
            prevBlock = prevBlock.getParent();
        }
    }
}
