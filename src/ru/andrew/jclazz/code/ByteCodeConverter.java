package ru.andrew.jclazz.code;

import ru.andrew.jclazz.attributes.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.code.blockdetectors.*;
import ru.andrew.jclazz.*;

import java.util.*;

public class ByteCodeConverter
{
    public static Block detectBlocks(Code codeAttr)
    {
        Block topBlock = codeAttr.getCodeBlock();

        detectBlocks(topBlock, new LoopDetector());

        detectBlocks(topBlock, new TryDetector(codeAttr.getExceptionTable()));
        detectBlocks(topBlock, new SwitchDetector());

        detectBlocks(topBlock, new IfDetector());
        return topBlock;
    }

    private static void detectBlocks(Block block, Detector detector)
    {
        if (block == null) return;
        detector.analyze(block);

        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem ci = block.next();
            if (ci instanceof Block)
            {
                detectBlocks((Block) ci, detector);
            }
            else if (ci instanceof Switch)
            {
                Switch sci = (Switch) ci;
                for (Iterator cbit = sci.getCaseBlocks().iterator(); cbit.hasNext();)
                {
                    detectBlocks(((Switch.CaseBlock) cbit.next()).getBlock(), detector);
                }
            }
        }
    }

    public static Block convert(Code codeAttr)
    {
        Block topBlock = detectBlocks(codeAttr);
        postProcess(topBlock);
        analyze(topBlock);
        return topBlock;
    }

    private static void postProcess(Block block)
    {
        if (block == null) return;
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            if (citem instanceof Block)
            {
                ((Block) citem).postProcess();
                postProcess((Block) citem);
            }
            else if (citem instanceof Switch)
            {
                Switch sci = (Switch) citem;
                Iterator cbit = sci.getCaseBlocks().iterator();
                while (cbit.hasNext())
                {
                    ((Switch.CaseBlock) cbit.next()).getBlock().postProcess();
                    postProcess(((Switch.CaseBlock) cbit.next()).getBlock());
                }
            }
        }
    }

    private static void analyze(Block block)
    {
        if (block == null) return;
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem citem = block.next();
            citem.analyze(block);

            if (citem instanceof Block)
            {
                analyze((Block) citem);
            }
            else if (citem instanceof Switch)
            {
                Switch sci = (Switch) citem;
                Iterator cbit = sci.getCaseBlocks().iterator();
                while (cbit.hasNext())
                {
                    analyze(((Switch.CaseBlock) cbit.next()).getBlock());
                }
            }
        }
    }
}
