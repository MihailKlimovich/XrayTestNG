package tests.ValidationRules;

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

public class ValidationRule2 extends BaseTest {

    @Test(priority = 1, description="Setting up validation rules")
    @Severity(SeverityLevel.NORMAL)
    @Description("Setup.thn__ByPass__c.thn__ByPassVR__c == true and User.thn__ByPassVR__c == false")
    @Story("Settings")
    public void settingUpValidationRules() throws InterruptedException, IOException {
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:auth:jwt:grant",
                "--clientid",
                CONSUMER_KEY,
                "--jwtkeyfile",
                SERVER_KEY_PATH,
                "--username",
                ORG_USERNAME,
                "--instanceurl",
                ORG_URL
        });
        System.out.println(authorise);
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "User",
                "-w",
                "Name='User User'",
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
                "thn__bypassvr__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder userRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "User",
                "-w",
                "Name='User User'",
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
        Assert.assertEquals(byPassVr, "true");
    }

    @Test(priority = 2, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c == null")
    public void testCreateNewMyceQuote1() throws InterruptedException, IOException {
        //force:data:soql:query -q "SELECT thn__Commissionable__c, thn__Commission_to__c FROM thn__MYCE_Quote__c where Name='Test24'" -u THYNK-VR --json
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test30' thn__Commissionable__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test30",
                "-u",
                ORG_USERNAME,
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String commissionable = JsonParser2.getFieldValue(res.toString(), "thn__Commissionable__c");
        String commission_to =  JsonParser2.getFieldValue(res.toString(), "thn__Commission_to__c");
        Assert.assertEquals(name, "Test30");
        Assert.assertEquals(commissionable, "true");
        Assert.assertEquals(commission_to, null);

    }

    @Test(priority = 3, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c != Agent & thn__Agent__c == null")
    public void testCreateNewMyceQuote2() throws InterruptedException, IOException {

        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test31' thn__Commissionable__c=true thn__Commission_to__c='Agent'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test31",
                "-u",
                ORG_USERNAME,
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String commissionable = JsonParser2.getFieldValue(res.toString(), "thn__Commissionable__c");
        String commission_to =  JsonParser2.getFieldValue(res.toString(), "thn__Commission_to__c");
        String agent =  JsonParser2.getFieldValue(res.toString(), "thn__Agent__c");
        Assert.assertEquals(name, "Test31");
        Assert.assertEquals(commissionable, "true");
        Assert.assertEquals(commission_to, "Agent");
        Assert.assertEquals(agent, null);
    }

    @Test(priority = 4, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c == Company & thn__Company__c == null")
    public void testCreateNewMyceQuote3() throws InterruptedException, IOException {

        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test32' thn__Commissionable__c=true thn__Commission_to__c='Company'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test32",
                "-u",
                ORG_USERNAME,
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String commissionable = JsonParser2.getFieldValue(res.toString(), "thn__Commissionable__c");
        String commission_to =  JsonParser2.getFieldValue(res.toString(), "thn__Commission_to__c");
        String company =  JsonParser2.getFieldValue(res.toString(), "thn__Company__c");
        Assert.assertEquals(name, "Test32");
        Assert.assertEquals(commissionable, "true");
        Assert.assertEquals(commission_to, "Company");
        Assert.assertEquals(company, null);
    }

    @Test(priority = 5, description = "Myce_Quote__c.VR05_Dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR05_Dates")
    @Story("thn__Departure_Date__c < thn__Arrival_Date__c")
    public void testCreateNewMyceQuote4() throws InterruptedException, IOException {

        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test33' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_minus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test33",
                "-u",
                ORG_USERNAME,
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String arrivalDay = JsonParser2.getFieldValue(res.toString(), "thn__Arrival_Date__c");
        String departureDay =  JsonParser2.getFieldValue(res.toString(), "thn__Departure_Date__c");
        Assert.assertEquals(name, "Test33");
        Assert.assertEquals(arrivalDay, date.generateTodayDate2());
        Assert.assertEquals(departureDay, date.generateTodayDate2_minus(0, 5));
    }

    @Test(priority = 6, description = "Myce_Quote__c.VR27_Company_Agent_Type")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR27_Company_Agent_Type")
    @Story("Create MYCE Quote: Select Company for Agent field ,Select Agent for Company field")
    public void testCreateNewMyceQuote5() throws InterruptedException, IOException {

        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "Account",
                "-w",
                "Name='Test Company'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String testCompanyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "Account",
                "-w",
                "Name='Test Agent'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String testAgentID = JsonParser2.getFieldValue(res2.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test34' thn__Company__c='" + testAgentID + "' thn__Agent__c='" + testCompanyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test34",
                "-u",
                ORG_USERNAME,
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String company = JsonParser2.getFieldValue(res.toString(), "thn__Company__c");
        String agent =  JsonParser2.getFieldValue(res.toString(), "thn__Agent__c");
        Assert.assertEquals(name, "Test34");
        Assert.assertEquals(company, testAgentID);
        Assert.assertEquals(agent, testCompanyID);
    }

    @Test(priority = 7, description = "Myce_Quote__c.VR13_Reservation_Guest")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR13_Reservation_Guest")
    @Story("Create MYCE Quote: leave thn__Reservation_Guest__c empty, Set thn__Send_to_Mews__c to TRUE")
    public void testCreateNewMyceQuote6() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test35' thn__SendToMews__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test35",
                "-u",
                ORG_USERNAME,
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String sendToMews = JsonParser2.getFieldValue(res.toString(), "thn__SendToMews__c");
        String reservationGuest =  JsonParser2.getFieldValue(res.toString(), "thn__Reservation_Guest__c");
        Assert.assertEquals(name, "Test35");
        Assert.assertEquals(sendToMews, "true");
        Assert.assertEquals(reservationGuest, null);
    }

    @Test(priority = 8, description = "Myce_Quote__c.VR22_ClosedStatus")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR22_ClosedStatus")
    @Story("Change Stage om MYCE Quote to '4 - Closed'")
    public void testCreateNewMyceQuote7() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test36' thn__Stage__c='4 - Closed'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test36",
                "-u",
                ORG_USERNAME,
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String stageStatus = JsonParser2.getFieldValue(res.toString(), "thn__Stage__c");
        Assert.assertEquals(name, "Test36");
        Assert.assertEquals(stageStatus, "4 - Closed");
    }

    @Test(priority = 9, description = "Myce_Quote__c.VR28_Cancelled_Status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR28_Cancelled_Status")
    @Story("Set thn__Is_Confirmed__c to false, Change MYCE Quote Closed Status to ‘Cancelled’")
    public void testCreateNewMyceQuote8() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test37' thn__Closed_Status__c='Cancelled'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test37",
                "-u",
                ORG_USERNAME,
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String closedStatus = JsonParser2.getFieldValue(res.toString(), "thn__Closed_Status__c");
        String isConfirmed = JsonParser2.getFieldValue(res.toString(), "thn__Is_Confirmed__c");
        Assert.assertEquals(name, "Test37");
        Assert.assertEquals(closedStatus, "Cancelled");
        Assert.assertEquals(isConfirmed, "false");
    }

    /*@Test(priority = 10, description = "Credit_Note_Line__c.Invoice_Line_Validation")
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
    }*/

    @Test(priority = 11, description = "Package_Line__c.VR30_IsMultidays")
    @Severity(SeverityLevel.NORMAL)
    @Description("Package_Line__c.VR30_IsMultidays")
    @Story("For Package where thn__Multi_Days__c == true, create Package line: thn__AppliedDay__c == null")
    public void testCreateNewPackageLine1() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test Package' thn__Multi_Days__c=true thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Test Package''",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(res2.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line' thn__Package__c='" + packageID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package_Line__c",
                "-w",
                "Name='Test Pack Line'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String namePackage = JsonParser2.getFieldValue(res2.toString(), "Name");
        String multiDays = JsonParser2.getFieldValue(res2.toString(), "thn__Multi_Days__c");
        String namePackageLine = JsonParser2.getFieldValue(res.toString(), "Name");
        String appliedDay = JsonParser2.getFieldValue(res.toString(), "thn__AppliedDay__c");
        Assert.assertEquals(namePackageLine, "Test Pack Line");
        Assert.assertEquals(appliedDay, null);
        Assert.assertEquals(namePackage, "Test Package");
        Assert.assertEquals(multiDays, "true");
    }

    @Test(priority = 12, description = "Package_Line__c.VR31_IsNotMultidays")
    @Severity(SeverityLevel.NORMAL)
    @Description("Package_Line__c.VR31_IsNotMultidays")
    @Story("For Package where hn__Multi_Days__c == false, create Package line: thn__AppliedDay__c != null")
    public void testCreateNewPackageLine2() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test Package 2' thn__Multi_Days__c=false thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Test Package 2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(res2.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line 2' thn__Package__c='" + packageID + "' thn__AppliedDay__c=25",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package_Line__c",
                "-w",
                "Name='Test Pack Line 2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String namePackage = JsonParser2.getFieldValue(res2.toString(), "Name");
        String multiDays = JsonParser2.getFieldValue(res2.toString(), "thn__Multi_Days__c");
        String namePackageLine = JsonParser2.getFieldValue(res.toString(), "Name");
        String appliedDay = JsonParser2.getFieldValue(res.toString(), "thn__AppliedDay__c");
        Assert.assertEquals(namePackageLine, "Test Pack Line 2");
        Assert.assertEquals(appliedDay, "25");
        Assert.assertEquals(namePackage, "Test Package 2");
        Assert.assertEquals(multiDays, "false");
    }

    @Test(priority = 13, description = "Package_Line__c.VR29_Product_property")
    @Severity(SeverityLevel.NORMAL)
    @Description("Package_Line__c.VR29_Product_property")
    @Story("Create package line where Proferty  ‘A' for Package with Property 'B’")
    public void testCreateNewPackageLine3() throws InterruptedException, IOException {
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
                "Name='Test Package 3' thn__Hotel__c='" + propertyID + "'",
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
                "Name='Test Package 3'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(res3.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line 3' thn__Package__c='" + packageID + "' thn__Type__c='Hotel Room' thn__Product__c='" + productID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package_Line__c",
                "-w",
                "Name='Test Pack Line 3'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String namePackage = JsonParser2.getFieldValue(res3.toString(), "Name");
        String property = JsonParser2.getFieldValue(res3.toString(), "thn__Hotel__c");
        String namePackageLine = JsonParser2.getFieldValue(res.toString(), "Name");
        String product = JsonParser2.getFieldValue(res.toString(), "thn__Product__c");
        Assert.assertEquals(namePackageLine, "Test Pack Line 3");
        Assert.assertEquals(product, productID);
        Assert.assertEquals(namePackage, "Test Package 3");
        Assert.assertEquals(property, propertyID);
    }

    @Test(priority = 14, description = "Quote_Hotel_Room__c.VR06_Departure_after")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Hotel_Room__c.VR06_Departure_after")
    @Story("Add Quote hotel room on MYCE Quote: thn__Arrival_Date_Time__c > thn__Departure_Date_Time__c")
    public void testCreateQuoteHotelRoom() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test38' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
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
                "Name='Test38'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res2.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{
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
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Name='Test Hotel Room 1'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String nameHotelRoom = JsonParser2.getFieldValue(res3.toString(), "Name");
        String nameMyceQuote = JsonParser2.getFieldValue(res3.toString(), "thn__MYCE_Quote__c");
        String arrivalDateTime = JsonParser2.getFieldValue(res3.toString(), "thn__Arrival_Date_Time__c");
        String departureDateTime = JsonParser2.getFieldValue(res3.toString(), "thn__Departure_Date_Time__c");
        Assert.assertEquals(nameHotelRoom, "Test Hotel Room 1");
        Assert.assertEquals(nameMyceQuote, myceQuoteID);
        Assert.assertEquals(arrivalDateTime, date.generateTodayDate2() + "T19:00:00.000+0000");
        Assert.assertEquals(departureDateTime, date.generateTodayDate2() + "T10:00:00.000+0000");
    }

    @Test(priority = 15, description = "Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Story("thn__Arrival_Date_Time__c < thn__MYCE_Quote__r.thn__Arrival_Date__c")
    public void testCreateQuoteHotelRoom2() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test39' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
                "Name='Test39'",
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
        SfdxCommand.runLinuxCommand1(new String[]{
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
        StringBuilder res5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID,
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuote = JsonParser2.getFieldValue(res5.toString(), "thn__MYCE_Quote__c");
        String arrivalDateMyceQuote = JsonParser2.getFieldValue(res3.toString(), "thn__Arrival_Date__c");
        String departureDateMyceQuote = JsonParser2.getFieldValue(res3.toString(), "thn__Departure_Date__c");
        String arrivalDateTime = JsonParser2.getFieldValue(res5.toString(), "thn__Arrival_Date_Time__c");
        String departureDateTime = JsonParser2.getFieldValue(res5.toString(), "thn__Departure_Date_Time__c");
        Assert.assertEquals(myceQuote, myceQuoteID);
        Assert.assertEquals(arrivalDateMyceQuote, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(departureDateMyceQuote, date.generateTodayDate2_plus(0, 2));
        Assert.assertTrue(arrivalDateTime.contains(date.generateTodayDate2()));
        Assert.assertTrue(departureDateTime.contains(date.generateTodayDate2_plus(0, 2)));
    }

    @Test(priority = 16, description = "Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Arrival_Date_Time__c >  thn__MYCE_Quote__r.thn__Departure_Date__c")
    @Story("thn__Arrival_Date_Time__c >  thn__MYCE_Quote__r.thn__Departure_Date__c")
    public void testCreateQuoteHotelRoom3() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test40' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
                "Name='Test40'",
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
        SfdxCommand.runLinuxCommand1(new String[]{
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
        StringBuilder res5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID,
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuote = JsonParser2.getFieldValue(res5.toString(), "thn__MYCE_Quote__c");
        String arrivalDateMyceQuote = JsonParser2.getFieldValue(res3.toString(), "thn__Arrival_Date__c");
        String departureDateMyceQuote = JsonParser2.getFieldValue(res3.toString(), "thn__Departure_Date__c");
        String arrivalDateTime = JsonParser2.getFieldValue(res5.toString(), "thn__Arrival_Date_Time__c");
        String departureDateTime = JsonParser2.getFieldValue(res5.toString(), "thn__Departure_Date_Time__c");
        Assert.assertEquals(myceQuote, myceQuoteID);
        Assert.assertEquals(arrivalDateMyceQuote, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(departureDateMyceQuote, date.generateTodayDate2_plus(0, 3));
        Assert.assertTrue(arrivalDateTime.contains(date.generateTodayDate2_plus(0, 4)));
        Assert.assertTrue(departureDateTime.contains(date.generateTodayDate2_plus(0, 3)));
    }

    @Test(priority = 17, description = "Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Departure_Date_Time__c <  thn__MYCE_Quote__r.thn__Arrival_Date__c")
    @Story("thn__Departure_Date_Time__c <  thn__MYCE_Quote__r.thn__Arrival_Date__c")
    public void testCreateQuoteHotelRoom4() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test41' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
                "Name='Test41'",
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
        SfdxCommand.runLinuxCommand1(new String[]{
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
        StringBuilder res5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID,
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuote = JsonParser2.getFieldValue(res5.toString(), "thn__MYCE_Quote__c");
        String arrivalDateMyceQuote = JsonParser2.getFieldValue(res3.toString(), "thn__Arrival_Date__c");
        String departureDateMyceQuote = JsonParser2.getFieldValue(res3.toString(), "thn__Departure_Date__c");
        String arrivalDateTime = JsonParser2.getFieldValue(res5.toString(), "thn__Arrival_Date_Time__c");
        String departureDateTime = JsonParser2.getFieldValue(res5.toString(), "thn__Departure_Date_Time__c");
        Assert.assertEquals(myceQuote, myceQuoteID);
        Assert.assertEquals(arrivalDateMyceQuote, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(departureDateMyceQuote, date.generateTodayDate2_plus(0, 3));
        Assert.assertTrue(arrivalDateTime.contains(date.generateTodayDate2_plus(0, 1)));
        Assert.assertTrue(departureDateTime.contains(date.generateTodayDate2()));
    }

    @Test(priority = 18, description = "Quote_Hotel_Room__c.VR09_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Add Quote hotel room on MYCE Quote where thn__Departure_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c")
    @Story("thn__Departure_Date_Time__c > thn__MYCE_Quote__r.thn__Departure_Date__c")
    public void testCreateQuoteHotelRoom5() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test42' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
                "Name='Test42'",
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
        SfdxCommand.runLinuxCommand1(new String[]{
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
        StringBuilder res5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID,
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuote = JsonParser2.getFieldValue(res5.toString(), "thn__MYCE_Quote__c");
        String arrivalDateMyceQuote = JsonParser2.getFieldValue(res3.toString(), "thn__Arrival_Date__c");
        String departureDateMyceQuote = JsonParser2.getFieldValue(res3.toString(), "thn__Departure_Date__c");
        String arrivalDateTime = JsonParser2.getFieldValue(res5.toString(), "thn__Arrival_Date_Time__c");
        String departureDateTime = JsonParser2.getFieldValue(res5.toString(), "thn__Departure_Date_Time__c");
        Assert.assertEquals(myceQuote, myceQuoteID);
        Assert.assertEquals(arrivalDateMyceQuote, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(departureDateMyceQuote, date.generateTodayDate2_plus(0, 3));
        Assert.assertTrue(arrivalDateTime.contains(date.generateTodayDate2_plus(0, 1)));
        Assert.assertTrue(departureDateTime.contains(date.generateTodayDate2_plus(0, 4)));
    }

    @Test(priority = 19, description = "Quote_Hotel_Room__c.VR15_Pax")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Hotel_Room__c.VR15_Pax")
    @Story("Add Quote hotel room on MYCE Quote: thn__Pax__c > thn__MYCE_Quote__r.thn__Pax__c")
    public void testCreateQuoteHotelRoom6() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test43' thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
                "Name='Test43'",
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
        SfdxCommand.runLinuxCommand1(new String[]{
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
        StringBuilder res5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID,
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuote = JsonParser2.getFieldValue(res5.toString(), "thn__MYCE_Quote__c");
        String myceQuotePax= JsonParser2.getFieldValue(res3.toString(), "thn__Pax__c");
        String hotelRoomPax= JsonParser2.getFieldValue(res5.toString(), "thn__Pax__c");
        Assert.assertEquals(myceQuote, myceQuoteID);
        Assert.assertEquals(myceQuotePax, "5");
        Assert.assertEquals(hotelRoomPax, "6");
    }

    @Test(priority = 20, description = "Quote_Meetings_Room__c.VR19_SetupResource")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR19_SetupResource")
    @Story("")
    public void testCreateQuoteMeetingsRoom1() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test44' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
                "Name='Test44'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res4.toString(), "Id");
        StringBuilder res5 =SfdxCommand.runLinuxCommand1(new String[]{
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
        String meetingRoomID1 = JsonParser2.getFieldValue(res5.toString(), "id");
        Assert.assertNotNull(meetingRoomID1);
        StringBuilder res6 =SfdxCommand.runLinuxCommand1(new String[]{
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
        String meetingRoomID2 = JsonParser2.getFieldValue(res6.toString(), "id");
        Assert.assertNotNull(meetingRoomID2);
        StringBuilder res7 =SfdxCommand.runLinuxCommand1(new String[]{
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
        String meetingRoomID3 = JsonParser2.getFieldValue(res7.toString(), "id");
        Assert.assertNotNull(meetingRoomID3);
        StringBuilder res8 =SfdxCommand.runLinuxCommand1(new String[]{
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
        String meetingRoomID4 = JsonParser2.getFieldValue(res8.toString(), "id");
        Assert.assertNotNull(meetingRoomID4);
        StringBuilder res9 =SfdxCommand.runLinuxCommand1(new String[]{
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
        String meetingRoomID5 = JsonParser2.getFieldValue(res9.toString(), "id");
        Assert.assertNotNull(meetingRoomID5);
        StringBuilder res10 =SfdxCommand.runLinuxCommand1(new String[]{
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
        String meetingRoomID6 = JsonParser2.getFieldValue(res10.toString(), "id");
        Assert.assertNotNull(meetingRoomID6);
        StringBuilder res11 =SfdxCommand.runLinuxCommand1(new String[]{
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
        String meetingRoomID7 = JsonParser2.getFieldValue(res11.toString(), "id");
        Assert.assertNotNull(meetingRoomID7);
        StringBuilder res12 =SfdxCommand.runLinuxCommand1(new String[]{
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
        String meetingRoomID8 = JsonParser2.getFieldValue(res12.toString(), "id");
        Assert.assertNotNull(meetingRoomID8);
        StringBuilder res13 =SfdxCommand.runLinuxCommand1(new String[]{
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
        String meetingRoomID9 = JsonParser2.getFieldValue(res13.toString(), "id");
        Assert.assertNotNull(meetingRoomID9);
        StringBuilder res14 =SfdxCommand.runLinuxCommand1(new String[]{
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
        String meetingRoomID10 = JsonParser2.getFieldValue(res14.toString(), "id");
        Assert.assertNotNull(meetingRoomID10);
    }

    @Test(priority = 21, description = "Quote_Meetings_Room__c.VR21_Lock_Resource")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR21_Lock_Resource")
    @Story("")
    public void testCreateQuoteMeetingsRoom2() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test45' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
                "Name='Test45'",
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
                        resourceID + "' thn__Pax__c=6 thn__Setup__c='Buffet'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingRoomID = JsonParser2.getFieldValue(res5.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
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
        StringBuilder res6 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "id='" + meetingRoomID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String lockResource = JsonParser2.getFieldValue(res6.toString(), "thn__Lock_Resource__c");
        String resource = JsonParser2.getFieldValue(res6.toString(), "thn__Resource__c");
        Assert.assertEquals(lockResource, "true");
        Assert.assertNull(resource);
    }

    @Test(priority = 22, description = "Quote_Meetings_Room__c.VR25_PackageDate")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Meetings_Room__c.VR25_PackageDate")
    @Story("Add meeting room to the package, Add package on MYCE Quote, Open Quote meeting room record," +
            " thn__Shadow__c == FALSE, Change thn__Start_Date_Time__c, thn__End_Date_Time__c")
    public void testCreateQuoteMeetingsRoom3() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test46' thn__Pax__c=4 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
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
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 1),
                "-u",
                ORG_USERNAME,
                "--json"});
        String success = JsonParser2.getFieldValue(updateResult.toString(), "success");
        Assert.assertEquals(success, "true");
    }

    /*@Test(priority = 23, description = "Quote_Package__c.VR12_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR12_Dates_within_Quote_dates")
    @Story("")
    public void testCreateQuotePackage1() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder packageRecord = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Pack c'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test47' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(myseQuoteResult);
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        //thn__Start_Date__c  < thn__MYCE_Quote__r.thn__Arrival_Date__c
        StringBuilder quotePackageResult1 = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
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
        String quotePackageID1 = JsonParser2.getFieldValue(quotePackageResult1.toString(), "id");
        Assert.assertNotNull(quotePackageID1);
        //thn__Start_Date__c> thn__MYCE_Quote__r.thn__Departure_Date__c
        /*StringBuilder quotePackageResult2 = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
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
        String quotePackageID2 = JsonParser2.getFieldValue(quotePackageResult2.toString(), "id");
        Assert.assertNotNull(quotePackageID2);*/
        //thn__End_Date__c< thn__MYCE_Quote__r.thn__Arrival_Date__c
        /*StringBuilder quotePackageResult3 = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
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
        String quotePackageID3 = JsonParser2.getFieldValue(quotePackageResult3.toString(), "id");
        Assert.assertNotNull(quotePackageID3);
        //thn__End_Date__c> thn__MYCE_Quote__r.thn__Departure_Date
        /*StringBuilder quotePackageResult4 = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
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
        String quotePackageID4 = JsonParser2.getFieldValue(quotePackageResult4.toString(), "id");
        Assert.assertNotNull(quotePackageID4);
    }*/

    @Test(priority = 24, description = "Quote_Package__c.VR14_Discount")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR14_Discount")
    @Story("")
    public void testCreateQuotePackage2() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test Package 4'  thn__Hotel__c='" + propertyID + "' thn__Discount_Max__c=15",
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
                "Name='Test48' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "id='" + quotePackageID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String discount = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Discount__c");
        String myceQuote = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__MYCE_Quote__c");
        Assert.assertEquals(discount, "70");
        Assert.assertEquals(myceQuote, myceQuoteID);
    }

    @Test(priority = 25, description = "Quote_Package__c.VR18_Pax")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR18_Pax")
    @Story("Add Quote package to the MYCE Quote: thn__Pax__c > thn__MYCE_Quote__r.thn__Pax__c")
    public void testCreateQuotePackage3() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test Package 4'",
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
                "Name='Test49' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
        String quotePackageID = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "id='" + quotePackageID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String paxPackage = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Pax__c");
        String myceQuote = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__MYCE_Quote__c");
        Assert.assertEquals(paxPackage, "15");
        Assert.assertEquals(myceQuote, myceQuoteID);
    }

    @Test(priority = 26, description = "Quote_Package__c.VR33_QuotePackage_Account")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR33_QuotePackage_Account")
    @Story("")
    public void testCreateQuotePackage4() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
        String packageID = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test50' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
        String quotePackageID = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        Assert.assertNotNull(quotePackageID);
    }

    @Test(priority = 27, description = "Quote_Package__c.VR34_QuotePackage_Dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR34_QuotePackage_Dates")
    @Story("")
    public void testCreateQuotePackage5() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productRoom1NightID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test Package 5'  thn__Hotel__c='" + propertyID + "' thn__Start_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) +
                        " thn__End_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageID = JsonParser2.getFieldValue(packageResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{SFDX,
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
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test51' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult1 = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
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
        String quotePackageID1 = JsonParser2.getFieldValue(quotePackageResult1.toString(), "id");
        Assert.assertNotNull(quotePackageID1);
        StringBuilder quotePackageResult2 = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
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
        String quotePackageID2 = JsonParser2.getFieldValue(quotePackageResult2.toString(), "id");
        Assert.assertNotNull(quotePackageID2);
        StringBuilder quotePackageResult3 = SfdxCommand.runLinuxCommand1(new String[]{SFDX,
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
        String quotePackageID3 = JsonParser2.getFieldValue(quotePackageResult3.toString(), "id");
        Assert.assertNotNull(quotePackageID3);
    }

    @Test(priority = 28, description = "Quote_Package__c.VR37_Max_Discount")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Package__c.VR37_Max_Discount")
    @Story("Add Quote Package to MYCE Quote: set Discount on quote package > Discount max on Package")
    public void testCreateQuotePackage6() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test Package 6'  thn__Discount_Max__c=10 thn__Hotel__c='" + propertyID + "' thn__Start_Date__c=" +
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
                "Name='Test52' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "id='" + quotePackageID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String discountQuotePackage = JsonParser2.getFieldValue(quotePackageRecord.toString(), "thn__Discount__c");
        Assert.assertEquals(discountQuotePackage, "11");
    }

    @Test(priority = 29, description = "Quote_Product__c.VR08_Start_End_Date")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR08_Start_End_Date")
    @Story("Add Quote Product to MYCE Quote: thn__Start_Date_Time__c >= thn__End_Date_Time__c")
    public void testCreateQuoteProduct1() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='WINES'",
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
                "Name='Test53' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
        String quoteProductID = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        Assert.assertNotNull(quoteProductID);
    }

    @Test(priority = 30, description = "Quote_Product__c.VR11_Dates_within_Quote_dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR11_Dates_within_Quote_dates")
    @Story("")
    public void testCreateQuoteProduct2() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='WINES'",
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
                "Name='Test54' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
        String quoteProductID1 = JsonParser2.getFieldValue(quoteProductResult1.toString(), "id");
        Assert.assertNotNull(quoteProductID1);
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
        String quoteProductID2 = JsonParser2.getFieldValue(quoteProductResult2.toString(), "id");
        Assert.assertNotNull(quoteProductID2);
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
        String quoteProductID3 = JsonParser2.getFieldValue(quoteProductResult3.toString(), "id");
        Assert.assertNotNull(quoteProductID3);
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
        String quoteProductID4 = JsonParser2.getFieldValue(quoteProductResult4.toString(), "id");
        Assert.assertNotNull(quoteProductID4);
    }

    @Test(priority = 31, description = "Quote_Product__c.VR17_Pax")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR17_Pax")
    @Story("Add Quote product to MYCE Quote: thn__Pax__c > thn__MYCE_Quote__r.thn__Pax__c")
    public void testCreateQuoteProduct3() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='WINES'",
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
                "Name='Test55' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
        String quoteProductID = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        Assert.assertNotNull(quoteProductID);
    }

    @Test(priority = 32, description = "Quote_Product__c.VR23_ServiceArea_date")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR23_ServiceArea_date")
    @Story("Add Meeting room to the Myce Quote, Add Quote product to the Quote: select Meeting Room while creating" +
            " Quote product, thn__Start_Date_Time__c != thn__Service_Area__r.thn__Start_Date_Time__c")
    public void testCreateQuoteProduct4() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test56' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
        String quoteProductID = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        Assert.assertNotNull(quoteProductID);
    }

    @Test(priority = 33, description = "Quote_Product__c.VR26_PackageDate")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR26_PackageDate")
    @Story("Add Package having products to the Quote, Open Quote product record, Change dates:" +
            " thn__Start_Date_Time__c != thn__Start_Date__c, thn__End_Date_Time__c !=thn__End_Date__c")
    public void testCreateQuoteProduct5() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test57' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
        String success = JsonParser2.getFieldValue(updateResult.toString(), "success");
        Assert.assertEquals(success, "true");
    }


    @Test(priority = 34, description = "Quote_Product__c.VR36_Consumption_on_Package_Line")
    @Severity(SeverityLevel.NORMAL)
    @Description("Quote_Product__c.VR36_Consumption_on_Package_Line")
    @Story("Add Package having products to the Quote, Open Quote product record, Set On_Consumption__c to TRUE")
    public void testCreateQuoteProduct6() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='Test58' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
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
        String success = JsonParser2.getFieldValue(updateResult.toString(), "success");
        Assert.assertEquals(success, "true");
    }

    @Test(priority = 35, description = "Guest__c.VR01_guest_send_to_mews")
    @Severity(SeverityLevel.NORMAL)
    @Description("Guest__c.VR01_guest_send_to_mews")
    @Story("Create Guest__c record, do not fill Hotel__c, Set Send_to_Mews__c to TRUE")
    public void testCreateGuest() throws InterruptedException, IOException {
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='John' thn__Send_to_Mews__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        Assert.assertNotNull(guestID);
    }

    @Test(priority = 36, description = "Item__c.VR02_item_send_to_mews")
    @Severity(SeverityLevel.NORMAL)
    @Description("Item__c.VR02_item_send_to_mews")
    @Story("On Item record where thn__Mews_Id__c != null, set thn__Send_to_Mews__c to TRUE")
    public void testCreateItem() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Rate__c",
                "-w",
                "Name='TestRateAuto2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "Name='TestRateAuto2' thn__IsActive__c=true thn__IsPublic__c=true thn__Hotel__c='" + propertyID + "'",
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
                "thn__FirstName__c='Test'",
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
                "thn__Reservation__c='" + reservationID + "' thn__Product__c='" + productRoom1NightID + "' thn__Mews_Id__c=555 thn__Send_to_Mews__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        String itemID = JsonParser2.getFieldValue(itemResult.toString(), "id");
        Assert.assertNotNull(itemID);
    }

    @Test(priority = 37, description = "Reservation__c.VR03_Reason_update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Reservation__c.VR03_Reason_update")
    @Story("On thn__Reservation__c record set thn__Update_Price__c to TRUE, Leave thn__Reason_update__c empty")
    public void testCreateReservation() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "thn__FirstName__c='Test2'",
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
        String reservationID = JsonParser2.getFieldValue(reservationResult.toString(), "id");
        StringBuilder  reservationRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Reservation__c",
                "-w",
                "id='" + reservationID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String updatePrice = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Update_Price__c");
        String reasonUpdate = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Reason_update__c");
        Assert.assertEquals(updatePrice, "true");
        Assert.assertEquals(reasonUpdate, null);
    }

    @Test(priority = 38, description = "Reservation__c.VR04_Cancellation_reason")
    @Severity(SeverityLevel.NORMAL)
    @Description("Reservation__c.VR04_Cancellation_reason")
    @Story("On thn__Reservation__c record where thn__Mews_Id__c != null change thn__State__c to “Canceled")
    public void testCreateReservation2() throws InterruptedException, IOException {
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
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
                "thn__FirstName__c='Test2'",
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
                        "' thn__Pricing_Type__c='Rate pricing' thn__Rate__c='" + rateID + "' thn__Mews_Id__c=''1234",
                "-u",
                ORG_USERNAME,
                "--json"});
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
        StringBuilder  reservationRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Reservation__c",
                "-w",
                "id='" + reservationID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String mewsId = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Mews_Id__c");
        String state = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__State__c");
        Assert.assertNotNull(mewsId);
        Assert.assertEquals(state, "Canceled");
    }

    @Test(priority = 39, description = "VR38_Resource_Grouping")
    @Severity(SeverityLevel.NORMAL)
    @Description("VR38_Resource_Grouping")
    @Story("Create Resource Grouping record, try to add Resources with different Properties")
    public void testCreateResourceGrouping() throws InterruptedException, IOException {
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
        String resourceGroupingID = JsonParser2.getFieldValue(resourceGroupingResult.toString(), "id");
        Assert.assertNotNull(resourceGroupingID);
        StringBuilder deleteGroupingResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Resource_Grouping__c",
                "-w",
                "id='" + resourceGroupingID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
    }

}
