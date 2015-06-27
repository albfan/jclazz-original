package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.attributes.*;

/**
 * Opcodes: 148 - 152<BR>
 * Parameters: no<BR>
 * Operand stack: value1, value2 => result<BR>
 */
public class Signum extends Operation
{
    private String var1;
    private String var2;

    public Signum(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return "int";
    }

    public String str()
    {
        return "signum(" + var1 + " - " + var2 + ")";
    }

    public String getVar1()
    {
        return var1;
    }

    public String getVar2()
    {
        return var2;
    }

    public void analyze(Block block)
    {
        Operation prev1 = block.removePriorPushOperation();
        var1 = prev1.str();
        Operation prev2 = block.removePriorPushOperation();
        var2 = prev2.str();
    }
}
