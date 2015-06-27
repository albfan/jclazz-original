package ru.andrew.jclazz.apps.tester.tests.clazz;

import java.awt.Component;

public class ClazzEnum2
{
    public void main()
    {
        Component.BaselineResizeBehavior os;
        os = Component.BaselineResizeBehavior.CONSTANT_ASCENT;
        switch (os)
        {
            case CONSTANT_ASCENT:
                System.out.println("You chose Windows!");
                break;
            case CONSTANT_DESCENT:
                System.out.println("You chose Unix!");
                break;
            case CENTER_OFFSET:
                System.out.println("You chose Linux!");
                break;
            case OTHER:
                System.out.println("You chose Macintosh!");
                break;
            default:
                System.out.println("I don't know your OS.");
                break;
        }
    }
}
