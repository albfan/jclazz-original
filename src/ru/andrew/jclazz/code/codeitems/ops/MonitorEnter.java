package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.*;

/**
 * Opcodes: 194<BR>
 * Parameters: no<BR>
 * Operand stack: objectref => <BR>
 */
public class MonitorEnter extends Operation
{
    public MonitorEnter(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    public boolean isPush()
    {
        return false;
    }

    public String getPushType()
    {
        return null;
    }

    public String str()
    {
        return null;
    }

    public void analyze(Block block)
    {
        Operation push = block.removePriorPushOperation();

        // Removing previous pop operation that stores sync object into local variable
        if (block.getPreviousOperation() instanceof Pop)
        {
            block.removePreviousOperation();
        }

        CodeItem ci = block.getNextOperation();
        if (!(ci instanceof Try))
        {
            throw new OperationException("MonitorEnter: try block is missing");
        }
        Sync sync = new Sync(push.str(), block, ((Try) ci).getOperations());
        block.replaceCurrentOperation(sync);

        // Removing next Try and Catch blocks
        block.next();
        block.removeCurrentOperation();
        CodeItem catchBlock = block.next();
        if (!(catchBlock instanceof Catch))
        {
            throw new OperationException("MonitorEnter: catch block is missing");
        }
        block.removeCurrentOperation();
        block.back();
    }
}
