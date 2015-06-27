package ru.andrew.jclazz.attributes.verification;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.attributes.*;

import java.io.*;

public class StackMapTable extends ATTRIBUTE_INFO
{
    private StackMapFrame[] stack_map_frame;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // attribute length
        
        int number_of_entries = cis.readU2();
        stack_map_frame = new StackMapFrame[number_of_entries];
        for (int i = 0; i < number_of_entries; i++)
        {
            stack_map_frame[i] = StackMapFrame.loadStackMapFrame(cis, clazz);
        }
    }

    public StackMapFrame[] getStackMapFrames()
    {
        return stack_map_frame;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("StackMapTable: \n");
        for (int i = 0; i < stack_map_frame.length; i++)
        {
            sb.append(INDENT).append(stack_map_frame[i].toString()).append("\n");
        }
        return sb.toString();
    }
}
