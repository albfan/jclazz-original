package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.*;

import java.io.*;

/**
 * Opcodes: 167, 200<BR>
 * Parameters: branch(2), branch(4) for wide<BR>
 * Operand stack: no change<BR>
 */
public class GoTo extends Operation
{
    private long targetOperation;

    private boolean isBreak = false;
    private boolean isContinue = false;

    private Loop loop;  //  inner loop support

    public GoTo(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        long offset;
        if (opcode == 167)   // goto
        {
            offset = (params[0] << 8) | params[1];
            if (offset > 32767) offset = offset - 65536;
        }
        else if (opcode == 200) // goto_w
        {
            offset = (params[0] << 24) | (params[1] << 16) | (params[2] << 8) | params[3];
            if (offset > 2147483647L) offset = offset - 4294967295L;
        }
        else
        {
            throw new OperationException("Not a goto operation");
        }
        targetOperation = start_byte + offset;
    }

    public GoTo(long start_byte, long targetOperation)
    {
        super(start_byte);
        this.targetOperation = targetOperation;
    }

    public long getTargetOperation()
    {
        return targetOperation;
    }

    public boolean isForwardBranch()
    {
        return targetOperation > start_byte;
    }

    public void setBreak(boolean isBreak)
    {
        this.isBreak = isBreak;
    }

    public void setContinue(boolean isContinue)
    {
        this.isContinue = isContinue;
    }

    public boolean isBreak()
    {
        return isBreak;
    }

    public boolean isContinue()
    {
        return isContinue;
    }

    public Loop getLoop()
    {
        return loop;
    }

    public void setLoop(Loop loop)
    {
        this.loop = loop;
    }

    public boolean isPush()
    {
        return false;
    }

    public String getPushType()
    {
        return null;
    }

    public void print(PrintWriter pw, String indent)
    {
        if (isBreak)
        {
            pw.println(indent + "break;");
        }
        else if (isContinue)
        {
            pw.println(indent + "continue;");
        }
        else if (loop != null)
        {
            // Print nothing
        }
        else
        {
            super.print(pw, indent);
        }
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

    public String str()
    {
        if (isBreak)
        {
            return "break";
        }
        else if (isContinue)
        {
            return "continue";
        }
        else if (loop != null)
        {
            // Print nothing
            return "";
        }
        else
        {
            return "goto " + targetOperation;
        }
    }
}
