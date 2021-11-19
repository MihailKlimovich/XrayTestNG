package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Contact extends BasePage {

    /**Constructor*/
    public Contact(WebDriver driver){
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create Contact SFDX")
    public String createContactSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder contactResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "Contact",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Contact create result:");
        System.out.println(contactResult);
        String contactID = JsonParser2.getFieldValue(contactResult.toString(), "id");
        return contactID;
    }

    @Step("Delete Contact SFDX")
    public void deleteContactSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "Contact",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

    @Step("Get Contact SFDX")
    public StringBuilder getContactSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder contactRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "Contact",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return contactRecord;
    }

}
