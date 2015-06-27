package ru.andrew.jclazz;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.attributes.Deprecated;
import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.signature.*;

import java.io.*;

public class FIELD_INFO
{
    public boolean DEBUG = false;

    public static final int ACC_PUBLIC = 0x0001;
    public static final int ACC_PRIVATE = 0x0002;
    public static final int ACC_PROTECTED = 0x0004;
    public static final int ACC_STATIC = 0x0008;
    public static final int ACC_FINAL = 0x0010;
    public static final int ACC_VOLATILE = 0x0040;
    public static final int ACC_TRANSIENT = 0x0080;
    public static final int ACC_SYNTHETIC = 0x1000;
    public static final int ACC_ENUM = 0x4000;

    private int access_flags; 
    private String name;
    private FieldDescriptor descriptor;
    private ATTRIBUTE_INFO[] attrs;

    private boolean isDeprecated;
    private boolean isSynthetic;
    private CP_INFO constantValue = null;
    private FieldTypeSignature signature;

    private Clazz clazz;

    public void load(ClazzInputStream cis, Clazz clazz) throws ClazzException, IOException
    {
        this.clazz = clazz;

        access_flags = cis.readU2();

        int name_index = cis.readU2();
        name = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[name_index]).getString();

        int descriptor_index = cis.readU2();
        descriptor = new FieldDescriptor(((CONSTANT_Utf8_info) clazz.getConstant_pool()[descriptor_index]).getString());

        int attributes_count = cis.readU2();
        attrs = new ATTRIBUTE_INFO[attributes_count];
        for (int i = 0; i < attributes_count; i++)
        {
            attrs[i] = ATTRIBUTE_INFO.loadAttribute(cis, clazz, null);
            if (attrs[i] instanceof Deprecated)
            {
                isDeprecated = true;
            }
            else if (attrs[i] instanceof Synthetic)
            {
                isSynthetic = true;
            }
            else if (attrs[i] instanceof ConstantValue)
            {
                constantValue = ((ConstantValue) attrs[i]).getConstant();
            }
            else if (attrs[i] instanceof Signature)
            {
                signature = FieldTypeSignature.parse(new StringBuffer(((Signature) attrs[i]).getSignature()));
            }
            else
            {
                if (isDebug()) System.out.println("FIELD INFO : attribute : " + attrs[i].getClass() + ", " + attrs[i]);
            }
        }
    }

    private boolean isDebug()
    {
        return DEBUG;
    }

    public int getAccessFlags()
    {
        return access_flags;
    }

    public String getName()
    {
        return name;
    }

    public FieldDescriptor getDescriptor()
    {
        return descriptor;
    }

    public ATTRIBUTE_INFO[] getAttributes()
    {
        return attrs;
    }

    public Clazz getClazz()
    {
        return clazz;
    }

    public boolean isDeprecated()
    {
        return isDeprecated;
    }

    public String getConstantValue()
    {
        if (constantValue == null) return null;
        String val = constantValue.toString();
        if ("boolean".equals(descriptor.getFQN()))
        {
            if ("1".equals(val))
            {
                return "true";
            }
            else if ("0".equals(val))
            {
                return "false";
            }
            else
            {
                throw new RuntimeException("FIELD_INFO: boolean type's other than 0-1");
            }
        }
        return val;
    }

    public FieldTypeSignature getSignature()
    {
        return signature;
    }

    // Access flags methods
    public boolean isPublic()
    {
        return (access_flags & ACC_PUBLIC) > 0;
    }

    public boolean isPrivate()
    {
        return (access_flags & ACC_PRIVATE) > 0;
    }

    public boolean isProtected()
    {
        return (access_flags & ACC_PROTECTED) > 0;
    }

    public boolean isStatic()
    {
        return (access_flags & ACC_STATIC) > 0;
    }

    public boolean isFinal()
    {
        return (access_flags & ACC_FINAL) > 0;
    }

    public boolean isVolatile()
    {
        return (access_flags & ACC_VOLATILE) > 0;
    }

    public boolean isTransient()
    {
        return (access_flags & ACC_TRANSIENT) > 0;
    }

    public boolean isSynthetic()
    {
        return ((access_flags & ACC_SYNTHETIC) > 0) || isSynthetic;
    }

    public boolean isEnum()
    {
        return (access_flags & ACC_ENUM) > 0;
    }
}
