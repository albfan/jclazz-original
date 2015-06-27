package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.attributes.*;

import java.io.*;

public class Nop extends Operation
{
    public Nop(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    public Nop(long start_byte)
    {
        super(start_byte);
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
        return null;
    }

    public void analyze(Block block)
    {
    }

    public void printRaw(PrintWriter pw, String indent)
    {
    }
}
