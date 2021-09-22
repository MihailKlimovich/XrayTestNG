package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;
import java.util.List;

public class HistoryTracking extends BaseTest {

    @Test(priority = 1, description = "Add Quote Meeting Room, Quote Product, Quote Hotel Room, Quote Package")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking() throws InterruptedException, IOException {
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:auth:jwt:grant",
                "--clientid",
                THY519_578_Key,
                "--jwtkeyfile",
                "/home/user/jdoe/JWT/server.key",
                "--username",
                THY519_578_UserName,
                "--instanceurl",
                "https://test.salesforce.com"
        });
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto1'",
                "-u",
                THY519_578_UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestHistoryTrackingAuto1'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordFood = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='FOOD'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String foodID = JsonParser2.getFieldValue(productRecordFood.toString(), "Id");
        StringBuilder productRecordWines = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String winesID = JsonParser2.getFieldValue(productRecordWines.toString(), "Id");
        StringBuilder productRecordMHD = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecordMHD.toString(), "Id");
        StringBuilder productRecordRoom1Night = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String room1NightID = JsonParser2.getFieldValue(productRecordRoom1Night.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto1' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                THY519_578_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestHistoryTrackingAuto1' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Fish and cheeps' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + foodID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                THY519_578_UserName,
                "--json"});
        String packageLineId1 = JsonParser2.getFieldValue(packageLineResult1.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Product__c' thn__Operation__c='Created'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        StringBuilder historyRecord2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Package__c' thn__Operation__c='Created'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID2 = JsonParser2.getFieldValue(historyRecord2.toString(), "Id");
        Assert.assertNotNull(historyID1);
        Assert.assertNotNull(historyID2);
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID +
                        "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        System.out.println(quoteMeetingRoomResult);
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + winesID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                        + roomTypeSingleID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder historyRecord3 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Meeting_Room__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c='" + quoteMeetingRoomId + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID3 = JsonParser2.getFieldValue(historyRecord3.toString(), "Id");
        StringBuilder historyRecord4 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Product__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c='" + quoteProductId + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID4 = JsonParser2.getFieldValue(historyRecord4.toString(), "Id");
        StringBuilder historyRecord5 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Hotel_Room__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c='" + quoteHotelRoomId + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID5 = JsonParser2.getFieldValue(historyRecord5.toString(), "Id");
        Assert.assertNotNull(historyID3);
        Assert.assertNotNull(historyID4);
        Assert.assertNotNull(historyID5);
    }

    @Test(priority = 2, description = "Create Payment record")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking2() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto2'",
                "-u",
                THY519_578_UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='JohnAuto'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='JohnAuto' thn__Email__c='werty@tut.by' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        System.out.println(guestResult);
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto2' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Reservation_Guest__c='" + guestID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder paymentResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Payment__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Guest__c='" + guestID + "' thn__Gross_Value__c=200" +
                        " thn__Type__c='Cash'",
                "-u",
                THY519_578_UserName,
                "--json"});
        System.out.println(paymentResult);
        String paymentID = JsonParser2.getFieldValue(paymentResult.toString(), "id");
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='Payment__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c='" + paymentID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        Assert.assertNotNull(historyID1);
    }

    @Test(priority = 3, description = "Create Invoice records")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking3() throws InterruptedException, IOException {
        StringBuilder res= SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Invoice__c",
                "-w",
                "thn__Commentaires__c='TestAuto'",
                "-u",
                THY519_578_UserName,
                "--json"});
        System.out.println(res);
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto3'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto3' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                THY519_578_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder invoiceResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Invoice__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Original_To_Invoice__c=50 thn__to_invoice__c=999" +
                        " thn__Commentaires__c='TestAuto'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String invoiceID = JsonParser2.getFieldValue(invoiceResult.toString(), "id");
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Invoice__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c='" + invoiceID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        Assert.assertNotNull(historyID1);
    }

    @Test(priority = 4, description = "Update MYCE Quote related records. Updated fields are specified in History Settings custom settings")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking4() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto4'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordWines = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String winesID = JsonParser2.getFieldValue(productRecordWines.toString(), "Id");
        StringBuilder productRecordMHD = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecordMHD.toString(), "Id");
        StringBuilder productRecordRoom1Night = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String room1NightID = JsonParser2.getFieldValue(productRecordRoom1Night.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto4' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                THY519_578_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID +
                        "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + winesID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                        + roomTypeSingleID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder quoteHotelRoomUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomId + "'",
                "-v",
                "thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 2),
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder quoteProductUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
                "-v",
                "thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 1),
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder quoteMeetingRoomUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "Id='" + quoteMeetingRoomId + "'",
                "-v",
                "thn__Pax__c=5",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Hotel_Room__c'" +
                        " thn__Operation__c='Updated' thn__Record_Id__c='" + quoteHotelRoomId + "'" +
                        " thn__Field_Name__c='thn__Departure_Date_Time__c'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder historyRecord2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Product__c'" +
                        " thn__Operation__c='Updated' thn__Record_Id__c='" + quoteProductId + "'" +
                        " thn__Field_Name__c='thn__End_Date_Time__c'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder historyRecord3 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Meeting_Room__c'" +
                        " thn__Operation__c='Updated' thn__Record_Id__c='" + quoteMeetingRoomId + "'" +
                        " thn__Field_Name__c='thn__Pax__c'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String history1OldValue = JsonParser2.getFieldValue(historyRecord1.toString(), "thn__Old_Value__c");
        String history1NewValue = JsonParser2.getFieldValue(historyRecord1.toString(), "thn__New_Value__c");
        String history2OldValue = JsonParser2.getFieldValue(historyRecord2.toString(), "thn__Old_Value__c");
        String history2NewValue = JsonParser2.getFieldValue(historyRecord2.toString(), "thn__New_Value__c");
        String history3OldValue = JsonParser2.getFieldValue(historyRecord3.toString(), "thn__Old_Value__c");
        String history3NewValue = JsonParser2.getFieldValue(historyRecord3.toString(), "thn__New_Value__c");
        Assert.assertEquals(history1OldValue, date.generateTodayDate2_plus(0, 3) + " 12:30:00");
        Assert.assertEquals(history1NewValue, date.generateTodayDate2_plus(0, 2) + " 00:00:00");
        Assert.assertEquals(history2OldValue, date.generateTodayDate2_plus(0, 0) + " 12:30:00");
        Assert.assertEquals(history2NewValue, date.generateTodayDate2_plus(0, 1) + " 00:00:00");
        Assert.assertEquals(history3OldValue, "10");
        Assert.assertEquals(history3NewValue, "5.0");
    }

    @Test(priority = 5, description = "Update Order")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    //Create new action on Myce Quote object. Action type - lighting component, thn:ProductModification "Order update"
    public void testHistoryTracking5() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto5'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto5' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                THY519_578_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder orderResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Order__c",
                "-v",
                "Name='OrderHistoryTrackingAuto5' thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String orderID = JsonParser2.getFieldValue(orderResult.toString(), "id");
        StringBuilder myceQuoteUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-v",
                "thn__Closed_Status__c='Won'",
                "-u",
                THY519_578_UserName,
                "--json"});
        loginPageForScratchOrg.logInOnScratchOrg2(driver, "https://test.salesforce.com/", THY519_578_UserName, THY519_578_Password);
        homePageForScratchOrg.openAppLauncher();
        homePageForScratchOrg.sendTextInAppWindow("MYCE Quotes");
        myceQuotes.openMyceQoteRecord("TestHistoryTrackingAuto5");
        myceQuotes.updateOrder();
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Order__c' thn__Operation__c='Flow'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        Assert.assertNotNull(historyID1);
    }

    @Test(priority = 6, description = "Update Invoices")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking6() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto6'",
                "-u",
                THY519_578_UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='JohnAuto1'",
                "-u",
                THY519_578_UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='JohnAuto2'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder guestResult1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='JohnAuto1' thn__Email__c='werty1@tut.by' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String guestID1 = JsonParser2.getFieldValue(guestResult1.toString(), "id");
        StringBuilder guestResult2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='JohnAuto2' thn__Email__c='werty2@tut.by' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String guestID2 = JsonParser2.getFieldValue(guestResult2.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto6' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Reservation_Guest__c='" + guestID1 + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder paymentResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Payment__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Guest__c='" + guestID1 + "' thn__Gross_Value__c=200" +
                        " thn__Type__c='Cash'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String paymentID = JsonParser2.getFieldValue(paymentResult.toString(), "id");
        StringBuilder paymentUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Payment__c",
                "-w",
                "Id='" + paymentID + "'",
                "-v",
                "thn__Guest__c='" + guestID2 + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Payment__c' thn__Operation__c='Updated'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        String oldValue = JsonParser2.getFieldValue(historyRecord1.toString(), "thn__Old_Value__c");
        String newValue = JsonParser2.getFieldValue(historyRecord1.toString(), "thn__New_Value__c");
        Assert.assertNotNull(historyID1);
        Assert.assertEquals(oldValue, guestID1);
        Assert.assertEquals(newValue, guestID2);
    }

    @Test(priority = 7, description = "Update Invoices")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    //take out in Field Sets thn__Amount_to_invoice__c
    public void testHistoryTracking7() throws InterruptedException, IOException {
        StringBuilder res= SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Invoice__c",
                "-w",
                "thn__Commentaires__c='TestAuto2'",
                "-u",
                THY519_578_UserName,
                "--json"});
        System.out.println(res);
        StringBuilder invoiceResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Invoice__c",
                "-v",
                "thn__Original_To_Invoice__c=50 thn__to_invoice__c=100" +
                        " thn__Commentaires__c='TestAuto2' thn__Amount_to_invoice__c=10",
                "-u",
                THY519_578_UserName,
                "--json"});
        String invoiceID = JsonParser2.getFieldValue(invoiceResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Invoice__c",
                "-w",
                "Id='" + invoiceID + "'",
                "-v",
                "thn__Amount_to_invoice__c=50",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__Record_Id__c='" + invoiceID + "' thn__Object__c='thn__Invoice__c' thn__Operation__c='Updated'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        String oldValue = JsonParser2.getFieldValue(historyRecord1.toString(), "thn__Old_Value__c");
        String newValue = JsonParser2.getFieldValue(historyRecord1.toString(), "thn__New_Value__c");
        Assert.assertNotNull(historyID1);
        Assert.assertEquals(oldValue, "10.00");
        Assert.assertEquals(newValue, "50.00");
    }

    @Test(priority = 8, description = "Clone Quote")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking8() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto8'",
                "-u",
                THY519_578_UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='CloneTestHistoryTrackingAuto8'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto8' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                THY519_578_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        loginPageForScratchOrg.logInOnScratchOrg2(driver, "https://test.salesforce.com/", THY519_578_UserName, THY519_578_Password);
        Thread.sleep(2000);
        homePageForScratchOrg.openAppLauncher();
        homePageForScratchOrg.sendTextInAppWindow("MYCE Quotes");
        myceQuotes.openMyceQoteRecord("TestHistoryTrackingAuto8");
        myceQuotes.cloneMyceQuote("CloneTestHistoryTrackingAuto8");
        StringBuilder cloneMyceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='CloneTestHistoryTrackingAuto8'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String cloneMyceQuoteID = JsonParser2.getFieldValue(cloneMyceQuoteRecord.toString(), "Id");
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + cloneMyceQuoteID + "' thn__Object__c='thn__MYCE_Quote__c' thn__Operation__c='Flow'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        Assert.assertNotNull(historyID1);
    }

    @Test(priority = 9, description = "Clone Quote package")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    //Add a component for cloning to the page
    public void testHistoryTracking9() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto9'",
                "-u",
                THY519_578_UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestHistoryTrackingAuto2'",
                "-u",
                THY519_578_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordFood = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='FOOD'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String foodID = JsonParser2.getFieldValue(productRecordFood.toString(), "Id");
        StringBuilder productRecordWines = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String winesID = JsonParser2.getFieldValue(productRecordWines.toString(), "Id");
        StringBuilder productRecordMHD = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecordMHD.toString(), "Id");
        StringBuilder productRecordRoom1Night = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String room1NightID = JsonParser2.getFieldValue(productRecordRoom1Night.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto9' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                THY519_578_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestHistoryTrackingAuto2' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Fish and cheeps' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + foodID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                THY519_578_UserName,
                "--json"});
        String packageLineId1 = JsonParser2.getFieldValue(packageLineResult1.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        loginPageForScratchOrg.logInOnScratchOrg2(driver, "https://test.salesforce.com/", THY519_578_UserName, THY519_578_Password);
        Thread.sleep(2000);
        homePageForScratchOrg.openAppLauncher();
        homePageForScratchOrg.sendTextInAppWindow("MYCE Quotes");
        myceQuotes.openMyceQoteRecord("TestHistoryTrackingAuto9");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0 , 0), "Quote Package");
        StringBuilder quotePackageSoql = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Package__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                THY519_578_UserName,
                "--json"});
        List<String> quotesPackageID= JsonParser2.getFieldValueSoql(quotePackageSoql.toString(), "Id");
        String quotePackageID1 = quotesPackageID.get(0);
        String quotePackageID2 = quotesPackageID.get(1);
        System.out.println(quotePackageID1);
        System.out.println(quotePackageID2);

    }

}
