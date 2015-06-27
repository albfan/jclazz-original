package ru.andrew.jclazz.apps.tester.tests.clazz;

import java.math.*;
import java.io.*;

public abstract class MethodsCommon
{
    static
    {
        System.out.println("STATIC");
    }

    public MethodsCommon()
    {
        System.out.println("DEFAULT CONSTRUCTOR");
    }

    public MethodsCommon(String initVar)
    {
        System.out.println("ADDITIONAL CONSTRUCTOR");
    }

    private void test1()
    {
        System.out.println("PRIVATE METHOD");
    }

    protected void test2()
    {
        System.out.println("PROTECTED METHOD");
    }

    public static void test3()
    {
        System.out.println("STATIC METHOD");
    }

    public final void test4()
    {
        System.out.println("FINAL METHOD");
    }

    public synchronized void test5()
    {
        System.out.println("SYNC METHOD");
    }

    public native void test6();

    public abstract void test7();

    public strictfp void test8()
    {
        System.out.println("STRICTFP METHOD");
    }

    public BigInteger test9(String par1, PrintWriter pw, int k, boolean b) throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        Class.forName("fake").newInstance();
        return BigInteger.valueOf(0L);
    }

    public void test10(Class cl)
    {
    }

    public void test11()
    {
        test10(Integer.class);
    }

    public abstract void test12(String str);
}
