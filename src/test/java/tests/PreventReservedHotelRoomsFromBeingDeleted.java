package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import utils.Listeners.TestListener;

import java.io.IOException;
import java.util.List;

@Listeners({TestListener.class})

public class PreventReservedHotelRoomsFromBeingDeleted extends BaseTest {


    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Prevent reserved hotel rooms from being deleted")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Preconditions: create a new myce quote,a hotel room and quote package that" +
            " contains a hotel room. Send the reservation to Mews")
    @Severity(SeverityLevel.NORMAL)
    @Story("Prevent reserved hotel rooms from being deleted")
    public void preconditions() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='DeleteReservedHotelRoomAutoTest'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='PackWithHotelRoomAuto", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder room2NightsRecord = product.getProductSFDX(SFDX, "Name='ROOM 2 NIGHTS'", ORG_USERNAME);
        String room2NightsID = JsonParser2.getFieldValue(room2NightsRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Single'", ORG_USERNAME);
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        System.out.println(guests);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='DeleteReservedHotelRoomAutoTest' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='1 - Qualify'" +
                " thn__SendToMews__c=false", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='PackWithHotelRoomAuto' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Hotel Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1 thn__Space_Area__c='"
                + roomTypesId.get(0) + "'", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                room2NightsID + "' thn__Space_Area__c='" + roomTypesId.get(1) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'" +
                " thn__SendToMews__c=true", ORG_USERNAME);
    }

    @Test(priority = 3, description = "Delete the quote hotel room. Expected result: you get an error message" +
            " preventing you from deleting the record")
    @Severity(SeverityLevel.NORMAL)
    @Story("Prevent reserved hotel rooms from being deleted")
    public void case1() throws InterruptedException, IOException {
        String expectedMessage = "Quote Hotel Room is reserved and cannot be deleted until it had been cancelled";
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='DeleteReservedHotelRoomAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder deleteResult = quoteHotelRoom.deleteQuoteHotelRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'  Name='ROOM 2 NIGHTS'",ORG_USERNAME);
        String message = JsonParser2.getFieldValue2(deleteResult.toString(), "name");
        Assert.assertEquals(message,expectedMessage);
    }

    @Test(priority = 4, description = "Delete the quote package. Expected result: you get an error message preventing" +
            " you from deleting the record")
    @Severity(SeverityLevel.NORMAL)
    @Story("Prevent reserved hotel rooms from being deleted")
    public void case2() throws InterruptedException, IOException {
        String expectedMessage = "Quote Hotel Room is reserved and cannot be deleted until it had been cancelled";
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='DeleteReservedHotelRoomAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder deleteResult = quoteMeetingPackages.deleteQuotePackageSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'  Name='PackWithHotelRoomAuto'",ORG_USERNAME);
        String message = JsonParser2.getFieldValue2(deleteResult.toString(), "name");
        Assert.assertEquals(message,expectedMessage);
    }

    @Test(priority = 5, description = "Change the Mews State in hotel room to ‘Canceled’ and delete the record")
    @Severity(SeverityLevel.NORMAL)
    @Story("Prevent reserved hotel rooms from being deleted")
    public void case3() throws InterruptedException, IOException {
        String expectedMessage = "No matching record found";
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='DeleteReservedHotelRoomAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                "  Name='ROOM 1 NIGHT", "thn__Reason_Canceled__c='Canceled' thn__Mews_State__c='Canceled'",
                ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                "  Name='ROOM 2 NIGHTS", "thn__Reason_Canceled__c='Canceled' thn__Mews_State__c='Canceled'",
                ORG_USERNAME);
        quoteMeetingPackages.deleteQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                "  Name='PackWithHotelRoomAuto'",ORG_USERNAME);
        quoteHotelRoom.deleteQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                "  Name='ROOM 2 NIGHTS'",ORG_USERNAME);
        StringBuilder quoteHotelRoom1 = quoteHotelRoom.getQuoteHotelRoomSFDX
                (SFDX, "thn__MYCE_Quote__c='" + quoteID + "' Name='ROOM 1 NIGHT", ORG_USERNAME);
        String message1 = JsonParser2.getFieldValue2(quoteHotelRoom1.toString(), "message");
        StringBuilder quoteHotelRoom2 = quoteHotelRoom.getQuoteHotelRoomSFDX
                (SFDX, "thn__MYCE_Quote__c='" + quoteID + "' Name='ROOM 2 NIGHT", ORG_USERNAME);
        String message2 = JsonParser2.getFieldValue2(quoteHotelRoom2.toString(), "message");
        Assert.assertEquals(message1, expectedMessage);
        Assert.assertEquals(message2, expectedMessage);
    }
}
