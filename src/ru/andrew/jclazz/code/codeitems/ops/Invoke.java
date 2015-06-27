package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.apps.decomp.*;
import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;
import java.util.*;

/**
 * Opcodes: 182 - 186<BR>
 * Parameters: ref_to_method(2), for opcode 185 additional parameters: count(1), 0<BR>
 * Operand stack: <BR>
 * invokestatic(184): [arg1, arg2, ...] => <BR> 
 * other: objectref, [arg1, arg2, ...] => <BR>
 */
public class Invoke extends Operation
{
    private String objectref;
    private String methodName;
    private List pushedParams;

    private MethodDescriptor md;

    private boolean isSuperMethodInvoke = false;
    private boolean isConstructor = false;
    private boolean isAnonymousConstructor = false;
    private InnerClass anonymousClass;

    // Inner Class support
    private boolean isICField = false;
    private String icFieldName;
    private int paramsAddition = 0;

    public Invoke(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
        pushedParams = new ArrayList();

        if (opcode == 185)  // invokeinterface
        {
            CONSTANT_InterfaceMethodref mi_info = (CONSTANT_InterfaceMethodref) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
            md = mi_info.getMethodDescriptor();
            methodName = mi_info.getName();
        }
        else if (opcode == 186) // invokedynamic
        {
            CONSTANT_NameAndType nat_info = (CONSTANT_NameAndType) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
            methodName = nat_info.getName();
            try
            {
                md = new MethodDescriptor(nat_info.getDescriptor());
            }
            catch (ClazzException e)
            {
                throw new OperationException("Invoke: exception during initialization", e);
            }
        }
        else
        {
            CONSTANT_Methodref m_info = (CONSTANT_Methodref) code.getClazz().getConstant_pool()[(params[0] << 8) | params[1]];
            md = m_info.getMethodDescriptor();
            methodName = m_info.getName();

            if (opcode == 183)
            {
                String refClass = m_info.getRefClazz().getFullyQualifiedName();
                String thisClass = code.getClazz().getThisClassInfo().getFullyQualifiedName();
                if (!thisClass.equals(refClass))
                {
                    isSuperMethodInvoke = true;
                }
            }

            if (opcode == 184)  // invokestatic
            {
                objectref = m_info.getRefClazz().getFullyQualifiedName();

                // Inner Class support
                if (code.getClazz().isInnerClass() &&
                        objectref.equals(code.getClazz().getOuterClazz().getThisClassInfo().getFullyQualifiedName()) &&
                        methodName.startsWith("access$"))
                {
                    MethodInfo m_ic = code.getClazz().getOuterClazz().getFieldNameForSyntheticMethod(methodName);
                    if (m_ic != null && m_ic.isGetFieldForIC())
                    {
                        icFieldName = m_ic.getFieldNameForIC();
                        isICField = true;
                    }
                }
            }
        }
    }

