package ru.andrew.jclazz.attributes.verification;

import ru.andrew.jclazz.*;

import java.io.*;

public class SameFrame extends StackMapFrame
{
    public SameFrame(int frame_type)
    {
        super(frame_type);
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
    }

    public int getOffsetDelta()
    {
        return frame_type;
    }

    public String toString()
    {
        return prefix(this);
    }
}
