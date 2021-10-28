package pageObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.qameta.allure.Step;
import org.json.JSONObject;
import org.json.*;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.util.ArrayList;
import java.util.List;

public class JsonParser2 extends BasePage {

    /**Constructor*/
    public JsonParser2(WebDriver driver) {
        super(driver);
    }

    @Step("Get value")
    public static String getFieldValue(String json, String key) throws UnsupportedOperationException {
        try {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String result = jsonObject.getAsJsonObject("result").get(key).getAsString();
        return result;
    }catch (UnsupportedOperationException e){
        return null;}
    }


    @Step("Get value2")
    public static String getFieldValue2(String json, String key) throws UnsupportedOperationException {
        try {
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            String result = jsonObject.getAsJsonObject().get(key).getAsString();
            return result;
        }catch (UnsupportedOperationException e){
            return null;}
    }

    @Step("Get value3")
    public static Integer getFieldValueLikeInteger(StringBuilder json, String key1, String key2) throws UnsupportedOperationException {
        try {
            JsonObject jsonObject = new JsonParser().parse(String.valueOf(json)).getAsJsonObject();
            int result = jsonObject.getAsJsonObject(key1).get(key2).getAsInt();
            return result;
        }catch (UnsupportedOperationException e){
            return null;}
    }

    @Step("Get value like double")
    public static Double getFieldValueLikeDouble(StringBuilder json, String key1, String key2) throws UnsupportedOperationException {
        try {
            JsonObject jsonObject = new JsonParser().parse(String.valueOf(json)).getAsJsonObject();
            double result = jsonObject.getAsJsonObject(key1).get(key2).getAsDouble();
            return result;
        }catch (UnsupportedOperationException e){
            return null;}
    }
    @Step("Get value from SOQL (String)")
    public static List<String> getFieldValueSoql(String json, String... keys) throws UnsupportedOperationException {
        JSONObject obj = new JSONObject(json);
        JSONArray array = obj.getJSONObject("result").getJSONArray("records");
        ArrayList <String> records = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            for (String key: keys) {
                String result = array.getJSONObject(i).getString(key);
                records.add(result);
            }
        }
        return records;
    }

    @Step("Get value from SOQL (Integer)")
    public static List<Integer> getFieldValueSoql2(String json, String... keys) throws UnsupportedOperationException {
        JSONObject obj = new JSONObject(json);
        JSONArray array = obj.getJSONObject("result").getJSONArray("records");
        ArrayList<Integer> records = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            for (String key: keys) {
                int result = array.getJSONObject(i).getInt(key);
                records.add(result);
            }
        }
        return records;
    }


}
