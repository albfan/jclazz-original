package ru.andrew.jclazz.apps.tester.suite;

import org.jdom.input.*;
import org.jdom.*;

import java.io.*;
import java.util.*;

import ru.andrew.jclazz.apps.tester.*;
import ru.andrew.jclazz.core.*;
import ru.andrew.jclazz.core.infoj.*;
import ru.andrew.jclazz.decompiler.*;

public class TestSuite
{
    private static final String XML_JAVA_VERSIONS = "java_versions";
    private static final String XML_JAVA_VERSION = "java_version";
    private static final String XML_JAVA_CURRENT_VERSION = "current_version";
    private static final String XML_JAVA_NEXT_VERSION = "next_version";
    private static final String XML_COMPILERS = "compilers";
    private static final String XML_COMPILER = "compiler";
    private static final String XML_COMPILER_NAME_ATTR = "name";
    private static final String XML_COMPILER_JAVA_VERSION_ATTR = "java_version";
    private static final String XML_COMPILER_PATH = "path";
    private static final String XML_COMPILER_DEFAULT_ARGS = "default_args";
    private static final String XML_SOURCES = "sources";
    private static final String XML_SOURCES_JAVA_VERSION_ATTR = "java_version";
    private static final String XML_SOURCES_CLASS = "class";
    private static final String XML_EXTERNAL = "external";
    private static final String XML_EXTERNAL_CLASS = "extclass";
    private static final String XML_EXTERNAL_CLASS_COMPILER_ATTR = "compiler";

    private Map<String, JavaVersion> javaVersions = new HashMap<String, JavaVersion>();
    private Map<String, List<Compiler>> javaCompilers = new HashMap<String, List<Compiler>>();
    private List<Source> sources = new ArrayList<Source>();
    private List<External> externals = new ArrayList<External>();

    private ExecutionLog log = new ExecutionLog();

