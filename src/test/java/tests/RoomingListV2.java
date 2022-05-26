package tests;

import com.github.automatedowl.tools.SeleniumDownloadKPI;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class RoomingListV2 extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote, specify Reservation Guest. Add Quote hotel room. Send" +
            " Quote to Mews to have Reservations created. Generate rooming lists. Download generated document. In" +
            " the document update Arrival and Departure date/time fields, make sure new dates are within the Quote" +
            " period. In the document specify Room. Fill Last name and Phone (or Email) fields. Save the document" +
            " as a .csv file. Upload document to the quote (using fileUpload component). Expected result: Arrival" +
            " and Departure date/time fields are updated on Rooming list records, Room specified in the document" +
            " is assigned to the Reservation.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-521: Rooming list - v2")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/TemplateConfig");
        files.deleteAllFilesFolder("/home/user/project/thynk-selenium/TemporaryFiles/");
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='RoomingListV2AutoTest'", ORG_USERNAME);
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='RoomingListV2AutoTest'" +
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
        StringBuilder recordRooms = myceQuotes.soql(SFDX, "SELECT Id, thn__Number__c FROM thn__Space__c WHERE" +
                " thn__Space_Area__c='" + roomTypeID.get(0) + "'", ORG_USERNAME);
        List<String> roomID = JsonParser2.getFieldValueSoql(recordRooms.toString(), "Id");
        List<String> roomNumber = JsonParser2.getFieldValueSoql(recordRooms.toString(), "thn__Number__c");
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomId + "'", ORG_USERNAME);
        String reservationID = JsonParser2.getFieldValue(reservationRecord.toString(), "Id");
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'",
                "thn__Generate_Rooming_List__c=true", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("RoomingListV2AutoTest");
        myceQuotes.goToFiles();
        files.clickDownload("Rooming List RoomingListV2AutoTest");
        files.updateXLS("Rooming List RoomingListV2AutoTest.xlsx", 1, 0, "Oreon");
        files.updateXLS("Rooming List RoomingListV2AutoTest.xlsx", 1, 1, "5556667");
        //files.updateXLS("Rooming List RoomingListV2AutoTest.xlsx", 1, 3, roomNumber.get(0));
        files.updateXLS("Rooming List RoomingListV2AutoTest.xlsx", 1, 4,
                date.generateTodayDate2_plus(0, 2) + " 13:25:00");
        files.updateXLS("Rooming List RoomingListV2AutoTest.xlsx", 1, 5,
                date.generateTodayDate2_plus(0, 4) + " 17:55:00");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("RoomingListV2AutoTest");
        myceQuotes.uploadFile
                ("/home/user/project/thynk-selenium/TemporaryFiles/Rooming List RoomingListV2AutoTest.xlsx");
        StringBuilder roomingListRecord = roomingList.
                getRoomungListSFDX(SFDX, "thn__Reservation__c='" + reservationID + "'", ORG_USERNAME);
        String roomingListGuest = JsonParser2.getFieldValue(roomingListRecord.toString(), "thn__Guest__c");
        String roomingListPhone = JsonParser2.getFieldValue(roomingListRecord.toString(), "thn__Phone__c");
        String roomingListArrivalDateTime = JsonParser2.
                getFieldValue(roomingListRecord.toString(), "thn__Arrival_Date_Time__c");
        String roomingListDepartureDateTime = JsonParser2.
                getFieldValue(roomingListRecord.toString(), "thn__Departure_Date_Time__c");
        Assert.assertNotNull(roomingListGuest);
        Assert.assertEquals(roomingListPhone, "5556667");
        Assert.assertEquals(roomingListArrivalDateTime,
                date.generateTodayDate2_plus(0, 2) + "T13:25:00.000+0000");
        Assert.assertEquals(roomingListDepartureDateTime,
                date.generateTodayDate2_plus(0, 4) + "T17:55:00.000+0000");
    }

    @Test(priority = 2, description = "Open Rooming list record. Update Last Name, Phone. Expected result:" +
            " Rooming list record is updated. Guest related to the Rooming list is updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-521: Rooming list - v2")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='RoomingListV2AutoTest'", ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        roomingList.updateRoomingListSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Last_Name__c='Werty' thn__Phone__c='5455458'", ORG_USERNAME);
        StringBuilder roomingListRecord = roomingList.
                getRoomungListSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String roomingListGuest = JsonParser2.getFieldValue(roomingListRecord.toString(), "thn__Guest__c");
        StringBuilder guestRecord = guests.getGuestSFDX(SFDX, "Id='" + roomingListGuest + "'", ORG_USERNAME);
        String guestLastName = JsonParser2.getFieldValue(guestRecord.toString(), "thn__LastName__c");
        String guestPhone = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Phone__c");
        Assert.assertEquals(guestLastName, "Werty");
        Assert.assertEquals(guestPhone, "5455458");
    }

    @Test(priority = 3, description = "Create MYCE Quote, specify Reservation Guest. Add Quote hotel rooms. Send" +
            " Quote to Mews to have Reservations created. Generate rooming lists. Change Reservation State to" +
            " “Cancelled”. Expected result: Rooming lists related to updated Reservation are deleted.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-521: Rooming list - v2")
    public void case3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='RoomingListV2AutoTest2'", ORG_USERNAME);
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='RoomingListV2AutoTest2'" +
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
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomId + "'", ORG_USERNAME);
        String reservationID = JsonParser2.getFieldValue(reservationRecord.toString(), "Id");
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'",
                "thn__Generate_Rooming_List__c=true", ORG_USERNAME);
        reservations.updateReservationSFDX(SFDX, "Id='" + reservationID + "'", "thn__State__c='Canceled'" +
                " thn__Notes__c='Canceled'", ORG_USERNAME);
        StringBuilder roomingListRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rooming_List__c WHERE" +
                " thn__Reservation__c='" + reservationID + "'", ORG_USERNAME);
        List<String> roomingListID = JsonParser2.getFieldValueSoql(roomingListRecords.toString(), "Id");
        Assert.assertEquals(roomingListID.size(), 0);
    }

    @Test(priority = 4, description = "Create MYCE Quote, specify Reservation Guest. Add Quote hotel room. Send" +
            " Quote to Mews to have Reservations created. Generate rooming lists. Specify Arrival date posterior" +
            " to Departure date. Upload document. Expected result: Rooming lists records are not updated / inserted." +
            " Rooming Lists Error records is inserted: one for Arrival / Departure date. Created errors are linked" +
            " to the Myce Quote and to the Rooming list.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-521: Rooming list - v2")
    public void case4() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        files.deleteAllFilesFolder("/home/user/project/thynk-selenium/TemporaryFiles/");
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='RoomingListV2AutoTest3'", ORG_USERNAME);
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
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='RoomingListV2AutoTest3'" +
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
        myceQuotes.
                updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'", ORG_USERNAME);
        Thread.sleep(15000);
        StringBuilder recordRooms = myceQuotes.soql(SFDX, "SELECT Id, thn__Number__c FROM thn__Space__c WHERE" +
                " thn__Space_Area__c='" + roomTypeID.get(0) + "'", ORG_USERNAME);
        List<String> roomID = JsonParser2.getFieldValueSoql(recordRooms.toString(), "Id");
        List<String> roomNumber = JsonParser2.getFieldValueSoql(recordRooms.toString(), "thn__Number__c");
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomId + "'", ORG_USERNAME);
        String reservationID = JsonParser2.getFieldValue(reservationRecord.toString(), "Id");
        String reservationMewsID = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'",
                "thn__Generate_Rooming_List__c=true", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("RoomingListV2AutoTest3");
        myceQuotes.goToFiles();
        files.clickDownload("Rooming List RoomingListV2AutoTest3");
        files.updateXLS("Rooming List RoomingListV2AutoTest3.xlsx", 1, 4,
                date.generateTodayDate2_plus(0, 5) + " 13:25:00");
        files.updateXLS("Rooming List RoomingListV2AutoTest3.xlsx", 1, 5,
                date.generateTodayDate2_plus(0, 4) + " 17:55:00");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("RoomingListV2AutoTest3");
        myceQuotes.uploadFile
                ("/home/user/project/thynk-selenium/TemporaryFiles/Rooming List RoomingListV2AutoTest3.xlsx");
        StringBuilder roomingListRecord = roomingList.
                getRoomungListSFDX(SFDX, "thn__Reservation__c='" + reservationID + "'", ORG_USERNAME);
        String roomingListGuest = JsonParser2.getFieldValue(roomingListRecord.toString(), "thn__Guest__c");
        String roomingListPhone = JsonParser2.getFieldValue(roomingListRecord.toString(), "thn__Phone__c");
        String roomingListArrivalDateTime = JsonParser2.
                getFieldValue(roomingListRecord.toString(), "thn__Arrival_Date_Time__c");
        String roomingListDepartureDateTime = JsonParser2.
                getFieldValue(roomingListRecord.toString(), "thn__Departure_Date_Time__c");
        StringBuilder roomingListErrorRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Message__c FROM" +
                " thn__Rooming_List_Error__c WHERE thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> roomingListErrorID = JsonParser2.getFieldValueSoql(roomingListErrorRecords.toString(), "Id");
        List<String> roomingListErrorMessage = JsonParser2.
                getFieldValueSoql(roomingListErrorRecords.toString(), "thn__Message__c");
        Assert.assertNull(roomingListGuest);
        Assert.assertEquals(roomingListPhone, null);
        Assert.assertEquals(roomingListArrivalDateTime,
                date.generateTodayDate2_plus(0, 2) + "T12:00:00.000+0000");
        Assert.assertEquals(roomingListDepartureDateTime,
                date.generateTodayDate2_plus(0, 4) + "T14:30:00.000+0000");
        Assert.assertEquals(roomingListErrorID.size(), 1);
        Assert.assertEquals(roomingListErrorMessage.get(0), "Invalid date/time: undefined");
    }

}
