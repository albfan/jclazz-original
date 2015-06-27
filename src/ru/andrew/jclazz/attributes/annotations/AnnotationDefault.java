package ru.andrew.jclazz.attributes.annotations;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.*;

import java.io.*;

public class AnnotationDefault extends ATTRIBUTE_INFO
{
    private ElementValuePair defaultValue;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // attribute length

        defaultValue = new ElementValuePair(null, (char) cis.readU1());
        defaultValue.loadValue(cis, clazz);
    }

    public ElementValuePair getDefaultValue()
    {
        return defaultValue;
    }

    public String toString()
    {
        return ATTR + "AnnotationDefault: " + defaultValue.toString();
    }
}
