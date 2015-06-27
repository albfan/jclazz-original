package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

import java.io.*;

/**
 * Opcodes: 192<BR>
 * Parameters: to_class(2)<BR>
 * Operand stack: objectref => objectref<BR>
 */
public class CheckCast extends Operation
{
    private String toClass;
    private String var;

    public CheckCast(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        CONSTANT_Class cl_info = (CONSTANT_Class) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
        toClass = cl_info.getFullyQualifiedName();
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return toClass;
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

        // Strange behaviour, found on jre 1.6: doubling of identical checkcast
        if (prev instanceof CheckCast)
        {
            CheckCast cc = (CheckCast) prev;
            if (toClass.equals(cc.getPushType()))
            {
                var = cc.var;
                return;
            }
        }

        var = prev.str();
    }

    public String str()
    {
        return "(" + alias(toClass) + ") " + var;
    }
}
