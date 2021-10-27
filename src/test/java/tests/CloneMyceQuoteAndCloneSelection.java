package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.MyceQuotes;

import java.io.IOException;

public class CloneMyceQuoteAndCloneSelection extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection")
    public void logIn() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);

    }

    @Test(priority = 2, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection")
    public void preconditions() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTest'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='CloneAutoTest", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder room2NightsRecord = product.getProductSFDX(SFDX, "Name='ROOM 2 NIGHTS'", ORG_USERNAME);
        String room2NightsID = JsonParser2.getFieldValue(room2NightsRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Single'", ORG_USERNAME);
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " thn__Closed_Status__c='Won'", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='CloneAutoTest' thn__Hotel__c='" + propertyID +
                "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Hotel Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                room2NightsID + "' thn__Space_Area__c='" + roomTypeSingleID + "'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                productDinerID + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
    }

    @Test(priority = 3, description = "Clone quote")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection")
    public void cloneQuote() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTestClone'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneMyceQuoteAutoTest");
        myceQuotes.cloneMyceQuote("CloneMyceQuoteAutoTestClone", date.generateTodayDate_plus(0, 1));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTestClone'", ORG_USERNAME);
        System.out.println(clonedQuoteRecord);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        String clonedQuoteArrivalDay = JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "thn__Arrival_Date__c");
        String clonedQuoteDepartureDate= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "thn__Departure_Date__c");
        String clonedQuotePax= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "thn__Pax__c");
        StringBuilder clonedQuoteHotelRoom1 = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Name='ROOM 1 NIGHT'" +
                " thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(clonedQuoteHotelRoom1);
        String clonedQuoteHotelRoomID1 = JsonParser2.getFieldValue(clonedQuoteHotelRoom1.toString(), "Id");
        StringBuilder clonedQuoteHotelRoom2 = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Name='ROOM 2 NIGHTS'" +
                " thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(clonedQuoteHotelRoom2);
        String clonedQuoteHotelRoomID2 = JsonParser2.getFieldValue(clonedQuoteHotelRoom2.toString(), "Id");
        StringBuilder clonedQuotePackage = quoteMeetingPackages.getQuotePackageSFDX(SFDX, "Name='CloneAutoTest'" +
                " thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(clonedQuotePackage);
        String clonedQuotePackageID = JsonParser2.getFieldValue(clonedQuotePackage.toString(), "Id");
        StringBuilder clonedQuoteProducts1 = quoteProducts.getQuoteProductSFDX(SFDX, "Name='BEVERAGE'" +
                " thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(clonedQuoteProducts1);
        String clonedQuoteProductsID1 = JsonParser2.getFieldValue(clonedQuoteProducts1.toString(), "Id");
        StringBuilder clonedQuoteProducts2 = quoteProducts.getQuoteProductSFDX(SFDX, "Name='DINER'" +
                " thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(clonedQuoteProducts2);
        String clonedQuoteProductsID2 = JsonParser2.getFieldValue(clonedQuoteProducts2.toString(), "Id");
        StringBuilder clonedQuoteMeeringRoom1 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Name='DEFAULT - MEETING FULL DAY'" +
                " thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(clonedQuoteMeeringRoom1);
        String clonedQuoteMeetingRoomD1 = JsonParser2.getFieldValue(clonedQuoteMeeringRoom1.toString(), "Id");
        StringBuilder clonedQuoteMeeringRoom2 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Name='DEFAULT - MEETING HALF DAY'" +
                " thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(clonedQuoteMeeringRoom2);
        String clonedQuoteMeetingRoomD2 = JsonParser2.getFieldValue(clonedQuoteMeeringRoom2.toString(), "Id");
        Assert.assertEquals(clonedQuoteArrivalDay, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(clonedQuoteDepartureDate, date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(clonedQuotePax, "5");
        Assert.assertNotNull(clonedQuoteHotelRoomID1);
        Assert.assertNotNull(clonedQuoteHotelRoomID2);
        Assert.assertNotNull(clonedQuotePackageID);
        Assert.assertNotNull(clonedQuoteProductsID1);
        Assert.assertNotNull(clonedQuoteProductsID2);
        Assert.assertNotNull(clonedQuoteMeetingRoomD1);
        Assert.assertNotNull(clonedQuoteMeetingRoomD2);

    }

}
