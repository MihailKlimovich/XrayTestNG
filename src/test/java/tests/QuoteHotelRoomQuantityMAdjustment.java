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
                        date.generateTodayDate2_plus(0, 3) + " thn__Closed_Status__c='Won'", ORG_USERNAME);
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
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'", ORG_USERNAME);

    }

}
