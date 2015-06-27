package ru.andrew.jclazz.attributes.verification;

import ru.andrew.jclazz.*;

import java.io.*;

public class SameLocals1StackItemFrameExtended extends StackMapFrame
{
    private int offset_delta;
    private VerificationTypeInfo vti;

    public SameLocals1StackItemFrameExtended(int frame_type)
    {
        super(frame_type);
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        offset_delta = cis.readU2();
        vti = new VerificationTypeInfo();
        vti.load(cis, clazz);
    }

    public int getOffsetDelta()
    {
        return offset_delta;
    }

    public VerificationTypeInfo getVerificationTypeInfo()
    {
        return vti;
    }

    public String toString()
    {
        return prefix(this) + "stack item = " + vti.toString();
    }
}
