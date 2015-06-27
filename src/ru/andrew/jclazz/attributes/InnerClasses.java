package ru.andrew.jclazz.attributes;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.constants.*;

import java.io.*;

public class InnerClasses extends ATTRIBUTE_INFO
{
    public static final String ANONYMOUS = "Anonymous";
    private InnerClass classes[];

    public void load(ClazzInputStream cis, Clazz clazz) throws IOException, ClazzException
    {
        cis.readU4();   // Attribute length
        int number_of_classes = cis.readU2();
        classes = new InnerClass[number_of_classes];
        for (int i = 0; i < number_of_classes; i++)
        {
            classes[i] = new InnerClass();
            int inner_class_info_index = cis.readU2();
            if (inner_class_info_index != 0)
            {
                classes[i].inner_class = (CONSTANT_Class_info) clazz.getConstant_pool()[inner_class_info_index];
            }
            int outer_class_info_index = cis.readU2();
            if (outer_class_info_index != 0)
            {
                classes[i].outer_class = (CONSTANT_Class_info) clazz.getConstant_pool()[outer_class_info_index];
            }
            int inner_name_index = cis.readU2();
            if (inner_name_index != 0)
            {
                classes[i].inner_name = ((CONSTANT_Utf8_info) clazz.getConstant_pool()[inner_name_index]).getString();
            }
            else
            {
                classes[i].inner_name = ANONYMOUS;
            }
            classes[i].inner_class_access_flags = cis.readU2();
        }
    }

    public InnerClass[] getInnerClasses()
    {
        return classes;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(ATTR);
        sb.append("InnerClasses: \n");
        for (int i = 0; i < classes.length; i++)
        {
            InnerClass ic = classes[i];
            sb.append(INDENT).append("* ");
            String _innerClass = ic.getInnerClass() != null ? ic.getInnerClass().getFullyQualifiedName() : "";
            sb.append("Inner class - ").append(_innerClass).append("\n");
            String _outerClass = ic.getOuterClass() != null ? ic.getOuterClass().getFullyQualifiedName() : "";
            sb.append(INDENT).append("  ").append("Outer class - ").append(_outerClass).append("\n");
            String _innerName = ic.getInnerName() != null ? ic.getInnerName() : "";
            sb.append(INDENT).append("  ").append("Inner name - ").append(_innerName).append("\n");
            sb.append(INDENT).append("  ").append("Inner class access flags - ").append(ic.getInnerClassAccessFlags());
            if (i < classes.length - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
