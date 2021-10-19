package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;

public class GuestManagementTesting extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest Management testing")
    public void logIn() throws InterruptedException, IOException {
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
    }

    @Test(priority = 2, description = "Create a new myce quote and fill values: company and contact, agent and contact," +
            " reservation guest name and set 'Bill to' to Company.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest Management testing")
    public void precondition() throws InterruptedException, IOException {

        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='GuestManagementTesting",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Contact",
                "-w",
                "LastName='ContactCompany",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Contact",
                "-w",
                "LastName='ContactAgent",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='TestAccountCompanyGM",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='TestAccountAgentGM",
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
        StringBuilder accountCompanyResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='TestAccountCompanyGM' thn__Type__c='Company'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String accountCompanyId = JsonParser2.getFieldValue(accountCompanyResult.toString(), "id");
        StringBuilder accountAgentResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='TestAccountAgentGM' thn__Type__c='Agent'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String accountAgentId = JsonParser2.getFieldValue(accountAgentResult.toString(), "id");
        StringBuilder contactCompanyResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Contact",
                "-v",
                "LastName='ContactCompany' AccountId='" + accountCompanyId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(contactCompanyResult);
        String contactCompanyId = JsonParser2.getFieldValue(contactCompanyResult.toString(), "id");
        StringBuilder contactAgentResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Contact",
                "-v",
                "LastName='ContactAgent' AccountId='" + accountAgentId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String contactAgentId = JsonParser2.getFieldValue(contactAgentResult.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='GuestManagementTesting' thn__Pax__c=1 thn__Hotel__c='" + propertyID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Company__c='" + accountCompanyId +
                        "' thn__Company_Contact__c='" + contactCompanyId + "' thn__Agent__c='" + accountAgentId +
                        "' thn__Agent_Contact__c='" + contactAgentId + "' thn__Bill_to__c='Company'" +
                        " thn__Reservation_Guest_Name__c='GuestManagementTesting'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
    }

    @Test(priority = 3, description = "Change stage to the one referenced in Default Agile Values mdt ('2 - Propose' by" +
            " default). Expected result: Company contact’s guest is created and sent to Mews, Reservation guest is" +
            " created and added on quote")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest Management testing")
    public void case1() throws InterruptedException, IOException {
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='GuestManagementTesting'",
                "-v",
                "thn__Stage__c='2 - Propose'",
                "-u",
                ORG_USERNAME,
                "--json"});
        Thread.sleep(3000);
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='GuestManagementTesting'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String reservationGuestID = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Reservation_Guest__c");
        System.out.println(reservationGuestID);
        StringBuilder contactCompanyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "Contact",
                "-w",
                "LastName='ContactCompany'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String companyGuestID = JsonParser2.getFieldValue(contactCompanyRecord.toString(), "thn__Guest__c");
        StringBuilder reservationGuestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Guest__c",
                "-w",
                "Id='" + reservationGuestID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String reservationGuestMewsId = JsonParser2.getFieldValue(reservationGuestRecord.toString(), "thn__Mews_Id__c");
        StringBuilder companyGuestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Guest__c",
                "-w",
                "Id='" + companyGuestID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String companyGuestMewsId = JsonParser2.getFieldValue(companyGuestRecord.toString(), "thn__Mews_Id__c");
        Assert.assertNotNull(reservationGuestMewsId);
        Assert.assertNotNull(companyGuestMewsId);
    }

    @Test(priority = 4, description = "Modify value in 'Bill to' to Agent. Expected result: Agent contact’s guest is created")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest Management testing")
    public void case2() throws InterruptedException, IOException {
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='GuestManagementTesting'",
                "-v",
                "thn__Bill_to__c='Agent'",
                "-u",
                ORG_USERNAME,
                "--json"});
        Thread.sleep(3000);
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='GuestManagementTesting'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String reservationGuestID = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Reservation_Guest__c");
        System.out.println(reservationGuestID);
        StringBuilder contactAgentRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "Contact",
                "-w",
                "LastName='ContactAgent'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String agentGuestID = JsonParser2.getFieldValue(contactAgentRecord.toString(), "thn__Guest__c");
        StringBuilder agentGuestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Guest__c",
                "-w",
                "Id='" + agentGuestID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String agentGuestMewsId = JsonParser2.getFieldValue(agentGuestRecord.toString(), "thn__Mews_Id__c");
        Assert.assertNotNull(agentGuestMewsId);
    }

    @Test(priority = 5, description = "Modify value in reservation guest name. Expected result: Reservation guest’s" +
            " last name is updated and sent to Mews")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest Management testing")
    public void case3() throws InterruptedException, IOException {
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='GuestManagementTesting'",
                "-v",
                "thn__Reservation_Guest_Name__c='AutoReservationGuest'",
                "-u",
                ORG_USERNAME,
                "--json"});
        Thread.sleep(3000);
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='GuestManagementTesting'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String reservationGuestID = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Reservation_Guest__c");
        StringBuilder reservationGuestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Guest__c",
                "-w",
                "Id='" + reservationGuestID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String reservationGuestMewsId = JsonParser2.getFieldValue(reservationGuestRecord.toString(), "thn__Mews_Id__c");
        String reservationGuestLastName = JsonParser2.getFieldValue(reservationGuestRecord.toString(), "thn__LastName__c");
        Assert.assertNotNull(reservationGuestMewsId);
        Assert.assertEquals(reservationGuestLastName, "AutoReservationGuest");
    }

}
