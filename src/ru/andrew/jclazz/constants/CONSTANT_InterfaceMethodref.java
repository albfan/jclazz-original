package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

public class CONSTANT_InterfaceMethodref extends CONSTANT_Ref
{
    private MethodDescriptor methodDescriptor;

    protected CONSTANT_InterfaceMethodref(int num, int tag, Clazz clazz)
    {
        super(num, tag, clazz);
    }

    public void update() throws ClazzException
    {
        super.update();

        methodDescriptor = new MethodDescriptor(refNameAndType.getDescriptor());
    }

    public MethodDescriptor getMethodDescriptor()
    {
        return methodDescriptor;
    }
}
