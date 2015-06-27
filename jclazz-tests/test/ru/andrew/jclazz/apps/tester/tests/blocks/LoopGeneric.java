package ru.andrew.jclazz.apps.tester.tests.blocks;

public class LoopGeneric
{
    public boolean exit;

    public void test1()
    {
        while (exit)
        {
            System.out.println("EXIT");
        }
    }

    // TODO
//    public void test2()
//    {
//        while (true)
//        {
//            System.out.println("LOOP");
//        }
//    }
    // TODO add tests with multiple conditions
}
