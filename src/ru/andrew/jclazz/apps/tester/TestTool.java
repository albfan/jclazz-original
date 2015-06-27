package ru.andrew.jclazz.apps.tester;

import java.io.*;

public final class TestTool
{
    public static void suite(File suiteDir, Tester tester)
    {
        File[] files = suiteDir.listFiles(tester.getInputFileFilter());
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isDirectory())
            {
                suite(files[i], tester);
            }
            else
            {
                try
                {
                    boolean res = tester.test(files[i]);
                    if (!res)
                    {
                        System.out.println(files[i].getName() + " - FAILED (" + tester.getName() + ")");
                    }
                }
                catch (Throwable th)
                {
                    System.out.println(files[i].getName() + " - FAILED (" + tester.getName() + ")");
                }
            }
        }
    }
}
