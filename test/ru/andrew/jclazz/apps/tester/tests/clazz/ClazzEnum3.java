package ru.andrew.jclazz.apps.tester.tests.clazz;

public enum ClazzEnum3
{
    PUBLIC(1), STATIC(2), FINAL(3);

    ClazzEnum3(int id)
    {
        this.id = id;
    }

    private int id;

    public int getId()
    {
        return id;
    }

    public static String getClassName()
    {
        return ClazzEnum3.class.getName();
    }
}
