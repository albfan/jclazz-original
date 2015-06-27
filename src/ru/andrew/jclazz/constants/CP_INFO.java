package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public abstract class CP_INFO
{
    public static final int CONSTANT_Class = 7;
    public static final int CONSTANT_Fieldref = 9;
    public static final int CONSTANT_Methodref = 10;
    public static final int CONSTANT_InterfaceMethodref = 11;
    public static final int CONSTANT_String = 8;
    public static final int CONSTANT_Integer = 3;
    public static final int CONSTANT_Float = 4;
    public static final int CONSTANT_Long = 5;
    public static final int CONSTANT_Double = 6;
    public static final int CONSTANT_NameAndType = 12;
    public static final int CONSTANT_Utf8 = 1;

    public abstract void load(ClazzInputStream cis, Clazz clazz) throws IOException;

    public abstract void complete() throws ClazzException;

    public abstract String getType();

    public static CP_INFO loadConstant(ClazzInputStream cis, Clazz clazz) throws ClazzException, IOException
    {
        CP_INFO cp_info;
        int tag = cis.readU1();
        switch(tag)
        {
            case CONSTANT_Class:
                cp_info = new CONSTANT_Class_info();
                break;
            case CONSTANT_Fieldref:
                cp_info = new CONSTANT_Fieldref_info();
                break;
            case CONSTANT_Methodref:
                cp_info = new CONSTANT_Methodref_info();
                break;
            case CONSTANT_InterfaceMethodref:
                cp_info = new CONSTANT_InterfaceMethodref_info();
                break;
            case CONSTANT_String:
                cp_info = new CONSTANT_String_info();
                break;
            case CONSTANT_Integer:
                cp_info = new CONSTANT_Integer_info();
                break;
            case CONSTANT_Float:
                cp_info = new CONSTANT_Float_info();
                break;
            case CONSTANT_Long:
                cp_info = new CONSTANT_Long_info();
                break;
            case CONSTANT_Double:
                cp_info = new CONSTANT_Double_info();
                break;
            case CONSTANT_NameAndType:
                cp_info = new CONSTANT_NameAndType_info();
                break;
            case CONSTANT_Utf8:
                cp_info = new CONSTANT_Utf8_info();
                break;
            default:
                throw new ClazzException("Invalid constant type " + tag);
        }
        cp_info.load(cis, clazz);
        return cp_info;
    }
}
