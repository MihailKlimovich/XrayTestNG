package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteHotelRoomQuantityReservations extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote: Arrival Date = today + 1 day, Departure Date = today +" +
            " 5 days. Instantiate a Quote Hotel Room. Change the stage of the MYCE Quote to ‘2 - Propose." +
            " Expected result: Five reservation records are created." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-664: Quote hotel room quantity - Reservations")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityReservationsAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");

        StringBuilder guest = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Guest__c WHERE" +
                " thn__Mews_Id__c!=null AND thn__Hotel__c='" + propertyID +  " LIMIT 1", ORG_USERNAME);
        List<String> guestID = JsonParser2.getFieldValueSoql(guest.toString(), "Id");
        System.out.println(guestID);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityReservationsAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0  ,1) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 5) + " RecordTypeId='" + recordTypeID.get(0), ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Reservation_Guest__c='" +
                guestID.get(0) + "'", ORG_USERNAME);
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) +
                "' thn__Property__c='" + propertyID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__SendToMews__c='true'",
                ORG_USERNAME);
        Thread.sleep(2000);
        StringBuilder reservationsRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Reservation__c WHERE" +
                " thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        List<String> reservationsID = JsonParser2.getFieldValueSoql(reservationsRecords.toString(), "Id");
        Assert.assertEquals(reservationsID.size(), 5);
    }

    @Test(priority = 2, description = "Go to Quote Hotel Room Prices. Change the Quantity for the QHPR with the date" +
            " today + 3 days from 5 to 7. Expected result: two more Reservation records are created." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-664: Quote hotel room quantity - Reservations")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityReservationsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder quoteHotelRoomPrice1 = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'" +
                " AND thn__Date__c=" + date.generateTodayDate2_plus(0 , 3), ORG_USERNAME);
        List<String> quoteHotelRoomPrceID1 = JsonParser2.
                getFieldValueSoql(quoteHotelRoomPrice1.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + quoteHotelRoomPrceID1.get(0) + "'",
                "thn__Quantity__c=7", ORG_USERNAME);
        StringBuilder reservationsRecords1 = myceQuotes.soql(SFDX, "SELECT Id, thn__StartUtc__c, thn__EndUtc__c" +
                " FROM thn__Reservation__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'", ORG_USERNAME);
        System.out.println(reservationsRecords1);
        List<String> reservationsID1 = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "Id");
        List<String> reservationsStartUTC = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__StartUtc__c");
        List<String> reservationsEndUTC = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__EndUtc__c");
        Assert.assertEquals(reservationsID1.size(), 7);
        Assert.assertEquals(reservationsStartUTC.get(0),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(0),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(1),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(1),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(2),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(2),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(3),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(3),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(4),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(4),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(5),
                date.generateTodayDate2_plus(0, 3) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(5),
                date.generateTodayDate2_plus(0, 4) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(6),
                date.generateTodayDate2_plus(0, 3) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(6),
                date.generateTodayDate2_plus(0, 4) + "T13:30:00.000+0000");
    }

    @Test(priority = 3, description = "Go to Quote Hotel Room Prices. Change the Quantity for the QHPR with the date" +
            " today + 3 days from 7 to 5. Expected result: Two reservation records with Arrival = today + 3 days and" +
            " Departure = today + 4 days are canceled." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-664: Quote hotel room quantity - Reservations")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityReservationsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder quoteHotelRoomPrice1 = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'" +
                " AND thn__Date__c=" + date.generateTodayDate2_plus(0 , 3), ORG_USERNAME);
        List<String> quoteHotelRoomPrceID1 = JsonParser2.
                getFieldValueSoql(quoteHotelRoomPrice1.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + quoteHotelRoomPrceID1.get(0) + "'",
                "thn__Quantity__c=5", ORG_USERNAME);
        StringBuilder reservationsRecords1 = myceQuotes.soql(SFDX, "SELECT Id, thn__StartUtc__c, thn__EndUtc__c," +
                " thn__State__c FROM thn__Reservation__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                ORG_USERNAME);
        System.out.println(reservationsRecords1);
        List<String> reservationsID1 = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "Id");
        List<String> reservationsStartUTC = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__StartUtc__c");
        List<String> reservationsEndUTC = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__EndUtc__c");
        List<String> reservationsState = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__State__c");
        Assert.assertEquals(reservationsID1.size(), 7);
        Assert.assertEquals(reservationsStartUTC.get(0),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(0),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(1),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(1),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(2),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(2),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(3),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(3),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(4),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(4),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(5),
                date.generateTodayDate2_plus(0, 3) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(5),
                date.generateTodayDate2_plus(0, 4) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(6),
                date.generateTodayDate2_plus(0, 3) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(6),
                date.generateTodayDate2_plus(0, 4) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsState.get(0), "Confirmed");
        Assert.assertEquals(reservationsState.get(1), "Confirmed");
        Assert.assertEquals(reservationsState.get(2), "Confirmed");
        Assert.assertEquals(reservationsState.get(3), "Confirmed");
        Assert.assertEquals(reservationsState.get(4), "Confirmed");
        Assert.assertEquals(reservationsState.get(5), "Canceled");
        Assert.assertEquals(reservationsState.get(6), "Canceled");
    }

    @Test(priority = 4, description = "Go to the Quote Hotel Room and update the Departure date to today + 7 days." +
            " Expected result: Reservations with State = Confirmed are updated. Departure date updated on the" +
            " Reservation." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-664: Quote hotel room quantity - Reservations")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityReservationsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID + "'",
                "thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 7) +
                        "T13:30:00.000+0000", ORG_USERNAME);
        StringBuilder reservationsRecords1 = myceQuotes.soql(SFDX, "SELECT Id, thn__StartUtc__c, thn__EndUtc__c," +
                        " thn__State__c FROM thn__Reservation__c WHERE thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomID + "'", ORG_USERNAME);
        System.out.println(reservationsRecords1);
        List<String> reservationsID1 = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "Id");
        List<String> reservationsStartUTC = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__StartUtc__c");
        List<String> reservationsEndUTC = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__EndUtc__c");
        List<String> reservationsState = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__State__c");
        Assert.assertEquals(reservationsID1.size(), 7);
        Assert.assertEquals(reservationsStartUTC.get(0),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(0),
                date.generateTodayDate2_plus(0, 7) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(1),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(1),
                date.generateTodayDate2_plus(0, 7) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(2),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(2),
                date.generateTodayDate2_plus(0, 7) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(3),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(3),
                date.generateTodayDate2_plus(0, 7) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(4),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(4),
                date.generateTodayDate2_plus(0, 7) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(5),
                date.generateTodayDate2_plus(0, 3) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(5),
                date.generateTodayDate2_plus(0, 4) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(6),
                date.generateTodayDate2_plus(0, 3) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(6),
                date.generateTodayDate2_plus(0, 4) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsState.get(0), "Confirmed");
        Assert.assertEquals(reservationsState.get(1), "Confirmed");
        Assert.assertEquals(reservationsState.get(2), "Confirmed");
        Assert.assertEquals(reservationsState.get(3), "Confirmed");
        Assert.assertEquals(reservationsState.get(4), "Confirmed");
        Assert.assertEquals(reservationsState.get(5), "Canceled");
        Assert.assertEquals(reservationsState.get(6), "Canceled");
    }

    @Test(priority = 5, description = "Use the Change Date flow. Select new Date =  today + 2 day." +
            " Expected result:Reservations are updated. Arrival and Departure Dates are updated with new values. " )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-664: Quote hotel room quantity - Reservations")
    public void case5() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityReservationsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("QuoteHotelRoomQuantityReservationsAutoTest");
        myceQuotes.changeDate(date.generateTodayDate3_plus(0, 2));
        StringBuilder reservationsRecords1 = myceQuotes.soql(SFDX, "SELECT Id, thn__StartUtc__c, thn__EndUtc__c," +
                " thn__State__c FROM thn__Reservation__c WHERE thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomID + "'", ORG_USERNAME);
        System.out.println(reservationsRecords1);
        List<String> reservationsID1 = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "Id");
        List<String> reservationsStartUTC = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__StartUtc__c");
        List<String> reservationsEndUTC = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__EndUtc__c");
        List<String> reservationsState = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__State__c");
        Assert.assertEquals(reservationsID1.size(), 7);
        Assert.assertEquals(reservationsStartUTC.get(0),
                date.generateTodayDate2_plus(0, 2) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(0),
                date.generateTodayDate2_plus(0, 8) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(1),
                date.generateTodayDate2_plus(0, 2) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(1),
                date.generateTodayDate2_plus(0, 8) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(2),
                date.generateTodayDate2_plus(0, 2) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(2),
                date.generateTodayDate2_plus(0, 8) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(3),
                date.generateTodayDate2_plus(0, 2) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(3),
                date.generateTodayDate2_plus(0, 8) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(4),
                date.generateTodayDate2_plus(0, 2) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(4),
                date.generateTodayDate2_plus(0, 8) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(5),
                date.generateTodayDate2_plus(0, 4) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(5),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(6),
                date.generateTodayDate2_plus(0, 4) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(6),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsState.get(0), "Confirmed");
        Assert.assertEquals(reservationsState.get(1), "Confirmed");
        Assert.assertEquals(reservationsState.get(2), "Confirmed");
        Assert.assertEquals(reservationsState.get(3), "Confirmed");
        Assert.assertEquals(reservationsState.get(4), "Confirmed");
        Assert.assertEquals(reservationsState.get(5), "Canceled");
        Assert.assertEquals(reservationsState.get(6), "Canceled");
    }

    @Test(priority = 6, description = "Go to the Quote Hotel Room Prices and update the Quantity = 4 on the records" +
            " with dates = today + 4 days and today + 5 days. Expected result: Two more reservations are created." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-664: Quote hotel room quantity - Reservations")
    public void case6() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityReservationsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder quoteHotelRoomPrice1 = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'" +
                " AND thn__Date__c=" + date.generateTodayDate2_plus(0 , 4), ORG_USERNAME);
        List<String> quoteHotelRoomPriceID1 = JsonParser2.
                getFieldValueSoql(quoteHotelRoomPrice1.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + quoteHotelRoomPriceID1.get(0) + "'",
                "thn__Quantity__c=4", ORG_USERNAME);
        StringBuilder quoteHotelRoomPrice2 = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'" +
                " AND thn__Date__c=" + date.generateTodayDate2_plus(0 , 5), ORG_USERNAME);
        List<String> quoteHotelRoomPriceID2= JsonParser2.
                getFieldValueSoql(quoteHotelRoomPrice2.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + quoteHotelRoomPriceID2.get(0) + "'",
                "thn__Quantity__c=4", ORG_USERNAME);
        StringBuilder reservationsRecords1 = myceQuotes.soql(SFDX, "SELECT Id, thn__StartUtc__c, thn__EndUtc__c" +
                " FROM thn__Reservation__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'", ORG_USERNAME);
        List<String> reservationsID1 = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "Id");
        List<String> reservationsStartUTC = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__StartUtc__c");
        List<String> reservationsEndUTC = JsonParser2.
                getFieldValueSoql(reservationsRecords1.toString(), "thn__EndUtc__c");
        Assert.assertEquals(reservationsID1.size(), 7);
        Assert.assertEquals(reservationsStartUTC.get(0),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(0),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(1),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(1),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(2),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(2),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(3),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(3),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(4),
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(4),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(5),
                date.generateTodayDate2_plus(0, 3) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(5),
                date.generateTodayDate2_plus(0, 4) + "T13:30:00.000+0000");
        Assert.assertEquals(reservationsStartUTC.get(6),
                date.generateTodayDate2_plus(0, 3) + "T11:00:00.000+0000");
        Assert.assertEquals(reservationsEndUTC.get(6),
                date.generateTodayDate2_plus(0, 4) + "T13:30:00.000+0000");
    }

}
