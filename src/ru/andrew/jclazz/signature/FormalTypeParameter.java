package ru.andrew.jclazz.signature;

import ru.andrew.jclazz.*;

import java.util.*;

/*
FormalTypeParameter:
   Identifier :(FieldTypeSignature)opt-classbound (:FieldTypeSignature)*-interfacebounds
 */
public class FormalTypeParameter
{
    private String identifier;
    private FieldTypeSignature classBound;
    private FieldTypeSignature[] intfBounds;

    private FormalTypeParameter(String identifier, FieldTypeSignature classBound, List intfBounds)
    {
        this.identifier = identifier;
        this.classBound = classBound;
        this.intfBounds = new FieldTypeSignature[intfBounds.size()];
        intfBounds.toArray(this.intfBounds);
    }

    public static FormalTypeParameter parse(StringBuffer sign)
    {
        int ind = sign.indexOf(":");
        String id = sign.substring(0, ind);
        sign.delete(0, ind);

        FieldTypeSignature clBound = null;
        sign.deleteCharAt(0);
        if (sign.length() > 0 && sign.charAt(0) != ':') // Loading classBound
        {
            clBound = FieldTypeSignature.parse(sign);
        }

        // Loading interface bounds
        List intBounds = new ArrayList();
        while (sign.charAt(0) == ':')
        {
            sign.deleteCharAt(0);
            intBounds.add(FieldTypeSignature.parse(sign));
        }

        return new FormalTypeParameter(id, clBound, intBounds);
    }

    public String getName()
    {
        return identifier;
    }

    public FieldTypeSignature getClassBound()
    {
        return classBound;
    }

    public FieldTypeSignature[] getInterfaceBounds()
    {
        return intfBounds;
    }

    public String str(Clazz clazz)
    {
        StringBuffer sb = new StringBuffer(identifier);

        ClassTypeSignature sup = classBound.getClassType();
        boolean exists = false;
        if ((sup != null) && (!"java.lang.Object".equals(sup.getPackage() + "." + sup.getClassType().getName())))
        {
            sb.append(" extends ").append(classBound.str(clazz));
            exists = true;
        }

        for (int i = 0; i < intfBounds.length; i++)
        {
            if (!exists)
            {
                sb.append(" extends ").append(intfBounds[i].str(clazz));
            }
            else
            {
                sb.append("&").append(intfBounds[i].str(clazz));
                exists = true;
            }
        }

        return sb.toString();
    }
}
