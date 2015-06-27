package ru.andrew.jclazz.gui;

import ru.andrew.jclazz.*;

import javax.swing.tree.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.CONSTANT;
import ru.andrew.jclazz.core.constants.CONSTANT_Class;

public class ClazzTreeUI
{
    private Clazz clazz;
    private DefaultTreeModel model;

    public ClazzTreeUI(Clazz clazz)
    {
        this.clazz = clazz;
        loadTree();
    }

    public TreeModel getTreeModel()
    {
        return model;
    }

    private void loadTree()
    {
        ClazzTreeNode root = createRootNode();

        AttributeInfo[] attrs = clazz.getAttributes();
        for (int i = 0; i < attrs.length; i++)
        {
            ClazzTreeNode node = createAttribtueNode(attrs[i]);
            root.add(node);
        }

        FieldInfo[] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++)
        {
            ClazzTreeNode node = createFieldNode(fields[i]);
            root.add(node);
        }

        MethodInfo[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            ClazzTreeNode node = createMethodNode(methods[i]);
            root.add(node);
        }

        InnerClass[] ics = clazz.getInnerClasses();
        if (ics != null)
        {
            for (int i = 0; i < ics.length; i++)
            {
                ClazzTreeNode node = createInnerClassNode(ics[i]);
                root.add(node);
            }
        }

        root.add(createConstantPoolNode());
        
