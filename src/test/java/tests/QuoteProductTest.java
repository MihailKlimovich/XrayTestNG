package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class QuoteProductTest extends BaseTest {

    @Test(priority = 1, description = "Quote_Product")
    @Severity(SeverityLevel.NORMAL)
    @Description("VR08_Start_End_Date")
    @Story("")
    public void testCreateQuoteProduct() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Start Date time cannot be posterior to End Date time";
        //when
        String text = "MYCE Quotes";
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2("Test222", date.generateDate_plus(0, 0),
                date.generateDate_plus(0, 4), "10", "Demo");
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "5", date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 0));
        //then
        Assert.assertEquals(quoteProducts.readErrorMessage3(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 2, description = "Quote_Product")
    @Severity(SeverityLevel.NORMAL)
    @Description("VR11_Dates_within_Quote_dates")
    @Story("")
    public void testCreateQuoteProduct2() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Start and end date of product must be within Quote arrival and departure dates";
        //when
        String text = "MYCE Quotes";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2("Test222", date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 4), "10", "Demo");
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "5", date.generateDate_plus(0, 0),
                date.generateDate_plus(0, 4));
        //then
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "5", date.generateDate_plus(0, 5),
                date.generateDate_plus(0, 4));
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "5",  date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 0));
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "5",  date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 5));
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 3, description = "Quote_Product")
    @Severity(SeverityLevel.NORMAL)
    @Description("VR17_Pax")
    @Story("")
    public void testCreateQuoteProduct3() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Pax cannot be greater than quote's pax";
        //when
        String text = "MYCE Quotes";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2("Test222", date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 4), "10", "Demo");
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "11", date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 4));
        //then
        Assert.assertEquals(quoteProducts.readHelpErrorMessage(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 4, description = "Quote_Product")
    @Severity(SeverityLevel.NORMAL)
    @Description("ServiceArea_date")
    @Story("")
    public void testCreateQuoteProduct4() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Date of the service area must be the same as the product's";
        //when
        String text = "MYCE Quotes";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test222", date.generateDate_plus(0, 1),
                        date.generateDate_plus(0, 4), "10", "Demo");
        Thread.sleep(2000);
        myceQuotes.openMeetingRooms(driver);
        quoteMeetingRoom.createMeetingRoom2("MEETING HALF DAY");
        quoteMeetingRoom.clickQuoteName("Test222");
        myceQuotes.openProducts();
        quoteProducts.createProduct2("DEFAULT - MEETING HALF DAY", "DINER",
                date.generateDate_plus(0, 2), date.generateDate_plus(0, 4));
        //then
        Assert.assertEquals(quoteProducts.readHelpErrorMessage(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 5, description = "Quote_Product")
    @Severity(SeverityLevel.NORMAL)
    @Description("PackageDate")
    @Story("")
    public void testCreateQuoteProduct5() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Date cannot be changed if Product is part of package";
        //when
        String text = "MYCE Quotes";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test222", date.generateDate_plus(0, 1),
                        date.generateDate_plus(0, 4), "10", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Pack d", "10", date.generateDate_plus(0, 2),
                date.generateDate_plus(0, 2), "20");
        quoteMeetingPackages.clickQuoteName("Test222");
        myceQuotes.openProducts();
        myceQuotes.openProduct("DINER");
        quoteProducts.changeDate(date.generateDate_plus(0, 3), date.generateDate_plus(0, 4));
        //then
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 6, description = "Quote_Product")
    @Severity(SeverityLevel.NORMAL)
    @Description("Consumption_on_Package_Line")
    @Story("")
    public void testCreateQuoteProduct6() throws InterruptedException, IOException {
        //given
        String expectedMessage = "In a package line quote product the on consumption option can not be used.";
        //when
        String text = "MYCE Quotes";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test222", date.generateDate_plus(0, 1),
                        date.generateDate_plus(0, 4), "10", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Pack d", "10", date.generateDate_plus(0, 2),
                date.generateDate_plus(0, 2), "20");
        quoteMeetingPackages.clickQuoteName("Test222");
        myceQuotes.openProducts();
        myceQuotes.openProduct("DINER");
        quoteProducts.changeOnConsumption();
        //then
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
    }
}
