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

public class RoomingListArrivalDateNotUpdatedIfArrivalDateOfTheReservationIsChanged extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote .Instantiate a Quote Hotel room. Create Reservations" +
            " (Change the stage of the Quote to '2 - Propose). Set Checkbox ‘Generate Rooming List’ = True on" +
            " MYCE Quote. Change the Departure Date on one of the Reservations. Expected Result:" +
            " The Departure Date Time on the related Rooming List record of the Reservation didn’t change.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-333: Rooming list arrival date not updated if arrival date of the reservation is changed")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB333Autotest'", ORG_USERNAME);
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB333Autotest' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 5) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 10) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Pax__c=1", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__SendToMews__c='true'",
                ORG_USERNAME);
        Thread.sleep(2000);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Generate_Rooming_List__c=true",
                ORG_USERNAME);
        reservations.updateReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'",
                "thn__EndUtc__c=" + date.generateTodayDate2_plus(0, 15) + "T15:29:00.000+0000",
                ORG_USERNAME);
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='" +
                quoteHotelRoomId + "'", ORG_USERNAME);
        String reservationDepartureDateTime = JsonParser2.
                getFieldValue(reservationRecord.toString(), "thn__EndUtc__c");
        String reservationId = JsonParser2.getFieldValue(reservationRecord.toString(), "Id");
        StringBuilder roomingListRecord = roomingList.
                getRoomungListSFDX(SFDX, "thn__Reservation__c='" + reservationId + "'", ORG_USERNAME);
        String roomingListDepartureDateTime = JsonParser2.
                getFieldValue(roomingListRecord.toString(), "thn__Departure_Date_Time__c");
        Assert.assertEquals(reservationDepartureDateTime,
                date.generateTodayDate2_plus(0, 15) + "T15:29:00.000+0000");
        Assert.assertEquals(roomingListDepartureDateTime,
                date.generateTodayDate2_plus(0, 10) + "T14:30:00.000+0000");
    }

    @Test(priority = 2, description = "Set checkbox ‘Generate Rooming List’ = True on MYCE Quote. Expected Result:" +
            " The Departure Date Time on the related Rooming List record of the Reservation changeв to the value" +
            " specified in the Reservation.Departure Date.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-333: Rooming list arrival date not updated if arrival date of the reservation is changed")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='TB333Autotest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Generate_Rooming_List__c=true",
                ORG_USERNAME);
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='" +
                quoteHotelRoomID + "'", ORG_USERNAME);
        String reservationDepartureDateTime = JsonParser2.
                getFieldValue(reservationRecord.toString(), "thn__EndUtc__c");
        String reservationId = JsonParser2.getFieldValue(reservationRecord.toString(), "Id");
        StringBuilder roomingListRecord = roomingList.
                getRoomungListSFDX(SFDX, "thn__Reservation__c='" + reservationId + "'", ORG_USERNAME);
        String roomingListDepartureDateTime = JsonParser2.
                getFieldValue(roomingListRecord.toString(), "thn__Departure_Date_Time__c");
        Assert.assertEquals(reservationDepartureDateTime,
                date.generateTodayDate2_plus(0, 15) + "T15:29:00.000+0000");
        Assert.assertEquals(roomingListDepartureDateTime,
                date.generateTodayDate2_plus(0, 15) + "T15:29:00.000+0000");
    }

}
