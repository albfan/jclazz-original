package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

import java.io.*;

public class CONSTANT_Utf8_info extends CP_INFO
{
    private String str;

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException
    {
        int length = cis.readU2();
        StringBuffer sb = new StringBuffer();

        int i = 0;
        while (i < length)
        {
            int x = cis.readU1();
            i++;
            if ((x & 128) > 0) // 10000000
            {
                int y = cis.readU1();
                i++;
                if ((x & 32) > 0) // 00100000
                {
                    // 3 Bytes
                    int z = cis.readU1();
                    char ch = (char) (((x & 15) << 12) + ((y & 63) << 6) + (z & 63));
                    i++;
                    sb.append(ch);
                }
                else
                {
                    // 2 Bytes
                    char ch = (char) (((x & 31) << 6) + (y & 63));
                    sb.append(ch);
                }
            }
            else
            {
                // 1 Byte
                sb.append((char) x);
            }
        }
        if (i > length)
        {
            throw new IOException("CONSTANT_Utf8_info >> " + sb + "[i=" + i + ", length=" + length + "]");
        }

        str = sb.toString();
        str = escape(str);
    }

    public void complete() throws ClazzException
    {
    }

    public String getType()
    {
        return "java.lang.String";
    }

    public String getString()
    {
        return str;
    }

    /*private void replace(String from, String to)
    {
        int ind = 0;
        while ((ind = str.indexOf(from, ind)) != -1)
        {
            str = str.substring(0, ind) + to + str.substring(ind + from.length());
            ind += to.length();
        }
    }*/

    private String escape(String str)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            switch(ch)
            {
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                //case '\'':
                //    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    // Support for octal escape (up ot \377) and non-printable characters
                    sb.append(ch);
            }
        }
        return sb.toString();
    }

    public String toString()
    {
        return str;
    }
}
