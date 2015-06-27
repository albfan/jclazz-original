package ru.andrew.jclazz;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.attributes.Deprecated;
import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.signature.*;

import java.io.*;
import java.util.*;

public class MethodInfo
{
    public static final String INIT_METHOD = "<init>";
    public static final String CLASS_INIT_METHOD = "<clinit>";

    public static final int ACC_PUBLIC = 0x0001;
    public static final int ACC_PRIVATE = 0x0002;
    public static final int ACC_PROTECTED = 0x0004;
    public static final int ACC_STATIC = 0x0008;
    public static final int ACC_FINAL = 0x0010;
    public static final int ACC_SYNCHRONIZED = 0x0020;
    public static final int ACC_BRIDGE = 0x0040;
    public static final int ACC_VARARGS = 0x0080;
    public static final int ACC_NATIVE = 0x0100;
    public static final int ACC_ABSTRACT = 0x0400;
    public static final int ACC_STRICT = 0x0800;
    public static final int ACC_SYNTHETIC = 0x1000;

    private int access_flags;
    private CONSTANT_Utf8 name;
    private MethodDescriptor descriptor;
    private CONSTANT_Utf8 descriptorUTF8;
    private AttributeInfo[] attributes;

    private boolean isDeprecated;
    private boolean isSynthetic;
    private Exceptions exc = null;
    private Code code = null;
    private Block topBlock = null;
    private MethodSignature signature;
    private Clazz clazz;

    // Inner Class support
    private boolean isGetFieldForIC = false;
    private String fieldName;

    public void load(ClazzInputStream cis, Clazz clazz) throws ClazzException, IOException
    {
        this.clazz = clazz;

        access_flags = cis.readU2();

        int name_index = cis.readU2();
        name = (CONSTANT_Utf8) clazz.getConstant_pool()[name_index];

        int descriptor_index = cis.readU2();
        descriptorUTF8 = (CONSTANT_Utf8) clazz.getConstant_pool()[descriptor_index];
        descriptor = new MethodDescriptor(descriptorUTF8.getString());

        // Loading attributes

        int attributes_count = cis.readU2();
        attributes = new AttributeInfo[attributes_count];
        for (int i = 0; i < attributes_count; i++)
        {
            AttributeInfo attribute = AttributesLoader.loadAttribute(cis, clazz, this);
            attributes[i] = attribute;
            if (attribute instanceof Deprecated)
            {
                isDeprecated = true;
            }
            else if (attribute instanceof Synthetic)
            {
                isSynthetic = true;
            }
            else if (attribute instanceof Exceptions)
            {
                if (exc != null) throw new ClazzException("Doubling exceptions attribute in method");
                exc = (Exceptions) attribute;
            }
            else if (attribute instanceof Code)
            {
                if (code != null) throw new ClazzException("Doubling code attribute in method");
                code = (Code) attribute;
                topBlock = code.getCodeBlock();
            }
            else if (attribute instanceof Signature)
            {
                signature = new MethodSignature(((Signature) attribute).getSignature(), this);
            }
            else
            {
                System.out.println("METHOD INFO : attribute : " + attribute.getClass() + ", " + attribute);
            }
        }

        // Fake top block for local variables
        if (topBlock == null)
        {
            topBlock = new Block(null, null, this);
        }

        // Initializing local variables, passed as params to this method
        int addition = 0;
        if (!isStatic())
        {
            topBlock.getLocalVariable(0, clazz.getThisClassInfo().getFullyQualifiedName(), "this");
            addition = 1;
        }
        if (!CLASS_INIT_METHOD.equals(name.getString()))
        {
            List params = descriptor.getParams();
            for (int i = 0; i < params.size(); i++)
            {
                FieldDescriptor fd = (FieldDescriptor) params.get(i);
                String type = fd.getClazz();
                if (type == null) type = fd.getBaseType();
                topBlock.getLocalVariable(i + addition, type);
            }
        }

        // InnerClass support
        if (isSynthetic() && isStatic() && getName().startsWith("access$"))
        {
            List syn_pars = descriptor.getParams();
            List ops = getCodeBlock() != null ? getCodeBlock().getCodeBlock().getOperations() : null;
            if (syn_pars.size() == 1 && ((FieldDescriptor) syn_pars.get(0)).getFQN().equals(clazz.getThisClassInfo().getFullyQualifiedName()) &&
                    ops != null && ops.size() == 3)
            {
                // Standard bytecode for synthetic Inner Class support method:
                // 0 aload_0
                // 1 getfield XXX
                // 4 areturn
                int op1 = ((Operation) ops.get(0)).getOpcode().getOpcode();
                int op2 = ((Operation) ops.get(1)).getOpcode().getOpcode();
                int op3 = ((Operation) ops.get(2)).getOpcode().getOpcode();
                if (op1 == 42 && op2 == 180 && op3 >= 172 && op3 <= 176)
                {
                    fieldName = ((GetField) ops.get(1)).getFieldName();
                    isGetFieldForIC = true;
                }
            }
        }
    }

