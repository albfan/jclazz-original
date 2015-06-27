package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class Exceptions extends ATTRIBUTE_INFO
{
    private CONSTANT_Class_info[] exception_table;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4(); // attribute length

        int number_of_exceptions = cis.readU2();
        exception_table = new CONSTANT_Class_info[number_of_exceptions];
        for (int i = 0; i < number_of_exceptions; i++)
        {
            int index = cis.readU2();
            exception_table[i] = (CONSTANT_Class_info) clazz.getConstant_pool()[index];
        }
    }

    public CONSTANT_Class_info[] getExceptionTable()
    {
        return exception_table;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("Exceptions: \n");
        for (int i = 0; i < exception_table.length; i++)
        {
            sb.append(INDENT).append(exception_table[i].getFullyQualifiedName());
            if (i < exception_table.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
