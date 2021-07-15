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
        Thread.sleep(2000);
    }


    @Test(priority = 3, description = "Quote_Meetings_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("PackageDate")
    @Story("")
    public void testCreateQuoteMeetingsRoom3() throws InterruptedException, IOException {
        String expectedMessage = "Date cannot be changed if Meeting room is part of package";
        //when
        String text = "MYCE Quotes";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        Thread.sleep(2000);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test333", date.generateTodayDate(), date.generateDate_plus(1, 3), "4", "Demo");
        myceQuotes.openMeetingPackages(driver);
        Thread.sleep(2000);
        quoteMeetingPackages.createMeetingPackages("Pack c", "4", date.generateTodayDate(), date.generateDate_plus(1, 3), "30");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("Test333");
        myceQuotes.openMeetingRooms(driver);
        quoteMeetingRoom.editDate("DEFAULT - MEETING HALF DAY", date.generateDate_plus(1, 1));
        //Then
        Assert.assertEquals(expectedMessage, quoteMeetingRoom.readErrorMessage2(driver));
        quoteMeetingRoom.closeWindow(driver);

    }











}
