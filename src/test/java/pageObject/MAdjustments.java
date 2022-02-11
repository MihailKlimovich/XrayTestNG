package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class MAdjustments extends BasePage {

    /**Constructor*/
    public MAdjustments(WebDriver driver) {
        super(driver);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////


    @Step("Get MAdjustment SFDX")
    public StringBuilder getMAdjustmentSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder mAdjustmentRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__MAdjustment__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return mAdjustmentRecord;
    }

}
