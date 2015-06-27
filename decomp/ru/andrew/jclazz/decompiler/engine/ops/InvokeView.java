package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.*;

import java.util.*;

public class InvokeView extends OperationView
{
    private String objectref;
    private List pushedParams;

    private boolean isConstructor = false;
    private boolean isAnonymousConstructor = false;
    private InnerClass anonymousClass;
    private String anonymousIndent;

    // Inner Class support
    private boolean isICField = false;
    private String icFieldName;
    private int paramsAddition = 0;

    public InvokeView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
        pushedParams = new ArrayList();

        if (getOpcode() == 184)  // invokestatic
        {
            objectref = ((Invoke) operation).getClassForStaticInvoke();

            // Inner Class support
            if (methodView.getClazzView().isInnerClass() &&
                    objectref.equals(methodView.getClazzView().getOuterClazz().getClazz().getThisClassInfo().getFullyQualifiedName()) &&
                    ((Invoke) operation).getMethodName().startsWith("access$"))
            {
                MethodSourceView m_ic = methodView.getClazzView().getOuterClazz().getFieldNameForSyntheticMethod(((Invoke) operation).getMethodName());
                if (m_ic != null && m_ic.isGetFieldForIC())
                {
                    icFieldName = m_ic.getFieldNameForIC();
                    isICField = true;
                }
            }
        }
    }

    public String source()
    {
        if (isICField) return icFieldName;

        StringBuffer sb = new StringBuffer();
        boolean isInit = false;
        if ("<init>".equals(((Invoke) operation).getMethodName()))
        {
            isInit = true;
        }
        String anonymousClassAsString = "";
        if (isConstructor)
        {
            // Anonymous Class support ^
            if (isAnonymousConstructor)
            {
                String inname = anonymousClass.getInnerClass().getName();
                String enclosingClassFileName = methodView.getClazz().getFileName();
                String path = enclosingClassFileName.substring(0, enclosingClassFileName.lastIndexOf(System.getProperty("file.separator")) + 1);
                Clazz innerClazz;
                try
                {
                    innerClazz = new Clazz(path + inname + ".class");
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                AnonymousClazzSourceView csv = new AnonymousClazzSourceView(innerClazz, methodView.getClazzView());
                csv.setPrintAsAnonymous(true);
                //csv.setIndent(anonymousIndent);
                csv.setIndent("    ");
                InnerClassView icv = methodView.getClazzView().getInnerClassView(innerClazz.getThisClassInfo().getFullyQualifiedName());
                icv.setClazzView(csv);
                anonymousClassAsString = csv.getSource();

                objectref = alias(csv.getAnonymousSuperClassFQN());
            }
            // Anonymous Class support v

            sb.append("new ").append(objectref);
        }
        else
        {
            if (((Invoke) operation).isSuperMethodInvoke())
            {
                if (isInit)
                {
                    // Do not print invokation of default constructor
                    if (((Invoke) operation).getMethodDescriptor().getParams().size() > 0)
                    {
                        sb.append("super");
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    sb.append("super.").append(((Invoke) operation).getMethodName());
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
                    if (!("this".equals(objectref) && ClazzSourceView.SUPPRESS_EXCESSED_THIS))
                    {
                        sb.append((getOpcode() == 184 ? alias(objectref) : objectref)).append(".");
                    }
                    sb.append(((Invoke) operation).getMethodName());
                }
            }
        }
        sb.append("(");

        if (((Invoke) operation).getMethodDescriptor().getParams().size() > 0)
        {
            if ((pushedParams == null) || (pushedParams.size() + paramsAddition != ((Invoke) operation).getMethodDescriptor().getParams().size()))
            {
                throw new OperationException("Invoke: invalid parameters");
            }
            for (int i =  pushedParams.size() - 1; i >= 0; i--)
            {
                if (i != pushedParams.size() - 1) sb.append(", ");
                OperationView pushOp = (OperationView) pushedParams.get(i);

                FieldDescriptor argDescriptor = (FieldDescriptor) ((Invoke) operation).getMethodDescriptor().getParams().get(pushedParams.size() + paramsAddition - i - 1);
                if ("boolean".equals(argDescriptor.getBaseType()))
                {
                    if ("1".equals(pushOp.source()))
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
                    // Narrowing Primitive Conversions
                    if (argDescriptor.isBaseType())
                    {
                        if (Block.widePrimitiveConversion(argDescriptor.getBaseType(), pushOp.getPushType()))
                        {
                            sb.append("(").append(argDescriptor.getBaseType()).append(") ");
                        }
                    }
                    sb.append(pushOp.source());
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
        return !"void".equals(((Invoke) operation).getMethodDescriptor().getReturnType().getBaseType()) || isConstructor;
    }

    public String getPushType()
    {
        if ("<init>".equals(((Invoke) operation).getMethodName()))
        {
            return objectref;
        }
        return ((Invoke) operation).getReturnType();
    }

    public void analyze(Block block)
    {
        List params = ((Invoke) operation).getMethodDescriptor().getParams();
        if (params != null && params.size() > 0)
        {
            for (int ip = 0; ip < params.size(); ip++)
            {
                OperationView pushOp = block.removePriorPushOperation();
                if (pushOp == null)
                {
                    throw new RuntimeException("Not enough pushs for invoke operation");
                }
                pushedParams.add(pushOp);
            }
        }

        if (getOpcode() != 184)  // NOT invokestatic
        {                                                                       
            OperationView refOp = block.removePriorPushOperation();
            if ((refOp instanceof NewView) && ("<init>".equals(((Invoke) operation).getMethodName())))    // new + init
            {
                isConstructor = true;

                // Inner Class support
                if (((NewView) refOp).isICConstructor() && pushedParams.size() > 0)
                {
                    paramsAddition = 1;
                    pushedParams.remove(pushedParams.size() - 1);
                }
                if (((NewView) refOp).isACConstructor())
                {
                    isAnonymousConstructor = true;
                    anonymousClass = ((NewView) refOp).getAnonymousClass();
                    anonymousIndent = block.getIndent();
                }
            }
            int refOpOpcode = refOp.getOpcode();
            if (refOp instanceof PushVariableView)
            {
                // TODO
                objectref = ((PushVariableView) refOp).source();
                //objectref = ((PushVariableView) refOp).getLocalVariable().getName((int) getStartByte());
            }
            else
            {
                objectref = refOp.source();
                if (refOp instanceof CheckCastView)
                {
                    objectref = "(" + objectref + ")";
                }
            }
            if ((((Invoke) operation).isSuperMethodInvoke()) && "this".equals(objectref))
            {
                objectref = "super";
            }
        }
    }
}
