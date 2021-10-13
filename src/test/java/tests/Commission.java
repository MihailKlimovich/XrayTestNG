package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;

public class Commission extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission")
    public void logIn() throws InterruptedException, IOException {
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:auth:jwt:grant",
                "--clientid",
                key,
                "--jwtkeyfile",
                serverKeyPath,
                "--username",
                ALIAS,
                "--instanceurl",
                urlForScratch
        });
        System.out.println(authorise);
    }

    @Test(priority = 2, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission")
    public void preconditions() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='TestAccountWithCommission",
                "-u",
                ALIAS,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='TestAccountWithoutCommission",
                "-u",
                ALIAS,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommission",
                "-u",
                ALIAS,
                "--json"});
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                ALIAS,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder accountResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='TestAccountWithCommission' thn__Comm_Activity__c=15 thn__Beverage__c=15 thn__Comm_Equipment__c=15" +
                        " thn__Food__c=15 thn__Room__c=15 thn__Meeting_Room__c=15 thn__Other__c=15 thn__Comm_Packages__c=15",
                "-u",
                ALIAS,
                "--json"});
        String accountId1 = JsonParser2.getFieldValue(accountResult1.toString(), "id");
        StringBuilder accountResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='TestAccountWithoutCommission'",
                "-u",
                ALIAS,
                "--json"});
        String accountId2 = JsonParser2.getFieldValue(accountResult2.toString(), "id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-v",
                "thn__Comm_Activity__c=10 thn__Comm_Beverage__c=10 thn__Comm_Equipment__c=10 thn__Comm_Food__c=10" +
                        " thn__Comm_Hotel_Rooms__c=10 thn__Comm_Meeting_Rooms__c=10 thn__Comm_Other__c=10" +
                        " thn__Comm_Packages__c=10",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(res);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestCommission' thn__Pax__c=1 thn__Hotel__c='" + propertyID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ALIAS,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
    }

    @Test(priority = 3, description = "Add an agent or a company to the quote (Account with Commission) and" +
            " change the field 'commission to' to the value of the field you completed")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommission'",
                "-u",
                ALIAS,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder accountRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "Account",
                "-w",
                "Name='TestAccountWithCommission'",
                "-u",
                ALIAS,
                "--json"});
        String accountID = JsonParser2.getFieldValue(accountRecord.toString(), "Id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-v",
                "thn__Company__c='" + accountID + "' thn__Commission_to__c='Company' thn__Commissionable__c=true",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(res);
        StringBuilder updateQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(updateQuoteRecord);
        String quoteCommActivity = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Activity__c");
        String quoteCommBeverage = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Beverage__c");
        String quoteCommEquipment = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Equipment__c");
        String quoteCommFood = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Food__c");
        String quoteCommHotelRooms = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Hotel_Rooms__c");
        String quoteCommMeetingRooms = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Meeting_Rooms__c");
        String quoteCommOther = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Other__c");
        String quoteCommPackage = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Package__c");
        String accountCommActivity = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Comm_Activity__c");
        String accountCommBeverage = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Beverage__c");
        String accountCommEquipment = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Comm_Equipment__c");
        String accountCommFood = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Food__c");
        String accountCommHotelRooms = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Room__c");
        String accountCommMeetingRooms = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Meeting_Room__c");
        String accountCommOther = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Other__c");
        String accountCommPackage = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Comm_Packages__c");
        Assert.assertEquals(quoteCommActivity, accountCommActivity);
        Assert.assertEquals(quoteCommBeverage, accountCommBeverage);
        Assert.assertEquals(quoteCommEquipment, accountCommEquipment);
        Assert.assertEquals(quoteCommFood, accountCommFood);
        Assert.assertEquals(quoteCommHotelRooms, accountCommHotelRooms);
        Assert.assertEquals(quoteCommMeetingRooms, accountCommMeetingRooms);
        Assert.assertEquals(quoteCommOther, accountCommOther);
        Assert.assertEquals(quoteCommPackage, accountCommPackage);
    }

    @Test(priority = 4, description = "Update an agent or a company to the quote (Account without Commission) and" +
            " change the field 'commission to' to the value of the field you completed")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommission'",
                "-u",
                ALIAS,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder accountRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "Account",
                "-w",
                "Name='TestAccountWithoutCommission'",
                "-u",
                ALIAS,
                "--json"});
        String accountID = JsonParser2.getFieldValue(accountRecord.toString(), "Id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-v",
                "thn__Company__c='" + accountID + "' thn__Commission_to__c='Company' thn__Commissionable__c=true",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(res);
        StringBuilder updateQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(updateQuoteRecord);
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                ALIAS,
                "--json"});
        String propertyCommActivity = JsonParser2.getFieldValue(propertyRecord.toString(), "thn__Comm_Activity__c");
        String propertyCommBeverage = JsonParser2.getFieldValue(propertyRecord.toString(), "thn__Comm_Beverage__c");
        String propertyCommEquipment = JsonParser2.getFieldValue(propertyRecord.toString(), "thn__Comm_Equipment__c");
        String propertyCommFood  = JsonParser2.getFieldValue(propertyRecord.toString(), "thn__Comm_Food__c");
        String propertyHotelRooms= JsonParser2.getFieldValue(propertyRecord.toString(), "thn__Comm_Hotel_Rooms__c");
        String propertyMeetingRooms  = JsonParser2.getFieldValue(propertyRecord.toString(), "thn__Comm_Meeting_Rooms__c");
        String propertyCommOther = JsonParser2.getFieldValue(propertyRecord.toString(), "thn__Comm_Other__c");
        String propertyCommPackage = JsonParser2.getFieldValue(propertyRecord.toString(), "thn__Comm_Packages__c");
        String quoteCommActivity = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Activity__c");
        String quoteCommBeverage = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Beverage__c");
        String quoteCommEquipment = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Equipment__c");
        String quoteCommFood = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Food__c");
        String quoteCommHotelRooms = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Hotel_Rooms__c");
        String quoteCommMeetingRooms = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Meeting_Rooms__c");
        String quoteCommOther = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Other__c");
        String quoteCommPackage = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Package__c");
        Assert.assertEquals(quoteCommActivity, propertyCommActivity);
        Assert.assertEquals(quoteCommBeverage, propertyCommBeverage);
        Assert.assertEquals(quoteCommEquipment, propertyCommEquipment);
        Assert.assertEquals(quoteCommFood, propertyCommFood);
        Assert.assertEquals(quoteCommHotelRooms, propertyHotelRooms);
        Assert.assertEquals(quoteCommMeetingRooms, propertyMeetingRooms);
        Assert.assertEquals(quoteCommOther, propertyCommOther);
        Assert.assertEquals(quoteCommPackage, propertyCommPackage);
    }

}
