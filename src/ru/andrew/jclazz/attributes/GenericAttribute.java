package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;

import java.io.*;

public class GenericAttribute extends ATTRIBUTE_INFO
{
    private String attributeName;
    private int[] bytes;

    public GenericAttribute(String attributeName)
    {
        this.attributeName = attributeName;
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        long attribute_length = cis.readU4();
        bytes = new int[(int) attribute_length];
        for (long i = 0; i < attribute_length; i++)
        {
            bytes[((int) i)] = cis.readU1();
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR).append("(GEN)");
        sb.append(attributeName).append(": ");
        if (bytes.length > 0)
        {
            for (int i = 0; i < bytes.length; i++)
            {
                sb.append(bytes[i]).append(" ");
            }
        }
        else
        {
            sb.append("empty");
        }
        return sb.toString();
    }
}
