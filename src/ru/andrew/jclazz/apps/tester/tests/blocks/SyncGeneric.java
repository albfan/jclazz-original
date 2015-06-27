package ru.andrew.jclazz.apps.tester.tests.blocks;

public class SyncGeneric
{
    public void testSync(Object obj)
    {
        System.out.println("BEFORE SYNC");
        synchronized(obj)
        {
            System.out.println("SYNC BLOCK");
        }
        System.out.println("AFTER SYNC");
    }
}
