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
        mews.logIn("thynk@mews.li", "sample");
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
        Thread.sleep(15000);
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomId + "'", ORG_USERNAME);
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        mews.findRecordByID(reservationMewsID, "ReservationAmountToZeroAutoTest");
        String avgRate = mews.readAvgRate();
        String totalAmount = mews.readTotalAmount();
        Assert.assertEquals(avgRate, "€0.00");
        Assert.assertEquals(totalAmount, "€0.00");
    }

    @Test(priority = 2, description = "In CMT > Default Agile Value Set the checkbox Do not price reservations to" +
            " true. Create a MYCE Quote, Instantiate a Quote Hotel Room, Set the prices on the Quote hotel room" +
            " prices to 50. On QHR set checkbox Do not price reservations = false. Change the Stage of the Quote to" +
            " ‘2 - Propose’. Reservations are created and Sent to Mews. Expected Result: The prices on the Mews side" +
            " are at 50.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-695: Reservation amount to 0 - Test")
    public void case2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ReservationAmountToZeroAutoTest2'", ORG_USERNAME);
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ReservationAmountToZeroAutoTest2'" +
                        " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                        quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "'", ORG_USERNAME);
        StringBuilder qhrPriceRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room_Price__c" +
                " WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        List<String> qhrPriceID = JsonParser2.getFieldValueSoql(qhrPriceRecords.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + qhrPriceID.get(0) + "'",
                "thn__Unit_Price_excl_Tax__c=50 thn__Unit_Price_incl_Tax__c=50 thn__Sales_Price_excl_Tax__c=50" +
                        " thn__Sales_Price_incl_Tax__c=50", ORG_USERNAME);
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + qhrPriceID.get(1) + "'",
                "thn__Unit_Price_excl_Tax__c=50 thn__Unit_Price_incl_Tax__c=50 thn__Sales_Price_excl_Tax__c=50" +
                        " thn__Sales_Price_incl_Tax__c=50", ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId + "'",
                "thn__Do_Not_Price_Reservations__c=false", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        Thread.sleep(15000);
        mews.goToMainPage();
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomId + "'", ORG_USERNAME);
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        mews.goToMainPage();
        mews.findRecordByID(reservationMewsID, "ReservationAmountToZeroAutoTest2");
        String avgRate = mews.readAvgRate();
        String totalAmount = mews.readTotalAmount();
        Assert.assertEquals(avgRate, "€50.00");
        Assert.assertEquals(totalAmount, "€100.00");
    }

    @Test(priority = 3, description = "In CMT > Default Agile Value Set the checkbox Do not price reservations to" +
            " false. Create a MYCE Quote, Instantiate a Quote Hotel Room, Set the prices on the Quote hotel room" +
            " prices to 50. On QHR set checkbox Do not price reservations = true. Change the Stage of the Quote to" +
            " ‘2 - Propose’. Reservations are created and Sent to Mews. Expected Result: The prices on the Mews side" +
            " are at 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-695: Reservation amount to 0 - Test")
    public void case3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ReservationAmountToZeroAutoTest3'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DoNotPriceReservationsFalse.apex");
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ReservationAmountToZeroAutoTest3'" +
                        " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "'", ORG_USERNAME);
        StringBuilder qhrPriceRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room_Price__c" +
                " WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        List<String> qhrPriceID = JsonParser2.getFieldValueSoql(qhrPriceRecords.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + qhrPriceID.get(0) + "'",
                "thn__Unit_Price_excl_Tax__c=50 thn__Unit_Price_incl_Tax__c=50 thn__Sales_Price_excl_Tax__c=50" +
                        " thn__Sales_Price_incl_Tax__c=50", ORG_USERNAME);
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + qhrPriceID.get(1) + "'",
                "thn__Unit_Price_excl_Tax__c=50 thn__Unit_Price_incl_Tax__c=50 thn__Sales_Price_excl_Tax__c=50" +
                        " thn__Sales_Price_incl_Tax__c=50", ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId + "'",
                "thn__Do_Not_Price_Reservations__c=true", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        Thread.sleep(15000);
        mews.goToMainPage();
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomId + "'", ORG_USERNAME);
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        mews.goToMainPage();
        mews.findRecordByID(reservationMewsID, "ReservationAmountToZeroAutoTest3");
        String avgRate = mews.readAvgRate();
        String totalAmount = mews.readTotalAmount();
        Assert.assertEquals(avgRate, "€0.00");
        Assert.assertEquals(totalAmount, "€0.00");
    }

    @Test(priority = 4, description = "In CMT > Default Agile Value Set the checkbox Do not price reservations to" +
            " false. Create a MYCE Quote, Instantiate a Quote Hotel Room, Set the prices on the Quote hotel room" +
            " prices to 50. On QHR set checkbox Do not price reservations = false. Change the Stage of the Quote to" +
            " ‘2 - Propose’. Reservations are created and Sent to Mews. Reservations on Mews side have values from" +
            " QHRP. Change on QHR Do not price reservations = true. Click Send to Mews.  Expected Result:" +
            " reservations be updated to 0 value on Mews side.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-695: Reservation amount to 0 - Test")
    public void case4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ReservationAmountToZeroAutoTest4'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DoNotPriceReservationsFalse.apex");
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ReservationAmountToZeroAutoTest4'" +
                        " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "'", ORG_USERNAME);
        StringBuilder qhrPriceRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room_Price__c" +
                " WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        List<String> qhrPriceID = JsonParser2.getFieldValueSoql(qhrPriceRecords.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + qhrPriceID.get(0) + "'",
                "thn__Unit_Price_excl_Tax__c=50 thn__Unit_Price_incl_Tax__c=50 thn__Sales_Price_excl_Tax__c=50" +
                        " thn__Sales_Price_incl_Tax__c=50", ORG_USERNAME);
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + qhrPriceID.get(1) + "'",
                "thn__Unit_Price_excl_Tax__c=50 thn__Unit_Price_incl_Tax__c=50 thn__Sales_Price_excl_Tax__c=50" +
                        " thn__Sales_Price_incl_Tax__c=50", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        Thread.sleep(15000);
        mews.goToMainPage();
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomId + "'", ORG_USERNAME);
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        mews.goToMainPage();
        mews.findRecordByID(reservationMewsID, "ReservationAmountToZeroAutoTest4");
        String avgRate = mews.readAvgRate();
        String totalAmount = mews.readTotalAmount();
        Assert.assertEquals(avgRate, "€50.00");
        Assert.assertEquals(totalAmount, "€100.00");
        mews.goToMainPage();
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId + "'",
                "thn__Do_Not_Price_Reservations__c=true", ORG_USERNAME);
        reservations.updateReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'",
                "thn__Send_to_Mews__c=true", ORG_USERNAME);
        mews.findRecordByID(reservationMewsID, "ReservationAmountToZeroAutoTest4");
        String updatedAvgRate = mews.readAvgRate();
        String updatedTotalAmount = mews.readTotalAmount();
        Assert.assertEquals(updatedAvgRate, "€0.00");
        Assert.assertEquals(updatedTotalAmount, "€0.00");
    }



    @AfterClass
    public void returnSettings() throws InterruptedException, IOException{
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DefaultAgileValueForUnlockedOrg.apex");
        }

}
