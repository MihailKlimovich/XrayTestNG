package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.CreditNoteLine;
import utils.Listeners.TestListener;

import java.io.IOException;

@Listeners({TestListener.class})

public class ValidationRuleTest extends BaseTest{

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Description("LogIn")
    @Story("LogIn")
    public void testLogIn() {
        //given
        //when
        loginPageForScratchOrg.logInOnScratchOrg(driver);
    }

    @Test(priority = 2, description="Setting up validation rules")
    @Severity(SeverityLevel.NORMAL)
    @Description("Setup.thn__ByPass__c.thn__ByPassVR__c == false and User.thn__ByPassVR__c == false")
    @Story("Settings")
    public void settingUpValidationRules() throws InterruptedException, IOException {
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/ValidationRule1");
        Thread.sleep(5000);
    }

    @Test(priority = 3, description = "Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Create New MYСE Quote with an empty commission field  ")
    @Story("")
    public void testCreateNewMyceQuote1() throws InterruptedException {
        //given
        String expectedMessage = "We hit a snag.";
        //when
        String text = "MYCE Quotes";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaForm_whenCommissionIsNone
                ("Test1508", date.generateTodayDate(), date.generateTodayDate(),
                "123", "test");
        String message = myceQuotes.readErrorMessage(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 4, description = "Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Create New MYСE Quote where commission field is Agent  ")
    @Story("")
    public void testCreateNewMyceQuote2() throws InterruptedException {
        //given
        String expectedMessage = "We hit a snag.";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaForm_whenCommissionIsAgent
                ("Test1508", date.generateTodayDate(), date.generateTodayDate(),
                        "123", "test");
        String message = myceQuotes.readErrorMessage(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 5, description = "Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Create New MYСE Quote where commission field is Company")
    @Story("")
    public void testCreateNewMyceQuote3() throws InterruptedException {
        //given
        String expectedMessage = "We hit a snag.";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaForm_whenCommissionIsCompany
                ("Test1508", date.generateTodayDate(), date.generateTodayDate(),
                        "123", "test");
        String message = myceQuotes.readErrorMessage(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 6, description = "Dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Create New MYСE Quote where Departure Date < Arrival Date")
    @Story("")
    public void testCreateNewMyceQuote4() throws InterruptedException {
        //given
        String expectedMessage = "Departure Date";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaFormWithoutCommission
                ("Test1508", date.generateTodayDate(), date.generateDate_minus(1, 1),
                        "123", "test");
        String message = myceQuotes.readErrorMessage2(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 7, description = "Company_Agent_Type")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Create New MYСE Quote where Сompany is selected in the Agent field and Agent is" +
            " selected in the Company field")
    @Story("")
    public void testCreateNewMyceQuote5() throws InterruptedException {
        //given
        String expectedMessage = "Company cannot be of type 'Agent' and Agent must be of type 'Agent' or 'Leads'";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaForm_whenCompanyIsAgentAndAgentIsCompany
                ("Test1508", date.generateTodayDate(), date.generateTodayDate(), "123", "Test",
                        "Agent", "Company");
        String message = myceQuotes.readErrorMessage2(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 8, description = "Reservation_Guest")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Create New MYСE Quote where Reservation Guest field is empty")
    @Story("")
    public void testCreateNewMyceQuote6() throws InterruptedException {
        //given
        String expectedMessage = "Reservation Guest";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaFormWhereReservationGuestIsNull
                ("Test1508", date.generateTodayDate(), date.generateTodayDate(), "123", "Test");
        String message = myceQuotes.readErrorMessage2(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 9, description = "Closed_Status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Change Stage on MYCE Quote to '4 - Closed'")
    @Story("")
    public void testChangeMyceQuote1() throws InterruptedException {
        //given
        String expectedMessage = "Closed Status is required when quote is at stage '4 - Closed'";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath
                ("Test1508", date.generateTodayDate(), date.generateTodayDate(), "123", "Test");
        myceQuotes.clickEdit(driver);
        myceQuotes.changeStage(driver);
        myceQuotes.clickSave(driver);
        String message = myceQuotes.readErrorMessage2(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 10, description = "Cancelled_Status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Change MYCE Quote Closed Status to ‘Cancelled’")
    @Story("")
    public void testChangeMyceQuote2() throws InterruptedException {
        //given
        String expectedMessage = "We hit a snag.";
        //when
        myceQuotes.clickEdit(driver);
        myceQuotes.clickIsConfirmed(driver);
        myceQuotes.changeCloseStatus(driver);
        myceQuotes.clickSave(driver);
        String message = myceQuotes.readErrorMessage(driver);
        System.out.println("DEBUG " + message);
        //then
        Assert.assertEquals(message, expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 11, description = "Invoice_Line_Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: Create New Credit Note Lines Quote" +
            " where Invoice Line == null & Amount == null & Quantity == null")
    @Story("")
    public void testCreateNewCreditNoteLine() throws InterruptedException {
        //given
        String expectedMessage = "When a credit note line isn't linked to an Invoice line then the Amount and VAT is required.";
        //when
        String text = "Credit Note Lines";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        creditNoteLine.clickNewCreditNoteLineButton(driver);
        creditNoteLine.fillOutNewCreditNoteLineForm(driver, "20");
        String message = creditNoteLine.readErrorMessage2(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        creditNoteLine.closeWindow(driver);
    }

    @Test(priority = 12, description = "Package_Line")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description:For package where hn__Multi_Days__c == true," +
            " create Package line:thn__AppliedDay__c == null")
    @Story("")
    public void testCreateNewPackageLine() throws InterruptedException {
        //given
        String expectedMessage = "Applied Day is required when a package is Multi days";
        //when
        packages.goToPackages();
        Thread.sleep(3000);
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath("Test15", "Demo");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_whereAppliedDateIsEmpty("Test15", "00:00", "01:00", "25");
        String message = packageLine.readErrorMessage2(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        packageLine.closeWindow(driver);
        Thread.sleep(3000);
    }

    @Test(priority = 13, description = "Package_Line")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description:For package where hn__Multi_Days__c == false," +
            " create Package line:thn__AppliedDay__c !== null")
    @Story("")
    public void testCreateNewPackageLine2() throws InterruptedException {
        //given
        String expectedMessage = "Applied Day must be left empty when a package is Multi days";
        //when
        packages.goToPackages();
        Thread.sleep(3000);
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath2("Test15", "Demo");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_whereAppliedDateIsNotEmpty
                ("Test15", "00:00", "01:00", "25", "20");
        String message = packageLine.readErrorMessage2(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        packageLine.closeWindow(driver);
        Thread.sleep(3000);
    }

    @Test(priority = 14, description = "Package_Line")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create package line where Property  ‘A' for Package with Property 'B’")
    @Story("")
    public void testCreateNewPackageLine3() throws InterruptedException {
        //given
        String expectedMessage = "Product";
        //when
        packages.goToPackages();
        Thread.sleep(3000);
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath3("Test15", "Test");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_whereAppliedDateIsEmpty("Test15", "00:00", "01:00", "25");
        String message = packageLine.readErrorMessage2(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        packageLine.closeWindow(driver);
    }

    @Test(priority = 15, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Arrival_Date_Time__c > thn__Departure_Date_Time__c")
    @Story("")
    public void testCreateQuoteHotelRoom() throws InterruptedException {
        String expectedMessage = "Arrival Date time cannot be after Departure Date time";
        //when
        String text = "MYCE Quotes";
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

    @Test(priority = 16, description = "Quote_Hotel_Room")
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

    @Test(priority = 17, description = "Quote_Hotel_Room")
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

    @Test(priority = 18, description = "Quote_Hotel_Room")
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

    @Test(priority = 19, description = "Quote_Hotel_Room")
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

    @Test(priority = 20, description = "Quote_Hotel_Room")
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
