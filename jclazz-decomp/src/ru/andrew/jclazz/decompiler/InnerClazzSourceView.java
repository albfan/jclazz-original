package ru.andrew.jclazz.decompiler;

import java.util.Map;
import ru.andrew.jclazz.core.*;

public class InnerClazzSourceView extends ClazzSourceView
{
    public InnerClazzSourceView(Clazz clazz, ClazzSourceView outerClazz, Map decompileParams)
    {
        super(clazz, outerClazz, decompileParams);
    }

    protected void printPackageAndImports()
    {
        // Print nothing for inner class
    }
}
