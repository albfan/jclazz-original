package ru.andrew.jclazz.decompiler.engine.blocks;

import ru.andrew.jclazz.decompiler.engine.*;
import ru.andrew.jclazz.decompiler.engine.ops.*;
import ru.andrew.jclazz.decompiler.*;

import java.util.*;

public class Block implements CodeItem
{
    protected final String NL = System.getProperty("line.separator");

    protected List ops;
    protected Block parent;
    private MethodSourceView m_info;
    protected String indent = "";

    protected Block(Block parent)
    {
        this.ops = new ArrayList();
        this.parent = parent;
    }

    public Block(Block parent, List ops)
    {
        this.ops = ops;
        this.parent = parent;
    }

    public Block(List ops, MethodSourceView m_info)
    {
        this.ops = ops;
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

    public String getIndent()
    {
        return indent;
    }

    public void setIndent(String indent)
    {
        this.indent = indent;
    }

    // Handling block operations by iterating

    private int ext_pos = 0;

    private static final int END_BLOCK_POS = Integer.MAX_VALUE;

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
        ext_pos = END_BLOCK_POS;
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
        if (index <= ext_pos && ext_pos != END_BLOCK_POS) ext_pos++;
        ops.add(index, ci);

        if (ci instanceof Block)
        {
            ((Block) ci).setParent(this);
        }
    }

