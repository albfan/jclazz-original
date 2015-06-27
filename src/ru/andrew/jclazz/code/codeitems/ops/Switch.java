package ru.andrew.jclazz.code.codeitems.ops;

import ru.andrew.jclazz.code.codeitems.blocks.*;
import ru.andrew.jclazz.attributes.*;

import java.io.*;
import java.util.*;

/**
 * Opcodes: 170, 171<BR>
 * Parameters: N<BR>
 * Operand stack: index => <BR>
 */
public class Switch extends Operation
{
    private List caseBlocks;
    private String switchVar;

    public Switch(int opcode, long start_byte, Code code)
    {
        super(opcode, start_byte, code);
    }

    protected void loadParams(Code code)
    {
        caseBlocks = new ArrayList();

        int zero_bytes = 3 - (int) (start_byte % 4);
        code.skipBytes(zero_bytes);

        int def1 = code.getNextByte();
        int def2 = code.getNextByte();
        int def3 = code.getNextByte();
        int def4 = code.getNextByte();
        int defaultOffset = (def1 << 24) | (def2 << 16) | (def3 << 8) | def4;
        caseBlocks.add(new CaseBlock(0, start_byte + defaultOffset, true));

        if (opcode.getOpcode() == 170)  // tableswitch
        {
            int lowbyte1 = code.getNextByte();
            int lowbyte2 = code.getNextByte();
            int lowbyte3 = code.getNextByte();
            int lowbyte4 = code.getNextByte();
            int highbyte1 = code.getNextByte();
            int highbyte2 = code.getNextByte();
            int highbyte3 = code.getNextByte();
            int highbyte4 = code.getNextByte();
            int high = (highbyte1 << 24) | (highbyte2 << 16) | (highbyte3 << 8) | highbyte4;
            int low = (lowbyte1 << 24) | (lowbyte2 << 16) | (lowbyte3 << 8) | lowbyte4;
            //int total_offsets = high - low + 1;

            for (int i = low; i <= high; i++)
            {
                int jumpbyte1 = code.getNextByte();
                int jumpbyte2 = code.getNextByte();
                int jumpbyte3 = code.getNextByte();
                int jumpbyte4 = code.getNextByte();
                int jumpOffset = (jumpbyte1 << 24) | (jumpbyte2 << 16) | (jumpbyte3 << 8) | jumpbyte4;
                caseBlocks.add(new CaseBlock(i, start_byte + jumpOffset, false));
            }
        }
        else if (opcode.getOpcode() == 171) // lookupswitch
        {
            int npairs1 = code.getNextByte();
            int npairs2 = code.getNextByte();
            int npairs3 = code.getNextByte();
            int npairs4 = code.getNextByte();
            int total_pairs = (npairs1 << 24) | (npairs2 << 16) | (npairs3 << 8) | npairs4;

            for (int i = 0; i < total_pairs; i++)
            {
                int matchbyte1 = code.getNextByte();
                int matchbyte2 = code.getNextByte();
                int matchbyte3 = code.getNextByte();
                int matchbyte4 = code.getNextByte();
                int matchOffset = (matchbyte1 << 24) | (matchbyte2 << 16) | (matchbyte3 << 8) | matchbyte4;

                int jumpbyte1 = code.getNextByte();
                int jumpbyte2 = code.getNextByte();
                int jumpbyte3 = code.getNextByte();
                int jumpbyte4 = code.getNextByte();
                int jumpOffset = (jumpbyte1 << 24) | (jumpbyte2 << 16) | (jumpbyte3 << 8) | jumpbyte4;
                caseBlocks.add(new CaseBlock(matchOffset, start_byte + jumpOffset, false));
            }
        }
    }

    public boolean isPush()
    {
        return false;
    }

    public String getPushType()
    {
        return null;
    }

    public String str()
    {
        return null;
    }

    public List getCaseBlocks()
    {
        return caseBlocks;
    }

