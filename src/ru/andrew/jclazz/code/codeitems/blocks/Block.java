package ru.andrew.jclazz.code.codeitems.blocks;

import ru.andrew.jclazz.code.codeitems.ops.*;
import ru.andrew.jclazz.code.codeitems.*;
import ru.andrew.jclazz.code.*;
import ru.andrew.jclazz.*;

import java.util.*;
import java.io.*;

public class Block implements CodeItem
{
    protected List ops;
    protected Block parent;
    protected Clazz clazz;
    protected MethodInfo m_info;

    protected Block(Block parent)
    {
        this.ops = new ArrayList();
        this.parent = parent;
        this.clazz = parent.getClazz();
    }

    public Block(Block parent, List ops)
    {
        this.ops = ops;
        this.parent = parent;
        this.clazz = parent.getClazz();
    }

    public Block(List ops, Clazz clazz, MethodInfo m_info)
    {
        this.ops = ops;
        this.clazz = clazz;
        this.m_info = m_info;
    }

    public void setParent(Block parent)
    {
        this.parent = parent;
    }

    public Block getParent()
    {
        return parent;
    }

    public void setOperations(List ops)
    {
        this.ops = ops;
    }

    public List getOperations()
    {
        return ops;
    }

    public Clazz getClazz()
    {
        return clazz;
    }

    // Handling block operations by iterating

    private int ext_pos = 0;

    public boolean hasMoreOperations()
    {
        return ext_pos < ops.size();
    }

    public CodeItem next()
    {
        CodeItem item = (CodeItem) ops.get(ext_pos);
        ext_pos++;
        return item; 
    }

    public void back()
    {
        ext_pos--;
    }

    public void seekEnd()
    {
        ext_pos = ops.size();
    }

    public void reset()
    {
        ext_pos = 0;
    }

    // Business methods

    public void postCreate()
    {
    }

    public void postProcess()
    {
    }

    public List createSubBlock(long startOp, long endOp, Block subblock)
    {
        List subops = new ArrayList();

        boolean moving = false;
        int pos = 0;
        while (pos < ops.size())
        {
            CodeItem citem = (CodeItem) ops.get(pos);
            if (citem.getStartByte() >= startOp) moving = true;
            if (citem.getStartByte() >= endOp) break;

            if (moving)
            {
                subops.add(citem);
                if (subblock != null && citem instanceof Block) ((Block) citem).setParent(subblock);
                ops.remove(pos);
                if (ext_pos > pos && pos != 0) ext_pos--;
            }
            else
            {
                pos++;
            }
        }

        if (subblock != null)
        {
            ops.add(pos, subblock);
            subblock.setOperations(subops);
            subblock.setParent(this);
            subblock.postCreate();
        }
        return subops;
    }

    public void addOperation(int index, CodeItem ci)
    {
        if (index <= ext_pos) ext_pos++;
        ops.add(index, ci);

        if (ci instanceof Block)
        {
            ((Block) ci).setParent(this);
        }
    }

    private CodeItem removeOperation(int pos)
    {
        if (pos <= ext_pos && ext_pos != 0) ext_pos--;
        return (CodeItem) ops.remove(pos);
    }

    public CodeItem removeLastOperation()
    {
        return ops.isEmpty() ? null : removeOperation(ops.size() - 1);
    }

    public CodeItem removeFirstOperation()
    {
        if (!ops.isEmpty())
        {
            return removeOperation(0);
        }
        return null;
    }

