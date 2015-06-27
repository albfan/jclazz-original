package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

/**
 * Opcodes: 197<BR>
 * Parameters: type(2), dimensions(1)<BR>
 * Operand stack: count1, [count2, ...] => arrayref<BR>
 */
public class MultiNewArray extends Operation
{
    private int dimensions;
    private String dims[];
    private String arrayClass;

    public MultiNewArray(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        CONSTANT_Class_info cl_info = (CONSTANT_Class_info) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
        arrayClass = cl_info.getFullyQualifiedName();

        dimensions = params[2];
        dims = new String[dimensions];
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return arrayClass;
    }

    public void print(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer(arrayClass.substring(0, arrayClass.indexOf("[]")));
        for (int i = 0; i < dimensions; i++)
        {
            sb.append("[").append(dims[i]).append("]");
        }
        pw.println(indent + "new " + sb.toString());
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic());
        sb.append(" x").append(dimensions).append(" of ").append(arrayClass);
        pw.println(sb.toString());
    }

    public void analyze(Block block)
    {
        for (int i = 0; i < dimensions; i++)
        {
            Operation push = block.removePriorPushOperation();
            dims[dimensions - i - 1] = push.str();
        }
    }

    public String str()
    {
        StringBuffer sb = new StringBuffer("new " + alias(arrayClass.substring(0, arrayClass.indexOf("[]"))));
        for (int i = 0; i < dimensions; i++)
        {
            sb.append("[").append(dims[i]).append("]");
        }
        return sb.toString();
    }
}
