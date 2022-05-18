package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class FunctionSheet extends BasePage {

    /**Constructor*/
    public FunctionSheet(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create Function Sheet SFDX")
    public String createFunctionSheetSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder functionSheetResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Function_Sheet__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Function Sheet create result:");
        System.out.println(functionSheetResult);
        String functionSheetResultID = JsonParser2.getFieldValue(functionSheetResult.toString(), "id");
        return functionSheetResultID;
    }

    @Step("Delete Function Sheet SFDX")
    public void deleteFunctionSheetSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Function_Sheet__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

    @Step("Get Function Sheet SFDX")
    public StringBuilder getFunctionSheetSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder functionSheetRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Function_Sheet__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return functionSheetRecord;
    }


}