    private CodeItem removeOperation(int pos)
    {
        if (pos <= ext_pos && ext_pos != 0 && ext_pos != END_BLOCK_POS) ext_pos--;
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
                if (i <= ext_pos && ext_pos != 0 && ext_pos != END_BLOCK_POS) ext_pos--;
                return (CodeItem) ops.remove(i);
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

    public OperationView removePriorPushOperation()
    {
        int start_pos = ext_pos != END_BLOCK_POS ? ext_pos - 2 : ops.size() - 1;
        for (int i = start_pos; i >= 0 ; i--)
        {
            if (ops.get(i) instanceof Block)
            {
                OperationView priorPush = getPriorPushFromSubblock((Block) ops.get(i));   // TODO why not remove
                if (priorPush != null) return priorPush;
            }
            else if (((OperationView) ops.get(i)).isPush())
            {
                if (ext_pos != END_BLOCK_POS) ext_pos--;
                return (OperationView) ops.remove(i);
            }
        }
        return null;
    }

    private OperationView getPriorPushFromSubblock(Block subblock)
    {
        subblock.seekEnd();
        return subblock.removePriorPushOperation();
    }

    public CodeItem removeCurrentOperation()
    {
        ext_pos--;
        return (CodeItem) ops.remove(ext_pos);
    }

    public OperationView getPriorPushOperation()
    {
        int start_pos = ext_pos != END_BLOCK_POS ? ext_pos - 2 : ops.size() - 1;
        for (int i = start_pos; i >= 0 ; i--)
        {
            if (ops.get(i) instanceof Block)
            {
                OperationView priorPush = getPriorPushFromSubblock((Block) ops.get(i));
                if (priorPush != null) return priorPush;
            }
            else if (((OperationView) ops.get(i)).isPush())
            {
                return (OperationView) ops.get(i);
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
        if (ext_pos > 0 && ext_pos != END_BLOCK_POS)
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

    public MethodSourceView getMethodView()
    {
        Block preBlock = this;
        while (preBlock.parent != null)
        {
            preBlock = preBlock.parent;
        }
        return preBlock.m_info;
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

    public String getSource()
    {
        return indent + "{" + NL + getOperationsSource() + indent + "}" + NL;
    }

    public String getOperationsSource()
    {
        return getOperationsSource(-1);
    }

    public String getOperationsSource(int endByte)
    {
        StringBuffer sb = new StringBuffer();
        boolean withLN = "yes".equals(getMethodView().getClazzView().getDecompileParameter(ClazzSourceView.WITH_LINE_NUMBERS)) &&
                         (getMethodView().getMethod().getCodeBlock() != null) &&
                         (getMethodView().getMethod().getLineNumberTable() != null);
        
        for (Iterator i = ops.iterator(); i.hasNext();)
        {
            CodeItem citem = (CodeItem) i.next();
            if (endByte != -1 && citem.getStartByte() >= endByte) break;
            if (withLN &&
                (!(citem instanceof Block) ||
                       (citem instanceof IfBlock) ||
                       (citem instanceof Loop && (((Loop) citem).isPrintPrecondition()))) )
            {
                int instruction;
                if (citem instanceof Loop)
                {
                    Condition cond0 = (Condition) ((List) ((Loop) citem).getConditions().get(0)).get(0);
                    instruction = (int) cond0.getIfOperation().getStartByte();
                }
                else
                {
                    instruction = (int) citem.getStartByte();
                }
                int ln = getMethodView().getMethod().getLineNumberTable().getLineNumber(instruction);
                if (ln != -1)
                {
                    // TODO improve printing line numbers
                    sb.append(indent).append("    ").append("/* ").append(ln).append(" */");
                }
            }
            if (citem instanceof Block)
            {
                ((Block) citem).setIndent(indent + "    ");
                sb.append(((Block) citem).getSource());
            }
            else
            {
                String opSource = ((OperationView) citem).source();
                if (opSource != null) sb.append(indent).append("    ").append(opSource).append(";").append(NL);
            }
        }
        return sb.toString();
    }

    protected String alias(String fqn)
    {
        return ImportManager.getInstance().importClass(fqn, getMethodView().getClazzView());
    }

    public void analyze(Block block)
    {
    }

    // Local Variables management

    private HashMap lvars = new HashMap();

    public LocalVariable getLocalVariable(int ivar, String type)
    {
        LocalVariable lv = (LocalVariable) lvars.get(new Integer(ivar));
        Block parentBlock = this;
        while ((lv == null) && (parentBlock != null))
        {
            lv = (LocalVariable) parentBlock.lvars.get(new Integer(ivar));
            parentBlock = parentBlock.parent;
        }
        if (lv != null && type != null && !LocalVariable.UNKNOWN_TYPE.equals(lv.getType()) && !lv.getType().equals(type))
        {
            if (!checkType(type, lv.getType()))
            {
                // Reuse local variable
                // TODO renew of local variables currently is not supported
                //lv = null;
            }
        }
        if (lv != null && type != null && LocalVariable.UNKNOWN_TYPE.equals(lv.getType()))
        {
            lv.renewType(type);
        }
        if (lv == null)
        {
            lv = new LocalVariable(ivar, type, getMethodView());
            lvars.put(new Integer(ivar), lv);
        }
        return lv;
    }

    // Checks if fqn can be casted to fqnBase without special cast operator
    private boolean checkType(String fqn, String fqnBase)
    {
        if (fqn.equals(fqnBase)) return true;

        try
        {
            return widePrimitiveConversion(fqn, fqnBase);
        }
        catch (IllegalArgumentException iae)
        {
            // Continue check
        }

        // Widening Reference Conversions
        // Try to instantiate class
        try
        {
            Class fqnClass = Class.forName(fqn);
            Class fqnBaseClass = Class.forName(fqnBase);
            return fqnBaseClass.isAssignableFrom(fqnClass);
        }
        catch (ClassNotFoundException e)
        {
            // If can not instantiate then always return true
            return true;
        }
    }

    public static boolean widePrimitiveConversion(String type, String wideType)
    {
        // Widening primitive conversions
        if ("byte".equals(type))
        {
            return "short".equals(wideType) || "int".equals(wideType) || "long".equals(wideType) ||
                   "float".equals(wideType) || "double".equals(wideType);
        }
        else if ("short".equals(type))
        {
            return "int".equals(wideType) || "long".equals(wideType) ||
                   "float".equals(wideType) || "double".equals(wideType);
        }
        else if ("char".equals(type))
        {
            return "int".equals(wideType) || "long".equals(wideType) ||
                   "float".equals(wideType) || "double".equals(wideType);
        }
        else if ("int".equals(type))
        {
            return "long".equals(wideType) ||
                   "float".equals(wideType) || "double".equals(wideType);
        }
        else if ("long".equals(type))
        {
            return "float".equals(wideType) || "double".equals(wideType);
        }
        else if ("float".equals(type))
        {
            return "double".equals(wideType);
        }
        else if ("double".equals(type))
        {
            return false;
        }
        else if ("boolean".equals(type))
        {
            return false;
        }
        else if ("void".equals(type))
        {
            return false;
        }
        else
        {
            throw new IllegalArgumentException("Not a primitive type");
        }
    }
}
