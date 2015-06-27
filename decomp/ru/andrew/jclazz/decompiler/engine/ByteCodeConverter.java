package ru.andrew.jclazz.decompiler.engine;

import ru.andrew.jclazz.core.code.ops.*;
import ru.andrew.jclazz.decompiler.engine.blocks.*;
import ru.andrew.jclazz.decompiler.engine.blockdetectors.*;
import ru.andrew.jclazz.decompiler.*;

import java.util.*;

public class ByteCodeConverter
{
    public static Block detectBlocks(Block topBlock, MethodSourceView msv)
    {
        detectBlocks(topBlock, new BackLoopDetector());
        detectBlocks(topBlock, new LoopDetector());

        detectBlocks(topBlock, new TryDetector(msv.getMethod().getCodeBlock().getExceptionTable()));
        detectBlocks(topBlock, new SwitchDetector());

        detectBlocks(topBlock, new IfDetector());
        return topBlock;
    }

    public static Block convertToViewOperations(List operations, MethodSourceView msv)
    {
        ArrayList list = new ArrayList(operations.size());
        for (Iterator i = operations.iterator(); i.hasNext();)
        {
            Operation op = (Operation) i.next();
            String className = op.getClass().getName();
            className = className.substring(className.lastIndexOf('.') + 1);
            String newName = "ru.andrew.jclazz.decompiler.engine.ops." + className + "View";
            try
            {
                CodeItem codeItem = (CodeItem) Class.forName(newName).getConstructor(new Class[]{Operation.class, MethodSourceView.class}).newInstance(new Object[]{op, msv});
                list.add(codeItem);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return new Block(list, msv);
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
        }
    }

    public static Block convert(Block topBlock, MethodSourceView msv)
    {
        detectBlocks(topBlock, msv);
        postProcess(topBlock);
        analyze(topBlock);
        detectCompoundBackLoops(topBlock, new BackLoopDetector());
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

                if (citem instanceof Loop)
                {
                    ((Loop) citem).postanalyze(block);
                }
            }
        }
    }

    private static Loop detectCompoundBackLoops(Block block, BackLoopDetector detector)
    {
        if (block == null) return null;
        Loop innerLoop = detector.collapseBackLoops(block);
        if (innerLoop != null)
        {
            return innerLoop;
        }

        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem ci = block.next();
            if (ci instanceof Block)
            {
                Block loop = (Block) ci;
                do
                {
                    loop = detectCompoundBackLoops(loop, detector);
                    if (loop != null) block.replaceCurrentOperation(loop);
                }
                while (loop != null);
            }
        }
        return null;
    }
}
