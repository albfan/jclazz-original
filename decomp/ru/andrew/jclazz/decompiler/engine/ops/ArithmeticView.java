package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

public class ArithmeticView extends OperationView
{
    private String value1;
    private String value2;
    private String negValue;

    public ArithmeticView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String source()
    {
        if (negValue != null)
        {
            return "-" + negValue;
        }
        else
        {
            return value2 + " " + ((Arithmetic) operation).getOperation() + " " + value1;
        }
    }

    public String getPushType()
    {
        return ((Arithmetic) operation).getResultType();  
    }

    public void analyze(Block block)
    {
        int opcode = getOpcode();
        if (opcode >= 116 && opcode <= 119) // neg
        {
            OperationView prev = block.removePriorPushOperation();
            negValue = prev.source();
        }
        else
        {
            OperationView pushVal1 = block.removePriorPushOperation();
            value1 = pushVal1.source();
            OperationView pushVal2 = block.removePriorPushOperation();
            value2 = pushVal2.source();
        }
    }
}
