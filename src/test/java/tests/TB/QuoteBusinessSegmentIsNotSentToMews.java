package tests.TB;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import tests.BaseTest;

import java.io.IOException;
import java.util.List;

public class QuoteBusinessSegmentIsNotSentToMews extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote. Select a valid Business Segment with MEWS Id on" +
            " MYCE Quote. Instantiate a Quote Hotel Room. Change the Stage of the Quote to ‘2 - Propose’ (Goal is" +
            " to have Reservation guest created). Set checkbox ‘Send to Mews’ = True on MYCE Quote (Reservations are" +
            " created and sent to MEWS). Open the QHR > change the Rate Plan (Reservations prices are updated and" +
            " sent to MEWS). Expected result: Business segment on Reservation was not set to Null.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-279: Quote business segment is not sent to Mews.")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteBusinessSegmentIsNotSentToMewsAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
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
        StringBuilder businessSegmentRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Business_Segment__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        List<String> businessSegmentId = JsonParser2.getFieldValueSoql(businessSegmentRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteBusinessSegmentIsNotSentToMewsAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2)  + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 5) + " thn__Release_Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " RecordTypeId='" + recordTypeID.get(0) +
                "' thn__Business_Segment__c='" + businessSegmentId.get(0) + "'", ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                + roomTypesId.get(0) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'", ORG_USERNAME);
        StringBuilder rateRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Rate__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        List<String> rateId = JsonParser2.getFieldValueSoql(businessSegmentRecords.toString(), "Id");
        Thread.sleep(3000);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String mewsGroupID = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Mews_Group_Id__c");
        Assert.assertNotNull(mewsGroupID);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID + "'",
                "thn__Rate_Plan__c='" + rateId.get(1) + "'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteBusinessSegment = JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Business_Segment__c");
        Assert.assertEquals(quoteBusinessSegment, businessSegmentId.get(0));
    }

}
