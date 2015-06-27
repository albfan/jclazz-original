package ru.andrew.jclazz.decompiler.engine;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.decompiler.*;

public class LocalVariable
{
    public static final String UNKNOWN_TYPE = "_UNKNOWN_TYPE_";

    private int lv_num;
    private String type;
    private String name;

    private boolean forceThis = false;

    private MethodSourceView methodView;

    private boolean isPrinted = false;

    public LocalVariable(int ivar, String type, MethodSourceView methodView)
    {
        this.lv_num = ivar;
        this.type = type != null ? type : UNKNOWN_TYPE;
        this.name = type != null ? methodView.getLVName(type) : null;

        this.methodView = methodView;
    }

    public int getLVNumber()
    {
        return lv_num;
    }

    private LocalVariableTable.LocalVariable cachedLV;

    public void ensure(int start_byte)
    {
        if (methodView.getMethod().getCodeBlock() != null && methodView.getMethod().getCodeBlock().getLocalVariableTable() != null)
        {
            LocalVariableTable.LocalVariable lv = methodView.getMethod().getCodeBlock().getLocalVariableTable().getLocalVariable(lv_num, start_byte);
            if (lv == cachedLV) return;
            cachedLV = lv;
            name = lv.name.getString();
            try
            {
                type = new FieldDescriptor(lv.descriptor.getString()).getFQN();
            }
            catch (ClazzException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public String getType()
    {
        return type;
    }

    public void renewType(String type)
    {
        this.type = type;
        this.name = methodView.getLVName(type);
    }

    public String getName()
    {
        return name;
    }

    public void forceThis()
    {
        forceThis = true;
        this.name = "this";
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
