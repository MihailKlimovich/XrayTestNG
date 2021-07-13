package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class ItemTest extends BaseTest {

    @Test(priority = 1, description = "Item")
    @Severity(SeverityLevel.NORMAL)
    @Description("VR02_item_send_to_mews")
    @Story("")
    public void testCreateItem() throws InterruptedException, IOException {
        //given
        String expectedMessage = "The Reservation product already exists and cannot be sent twice";
        //when
        String text = "Items";
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        items.clickNew();
        items.createItem("MEETING HALF DAY", "555");
        //then
        Assert.assertEquals(items.readHelpErrorMessage(), expectedMessage);
        items.closeWindow();

    }

}
