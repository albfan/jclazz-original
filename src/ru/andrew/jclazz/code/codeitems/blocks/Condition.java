package ru.andrew.jclazz.code.codeitems.blocks;

import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.*;

import java.util.*;
import java.io.*;

public class Condition extends Block
{
    private If ifOp;

    private String var1;
    private String var2;
    private String operation;
    private boolean isVar1Boolean;

    private boolean needReverseOperation = true;
    private static final Map reversedOps;

    static
    {
        reversedOps = new HashMap(6);
        reversedOps.put("!=", "==");
        reversedOps.put("==", "!=");
        reversedOps.put(">=", "<");
        reversedOps.put("<", ">=");
        reversedOps.put("<=", ">");
        reversedOps.put(">", "<=");
    }

    public void setNeedReverseOperation(boolean needReverseOperation)
    {
        this.needReverseOperation = needReverseOperation;
    }

    public If getIfOperation()
    {
        return ifOp;
    }

    public Condition(If ifOp, Block parent, List ops)
    {
        super(parent, ops != null ? ops : new ArrayList());
        this.ifOp = ifOp;

        switch (ifOp.getOpcode().getOpcode())
        {
            case 153:
                operation = "==";
                break;
            case 159:
                operation = "==";
                break;
            case 154:
                operation = "!=";
                break;
            case 160:
                operation = "!=";
                break;
            case 155:
                operation = "<";
                break;
            case 161:
                operation = "<";
                break;
            case 156:
                operation = ">=";
                break;
            case 162:
                operation = ">=";
                break;
            case 157:
                operation = ">";
                break;
            case 163:
                operation = ">";
                break;
            case 158:
                operation = "<=";
                break;
            case 164:
                operation = "<=";
                break;
            case 198:
                operation = "==";
                break;
            case 199:
                operation = "!=";
                break;
            default:
                throw new OperationException("BrachCondition: unknown opcode");
        }
    }

    public long getStartByte()
    {
        return ifOp.getStartByte();
    }

    public void print(PrintWriter pw, String indent)
    {
    }

    public void printRaw(PrintWriter pw, String indent)
    {
    }

    public void analyze(Block block)
    {
        reset();
        while (hasMoreOperations())
        {
            CodeItem citem = next();
            if (hasMoreOperations()) citem.analyze(this);    // Don't analyze last If
        }

        analyzeIf(block);
    }

    private void analyzeIf(Block blockA)
    {
        Block block = getPriorPushOperation() != null ? this : blockA;
        int opcode = ifOp.getOpcode().getOpcode();

        Operation prev1 = block.removePriorPushOperation();
        var1 = prev1.str();
        isVar1Boolean = "boolean".equals(prev1.getPushType());

        if (opcode == 198 || opcode == 199)
        {
            var2 = "null";
        }
        else if (opcode >= 153 && opcode <= 158)
        {
            var2 = "0";
            if (prev1 instanceof Signum)
            {
                var2 = ((Signum) prev1).getVar2();
                var1 = ((Signum) prev1).getVar1();
            }
        }
        else
        {
            Operation prev2 = block.removePriorPushOperation();
            var2 = prev2.str();
            // TODO what to do with signum
        }
    }

    public String str()
    {
        if (isVar1Boolean && "0".equals(var2))
        {
            String oper = needReverseOperation ? (String) reversedOps.get(operation) : operation;
            if ("==".equals(oper))
            {
                return "!" + var1;
            }
            else if ("!=".equals(oper))
            {
                return var1;
            }
        }
        return var2 + " " + (needReverseOperation ? (String) reversedOps.get(operation) : operation) + " " + var1;
    }

}
