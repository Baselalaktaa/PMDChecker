import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ErrorAnalyser {
    private String errorCode = "";
    private String errorMsg = "";
    private String feedbackMsg = "";
    private List<String> potentialErrors;
    private HashMap<String , JsonArray> styleViolations ;
    private String styleVioDescription;
    private String styleVioRule;
    private int vioLine;
    private HashMap<Integer , String> resultMap;

    public ErrorAnalyser(){
        potentialErrors = new ArrayList<>();

        potentialErrors.add("compiler.err.illegal.start.of.type");
        potentialErrors.add("compiler.err.var.might.not.have.been.initialized");
        potentialErrors.add("compiler.err.expected");
    }


    //todo Key/Value for Errors
    public ErrorAnalyser(HashMap<String , JsonArray> styleViolations){
        potentialErrors = new ArrayList<>();

        potentialErrors.add("compiler.err.illegal.start.of.type");
        potentialErrors.add("compiler.err.var.might.not.have.been.initialized");
        potentialErrors.add("compiler.err.expected");
        this.styleViolations = styleViolations;

        styleVioDescription = "";
        styleVioRule ="";
        vioLine = -1;

        JsonArray violations = new JsonArray();

        resultMap = new HashMap<>();
        for (String key : styleViolations.keySet()){
            violations = styleViolations.get(key);
            for (int i = 0; i < violations.size(); i++) {
                JsonObject tempVio = (JsonObject) violations.get(i);

                styleVioDescription = tempVio.get("description").toString();
                styleVioRule = tempVio.get("rule").toString();
                vioLine = tempVio.get("beginline").getAsInt();
                resultMap.put(i , "Description : " + styleVioDescription + " at line : " + vioLine + " at File : " + key);
            }
        }
    }

    public void printResultMap(){
        for (Integer key : resultMap.keySet()){
            System.out.println( "vio number : " + (key + 1) + " " +  resultMap.get(key));
        }
    }


    public String getFeedbackMsg() {
        analyseError();
        return feedbackMsg;
    }

    public void receiveErrorMsg (String errorMsg){
        this.errorMsg = errorMsg;
    }

    public void receiveErrorCode (String errorCode){
        this.errorCode = errorCode;
    }

    private void analyseError () {

        if (potentialErrors.contains(errorCode)){
            if (errorCode.equals("compiler.err.illegal.start.of.type")){
                feedbackMsg = errorMsg + " sie sollen alle geöffenten Klammern schließen! ";
            }
            if (errorCode.equals("compiler.err.var.might.not.have.been.initialized")){
                feedbackMsg = errorMsg + " man kann ein Variabel so initialisieren : int variabelName = 1;  ";
            }
            if (errorCode.equals("compiler.err.expected")){
                feedbackMsg = errorMsg + " Sie haben eine Semikolen vergissen bzw. eine Klammer geschloßen  und nicht geöffnet ";
            }
        }
    }
}
