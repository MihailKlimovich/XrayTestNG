package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class PresentationTest extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Description("LogIn")
    @Story("LogIn")
    public void testLogIn() {
        //given
        //when
        loginPageForScratchOrg.logInOnScratchOrg(driver);
    }

    /*@Test(priority = 2, description="Setting up validation rules")
    @Severity(SeverityLevel.NORMAL)
    @Description("Setup.thn__ByPass__c.thn__ByPassVR__c == false and User.thn__ByPassVR__c == false")
    @Story("Settings")
    public void settingUpValidationRules() throws InterruptedException, IOException {
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/ValidationRule1");
        Thread.sleep(5000);
    }*/

    @Test(priority = 3, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c == null")
    public void testCreateNewMyceQuote1() throws InterruptedException {
        //given
        String expectedMessage = "If commissionable = true, 'Commission to' field shouldn't be null or " +
                "if 'Commission to' field equals 'agent', agent shouldn't be null or " +
                "if 'Commission to' field equals 'company', company shouldn't be null";
        //when
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote();
        myceQuotes.fillOutTheQuotaForm_whenCommissionIsNone
                ("Test1", date.generateTodayDate(), date.generateTodayDate(),
                        "10", "Demo");
        //then
        Assert.assertEquals(myceQuotes.readErrorMessage2(), expectedMessage);
        myceQuotes.closeWindow();
    }

    @Test(priority = 4, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c != Agent & thn__Agent__c == null")
    public void testCreateNewMyceQuote2() throws InterruptedException {
        //given
        String expectedMessage = "If commissionable = true, 'Commission to' field shouldn't be null or " +
                "if 'Commission to' field equals 'agent', agent shouldn't be null or " +
                "if 'Commission to' field equals 'company', company shouldn't be null";
        //when
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote();
        myceQuotes.fillOutTheQuotaForm_whenCommissionIsAgent
                ("Test2", date.generateTodayDate(), date.generateTodayDate(),
                        "10", "Demo");
        //then
        Assert.assertEquals(myceQuotes.readErrorMessage2(), expectedMessage);
        //myceQuotes.closeWindow();
    }

}
