package ru.andrew.jclazz.apps.tester.tests.composite;

public class DoubleIfInLoop
{
    public boolean exit;

    public void run(String cmdS, String messageS)
        {
            while (exit)
            {
                System.out.println("LOOP START");
                if (cmdS.equalsIgnoreCase("connect"))
                {
                    if (!messageS.equalsIgnoreCase("accept"))
                    {
                        System.out.println("IF-1-1");
                        System.exit(1);
                    }
                    else
                    {
                        System.out.println("IF-1-2");
                    }
                }
                if (cmdS.equalsIgnoreCase("disconnect"))
                {
                    System.out.println("IF-2-1");
                    System.exit(1);
                }
                System.out.println("LOOP END");
            }
        }
}
