package ru.andrew.jclazz.apps.tester.tests.other;

public class ClassTest
{
    public int foo(int i, int j) 
    {
        while (true) 
        {
            try
            {   
                while (i < j)
                {
                    i = j++/i;
                }
            }
            catch (RuntimeException re)
            {   
                i = 10;
                continue;
            }
            break;
        }
        return j;
    }
}