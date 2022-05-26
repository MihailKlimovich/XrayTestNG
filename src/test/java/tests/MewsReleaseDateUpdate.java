package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class MewsReleaseDateUpdate extends BaseTest {


    @Test(priority = 1, description = "Create a MYCE Quote. Instantiate a Quote hotel Room. Change the stage of" +
            " the MYCE Quote to ‘2 - Propose’. Change the Mews state of the Quote Hotel Room to Optional. Expected" +
            " result: Reservations are created and sent to Mews. State is changed to Optional on the reservation.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-641: Mews - Release date update")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MewsReleaseDateUpdateAutoTest'", ORG_USERNAME);
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MewsReleaseDateUpdateAutoTest' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 5) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 10) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(1) +
                "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID + "'",
                "thn__Mews_State__c='Optional'", ORG_USERNAME);
        StringBuilder myceQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteReleaseDate = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Release_Date__c");
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='" +
                quoteHotelRoomID + "'", ORG_USERNAME);
        String reservationState = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__State__c");
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        Assert.assertEquals(quoteReleaseDate, date.generateTodayDate2_plus(0, 4));
        Assert.assertEquals(reservationState, "Optional");
        Assert.assertNotNull(reservationMewsID);
    }

    @Test(priority = 2, description = "Change the Realeased Utc on the Reservation. Set the checkbox Send to Mews to" +
            " true. Expected result: Release date is updated in MEWS.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-641: Mews - Release date update")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='MewsReleaseDateUpdateAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        reservations.updateReservationSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__ReleasedUtc__c=" + date.generateTodayDate2_plus(0, 3) + "T11:00:00.000+0000",
                ORG_USERNAME);
        reservations.updateReservationSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Send_to_Mews__c=true", ORG_USERNAME);
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'", ORG_USERNAME);
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        String reservationReleaseUtc= JsonParser2.getFieldValue(reservationRecord.
                toString(), "thn__ReleasedUtc__c");
        mews.logIn("thynk@mews.li", "sample");
        mews.findRecordByID(reservationMewsID, "MewsReleaseDateUpdateAutoTest");
        String releaseDate = mews.readRelease();
        Assert.assertEquals(reservationReleaseUtc, date.generateTodayDate2_plus(0, 3) +
                "T11:00:00.000+0000");
        Assert.assertTrue(releaseDate.contains(date.generateDate_plus4(0, 3)));
    }

}
