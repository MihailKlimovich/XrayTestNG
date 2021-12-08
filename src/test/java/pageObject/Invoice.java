package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Invoice extends BasePage {

    /**Constructor*/
    public Invoice(WebDriver driver) {
        super(driver);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create Invoice SFDX")
    public String createInvoiceSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder invoiceResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Invoice__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Invoice create result:");
        System.out.println(invoiceResult);
        String invoiceID = JsonParser2.getFieldValue(invoiceResult.toString(), "id");
        return invoiceID;
    }

    @Step("Get Invoice SFDX")
    public StringBuilder getInvoiceSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder invoiceRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Invoice__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return invoiceRecord;
    }

    @Step("Update Invoice SFDX")
    public StringBuilder updateInvoiceSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder invoiceUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Invoice__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(invoiceUpdateResult);
        return invoiceUpdateResult;
    }
}