    public void print(PrintWriter pw, String indent)
    {
        pw.println(indent + "switch (" + switchVar + ")");
        pw.println(indent + "{");
        for (Iterator it = caseBlocks.iterator(); it.hasNext();)
        {
            CaseBlock cb = (CaseBlock) it.next();
            cb.print(pw, "    " + indent);
        }
        pw.println(indent + "}");
    }

    public void analyze(Block block)
    {
        Operation intVal = block.removePriorPushOperation();
        switchVar = intVal.str();
    }

    public void detectCaseBlocks(Block block)
    {
        Collections.sort(caseBlocks, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return (int) (((CaseBlock) o1).getOffset() - ((CaseBlock) o2).getOffset());
            }
        });

        long prev_offset = ((CaseBlock) caseBlocks.get(0)).getOffset();
        for (int i = 1; i < caseBlocks.size(); i++)
        {
            CaseBlock nextCase = (CaseBlock) caseBlocks.get(i);
            Block caseBlock = new Block(block, block.createSubBlock(prev_offset, nextCase.getOffset(), null));
            ((CaseBlock) caseBlocks.get(i - 1)).setBlock(caseBlock);
            prev_offset = nextCase.getOffset();
        }
        // Trying to find at least one case block with last GoTo operation
        long exitGoto = 0;
        Iterator it = caseBlocks.iterator();
        while (it.hasNext())
        {
            exitGoto = ((CaseBlock) it.next()).getLastGotoOffset();
            if (exitGoto != 0) break;
        }
        // If found creating block for last case block
        if (exitGoto != 0)
        {
            Block caseBlock = new Block(block, block.createSubBlock(prev_offset, exitGoto, null));
            ((CaseBlock) caseBlocks.get(caseBlocks.size() - 1)).setBlock(caseBlock);
        }
        else    // Last (usually default) block is print outside switch, so no need to create subblock
        {
            // do nothing
        }
    }

    public class CaseBlock
    {
        private int value;
        private long offset;    // Temporary parameter
        private boolean isDeafult = false;
        private Block block;

        private long exitGoto = 0;

        public CaseBlock(int value, long offset, boolean deafult)
        {
            this.value = value;
            this.offset = offset;
            this.isDeafult = deafult;
        }

        public void setBlock(Block block)
        {
            this.block = block;

            // Try to find last goto, which means the end of switch block
            if (this.block.getLastOperation() instanceof GoTo)
            {
                exitGoto = ((GoTo) this.block.removeLastOperation()).getTargetOperation();
            }
        }

        public long getOffset()
        {
            return offset;
        }

        public int getValue()
        {
            return value;
        }

        public boolean isDeafult()
        {
            return isDeafult;
        }

        public Block getBlock()
        {
            return block;
        }

        public long getLastGotoOffset()
        {
            return exitGoto;
        }

        public void print(PrintWriter pw, String indent)
        {
            if (isDeafult && block == null) return; // absence of default block
            pw.print(indent);
            if (isDeafult)
            {
                pw.print("default");
            }
            else
            {
                pw.print("case " + value);
            }
            pw.println(":");
            if (block != null)
            {
                block.printOperations(pw, indent);
            }
            else
            {
                pw.println(indent + "    BLOCK IS ABSENT!!!");  // TODO what to do
            }
            if (exitGoto != 0)
            {
                pw.println(indent + "    break;");
            }
        }
    }

    public void printRaw(PrintWriter pw, String indent)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(indent).append(start_byte).append(" ").append(opcode.getMnemonic()).append(" ");
        for (Iterator it = caseBlocks.iterator(); it.hasNext();)
        {
            CaseBlock cb = (CaseBlock) it.next();
            if (cb.isDeafult)
            {
                sb.append("def:");
            }
            else
            {
                sb.append(cb.value).append(":");
            }
            sb.append(cb.offset).append(" ");
        }
        pw.println(sb.toString());
    }
    
}
