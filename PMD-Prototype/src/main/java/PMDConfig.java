import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;

import java.io.File;

public class PMDConfig {

    public void run(File... files){
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths("C:/Users/Basel Alaktaa/Desktop/PMD/TestClass.java");
        configuration.setRuleSets("rulesets/java/quickstart.xml");
        configuration.setReportFormat("json");
        configuration.setReportFile("C:/Users/Basel Alaktaa/Desktop/PMD/report.json");
        PMD.doPMD(configuration);
    }
}
