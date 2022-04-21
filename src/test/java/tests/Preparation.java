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
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DefaultAgileValueForUnlockedOrg.apex");
    }

    @Test (priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Description("Create Occupancy Type Metadata")
    @Story("Occupancy Type Metadata")
    public void updateDefaultAgileValue2() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/Demo2DefaultAgileValue.apex");
    }

    @Test (priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Description("Create Occupancy Type Metadata")
    @Story("Occupancy Type Metadata")
    public void createOccupancyTypeMetadata() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/OccupancyTypeMetadata.apex");
    }

    @Test (priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Description("Create Hapi Connector Metadata")
    @Story("Hapi Connector Metadata")
    public void createHapiConnector() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/HapiConnectorDemo.apex");
    }


}
