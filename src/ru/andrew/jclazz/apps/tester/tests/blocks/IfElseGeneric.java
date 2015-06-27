package ru.andrew.jclazz.apps.tester.tests.blocks;

import java.util.*;

public class IfElseGeneric
{
    public void test1(List list)
    {
        System.out.println("BEFORE IF");
        if (list instanceof ArrayList)
        {
            System.out.println("TRUE");
        }
        System.out.println("AFTER IF");
    }

    public void test2(List list)
    {
        System.out.println("BEFORE IF");
        if (list instanceof ArrayList)
        {
            System.out.println("TRUE");
        }
        else
        {
            System.out.println("FALSE");
        }
        System.out.println("AFTER IF");
    }

    public void test3(List list, int i)
    {
        System.out.println("BEFORE IF");
        if ((list instanceof ArrayList) && (i > 10))
        {
            System.out.println("IF");
        }
        else if (i <= 10)
        {
            System.out.println("ELSEIF");
        }
        else
        {
            System.out.println("ELSE");
        }
        System.out.println("AFTER IF");
    }

    public void test4(List list, int i)
    {
        System.out.println("BEFORE IF");
        if ((list instanceof ArrayList) && (i > 10))
        {
            System.out.println("IF");
        }
        else if (i <= 10)
        {
            System.out.println("ELSEIF");
        }
        System.out.println("AFTER IF");
    }
}
