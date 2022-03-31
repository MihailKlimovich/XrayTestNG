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

public class HotelRoomsRevenueIsCountedToTheQuoteTotalHotelRoomRevenueDespiteTheHotelRoomsBeingCancelled extends
        BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote. Instantiate a Quote Hotel Room. Change the Mews State on" +
            " the Quote Hotel Room to Canceled. Expected result: Hotel Rooms Amount and Total Amount incl. Tax were" +
            " set to 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-316: Hotel Rooms revenue is counted to the Quote Total Hotel Room revenue, despite the Hotel Rooms " +
            "being cancelled.")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB-316'", ORG_USERNAME);
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB-316' thn__Pax__c=1 thn__Hotel__c='" +
                propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                + roomTypesId.get(0) + "' thn__Unit_Price__c=500", ORG_USERNAME);
        StringBuilder myceQuteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomAmount = JsonParser2.
                getFieldValue(myceQuteRecord.toString(), "thn__Hotel_Rooms_Amount__c");
        String quoteTotalAmountInclTax = JsonParser2.
                getFieldValue(myceQuteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID + "'",
                "thn__Mews_State__c='Canceled'", ORG_USERNAME);
        StringBuilder updatedMyceQuteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String updatedQuoteHotelRoomAmount = JsonParser2.
                getFieldValue(updatedMyceQuteRecord.toString(), "thn__Hotel_Rooms_Amount__c");
        String updatedQuoteTotalAmountInclTax = JsonParser2.
                getFieldValue(updatedMyceQuteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        Assert.assertNotEquals(updatedQuoteHotelRoomAmount, quoteHotelRoomAmount);
        Assert.assertNotEquals(updatedQuoteTotalAmountInclTax, quoteTotalAmountInclTax);
        Assert.assertEquals(updatedQuoteHotelRoomAmount, "0");
        Assert.assertEquals(updatedQuoteTotalAmountInclTax, "0");
    }

}
