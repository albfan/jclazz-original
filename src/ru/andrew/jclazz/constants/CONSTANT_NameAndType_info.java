package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_NameAndType_info extends CP_INFO
{
    private int name_index;
    private int descriptor_index;
    private boolean loaded = false;
    private Clazz clazz;

    private String name;
    private String fd;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        name_index = cis.readU2();
        descriptor_index = cis.readU2();
        this.clazz = clazz;
    }

    public void complete() throws ClazzException
    {
        if (loaded) return;

        loaded = true;
        clazz.getConstant_pool()[name_index].complete();
        name = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[name_index]).getString();
        clazz.getConstant_pool()[descriptor_index].complete();
        fd = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[descriptor_index]).getString();
    }

    public String getType()
    {
        return null;
    }

    public String getName()
    {
        return name;
    }

    public String getDescriptor()
    {
        return fd;
    }

    public String toString()
    {
        return name + "(" + fd + ")";
    }
}
