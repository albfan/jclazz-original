package ru.andrew.jclazz.apps.tester;

import java.io.*;

public interface Tester
{
    public String getName();

    public boolean test(File file);

    public FileFilter getInputFileFilter();
}
