package ru.andrew.jclazz.apps.tester.tests.clazz;

import java.util.*;
import java.math.*;

public class AnonymousClazz01
{
    private String str;
    private BigInteger bi;
    private int i;

    public void test(List list)
    {
        Collections.sort(list, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                if (i == 12 && bi.equals(BigInteger.TEN) && str.equals("123"))
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
        });
    }
}
