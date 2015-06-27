package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

public class MultiNewArrayView extends OperationView
{
    private String dims[];

    public MultiNewArrayView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
        dims = new String[((MultiNewArray) operation).getDimensions()];
    }

    public String getPushType()
    {
        return ((MultiNewArray) operation).getArrayType();
    }

    public String source()
    {
        StringBuffer sb = new StringBuffer("new " + alias(((MultiNewArray) operation).getArrayType().substring(0, ((MultiNewArray) operation).getArrayType().indexOf("[]"))));
        for (int i = 0; i < ((MultiNewArray) operation).getDimensions(); i++)
        {
            sb.append("[").append(dims[i]).append("]");
        }
        return sb.toString();
    }

    public void analyze(Block block)
    {
        for (int i = 0; i < ((MultiNewArray) operation).getDimensions(); i++)
        {
            OperationView push = block.removePriorPushOperation();
            dims[((MultiNewArray) operation).getDimensions() - i - 1] = push.source();
        }
    }
}
