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

    public void test5(long i)
    {
        if (i == 17L)
        {
            System.out.println("lcmp");
        }
    }

    public void test6(double i)
    {
        if (i < 2.0)
        {
            System.out.println("dcmpl");
        }
    }

    public void test7(String str)
    {
        if (str == "")
        {
            System.out.println("==");
        }
        System.out.println("IF");
        if (str != "")
        {
            System.out.println("!=");
        }
    }

    public void test8(int a)
    {
        if (a == 10);
        System.out.println("A");
    }

    public void test9(boolean b)
    {
        int i = 0;
        i = b ? 1 : -1;
    }

    public boolean test10(boolean b)
    {
        return b ? false : true;
    }

    private native int intVal1();

    private native int intVal2();

    public void test11(boolean b)
    {
        int i = 0;
        i = b ? intVal1() : intVal2();
    }

    private int k;

    public void test12(boolean b)
    {
        k = b ? intVal1() : intVal2();
    }

    boolean t;
    public void test13(int indx)
    {
        if (indx < 0)
        {
            return;
        }
        System.out.println("TESTTTT");
    }
}
