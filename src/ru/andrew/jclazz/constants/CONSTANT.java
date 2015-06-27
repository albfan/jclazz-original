package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public abstract class CONSTANT
{
    protected int index;
    protected int tag;
    protected Clazz clazz;

    protected CONSTANT(int num, int tag, Clazz clazz)
    {
        this.index = num;
        this.tag = tag;
        this.clazz = clazz;
    }

    public int getIndex()
    {
        return index;
    }

    public Clazz getClazz()
    {
        return clazz;
    }

    public abstract String getType();

    public abstract String str();

    // For loading constants

    public abstract void load(ClazzInputStream cis) throws IOException;

    public abstract void update() throws ClazzException;

    // For storing constants

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU1(tag);
    }
}
