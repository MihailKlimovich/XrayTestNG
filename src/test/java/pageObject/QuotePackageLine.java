package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class QuotePackageLine extends BasePage {

    /**Constructor*/
    public QuotePackageLine(WebDriver driver) {
        super(driver);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Get Quote Package Line SFDX")
    public StringBuilder getQuotePackageLineSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder quotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return quotePackageLineRecord;
    }

    @Step("Update Quote Package Line SFDX")
    public void updateQuotePackageLineSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder quotePackageLineUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(quotePackageLineUpdateResult);
    }

}
