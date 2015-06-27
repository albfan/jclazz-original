package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

import java.util.*;

public class NewArrayView extends OperationView
{
    private String count;

    private List initVariables = new ArrayList();

    public NewArrayView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public void addInitVariable(String pushVar)
    {
        initVariables.add(pushVar);
    }

    public String source()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("new ").append(alias(((NewArray) operation).getNewArrayType())).append("[");
        if (initVariables.isEmpty()) sb.append(count);
        sb.append("]");
        if (!initVariables.isEmpty())
        {
            sb.append("{");
            for (Iterator it = initVariables.iterator(); it.hasNext();)
            {
                sb.append(it.next());
                if (it.hasNext()) sb.append(", ");
            }
            sb.append("}");
        }
        return sb.toString();
    }

    public String getPushType()
    {
        return ((NewArray) operation).getNewArrayType() + "[]";
    }

    public void analyze(Block block)
    {
        OperationView prev = block.removePriorPushOperation();
        count = prev.source();
    }
}
