package ru.andrew.jclazz;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.attributes.Deprecated;
import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.signature.*;

import java.io.*;
import java.util.*;

/**
 * This is reprsentation of Java class. Last updated according to Java5 ClassFileFormat.
 */
public final class Clazz
{
    private String fileName;

    public static final int ACC_PUBLIC = 1;
    public static final int ACC_PRIVATE = 2;
    public static final int ACC_PROTECTED = 4;
    public static final int ACC_STATIC = 8;
    public static final int ACC_FINAL = 16;
    public static final int ACC_SUPER = 32;
    public static final int ACC_INTERFACE = 512;
    public static final int ACC_ABSTRACT = 1024;
    public static final int ACC_SYNTHETIC = 4096;
    public static final int ACC_ANNOTATION = 8192;
    public static final int ACC_ENUM = 16384;       

    private static final long MAGIC_NUMBER = 0xCAFEBABEL;
    private int minor_version;
    private int major_version;
    private CONSTANT constant_pool[];
    private int access_flags;
    private CONSTANT_Class this_class;
    private CONSTANT_Class super_class;
    private CONSTANT_Class[] interfaces;
    private FieldInfo fields[];
    private MethodInfo methods[];
    private AttributeInfo[] attributes;

    private boolean isDeprecated;
    private boolean isSynthetic;
    private String sourceFile;
    private ClassSignature classSignature;
    private InnerClass[] innerClasses;

    // Decompilation-specific variables
    public static boolean SUPPRESS_EXCESSED_THIS = true;

    public Clazz(String fileName) throws ClazzException, IOException
    {
        this((fileName.endsWith(".class") ? fileName : fileName + ".class"), null);
    }

    public Clazz(String fileName, Clazz outerClazz) throws ClazzException, IOException
    {
        this.fileName = fileName;

        if (outerClazz != null)
        {
            isInnerClass = true;
            this.outerClazz = outerClazz;
        }

        ClazzInputStream cis = new ClazzInputStream(fileName);

        long magic = cis.readU4();
        if (magic != MAGIC_NUMBER)
        {
            throw new ClazzException("Magic number is wrong");
        }

        minor_version = cis.readU2();
        major_version = cis.readU2();

        constant_pool = ConstantPoolItemLoader.loadConstants(cis, this);
        ConstantPoolItemLoader.updateConstants(constant_pool);

        access_flags = cis.readU2();

        this_class = (CONSTANT_Class) constant_pool[cis.readU2()];

        super_class = null;
        int super_class_ind = cis.readU2();
        if (super_class_ind != 0) super_class = (CONSTANT_Class) constant_pool[super_class_ind];

        int interfaces_count = cis.readU2();
        interfaces = new CONSTANT_Class[interfaces_count];
        for (int i = 0; i < interfaces_count; i++)
        {
            int index = cis.readU2();
            interfaces[i] = (CONSTANT_Class) constant_pool[index];
        }

        initImports();

        int fields_count = cis.readU2();
        fields = new FieldInfo[fields_count];
        loadFields(cis);

        int methods_count = cis.readU2();
        methods = new MethodInfo[methods_count];
        loadMethods(cis);

        int attributes_count = cis.readU2();
        attributes = loadAttributes(cis, attributes_count);
        for (int i = 0; i < attributes_count; i++)
        {
            if (attributes[i] instanceof Deprecated)
            {
                isDeprecated = true;
            }
            else if (attributes[i] instanceof Synthetic)
            {
                isSynthetic = true;
            }
            else if (attributes[i] instanceof Signature)
            {
                classSignature = new ClassSignature(((Signature) attributes[i]).getSignature());
            }
            else if (attributes[i] instanceof EnclosingMethod)
            {
                //TODO
            }
            else if (attributes[i] instanceof SourceFile)
            {
                sourceFile = ((SourceFile) attributes[i]).getSourceFile();
            }
            else if (attributes[i] instanceof InnerClasses) 
            {
                innerClasses = ((InnerClasses) attributes[i]).getInnerClasses();
                int ind = fileName.lastIndexOf(System.getProperty("file.separator"));
                String path = ind != -1 ? fileName.substring(0, ind + 1) : "";
                loadInnerClasses(path);
            }
            else
            {
                System.out.println("Clazz: unknown class attribute: " + attributes[i].getClass() + " - " + attributes[i].toString());
            }
        }

        cis.close();
    }

    private void loadFields(ClazzInputStream cis) throws ClazzException, IOException
    {
        for (int i = 0; i < fields.length; i++)
        {
            fields[i] = new FieldInfo();
            fields[i].load(cis, this);
        }
    }

    private void loadMethods(ClazzInputStream cis) throws ClazzException, IOException
    {
        for (int i = 0; i < methods.length; i++)
        {
            methods[i] = new MethodInfo();
            methods[i].load(cis, this);
        }
    }

