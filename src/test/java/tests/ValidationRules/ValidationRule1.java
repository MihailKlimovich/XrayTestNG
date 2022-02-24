package tests.ValidationRules;

import com.google.gson.JsonObject;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;
import tests.BaseTest;
import utils.Listeners.TestListener;

import java.io.IOException;
import java.util.List;

@Listeners({TestListener.class})

public class ValidationRule1 extends BaseTest{


    @Test(priority = 1, description="Setting up validation rules: Setup.thn__ByPass__c.thn__ByPassVR__c == false" +
            " and User.thn__ByPassVR__c == false ")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-510: Validation rule updated")
    public void settingUpValidationRules() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DataForVRTesting.apex");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "User",
                "-w",
                "Username='" + ORG_USERNAME + "'",
                "-v",
                "thn__ByPassVR__c=false",
                "-u",
                ORG_USERNAME,
                "--json"});
        Object byPass = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__bypass__c",
                "-u",
                ORG_USERNAME,
                "--json"});
        List<String> byPassID= JsonParser2.getFieldValueSoql(byPass.toString(), "Id");
        String byPassId = byPassID.get(0);
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__bypass__c",
                "-w",
                "Id='" + byPassId + "'",
                "-v",
                "thn__bypassvr__c=false",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder userRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "User",
                "-w",
                "Username='" + ORG_USERNAME + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String userByPass = JsonParser2.getFieldValue(userRecord.toString(), "thn__ByPassVR__c");
        StringBuilder byPassRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__bypass__c",
                "-w",
                "Id='" + byPassId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(byPassRecord);
        String byPassVr = JsonParser2.getFieldValue(byPassRecord.toString(), "thn__ByPassVR__c");
        Assert.assertEquals(userByPass, "false");
        Assert.assertEquals(byPassVr, "false");
    }


    @Test(priority = 2, description = "Myce_Quote__c.Commission_Validation_Rule: Commissionable == true &" +
            " thn__Commission_to__c == null ")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-510: Validation rule updated")
    public void testCreateNewMyceQuote1() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test1'", ORG_USERNAME);
        String expectedMessage = "If commissionable = true, 'Commission to' field shouldn't be null or \n" +
                "if  'Commission to' field equals 'agent', agent shouldn't be null or\n" +
                "if  'Commission to' field equals 'company', company shouldn't be null";
        StringBuilder result =SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test1' thn__Commissionable__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(result);
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);

    }

    @Test(priority = 3, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c != Agent & thn__Agent__c == null")
    public void testCreateNewMyceQuote2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test2'", ORG_USERNAME);
        String expectedMessage = "If commissionable = true, 'Commission to' field shouldn't be null or \n" +
                "if  'Commission to' field equals 'agent', agent shouldn't be null or\n" +
                "if  'Commission to' field equals 'company', company shouldn't be null";
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test2' thn__Commissionable__c=true thn__Commission_to__c='Agent'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 4, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c == Company & thn__Company__c == null")
    public void testCreateNewMyceQuote3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test3'", ORG_USERNAME);
        String expectedMessage = "If commissionable = true, 'Commission to' field shouldn't be null or \n" +
                "if  'Commission to' field equals 'agent', agent shouldn't be null or\n" +
                "if  'Commission to' field equals 'company', company shouldn't be null";
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test3' thn__Commissionable__c=true thn__Commission_to__c='Company'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 5, description = "Myce_Quote__c.VR05_Dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR05_Dates")
    @Story("thn__Departure_Date__c < thn__Arrival_Date__c")
    public void testCreateNewMyceQuote4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test4'", ORG_USERNAME);
        String expectedMessage = "Departure Date cannot be anterior to Arrival Date";
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test4' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_minus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 6, description = "Myce_Quote__c.VR27_Company_Agent_Type")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR27_Company_Agent_Type")
    @Story("Create MYCE Quote: Select Company for Agent field ,Select Agent for Company field")
    public void testCreateNewMyceQuote5() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test5'", ORG_USERNAME);
        accounts.deleteAccountSFDX(SFDX, "Name='Test Agent'", ORG_USERNAME);
        accounts.deleteAccountSFDX(SFDX, "Name='Test Company'", ORG_USERNAME);
        String accountAgentId = accounts.createAccountSFDX(SFDX, "Name='Test Agent' thn__Type__c='Agent'",
                ORG_USERNAME);
        String accountCompanyId = accounts.createAccountSFDX(SFDX, "Name='Test Company'" +
                " thn__Type__c='Company'", ORG_USERNAME);
        String expectedMessage = "Company cannot be of type 'Agent'  and Agent must be of type 'Agent' or 'Leads'";
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test5' thn__Company__c='" + accountAgentId + "' thn__Agent__c='" + accountCompanyId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 7, description = "Myce_Quote__c.VR13_Reservation_Guest")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR13_Reservation_Guest")
    @Story("Create MYCE Quote: leave thn__Reservation_Guest__c empty, Set thn__Send_to_Mews__c to TRUE")
    public void testCreateNewMyceQuote6() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test6'", ORG_USERNAME);
        String expectedMessage = "Reservation guest is required to send reservations to Mews";
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test6' thn__SendToMews__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 8, description = "Myce_Quote__c.VR22_ClosedStatus")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR22_ClosedStatus")
    @Story("Change Stage om MYCE Quote to '4 - Closed'")
    public void testCreateNewMyceQuote7() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test7'", ORG_USERNAME);
        String expectedMessage = "Closed Status is required when quote is at stage '4 - Closed'";
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test7' thn__Stage__c='4 - Closed'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 9, description = "Myce_Quote__c.VR28_Cancelled_Status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR28_Cancelled_Status")
    @Story("Set thn__Is_Confirmed__c to false, Change MYCE Quote Closed Status to ‘Cancelled’")
    public void testCreateNewMyceQuote8() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test8'", ORG_USERNAME);
        String expectedMessage = "Closed Status can be 'Cancelled' only if Myce quote was 'Won'";
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='Test8' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2), ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Is_Confirmed__c=false thn__Closed_Status__c='Lost'", ORG_USERNAME );
        StringBuilder result = myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'",
                "thn__Closed_Status__c='Cancelled'", ORG_USERNAME);
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 10, description = "Credit_Note_Line__c.Invoice_Line_Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Credit_Note_Line__c.Invoice_Line_Validation")
    @Story("Create Credit Note Line record: thn__Invoice_Line__c == null & thn__Amount__c == null & thn__Quantity__c == null")
    public void testCreateNewCreditNoteLine() throws InterruptedException, IOException {
        String expectedMessage = "When a credit note line isn't linked to an Invoice line then the Amount and VAT is required.";
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Credit_Note__c",
                "-v",
                "thn__Status__c='Draft'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String creditNoteID = JsonParser2.getFieldValue(res1.toString(), "id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Credit_Note_Line__c",
                "-v",
                "thn__Credit_Note__c='" + creditNoteID + "' thn__VAT_Category__c='1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 11, description = "Package_Line__c.VR30_IsMultidays")
    @Severity(SeverityLevel.NORMAL)
    @Description("Package_Line__c.VR30_IsMultidays")
    @Story("For Package where thn__Multi_Days__c == true, create Package line: thn__AppliedDay__c == null")
    public void testCreateNewPackageLine1() throws InterruptedException, IOException {
        packages.deletePackageSFDX(SFDX, "Name='Test Package 11", ORG_USERNAME);
        String expectedMessage = "Applied Day is required when a package is Multi days";
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test Package 11' thn__Multi_Days__c=true thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Test Package 11''",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(res2.toString(), "Id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line' thn__Package__c='" + packageID + "' thn__Type__c='Hotel Room' thn__Product__c='" + productID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 12, description = "Package_Line__c.VR31_IsNotMultidays")
    @Severity(SeverityLevel.NORMAL)
    @Description("Package_Line__c.VR31_IsNotMultidays")
    @Story("For Package where hn__Multi_Days__c == false, create Package line: thn__AppliedDay__c != null")
    public void testCreateNewPackageLine2() throws InterruptedException, IOException {
        packages.deletePackageSFDX(SFDX, "Name='Test Package 22", ORG_USERNAME);
        String expectedMessage = "Applied Day must be left empty when a package is not Multi days";
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test Package 22' thn__Multi_Days__c=false thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line 2' thn__Package__c='" + packageID + "' thn__AppliedDay__c=25 " +
                        "thn__Type__c='Hotel Room' thn__Product__c='" + productID + "' thn__Start_Time__c='12:00'" +
                        " thn__End_Time__c='15:00' thn__Unit_Price__c=25 thn__VAT_Category__c='1'" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 13, description = "Package_Line__c.VR29_Product_property")
    @Severity(SeverityLevel.NORMAL)
    @Description("Package_Line__c.VR29_Product_property")
    @Story("Create package line where Proferty  ‘A' for Package with Property 'B’")
    public void testCreateNewPackageLine3() throws InterruptedException, IOException {
        packages.deletePackageSFDX(SFDX, "Name='Test Package 33", ORG_USERNAME);
        //given
        String expectedMessage = "Product's property must be the same than the package's";
        //when
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Test'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test Package 33' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(res2.toString(), "Id");
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Test Package 33'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(res3.toString(), "Id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line 3' thn__Package__c='" + packageID + "' thn__Type__c='Hotel Room' thn__Product__c='" + productID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(result);
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 14, description = "Quote_Hotel_Room__c.VR06_Departure_after")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Hotel_Room__c.VR06_Departure_after")
    @Story("Add Quote hotel room on MYCE Quote: thn__Arrival_Date_Time__c > thn__Departure_Date_Time__c")
    public void testCreateQuoteHotelRoom() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test9'", ORG_USERNAME);
        String expectedMessage = "Arrival Date time cannot be after Departure Date time";
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test9' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                        " thn__Departure_Date__c=" + date.generateTodayDate2(),
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='Test9'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res2.toString(), "Id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "Name='Test Hotel Room 1' thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2() + "T19:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2() + "T10:00:00.000+0000",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 15, description = "Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Story("thn__Arrival_Date_Time__c < thn__MYCE_Quote__r.thn__Arrival_Date__c")
    public void testCreateQuoteHotelRoom2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test10'", ORG_USERNAME);
        String expectedMessage = "Arrival and Departure date of hotel room must be within Quote arrival and departure dates";
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 2 NIGHTS'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(res2.toString(), "Id");
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test10' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 2),
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='Test10'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res3.toString(), "Id");
        StringBuilder res4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(res4.toString(), "Id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID +
                        "' thn__Space_Area__c='" + roomTypeID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2() + "T10:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 2) + "T19:00:00.000+0000 thn__Pax__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 16, description = "Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Arrival_Date_Time__c >  thn__MYCE_Quote__r.thn__Departure_Date__c")
    @Story("thn__Arrival_Date_Time__c >  thn__MYCE_Quote__r.thn__Departure_Date__c")
    public void testCreateQuoteHotelRoom3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test11'", ORG_USERNAME);
        String expectedMessage = "Arrival Date time cannot be after Departure Date time";
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 2 NIGHTS'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(res2.toString(), "Id");
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test11' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='Test11'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res3.toString(), "Id");
        StringBuilder res4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(res4.toString(), "Id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID +
                        "' thn__Space_Area__c='" + roomTypeID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 4) + "T10:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 3) + "T19:00:00.000+0000 thn__Pax__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 17, description = "Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Departure_Date_Time__c <  thn__MYCE_Quote__r.thn__Arrival_Date__c")
    @Story("thn__Departure_Date_Time__c <  thn__MYCE_Quote__r.thn__Arrival_Date__c")
    public void testCreateQuoteHotelRoom4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test12'", ORG_USERNAME);
        String expectedMessage = "Arrival Date time cannot be after Departure Date time";
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 2 NIGHTS'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(res2.toString(), "Id");
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test12' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='Test12'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res3.toString(), "Id");
        StringBuilder res4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(res4.toString(), "Id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID +
                        "' thn__Space_Area__c='" + roomTypeID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 1) + "T10:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2() + "T19:00:00.000+0000 thn__Pax__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 18, description = "Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Departure_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c")
    @Story("thn__Departure_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c")
    public void testCreateQuoteHotelRoom5() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test13'", ORG_USERNAME);
        String expectedMessage = "Arrival and Departure date of hotel room must be within Quote arrival and departure dates";
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 2 NIGHTS'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(res2.toString(), "Id");
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test13' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='Test13'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res3.toString(), "Id");
        StringBuilder res4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(res4.toString(), "Id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID +
                        "' thn__Space_Area__c='" + roomTypeID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 1) + "T10:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 4) + "T19:00:00.000+0000 thn__Pax__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(result);
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 19, description = "Quote_Hotel_Room__c.VR15_Pax")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Hotel_Room__c.VR15_Pax")
    @Story("Add Quote hotel room on MYCE Quote: thn__Pax__c > thn__MYCE_Quote__r.thn__Pax__c")
    public void testCreateQuoteHotelRoom6() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test14'", ORG_USERNAME);
        String expectedMessage = "Pax cannot be greater than quote's pax";
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 2 NIGHTS'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(res2.toString(), "Id");
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test14' thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='Test14'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res3.toString(), "Id");
        StringBuilder res4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(res4.toString(), "Id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID +
                        "' thn__Space_Area__c='" + roomTypeID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 1) + "T10:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 3) + "T19:00:00.000+0000 thn__Pax__c=6",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 20, description = "Quote_Meetings_Room__c.VR19_SetupResource")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR19_SetupResource")
    @Story("")
    public void testCreateQuoteMeetingsRoom1() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test15'", ORG_USERNAME);
            String expectedMessage = "Meeting room's pax exceeds the resource's capacity for this setup";
            StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__Hotel__c",
                    "-w",
                    "thn__Unique_Id__c='Demo'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__Product__c",
                    "-w",
                    "Name='MEETING HALF DAY'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__Resource__c",
                    "-w",
                    "Name='TestRes'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
            String productID = JsonParser2.getFieldValue(res2.toString(), "Id");
            String resourceID = JsonParser2.getFieldValue(res3.toString(), "Id");
            SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__MYCE_Quote__c",
                    "-v",
                    "Name='Test15' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                            date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                            date.generateTodayDate2_plus(0, 3),
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            StringBuilder res4= SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:get",
                    "-s",
                    "thn__MYCE_Quote__c",
                    "-w",
                    "Name='Test15'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String myceQuoteID = JsonParser2.getFieldValue(res4.toString(), "Id");
            StringBuilder result1 =SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Meeting_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                            resourceID + "' thn__Pax__c=6 thn__Setup__c='Buffet'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String message1 = JsonParser2.getFieldValue2(result1.toString(), "message");
            Assert.assertEquals(message1, expectedMessage);
            StringBuilder result2 =SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Meeting_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                            resourceID + "' thn__Pax__c=6 thn__Setup__c='Cabaret'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String message2 = JsonParser2.getFieldValue2(result2.toString(), "message");
            Assert.assertEquals(message2, expectedMessage);
            StringBuilder result3 =SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Meeting_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                            resourceID + "' thn__Pax__c=6 thn__Setup__c='Circle'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String message3 = JsonParser2.getFieldValue2(result3.toString(), "message");
            Assert.assertEquals(message3, expectedMessage);
            StringBuilder result4 =SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Meeting_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                            resourceID + "' thn__Pax__c=6 thn__Setup__c='Classroom'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String message4 = JsonParser2.getFieldValue2(result4.toString(), "message");
            Assert.assertEquals(message4, expectedMessage);
            StringBuilder result5 =SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Meeting_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                            resourceID + "' thn__Pax__c=6 thn__Setup__c='Custom'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String message5 = JsonParser2.getFieldValue2(result5.toString(), "message");
            Assert.assertEquals(message5, expectedMessage);
            StringBuilder result6 =SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Meeting_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                            resourceID + "' thn__Pax__c=6 thn__Setup__c='Dinner'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String message6 = JsonParser2.getFieldValue2(result6.toString(), "message");
            Assert.assertEquals(message6, expectedMessage);
            StringBuilder result7 =SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Meeting_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                            resourceID + "' thn__Pax__c=6 thn__Setup__c='Party'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String message7 = JsonParser2.getFieldValue2(result7.toString(), "message");
            Assert.assertEquals(message7, expectedMessage);
            StringBuilder result8 =SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Meeting_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                            resourceID + "' thn__Pax__c=6 thn__Setup__c='Square'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String message8 = JsonParser2.getFieldValue2(result8.toString(), "message");
            Assert.assertEquals(message8, expectedMessage);
            StringBuilder result9 =SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Meeting_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                            resourceID + "' thn__Pax__c=6 thn__Setup__c='Theater'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String message9 = JsonParser2.getFieldValue2(result9.toString(), "message");
            Assert.assertEquals(message9, expectedMessage);
            StringBuilder result10 =SfdxCommand.runLinuxCommand1(new String[]{
                    SFDX,
                    "force:data:record:create",
                    "-s",
                    "thn__Quote_Meeting_Room__c",
                    "-v",
                    "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                            resourceID + "' thn__Pax__c=6 thn__Setup__c='U-Shape'",
                    "-u",
                    ORG_USERNAME,
                    "--json"});
            String message10 = JsonParser2.getFieldValue2(result10.toString(), "message");
            Assert.assertEquals(message10, expectedMessage);
    }

    @Test(priority = 21, description = "Quote_Meetings_Room__c.VR21_Lock_Resource")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR21_Lock_Resource")
    @Story("")
    public void testCreateQuoteMeetingsRoom2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test16'", ORG_USERNAME);
        String expectedMessage = "Resource cannot be changed when meeting room is locked";
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Resource__c",
                "-w",
                "Name='TestRes'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Resource__c",
                "-w",
                "Name='TestRes2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String resourceID2 = JsonParser2.getFieldValue(res.toString(), "Id");
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        String productID = JsonParser2.getFieldValue(res2.toString(), "Id");
        String resourceID = JsonParser2.getFieldValue(res3.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test16' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res4= SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='Test16'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res4.toString(), "Id");
        StringBuilder res5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "' thn__Resource__c='" +
                        resourceID + "' thn__Pax__c=2 thn__Setup__c='Buffet'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingRoomID = JsonParser2.getFieldValue(res5.toString(), "id");
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "id='" + meetingRoomID + "'",
                "-v",
                "thn__Lock_Resource__c=true thn__Resource__c=''",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(result.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 22, description = "Quote_Meetings_Room__c.VR25_PackageDate")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR25_PackageDate")
    @Story("Add meeting room to the package, Add package on MYCE Quote, Open Quote meeting room record," +
            " thn__Shadow__c == FALSE, Change thn__Start_Date_Time__c, thn__End_Date_Time__c")
    public void testCreateQuoteMeetingsRoom3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test17'", ORG_USERNAME);
        String expectedMessage = "Date cannot be changed if Meeting room is part of package";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder packageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Pack c'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test17' thn__Pax__c=4 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=4 thn__Start_Date__c=" + date.generateTodayDate2() + " thn__End_Date__c=" +
                        date.generateTodayDate2() + " thn__Unit_Price__c=30",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder meetingRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingRoomID = JsonParser2.getFieldValue(meetingRoomRecord.toString(), "Id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "id='" + meetingRoomID + "'",
                "-v",
                "thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 1) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 2),
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(updateResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 23, description = "Quote_Package__c.VR12_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR12_Dates_within_Quote_dates")
    @Story("")
    public void testCreateQuotePackage1() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test18'", ORG_USERNAME);
        String expectedMessage = "Start and end date of package must be within Quote arrival and departure dates";
        String expectedMessage2 = "Start Date of the package is after the Departure Date";
        String expectedMessage3 = "End Date of the package is after the Departure Date";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder packageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Pack c'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test18' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        //thn__Start_Date__c  < thn__MYCE_Quote__r.thn__Arrival_Date__c
        StringBuilder quotePackageResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2_minus(0, 1) + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Unit_Price__c=30",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(quotePackageResult1.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
        //thn__Start_Date__c> thn__MYCE_Quote__r.thn__Departure_Date__c
        StringBuilder quotePackageResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2_plus(0, 4) + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Unit_Price__c=30",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message2 = JsonParser2.getFieldValue2(quotePackageResult2.toString(), "message");
        Assert.assertEquals(message2, expectedMessage2);
        //thn__End_Date__c< thn__MYCE_Quote__r.thn__Arrival_Date__c
        StringBuilder quotePackageResult3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2() + " thn__End_Date__c=" +
                        date.generateTodayDate2_minus(0, 1) + " thn__Unit_Price__c=30",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message3 = JsonParser2.getFieldValue2(quotePackageResult3.toString(), "message");
        Assert.assertEquals(message3, expectedMessage);
        //thn__End_Date__c> thn__MYCE_Quote__r.thn__Departure_Date
        StringBuilder quotePackageResult4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2() + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 4) + " thn__Unit_Price__c=30",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message4 = JsonParser2.getFieldValue2(quotePackageResult4.toString(), "message");
        Assert.assertEquals(message4, expectedMessage3);
    }

    @Test(priority = 24, description = "Quote_Package__c.VR14_Discount")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR14_Discount")
    @Story("")
    public void testCreateQuotePackage2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test19'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='Test Package 44'", ORG_USERNAME);
        String expectedMessage = "No Discount possible, package is not configured correctly. Please contact your admin";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='DINER'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productDinerID = JsonParser2.getFieldValue(productRecord1.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test Package 44'  thn__Hotel__c='" + propertyID + "' thn__Discount_Max__c=15",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line 4' thn__Package__c='" + packageID + "' thn__Type__c='Food' thn__Product__c='" +
                        productDinerID + "' thn__Apply_Discount__c=true thn__Start_Time__c=12:00 thn__End_Time__c=15:00 " +
                        "thn__Unit_Price__c=20 thn__VAT_Category__c='1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line 5' thn__Package__c='" + packageID + "' thn__Type__c='Hotel Room' thn__Product__c='" +
                        productRoom1NightID + "' thn__Apply_Discount__c=true thn__Start_Time__c=12:00 thn__End_Time__c=15:00 " +
                        "thn__Unit_Price__c=20 thn__VAT_Category__c='1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test19' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=4 thn__Start_Date__c=" + date.generateTodayDate2() + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 0) + " thn__Unit_Price__c=30",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageID = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "id='" + quotePackageID + "'",
                "-v",
                "thn__Discount__c=70",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(updateResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 25, description = "Quote_Package__c.VR18_Pax")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR18_Pax")
    @Story("Add Quote package to the MYCE Quote: thn__Pax__c > thn__MYCE_Quote__r.thn__Pax__c")
    public void testCreateQuotePackage3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test20'", ORG_USERNAME);
        String expectedMessage = "Pax cannot be greater than quote's pax";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder  packageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Test Package 44'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test20' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=15 thn__Start_Date__c=" + date.generateTodayDate2() + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 0) + " thn__Unit_Price__c=30",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(quotePackageResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 26, description = "Quote_Package__c.VR33_QuotePackage_Account")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR33_QuotePackage_Account")
    @Story("")
    public void testCreateQuotePackage4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test21'", ORG_USERNAME);
        String expectedMessage = "This Package can only be instantiated on a quote which is related to its account";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder  packageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Pack a'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(packageRecord);
        String packageID = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test21' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2() + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 0) + " thn__Unit_Price__c=20",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageResult);
        String message = JsonParser2.getFieldValue2(quotePackageResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 27, description = "Quote_Package__c.VR34_QuotePackage_Dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR34_QuotePackage_Dates")
    @Story("")
    public void testCreateQuotePackage5() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test22'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='Test Package 55'", ORG_USERNAME);
        String expectedMessage = "Quote package start date must be within package's start and end dates";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test Package 55'  thn__Hotel__c='" + propertyID + "' thn__Start_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) +
                        " thn__End_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line 6' thn__Package__c='" + packageID + "' thn__Type__c='Hotel Room' thn__Product__c='" +
                        productRoom1NightID + "' thn__Apply_Discount__c=true thn__Start_Time__c=12:00 thn__End_Time__c=15:00 " +
                        "thn__Unit_Price__c=20 thn__VAT_Category__c='1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test22' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2() + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Unit_Price__c=20",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message1 = JsonParser2.getFieldValue2(quotePackageResult1.toString(), "message");
        Assert.assertEquals(message1, expectedMessage);
        StringBuilder quotePackageResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2_plus(0, 4) + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Unit_Price__c=20",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message2 = JsonParser2.getFieldValue2(quotePackageResult2.toString(), "message");
        Assert.assertEquals(message2, expectedMessage);
        StringBuilder quotePackageResult3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2_plus(0, 1) + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 4) + " thn__Unit_Price__c=20",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message3 = JsonParser2.getFieldValue2(quotePackageResult3.toString(), "message");
        Assert.assertEquals(message3, expectedMessage);
    }

    @Test(priority = 28, description = "Quote_Package__c.VR37_Max_Discount")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR37_Max_Discount")
    @Story("Add Quote Package to MYCE Quote: set Discount on quote package > Discount max on Package")
    public void testCreateQuotePackage6() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test23'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='Test Package 66'", ORG_USERNAME);
        String expectedMessage = "Discount on quote package cannot be greater than discount max.";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test Package 66'  thn__Discount_Max__c=10 thn__Hotel__c='" + propertyID + "' thn__Start_Date__c=" +
                        date.generateTodayDate2() +
                        " thn__End_Date__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line 7' thn__Package__c='" + packageID + "' thn__Type__c='Hotel Room' thn__Product__c='" +
                        productRoom1NightID + "' thn__Apply_Discount__c=true thn__Start_Time__c=12:00 thn__End_Time__c=15:00 " +
                        "thn__Unit_Price__c=100 thn__VAT_Category__c='1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test23' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2() + " thn__End_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Unit_Price__c=100",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageID = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder updateQuotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "id='" + quotePackageID + "'",
                "-v",
                "thn__Discount__c=11",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(updateQuotePackageResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 29, description = "Quote_Product__c.VR08_Start_End_Date")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR08_Start_End_Date")
    @Story("Add Quote Product to MYCE Quote: thn__Start_Date_Time__c >= thn__End_Date_Time__c")
    public void testCreateQuoteProduct1() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test24'", ORG_USERNAME);
        String expectedMessage = "Start Date time cannot be posterior to End Date time";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test24' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=10 thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 1) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2(),
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(quoteProductResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 30, description = "Quote_Product__c.VR11_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR11_Dates_within_Quote_dates")
    @Story("")
    public void testCreateQuoteProduct2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test25'", ORG_USERNAME);
        String expectedMessage1 = "Start and end date of product must be within Quote arrival and departure dates";
        String expectedMessage2 = "Start Date time cannot be posterior to End Date time";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test25' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        //thn__Start_Date_Time__c < thn__MYCE_Quote__r.thn__Arrival_Date__c
        StringBuilder quoteProductResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=10 thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 0) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String message1 = JsonParser2.getFieldValue2(quoteProductResult1.toString(), "message");
        Assert.assertEquals(message1, expectedMessage1);
        //thn__Start_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c
        StringBuilder quoteProductResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=10 thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 6) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String message2 = JsonParser2.getFieldValue2(quoteProductResult2.toString(), "message");
        Assert.assertEquals(message2, expectedMessage2);
        //thn__End_Date_Time__c < thn__MYCE_Quote__r.thn__Arrival_Date__c
        StringBuilder quoteProductResult3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=10 thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 1) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 0),
                "-u",
                ORG_USERNAME,
                "--json"});
        String message3 = JsonParser2.getFieldValue2(quoteProductResult3.toString(), "message");
        Assert.assertEquals(message3, expectedMessage2);
        //thn__End_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c
        StringBuilder quoteProductResult4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=10 thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 1) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 6),
                "-u",
                ORG_USERNAME,
                "--json"});
        String message4 = JsonParser2.getFieldValue2(quoteProductResult4.toString(), "message");
        Assert.assertEquals(message4, expectedMessage1);
    }

    @Test(priority = 31, description = "Quote_Product__c.VR17_Pax")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR17_Pax")
    @Story("Add Quote product to MYCE Quote: thn__Pax__c > thn__MYCE_Quote__r.thn__Pax__c")
    public void testCreateQuoteProduct3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test26'", ORG_USERNAME);
        String expectedMessage = "Pax cannot be greater than quote's pax";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test26' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=11 thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 1) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(quoteProductResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 32, description = "Quote_Product__c.VR23_ServiceArea_date")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR23_ServiceArea_date")
    @Story("Add Meeting room to the Myce Quote, Add Quote product to the Quote: select Meeting Room while creating" +
            " Quote product, thn__Start_Date_Time__c != thn__Service_Area__r.thn__Start_Date_Time__c")
    public void testCreateQuoteProduct4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test27'", ORG_USERNAME);
        String expectedMessage = "Date of the service area must be the same as the product's";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='DINER'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID1 = JsonParser2.getFieldValue(productRecord1.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID2 = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test27' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder meetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID2 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteMeetingRoomID = JsonParser2.getFieldValue(meetingRoomResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Service_Area__c='" + quoteMeetingRoomID + "' thn__Product__c='" + productID1 +
                        "' thn__Pax__c=10 thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 2) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(quoteProductResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 33, description = "Quote_Product__c.VR26_PackageDate")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR26_PackageDate")
    @Story("Add Package having products to the Quote, Open Quote product record, Change dates:" +
            " thn__Start_Date_Time__c != thn__Start_Date__c, thn__End_Date_Time__c !=thn__End_Date__c")
    public void testCreateQuoteProduct5() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test28'", ORG_USERNAME);
        String expectedMessage = "Date cannot be changed if Product is part of package";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder  packageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Pack d'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test28' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2() + " thn__End_Date__c=" +
                        date.generateTodayDate2() + " thn__Unit_Price__c=20",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageID = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder  quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductID = JsonParser2.getFieldValue(quoteProductRecord.toString(), "Id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "id='" + quoteProductID + "'",
                "-v",
                "thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 3) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 4),
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(updateResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }


    @Test(priority = 34, description = "Quote_Product__c.VR36_Consumption_on_Package_Line")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR36_Consumption_on_Package_Line")
    @Story("Add Package having products to the Quote, Open Quote product record, Set On_Consumption__c to TRUE")
    public void testCreateQuoteProduct6() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test29'", ORG_USERNAME);
        String expectedMessage = "In a package line quote product the on consumption option can not be used.";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder  packageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Pack d'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test29' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageID +
                        "' thn__Pax__c=10 thn__Start_Date__c=" + date.generateTodayDate2() + " thn__End_Date__c=" +
                        date.generateTodayDate2() + " thn__Unit_Price__c=20",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageID = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder  quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductID = JsonParser2.getFieldValue(quoteProductRecord.toString(), "Id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "id='" + quoteProductID + "'",
                "-v",
                "thn__On_Consumption__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(updateResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 35, description = "Guest__c.VR01_guest_send_to_mews")
    @Severity(SeverityLevel.NORMAL)
    @Description("Guest__c.VR01_guest_send_to_mews")
    @Story("Create Guest__c record, do not fill Hotel__c, Set Send_to_Mews__c to TRUE")
    public void testCreateGuest() throws InterruptedException, IOException {
        guests.deleteGuestSFDX(SFDX, "thn__FirstName__c='JohnAutoTest'", ORG_USERNAME);
        String expectedMessage = "Hotel is required to create/update guest in Mews";
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='JohnAutoTest' thn__Send_to_Mews__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(guestResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 36, description = "Item__c.VR02_item_send_to_mews")
    @Severity(SeverityLevel.NORMAL)
    @Description("Item__c.VR02_item_send_to_mews")
    @Story("On Item record where thn__Mews_Id__c != null, set thn__Send_to_Mews__c to TRUE")
    public void testCreateItem() throws InterruptedException, IOException {
        guests.deleteGuestSFDX(SFDX, "thn__FirstName__c='TestVRGuest1'", ORG_USERNAME);
        items.deleteItemSFDX(SFDX, "thn__Mews_Id__c=555", ORG_USERNAME);
        String expectedMessage = "The Reservation product already exists and cannot be sent twice";
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Rate__c",
                "-w",
                "Name='TestRateAuto1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder mewsServiceRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Mews_Service__c",
                "-w",
                "Name='Stay'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String serviceID = JsonParser2.getFieldValue(mewsServiceRecord.toString(), "Id");
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder rateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Rate__c",
                "-v",
                "Name='TestRateAuto1' thn__IsActive__c=true thn__IsPublic__c=true thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(rateResult);
        String rateID = JsonParser2.getFieldValue(rateResult.toString(), "id");
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='TestVRGuest1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        StringBuilder reservationResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Reservation__c",
                "-v",
                "thn__Hotel__c='" + propertyID + "' thn__Mews_Service__c=' " + serviceID +
                        "' thn__Customer__c='" + guestID + "' thn__StartUtc__c=" + date.generateTodayDate2() +
                        " thn__EndUtc__c=" + date.generateTodayDate2_plus(0, 5) +
                        " thn__AdultCount__c=2 thn__ChildCount__c=1 thn__RequestedCategory__c='" + roomTypeID +
                        "' thn__Pricing_Type__c='Rate pricing' thn__Rate__c='" + rateID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String reservationID = JsonParser2.getFieldValue(reservationResult.toString(), "id");
        StringBuilder itemResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Item__c",
                "-v",
                "thn__Reservation__c='" + reservationID + "' thn__Product__c='" + productRoom1NightID +
                        "' thn__Mews_Id__c=555 thn__Send_to_Mews__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(itemResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 37, description = "Reservation__c.VR03_Reason_update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Reservation__c.VR03_Reason_update")
    @Story("On thn__Reservation__c record set thn__Update_Price__c to TRUE, Leave thn__Reason_update__c empty")
    public void testCreateReservation() throws InterruptedException, IOException {
        guests.deleteGuestSFDX(SFDX, "thn__FirstName__c='TestVRGuest2'", ORG_USERNAME);
        String expectedMessage = "Reason update is required when price is updated";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder mewsServiceRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Mews_Service__c",
                "-w",
                "Name='Stay'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String serviceID = JsonParser2.getFieldValue(mewsServiceRecord.toString(), "Id");
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder rateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Rate__c",
                "-w",
                "Name='BAR'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String rateID = JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='TestVRGuest2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        StringBuilder reservationResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Reservation__c",
                "-v",
                "thn__Hotel__c='" + propertyID + "' thn__Mews_Service__c=' " + serviceID +
                        "' thn__Customer__c='" + guestID + "' thn__StartUtc__c=" + date.generateTodayDate2() +
                        " thn__EndUtc__c=" + date.generateTodayDate2_plus(0, 5) +
                        " thn__AdultCount__c=2 thn__ChildCount__c=1 thn__RequestedCategory__c='" + roomTypeID +
                        "' thn__Pricing_Type__c='Rate pricing' thn__Rate__c='" + rateID + "' thn__Update_Price__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(reservationResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 38, description = "Reservation__c.VR04_Cancellation_reason")
    @Severity(SeverityLevel.NORMAL)
    @Description("Reservation__c.VR04_Cancellation_reason")
    @Story("On thn__Reservation__c record where thn__Mews_Id__c != null change thn__State__c to “Canceled")
    public void testCreateReservation2() throws InterruptedException, IOException {
        guests.deleteGuestSFDX(SFDX, "thn__FirstName__c='TestVRGuest3'", ORG_USERNAME);
        reservations.deleteReservationSFDX(SFDX, "thn__Mews_Id__c=123", ORG_USERNAME);
        String expectedMessage = "Notes cannot be empty if state is canceled";
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder mewsServiceRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Mews_Service__c",
                "-w",
                "Name='Stay'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String serviceID = JsonParser2.getFieldValue(mewsServiceRecord.toString(), "Id");
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder rateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Rate__c",
                "-w",
                "Name='BAR'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String rateID = JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='TestVRGuest3'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        StringBuilder reservationResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Reservation__c",
                "-v",
                "thn__Hotel__c='" + propertyID + "' thn__Mews_Service__c=' " + serviceID +
                        "' thn__Customer__c='" + guestID + "' thn__StartUtc__c=" + date.generateTodayDate2() +
                        " thn__EndUtc__c=" + date.generateTodayDate2_plus(0, 5) +
                        " thn__AdultCount__c=2 thn__ChildCount__c=1 thn__RequestedCategory__c='" + roomTypeID +
                        "' thn__Pricing_Type__c='Rate pricing' thn__Rate__c='" + rateID + "' thn__Mews_Id__c='123'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(reservationResult);
        String reservationID = JsonParser2.getFieldValue(reservationResult.toString(), "id");
        StringBuilder updateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Reservation__c",
                "-w",
                "id='" +  reservationID + "'",
                "-v",
                "thn__State__c='Canceled'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(updateResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 39, description = "VR38_Resource_Grouping")
    @Severity(SeverityLevel.NORMAL)
    @Description("VR38_Resource_Grouping")
    @Story("Create Resource Grouping record, try to add Resources with different Properties")
    public void testCreateResourceGrouping() throws InterruptedException, IOException {
        String expectedMessage = "Property of both resources must be the same";
        StringBuilder resourceRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Resource__c",
                "-w",
                "Name='TestRes'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String resourceID1 = JsonParser2.getFieldValue(resourceRecord1.toString(), "Id");
        StringBuilder resourceRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Resource__c",
                "-w",
                "Name='TestRes2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String resourceID2 = JsonParser2.getFieldValue(resourceRecord2.toString(), "Id");
        StringBuilder resourceGroupingResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Resource_Grouping__c",
                "-v",
                "thn__Grouped_Resource__c='" + resourceID1 + "' thn__Resource_Group__c='" + resourceID2 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String message = JsonParser2.getFieldValue2(resourceGroupingResult.toString(), "message");
        Assert.assertEquals(message, expectedMessage);
    }
}
