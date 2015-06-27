package ru.andrew.jclazz.apps.tester.tests.blocks;

public class IfOr
{
    public void test1(int i1, int i2)
    {
        System.out.println("BEFORE IF");
        if (i1 == 2 || i2 > 7)
        {
            System.out.println("IF");
        }
        System.out.println("AFTER IF");
    }

    public void test2(int i1, int i2, int i3)
    {
        System.out.println("BEFORE IF");
        if (i1 == 2 || i2 > 7 || i3 <= 4)
        {
            System.out.println("IF");
        }
        System.out.println("AFTER IF");
    }

    public void test3(int i1, int i2)
    {
        System.out.println("BEFORE IF");
        if (i1 == 2 || i2 > 7)
        {
            System.out.println("IF");
        }
        else
        {
            System.out.println("ELSE");
        }
        System.out.println("AFTER IF");
    }

    public void test4(int i1, int i2, int i3, int i4)
    {
        System.out.println("BEFORE IF");
        if ((i1 == 2 || i2 > 7) && (i3 < 8 || i4 >= 7))
        {
            System.out.println("IF");
        }
        else
        {
            System.out.println("ELSE");
        }
        System.out.println("AFTER IF");
    }
}
