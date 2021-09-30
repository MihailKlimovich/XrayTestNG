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

public class ReservationCreationProcess extends BaseTest {

    @Test(priority = 1, description = "THY-487 Reservation creation process")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-487 Reservation creation process")
    @Story("Case 1")
    public void reservationCreationProcess1() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                ALIAS,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ALIAS,
                "--json"});
        String productID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='TestGuest' thn__Email__c='werty@tut.by' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ALIAS,
                "--json"});
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test reservation process' thn__Pax__c=1 thn__Reservation_Guest__c='" + guestID + "' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ALIAS,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='a1t1j000000xJ89'",
                "-u",
                ALIAS,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypeID + "' thn__Unit_Price__c=10" ,
                "-u",
                ALIAS,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "id='" + myceQuoteID + "'",
                "-v",
                "thn__SendToMews__c=true",
                "-u",
                ALIAS,
                "--json"});
        StringBuilder reservationRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Reservation__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ALIAS,
                "--json"});
        String reservationId = JsonParser2.getFieldValue(reservationRecord.toString(), "Id");
        System.out.println(reservationId);
        StringBuilder reservationPricesRecords = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Reservation_Price__c",
                "-w",
                "thn__Reservation__c='" + reservationId + "'",
                "-u",
                ALIAS,
                "--json"});
        String message = JsonParser2.getFieldValue2(reservationPricesRecords.toString(), "message");
        Assert.assertTrue(message.contains("3 records were retrieved"));
    }

    @Test(priority = 2, description = "THY-487 Reservation creation process")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-487 Reservation creation process")
    @Story("Case 2")
    public void reservationCreationProcess2() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                ALIAS,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ALIAS,
                "--json"});
        String productID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='TestGuest2' thn__Email__c='werty@tut.by' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ALIAS,
                "--json"});
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test reservation process 2' thn__Pax__c=1 thn__Reservation_Guest__c='" + guestID + "' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ALIAS,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='a1t1j000000xJ89'",
                "-u",
                ALIAS,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypeID + "' thn__Unit_Price__c=10" ,
                "-u",
                ALIAS,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "id='" + myceQuoteID + "'",
                "-v",
                "thn__SendToMews__c=true",
                "-u",
                ALIAS,
                "--json"});
        StringBuilder reservationRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Reservation__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ALIAS,
                "--json"});
        String reservationId = JsonParser2.getFieldValue(reservationRecord.toString(), "Id");
        System.out.println(reservationId);
        StringBuilder reservationPricesRecords = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Reservation_Price__c",
                "-w",
                "thn__Reservation__c='" + reservationId + "'",
                "-u",
                ALIAS,
                "--json"});
        String message = JsonParser2.getFieldValue2(reservationPricesRecords.toString(), "message");
        System.out.println(reservationPricesRecords);
        Assert.assertTrue(message.contains("5 records were retrieved"));
        StringBuilder update =SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "id='" + quoteHotelRoomId + "'",
                "-v",
                "thn__Arrival_Date_Time__c=" + date.generateTodayDate2() + " thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 2),
                "-u",
                ALIAS,
                "--json"});
        System.out.println(update);
        StringBuilder reservationPricesRecords2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Reservation_Price__c",
                "-w",
                "thn__Reservation__c='" + reservationId + "'",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(reservationPricesRecords2);
        String message2 = JsonParser2.getFieldValue2(reservationPricesRecords2.toString(), "message");
        Assert.assertTrue(message2.contains("2 records were retrieved"));
        StringBuilder update2 =SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "id='" + quoteHotelRoomId + "'",
                "-v",
                "thn__Arrival_Date_Time__c=" + date.generateTodayDate2() + " thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ALIAS,
                "--json"});
        StringBuilder reservationPricesRecords3 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Reservation_Price__c",
                "-w",
                "thn__Reservation__c='" + reservationId + "'",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(reservationPricesRecords3);
        String message3 = JsonParser2.getFieldValue2(reservationPricesRecords3.toString(), "message");
        Assert.assertTrue(message3.contains("3 records were retrieved"));
        StringBuilder update3 =SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "id='" + quoteHotelRoomId + "'",
                "-v",
                "thn__Arrival_Date_Time__c=" + date.generateTodayDate2() + " thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 10),
                "-u",
                ALIAS,
                "--json"});
        System.out.println(update);
        StringBuilder reservationPricesRecords4 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Reservation_Price__c",
                "-w",
                "thn__Reservation__c='" + reservationId + "'",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(reservationPricesRecords4);
        String message4= JsonParser2.getFieldValue2(reservationPricesRecords4.toString(), "message");
        Assert.assertTrue(message4.contains("3 records were retrieved"));
    }

}
