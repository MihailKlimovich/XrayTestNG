package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Payment extends BasePage {

    /**Constructor*/
    public Payment(WebDriver driver) {
        super(driver);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create Payment SFDX")

    public String createPaymentSFDX(String sfdxPath, String value, String userName) throws IOException, InterruptedException {
        StringBuilder paymentResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Payment__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Payment create result:");
        System.out.println(paymentResult);
        String paymentID = JsonParser2.getFieldValue(paymentResult.toString(), "id");
        return paymentID;
    }

    @Step("Update Payment SFDX")
    public void updatePaymentSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder paymentUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Payment__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(paymentUpdateResult);
    }

    @Step("Get Payment SFDX")
    public StringBuilder getPaymentSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder paymentRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Payment__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return paymentRecord;
    }

}
