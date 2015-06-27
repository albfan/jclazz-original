package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class LocalVariableTable extends ATTRIBUTE_INFO
{
    class LocalVariable
    {
        int start_pc;
        int length;
        String name;
        String descriptor;
        int index;           
    }

    private LocalVariable[] local_variable_table;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // attribute length
        
        int local_variable_table_length = cis.readU2();
        local_variable_table = new LocalVariable[local_variable_table_length];
        for (int i = 0; i < local_variable_table_length; i++)
        {
            local_variable_table[i] = new LocalVariable();
            local_variable_table[i].start_pc = cis.readU2();
            local_variable_table[i].length = cis.readU2();
            int name_index = cis.readU2(); 
            local_variable_table[i].name = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[name_index]).getString();
            int descriptor_index = cis.readU2();
            local_variable_table[i].descriptor = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[descriptor_index]).getString();
            local_variable_table[i].index = cis.readU2();
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
            sb.append(lv.name).append(" (").append(lv.descriptor).append("), ").append(lv.index);
            if (i < local_variable_table.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
