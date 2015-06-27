package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;

import java.io.*;

public class Deprecated extends ATTRIBUTE_INFO
{
    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // Attribute length
    }

    public String toString()
    {
        return ATTR + "Deprecated";
    }
}
