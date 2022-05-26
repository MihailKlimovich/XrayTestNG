package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class RoomingList extends BasePage {

    /**Constructor*/
    public RoomingList(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Get Rooming List SFDX")
    public StringBuilder getRoomungListSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder roomingListRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Rooming_List__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return roomingListRecord;
    }

    @Step("Update Rooming List SFDX")
    public void updateRoomingListSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder roomingListUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Rooming_List__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(roomingListUpdateResult);
    }

}
