package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;

import java.io.*;

public class Synthetic extends ATTRIBUTE_INFO
{
    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // attribute length
    }

    public String toString()
    {
        return ATTR + "Synthetic";
    }
}
