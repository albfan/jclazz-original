package ru.andrew.jclazz.apps.decomp;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.attributes.*;

import java.io.*;

public class FieldPrinter
{
    private static final String INDENT = "    ";
    private String indent = INDENT;

    public void setIndent(String indent)
    {
        this.indent = indent;
    }

    public void print(PrintWriter pw, FieldInfo f_info)
    {
        // Inner Class support
        if (f_info.getClazz().isInnerClass() &&
                f_info.isSynthetic() && f_info.isFinal() &&
                f_info.getName().equals("this$0") &&
                f_info.getDescriptor().getFQN().equals(f_info.getClazz().getOuterClazz().getThisClassInfo().getFullyQualifiedName()))
        {
            return;
        }
        if (f_info.isSynthetic()) return;

        if (f_info.isDeprecated() || f_info.isSynthetic())
        {
            pw.println(indent + "/**");
            if (f_info.isDeprecated()) pw.println(indent + "  * @deprecated");
            if (f_info.isSynthetic()) pw.println(indent + "  * @synthetic");
            pw.println(indent + " */");
        }
        pw.print(indent);
        if (f_info.isPublic()) pw.print("public ");
        if (f_info.isPrivate()) pw.print("private ");
        if (f_info.isProtected()) pw.print("protected ");
        if (f_info.isStatic()) pw.print("static ");
        if (f_info.isFinal()) pw.print("final ");
        if (f_info.isVolatile()) pw.print("volatile ");
        if (f_info.isTransient()) pw.print("transient ");

        if (f_info.isEnum()) pw.print("enum ");

        if (f_info.getSignature() == null)
        {
            String descriptor = f_info.getDescriptor().getFQN();

            // Inner Class support
            if (descriptor.indexOf('$') != -1)
            {
                InnerClass inCl = f_info.getClazz().getInnerClass(descriptor);
                if (inCl != null)
                {
                    descriptor = descriptor.substring(descriptor.indexOf('$') + 1);
                }
            }

            pw.print(f_info.getClazz().importClass(descriptor));
        }
        else
        {
            pw.print(f_info.getSignature().str(f_info.getClazz()));
        }

        pw.print(" ");

        pw.print(f_info.getName());
        if (f_info.getConstantValue() != null)
        {
            pw.print(" = ");
            pw.print(f_info.getConstantValue());
        }
        pw.println(";");
    }
}
