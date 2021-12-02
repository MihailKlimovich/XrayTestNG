package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;
import utils.Listeners.TestListener;

import java.io.IOException;

@Listeners({TestListener.class})

public class Request_Agent extends BaseTest{

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-506: Request - Agent")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
    }

    @Test(priority = 2, description = "Fill Agent contact Email, make sure Contact with matching email exists in the" +
            " system and is linked to Account, convert the record")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-506: Request - Agent")
    public void RequestAgent_case1() throws InterruptedException, IOException {
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='TestRequest",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(res);
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Contact",
                "-w",
                "LastName='Shepard'",
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
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Hotel__c",
                "-w",
                "id='" + propertyID + "'",
                "-v",
                "thn__Request_Name__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder accountResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='TestRequest",
                "-u",
                ORG_USERNAME,
                "--json"});
        String accountId = JsonParser2.getFieldValue(accountResult.toString(), "id");
        StringBuilder contactResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Contact",
                "-v",
                "LastName='Shepard' Email='test@tut.by' AccountId='" + accountId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String contactId = JsonParser2.getFieldValue(contactResult.toString(), "id");
        StringBuilder requestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Request__c",
                "-v",
                "thn__Agent_contact_Email__c='test@tut.by'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String requestId = JsonParser2.getFieldValue(requestResult.toString(), "id");
        StringBuilder requestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Request__c",
                "-w",
                "Id='" + requestId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(requestRecord);
        String agentContact = JsonParser2.getFieldValue(requestRecord.toString(), "thn__Agent_contact__c");
        String agent = JsonParser2.getFieldValue(requestRecord.toString(), "thn__Agent__c");
        Assert.assertEquals(agentContact, contactId);
        Assert.assertEquals(agent, accountId);
    }

    @Test(priority = 3, description = "    Fill Agent contact Email, make sure Contact with matching email exists" +
            " in the system, fill Agent Name, make sure such an account doesn’t exist, convert the record")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-506: Request - Agent")
    public void RequestAgent_case2() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Contact",
                "-w",
                "LastName='Anderson'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='Sandman",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder contactResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Contact",
                "-v",
                "LastName='Anderson' Email='test2@tut.by'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String contactId = JsonParser2.getFieldValue(contactResult.toString(), "id");
        StringBuilder requestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Request__c",
                "-v",
                "thn__Agent_contact_Email__c='test2@tut.by' thn__Agent_Name__c='Sandman'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String requestId = JsonParser2.getFieldValue(requestResult.toString(), "id");
        requests.openRequestRecord(requestId);
        requests.clickConvert();
        convertWindow.fillConvertForm("Demo", "3", date.generateTodayDate(), date.generateDate_plus(0, 3));
        Thread.sleep(3000);
        StringBuilder requestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Request__c",
                "-w",
                "Id='" + requestId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String agentContactId = JsonParser2.getFieldValue(requestRecord.toString(), "thn__Agent_contact__c");
        String agentId = JsonParser2.getFieldValue(requestRecord.toString(), "thn__Agent__c");
        String requestContactId = JsonParser2.getFieldValue(requestRecord.toString(), "thn__Contact__c");
        String requestAccountId = JsonParser2.getFieldValue(requestRecord.toString(), "thn__Account__c");
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "thn__Agent__c='" + agentId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteAgentContactId = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Agent_Contact__c");
        String myceQuoteAgentId = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Agent__c");
        String myceQuoteCompanyId = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Company__c");
        String myceQuoteCompanyContactId = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Company_Contact__c");
        Assert.assertEquals(myceQuoteAgentContactId, agentContactId);
        Assert.assertEquals(agentId, myceQuoteAgentId);
        Assert.assertEquals(myceQuoteCompanyId, requestAccountId);
        Assert.assertEquals(myceQuoteCompanyContactId, requestContactId);
    }

    @Test(priority = 4, description = "Fill Agent contact Email, Contact with matching email doesn’t exist in the" +
            " system, fill Agent Name, convert record")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-506: Request - Agent")
    public void RequestAgent_case3() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='Sandman2",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "TestAgentContact",
                "-w",
                "LastName='Anderson'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "TestContact",
                "-w",
                "LastName='Anderson'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='TestAccount",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='Sandman2",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder requestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Request__c",
                "-v",
                "thn__Agent_contact_Email__c='test3@tut.by' thn__Agent_contact_Last_Name__c='TestAgentContact'" +
                        " thn__Last_Name__c='TestContact' thn__Account_Name__c='TestAccount'  thn__Agent_Name__c='Sandman2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String requestId = JsonParser2.getFieldValue(requestResult.toString(), "id");
        requests.openRequestRecord(requestId);
        requests.clickConvert();
        convertWindow.fillConvertForm("Demo", "3", date.generateTodayDate(), date.generateDate_plus(0, 3));
        Thread.sleep(3000);
        StringBuilder requestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Request__c",
                "-w",
                "Id='" + requestId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String agentContactId = JsonParser2.getFieldValue(requestRecord.toString(), "thn__Agent_contact__c");
        String agentId = JsonParser2.getFieldValue(requestRecord.toString(), "thn__Agent__c");
        String requestContactId = JsonParser2.getFieldValue(requestRecord.toString(), "thn__Contact__c");
        String requestAccountId = JsonParser2.getFieldValue(requestRecord.toString(), "thn__Account__c");
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "thn__Agent__c='" + agentId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteAgentContactId = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Agent_Contact__c");
        String myceQuoteAgentId = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Agent__c");
        String myceQuoteCompanyId = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Company__c");
        String myceQuoteCompanyContactId = JsonParser2.getFieldValue(myceQuoteRecord.toString(), "thn__Company_Contact__c");
        Assert.assertEquals(myceQuoteAgentContactId, agentContactId);
        Assert.assertEquals(agentId, myceQuoteAgentId);
        Assert.assertEquals(myceQuoteCompanyId, requestAccountId);
        Assert.assertEquals(myceQuoteCompanyContactId, requestContactId);
    }

}
