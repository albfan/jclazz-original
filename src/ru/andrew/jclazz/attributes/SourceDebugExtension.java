package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;

import java.io.*;

public class SourceDebugExtension extends ATTRIBUTE_INFO
{
    private String debugInfo;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        long length = cis.readU4();
        byte[] bytes = new byte[(int) length];
        for (int i = 0; i < length; i++)
        {
            bytes[i] = (byte) cis.readU1();
        }
        debugInfo = new String(bytes, "UTF-8");
    }

    public String getDebugInfo()
    {
        return debugInfo;
    }

    public String toString()
    {
        return ATTR + "SourceDebugExtension: " + debugInfo;
    }
}
