package tests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestInWork extends BaseTest{

    @Test(priority = 1, description = "Quote_Meetings_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("SetupResource")
    @Story("")
    public void testCreateQuoteMeetingsRoom() throws InterruptedException, IOException {
        String expectedMessage = "Meeting room's pax exceeds the resource's capacity for this setup";
        //when
        String text = "MYCE Quotes";
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/Resource");
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test222", date.generateDate_plus(1, 3), date.generateDate_plus(1, 3), "10", "Demo");
        myceQuotes.openMeetingRooms(driver);
        quoteMeetingRoom.createMeetingRoom("6");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.changeSetupType("Cabaret");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.changeSetupType("Circle");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.changeSetupType("Classroom");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.changeSetupType("Custom");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.changeSetupType("Dinner");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.changeSetupType("Party");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.changeSetupType("Square");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.changeSetupType("Theater");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.changeSetupType("U-Shape");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        Thread.sleep(2);
        quoteMeetingRoom.closeWindow(driver);
    }

    @Test(priority = 2, description = "Quote_Meetings_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Lock_Resource")
    @Story("")
    public void testCreateQuoteMeetingsRoom2() throws InterruptedException, IOException {
        String expectedMessage = "Resource";
        //when
        myceQuotes.openMeetingRooms(driver);
        quoteMeetingRoom.createMeetingRoom("4");
        quoteMeetingRoom.changeResource();
        //Then
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.closeWindow(driver);
    }

    @Test(priority = 2, description = "Quote_Meetings_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Lock_Resource")
    @Story("")
    public void testCreateQuoteMeetingsRoom3() throws InterruptedException, IOException {
        String expectedMessage = "Resource";
        //when
        String text = "MYCE Quotes";
        myceQuotes.openMeetingRooms(driver);
        quoteMeetingRoom.createMeetingRoom("4");
        quoteMeetingRoom.changeResource();
        //Then
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2(driver));
        quoteMeetingRoom.closeWindow(driver);
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test222", date.generateDate_plus(1, 3), date.generateDate_plus(1, 3), "4", "Demo");


    }











}
