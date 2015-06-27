package ru.andrew.jclazz.attributes.annotations;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class ElementValuePair
{
    private String element_name;
    private char tag;

    private boolean isConstValue;
    private CP_INFO const_value;

    private boolean isEnumConstValue;
    private String enum_const_type_name;
    private String enum_const_name;

    private boolean isClassInfo;
    private FieldDescriptor returnClassInfo;

    private boolean isAnnotationValue;
    private Annotation annotationValue;

    private boolean isArrayValue;
    private ElementValuePair[] arrayValue;

    public ElementValuePair(String element_name, char tag) throws ClazzException
    {
        this.element_name = element_name;
        this.tag = tag;

        switch (tag)
        {
            case 'B':   // byte
                isConstValue = true;
                break;
            case 'C':   // char
                isConstValue = true;
                break;
            case 'D':   // double
                isConstValue = true;
                break;
            case 'F':   // float
                isConstValue = true;
                break;
            case 'I':   // int
                isConstValue = true;
                break;
            case 'J':   // long
                isConstValue = true;
                break;
            case 'S':   // short
                isConstValue = true;
                break;
            case 'Z':   // boolean
                isConstValue = true;
                break;
            case 's':   // String
                isConstValue = true;
                break;
            case 'e':   // enum constant
                isEnumConstValue = true;
                break;
            case 'c':   // Class
                isClassInfo = true;
                break;
            case '@':   // annotation type
                isAnnotationValue = true;
                break;
            case '[':   // array
                isArrayValue = true;
                break;
            default:
                throw new ClazzException("Illegal ElementValuePair tag");
        }
    }

    public void loadValue(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        if (isConstValue)
        {
            int const_value_index = cis.readU2();
            const_value = clazz.getConstant_pool()[const_value_index];
        }
        else if (isEnumConstValue)
        {
            int ec_type_name_index = cis.readU2();
            enum_const_type_name = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[ec_type_name_index]).getString();

            int ec_name_index = cis.readU2();
            enum_const_name = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[ec_name_index]).getString();
        }
        else if (isClassInfo)
        {
            int class_info_index = cis.readU2();
            returnClassInfo = new FieldDescriptor(((CONSTANT_Utf8_info) clazz.getConstant_pool()[class_info_index]).getString());
        }
        else if (isAnnotationValue)
        {
            annotationValue = Annotation.load(cis, clazz);
        }
        else if (isArrayValue)
        {
            int num_values = cis.readU2();
            arrayValue = new ElementValuePair[num_values];
            for (int k = 0; k < num_values; k++)
            {
                arrayValue[k] = new ElementValuePair(null, (char) cis.readU1());
                arrayValue[k].loadValue(cis, clazz);
            }
        }
    }

    // Getters

    public String getElementName()
    {
        return element_name;
    }

    public CP_INFO getConstValue()
    {
        return const_value;
    }

    public String getEnumConstTypeName()
    {
        return enum_const_type_name;
    }

    public String getEnumConstName()
    {
        return enum_const_name;
    }

    public FieldDescriptor getReturnClassInfo()
    {
        return returnClassInfo;
    }

    public Annotation getAnnotationValue()
    {
        return annotationValue;
    }

    public ElementValuePair[] getArrayValue()
    {
        return arrayValue;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(").append(element_name).append(" = ");
        if (isConstValue) sb.append(const_value.toString());
        if (isEnumConstValue) sb.append(enum_const_name).append(" of ").append(enum_const_type_name);
        if (isClassInfo) sb.append(returnClassInfo.getFQN());
        if (isAnnotationValue) sb.append(annotationValue.toString());
        if (isArrayValue)
        {
            sb.append("[");
            for (int i = 0; i < arrayValue.length; i++)
            {
                sb.append(arrayValue[i].toString());
                if (i < arrayValue.length - 1) sb.append(", ");
            }
            sb.append("]");
        }
        sb.append(")");
        return sb.toString();
    }
}
