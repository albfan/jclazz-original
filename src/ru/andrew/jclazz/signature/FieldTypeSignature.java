package ru.andrew.jclazz.signature;

import ru.andrew.jclazz.*;

/*
FieldTypeSignature:
   ClassTypeSignature
   [TypeSignature - array
   T Identifer ; - type variable
 */
public class FieldTypeSignature
{
    private ClassTypeSignature classType;
    private TypeSignature array;
    private String variable;

    private FieldTypeSignature(ClassTypeSignature classType)
    {
        this.classType = classType;
    }

    private FieldTypeSignature(TypeSignature array)
    {
        this.array = array;
    }

    private FieldTypeSignature(String variable)
    {
        this.variable = variable;
    }

    public static FieldTypeSignature parse(StringBuffer sign)
    {
        char prefix = sign.charAt(0);
        switch (prefix)
        {
            case 'L':
                return new FieldTypeSignature(ClassTypeSignature.parse(sign));
            case '[':
                sign.deleteCharAt(0);
                return new FieldTypeSignature(TypeSignature.parse(sign)); 
            case 'T':
                sign.deleteCharAt(0);
                String id = sign.substring(0, sign.indexOf(";"));
                sign.delete(0, sign.indexOf(";"));
                sign.deleteCharAt(0);
                return new FieldTypeSignature(id);
        }
        throw new RuntimeException("FieldTypeSignature: invalid signature prefix = " + prefix);
    }

    public ClassTypeSignature getClassType()
    {
        return classType;
    }

    public TypeSignature getArrayType()
    {
        return array;
    }

    public String getVariable()
    {
        return variable;
    }

    public String str(Clazz clazz)
    {
        if (classType != null)
        {
            return classType.str(clazz);
        }
        else if (array != null)
        {
            return array.str(clazz) + "[]";
        }
        else
        {
            return variable;
        }
    }
}