    public void store(ClazzOutputStream cos) throws IOException
    {
        cos.writeU2(access_flags);
        cos.writeU2(name.getIndex());
        cos.writeU2(descriptorUTF8.getIndex());
        for (int i = 0; i < attributes.length; i++)
        {
            attributes[i].store(cos);
        }
    }

    public int getAccessFlags()
    {
        return access_flags;
    }

    public String getName()
    {
        return name.getString();
    }

    public MethodDescriptor getDescriptor()
    {
        return descriptor;
    }

    public MethodSignature getSignature()
    {
        return signature;
    }

    public Clazz getClazz()
    {
        return clazz;
    }

    public boolean isDeprecated()
    {
        return isDeprecated;
    }

    public Exceptions getExceptions()
    {
        return exc;
    }

    public Code getCodeBlock()
    {
        return code;
    }

    public Block getTopBlock()
    {
        return topBlock;
    }

    public AttributeInfo[] getAttributes()
    {
        return attributes;
    }

    public boolean isInit()
    {
        return INIT_METHOD.equals(name.getString());
    }

    // Inner Class support

    public String getFieldNameForIC()
    {
        return fieldName;
    }

    public boolean isGetFieldForIC()
    {
        return isGetFieldForIC;
    }

    // Access checks

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

    public boolean isSynchronized()
    {
        return (access_flags & ACC_SYNCHRONIZED) > 0;
    }

    public boolean isNative()
    {
        return (access_flags & ACC_NATIVE) > 0;
    }

    public boolean isAbstract()
    {
        return (access_flags & ACC_ABSTRACT) > 0;
    }

    public boolean isStrictFP()
    {
        return (access_flags & ACC_STRICT) > 0;
    }

    public boolean isSynthetic()
    {
        return ((access_flags & ACC_SYNTHETIC) > 0) || isSynthetic;
    }

    public boolean isBridge()
    {
        return (access_flags & ACC_BRIDGE) > 0;
    }

    public boolean isVarargs()
    {
        return (access_flags & ACC_VARARGS) > 0;
    }

    // Local variable naming support

    public static final int VAR_N_LOWER = 1;
    public static final int VAR_N_BL = 2;
    public static int VAR_NAMING = VAR_N_LOWER;
    private HashMap lvarNames = new HashMap();

    public String getLVName(String ftype)
    {
        // Removing full package path
        int ind0 = ftype.lastIndexOf('.');
        String type = ind0 != -1 ? ftype.substring(ind0 + 1) : ftype;

        String m_name;
        switch (VAR_NAMING)
        {
            case VAR_N_LOWER:
                m_name = type.toLowerCase();
                break;
            case VAR_N_BL:
                m_name = "";
                String low_name = type.toLowerCase();
                for (int j = 0; j < type.length(); j++)
                {
                    if (type.charAt(j) != low_name.charAt(j)) m_name = m_name + low_name.charAt(j);
                }
                if ("".equals(m_name)) m_name = type.toLowerCase();
                break;
            default:
                m_name = type;
        }

        // Array variables
        while (m_name.indexOf('[') != -1)
        {
            int ind = m_name.indexOf('[');
            m_name = m_name.substring(0, ind) + "s" + m_name.substring(ind + 2);
        }

        String str = (String) lvarNames.get(m_name);
        if (str == null)
        {
            str = m_name + "_1";
        }
        else
        {
            str = m_name + "_" + (Integer.valueOf(str.substring(str.indexOf('_') + 1)).intValue() + 1);
        }
        lvarNames.put(m_name, str);

        return str;
    }
}
