package ru.andrew.jclazz.apps.tester.tests.blocks;

public class LoopOr
{
    public void test1(int i1, int i2)
    {
        System.out.println("BEFORE LOOP");
        while (i1 == 2 || i2 > 7)
        {
            System.out.println("LOOP");
            i1++;
            i2++;
        }
        System.out.println("AFTER LOOP");
    }

    public void test2(int i1, int i2, int i3)
    {
        System.out.println("BEFORE LOOP");
        while (i1 == 2 || i2 > 7 || i3 <= 4)
        {
            System.out.println("LOOP");
            i1++;
            i2++;
        }
        System.out.println("AFTER LOOP");
    }

    public void test3(int i1, int i2, int i3, int i4)
    {
        System.out.println("BEFORE LOOP");
        while ((i1 == 2 || i2 > 7) && (i3 < 8 || i4 >= 7))
        {
            System.out.println("LOOP");
            i1++;
            i2++;
        }
        System.out.println("AFTER LOOP");
    }

    public void test4(int i1, int i2)
    {
        System.out.println("BEFORE LOOP");
        do
        {
            System.out.println("LOOP");
            i1++;
            i2++;
        }
        while (i1 == 2 || i2 > 7);
        System.out.println("AFTER LOOP");
    }

    public void test5(int i1, int i2, int i3)
    {
        System.out.println("BEFORE LOOP");
        do
        {
            System.out.println("LOOP");
            i1++;
            i2++;
        }
        while (i1 == 2 || i2 > 7 || i3 <= 4);
        System.out.println("AFTER LOOP");
    }

    public void test6(int i1, int i2, int i3, int i4)
    {
        System.out.println("BEFORE LOOP");
        do
        {
            System.out.println("LOOP");
            i1++;
            i2++;
        }
        while ((i1 == 2 || i2 > 7) && (i3 < 8 || i4 >= 7));
        System.out.println("AFTER LOOP");
    }

    public void test7(int i1, int i2)
    {
        System.out.println("BEFORE LOOP");
        while (i1 == 2 && i2 > 7)
        {
            System.out.println("LOOP");
            i1++;
            i2++;
        }
        System.out.println("AFTER LOOP");
    }

    public void test8(int i1, int i2)
    {
        System.out.println("BEFORE LOOP");
        do
        {
            System.out.println("LOOP");
            i1++;
            i2++;
        }
        while (i1 == 2 && i2 > 7);
        System.out.println("AFTER LOOP");
    }

    public void test9(int i1, int i2, int i3, int i4)
    {
        System.out.println("BEFORE LOOP");
        do
        {
            do
            {
                System.out.println("LOOP");
                i1++;
                i2++;
            }
            while (i3 < 8 || i4 >= 7);
            System.out.println("BETWEEN LOOPS");
        }
        while (i1 == 2 || i2 > 7);
        System.out.println("AFTER LOOP");
    }
}
