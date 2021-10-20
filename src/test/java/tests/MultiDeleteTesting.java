package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void logIn() throws InterruptedException, IOException {
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:auth:jwt:grant",
                "--clientid",
                CONSUMER_KEY,
                "--jwtkeyfile",
                SERVER_KEY_PATH,
                "--username",
                ORG_USERNAME,
                "--instanceurl",
                ORG_URL
        });
        System.out.println(authorise);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
    }

    @Test(priority = 2, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void preconditions() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='PackageTestMultiDeleteAuto1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestMultiDeleteAuto1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestMultiDeleteAuto2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='QuoteTestMultiDeleteAuto3'",
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
        StringBuilder productRecordMHD = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecordMHD.toString(), "Id");
        StringBuilder productRecordMFD = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING FULL DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingFullDayID = JsonParser2.getFieldValue(productRecordMFD.toString(), "Id");
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
        StringBuilder productRecordRoom2Nights = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 2 NIGHTS'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String room2NightsID = JsonParser2.getFieldValue(productRecordRoom2Nights.toString(), "Id");
        StringBuilder winesRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String winesID = JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        StringBuilder beverageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='BEVERAGE'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
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
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='PackageTestMultiDeleteAuto1' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestMultiDeletePL1' thn__Package__c='" + packageId + "' thn__Type__c='Meeting Room'" +
                        " thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(packageLineResult1);
        String packageLineId1 = JsonParser2.getFieldValue(packageLineResult1.toString(), "id");
        StringBuilder packageLineResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestMultiDeletePL2' thn__Package__c='" + packageId + "' thn__Type__c='Meeting Room'" +
                        " thn__Product__c='" + meetingFullDayID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId2 = JsonParser2.getFieldValue(packageLineResult2.toString(), "id");
        StringBuilder packageLineResult3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestMultiDeletePL3' thn__Package__c='" + packageId + "' thn__Type__c='Hotel Room'" +
                        " thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=50 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId3 = JsonParser2.getFieldValue(packageLineResult3.toString(), "id");
        StringBuilder packageLineResult4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestMultiDeletePL4' thn__Package__c='" + packageId + "' thn__Type__c='Hotel Room'" +
                        " thn__Product__c='" + room2NightsID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=50 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId4 = JsonParser2.getFieldValue(packageLineResult4.toString(), "id");
        StringBuilder packageLineResult5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestMultiDeletePL5' thn__Package__c='" + packageId + "' thn__Type__c='Beverage'" +
                        " thn__Product__c='" + winesID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId5 = JsonParser2.getFieldValue(packageLineResult5.toString(), "id");
        StringBuilder packageLineResult6 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestMultiDeletePL5' thn__Package__c='" + packageId + "' thn__Type__c='Beverage'" +
                        " thn__Product__c='" + beverageID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId6 = JsonParser2.getFieldValue(packageLineResult6.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='QuoteTestMultiDeleteAuto1' thn__Pax__c=1 thn__Hotel__c='" + propertyID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
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
                        "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder myseQuoteResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='QuoteTestMultiDeleteAuto2' thn__Pax__c=1 thn__Hotel__c='" + propertyID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID2 = JsonParser2.getFieldValue(myseQuoteResult2.toString(), "id");
        StringBuilder quoteHotelRoom1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID2 + "' thn__Product__c='"
                        + room1NightID + "' thn__Space_Area__c='" + roomTypeSingleID + "'" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId1 = JsonParser2.getFieldValue(quoteHotelRoom1.toString(), "id");
        StringBuilder quoteHotelRoom2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID2 + "' thn__Product__c='"
                        + room2NightsID + "' thn__Space_Area__c='" + roomTypeSingleID + "'" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId2 = JsonParser2.getFieldValue(quoteHotelRoom2.toString(), "id");
        StringBuilder quoteMeetingRoom1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID2 + "' thn__Product__c='"
                        + meetingHalfDayID + "'" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteMeetingRoomID1 = JsonParser2.getFieldValue(quoteMeetingRoom1.toString(), "id");
        StringBuilder quoteMeetingRoom2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID2 + "' thn__Product__c='"
                        + meetingFullDayID + "'" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteMeetingRoomID2 = JsonParser2.getFieldValue(quoteMeetingRoom2.toString(), "id");
        StringBuilder quoteProduct1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID2 + "' thn__Product__c='"
                        + winesID + "'" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId1 = JsonParser2.getFieldValue(quoteProduct1.toString(), "id");
        StringBuilder quoteProduct2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID2 + "' thn__Product__c='"
                        + beverageID + "'" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId2 = JsonParser2.getFieldValue(quoteProduct2.toString(), "id");
        StringBuilder myseQuoteResult3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='QuoteTestMultiDeleteAuto3' thn__Pax__c=1 thn__Hotel__c='" + propertyID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID3 = JsonParser2.getFieldValue(myseQuoteResult3.toString(), "id");
        StringBuilder quoteHotelRoom3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID3 + "' thn__Product__c='"
                        + room1NightID + "' thn__Space_Area__c='" + roomTypeSingleID + "' thn__Reserved__c=true" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId3 = JsonParser2.getFieldValue(quoteHotelRoom3.toString(), "id");
        StringBuilder quoteHotelRoom4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID3 + "' thn__Product__c='"
                        + room1NightID + "' thn__Space_Area__c='" + roomTypeSingleID + "'" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId4 = JsonParser2.getFieldValue(quoteHotelRoom4.toString(), "id");
    }

    @Test(priority = 3, description = "Delete quote products, meeting rooms, hotel rooms which are part of the package")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void multiDeleteTest1() throws InterruptedException, IOException {
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto1");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.selectItems("2");
        quoteHotelRoom.multiDeleteRecords();
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto1");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItems("2");
        quoteMeetingRoom.multiDeleteRecords();
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto1");
        myceQuotes.openProducts();
        quoteProducts.selectAllItems("2");
        quoteProducts.multiDeleteRecords();
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

    @Test(priority = 4, description = "Delete quote package")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void multiDeleteTest2() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, urlForScratch, ALIAS, passwordForScratch);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto1");
        myceQuotes.openMeetingPackages();
        quoteMeetingPackages.selectItemNumber("1");
        quoteMeetingPackages.multiDeleteRecords();
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

    @Test(priority = 5, description = "Delete quote products, meeting rooms, hotel rooms which are not part of the package")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void multiDeleteTest3() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, urlForScratch, ALIAS, passwordForScratch);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto2");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.selectItems("2");
        quoteHotelRoom.multiDeleteRecords();
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto2");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItems("2");
        quoteMeetingRoom.multiDeleteRecords();
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto2");
        myceQuotes.openProducts();
        quoteProducts.selectAllItems("2");
        quoteProducts.multiDeleteRecords();
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

    @Test(priority = 6, description = "Delete quote hotel rooms Quote_Hotel_Room__c records If in one of them" +
            " reserved__c == true and Mews_State__c != Cancelled")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi delete testing")
    public void multiDeleteTest4() throws InterruptedException, IOException {
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("QuoteTestMultiDeleteAuto3");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.selectItems("2");
        quoteHotelRoom.multiDeleteRecords();
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
