package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class PushConstView extends OperationView
{
    public PushConstView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return ((PushConst) operation).getPushType();
    }

    public String source()
    {
        return ((PushConst) operation).isClassPushed() ? alias(((PushConst) operation).getPushValue()) : ((PushConst) operation).getPushValue();
    }
    
    public void analyze(Block block)
    {
    }
}
