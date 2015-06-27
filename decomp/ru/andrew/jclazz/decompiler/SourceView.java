package ru.andrew.jclazz.decompiler;

import java.io.*;

public abstract class SourceView
{
    private ByteArrayOutputStream baos;
    private PrintWriter pw;

    private String indent = "";

    private String source;

    protected SourceView()
    {
        baos = new ByteArrayOutputStream();
        pw = new PrintWriter(baos);
    }

    protected abstract void parse();

    protected void loadSource()
    {
        parse();
        flush();
        source = baos.toString();
        close();
    }

    protected void print(String str)
    {
        pw.print(str);
    }

    protected void println(String str)
    {
        pw.println(str);
    }

    protected void flush()
    {
        pw.flush();
    }

    protected void clearAll()
    {
        flush();
        baos.reset();
    }

    protected void close()
    {
        try
        {
            baos.close();
        }
        catch (IOException ioe)
        {
            throw new RuntimeException(ioe);
        }
        pw.close();
    }

    // Useful methods

    public void setIndent(String indent)
    {
        this.indent = indent;
    }

    public String getSource()
    {
        String nl = System.getProperty("line.separator");
        String src = indent + source.replaceAll(nl, nl + indent);
        if (src.endsWith(indent)) src = src.substring(0, src.length() - indent.length());
        return src;
    }

    public abstract ClazzSourceView getClazzView();

    protected String importClass(String clazz)
    {
        return ImportManager.getInstance().importClass(clazz, getClazzView());
    }
}
