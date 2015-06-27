package ru.andrew.jclazz.decompiler.engine.blocks;

import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;

import java.util.*;

public class Sync extends Block
{
    private String syncObject;

    public Sync(String syncObject, Block parent, List ops)
    {
        super(parent, ops);
        this.syncObject = syncObject;

        // Removing last push and monitorexit operations
        CodeItem monitorExit = getLastOperation();
        if (!(monitorExit instanceof MonitorExitView))
        {
            throw new RuntimeException("monitorexit opcode's not found in Sync block");
        }
        CodeItem push = getOperationPriorTo(monitorExit.getStartByte());
        if (!(push instanceof OperationView) || !((OperationView) push).isPush())
        {
            throw new RuntimeException("push opcode for monitorexit's not found in Sync block");
        }
        removeOperation(monitorExit.getStartByte());
        removeOperation(push.getStartByte());
    }

    public String getSource()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append("synchronized (").append(syncObject).append(")").append(NL).append(super.getSource());
        return sb.toString();
    }
}