        model = new DefaultTreeModel(root);
    }

    private ClazzTreeNode createAttribtueNode(AttributeInfo attrInfo)
    {
        StringBuffer sb = new StringBuffer("<html>");

        sb.append(attrInfo.toString());

        sb.append("</html>");
        return new ClazzTreeNode(attrInfo.getAttributeName().getString(), attrInfo, sb.toString());
    }

    private ClazzTreeNode createFieldNode(FieldInfo fieldInfo)
    {
        StringBuffer sb = new StringBuffer("<html>");

        sb.append("<b>").append(fieldInfo.getName()).append("</b><br>");
        sb.append("Access flags: ").append(fieldInfo.getAccessFlags()).append("&nbsp;(");
        if (fieldInfo.isDeprecated()) sb.append("deprecated ");
        if (fieldInfo.isEnum()) sb.append("enum ");
        if (fieldInfo.isFinal()) sb.append("final ");
        if (fieldInfo.isPrivate()) sb.append("private ");
        if (fieldInfo.isProtected()) sb.append("protected ");
        if (fieldInfo.isPublic()) sb.append("public ");
        if (fieldInfo.isStatic()) sb.append("static ");
        if (fieldInfo.isSynthetic()) sb.append("synthetic ");
        if (fieldInfo.isTransient()) sb.append("transient ");
        if (fieldInfo.isVolatile()) sb.append("volatile ");
        sb.append(")<br>");

        sb.append("Signature: ").append(fieldInfo.getSignature()).append("<br>");
        sb.append("Descriptor: ").append(fieldInfo.getDescriptor()).append("<br>");
        sb.append("Constant Value: ").append(fieldInfo.getConstantValue()).append("<br>");

        sb.append("</html>");

        ClazzTreeNode root = new ClazzTreeNode(fieldInfo.getName(), fieldInfo, sb.toString());

        AttributeInfo[] attrs = fieldInfo.getAttributes();
        if (attrs != null)
        {
            for (int i = 0; i < attrs.length; i++)
            {
                ClazzTreeNode attrNode = createAttribtueNode(attrs[i]);
                root.add(attrNode);
            }
        }

        return root; 
    }

    private ClazzTreeNode createMethodNode(MethodInfo methodInfo)
    {
        StringBuffer sb = new StringBuffer("<html>");

        sb.append("<b>").append(escape(methodInfo.getName())).append("</b><br>");
        sb.append("Access flags: ").append(methodInfo.getAccessFlags()).append("&nbsp;(");
        if (methodInfo.isAbstract()) sb.append("abstract ");
        if (methodInfo.isBridge()) sb.append("bridge ");
        if (methodInfo.isDeprecated()) sb.append("deprecated ");
        if (methodInfo.isFinal()) sb.append("final ");
        if (methodInfo.isNative()) sb.append("native ");
        if (methodInfo.isPrivate()) sb.append("private ");
        if (methodInfo.isProtected()) sb.append("protected ");
        if (methodInfo.isPublic()) sb.append("public ");
        if (methodInfo.isStatic()) sb.append("static ");
        if (methodInfo.isStrictFP()) sb.append("strictfp ");
        if (methodInfo.isSynchronized()) sb.append("synchronized ");
        if (methodInfo.isSynthetic()) sb.append("synthetic ");
        if (methodInfo.isVarargs()) sb.append("varargs ");
        sb.append(")<br>");

        sb.append("Signature: ").append(methodInfo.getSignature()).append("<br>");
        sb.append("Descriptor: ").append(methodInfo.getDescriptor()).append("<br>");

        if (methodInfo.getExceptions() != null)
        {
            CONSTANT_Class[] exc = methodInfo.getExceptions().getExceptionTable();
            sb.append("Throws: ");
            for (int i = 0; i <exc.length; i++)
            {
                if (i > 0) sb.append(", ");
                sb.append(exc[i].getFullyQualifiedName());
            }
            sb.append("<br>");
        }

        sb.append("</html>");

        ClazzTreeNode root = new ClazzTreeNode(methodInfo.getName(), methodInfo, sb.toString());

        AttributeInfo[] attrs = methodInfo.getAttributes();
        if (attrs != null)
        {
            for (int i = 0; i < attrs.length; i++)
            {
                ClazzTreeNode attrNode = createAttribtueNode(attrs[i]);
                root.add(attrNode);
            }
        }

        return root;
    }

    private ClazzTreeNode createInnerClassNode(InnerClass innerClass)
    {
        StringBuffer sb = new StringBuffer("<html>");

        sb.append(innerClass.toString());

        sb.append("</html>");
        String innerName = innerClass.getInnerClass().getFullyQualifiedName();
        return new ClazzTreeNode(innerName != null ? innerName : "INNER CLASS", innerClass, sb.toString());
    }

    private ClazzTreeNode createRootNode()
    {
        StringBuffer sb = new StringBuffer("<html>");

        sb.append("Version: ").append(clazz.getVersion()).append("<br>");
        sb.append("JVM supported: ").append(clazz.getJVMSupportedVersion()).append("<br>");
        if (clazz.getClassSignature() != null)
        {
            sb.append("Signature: ").append(clazz.getClassSignature()).append("<br>");
        }
        sb.append("Extends <i>").append(clazz.getSuperClassInfo().getFullyQualifiedName()).append("</i><br>");
        CONSTANT_Class[] intfs = clazz.getInterfaces();
        if (intfs != null && intfs.length > 0)
        {
            sb.append("Implements <i>");
            for (int i = 0; i < intfs.length; i++)
            {
                if (i != 0) sb.append(", <i>");
                sb.append(intfs[i].getFullyQualifiedName()).append("</i>");
            }
            sb.append("<br>");
        }

        sb.append("Access flags: ").append(clazz.getAccessFlags()).append("&nbsp;(");
        if (clazz.isAbstract()) sb.append("abstract ");
        if (clazz.isAnnotation()) sb.append("annotation ");
        if (clazz.isDeprecated()) sb.append("deprecated ");
        if (clazz.isEnumeration()) sb.append("enum ");
        if (clazz.isFinal()) sb.append("final ");
        // TODO if (clazz.isInnerClass()) sb.append("inner ");
        if (clazz.isInterface()) sb.append("interface ");
        if (clazz.isPublic()) sb.append("public ");
        if (clazz.isSuper()) sb.append("super ");
        if (clazz.isSynthetic()) sb.append("synthetic ");
        sb.append(")<br>");

        sb.append("<b>").append(clazz.getThisClassInfo().getFullyQualifiedName()).append("</b>");

        sb.append("</html>");
        return new ClazzTreeNode(clazz.getThisClassInfo().getName(), clazz, sb.toString());
    }

    private ClazzTreeNode createConstantPoolNode()
    {
        StringBuffer sb = new StringBuffer();

        CONSTANT[] consts = clazz.getConstant_pool();
        for (int i = 0; i < consts.length; i++)
        {
            if (consts[i] == null) continue;
            sb.append(i).append(" ").append(consts[i].toString()).append("<br>");
        }

        return new ClazzTreeNode("Constant Pool", consts, sb.toString());
    }

    private String escape(String s)
    {
        return s.replaceAll("<", "&lt;");
    }
}
