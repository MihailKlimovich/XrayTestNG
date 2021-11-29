package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;
import java.util.List;

public class ReservationCreationProcess extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote, link Reservation guest to the Quote. Add Quote hotel rooms." +
            " On MYCE Quote set ‘Send To Mews’ and ‘Generate Rooming List’ checkboxes to true. Result: Reservation" +
            " and Reservation price records linked to Reservations are created")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-487 Reservation creation process\"")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TestReservationProcessAuto1'", ORG_USERNAME);
        guests.deleteGuestSFDX(SFDX, "thn__LastName__c='TestGuestAuto1'" , ORG_USERNAME);
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__LastName__c='TestGuestAuto1' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        guests.updateGuestSFDX(SFDX, "Id='" + guestID + "'", "thn__Send_to_Mews__c=true", ORG_USERNAME);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestReservationProcessAuto1' thn__Pax__c=1 thn__Reservation_Guest__c='" + guestID + "'" +
                        " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        System.out.println(guests);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypesId.get(0) + "' thn__Unit_Price__c=10" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "id='" + myceQuoteID + "'",
                "-v",
                "thn__SendToMews__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder reservationRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Reservation__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String reservationId = JsonParser2.getFieldValue(reservationRecord.toString(), "Id");
        System.out.println(reservationId);
        StringBuilder reservationPricesRecords = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Reservation_Price__c",
                "-w",
                "thn__Reservation__c='" + reservationId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(reservationPricesRecords.toString(), "message");
        Assert.assertTrue(message.contains("3 records were retrieved"));
    }

    @Test(priority = 2, description = "Change dates on n Quote Hotel Room record. Result: Reservation price records" +
            " linked to Reservations are updated: in case hotel room period is educed, records outside the period are" +
            " deleted; if hotel room period is increased and is within the Quote period, new Reservation price" +
            " records ate created; if new dates are outside Quote period nothing happens")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-487 Reservation creation process")
    public void case2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TestReservationProcessAuto2'", ORG_USERNAME);
        guests.deleteGuestSFDX(SFDX, "thn__LastName__c='TestGuestAuto2'" , ORG_USERNAME);
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__LastName__c='TestGuestAuto2' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        guests.updateGuestSFDX(SFDX, "Id='" + guestID + "'", "thn__Send_to_Mews__c=true", ORG_USERNAME);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestReservationProcessAuto2' thn__Pax__c=1 thn__Reservation_Guest__c='" + guestID + "'" +
                        " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        System.out.println(guests);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypesId.get(0) + "' thn__Unit_Price__c=10" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "id='" + myceQuoteID + "'",
                "-v",
                "thn__SendToMews__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder reservationRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Reservation__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String reservationId = JsonParser2.getFieldValue(reservationRecord.toString(), "Id");
        System.out.println(reservationId);
        StringBuilder reservationPricesRecords = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Reservation_Price__c",
                "-w",
                "thn__Reservation__c='" + reservationId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(reservationPricesRecords.toString(), "message");
        System.out.println(reservationPricesRecords);
        Assert.assertTrue(message.contains("5 records were retrieved"));
        StringBuilder update =SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "id='" + quoteHotelRoomId + "'",
                "-v",
                "thn__Arrival_Date_Time__c=" + date.generateTodayDate2() + " thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 2),
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(update);
        StringBuilder reservationPricesRecords2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Reservation_Price__c",
                "-w",
                "thn__Reservation__c='" + reservationId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(reservationPricesRecords2);
        String message2 = JsonParser2.getFieldValue2(reservationPricesRecords2.toString(), "message");
        Assert.assertTrue(message2.contains("2 records were retrieved"));
        StringBuilder update2 =SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "id='" + quoteHotelRoomId + "'",
                "-v",
                "thn__Arrival_Date_Time__c=" + date.generateTodayDate2() + " thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 2),
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder reservationPricesRecords3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Reservation_Price__c",
                "-w",
                "thn__Reservation__c='" + reservationId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(reservationPricesRecords3);
        String message3 = JsonParser2.getFieldValue2(reservationPricesRecords3.toString(), "message");
        Assert.assertTrue(message3.contains("2 records were retrieved"));
        StringBuilder update3 =SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "id='" + quoteHotelRoomId + "'",
                "-v",
                "thn__Arrival_Date_Time__c=" + date.generateTodayDate2() + " thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 10),
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(update);
        StringBuilder reservationPricesRecords4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Reservation_Price__c",
                "-w",
                "thn__Reservation__c='" + reservationId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(reservationPricesRecords4);
        String message4= JsonParser2.getFieldValue2(reservationPricesRecords4.toString(), "message");
        Assert.assertTrue(message4.contains("2 records were retrieved"));
    }

}
