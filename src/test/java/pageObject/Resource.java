package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Resource extends BasePage {

    /**Constructor*/
    public Resource(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create Resource SFDX")
    public String createResourceSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder resourceResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Resource__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Quote create result:");
        System.out.println(resourceResult);
        String resourceID = JsonParser2.getFieldValue(resourceResult.toString(), "id");
        return resourceID;
    }

    @Step("Delete Resource SFDX")
    public void deleteResourceSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Resource__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

    @Step("Get Resource SFDX")
    public StringBuilder getResourceSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder resourceRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Resource__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return resourceRecord;
    }

    @Step("Update Resource SFDX")
    public StringBuilder updateResourceSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder resourceUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Resource__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(resourceUpdateResult);
        return resourceUpdateResult;
    }





}
