package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;

public class QuotesAccountToMews extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Quote's accounts to Mews")
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

    @Test(priority = 2, description = "1. Create Myce Quote, 2. Specify Agent, make sure agent.mews_id == null," +
            " agent.hotel != quote.hotel, 3. Update Quote’s stage to stage defined in ‘Guest Creation Stage’ field of" +
            " the ‘Default Agile Values’ mdt. Result: agent.hotel__c is updated to quote.hotel__c and" +
            " agent.send_to_mews__c is updated to TRUE; agent is sent to mews: if successfully, Mews_Id is filled," +
            " else Mews Error Message is filled")
    @Severity(SeverityLevel.NORMAL)
    @Story("Quote's accounts to Mews")
    public void case1() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='AccountToMewsTest1",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='AccountToMews",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder propertyTestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Test'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyTestID = JsonParser2.getFieldValue(propertyTestRecord.toString(), "Id");
        StringBuilder accountResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='AccountToMews' thn__Hotel__c='" + propertyTestID + "' thn__Type__c='Agent'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String accountId = JsonParser2.getFieldValue(accountResult.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='AccountToMewsTest1' thn__Pax__c=1 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-v",
                "thn__Agent__c='" + accountId + "' thn__Stage__c='2 - Propose'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(res);
        Thread.sleep(3000);
        StringBuilder accountRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "Account",
                "-w",
                "Name='AccountToMews'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String accountHotel = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Hotel__c");
        String accountMewsID = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Mews_Id__c");
        Assert.assertEquals(accountHotel, propertyDemoID);
        Assert.assertNotNull(accountMewsID);
    }

    @Test(priority = 3, description = "1. Create Myce Quote, 2. Specify Company, make sure company.mews_id == null," +
            " company.hotel != quote.hotel, 3. Update Quote’s stage to stage defined in ‘Guest Creation Stage’ field of" +
            " the ‘Default Agile Values’ mdt. Result: company.hotel__c is updated to quote.hotel__c and" +
            " company.send_to_mews__c is updated to TRUE; company is sent to mews: if successfully, Mews_Id is filled," +
            " else Mews Error Message is filled")
    @Severity(SeverityLevel.NORMAL)
    @Story("Quote's accounts to Mews")
    public void case2() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='AccountToMewsTest2",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='AccountToMews2",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder propertyTestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Test'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyTestID = JsonParser2.getFieldValue(propertyTestRecord.toString(), "Id");
        StringBuilder accountResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='AccountToMews2' thn__Hotel__c='" + propertyTestID + "' thn__Type__c='Company'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String accountId = JsonParser2.getFieldValue(accountResult.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='AccountToMewsTest2' thn__Pax__c=1 thn__Hotel__c='" + propertyDemoID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + quoteID + "'",
                "-v",
                "thn__Company__c='" + accountId + "' thn__Stage__c='2 - Propose'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(res);
        Thread.sleep(3000);
        StringBuilder accountRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "Account",
                "-w",
                "Name='AccountToMews2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(accountRecord);
        String accountHotel = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Hotel__c");
        String accountMewsID = JsonParser2.getFieldValue(accountRecord.toString(), "thn__Mews_Id__c");
        Assert.assertEquals(accountHotel, propertyDemoID);
        Assert.assertNotNull(accountMewsID);
    }

}
