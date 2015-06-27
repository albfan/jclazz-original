package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

import java.io.*;

/**
 * Opcodes: 193<BR>
 * Parameters: cast_to_class(2)<BR>
 * Operand stack: objectref => result<BR>
 */
public class InstanceOf extends Operation
{
    private String toClass;
    private String var;

    public InstanceOf(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        CONSTANT_Class_info cl_info = (CONSTANT_Class_info) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
        toClass = cl_info.getFullyQualifiedName();
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return "boolean";
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic());
        sb.append(" ").append(toClass);
        pw.println(sb.toString());
    }

    public void analyze(Block block)
    {
        Operation prev = block.removePriorPushOperation();
        var = prev.str();
    }

    public String str()
    {
        return var + " instanceof " + alias(toClass);
    }
}
