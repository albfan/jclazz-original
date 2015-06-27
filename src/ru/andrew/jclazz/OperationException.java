package ru.andrew.jclazz;

public class OperationException extends RuntimeException
{
    public OperationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public OperationException(String message)
    {
        super(message);
    }
}
