package ru.andrew.jclazz.decompiler;

import java.util.*;

public class DecompileResult
{
    private String outFile;
    private Set errors;

    public String getOutFile()
    {
        return outFile;
    }

    public void setOutFile(String outFile)
    {
        this.outFile = outFile;
    }

    public Set getErrors()
    {
        return errors;
    }

    public void setErrors(Set errors)
    {
        this.errors = errors;
    }
}
