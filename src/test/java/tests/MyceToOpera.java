package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyceToOpera extends BaseTest {

    @Test(priority = 1, description = "Add in Quote 1 hotel room and sent to PMS")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-589: MYCE to Opera")
    public void testMyceToOpera_WhereOneHotelRoom() throws InterruptedException, IOException {
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:auth:jwt:grant",
                "--clientid",
                THY589Key,
                "--jwtkeyfile",
                "/home/user/jdoe/JWT/server.key",
                "--username",
                THY589UserName,
                "--instanceurl",
                "https://test.salesforce.com"
        });
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestMyceToOperaAuto1'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Id='" + propertyDemoID + "'",
                "-v",
                "thn__HotelCode__c='DEMO_HOTEL_CODE' thn__HotelBrand__c='DEMO_BRAND_CODE' thn__Unique_Id__c='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(updateResult);
        StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestMyceToOperaAuto1' thn__Pax__c=10 thn__Release_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Closed_Status__c='Won'" +
                        " thn__Shoulder_Start_Date__c=" + date.generateTodayDate2() + " thn__Shoulder_End_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                THY589UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightID +
                        "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeSingleID + "'",
                "-u",
                THY589UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-v",
                "thn__SendToPMS__c=true",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(myceQuoteRecord);
        StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRecord);
        StringBuilder propertyDemoRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Id='" + propertyDemoID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockID= JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        StringBuilder pmsBlockInventoryRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID + "'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder pmsBlockRateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRateRecord);
        String pmsBlockRateMessage= JsonParser2.getFieldValue2(pmsBlockRateRecord.toString(), "message");
        String pmsBlockInventoryStartDate = JsonParser2.getFieldValue(pmsBlockInventoryRecord.toString(), "thn__Start__c");
        String pmsBlockInventoryEndDate = JsonParser2.getFieldValue(pmsBlockInventoryRecord.toString(), "thn__End__c");
        String pmsBlockInventoryRoomType = JsonParser2.getFieldValue(pmsBlockInventoryRecord.toString(), "thn__RoomType__c");
        String myceQuoteName = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "Name");
        String pmsBlockName = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Name");
        String myceQuoteShoulderStartDate = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Shoulder_Start_Date__c");
        String pmsBlockShoulderStartDate = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__StartShoulderDate__c");
        String myceQuoteShoulderEndDate = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Shoulder_End_Date__c");
        String pmsBlockShoulderEndDate = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__EndShoulderDate__c");
        String myceQuoteArrivalDate = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Arrival_Date__c");
        String pmsBlockArrivalDate = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__Start__c");
        String myceQuoteDepartureDate = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Departure_Date__c");
        String pmsBlockDepartureDate = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__End__c");
        String myceQuoteReleaseDate = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Release_Date__c");
        String pmsBlockReleaseDate = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Release_Date_Time__c");
        String propertyHotelCode = JsonParser2.getFieldValue(propertyDemoRecord2.toString(), "thn__HotelCode__c");
        String pmsBlockHotelCode = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PropertyDetailsCode__c");
        String propertyHotelBrandCode = JsonParser2.getFieldValue(propertyDemoRecord2.toString(), "thn__HotelBrand__c");
        String pmsBlockHotelBrandCode = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PropertyDetailsBrandCode__c");
        Assert.assertEquals(pmsBlockName, myceQuoteName);
        Assert.assertEquals(pmsBlockShoulderStartDate, myceQuoteShoulderStartDate);
        Assert.assertEquals(pmsBlockShoulderEndDate, myceQuoteShoulderEndDate);
        Assert.assertEquals(pmsBlockArrivalDate, myceQuoteArrivalDate);
        Assert.assertEquals(pmsBlockDepartureDate, myceQuoteDepartureDate);
        Assert.assertTrue(pmsBlockReleaseDate.contains(myceQuoteReleaseDate));
        Assert.assertEquals(pmsBlockHotelCode, propertyHotelCode);
        Assert.assertEquals(pmsBlockHotelBrandCode, propertyHotelBrandCode);
        Assert.assertEquals(pmsBlockInventoryStartDate, myceQuoteArrivalDate);
        Assert.assertEquals(pmsBlockInventoryEndDate, myceQuoteDepartureDate);
        Assert.assertEquals(pmsBlockInventoryRoomType, roomTypeSingleID);
        Assert.assertTrue(pmsBlockRateMessage.contains("3 records were retrieved"));
    }

        @Test(priority = 2, description = "Add new quote hotel room after sending to PMS")
        @Severity(SeverityLevel.NORMAL)
        @Story("THY-589: MYCE to Opera")
        public void testMyceToOpera_addQuoteHotelRoom() throws InterruptedException, IOException, ParseException {
            SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:delete",
                    "-s",
                    "thn__MYCE_Quote__c",
                    "-w",
                    "Name='TestMyceToOperaAuto2'",
                    "-u",
                    THY589UserName,
                    "--json"});
            StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__Hotel__c",
                    "-w",
                    "Name='Demo'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
            StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:update",
                    "-s",
                    "thn__Hotel__c",
                    "-w",
                    "Id='" + propertyDemoID + "'",
                    "-v",
                    "thn__HotelCode__c='DEMO_HOTEL_CODE' thn__HotelBrand__c='DEMO_BRAND_CODE' thn__Unique_Id__c='Demo'",
                    "-u",
                    THY589UserName,
                    "--json"});
            StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__Product__c",
                    "-w",
                    "Name='ROOM 1 NIGHT'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");
            StringBuilder productRoom2NightsRecord = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__Product__c",
                    "-w",
                    "Name='ROOM 2 NIGHTS'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String productRoom2NightsID = JsonParser2.getFieldValue(productRoom2NightsRecord.toString(), "Id");
            StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__Space_Area__c",
                    "-w",
                    "Name='Single'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
            StringBuilder roomTypeQueenRecord = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__Space_Area__c",
                    "-w",
                    "Name='Queen'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
            StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__MYCE_Quote__c",
                    "-v",
                    "Name='TestMyceToOperaAuto2' thn__Pax__c=10 thn__Release_Date__c=" +
                            date.generateTodayDate2_plus(0, 3) + " thn__Hotel__c='" + propertyDemoID +
                            "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                            date.generateTodayDate2_plus(0, 3) + " thn__Closed_Status__c='Won'" +
                            " thn__Shoulder_Start_Date__c=" + date.generateTodayDate2() + " thn__Shoulder_End_Date__c=" +
                            date.generateTodayDate2_plus(0, 3),
                    "-u",
                    THY589UserName,
                    "--json"});
            String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
            StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Hotel_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightID +
                            "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeSingleID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:update",
                    "-s",
                    "thn__MYCE_Quote__c",
                    "-w",
                    "Id='" + myceQuoteID + "'",
                    "-v",
                    "thn__SendToPMS__c=true",
                    "-u",
                    THY589UserName,
                    "--json"});
            StringBuilder quoteHotelRoomResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Hotel_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom2NightsID +
                            "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeQueenID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__MYCE_Quote__c",
                    "-w",
                    "Id='" + myceQuoteID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__PMS_Block__c",
                    "-w",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String pmsBlockID= JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
            StringBuilder pmsBlockInventoryRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__PMS_Block_Inventory__c",
                    "-w",
                    "thn__PMS_Block__c='" + pmsBlockID + "' thn__RoomType__c='" + roomTypeSingleID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String pmsBlockInventoryID1= JsonParser2.getFieldValue(pmsBlockInventoryRecord1.toString(), "Id");
            StringBuilder pmsBlockInventoryRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__PMS_Block_Inventory__c",
                    "-w",
                    "thn__PMS_Block__c='" + pmsBlockID + "' thn__RoomType__c='" + roomTypeQueenID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String pmsBlockInventoryID2= JsonParser2.getFieldValue(pmsBlockInventoryRecord2.toString(), "Id");
            StringBuilder pmsBlockRateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__BlockRate__c",
                    "-w",
                    "thn__PMS_Block__c='" + pmsBlockID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String pmsBlockRateMessage= JsonParser2.getFieldValue2(pmsBlockRateRecord.toString(), "message");
            Assert.assertNotNull(pmsBlockInventoryID1);
            Assert.assertNotNull(pmsBlockInventoryID2);
            Assert.assertTrue(pmsBlockRateMessage.contains("6 records were retrieved"));


            Object rates = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
                    "force:data:soql:query",
                    "-q",
                    "SELECT Id, thn__Start__c, thn__End__c FROM thn__BlockRate__c WHERE thn__PMS_Block__c='" + pmsBlockID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            System.out.println(rates);
            List<String> values= JsonParser2.getFieldValueSoql(rates.toString(), "thn__Start__c" , "thn__End__c");
            System.out.println(values);
            String date1 = values.get(0);

    }

    @Test(priority = 3, description = "Delete quote hotel room after sending to PMS")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-589: MYCE to Opera")
    public void testMyceToOpera_deleteQuoteHotelRoom() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestMyceToOperaAuto3'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Id='" + propertyDemoID + "'",
                "-v",
                "thn__HotelCode__c='DEMO_HOTEL_CODE' thn__HotelBrand__c='DEMO_BRAND_CODE' thn__Unique_Id__c='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(updateResult);
        StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");
        StringBuilder productRoom2NightsRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 2 NIGHTS'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom2NightsID = JsonParser2.getFieldValue(productRoom2NightsRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestMyceToOperaAuto3' thn__Pax__c=10 thn__Release_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Closed_Status__c='Won'" +
                        " thn__Shoulder_Start_Date__c=" + date.generateTodayDate2() + " thn__Shoulder_End_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                THY589UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightID +
                        "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeSingleID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String quoteHotelRoomID = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder quoteHotelRoomResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom2NightsID +
                        "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeQueenID + "'",
                "-u",
                THY589UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-v",
                "thn__SendToPMS__c=true",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder deleteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='"+ quoteHotelRoomID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(deleteResult);
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");

        StringBuilder pmsBlockInventoryRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID + "' thn__RoomType__c='" + roomTypeSingleID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockInventoryRecord1);
        String pmsBlockInventoryID1 = JsonParser2.getFieldValue2(pmsBlockInventoryRecord1.toString(), "message");
        System.out.println(pmsBlockInventoryID1);
        StringBuilder pmsBlockInventoryRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID + "' thn__RoomType__c='" + roomTypeQueenID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockInventoryID2 = JsonParser2.getFieldValue(pmsBlockInventoryRecord2.toString(), "Id");
        StringBuilder pmsBlockRateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockRateMessage= JsonParser2.getFieldValue2(pmsBlockRateRecord.toString(), "message");
        Assert.assertTrue(pmsBlockInventoryID1.contains("No matching record found"));
        Assert.assertNotNull(pmsBlockInventoryID2);
        Assert.assertTrue(pmsBlockRateMessage.contains("3 records were retrieved"));
    }

    @Test(priority = 4, description = "Change departure date on quote hotel room after sending to PMS")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-589: MYCE to Opera")
    public void testMyceToOpera_changeQuoteDepartureDate() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestMyceToOperaAuto4'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Id='" + propertyDemoID + "'",
                "-v",
                "thn__HotelCode__c='DEMO_HOTEL_CODE' thn__HotelBrand__c='DEMO_BRAND_CODE' thn__Unique_Id__c='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(updateResult);
        StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestMyceToOperaAuto4' thn__Pax__c=10 thn__Release_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Closed_Status__c='Won'" +
                        " thn__Shoulder_Start_Date__c=" + date.generateTodayDate2() + " thn__Shoulder_End_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                THY589UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightID +
                        "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeSingleID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String quoteHotelRoomID = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-v",
                "thn__SendToPMS__c=true",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder updateResult11 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomID + "'",
                "-v",
                "thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 2),
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(updateResult11);
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomID + "'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRecord);
        String pmsBlockID= JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        StringBuilder pmsBlockInventoryRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID + "'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder pmsBlockRateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRateRecord);
        String pmsBlockRateMessage= JsonParser2.getFieldValue2(pmsBlockRateRecord.toString(), "message");
        Assert.assertTrue(pmsBlockRateMessage.contains("2 records were retrieved"));
        String pmsBlockInventoryStartDate = JsonParser2.getFieldValue(pmsBlockInventoryRecord.toString(), "thn__Start__c");
        String pmsBlockInventoryEndDate = JsonParser2.getFieldValue(pmsBlockInventoryRecord.toString(), "thn__End__c");
        String quoteHotelroomArrivalDate = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date__c");
        String quoteHotelRoomDepartureDate = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date__c");
        Assert.assertEquals(pmsBlockInventoryStartDate, quoteHotelroomArrivalDate);
        Assert.assertEquals(pmsBlockInventoryEndDate, quoteHotelRoomDepartureDate);
        Assert.assertTrue(pmsBlockRateMessage.contains("2 records were retrieved"));
    }

    @Test(priority = 5, description = "Add Quote Hotel Room from another Property")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-589: MYCE to Opera")
    public void addQuoteHotelRoomFromAnotherProperty() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestMyceToOperaAuto5'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder propertyAutoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Property Auto'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyAuroID = JsonParser2.getFieldValue(propertyAutoRecord.toString(), "Id");
        StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");
        StringBuilder productRoom1NightAutoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT PROPERTY AUTO' thn__Hotel__c='" + propertyAuroID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightAutoID = JsonParser2.getFieldValue(productRoom1NightAutoRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder roomTypeQueenAutoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen Property Auto'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeQueenAutoID = JsonParser2.getFieldValue(roomTypeQueenAutoRecord.toString(), "Id");
        StringBuilder rateDefaultRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Rate__c",
                "-w",
                "Name='DEFAULT'",
                "-u",
                THY589UserName,
                "--json"});
        String rateDefaultID = JsonParser2.getFieldValue(rateDefaultRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestMyceToOperaAuto5' thn__Pax__c=1  thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__SendToPMS__c=true" +
                        " thn__InventoryBlockType__c='ELASTIC' ",
                "-u",
                THY589UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightID +
                        "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeSingleID +
                        "' thn__Rate_Plan__c=" + rateDefaultID + "'" ,
                "-u",
                THY589UserName,
                "--json"});
        String quoteHotelRoomID1 = JsonParser2.getFieldValue(quoteHotelRoomResult1.toString(), "id");
        StringBuilder quoteHotelRoomResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightAutoID +
                        "' thn__Property__c='" + propertyAuroID + "' thn__Space_Area__c='" + roomTypeQueenAutoID +
                        "'",
                "-u",
                THY589UserName,
                "--json"});
        String quoteHotelRoomID2 = JsonParser2.getFieldValue(quoteHotelRoomResult2.toString(), "id");
        Object pmsBlock = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, thn__Property__c, thn__Start__c, thn__End__c FROM thn__PMS_Block__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlock);
        List<String> values= JsonParser2.getFieldValueSoql(pmsBlock.toString(), "Id");
        String pmsBlockID1 = values.get(0);
        String pmsBlockID2 = values.get(1);
        StringBuilder pmsBlockInventoryRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID1 + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockInventoryID1 = JsonParser2.getFieldValue(pmsBlockInventoryRecord1.toString(), "Id");
        StringBuilder pmsBlockInventoryRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID2 + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockInventoryID2 = JsonParser2.getFieldValue(pmsBlockInventoryRecord2.toString(), "Id");
        Object pmsBlockRates1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, thn__Start__c, thn__End__c, thn__Rate__c, thn__RateCode__c, thn__RoomType__c  FROM" +
                        " thn__BlockRate__c WHERE thn__PMS_Block__c='" + pmsBlockID1 + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRates1);
        List<String> pmsBlockRatesID1= JsonParser2.getFieldValueSoql(pmsBlockRates1.toString(), "Id");
        Object pmsBlockRates2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, thn__Start__c, thn__End__c, thn__Rate__c, thn__RateCode__c, thn__RoomType__c  FROM" +
                        " thn__BlockRate__c WHERE thn__PMS_Block__c='" + pmsBlockID2 + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRates2);
        List<String> pmsBlockRatesID2= JsonParser2.getFieldValueSoql(pmsBlockRates2.toString(), "Id");
        StringBuilder pmsBlockRate1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID1.get(0) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate1);
        StringBuilder pmsBlockRate2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID1.get(1) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate2);
        StringBuilder pmsBlockRate3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID1.get(2) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate3);
        StringBuilder pmsBlockRate4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID2.get(0) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate4);
        StringBuilder pmsBlockRate5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID2.get(1) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate5);
        StringBuilder pmsBlockRate6 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID2.get(2) + "'",
                "-u",
                THY589UserName,
                "--json"});
        String rate1 = JsonParser2.getFieldValue(pmsBlockRate1.toString(), "thn__Rate__c");
        String rate2 = JsonParser2.getFieldValue(pmsBlockRate2.toString(), "thn__Rate__c");
        String rate3 = JsonParser2.getFieldValue(pmsBlockRate3.toString(), "thn__Rate__c");
        String rate4 = JsonParser2.getFieldValue(pmsBlockRate4.toString(), "thn__Rate__c");
        String rate5 = JsonParser2.getFieldValue(pmsBlockRate5.toString(), "thn__Rate__c");
        String rate6 = JsonParser2.getFieldValue(pmsBlockRate6.toString(), "thn__Rate__c");
        String roomTypeBlockRate1 = JsonParser2.getFieldValue(pmsBlockRate1.toString(), "thn__RoomType__c");
        String roomTypeBlockRate2 = JsonParser2.getFieldValue(pmsBlockRate2.toString(), "thn__RoomType__c");
        String roomTypeBlockRate3 = JsonParser2.getFieldValue(pmsBlockRate3.toString(), "thn__RoomType__c");
        String roomTypeBlockRate4 = JsonParser2.getFieldValue(pmsBlockRate4.toString(), "thn__RoomType__c");
        String roomTypeBlockRate5 = JsonParser2.getFieldValue(pmsBlockRate5.toString(), "thn__RoomType__c");
        String roomTypeBlockRate6 = JsonParser2.getFieldValue(pmsBlockRate6.toString(), "thn__RoomType__c");
        Assert.assertEquals(rate1, rateDefaultID );
        Assert.assertEquals(rate2, rateDefaultID );
        Assert.assertEquals(rate3, rateDefaultID );
        Assert.assertEquals(rate4, null );
        Assert.assertEquals(rate5, null);
        Assert.assertEquals(rate6, null );
        Assert.assertNotNull(pmsBlockInventoryID1);
        Assert.assertNotNull(pmsBlockInventoryID2);
        Assert.assertEquals(pmsBlockRatesID1.size(), 3);
        Assert.assertEquals(pmsBlockRatesID2.size(), 3);
        Assert.assertEquals(roomTypeBlockRate1, roomTypeSingleID);
        Assert.assertEquals(roomTypeBlockRate2, roomTypeSingleID);
        Assert.assertEquals(roomTypeBlockRate3, roomTypeSingleID);
        Assert.assertEquals(roomTypeBlockRate4, roomTypeQueenAutoID);
        Assert.assertEquals(roomTypeBlockRate5, roomTypeQueenAutoID);
        Assert.assertEquals(roomTypeBlockRate6, roomTypeQueenAutoID);
        List<String> pmsBlockRatesStartEndDates1= JsonParser2.getFieldValueSoql(pmsBlockRates1.toString(), "thn__Start__c", "thn__End__c");
        List<String> pmsBlockRatesStartEndDates2= JsonParser2.getFieldValueSoql(pmsBlockRates2.toString(), "thn__Start__c", "thn__End__c");
        Assert.assertEquals(pmsBlockRatesStartEndDates1.get(0), date.generateTodayDate2() );
        Assert.assertEquals(pmsBlockRatesStartEndDates1.get(1), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(pmsBlockRatesStartEndDates1.get(2), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(pmsBlockRatesStartEndDates1.get(3), date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(pmsBlockRatesStartEndDates1.get(4), date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(pmsBlockRatesStartEndDates1.get(5), date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(pmsBlockRatesStartEndDates2.get(0), date.generateTodayDate2() );
        Assert.assertEquals(pmsBlockRatesStartEndDates2.get(1), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(pmsBlockRatesStartEndDates2.get(2), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(pmsBlockRatesStartEndDates2.get(3), date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(pmsBlockRatesStartEndDates2.get(4), date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(pmsBlockRatesStartEndDates2.get(5), date.generateTodayDate2_plus(0, 3));
    }

    @Test(priority = 6, description = "Add Quote Hotel Room from another Property")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-589: MYCE to Opera")
    public void cloneMyceQuote() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestMyceToOperaAuto6'",
                "-u",
                THY589UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='CloneTestMyceToOperaAuto6'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder propertyAutoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Property Auto'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyAuroID = JsonParser2.getFieldValue(propertyAutoRecord.toString(), "Id");
        StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");
        StringBuilder productRoom1NightAutoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT PROPERTY AUTO' thn__Hotel__c='" + propertyAuroID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightAutoID = JsonParser2.getFieldValue(productRoom1NightAutoRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder roomTypeQueenAutoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen Property Auto'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeQueenAutoID = JsonParser2.getFieldValue(roomTypeQueenAutoRecord.toString(), "Id");
        StringBuilder rateDefaultRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Rate__c",
                "-w",
                "Name='DEFAULT'",
                "-u",
                THY589UserName,
                "--json"});
        String rateDefaultID = JsonParser2.getFieldValue(rateDefaultRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestMyceToOperaAuto6' thn__Pax__c=1  thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__SendToPMS__c=true" +
                        " thn__InventoryBlockType__c='ELASTIC' ",
                "-u",
                THY589UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightID +
                        "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeSingleID +
                        "' thn__Rate_Plan__c=" + rateDefaultID + "'" ,
                "-u",
                THY589UserName,
                "--json"});
        String quoteHotelRoomID1 = JsonParser2.getFieldValue(quoteHotelRoomResult1.toString(), "id");
        StringBuilder quoteHotelRoomResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightAutoID +
                        "' thn__Property__c='" + propertyAuroID + "' thn__Space_Area__c='" + roomTypeQueenAutoID +
                        "'",
                "-u",
                THY589UserName,
                "--json"});
        String quoteHotelRoomID2 = JsonParser2.getFieldValue(quoteHotelRoomResult2.toString(), "id");
        Object pmsBlock = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, thn__Property__c, thn__Start__c, thn__End__c FROM thn__PMS_Block__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlock);
        List<String> values= JsonParser2.getFieldValueSoql(pmsBlock.toString(), "Id");
        String pmsBlockID1 = values.get(0);
        String pmsBlockID2 = values.get(1);
        StringBuilder pmsBlockInventoryRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID1 + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockInventoryID1 = JsonParser2.getFieldValue(pmsBlockInventoryRecord1.toString(), "Id");
        StringBuilder pmsBlockInventoryRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID2 + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockInventoryID2 = JsonParser2.getFieldValue(pmsBlockInventoryRecord2.toString(), "Id");
        Object pmsBlockRates1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, thn__Start__c, thn__End__c, thn__Rate__c, thn__RateCode__c, thn__RoomType__c  FROM" +
                        " thn__BlockRate__c WHERE thn__PMS_Block__c='" + pmsBlockID1 + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRates1);
        List<String> pmsBlockRatesID1= JsonParser2.getFieldValueSoql(pmsBlockRates1.toString(), "Id");
        Object pmsBlockRates2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, thn__Start__c, thn__End__c, thn__Rate__c, thn__RateCode__c, thn__RoomType__c  FROM" +
                        " thn__BlockRate__c WHERE thn__PMS_Block__c='" + pmsBlockID2 + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRates2);
        List<String> pmsBlockRatesID2= JsonParser2.getFieldValueSoql(pmsBlockRates2.toString(), "Id");
        StringBuilder pmsBlockRate1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID1.get(0) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate1);
        StringBuilder pmsBlockRate2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID1.get(1) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate2);
        StringBuilder pmsBlockRate3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID1.get(2) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate3);
        StringBuilder pmsBlockRate4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID2.get(0) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate4);
        StringBuilder pmsBlockRate5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID2.get(1) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate5);
        StringBuilder pmsBlockRate6 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesID2.get(2) + "'",
                "-u",
                THY589UserName,
                "--json"});
        loginPageForScratchOrg.logInOnScratchOrg2(driver, "https://test.salesforce.com/", THY589UserName, THY589Password);
        homePageForScratchOrg.openAppLauncher();
        homePageForScratchOrg.sendTextInAppWindow("MYCE Quotes");
        myceQuotes.openMyceQoteRecord("TestMyceToOperaAuto6");
        myceQuotes.cloneMyceQuote("CloneTestMyceToOperaAuto6");
        StringBuilder cloneMyceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='CloneTestMyceToOperaAuto6'",
                "-u",
                THY589UserName,
                "--json"});
        String cloneMyceQuoteID = JsonParser2.getFieldValue(cloneMyceQuoteRecord.toString(), "Id");
        Object pmsBlockClone = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, thn__Property__c, thn__Start__c, thn__End__c FROM thn__PMS_Block__c WHERE thn__MYCE_Quote__c='" + cloneMyceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        List<String> values2= JsonParser2.getFieldValueSoql(pmsBlock.toString(), "Id");
        String pmsBlockCloneID1 = values2.get(0);
        String pmsBlockCloneID2 = values2.get(1);
        Object pmsBlockRatesClone1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, thn__Start__c, thn__End__c, thn__Rate__c, thn__RateCode__c, thn__RoomType__c  FROM" +
                        " thn__BlockRate__c WHERE thn__PMS_Block__c='" + pmsBlockCloneID1 + "'",
                "-u",
                THY589UserName,
                "--json"});
        List<String> pmsBlockRatesCloneID1= JsonParser2.getFieldValueSoql(pmsBlockRatesClone1.toString(), "Id");
        Object pmsBlockRatesClone2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, thn__Start__c, thn__End__c, thn__Rate__c, thn__RateCode__c, thn__RoomType__c  FROM" +
                        " thn__BlockRate__c WHERE thn__PMS_Block__c='" + pmsBlockCloneID2 + "'",
                "-u",
                THY589UserName,
                "--json"});
        List<String> pmsBlockRatesCloneID2= JsonParser2.getFieldValueSoql(pmsBlockRatesClone2.toString(), "Id");
        StringBuilder pmsBlockRateClone1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesCloneID1.get(0) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate1);
        StringBuilder pmsBlockRateClone2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesCloneID1.get(1) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate2);
        StringBuilder pmsBlockRateClone3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesCloneID1.get(2) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate3);
        StringBuilder pmsBlockRateClone4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesCloneID2.get(0) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate4);
        StringBuilder pmsBlockRateClone5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesCloneID2.get(1) + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRate5);
        StringBuilder pmsBlockRateClone6 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__BlockRate__c",
                "-w",
                "Id='" + pmsBlockRatesCloneID2.get(2) + "'",
                "-u",
                THY589UserName,
                "--json"});
        String rate1 = JsonParser2.getFieldValue(pmsBlockRate1.toString(), "thn__Rate__c");
        String rate2 = JsonParser2.getFieldValue(pmsBlockRate2.toString(), "thn__Rate__c");
        String rate3 = JsonParser2.getFieldValue(pmsBlockRate3.toString(), "thn__Rate__c");
        String rate4 = JsonParser2.getFieldValue(pmsBlockRate4.toString(), "thn__Rate__c");
        String rate5 = JsonParser2.getFieldValue(pmsBlockRate5.toString(), "thn__Rate__c");
        String rate6 = JsonParser2.getFieldValue(pmsBlockRate6.toString(), "thn__Rate__c");
        String roomTypeBlockRate1 = JsonParser2.getFieldValue(pmsBlockRate1.toString(), "thn__RoomType__c");
        String roomTypeBlockRate2 = JsonParser2.getFieldValue(pmsBlockRate2.toString(), "thn__RoomType__c");
        String roomTypeBlockRate3 = JsonParser2.getFieldValue(pmsBlockRate3.toString(), "thn__RoomType__c");
        String roomTypeBlockRate4 = JsonParser2.getFieldValue(pmsBlockRate4.toString(), "thn__RoomType__c");
        String roomTypeBlockRate5 = JsonParser2.getFieldValue(pmsBlockRate5.toString(), "thn__RoomType__c");
        String roomTypeBlockRate6 = JsonParser2.getFieldValue(pmsBlockRate6.toString(), "thn__RoomType__c");
        String rateClone1 = JsonParser2.getFieldValue(pmsBlockRateClone1.toString(), "thn__Rate__c");
        String rateClone2 = JsonParser2.getFieldValue(pmsBlockRateClone2.toString(), "thn__Rate__c");
        String rateClone3 = JsonParser2.getFieldValue(pmsBlockRateClone3.toString(), "thn__Rate__c");
        String rateClone4 = JsonParser2.getFieldValue(pmsBlockRateClone4.toString(), "thn__Rate__c");
        String rateClone5 = JsonParser2.getFieldValue(pmsBlockRateClone5.toString(), "thn__Rate__c");
        String rateClone6 = JsonParser2.getFieldValue(pmsBlockRateClone6.toString(), "thn__Rate__c");
        String roomTypeBlockRateClone1 = JsonParser2.getFieldValue(pmsBlockRateClone1.toString(), "thn__RoomType__c");
        String roomTypeBlockRateClone2 = JsonParser2.getFieldValue(pmsBlockRateClone2.toString(), "thn__RoomType__c");
        String roomTypeBlockRateClone3 = JsonParser2.getFieldValue(pmsBlockRateClone3.toString(), "thn__RoomType__c");
        String roomTypeBlockRateClone4 = JsonParser2.getFieldValue(pmsBlockRateClone4.toString(), "thn__RoomType__c");
        String roomTypeBlockRateClone5 = JsonParser2.getFieldValue(pmsBlockRateClone5.toString(), "thn__RoomType__c");
        String roomTypeBlockRateClone6 = JsonParser2.getFieldValue(pmsBlockRateClone6.toString(), "thn__RoomType__c");
        Assert.assertEquals(rateClone1, rate1);
        Assert.assertEquals(rateClone2, rate2);
        Assert.assertEquals(rateClone3, rate3);
        Assert.assertEquals(rateClone4, rate4);
        Assert.assertEquals(rateClone5, rate5);
        Assert.assertEquals(rateClone6, rate6);
        Assert.assertEquals(roomTypeBlockRateClone1, roomTypeBlockRate1);
        Assert.assertEquals(roomTypeBlockRateClone2, roomTypeBlockRate2);
        Assert.assertEquals(roomTypeBlockRateClone3, roomTypeBlockRate3);
        Assert.assertEquals(roomTypeBlockRateClone4, roomTypeBlockRate4);
        Assert.assertEquals(roomTypeBlockRateClone5, roomTypeBlockRate5);
        Assert.assertEquals(roomTypeBlockRateClone6, roomTypeBlockRate6);
    }

    @Test(priority = 7, description = "Change the date of the Quote")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-589: MYCE to Opera")
    public void changeDateMyceQuote() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestMyceToOperaAuto7'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");

        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder rateDefaultRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Rate__c",
                "-w",
                "Name='DEFAULT'",
                "-u",
                THY589UserName,
                "--json"});
        String rateDefaultID = JsonParser2.getFieldValue(rateDefaultRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestMyceToOperaAuto7' thn__Pax__c=1  thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__SendToPMS__c=true" +
                        " thn__InventoryBlockType__c='ELASTIC' ",
                "-u",
                THY589UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightID +
                        "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeSingleID +
                        "' thn__Rate_Plan__c=" + rateDefaultID + "'" ,
                "-u",
                THY589UserName,
                "--json"});
        String quoteHotelRoomID1 = JsonParser2.getFieldValue(quoteHotelRoomResult1.toString(), "id");
        homePageForScratchOrg.openAppLauncher();
        homePageForScratchOrg.sendTextInAppWindow("MYCE Quotes");
        myceQuotes.openMyceQoteRecord("TestMyceToOperaAuto7");
        Thread.sleep(2000);
        myceQuotes.changeDate(date.generateTodayDate3_plus(0, 1));
        StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");

        Object pmsBlockRates = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, thn__Start__c, thn__End__c, thn__Rate__c, thn__RateCode__c, thn__RoomType__c  FROM" +
                        " thn__BlockRate__c WHERE thn__PMS_Block__c='" + pmsBlockID + "'",
                "-u",
                THY589UserName,
                "--json"});
        List<String> pmsBlockRatesStartEndDates= JsonParser2.getFieldValueSoql(pmsBlockRates.toString(), "thn__Start__c", "thn__End__c");
        Assert.assertEquals(pmsBlockRatesStartEndDates.get(0), date.generateTodayDate2_plus(0, 1) );
        Assert.assertEquals(pmsBlockRatesStartEndDates.get(1), date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(pmsBlockRatesStartEndDates.get(2), date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(pmsBlockRatesStartEndDates.get(3), date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(pmsBlockRatesStartEndDates.get(4), date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(pmsBlockRatesStartEndDates.get(5), date.generateTodayDate2_plus(0, 4));
    }
}
