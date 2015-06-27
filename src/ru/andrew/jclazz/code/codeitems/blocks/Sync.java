package ru.andrew.jclazz.code.codeitems.blocks;

import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.codeitems.ops.*;

import java.util.*;
import java.io.*;

public class Sync extends Block
{
    private String syncObject;

    public Sync(String syncObject, Block parent, List ops)
    {
        super(parent, ops);
        this.syncObject = syncObject;

        // Removing last push and monitorexit operations
        CodeItem monitorExit = getLastOperation();
        if (!(monitorExit instanceof Operation) || ((Operation) monitorExit).getOpcode().getOpcode() != 195)
        {
            throw new RuntimeException("monitorexit opcode's not found in Sync block");
        }
        CodeItem push = getOperationPriorTo(monitorExit.getStartByte());
        if (!(push instanceof Operation) || !((Operation) push).isPush())
        {
            throw new RuntimeException("push opcode for monitorexit's not found in Sync block");
        }
        removeOperation(monitorExit.getStartByte());
        removeOperation(push.getStartByte());
    }

    public void print(PrintWriter pw, String init_indent)
    {
        pw.println(init_indent + "synchronized (" + syncObject + ")");
        super.print(pw, init_indent);
    }
}
