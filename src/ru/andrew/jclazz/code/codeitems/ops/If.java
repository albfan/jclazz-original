package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

import java.io.*;

/**
 * Opcodes: 153 - 166, 198, 199<BR>
 * Parameters: branch(2) for 153 - 166, 198, 199<BR>
 * Operand stack: <BR>
 * if[non]null(198, 199), if[cond](153-158): value => <BR>
 * if_[a,i]cmp[cond](159-166): value1, value2 => <BR>
 */
public class If extends Operation
{
    private long targetOperation;

    public If(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        long offset = (params[0] << 8) | params[1];
        if (offset > 32767) offset = offset - 65536;
        targetOperation = start_byte + offset;
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
        return "If";
    }

    public boolean isPush()
    {
        return false;
    }

    public String getPushType()
    {
        return null;
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic());
        sb.append(" ").append(targetOperation);
        pw.println(sb.toString());
    }

    public void analyze(Block block)
    {
    }
}
