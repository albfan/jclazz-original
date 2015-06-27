package ru.andrew.jclazz.code.codeitems;

import ru.andrew.jclazz.code.codeitems.blocks.*;

import java.io.*;

public interface CodeItem
{
    public long getStartByte();

    public void print(PrintWriter pw, String indent);

    public void printRaw(PrintWriter pw, String indent);

    public void analyze(Block block);
}
