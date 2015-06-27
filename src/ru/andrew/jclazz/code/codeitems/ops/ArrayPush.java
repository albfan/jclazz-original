package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.attributes.*;


/**
 * Opcodes: 46 - 53<BR>
 * Parameters: no<BR>
 * Operand stack: arrayref, index => value<BR>
 */
public class ArrayPush extends Operation
{
    private String arrayRef;
    private String index;
    private String arrayType;

    public ArrayPush(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    public String str()
    {
        return arrayRef + "[" + index + "]";
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return arrayType;
    }

    public void analyze(Block block)
    {
        Operation indexOp = block.removePriorPushOperation();
        index = indexOp.str();

        Operation arrayOp = block.removePriorPushOperation();
        arrayRef = arrayOp.str();
        arrayType = arrayOp.getPushType();
        // Removing 1 array dimension
        if (arrayType.endsWith("[]")) arrayType = arrayType.substring(0, arrayType.length() - 2);
    }
}
