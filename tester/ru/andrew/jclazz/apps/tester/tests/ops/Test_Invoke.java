package ru.andrew.jclazz.apps.tester.tests.ops;

import java.util.*;

public class Test_Invoke
{
    public void testStatic()
    {
        Math.abs(-1);
    }

    public void testInterface(List list)
    {
        list.size();
    }

    public void testVirtual(Integer i)
    {
        i.intValue();
    }

    private void testSpecial1()
    {
        new Test_Invoke();
    }

    public void testSpecial2()
    {
        testSpecial1();
    }

    public void testSpecial3()
    {
        super.toString();
    }

    public void testVirtual2()
    {
        toString();
    }

    public String toString()
    {
        return "123";
    }

    public void testDynamic()
    {
        // TODO
    }

    private void testBool(boolean bool)
    {
        System.out.println("BOOL");
    }

    public void testBoolMain()
    {
        testBool(true);
        testBool(false);
    }
}
