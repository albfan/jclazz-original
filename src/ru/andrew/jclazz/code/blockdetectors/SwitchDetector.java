package ru.andrew.jclazz.code.blockdetectors;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.codeitems.ops.*;

public class SwitchDetector implements Detector
{
    public void analyze(Block block)
    {
        block.reset();
        while (block.hasMoreOperations())
        {
            CodeItem ci = block.next();
            if (ci instanceof Switch)
            {
                ((Switch) ci).detectCaseBlocks(block);
            }
        }
    }
}
