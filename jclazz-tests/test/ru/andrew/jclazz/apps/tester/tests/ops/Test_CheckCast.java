package ru.andrew.jclazz.apps.tester.tests.ops;

import java.util.*;

public class Test_CheckCast
{
    public void test1(List list)
    {
        String str = (String) list.get(0);
    }

    public void test2(Object[] objs)
    {
        Integer[] list = (Integer[]) objs;
    }

    public void test3(List list)
    {
        ((Integer) list.get(0)).intValue();
    }

    public void test4(List list)
    {
        int len = ((Object[]) list.get(0)).length;
    }

    public int k;

    public void test5(List list)
    {
        int i = ((Test_CheckCast) list.get(0)).k;
    }
}
