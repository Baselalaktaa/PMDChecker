import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;


public class StyleChecker {

    public void run(String ruleSet){

        if (ruleSet== null){
            ruleSet = "rulesets/java/quickstart.xml";
        }


        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths("TestClass.java");
        configuration.setRuleSets(ruleSet);
        configuration.setReportFormat("json");
        configuration.setReportFile("report.json");
        System.out.println(configuration.getRuleSets());
        PMD.doPMD(configuration);

    }
}
