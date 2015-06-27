package ru.andrew.jclazz.attributes.annotations;

import ru.andrew.jclazz.constants.*;
import ru.andrew.jclazz.*;

public class RuntimeInvisibleParameterAnnotations extends RuntimeVisibleParameterAnnotations
{
    public RuntimeInvisibleParameterAnnotations(CONSTANT_Utf8 attributeName, Clazz clazz)
    {
        super(attributeName, clazz);
    }

    public String toString()
    {
        return toString("RuntimeInvisibleParameterAnnotations");
    }
}
