package ru.andrew.jclazz.apps.tester.tests.ops;

public class Test_Switch
{
    public void testSwitch1(int i) // as lookup
    {
        System.out.println("BEFORE SWITCH");
        switch (i)
        {
            case 0:
                System.out.println("0");
                break;
            case 10:
                System.out.println("10");
                break;
            case 50:
                System.out.println("50");
                return;
            default:
                System.out.println("DEFAULT");
        }
        System.out.println("AFTER SWITCH");
    }

    public void testSwitch2(int i)  // as tableswitch
    {
        System.out.println("BEFORE SWITCH");
        switch (i)
        {
            case 0:
                System.out.println("0");
            case 1:
                System.out.println("1");
                break;
            case 2:
                System.out.println("2");
                return;
            default:
                System.out.println("DEFAULT");
        }
        System.out.println("AFTER SWITCH");
    }

    public void testSwitch3(int i)  // absence of default
    {
        System.out.println("BEFORE SWITCH");
        switch (i)
        {
            case 0:
                System.out.println("0");
                break;
        }
        System.out.println("AFTER SWITCH");
    }

    public void testSwitch4(int i)
    {
        System.out.println("BEFORE SWITCH");
        switch (i)
        {
            case 0:
            case 1:
                System.out.println("1");
                break;
            case 2:
                System.out.println("2");
                return;
        }
        System.out.println("AFTER SWITCH");
    }
}
