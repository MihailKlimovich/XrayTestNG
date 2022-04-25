package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class QuoteAnalytics extends BasePage {

    /**Constructor*/
    public QuoteAnalytics(WebDriver driver) {
        super(driver);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Get Quote Analytics SFDX")
    public StringBuilder getQuoteAnalyticsSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder quoteAnalyticsRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Quote_Analytics__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return quoteAnalyticsRecord;
    }

}
