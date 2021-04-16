import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;

public class PMDExample {

    public static void main(String[] args) {
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths("C:/Users/Basel Alaktaa/Desktop/PMD-Prototype/src/main/java/Code.java");
        configuration.setRuleSets("rulesets/java/quickstart.xml");
        configuration.setReportFormat("json");
        configuration.setReportFile("C:/Users/Basel Alaktaa/Desktop/PMD/report.json");
        PMD.doPMD(configuration);
    }
}
