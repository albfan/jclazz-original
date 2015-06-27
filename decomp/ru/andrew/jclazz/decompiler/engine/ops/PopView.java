package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.*;

public class PopView extends OperationView
{
    private String pushedValue;
    private LocalVariable lvar;

    private String arrayRef;
    private String index;

    public PopView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return null;
    }

    public int getLocalVariableNumber()
    {
        return ((Pop) operation).getLocalVariableNumber();
    }

    public String source()
    {
        if (lvar == null && arrayRef == null && index == null)
        {
            return pushedValue;
        }
        String src;
        if (lvar != null)
        {
            if (!lvar.isPrinted())
            {
                src = alias(getLVType(lvar)) + " ";
                lvar.setPrinted(true);
            }
            else
            {
                src = "";
            }
            src += getLVName(lvar);
        }
        else
        {
            src = arrayRef + "[" + index + "]";
        }
        src += " = " + pushedValue;
        return src;
    }

    public void analyze(Block block)
    {
        if (getOpcode() >= 79 && getOpcode() <= 86)
        {
            OperationView val_ci = block.removePriorPushOperation();
            pushedValue = val_ci.source();

            OperationView ind_ci = block.removePriorPushOperation();
            index = ind_ci.source();

            OperationView arr_ci = block.removePriorPushOperation();
            arrayRef = arr_ci.source();

            // Support array initialization : new Object[]{...}
            if (arrayRef.startsWith("new "))
            {
                OperationView prev = block.getPriorPushOperation();
                if (prev instanceof NewArrayView)
                {
                    ((NewArrayView) prev).addInitVariable(pushedValue);
                    block.removeCurrentOperation();
                }
            }
        }
        else
        {
            OperationView pushOp = block.removePriorPushOperation();
            if (getOpcode() >= 54 && getOpcode() <= 78)
            {
                lvar = block.getLocalVariable(((Pop) operation).getLocalVariableNumber(), pushOp.getPushType());
            }
            pushedValue = pushOp.source();
            if (getOpcode() == 88)
            {
                String pusht = pushOp.getPushType();
                if (!"double".equals(pusht) && !"long".equals(pusht))    // pop2 form1 support
                {
                    block.removePriorPushOperation();   // removing previous second push
                }
            }
        }
    }
}
