package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class EnclosingMethod extends ATTRIBUTE_INFO
{
    private CONSTANT_Class_info enclosingClass;
    private CONSTANT_NameAndType_info enclosingMethod;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // attribute legth

        enclosingClass = (CONSTANT_Class_info) clazz.getConstant_pool()[cis.readU2()];
        int enclosing_method_index = cis.readU2();
        if (enclosing_method_index > 0)
        {
            enclosingMethod = (CONSTANT_NameAndType_info) clazz.getConstant_pool()[enclosing_method_index];
        }
    }

    public CONSTANT_Class_info getEnclosingClass()
    {
        return enclosingClass;
    }

    public CONSTANT_NameAndType_info getEnclosingMethod()
    {
        return enclosingMethod;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("EnclosingMethod: \n");
        sb.append(INDENT).append("Enclosing class: ").append(enclosingClass.getFullyQualifiedName());
        if (enclosingMethod != null)
        {
            sb.append("\n");
            sb.append(INDENT).append("Enclosing method: ").append(enclosingMethod.toString());
        }
        return sb.toString();
    }
}
