package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.*;

import java.io.*;

/**
 * Opcodes: 179, 181<BR>
 * Parameters: ref_to_field(2)<BR>
 * Operand stack: putfield: objectref, value => ; putstatic: value => <BR>
 */
public class PutField extends Operation
{
    private String objectRef;
    private String name;
    private String value;

    private boolean isBoolean = false;

    private boolean isInInnerClass = false;

    public PutField(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        isInInnerClass = code.getClazz().isInnerClass();

        CONSTANT_Fieldref_info f_info = (CONSTANT_Fieldref_info) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
        name = f_info.getName();
        isBoolean = "boolean".equalsIgnoreCase(f_info.getFieldDescriptor().getBaseType());
        if (opcode == 179)   // putstatic
        {
            objectRef = f_info.getRefClazz().getFullyQualifiedName();
        }
    }

    public String str()
    {
        StringBuffer sb = new StringBuffer();
        if (!("this".equals(objectRef) && Clazz.SUPPRESS_EXCESSED_THIS))
        {
            sb.append((opcode.getOpcode() == 179 ? alias(objectRef) : objectRef)).append(".");
        }
        sb.append(name).append(" = ");
        if (isBoolean)
        {
            sb.append("0".equals(value) ? "false" : "true");
        }
        else
        {
            sb.append(value);
        }
        return sb.toString();
    }

    public boolean isPush()
    {
        return false;
    }

    public String getPushType()
    {
        return null;
    }

    public void print(PrintWriter pw, String indent)
    {
        // Inner Class support
        if ("this$0".equals(name) && isInInnerClass)
        {
            return;
        }

        // final variables initialization in constructors should not be printed
        if ("this".equals(objectRef) && super.m_info.isInit())
        {
            FIELD_INFO field = super.m_info.getClazz().getFieldByName(name);
            if (field.isFinal() && field.getConstantValue() != null)
            {
                return;
            }
        }

        pw.println(indent + str() + ";");
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic()).append(" ");
        if (opcode.getOpcode() == 179)
        {
            sb.append(objectRef).append(".");
        }
        sb.append(name);
        pw.println(sb.toString());
    }

    public void analyze(Block block)
    {
        Operation pushOp = block.removePriorPushOperation();
        value = pushOp.str();

        if (getOpcode().getOpcode() == 181)  // putfield (non static)
        {
            Operation refOp = block.removePriorPushOperation();
            if (refOp != null)
            {
                objectRef = refOp.str();
            }
            else
            {
                objectRef = "this";
            }
        }
    }
}
