package ru.andrew.jclazz.apps.tester.tests.ops;

public class Test_PushPop
{
    public void test1()
    {
        String s1 = null;

        int i1 = -1;
        int i2 = 0;
        int i3 = 1;
        int i4 = 2;
        int i5 = 3;
        int i6 = 4;
        int i7 = 5;

        long l1 = 0L;
        long l2 = 1L;

        float f1 = 0.0f;
        float f2 = 1.0f;
        float f3 = 2.0f;

        double d1 = 0.0;
        double d2 = 1.0;

        int i8 = 17;
        int i9 = 440;

        String s2 = "123";
        Class c1 = Integer.class;
    }

    public void test2()
    {
        long l1 = 1L;
        long l2 = 2L;
        long l3 = 3L;
        long l4 = 4L;
        long l5 = 5L;

        long l6 = l2;
        long l7 = l5;
    }

    public void test3()
    {
        double[] arr = new double[3];
        arr[0] = 0.0;
        arr[1] = 1.0;
        arr[2] = 2.0;

        double d = arr[1];
    }

    private native int getInt();

    private native long getLong();

    public int test87()    // pop
    {
        int i = 2;
        getInt();
        return i;
    }

    public long test88()    //pop2
    {
        long i = 5L;
        getLong();
        return i;
    }

    public long test88_2()    //pop2
    {
        long i = 5L;
        getInt();
        getInt();
        return i;
    }
}
