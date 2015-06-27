package ru.andrew.jclazz.gui;

import javax.swing.tree.*;
import javax.swing.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.attributes.*;
import ru.andrew.jclazz.core.constants.*;

public class ClazzTreeNode extends DefaultMutableTreeNode
{
    protected String description;
    protected Icon icon;

    public ClazzTreeNode(Object userObject, Object clazzObject, String description)
    {
        super(userObject);

        if (clazzObject instanceof AttributeInfo)
        {
            icon = new ImageIcon("rsc\\anonymousClass.png");
        }
        else if (clazzObject instanceof FieldInfo)
        {
            icon = new ImageIcon("rsc\\field.png");
        }
        else if (clazzObject instanceof MethodInfo)
        {
            icon = new ImageIcon("rsc\\method.png");
        }
        else if (clazzObject instanceof Clazz)
        {
            if (((Clazz) clazzObject).isInterface())
            {
                icon = new ImageIcon("rsc\\interface.png");
            }
            else
            {
                icon = new ImageIcon("rsc\\class.png");
            }
        }
        else if (clazzObject instanceof InnerClass)
        {
            icon = new ImageIcon("rsc\\class.png");
        }
        else if (clazzObject instanceof CONSTANT[])
        {
            icon = new ImageIcon("rsc\\class.png");
        }
        else
        {
            icon = new ImageIcon("rsc\\advice.png");
        }

        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public Icon getIcon()
    {
        return icon;
    }
}
