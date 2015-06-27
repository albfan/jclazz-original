package ru.andrew.jclazz.apps.tester.tests.blocks;

public class LoopIfContinue
{
    public void test1()
    {
        int i = 0;
        while (i < 10)
        {
            System.out.println("START LOOP");
            if (i == 5)
            {
                System.out.println("IF");
                continue;
            }
            i++;
            System.out.println("END LOOP");
        }
    }

    public void test2()
    {
        int i = 0;
        while (i < 10)
        {
            System.out.println("START LOOP");
            if (i == 5)
            {
                System.out.println("IF");
            }
            else if (i == 6)
            {
                System.out.println("IFELSE");
                continue;
            }
            else
            {
                System.out.println("ELSE");
            }
            i++;
            System.out.println("END LOOP");
        }
    }

    public void test3()
    {
        for (int i = 0; i < 10; i++)
        {
            System.out.println("START LOOP");
            if (i == 5)
            {
                System.out.println("IF");
                continue;
            }
            System.out.println("END LOOP");
        }
    }

    public void test4()
    {
        for (int i = 0; i < 10; i++)
        {
            System.out.println("START LOOP");
            if (i == 5)
            {
                System.out.println("IF");
            }
            else if (i == 6)
            {
                System.out.println("IFELSE");
                continue;
            }
            else
            {
                System.out.println("ELSE");
            }
            System.out.println("END LOOP");
        }
    }

    public void test5()
    {
        int i = 0;
        do
        {
            System.out.println("START LOOP");
            if (i == 5)
            {
                System.out.println("IF");
                continue;
            }
            i++;
            System.out.println("END LOOP");
        }
        while (i < 10);
    }

    public void test6()
    {
        int i = 0;
        do
        {
            System.out.println("START LOOP");
            if (i == 5)
            {
                System.out.println("IF");
            }
            else if (i == 6)
            {
                System.out.println("IFELSE");
                continue;
            }
            else
            {
                System.out.println("ELSE");
            }
            i++;
            System.out.println("END LOOP");
        }
        while (i < 10);
    }

    public void test7()
    {
        int i = 0;
        while (i < 10)
        {
            System.out.println("START LOOP");
            if (i == 5)
            {
                System.out.println("IF");
            }
            else if (i == 6)
            {
                System.out.println("IFELSE");
            }
            else
            {
                System.out.println("ELSE");
                continue;
            }
            i++;
            System.out.println("END LOOP");
        }
    }

    public void test8()
    {
        int i = 0;
        while (i < 10)
        {
            System.out.println("START LOOP");
            if (i == 5)
            {
                System.out.println("IF");
            }
            else if (i == 6)
            {
                System.out.println("IFELSE");
                continue;
            }
            else if (i == 7)
            {
                System.out.println("ELSE");
            }
            else
            {
                System.out.println("ELSE2");
            }
            i++;
            System.out.println("END LOOP");
        }
    }

    public void test9(int i2, int i3, int i4)
    {
        int i = 0;
        while (i < 10)
        {
            System.out.println("START LOOP");
            if ((i == 5 || i2 > 7) && (i3 <= 6 || i4 != 0))
            {
                System.out.println("IF");
                continue;
            }
            i++;
            System.out.println("END LOOP");
        }
    }
}
