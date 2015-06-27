package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_Float_info extends CP_INFO
{
    private float floatValue;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        long bytes = cis.readU4();  

        // Decode
        if (bytes == 0x7f800000L)
        {
            floatValue = Float.POSITIVE_INFINITY;
        }
        else if (bytes == 0xff800000L)
        {
            floatValue = Float.NEGATIVE_INFINITY;
        }
        else if ((bytes >= 0x7f800001L && bytes <= 0x7fffffffL) ||
                (bytes >= 0xff800001L && bytes <= 0xffffffffL))
        {
            floatValue = Float.NaN;
        }
        else
        {
            int s = ((bytes >> 31) == 0) ? 1 : -1;
    	    int e = (int) ((bytes >> 23) & 0xff);
    	    int m = (int) ((e == 0) ?  (bytes & 0x7fffffL) << 1 : (bytes & 0x7fffffL) | 0x800000L);
            floatValue = (float) ((double) (s * m) * Math.pow(2.0, e - 150));
        }
    }

    public void complete() throws ClazzException
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

    public String toString()
    {
        return String.valueOf(floatValue) + "f";
    }

}
