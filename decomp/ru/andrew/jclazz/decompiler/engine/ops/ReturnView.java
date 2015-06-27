package ru.andrew.jclazz.decompiler.engine.ops;

import java.util.ArrayList;
import java.util.List;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.CodeItem;

public class ReturnView extends OperationView
{
    private String retValue;
    private boolean isBooleanReturned = false;

    private boolean isLastInBlock;

    public ReturnView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);

        if ("boolean".equals(methodView.getMethod().getDescriptor().getReturnType().getBaseType()))
        {
            isBooleanReturned = true;
        }
    }

    public String getPushType()
    {
        return null;
    }

    public String source()
    {
        if (getOpcode() == 177)   // Return void
        {
            return isLastInBlock ? null : "return";
        }
        else
        {
            if (isBooleanReturned)
            {
                if ("1".equals(retValue))
                {
                    return "return true";
                }
                else if ("0".equals(retValue))
                {
                    return "return false";
                }
                else
                {
                    return "return " + retValue;
                }
            }
            else
            {
                return "return " + retValue;
            }
        }
    }

    public void analyze(Block block)
    {
        /*
        isLastInBlock = !block.hasMoreOperations();
        Block parent = block;
        long start = getStartByte();
        while (parent != null)
        {
            if (parent.getOperationAfter(start) != null)
            {
                isLastInBlock = false;
                break;
            }
            start = parent.getStartByte();
            parent = parent.getParent();
        }

        // multi-conditional return
        if (block.getPreviousOperation() instanceof Else)
        {
            Else elseBlock = (Else) block.getPreviousOperation();
            CodeItem probIfBlock = block.getOperationPriorTo(elseBlock.getStartByte());
            if (probIfBlock instanceof IfBlock)
            {
                IfBlock ifBlock = (IfBlock) probIfBlock;
                if (ifBlock.size() == 1 && elseBlock.size() == 1 &&
                        ifBlock.getFirstOperation() instanceof PushConstView &&
                        elseBlock.getFirstOperation() instanceof PushConstView)
                {
                    PushConstView ifPushConst = (PushConstView) ifBlock.getFirstOperation();
                    PushConstView elsePushConst = (PushConstView) elseBlock.getFirstOperation();
                    String ifConst = ifPushConst.getPushValue();
                    String elseConst = elsePushConst.getPushValue();
                    if ("1".equals(ifConst) || "0".equals(elseConst))
                    {
                        block.removeOperation(ifBlock.getStartByte());
                        block.removeOperation(elseBlock.getStartByte());
                        retValue = ifBlock.getSourceConditions();
                        return;
                    }
                    if ("0".equals(ifConst) || "1".equals(elseConst))
                    {
                        block.removeOperation(ifBlock.getStartByte());
                        block.removeOperation(elseBlock.getStartByte());
                        retValue = "!" + ifBlock.getSourceConditions();
                        return;
                    }
                }
            }
        }

        if ("<init>".equals(((Return) operation).getReturnType()))
        {
            // Return in constructor
            return;
        }

        if (getOpcode() == 177) return;
        OperationView pushOp = block.removePriorPushOperation();
        retValue = pushOp.source();
         */
    }

    public void analyze2(Block block)
    {
        isLastInBlock = !block.hasMoreOperations();
        Block parent = block;
        long start = getStartByte();
        while (parent != null)
        {
            if (parent.getOperationAfter(start) != null)
            {
                isLastInBlock = false;
                break;
            }
            start = parent.getStartByte();
            parent = parent.getParent();
        }

        // multi-conditional return
        if (block.getPreviousOperation() instanceof Else)
        {
            Else elseBlock = (Else) block.getPreviousOperation();
            CodeItem probIfBlock = block.getOperationPriorTo(elseBlock.getStartByte());
            if (probIfBlock instanceof IfBlock)
            {
                IfBlock ifBlock = (IfBlock) probIfBlock;
                if (ifBlock.size() == 1 && elseBlock.size() == 1 &&
                        ifBlock.getFirstOperation() instanceof PushConstView &&
                        elseBlock.getFirstOperation() instanceof PushConstView)
                {
                    PushConstView ifPushConst = (PushConstView) ifBlock.getFirstOperation();
                    PushConstView elsePushConst = (PushConstView) elseBlock.getFirstOperation();
                    String ifConst = ifPushConst.getPushValue();
                    String elseConst = elsePushConst.getPushValue();
                    if ("1".equals(ifConst) || "0".equals(elseConst))
                    {
                        block.removeOperation(ifBlock.getStartByte());
                        block.removeOperation(elseBlock.getStartByte());
                        List res = new ArrayList();
                        res.add("return ");
                        res.addAll(ifBlock.getSourceConditionsView());
                        view = new Object[res.size()];
                        view = res.toArray(view);
                        return;
                    }
                    if ("0".equals(ifConst) || "1".equals(elseConst))
                    {
                        block.removeOperation(ifBlock.getStartByte());
                        block.removeOperation(elseBlock.getStartByte());
                        List res = new ArrayList();
                        res.add("return !");
                        res.addAll(ifBlock.getSourceConditionsView());
                        view = new Object[res.size()];
                        view = res.toArray(view);
                        return;
                    }
                }
            }
        }

        if ("<init>".equals(((Return) operation).getReturnType()))
        {
            // Return in constructor
            return;
        }
        
        if (getOpcode() == 177)   // Return void
        {
            if (!isLastInBlock)
            {
                view = new Object[]{"return"};
            }
        }
        else
        {
            OperationView retVal = context.pop();
            retValue = retVal.source3();
            if (isBooleanReturned)
            {
                if ("1".equals(retValue))
                {
                    view = new Object[]{"return true"};
                }
                else if ("0".equals(retValue))
                {
                    view = new Object[]{"return false"};
                }
                else
                {
                    view = new Object[]{"return ", retVal};
                }
            }
            else
            {
                view = new Object[]{"return ", retVal};
            }
        }
    }
}
