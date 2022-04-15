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

public class ReservationStatusDiscrepancyBetweenThynkAndMews extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote. Instantiate a Quote Hotel Room. Change the stage of" +
            " the MYCE Quote to ‘2 - Propose’. Reservations are created and sent to Mews. Edit the Quote hotel room" +
            " record. Set the new values for the Mews state and Departure time. Save so the changes are applied at" +
            " the same time. Expected Result: The reservation updated it’s date and state in mews.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-285: Reservation status discrepancy between Thynk and Mews")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB-285Autotest'", ORG_USERNAME);
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB-285Autotest' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 5) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 10) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(1) +
                "' thn__Arrival_Date_Time__c=" + date.generateTodayDate2_plus(0, 6) + "T11:00:00.000+0000" +
                " thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 9) + "T15:00:00.000+0000",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        Thread.sleep(3000);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID + "'",
                "thn__Mews_State__c='Confirmed' thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 8) + "T15:00:00.000+0000", ORG_USERNAME);
        reservations.updateReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                "thn__Send_to_Mews__c=true", ORG_USERNAME);
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomID + "'", ORG_USERNAME);
        String reservationState = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__State__c");
        String reservationDeparture = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__EndUtc__c");
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        mews.logIn("thynk@mews.li", "sample");
        mews.findRecordByID(reservationMewsID, "TB-285Autotest");
        String mewsDeparture = mews.readDeparture();
        String mewsStatus = mews.readStatus();
        Assert.assertEquals(reservationState, "Confirmed");
        Assert.assertEquals(reservationDeparture, date.generateTodayDate2_plus(0, 8) +
                "T15:00:00.000+0000");
        Assert.assertEquals(mewsStatus, "FUTURE");
        Assert.assertTrue(mewsDeparture.contains(date.generateDate_plus4(0, 8)));
    }

}
