package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.*;

public class InnerClass
{
    CONSTANT_Class inner_class;
    CONSTANT_Class outer_class;
    CONSTANT_Utf8 inner_name;
    int inner_class_access_flags;

    private Clazz innerClazz;
    private boolean isPrinted = false;

    public CONSTANT_Class getInnerClass()
    {
        return inner_class;
    }

    public CONSTANT_Class getOuterClass()
    {
        return outer_class;
    }

    public String getInnerName()
    {

        return inner_name != null ? inner_name.getString() : null;
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

    public CONSTANT_Class getAnonymousSuperClass()
    {
        if (innerClazz == null) return null;    // Usually it is invoked when innerClazz is already set
        CONSTANT_Class[] intfs = innerClazz.getInterfaces();
        if (intfs == null || intfs.length == 0)
        {
            return innerClazz.getSuperClassInfo();
        }
        return intfs[0];
    }
}
