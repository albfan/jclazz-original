package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.*;

import java.io.*;

/**
 * Opcodes: 172 - 177<BR>
 * Parameters: no<BR>
 * Operand stack: return(177): no change; others: value => <BR>
 */
public class Return extends Operation
{
    private String retValue;
    private String baseType;

    private boolean isLastInBlock;

    public Return(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);

        FieldDescriptor returnType = code.getMethod().getDescriptor().getReturnType();
        if (METHOD_INFO.INIT_METHOD.equals(code.getMethod().getName()))
        {
            baseType = "<init>";
        }
        else
        {
            baseType = returnType.getBaseType();
            rewriteReturnValue();
        }
    }

    private void rewriteReturnValue()
    {
        if ("boolean".equals(baseType))
        {
            if ("0".equals(retValue))
            {
                retValue = "false";
            }
            else
            {
                retValue = "true";
            }
        }
    }

    public String str()
    {
        if (opcode.getOpcode() == 177)   // Return void
        {
            return "return";
        }
        else
        {
            return "return " + retValue;
        }
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
        if (!isLastInBlock || opcode.getOpcode() != 177)
        {
            pw.println(indent + str() + ";");
        }
    }

    public void analyze(Block block)
    {
        isLastInBlock = !block.hasMoreOperations() && block.getParent() == null;    // TODO bug
        if ("<init>".equals(baseType))
        {
            // Return in constructor
            return;
        }

        if (getOpcode().getOpcode() == 177) return;
        Operation pushOp = block.removePriorPushOperation();
        retValue = pushOp.str();
        rewriteReturnValue();
    }
}
