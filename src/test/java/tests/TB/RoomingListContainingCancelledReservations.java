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

public class RoomingListContainingCancelledReservations extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote. Instantiate 2 Quote Hotel Room. Change the Stage of" +
            " the Quote to ‘2 - Propose’. Set the checkbox ‘Send to Mews’ to true. Go to one of the Quote Hotel" +
            " Rooms and change the Mews State to Canceled. Set the checkbox ‘Generate rooming list’ to true." +
            " Expected Result: Open the generated xlsx file. Only the non canceled reservations are listed in the" +
            " xlsx file.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-327: Rooming list containing cancelled reservations")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB327Autotest'", ORG_USERNAME);
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB327Autotest' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 5) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 10) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Pax__c=1", ORG_USERNAME);
        String quoteHotelRoomId2 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(1) +
                "' thn__Property__c='" + propertyID + "' thn__Pax__c=1", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__SendToMews__c='true'",
                ORG_USERNAME);
        Thread.sleep(2000);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Generate_Rooming_List__c=true",
                ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId + "'",
                "thn__Mews_State__c='Canceled' thn__Reason_Canceled__c='151515'", ORG_USERNAME);
        StringBuilder reservationRecord1 = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='" +
                quoteHotelRoomId + "'", ORG_USERNAME);
        String reservationID1 = JsonParser2.getFieldValue(reservationRecord1.toString(), "Id");
        StringBuilder reservationRecord2 = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='" +
                quoteHotelRoomId2 + "'", ORG_USERNAME);
        String reservationID2 = JsonParser2.getFieldValue(reservationRecord2.toString(), "Id");
        StringBuilder roomingList1 = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rooming_List__c WHERE" +
                " thn__Reservation__c='" + reservationID1 + "'", ORG_USERNAME);
        List<String> roomingListID1 = JsonParser2.getFieldValueSoql(roomingList1.toString(), "Id");
        StringBuilder roomingList2 = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rooming_List__c WHERE" +
                " thn__Reservation__c='" + reservationID2 + "'", ORG_USERNAME);
        List<String> roomingListID2 = JsonParser2.getFieldValueSoql(roomingList2.toString(), "Id");
        Assert.assertEquals(roomingListID1.size(), 0);
        Assert.assertEquals(roomingListID2.size(), 1);
    }

}
