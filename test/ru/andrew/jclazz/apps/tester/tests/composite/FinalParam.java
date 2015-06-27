package ru.andrew.jclazz.apps.tester.tests.composite;

import java.util.Comparator;

public class FinalParam
{
    public void test1(String s0, final String str)
    {
        Comparator comp = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                System.out.println(str);
                return 0;
            }
        };
    }

    public void test2(String s0)
    {
        final String str = s0;
        Comparator comp = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                System.out.println(str);
                return 0;
            }
        };
        System.out.println(str);
    }
}
