package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;
import utils.Listeners.TestListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Listeners({TestListener.class})

public class MultiDeleteTesting extends BaseTest {


    @BeforeClass
    public void classLevelSetup() {
        ChromeOptions options= new ChromeOptions();
        options.addArguments("--disable-cache");
        options.addArguments("--disk-cache-size=1");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-data-dir=/tmp/temp_profile");
        options.addArguments(" --whitelisted-ips=\"\"");
        options.addArguments("--headless", "window-size=1920,1024", "--no-sandbox");
        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @AfterClass
    public void teardown() {
        driver.close();
        driver.quit();
        if (driver != null) {
            driver = null;
        }
    }


    @Test(priority = 1, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void preconditions() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        packages.deletePackageSFDX(SFDX, "Name='PackageTestMultiDeleteAuto1'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteTestMultiDeleteAuto1", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteTestMultiDeleteAuto2", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteTestMultiDeleteAuto3", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder room2NightsRecord = product.getProductSFDX(SFDX, "Name='ROOM 2 NIGHTS'", ORG_USERNAME);
        String room2NightsID = JsonParser2.getFieldValue(room2NightsRecord.toString(), "Id");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String winesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Single'", ORG_USERNAME);
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        String packageId = packages.createPackageSFDX(SFDX, "Name='PackageTestMultiDeleteAuto1' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='TestMultiDeletePL1' thn__Package__c='" + packageId +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='TestMultiDeletePL2' thn__Package__c='" + packageId +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingFullDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='TestMultiDeletePL3' thn__Package__c='" + packageId +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=50 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='TestMultiDeletePL4' thn__Package__c='" + packageId +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room2NightsID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=50 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='TestMultiDeletePL5' thn__Package__c='" + packageId +
                "' thn__Type__c='Beverage' thn__Product__c='" + winesID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='TestMultiDeletePL6' thn__Package__c='" + packageId +
                "' thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String myceQuoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteTestMultiDeleteAuto1' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + "" +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID +
                "' thn__Package__c='" + packageId + "'", ORG_USERNAME);
        String myceQuoteID2 = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteTestMultiDeleteAuto2' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + "" +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID2 + "'" +
                " thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeSingleID + "'", ORG_USERNAME);
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID2 + "'" +
                " thn__Product__c='" + room2NightsID + "' thn__Space_Area__c='" + roomTypeSingleID + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID2 +
                "' thn__Product__c='" + meetingHalfDayID + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID2 +
                "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID2 + "'" +
                " thn__Product__c='" + winesID + "'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID2 + "'" +
                " thn__Product__c='" + beverageID + "'", ORG_USERNAME);
        String myceQuoteID3 = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteTestMultiDeleteAuto3' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + "" +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID3 + "'" +
                " thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeSingleID + "'" +
                " thn__Reserved__c=true", ORG_USERNAME);
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID3 + "'" +
                " thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeSingleID + "'", ORG_USERNAME);
    }

    @Test(priority = 2, description = "Delete quote products, meeting rooms, hotel rooms which are part of the package")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void multiDeleteTest1() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto1");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.selectItems("2");
        quoteHotelRoom.clickMultiDeleteButton();
        multiDelete.multiDeleteRecords();

        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto1");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItems("2");
        quoteMeetingRoom.clickMultiDeleteButton();
        multiDelete.multiDeleteRecords();

        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto1");
        myceQuotes.openProducts();
        quoteProducts.selectAllItems("2");
        quoteProducts.clickMultiDeleteButton();
        multiDelete.multiDeleteRecords();
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestMultiDeleteAuto1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteProductsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesProductsID = JsonParser2.getFieldValueSoql(quoteProductsSoql.toString(), "Id");
        StringBuilder quoteHotelRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesHotelRoomsID = JsonParser2.getFieldValueSoql(quoteHotelRoomsSoql.toString(), "Id");
        StringBuilder quoteMeetingRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoomsSoql.toString(), "Id");
        Assert.assertEquals(quotesProductsID.size(), 2);
        Assert.assertEquals(quotesHotelRoomsID.size(), 2);
        Assert.assertEquals(quotesMeetingRoomsID.size(), 2);
    }

    @Test(priority = 3, description = "Delete quote package")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void multiDeleteTest2() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, urlForScratch, ALIAS, passwordForScratch);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto1");
        myceQuotes.openMeetingPackages();
        quoteMeetingPackages.selectItemNumber("1");
        quoteMeetingPackages.clickMultiDeleteButton();
        multiDelete.multiDeleteRecords();
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestMultiDeleteAuto1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteProductsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesProductsID = JsonParser2.getFieldValueSoql(quoteProductsSoql.toString(), "Id");
        StringBuilder quoteHotelRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesHotelRoomsID = JsonParser2.getFieldValueSoql(quoteHotelRoomsSoql.toString(), "Id");
        StringBuilder quoteMeetingRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoomsSoql.toString(), "Id");
        Assert.assertEquals(quotesProductsID.size(), 0);
        Assert.assertEquals(quotesHotelRoomsID.size(), 0);
        Assert.assertEquals(quotesMeetingRoomsID.size(), 0);
    }

    @Test(priority = 4, description = "Delete quote products, meeting rooms, hotel rooms which are not part of the package")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void multiDeleteTest3() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, urlForScratch, ALIAS, passwordForScratch);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto2");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.selectItems("2");
        quoteHotelRoom.clickMultiDeleteButton();
        multiDelete.multiDeleteRecords();
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto2");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItems("2");
        quoteMeetingRoom.clickMultiDeleteButton();
        multiDelete.multiDeleteRecords();
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto2");
        myceQuotes.openProducts();
        quoteProducts.selectAllItems("2");
        quoteProducts.clickMultiDeleteButton();
        multiDelete.multiDeleteRecords();
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestMultiDeleteAuto2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteProductsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesProductsID = JsonParser2.getFieldValueSoql(quoteProductsSoql.toString(), "Id");
        StringBuilder quoteHotelRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesHotelRoomsID = JsonParser2.getFieldValueSoql(quoteHotelRoomsSoql.toString(), "Id");
        StringBuilder quoteMeetingRoomsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> quotesMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoomsSoql.toString(), "Id");
        Assert.assertEquals(quotesProductsID.size(), 0);
        Assert.assertEquals(quotesHotelRoomsID.size(), 0);
        Assert.assertEquals(quotesMeetingRoomsID.size(), 0);
    }

    @Test(priority = 5, description = "Delete quote hotel rooms Quote_Hotel_Room__c records If in one of them" +
            " reserved__c == true and Mews_State__c != Cancelled")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void multiDeleteTest4() throws InterruptedException, IOException {
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto3");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.selectItems("2");
        quoteHotelRoom.clickMultiDeleteButton();
        multiDelete.multiDeleteRecords();
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestMultiDeleteAuto3'",
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
        Assert.assertEquals(quotesHotelRoomsID.size(), 2);
    }

}
