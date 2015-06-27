package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class Signature extends ATTRIBUTE_INFO
{
    private String signature;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // attribute length

        signature = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[cis.readU2()]).getString();
    }

    public String getSignature()
    {
        return signature;
    }

    public String toString()
    {
        return ATTR + "Signature: " + signature;
    }
}
