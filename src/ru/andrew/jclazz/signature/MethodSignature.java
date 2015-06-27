package ru.andrew.jclazz.signature;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.code.*;

import java.util.*;

/*
MethodTypeSignature:
   <FormalTypeParameter+>opt (TypeSignature*) ReturnType ThrowsSignature*
ReturnType:
   TypeSignature
   V
ThrowsSignature:
   ^ClassTypeSignature
   ^T Identifer; - type variable
 */
public class MethodSignature
{
    private METHOD_INFO m_info;

    private FormalTypeParameter[] typeParameters;
    private TypeSignature[] paramTypes;

    private boolean isVoidReturned;
    private TypeSignature retType;

    private ClassTypeSignature[] thrownClasses;
    private String[] thrownVariables;

    public MethodSignature(String signature, METHOD_INFO m_info)
    {
        this.m_info = m_info;

        StringBuffer sb = new StringBuffer(signature);

        // Loading type parameters
        if (signature.charAt(0) == '<')
        {
            sb.deleteCharAt(0);
            List ftps = new ArrayList();
            while (sb.charAt(0) != '>')
            {
                ftps.add(FormalTypeParameter.parse(sb));
            }
            typeParameters = new FormalTypeParameter[ftps.size()];
            ftps.toArray(typeParameters);
            sb.deleteCharAt(0);
        }

        // Loading method parameters
        List pars = new ArrayList();
        sb.deleteCharAt(0); // (
        while (sb.charAt(0) != ')')
        {
            pars.add(TypeSignature.parse(sb));
        }
        paramTypes = new TypeSignature[pars.size()];
        pars.toArray(paramTypes);
        sb.deleteCharAt(0); // )

        // Loading return type
        if (sb.charAt(0) == 'V')
        {
            isVoidReturned = true;
            sb.deleteCharAt(0);
        }
        else
        {
            isVoidReturned = false;
            retType = TypeSignature.parse(sb);
        }

        // Loading throws signatures
        List thClasses = new ArrayList();
        List thVars = new ArrayList();
        while (sb.length() > 0)
        {
            sb.deleteCharAt(0); // ^
            if (sb.charAt(0) == 'T')
            {
                sb.deleteCharAt(0);
                String id = sb.substring(0, sb.indexOf(";"));
                sb.delete(0, sb.indexOf(";"));
                thVars.add(id);
                sb.deleteCharAt(0);
            }
            else
            {
                thClasses.add(ClassTypeSignature.parse(sb));
            }
        }
        thrownClasses = new ClassTypeSignature[thClasses.size()];
        thClasses.toArray(thrownClasses);
        thrownVariables = new String[thVars.size()];
        thVars.toArray(thrownVariables);
    }

    public FormalTypeParameter[] getTypeParameters()
    {
        return typeParameters;
    }

    public TypeSignature[] getParamTypes()
    {
        return paramTypes;
    }

    public boolean isVoidReturned()
    {
        return isVoidReturned;
    }

    public TypeSignature getReturnType()
    {
        return retType;
    }

    public ClassTypeSignature[] getThrownClassType()
    {
        return thrownClasses;
    }

    public String[] getThrownVariables()
    {
        return thrownVariables;
    }

    public boolean hasThrowClause()
    {
        return (thrownClasses.length > 0) || (thrownVariables.length > 0);
    }

    public String strPreName(Clazz clazz)
    {
        StringBuffer sb = new StringBuffer();
        if (typeParameters != null && typeParameters.length > 0)
        {
            sb.append("<");
            for (int i = 0; i < typeParameters.length; i++)
            {
                if (i > 0) sb.append(", ");
                sb.append(typeParameters[i].str(clazz));
            }
            sb.append("> ");
        }
        if (isVoidReturned)
        {
            sb.append("void ");
        }
        else
        {
            sb.append(retType.str(clazz)).append(" ");
        }
        return sb.toString();
    }

    public String strPostName(Clazz clazz)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        int addition = m_info.isStatic() ? 0 : 1;
        for (int i = 0; i < paramTypes.length - 1; i++)
        {
            LocalVariable lv = m_info.getCodeBlock().getCodeBlock().getLocalVariable(i + addition, null);
            sb.append(paramTypes[i].str(clazz)).append(" ").append(lv.getName()).append(", ");
        }
        if (paramTypes.length > 0)
        {
            LocalVariable lv = m_info.getCodeBlock().getCodeBlock().getLocalVariable(paramTypes.length - 1 + addition, null);
            String lp = paramTypes[paramTypes.length - 1].str(clazz);
            if (!m_info.isVarargs())
            {
                sb.append(lp);
            }
            else
            {
                lp = lp.substring(0, lp.length() - 2);
                sb.append(lp + "...");
            }
            sb.append(" ").append(lv.getName());
        }
        sb.append(")");
        return sb.toString();
    }

    public String strThrows(Clazz clazz)
    {
        StringBuffer sb = new StringBuffer();
        if (thrownClasses.length > 0 || thrownVariables.length > 0) sb.append(" throws ");
        for (int i = 0; i < thrownClasses.length; i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(thrownClasses[i].str(clazz));
        }
        for (int i = 0; i < thrownVariables.length; i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(thrownVariables[i]);
        }
        return sb.toString();
    }
}
