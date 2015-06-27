package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.*;

import java.io.*;

/**
 * Opcodes: 178, 180<BR>
 * Parameters: ref to field (2)<BR>
 * Operand stack: <BR>
 * getstatic: => value<BR>
 * getfield: objectref => value<BR>
 */
public class GetField extends Operation
{
    private String objectRef;
    private String name;
    private String fieldType;

    private boolean isInInnerClass = false;

    private boolean isJava1_4dotclass = false;
    private String java1_4dotclass;

    public GetField(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        isInInnerClass = code.getClazz().isInnerClass();

        CONSTANT_Fieldref f_info = (CONSTANT_Fieldref) code.getClazz().getConstant_pool()[(params[0]) | params[1]];
        name = f_info.getName();
        fieldType = f_info.getFieldDescriptor().getFQN();
        if (opcode == 178)   // getstatic
        {
            objectRef = alias(f_info.getRefClazz().getFullyQualifiedName());

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
            if (f_info.getRefClazz().getFullyQualifiedName().equals(m_info.getClazz().getThisClassInfo().getFullyQualifiedName()) &&
                    name.startsWith("class$"))
            {
                isJava1_4dotclass = true;
            }
        }
    }

    public String str()
    {
        if (isJava1_4dotclass)
        {
            return alias(java1_4dotclass) + ".class";
        }

        StringBuffer sb = new StringBuffer();
        if (!("this".equals(objectRef) && Clazz.SUPPRESS_EXCESSED_THIS))
        {
            sb.append(isInInnerClass && "this$0".equals(objectRef) ? "" : objectRef + ".");
        }
        sb.append(name);
        return sb.toString();
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return fieldType;
    }

    public String getFieldName()
    {
        return name;
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic()).append(" ");
        if (opcode.getOpcode() == 178)
        {
            sb.append(objectRef).append(".");
        }
        sb.append(name);
        pw.println(sb.toString());
    }

    public void analyze(Block block)
    {
        if (getOpcode().getOpcode() == 180)  // getfield (non static)
        {
            Operation prev = block.removePriorPushOperation();
            objectRef = prev.str();
            if (prev instanceof CheckCast)
            {
                objectRef = "(" + objectRef + ")";
            }
        }
        else if (isJava1_4dotclass)   // getstatic
        {
            if (block.getNextOperation() instanceof IfBlock)
            {
                IfBlock ifb = (IfBlock) block.getNextOperation();
                long target = block.getOperationAfter(ifb.getStartByte()).getStartByte();
                java1_4dotclass = ((Push) ifb.getFirstOperation()).str();
                // Removing first and last "
                java1_4dotclass = java1_4dotclass.substring(1, java1_4dotclass.length() - 1);
                block.createSubBlock(start_byte + 1, target + 1, null);
            }
        }
    }
}
