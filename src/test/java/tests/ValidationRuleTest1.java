package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.apache.poi.ss.formula.functions.T;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.Listeners.TestListener;

import java.io.IOException;

@Listeners({TestListener.class})

public class ValidationRuleTest1 extends BaseTest {


        @Test(priority = 1, description = "LogIn")
        @Severity(SeverityLevel.NORMAL)
        @Description("LogIn")
        @Story("LogIn")
        public void testLogIn() {
            //given
            //when
            loginPageForScratchOrg.logInOnScratchOrg(driver);
        }

        /*@Test(priority = 2, description="Setting up validation rules")
        @Severity(SeverityLevel.NORMAL)
        @Description("Setup.thn__ByPass__c.thn__ByPassVR__c == false and User.thn__ByPassVR__c == false")
        @Story("Settings")
        public void settingUpValidationRules() throws InterruptedException, IOException {
            developerConsoleWindow.openDeveloperConsole();
            developerConsoleWindow.openExecuteAnonymousWindow();
            developerConsoleWindow.runApexCodeFromFile("src/main/Data/ValidationRule1");
            Thread.sleep(5000);
        }*/

    @Test(priority = 2, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c == null")
    public void testCreateNewMyceQuote1() throws InterruptedException {
        //given
        String expectedMessage = "If commissionable = true, 'Commission to' field shouldn't be null or " +
                "if 'Commission to' field equals 'agent', agent shouldn't be null or " +
                "if 'Commission to' field equals 'company', company shouldn't be null or";
        //when
        //String text = "MYCE Quotes";
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaForm_whenCommissionIsNone
                ("Test1", date.generateTodayDate(), date.generateTodayDate(),
                        "10", "Test");
        //then
        Assert.assertEquals(myceQuotes.readErrorMessage2(driver), expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 3, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c != Agent & thn__Agent__c == null")
    public void testCreateNewMyceQuote2() throws InterruptedException {
        //given
        String expectedMessage = "If commissionable = true, 'Commission to' field shouldn't be null or " +
                "if 'Commission to' field equals 'agent', agent shouldn't be null or " +
                "if 'Commission to' field equals 'company', company shouldn't be null or";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaForm_whenCommissionIsAgent
                ("Test2", date.generateTodayDate(), date.generateTodayDate(),
                        "10", "Test");
        //then
        Assert.assertEquals(myceQuotes.readErrorMessage2(driver), expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 4, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c != Company & thn__Company__c == null")
    public void testCreateNewMyceQuote3() throws InterruptedException {
        //given
        String expectedMessage = "If commissionable = true, 'Commission to' field shouldn't be null or " +
                "if 'Commission to' field equals 'agent', agent shouldn't be null or " +
                "if 'Commission to' field equals 'company', company shouldn't be null or";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaForm_whenCommissionIsCompany
                ("Test3", date.generateTodayDate(), date.generateTodayDate(),
                        "10", "Test");
        //then
        Assert.assertEquals(myceQuotes.readErrorMessage2(driver), expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 5, description = "Myce_Quote__c.VR05_Dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR05_Dates")
    @Story("thn__Departure_Date__c < thn__Arrival_Date__c")
    public void testCreateNewMyceQuote4() throws InterruptedException {
        //given
        String expectedMessage = "Departure Date cannot be anterior to Arrival Date";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaFormWithoutCommission
                ("Test4", date.generateTodayDate(), date.generateDate_minus(1, 1),
                        "10", "Test");
        //then
        Assert.assertEquals(myceQuotes.readDataErrorMessage(), expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 6, description = "Myce_Quote__c.VR27_Company_Agent_Type")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR27_Company_Agent_Type")
    @Story("Create MYCE Quote: Select Company for Agent field ,Select Agent for Company field")
    public void testCreateNewMyceQuote5() throws InterruptedException {
        //given
        String expectedMessage = "Company cannot be of type 'Agent' and Agent must be of type 'Agent' or 'Leads'";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaForm_whenCompanyIsAgentAndAgentIsCompany
                ("Test5", date.generateTodayDate(), date.generateTodayDate(), "10", "Test",
                        "Test Agent", "Test Company");
        //then
        Assert.assertEquals(myceQuotes.readErrorMessage2(driver), expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 7, description = "Myce_Quote__c.VR13_Reservation_Guest")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR13_Reservation_Guest")
    @Story("On thn__Reservation__c record where thn__Mews_Id__c != null change thn__State__c to “Canceled”")
    public void testCreateNewMyceQuote6() throws InterruptedException {
        //given
        String expectedMessage = "Reservation guest is required to send reservations to Mews";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.fillOutTheQuotaFormWhereReservationGuestIsNull
                ("Test6", date.generateTodayDate(), date.generateTodayDate(), "10", "Test");
        //then
        Assert.assertEquals(myceQuotes.readHelpErrorMessage(), expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 8, description = "Myce_Quote__c.VR22_ClosedStatus")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR22_ClosedStatus")
    @Story("Change Stage om MYCE Quote to '4 - Closed'")
    public void testCreateNewMyceQuote7() throws InterruptedException {
        //given
        String expectedMessage = "Closed Status is required when quote is at stage '4 - Closed'";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath
                ("Test7", date.generateTodayDate(), date.generateTodayDate(), "10", "Test");
        myceQuotes.clickEdit(driver);
        myceQuotes.changeStage(driver);
        myceQuotes.clickSave(driver);
        //then
        Assert.assertEquals(myceQuotes.readErrorMessage2(driver), expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 9, description = "Myce_Quote__c.VR28_Cancelled_Status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR28_Cancelled_Status")
    @Story("Set thn__Is_Confirmed__c to true, Change MYCE Quote Closed Status to ‘Cancelled’")
    public void testCreateNewMyceQuote8() throws InterruptedException {
        //given
        String expectedMessage = "Closed Status can be 'Cancelled' only if Myce quote was 'Won'";
        //when
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath
                ("Test8", date.generateTodayDate(), date.generateTodayDate(), "10", "Test");
        myceQuotes.clickEdit(driver);
        myceQuotes.clickIsConfirmed(driver);
        myceQuotes.changeCloseStatus(driver);
        myceQuotes.clickSave(driver);
        //then
        Assert.assertEquals(myceQuotes.readErrorMessage2(driver), expectedMessage);
        myceQuotes.closeWindow(driver);
    }

    @Test(priority = 10, description = "Credit_Note_Line__c.Invoice_Line_Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Credit_Note_Line__c.Invoice_Line_Validation")
    @Story("Create Credit Note Line record: thn__Invoice_Line__c == null & thn__Amount__c == null & thn__Quantity__c == null")
    public void testCreateNewCreditNoteLine() throws InterruptedException {
        //given
        String expectedMessage = "When a credit note line isn't linked to an Invoice line then the Amount and VAT is required.";
        //when
        String text = "Credit Note Lines";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        creditNoteLine.clickNewCreditNoteLineButton(driver);
        creditNoteLine.fillOutNewCreditNoteLineForm(driver, "20");
        //then
        Assert.assertEquals(creditNoteLine.readErrorMessage2(driver), expectedMessage);
        creditNoteLine.closeWindow(driver);
    }

    @Test(priority = 11, description = "Package_Line__c.VR30_IsMultidays")
    @Severity(SeverityLevel.NORMAL)
    @Description("Package_Line__c.VR30_IsMultidays")
    @Story("For Package where hn__Multi_Days__c == true, create Package line: thn__AppliedDay__c == null")
    public void testCreateNewPackageLine1() throws InterruptedException {
        //given
        String expectedMessage = "Applied Day is required when a package is Multi days";
        //when
        packages.goToPackages();
        Thread.sleep(3000);
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath("Test15", "Demo");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_whereAppliedDateIsEmpty("Test15", "00:00", "01:00", "25");
        //then
        Assert.assertEquals(packageLine.readErrorMessage2(driver), expectedMessage);
        packageLine.closeWindow(driver);;
    }

    @Test(priority = 12, description = "Package_Line__c.VR31_IsNotMultidays")
    @Severity(SeverityLevel.NORMAL)
    @Description("Package_Line__c.VR31_IsNotMultidays")
    @Story("For Package where hn__Multi_Days__c == false, create Package line: thn__AppliedDay__c != null")
    public void testCreateNewPackageLine2() throws InterruptedException {
        //given
        String expectedMessage = "Applied Day must be left empty when a package is Multi days";
        //when
        packages.goToPackages();
        Thread.sleep(3000);
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath2("Test15", "Demo", "15");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_whereAppliedDateIsNotEmpty
                ("Test15", "00:00", "01:00", "25", "20");
        //then
        Assert.assertEquals(packageLine.readErrorMessage2(driver), expectedMessage);
        packageLine.closeWindow(driver);
    }

    @Test(priority = 13, description = "Package_Line__c.VR29_Product_property")
    @Severity(SeverityLevel.NORMAL)
    @Description("Package_Line__c.VR29_Product_property")
    @Story("Create package line where Proferty  ‘A' for Package with Property 'B’")
    public void testCreateNewPackageLine3() throws InterruptedException {
        //given
        String expectedMessage = "Product's property must be the same than the package's";
        //when
        packages.goToPackages();
        Thread.sleep(3000);
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath3("Test15", "Test");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_whereAppliedDateIsEmpty("Test15", "00:00", "01:00", "25");
        //then
        Assert.assertEquals(packageLine.readHelpErrorMessage(), expectedMessage);
        packageLine.closeWindow(driver);
    }

    @Test(priority = 14, description = "Quote_Hotel_Room__c.VR06_Departure_after")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Hotel_Room__c.VR06_Departure_after")
    @Story("Add Quote hotel room on MYCE Quote: thn__Arrival_Date_Time__c > thn__Departure_Date_Time__c")
    public void testCreateQuoteHotelRoom() throws InterruptedException {
        String expectedMessage = "Arrival Date time cannot be after Departure Date time";
        //when
        String text = "MYCE Quotes";
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test9", date.generateDate_plus(1, 3), date.generateDate_plus(1, 3), "3", "Demo");
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom
                (  date.generateDate_plus(1, 3), "19:00", date.generateDate_plus(1, 3), "10:00");
        //then
        Assert.assertEquals(quoteHotelRoom.readErrorMessage3(driver), expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 15, description = "Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Story("thn__Arrival_Date_Time__c < thn__MYCE_Quote__r.thn__Arrival_Date__c")
    public void testCreateQuoteHotelRoom2() throws InterruptedException {
        String expectedMessage = "Arrival and Departure date of hotel room must be within Quote arrival and departure dates";
        //when
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom
                (  date.generateDate_plus(1, 2), "10:00", date.generateDate_plus(1, 3), "19:00");
        //then
        Assert.assertEquals(quoteHotelRoom.readErrorMessage2(driver), expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 16, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Arrival_Date_Time__c >  thn__MYCE_Quote__r.thn__Departure_Date__c")
    @Story("thn__Arrival_Date_Time__c >  thn__MYCE_Quote__r.thn__Departure_Date__c")
    public void testCreateQuoteHotelRoom3() throws InterruptedException {
        String expectedMessage = "Arrival and Departure date of hotel room must be within Quote arrival and departure dates";
        //when
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom
                (  date.generateDate_plus(1, 4), "10:00", date.generateDate_plus(1, 2), "19:00");
        //then
        Assert.assertEquals(quoteHotelRoom.readErrorMessage2(driver), expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 17, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Departure_Date_Time__c <  thn__MYCE_Quote__r.thn__Arrival_Date__c")
    @Story("thn__Departure_Date_Time__c <  thn__MYCE_Quote__r.thn__Arrival_Date__c")
    public void testCreateQuoteHotelRoom4() throws InterruptedException {
        String expectedMessage = "Arrival and Departure date of hotel room must be within Quote arrival and departure dates";
        //when
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom
                (  date.generateDate_plus(1, 3), "10:00", date.generateDate_plus(1, 2), "19:00");
        //then
        Assert.assertEquals(quoteHotelRoom.readErrorMessage2(driver), expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 18, description = "Quote_Hotel_Room")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Departure_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c")
    @Story("thn__Departure_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c")
    public void testCreateQuoteHotelRoom5() throws InterruptedException {
        String expectedMessage = "Arrival and Departure date of hotel room must be within Quote arrival and departure dates";
        //when
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom
                (  date.generateDate_plus(1, 3), "10:00", date.generateDate_plus(1, 4), "19:00");
        //then
        Assert.assertEquals(quoteHotelRoom.readErrorMessage2(driver), expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 19, description = "Quote_Hotel_Room__c.VR15_Pax")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Hotel_Room__c.VR15_Pax")
    @Story("Add Quote hotel room on MYCE Quote: thn__Pax__c > thn__MYCE_Quote__r.thn__Pax__c")
    public void testCreateQuoteHotelRoom6() throws InterruptedException {
        String expectedMessage = "Pax cannot be greater than quote's pax";
        //when
        myceQuotes.openHotelRooms(driver);
        quoteHotelRoom.createHotelRoom2("4");
        //then
        Assert.assertEquals(quoteHotelRoom.readHelpErrorMessage(), expectedMessage);
        quoteHotelRoom.closeWindow(driver);
    }

    @Test(priority = 20, description = "Quote_Meetings_Room__c.VR19_SetupResource")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR19_SetupResource")
    @Story("")
    public void testCreateQuoteMeetingsRoom1() throws InterruptedException, IOException {
        String expectedMessage = "Meeting room's pax exceeds the resource's capacity for this setup";
        //when
        String text = "MYCE Quotes";
        //developerConsoleWindow.openDeveloperConsole();
        //developerConsoleWindow.openExecuteAnonymousWindow();
        //developerConsoleWindow.runApexCodeFromFile("src/main/Data/Resource");
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test10", date.generateDate_plus(1, 3), date.generateDate_plus(1, 3), "10", "Demo");
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

    @Test(priority = 21, description = "Quote_Meetings_Room__c.VR21_Lock_Resource")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR21_Lock_Resource")
    @Story("")
    public void testCreateQuoteMeetingsRoom2() throws InterruptedException, IOException {
        String expectedMessage = "Resource cannot be changed when meeting room is locked";
        //when
        myceQuotes.openMeetingRooms(driver);
        quoteMeetingRoom.createMeetingRoom("4");
        quoteMeetingRoom.changeResource();
        //Then
        Assert.assertEquals(myceQuotes.readHelpErrorMessage(), expectedMessage);
        quoteMeetingRoom.closeWindow(driver);
    }


    @Test(priority = 22, description = "Quote_Meetings_Room__c.VR25_PackageDate")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR25_PackageDate")
    @Story("Add meeting room to the package, Add package on MYCE Quote, Open Quote meeting room record," +
            " thn__Shadow__c == FALSE, Change thn__Start_Date_Time__c, thn__End_Date_Time__")
    public void testCreateQuoteMeetingsRoom3() throws InterruptedException, IOException {
        String expectedMessage = "Date cannot be changed if Meeting room is part of package";
        //when
        String text = "MYCE Quotes";
        myceQuotes.goToMyceQuotes();
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        //Thread.sleep(2000);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test11", date.generateTodayDate(), date.generateDate_plus(1, 3), "4", "Demo");
        myceQuotes.openMeetingPackages(driver);
        Thread.sleep(2000);
        quoteMeetingPackages.createMeetingPackages("Pack c", "4", date.generateTodayDate(), date.generateDate_plus(1, 3), "30");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("Test11");
        myceQuotes.openMeetingRooms(driver);
        quoteMeetingRoom.editDate("DEFAULT - MEETING HALF DAY", date.generateDate_plus(1, 1));
        //Then
        Assert.assertEquals(expectedMessage, quoteMeetingRoom.readErrorMessage2(driver));
        quoteMeetingRoom.closeWindow(driver);
    }

    @Test(priority = 23, description = "Quote_Package__c.VR12_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR12_Dates_within_Quote_dates")
    @Story("")
    public void testCreateQuotePackage1() throws InterruptedException, IOException {
        String expectedMessage = "Start and end date of package must be within Quote arrival and departure dates";
        String expectedMessage2 = "Start Date of the package is after the Departure Date";
        String expectedMessage3 = "End Date of the package is after the Departure Date";
        //when
        String text = "MYCE Quotes";
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test12", date.generateDate_plus(1, 3), date.generateDate_plus(1, 4), "10", "Demo");
        myceQuotes.openMeetingPackages(driver);
        //thn__Start_Date__c  < thn__MYCE_Quote__r.thn__Arrival_Date__c
        quoteMeetingPackages.createMeetingPackages("Pack c", "4",date.generateDate_plus(1, 2), date.generateDate_plus(1, 4), "30");
        //then
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver),expectedMessage);
        //thn__Start_Date__c> thn__MYCE_Quote__r.thn__Departure_Date__c
        quoteMeetingPackages.changeDate(date.generateDate_plus(1, 5), date.generateDate_plus(1, 4));
        Thread.sleep(1000);
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage2);
        //thn__End_Date__c< thn__MYCE_Quote__r.thn__Arrival_Date__c
        quoteMeetingPackages.changeDate(date.generateDate_plus(1, 3), date.generateDate_plus(1, 2));
        Thread.sleep(1000);
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage);
        //thn__End_Date__c> thn__MYCE_Quote__r.thn__Departure_Date
        quoteMeetingPackages.changeDate(date.generateDate_plus(1, 3), date.generateDate_plus(1, 5));
        Thread.sleep(1000);
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage3);
        quoteMeetingPackages.closeWindow();
        Thread.sleep(2000);
    }

    @Test(priority = 24, description = "Quote_Package__c.VR14_Discount")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR14_Discount")
    @Story("")
    public void testCreateQuotePackage2() throws InterruptedException {
        //given
        String expectedMessage = "No Discount possible, package is not configured correctly. Please contact your admin";
        //when
        packages.goToPackages();
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath2("Test4", "DEMO", "15" );
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_applyDiscountIsTrue("Pack1", "Food", "DINER", "00:00", "01:00", "20");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_applyDiscountIsTrue("Pack2", "Hotel Room", "ROOM 1 NIGHT", "00:00", "01:00", "20");
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        //Thread.sleep(2000);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test13", date.generateTodayDate(), date.generateDate_plus(1, 3), "4", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Test4", "4",date.generateTodayDate(), date.generateDate_plus(1, 3), "30");
        quoteMeetingPackages.clickEdit();
        quoteMeetingPackages.changeDiscount("70");
        //then
        Assert.assertEquals(quoteMeetingPackages.readHelpErrorMessage(), expectedMessage);
        quoteMeetingPackages.closeWindow();
    }

    @Test(priority = 25, description = "Quote_Package__c.VR18_Pax")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR18_Pax")
    @Story("Add Quote package to the MYCE Quote: thn__Pax__c > thn__MYCE_Quote__r.thn__Pax__c")
    public void testCreateQuotePackage3() throws InterruptedException {
        //given
        String expectedMessage = "Pax cannot be greater than quote's pax";
        //when
        String text = "MYCE Quotes";
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.goToMyceQuotes();
        Thread.sleep(2000);
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test14", date.generateTodayDate(), date.generateDate_plus(1, 3), "4", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Test4", "5",date.generateTodayDate(), date.generateDate_plus(1, 3), "30");
        //then
        Assert.assertEquals(quoteMeetingPackages.readHelpErrorMessage(), expectedMessage);
        quoteMeetingPackages.closeWindow();
    }

    @Test(priority = 26, description = "Quote_Package__c.VR33_QuotePackage_Account")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR33_QuotePackage_Account")
    @Story("")
    public void testCreateQuotePackage4() throws InterruptedException, IOException {
        //given
        String expectedMessage = "This Package can only be instantiated on a quote which is related to its account";
        //when
        String text = "MYCE Quotes";
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test15", date.generateTodayDate(), date.generateDate_plus(1, 3), "4", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Pack a", "4",date.generateTodayDate(), date.generateDate_plus(1, 3), "20");
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage);
        quoteMeetingPackages.closeWindow();
    }

    @Test(priority = 27, description = "Quote_Package__c.VR34_QuotePackage_Dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR34_QuotePackage_Dates")
    @Story("")
    public void testCreateQuotePackage5() throws InterruptedException {
        //given
        String expectedMessage = "Quote package start date must be within package's start and end dates";
        //when
        String text = "MYCE Quotes";
        packages.goToPackages();
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath4("Pack e", "Demo", date.generateDate_plus(0, 1), date.generateDate_plus(0, 3));
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_applyDiscountIsTrue("Pack", "Hotel Room", "ROOM 1 NIGHT", "00:00", "01:00", "20");
       // homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        //Thread.sleep(2000);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test16", date.generateTodayDate(), date.generateDate_plus(0, 5), "4", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Pack e", "4",date.generateDate_plus(0, 0), date.generateDate_plus(0, 3), "30");
        //then
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage);
        quoteMeetingPackages.closeWindow();
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Pack e", "4",date.generateDate_plus(0, 4), date.generateDate_plus(0, 3), "30");
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage);
        quoteMeetingPackages.closeWindow();
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Pack e", "4",date.generateDate_plus(0, 1), date.generateDate_plus(0, 4), "30");
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage);
        quoteMeetingPackages.closeWindow();
    }

    @Test(priority = 28, description = "Quote_Package__c.VR37_Max_Discount")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR37_Max_Discount")
    @Story("Add Quote Package to MYCE Quote: set Discount on quote package > Discount max on Package")
    public void testCreateQuotePackage6() throws InterruptedException {
        //given
        String expectedMessage = "Discount on quote package cannot be greater than discount max.";
        //when
        String text = "MYCE Quotes";
        packages.goToPackages();
        packages.clickNewPackage(driver);
        packages.createPackage_happyPath2("Pack f", "Demo", "10");
        packageLine.clickNewPackageLine(driver);
        packageLine.createPackageLine_applyDiscountIsTrue("Pack", "Hotel Room", "ROOM 1 NIGHT", "00:00", "01:00", "100");
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        //Thread.sleep(2000);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test17", date.generateTodayDate(), date.generateDate_plus(0, 5), "4", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Pack f", "4",date.generateTodayDate(), date.generateDate_plus(0, 5), "100");
        quoteMeetingPackages.clickEdit();
        quoteMeetingPackages.changeDiscount("11");
        Assert.assertEquals(quoteMeetingPackages.readErrorMessage2(driver), expectedMessage);
        quoteMeetingPackages.closeWindow();
    }

    @Test(priority = 29, description = "Quote_Product__c.VR08_Start_End_Date")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR08_Start_End_Date")
    @Story("Add Quote Product to MYCE Quote: thn__Start_Date_Time__c >= thn__End_Date_Time__c")
    public void testCreateQuoteProduct1() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Start Date time cannot be posterior to End Date time";
        //when
        String text = "MYCE Quotes";
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2("Test18", date.generateDate_plus(0, 0),
                date.generateDate_plus(0, 4), "10", "Demo");
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "5", date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 0));
        //then
        Assert.assertEquals(quoteProducts.readErrorMessage3(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 30, description = "Quote_Product__c.VR11_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR11_Dates_within_Quote_dates")
    @Story("")
    public void testCreateQuoteProduct2() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Start and end date of product must be within Quote arrival and departure dates";
        //when
        String text = "MYCE Quotes";
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2("Test19", date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 4), "10", "Demo");
        //thn__Start_Date_Time__c < thn__MYCE_Quote__r.thn__Arrival_Date__c
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "5", date.generateDate_plus(0, 0),
                date.generateDate_plus(0, 4));
        //then
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
        //thn__Start_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "5", date.generateDate_plus(0, 5),
                date.generateDate_plus(0, 4));
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        //thn__End_Date_Time__c < thn__MYCE_Quote__r.thn__Arrival_Date__c
        quoteProducts.closeWindow();
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "5",  date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 0));
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
        //thn__End_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "5",  date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 5));
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 31, description = "Quote_Product__c.VR17_Pax")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR17_Pax")
    @Story("Add Quote product to MYCE Quote: thn__Pax__c > thn__MYCE_Quote__r.thn__Pax__c")
    public void testCreateQuoteProduct3() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Pax cannot be greater than quote's pax";
        //when
        String text = "MYCE Quotes";
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2("Test20", date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 4), "10", "Demo");
        myceQuotes.openProducts();
        quoteProducts.createProduct("WINES", "11", date.generateDate_plus(0, 1),
                date.generateDate_plus(0, 4));
        //then
        Assert.assertEquals(quoteProducts.readHelpErrorMessage(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 32, description = "Quote_Product__c.VR23_ServiceArea_date")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR23_ServiceArea_date")
    @Story("Add Meeting room to the Myce Quote, Add Quote product to the Quote: select Meeting Room while creating" +
            " Quote product, thn__Start_Date_Time__c != thn__Service_Area__r.thn__Start_Date_Time__c")
    public void testCreateQuoteProduct4() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Date of the service area must be the same as the product's";
        //when
        //homePageForScratchOrg.openAppLauncher(driver);
        //homePageForScratchOrg.sendTextInAppWindow(driver, text);
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test21", date.generateDate_plus(0, 1),
                        date.generateDate_plus(0, 4), "10", "Demo");
        Thread.sleep(2000);
        myceQuotes.openMeetingRooms(driver);
        quoteMeetingRoom.createMeetingRoom2("MEETING HALF DAY");
        quoteMeetingRoom.clickQuoteName("Test21");
        myceQuotes.openProducts();
        quoteProducts.createProduct2("DEFAULT - MEETING HALF DAY", "DINER",
                date.generateDate_plus(0, 2), date.generateDate_plus(0, 4));
        //then
        Assert.assertEquals(quoteProducts.readHelpErrorMessage(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 33, description = "Quote_Product__c.VR26_PackageDate")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR26_PackageDate")
    @Story("Add Package having products to the Quote, Open Quote product record, Change dates:" +
            " thn__Start_Date_Time__c != thn__Start_Date__c, thn__End_Date_Time__c !=thn__End_Date__c")
    public void testCreateQuoteProduct5() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Date cannot be changed if Product is part of package";
        //when
        String text = "MYCE Quotes";
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test22", date.generateDate_plus(0, 1),
                        date.generateDate_plus(0, 4), "10", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Pack d", "10", date.generateDate_plus(0, 2),
                date.generateDate_plus(0, 2), "20");
        quoteMeetingPackages.clickQuoteName("Test22");
        myceQuotes.openProducts();
        myceQuotes.openProduct("DINER");
        quoteProducts.changeDate(date.generateDate_plus(0, 3), date.generateDate_plus(0, 4));
        //then
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 34, description = "Quote_Product__c.VR36_Consumption_on_Package_Line")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR36_Consumption_on_Package_Line")
    @Story("Add Package having products to the Quote, Open Quote product record, Set On_Consumption__c to TRUE")
    public void testCreateQuoteProduct6() throws InterruptedException, IOException {
        //given
        String expectedMessage = "In a package line quote product the on consumption option can not be used.";
        //when
        String text = "MYCE Quotes";
        myceQuotes.goToMyceQuotes();
        myceQuotes.createNewMyceQuote(driver);
        myceQuotes.createMyceQuote_happyPath2
                ("Test23", date.generateDate_plus(0, 1),
                        date.generateDate_plus(0, 4), "10", "Demo");
        myceQuotes.openMeetingPackages(driver);
        quoteMeetingPackages.createMeetingPackages("Pack d", "10", date.generateDate_plus(0, 2),
                date.generateDate_plus(0, 2), "20");
        quoteMeetingPackages.clickQuoteName("Test23");
        myceQuotes.openProducts();
        myceQuotes.openProduct("DINER");
        quoteProducts.changeOnConsumption();
        //then
        Assert.assertEquals(quoteProducts.readErrorMessage2(), expectedMessage);
        quoteProducts.closeWindow();
    }

    @Test(priority = 35, description = "Guest__c.VR01_guest_send_to_mews")
    @Severity(SeverityLevel.NORMAL)
    @Description("Guest__c.VR01_guest_send_to_mews")
    @Story("Create Guest__c record, do not fill Hotel__c, Set Send_to_Mews__c to TRUE")
    public void testCreateGuest() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Hotel is required to create/update guest in Mews";
        //when
        String text = "Guests";
        homePageForScratchOrg.openAppLauncher(driver);
        homePageForScratchOrg.sendTextInAppWindow(driver, text);
        guests.clickNew();
        guests.createGuest("John");
        //then
        Assert.assertEquals(guests.readHelpErrorMessage(), expectedMessage);
        guests.closeWindow();
    }

    @Test(priority = 36, description = "Item__c.VR02_item_send_to_mews")
    @Severity(SeverityLevel.NORMAL)
    @Description("Item__c.VR02_item_send_to_mews")
    @Story("On Item record where thn__Mews_Id__c != null, set thn__Send_to_Mews__c to TRUE")
    public void testCreateItem() throws InterruptedException, IOException {
        //given
        String expectedMessage = "The Reservation product already exists and cannot be sent twice";
        //when
        items.goToItems();
        items.clickNew();
        items.createItem("MEETING HALF DAY", "555");
        //then
        Assert.assertEquals(items.readHelpErrorMessage(), expectedMessage);
        items.closeWindow();
    }

    @Test(priority = 37, description = "Reservation__c.VR03_Reason_update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Reservation__c.VR03_Reason_update")
    @Story("On thn__Reservation__c record set thn__Update_Price__c to TRUE, Leave thn__Reason_update__c empty")
    public void testCreateReservation() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Reason update is required when price is updated";
        //when
        reservations.goToReservations();
        reservations.clickNew();
        reservations.createReservation("Demo", "ORDER", date.generateDate_plus(0,0),
                date.generateDate_plus(0, 5), "2", "1", "Double",
                "Rate pricing", "DEFAULT");
        //then
        Assert.assertEquals(reservations.readHelpErrorMessage(), expectedMessage);
        reservations.closeWindow();
    }

    @Test(priority = 38, description = "Reservation__c.VR04_Cancellation_reason")
    @Severity(SeverityLevel.NORMAL)
    @Description("Reservation__c.VR04_Cancellation_reason")
    @Story("On thn__Reservation__c record where thn__Mews_Id__c != null change thn__State__c to “Canceled")
    public void testCreateReservation2() throws InterruptedException, IOException {
        //given
        String expectedMessage = "Notes cannot be empty if state is canceled";
        //when
        reservations.goToReservations();
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
