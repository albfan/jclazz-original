package ru.andrew.jclazz.apps.tester;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.apps.*;
import ru.andrew.jclazz.apps.decomp.*;

import java.io.*;
import java.util.*;

public class RecompileTester implements Tester, FileFilter
{
    private String javac;
    private boolean isPrecompile;
    private String buildDir;
    private String testDir;

    public RecompileTester(Map params)
    {
        isPrecompile = "yes".equals(params.get(Params.RCT_PRECOMPILE));

        String fs = System.getProperty("file.separator");
        String javaHome = (String) params.get(Params.RCT_JAVA_HOME);
        if (javaHome == null || "".equals(javaHome))
        {
            throw new RuntimeException("No " + Params.RCT_JAVA_HOME + " specified");
        }
        this.javac = javaHome + fs + "bin" + fs + "javac";

        testDir = (String) params.get(Params.TESTS_DIR);

        // Adding sourcedir
        this.javac += " -sourcepath " + testDir;

        if (params.get(Params.RCT_JAVAC_ARGS) != null)
        {
            this.javac += " " + params.get(Params.RCT_JAVAC_ARGS);
        }

        // Creating build directory
        File bdir = new File(params.get(Params.TESTS_DIR) + fs + "\\_build");
        bdir.mkdir();
        buildDir = bdir.getAbsolutePath();
    }

    public String getName()
    {
        return "recompile";
    }

    public boolean test(File file)
    {
        try
        {
            Map params = new HashMap();
            params.put(Params.EXTENSION, "java");
                    
            String compiledName = file.getAbsolutePath();
            compiledName = compiledName.substring(0, compiledName.lastIndexOf('.'));

            if (isPrecompile)
            {
                boolean res = execJavac(javac, file.getAbsolutePath());
                String nn = compiledName + ".java_orig";
                file.renameTo(new File(nn));
                if (!res) return false;
            }

            String outFile = ClassDecompiler.generateJavaFile(compiledName, params);

            boolean res = execJavac(javac + " -d \"" + buildDir + "\"", outFile);

            // Renaming to prevent looping
            File outJavaFile = new File(outFile);
            outJavaFile.renameTo(new File(outJavaFile.getAbsolutePath() + ".post"));

            if (!res) return false;

            return compare(compiledName);
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

    private boolean execJavac(String cmd, String path) throws IOException
    {
        JavacWrap wrapper = new JavacWrap(cmd + " " + path);
        wrapper.start();
        int exitValue = wrapper.result();
        if (exitValue == 0)
        {
            return true;
        }

        if (exitValue == -13)
        {
            System.out.println("Forced termination while compiling " + path);
            return false;
        }
        System.out.println("Error while compiling " + path);
        BufferedReader br = new BufferedReader(new InputStreamReader(wrapper.getErrorStream()));
        String line;
        while ((line = br.readLine()) != null)
        {
            System.out.println(line);
        }
        return false;
    }

    private boolean compare(String path)
    {
        String fs = System.getProperty("file.separator");
        String originalPath = path.substring(0, path.lastIndexOf(fs));
        final String clname = path.substring(path.lastIndexOf(fs) + 1);
        File originalDir = new File(originalPath);
        File[] origFiles = originalDir.listFiles(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return name.startsWith(clname) && name.endsWith(".class");
            }
        });

        // Calculating destination directory
        String packageDir = originalDir.getAbsolutePath();
        packageDir = packageDir.substring(testDir.length());

        boolean res = true;
        for (int i = 0; i < origFiles.length; i++)
        {
            String testFile = buildDir + packageDir + fs + origFiles[i].getName();
            res &= FileComparator.compare(origFiles[i].getAbsolutePath(), testFile);
        }

        return res;
    }

    public FileFilter getInputFileFilter()
    {
        return this;
    }

    public boolean accept(File pathname)
    {
        if (isPrecompile)
        {
            return pathname.isDirectory() || pathname.getName().endsWith(".java");
        }

        String name = pathname.getName();
        boolean contains$ = name.indexOf('$') != -1;
        int ind = name.lastIndexOf('.');
        if (ind != -1)
        {
            if ("class".equals(name.substring(ind + 1)) && !contains$)
            {
                return true;
            }
        }
        return pathname.isDirectory();
    }
}
