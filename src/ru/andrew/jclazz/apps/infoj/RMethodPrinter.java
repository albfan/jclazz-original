package ru.andrew.jclazz.apps.infoj;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.attributes.*;

import java.io.*;

public class RMethodPrinter
{
    private static final String INDENT = "    ";
    
    public void print(PrintWriter pw, MethodInfo m_info)
    {
        pw.println(m_info.getName() + " is " + m_info.getDescriptor().getRawDescriptor());
        pw.println("{");
        pw.println(INDENT + "Access Flags: " + m_info.getAccessFlags());
        pw.println(INDENT + "PUBLIC      : " + ((m_info.getAccessFlags() & MethodInfo.ACC_PUBLIC) > 0 ? "x" : ""));
        pw.println(INDENT + "PRIVATE     : " + ((m_info.getAccessFlags() & MethodInfo.ACC_PRIVATE) > 0 ? "x" : ""));
        pw.println(INDENT + "PROTECTED   : " + ((m_info.getAccessFlags() & MethodInfo.ACC_PROTECTED) > 0 ? "x" : ""));
        pw.println(INDENT + "STATIC      : " + ((m_info.getAccessFlags() & MethodInfo.ACC_STATIC) > 0 ? "x" : ""));
        pw.println(INDENT + "FINAL       : " + ((m_info.getAccessFlags() & MethodInfo.ACC_FINAL) > 0 ? "x" : ""));
        pw.println(INDENT + "SYNCHRONIZED: " + ((m_info.getAccessFlags() & MethodInfo.ACC_SYNCHRONIZED) > 0 ? "x" : ""));
        pw.println(INDENT + "BRIDGE      : " + ((m_info.getAccessFlags() & MethodInfo.ACC_BRIDGE) > 0 ? "x" : ""));
        pw.println(INDENT + "VARARGS     : " + ((m_info.getAccessFlags() & MethodInfo.ACC_VARARGS) > 0 ? "x" : ""));
        pw.println(INDENT + "NATIVE      : " + ((m_info.getAccessFlags() & MethodInfo.ACC_NATIVE) > 0 ? "x" : ""));
        pw.println(INDENT + "ABSTRACT    : " + ((m_info.getAccessFlags() & MethodInfo.ACC_ABSTRACT) > 0 ? "x" : ""));
        pw.println(INDENT + "STRICT      : " + ((m_info.getAccessFlags() & MethodInfo.ACC_STRICT) > 0 ? "x" : ""));
        pw.println(INDENT + "SYNTHETIC   : " + ((m_info.getAccessFlags() & MethodInfo.ACC_SYNTHETIC) > 0 ? "x" : ""));
        pw.println();

        pw.println(INDENT + "Attributes:");
        AttributeInfo[] attrs = m_info.getAttributes();
        Code code = null;
        for (int i = 0; i < attrs.length; i++)
        {
            if (attrs[i] instanceof Code)
            {
                code = (Code) attrs[i];
            }
            else
            {
                String attrInfo = attrs[i].toString();
                attrInfo = attrInfo.replaceAll("\n", "\n" + INDENT + INDENT);
                pw.println(INDENT + attrInfo);
            }
        }

        if (code != null)
        {
            pw.println(INDENT + "CODE");
            pw.println(INDENT + "   (");
            pw.println(INDENT + "    Max stack = " + code.getMaxStack());
            pw.println(INDENT + "    Max locals = " + code.getMaxLocals());
            pw.println(INDENT + "    Attributes:");
            for (int j = 0; j < code.getAttributes().length; j++)
            {
                String attrInfo = code.getAttributes()[j].toString();
                attrInfo = attrInfo.replaceAll("\n", "\n" + INDENT + INDENT + INDENT);
                pw.println(INDENT + "    " + INDENT + attrInfo);
            }
            pw.println(INDENT + "    Exceptions:");
            for (int j = 0; j < code.getExceptionTable().length; j++)
            {
                Code.ExceptionTable cet = code.getExceptionTable()[j];
                pw.println(INDENT + "    " + INDENT + "[" + cet.start_pc + "-" + cet.end_pc + "): " + cet.handler_pc + (cet.catch_type != null ? " - " + cet.catch_type.getFullyQualifiedName() : ""));
            }
            pw.println(INDENT + "   )");
            code.getCodeBlock().printRaw(pw, INDENT);
        }

        pw.println("}");
    }
}
