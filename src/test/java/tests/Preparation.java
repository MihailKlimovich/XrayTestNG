package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import java.io.IOException;

public class Preparation extends BaseTest {

    @Test(priority = 1, description = "Update Default Agile Value for hotel Demo")
    @Severity(SeverityLevel.NORMAL)
    @Story("Preparation")
    public void updateDefaultAgileValue() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ADMIN_USERNAME, ADMIN_PASSWORD);
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/DefaultAgileValueForUnlockedOrg");
    }

    @Test (priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Create Occupancy Type Metadata")
    @Story("Occupancy Type Metadata")
    public void createOccupancyTypeMetadata() throws InterruptedException, IOException {
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/OccupancyTypeMetadata");
    }

}
