package ru.andrew.jclazz.apps.tester.suite;

import java.io.*;

public final class CompilerWrapper extends Thread
{
    public static int TIMEOUT = 3000;

    private String command;
    private int exitValue = -1;

    private Process proc;
    private String out;
    private String err;

    public CompilerWrapper(String command)
    {
        this.command = command;
    }

    public void run()
    {
        try
        {
            proc = Runtime.getRuntime().exec(command);
            WrapperOutputReader worOut = new WrapperOutputReader(proc.getInputStream());
            WrapperOutputReader worErr = new WrapperOutputReader(proc.getErrorStream());
            worOut.start();
            worErr.start();
            exitValue = proc.waitFor();
            out = worOut.getBuffer();
            err = worErr.getBuffer();
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
            for (int i = 0; i < TIMEOUT / 100; i++)
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

    public String getError()
    {
        return err;

//        String ln = System.getProperty("line.separator");
//        try
//        {
//            StringBuffer sb = new StringBuffer();
//            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
//            String line;
//            while ((line = br.readLine()) != null)
//            {
//                sb.append(line).append(ln);
//            }
//            return sb.toString();
//        }
//        catch (IOException ioe)
//        {
//            return "!!! ERROR READING ERR STREAM !!!";
//        }
    }

    class WrapperOutputReader extends Thread
    {
        private InputStream inputStream;
        private StringBuffer buffer;
        private final String NL = System.getProperty("line.separator");

        WrapperOutputReader(InputStream inputStream)
        {
            this.inputStream = inputStream;
            this.buffer = new StringBuffer();
        }

        public void run()
        {
            try
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                while (true)
                {
                    String s = br.readLine();
                    if (s == null) break;
                    buffer.append(s).append(NL);
                }
                br.close();
            }
            catch (IOException e)
            {
                buffer.append("IOException occured ").append(e.getMessage());
            }
        }

        public String getBuffer()
        {
            return buffer.toString();
        }
    }
}
