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

public class HotelRoomRateChangeNotSentToMews extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote. Instantiate a Quote Hotel Room. Open the Group Booking" +
            " component and change the Prices per day for the Quote hotel room. Change the Stage of the Quote to" +
            " ‘2 - Propose’. Go to the Quote Hotel Room record and update the Departure Date. Expected result: The" +
            " date has been updated on the SF reservations and in MEWS. The Prices were not changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-315: Hotel room rate change not sent to Mews")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB-315Autotest'", ORG_USERNAME);
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB-315Autotest' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 5) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 10) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                        quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(1) +
                        "' thn__Arrival_Date_Time__c=" + date.generateTodayDate2_plus(0, 5) +
                        "T11:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 8) + "T15:00:00.000+0000 thn__Unit_Price__c=400",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("TB-315Autotest");
        myceQuotes.clickGroupBookingTab();
        groupBookingComponent.changePricePerDay("1", "300");
        groupBookingComponent.changePricePerDay("2", "200");
        groupBookingComponent.clickSaveButton();
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID + "'",
                "thn__Departure_Date_Time__c='" + date.generateTodayDate2_plus(0, 9) +
                        "T15:00:00.000+0000" , ORG_USERNAME);
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'", ORG_USERNAME);
        String reservationID = JsonParser2.getFieldValue(reservationRecord.toString(), "Id");
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        reservations.updateReservationSFDX(SFDX, "Id='" + reservationID + "'", "thn__Send_to_Mews__c=true",
                ORG_USERNAME);
        StringBuilder reservationPriceRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__GrossValue__c FROM" +
                " thn__Reservation_Price__c WHERE thn__Reservation__c='" + reservationID + "'", ORG_USERNAME);
        List<String> reservationPriceID = JsonParser2.getFieldValueSoql(reservationPriceRecords.toString(), "Id");
        List<Integer> reservationPriceGrossValue = JsonParser2.
                getFieldValueSoql2(reservationPriceRecords.toString(), "thn__GrossValue__c");
        Assert.assertEquals(reservationPriceID.size(), 4);
        mews.logIn("thynk@mews.li", "sample");
        mews.findRecordByID(reservationMewsID, "TB-315Autotest");
        String mewsDeparture = mews.readDeparture();
        String night1Rate = mews.readNightsRate("1");
        String night2Rate = mews.readNightsRate("2");
        String night3Rate = mews.readNightsRate("3");
        String night4Rate = mews.readNightsRate("4");
        Assert.assertEquals(reservationPriceGrossValue.get(0).intValue(), 300);
        Assert.assertEquals(reservationPriceGrossValue.get(1).intValue(), 200);
        Assert.assertEquals(reservationPriceGrossValue.get(2).intValue(), 400);
        Assert.assertEquals(reservationPriceGrossValue.get(3).intValue(), 300);
        Assert.assertEquals(night1Rate, "€300.00");
        Assert.assertEquals(night2Rate, "€200.00");
        Assert.assertEquals(night3Rate, "€400.00");
        Assert.assertEquals(night4Rate, "€300.00");
        Assert.assertTrue(mewsDeparture.contains(date.generateDate_plus4(0, 9)));
    }

}
