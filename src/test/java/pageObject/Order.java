package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Order extends BasePage {

    public Order(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Get Order SFDX")
    public StringBuilder getOrderSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder orderRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Order__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return orderRecord;
    }

    @Step("Get Order Line SFDX")
    public StringBuilder getOrderLineSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder orderLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Order_Line__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return orderLineRecord;
    }

    @Step("Update Order SFDX")
    public void updateOrderSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder quoteMeetingRoomUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Order__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(quoteMeetingRoomUpdateResult);
    }



}
