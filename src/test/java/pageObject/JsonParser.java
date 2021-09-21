package pageObject;

import io.qameta.allure.Step;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.util.Iterator;

public class JsonParser extends BasePage {

    /**Constructor*/
    public JsonParser(WebDriver driver) {
        super(driver);
    }

    @Step("Get value for Json Array")
    public static String getFieldValue(String jsonString, String key) throws ParseException {
        Object obj = new JSONParser().parse(jsonString);
        JSONObject jo = (JSONObject) obj;
        JSONArray records = (JSONArray) jo.get("records");
        Iterator recordsItr = records.iterator();
        while (recordsItr.hasNext()) {
            JSONObject test = (JSONObject) recordsItr.next();
            String value = (String) test.get(key);
            return value;
        }
        return null;

    }

}
