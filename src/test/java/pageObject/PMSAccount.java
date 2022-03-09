package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class PMSAccount extends BasePage {

    /**Constructor*/
    public PMSAccount(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create PMS Account SFDX")
    public String createPMSAccountSFDX(String sfdxPath, String value, String userName) throws IOException, InterruptedException {
        StringBuilder pmsAccountResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__PMS_Account__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("PMS Account create result:");
        System.out.println(pmsAccountResult);
        String pmsAccountID = JsonParser2.getFieldValue(pmsAccountResult.toString(), "id");
        return pmsAccountID;
    }

    @Step("Get PMS Account SFDX")
    public StringBuilder getPMSAccountSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder pmsAccountRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__PMS_Account__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return pmsAccountRecord;
    }

    @Step("Delete PMS Account SFDX")
    public void deletePMSAccountSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__PMS_Account__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

    @Step("Update PMS Account SFDX")
    public void updatePMSaccountSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder pmsAccountUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__PMS_Account__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(pmsAccountUpdateResult);
    }



}
