package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

public class GenOperation extends Operation
{
    public GenOperation(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    public boolean isPush()
    {
        return false;
    }

    public String getPushType()
    {
        return null;
    }

    public String str()
    {
        return "[GEN]" + opcode.getMnemonic();
    }

    public void analyze(Block block)
    {
    }
}
