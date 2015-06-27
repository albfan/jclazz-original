package ru.andrew.jclazz.apps.decomp;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;
import java.util.*;

public class MethodPrinter
{
    private static final String INDENT = "    ";
    public static final String INIT_METHOD = "<init>";
    public static final String CLASS_INIT_METHOD = "<clinit>";

    private String indent = INDENT;

    public void setIndent(String indent)
    {
        this.indent = indent;
    }

    public void print(PrintWriter pw, METHOD_INFO m_info, Clazz clazz)
    {
        if (m_info.isGetFieldForIC()) return;

        if (m_info.isDeprecated() || m_info.isSynthetic())
        {
            pw.println(indent + "/**");
            if (m_info.isDeprecated()) pw.println(indent + "  * @deprecated");
            if (m_info.isSynthetic()) pw.println(indent + "  * @synthetic");
            pw.println(indent + " */");
        }

        pw.print(indent);
        if (m_info.isPublic()) pw.print("public ");
        if (m_info.isPrivate()) pw.print("private ");
        if (m_info.isProtected()) pw.print("protected ");
        if (m_info.isStatic()) pw.print("static ");
        if (m_info.isFinal()) pw.print("final ");
        if (m_info.isStrictFP()) pw.print("strictfp ");
        if (m_info.isSynchronized()) pw.print("synchronized ");
        if (m_info.isNative()) pw.print("native ");
        if (m_info.isAbstract()) pw.print("abstract ");

        if (m_info.getSignature() != null)
        {
            pw.print(m_info.getSignature().strPreName(m_info.getClazz()));
        }
        else if (!INIT_METHOD.equals(m_info.getName()) && !CLASS_INIT_METHOD.equals(m_info.getName()))
        {
            String retClass = clazz.importClass(m_info.getDescriptor().getReturnType().getFQN());
            pw.print(retClass);
            pw.print(" ");
        }

        if (INIT_METHOD.equals(m_info.getName()))
        {
            String initName = clazz.getThisClassInfo().getName();
            if (m_info.getClazz().isInnerClass())
            {
                initName = initName.substring(initName.indexOf('$') + 1);
            }
            pw.print(initName);
        }
        else if (CLASS_INIT_METHOD.equals(m_info.getName()))
        {
        }
        else
        {
            pw.print(m_info.getName());
        }

        if (m_info.getSignature() == null)
        {
            int addition = m_info.isStatic() ? 0 : 1;
            if (!CLASS_INIT_METHOD.equals(m_info.getName()))
            {
                pw.print("(");
                List params = new ArrayList(m_info.getDescriptor().getParams());

                // Inner Class support
                if (m_info.getClazz().isInnerClass() &&
                        INIT_METHOD.equals(m_info.getName()) &&
                        m_info.getClazz().getOuterClazz().getThisClassInfo().getFullyQualifiedName().equals(((FieldDescriptor) params.get(0)).getFQN()))
                {
                    addition++;
                    params.remove(0);
                }

                for (int i = 0; i < params.size() - 1; i++)
                {
                    FieldDescriptor fd = (FieldDescriptor) params.get(i);
                    LocalVariable lv = m_info.getTopBlock().getLocalVariable(i + addition, null);
                    pw.print(clazz.importClass(fd.getFQN()) + " " + lv.getName());
                    lv.setPrinted(true);
                    pw.print(", ");
                }
                if (params.size() > 0)
                {
                    FieldDescriptor fd = (FieldDescriptor) params.get(params.size() - 1);
                    LocalVariable lv = m_info.getTopBlock().getLocalVariable(params.size() - 1 + addition, null);
                    String lpFQN = clazz.importClass(fd.getFQN());
                    if (!m_info.isVarargs())
                    {
                        pw.print(lpFQN + " " + lv.getName());
                    }
                    else
                    {
                        lpFQN = lpFQN.substring(0, lpFQN.length() - 2);
                        pw.print(lpFQN + "... " + lv.getName());
                    }
                    lv.setPrinted(true);
                }
                pw.print(")");
            }
        }
        else
        {
            pw.print(m_info.getSignature().strPostName(m_info.getClazz()));
        }

        if (m_info.getSignature() == null || !m_info.getSignature().hasThrowClause())
        {
            if (m_info.getExceptions() != null)
            {
                pw.print(" throws ");
                CONSTANT_Class_info[] exceptions = m_info.getExceptions().getExceptionTable();
                for (int e = 0; e < exceptions.length; e++)
                {
                    pw.print(clazz.importClass(exceptions[e].getFullyQualifiedName()));
                    if (e != exceptions.length - 1) pw.print(", ");
                }
            }
        }
        else
        {
            pw.print(m_info.getSignature().strThrows(m_info.getClazz()));
        }

        if (m_info.getCodeBlock() != null)
        {
            try
            {
                pw.println();
                Code code = m_info.getCodeBlock();
                ByteCodeConverter.convert(code).print(pw, indent);
            }
            catch (OperationException oe)
            {
                pw.println("!!! EXCEPTION OCCURED !!!");
                oe.printStackTrace(pw);
            }
        }
        else
        {
            pw.println(";");
        }
        pw.println();
    }
}
