package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Availability extends BasePage {

    /**Constructor*/

    public Availability(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Create Availability SFDX")
    public String createAvailabilitySFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder availabilityResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Availability__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Availability create result:");
        System.out.println(availabilityResult);
        String availabilityID = JsonParser2.getFieldValue(availabilityResult.toString(), "id");
        return availabilityID;
    }

    @Step("Get Availability SFDX")
    public StringBuilder getAvailabilitySFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder availabilityRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Availability__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return availabilityRecord;
    }

    @Step("Delete Availability SFDX")
    public StringBuilder deleteAvailabilitySFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Availability__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
        return result;
    }

}
