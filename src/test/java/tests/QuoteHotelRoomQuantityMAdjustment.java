package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteHotelRoomQuantityMAdjustment extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote. Instantiate a Quote Hotel Room. Set checkbox ‘Create" +
            " PMS Block’ = True on MYCE Quote. Expected result: PMS block is created. Start Date=quote.arrivalDate," +
            " End Date=quote.departureDate, PMS Release Date Time = quote.releaseDate, PMS Status = Send," +
            " PMS Response = 200 OK.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-665: Quote hotel room quantity - MAdjustment")
    public void precondition() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityMAdjustmentAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true AND" +
                " Name='Bar'", ORG_USERNAME);
        List<String> rateId = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='QuoteHotelRoomQuantityMAdjustmentAutoTest' thn__Pax__c=1 thn__Hotel__c='"
                        + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2_plus(0, 4) + "" +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 7) + "" +
                        " RecordTypeId='" + recordTypeID.get(0) + "' thn__Release_Date__c=" +
                        date.generateTodayDate2_plus(0, 3), ORG_USERNAME);
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) +
                "' thn__Rate_Plan__c='" + rateId.get(0) + "' thn__Property__c='" + propertyID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true",
                ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        String pmsResponce = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Response__c");
        String pmsStatus = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Status__c");
        String pmsStartDate = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__Start__c");
        String pmsEndDate = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__End__c");
        String pmsReleaseDateTime = JsonParser2.
                getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Release_Date_Time__c");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteArrivalDate = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Arrival_Date__c");
        String quoteDepartureDate = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Departure_Date__c");
        String quoteReleaseDate = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Release_Date__c");
        Assert.assertNotNull(pmsBlockID);
        Assert.assertEquals(pmsResponce, "200 OK");
        Assert.assertEquals(pmsStatus, "Send");
        Assert.assertEquals(pmsStartDate, quoteArrivalDate);
        Assert.assertEquals(pmsEndDate, quoteDepartureDate);
        Assert.assertTrue(pmsReleaseDateTime.contains(quoteReleaseDate));
    }

    @Test(priority = 2, description = "Change the stage of the Quote to ‘3 - Tentative’. Expected result: MAdjustment" +
            " records are created on PMS Block. Start UTC = (date == day 1 ? quote hotel room price date + quote" +
            " hotel room arrival time : quote hotel room date 00:00). End UTC =  (date == last day ? quote hotel" +
            " room price date + quote hotel room departure time : quote hotel room date 23:59). PMS Quantity = quote" +
            " hotel room price quantity")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-665: Quote hotel room quantity - MAdjustment")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityMAdjustmentAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        StringBuilder mAdjustmentReocords = myceQuotes.soql(SFDX, "SELECT Id, thn__PMS_Start_UTC__c," +
                " thn__PMS_End_UTC__c, thn__PMS_Quantity__c FROM thn__MAdjustment__c WHERE thn__PMS_Block__c='" +
                pmsBlockID + "'", ORG_USERNAME);
        System.out.println(mAdjustmentReocords);
        List<String> mAdjustmentID = JsonParser2.
                getFieldValueSoql(mAdjustmentReocords.toString(), "Id");
        List<String> mAdjustmentStartUTC = JsonParser2.
                getFieldValueSoql(mAdjustmentReocords.toString(), "thn__PMS_Start_UTC__c");
        List<String> mAdjustmentEndUTC = JsonParser2.
                getFieldValueSoql(mAdjustmentReocords.toString(), "thn__PMS_End_UTC__c");
        List<Integer> mAdjustmentPMSquantity = JsonParser2.
                getFieldValueSoql2(mAdjustmentReocords.toString(), "thn__PMS_Quantity__c");
        Assert.assertEquals(mAdjustmentID.size(), 3);
        Assert.assertEquals(mAdjustmentStartUTC.get(0),
                date.generateTodayDate2_plus(0, 4) + "T12:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(0),
                date.generateTodayDate2_plus(0, 5) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(1),
                date.generateTodayDate2_plus(0, 5) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(1),
                date.generateTodayDate2_plus(0, 6) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(2),
                date.generateTodayDate2_plus(0, 6) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(2),
                date.generateTodayDate2_plus(0, 7) + "T14:30:00.000+0000");
        Assert.assertEquals(mAdjustmentPMSquantity.get(0).intValue(), 1);
        Assert.assertEquals(mAdjustmentPMSquantity.get(1).intValue(), 1);
        Assert.assertEquals(mAdjustmentPMSquantity.get(2).intValue(), 1);
    }

    @Test(priority = 3, description = "Change the Quantity=5 on the QHRP record. Expected result: Madjustments" +
            " records were updated")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-665: Quote hotel room quantity - MAdjustment")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityMAdjustmentAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder quoteHotelRoomPriceReocords = myceQuotes.soql(SFDX, "SELECT Id, thn__Date__c FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "' AND" +
                " thn__Date__c=" + date.generateTodayDate2_plus(0, 5), ORG_USERNAME);
        List<String> quoteHotelRoomPriceID = JsonParser2.
                getFieldValueSoql(quoteHotelRoomPriceReocords.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + quoteHotelRoomPriceID.get(0) + "'",
                "thn__Quantity__c=5", ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        StringBuilder mAdjustmentReocords = myceQuotes.soql(SFDX, "SELECT Id, thn__PMS_Start_UTC__c," +
                " thn__PMS_End_UTC__c, thn__PMS_Quantity__c FROM thn__MAdjustment__c WHERE thn__PMS_Block__c='" +
                pmsBlockID + "'", ORG_USERNAME);
        System.out.println(mAdjustmentReocords);
        List<String> mAdjustmentID = JsonParser2.
                getFieldValueSoql(mAdjustmentReocords.toString(), "Id");
        List<Integer> mAdjustmentPMSquantity = JsonParser2.
                getFieldValueSoql2(mAdjustmentReocords.toString(), "thn__PMS_Quantity__c");
        List<String> mAdjustmentStartUTC = JsonParser2.
                getFieldValueSoql(mAdjustmentReocords.toString(), "thn__PMS_Start_UTC__c");
        List<String> mAdjustmentEndUTC = JsonParser2.
                getFieldValueSoql(mAdjustmentReocords.toString(), "thn__PMS_End_UTC__c");
        Assert.assertEquals(mAdjustmentID.size(), 9);
        Assert.assertEquals(mAdjustmentPMSquantity.get(0).intValue(), 1);
        Assert.assertEquals(mAdjustmentPMSquantity.get(1).intValue(), 1);
        Assert.assertEquals(mAdjustmentPMSquantity.get(2).intValue(), 1);
        Assert.assertEquals(mAdjustmentPMSquantity.get(3).intValue(), -1);
        Assert.assertEquals(mAdjustmentPMSquantity.get(4).intValue(), -1);
        Assert.assertEquals(mAdjustmentPMSquantity.get(5).intValue(), -1);
        Assert.assertEquals(mAdjustmentPMSquantity.get(6).intValue(), 1);
        Assert.assertEquals(mAdjustmentPMSquantity.get(7).intValue(), 5);
        Assert.assertEquals(mAdjustmentPMSquantity.get(8).intValue(), 1);
        Assert.assertEquals(mAdjustmentStartUTC.get(0),
                date.generateTodayDate2_plus(0, 4) + "T12:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(0),
                date.generateTodayDate2_plus(0, 5) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(1),
                date.generateTodayDate2_plus(0, 5) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(1),
                date.generateTodayDate2_plus(0, 6) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(2),
                date.generateTodayDate2_plus(0, 6) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(2),
                date.generateTodayDate2_plus(0, 7) + "T14:30:00.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(3),
                date.generateTodayDate2_plus(0, 4) + "T12:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(3),
                date.generateTodayDate2_plus(0, 5) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(4),
                date.generateTodayDate2_plus(0, 5) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(4),
                date.generateTodayDate2_plus(0, 6) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(5),
                date.generateTodayDate2_plus(0, 6) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(5),
                date.generateTodayDate2_plus(0, 7) + "T14:30:00.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(6),
                date.generateTodayDate2_plus(0, 4) + "T12:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(6),
                date.generateTodayDate2_plus(0, 5) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(7),
                date.generateTodayDate2_plus(0, 5) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(7),
                date.generateTodayDate2_plus(0, 6) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(8),
                date.generateTodayDate2_plus(0, 6) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(8),
                date.generateTodayDate2_plus(0, 7) + "T14:30:00.000+0000");
    }

}
