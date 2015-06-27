package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_Class_info extends CP_INFO
{
    private int fqn_index;
    private Clazz clazz;
    private boolean loaded = false;

    private String name = null;
    private String packageName = null;
    private String baseType = null;
    String arrayQN = "";

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        fqn_index = cis.readU2();
        this.clazz = clazz;
    }

    public void complete() throws ClazzException
    {
        if (loaded) return;

        loaded = true;

        clazz.getConstant_pool()[fqn_index].complete();
        CONSTANT_Utf8_info utf8_info = (CONSTANT_Utf8_info) clazz.getConstant_pool()[fqn_index];
        if (utf8_info == null)
        {
            throw new ClazzException("CONSTANT_Class_Info, name_index = " + fqn_index);
        }
        String descriptor = utf8_info.getString();

        int arrayDimensions = 0;
        while (descriptor.charAt(arrayDimensions) == '[')
        {
            arrayDimensions++;
            arrayQN += "[]";
        }
        int currentPos = arrayDimensions;
        if (currentPos == 0)
        {
            String typeL = descriptor.substring(currentPos, descriptor.length());
            typeL = typeL.replace('/', '.');
            packageName = typeL.substring(0, typeL.lastIndexOf('.'));
            name = typeL.substring(typeL.lastIndexOf('.') + 1);
        }
        else
        {
            switch (descriptor.charAt(currentPos))
            {
                case 'B':
                    baseType = "byte";
                    break;
                case 'C':
                    baseType = "char";
                    break;
                case 'D':
                    baseType = "double";
                    break;
                case 'F':
                    baseType = "float";
                    break;
                case 'I':
                    baseType = "int";
                    break;
                case 'J':
                    baseType = "long";
                    break;
                case 'S':
                    baseType = "short";
                    break;
                case 'Z':
                    baseType = "boolean";
                    break;
                case 'L':
                    String type = descriptor.substring(currentPos + 1, descriptor.length() - 1);
                    type = type.replace('/', '.');
                    packageName = type.substring(0, type.lastIndexOf('.'));
                    name = type.substring(type.lastIndexOf('.') + 1);
                    break;
            }
        }
    }

    public String getType()
    {
        return "java.lang.Class";
    }

    public String getName()
    {
        return name;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public String getFullyQualifiedName()
    {
        if (baseType != null) return baseType + arrayQN;
        StringBuffer sb = new StringBuffer();
        if (packageName != null) sb.append(packageName).append(".");
        sb.append(name).append(arrayQN);
        return sb.toString();
    }

    public String toString()
    {
        return getFullyQualifiedName();
    }
}
