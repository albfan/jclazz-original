package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_Long_info extends CP_INFO
{
    private long longValue;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        long high_bytes = cis.readU4();
        long low_bytes = cis.readU4();

        // Decode
        longValue = (high_bytes << 32) + low_bytes;
    }

    public void complete() throws ClazzException
    {
    }

    public String getType()
    {
        return "long";
    }

    public long getLong()
    {
        return longValue;
    }

    public String toString()
    {
        return String.valueOf(longValue) + "L";
    }

}
