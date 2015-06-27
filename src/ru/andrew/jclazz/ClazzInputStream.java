package ru.andrew.jclazz;

import java.io.*;

public class ClazzInputStream
{
    private DataInputStream dis = null;
    private long cnt = 0;

    public ClazzInputStream(String fileName) throws FileNotFoundException
    {
        dis = new DataInputStream(new FileInputStream(fileName));
    }

    public void close() throws IOException
    {
        if (dis != null) dis.close();
    }

    public int readU1() throws IOException
    {
        cnt++;
        return dis.readUnsignedByte();
    }

    public int readU2() throws IOException
    {
        cnt += 2;
        return dis.readUnsignedShort();
    }

    public long readU4() throws IOException
    {
        long high = dis.readUnsignedShort();
        long low = dis.readUnsignedShort();
        cnt += 4;
        return (high << 16) + low;
    }

    public long getPosition()
    {
        return cnt;
    }
}
