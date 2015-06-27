package ru.andrew.jclazz.decompiler.engine;

import java.util.*;

public class ClazzContext
{
    private Set errors = new HashSet();

    public void registerError(String message)
    {
        errors.add(message);
    }

    public Set getErrors()
    {
        return errors;
    }
}
