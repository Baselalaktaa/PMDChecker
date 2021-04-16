import java.util.ArrayList;
import java.util.List;

public class ErrorAnalyser {
    private String errorCode = "";
    private String errorMsg = "";
    private String feedbackMsg = "";
    private List<String> potentialErrors;

    public ErrorAnalyser(){
        potentialErrors = new ArrayList<>();

        potentialErrors.add("compiler.err.illegal.start.of.type");
        potentialErrors.add("compiler.err.var.might.not.have.been.initialized");
        potentialErrors.add("compiler.err.expected");
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
