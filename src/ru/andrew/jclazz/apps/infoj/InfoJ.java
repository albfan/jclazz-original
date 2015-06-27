package ru.andrew.jclazz.apps.infoj;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.apps.*;

import java.io.*;
import java.util.*;

public final class InfoJ
{
    public static void main(String[] args) throws ClazzException, IOException
    {
        if ( (args.length == 0) ||
             (args.length == 1 && Params.HELP.equals(args[0])) )
        {
            if (args.length == 0) System.out.println("No input file specified!");
            System.out.println("USAGE:\n" +
                    "InfoJ [OPTIONS...] classfile\n" +
                    "Options:\n" +
                    "   --print_constant_pool: print constant pool information\n");
            return;
        }
        Map params = Utils.parseArguments(args);
        String classFile = args[args.length - 1];
        Clazz clazz = new Clazz(classFile);
        clazz.setDecompileParameters(params);

        InfoJCreator.generateInfoFile(clazz);
    }
}
