package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_Integer_info extends CP_INFO
{
    private int intValue;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        long bytes = cis.readU4();

        // Decode
        intValue = (int) bytes;
    }

    public void complete() throws ClazzException
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

    public String toString()
    {
        return String.valueOf(intValue);
    }

}