    public MethodDescriptor getMethodDescriptor()
    {
        return md;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public String str()
    {
        if (isICField) return icFieldName;

        StringBuffer sb = new StringBuffer();
        boolean isInit = false;
        if ("<init>".equals(methodName))
        {
            isInit = true;
        }
        String anonymousClassAsString = "";
        if (isConstructor)
        {
            // Anonymous Class support ^
            if (isAnonymousConstructor)
            {
                Clazz ac = anonymousClass.getInnerClazz();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ClassPrinter acPrinter = new ClassPrinter(byteStream);
                //acPrinter.setIndent(); TODO
                acPrinter.setPrintAsAnonymous(true);
                try
                {
                    acPrinter.print(ac, false);
                    anonymousClassAsString = byteStream.toString();
                    acPrinter.close();
                    anonymousClass.setPrinted(true);
                }
                catch (IOException e)
                {
                    throw new OperationException("Error occured while printing anonymous class", e);
                }

                objectref = alias(anonymousClass.getAnonymousSuperClass().getFullyQualifiedName());
            }
            // Anonymous Class support v

            sb.append("new ").append(objectref);
        }
        else
        {
            if (isSuperMethodInvoke)
            {
                if (isInit)
                {
                    sb.append("super");
                }
                else
                {
                    sb.append("super.").append(methodName);
                }
            }
            else
            {
                if (isInit)
                {
                    sb.append("this");
                }
                else
                {
                    if (!("this".equals(objectref) && Clazz.SUPPRESS_EXCESSED_THIS))
                    {
                        sb.append((opcode.getOpcode() == 184 ? alias(objectref) : objectref)).append(".");
                    }
                    sb.append(methodName);
                }
            }
        }
        sb.append("(");

        if (md.getParams().size() > 0)
        {
            if ((pushedParams == null) || (pushedParams.size() + paramsAddition != md.getParams().size()))
            {
                throw new OperationException("Invoke: invalid parameters");
            }
            for (int i =  pushedParams.size() - 1; i >= 0; i--)
            {
                if (i != pushedParams.size() - 1) sb.append(", ");
                Operation pushOp = (Operation) pushedParams.get(i);

                if ("boolean".equals(((FieldDescriptor) md.getParams().get(pushedParams.size() + paramsAddition - i - 1)).getBaseType()))
                {
                    if ("1".equals(pushOp.str()))
                    {
                        sb.append("true");
                    }
                    else
                    {
                        sb.append("false");
                    }
                }
                else
                {
                    sb.append(pushOp.str());
                }
            }
        }

        sb.append(")");

        if (isAnonymousConstructor)
        {
            sb.append(anonymousClassAsString);
        }

        return sb.toString();
    }

    public boolean isPush()
    {
        return !"void".equals(md.getReturnType().getBaseType()) || isConstructor;
    }

    public String getPushType()
    {
        if ("<init>".equals(methodName))
        {
            return objectref;
        }
        return md.getReturnType().getFQN();
    }

    public void print(PrintWriter pw, String indent)
    {
        String str = str();
        if ("this()".equals(str) || "super()".equals(str)) return;
        pw.println(indent + str + ";");
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic()).append(" ");
        if (opcode.getOpcode() == 184)
        {
            sb.append(objectref).append(".");
        }
        sb.append(methodName);
        pw.println(sb.toString());
    }

    public void analyze(Block block)
    {
        List params = getMethodDescriptor().getParams();
        if (params != null && params.size() > 0)
        {
            for (int ip = 0; ip < params.size(); ip++)
            {
                Operation pushOp = block.removePriorPushOperation();
                if (pushOp == null)
                {
                    throw new RuntimeException("Not enough pushs for invoke operation");
                }
                pushedParams.add(pushOp);
            }
        }

        if (opcode.getOpcode() != 184)  // NOT invokestatic
        {                                                                       
            Operation refOp = block.removePriorPushOperation();
            if ((refOp instanceof New) && ("<init>".equals(methodName)))    // new + init
            {
                isConstructor = true;

                // Inner Class support
                if (((New) refOp).isICConstructor() && pushedParams.size() > 0)
                {
                    paramsAddition = 1;
                    pushedParams.remove(pushedParams.size() - 1);
                }
                if (((New) refOp).isACConstructor())
                {
                    isAnonymousConstructor = true;
                    anonymousClass = ((New) refOp).getAnonymousClass();
                }
            }
            int refOpOpcode = refOp.getOpcode().getOpcode();
            if (refOp instanceof Push && refOpOpcode >= 21 && refOpOpcode <= 45) // Push variables
            {
                objectref = ((Push) refOp).getLocalVariable().getName();
            }
            else
            {
                objectref = refOp.str();
                if (refOp instanceof CheckCast)
                {
                    objectref = "(" + objectref + ")";
                }
            }
            if ((isSuperMethodInvoke) && "this".equals(objectref))
            {
                objectref = "super";
            }
        }
    }
}
