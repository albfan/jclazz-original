package ru.andrew.jclazz.apps.tester.tests.ops;

public class Test_Array
{
    public void test1(Object[] objs)
    {
        int len = objs.length;
    }

    public void test2(Object[] objs)
    {
        Object obj = objs[7];
    }

    public void test3()
    {
        String[] strs = {"1", "2", "3"};
    }

    public void test4()
    {
        int[][] massive;
        massive = new int[10][];
    }
}
