package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;

import java.io.*;

/**
 * Opcodes: 132<BR>
 * Parameters: index(1), const(1)<BR>
 * Operand stack: no change<BR>
 */
public class Iinc extends Operation
{
    private int varN;
    private LocalVariable lvar;
    private int incValue;

    public Iinc(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        varN = params[0];
        incValue = params[1];
    }

    // For wide operation
    public Iinc(Wide woper)
    {
        super(132, woper.getStartByte(), woper.getCode(), false);

        this.varN = woper.getLocalVariableNum();
        this.incValue = woper.getIncrementValue();
    }

    public String str()
    {
        return lvar.getName() + (incValue == 1 ? "++" : " += " + incValue);
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
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic());
        sb.append(" LV-").append(varN).append(" by ").append(incValue);
        pw.println(sb.toString());
    }

    public void analyze(Block block)
    {
        lvar = block.getLocalVariable(varN, null);
    }
}
