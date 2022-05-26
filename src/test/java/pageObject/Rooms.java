package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Rooms extends BasePage {

    /**Constructor*/
    public Rooms(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Get Room SFDX")
    public StringBuilder getRoomSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder roomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Space__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return roomRecord;
    }

}
