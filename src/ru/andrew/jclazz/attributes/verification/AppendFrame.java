package ru.andrew.jclazz.attributes.verification;

import ru.andrew.jclazz.*;

import java.io.*;

public class AppendFrame extends StackMapFrame
{
    private int offset_delta;
    private VerificationTypeInfo[] vtis;

    public AppendFrame(int frame_type)
    {
        super(frame_type);
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        offset_delta = cis.readU2();
        vtis = new VerificationTypeInfo[getAdditionalLocals()];
        for (int i = 0; i < vtis.length; i++)
        {
            vtis[i] = new VerificationTypeInfo();
            vtis[i].load(cis, clazz);
        }
    }

    public int getOffsetDelta()
    {
        return offset_delta;
    }

    public int getAdditionalLocals()
    {
        return frame_type - 251;
    }

    public VerificationTypeInfo[] getAdditionalVerificationTypeInfos()
    {
        return vtis;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(prefix(this));
        sb.append("additional locals - ");
        for (int i = 0; i < vtis.length; i++)
        {
            sb.append(vtis[i].toString());
            if (i < vtis.length - 1) sb.append(" ");
        }
        return sb.toString();
    }
}
