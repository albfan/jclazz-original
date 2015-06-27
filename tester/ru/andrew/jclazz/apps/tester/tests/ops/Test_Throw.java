package ru.andrew.jclazz.apps.tester.tests.ops;

public class Test_Throw
{
    public void test1(int i, RuntimeException re)
    {
        if (i == 4)
        {
            throw re; 
        }
    }
}