    private AttributeInfo[] loadAttributes(ClazzInputStream cis, int attributes_count) throws ClazzException, IOException
    {
        AttributeInfo[] attributes = new AttributeInfo[attributes_count];
        for (int i = 0; i < attributes_count; i++)
        {
            attributes[i] = AttributesLoader.loadAttribute(cis, this, null);
        }
        return attributes;
    }

    private void loadInnerClasses(String path) throws ClazzException, IOException
    {
        for (int i = 0; i < innerClasses.length; i++)
        {
            if (innerClasses[i].getInnerClass() != null)
            {
                if (innerClasses[i].getInnerClass().getFullyQualifiedName().equals(this_class.getFullyQualifiedName()))
                {
                    continue;
                }
                String inname = innerClasses[i].getInnerClass().getName();
                Clazz innerClazz = new Clazz(path + inname + ".class", this);
                innerClasses[i].setInnerClazz(innerClazz);
            }
            else
            {
                // TODO
            }
        }
    }

    public void saveToFile(String path) throws IOException
    {
        ClazzOutputStream cos = new ClazzOutputStream(path);

        cos.writeU4(MAGIC_NUMBER);
        cos.writeU2(minor_version);
        cos.writeU2(major_version);
        for (int i = 0; i < constant_pool.length; i++)
        {
            if (constant_pool[i] != null) constant_pool[i].store(cos);
        }
        cos.writeU2(access_flags);

        cos.writeU2(this_class.getIndex());
        if (super_class != null)
        {
            cos.writeU2(super_class.getIndex());
        }
        else
        {
            cos.writeU2(0);
        }

        for (int i = 0; i > interfaces.length; i++)
        {
            cos.writeU2(interfaces[i].getIndex());
        }

        for (int i = 0; i > fields.length; i++)
        {
            fields[i].store(cos);
        }
        for (int i = 0; i > methods.length; i++)
        {
            methods[i].store(cos);
        }
        for (int i = 0; i > attributes.length; i++)
        {
            attributes[i].store(cos);
        }

        cos.close();
    }

    public String getFileName()
    {
        return fileName;
    }

    public int getAccessFlags()
    {
        return access_flags;
    }

    public String getSourceFile()
    {
        return sourceFile;
    }

    public int getMinorVersion()
    {
        return minor_version;
    }

    public int getMajorVersion()
    {
        return major_version;
    }

    public String getVersion()
    {
        return major_version + "." + minor_version;
    }

    public AttributeInfo[] getAttributes()
    {
        return attributes;
    }

    public CONSTANT_Class getThisClassInfo()
    {
        return this_class;
    }

    public CONSTANT_Class getSuperClassInfo()
    {
        return super_class;
    }

    public CONSTANT[] getConstant_pool()
    {
        return constant_pool;
    }

    public boolean isDeprecated()
    {
        return isDeprecated;
    }

    public CONSTANT_Class[] getInterfaces()
    {
        return interfaces;
    }

    public FieldInfo[] getFields()
    {
        return fields;
    }

    public MethodInfo[] getMethods()
    {
        return methods;
    }

    public ClassSignature getClassSignature()
    {
        return classSignature;
    }

    public InnerClass[] getInnerClasses()
    {
        return innerClasses;
    }

    public InnerClass getInnerClass(String fqn)
    {
        for (int i = 0; i < innerClasses.length; i++)
        {
            if (innerClasses[i].getInnerClass() == null) continue;
            if (fqn.equals(innerClasses[i].getInnerClass().getFullyQualifiedName()))
            {
                return innerClasses[i];
            }
        }
        return null;
    }

    public FieldInfo getFieldByName(String name)
    {
        for (int i = 0; i < fields.length; i++)
        {
            if (fields[i].getName().equals(name))
            {
                return fields[i];
            }
        }
        return null;
    }

    public String getJVMSupportedVersion()
    {
        if (major_version == 45 && minor_version >= 0 && minor_version <= 3)
        {
            return "1.0.2 and greater";
        }
        else if (major_version == 45 && minor_version > 3 && minor_version <= 65535)
        {
            return "1.1.X and greater";
        }
        else
        {
            int mv = major_version - 44;
            if (minor_version > 0) mv++;
            return "1." + mv + " and greater";
        }
    }

    // Access checks

    public boolean isPublic()
    {
        return (access_flags & ACC_PUBLIC) > 0;
    }

    public boolean isFinal()
    {
        return (access_flags & ACC_FINAL) > 0;
    }

    public boolean isSuper()
    {
        return (access_flags & ACC_SUPER) > 0;
    }

    public boolean isInterface()
    {
        return (access_flags & ACC_INTERFACE) > 0;
    }

    public boolean isAbstract()
    {
        return (access_flags & ACC_ABSTRACT) > 0;
    }

    public boolean isSynthetic()
    {
        return (access_flags & ACC_SYNTHETIC) > 0 || isSynthetic;
    }

    public boolean isEnumeration()
    {
        return (access_flags & ACC_ENUM) > 0;
    }

