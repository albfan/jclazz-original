package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

// TODO the following opcodes should be implemented
// 90 - 95   <- dup[2][_x[1,2]], swap
public abstract class OperationView implements CodeItem
{
    protected Operation operation;
    protected MethodSourceView methodView;

    public OperationView(Operation operation, MethodSourceView methodView)
    {
        this.operation = operation;
        this.methodView = methodView;
    }

    // For Fake Push Operation and Nop operation
    //protected OperationView(long start_byte)
    //{
    //    this.start_byte = start_byte;
    //}

    public int getOpcode()
    {
        return operation.getOpcode().getOpcode();
    }

    public long getStartByte()
    {
        return operation.getStartByte();
    }

    public boolean isPush()
    {
        return operation instanceof PushOperation;
    }

    public abstract String getPushType();

    protected String alias(String fqn)
    {
        return ImportManager.getInstance().importClass(fqn, methodView.getClazzView());
    }

    public abstract String source();

    public abstract void analyze(Block block);

    protected String getLVName(LocalVariable lv)
    {
        lv.ensure((int) getStartByte());
        return lv.getName();
    }

    protected String getLVType(LocalVariable lv)
    {
        lv.ensure((int) getStartByte());
        if (LocalVariable.UNKNOWN_TYPE.equals(lv.getType()))
        {
            lv.renewType("java.lang.Object");
        }
        return lv.getType();
    }
}
