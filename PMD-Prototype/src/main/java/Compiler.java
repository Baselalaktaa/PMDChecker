
import com.google.gson.JsonArray;

import javax.tools.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Compiler {
    private final String input;

    private String errorMessage;
    private String errorCode;

    public Compiler(String input) {
        this.input = input;
        this.errorMessage = "";
        this.errorCode = "";
    }


    //todo: Enum for Difficulty/priority
    //todo: Java Doc @later
    public SimpleJavaFileObject getJavaFileContentFromString() {
        StringBuilder javaFileContent = new StringBuilder("" + "import java.lang.reflect.Method;" +
                "class TestClass {" +
                input
                +
                "public static void print1(){System.out.println(\" hallo \");}" +
                "    public static void main(String[] args) { print1();}"
                +
                "}");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("TestClass.java"))) {
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

        }
        if (result) {
            System.out.println("build Success");
            StyleChecker styleChecker = new StyleChecker();
            styleChecker.run("ruleset.xml");

            //map to save the Diagnostics
            HashMap<String , JsonArray> styleViolations;


            ViolationsFromJsonParser violations = new ViolationsFromJsonParser();

            styleViolations = violations.parse("report.json");
            ErrorAnalyser errorAnalyser = new ErrorAnalyser(styleViolations);
            errorAnalyser.printResultMap();
            return true;
        } else {
            System.out.println("failed..");
        }
        return false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode(){
        return this.errorCode;
    }


    public static void main(String[] args) throws Exception {
        String input = "class X{ int print(int a , int b){  boolean b1 = \"x\" == \"x1\" return a + b;   }" +
                "" +
                "}";

        List<Integer> list = new ArrayList<>();

        boolean b = "x" == "x1";

        Compiler compiler = new Compiler(input);
        compiler.compile();
        System.out.println(compiler.getErrorCode());
        System.out.println(compiler.getErrorMessage());
//        System.out.println("var hier".contains("var"));
    }
}
