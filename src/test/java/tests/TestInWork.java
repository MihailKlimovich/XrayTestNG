package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;


import java.io.IOException;

public class TestInWork extends BaseTest{


    @Test(priority = 2, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c == null")
    public void testCreateNewMyceQuote1() throws InterruptedException, IOException {
        //force:data:soql:query -q "SELECT thn__Commissionable__c, thn__Commission_to__c FROM thn__MYCE_Quote__c where Name='Test24'" -u THYNK-VR --json
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test24' thn__Commissionable__c=true",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
        "force:data:record:get",
        "-s",
        "thn__MYCE_Quote__c",
        "-w",
        "Name=Test24",
        "-u",
        "THYNK-VR",
        "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String commissionable = JsonParser2.getFieldValue(res.toString(), "thn__Commissionable__c");
        String commission_to =  JsonParser2.getFieldValue(res.toString(), "thn__Commission_to__c");
        Assert.assertEquals(name, "Test24");
        Assert.assertEquals(commissionable, "true");
        Assert.assertEquals(commission_to, null);

    }

    @Test(priority = 3, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c != Agent & thn__Agent__c == null")
    public void testCreateNewMyceQuote2() throws InterruptedException, IOException {

        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test25' thn__Commissionable__c=true thn__Commission_to__c='Agent'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test25",
                "-u",
                "THYNK-VR",
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String commissionable = JsonParser2.getFieldValue(res.toString(), "thn__Commissionable__c");
        String commission_to =  JsonParser2.getFieldValue(res.toString(), "thn__Commission_to__c");
        String agent =  JsonParser2.getFieldValue(res.toString(), "thn__Agent__c");
        Assert.assertEquals(name, "Test25");
        Assert.assertEquals(commissionable, "true");
        Assert.assertEquals(commission_to, "Agent");
        Assert.assertEquals(agent, null);
    }

    @Test(priority = 4, description = "Myce_Quote__c.Commission_Validation_Rule")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.Commission_Validation_Rule")
    @Story("Commissionable == true & thn__Commission_to__c == Company & thn__Company__c == null")
    public void testCreateNewMyceQuote3() throws InterruptedException, IOException {

        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test26' thn__Commissionable__c=true thn__Commission_to__c='Company'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test26",
                "-u",
                "THYNK-VR",
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String commissionable = JsonParser2.getFieldValue(res.toString(), "thn__Commissionable__c");
        String commission_to =  JsonParser2.getFieldValue(res.toString(), "thn__Commission_to__c");
        String company =  JsonParser2.getFieldValue(res.toString(), "thn__Company__c");
        Assert.assertEquals(name, "Test26");
        Assert.assertEquals(commissionable, "true");
        Assert.assertEquals(commission_to, "Company");
        Assert.assertEquals(company, null);
    }

    @Test(priority = 5, description = "Myce_Quote__c.VR05_Dates")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR05_Dates")
    @Story("thn__Departure_Date__c < thn__Arrival_Date__c")
    public void testCreateNewMyceQuote4() throws InterruptedException, IOException {

        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test27' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_minus(0, 5),
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test27",
                "-u",
                "THYNK-VR",
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String arrivalDay = JsonParser2.getFieldValue(res.toString(), "thn__Arrival_Date__c");
        String departureDay =  JsonParser2.getFieldValue(res.toString(), "thn__Departure_Date__c");
        Assert.assertEquals(name, "Test27");
        Assert.assertEquals(arrivalDay, date.generateTodayDate2());
        Assert.assertEquals(departureDay, date.generateTodayDate2_minus(0, 5));
    }

    @Test(priority = 6, description = "Myce_Quote__c.VR27_Company_Agent_Type")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR27_Company_Agent_Type")
    @Story("Create MYCE Quote: Select Company for Agent field ,Select Agent for Company field")
    public void testCreateNewMyceQuote5() throws InterruptedException, IOException {

        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "Account",
                "-w",
                "Name='Test Company'",
                "-u",
                "THYNK-VR",
                "--json"});
        String testCompanyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "Account",
                "-w",
                "Name='Test Agent'",
                "-u",
                "THYNK-VR",
                "--json"});
        String testAgentID = JsonParser2.getFieldValue(res2.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test28' thn__Company__c='" + testAgentID + "' thn__Agent__c='" + testCompanyID + "'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test28",
                "-u",
                "THYNK-VR",
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String company = JsonParser2.getFieldValue(res.toString(), "thn__Company__c");
        String agent =  JsonParser2.getFieldValue(res.toString(), "thn__Agent__c");
        Assert.assertEquals(name, "Test28");
        Assert.assertEquals(company, testAgentID);
        Assert.assertEquals(agent, testCompanyID);
    }

    @Test(priority = 7, description = "Myce_Quote__c.VR13_Reservation_Guest")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR13_Reservation_Guest")
    @Story("Create MYCE Quote: leave thn__Reservation_Guest__c empty, Set thn__Send_to_Mews__c to TRUE")
    public void testCreateNewMyceQuote6() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test29' thn__SendToMews__c=true",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test29",
                "-u",
                "THYNK-VR",
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String sendToMews = JsonParser2.getFieldValue(res.toString(), "thn__SendToMews__c");
        String reservationGuest =  JsonParser2.getFieldValue(res.toString(), "thn__Reservation_Guest__c");
        Assert.assertEquals(name, "Test29");
        Assert.assertEquals(sendToMews, "true");
        Assert.assertEquals(reservationGuest, null);
    }

    @Test(priority = 8, description = "Myce_Quote__c.VR22_ClosedStatus")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR22_ClosedStatus")
    @Story("Change Stage om MYCE Quote to '4 - Closed'")
    public void testCreateNewMyceQuote7() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test30' thn__Stage__c='4 - Closed'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test30",
                "-u",
                "THYNK-VR",
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String stageStatus = JsonParser2.getFieldValue(res.toString(), "thn__Stage__c");
        Assert.assertEquals(name, "Test30");
        Assert.assertEquals(stageStatus, "4 - Closed");
    }

    @Test(priority = 9, description = "Myce_Quote__c.VR28_Cancelled_Status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Myce_Quote__c.VR28_Cancelled_Status")
    @Story("Set thn__Is_Confirmed__c to false, Change MYCE Quote Closed Status to ‘Cancelled’")
    public void testCreateNewMyceQuote8() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test31' thn__Closed_Status__c='Cancelled'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name=Test31",
                "-u",
                "THYNK-VR",
                "--json"});
        String name = JsonParser2.getFieldValue(res.toString(), "Name");
        String closedStatus = JsonParser2.getFieldValue(res.toString(), "thn__Closed_Status__c");
        String isConfirmed = JsonParser2.getFieldValue(res.toString(), "thn__Is_Confirmed__c");
        Assert.assertEquals(name, "Test31");
        Assert.assertEquals(closedStatus, "Cancelled");
        Assert.assertEquals(isConfirmed, "false");
    }

    /*@Test(priority = 10, description = "Credit_Note_Line__c.Invoice_Line_Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Credit_Note_Line__c.Invoice_Line_Validation")
    @Story("Create Credit Note Line record: thn__Invoice_Line__c == null & thn__Amount__c == null & thn__Quantity__c == null")
    public void testCreateNewCreditNoteLine() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Credit_Note__c",
                "-w",
                "Name='CN-0001'",
                "-u",
                "THYNK-VR",
                "--json"});
        String creditNoteID = JsonParser2.getFieldValue(res1.toString(), "Id");
        System.out.println(creditNoteID);
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Credit_Note_Line__c",
                "-v",
                "thn__Credit_Note__c='" + creditNoteID + "' thn__VAT_Category__c='1'",
                "-u",
                "THYNK-VR",
                "--json"});
        System.out.println(res);
    }*/

    @Test(priority = 11, description = "Package_Line__c.VR30_IsMultidays")
    @Severity(SeverityLevel.NORMAL)
    @Description("Package_Line__c.VR30_IsMultidays")
    @Story("For Package where thn__Multi_Days__c == true, create Package line: thn__AppliedDay__c == null")
    public void testCreateNewPackageLine1() throws InterruptedException, IOException {
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                "THYNK-VR",
                "--json"});
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
        "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test Package' thn__Multi_Days__c=true thn__Hotel__c='" + propertyID + "'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Test Package''",
                "-u",
                "THYNK-VR",
                "--json"});
        String packageID = JsonParser2.getFieldValue(res2.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line' thn__Package__c='" + packageID + "'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Package_Line__c",
                "-w",
                "Name='Test Pack Line'",
                "-u",
                "THYNK-VR",
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
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                "THYNK-VR",
                "--json"});
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test Package 2' thn__Multi_Days__c=false thn__Hotel__c='" + propertyID + "'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Test Package 2'",
                "-u",
                "THYNK-VR",
                "--json"});
        String packageID = JsonParser2.getFieldValue(res2.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line 2' thn__Package__c='" + packageID + "' thn__AppliedDay__c=25",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Package_Line__c",
                "-w",
                "Name='Test Pack Line 2'",
                "-u",
                "THYNK-VR",
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
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Test'",
                "-u",
                "THYNK-VR",
                "--json"});
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='Test Package 3' thn__Hotel__c='" + propertyID + "'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                "THYNK-VR",
                "--json"});
        String productID = JsonParser2.getFieldValue(res2.toString(), "Id");
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='Test Package 3'",
                "-u",
                "THYNK-VR",
                "--json"});
        String packageID = JsonParser2.getFieldValue(res3.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Test Pack Line 3' thn__Package__c='" + packageID + "' thn__Type__c='Hotel Room' thn__Product__c='" + productID + "'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Package_Line__c",
                "-w",
                "Name='Test Pack Line 3'",
                "-u",
                "THYNK-VR",
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
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                "THYNK-VR",
                "--json"});
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test32' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                        " thn__Departure_Date__c=" + date.generateTodayDate2(),
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='Test32'",
                "-u",
                "THYNK-VR",
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res2.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "Name='Test Hotel Room 1' thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2() + "T19:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2() + "T10:00:00.000+0000",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Name='Test Hotel Room 1'",
                "-u",
                "THYNK-VR",
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
        StringBuilder res1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 2 NIGHTS'",
                "-u",
                "THYNK-VR",
                "--json"});
        String productID = JsonParser2.getFieldValue(res2.toString(), "Id");
        String propertyID = JsonParser2.getFieldValue(res1.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test33' thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 2),
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res3 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='Test33'",
                "-u",
                "THYNK-VR",
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(res3.toString(), "Id");
        StringBuilder res4 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                "THYNK-VR",
                "--json"});
        String roomTypeID = JsonParser2.getFieldValue(res4.toString(), "Id");
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID +
                        "' thn__Space_Area__c='" + roomTypeID + "' thn__Arrival_Date_Time__c=" +
                        date.generateTodayDate2() + "T10:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 2) + "T19:00:00.000+0000 thn__Pax__c=10",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res5 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "thn__MYCE_Quote__c='" + myceQuoteID,
                "-u",
                "THYNK-VR",
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











}
