package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.*;

import java.io.*;

// TODO the following opcodes should be implemented
// 0         <- nop
// 90 - 95   <- dup[2][_x[1,2]], swap
// 168 - 169 <- jsr, ret
// 201       <- jsr_w
public abstract class Operation implements CodeItem
{
    protected long start_byte;
    protected Opcode opcode;
    protected int params[];
    protected METHOD_INFO m_info;

    public Operation(int opcode, long start_byte, Code code, boolean loadParams)
    {
        this.opcode = Opcode.getOpcode(opcode);
        this.start_byte = start_byte;
        this.m_info = code.getMethod();
        if (loadParams) loadParams(code);
    }

    public Operation(int opcode, long start_byte, Code code)
    {
        this(opcode, start_byte, code, true);
    }

    // Loads only fixed-length opcodes
    protected void loadParams(Code code)
    {
        int params_count = this.opcode.getParamsCount();
        params = new int[params_count];
        for (int i = 0; i < params_count; i++)
        {
            params[i] = code.getNextByte();
        }
    }

    // For Fake Push Operation
    protected Operation(long start_byte)
    {
        this.start_byte = start_byte;
    }

    public Opcode getOpcode()
    {
        return opcode;
    }

    public int[] getParams()
    {
        return params;
    }

    public long getStartByte()
    {
        return start_byte;
    }

    public METHOD_INFO getMethod()
    {
        return m_info;
    }

    public abstract boolean isPush();

    public abstract String getPushType();

    protected String alias(String fqn)
    {
        return m_info.getClazz().importClass(fqn);
    }

    public abstract String str();

    public void print(PrintWriter pw, String indent)
    {
        printRaw(pw, indent);
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic());
        for (int i = 0; i < getParams().length; i++)
        {
            sb.append(" ").append(getParams()[i]);
        }
        pw.println(sb.toString());
    }
}
