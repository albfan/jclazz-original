package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;

import java.io.*;

public class LineNumberTable extends ATTRIBUTE_INFO
{
    class LineNumber
    {
        int start_pc;
        int line_number;
    }

    private LineNumber line_number_table[];

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // attribute length
        
        int line_number_table_length = cis.readU2();
        line_number_table = new LineNumber[line_number_table_length];
        for (int i = 0; i < line_number_table_length; i++)
        {
            line_number_table[i] = new LineNumber();
            line_number_table[i].start_pc = cis.readU2();
            line_number_table[i].line_number = cis.readU2();
        }
    }

    public LineNumber[] getLineNumbersTable()
    {
        return line_number_table;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("LineNumberTable:\n");
        for (int i = 0; i < line_number_table.length; i++)
        {
            LineNumber ln = line_number_table[i];
            sb.append(INDENT).append(ln.start_pc).append(": line ").append(ln.line_number);
            if (i < line_number_table.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
