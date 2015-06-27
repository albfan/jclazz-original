package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.*;

public class PopView extends OperationView
{
    //private String pushedValue;
    private LocalVariable lvar;

    //private String arrayRef;
    //private String index;

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
        /*
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
         * */
        return null;
    }

    public void analyze(Block block)
    {
        /*
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
                    //((NewArrayView) prev).addInitVariable(pushedValue);
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
                if (pushOp.ref != null)
                {
                    pushOp.ref.setLocalVariable(lvar);

                }
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
         */
    }

    public void analyze2(Block block)
    {
        if (getOpcode() >= 79 && getOpcode() <= 86)
        {
            OperationView val_ci = context.pop();
            OperationView ind_ci = context.pop();
            OperationView arr_ci = context.pop();
            view = new Object[]{arr_ci, "[", ind_ci, "] = ", val_ci};
            // Support array initialization : new Object[]{...}
            //if (arrayRef.startsWith("new "))
            if (context.stackSize() > 0)
            {
                OperationView prev = context.peek();
                if (prev instanceof NewArrayView)
                {
                    ((NewArrayView) prev).addInitVariable(val_ci);
                    block.removeCurrentOperation();
                }
            }
        }
        else
        {
            OperationView pushOp = context.pop();
            if (getOpcode() >= 54 && getOpcode() <= 78)
            {
                lvar = block.getLocalVariable(((Pop) operation).getLocalVariableNumber(), pushOp.getPushType());
                
                //if (pushOp.ref != null)
                //{
                //    pushOp.ref.setLocalVariable(lvar);
                //}
                lvar.ensure((int) getStartByte());

                // Check for (condition ? result1 : result2) construction
                CodeItem prevItem = block.getPreviousOperation();
                if (prevItem != null && (prevItem instanceof Else) && ((Else) prevItem).getOperationByStartByte(pushOp.getStartByte()) != null)
                {
                    OperationView pushOp2 = context.peek();
                    CodeItem prev2 = block.getOperationPriorTo(prevItem.getStartByte());
                    if (prev2 != null && (prev2 instanceof IfBlock) && ((IfBlock) prev2).getOperationByStartByte(pushOp2.getStartByte()) != null)
                    {
                        context.pop();
                        IfBlock ifb = (IfBlock) prev2;
                        String scond = ifb.getSourceConditions();
                        view = new Object[]{lvar.getView(), " = ", scond.substring(1, scond.length() - 1), " ? ", pushOp2, " : ", pushOp};
                        block.removePreviousOperation();
                        block.removePreviousOperation();
                        return;
                    }
                }
                view = new Object[]{lvar.getView(), " = ", pushOp};
            }
            if (getOpcode() == 87)  // pop
            {
                if (pushOp instanceof InvokeView)
                {
                    view = new Object[]{pushOp};
                }
            }
            if (getOpcode() == 88)  // pop2
            {
                if (pushOp instanceof InvokeView)
                {
                    view = new Object[]{pushOp};
                }

                String pusht = pushOp.getPushType();
                if (!"double".equals(pusht) && !"long".equals(pusht))    // pop2 form1 support
                {
                    context.pop();   // removing previous second push
                }
            }
        }
    }
}
