package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_Ref_info extends CP_INFO
{
    private int class_index;
    private int name_and_type_index;
    private boolean loaded = false;
    private Clazz clazz;

    protected CONSTANT_Class_info refClazz;
    protected CONSTANT_NameAndType_info refNameAndType;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        class_index = cis.readU2();
        name_and_type_index = cis.readU2();
        this.clazz = clazz;
    }

    public void complete() throws ClazzException
    {
        if (loaded) return;

        loaded = true;
        clazz.getConstant_pool()[class_index].complete();
        refClazz = (CONSTANT_Class_info) clazz.getConstant_pool()[class_index];
        clazz.getConstant_pool()[name_and_type_index].complete();
        refNameAndType = ((CONSTANT_NameAndType_info) clazz.getConstant_pool()[name_and_type_index]);
    }

    public String getType()
    {
        return null;
    }

    public CONSTANT_Class_info getRefClazz()
    {
        return refClazz; 
    }

    public String getName()
    {
        return refNameAndType.getName();
    }

    public String getDescriptor()
    {
        return refNameAndType.getDescriptor();
    }

    public String toString()
    {
        return refClazz.getFullyQualifiedName() + "." + refNameAndType.toString();
    }
}
