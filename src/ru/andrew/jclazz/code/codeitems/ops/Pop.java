package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

import java.io.*;

/**
 * Opcodes: 54 - 88<BR>
 * Parameters: local_var(1) for xstore(54-58), no for others<BR>
 * Operand stack: <BR>
 * xstore(54-58), xstore_Y(59-78), pop(87): value => <BR>
 * xastore(79-86): arrayref, index, value => <BR>
 * pop2(88): 1) type1: value1, value2 => 2) type2: value => <BR>  
 */
public class Pop extends Operation
{
    private String pushedValue;
    private int varN;
    private LocalVariable lvar;

    private String arrayRef;
    private String index;

    public Pop(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        // Pop variables
        if (opcode >= 54 && opcode <= 78)
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
        else if (opcode >= 79 && opcode <= 86)
        {
            pushedValue = "Xastore";
        }
        else if (opcode >= 87 && opcode <= 88)
        {
        }
    }

    public int getLocalVariableNumber()
    {
        return varN;
    }

    public LocalVariable getLocalVariable()
    {
        return lvar;
    }

    public String str()
    {
        if (lvar == null && arrayRef == null && index == null)
        {
            return pushedValue; 
        }

        return (lvar != null ? (!lvar.isPrinted() ? alias(lvar.getType()) + " " : "") + lvar.getName() : arrayRef + "[" + index + "]") + " = " + pushedValue;
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
        pw.println(indent + str() + ";");
        if (lvar != null) lvar.setPrinted(true);
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        if (opcode.getOpcode() >= 54 && opcode.getOpcode() <= 58)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic()).append(" LV-").append(varN);
            pw.println(sb.toString());                         
        }
        else
        {
            super.printRaw(pw, indent);
        }
    }

    public void analyze(Block block)
    {
        if (getOpcode().getOpcode() >= 79 && getOpcode().getOpcode() <= 86)
        {
            Operation val_ci = block.removePriorPushOperation();
            pushedValue = val_ci.str();

            Operation ind_ci = block.removePriorPushOperation();
            index = ind_ci.str();

            Operation arr_ci = block.removePriorPushOperation();
            arrayRef = arr_ci.str();

            // Support array initialization : new Object[]{...}
            if (arrayRef.startsWith("new "))
            {
                Operation prev = block.getPriorPushOperation();
                if (prev instanceof NewArray)
                {
                    ((NewArray) prev).addInitVariable(pushedValue);
                    block.removeCurrentOperation();
                }
            }
        }
        else
        {
            Operation pushOp = block.removePriorPushOperation();
            if (opcode.getOpcode() >= 54 && opcode.getOpcode() <= 78)
            {
                lvar = block.getLocalVariable(varN, pushOp.getPushType());
            }
            pushedValue = pushOp.str();
            if (opcode.getOpcode() == 88)
            {
                String pusht = pushOp.getPushType();
                if (!"double".equals(pusht) && !"long".equals(pusht))    // pop2 form1 support
                {
                    block.removePriorPushOperation();   // removing previous second push
                }
            }
        }
    }
}
