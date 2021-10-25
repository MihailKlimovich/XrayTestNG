package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;


import java.io.IOException;

public class TestInWork extends BaseTest{

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-506: Request - Agent")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }


}
