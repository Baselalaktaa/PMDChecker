import org.json.simple.JSONObject;

public class StyleFeedbackEntry {
    Long endLine = 0L;
    Long beginLine = 0L;
    String description = "";
    String rule = "";
    Long priority = 0L;

    public StyleFeedbackEntry(Long endLine, Long beginLine, String description, String rule, Long priority) {
        this.endLine = endLine;
        this.beginLine = beginLine;
        this.description = description;
        this.rule = rule;
        this.priority = priority;
    }

    public StyleFeedbackEntry(JSONObject jsonObject) {
        this.endLine = (Long) jsonObject.get("endline");
        this.beginLine = (Long) jsonObject.get("beginline");
        this.description = (String) jsonObject.get("description");
        this.rule = (String) jsonObject.get("rule");
        this.priority = (Long) jsonObject.get("priority");
    }

    public Long getEndLine() {
        return endLine;
    }

    public void setEndLine(Long endLine) {
        this.endLine = endLine;
    }

    public Long getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(Long beginLine) {
        this.beginLine = beginLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "StyleFeedbackEntry{" +
                "endLine='" + endLine + '\'' +
                ", beginLine='" + beginLine + '\'' +
                ", description='" + description + '\'' +
                ", rule='" + rule + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
