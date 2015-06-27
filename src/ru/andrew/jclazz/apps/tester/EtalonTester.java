package ru.andrew.jclazz.apps.tester;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.apps.decomp.*;

import java.io.*;
import java.util.*;

public class EtalonTester implements Tester, FileFilter
{
    public String getName()
    {
        return "etalon";
    }

    public boolean test(File file)
    {
        try
        {
            String outFile = ClassDecompiler.generateJavaFile(file.getAbsolutePath(), new HashMap());
            return FileComparator.compare(replaceExt(file, "etalon"), outFile);
        }
        catch (ClazzException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private String replaceExt(File file, String ext)
    {
        String fileStr = file.getAbsolutePath();
        fileStr = fileStr.substring(0, fileStr.lastIndexOf('.') + 1);
        return fileStr + ext;
    }

    public FileFilter getInputFileFilter()
    {
        return this;
    }

    public boolean accept(File pathname)
    {
        String name = pathname.getName();
        boolean contains$ = name.indexOf('$') != -1;
        if (name.endsWith(".class") && !contains$)
        {
            return true;
        }
        return pathname.isDirectory();
    }
}
