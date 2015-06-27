package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.code.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;
import java.util.*;

public class Code extends ATTRIBUTE_INFO
{
    public class ExceptionTable
    {
        public int start_pc;
        public int end_pc;
        public int handler_pc;
        public CONSTANT_Class_info catch_type;  
    }

    private Clazz clazz;
    private METHOD_INFO method;

    private int max_stack;
    private int max_locals;

    private Block codeBlock;
    private ExceptionTable[] exception_table;
    private ATTRIBUTE_INFO[] attributes;

    private LineNumberTable lnTable = null;
    private LocalVariableTable lvTable = null;

    // Temporary variables
    private long code_length;
    private int[][] code;

    public void setMethod(METHOD_INFO method)
    {
        this.method = method;
    }

    public METHOD_INFO getMethod()
    {
        return method;
    }

    public void load(ClazzInputStream cis, Clazz clazz) throws ClazzException, IOException
    {
        this.clazz = clazz;

        cis.readU4();   // attribute length
        max_stack = cis.readU2();
        max_locals = cis.readU2();
        
        code_length = cis.readU4();
        int H;
        int L;
        if (code_length <= Integer.MAX_VALUE)
        {
            H = 1;
            L = (int) code_length;
            code = new int[H][L];
        }
        else
        {
            H = 1 + (int) (code_length / Integer.MAX_VALUE);
            L = (int) (code_length % Integer.MAX_VALUE);
            code = new int[H][Integer.MAX_VALUE];
        }
        for (int j = 0; j < H; j++)
        {
            for (int i = 0; (j == H - 1) ? (i < L) : (i < Integer.MAX_VALUE); i++)
            {
                code[j][i] = cis.readU1();
            }
        }

        int exception_table_length = cis.readU2();
        exception_table = new ExceptionTable[exception_table_length];
        for (int i = 0; i < exception_table_length; i++)
        {
            exception_table[i] = new ExceptionTable();
            exception_table[i].start_pc = cis.readU2();
            exception_table[i].end_pc = cis.readU2();
            exception_table[i].handler_pc = cis.readU2();
            int catch_type_index = cis.readU2();
            if (catch_type_index == 0)
            {
                exception_table[i].catch_type = null;
            }
            else
            {
                exception_table[i].catch_type = (CONSTANT_Class_info) clazz.getConstant_pool()[catch_type_index];
            }
        }

        int attributes_count = cis.readU2();
        attributes = new ATTRIBUTE_INFO[attributes_count];
        for (int i = 0; i < attributes_count; i++)
        {
            attributes[i] = ATTRIBUTE_INFO.loadAttribute(cis, clazz, method);
            if (attributes[i] instanceof LineNumberTable)
            {
                lnTable = (LineNumberTable) attributes[i];
            }
            else if (attributes[i] instanceof LocalVariableTable)
            {
                lvTable = (LocalVariableTable) attributes[i];
            }
        }

        // Basic code transformation
        ArrayList ops = new ArrayList();

        OperationFactory oFactory = OperationFactory.getInstance();
        reset();
        while (!eof())
        {
            long position = getCurrentPosition();
            int opcode = getNextByte();
            ops.add(oFactory.createOperation(opcode, position, this));
        }

        codeBlock = new Block(ops, clazz, method);
    }

    public Block getCodeBlock()
    {
        return codeBlock;
    }

    public Clazz getClazz()
    {
        return clazz;
    }

    public ExceptionTable[] getExceptionTable()
    {
        return exception_table;
    }

    public int getMaxStack()
    {
        return max_stack;
    }

    public int getMaxLocals()
    {
        return max_locals;
    }

    public ATTRIBUTE_INFO[] getAttributes()
    {
        return attributes;
    }

    public LineNumberTable getLineNumberTable()
    {
        return lnTable;
    }

    public LocalVariableTable getLocalVariableTable()
    {
        return lvTable;
    }

    // Raw Code Reader
    
    private int h = 0;
    private int l = 0;

    public boolean eof()
    {
        return getCurrentPosition() == code_length;
    }

    public void reset()
    {
        h = 0;
        l = 0;
    }

    public int getNextByte()
    {
        if (eof()) throw new RuntimeException("End of code has reached");
        int retVal = code[h][l];
        if (l == Integer.MAX_VALUE - 1)
        {
            h++;
            l = 0;
        }
        else
        {
            l++;
        }
        return retVal;
    }

    public void skipBytes(int count)
    {
        for (int i = 0; i < count; i++)
        {
            getNextByte();
        }
    }

    public long getCurrentPosition()
    {
        return (long) h * (long) Integer.MAX_VALUE + (long) l;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("Code: ...");
        return sb.toString();
    }
}
