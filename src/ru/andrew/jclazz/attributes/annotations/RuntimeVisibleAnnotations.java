package ru.andrew.jclazz.attributes.annotations;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.attributes.*;

import java.io.*;

public class RuntimeVisibleAnnotations extends ATTRIBUTE_INFO
{
    private Annotation[] annotations;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // attribute length

        int num_annotations = cis.readU2();
        annotations = new Annotation[num_annotations];
        for (int i = 0; i < num_annotations; i++)
        {
            annotations[i] = Annotation.load(cis, clazz);
        }
    }

    public Annotation[] getAnnotations()
    {
        return annotations;
    }

    public String toString()
    {
        return toString("RuntimeVisibleAnnotations");
    }

    protected String toString(String attrName)
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append(attrName).append(": \n");
        for (int i = 0; i < annotations.length; i++)
        {
            sb.append(INDENT).append(annotations[i].toString());
            if (i < annotations.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
