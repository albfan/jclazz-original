package ru.andrew.jclazz.apps.decomp;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.apps.*;

import java.util.*;
import java.io.*;

public final class ClassDecompiler
{
    public static String generateJavaFile(String in, Map params) throws ClazzException, IOException
    {
        Clazz clazz = new Clazz(in);
        clazz.setDecompileParameters(params);

        String outFile = clazz.getDecompileParameter(Params.OUT_FILE);
        if (outFile == null)
        {
            outFile = clazz.getFileName();
            outFile = outFile.substring(0, outFile.lastIndexOf('.') + 1);
            if (clazz.getDecompileParameter(Params.EXTENSION) != null)
            {
                outFile += clazz.getDecompileParameter(Params.EXTENSION);
            }
            else
            {
                outFile += "jav_";
            }
        }

        ClassPrinter cp = new ClassPrinter(new FileOutputStream(outFile));
        cp.print(clazz, true);
        cp.close();

        return outFile;
    }
}
