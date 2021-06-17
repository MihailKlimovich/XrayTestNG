package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.Listeners.TestListener;

@Listeners({TestListener.class})


public class CreateAccountTest extends BaseTest {


    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Description("LogIn")
    @Story("LogIn")
    public void testLogIn() {
        //given
        //when
        loginPageForScratchOrg.logInOnScratchOrg(driver);
    }

    @Test(priority = 2, description="Delete Old Data")
    @Severity(SeverityLevel.NORMAL)
    @Story("Settings")
    public void deleteOldData() throws InterruptedException {
    developerConsoleWindow.openDeveloperConsole();
    developerConsoleWindow.openExecuteAnonymousWindow();
    developerConsoleWindow.runApexCode("  delete [select id from Account];" );
    Thread.sleep(10000);
    //where name='First Test Account'
    }

    @Test(priority = 3, description = "CreateNewAccount")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: CreateNewAccount")
    @Story("CreateNewAccount")
    public void testCreateNewAccount() throws InterruptedException {
        //given

        //when
        developerConsoleWindow.openDeveloperConsole();
        String text = "Accounts";
        String expectedAccountName = "First Test Account";
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCode(" List<Account> accounts = new  List<Account>();\n" +
                "accounts.add(new Account(Name = '" + expectedAccountName + "'));\n" +
                "try {insert accounts;\n" +
                "System.debug('Insert Accounts - Done');\n" +
                "} catch (DmlException e)  {System.debug('Error');}");
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        String producedName = accounts.getAccountName(driver);
        //then
        Assert.assertEquals(producedName, expectedAccountName);
    }

    @Test(priority = 4, description = "ChangeIndusty")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: ChangeIndusty")
    @Story("ChangeIndusty")
    public void changeIndustry() throws InterruptedException{
        //Given
        String expectedIndustryName = "Food & Beverage";
        //When
        accounts.changeIndustry();
        String industryName = accounts.getIndustryName(driver);
        //Then
        Assert.assertEquals(industryName, expectedIndustryName);


    }
}
