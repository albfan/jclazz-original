package ru.andrew.jclazz.attributes.annotations;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.*;

import java.io.*;

public class RuntimeVisibleParameterAnnotations extends ATTRIBUTE_INFO
{
    private Annotation[][] parameter_annotations;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // attribute length

        int num_parameters = cis.readU1();
        parameter_annotations = new Annotation[num_parameters][];
        for (int i = 0; i < num_parameters; i++)
        {
            int num_annotations = cis.readU2();
            parameter_annotations[i] = new Annotation[num_annotations];
            for (int j = 0; j < num_annotations; j++)
            {
                parameter_annotations[i][j] = Annotation.load(cis, clazz);
            }
        }
    }

    public Annotation[][] getParameterAnnotations()
    {
        return parameter_annotations;
    }

    public String toString()
    {
        return toString("RuntimeVisibleParameterAnnotations");
    }

    protected String toString(String attrName)
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append(attrName).append(": \n");
        for (int i = 0; i < parameter_annotations.length; i++)
        {
            sb.append(INDENT).append("*\n");
            for (int j = 0; j < parameter_annotations[i].length; j++)
            {
                sb.append(INDENT).append(parameter_annotations[i][j].toString());
                if (j < parameter_annotations[i].length - 1) sb.append("\n");
            }
            if (i < parameter_annotations.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
