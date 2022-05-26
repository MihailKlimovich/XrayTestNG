package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.PMSBlock;

import java.io.IOException;
import java.util.List;

public class OperaBlockMultiPropertiesQuote extends BaseTest {

    @Test(priority = 1, description = "Create a new MYCE Quote. Add a Quote Hotel Room. For the Property Demo. Add" +
            " another Hotel room with another property. Set ‘Send to PMS’ = ‘True’ on the MYCE Quote. Expected" +
            " result: Two PMS Block are created for each Hotel room with different Property. For each PMS Block one" +
            " PMS Block Inventory and One Block Rate record is created for each day. start = current date and end" +
            " = current date. Occupancy blocked (number depends on occupancy type) = number on hotel room. 1 block" +
            " rate per quote hotel room price with room type. room type code = room_type.code. rate, rate code =" +
            " rate.code. occupancy price (number depends on occupancy type) = quote hotel room price value. start =" +
            " quote hotel room price date and end = quote hotel room price date." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY- 650: Opera Block - multi properties quote")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OperaBlockMultiPropertiesQuoteAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord1= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID1 = JsonParser2.getFieldValue(hotelRecord1.toString(), "Id");
        StringBuilder hotelRecord2= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='CANHM'", ORG_USERNAME);
        String propertyID2 = JsonParser2.getFieldValue(hotelRecord2.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT' thn__Hotel__c='" +
                propertyID1 + "'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomRecord = product.getProductSFDX(SFDX, "Name='Room' thn__Hotel__c='" + propertyID2 +
                "'", ORG_USERNAME);
        String roomID = JsonParser2.getFieldValue(roomRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder roomTypeSSV = roomType.getRoomTypeSFDX(SFDX, "Name='SSV' thn__Hotel__c='" + propertyID2 +
                "'", ORG_USERNAME);
        String roomTypeSSVID = JsonParser2.getFieldValue(roomTypeSSV.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OperaBlockMultiPropertiesQuoteAutoTest'" +
                " thn__Pax__c=100 thn__Hotel__c='" + propertyID1 + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 1) + " RecordTypeId='" +
                recordTypeID.get(0) + "' thn__Source__c='Walk in' thn__Market_Segment__c='Leisure'", ORG_USERNAME);
        String quoteHotelRoomID1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Property__c='" + propertyID1 + "' thn__Product__c='" + room1NightID + "'" +
                " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID1 + "'",
                "thn__Unit_Price__c=100 thn__VAT_Category__c='1'", ORG_USERNAME);
        String quoteHotelRoomID2 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Property__c='" + propertyID2 + "' thn__Product__c='" + roomID + "'" +
                " thn__Space_Area__c='" + roomTypeSSVID + "'", ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID2 + "'",
                "thn__Unit_Price__c=200 thn__VAT_Category__c='1'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX,"Id='" + quoteID + "'", "thn__SendToPMS__c=true", ORG_USERNAME);
        StringBuilder pmsBlockRecord1 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Property__c='" + propertyID1 + "'", ORG_USERNAME);
        String pmsBlockID1 = JsonParser2.getFieldValue(pmsBlockRecord1.toString(), "Id");
        StringBuilder pmsBlockRecord2 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Property__c='" + propertyID2 + "'", ORG_USERNAME);
        String pmsBlockID2 = JsonParser2.getFieldValue(pmsBlockRecord2.toString(), "Id");
        StringBuilder blockInventory1 = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_1_Blocked__c FROM" +
                " thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" + pmsBlockID1 + "'", ORG_USERNAME);
        System.out.println(blockInventory1);
        List<String> blockInventoryID1 = JsonParser2.getFieldValueSoql(blockInventory1.toString(), "Id");
        List<Integer> blockInventoryOccupancy1Blocked1 = JsonParser2.
                getFieldValueSoql2(blockInventory1.toString(), "thn__Occupancy_1_Blocked__c");
        StringBuilder blockInventory2 = myceQuotes.soql(SFDX, "SELECT Id, thn__Occupancy_1_Blocked__c FROM" +
                " thn__PMS_Block_Inventory__c WHERE thn__PMS_Block__c='" + pmsBlockID2 + "'", ORG_USERNAME);
        System.out.println(blockInventory2);
        List<String> blockInventoryID2 = JsonParser2.getFieldValueSoql(blockInventory2.toString(), "Id");
        List<Integer> blockInventoryOccupancy1Blocked2 = JsonParser2.
                getFieldValueSoql2(blockInventory2.toString(), "thn__Occupancy_1_Blocked__c");
        StringBuilder blockRate1 = myceQuotes.soql(SFDX, "SELECT Id, thn__Start__c, thn__End__c," +
                " thn__RoomTypeCode__c, thn__RateCode__c, thn__Occupancy_1_price__c FROM thn__BlockRate__c WHERE" +
                " thn__PMS_Block__c='" + pmsBlockID1 + "'", ORG_USERNAME);
        System.out.println(blockRate1);
        StringBuilder blockRate2 = myceQuotes.soql(SFDX, "SELECT Id, thn__Start__c, thn__End__c," +
                " thn__RoomTypeCode__c, thn__RateCode__c, thn__Occupancy_1_price__c FROM thn__BlockRate__c WHERE" +
                " thn__PMS_Block__c='" + pmsBlockID2 + "'", ORG_USERNAME);
        System.out.println(blockRate2);
        List<String> blockRateID1 = JsonParser2.getFieldValueSoql(blockRate1.toString(), "Id");
        List<String> blockRateID2 = JsonParser2.getFieldValueSoql(blockRate2.toString(), "Id");
        List<String> blockRateStart1 = JsonParser2.getFieldValueSoql(blockRate1.toString(), "thn__Start__c");
        List<String> blockRateStart2 = JsonParser2.getFieldValueSoql(blockRate2.toString(), "thn__Start__c");
        List<String> blockRateEnd1 = JsonParser2.getFieldValueSoql(blockRate1.toString(), "thn__End__c");
        List<String> blockRateEnd2 = JsonParser2.getFieldValueSoql(blockRate2.toString(), "thn__End__c");
        List<String> blockRateRoomTypeCode1 = JsonParser2.
                getFieldValueSoql(blockRate1.toString(), "thn__RoomTypeCode__c");
        List<String> blockRateRoomTypeCode2 = JsonParser2.
                getFieldValueSoql(blockRate2.toString(), "thn__RoomTypeCode__c");
        List<String> blockRateRateCode1 = JsonParser2.
                getFieldValueSoql(blockRate1.toString(), "thn__RateCode__c");
        List<String> blockRateRateCode2 = JsonParser2.
                getFieldValueSoql(blockRate2.toString(), "thn__RateCode__c");
        List<Integer> blockRateOccupancy1Price1 = JsonParser2.
                getFieldValueSoql2(blockRate1.toString(), "thn__Occupancy_1_price__c");
        List<Integer> blockRateOccupancy1Price2 = JsonParser2.
                getFieldValueSoql2(blockRate2.toString(), "thn__Occupancy_1_price__c");
        Assert.assertEquals(blockInventoryID1.size(), 1);
        Assert.assertEquals(blockInventoryID2.size(), 1);
        Assert.assertEquals(blockInventoryOccupancy1Blocked1.get(0).intValue(), 100);
        Assert.assertEquals(blockInventoryOccupancy1Blocked2.get(0).intValue(), 100);
        Assert.assertEquals(blockRateID1.size(), 1);
        Assert.assertEquals(blockRateID2.size(), 1);
        Assert.assertEquals(blockRateStart1.get(0), date.generateTodayDate2());
        Assert.assertEquals(blockRateStart2.get(0), date.generateTodayDate2());
        Assert.assertEquals(blockRateEnd1.get(0), date.generateTodayDate2());
        Assert.assertEquals(blockRateEnd2.get(0), date.generateTodayDate2());
        Assert.assertEquals(blockRateRoomTypeCode1.get(0), "SSC");
        Assert.assertEquals(blockRateRoomTypeCode2.get(0), "SSV");
        Assert.assertEquals(blockRateRateCode1.get(0), "PKBBADB");
        Assert.assertEquals(blockRateRateCode2.get(0), "PKBBADV");
        Assert.assertEquals(blockRateOccupancy1Price1.get(0).intValue(), 90.0);
        Assert.assertEquals(blockRateOccupancy1Price2.get(0).intValue(), 200);
    }

}
