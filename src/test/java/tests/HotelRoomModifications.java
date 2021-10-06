package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pageObject.SfdxCommand;

import java.io.IOException;

public class HotelRoomModifications extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Hotel ")
    public void logIn() throws InterruptedException, IOException {
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:auth:jwt:grant",
                "--clientid",
                key,
                "--jwtkeyfile",
                "/home/user/jdoe/JWT/server.key",
                "--username",
                ALIAS,
                "--instanceurl",
                "https://test.salesforce.com"
        });
        System.out.println(authorise);
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, urlForScratch, ALIAS, passwordForScratch);
    }


}
