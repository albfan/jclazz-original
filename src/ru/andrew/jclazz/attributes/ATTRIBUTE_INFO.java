package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.attributes.annotations.*;
import ru.andrew.jclazz.attributes.verification.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public abstract class ATTRIBUTE_INFO
{
    public static final String SOURCE_FILE = "SourceFile";
    public static final String CONSTANT_VALUE = "ConstantValue";
    public static final String CODE = "Code";
    public static final String EXCEPTIONS = "Exceptions";
    public static final String INNERCLASSES = "InnerClasses";
    public static final String ENCLOSING_METHOD = "EnclosingMethod";
    public static final String SYNTHETIC = "Synthetic";
    public static final String SIGNATURE = "Signature";
    public static final String SOURCE_DEBUG_EXTENSION = "SourceDebugExtension";
    public static final String LINE_NUMBER_TABLE = "LineNumberTable";
    public static final String LOCAL_VARIABLE_TABLE = "LocalVariableTable";
    public static final String LOCAL_VARIABLE_TYPE_TABLE = "LocalVariableTypeTable";
    public static final String DEPRECATED = "Deprecated";
    public static final String RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations";
    public static final String RUNTIME_INVISIBLE_ANNOTATIONS = "RuntimeInvisibleAnnotations";
    public static final String RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS = "RuntimeVisibleParameterAnnotations";
    public static final String RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS = "RuntimeInvisibleParameterAnnotations";
    public static final String ANNOTATION_DEFAULT = "AnnotationDefault";
    public static final String STACK_MAP_TABLE = "StackMapTable";

    protected static final String INDENT = "    ";
    protected static final String ATTR = "@";

    public abstract void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException;

    public static ATTRIBUTE_INFO loadAttribute(ClazzInputStream cis, Clazz clazz, METHOD_INFO method) throws ClazzException, IOException
    {
        ATTRIBUTE_INFO attr_info;
        int attribute_name_index = cis.readU2();
        String name = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[attribute_name_index]).getString();

        if (CONSTANT_VALUE.equals(name))
        {
            attr_info = new ConstantValue();
        }
        else if (CODE.equals(name))
        {
            attr_info = new Code();
            ((Code) attr_info).setMethod(method);
        }
        else if (EXCEPTIONS.equals(name))
        {
            attr_info = new Exceptions();
        }
        else if (INNERCLASSES.equals(name))
        {
            attr_info = new InnerClasses();
        }
        else if (SYNTHETIC.equals(name))
        {
            attr_info = new Synthetic();
        }
        else if (SOURCE_FILE.equals(name))
        {
            attr_info = new SourceFile();
        }
        else if (LINE_NUMBER_TABLE.equals(name))
        {
            attr_info = new LineNumberTable();
        }
        else if (LOCAL_VARIABLE_TABLE.equals(name))
        {
            attr_info = new LocalVariableTable();
        }
        else if (DEPRECATED.equals(name))
        {
            attr_info = new Deprecated();
        }
        else if (ENCLOSING_METHOD.equals(name))
        {
            attr_info = new EnclosingMethod();
        }
        else if (SIGNATURE.equals(name))
        {
            attr_info = new Signature();
        }
        else if (SOURCE_DEBUG_EXTENSION.equals(name))
        {
            attr_info = new SourceDebugExtension();
        }
        else if (LOCAL_VARIABLE_TYPE_TABLE.equals(name))
        {
            attr_info = new LocalVariableTypeTable();
        }
        else if (RUNTIME_VISIBLE_ANNOTATIONS.equals(name))
        {
            attr_info = new RuntimeVisibleAnnotations();
        }
        else if (RUNTIME_INVISIBLE_ANNOTATIONS.equals(name))
        {
            attr_info = new RuntimeInvisibleAnnotations();
        }
        else if (RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS.equals(name))
        {
            attr_info = new RuntimeVisibleParameterAnnotations();
        }
        else if (RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS.equals(name))
        {
            attr_info = new RuntimeInvisibleParameterAnnotations();
        }
        else if (ANNOTATION_DEFAULT.equals(name))
        {
            attr_info = new AnnotationDefault();
        }
        else if (STACK_MAP_TABLE.equals(name))
        {
            attr_info = new StackMapTable();
        }
        else
        {
            attr_info = new GenericAttribute(name);
        }
        attr_info.load(cis, clazz);

        return attr_info;
    }
}
