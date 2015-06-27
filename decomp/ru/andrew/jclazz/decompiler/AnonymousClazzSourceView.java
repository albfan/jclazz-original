package ru.andrew.jclazz.decompiler;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.constants.*;

import java.io.*;

public class AnonymousClazzSourceView extends ClazzSourceView
{
    public AnonymousClazzSourceView(Clazz clazz, ClazzSourceView outerClazz)
    {
        super(clazz, outerClazz);
    }

    protected void printPackageAndImports()
    {
        // Print nothing for anonymous class
    }

    protected void printClassSignature(PrintWriter pw)
    {
        pw.println();
    }

    protected void printMethod(PrintWriter pw, MethodSourceView msv)
    {
        if (msv.getMethod().isInit()) return;

        super.printMethod(pw, msv);
    }

    public String getAnonymousSuperClassFQN()
    {
        CONSTANT_Class[] intfs = clazz.getInterfaces();
        if (intfs != null && intfs.length > 0)
        {
            return intfs[0].getFullyQualifiedName();
        }
        else
        {
            return clazz.getSuperClassInfo().getFullyQualifiedName();
        }
    }

}
