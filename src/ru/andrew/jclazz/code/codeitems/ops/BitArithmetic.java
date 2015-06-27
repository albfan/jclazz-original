package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.*;

/**
 * Opcodes: 120 - 131<BR>
 * Parameters: no<BR>
 * Operand stack: value1, value2 => result<BR>
 */
public class BitArithmetic extends Operation
{
    private String type;
    private String oper;
    private String value1;
    private String value2;

    public BitArithmetic(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        switch (opcode)
        {
            case 120:
                oper = "<<";
                type = "int";
                break;
            case 121:
                oper = "<<";
                type = "long";
                break;
            case 122:
                oper = ">>";
                type = "int";
                break;
            case 123:
                oper = ">>";
                type = "long";
                break;
            case 124:
                oper = ">>>";
                type = "int";
                break;
            case 125:
                oper = ">>>";
                type = "long";
                break;
            case 126:
                oper = "&";
                type = "int";
                break;
            case 127:
                oper = "&";
                type = "long";
                break;
            case 128:
                oper = "|";
                type = "int";
                break;
            case 129:
                oper = "|";
                type = "long";
                break;
            case 130:
                oper = "^";
                type = "int";
                break;
            case 131:
                oper = "^";
                type = "long";
                break;
            default:
                throw new OperationException("BitArithmetic: unknown opcode");
        }
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return type;
    }

    public void analyze(Block block)
    {
        Operation prev1 = block.removePriorPushOperation();
        value1 = prev1.str();
        Operation prev2 = block.removePriorPushOperation();
        value2 = prev2.str();
    }

    public String str()
    {
        return value2 + " " + oper + " " + value1;
    }
}
