package ru.andrew.jclazz.apps.tester;

import ru.andrew.jclazz.apps.*;

import java.io.*;
import java.util.*;

public class TestsRunner
{
    public static final String ETALON_TESTER = "etalon";
    public static final String RECOMPILE_TESTER = "recompile";

    public static void main(String[] args)
    {
        if ( (args.length < 2) ||
             (args.length == 1 && Params.HELP.equals(args[0])) )
        {
            System.out.println("USAGE:\n" +
                    "TestsRunner <Tester> -tests_dir=<Test DIR> <Tester params>\n" +
                    "   <Tester>: could be etalon, recompile\n" +
                    "   <Test DIR>: directory with test files\n" +
                    "   <Tester params>: parameters for each tester\n");
            return;
        }
        Map params = Utils.parseArguments(args);

        String tester = args[0];
        Tester testerClass;
        if (ETALON_TESTER.equalsIgnoreCase(tester))
        {
            testerClass = new EtalonTester();
        }
        else if (RECOMPILE_TESTER.equalsIgnoreCase(tester))
        {
            testerClass = new RecompileTester(params);
        }
        else
        {
            throw new RuntimeException("No such tester");
        }

        File testDir = new File((String) params.get(Params.TESTS_DIR));
        TestTool.suite(testDir, testerClass);
    }
}
