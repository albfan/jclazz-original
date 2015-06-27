package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

/**
 * Opcodes: 190<BR>
 * Parameters: no<BR>
 * Operand stack: arrayref => length<BR>
 */
public class ArrayLength extends Operation
{
    private String arrayRef;

    public ArrayLength(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    public String str()
    {
        return arrayRef + ".length";
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return "int";
    }

    public void analyze(Block block)
    {
        Operation prev = block.removePriorPushOperation();
        arrayRef = prev.str();
        if (prev instanceof CheckCast)
        {
            arrayRef = "(" + arrayRef + ")";
        }
    }
}
