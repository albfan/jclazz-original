package ru.andrew.jclazz.apps.tester.tests.ops;

public class Test_Return
{
    public Test_Return(String s)
    {
        System.out.println("CONSTRUCTOR");
    }

    public void test1()
    {
        System.out.println("RET VOID");
    }

    public boolean test2()
    {
        return true;
    }

    public int test3()
    {
        return 12;
    }

    public long test4()
    {
        return 45L;
    }

    public float test5()
    {
        return 12.0f;
    }

    public double test6()
    {
        return 11.0;
    }

    public String test7()
    {
        return "123";
    }

    public Object test8(String s)
    {
        return s;
    }
}
