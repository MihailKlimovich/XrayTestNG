package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;
import java.util.List;

public class Commission extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        /*StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:auth:jwt:grant",
                "--clientid",
                CONSUMER_KEY,
                "--jwtkeyfile",
                SERVER_KEY_PATH,
                "--username",
                ORG_USERNAME,
                "--instanceurl",
                ORG_URL
        });
        System.out.println(authorise);*/
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
                "Name='TestAccountWithCommissionAuto",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='TestAccountWithoutCommission",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommissionAuto",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestCommissionPackageAuto",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder accountResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='TestAccountWithCommissionAuto' thn__Comm_Activity__c=15 thn__Beverage__c=15 thn__Comm_Equipment__c=15" +
                        " thn__Food__c=15 thn__Room__c=15 thn__Meeting_Room__c=15 thn__Other__c=15 thn__Comm_Packages__c=15",
                "-u",
                ORG_USERNAME,
                "--json"});
        String accountId1 = JsonParser2.getFieldValue(accountResult1.toString(), "id");
        StringBuilder accountResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='TestAccountWithoutCommissionAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String accountId2 = JsonParser2.getFieldValue(accountResult2.toString(), "id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-v",
                "thn__Comm_Activity__c=10 thn__Comm_Beverage__c=10 thn__Comm_Equipment__c=10 thn__Comm_Food__c=10" +
                        " thn__Comm_Hotel_Rooms__c=10 thn__Comm_Meeting_Rooms__c=10 thn__Comm_Other__c=10" +
                        " thn__Comm_Packages__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(res);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestCommissionAuto' thn__Pax__c=1 thn__Hotel__c='" + propertyID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
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
                "Name='TestCommissionAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder accountRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "Account",
                "-w",
                "Name='TestAccountWithCommissionAuto'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        StringBuilder updateQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
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
                "Name='TestCommissionAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder accountRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "Account",
                "-w",
                "Name='TestAccountWithoutCommissionAuto'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        System.out.println(updateQuoteRecord);
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
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

    @Test(priority = 5, description = "Add related records to the quote where it is part of Package ")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommissionAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecordMHD = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecordMHD.toString(), "Id");
        StringBuilder productRecordRoom1Night = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String room1NightID = JsonParser2.getFieldValue(productRecordRoom1Night.toString(), "Id");
        StringBuilder beverageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='BEVERAGE'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder activityRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ACTIVITY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String activityID = JsonParser2.getFieldValue(activityRecord.toString(), "Id");
        StringBuilder equipmentRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='EQUIPMENT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String equipmentID = JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        StringBuilder dinerRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='DINER'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String dinerID = JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestCommissionPackageAuto' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Meeting Room' thn__Package__c='" + packageId + "' thn__Type__c='Meeting Room'" +
                        " thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId1 = JsonParser2.getFieldValue(packageLineResult1.toString(), "id");
        StringBuilder packageLineResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Hotel Room' thn__Package__c='" + packageId + "' thn__Type__c='Hotel Room'" +
                        " thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=100 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId2 = JsonParser2.getFieldValue(packageLineResult2.toString(), "id");
        StringBuilder packageLineResult3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Beer' thn__Package__c='" + packageId + "' thn__Type__c='Beverage'" +
                        " thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00 thn__End_Time__c=16:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId3 = JsonParser2.getFieldValue(packageLineResult3.toString(), "id");
        StringBuilder packageLineResult4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Tennis' thn__Package__c='" + packageId + "' thn__Type__c='Activity'" +
                        " thn__Product__c='" + activityID + "' thn__Start_Time__c=15:00 thn__End_Time__c=16:00" +
                        " thn__Unit_Price__c=40 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId4 = JsonParser2.getFieldValue(packageLineResult4.toString(), "id");
        StringBuilder packageLineResult5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Bicycle' thn__Package__c='" + packageId + "' thn__Type__c='Equipment'" +
                        " thn__Product__c='" + equipmentID + "' thn__Start_Time__c=15:00 thn__End_Time__c=16:00" +
                        " thn__Unit_Price__c=50 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId5 = JsonParser2.getFieldValue(packageLineResult5.toString(), "id");
        StringBuilder packageLineResult6 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Burger' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + dinerID + "' thn__Start_Time__c=15:00 thn__End_Time__c=16:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId6 = JsonParser2.getFieldValue(packageLineResult6.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + quoteID + "' thn__Package__c='" + packageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-v",
                "thn__Comm_Package__c=33",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId +"'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Name='ROOM 1 NIGHT' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder beverageProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='BEVERAGE' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder activityProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='ACTIVITY' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder equipmentProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='EQUIPMENT' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder foodProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='DINER' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteMeetingRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "Name='DEFAULT - MEETING HALF DAY' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder updateQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteCommPackage = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Package__c");
        String commissionPercentageQuotePackage = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Commission__c");
        String commissionPercentageQuoteHotelRoom = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Commission_Percentage__c");
        String commissionPercentageBeverageProduct = JsonParser2.getFieldValue(beverageProductRecord.toString(), "thn__Commission_Percentage__c");
        String commissionPercentageActivityProduct = JsonParser2.getFieldValue(activityProductRecord.toString(), "thn__Commission_Percentage__c");
        String commissionPercentageEquipmentProduct = JsonParser2.getFieldValue(equipmentProductRecord.toString(), "thn__Commission_Percentage__c");
        String commissionPercentageFoodProduct = JsonParser2.getFieldValue(foodProductRecord.toString(), "thn__Commission_Percentage__c");
        String commissionPercentageQuoteMeetingRoom = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Commission_Percentage__c");
        Assert.assertEquals(commissionPercentageQuotePackage, quoteCommPackage);
        Assert.assertEquals(commissionPercentageQuoteHotelRoom, quoteCommPackage);
        Assert.assertEquals(commissionPercentageBeverageProduct, quoteCommPackage);
        Assert.assertEquals(commissionPercentageActivityProduct, quoteCommPackage);
        Assert.assertEquals(commissionPercentageEquipmentProduct, quoteCommPackage);
        Assert.assertEquals(commissionPercentageFoodProduct, quoteCommPackage);
        Assert.assertEquals(commissionPercentageQuoteMeetingRoom, quoteCommPackage);
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId +"'",
                "-u",
                ORG_USERNAME,
                "--json"});
    }

    @Test(priority = 6, description = "Add related records to the quote where it is not  part of Package and update one" +
            " or multiple comm. fields on quote. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommissionAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecordMHD = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecordMHD.toString(), "Id");
        StringBuilder productRecordRoom1Night = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String room1NightID = JsonParser2.getFieldValue(productRecordRoom1Night.toString(), "Id");
        StringBuilder beverageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='BEVERAGE'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder activityRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ACTIVITY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String activityID = JsonParser2.getFieldValue(activityRecord.toString(), "Id");
        StringBuilder equipmentRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='EQUIPMENT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String equipmentID = JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        StringBuilder dinerRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='DINER'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String dinerID = JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" + room1NightID +
                        "' thn__Space_Area__c='" + roomTypeID + "' thn__Sales_Price_incl_Tax__c=300",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomId + "'",
                "-v",
                "thn__Sales_Price_excl_Tax__c=200 thn__Sales_Price_incl_Tax__c=300",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteProductBeverageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" + beverageID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductBeverageId = JsonParser2.getFieldValue(quoteProductBeverageResult.toString(), "id");
        StringBuilder quoteProductActivityResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" + activityID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductActivityId = JsonParser2.getFieldValue(quoteProductActivityResult.toString(), "id");
        StringBuilder quoteProductEquipmentResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" + equipmentID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductEquipmentId = JsonParser2.getFieldValue(quoteProductEquipmentResult.toString(), "id");
        StringBuilder quoteProductDinerResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" + dinerID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductDinerId = JsonParser2.getFieldValue(quoteProductDinerResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" + meetingHalfDayID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-v",
                "thn__Stage__c='2 - Propose' thn__Comm_Activity__c=5 thn__Comm_Beverage__c=6 thn__Comm_Equipment__c=7 thn__Comm_Food__c=8" +
                        " thn__Comm_Hotel_Rooms__c=9 thn__Comm_Meeting_Rooms__c=11 thn__Comm_Other__c=12",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(res);
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder beverageProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductBeverageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder activityProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductActivityId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder equipmentProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductEquipmentId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder foodProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductDinerId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteMeetingRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "Id='" + quoteMeetingRoomId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder updateQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(updateQuoteRecord);
        StringBuilder commissionRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Name FROM thn__Commission__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(commissionRecordsSoql);
        List<String> commissionName = JsonParser2.getFieldValueSoql(commissionRecordsSoql.toString(), "Name");
        String quoteCommActivity = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Activity__c");
        String quoteCommBeverage = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Beverage__c");
        String quoteCommEquipment = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Equipment__c");
        String quoteCommFood = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Food__c");
        String quoteCommHotelRooms = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Hotel_Rooms__c");
        String quoteCommMeetingRooms = JsonParser2.getFieldValue(updateQuoteRecord.toString(), "thn__Comm_Meeting_Rooms__c");
        String commissionPercentageQuoteHotelRoom = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Commission_Percentage__c");
        String commissionPercentageBeverageProduct = JsonParser2.getFieldValue(beverageProductRecord.toString(), "thn__Commission_Percentage__c");
        String commissionPercentageActivityProduct = JsonParser2.getFieldValue(activityProductRecord.toString(), "thn__Commission_Percentage__c");
        String commissionPercentageEquipmentProduct = JsonParser2.getFieldValue(equipmentProductRecord.toString(), "thn__Commission_Percentage__c");
        String commissionPercentageFoodProduct = JsonParser2.getFieldValue(foodProductRecord.toString(), "thn__Commission_Percentage__c");
        String commissionPercentageQuoteMeetingRoom = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Commission_Percentage__c");
        Assert.assertEquals(commissionPercentageQuoteHotelRoom, quoteCommHotelRooms);
        Assert.assertEquals(commissionPercentageBeverageProduct, quoteCommBeverage);
        Assert.assertEquals(commissionPercentageActivityProduct, quoteCommActivity);
        Assert.assertEquals(commissionPercentageEquipmentProduct, quoteCommEquipment);
        Assert.assertEquals(commissionPercentageFoodProduct, quoteCommFood);
        Assert.assertEquals(commissionPercentageQuoteMeetingRoom, quoteCommMeetingRooms);
        Assert.assertEquals(commissionName.size(), 6);
        Assert.assertTrue(commissionName.contains("Hotel Room - 9.00%"));
        Assert.assertTrue(commissionName.contains("Meeting Room - 11.00%"));
        Assert.assertTrue(commissionName.contains("Food - 8.00%"));
        Assert.assertTrue(commissionName.contains("Beverage - 6.00%"));
        Assert.assertTrue(commissionName.contains("Activity - 5.00%"));
        Assert.assertTrue(commissionName.contains("Equipment - 7.00%"));
    }

    @Test(priority = 7, description = "Update some related records comm. %.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission")
    public void case5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommissionAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Name='ROOM 1 NIGHT' thn__MYCE_Quote__c='" + quoteID + "'",
                "-v",
                "thn__Commission_Percentage__c=19",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder beverageProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='BEVERAGE' thn__MYCE_Quote__c='" + quoteID + "'",
                "-v",
                "thn__Commission_Percentage__c=16",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder activityProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='ACTIVITY' thn__MYCE_Quote__c='" + quoteID + "'",
                "-v",
                "thn__Commission_Percentage__c=15",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder equipmentProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='EQUIPMENT' thn__MYCE_Quote__c='" + quoteID + "'",
                "-v",
                "thn__Commission_Percentage__c=17",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder foodProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='DINER' thn__MYCE_Quote__c='" + quoteID + "'",
                "-v",
                "thn__Commission_Percentage__c=18",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteMeetingRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "Name='DEFAULT - MEETING HALF DAY' thn__MYCE_Quote__c='" + quoteID + "'",
                "-v",
                "thn__Commission_Percentage__c=21",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder commissionRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Name FROM thn__Commission__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(commissionRecordsSoql);
        List<String> commissionName = JsonParser2.getFieldValueSoql(commissionRecordsSoql.toString(), "Name");
        Assert.assertEquals(commissionName.size(), 6);
        Assert.assertTrue(commissionName.contains("Hotel Room - 19.00%"));
        Assert.assertTrue(commissionName.contains("Meeting Room - 21.00%"));
        Assert.assertTrue(commissionName.contains("Food - 18.00%"));
        Assert.assertTrue(commissionName.contains("Beverage - 16.00%"));
        Assert.assertTrue(commissionName.contains("Activity - 15.00%"));
        Assert.assertTrue(commissionName.contains("Equipment - 17.00%"));
    }

    @Test(priority = 8, description = "Update one or multiple comm. fields on quote. Expected result: the related which" +
            " comm % value isn’t equal to the previous value on quote are not updated. the others are.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission")
    public void case6() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommissionAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-v",
                "thn__Stage__c='2 - Propose' thn__Comm_Activity__c=15 thn__Comm_Beverage__c=15 thn__Comm_Equipment__c=15 thn__Comm_Food__c=15" +
                        " thn__Comm_Hotel_Rooms__c=15 thn__Comm_Meeting_Rooms__c=15 thn__Comm_Other__c=15",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder commissionRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Name FROM thn__Commission__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(commissionRecordsSoql);
        List<String> commissionName = JsonParser2.getFieldValueSoql(commissionRecordsSoql.toString(), "Name");
        Assert.assertEquals(commissionName.size(), 6);
        Assert.assertTrue(commissionName.contains("Hotel Room - 19.00%"));
        Assert.assertTrue(commissionName.contains("Meeting Room - 21.00%"));
        Assert.assertTrue(commissionName.contains("Food - 18.00%"));
        Assert.assertTrue(commissionName.contains("Beverage - 16.00%"));
        Assert.assertTrue(commissionName.contains("Activity - 15.00%"));
        Assert.assertTrue(commissionName.contains("Equipment - 17.00%"));
    }



}
