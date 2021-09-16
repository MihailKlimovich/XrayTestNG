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

public class MyceToOpera extends BaseTest {

    @Test(priority = 1, description = "Add in Quote 1 hotel room and sent to PMS")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-589: MYCE to Opera")
    public void testMyceToOpera_WhereOneHotelRoom() throws InterruptedException, IOException {
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestMyceToOperaAuto1'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightID +
                        "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeSingleID + "'",
                "-u",
                THY589UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(myceQuoteRecord);
        StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(pmsBlockRecord);
        StringBuilder propertyDemoRecord2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Id='" + propertyDemoID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockID= JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        StringBuilder pmsBlockInventoryRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID + "'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder pmsBlockRateRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        public void testMyceToOpera_addQuoteHotelRoom() throws InterruptedException, IOException {
            SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:delete",
                    "-s",
                    "thn__MYCE_Quote__c",
                    "-w",
                    "Name='TestMyceToOperaAuto2'",
                    "-u",
                    THY589UserName,
                    "--json"});
            StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:get",
                    "-s",
                    "thn__Hotel__c",
                    "-w",
                    "Name='Demo'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
            StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
            StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:get",
                    "-s",
                    "thn__Product__c",
                    "-w",
                    "Name='ROOM 1 NIGHT'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");
            StringBuilder productRoom2NightsRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:get",
                    "-s",
                    "thn__Product__c",
                    "-w",
                    "Name='ROOM 2 NIGHTS'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String productRoom2NightsID = JsonParser2.getFieldValue(productRoom2NightsRecord.toString(), "Id");
            StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:get",
                    "-s",
                    "thn__Space_Area__c",
                    "-w",
                    "Name='Single'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
            StringBuilder roomTypeQueenRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:get",
                    "-s",
                    "thn__Space_Area__c",
                    "-w",
                    "Name='Queen'",
                    "-u",
                    THY589UserName,
                    "--json"});
            String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
            StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
            StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Hotel_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom1NightID +
                            "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeSingleID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
            StringBuilder quoteHotelRoomResult2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Hotel_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom2NightsID +
                            "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeQueenID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:get",
                    "-s",
                    "thn__MYCE_Quote__c",
                    "-w",
                    "Id='" + myceQuoteID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            System.out.println(myceQuoteRecord);
            StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
            StringBuilder pmsBlockInventoryRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:get",
                    "-s",
                    "thn__PMS_Block_Inventory__c",
                    "-w",
                    "thn__PMS_Block__c='" + pmsBlockID + "' thn__RoomType__c='" + roomTypeSingleID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            System.out.println(pmsBlockInventoryRecord1);
            String pmsBlockInventoryID1= JsonParser2.getFieldValue(pmsBlockInventoryRecord1.toString(), "Id");
            StringBuilder pmsBlockInventoryRecord2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                    "force:data:record:get",
                    "-s",
                    "thn__PMS_Block_Inventory__c",
                    "-w",
                    "thn__PMS_Block__c='" + pmsBlockID + "' thn__RoomType__c='" + roomTypeQueenID + "'",
                    "-u",
                    THY589UserName,
                    "--json"});
            System.out.println(pmsBlockInventoryRecord1);
            String pmsBlockInventoryID2= JsonParser2.getFieldValue(pmsBlockInventoryRecord2.toString(), "Id");
            StringBuilder pmsBlockRateRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
            Assert.assertNotNull(pmsBlockInventoryID1);
            Assert.assertNotNull(pmsBlockInventoryID2);
            Assert.assertTrue(pmsBlockRateMessage.contains("6 records were retrieved"));
    }

    @Test(priority = 3, description = "Delete quote hotel room after sending to PMS")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-589: MYCE to Opera")
    public void testMyceToOpera_deleteQuoteHotelRoom() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestMyceToOperaAuto3'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");
        StringBuilder productRoom2NightsRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 2 NIGHTS'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom2NightsID = JsonParser2.getFieldValue(productRoom2NightsRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder quoteHotelRoomResult2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productRoom2NightsID +
                        "' thn__Property__c='" + propertyDemoID + "' thn__Space_Area__c='" + roomTypeQueenID + "'",
                "-u",
                THY589UserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder deleteResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:delete",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='"+ quoteHotelRoomID + "'",
                "-u",
                THY589UserName,
                "--json"});
        System.out.println(deleteResult);
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");

        StringBuilder pmsBlockInventoryRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder pmsBlockInventoryRecord2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID + "' thn__RoomType__c='" + roomTypeQueenID + "'",
                "-u",
                THY589UserName,
                "--json"});
        String pmsBlockInventoryID2 = JsonParser2.getFieldValue(pmsBlockInventoryRecord2.toString(), "Id");
        StringBuilder pmsBlockRateRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestMyceToOperaAuto4'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                THY589UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder productRoom1NightRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                THY589UserName,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRoom1NightRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                THY589UserName,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder updateResult11 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomID + "'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder pmsBlockInventoryRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__PMS_Block_Inventory__c",
                "-w",
                "thn__PMS_Block__c='" + pmsBlockID + "'",
                "-u",
                THY589UserName,
                "--json"});
        StringBuilder pmsBlockRateRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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

}
