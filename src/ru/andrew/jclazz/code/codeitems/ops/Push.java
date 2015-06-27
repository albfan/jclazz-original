package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.code.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

import java.io.*;

/**
 * Opcodes: 1 - 45<BR>
 * Parameters: <BR>
 * bipush(16): byte(1); sipush(17): byte(2)
 * ldc(18): res(1); ldc_w(19), ldc2_w(20): res(2)
 * xload(21-25): lv_num(1)
 * other: no<BR>
 * Operand stack: => value<BR>
 */
public class Push extends Operation
{
    private String pushValue;
    private String pushType;
    private boolean isClassPushed;

    private int varN;
    private LocalVariable lvar;

    public Push(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        isClassPushed = false;
        // Push constants
        switch (opcode)
        {
            case 1:
                pushValue = "null";
                pushType = "java.lang.Object";
                break;
            case 2:
                pushValue = "-1";
                pushType = "int";
                break;
            case 3:
                pushValue = "0";
                pushType = "int";
                break;
            case 4:
                pushValue = "1";
                pushType = "int";
                break;
            case 5:
                pushValue = "2";
                pushType = "int";
                break;
            case 6:
                pushValue = "3";
                pushType = "int";
                break;
            case 7:
                pushValue = "4";
                pushType = "int";
                break;
            case 8:
                pushValue = "5";
                pushType = "int";
                break;
            case 9:
                pushValue = "0L";
                pushType = "long";
                break;
            case 10:
                pushValue = "1L";
                pushType = "long";
                break;
            case 11:
                pushValue = "0.0f";
                pushType = "float";
                break;
            case 12:
                pushValue = "1.0f";
                pushType = "float";
                break;
            case 13:
                pushValue = "2.0f";
                pushType = "float";
                break;
            case 14:
                pushValue = "0.0";
                pushType = "double";
                break;
            case 15:
                pushValue = "1.0";
                pushType = "double";
                break;
            case 16:
                pushValue = String.valueOf(params[0]);
                pushType = "int";
                break;
            case 17:
                int short_value = (params[0] << 8) | params[1];
                pushValue = String.valueOf(short_value);
                pushType = "int";
                break;
            case 18:
                CP_INFO cp_info_18 = code.getClazz().getConstant_pool()[params[0]];
                if (cp_info_18 instanceof CONSTANT_Class_info)
                {
                    pushValue = ((CONSTANT_Class_info) cp_info_18).getFullyQualifiedName() + ".class";
                    isClassPushed = true;
                }
                else
                {
                    pushValue = cp_info_18.toString();
                }
                pushType = cp_info_18.getType();
                break;
            case 19:
                CP_INFO cp_info_19 = code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
                if (cp_info_19 instanceof CONSTANT_Class_info)
                {
                    pushValue = ((CONSTANT_Class_info) cp_info_19).getFullyQualifiedName() + ".class";
                    isClassPushed = true;
                }
                else
                {
                    pushValue = cp_info_19.toString();
                }
                pushType = cp_info_19.getType();
                break;
            case 20:
                CP_INFO cp_info_20 = code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
                if (cp_info_20 instanceof CONSTANT_Class_info)
                {
                    pushValue = ((CONSTANT_Class_info) cp_info_20).getFullyQualifiedName() + ".class";
                    isClassPushed = true;
                }
                else
                {
                    pushValue = cp_info_20.toString();
                }
                pushType = cp_info_20.getType();
                break;
        }

        // Push variables
        if (opcode >= 21 && opcode <= 45)
        {
            String mnemonic = this.opcode.getMnemonic();
            if (params != null && params.length > 0)
            {
                varN = params[0];
            }
            else
            {
                varN = Integer.valueOf(mnemonic.substring(mnemonic.indexOf('_') + 1)).intValue();
            }
        }
    }

    public String str()
    {
        return lvar == null ? (isClassPushed ? alias(pushValue) : pushValue) : lvar.getName();
    }

    public boolean isPush()
    {
        return true;
    }

    public String getPushType()
    {
        return lvar == null ? pushType : lvar.getType();
    }

    public LocalVariable getLocalVariable()
    {
        return lvar;
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        if (opcode.getOpcode() >= 16 && opcode.getOpcode() <= 25)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic()).append(" ");
            if (opcode.getOpcode() >= 21 && opcode.getOpcode() <= 25)
            {
                sb.append("LV-").append(varN);
            }
            else
            {
                sb.append(pushValue);
            }

            pw.println(sb.toString());
        }
        else
        {
            super.printRaw(pw, indent);
        }
    }

    public void analyze(Block block)
    {
        if (opcode.getOpcode() >= 21 && opcode.getOpcode() <= 45)
        {
            String suffix = "";
            char mn = opcode.getMnemonic().charAt(0);
            if (mn == 'f') suffix = "float";
            else if (mn == 'l') suffix = "long";
            else if (mn == 'd') suffix = "double";
            else if (mn == 'i') suffix = "int";
            else if (mn == 'a') suffix = null;   // TODO what is ref?
            lvar = block.getLocalVariable(varN, suffix);
        }
    }
}
