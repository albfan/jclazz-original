package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.*;
import ru.andrew.jclazz.core.code.ops.*;

public class GetFieldView extends OperationView
{
    private String objectRef;

    private boolean isInInnerClass = false;

    private boolean isJava1_4dotclass = false;
    private String java1_4dotclass;

    public GetFieldView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);

        isInInnerClass = methodView.getClazzView().isInnerClass();
        if (getOpcode() == 178)   // getstatic
        {
            objectRef = ((GetField) operation).getClassForStaticField();

            // Support Java 1.4 Integer.class representation
            // + static synthetic field: class$java$lang$Integer of java.lang.Class
            // + static synthetic method: class$
            // Code :
            //  getstatic class$java$lang$Integer
            //  ifnonnull XX
            //    ldc "java.lang.Integer"
            //    ...  (class$... field initialization)
            //    ... goto YY
            //  XX: getstatic class$java$lang$Integer
            //  YY: method
            if (objectRef.equals(methodView.getClazz().getThisClassInfo().getFullyQualifiedName()) &&
                    ((GetField) operation).getFieldName().startsWith("class$"))
            {
                isJava1_4dotclass = true;
            }
        }
    }

    public String source()
    {
        if (isJava1_4dotclass)
        {
            return alias(java1_4dotclass) + ".class";
        }

        StringBuffer sb = new StringBuffer();
        if (!("this".equals(objectRef)/* && ClazzSourceView.SUPPRESS_EXCESSED_THIS*/))
        {
            sb.append(isInInnerClass && "this$0".equals(objectRef) ? "" : objectRef + ".");
        }
        sb.append(((GetField) operation).getFieldName());
        return sb.toString();
    }

    public String getPushType()
    {
        return ((GetField) operation).getFieldType();
    }

    public void analyze(Block block)
    {
        if (getOpcode() == 178)   // getstatic
        {
            objectRef = alias(objectRef);
        }
        if (getOpcode() == 180)  // getfield (non static)
        {
            OperationView prev = block.removePriorPushOperation();
            objectRef = prev.source();
            if (prev instanceof CheckCastView)
            {
                objectRef = "(" + objectRef + ")";
            }
        }
        else if (isJava1_4dotclass)   // getstatic
        {
            // Javac
            if (block.getNextOperation() instanceof IfBlock)
            {
                IfBlock ifb = (IfBlock) block.getNextOperation();
                long target = block.getOperationAfter(ifb.getStartByte()).getStartByte();
                java1_4dotclass = ((OperationView) ifb.getFirstOperation()).source();
                // Removing first and last "
                java1_4dotclass = java1_4dotclass.substring(1, java1_4dotclass.length() - 1);
                block.createSubBlock(getStartByte() + 1, target + 1, null);
            }
            // Jikes
            else if (block.getNextOperation() instanceof DupView)
            {
                CodeItem ifItem = block.getOperationAfter(block.getNextOperation().getStartByte());
                if (ifItem instanceof IfBlock)
                {
                    IfBlock ifb = (IfBlock) ifItem;
                    long target = block.getOperationAfter(ifb.getStartByte()).getStartByte();
                    ifb.removeFirstOperation();
                    java1_4dotclass = ((OperationView) ifb.getFirstOperation()).source();
                    // Removing first and last " + [L and ;
                    java1_4dotclass = java1_4dotclass.substring(3, java1_4dotclass.length() - 2);
                    block.createSubBlock(getStartByte() + 1, target, null);
                }
            }
        }
    }
}
