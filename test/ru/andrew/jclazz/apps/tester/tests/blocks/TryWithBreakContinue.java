package ru.andrew.jclazz.apps.tester.tests.blocks;

public class TryWithBreakContinue
{
    public void test1()
    {
        boolean exit = true;
        do
        {
            System.out.println("START DO");
            try
            {
                System.out.println("TRY");
            }
            catch (Exception e)
            {
                System.out.println("CATCH");
                break;
            }
            System.out.println("END LOOP");
        }
        while (exit);
    }

    public void test2()
    {
        boolean exit = true;
        do
        {
            System.out.println("START DO");
            try
            {
                System.out.println("TRY");
                break;
            }
            catch (Exception e)
            {
                System.out.println("CATCH");
            }
            System.out.println("END LOOP");
        }
        while (exit);
    }

    public void test3()
    {
        boolean exit = true;
        do
        {
            System.out.println("START DO");
            try
            {
                System.out.println("TRY");
            }
            catch (Exception e)
            {
                System.out.println("CATCH");
                continue;
            }
            System.out.println("END LOOP");
        }
        while (exit);
    }

    public void test4()
    {
        boolean exit = true;
        do
        {
            System.out.println("START DO");
            try
            {
                System.out.println("TRY");
                continue;
            }
            catch (Exception e)
            {
                System.out.println("CATCH");
            }
            System.out.println("END LOOP");
        }
        while (exit);
    }

    public void test4_2()
    {
        for (;;)
        {
            System.out.println("START DO");
            try
            {
                System.out.println("TRY");
                continue;
            }
            catch (Exception e)
            {
                System.out.println("CATCH");
            }
            System.out.println("END LOOP");
        }
    }
}
