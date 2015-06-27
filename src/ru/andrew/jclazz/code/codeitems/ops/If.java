package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

import java.io.*;

/**
 * Opcodes: 148 - 166, 198, 199<BR>
 * Parameters: branch(2) for 153 - 166, 198, 199; no for 148 - 152<BR>
 * Operand stack: <BR>
 * if[non]null(198, 199), if[cond](153-158): value => <BR>
 * [f,d]cmp[op](149-152), lcmp(148): value1, value2 => result<BR>
 * if_[a,i]cmp[cond](159-166): value1, value2 => <BR>
 */
public class If extends Operation
{
    private String var1;
    private String var2;
    private long targetOperation;
    private boolean isPush;
    private String nullVar; // for ifnull & ifnonnull opcodes

    public If(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        if (opcode >= 148 && opcode <= 152)
        {
            isPush = true;
            targetOperation = -1;
        }
        else
        {
            isPush = false;
            long offset = (params[0] << 8) | params[1];
            if (offset > 32767) offset = offset - 65536;
            targetOperation = start_byte + offset;
        }
    }

    public long getTargetOperation()
    {
        return targetOperation;
    }

    public boolean isForwardBranch()
    {
        return targetOperation > start_byte;
    }

    public String str()
    {
        if (nullVar != null)
        {
            if (opcode.getOpcode() == 198)
            {
                return nullVar + " == null";
            }
            else
            {
                return nullVar + " != null";
            }
        }
        else
        {
            return "signum(" + var1 + " - " + var2 + ")"; // TODO smth
        }
    }

    public boolean isPush()
    {
        return isPush;
    }

    public String getPushType()
    {
        return isPush ? "int" : null;
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        if (targetOperation != -1)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic());
            sb.append(" ").append(targetOperation);
            pw.println(sb.toString());
        }
        else
        {
            super.printRaw(pw, indent);
        }
    }

    public void analyze(Block block)
    {
        if (opcode.getOpcode() == 198 || opcode.getOpcode() == 199)
        {
            Operation prev = block.removePriorPushOperation();
            nullVar = prev.str();
        }
        else
        {
            Operation prev1 = block.removePriorPushOperation();
            var1 = prev1.str();
            Operation prev2 = block.removePriorPushOperation();
            var2 = prev2.str();
        }
    }
}
