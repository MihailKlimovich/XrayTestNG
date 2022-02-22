package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.MyceQuotes;
import utils.Listeners.TestListener;

import java.io.IOException;
import java.util.List;

@Listeners({TestListener.class})

public class CloneMyceQuoteAndCloneSelection extends BaseTest {

    @Test(priority = 1, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection")
    public void preconditions() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
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
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
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

    @Test(priority = 2, description = "Clone quote")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection")
    public void cloneQuote() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTestClone'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneMyceQuoteAutoTest");
        myceQuotes.cloneMyceQuote("CloneMyceQuoteAutoTestClone", date.generateTodayDate3_plus(0, 0), "5");
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTestClone'", ORG_USERNAME);
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
        StringBuilder clonedQuoteMeeringRoom1 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Name='DEFAULT - MEETING FULL DAY' thn__MYCE_Quote__c='"
                        + clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(clonedQuoteMeeringRoom1);
        String clonedQuoteMeetingRoomD1 = JsonParser2.getFieldValue(clonedQuoteMeeringRoom1.toString(), "Id");
        StringBuilder clonedQuoteMeeringRoom2 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Name='DEFAULT - MEETING HALF DAY' thn__MYCE_Quote__c='" +
                        clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(clonedQuoteMeeringRoom2);
        String clonedQuoteMeetingRoomD2 = JsonParser2.getFieldValue(clonedQuoteMeeringRoom2.toString(), "Id");
        Assert.assertEquals(clonedQuoteArrivalDay, date.generateTodayDate2_plus(0, 0));
        Assert.assertEquals(clonedQuoteDepartureDate, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(clonedQuotePax, "5");
        Assert.assertNotNull(clonedQuoteHotelRoomID1);
        Assert.assertNotNull(clonedQuoteHotelRoomID2);
        Assert.assertNotNull(clonedQuotePackageID);
        Assert.assertNotNull(clonedQuoteProductsID1);
        Assert.assertNotNull(clonedQuoteProductsID2);
        Assert.assertNotNull(clonedQuoteMeetingRoomD1);
        Assert.assertNotNull(clonedQuoteMeetingRoomD2);
    }

    @Test(priority = 3, description = "Clone selection of records: quote hotel room")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection")
    public void cloneQuoteHotelRooms() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneMyceQuoteAutoTest");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0 , 0), "Quote Hotel Room");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRooms = myceQuotes.soql(SFDX, "SELECT Id, thn__Space_Area__c," +
                " thn__Arrival_Date_Time__c, thn__Departure_Date_Time__c  FROM thn__Quote_Hotel_Room__c WHERE" +
                " Name='ROOM 2 NIGHTS' and thn__MYCE_Quote__c='" + myceQuoteID + "'", ORG_USERNAME);
        System.out.println(quoteHotelRooms);
        List<String> quoteHotelRoomsID = JsonParser2.getFieldValueSoql(quoteHotelRooms.toString(), "Id");
        List<String> quoteHotelRoomsRoomType = JsonParser2.
                getFieldValueSoql(quoteHotelRooms.toString(), "thn__Space_Area__c");
        List<String> quoteHotelRoomsArrivalDateTime = JsonParser2.
                getFieldValueSoql(quoteHotelRooms.toString(), "thn__Arrival_Date_Time__c");
        List<String> quoteHotelRoomsDepartureDateTime = JsonParser2.
                getFieldValueSoql(quoteHotelRooms.toString(), "thn__Departure_Date_Time__c");
        Assert.assertEquals(quoteHotelRoomsID.size(), 2);
        Assert.assertEquals(quoteHotelRoomsRoomType.get(0), quoteHotelRoomsRoomType.get(1));
        Assert.assertEquals(quoteHotelRoomsArrivalDateTime.get(0), quoteHotelRoomsArrivalDateTime.get(1));
        Assert.assertEquals(quoteHotelRoomsDepartureDateTime.get(0), quoteHotelRoomsDepartureDateTime.get(1));
    }

    @Test(priority = 4, description = "Clone selection of records: quote product")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection")
    public void cloneQuoteProduct() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneMyceQuoteAutoTest");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0 , 0), "Quote Product");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteProducts = myceQuotes.soql(SFDX, "SELECT Id, thn__Pax__c," +
                " thn__Start_Date_Time__c, thn__End_Date_Time__c  FROM thn__Quote_Product__c WHERE" +
                " Name='DINER' and thn__MYCE_Quote__c='" + myceQuoteID + "'", ORG_USERNAME);
        List<String> quoteProductsID = JsonParser2.getFieldValueSoql(quoteProducts.toString(), "Id");
        List<Integer> quoteProductsPax = JsonParser2.
                getFieldValueSoql2(quoteProducts.toString(), "thn__Pax__c");
        List<String> quoteProductsStartDateTime = JsonParser2.
                getFieldValueSoql(quoteProducts.toString(), "thn__Start_Date_Time__c");
        List<String> quoteProductsEndDateTime = JsonParser2.
                getFieldValueSoql(quoteProducts.toString(), "thn__End_Date_Time__c");
        Assert.assertEquals(quoteProductsID.size(), 2);
        Assert.assertEquals(quoteProductsPax.get(0), quoteProductsPax.get(1));
        Assert.assertEquals(quoteProductsStartDateTime.get(0), quoteProductsStartDateTime.get(1));
        Assert.assertEquals(quoteProductsEndDateTime.get(0), quoteProductsEndDateTime.get(1));
    }

    @Test(priority = 5, description = "Clone selection of records: quote meeting room")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection")
    public void cloneQuoteMeetingRoom() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneMyceQuoteAutoTest");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0 , 0), "Quote Meetings Room");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id, thn__Product__c," +
                " thn__Pax__c, thn__Start_Date_Time__c, thn__End_Date_Time__c  FROM thn__Quote_Meeting_Room__c WHERE" +
                " Name='DEFAULT - MEETING FULL DAY' and thn__MYCE_Quote__c='" + myceQuoteID + "'", ORG_USERNAME);
        List<String> quoteMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoom.toString(), "Id");
        List<String> quoteMeetingRoomsProduct = JsonParser2.getFieldValueSoql(quoteMeetingRoom.toString(), "thn__Product__c");
        List<Integer> quoteMeetingRoomsPax = JsonParser2.
                getFieldValueSoql2(quoteMeetingRoom.toString(), "thn__Pax__c");
        List<String> quoteMeetingRoomsStartDateTime = JsonParser2.
                getFieldValueSoql(quoteMeetingRoom.toString(), "thn__Start_Date_Time__c");
        List<String> quoteMeetingRoomsEndDateTime = JsonParser2.
                getFieldValueSoql(quoteMeetingRoom.toString(), "thn__End_Date_Time__c");
        Assert.assertEquals(quoteMeetingRoomsID.size(), 2);
        Assert.assertEquals(quoteMeetingRoomsPax.get(0), quoteMeetingRoomsPax.get(1));
        Assert.assertEquals(quoteMeetingRoomsProduct.get(0), quoteMeetingRoomsProduct.get(1));
        Assert.assertEquals(quoteMeetingRoomsStartDateTime.get(0), quoteMeetingRoomsStartDateTime.get(1));
        Assert.assertEquals(quoteMeetingRoomsEndDateTime.get(0), quoteMeetingRoomsEndDateTime.get(1));
    }

    @Test(priority = 6, description = "Clone selection of records: quote package")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection")
    public void cloneQuotePackage() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneMyceQuoteAutoTest");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0 , 0), "Quote Package");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneMyceQuoteAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackage = myceQuotes.soql(SFDX, "SELECT Id, thn__Package__c," +
                " thn__Pax__c, thn__Start_Date__c, thn__End_Date__c  FROM thn__Quote_Package__c WHERE" +
                " Name='CloneAutoTest' and thn__MYCE_Quote__c='" + myceQuoteID + "'", ORG_USERNAME);
        List<String> quotePackagesID = JsonParser2.getFieldValueSoql(quotePackage.toString(), "Id");
        List<String> quotePackagesPack = JsonParser2.getFieldValueSoql(quotePackage.toString(), "thn__Package__c");
        List<Integer> quotePackagesPax = JsonParser2.
                getFieldValueSoql2(quotePackage.toString(), "thn__Pax__c");
        List<String> quotePackageStartDate = JsonParser2.
                getFieldValueSoql(quotePackage.toString(), "thn__Start_Date__c");
        List<String> quoteMPackageEndDate = JsonParser2.
                getFieldValueSoql(quotePackage.toString(), "thn__End_Date__c");
        Assert.assertEquals(quotePackagesID.size(), 2);
        Assert.assertEquals(quotePackagesPax.get(0), quotePackagesPax.get(1));
        Assert.assertEquals(quotePackagesPack.get(0), quotePackagesPack.get(1));
        Assert.assertEquals(quotePackageStartDate.get(0), quotePackageStartDate.get(1));
        Assert.assertEquals(quoteMPackageEndDate.get(0), quoteMPackageEndDate.get(1));
    }
}
