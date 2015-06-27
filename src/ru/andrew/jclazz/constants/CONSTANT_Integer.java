package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_Integer extends CONSTANT
{
    private int intValue;

    protected CONSTANT_Integer(int num, int tag, Clazz clazz)
    {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException
    {
        intValue = cis.readInt();
    }

    public void update() throws ClazzException
    {
    }

    public String getType()
    {
        return "int";
    }

    public int getInt()
    {
        return intValue;
    }

    public String str()
    {
        return String.valueOf(intValue);
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        super.store(cos);

        cos.writeInt(intValue);
    }
}
