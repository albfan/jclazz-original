package ru.andrew.jclazz.code;

public class LocalVariable
{
    private int lv_num;
    private String type;
    private String name;

    private boolean isPrinted = false;

    public LocalVariable(int ivar, String type, String name)
    {
        this.lv_num = ivar;
        this.type = type;
        this.name = name;
    }

    public int getLVNumber()
    {
        return lv_num;
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public boolean isPrinted()
    {
        return isPrinted;
    }

    public void setPrinted(boolean printed)
    {
        isPrinted = printed;
    }

    public String toString()
    {
        return "LV-" + lv_num + "(" + type + " as " + name + ")";
    }
}
