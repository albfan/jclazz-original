package ru.andrew.jclazz.apps.tester.suite;

public class Compiler
{
    private String name;
    private String javaVersion;
    private String exec;
    private String args;

    public Compiler(String name, String javaVersion, String exec, String args)
    {
        this.name = name;
        this.javaVersion = javaVersion;
        this.exec = exec;
        this.args = args;
    }

    public String getName()
    {
        return name;
    }

    public String getJavaVersion()
    {
        return javaVersion;
    }

    public String getCmd()
    {
        return exec + " " + args;
    }

    public String getExec()
    {
        return exec;
    }
}
