package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

public class ArrayPushView extends OperationView
{
    private String arrayRef;
    private String index;
    private String arrayType;

    public ArrayPushView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String source()
    {
//        if (ref != null)
//        {
//            return ref.getOperation();
//        }
        return arrayRef + "[" + index + "]";
    }

    public String getPushType()
    {
        return arrayType;
    }

    public void analyze2(Block block)
    {
        OperationView indexOp = context.pop();
        OperationView arrayOp = context.pop();

        arrayType = arrayOp.getPushType();
        // Removing 1 array dimension
        if (arrayType.endsWith("[]")) arrayType = arrayType.substring(0, arrayType.length() - 2);
        
        view = new Object[]{arrayOp, "[", indexOp, "]"};
        context.push(this);
    }

    public boolean isPrintable()
    {
        return false;
    }
}
