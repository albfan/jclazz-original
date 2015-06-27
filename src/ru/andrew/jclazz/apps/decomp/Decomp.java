package ru.andrew.jclazz.apps.decomp;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.apps.*;

import java.io.*;
import java.util.*;

public final class Decomp
{
    public static void main(String[] args) throws ClazzException, IOException
    {
        if ( (args.length == 0) ||
             (args.length == 1 && Params.HELP.equals(args[0])) )
        {
            if (args.length == 0) System.out.println("No input file specified!");
            System.out.println("USAGE:\n" +
                    "Decomp [OPTIONS...] classfile\n" +
                    "Options:\n" +
                    "   --print_header: prints comment header at the top od decompiled class\n" +
                    "   -out=FILE: specify output file\n" +
                    "   -ext=EXT: specify extension of output file\n");
            return;
        }
        Map params = Utils.parseArguments(args);
        String classFile = args[args.length - 1];
        
        ClassDecompiler.generateJavaFile(classFile, params);
    }
}
