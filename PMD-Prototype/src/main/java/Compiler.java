
import com.google.gson.JsonArray;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.RulesetsFactoryUtils;
import net.sourceforge.pmd.ThreadSafeReportListener;
import net.sourceforge.pmd.renderers.Renderer;
import net.sourceforge.pmd.renderers.XMLRenderer;
import net.sourceforge.pmd.stat.Metric;
import net.sourceforge.pmd.util.ClasspathClassLoader;
import net.sourceforge.pmd.util.datasource.DataSource;
import net.sourceforge.pmd.util.datasource.FileDataSource;

import javax.tools.*;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.*;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Compiler {
    private final String input;

    private String errorMessage;
    private String errorCode;

    public Compiler(String input) {
        this.input = input;
        this.errorMessage = "";
        this.errorCode = "";
    }

    //todo: Enum for Input as Whole Class or Just Method
    //todo :Enum for Difficulty
    public SimpleJavaFileObject getJavaFileContentFromString() {
        StringBuilder javaFileContent = new StringBuilder("" + "import java.lang.reflect.Method;" +
                "class TestClass {" +
                input
                +
                "public static void print1(){System.out.println(\" hallo \");}" +
                "    public static void main(String[] args) { print1();}"
                +
                "}");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/user/Desktop/Test1/PMDChecker/TestClass.java"))) {
            writer.write(javaFileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parsing
        JavaObjectFromString javaObjectFromString = null;
        try {
            javaObjectFromString = new JavaObjectFromString("TestClass", javaFileContent.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return javaObjectFromString;
    }

    public boolean compile() throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector diagnosticCollector = new DiagnosticCollector();
        StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        JavaFileObject javaFileObjectFromString = getJavaFileContentFromString();
        Iterable<JavaFileObject> fileObjects = Collections.singletonList(javaFileObjectFromString);

        StringWriter output = new StringWriter();

        JavaCompiler.CompilationTask task = compiler.getTask(output, standardJavaFileManager, diagnosticCollector, null, null, fileObjects);
        Boolean result = task.call();

        List<Diagnostic> diagnostics = diagnosticCollector.getDiagnostics();
        for (Diagnostic diagnostic : diagnostics) {
            errorMessage = diagnostic.getMessage(null);
            errorCode =  diagnostic.getCode();
            System.out.println("msg : " +  errorMessage);
            System.out.println("code : "  + diagnostic.getCode());
            System.out.println("source : " + diagnostic.getSource());
            System.out.println("Kind : " + diagnostic.getKind());
        }
        if (result) {
            System.out.println("build Success");
//            File f = File.createTempFile("temp" , ".java");
//            OutputStream s = new FileOutputStream(f);
//            s.write(input.getBytes(StandardCharsets.UTF_8));
//            s.flush();
//            s.close();
//
//            InputStream inputStream = new FileInputStream(f);
//            byte[] bbb =  inputStream.readAllBytes();

            PMDConfiguration configuration = new PMDConfiguration();
            configuration.setInputPaths("C:/Users/user/Desktop/Test1/PMDChecker/TestClass.java");
           // configuration.setRuleSets("rulesets/java/quickstart.xml");
            List<String> list = new LinkedList<>();
            list.add("rulesets/java/quickstart.xml");
            list.add("category/java/documentation.xml");
            list.add("category/java/codestyle.xml");
            for(int i = 0;i<list.size();i++) {
                configuration.setRuleSets(list.get(i));
                configuration.setReportFormat("json");
                configuration.setReportFile("C:/Users/user/Desktop/Test1/PMDChecker/report.json");

                PMD.doPMD(configuration);
                readReports("C:/Users/user/Desktop/Test1/PMDChecker/report.json");
            }
            return true;
        } else {
            System.out.println("failed..");
        }
        return false;
    }

    public static JSONArray readReports(String reportPath) {
        JSONParser parser = new JSONParser();
        JSONArray result = new JSONArray();
        try {
            Object obj = parser.parse(new FileReader("C:/Users/user/Desktop/Test1/PMDChecker/report.json"));

            JSONObject jsonObject =  (JSONObject) obj;

            JSONArray jsonArrayFiles = (JSONArray) jsonObject.get("files");
            JSONObject jsonArrayFile = (JSONObject) jsonArrayFiles.get(0);
            JSONArray jsonArrayViolations = (JSONArray) jsonArrayFile.get("violations");
            result = jsonArrayViolations;
            List<StyleFeedbackEntry> styleFeedbackEntries = new LinkedList<>();
            jsonArrayViolations.forEach(  var -> {
                JSONObject jsonObject1 = (JSONObject) var;
                if(
                        (Long)jsonObject1.get("priority") <=5
                ) {
                    styleFeedbackEntries.add(new StyleFeedbackEntry(jsonObject1));
                }
            });

            styleFeedbackEntries.forEach(System.out::println);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode(){
        return this.errorCode;
    }


    public static void main(String[] args) throws Exception {
        String input = "  boolean print(){  boolean x = \"l\"==\"t\"; return x;   " +
                "" +
                "}";

        List<Integer> list = new ArrayList<>();
boolean x = "l"=="t";


        Compiler compiler = new Compiler(input);

        compiler.compile();

        System.out.println(compiler.getErrorCode());
        System.out.println(compiler.getErrorMessage());

//        System.out.println("var hier".contains("var"));

    }
}
