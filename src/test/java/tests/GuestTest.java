package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class GuestTest extends BaseTest {

    @Test(priority = 1, description = "Guest__c")
    @Severity(SeverityLevel.NORMAL)
    @Description("VR01_guest_send_to_mews")
    @Story("")
    public void testCreateGuest() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Hotel is required to create/update guest in Mews";
        //when
        String text = "Guests";
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        guests.clickNew();
        guests.createGuest("John");
        //then
        Assert.assertEquals(guests.readHelpErrorMessage(), expectedMessage);
        //guests.closeWindow();
    }




}
