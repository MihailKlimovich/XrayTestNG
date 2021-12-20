package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class RoomType extends BasePage {

    /**Constructor*/
    public RoomType(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Get Room Type SFDX")
    public StringBuilder getRoomTypeSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return roomTypeRecord;
    }

    @Step("Delete Room Type SFDX")
    public void deleteRoomTypeSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Space_Area__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }


}
