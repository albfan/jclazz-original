package ru.andrew.jclazz.apps.tester.suite;

public class External extends Source
{
    private String compiler;

    public External(String path, String javaVersion, String compiler)
    {
        super(path, javaVersion);

        this.compiler = compiler;
    }

    public String getPath(Compiler compiler)
    {
        String fsep = System.getProperty("file.separator");
        return path + fsep + "ext" + fsep + name.replace('.', fsep.charAt(0));
    }

    public String getCompiler()
    {
        return compiler;
    }
}
