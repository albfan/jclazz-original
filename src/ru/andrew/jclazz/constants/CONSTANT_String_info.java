package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_String_info extends CP_INFO
{
    private int string_index;
    private boolean loaded = false;
    private Clazz clazz;

    private String string;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        string_index = cis.readU2();
        this.clazz = clazz;
    }

    public void complete() throws ClazzException
    {
        if (loaded) return;

        loaded = true;
        string = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[string_index]).getString(); 
    }

    public String getType()
    {
        return "java.lang.String";
    }

    public String toString()
    {
        return "\"" + string + "\"";
    }
}
