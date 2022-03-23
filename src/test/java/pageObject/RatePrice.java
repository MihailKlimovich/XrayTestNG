package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class RatePrice extends BasePage {

    /**Constructor*/
    public RatePrice(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Create Rate Price SFDX")
    public String createRatePriceSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder ratePriceResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Rate_Price__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Rate Price create result:");
        System.out.println(ratePriceResult);
        String ratePriceID = JsonParser2.getFieldValue(ratePriceResult.toString(), "id");
        return ratePriceID;
    }

    @Step("Get Rate Price SFDX")
    public StringBuilder getRatePriceSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder ratePriceRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Rate_Price__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return ratePriceRecord;
    }

}
