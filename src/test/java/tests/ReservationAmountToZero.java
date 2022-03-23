package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class ReservationAmountToZero extends BaseTest {

    @Test(priority = 1, description = "In CMT > Default Agile Value Set the checkbox Do not price reservations to" +
            " true. Create a MYCE Quote, Instantiate a Quote Hotel Room, Set the prices on the Quote hotel room" +
            " prices to 50. Change the Stage of the Quote to ‘2 - Propose’. Reservations are created and Sent to" +
            " Mews. Expected Result: The prices on the Mews side are at 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-695: Reservation amount to 0 - Test")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ReservationAmountToZeroAutoTest'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DoNotPriceReservationsTrue.apex");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Space_Area__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null", ORG_USERNAME);
        List<String> roomTypeID = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ReservationAmountToZeroAutoTest'" +
                " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) + "'",
                ORG_USERNAME);
        StringBuilder qhrPriceRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room_Price__c" +
                " WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        List<String> qhrPriceID = JsonParser2.getFieldValueSoql(qhrPriceRecords.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + qhrPriceID.get(0) + "'",
                "thn__Unit_Price_excl_Tax__c=50 thn__Unit_Price_incl_Tax__c=50 thn__Sales_Price_excl_Tax__c=50" +
                        " thn__Sales_Price_incl_Tax__c=50", ORG_USERNAME);
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + qhrPriceID.get(1) + "'",
                "thn__Unit_Price_excl_Tax__c=50 thn__Unit_Price_incl_Tax__c=50 thn__Sales_Price_excl_Tax__c=50" +
                        " thn__Sales_Price_incl_Tax__c=50", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'", ORG_USERNAME);
        Thread.sleep(5000);
        mews.logIn("thynk@mews.li", "sample");
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomId + "'", ORG_USERNAME);
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        mews.findRecordByID(reservationMewsID, "ReservationAmountToZeroAutoTest");
        String avgRate = mews.readAvgRate();
        String totalAmount = mews.readTotalAmount();
        Assert.assertEquals(avgRate, "€0.00");
        Assert.assertEquals(totalAmount, "€0.00");
    }

    @AfterClass
    public void returnSettings() throws InterruptedException, IOException{
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DefaultAgileValueForUnlockedOrg.apex");
        }

}
