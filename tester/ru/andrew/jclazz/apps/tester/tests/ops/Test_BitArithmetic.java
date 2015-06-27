package ru.andrew.jclazz.apps.tester.tests.ops;

public class Test_BitArithmetic
{
    public void test1()
    {
        int i1 = 1;
        long l1 = 1L;

        int i2 = i1 << 4;
        long l2 = l1 << 4;

        int i3 = i1 >> 4;
        long l3 = l1 >> 4;

        int i4 = i1 >>> 4;
        long l4 = l1 >>> 4;

        int i5 = i1 & 4;
        long l5 = l1 & 4;

        int i6 = i1 | 4;
        long l6 = l1 | 4;

        int i7 = i1 ^ 4;
        long l7 = l1 ^ 4;
    }

    public void test2()
    {
        int s = 3;
        int i1 = 1;
        long l1 = 1L;

        int i2 = i1 << s;
        long l2 = l1 << s;

        int i3 = i1 >> s;
        long l3 = l1 >> s;

        int i4 = i1 >>> s;
        long l4 = l1 >>> s;

        int i5 = i1 & s;
        long l5 = l1 & s;

        int i6 = i1 | s;
        long l6 = l1 | s;

        int i7 = i1 ^ s;
        long l7 = l1 ^ s;
    }
}
