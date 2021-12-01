package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;
import java.util.List;

public class HotelRoomModifications extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Hotel room modification")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("Hotel room modification")
    public void preconditions() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestHotelRoomModification'",
                "-u",
                ORG_USERNAME,
                "--json"});
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
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='QuoteTestHotelRoomModification' thn__Pax__c=1 thn__Hotel__c='" + propertyID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
    }

    @Test(priority = 3, description = "Add Quote hotel room, while adding select Type of Occupancy")
    @Severity(SeverityLevel.NORMAL)
    @Story("Hotel room modification")
    public void hotelRoomModificationTest1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestHotelRoomModification'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder productRecordRoom1Night = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String room1NightID = JsonParser2.getFieldValue(productRecordRoom1Night.toString(), "Id");
        StringBuilder roomTypeSingleRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Single'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder OccupancyTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Occupancy_Type__mdt",
                "-w",
                "thn__Name__c='Twin'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String numberOccupancyType = JsonParser2.getFieldValue(OccupancyTypeRecord.toString(), "thn__Number__c");
        StringBuilder quoteHotelRoom1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='"
                        + room1NightID + "' thn__Space_Area__c='" + roomTypeSingleID + "' thn__Type_of_Occupancy__c='Twin'" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId1 = JsonParser2.getFieldValue(quoteHotelRoom1.toString(), "id");
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quoteHotelRoomId1 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String numberOfAdults = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Number_of_Adults__c");
        Assert.assertEquals(numberOfAdults, numberOccupancyType);
    }

    @Test(priority = 4, description = "Update Type of Occupancy on the room to the for which there is custom metadata type record")
    @Severity(SeverityLevel.NORMAL)
    @Story("Hotel room modification")
    public void hotelRoomModificationTest2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestHotelRoomModification'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder OccupancyTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Occupancy_Type__mdt",
                "-w",
                "thn__Name__c='Quadruple'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String numberOccupancyType = JsonParser2.getFieldValue(OccupancyTypeRecord.toString(), "thn__Number__c");
        StringBuilder quoteHotelRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesHotelRoomsID = JsonParser2.getFieldValueSoql(quoteHotelRoomsSoql.toString(), "Id");
        StringBuilder quoteHotelRoomUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-v",
                "thn__Type_of_Occupancy__c='Quadruple'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String numberOfAdults = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Number_of_Adults__c");
        Assert.assertEquals(numberOfAdults, numberOccupancyType);
    }

    @Test(priority = 5, description = "Update Type of Occupancy on the room to the one for which there is no custom metadata type record")
    @Severity(SeverityLevel.NORMAL)
    @Story("Hotel room modification")
    public void hotelRoomModificationTest3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestHotelRoomModification'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder OccupancyTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Occupancy_Type__mdt",
                "-w",
                "thn__Name__c='Single'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(OccupancyTypeRecord);
        String occupancyTypeMessage = JsonParser2.getFieldValue2(OccupancyTypeRecord.toString(), "message");
        StringBuilder quoteHotelRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesHotelRoomsID = JsonParser2.getFieldValueSoql(quoteHotelRoomsSoql.toString(), "Id");
        StringBuilder quoteHotelRoomUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-v",
                "thn__Type_of_Occupancy__c='Single'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String numberOfAdults = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Number_of_Adults__c");
        Assert.assertEquals(numberOfAdults,"1");
        Assert.assertEquals(occupancyTypeMessage, "No matching record found");
    }

    @Test(priority = 6, description = "Update Type of Occupancy to -None-")
    @Severity(SeverityLevel.NORMAL)
    @Story("Hotel room modification")
    public void hotelRoomModificationTest4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestHotelRoomModification'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesHotelRoomsID = JsonParser2.getFieldValueSoql(quoteHotelRoomsSoql.toString(), "Id");
        StringBuilder quoteHotelRoomUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-v",
                "thn__Type_of_Occupancy__c=''",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomUpdateResult);

        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomRecord);
        String numberOfAdults = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Number_of_Adults__c");
        Assert.assertEquals(numberOfAdults,"1");
    }

    @Test(priority = 7, description = "Update Number of Adults having Type of Occupancy specified")
    @Severity(SeverityLevel.NORMAL)
    @Story("Hotel room modification")
    public void hotelRoomModificationTest5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestHotelRoomModification'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesHotelRoomsID = JsonParser2.getFieldValueSoql(quoteHotelRoomsSoql.toString(), "Id");
        StringBuilder quoteHotelRoomUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-v",
                "thn__Type_of_Occupancy__c='Quadruple'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteHotelRoomUpdateResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-v",
                "thn__Number_of_Adults__c=15",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomUpdateResult2);

        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomRecord);
        String numberOfAdults = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Number_of_Adults__c");
        String typeOfOccupancy = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Type_of_Occupancy__c");
        Assert.assertEquals(typeOfOccupancy, "Quadruple");
        Assert.assertEquals(numberOfAdults,"15");
    }

    @Test(priority = 8, description = "Update room, any field but Number of Adults and Type of Occupancy")
    @Severity(SeverityLevel.NORMAL)
    @Story("Hotel room modification")
    public void hotelRoomModificationTest6() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestHotelRoomModification'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesHotelRoomsID = JsonParser2.getFieldValueSoql(quoteHotelRoomsSoql.toString(), "Id");
        StringBuilder quoteHotelRoomUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-v",
                "thn__Discount__c='10'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Id='" + quotesHotelRoomsID.get(0) + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomRecord);
        String numberOfAdults = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Number_of_Adults__c");
        String typeOfOccupancy = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Type_of_Occupancy__c");
        String discount = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount__c");
        Assert.assertEquals(discount, "10");
        Assert.assertEquals(typeOfOccupancy, "Quadruple");
        Assert.assertEquals(numberOfAdults,"15");
    }




}
