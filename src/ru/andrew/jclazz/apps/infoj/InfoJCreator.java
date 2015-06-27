package ru.andrew.jclazz.apps.infoj;

import ru.andrew.jclazz.*;

import java.io.*;

public final class InfoJCreator
{
    public static String generateInfoFile(Clazz clazz) throws FileNotFoundException
    {
        String outFile = clazz.getFileName();
        outFile = outFile.substring(0, outFile.lastIndexOf('.')) + ".jinfo";

        RClassPrinter rcp = new RClassPrinter(new PrintWriter(new FileOutputStream(outFile)));
        rcp.print(clazz);
        rcp.close();

        return outFile;
    }
}
