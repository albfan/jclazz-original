package ru.andrew.jclazz.apps.decomp;

import ru.andrew.jclazz.*;
import ru.andrew.jclazz.apps.*;
import ru.andrew.jclazz.attributes.*;

import java.io.*;
import java.util.*;

public class ClassPrinter
{
    private OutputStream output;
    private String indent = "";
    private boolean printAsAnonymous = false;

    public ClassPrinter(OutputStream output)
    {
        this.output = output;
    }

    public void close() throws IOException
    {
        output.close();
    }

    public void setIndent(String indent)
    {
        this.indent = indent;
    }

    public void setPrintAsAnonymous(boolean printAsAnonymous)
    {
        this.printAsAnonymous = printAsAnonymous;
    }

    public void print(Clazz clazz, boolean withHeader) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        if (!printAsAnonymous)
        {
            if (clazz.isDeprecated())
            {
                pw.println(indent + "/**");
                pw.println(indent + " * @deprecated");
                pw.println(indent + " */");
            }

            pw.print(indent);
            if (clazz.isPublic()) pw.print("public ");
            if (clazz.isFinal() && !clazz.isEnumeration()) pw.print("final ");
            if (clazz.isAbstract() && !clazz.isInterface()) pw.print("abstract ");
            if (clazz.isInterface())
            {
                if (clazz.isAnnotation()) pw.print("@");
                pw.print("interface ");
            }
            else if (clazz.isEnumeration())
            {
                pw.print("enum ");
            }
            else
            {
                pw.print("class ");
            }

            String clazzName = clazz.getThisClassInfo().getName();
            if (clazz.isInnerClass())
            {
                clazzName = clazzName.substring(clazzName.indexOf('$') + 1);
            }
            pw.print(clazzName);

            if (clazz.getClassSignature() == null)
            {
                if ((clazz.getSuperClassInfo() != null) && (!"java.lang.Object".equals(clazz.getSuperClassInfo().getFullyQualifiedName())))
                {
                    pw.print(" extends ");
                    String superClassName = clazz.importClass(clazz.getSuperClassInfo().getFullyQualifiedName());
                    pw.print(superClassName);
                }

                if (clazz.getInterfaces().length > 0 && !clazz.isAnnotation())
                {
                    pw.print(" implements ");
                    for (int i = 0; i < clazz.getInterfaces().length; i++)
                    {
                        String intName = clazz.importClass(clazz.getInterfaces()[i].getFullyQualifiedName());
                        pw.print(intName);
                        if (i != clazz.getInterfaces().length - 1) pw.print(", ");
                    }
                }
            }
            else if (!clazz.isEnumeration())
            {
                pw.print(clazz.getClassSignature().str(clazz));
            }

            pw.println();
        }
        else
        {
            pw.println();
        }

        pw.println(indent + "{");
        // TODO enum class
        if (!clazz.isEnumeration())
        {
            FieldPrinter fp = new FieldPrinter();
            fp.setIndent(indent + "    ");
            for (int i = 0; i < clazz.getFields().length; i++)
            {
                pw.flush();
                fp.print(pw, clazz.getFields()[i]);
            }
            pw.println();

            MethodPrinter mp = new MethodPrinter();
            mp.setIndent(indent + "    ");
            for (int i = 0; i < clazz.getMethods().length; i++)
            {
                if (clazz.getMethods()[i].isInit() && printAsAnonymous) continue;
                pw.flush();
                mp.print(pw, clazz.getMethods()[i], clazz);
            }

            // Printing Inner Classes
            InnerClass[] inners = clazz.getInnerClasses();
            if (inners != null)
            {
                for (int i = 0; i < inners.length; i++)
                {
                    if (inners[i].getInnerClazz() != null)
                    {
                        if (inners[i].isPrinted()) continue;
                        pw.flush();
                        ClassPrinter innerClassPrinter = new ClassPrinter(baos);
                        innerClassPrinter.setIndent("    ");
                        innerClassPrinter.print(inners[i].getInnerClazz(), false);
                        pw.println();
                    }
                }
            }
        }
        else
        {
            pw.print(indent + "    ");
            boolean isFirstVar = true;
            for (int i = 0; i < clazz.getFields().length; i++)
            {
                FieldInfo f_info = clazz.getFields()[i];
                if (f_info.isEnum())
                {
                    if (!isFirstVar) pw.print(", ");
                    pw.print(f_info.getName());
                    isFirstVar = false;
                }
            }
            pw.println();
        }
        pw.print(indent + "}");
        if (!printAsAnonymous) pw.println();

        // Starting print to output
        if (withHeader)
        {
            printHeader(output, clazz);
        }
        pw.flush();
        baos.writeTo(output);
        baos.close();
    }

    private void printHeader(OutputStream out, Clazz clazz)
    {
        PrintWriter pw = new PrintWriter(out);

        if ("yes".equals(clazz.getDecompileParameter(Params.PRINT_HEADER)))
        {
            pw.println("/*");
            pw.println(" Decompiled by Andrew");
            pw.println(" SourceFile: " + (clazz.getSourceFile() != null ? clazz.getSourceFile() : "na"));
            pw.println(" Class Version: " + String.valueOf(clazz.getMajorVersion()) + "." + String.valueOf(clazz.getMinorVersion()));
            pw.println("*/");
            pw.println();
        }

        if (clazz.getThisClassInfo().getPackageName() != null)
        {
            pw.print("package ");
            pw.print(clazz.getThisClassInfo().getPackageName());
            pw.println(";");
            pw.println();
        }

        Collection imports = clazz.getImports();
        for (Iterator iit = imports.iterator(); iit.hasNext();)
        {
            pw.println("import " + iit.next() + ";");
        }
        if (!imports.isEmpty()) pw.println();

        pw.flush();
    }
}
