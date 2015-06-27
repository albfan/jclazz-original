package ru.andrew.jclazz.apps.tester;

import java.io.*;

public class JavacWrap extends Thread
{
    private String command;
    private int exitValue = -1;

    private Process proc;

    public JavacWrap(String command)
    {
        this.command = command;
    }

    public void run()
    {
        try
        {
            proc = Runtime.getRuntime().exec(command);
            exitValue = proc.waitFor();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public int result()
    {
        try
        {
            for (int i = 0; i < 30; i++)
            {
                sleep(100);
                if (exitValue != -1) return exitValue;
            }
            proc.destroy();
            return -13;
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public InputStream getErrorStream()
    {
        return proc.getErrorStream();
    }
}
