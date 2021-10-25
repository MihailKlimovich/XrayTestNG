package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;

public class MultiDaysPackages extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-607-608: Multi days packages")
    public void logIn() throws InterruptedException, IOException {
        /*StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
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
        System.out.println(authorise);*/
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-607-608: Multi days packages")
    public void preconditions() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Package__c",
                "-w",
                "Name='TestMultiDaysPackagesAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestQuoteMultiDaysPackageAuto'",
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
                "Name='TestMultiDaysPackagesAuto' thn__Hotel__c='" + propertyID + "' thn__Multi_Days__c=true",
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
                        " thn__Unit_Price__c=20 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
                "-u",
                ORG_USERNAME,
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
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
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
                        " thn__Unit_Price__c=40 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
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
                        " thn__Unit_Price__c=50 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
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
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1 thn__AppliedDay__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId6 = JsonParser2.getFieldValue(packageLineResult6.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestQuoteMultiDaysPackageAuto' thn__Pax__c=1 thn__Hotel__c='" + propertyID +
                        "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Stage__c='4 - Closed'" +
                        " thn__Closed_Status__c='Won'",
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
    }

    @Test(priority = 3, description = "Edit the ‘Quote Package Line' with type ‘Activity’ on the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-607-608: Multi days packages")
    public void testMultiDaysPackages1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestQuoteMultiDaysPackageAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Name='Tennis' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageLineRecord);
        String quotePackageLineId = JsonParser2.getFieldValue(quotePackageLineRecord.toString(), "Id");
        StringBuilder updateQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-v",
                "thn__Applied_Day__c=2",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(updateQuotePackageLineRecord);
        StringBuilder updatedQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageLineFromDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__From_Date__c");
        String quotePackageLineToDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__To_Date__c");
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='ACTIVITY' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteProductRecord);
        String fsDateId =JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__FS_Date__c");
        StringBuilder fsDateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__FS_Date__c",
                "-w",
                "Id='" + fsDateId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteProductRecord);
        String fsDateDate =JsonParser2.getFieldValue(fsDateRecord.toString(), "thn__Date__c");
        String quoteProductStartDate = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        Assert.assertEquals(quotePackageLineFromDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quotePackageLineToDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteProductStartDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteProductEndDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(fsDateDate, quoteProductStartDate);
    }

    @Test(priority = 4, description = "Edit the ‘Quote Package Line' with type ‘Equipment’ on the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-607-608: Multi days packages")
    public void testMultiDaysPackages2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestQuoteMultiDaysPackageAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Name='Bicycle' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageLineRecord);
        String quotePackageLineId = JsonParser2.getFieldValue(quotePackageLineRecord.toString(), "Id");
        StringBuilder updateQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-v",
                "thn__Applied_Day__c=2",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(updateQuotePackageLineRecord);
        StringBuilder updatedQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageLineFromDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__From_Date__c");
        String quotePackageLineToDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__To_Date__c");
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='EQUIPMENT' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteProductRecord);
        String fsDateId =JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__FS_Date__c");
        StringBuilder fsDateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__FS_Date__c",
                "-w",
                "Id='" + fsDateId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String fsDateDate =JsonParser2.getFieldValue(fsDateRecord.toString(), "thn__Date__c");
        String quoteProductStartDate = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        Assert.assertEquals(quotePackageLineFromDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quotePackageLineToDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteProductStartDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteProductEndDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(fsDateDate, quoteProductStartDate);
    }

    @Test(priority = 5, description = "Edit the ‘Quote Package Line' with type ‘Beverage’ on the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-607-608: Multi days packages")
    public void testMultiDaysPackages3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestQuoteMultiDaysPackageAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Name='Beer' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageLineRecord);
        String quotePackageLineId = JsonParser2.getFieldValue(quotePackageLineRecord.toString(), "Id");
        StringBuilder updateQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-v",
                "thn__Applied_Day__c=2",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(updateQuotePackageLineRecord);
        StringBuilder updatedQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageLineFromDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__From_Date__c");
        String quotePackageLineToDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__To_Date__c");
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='BEVERAGE' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteProductRecord);
        String fsDateId =JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__FS_Date__c");
        StringBuilder fsDateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__FS_Date__c",
                "-w",
                "Id='" + fsDateId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String fsDateDate =JsonParser2.getFieldValue(fsDateRecord.toString(), "thn__Date__c");
        String quoteProductStartDate = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        Assert.assertEquals(quotePackageLineFromDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quotePackageLineToDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteProductStartDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteProductEndDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(fsDateDate, quoteProductStartDate);
    }

    @Test(priority = 6, description = "Edit the ‘Quote Package Line' with type ‘Food’ on the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-607-608: Multi days packages")
    public void testMultiDaysPackages4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestQuoteMultiDaysPackageAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Name='Burger' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageLineRecord);
        String quotePackageLineId = JsonParser2.getFieldValue(quotePackageLineRecord.toString(), "Id");
        StringBuilder updateQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-v",
                "thn__Applied_Day__c=2",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(updateQuotePackageLineRecord);
        StringBuilder updatedQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageLineFromDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__From_Date__c");
        String quotePackageLineToDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__To_Date__c");
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Name='DINER' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteProductRecord);
        String fsDateId =JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__FS_Date__c");
        StringBuilder fsDateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__FS_Date__c",
                "-w",
                "Id='" + fsDateId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String fsDateDate =JsonParser2.getFieldValue(fsDateRecord.toString(), "thn__Date__c");
        String quoteProductStartDate = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        Assert.assertEquals(quotePackageLineFromDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quotePackageLineToDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteProductStartDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteProductEndDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(fsDateDate, quoteProductStartDate);
    }

    @Test(priority = 7, description = "Edit the ‘Quote Package Line' with type ‘Hotel Room’ on the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-607-608: Multi days packages")
    public void testMultiDaysPackages5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestQuoteMultiDaysPackageAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Name='Hotel Room' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageLineRecord);
        String quotePackageLineId = JsonParser2.getFieldValue(quotePackageLineRecord.toString(), "Id");
        StringBuilder updateQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-v",
                "thn__Applied_Day__c=2",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(updateQuotePackageLineRecord);
        StringBuilder updatedQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageLineFromDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__From_Date__c");
        String quotePackageLineToDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__To_Date__c");
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                "Name='ROOM 1 NIGHT' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteHotelRoomRecord);
        String quoteHotelRoomArrivalDate = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date__c");
        String quoteHotelRoomDepartureDate = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date__c");
        Assert.assertEquals(quotePackageLineFromDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quotePackageLineToDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteHotelRoomArrivalDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteHotelRoomDepartureDate, date.generateTodayDate2_plus(0, 2));
    }

    @Test(priority = 8, description = "Edit the ‘Quote Package Line' with type ‘Hotel Room’ on the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-607-608: Multi days packages")
    public void testMultiDaysPackages6() throws InterruptedException, IOException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestQuoteMultiDaysPackageAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Name='Meeting Room' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageLineRecord);
        String quotePackageLineId = JsonParser2.getFieldValue(quotePackageLineRecord.toString(), "Id");
        StringBuilder updateQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-v",
                "thn__Applied_Day__c=2",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(updateQuotePackageLineRecord);
        StringBuilder updatedQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quotePackageLineFromDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__From_Date__c");
        String quotePackageLineToDate = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__To_Date__c");
        StringBuilder quoteMeetingRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                "Name='DEFAULT - MEETING HALF DAY' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quoteMeetingRoomRecord);
        String fsDateId =JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__FS_Date__c");
        StringBuilder fsDateRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__FS_Date__c",
                "-w",
                "Id='" + fsDateId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String fsDateDate =JsonParser2.getFieldValue(fsDateRecord.toString(), "thn__Date__c");
        String quoteMeetingRoomStartDate = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Date__c");
        String quoteMeetingRoomEndDate = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Date__c");
        Assert.assertEquals(quotePackageLineFromDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quotePackageLineToDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteMeetingRoomStartDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteMeetingRoomEndDate, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(fsDateDate, quoteMeetingRoomStartDate);
    }

    @Test(priority = 9, description = "Edit the ‘Quote Package Line' with type ‘Hotel Room’ on the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-607-608: Multi days packages")
    public void testMultiDaysPackages7() throws InterruptedException, IOException {
        String expectedMessage = "Failed to update record with code FIELD_CUSTOM_VALIDATION_EXCEPTION.";
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestQuoteMultiDaysPackageAuto'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Name='Meeting Room' thn__MYCE_Quote__c='" + quoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageLineRecord);
        String quotePackageLineId = JsonParser2.getFieldValue(quotePackageLineRecord.toString(), "Id");
        StringBuilder updateQuotePackageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + quotePackageLineId + "'",
                "-v",
                "thn__From_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(updateQuotePackageLineRecord);
        String message = JsonParser2.getFieldValue2(updateQuotePackageLineRecord.toString(), "message");
        Assert.assertTrue(message.contains(expectedMessage));
    }

}
