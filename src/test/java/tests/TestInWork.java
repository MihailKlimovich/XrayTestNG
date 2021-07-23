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


    @Test(priority = 21, description = "Quote_Meetings_Room__c.VR19_SetupResource")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR19_SetupResource")
    @Story("")
    public void testCreateQuoteMeetingsRoom1() throws InterruptedException, IOException {
        String expectedMessage = "Meeting room's pax exceeds the resource's capacity for this setup";
        //when
        //developerConsoleWindow.openDeveloperConsole();
        //developerConsoleWindow.openExecuteAnonymousWindow();
        //developerConsoleWindow.runApexCodeFromFile("src/main/Data/Resource");
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote();
        myceQuotes.createMyceQuote_happyPath2
                ("Test10", date.generateDate_plus(1, 3), date.generateDate_plus(1, 3), "10", "Demo");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.createMeetingRoom("6");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2());
        quoteMeetingRoom.changeSetupType("Cabaret");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2());
        quoteMeetingRoom.changeSetupType("Circle");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2());
        quoteMeetingRoom.changeSetupType("Classroom");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2());
        quoteMeetingRoom.changeSetupType("Custom");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2());
        quoteMeetingRoom.changeSetupType("Dinner");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2());
        quoteMeetingRoom.changeSetupType("Party");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2());
        quoteMeetingRoom.changeSetupType("Square");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2());
        quoteMeetingRoom.changeSetupType("Theater");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2());
        quoteMeetingRoom.changeSetupType("U-Shape");
        Assert.assertEquals(expectedMessage, myceQuotes.readErrorMessage2());
        Thread.sleep(2);
        quoteMeetingRoom.closeWindow();
    }

    @Test(priority = 22, description = "Quote_Meetings_Room__c.VR21_Lock_Resource")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR21_Lock_Resource")
    @Story("")
    public void testCreateQuoteMeetingsRoom2() throws InterruptedException, IOException {
        String expectedMessage = "Resource cannot be changed when meeting room is locked";
        //when
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.createMeetingRoom("4");
        quoteMeetingRoom.changeResource();
        //Then
        Assert.assertEquals(myceQuotes.readHelpErrorMessage(), expectedMessage);
        quoteMeetingRoom.closeWindow();
    }












}
