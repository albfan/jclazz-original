package ru.andrew.jclazz.code.blockdetectors;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.code.codeitems.ops.*;

import java.util.*;

public class TryDetector implements Detector
{
    private List trys = new ArrayList();
    private List fakeFinallies = new ArrayList();

    public TryDetector(Code.ExceptionTable[] exc_table)
    {
        if (exc_table != null && exc_table.length > 0)
        {
            trys.addAll(Arrays.asList(exc_table));

            // Removing fake finally blocks for each catch block
            for (Iterator fit = trys.iterator(); fit.hasNext();)
            {
                Code.ExceptionTable cet = (Code.ExceptionTable) fit.next();
                if (cet.catch_type == null && !isReallyFinally(exc_table, cet))
                {
                    fakeFinallies.add(cet);
                    fit.remove();
                }
            }

            // sorting try-catches
            Collections.sort(trys, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    int comparison = ((Code.ExceptionTable) o1).end_pc - ((Code.ExceptionTable) o2).end_pc;
                    if (comparison == 0) comparison = ((Code.ExceptionTable) o1).handler_pc - ((Code.ExceptionTable) o2).handler_pc;
                    return comparison;
                }
            });
        }
    }

    private boolean isReallyFinally(Code.ExceptionTable[] exc_table, Code.ExceptionTable fin)
    {
        if (fin.start_pc == fin.handler_pc) return false;

        for (int i = 0; i < exc_table.length; i++)
        {
            if ((exc_table[i].catch_type != null) && (exc_table[i].handler_pc == fin.start_pc))
            {
                return false;
            }
        }
        return true;
    }

    public void analyze(Block block)
    {
        if (trys.isEmpty()) return;

        // Creating blocks
        Iterator it = trys.iterator();
        boolean hasMoreTries = true;
        Code.ExceptionTable etn = null;
        do
        {
            if (!it.hasNext()) break;
            Code.ExceptionTable et = etn == null ? (Code.ExceptionTable) it.next() : etn;

            if ((et.start_pc < block.getStartByte()) || (et.end_pc > block.getLastOperation().getStartByte()))
            {
                continue;
            }
            if (findSubBlock(block, et.start_pc, et.end_pc))
            {
                etn = null;
                continue;
            }

            // Finding last goto
            CodeItem gop = block.getOperationPriorTo(et.end_pc);
            while (!(gop instanceof GoTo || gop instanceof Return))
            {
                gop = block.getOperationAfter(gop.getStartByte());
            }

            Try _try = new Try(et.start_pc, et.end_pc, block);
            _try.setFinallyException(fakeFinallies);
            block.createSubBlock(et.start_pc, gop.getStartByte() + 1, _try);
            it.remove();

            // Processing several catch blocks (except last)
            Catch _catch = new Catch(et.handler_pc, et.catch_type, block);
            hasMoreTries = false;
            while (it.hasNext())
            {
                etn = (Code.ExceptionTable) it.next();
                if (((etn.start_pc != et.start_pc) || (etn.end_pc != et.end_pc)))
                {
                    hasMoreTries = true;
                    break;
                }

                block.createSubBlock(et.handler_pc, etn.handler_pc, _catch);
                _try.addCatchBlock(_catch);
                et = etn;
                _catch = new Catch(et.handler_pc, et.catch_type, block);
                it.remove();

            }
            // Processing last catch block
            block.createSubBlock(et.handler_pc, _try.getRetrieveOperation(), _catch);
            _try.addCatchBlock(_catch);
        }
        while (hasMoreTries);
    }

    private boolean findSubBlock(Block block, long start_pc, long end_pc)
    {
        CodeItem ci = block.getOperationByStartByte(start_pc);
        if (ci instanceof Block)
        {
            return ((Block) ci).getLastOperation().getStartByte() > end_pc;
        }          
        return ci == null;
    }
}
