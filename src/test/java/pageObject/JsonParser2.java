package pageObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.qameta.allure.Step;
import org.json.JSONObject;
import org.json.*;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

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

    public static void getFieldValue3(StringBuilder json, String key) throws UnsupportedOperationException {

        System.out.println(json);
        JSONObject obj = new JSONObject(json);
        //obj.getJSONObject("result");
        JSONArray arr = obj.getJSONArray("records"); // notice that `"posts": [...]`
        for (int i = 0; i < arr.length(); i++)
        {
            String result = arr.getJSONObject(i).getString(key);
            System.out.println(result);

        }


    }

    /*public static String getFieldValue(String json, String key) {
        JSONObject obj = new JSONObject(json);
        String result = obj.getJSONObject("result").getString(key);
        return result;
    }*/
}
