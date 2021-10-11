package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;
import java.util.List;

public class FSDatesOnQuotePackage extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("FS dates on quote package")
    public void logIn() throws InterruptedException, IOException {
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:auth:jwt:grant",
                "--clientid",
                key,
                "--jwtkeyfile",
                serverKeyPath,
                "--username",
                ALIAS,
                "--instanceurl",
                urlForScratch
        });
        System.out.println(authorise);
    }

    @Test(priority = 2, description = "Create Myce Quote, Add Quote package, Change Myce Quote stage to 2 - Propose to generate FS dates")
    @Severity(SeverityLevel.NORMAL)
    @Story("FS dates on quote package")
    public void case1() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestFSDatesOnQuotePackage",
                "-u",
                ALIAS,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestFSDatesOnQuotePackage",
                "-u",
                ALIAS,
                "--json"});
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                ALIAS,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecordMHD = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ALIAS,
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
                ALIAS,
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
                ALIAS,
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
                ALIAS,
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
                ALIAS,
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
                ALIAS,
                "--json"});
        String dinerID = JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestFSDatesOnQuotePackage' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ALIAS,
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
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(packageLineResult1);
        String packageLineId1 = JsonParser2.getFieldValue(packageLineResult1.toString(), "id");
        StringBuilder packageLineResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='Hotel Room' thn__Package__c='" + packageId + "' thn__Type__c='Hotel Room'" +
                        " thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=100 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
                "-u",
                ALIAS,
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
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
                "-u",
                ALIAS,
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
                        " thn__Unit_Price__c=40 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
                "-u",
                ALIAS,
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
                        " thn__Unit_Price__c=50 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
                "-u",
                ALIAS,
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
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
                "-u",
                ALIAS,
                "--json"});
        String packageLineId6 = JsonParser2.getFieldValue(packageLineResult6.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestFSDatesOnQuotePackage' thn__Pax__c=1 thn__Hotel__c='" + propertyID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ALIAS,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "'",
                "-u",
                ALIAS,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-v",
                "thn__Stage__c='2 - Propose'",
                "-u",
                ALIAS,
                "--json"});
        StringBuilder fsDateRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT thn__Date__c FROM thn__FS_Date__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(fsDateRecordsSoql);
        List<String> dates = JsonParser2.getFieldValueSoql(fsDateRecordsSoql.toString(), "thn__Date__c");
        System.out.println(dates);
        Assert.assertEquals(dates.size(), 4);
        Assert.assertEquals(dates.get(0), date.generateTodayDate2_plus(0, 0));
        Assert.assertEquals(dates.get(1), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(dates.get(2), date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(dates.get(3), date.generateTodayDate2_plus(0, 3));
    }

    @Test(priority = 2, description = "Update Quote package, change Start / End date")
    @Severity(SeverityLevel.NORMAL)
    @Story("FS dates on quote package")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestFSDatesOnQuotePackage'",
                "-u",
                ALIAS,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ALIAS,
                "--json"});
        String quotePackageID = JsonParser2.getFieldValue(quotePackageRecord.toString(), "Id");
        StringBuilder update = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageID + "'",
                "-v",
                "thn__End_Date__c=" + date.generateTodayDate2_plus(0, 1),
                "-u",
                ALIAS,
                "--json"});
        System.out.println(update);
        StringBuilder fsDateRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT thn__Date__c FROM thn__FS_Date__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(fsDateRecordsSoql);
        List<String> dates = JsonParser2.getFieldValueSoql(fsDateRecordsSoql.toString(), "thn__Date__c");
        System.out.println(dates);
        Assert.assertEquals(dates.size(), 2);
        Assert.assertEquals(dates.get(0), date.generateTodayDate2_plus(0, 0));
        Assert.assertEquals(dates.get(1), date.generateTodayDate2_plus(0, 1));
    }

}
