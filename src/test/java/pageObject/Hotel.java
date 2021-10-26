package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Hotel extends BasePage {

    /**Constructor*/
    public Hotel(WebDriver driver) {
        super(driver);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Get Hotel SFDX")
    public StringBuilder getHotelSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder hotelRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return hotelRecord;
    }



}
