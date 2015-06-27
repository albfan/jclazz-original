package ru.andrew.jclazz.signature;

import ru.andrew.jclazz.*;

/*
TypeArgument:
   [+-]opt FieldTypeSignature
   *
 */
public class TypeArgument
{
    private char modifier;
    private FieldTypeSignature fieldType;

    private TypeArgument(char modifier, FieldTypeSignature fieldType)
    {
        this.modifier = modifier;
        this.fieldType = fieldType;
    }

    private TypeArgument(char modifier)
    {
        this.modifier = modifier;
    }

    public static TypeArgument parse(StringBuffer sign)
    {
        if (sign.charAt(0) == '*')
        {
            sign.deleteCharAt(0);
            return new TypeArgument('*');
        }

        char mod = 'x';
        if (sign.charAt(0) == '+' || sign.charAt(0) == '-')
        {
            mod = sign.charAt(0);
            sign.deleteCharAt(0); 
        }
        FieldTypeSignature fts = FieldTypeSignature.parse(sign);
        return new TypeArgument(mod, fts);
    }

    public char getModifier()
    {
        return modifier;
    }

    public FieldTypeSignature getFieldType()
    {
        return fieldType;
    }

    public String str(Clazz clazz)
    {
        StringBuffer sb = new StringBuffer();
        switch (modifier)
        {
            case '*':
                sb.append("?");
                break;
            case '-':
                sb.append("? super ");
                break;
            case '+':
                sb.append("? extends ");
                break;
            default:
        }
        if (fieldType != null) sb.append(fieldType.str(clazz));

        return sb.toString();
    }
}
