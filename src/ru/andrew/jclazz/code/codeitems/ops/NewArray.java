package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;
import java.util.*;

/**
 * Opcodes: 188, 189<BR>
 * Parameters: type(1) for newarray(188), class(2) for anewarray(189)<BR>
 * Operand stack: count => arrayref<BR>
 */
public class NewArray extends Operation
{
    public static final int T_BOOLEAN = 4;
    public static final int T_CHAR = 5;
    public static final int T_FLOAT = 6;
    public static final int T_DOUBLE = 7;
    public static final int T_BYTE = 8;
    public static final int T_SHORT = 9;
    public static final int T_INT = 10;
    public static final int T_LONG = 11;

    private String baseType;
    private String classType;
    private String count;

    private List initVariables = new ArrayList();

    public NewArray(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        if (opcode == 188)      // newarray
        {
            switch(params[0])
            {
                case T_BOOLEAN:
                    baseType = "boolean";
                    break;
                case T_CHAR:
                    baseType = "char";
                    break;
                case T_FLOAT:
                    baseType = "float";
                    break;
                case T_DOUBLE:
                    baseType = "double";
                    break;
                case T_BYTE:
                    baseType = "byte";
                    break;
                case T_SHORT:
                    baseType = "short";
                    break;
                case T_INT:
                    baseType = "int";
                    break;
                case T_LONG:
                    baseType = "long";
                    break;
            }
        }
        else if (opcode == 189)
        {
            classType = ((CONSTANT_Class_info) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]]).getFullyQualifiedName();
        }
    }

    public void addInitVariable(String pushVar)
    {
        initVariables.add(pushVar);
    }

    public String str()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("new ").append(baseType != null ? baseType : alias(classType)).append("[");
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

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return (baseType != null ? baseType : classType) + "[]";
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic());
        sb.append(" of ");
        if (baseType != null)
        {
            sb.append(baseType);
        }
        else
        {
            sb.append(classType);
        }
        pw.println(sb.toString());
    }

    public void analyze(Block block)
    {
        Operation prev = block.removePriorPushOperation();
        count = prev.str();
    }
}
