package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class SourceFile extends ATTRIBUTE_INFO
{
    private String sourceFile;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        long attribute_length = cis.readU4();
        if (attribute_length != 2) throw new ClazzException("SourceFile Attribute length is invalid");
        
        int sourcefile_index = cis.readU2();
        sourceFile = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[sourcefile_index]).getString();
    }

    public String getSourceFile()
    {
        return sourceFile;
    }

    public String toString()
    {
        return ATTR + "SourceFile: " + sourceFile;
    }
}
