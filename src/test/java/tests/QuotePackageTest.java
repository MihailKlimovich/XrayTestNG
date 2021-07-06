package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class QuotePackageTest extends BaseTest {

    @Test(priority = 1, description = "Quote_Package")
    @Severity(SeverityLevel.NORMAL)
    @Description("Dates_within_Quote_dates")
    @Story("")
    public void testCreateQuotePackage() throws InterruptedException, IOException {
        String expectedMessage = "Start and end date of package must be within Quote arrival and departure dates";
        String expectedMessage2 = "Start Date of the package is after the Departure Date";
        String expectedMessage3 = "End Date of the package is after the Departure Date";

        //when
        String text = "MYCE Quotes";
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test222", date.generateDate_plus(1, 3), date.generateDate_plus(1, 4), "10", "Demo");
        myceQuotes.openMeetingPackages(driver);
        //thn__Start_Date__c  < thn__MYCE_Quote__r.thn__Arrival_Date__c
        quoteMeetingPackages.createMeetingPackages("Package 1", "4",date.generateDate_plus(1, 2), date.generateDate_plus(1, 4), "30");
        //then
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver),expectedMessage);
        //thn__Start_Date__c> thn__MYCE_Quote__r.thn__Departure_Date__c
        quoteMeetingPackages.changeDate(date.generateDate_plus(1, 5), date.generateDate_plus(1, 4));
        Thread.sleep(1000);
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage2);
        //thn__End_Date__c< thn__MYCE_Quote__r.thn__Arrival_Date__c
        quoteMeetingPackages.changeDate(date.generateDate_plus(1, 3), date.generateDate_plus(1, 2));
        Thread.sleep(1000);
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage);
        //thn__End_Date__c> thn__MYCE_Quote__r.thn__Departure_Date
        quoteMeetingPackages.changeDate(date.generateDate_plus(1, 3), date.generateDate_plus(1, 5));
        Thread.sleep(1000);
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage3);
        quoteMeetingPackages.closeWindow();
        Thread.sleep(2000);
    }

    @Test(priority = 2, description = "Quote_Package")
    @Severity(SeverityLevel.NORMAL)
    @Description("Discount")
    @Story("")
    public void testCreateQuotePackage2() throws InterruptedException {
        String expectedMessage = "Discount on quote package cannot be greater than discount max.";

        //when
        String text = "MYCE Quotes";
        packages.goToPackages();
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath2("Test2", "DEMO", "15" );
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_applyDiscountIsTrue("Pack1", "Food", "DINER", "00:00", "01:00", "20");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_applyDiscountIsTrue("Pack2", "Hotel Room", "ROOM 1 NIGHT", "00:00", "01:00", "20");
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        Thread.sleep(2000);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test222", date.generateTodayDate(), date.generateDate_plus(1, 3), "4", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Test2", "4",date.generateTodayDate(), date.generateDate_plus(1, 3), "30");
        quoteMeetingPackages.clickEdit();
        quoteMeetingPackages.changeDiscount("70");
        //then
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage);
        quoteMeetingPackages.closeWindow();
    }

    @Test(priority = 3, description = "Quote_Package")
    @Severity(SeverityLevel.NORMAL)
    @Description("Pax")
    @Story("")
    public void testCreateQuotePackage3() throws InterruptedException {
        String expectedMessage = "Pax";

        //when
        String text = "MYCE Quotes";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        Thread.sleep(2000);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test222", date.generateTodayDate(), date.generateDate_plus(1, 3), "4", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Test2", "5",date.generateTodayDate(), date.generateDate_plus(1, 3), "30");
        //then
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage);
        quoteMeetingPackages.closeWindow();
    }

    @Test(priority = 2, description = "Quote_Package")
    @Severity(SeverityLevel.NORMAL)
    @Description("QuotePackage_Account")
    @Story("")
    public void testCreateQuotePackage4() throws InterruptedException {
        String expectedMessage = "Discount on quote package cannot be greater than discount max.";

        //when
        String text = "MYCE Quotes";
        packages.goToPackages();
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath2("Test3", "DEMO", "15" );
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_applyDiscountIsTrue("Pack1", "Food", "DINER", "00:00", "01:00", "20");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_applyDiscountIsTrue("Pack2", "Hotel Room", "ROOM 1 NIGHT", "00:00", "01:00", "20");
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        Thread.sleep(2000);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test222", date.generateTodayDate(), date.generateDate_plus(1, 3), "4", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Test2", "4",date.generateTodayDate(), date.generateDate_plus(1, 3), "30");
        quoteMeetingPackages.clickEdit();
        quoteMeetingPackages.changeDiscount("70");
        //then
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage);
        quoteMeetingPackages.closeWindow();
    }




}
