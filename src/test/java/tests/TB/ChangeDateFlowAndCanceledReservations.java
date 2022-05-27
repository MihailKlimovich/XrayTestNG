package tests.TB;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import tests.BaseTest;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ChangeDateFlowAndCanceledReservations extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote. Instantiate a Quote Hotel Room. Change the stage of the" +
            " MYCE Quote to ‘2 - Propose’ (Reservation guest is created, sent to mews and linked to the MYCE Quote," +
            " Reservations are created.) Change the Stage of the MYCE Quote to ‘4 - Closed’," +
            " Closed Status - ‘Canceled’. (Reservations are canceled. Use Change Date Flow. Expected result: Dates" +
            " of the canceled reservations should not be changed when change date flow is used.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-298: Change Date Flow and Canceled Reservations")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        Thread.sleep(3000);
        URL baseUrl = new URL(driver.getCurrentUrl());
        String url = "https://" + baseUrl.getAuthority() + "/lightning/o/thn__MYCE_Quote__c/list?filterName=Recent";
        System.out.println(url);
        driver.navigate().to(url);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ChangeDateFlowAndCanceledReservationsAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord1= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord1.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        System.out.println(guests);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='ChangeDateFlowAndCanceledReservationsAutoTest' thn__Pax__c=1 thn__Hotel__c='"
                        + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5) +
                        " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) +
                "' thn__Property__c='" + propertyID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'", ORG_USERNAME);
        StringBuilder myceQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID +"'", ORG_USERNAME);
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteArrivalDate = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Arrival_Date__c");
        String quoteDepartureDate = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Departure_Date__c");
        String reservationArrival = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__StartUtc__c");
        String reservationDeparture = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__EndUtc__c");
        Assert.assertTrue(reservationArrival.contains(quoteArrivalDate));
        Assert.assertTrue(reservationDeparture.contains(quoteDepartureDate));
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Lost'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ChangeDateFlowAndCanceledReservationsAutoTest");
        myceQuotes.changeDate(date.generateTodayDate3_plus(0, 3));
        StringBuilder updatedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID +"'", ORG_USERNAME);
        StringBuilder updatedReservationRecord = reservations.getReservationSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String updatedQuoteArrivalDate = JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Arrival_Date__c");
        String updatedQuoteDepartureDate = JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Departure_Date__c");
        String updatedReservationArrival = JsonParser2.
                getFieldValue(updatedReservationRecord.toString(), "thn__StartUtc__c");
        String updatedReservationDeparture = JsonParser2.
                getFieldValue(updatedReservationRecord.toString(), "thn__EndUtc__c");
        String reservationState = JsonParser2.
                getFieldValue(updatedReservationRecord.toString(), "thn__State__c");
        Assert.assertEquals(updatedQuoteArrivalDate, date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(updatedQuoteDepartureDate, date.generateTodayDate2_plus(0, 6));
        Assert.assertEquals(updatedReservationArrival, reservationArrival);
        Assert.assertEquals(updatedReservationDeparture, reservationDeparture);
        Assert.assertEquals(reservationState, "Canceled");
    }

}
