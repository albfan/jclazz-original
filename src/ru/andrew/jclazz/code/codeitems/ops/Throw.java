package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

import java.io.*;

/**
 * Opcodes: 191<BR>
 * Parameters: no<BR>
 * Operand stack: no change<BR>
 */
public class Throw extends Operation
{
    private String thrownValue;

    public Throw(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    public String str()
    {
        return "throw " + thrownValue;
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
        pw.println(indent + str() + ";");
    }

    public void analyze(Block block)
    {
        Operation prev = block.removePriorPushOperation();
        thrownValue = prev.str();
    }
}
