package ru.andrew.jclazz.apps.infoj;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.apps.*;
import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class RClassPrinter
{
    private PrintWriter pw;

    public RClassPrinter(PrintWriter pw)
    {
        this.pw = pw;
    }

    public void close()
    {
        pw.close();
    }

    public void print(Clazz clazz)
    {
        pw.println("This Class  : " + clazz.getThisClassInfo().getFullyQualifiedName());
        pw.println("Super Class : " + (clazz.getSuperClassInfo() != null ? clazz.getSuperClassInfo().getFullyQualifiedName() : ""));
        pw.println("Implemented Interfaces:");
        CONSTANT_Class[] intfs = clazz.getInterfaces();
        for (int i = 0; i < intfs.length; i++)
        {
            pw.println("   " + intfs[i].getFullyQualifiedName());
        }
        pw.println();
        
        pw.println("Version: " + clazz.getVersion());
        pw.println();
        pw.println("Access Flags: " + clazz.getAccessFlags());
        pw.println("PUBLIC      : " + ((clazz.getAccessFlags() & Clazz.ACC_PUBLIC) > 0 ? "x" : ""));
        pw.println("FINAL       : " + ((clazz.getAccessFlags() & Clazz.ACC_FINAL) > 0 ? "x" : ""));
        pw.println("SUPER       : " + ((clazz.getAccessFlags() & Clazz.ACC_SUPER) > 0 ? "x" : ""));
        pw.println("INTERFACE   : " + ((clazz.getAccessFlags() & Clazz.ACC_INTERFACE) > 0 ? "x" : ""));
        pw.println("ABSTRACT    : " + ((clazz.getAccessFlags() & Clazz.ACC_ABSTRACT) > 0 ? "x" : ""));
        pw.println("SYNTHETIC   : " + ((clazz.getAccessFlags() & Clazz.ACC_SYNTHETIC) > 0 ? "x" : ""));
        pw.println("ANNOTATION  : " + ((clazz.getAccessFlags() & Clazz.ACC_ANNOTATION) > 0 ? "x" : ""));
        pw.println("ENUM        : " + ((clazz.getAccessFlags() & Clazz.ACC_ENUM) > 0 ? "x" : ""));
        pw.println();

        pw.println("Attributes:");
        AttributeInfo[] attrs = clazz.getAttributes();
        for (int i = 0; i < attrs.length; i++)
        {
            pw.println(attrs[i].toString());
        }
        pw.println();

        pw.println("Fields:");
        RFieldPrinter rfp = new RFieldPrinter();
        FieldInfo[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++)
        {
            rfp.print(pw, fields[i]);
            pw.println();
        }
        pw.println();

        pw.println("Methods:");
        RMethodPrinter rmp = new RMethodPrinter();
        MethodInfo[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            rmp.print(pw, methods[i]);
            pw.println();
        }

        if ("yes".equals(clazz.getDecompileParameter(Params.PRINT_CONSTANT_POOL)))
        {
            pw.println("Constant Pool:");
            CONSTANT[] cps = clazz.getConstant_pool();
            for (int i = 0; i < cps.length; i++)
            {
                pw.print(i + ": ");
                if (cps[i] != null)
                {
                    String type = cps[i].getClass().getSimpleName();
                    type = type.substring(type.indexOf('_') + 1);
                    pw.print(type + ": ");
                    pw.print(cps[i].str());
                }
                pw.println();
            }
        }
    }
}
