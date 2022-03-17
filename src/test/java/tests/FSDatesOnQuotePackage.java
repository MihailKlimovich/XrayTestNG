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


public class FSDatesOnQuotePackage extends BaseTest {


    @Test(priority = 1, description = "Create Myce Quote, Add Quote package, Change Myce Quote stage to 2 - Propose" +
            " to generate FS dates")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-502: Quote Package - FS Dates")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestFSDatesOnQuotePackage",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestFSDatesOnQuotePackage",
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
                "Name='TestFSDatesOnQuotePackage' thn__Hotel__c='" + propertyID + "'",
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
        System.out.println(packageLineResult1);
        String packageLineId1 = JsonParser2.getFieldValue(packageLineResult1.toString(), "id");
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
                ORG_USERNAME,
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
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        StringBuilder fsDateRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT thn__Date__c, Id FROM thn__FS_Date__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteProductRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT thn__FS_Date__c FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteMeetingRoomRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT thn__FS_Date__c FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(fsDateRecordsSoql);
        List<String> quoteProductFSDate = JsonParser2.
                getFieldValueSoql(quoteProductRecordsSoql.toString(), "thn__FS_Date__c");
        List<String> quoteMeetingRoomFSDate = JsonParser2.
                getFieldValueSoql(quoteMeetingRoomRecordsSoql.toString(), "thn__FS_Date__c");
        List<String> dates = JsonParser2.
                getFieldValueSoql(fsDateRecordsSoql.toString(), "thn__Date__c");
        List<String> IdFSDate = JsonParser2.
                getFieldValueSoql(fsDateRecordsSoql.toString(), "Id");
        System.out.println(dates);
        Assert.assertEquals(quoteProductFSDate.size(), 1);
        Assert.assertEquals(quoteMeetingRoomFSDate.size(), 1);
        Assert.assertEquals(quoteProductFSDate.get(0), IdFSDate.get(0));
        Assert.assertEquals(quoteMeetingRoomFSDate.get(0), IdFSDate.get(0));
        Assert.assertEquals(dates.size(), 4);
        Assert.assertEquals(dates.get(0), date.generateTodayDate2_plus(0, 0));
        Assert.assertEquals(dates.get(1), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(dates.get(2), date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(dates.get(3), date.generateTodayDate2_plus(0, 3));
    }

    @Test(priority = 2, description = "Update Quote package, change Start / End date")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-502: Quote Package - FS Dates")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestFSDatesOnQuotePackage'",
                "-u",
                ORG_USERNAME,
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
                ORG_USERNAME,
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
                ORG_USERNAME,
                "--json"});
        System.out.println(update);
        StringBuilder fsDateRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT thn__Date__c, Id FROM thn__FS_Date__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteProductRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT thn__FS_Date__c FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteMeetingRoomRecordsSoql = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT thn__FS_Date__c FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(fsDateRecordsSoql);
        List<String> quoteProductFSDate = JsonParser2.
                getFieldValueSoql(quoteProductRecordsSoql.toString(), "thn__FS_Date__c");
        List<String> quoteMeetingRoomFSDate = JsonParser2.
                getFieldValueSoql(quoteMeetingRoomRecordsSoql.toString(), "thn__FS_Date__c");
        List<String> dates = JsonParser2.
                getFieldValueSoql(fsDateRecordsSoql.toString(), "thn__Date__c");
        List<String> IdFSDate = JsonParser2.
                getFieldValueSoql(fsDateRecordsSoql.toString(), "Id");
        System.out.println(dates);
        Assert.assertEquals(quoteProductFSDate.size(), 2);
        Assert.assertEquals(quoteMeetingRoomFSDate.size(), 2);
        Assert.assertTrue(IdFSDate.contains(quoteProductFSDate.get(0)));
        Assert.assertTrue(IdFSDate.contains(quoteProductFSDate.get(1)));
        Assert.assertTrue(IdFSDate.contains(quoteMeetingRoomFSDate.get(0)));
        Assert.assertTrue(IdFSDate.contains(quoteMeetingRoomFSDate.get(1)));
        /*Assert.assertEquals(quoteProductFSDate.get(0), IdFSDate.get(0));
        Assert.assertEquals(quoteProductFSDate.get(1), IdFSDate.get(1));
        Assert.assertEquals(quoteMeetingRoomFSDate.get(0), IdFSDate.get(0));
        Assert.assertEquals(quoteMeetingRoomFSDate.get(1), IdFSDate.get(1));*/
        Assert.assertEquals(dates.size(), 4);
        Assert.assertEquals(dates.get(0), date.generateTodayDate2_plus(0, 0));
        Assert.assertEquals(dates.get(1), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(dates.get(2), date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(dates.get(3), date.generateTodayDate2_plus(0, 3));
    }

}
