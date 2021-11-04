package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Deposit extends BasePage {

    /**Constructor*/
    public Deposit(WebDriver driver){
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create Deposit SFDX")
    public String createDepositSFDX(String sfdxPath, String value, String userName) throws IOException, InterruptedException {
        StringBuilder depositResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Deposit__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Deposit create result:");
        System.out.println(depositResult);
        String depositID = JsonParser2.getFieldValue(depositResult.toString(), "id");
        return depositID;
    }

    @Step("Get Deposit SFDX")
    public StringBuilder getDepositSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder depositRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Deposit__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return depositRecord;
    }
}
