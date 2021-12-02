package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;
import utils.Listeners.TestListener;

import java.io.IOException;

@Listeners({TestListener.class})

public class QuoteHotelRoomTimeFields extends BaseTest {

    @Test(priority = 1, description = "Quote hotel room’s arrival / departure datetimes are auto populated from the" +
            " Quote while creation. Create MYCE Quote → Add Quote hotel room, do not fill arrival / departure" +
            " datetimes. Result: Arrival date and departure date fields are filled with value from arrival and" +
            " departure datetime fields.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-512 Quote hotel room - time fields")
    public void quoteHotelRoom_timeFieldsTest1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
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
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='QHR time test 1' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypeID + "' thn__Unit_Price__c=10" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(myceQuoteRecord);
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomRecord);
        String arrivalDateMyceQuote = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Arrival_Date__c");
        String departureDateMyceQuote = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Departure_Date__c");
        String arrivalDateTimeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date_Time__c");
        String departureDateTimeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date_Time__c");

        Assert.assertTrue(arrivalDateTimeQuoteHotelRoom.contains(arrivalDateMyceQuote));
        Assert.assertTrue(departureDateTimeQuoteHotelRoom.contains(departureDateMyceQuote));
    }

    @Test(priority = 2, description = "Quote hotel room’s arrival / departure datetimes are manually filled while" +
            " creation. Add Quote hotel room → Fill arrival / departure datetimes. Result: Arrival date and departure" +
            " date fields are filled with value from arrival and departure datetime fields.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-512 Quote hotel room - time fields")
    public void quoteHotelRoom_timeFieldsTest2() throws InterruptedException, IOException {
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
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='QHR time test 2' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c="
                        + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypeID + "' thn__Unit_Price__c=10 thn__Arrival_Date_Time__c="
                        + date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(myceQuoteRecord);
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomRecord);
        String arrivalDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date__c");
        String departureDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date__c");
        String arrivalDateTimeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date_Time__c");
        String departureDateTimeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date_Time__c");
        String arrivalDateMyceQuote = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Arrival_Date__c");
        String departureDateMyceQuote = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Departure_Date__c");
        Assert.assertNotEquals(arrivalDateQuoteHotelRoom, arrivalDateMyceQuote);
        Assert.assertNotEquals(departureDateQuoteHotelRoom, departureDateMyceQuote);
        Assert.assertEquals(arrivalDateQuoteHotelRoom, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(departureDateQuoteHotelRoom, date.generateTodayDate2_plus(0, 3));
        Assert.assertTrue(arrivalDateTimeQuoteHotelRoom.contains(arrivalDateQuoteHotelRoom));
        Assert.assertTrue(departureDateTimeQuoteHotelRoom.contains(departureDateQuoteHotelRoom));
    }

    @Test(priority = 3, description = "Quote hotel room’s arrival / departure datetimes are manually updated. Open" +
            " Quote hotel room record → Change arrival / departure datetimes. Result: Arrival date and departure date" +
            " fields are updared to value from new arrival and departure datetime fields.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-512 Quote hotel room - time fields")
    public void quoteHotelRoom_timeFieldsTest3() throws InterruptedException, IOException {
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
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='QHR time test 3' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypeID + "' thn__Unit_Price__c=10 thn__Arrival_Date_Time__c="
                        + date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "id='" + quoteHotelRoomId + "'",
                "-v",
                "thn__Arrival_Date_Time__c=" + date.generateTodayDate2_plus(0, 1) + "" +
                        " thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 4),
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(result);
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(myceQuoteRecord);
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomRecord);
        String arrivalDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date__c");
        String departureDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date__c");
        String arrivalDateTimeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date_Time__c");
        String departureDateTimeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date_Time__c");
        String arrivalDateMyceQuote = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Arrival_Date__c");
        String departureDateMyceQuote = JsonParser2
                .getFieldValue(myceQuoteRecord.toString(), "thn__Departure_Date__c");
        Assert.assertNotEquals(arrivalDateQuoteHotelRoom, arrivalDateMyceQuote);
        Assert.assertNotEquals(departureDateQuoteHotelRoom, departureDateMyceQuote);
        Assert.assertEquals(arrivalDateQuoteHotelRoom, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(departureDateQuoteHotelRoom, date.generateTodayDate2_plus(0, 4));
        Assert.assertTrue(arrivalDateTimeQuoteHotelRoom.contains(arrivalDateQuoteHotelRoom));
        Assert.assertTrue(departureDateTimeQuoteHotelRoom.contains(departureDateQuoteHotelRoom));
    }

    @Test(priority = 4, description = "Quote hotel room is a part of the Quote package. Add Package to the quote." +
            " Result: Arrival date and departure date fields are filled with value from arrival and departure" +
            " datetime fields, which are taken from the package (The departure date in the Quote Hotel Room must be" +
            " one day longer than the End Date in the Quote Package).")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-512 Quote hotel room - time fields")
    public void quoteHotelRoom_timeFieldsTest4() throws InterruptedException, IOException {
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
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test QHR Date 1' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test QHR Date 1' thn__Package__c='" + packageId + "' thn__Type__c='Hotel Room'" +
                        " thn__Product__c='" + productID + "' thn__Start_Time__c=00:00 thn__End_Time__c=09:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(packageLineResult);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='QHR time test 4' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "' thn__Unit_Price__c=10 thn__Start_Date__c=" + date.generateTodayDate2_plus(0, 2) +
                        " thn__End_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomRecord);
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageRecord);
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(myceQuoteRecord);
        String arrivalDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date__c");
        String departureDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date__c");
        String arrivalDateTimeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date_Time__c");
        String departureDateTimeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date_Time__c");
        String arrivalDateMyceQuote = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Arrival_Date__c");
        String departureDateMyceQuote = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Departure_Date__c");
        String startDateQuotePackage = JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Start_Date__c");
        String endDateQuotePackage = JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__End_Date__c");
        Assert.assertNotEquals(arrivalDateQuoteHotelRoom, arrivalDateMyceQuote);
        Assert.assertNotEquals(departureDateQuoteHotelRoom, departureDateMyceQuote);
        Assert.assertEquals(arrivalDateQuoteHotelRoom, startDateQuotePackage);
        Assert.assertEquals(departureDateQuoteHotelRoom, date.generateTodayDate2_plus(0, 4));
        Assert.assertTrue(arrivalDateTimeQuoteHotelRoom.contains(arrivalDateQuoteHotelRoom));
        Assert.assertTrue(departureDateTimeQuoteHotelRoom.contains(departureDateQuoteHotelRoom));
    }

    @Test(priority = 5, description = "Start date and end date fields are updated on the Quote package. Open Quote" +
            " package record → Change start / end date. Result: Arrival / departure datetimes are updated to the new" +
            " value from the Quote package, Arrival date and departure date fields are updared to value from new" +
            " arrival and departure datetime fields. (The departure date in the Quote Hotel Room must be one day" +
            " longer than the End Date in the Quote Package).")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-512 Quote hotel room - time fields")
    public void quoteHotelRoom_timeFieldsTest5() throws InterruptedException, IOException {
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
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test QHR Date 2' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test QHR Date 2' thn__Package__c='" + packageId + "' thn__Type__c='Hotel Room'" +
                        " thn__Product__c='" + productID + "' thn__Start_Time__c=00:00 thn__End_Time__c=09:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(packageLineResult);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='QHR time test 5' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "' thn__Unit_Price__c=10 thn__Start_Date__c=" + date.generateTodayDate2_plus(0, 2) +
                        " thn__End_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "id='" + quotePackageId + "'",
                "-v",
                "thn__Start_Date__c=" + date.generateTodayDate2_plus(0, 1) + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 4),
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(result);
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomRecord);
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageRecord);
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(myceQuoteRecord);
        String arrivalDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date__c");
        String departureDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date__c");
        String arrivalDateTimeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date_Time__c");
        String departureDateTimeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date_Time__c");
        String startDateQuotePackage = JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Start_Date__c");
        String endDateQuotePackage = JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__End_Date__c");
        Assert.assertEquals(startDateQuotePackage, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(endDateQuotePackage, date.generateTodayDate2_plus(0, 4));
        Assert.assertEquals(arrivalDateQuoteHotelRoom, startDateQuotePackage);
        Assert.assertEquals(departureDateQuoteHotelRoom, date.generateTodayDate2_plus(0, 5));
        Assert.assertTrue(arrivalDateTimeQuoteHotelRoom.contains(arrivalDateQuoteHotelRoom));
        Assert.assertTrue(departureDateTimeQuoteHotelRoom.contains(departureDateQuoteHotelRoom));
    }

    /*@Test(priority = 6, description = "Quote hotel room’s arrival / departure datetimes are  updated with the Change" +
            " Date feature. Quote hotel room is a part of the Quote package. Open Quote record → Press Change Date" +
            " button → Change date → Finish the process. Result: Arrival date and departure date fields are updared" +
            " to value from new arrival and departure datetime fields.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-512 Quote hotel room - time fields")
    public void quoteHotelRoom_timeFieldsTest6() throws InterruptedException, IOException {
        packages.deletePackageSFDX(SFDX, "Name='TestQHRDateAuto3", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QHR time test 6'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='TestQHRDateAuto3'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='TestQHRDateAuto3' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QHR time test 6' thn__Pax__c=10" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5), ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Package__c='" + packageID + "'", ORG_USERNAME);
    }*/


}
