package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.*;

public class InnerClass
{
    CONSTANT_Class_info inner_class;
    CONSTANT_Class_info outer_class;
    String inner_name;
    int inner_class_access_flags;

    private Clazz innerClazz;
    private boolean isPrinted = false;

    public CONSTANT_Class_info getInnerClass()
    {
        return inner_class;
    }

    public CONSTANT_Class_info getOuterClass()
    {
        return outer_class;
    }

    public String getInnerName()
    {
        return inner_name;
    }

    public int getInnerClassAccessFlags()
    {
        return inner_class_access_flags;
    }

    public Clazz getInnerClazz()
    {
        return innerClazz;
    }

    public void setInnerClazz(Clazz innerClazz)
    {
        this.innerClazz = innerClazz;
    }

    public boolean isPrinted()
    {
        return isPrinted;
    }

    public void setPrinted(boolean printed)
    {
        isPrinted = printed;
    }

    public CONSTANT_Class_info getAnonymousSuperClass()
    {
        if (innerClazz == null) return null;    // Usually it is invoked when innerClazz is already set
        CONSTANT_Class_info[] intfs = innerClazz.getInterfaces();
        if (intfs == null || intfs.length == 0)
        {
            return innerClazz.getSuperClassInfo();
        }
        return intfs[0];
    }
}
