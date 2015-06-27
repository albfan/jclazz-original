package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

public class CONSTANT_Methodref_info extends CONSTANT_Ref_info
{
    private MethodDescriptor methodDescriptor;

    public void complete() throws ClazzException
    {
        super.complete();

        methodDescriptor = new MethodDescriptor(getDescriptor());
    }

    public MethodDescriptor getMethodDescriptor()
    {
        return methodDescriptor;
    }
}
