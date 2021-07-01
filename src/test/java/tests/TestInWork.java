package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestInWork extends BaseTest{

    @Test(priority = 1, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Arrival_Date_Time__c > thn__Departure_Date_Time__c")
    @Story("")
    public void testCreateQuoteMeetingsRoom() throws InterruptedException, IOException {
        String expectedMessage = "Arrival Date time cannot be after Departure Date time";
        //when
        String text = "MYCE Quotes";
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        //developerConsoleWindow.openDeveloperConsole();
        //developerConsoleWindow.openExecuteAnonymousWindow();
        //developerConsoleWindow.runApexCodeFromFile("src/main/Data/Resource");
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test222", date.generateDate_plus(1, 3), date.generateDate_plus(1, 3), "10", "Demo");
        myceQuotes.openMeetingRooms(driver);

    }









}
