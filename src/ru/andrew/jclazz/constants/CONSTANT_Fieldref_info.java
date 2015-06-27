package ru.andrew.jclazz.constants;

import ru.andrew.jclazz.*;

public class CONSTANT_Fieldref_info extends CONSTANT_Ref_info
{
    private FieldDescriptor fieldDescriptor;

    public void complete() throws ClazzException
    {
        super.complete();

        fieldDescriptor = new FieldDescriptor(getDescriptor());
    }

    public FieldDescriptor getFieldDescriptor()
    {
        return fieldDescriptor;
    }

    public String toString()
    {
        return fieldDescriptor.getFQN();
    }
}
