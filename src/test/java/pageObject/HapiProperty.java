package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class HapiProperty extends BasePage {


    /**Constructor*/
    public HapiProperty(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Get Hapi Property SFDX")
    public StringBuilder getHapiPropertySFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder hapiPropertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "hapi__Property__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return hapiPropertyRecord;
    }

}
