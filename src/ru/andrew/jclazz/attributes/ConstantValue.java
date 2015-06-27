package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class ConstantValue extends ATTRIBUTE_INFO
{
    private CP_INFO cp_info;

    public void load(ClazzInputStream cis, Clazz clazz) throws ClazzException, IOException
    {
        long attribute_length = cis.readU4();
        if (attribute_length != 2) throw new ClazzException("Invalid length of ConstantValue attribute");
        int constantvalue_index = cis.readU2();
        cp_info = clazz.getConstant_pool()[constantvalue_index];
        if ( (!(cp_info instanceof CONSTANT_Long_info)) &&
                (!(cp_info instanceof CONSTANT_Float_info)) &&
                (!(cp_info instanceof CONSTANT_Double_info)) &&
                (!(cp_info instanceof CONSTANT_Integer_info)) &&
                (!(cp_info instanceof CONSTANT_String_info)) )
        {
            throw new ClazzException("ConstantValue is of illegal type");
        }
    }

    public CP_INFO getConstant()
    {
        return cp_info;
    }

    public String toString()
    {
        return ATTR + "ConstantValue: " + cp_info.toString();
    }
}
