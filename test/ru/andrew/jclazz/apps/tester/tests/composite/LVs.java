package ru.andrew.jclazz.apps.tester.tests.composite;

import java.util.*;
import java.io.*;

public class LVs
{
    public void test1()
    {
        int i = 0;
        try
        {
            i = 7;
            i = i & 4;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void test2(int i)
    {
        i = 0;
        try
        {
            i = 7;
            i = i & 4;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public String test3()
    {
        String str = null;
        try
        {
            str = "123";
        }
        catch (Exception e)
        {
            System.out.println("EXCEPTION");
        }
        return str;
    }

    public void test4()
    {
        HashSet list = new HashSet();
        char i = 0;
        while (i < 150)
        {
            System.out.println("CYCLE");
            list = new LinkedHashSet();
            System.out.println("i = " + (int) i);
            i++;
        }
    }

    public void test5() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("path")));
        String s;
        while ((s = br.readLine()).equals("456"))
        {
            System.out.println(" = " + s);
        }
    }

    public void test6(boolean test) throws IOException
    {
        File file;
        while (test && (file = new File("path")).isDirectory())
        {
            System.out.println(" = " + file);
        }
    }

    public void test7(boolean test) throws IOException
    {
        int i;
        while (test && (i = new Integer("123").intValue()) > 10)
        {
            System.out.println(" = " + i);
        }
    }

    public void test8(String sender, char keyCode, int modifiers)
    {
        if('j' == keyCode)
        {
            System.out.println("IF char");
        }
    }
}
