package ru.andrew.jclazz.core.code.ops;

import ru.andrew.jclazz.core.attributes.*;

/**
 * Opcodes: 89<BR>
 * Parameters: no<BR>
 * Operand stack: value => value, value<BR>
 */
public class Dup extends PushOperation
{
    public Dup(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }
}
