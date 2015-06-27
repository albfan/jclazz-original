package ru.andrew.jclazz.gui;

import java.net.URL;
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
            icon = loadIconFromJar("/res/anonymousClass.png");
        }
        else if (clazzObject instanceof FieldInfo)
        {
            icon = loadIconFromJar("/res/field.png");
        }
        else if (clazzObject instanceof MethodInfo)
        {
            icon = loadIconFromJar("/res/method.png");
        }
        else if (clazzObject instanceof Clazz)
        {
            if (((Clazz) clazzObject).isInterface())
            {
                icon = loadIconFromJar("/res/interface.png");
            }
            else
            {
                icon = loadIconFromJar("/res/class.png");
            }
        }
        else if (clazzObject instanceof InnerClass)
        {
            icon = loadIconFromJar("/res/class.png");
        }
        else if (clazzObject instanceof CONSTANT[])
        {
            icon = loadIconFromJar("/res/class.png");
        }
        else
        {
            icon = loadIconFromJar("/res/advice.png");
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

    private Icon loadIconFromJar(String path)
    {
        URL url = this.getClass().getResource(path);
        return new ImageIcon(url);
    }
}
