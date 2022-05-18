package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class FunctionSheetDay extends BasePage {

    /**Constructor*/
    public FunctionSheetDay(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create Function Sheet Day SFDX")
    public String createFunctionSheetDaySFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder functionSheetDayResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Function_Sheet_Day__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Function Sheet Day create result:");
        System.out.println(functionSheetDayResult);
        String functionSheetDayResultID = JsonParser2.getFieldValue(functionSheetDayResult.toString(), "id");
        return functionSheetDayResultID;
    }

}
