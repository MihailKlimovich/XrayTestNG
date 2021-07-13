package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class ReservationTest extends BaseTest {

    @Test(priority = 1, description = "Reservation__c")
    @Severity(SeverityLevel.NORMAL)
    @Description("VR03_Reason_update")
    @Story("")
    public void testCreateReservation() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Reason update is required when price is updated";
        //when
        String text = "Reservations";
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        reservations.clickNew();
        reservations.createReservation("Demo", "ORDER", date.generateDate_plus(0,0),
                date.generateDate_plus(0, 5), "2", "1", "Double",
                "Rate pricing", "DEFAULT");
        //then
        Assert.assertEquals(reservations.readHelpErrorMessage(), expectedMessage);
        reservations.closeWindow();
    }

    @Test(priority = 2, description = "Reservation__c")
    @Severity(SeverityLevel.NORMAL)
    @Description("VR04_Cancellation_reason")
    @Story("")
    public void testCreateReservation2() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Notes cannot be empty if state is canceled";
        //when
        String text = "Reservations";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        Thread.sleep(2000);
        reservations.clickNew();
        reservations.createReservation2("Demo", "ORDER", date.generateDate_plus(0,0),
                date.generateDate_plus(0, 5), "2", "1", "Double",
                "Rate pricing", "DEFAULT", "123");
        reservations.clickEdit();
        Thread.sleep(2000);
        reservations.changeState("Canceled");
        //then
        Assert.assertEquals(reservations.readHelpErrorMessage(), expectedMessage);
        reservations.closeWindow();
    }

}
