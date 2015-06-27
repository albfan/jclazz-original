package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class ArrayLengthView extends OperationView
{
    private String arrayRef;

    public ArrayLengthView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String source()
    {
        return arrayRef + ".length";
    }

    public String getPushType()
    {
        return ((ArrayLength) operation).getResultType();
    }

    public void analyze(Block block)
    {
        OperationView prev = block.removePriorPushOperation();
        arrayRef = prev.source();
        if (prev instanceof CheckCastView)
        {
            arrayRef = "(" + arrayRef + ")";
        }
    }
}
