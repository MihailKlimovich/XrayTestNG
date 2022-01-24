package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Rate extends BasePage {

    /**Constructor*/
    public Rate(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Create Rate SFDX")
    public String createRateSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder rateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Rate__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Rate create result:");
        System.out.println(rateResult);
        String rateID = JsonParser2.getFieldValue(rateResult.toString(), "id");
        return rateID;
    }

    @Step("Delete Rate SFDX")
    public void deleteRateSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Rate__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

    @Step("Get Rate SFDX")
    public StringBuilder getRateSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder rateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Rate__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return rateRecord;
    }

}
