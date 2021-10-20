package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MultiEditOnQuotesRelatedList extends BaseTest {

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
    @Story("Multi edit on quote's related list UAT")
    public void login() throws InterruptedException, IOException {
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
        StringBuilder result2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "User",
                "-w",
                "Name='User User'",
                "-v",
                "thn__ByPassVR__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(result2);
        Object byPass = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__bypass__c",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> byPassID = JsonParser2.getFieldValueSoql(byPass.toString(), "Id");
        String byPassId = byPassID.get(0);
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__bypass__c",
                "-w",
                "Id='" + byPassId + "'",
                "-v",
                "thn__bypassvr__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder userRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "User",
                "-w",
                "Name='User User'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String userByPass = JsonParser2.getFieldValue(userRecord.toString(), "thn__ByPassVR__c");
        StringBuilder byPassRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__bypass__c",
                "-w",
                "Id='" + byPassId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(byPassRecord);
        String byPassVr = JsonParser2.getFieldValue(byPassRecord.toString(), "thn__ByPassVR__c");
        Assert.assertEquals(userByPass, "true");
        Assert.assertEquals(byPassVr, "true");
    }

    @Test(priority = 2, description = "Quote products and Quote meeting rooms are part of the Quote Package")
    @Severity(SeverityLevel.NORMAL)
    @Story("Multi edit on quote's related list UAT")
    public void case1() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestMultiEditAuto1",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestMultiEditAuto",
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
        StringBuilder productRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecord1.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='DINER'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String dinerID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder productRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING FULL DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingFullDayID = JsonParser2.getFieldValue(productRecord3.toString(), "Id");
        StringBuilder productRecord4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='FOOD'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String foodID = JsonParser2.getFieldValue(productRecord4.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestMultiEditAuto' thn__Hotel__c='" + propertyID + "'",
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
                "Name='TestPackLineMultiEdit1' thn__Package__c='" + packageId + "' thn__Type__c='Meeting Room'" +
                        " thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00 thn__End_Time__c=18:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder packageLineResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestPackLineMultiEdit2' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + dinerID + "' thn__Start_Time__c=12:00 thn__End_Time__c=18:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder packageLineResult3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestPackLineMultiEdit3' thn__Package__c='" + packageId + "' thn__Type__c='Meeting Room'" +
                        " thn__Product__c='" + meetingFullDayID + "' thn__Start_Time__c=12:00 thn__End_Time__c=18:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder packageLineResult4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestPackLineMultiEdit4' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + foodID + "' thn__Start_Time__c=12:00 thn__End_Time__c=18:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestMultiEditAuto1' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
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
                        "' thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        homePageForScratchOrg.openAppLauncher();
        homePageForScratchOrg.sendTextInAppWindow("MYCE Quotes");
        myceQuotes.openMyceQoteRecord("TestMultiEditAuto1");
        myceQuotes.openProducts();
        quoteProducts.selectAllItems("2");
        quoteProducts.clickMultiedit();
        quoteProducts.multiEditProducts();
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("TestMultiEditAuto1");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItems("2");
        quoteMeetingRoom.clickMultiedit();
        quoteMeetingRoom.multiEditMeetingRooms();
        StringBuilder quoteProductDinerRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='DINER' thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteProductFoodRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='FOOD' thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteMeetingRoomHalfDayRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "Name='DEFAULT - MEETING HALF DAY' thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});

        StringBuilder quoteMeetingRoomFullDayRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "Name='DEFAULT - MEETING FULL DAY' thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String setupMHD = JsonParser2.getFieldValue(quoteMeetingRoomHalfDayRecord.toString(), "thn__Setup__c");
        String functionMHD = JsonParser2.getFieldValue(quoteMeetingRoomHalfDayRecord.toString(), "thn__Function__c");
        String reservationStatusMHD = JsonParser2.getFieldValue(quoteMeetingRoomHalfDayRecord.toString(), "thn__Reservation_Status__c");
        String commissionableMHD = JsonParser2.getFieldValue(quoteMeetingRoomHalfDayRecord.toString(), "thn__Commissionable__c");
        String lockResourceMHD = JsonParser2.getFieldValue(quoteMeetingRoomHalfDayRecord.toString(), "thn__Lock_Resource__c");
        String hideOnOfferMHD = JsonParser2.getFieldValue(quoteMeetingRoomHalfDayRecord.toString(), "thn__Hide_on_Offer__c");
        String setupMFD = JsonParser2.getFieldValue(quoteMeetingRoomFullDayRecord.toString(), "thn__Setup__c");
        String functionMFD = JsonParser2.getFieldValue(quoteMeetingRoomFullDayRecord.toString(), "thn__Function__c");
        String reservationStatusMFD = JsonParser2.getFieldValue(quoteMeetingRoomFullDayRecord.toString(), "thn__Reservation_Status__c");
        String commissionableMFD = JsonParser2.getFieldValue(quoteMeetingRoomFullDayRecord.toString(), "thn__Commissionable__c");
        String lockResourceMFD= JsonParser2.getFieldValue(quoteMeetingRoomFullDayRecord.toString(), "thn__Lock_Resource__c");
        String hideOnOfferMFD = JsonParser2.getFieldValue(quoteMeetingRoomFullDayRecord.toString(), "thn__Hide_on_Offer__c");
        String onConsumptionProductDiner = JsonParser2.getFieldValue(quoteProductDinerRecord.toString(), "thn__On_Consumption__c");
        String commissionableProductDiner = JsonParser2.getFieldValue(quoteProductDinerRecord.toString(), "thn__Commissionable__c");
        String hideOnOfferProductDiner = JsonParser2.getFieldValue(quoteProductDinerRecord.toString(), "thn__Hide_on_Offer__c");
        String onConsumptionProductFood = JsonParser2.getFieldValue(quoteProductFoodRecord.toString(), "thn__On_Consumption__c");
        String commissionableProductFood = JsonParser2.getFieldValue(quoteProductFoodRecord.toString(), "thn__Commissionable__c");
        String hideOnOfferProductFood = JsonParser2.getFieldValue(quoteProductFoodRecord.toString(), "thn__Hide_on_Offer__c");
        Assert.assertEquals(setupMFD, setupMHD);
        Assert.assertEquals(functionMHD, functionMFD);
        Assert.assertEquals(reservationStatusMHD, reservationStatusMFD);
        Assert.assertEquals(commissionableMHD,"true");
        Assert.assertEquals(commissionableMFD,"true");
        Assert.assertEquals(lockResourceMHD,"true");
        Assert.assertEquals(lockResourceMFD,"true");
        Assert.assertEquals(hideOnOfferMHD,"true");
        Assert.assertEquals(hideOnOfferMFD,"true");
        Assert.assertEquals(onConsumptionProductDiner,"true");
        Assert.assertEquals(onConsumptionProductFood,"true");
        Assert.assertEquals(commissionableProductDiner,"true");
        Assert.assertEquals(commissionableProductFood,"true");
        Assert.assertEquals(hideOnOfferProductDiner,"true");
        Assert.assertEquals(hideOnOfferProductFood,"true");
    }

}
