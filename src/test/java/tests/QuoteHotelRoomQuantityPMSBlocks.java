package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteHotelRoomQuantityPMSBlocks extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote: Arrival Date = today, Departure Date = today + 4 days," +
            " Pax = 5, Type of Occupancy = Triple. Instantiate a Quote Hotel Room: Arrival Date = today," +
            " Departure Date = today + 4 days. On MYCE Quote set ‘Send to PMS’ checkbox to True. Expected result:" +
            " a PMS Block record is created, PMS Status = New, Four PMS Block Inventory and Block Rates records were" +
            " created. On the PMS Block Inventory: Occupancy 3 Blocked = 5 ." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-663: Quote hotel room quantity - PMS Blocks")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityPMSBlockAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityPMSBlockAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                        + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                        + roomTypeQueenID + "' thn__Type_of_Occupancy__c='Triple'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__SendToPMS__c='true'", ORG_USERNAME);
        StringBuilder pmsBlockrecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockId = JsonParser2.getFieldValue(pmsBlockrecord.toString(), "Id");
        StringBuilder pmsBlockInventoryRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_3_Blocked__c," +
                " thn__RoomType__c FROM thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" +pmsBlockId + "'",
                ORG_USERNAME);
        List<String> pmsBlockInventoryID = JsonParser2.
                getFieldValueSoql(pmsBlockInventoryRecords.toString(), "Id");
        List<String> pmsBlockRoomTypes = JsonParser2.
                getFieldValueSoql(pmsBlockInventoryRecords.toString(), "thn__RoomType__c");
        List<Integer> pmsBlockInventoryOccupancy3Blocked = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecords.toString(), "thn__Occupancy_3_Blocked__c");
        StringBuilder blockRatesRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__BlockRate__c WHERE" +
                " thn__PMS_Block__c='" +pmsBlockId + "'", ORG_USERNAME);
        List<String> blockRatesID = JsonParser2.
                getFieldValueSoql(blockRatesRecords.toString(), "Id");
        Assert.assertEquals(pmsBlockInventoryID.size(), 4);
        Assert.assertEquals(blockRatesID.size(), 4);
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked.get(0).intValue(), 5);
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked.get(1).intValue(), 5);
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked.get(2).intValue(), 5);
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked.get(3).intValue(), 5);
        Assert.assertEquals(pmsBlockRoomTypes.get(0), roomTypeQueenID);
        Assert.assertEquals(pmsBlockRoomTypes.get(1), roomTypeQueenID);
        Assert.assertEquals(pmsBlockRoomTypes.get(2), roomTypeQueenID);
        Assert.assertEquals(pmsBlockRoomTypes.get(3), roomTypeQueenID);
    }

    @Test(priority = 2, description = "Change the Quantity of the Quote Hotel Room Price Records. Expected result:" +
            " The Occupancy 3 Blocked of related PMS Block Inventory of the PMS Block got updated." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-663: Quote hotel room quantity - PMS Blocks")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityPMSBlockAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder quoteHotelRoomPrice1 = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'" +
                " AND thn__Date__c=" + date.generateTodayDate2_plus(0 , 0), ORG_USERNAME);
        List<String> quoteHotelRoomPrceID1 = JsonParser2.
                getFieldValueSoql(quoteHotelRoomPrice1.toString(), "Id");
        StringBuilder quoteHotelRoomPrice2 = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'" +
                " AND thn__Date__c=" + date.generateTodayDate2_plus(0 , 1), ORG_USERNAME);
        List<String> quoteHotelRoomPrceID2 = JsonParser2.
                getFieldValueSoql(quoteHotelRoomPrice2.toString(), "Id");
        StringBuilder quoteHotelRoomPrice3 = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'" +
                " AND thn__Date__c=" + date.generateTodayDate2_plus(0 , 2), ORG_USERNAME);
        List<String> quoteHotelRoomPrceID3 = JsonParser2.
                getFieldValueSoql(quoteHotelRoomPrice3.toString(), "Id");
        StringBuilder quoteHotelRoomPrice4 = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'" +
                " AND thn__Date__c=" + date.generateTodayDate2_plus(0 , 3), ORG_USERNAME);
        List<String> quoteHotelRoomPrceID4 = JsonParser2.
                getFieldValueSoql(quoteHotelRoomPrice4.toString(), "Id");
        System.out.println(quoteHotelRoomPrice1);
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + quoteHotelRoomPrceID2.get(0) + "'",
                "thn__Quantity__c=10", ORG_USERNAME);
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + quoteHotelRoomPrceID3.get(0) + "'",
                "thn__Quantity__c=15", ORG_USERNAME);
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + quoteHotelRoomPrceID4.get(0) + "'",
                "thn__Quantity__c=1", ORG_USERNAME);
        StringBuilder pmsBlockrecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockId = JsonParser2.getFieldValue(pmsBlockrecord.toString(), "Id");
        StringBuilder pmsBlockInventoryRecord1 = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_3_Blocked__c" +
                " FROM thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" +pmsBlockId + "' AND thn__Start__c="
                + date.generateTodayDate2_plus(0 , 0), ORG_USERNAME);
        StringBuilder pmsBlockInventoryRecord2 = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_3_Blocked__c" +
                " FROM thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" +pmsBlockId + "' AND thn__Start__c="
                + date.generateTodayDate2_plus(0 , 1), ORG_USERNAME);
        StringBuilder pmsBlockInventoryRecord3 = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_3_Blocked__c" +
                " FROM thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" +pmsBlockId + "' AND thn__Start__c="
                + date.generateTodayDate2_plus(0 , 2), ORG_USERNAME);
        StringBuilder pmsBlockInventoryRecord4 = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_3_Blocked__c" +
                " FROM thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" +pmsBlockId + "' AND thn__Start__c="
                + date.generateTodayDate2_plus(0 , 3), ORG_USERNAME);
        List<Integer> pmsBlockInventoryOccupancy3Blocked1 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord1.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> pmsBlockInventoryOccupancy3Blocked2 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord2.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> pmsBlockInventoryOccupancy3Blocked3 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord3.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> pmsBlockInventoryOccupancy3Blocked4 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord4.toString(), "thn__Occupancy_3_Blocked__c");
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked1.get(0).intValue(), 5);
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked2.get(0).intValue(), 10);
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked3.get(0).intValue(), 15);
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked4.get(0).intValue(), 1);
    }

    @Test(priority = 3, description = "Change  Type of Occupancy of the Quote Hotel Room. Type of Occupancy = double." +
            "  Expected result: The Occupancy 2 Block of related PMS Block Inventory of the PMS Block got updated." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-663: Quote hotel room quantity - PMS Blocks")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityPMSBlockAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder pmsBlockrecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockId = JsonParser2.getFieldValue(pmsBlockrecord.toString(), "Id");
        StringBuilder pmsBlockInventoryRecord1 = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_3_Blocked__c," +
                " thn__Occupancy_2_Blocked__c FROM thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" +pmsBlockId +
                "' AND thn__Start__c=" + date.generateTodayDate2_plus(0 , 0), ORG_USERNAME);
        StringBuilder pmsBlockInventoryRecord2 = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_3_Blocked__c," +
                " thn__Occupancy_2_Blocked__c FROM thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" +pmsBlockId +
                "' AND thn__Start__c=" + date.generateTodayDate2_plus(0 , 1), ORG_USERNAME);
        StringBuilder pmsBlockInventoryRecord3 = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_3_Blocked__c," +
                " thn__Occupancy_2_Blocked__c FROM thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" +pmsBlockId +
                "' AND thn__Start__c=" + date.generateTodayDate2_plus(0 , 2), ORG_USERNAME);
        StringBuilder pmsBlockInventoryRecord4 = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_3_Blocked__c," +
                " thn__Occupancy_2_Blocked__c FROM thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" +pmsBlockId +
                "' AND thn__Start__c=" + date.generateTodayDate2_plus(0 , 3), ORG_USERNAME);
        List<Integer> pmsBlockInventoryOccupancy3Blocked1 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord1.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> pmsBlockInventoryOccupancy3Blocked2 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord2.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> pmsBlockInventoryOccupancy3Blocked3 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord3.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> pmsBlockInventoryOccupancy3Blocked4 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord4.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> pmsBlockInventoryOccupancy2Blocked1 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord1.toString(), "thn__Occupancy_2_Blocked__c");
        List<Integer> pmsBlockInventoryOccupancy2Blocked2 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord2.toString(), "thn__Occupancy_2_Blocked__c");
        List<Integer> pmsBlockInventoryOccupancy2Blocked3 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord3.toString(), "thn__Occupancy_2_Blocked__c");
        List<Integer> pmsBlockInventoryOccupancy2Blocked4 = JsonParser2.
                getFieldValueSoql2(pmsBlockInventoryRecord4.toString(), "thn__Occupancy_2_Blocked__c");
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked1.get(0).intValue(), 5);
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked2.get(0).intValue(), 10);
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked3.get(0).intValue(), 15);
        Assert.assertEquals(pmsBlockInventoryOccupancy3Blocked4.get(0).intValue(), 1);
        Assert.assertEquals(pmsBlockInventoryOccupancy2Blocked1.get(0).intValue(), 0);
        Assert.assertEquals(pmsBlockInventoryOccupancy2Blocked2.get(0).intValue(), 0);
        Assert.assertEquals(pmsBlockInventoryOccupancy2Blocked3.get(0).intValue(), 0);
        Assert.assertEquals(pmsBlockInventoryOccupancy2Blocked4.get(0).intValue(), 0);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "id='" + quoteHotelRoomID + "'",
                "thn__Type_of_Occupancy__c='Double'", ORG_USERNAME );
        StringBuilder updatedPMSBlockInventoryRecord1 = myceQuotes.soql(SFDX, "SELECT Id," +
                " thn__Occupancy_3_Blocked__c, thn__Occupancy_2_Blocked__c FROM thn__PMS_Block_Inventory__c WHERE" +
                " thn__PMS_Block__c='" +pmsBlockId + "' AND thn__Start__c=" +
                date.generateTodayDate2_plus(0 , 0), ORG_USERNAME);
        System.out.println(updatedPMSBlockInventoryRecord1);
        StringBuilder updatedPMSBlockInventoryRecord2 = myceQuotes.soql(SFDX, "SELECT Id," +
                " thn__Occupancy_3_Blocked__c, thn__Occupancy_2_Blocked__c FROM thn__PMS_Block_Inventory__c WHERE" +
                " thn__PMS_Block__c='" +pmsBlockId + "' AND thn__Start__c=" +
                date.generateTodayDate2_plus(0 , 1), ORG_USERNAME);
        StringBuilder updatedPMSBlockInventoryRecord3 = myceQuotes.soql(SFDX, "SELECT Id," +
                " thn__Occupancy_3_Blocked__c, thn__Occupancy_2_Blocked__c FROM thn__PMS_Block_Inventory__c WHERE" +
                " thn__PMS_Block__c='" +pmsBlockId + "' AND thn__Start__c=" +
                date.generateTodayDate2_plus(0 , 2), ORG_USERNAME);
        StringBuilder updatedPMSBlockInventoryRecord4 = myceQuotes.soql(SFDX, "SELECT Id," +
                " thn__Occupancy_3_Blocked__c, thn__Occupancy_2_Blocked__c FROM thn__PMS_Block_Inventory__c WHERE" +
                " thn__PMS_Block__c='" +pmsBlockId + "' AND thn__Start__c=" +
                date.generateTodayDate2_plus(0 , 3), ORG_USERNAME);
        List<Integer> updatedPMSBlockInventoryOccupancy3Blocked1 = JsonParser2.
                getFieldValueSoql2(updatedPMSBlockInventoryRecord1.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> updatedPMSBlockInventoryOccupancy3Blocked2 = JsonParser2.
                getFieldValueSoql2(updatedPMSBlockInventoryRecord2.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> updatedPMSBlockInventoryOccupancy3Blocked3 = JsonParser2.
                getFieldValueSoql2(updatedPMSBlockInventoryRecord3.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> updatedPMSBlockInventoryOccupancy3Blocked4 = JsonParser2.
                getFieldValueSoql2(updatedPMSBlockInventoryRecord4.toString(), "thn__Occupancy_3_Blocked__c");
        List<Integer> updatedPMSBlockInventoryOccupancy2Blocked1 = JsonParser2.
                getFieldValueSoql2(updatedPMSBlockInventoryRecord1.toString(), "thn__Occupancy_2_Blocked__c");
        List<Integer> updatedPMSBlockInventoryOccupancy2Blocked2 = JsonParser2.
                getFieldValueSoql2(updatedPMSBlockInventoryRecord2.toString(), "thn__Occupancy_2_Blocked__c");
        List<Integer> updatedPMSBlockInventoryOccupancy2Blocked3= JsonParser2.
                getFieldValueSoql2(updatedPMSBlockInventoryRecord3.toString(), "thn__Occupancy_2_Blocked__c");
        List<Integer> updatedPMSBlockInventoryOccupancy2Blocked4 = JsonParser2.
                getFieldValueSoql2(updatedPMSBlockInventoryRecord4.toString(), "thn__Occupancy_2_Blocked__c");
        Assert.assertEquals(updatedPMSBlockInventoryOccupancy3Blocked1.get(0).intValue(), 0);
        Assert.assertEquals(updatedPMSBlockInventoryOccupancy3Blocked2.get(0).intValue(), 0);
        Assert.assertEquals(updatedPMSBlockInventoryOccupancy3Blocked3.get(0).intValue(), 0);
        Assert.assertEquals(updatedPMSBlockInventoryOccupancy3Blocked4.get(0).intValue(), 0);
        Assert.assertEquals(updatedPMSBlockInventoryOccupancy2Blocked1.get(0).intValue(), 5);
        Assert.assertEquals(updatedPMSBlockInventoryOccupancy2Blocked2.get(0).intValue(), 10);
        Assert.assertEquals(updatedPMSBlockInventoryOccupancy2Blocked3.get(0).intValue(), 15);
        Assert.assertEquals(updatedPMSBlockInventoryOccupancy2Blocked4.get(0).intValue(), 1);
    }

}
