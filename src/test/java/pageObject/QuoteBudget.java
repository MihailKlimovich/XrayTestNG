package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class QuoteBudget extends BasePage {

    /**Constructor*/
    public QuoteBudget(WebDriver driver) {
        super(driver);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////


    @Step("Get Quote Budget  SFDX")
    public StringBuilder getQuoteBudgetSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder quoteBudgetRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Quote_Budget__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return quoteBudgetRecord;
    }

}
