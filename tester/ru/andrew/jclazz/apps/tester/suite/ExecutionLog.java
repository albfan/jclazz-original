package ru.andrew.jclazz.apps.tester.suite;

import java.util.*;

public final class ExecutionLog
{
    private List<String> log = new ArrayList<String>();

    public void debug(String message)
    {
        log.add("DEBUG: " + message);
    }

    public void error(String message)
    {
        log.add(message);
    }

    public List<String> getLog()
    {
        return log;
    }
}
