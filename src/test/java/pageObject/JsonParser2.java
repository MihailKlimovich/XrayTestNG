package pageObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

public class JsonParser2 {

    public static String getFieldValue(String json, String key) throws UnsupportedOperationException {
        try {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String result = jsonObject.getAsJsonObject("result").get(key).getAsString();
        return result;
    }catch (UnsupportedOperationException e){
        return null;}
    }

    /*public static String getFieldValue(String json, String key) {
        JSONObject obj = new JSONObject(json);
        String result = obj.getJSONObject("result").getString(key);
        return result;
    }*/
}
