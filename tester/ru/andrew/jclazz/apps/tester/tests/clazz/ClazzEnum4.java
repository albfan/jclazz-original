package ru.andrew.jclazz.apps.tester.tests.clazz;

import java.util.*;

public enum ClazzEnum4
{
    E1(new ArrayList<String>(1)), E2(new ArrayList<String>(2)), E3(new ArrayList<String>(3));

    static
    {
        System.out.println("STATIC");
    }

    private List<String> str;

    private ClazzEnum4(List<String> str)
    {
        this.str = str;
        System.out.println("STR");
    }
}
