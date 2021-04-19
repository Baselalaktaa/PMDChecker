import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.util.HashMap;

public class ViolationsFromJsonParser {

    public ViolationsFromJsonParser(){

    }
    public HashMap<String , JsonArray> parse(String filePath){

        HashMap<String ,  JsonArray> result = new HashMap<>();
        JsonParser jsonParser = new JsonParser();
        try {
            Object obj = jsonParser.parse(new FileReader(filePath));
            JsonObject jsonObject = (JsonObject) obj;
            JsonArray files = (JsonArray) jsonObject.get("files");
            for (int i = 0; i < files.size(); i++) {
                JsonObject tempClass = files.get(i).getAsJsonObject();
                String fileName = tempClass.get("filename").toString();
                JsonArray tempViolations =  (JsonArray) tempClass.get("violations");
                result.put(fileName , tempViolations);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
