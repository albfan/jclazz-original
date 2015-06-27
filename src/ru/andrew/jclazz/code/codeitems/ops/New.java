package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.*;

import java.io.*;

/**
 * Opcodes: 187<BR>
 * Parameters: class(2)<BR>
 * Operand stack: => objectref<BR>
 */
public class New extends Operation
{
    private String clazzName;

    private Clazz clazz;

    private boolean isICConstructor = false;
    private boolean isACConstructor = false;
    private InnerClass anonymousClass;

    public New(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        this.clazz = code.getClazz();

        int _newindex = (params[0] << 8) | params[1];
        CONSTANT_Class cl_new_info = (CONSTANT_Class) code.getClazz().getConstant_pool()[_newindex];
        clazzName = cl_new_info.getFullyQualifiedName();
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return clazzName;
    }

    public boolean isICConstructor()
    {
        return isICConstructor;
    }

    public boolean isACConstructor()
    {
        return isACConstructor;
    }

    public InnerClass getAnonymousClass()
    {
        return anonymousClass;
    }

    public String str()
    {
        return alias(clazzName);
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic());
        sb.append(" ").append(clazzName);
        pw.println(sb.toString());
    }

    public void analyze(Block block)
    {
        // Inner Class support
        if (clazzName.indexOf('$') != -1)
        {
            InnerClass ic = clazz.getInnerClass(clazzName);
            if (ic != null)
            {
                if (ic.getInnerName() == null)
                {
                    isACConstructor = true;
                    anonymousClass = ic;
                }
                clazzName = clazzName.substring(clazzName.indexOf('$') + 1);
                isICConstructor = true;
            }
        }
    }
}
