package ru.andrew.jclazz.attributes.verification;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.util.*;
import java.io.*;

public class VerificationTypeInfo
{
    private static Map types;   // int -> String

    private int tag;

    // Object variable
    private CONSTANT_Class object_class;

    // Uninitialized variable
    private int offset;

    static
    {
        types = new HashMap(9);
        types.put(Integer.valueOf(0), "Top");
        types.put(Integer.valueOf(1), "Integer");
        types.put(Integer.valueOf(2), "Float");
        types.put(Integer.valueOf(3), "Double");
        types.put(Integer.valueOf(4), "Long");
        types.put(Integer.valueOf(5), "Null");
        types.put(Integer.valueOf(6), "UninitializedThis");
        types.put(Integer.valueOf(7), "Object");
        types.put(Integer.valueOf(8), "Uninitialized");
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        tag = cis.readU1();

        if (tag == 7)   // Object
        {
            int cp_index = cis.readU2();
            object_class = (CONSTANT_Class) clazz.getConstant_pool()[cp_index];
        }
        if (tag == 8)   // Uninitialized
        {
            offset = cis.readU2();
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU1(tag);
        if (tag == 7)   // Object
        {
            cos.writeU2(object_class.getIndex());
        }
        if (tag == 8)   // Uninitialized
        {
            cos.writeU2(offset);
        }
    }

    public int getTag()
    {
        return tag;
    }

    public String getType()
    {
        return (String) types.get(Integer.valueOf(tag));
    }

    public CONSTANT_Class getObjectClassForObjectVariable()
    {
        return object_class;
    }

    public int getOffsetForUninitializedVariable()
    {
        return offset;
    }

    public String toString()
    {
        return types.get(Integer.valueOf(tag)) +
                (tag == 7 ? "(" + object_class.getFullyQualifiedName() + ")" : "") +
                (tag == 8 ? "(at +" + offset + ")" : "");
    }
}
