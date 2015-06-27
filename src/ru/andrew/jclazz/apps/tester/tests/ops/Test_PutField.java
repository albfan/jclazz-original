package ru.andrew.jclazz.apps.tester.tests.ops;

public class Test_PutField
{
    public static String sstr;
    public String pstr;
    private String str;

    public void test1()
    {
        str = "123";
    }

    public void test2(Test_PutField obj)
    {
        obj.pstr = "456";
    }

    public void test3()
    {
        Test_PutField.sstr = "789";
    }
}
