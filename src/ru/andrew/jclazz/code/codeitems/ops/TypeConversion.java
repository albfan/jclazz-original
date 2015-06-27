package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.*;

/**
 * Opcodes: 133 - 147<BR>
 * Parameters: no<BR>
 * Operand stack: value => value<BR>
 */
public class TypeConversion extends Operation
{
    private String pushType;
    private String convValue;

    public TypeConversion(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        String mnemonic = Opcode.getOpcode(opcode).getMnemonic();
        char endChar = mnemonic.charAt(mnemonic.length() - 1);
        switch (endChar)
        {
            case 'd':
                pushType = "double";
                break;
            case 'l':
                pushType = "long";
                break;
            case 'f':
                pushType = "float";
                break;
            case 'i':
                pushType = "int";
                break;
            case 'b':
                pushType = "byte";
                break;
            case 'c':
                pushType = "char";
                break;
            case 's':
                pushType = "short";
                break;
            default:
                throw new OperationException("TypeConversion: invalid opcode");
        }
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return pushType;
    }

    public void analyze(Block block)
    {
        Operation prev = block.removePriorPushOperation();
        convValue = prev.str();
    }

    public String str()
    {
        return "(" + pushType + ") " + convValue;
    }
}
