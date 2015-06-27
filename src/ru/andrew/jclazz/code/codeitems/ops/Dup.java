package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

/**
 * Opcodes: 89<BR>
 * Parameters: no<BR>
 * Operand stack: value => value, value<BR>
 */
public class Dup extends Operation
{
    private String pushValue;
    private String pushType;

    public Dup(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return pushType;
    }

    public void analyze(Block block)
    {
        Operation prev = block.getPriorPushOperation();
        if (prev instanceof New)    // For new + init
        {
            // removing current Dup operation
            block.removeCurrentOperation();
            return;
        }
        pushValue = prev.str();
        pushType = prev.getPushType(); 
    }

    public String str()
    {
        return pushValue;
    }
}
