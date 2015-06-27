package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

/**
 * Opcodes: 96 - 119<BR>
 * Parameters: no<BR>
 * Operand stack:<BR>
 * xneg: value => result<BR>
 * other: value1, value2 => result<BR>
 */
public class Arithmetic extends Operation
{
    private String value1;
    private String value2;
    private String negValue;
    
    private String operation;
    private String resType;

    public Arithmetic(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        if (opcode >= 96 && opcode <= 99)
        {
            operation = "+";
        }
        else if (opcode >= 100 && opcode <= 103)
        {
            operation = "-";
        }
        else if (opcode >= 104 && opcode <= 107)
        {
            operation = "*";
        }
        else if (opcode >= 108 && opcode <= 111)
        {
            operation = "/";
        }
        else if (opcode >= 112 && opcode <= 115)
        {
            operation = "%";
        }

        int rem = opcode % 4;
        switch (rem)
        {
            case 0:
                resType = "int";
                break;
            case 1:
                resType = "long";
                break;
            case 2:
                resType = "float";
                break;
            case 3:
                resType = "double";
                break;
        }
    }

    public String str()
    {
        if (negValue != null)
        {
            return "-" + negValue;
        }
        else
        {
            return value2 + " " + operation + " " + value1;
        }
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return resType;  
    }

    public void analyze(Block block)
    {
        int opcode = getOpcode().getOpcode();
        if (opcode >= 116 && opcode <= 119) // neg
        {
            Operation prev = block.removePriorPushOperation();
            negValue = prev.str();
        }
        else
        {
            Operation pushVal1 = block.removePriorPushOperation();
            value1 = pushVal1.str();
            Operation pushVal2 = block.removePriorPushOperation();
            value2 = pushVal2.str();
        }
    }
}
