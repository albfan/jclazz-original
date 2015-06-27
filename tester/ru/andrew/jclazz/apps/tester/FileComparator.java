package ru.andrew.jclazz.apps.tester;

import java.io.*;
import java.util.*;

public final class FileComparator
{
    public static boolean compare(String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(path2);
        int bufferSize = (int) Math.max(file1.length(), file2.length());
        FileInputStream fis1 = null;
        FileInputStream fis2 = null;
        byte b1[] = new byte[bufferSize];
        byte b2[] = new byte[bufferSize];
        try
        {
            fis1 = new FileInputStream(file1);
            fis2 = new FileInputStream(file2);

            int res1, res2;
            do
            {
                res1 = fis1.read(b1);
                res2 = fis2.read(b2);
                if (res1 == -1 && res2 == -1) break;
                if ((res1 == -1 && res2 != -1) || (res1 != -1 && res2 == -1) || (res1 != res2))
                {
                    return false;
                }

                if (!Arrays.equals(b1, b2)) return false;
            }
            while (res1 != -1 || res2 != -1);

            return true;
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                if (fis1 != null) fis1.close();
                if (fis2 != null) fis2.close();
            }
            catch (IOException e)
            {
                System.out.println("ERROR");
            }
        }
    }
}
