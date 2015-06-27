package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class DupView extends OperationView
{
    private String pushValue;
    private String pushType;

    public DupView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return pushType;
    }

    public String source()
    {
        return pushValue;
    }

    public void analyze(Block block)
    {
        OperationView prev = block.getPriorPushOperation();
        if (prev instanceof NewView)    // For new + init
        {
            // removing current Dup operation
            block.removeCurrentOperation();
            return;
        }
        pushValue = prev.source();
        pushType = prev.getPushType(); 
    }
}
