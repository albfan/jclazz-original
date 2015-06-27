package ru.andrew.jclazz.attributes.verification;

import ru.andrew.jclazz.*;

import java.io.*;

public class SameFrameExtended extends StackMapFrame
{
    private int offset_delta;

    public SameFrameExtended(int frame_type)
    {
        super(frame_type);
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        offset_delta = cis.readU2();
    }

    public int getOffsetDelta()
    {
        return offset_delta;
    }

    public String toString()
    {
        return prefix(this);
    }
}
