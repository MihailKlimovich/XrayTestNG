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

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void logIn() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);

    }

    @Test(priority = 1, description = "Add Quote Meeting Room, Quote Product, Quote Hotel Room, Quote Package")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestHistoryTrackingAuto1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordFood = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='FOOD'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String foodID = JsonParser2.getFieldValue(productRecordFood.toString(), "Id");
        StringBuilder productRecordWines = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String winesID = JsonParser2.getFieldValue(productRecordWines.toString(), "Id");
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
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestHistoryTrackingAuto1' thn__Hotel__c='" + propertyDemoID + "'",
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
                "Name='Fish and cheeps' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + foodID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId1 = JsonParser2.getFieldValue(packageLineResult1.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Product__c'" +
                        " thn__Operation__c='Created'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        StringBuilder historyRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Package__c'" +
                        " thn__Operation__c='Created'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID2 = JsonParser2.getFieldValue(historyRecord2.toString(), "Id");
        Assert.assertNotNull(historyID1);
        Assert.assertNotNull(historyID2);
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID +
                        "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteMeetingRoomResult);
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + winesID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                        + roomTypeSingleID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder historyRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Meeting_Room__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c='" + quoteMeetingRoomId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID3 = JsonParser2.getFieldValue(historyRecord3.toString(), "Id");
        StringBuilder historyRecord4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Product__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c='" + quoteProductId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID4 = JsonParser2.getFieldValue(historyRecord4.toString(), "Id");
        StringBuilder historyRecord5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Hotel_Room__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c='" + quoteHotelRoomId + "'",
                "-u",
                ORG_USERNAME,
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
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='JohnAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='JohnAuto' thn__Email__c='werty@tut.by' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        System.out.println(paymentResult);
        String paymentID = JsonParser2.getFieldValue(paymentResult.toString(), "id");
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='Payment__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c='" + paymentID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        Assert.assertNotNull(historyID1);
    }

    @Test(priority = 3, description = "Create Invoice records")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking3() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto3'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Invoice__c",
                "-w",
                "thn__Commentaires__c='TestAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder invoiceResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Invoice__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Original_To_Invoice__c=50 thn__to_invoice__c=999" +
                        " thn__Commentaires__c='TestAuto' thn__Type__c='Finale'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(invoiceResult);
        String invoiceID = JsonParser2.getFieldValue(invoiceResult.toString(), "id");
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Invoice__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c='" + invoiceID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        Assert.assertNotNull(historyID1);
    }

    @Test(priority = 4, description = "Update MYCE Quote related records. Updated fields are specified in History" +
            " Settings custom settings")
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
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordWines = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String winesID = JsonParser2.getFieldValue(productRecordWines.toString(), "Id");
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
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID +
                        "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + winesID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                        + roomTypeSingleID + "'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteProductUpdateResult);
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
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomUpdateResult);
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Hotel_Room__c'" +
                        " thn__Operation__c='Updated' thn__Record_Id__c='" + quoteHotelRoomId + "'" +
                        " thn__Field_Name__c='thn__Departure_Date_Time__c'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(historyRecord1);
        StringBuilder historyRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Product__c'" +
                        " thn__Operation__c='Updated' thn__Record_Id__c='" + quoteProductId + "'" +
                        " thn__Field_Name__c='thn__End_Date_Time__c'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder historyRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Meeting_Room__c'" +
                        " thn__Operation__c='Updated' thn__Record_Id__c='" + quoteMeetingRoomId + "'" +
                        " thn__Field_Name__c='thn__Pax__c'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String history1OldValue = JsonParser2.getFieldValue(historyRecord1.toString(), "thn__Old_Value__c");
        String history1NewValue = JsonParser2.getFieldValue(historyRecord1.toString(), "thn__New_Value__c");
        String history2OldValue = JsonParser2.getFieldValue(historyRecord2.toString(), "thn__Old_Value__c");
        String history2NewValue = JsonParser2.getFieldValue(historyRecord2.toString(), "thn__New_Value__c");
        String history3OldValue = JsonParser2.getFieldValue(historyRecord3.toString(), "thn__Old_Value__c");
        String history3NewValue = JsonParser2.getFieldValue(historyRecord3.toString(), "thn__New_Value__c");
        Assert.assertEquals(history1OldValue, date.generateTodayDate2_plus(0, 3) + " 13:30:00");
        Assert.assertEquals(history1NewValue, date.generateTodayDate2_plus(0, 2) + " 00:00:00");
        Assert.assertEquals(history2OldValue, date.generateTodayDate2_plus(0, 0) + " 13:30:00");
        Assert.assertEquals(history2NewValue, date.generateTodayDate2_plus(0, 1) + " 00:00:00");
        Assert.assertEquals(history3OldValue, "10");
        Assert.assertEquals(history3NewValue, "5.0");
    }

    @Test(priority = 5, description = "Update Order")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    //Create new action on Myce Quote object. Action type - lighting component, thn:ProductModification "Order update"
    public void testHistoryTracking5() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto5'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
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
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});

        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("TestHistoryTrackingAuto5");
        myceQuotes.updateOrder();
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Order__c' thn__Operation__c='Flow'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        Assert.assertNotNull(historyID1);
    }

    @Test(priority = 6, description = "Update Payment")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking6() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='JohnAuto1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='JohnAuto2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto6'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder guestResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='JohnAuto1' thn__Email__c='werty1@tut.by' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String guestID1 = JsonParser2.getFieldValue(guestResult1.toString(), "id");
        StringBuilder guestResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='JohnAuto2' thn__Email__c='werty2@tut.by' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
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
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Payment__c' thn__Operation__c='Updated'",
                "-u",
                ORG_USERNAME,
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
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Invoice__c",
                "-w",
                "thn__Commentaires__c='TestAuto2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder invoiceResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Invoice__c",
                "-v",
                "thn__Original_To_Invoice__c=50 thn__to_invoice__c=100" +
                        " thn__Commentaires__c='TestAuto2' thn__Amount_to_invoice__c=10",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__Record_Id__c='" + invoiceID + "' thn__Object__c='thn__Invoice__c' thn__Operation__c='Updated'",
                "-u",
                ORG_USERNAME,
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
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='CloneTestHistoryTrackingAuto8'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto8'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto8' RecordTypeId='" + recordTypeID.get(0) + "'" +
                        " thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID + "' thn__Arrival_Date__c="
                        + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(recordTypeID.get(0));
        System.out.println(myseQuoteResult);
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("TestHistoryTrackingAuto8");
        myceQuotes.cloneMyceQuote("CloneTestHistoryTrackingAuto8", date.generateTodayDate3_plus(0, 0));
        StringBuilder cloneMyceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='CloneTestHistoryTrackingAuto8'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String cloneMyceQuoteID = JsonParser2.getFieldValue(cloneMyceQuoteRecord.toString(), "Id");
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + cloneMyceQuoteID + "' thn__Object__c='thn__MYCE_Quote__c' thn__Operation__c='Flow'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestHistoryTrackingAuto2'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordFood = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='FOOD'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String foodID = JsonParser2.getFieldValue(productRecordFood.toString(), "Id");
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
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestHistoryTrackingAuto2' thn__Hotel__c='" + propertyDemoID + "'",
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
                "Name='Fish and cheeps' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + foodID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId1 = JsonParser2.getFieldValue(packageLineResult1.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("TestHistoryTrackingAuto9");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0 , 0), "Quote Package");
        StringBuilder quotePackageSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Package__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesPackageID= JsonParser2.getFieldValueSoql(quotePackageSoql.toString(), "Id");
        String quotePackageID1 = quotesPackageID.get(0);
        String quotePackageID2 = quotesPackageID.get(1);
        System.out.println(quotePackageID1);
        System.out.println(quotePackageID2);
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Package__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c=" + quotePackageID1 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        StringBuilder historyRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Package__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c=" + quotePackageID2 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID2 = JsonParser2.getFieldValue(historyRecord2.toString(), "Id");
        Assert.assertNotNull(historyID1);
        Assert.assertNotNull(historyID2);
    }

    @Test(priority = 10, description = "Clone Quote hotel room")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    //Add a component for cloning to the page
    public void testHistoryTracking10() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto10'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
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
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto10' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                        + roomTypeSingleID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        //String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("TestHistoryTrackingAuto10");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0 , 0), "Quote Hotel Room");
        StringBuilder quoteHotelRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesHotelRoomsID= JsonParser2.getFieldValueSoql(quoteHotelRoomsSoql.toString(), "Id");
        String quoteHotelRoomID1 = quotesHotelRoomsID.get(0);
        String quoteHotelRoomID2 = quotesHotelRoomsID.get(1);
        System.out.println(quoteHotelRoomID1);
        System.out.println(quoteHotelRoomID2);
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Hotel_Room__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c=" + quoteHotelRoomID1 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        StringBuilder historyRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Hotel_Room__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c=" + quoteHotelRoomID2 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID2 = JsonParser2.getFieldValue(historyRecord2.toString(), "Id");
        Assert.assertNotNull(historyID1);
        Assert.assertNotNull(historyID2);
    }

    @Test(priority = 11, description = "Clone Quote product")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    //Add a component for cloning to the page
    public void testHistoryTracking11() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto11'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordWines = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String winesID = JsonParser2.getFieldValue(productRecordWines.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto11' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + winesID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("TestHistoryTrackingAuto11");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0, 0), "Quote Product");
        StringBuilder quoteProductsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesProductsID = JsonParser2.getFieldValueSoql(quoteProductsSoql.toString(), "Id");
        String quoteProductID1 = quotesProductsID.get(0);
        String quoteProductID2 = quotesProductsID.get(1);
        System.out.println(quoteProductID1);
        System.out.println(quoteProductID2);
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Product__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c=" + quoteProductID1 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        StringBuilder historyRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Product__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c=" + quoteProductID2 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID2 = JsonParser2.getFieldValue(historyRecord2.toString(), "Id");
        Assert.assertNotNull(historyID1);
        Assert.assertNotNull(historyID2);
    }

    @Test(priority = 12, description = "Clone Quote meeting room")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    //Add a component for cloning to the page
    public void testHistoryTracking12() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto12'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
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
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto12' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID +
                        "'",
                "-u",
                ORG_USERNAME,
                "--json"});

        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("TestHistoryTrackingAuto12");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0, 0), "Quote Meetings Room");
        StringBuilder quoteMeetingRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoomsSoql.toString(), "Id");
        String quoteMeetingRoomID1 = quotesMeetingRoomsID.get(0);
        String quoteMeetingRoomID2 = quotesMeetingRoomsID.get(1);
        System.out.println(quoteMeetingRoomID1);
        System.out.println(quoteMeetingRoomID2);
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Meeting_Room__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c=" + quoteMeetingRoomID1 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        StringBuilder historyRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Meeting_Room__c'" +
                        " thn__Operation__c='Created' thn__Record_Id__c=" + quoteMeetingRoomID2 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID2 = JsonParser2.getFieldValue(historyRecord2.toString(), "Id");
        Assert.assertNotNull(historyID1);
        Assert.assertNotNull(historyID2);
    }

    @Test(priority = 13, description = "Change dates")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    //Add a component for cloning to the page
    public void testHistoryTracking13() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto13'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto13' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("TestHistoryTrackingAuto13");
        myceQuotes.changeDate(date.generateTodayDate3_plus(0, 1));
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__MYCE_Quote__c'" +
                        " thn__Operation__c='Flow' thn__Flow_Name__c='Quote change date'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        Assert.assertNotNull(historyID1);
    }

    @Test(priority = 14, description = "Delete Quote meeting room / Quote hotel room / Quote product records")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking14() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto14'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordWines = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String winesID = JsonParser2.getFieldValue(productRecordWines.toString(), "Id");
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
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto14' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID +
                        "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteMeetingRoomResult);
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + winesID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                        + roomTypeSingleID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "Id='" + quoteMeetingRoomId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Meeting_Room__c'" +
                        " thn__Operation__c='Deleted' thn__Record_Name__c='DEFAULT - MEETING HALF DAY'" +
                        " thn__Record_Id__c='" + quoteMeetingRoomId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        StringBuilder historyRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Product__c'" +
                        " thn__Operation__c='Deleted' thn__Record_Name__c='WINES'" +
                        " thn__Record_Id__c='" + quoteProductId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID2 = JsonParser2.getFieldValue(historyRecord2.toString(), "Id");
        StringBuilder historyRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Hotel_Room__c'" +
                        " thn__Operation__c='Deleted' thn__Record_Name__c='ROOM 1 NIGHT'" +
                        " thn__Record_Id__c='" + quoteHotelRoomId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID3 = JsonParser2.getFieldValue(historyRecord3.toString(), "Id");
        Assert.assertNotNull(historyID1);
        Assert.assertNotNull(historyID2);
        Assert.assertNotNull(historyID3);
    }

    @Test(priority = 15, description = "Delete Quote meeting package")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking15() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto15'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestHistoryTrackingAuto3'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordFood = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='FOOD'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String foodID = JsonParser2.getFieldValue(productRecordFood.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto15' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestHistoryTrackingAuto3' thn__Hotel__c='" + propertyDemoID + "'",
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
                "Name='Fish and cheeps' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + foodID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId1 = JsonParser2.getFieldValue(packageLineResult1.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Package__c'" +
                        " thn__Operation__c='Deleted' thn__Record_Name__c='TestHistoryTrackingAuto3'" +
                        " thn__Record_Id__c='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        StringBuilder historyRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Quote_Product__c'" +
                        " thn__Operation__c='Deleted' thn__Record_Name__c='FOOD'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID2 = JsonParser2.getFieldValue(historyRecord2.toString(), "Id");
        Assert.assertNotNull(historyID1);
        Assert.assertNotNull(historyID2);
    }

    @Test(priority = 16, description = "Delete Payment record")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking16() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='JohnAuto3'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto16'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='JohnAuto3' thn__Email__c='werty3@tut.by' thn__Hotel__c='" + propertyDemoID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(guestResult);
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto16' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Reservation_Guest__c='" + guestID + "'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});;
        String paymentID = JsonParser2.getFieldValue(paymentResult.toString(), "id");
        StringBuilder paymentRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Payment__c",
                "-w",
                "Id='" + paymentID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String paymentName = JsonParser2.getFieldValue(paymentRecord.toString(), "Name");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Payment__c",
                "-w",
                "Id='" + paymentID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='Payment__c'" +
                        " thn__Operation__c='Deleted' thn__Record_Name__c='" + paymentName + "'" +
                        " thn__Record_Id__c='" + paymentID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        Assert.assertNotNull(historyID1);
    }

    @Test(priority = 17, description = "Delete Invoice record")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-519: History tracking")
    public void testHistoryTracking17() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Invoice__c",
                "-w",
                "thn__Commentaires__c='TestAuto3'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestHistoryTrackingAuto17'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestHistoryTrackingAuto17' thn__Pax__c=10 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder invoiceResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Invoice__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Original_To_Invoice__c=50 thn__to_invoice__c=50" +
                        " thn__Commentaires__c='TestAuto3' thn__Type__c='Finale'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String invoiceID = JsonParser2.getFieldValue(invoiceResult.toString(), "id");
        StringBuilder invoiceRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Invoice__c",
                "-w",
                "Id='" + invoiceID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String invoiceName = JsonParser2.getFieldValue(invoiceRecord.toString(), "Name");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Invoice__c",
                "-w",
                "Id='" + invoiceID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder historyRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__History__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Object__c='thn__Invoice__c'" +
                        " thn__Operation__c='Deleted' thn__Record_Name__c='" + invoiceName + "'" +
                        " thn__Record_Id__c='" + invoiceID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String historyID1 = JsonParser2.getFieldValue(historyRecord1.toString(), "Id");
        Assert.assertNotNull(historyID1);
    }
}
