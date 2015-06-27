package ru.andrew.jclazz.apps.tester.tests.composite;

public class LVs
{
    public void test1()
    {
        int i = 0;
        try
        {
            i = 7;
            i = i & 4;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void test2(int i)
    {
        i = 0;
        try
        {
            i = 7;
            i = i & 4;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
