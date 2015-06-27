package ru.andrew.jclazz.apps.tester.tests.ops;

public class Test_GetField
{
    private int f;
    public static String str;

    public void test1(Test_GetField obj)
    {
        f = obj.f;
    }

    public void test2()
    {
        System.out.println(Test_GetField.str);
    }
}