    public CodeItem removeOperation(long start_byte)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            if (((CodeItem) ops.get(i)).getStartByte() == start_byte)
            {
                if (i <= ext_pos && ext_pos != 0) ext_pos--;
                return (Operation) ops.remove(i);
            }
        }
        return null;
    }

    public CodeItem getOperationPriorTo(long start_byte)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            if (((CodeItem) ops.get(i)).getStartByte() >= start_byte)
            {
                if (i == 0) return null;
                return (CodeItem) ops.get(i - 1);
            }
        }
        return getLastOperation();
    }

    public CodeItem getOperationAfter(long start_byte)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            if (((CodeItem) ops.get(i)).getStartByte() > start_byte) return (CodeItem) ops.get(i);
        }
        return null;
    }

    public CodeItem getOperationByStartByte(long start_byte)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            if (((CodeItem) ops.get(i)).getStartByte() == start_byte) return (CodeItem) ops.get(i);
        }
        return null;
    }

    public Operation removePriorPushOperation()
    {
        for (int i = ext_pos - 2; i >= 0 ; i--)
        {
            if (ops.get(i) instanceof Block)
            {
                Operation priorPush = getPriorPushFromSubblock((Block) ops.get(i));
                if (priorPush != null) return priorPush;
            }
            else if (((Operation) ops.get(i)).isPush())
            {
                ext_pos--;
                return (Operation) ops.remove(i);
            }
        }
        return null;
    }

    private Operation getPriorPushFromSubblock(Block subblock)
    {
        // Checking is last operation is Nop for correct seekEnd
        if (!(subblock.getLastOperation() instanceof Nop))
        {
            subblock.addOperation(subblock.size(), new Nop(-1));
        }
        subblock.seekEnd();
        return subblock.removePriorPushOperation();
    }

    public CodeItem removeCurrentOperation()
    {
        ext_pos--;
        return (CodeItem) ops.remove(ext_pos);
    }

    public Operation getPriorPushOperation()
    {
        for (int i = ext_pos - 2; i >= 0 ; i--)
        {
            if (ops.get(i) instanceof Block)
            {
                Operation priorPush = getPriorPushFromSubblock((Block) ops.get(i));
                if (priorPush != null) return priorPush;
            }
            else if (((Operation) ops.get(i)).isPush())
            {
                return (Operation) ops.get(i);
            }
        }
        return null;
    }

    public CodeItem getPreviousOperation()
    {
        return (CodeItem) (ext_pos - 2 >= 0 ? ops.get(ext_pos - 2) : null);
    }

    public CodeItem removePreviousOperation()
    {
        if (ext_pos - 2 >= 0)
        {
            CodeItem ci = (CodeItem) ops.remove(ext_pos - 2);
            ext_pos--;
            return ci;
        }
        return null;
    }

    public CodeItem getNextOperation()
    {
        if (hasMoreOperations())
        {
            return (CodeItem) ops.get(ext_pos);
        }
        return null;
    }

    public void replaceOperation(long start_byte, CodeItem newop)
    {
        for (int i = 0; i < ops.size(); i++)
        {
            if (((CodeItem) ops.get(i)).getStartByte() == start_byte)
            {
                ops.remove(i);
                ops.add(i, newop);
                return;
            }
        }
    }

    public void replaceCurrentOperation(CodeItem newop)
    {
        if (ext_pos > 0)
        {
            if (newop != null)
            {
                ops.set(ext_pos - 1, newop);
                if (newop instanceof Block) ((Block) newop).setParent(this);
            }
            else
            {
                ops.remove(ext_pos - 1);
                ext_pos--;
            }
        }
    }

    public void truncate(long fromStartByte)
    {
        Iterator it = ops.iterator();
        while (it.hasNext())
        {
            CodeItem ci = (CodeItem) it.next();
            if (ci.getStartByte() >= fromStartByte) it.remove();
        }
    }

    public boolean isEmpty()
    {
        return ops == null || ops.isEmpty(); 
    }

    public int size()
    {
        return ops == null ? 0 : ops.size();
    }

    public CodeItem getFirstOperation()
    {
        return ops.isEmpty() ? null : (CodeItem) ops.get(0);
    }

    public CodeItem getLastOperation()
    {
        return ops.isEmpty() ? null : (CodeItem) ops.get(ops.size() - 1);
    }

    public long getStartByte()
    {
        return ((CodeItem) ops.get(0)).getStartByte();
    }

    public void print(PrintWriter pw, String init_indent)
    {
        pw.println(init_indent + "{");
        printOperations(pw, init_indent);
        pw.println(init_indent + "}");
    }

    public void printRaw(PrintWriter pw, String init_indent)
    {
        pw.println(init_indent + "{");
        String indent = init_indent + "    ";
        for (Iterator i = ops.iterator(); i.hasNext();)
        {
            CodeItem citem = (CodeItem) i.next();
            citem.printRaw(pw, indent);
        }
        pw.println(init_indent + "}");
    }

    public void printOperations(PrintWriter pw, String init_indent)
    {
        String indent = init_indent + "    ";
        for (Iterator i = ops.iterator(); i.hasNext();)
        {
            CodeItem citem = (CodeItem) i.next();
            citem.print(pw, indent);
        }
    }

    protected String alias(String fqn)
    {
        return clazz.importClass(fqn);
    }

    public void analyze(Block block)
    {
    }

    // Local Variables management

    private HashMap lvars = new HashMap();

    public LocalVariable getLocalVariable(int ivar, String type)
    {
        return getLocalVariable(ivar, type, null, false);
    }

    public LocalVariable getLocalVariable(int ivar, String type, boolean forceReload)
    {
        return getLocalVariable(ivar, type, null, forceReload);
    }

    public LocalVariable getLocalVariable(int ivar, String type, String name)
    {
        return getLocalVariable(ivar, type, name, false);
    }

    public LocalVariable getLocalVariable(int ivar, String type, String name, boolean forceReload)
    {
        LocalVariable lv = (LocalVariable) lvars.get(Integer.valueOf(ivar));
        Block parentBlock = getParent();
        while ((lv == null) && (parentBlock != null))
        {
            lv = (LocalVariable) parentBlock.lvars.get(Integer.valueOf(ivar));
            parentBlock = parentBlock.parent;
        }
        if (forceReload) lv = null;
        if (lv == null)
        {
            Block preBlock = this;
            while (preBlock.parent != null)
            {
                preBlock = preBlock.parent;
            }
            MethodInfo method = preBlock.m_info;
            lv = new LocalVariable(ivar, type, name != null ? name : (type != null ? method.getLVName(type) : null));
            lvars.put(Integer.valueOf(ivar), lv);
        }
        return lv;
    }
}
