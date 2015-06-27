package ru.andrew.jclazz.apps.tester.suite;

import java.io.*;

public class Source
{
    protected String javaVersion;
    protected String path;
    protected String name;

    public Source(String path, String javaVersion)
    {
        this.javaVersion = javaVersion;
        this.name = path;
        this.path = new File(".").getAbsolutePath();
    }

    public String getJavaVersion()
    {
        return javaVersion;
    }

    public String getName()
    {
        return name;
    }

    public String getPath(Compiler compiler)
    {
        String fsep = System.getProperty("file.separator");
        return path + fsep + compiler.getName() + fsep + name.replace('.', fsep.charAt(0));
    }
}
