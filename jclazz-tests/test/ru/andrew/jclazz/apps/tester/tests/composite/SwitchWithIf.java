package ru.andrew.jclazz.apps.tester.tests.composite;

public class SwitchWithIf
{
    public void test1(int i, int str)
    {
        switch(i)
        {
            case 31:
                if (str ==7)
                {
                    System.out.println("IF");
                }
                else
                {
                    System.out.println("ELSE");
                }
        }
    }
}
