package ru.andrew.jclazz.decompiler.engine;

import java.util.*;

public class Converter
{
    private Collection transferPoints = new ArrayList();
    private Map transfers = new HashMap();

    public void addTransferPoint(long start_byte, long target_byte, boolean isConditional)
    {
        TransferPoint tp = new TransferPoint();
        tp.start_byte = start_byte;
        tp.target_byte = target_byte;
        tp.conditional = isConditional;

        transferPoints.add(tp);
        Long target = new Long(target_byte);
        Collection points = (Collection) transfers.get(target);
        if (points == null)
        {
            points = new ArrayList();
            transfers.put(target, points);
        }
        points.add(tp);
    }

    public Collection getTransferPoints()
    {
        return transferPoints;
    }

    public Map getTransfers()
    {
        return transfers;
    }

    public class TransferPoint
    {
        long start_byte;
        long target_byte;
        boolean conditional;

        public boolean isFwd()
        {
            return target_byte > start_byte;
        }

        public boolean isConditional()
        {
            return conditional;
        }

        public long getStartByte()
        {
            return start_byte;
        }

        public long getTargetByte()
        {
            return target_byte;
        }

    }
}