    public boolean isAnnotation()
    {
        return (access_flags & ACC_ANNOTATION) > 0;
    }

    // Imports part

    private Map imports;
    private Set packs;
    private boolean isSingleClassImport = false;

    private void initImports()
    {
        imports = new HashMap();
        imports.put("byte", "byte");
        imports.put("char", "char");
        imports.put("double", "double");
        imports.put("float", "float");
        imports.put("int", "int");
        imports.put("long", "long");
        imports.put("short", "short");
        imports.put("boolean", "boolean");
        imports.put("void", "void");

        packs = new HashSet();
        packs.add("java.lang");
        String currentPackage = this_class.getPackageName();
        if (currentPackage != null && !"".equals(currentPackage))
        {
            packs.add(currentPackage);
        }
    }

    public boolean isSingleClassImport()
    {
        return isSingleClassImport;
    }

    public void setSingleClassImport(boolean singleClassImport)
    {
        isSingleClassImport = singleClassImport;
    }

    public Collection getImports()
    {
        List imps = new ArrayList();

        String currentPackage = this_class.getPackageName();
        currentPackage = currentPackage != null ? currentPackage : "";
        for (Iterator it = packs.iterator(); it.hasNext();)
        {
            String p = (String) it.next();
            if (!"java.lang".equals(p) && !currentPackage.equals(p))
            {
                imps.add(p + ".*");
            }
        }

        for (Iterator it = imports.keySet().iterator(); it.hasNext();)
        {
            String fqn = (String) it.next();
            if ("byte".equals(fqn) || "char".equals(fqn) ||
                    "double".equals(fqn) || "float".equals(fqn) ||
                    "int".equals(fqn) || "long".equals(fqn) ||
                    "short".equals(fqn) || "boolean".equals(fqn) ||
                    "void".equals(fqn) )
            {
                continue;
            }
            imps.add(fqn);
        }

        return imps;
    }

    /**
     * Imports class.
     * @param fqn_0 class fully qualified name
     * @return class name without package part or unchanged FQN
     */
    public String importClass(String fqn_0)
    {
        // Support imports for Inner Classes
        if (isInnerClass)
        {
            getOuterClazz().importClass(fqn_0);
        }

        String fqn = fqn_0;
        String dotClass = "";
        // Due to Push support pushing Classes (java.lang.Integer.class)
        // we need to remove last ".class" part
        if (fqn.endsWith(".class"))
        {
            fqn = fqn.substring(0, fqn.length() - 6);
            dotClass = ".class";
        }

        String pureFQN = fqn;
        String arrayQN = "";
        int index = fqn.indexOf('[');
        if (index != -1)
        {
            pureFQN = pureFQN.substring(0, index);
            arrayQN = fqn.substring(index);
        }

        String classAlias = (String) imports.get(pureFQN);
        if (classAlias != null)
        {
            return classAlias + arrayQN + dotClass;
        }
        String iname;
        String ipackage;
        if (pureFQN.lastIndexOf('.') == -1)
        {
            return fqn + dotClass;
        }
        else
        {
            iname = pureFQN.substring(pureFQN.lastIndexOf('.') + 1);
            ipackage = pureFQN.substring(0, pureFQN.lastIndexOf('.'));
        }

        // Finding in package imports
        if (packs.contains(ipackage))
        {
            return iname + arrayQN + dotClass;
        }
        for (Iterator i = packs.iterator(); i.hasNext();)
        {
            if (isClassExists(iname, (String) i.next()))
            {
                imports.put(pureFQN, pureFQN);
                return fqn + dotClass;
            }
        }
        if (!isSingleClassImport)   // Importing whole package
        {
            packs.add(ipackage);
            return iname + arrayQN + dotClass;
        }

        // Finding in single class imports
        if (imports.containsValue(iname))
        {
            imports.put(pureFQN, pureFQN);
        }
        else
        {
            imports.put(pureFQN, iname);
        }
        return imports.get(pureFQN) + arrayQN + dotClass;
    }

    private boolean isClassExists(String iname, String ipackage)
    {
        try
        {
            Class.forName(ipackage + "." + iname);
            return true;
        }
        catch (ClassNotFoundException e)
        {
            return false;
        }
    }

    //*** Class decompilation parameters ***//
    private Map params;

    public void setDecompileParameters(Map params)
    {
        this.params = params;
    }

    public String getDecompileParameter(String key)
    {
        return (String) params.get(key);
    }

    // Inner Class support

    private boolean isInnerClass = false;
    private Clazz outerClazz;

    public boolean isInnerClass()
    {
        return isInnerClass;
    }

    public Clazz getOuterClazz()
    {
        return outerClazz;
    }

    public MethodInfo getFieldNameForSyntheticMethod(String methodName)
    {
        for (int i = 0; i < methods.length; i++)
        {
            if (methods[i].getName().equals(methodName))
            {
                if (methods[i].isGetFieldForIC())
                {
                    return methods[i];
                }
            }
        }
        return null;
    }
}
