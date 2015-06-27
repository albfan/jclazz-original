package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class LocalVariableTypeTable extends ATTRIBUTE_INFO
{
    class LocalSignedVariable
    {
        int start_pc;
        int length;
        String name;
        String signature;
        int index;
    }

    private LocalSignedVariable[] local_variable_type_table;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // attribute length

        int local_variable_table_type_length = cis.readU2();
        local_variable_type_table = new LocalSignedVariable[local_variable_table_type_length];
        for (int i = 0; i < local_variable_table_type_length; i++)
        {
            local_variable_type_table[i] = new LocalSignedVariable();
            local_variable_type_table[i].start_pc = cis.readU2();
            local_variable_type_table[i].length = cis.readU2();
            int name_index = cis.readU2();
            local_variable_type_table[i].name = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[name_index]).getString();
            int signature_index = cis.readU2();
            local_variable_type_table[i].signature = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[signature_index]).getString();
            local_variable_type_table[i].index = cis.readU2();
        }
    }

    public LocalSignedVariable[] getLocalSignedVariablesTable()
    {
        return local_variable_type_table;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("LocalVariableTypeTable: \n");
        for (int i = 0; i < local_variable_type_table.length; i++)
        {
            LocalSignedVariable lsv = local_variable_type_table[i];
            sb.append(INDENT).append(lsv.start_pc).append("+").append(lsv.length).append(": ");
            sb.append(lsv.name).append(" (sign=").append(lsv.signature).append("), ").append(lsv.index);
            if (i < local_variable_type_table.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
