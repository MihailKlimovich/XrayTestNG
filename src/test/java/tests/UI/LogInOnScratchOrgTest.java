package tests.UI;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import tests.BaseTest;

public class LogInOnScratchOrgTest extends BaseTest{

    @Test (priority = 1, description="LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Description: LogIn")
    @Story("LogIn")
    public void testLoginPageForScratchOrg_happyPath() throws InterruptedException {
        //given
        String text = "Accounts";

        //when
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        String AccountName = accounts.getAccountName(driver);
        System.out.println(AccountName);

        //@Test(priority = 2, description="Delete Old Data")
        //@Severity(SeverityLevel.NORMAL)
        //@Story("Settings")
        //public void deleteOldData() throws InterruptedException {
            //developerConsoleWindow.openDeveloperConsole();
            //developerConsoleWindow.openExecuteAnonymousWindow();
            //developerConsoleWindow.runApexCode("  delete [select id from Account];" );
            //Thread.sleep(60000);
            //where name='First Test Account'
        //}





    }

}
