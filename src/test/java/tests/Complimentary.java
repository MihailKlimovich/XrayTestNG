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

public class Complimentary extends BaseTest {

    @Test(priority = 1, description = "THY-540 Myce Quote - Complimentary")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-540 Myce Quote - Complimentary")
    @Story("Case 1: Make Quote having Quote related records linked to the Quote package and not linked to the Quote package Complimentary")
    public void testComplimentary1() throws InterruptedException, IOException {
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
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL);
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
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder productRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecord3.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestComplimentary' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestPackLineComp' thn__Package__c='" + packageId + "' thn__Type__c='Hotel Room'" +
                        " thn__Product__c='" + productID + "' thn__Start_Time__c=00:00 thn__End_Time__c=09:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(packageLineResult);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test Complimentary 1' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
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
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2() + "T00:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2() + "T10:00:00.000+0000 thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypeID + "' thn__Unit_Price__c=10" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=10 thn__Unit_Price__c=10 thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 0) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 1),
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "' thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "id='" + myceQuoteID + "'",
                "-v",
                "thn__Complimentary__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(result);
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(myceQuoteRecord);
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
        System.out.println(quoteHotelRoomRecord);
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteProductRecord);
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
        System.out.println(quoteMeetingRoomRecord);
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageRecord);
        String complimentaryMyceQuote = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Complimentary__c");
        String complimentaryQuoteHotelRoom = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Complimentary__c");
        String complimentaryQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Complimentary__c");
        String complimentaryQuoteMeetingRoom = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Complimentary__c");
        String complimentaryQuotePackage = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Complimentary__c");
        String unitPriceQuoteHotelRoom = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String unitPriceQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String unitPriceQuoteMeetingRoom = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String unitPriceQuotePackage = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price__c");
        String discountQuoteHotelRoom = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String discountQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        String discountQuoteMeetingRoom = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        String discountQuotePackage = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Discount__c");
        Assert.assertEquals(complimentaryMyceQuote, "true");
        Assert.assertEquals(complimentaryQuoteHotelRoom, "true");
        Assert.assertEquals(complimentaryQuoteProduct, "true");
        Assert.assertEquals(complimentaryQuoteMeetingRoom, "true");
        Assert.assertEquals(complimentaryQuotePackage, "true");
        Assert.assertEquals(unitPriceQuoteHotelRoom, "0");
        Assert.assertEquals(unitPriceQuoteProduct, "0");
        Assert.assertEquals(unitPriceQuoteMeetingRoom, "0");
        Assert.assertEquals(unitPriceQuotePackage, "0");
        Assert.assertEquals(discountQuoteHotelRoom, "100");
        Assert.assertEquals(discountQuoteProduct, "100");
        Assert.assertEquals(discountQuoteMeetingRoom, "100");
        Assert.assertEquals(discountQuotePackage, "100");
    }

    @Test(priority = 2, description = "THY-540 Myce Quote - Complimentary")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-540 Myce Quote - Complimentary")
    @Story("Case 2: Set Complimentary to FALSE on Quote with related records after Complimentary was set to TRUE")
    public void testComplimentary2() throws InterruptedException, IOException {
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
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder productRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecord3.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestComplimentary2' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestPackLineComp2' thn__Package__c='" + packageId + "' thn__Type__c='Hotel Room'" +
                        " thn__Product__c='" + productID + "' thn__Start_Time__c=00:00 thn__End_Time__c=09:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(packageLineResult);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test Complimentary 2' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
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
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2() + "T00:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2() + "T10:00:00.000+0000 thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypeID + "' thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=10 thn__Unit_Price__c=10 thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 0) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 1),
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "' thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "id='" + myceQuoteID + "'",
                "-v",
                "thn__Complimentary__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "id='" + myceQuoteID + "'",
                "-v",
                "thn__Complimentary__c=false",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(myceQuoteRecord);
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
        System.out.println(quoteHotelRoomRecord);
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteProductRecord);
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
        System.out.println(quoteMeetingRoomRecord);
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageRecord);
        String complimentaryMyceQuote = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Complimentary__c");
        String complimentaryQuoteHotelRoom = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Complimentary__c");
        String complimentaryQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Complimentary__c");
        String complimentaryQuoteMeetingRoom = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Complimentary__c");
        String complimentaryQuotePackage = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Complimentary__c");
        String unitPriceQuoteHotelRoom = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String unitPriceQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String unitPriceQuoteMeetingRoom = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String unitPriceQuotePackage = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price__c");
        String discountQuoteHotelRoom = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String discountQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        String discountQuoteMeetingRoom = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        String discountQuotePackage = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Discount__c");
        Assert.assertEquals(complimentaryMyceQuote, "false");
        Assert.assertEquals(complimentaryQuoteHotelRoom, "false");
        Assert.assertEquals(complimentaryQuoteProduct, "false");
        Assert.assertEquals(complimentaryQuoteMeetingRoom, "false");
        Assert.assertEquals(complimentaryQuotePackage, "false");
        Assert.assertEquals(unitPriceQuoteHotelRoom, "10");
        Assert.assertEquals(unitPriceQuoteProduct, "10");
        Assert.assertEquals(unitPriceQuoteMeetingRoom, "10");
        Assert.assertEquals(unitPriceQuotePackage, "10");
        Assert.assertEquals(discountQuoteHotelRoom, "0");
        Assert.assertEquals(discountQuoteProduct, "0");
        Assert.assertEquals(discountQuoteMeetingRoom, "0");
        Assert.assertEquals(discountQuotePackage, "0");
    }

    @Test(priority = 3, description = "THY-540 Myce Quote - Complimentary")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-540 Myce Quote - Complimentary")
    @Story("Case 3: Add Quote hotel rooms, Quote meeting rooms, Quote products and Quote meeting packages to the Quote when Complimentary is set to TRUE")
    public void testComplimentary3() throws InterruptedException, IOException {
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
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder productRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecord3.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestComplimentary3' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestPackLineComp3' thn__Package__c='" + packageId + "' thn__Type__c='Hotel Room'" +
                        " thn__Product__c='" + productID + "' thn__Start_Time__c=00:00 thn__End_Time__c=09:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(packageLineResult);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test Complimentary 3' thn__Pax__c=10 thn__Complimentary__c=true thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
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
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2() + "T00:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2() + "T10:00:00.000+0000 thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypeID + "' thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=10 thn__Unit_Price__c=10 thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 0) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 1),
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "' thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(myceQuoteRecord);
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
        System.out.println(quoteHotelRoomRecord);
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteProductRecord);
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
        System.out.println(quoteMeetingRoomRecord);
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageRecord);
        String complimentaryMyceQuote = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Complimentary__c");
        String hotelRoomsAmount = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Hotel_Rooms_Amount__c");
        String meetingRoomsAmount = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Meeting_Rooms_Amount__c");
        String productAmount = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Products_Amount__c");
        String totalAmountExclTax = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        String totalAmountInclTax = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Total_Amount_excl_Tax__c");
        String totalVatAmount = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Total_VAT_Amount__c");
        String unitPriceQuoteHotelRoom = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String unitPriceQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String unitPriceQuoteMeetingRoom = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String unitPriceQuotePackage = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price__c");
        Assert.assertEquals(complimentaryMyceQuote, "true");
        Assert.assertEquals(unitPriceQuoteHotelRoom, "0");
        Assert.assertEquals(unitPriceQuoteProduct, "0");
        Assert.assertEquals(unitPriceQuoteMeetingRoom, "0");
        Assert.assertEquals(unitPriceQuotePackage, "0");
        Assert.assertEquals(hotelRoomsAmount, "0");
        Assert.assertEquals(meetingRoomsAmount, "0");
        Assert.assertEquals(productAmount, "0");
        Assert.assertEquals(totalAmountExclTax, "0");
        Assert.assertEquals(totalAmountInclTax, "0");
        Assert.assertEquals(totalVatAmount, "0");
    }

}
