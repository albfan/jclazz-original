package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_Double_info extends CP_INFO
{
    private double doubleValue;

    private static final U8 D_POS_INF = new U8("0x7ff0000000000000"); 
    private static final U8 D_NEG_INF = new U8("0xfff0000000000000"); 
    private static final U8 D_POS_INF_1 = new U8("0x7ff0000000000001");
    private static final U8 D_MAX = new U8("0x7fffffffffffffff");
    private static final U8 D_NEG_INF_1 = new U8("0xfff0000000000001");
    private static final U8 D_MIN = new U8("0xffffffffffffffff"); 

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        long high_bytes = cis.readU4(); // U4
        long low_bytes = cis.readU4();  // U4
        U8 value = new U8(high_bytes, low_bytes);

        // Decode
        if (value.compareTo(D_POS_INF) == 0)
        {
            doubleValue = Double.POSITIVE_INFINITY;
        }
        else if (value.compareTo(D_NEG_INF) == 0)
        {
            doubleValue = Double.NEGATIVE_INFINITY;
        }
        else if ((value.compareTo(D_POS_INF_1) >= 0 && value.compareTo(D_MAX) <= 0) ||
                (value.compareTo(D_NEG_INF_1) >= 0 && value.compareTo(D_MIN) <= 0))
        {
            doubleValue = Double.NaN;
        }
        else
        {
            int s = ((high_bytes >> 31) == 0) ? 1 : -1;
    	    int e = (int) ((high_bytes >> 20) & 0x7ffL);
    	    long m = (e == 0) ? (value.lowest52bit()) << 1 : (value.lowest52bit()) | 0x10000000000000L;
            doubleValue = (double) (s * m) * Math.pow(2.0, e - 1075);
        }
    }

    public void complete() throws ClazzException
    {
    }

    public String getType()
    {
        return "double";
    }

    public double getDouble()
    {
        return doubleValue;
    }

    public String toString()
    {
        return String.valueOf(doubleValue);
    }
}
