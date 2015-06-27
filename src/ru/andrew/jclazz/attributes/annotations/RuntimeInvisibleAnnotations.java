package ru.andrew.jclazz.attributes.annotations;

import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.*;

public class RuntimeInvisibleAnnotations extends RuntimeVisibleAnnotations
{
    public RuntimeInvisibleAnnotations(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public String toString()
    {
        return toString("RuntimeInvisibleAnnotations");
    }
}
