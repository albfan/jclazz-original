package ru.andrew.jclazz.attributes.annotations;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class AnnotationDefault extends AttributeInfo
{
    private ElementValuePair defaultValue;

    public AnnotationDefault(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public void load(ClazzInputStream cis) throws IOException, ClazzException
    {
        attributeLength = (int) cis.readU4();

        defaultValue = new ElementValuePair(null, (char) cis.readU1());
        defaultValue.loadValue(cis, clazz);
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        defaultValue.storeValue(cos);
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
