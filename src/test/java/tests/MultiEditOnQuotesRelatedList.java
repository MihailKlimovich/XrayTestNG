package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;

public class MultiEditOnQuotesRelatedList extends BaseTest {

    @Test(priority = 1, description = "THY-516: Multi edit on quote's related list")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-516: Multi edit on quote's related list")
    @Story("THY-516: Multi edit on quote's related list")
    public void RequestAgent_case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg(driver);
        SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:update",
                "-s",
                "User",
                "-w",
                "Name='User User'",
                "-v",
                "thn__ByPassVR__c=true",
                "-u",
                ALIAS,
                "--json"});
        StringBuilder res = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:update",
                "-s",
                "thn__bypass__c",
                "-w",
                "Id='a061j000003e6IcAAI'",
                "-v",
                "thn__bypassvr__c=true",
                "-u",
                ALIAS,
                "--json"});


        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                ALIAS,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ALIAS,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecord1.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='DINER'",
                "-u",
                ALIAS,
                "--json"});
        String dinerID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder productRecord3 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING FULL DAY'",
                "-u",
                ALIAS,
                "--json"});
        String meetingFullDayID = JsonParser2.getFieldValue(productRecord3.toString(), "Id");
        StringBuilder productRecord4 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='FOOD'",
                "-u",
                ALIAS,
                "--json"});
        String foodID = JsonParser2.getFieldValue(productRecord4.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestMultiEdit' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ALIAS,
                "--json"});
        String packageId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        StringBuilder packageLineResult1 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestPackLineMultiEdit1' thn__Package__c='" + packageId + "' thn__Type__c='Meeting Room'" +
                        " thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00 thn__End_Time__c=18:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ALIAS,
                "--json"});
        StringBuilder packageLineResult2 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestPackLineMultiEdit2' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + dinerID + "' thn__Start_Time__c=12:00 thn__End_Time__c=18:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ALIAS,
                "--json"});
        StringBuilder packageLineResult3 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestPackLineMultiEdit3' thn__Package__c='" + packageId + "' thn__Type__c='Meeting Room'" +
                        " thn__Product__c='" + meetingFullDayID + "' thn__Start_Time__c=12:00 thn__End_Time__c=18:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ALIAS,
                "--json"});
        StringBuilder packageLineResult4 = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                "Name='TestPackLineMultiEdit4' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + foodID + "' thn__Start_Time__c=12:00 thn__End_Time__c=18:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
                "-u",
                ALIAS,
                "--json"});
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestMultiEdit1' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                ALIAS,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Package__c='" + packageId +
                        "' thn__Unit_Price__c=10",
                "-u",
                ALIAS,
                "--json"});
        String quotePackageId = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        homePageForScratchOrg.openAppLauncher();
        homePageForScratchOrg.sendTextInAppWindow("MYCE Quotes");
        myceQuotes.openMyceQoteRecord("TestMultiEdit1");
        myceQuotes.openProducts();
        quoteProducts.selectAllItems("2");
        quoteProducts.clickMultiedit();
        multiEditProducts.editProducts();

    }

}
