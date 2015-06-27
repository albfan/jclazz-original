package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class LocalVariableTable extends AttributeInfo
{
    class LocalVariable
    {
        int start_pc;
        int length;
        CONSTANT_Utf8 name;
        CONSTANT_Utf8 descriptor;
        int index;           
    }

    private LocalVariable[] local_variable_table;

    public LocalVariableTable(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();
        
        int local_variable_table_length = cis.readU2();
        local_variable_table = new LocalVariable[local_variable_table_length];
        for (int i = 0; i < local_variable_table_length; i++)
        {
            local_variable_table[i] = new LocalVariable();
            local_variable_table[i].start_pc = cis.readU2();
            local_variable_table[i].length = cis.readU2();
            int name_index = cis.readU2(); 
            local_variable_table[i].name = (CONSTANT_Utf8) clazz.getConstant_pool()[name_index];
            int descriptor_index = cis.readU2();
            local_variable_table[i].descriptor = (CONSTANT_Utf8) clazz.getConstant_pool()[descriptor_index];
            local_variable_table[i].index = cis.readU2();
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        cos.writeU2(local_variable_table.length);
        for (int i = 0; i < local_variable_table.length; i++)
        {
            cos.writeU2(local_variable_table[i].start_pc);
            cos.writeU2(local_variable_table[i].length);
            cos.writeU2(local_variable_table[i].name.getIndex());
            cos.writeU2(local_variable_table[i].descriptor.getIndex());
            cos.writeU2(local_variable_table[i].index);
        }
    }

    public LocalVariable[] getLocalVariablesTable()
    {
        return local_variable_table;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("LocalVariableTable: \n");
        for (int i = 0; i < local_variable_table.length; i++)
        {
            LocalVariable lv = local_variable_table[i];
            sb.append(INDENT).append(lv.start_pc).append("+").append(lv.length).append(": ");
            sb.append(lv.name.getString()).append(" (").append(lv.descriptor.getString()).append("), ").append(lv.index);
            if (i < local_variable_table.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
