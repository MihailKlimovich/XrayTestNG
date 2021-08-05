package pageObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParser {

    public static String getFieldValue(String json, String key) {
        JSONObject obj = new JSONObject(json);
        String result = obj.getJSONObject("result").getString(key);
        return result;
    }

}
