package ru.andrew.jclazz.decompiler.engine.ops;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.*;

public class PushVariableView extends OperationView
{
    private LocalVariable lvar;

    public PushVariableView(Operation operation, MethodSourceView methodView)
    {
        super(operation, methodView);
    }

    public String getPushType()
    {
        return getLVType(lvar);
    }

    public String source()
    {
        return getLVName(lvar);
    }

    public void analyze(Block block)
    {
        if (getOpcode() >= 21 && getOpcode() <= 45)
        {
            String suffix = "";
            char mn = operation.getOpcode().getMnemonic().charAt(0);
            if (mn == 'f') suffix = "float";
            else if (mn == 'l') suffix = "long";
            else if (mn == 'd') suffix = "double";
            else if (mn == 'i') suffix = "int";
            else if (mn == 'a') suffix = null;   // TODO what is ref?
            lvar = block.getLocalVariable(((PushVariable) operation).getLocalVariableNumber(), suffix);
        }
    }
}
