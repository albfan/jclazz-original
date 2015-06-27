package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_Float extends CONSTANT
{
    private float floatValue;

    protected CONSTANT_Float(int num, int tag, Clazz clazz)
    {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException
    {
        floatValue = cis.readFloat();
    }

    public void update() throws ClazzException
    {
    }

    public String getType()
    {
        return "float";
    }

    public float getFloat()
    {
        return floatValue;
    }

    public String str()
    {
        return String.valueOf(floatValue) + "f";
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        super.store(cos);

        cos.writeFloat(floatValue);
    }
}
