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


public class PmdExample2 {
    String input = "  boolean print(){  boolean x = \"l\"==\"t\"; return x;   " +
            "" +
            "}";
    public static void main(String[] args) throws IOException {
        String input = "  boolean print(){  boolean x = \"l\"==\"t\"; return x;   " +
                "" +
                "}";
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setMinimumPriority(RulePriority.MEDIUM);
        configuration.setRuleSets("rulesets/java/quickstart.xml");
        configuration.prependClasspath("/home/workspace/target/classes");
        RuleSetFactory ruleSetFactory = RulesetsFactoryUtils.createFactory(configuration);
        File f = File.createTempFile("temp" , "java");
        OutputStream s = new FileOutputStream(f);
        s.write(input.getBytes(StandardCharsets.UTF_8));
        s.flush();
        s.close();
        List<DataSource> files = determineFiles(f.getPath());

        Writer rendererOutput = new StringWriter();
        Renderer renderer = createRenderer(rendererOutput);
        renderer.start();

        RuleContext ctx = new RuleContext();

        ctx.getReport().addListener(createReportListener()); // alternative way to collect violations

        try {
            PMD.processFiles(configuration, ruleSetFactory, files, ctx,
                    Collections.singletonList(renderer));
        } finally {
            ClassLoader auxiliaryClassLoader = configuration.getClassLoader();
            if (auxiliaryClassLoader instanceof ClasspathClassLoader) {
                ((ClasspathClassLoader) auxiliaryClassLoader).close();
            }
        }

        renderer.end();
        renderer.flush();
        System.out.println("Rendered Report:");
        System.out.println(rendererOutput.toString());
    }

    private static ThreadSafeReportListener createReportListener() {
        return new ThreadSafeReportListener() {
            @Override
            public void ruleViolationAdded(RuleViolation ruleViolation) {
                System.out.printf("%-20s:%d %s%n", ruleViolation.getFilename(),
                        ruleViolation.getBeginLine(), ruleViolation.getDescription());
            }

            @Override
            public void metricAdded(Metric metric) {
                // ignored
            }
        };
    }

    private static Renderer createRenderer(Writer writer) {
        XMLRenderer xml = new XMLRenderer("UTF-8");
        xml.setWriter(writer);
        return xml;
    }

    private static List<DataSource> determineFiles(String basePath) throws IOException {
        Path dirPath = FileSystems.getDefault().getPath(basePath);
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.java");

        List<DataSource> files = new ArrayList<>();

        Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                if (matcher.matches(path.getFileName())) {
                    System.out.printf("Using %s%n", path);
                    files.add(new FileDataSource(path.toFile()));
                } else {
                    System.out.printf("Ignoring %s%n", path);
                }
                return super.visitFile(path, attrs);
            }
        });
        System.out.printf("Analyzing %d files in %s%n", files.size(), basePath);
        return files;
    }
}
