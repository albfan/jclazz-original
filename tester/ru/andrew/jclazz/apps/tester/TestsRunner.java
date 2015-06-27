package ru.andrew.jclazz.apps.tester;

import ru.andrew.jclazz.apps.tester.suite.*;

public class TestsRunner
{
    public static void main(String[] args)
    {
        TestSuite testSuite = new TestSuite(args[0]);
        testSuite.suite();
    }
}
