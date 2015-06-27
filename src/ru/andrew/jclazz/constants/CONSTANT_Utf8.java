package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_Utf8 extends CONSTANT
{
    private String utf8m;

    protected CONSTANT_Utf8(int num, int tag, Clazz clazz)
    {
        super(num, tag, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException
    {
        utf8m = cis.readUTF();
    }

    public void update() throws ClazzException
    {
    }

    public String getType()
    {
        return "java.lang.String";
    }

    public String getString()
    {
        return utf8m;
    }

    public String str()
    {
        return utf8m;
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        super.store(cos);

        cos.writeUTF(utf8m);
    }
}
