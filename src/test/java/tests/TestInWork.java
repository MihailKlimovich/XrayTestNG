package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestInWork extends BaseTest{

    @Test(priority = 1, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Arrival_Date_Time__c > thn__Departure_Date_Time__c")
    @Story("")
    public void testCreateQuoteHotelRoom() throws InterruptedException {
        String expectedMessage = "Arrival Date time cannot be after Departure Date time";
        //when
        String text = "MYCE Quotes";
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test111", date.generateDate_plus(1, 3), date.generateDate_plus(1, 3), "3", "Demo");
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom
                (  date.generateDate_plus(1, 3), "19:00", date.generateDate_plus(1, 3), "10:00");
        String errormessage = quoteHotelRoom.readErrorMessage3(driver);
        //then
        Assert.assertEquals(errormessage, expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 2, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Arrival_Date_Time__c < thn__MYCE_Quote__r.thn__Arrival_Date__c")
    @Story("")
    public void testCreateQuoteHotelRoom2() throws InterruptedException {
        String expectedMessage = "Arrival and Departure date of hotel room must be within Quote arrival and departure dates";
        //when
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom
                (  date.generateDate_plus(1, 2), "10:00", date.generateDate_plus(1, 3), "19:00");
        String errormessage = quoteHotelRoom.readErrorMessage2(driver);
        //then
        Assert.assertEquals(errormessage, expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 3, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Arrival_Date_Time__c >  thn__MYCE_Quote__r.thn__Departure_Date__c")
    @Story("")
    public void testCreateQuoteHotelRoom3() throws InterruptedException {
        String expectedMessage = "Arrival and Departure date of hotel room must be within Quote arrival and departure dates";
        //when
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom
                (  date.generateDate_plus(1, 4), "10:00", date.generateDate_plus(1, 2), "19:00");
        String errormessage = quoteHotelRoom.readErrorMessage2(driver);
        //then
        Assert.assertEquals(errormessage, expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 4, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Departure_Date_Time__c <  thn__MYCE_Quote__r.thn__Arrival_Date__c")
    @Story("")
    public void testCreateQuoteHotelRoom4() throws InterruptedException {
        String expectedMessage = "Arrival and Departure date of hotel room must be within Quote arrival and departure dates";
        //when
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom
                (  date.generateDate_plus(1, 3), "10:00", date.generateDate_plus(1, 2), "19:00");
        String errormessage = quoteHotelRoom.readErrorMessage2(driver);
        //then
        Assert.assertEquals(errormessage, expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 5, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Departure_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c")
    @Story("")
    public void testCreateQuoteHotelRoom5() throws InterruptedException {
        String expectedMessage = "Arrival and Departure date of hotel room must be within Quote arrival and departure dates";
        //when
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom
                (  date.generateDate_plus(1, 3), "10:00", date.generateDate_plus(1, 4), "19:00");
        String errormessage = quoteHotelRoom.readErrorMessage2(driver);
        //then
        Assert.assertEquals(errormessage, expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 6, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Pax__c > thn__MYCE_Quote__r.thn__Pax__c")
    @Story("")
    public void testCreateQuoteHotelRoom6() throws InterruptedException {
        String expectedMessage = "Number";
        //when
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom2("4");
        String errormessage = quoteHotelRoom.readErrorMessage2(driver);
        //then
        Assert.assertEquals(errormessage, expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }







}