    public TestSuite(String xmlConfig)
    {
        SAXBuilder sax = new SAXBuilder();
        try
        {
            Element root = sax.build(xmlConfig).getRootElement();

            // Loading Java versions
            for (Object ejvObj : root.getChild(XML_JAVA_VERSIONS).getChildren(XML_JAVA_VERSION))
            {
                Element eJavaVersion = (Element) ejvObj;
                String currentVersion = eJavaVersion.getChild(XML_JAVA_CURRENT_VERSION).getText();
                javaVersions.put(currentVersion, new JavaVersion(currentVersion, eJavaVersion.getChild(XML_JAVA_NEXT_VERSION).getText()));
            }

            // Loading Java compilers
            for (Object jcObj : root.getChild(XML_COMPILERS).getChildren(XML_COMPILER))
            {
                Element eJavaCompiler = (Element) jcObj;
                String jcVersion = eJavaCompiler.getAttributeValue(XML_COMPILER_JAVA_VERSION_ATTR);
                List<Compiler> compilers = javaCompilers.get(jcVersion);
                if (compilers == null)
                {
                    compilers = new ArrayList<Compiler>();
                    javaCompilers.put(jcVersion, compilers);
                }
                compilers.add(new Compiler(eJavaCompiler.getAttributeValue(XML_COMPILER_NAME_ATTR),
                                           jcVersion,
                                           eJavaCompiler.getChild(XML_COMPILER_PATH).getText(),
                                           eJavaCompiler.getChild(XML_COMPILER_DEFAULT_ARGS).getText()));
            }

            // Loading sources
            for (Object sObj : root.getChildren(XML_SOURCES))
            {
                Element eSource = (Element) sObj;
                String sourceJavaVersion = eSource.getAttributeValue(XML_SOURCES_JAVA_VERSION_ATTR);
                for (Object cObj : eSource.getChildren(XML_SOURCES_CLASS))
                {
                    Element eClass = (Element) cObj;
                    sources.add(new Source(eClass.getText(), sourceJavaVersion));
                }
            }

            // Loading external tests
            for (Object eObj : root.getChild(XML_EXTERNAL).getChildren(XML_EXTERNAL_CLASS))
            {
                Element eExternalClass = (Element) eObj;
                externals.add(new External(eExternalClass.getText(), "ext",
                                           eExternalClass.getAttributeValue(XML_EXTERNAL_CLASS_COMPILER_ATTR)));
            }
        }
        catch (JDOMException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void suite()
    {
        // Running standard sources
        for (Source source : sources)
        {
            String version = source.getJavaVersion();
            do
            {
                for (Compiler compiler : javaCompilers.get(version))
                {
                    testAll(source, compiler);
                }

                version = javaVersions.get(version).getNextVersion(); 
            }
            while (version != null && !"".equals(version));
        }

        // Running tests on external classes
        List<Compiler> extCompilers = javaCompilers.get("ext");
        if (extCompilers != null)
        {
            for (External externalClass : externals)
            {
                String compilerName = externalClass.getCompiler();
                for (Compiler comp : extCompilers)
                {
                    if (comp.getName().equals(compilerName))
                    {
                        testExternal(externalClass, comp);
                        break;
                    }
                }
            }
        }

        // Printing logs
        for (String line : log.getLog())
        {
            System.out.println(line);
        }
    }

    private void testAll(Source source, Compiler compiler)
    {
        // Stage 1 - Compiling source
        CompilerWrapper wrapper = new CompilerWrapper(compiler.getCmd() + " " + source.getPath(compiler) + ".java");
        wrapper.start();
        int compile_1_result = wrapper.result();
        if (compile_1_result != 0)
        {
            if (compile_1_result == -13)
            {
                logError(source, compiler, "Compile source terminated by timeout");
            }
            else
            {
                logError(source, compiler, "Compile source error: " + wrapper.getError());
            }
            return;
        }
        new File(source.getPath(compiler) + ".java").renameTo(new File(source.getPath(compiler) + ".java_orig"));

        // Stage 2 - decompiling .class
        Map<String, String> params = new HashMap<String, String>();
        params.put(Params.EXTENSION, "java");
        try
        {
            ClassDecompiler.generateJavaFile(source.getPath(compiler) + ".class", params);
        }
        catch (ClazzException ce)
        {
            logError(source, compiler, "Decompile class error: " + ce.getMessage());
            return;
        }
        catch (Exception ce)
        {
            logError(source, compiler, "Decompile unexpected error", ce);
            return;
        }

        // Stage 3 - Recompiling source
        new File(source.getPath(compiler) + ".class").renameTo(new File(source.getPath(compiler) + "_orig.class"));
        CompilerWrapper wrapper2 = new CompilerWrapper(compiler.getCmd() + " " + source.getPath(compiler) + ".java");
        wrapper2.start();
        int compile_2_result = wrapper2.result();
        if (compile_2_result != 0)
        {
            if (compile_2_result == -13)
            {
                logError(source, compiler, "Compile PASS-2 terminated by timeout");
            }
            else
            {
                logError(source, compiler, "Compile PASS-2 error: " + wrapper2.getError());
            }
            return;
        }

        // Comparing compiled classes
        boolean classComparison1 = FileComparator.compare(source.getPath(compiler) + ".class", source.getPath(compiler) + "_orig.class");
        if (!classComparison1)
        {
            logError(source, compiler, "Compiled Class Comparison FAILED after PASS-2 compilation");
            // Generating infoj files for further comparison
            try
            {
                InfoJCreator.generateInfoFile(source.getPath(compiler) + ".class");
                new File(source.getPath(compiler) + ".jinfo").renameTo(new File(source.getPath(compiler) + ".jinfo"));
                InfoJCreator.generateInfoFile(source.getPath(compiler) + "_orig.class");
                new File(source.getPath(compiler) + ".jinfo").renameTo(new File(source.getPath(compiler) + "_orig.jinfo"));
            }
            catch (IOException ioe)
            {
                throw new RuntimeException(ioe);
            }
            catch (ClazzException ce)
            {
                logError(source, compiler, "InfoJ generation error: " + ce.getMessage());
            }
        }

        // Here we can try to compare with etalon
        if (!new File(source.getPath(compiler) + ".etalon").exists())
        {
            logError(source, compiler, "Etalon file is MISSING");
        }
        else
        {
            boolean etalonComparison = FileComparator.compare(source.getPath(compiler) + ".etalon", source.getPath(compiler) + ".java");
            if (!etalonComparison)
            {
                logError(source, compiler, "Etalon test FAILED");
            }
        }
    }

    private void testExternal(External source, Compiler compiler)
    {
        // Stage 1 - decompiling .class
        Map<String, String> params = new HashMap<String, String>();
        params.put(Params.EXTENSION, "java");
        try
        {
            ClassDecompiler.generateJavaFile(source.getPath(compiler) + ".class", params);
        }
        catch (Exception ce)
        {
            logError(source, compiler, "Decompile external class error: ", ce);
            try
            {
                InfoJCreator.generateInfoFile(source.getPath(compiler) + ".class");
            }
            catch (IOException ioe)
            {
                throw new RuntimeException(ioe);
            }
            catch (ClazzException ce2)
            {
                logError(source, compiler, "InfoJ generation error: " + ce2.getMessage());
            }
            return;
        }

        // Here we can compare it with etalon
        if (!new File(source.getPath(compiler) + ".etalon").exists())
        {
            logError(source, compiler, "Etalon external: etalon file is MISSING");
            return;
        }
        boolean etalonComparison = FileComparator.compare(source.getPath(compiler) + ".etalon", source.getPath(compiler) + ".java");
        if (!etalonComparison)
        {
            logError(source, compiler, "Etalon external test FAILED");
        }
    }

    private void logError(Source source, Compiler compiler, String message)
    {
        log.error("ERROR: <" + source.getName() + ", " + compiler.getName() + "> " + message);
    }

    private void logError(Source source, Compiler compiler, String message, Throwable th)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        th.printStackTrace(pw);
        pw.flush();
        String exc = sw.getBuffer().toString();
        pw.close();
        log.error("ERROR: <" + source.getName() + ", " + compiler.getName() + "> " + message);
        log.error("     STACKTRACE: " + exc);
    }
    
}
