package ru.andrew.jclazz.apps.tester.tests.ops;

public class Test_TypeConversion
{
    public void test1()
    {
        int i = 4;
        long l = (long) i;
        float f = (float) i;
        double d = (double) i;
        byte b = (byte) i;
        char c = (char) i;
        short s = (short) i;
    }

    public void test2()
    {
        long l = 12L;
        int i = (int) l;
        float f = (float) l;
        double d = (double) l;
    }

    public void test3()
    {
        float f = 12.0f;
        int i = (int) f;
        long l = (long) f;
        double d = (double) f;
    }

    public void test4()
    {
        double d = 12.0;
        int i = (int) d;
        long l = (long) d;
        float f = (float) d;
    }
}
