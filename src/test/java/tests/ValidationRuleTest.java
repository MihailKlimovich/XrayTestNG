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
    public void settingUpValidationRules() throws InterruptedException {
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCode("  update new User(Id = UserInfo.getUserId(),thn__ByPassVR__c = false);\n" +
                "thn__bypass__c bp = [SELECT Id, thn__bypassvr__c FROM thn__bypass__c];\n" +
                "bp.thn__bypassvr__c = false;\n" +
                "update bp;" );
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
                ("Test1508", date.generateTodayDate(), date.generateDate_ddMMyyyy(1, 1),
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
        //then
        Assert.assertEquals(message, expectedMessage);
        //myceQuotes.closeWindow(driver);
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
        String text = "Packages";
        packages.goToPackages();
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
        String text = "Packages";
        packages.goToPackages();
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath2("Test15", "Demo");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_whereAppliedDateIsNotEmpty
                ("Test15", "00:00", "01:00", "25", "20");
        String message = packageLine.readErrorMessage2(driver);
        //then
        Assert.assertEquals(message, expectedMessage);
        packageLine.closeWindow(driver);
    }






}
