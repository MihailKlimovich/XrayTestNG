package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

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
        Thread.sleep(10000);
    }

    @Test(priority = 3, description = "")
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

    @Test(priority = 4, description = "")
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

    @Test(priority = 5, description = "")
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

    @Test(priority = 6, description = "")
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

    @Test(priority = 7, description = "")
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



}
