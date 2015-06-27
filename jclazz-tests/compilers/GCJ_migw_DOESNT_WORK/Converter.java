package com.netcracker.xmlutils.modules;

import java.io.*;

/**
 * Andrew Dmitriev
 * Date: 22.03.2007
 */
public class Converter
{
    private StringBuffer content = new StringBuffer();

    public String convertToUTF8(String inFile, String outFile)
    {
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        try
        {
            isr = new InputStreamReader(new FileInputStream(inFile));
            osw = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
            content.append("Input file encoding > ").append(isr.getEncoding());
            content.append("Output file encoding > ").append(osw.getEncoding());
            int ch = 0;
            while (ch != -1)
            {
                ch = isr.read();
                osw.write(ch);
            }
        }
        catch (FileNotFoundException fnfe)
        {
            content.append("File not found: ").append(fnfe.getMessage());
        }
        catch (IOException ioe)
        {
            content.append("IO Exception: ").append(ioe.getMessage());
        }
        finally
        {
            try
            {
                if (isr != null) isr.close();
            }
            catch (IOException ine)
            {
                content.append("Error while closing ").append(inFile).append(": ").append(ine.getMessage());
            }
            finally
            {
                try
                {
                    if (osw != null) osw.close();
                }
                catch (IOException oute)
                {
                    content.append("Error while closing ").append(outFile).append(": ").append(oute.getMessage());
                }
            }
        }
        return content.toString();
    }
}
