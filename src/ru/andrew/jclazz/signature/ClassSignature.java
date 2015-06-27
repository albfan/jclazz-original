package ru.andrew.jclazz.signature;

import ru.andrew.jclazz.*;

import java.util.*;

/*
ClassSignature:
   <FormalTypeParameter+>opt ClassTypeSignature-superclass ClassTypeSignature*-interfaces
 */
public class ClassSignature
{
    private ClassTypeSignature superClass;
    private ClassTypeSignature[] interfaces;
    private FormalTypeParameter[] typeParameters;

    public ClassSignature(String signature)
    {
        StringBuffer sb = new StringBuffer(signature);
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

        // Loading super class
        if (sb.length() > 0)
        {
            superClass = ClassTypeSignature.parse(sb);
        }
        List intfs = new ArrayList();
        while (sb.length() > 0)
        {
            intfs.add(ClassTypeSignature.parse(sb));
        }
        interfaces = new ClassTypeSignature[intfs.size()];
        intfs.toArray(interfaces);
    }

    public ClassTypeSignature getSuperClass()
    {
        return superClass;
    }

    public ClassTypeSignature[] getInterfaces()
    {
        return interfaces;
    }

    public FormalTypeParameter[] getTypeParameters()
    {
        return typeParameters;
    }

    public String str(Clazz clazz)
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
            sb.append(">");
        }

        if ((superClass != null) && (!"java.lang.Object".equals(superClass.getPackage() + "." + superClass.getClassType().getName())))
        {
            sb.append(" extends ").append(superClass.str(clazz));
        }

        if (interfaces.length > 0) sb.append(" implements ");
        for (int i = 0; i < interfaces.length; i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(interfaces[i].str(clazz));
        }

        return sb.toString();
    }
}
