package tests;

import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestData extends BaseTest{



        @Test(priority = 1, description = "LogIn")
        @Severity(SeverityLevel.NORMAL)
        @Description("LogIn")
        @Story("LogIn")
        public void testLogIn() {
            //given
            //when
            loginPageForScratchOrg.logInOnScratchOrg(driver);
        }

        @Test (priority = 2)
        @Severity(SeverityLevel.NORMAL)
        @Description("Delete old data")
        @Story("Delete Test Data")
        public void deleteData() throws InterruptedException, IOException {
            developerConsoleWindow.openDeveloperConsole();
            developerConsoleWindow.openExecuteAnonymousWindow();
            developerConsoleWindow.runApexCodeFromFile("src/main/Data/DeleteData");
        }

}
