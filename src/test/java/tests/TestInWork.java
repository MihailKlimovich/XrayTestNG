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

    @Test(priority = 10, description = "Credit_Note_Line__c.Invoice_Line_Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Credit_Note_Line__c.Invoice_Line_Validation")
    @Story("Create Credit Note Line record: thn__Invoice_Line__c == null & thn__Amount__c == null & thn__Quantity__c == null")
    public void testCreateNewCreditNoteLine() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Credit_Note__c",
                "-v",
                "thn__Date__c='" + date.generateTodayDate2() + "'",
                "-u",
                "THYNK-VR",
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Credit_Note__c",
                "-w",
                "Name=Test31",
                "-u",
                "THYNK-VR",
                "--json"});


    }












}
