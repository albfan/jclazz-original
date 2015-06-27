package ru.andrew.jclazz;

import java.util.*;

public class MethodDescriptor
{
    private String rawDescriptor;
    private FieldDescriptor returnType;
    private List params;

    public MethodDescriptor(String descriptor) throws ClazzException
    {
        this.rawDescriptor = descriptor;
        if (descriptor.charAt(0) != '(') throw new ClazzException("Invalid method descriptor");
        params = new ArrayList();
        int currentPos = 1;
        while (descriptor.charAt(currentPos) != ')')
        {
            int nextPos = currentPos;
            while (descriptor.charAt(nextPos) == '[') nextPos++;
            if (descriptor.charAt(nextPos) == 'L')
            {
                nextPos = descriptor.indexOf(';', nextPos);
            }
            nextPos++;
            params.add(new FieldDescriptor(descriptor.substring(currentPos, nextPos)));
            currentPos = nextPos;
        }
        returnType = new FieldDescriptor(descriptor.substring(currentPos + 1));
    }

    public FieldDescriptor getReturnType()
    {
        return returnType;
    }

    public List getParams()
    {
        return params;
    }

    public String getRawDescriptor()
    {
        return rawDescriptor;
    }
}
