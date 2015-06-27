package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.attributes.verification.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.code.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;
import java.util.*;

public class Code extends AttributeInfo
{
    public class ExceptionTable
    {
        public int start_pc;
        public int end_pc;
        public int handler_pc;
        public CONSTANT_Class catch_type;
    }

    private MethodInfo method;

    private int max_stack;
    private int max_locals;

    private Block codeBlock;
    private ExceptionTable[] exception_table;
    private AttributeInfo[] attributes;

    private LineNumberTable lnTable = null;
    private LocalVariableTable lvTable = null;
    private StackMapTable smTable = null;

    // Temporary variables
    private int[] code;
    private int l;

    public Code(CONSTANT_Utf8 attributeName, Clazz clazz, MethodInfo method)
    {
        super(attributeName, clazz);

        this.method = method;
    }

    public MethodInfo getMethod()
    {
        return method;
    }

    public void load(ClazzInputStream cis) throws ClazzException, IOException
    {
        attributeLength = (int) cis.readU4();
        max_stack = cis.readU2();
        max_locals = cis.readU2();
        
        int code_length = (int) cis.readU4();
        code = new int[code_length];
        for (int j = 0; j < code_length; j++)
        {
            code[j] = cis.readU1();
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
                exception_table[i].catch_type = (CONSTANT_Class) clazz.getConstant_pool()[catch_type_index];
            }
        }

        int attributes_count = cis.readU2();
        attributes = new AttributeInfo[attributes_count];
        for (int i = 0; i < attributes_count; i++)
        {
            attributes[i] = AttributesLoader.loadAttribute(cis, clazz, method);
            if (attributes[i] instanceof LineNumberTable)
            {
                lnTable = (LineNumberTable) attributes[i];
            }
            else if (attributes[i] instanceof LocalVariableTable)
            {
                lvTable = (LocalVariableTable) attributes[i];
            }
            else if (attributes[i] instanceof StackMapTable)
            {
                smTable = (StackMapTable) attributes[i];
            }
        }

        // Basic code transformation
        ArrayList ops = new ArrayList();

        OperationFactory oFactory = OperationFactory.getInstance();
        l = -1;
        while (l < code.length - 1)
        {
            ops.add(oFactory.createOperation(getNextByte(), l, this));
        }

        codeBlock = new Block(ops, clazz, method);
    }

    public int getNextByte()
    {
        l++;
        if (l >= code.length) throw new RuntimeException("End of code is reached");
        return code[l];
    }

    public void skipBytes(int count)
    {
        l += count;
    }

    public int getBytecodeLength()
    {
        if (codeBlock == null) return 0;

        int len = 0;
        codeBlock.reset();
        while (codeBlock.hasMoreOperations())
        {
            Operation ci = (Operation) codeBlock.next();
            len += ci.getLength();
        }
        return len;
    }

    public Block getCodeBlock()
    {
        return codeBlock;
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

    public AttributeInfo[] getAttributes()
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

    public StackMapTable getStackMapTable()
    {
        return smTable;
    }

    public String toString()
    {
        return ATTR + "Code...";
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU4(attributeLength);
        cos.writeU2(max_stack);
        cos.writeU2(max_locals);

        cos.writeU4(getBytecodeLength());
        // TODO writing code

        cos.writeU2(exception_table.length);
        for (int i = 0; i < exception_table.length; i++)
        {
            cos.writeU2(exception_table[i].start_pc);
            cos.writeU2(exception_table[i].end_pc);
            cos.writeU2(exception_table[i].handler_pc);
            if (exception_table[i].catch_type == null)
            {
                cos.writeU2(0);
            }
            else
            {
                cos.writeU2(exception_table[i].catch_type.getIndex());
            }
        }

        cos.writeU2(attributes.length);
        for (int i = 0; i < attributes.length; i++)
        {
            attributes[i].store(cos);
        }
    }
}
