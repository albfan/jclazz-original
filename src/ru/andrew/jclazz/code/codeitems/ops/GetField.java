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

    public GetField(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        isInInnerClass = code.getClazz().isInnerClass();

        CONSTANT_Fieldref_info f_info = (CONSTANT_Fieldref_info) code.getClazz().getConstant_pool()[(params[0]) | params[1]];
        name = f_info.getName();
        fieldType = f_info.getFieldDescriptor().getFQN();
        if (opcode == 178)   // getstatic
        {
            
            objectRef = alias(f_info.getRefClazz().getFullyQualifiedName());
        }
    }

    public String str()
    {
        return (("this".equals(objectRef) && Clazz.SUPPRESS_EXCESSED_THIS) ? "" : (isInInnerClass && "this$0".equals(objectRef) ? "" : objectRef + ".")) + name;
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
    }
}
