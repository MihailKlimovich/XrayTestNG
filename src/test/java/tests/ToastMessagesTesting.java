package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class ToastMessagesTesting extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Toast messages testing")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
    }

    @Test(priority = 2, description = "Create a MYCE Quote and add a Hotel room to it. Add a Discount to the Quote" +
            " Hotel Room.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Toast messages testing")
    public void preconditions() throws InterruptedException, IOException {
        resource.deleteResourceSFDX(SFDX, "Name='ToastMessagesTesting", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ToastMessagesTesting'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room2NightsRecord = product.getProductSFDX(SFDX, "Name='ROOM 2 NIGHTS'", ORG_USERNAME);
        String room2NightsID = JsonParser2.getFieldValue(room2NightsRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        System.out.println(guests);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ToastMessagesTesting' thn__Pax__c=3" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                room2NightsID + "' thn__Space_Area__c='" + roomTypesId.get(1) + "' thn__Discountable__c=true" +
                " thn__Discount_Percent__c=5", ORG_USERNAME);
        String resourceID = resource.createResourceSFDX(SFDX, "Name='ToastMessagesTesting'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
    }

    @Test(priority = 3, description = "Change the Room Type of the added room. Result: A toast message appears with" +
            " the message: ‘Discount % updated due to change of Room type/Rate plan.’The discount was set to 0 %.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Toast messages testing")
    public void case1() throws InterruptedException, IOException {
        String expectedMessage = "Discount % updated due to change of room type/rate plan.";
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("ToastMessagesTesting");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.openRecordByName("ROOM 2 NIGHTS");
        quoteHotelRoom.editRoomType("Queen");
        String message = quoteHotelRoom.readToastMessage();
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ToastMessagesTesting'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomDiscountPercente= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        Assert.assertEquals(message, expectedMessage);
        Assert.assertEquals(quoteHotelRoomDiscountPercente, "0");
    }

    @Test(priority = 4, description = "Set the discount of the Quote Hotel Room to 10%. Change the Rate plan of" +
            " the Quote Hotel Room. Result: A toast message appears with the message: ‘Discount % updated due to" +
            " change of Room type/Rate plan. The discount was set to 0 %.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Toast messages testing")
    public void case2() throws InterruptedException, IOException {
        String expectedMessage = "Discount % updated due to change of room type/rate plan.";
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ToastMessagesTesting'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Discount_Percent__c=10", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("ToastMessagesTesting");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.openRecordByName("ROOM 2 NIGHTS");
        quoteHotelRoom.editRatePlan("TestRateAuto3");
        String message = quoteHotelRoom.readToastMessage();
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomDiscountPercente= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        Assert.assertEquals(message, expectedMessage);
        Assert.assertEquals(quoteHotelRoomDiscountPercente, "0");
    }

    @Test(priority = 5, description = "Add a Meeting Room to the MYCE Quote with resount that is not Default. Add" +
            " another Meeting room to the Quote so that it’s dates overlap the first one. Result: A toast message " +
            "appears saying: ‘Quote Meeting Room is overbooked.’ The ‘Overbooking Message' field of the Quote" +
            " Meeting room is filled with 'Quote Meeting Room is overbooked.’")
    @Severity(SeverityLevel.NORMAL)
    @Story("Toast messages testing")
    public void case3() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        String expectedMessage = "Quote Meeting Room is overbooked.";
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ToastMessagesTesting'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("ToastMessagesTesting");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.createQuoteMeetingRoom("ToastMessagesTesting", "MEETING FULL DAY",
                date.generateTodayDate(), date.generateTodayDate(), "18:00", "19:00");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("ToastMessagesTesting");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.createQuoteMeetingRoom("ToastMessagesTesting", "MEETING FULL DAY",
                date.generateTodayDate(), date.generateTodayDate(), "18:00", "19:00");
        Thread.sleep(2000);
        String message = quoteMeetingRoom.readToastMessage();
        Assert.assertEquals(message, expectedMessage);
    }
}

