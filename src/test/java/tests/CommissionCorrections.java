package tests;

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
import java.util.List;

@Listeners({TestListener.class})

public class CommissionCorrections extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission correction")
    public void logIn() throws InterruptedException, IOException {

        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission correction")
    public void preconditions() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "Account",
                "-w",
                "Name='TestAccountWithoutCommission2",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommissionCorrection",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestCommissionCorrectionPackage",
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
        StringBuilder accountResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='TestAccountWithoutCommission2'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String accountID = JsonParser2.getFieldValue(accountResult2.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestCommissionCorrection' thn__Pax__c=1 thn__Hotel__c='" + propertyID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder productRecordMHD = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecordMHD.toString(), "Id");
        StringBuilder productRecordRoom1Night = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String room1NightID = JsonParser2.getFieldValue(productRecordRoom1Night.toString(), "Id");
        StringBuilder beverageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='BEVERAGE'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder activityRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ACTIVITY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String activityID = JsonParser2.getFieldValue(activityRecord.toString(), "Id");
        StringBuilder equipmentRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='EQUIPMENT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String equipmentID = JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        StringBuilder dinerRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='DINER'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String dinerID = JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestCommissionCorrectionPackage' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Meeting Room' thn__Package__c='" + packageId + "' thn__Type__c='Meeting Room'" +
                        " thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId1 = JsonParser2.getFieldValue(packageLineResult1.toString(), "id");
        StringBuilder packageLineResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Hotel Room' thn__Package__c='" + packageId + "' thn__Type__c='Hotel Room'" +
                        " thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=100 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId2 = JsonParser2.getFieldValue(packageLineResult2.toString(), "id");
        StringBuilder packageLineResult3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Beer' thn__Package__c='" + packageId + "' thn__Type__c='Beverage'" +
                        " thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00 thn__End_Time__c=16:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId3 = JsonParser2.getFieldValue(packageLineResult3.toString(), "id");
        StringBuilder packageLineResult4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Tennis' thn__Package__c='" + packageId + "' thn__Type__c='Activity'" +
                        " thn__Product__c='" + activityID + "' thn__Start_Time__c=15:00 thn__End_Time__c=16:00" +
                        " thn__Unit_Price__c=40 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId4 = JsonParser2.getFieldValue(packageLineResult4.toString(), "id");
        StringBuilder packageLineResult5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Bicycle' thn__Package__c='" + packageId + "' thn__Type__c='Equipment'" +
                        " thn__Product__c='" + equipmentID + "' thn__Start_Time__c=15:00 thn__End_Time__c=16:00" +
                        " thn__Unit_Price__c=50 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId5 = JsonParser2.getFieldValue(packageLineResult5.toString(), "id");
        StringBuilder packageLineResult6 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Burger' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + dinerID + "' thn__Start_Time__c=15:00 thn__End_Time__c=16:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId6 = JsonParser2.getFieldValue(packageLineResult6.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-v",
                "thn__Stage__c='2 - Propose' thn__Company__c='" + accountID + "' thn__Commission_to__c='Company'" +
                        " thn__Commissionable__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(res);
    }

    @Test(priority = 3, description = "When commission on quote package is updated, its related records are updated as well")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission correction")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommissionCorrection'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "thn__MYCE_Quote__c='" + quoteID + "'",
                "-v",
                "thn__Commission__c=59",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(res);
        StringBuilder commissionRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Name FROM thn__Commission__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(commissionRecordsSoql);
        List<String> commissionName = JsonParser2.getFieldValueSoql(commissionRecordsSoql.toString(), "Name");
        Assert.assertEquals(commissionName.size(), 6);
        Assert.assertTrue(commissionName.contains("Hotel Room - 59.00%"));
        Assert.assertTrue(commissionName.contains("Meeting Room - 59.00%"));
        Assert.assertTrue(commissionName.contains("Food - 59.00%"));
        Assert.assertTrue(commissionName.contains("Beverage - 59.00%"));
        Assert.assertTrue(commissionName.contains("Activity - 59.00%"));
        Assert.assertTrue(commissionName.contains("Equipment - 59.00%"));
    }

    @Test(priority = 3, description = "If a commission record has an amount = 0, it is deleted")
    @Severity(SeverityLevel.NORMAL)
    @Story("Commission correction")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestCommissionCorrection'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "thn__MYCE_Quote__c='" + quoteID + "'",
                "-v",
                "thn__Commission__c=0",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(res);
        StringBuilder commissionRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Name FROM thn__Commission__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(commissionRecordsSoql);
        List<String> commissionName = JsonParser2.getFieldValueSoql(commissionRecordsSoql.toString(), "Name");
        Assert.assertEquals(commissionName.size(), 0);
    }
}
