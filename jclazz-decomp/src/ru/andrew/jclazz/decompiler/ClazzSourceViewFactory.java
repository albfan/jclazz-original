package ru.andrew.jclazz.decompiler;

import java.util.Map;
import ru.andrew.jclazz.core.*;

public final class ClazzSourceViewFactory
{
    public static ClazzSourceView getClazzSourceView(Clazz clazz, Map decompileParams)
    {
        if (clazz.isEnumeration())
        {
            return new EnumSourceView(clazz, null, decompileParams);
        }
        else
        {
            return new ClazzSourceView(clazz, null, decompileParams);
        }
    }
}
