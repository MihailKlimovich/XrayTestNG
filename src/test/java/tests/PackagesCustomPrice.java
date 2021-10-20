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

public class PackagesCustomPrice extends BaseTest {

    @Test(priority = 1, description = "THY-588: Packages - custom price")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-588: Packages - custom price")
    @Story("Case 1: Delete a ‘Quote Package Line’.")
    public void packageCustomPrice_DeletePackageLine() throws InterruptedException, IOException {
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
        StringBuilder productRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='FOOD'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String foodID = JsonParser2.getFieldValue(productRecord1.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='BEVERAGE'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String beverageID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder productRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='EQUIPMENT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String equipmentID = JsonParser2.getFieldValue(productRecord3.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestPackagesCustomPrice' thn__Hotel__c='" + propertyID + "' thn__Custom_Price__c=true",
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
                "Name='Fish and cheeps' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + foodID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
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
                "Name='Soda' thn__Package__c='" + packageId + "' thn__Type__c='Beverage'" +
                        " thn__Product__c='" + beverageID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
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
                        " thn__Product__c='" + beverageID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=15 thn__VAT_Category__c=1",
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
                "Name='Car' thn__Package__c='" + packageId + "' thn__Type__c='Equipment'" +
                        " thn__Product__c='" + equipmentID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=100 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId4 = JsonParser2.getFieldValue(packageLineResult4.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestPackageCustomPrice1' thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
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
        StringBuilder quotePackageLineRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId1 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        //System.out.println(quotePackageLineRecord1);
        //String quotePackageLineId1 = JsonParser2.getFieldValue(quotePackageResult.toString(), "Id");
        StringBuilder quotePackageLineRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId2 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageLineRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId3 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageLineRecord4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId4 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        Integer listPriceQuotePackLine1 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord1, "result", "thn__List_Price__c");
        Integer listPriceQuotePackLine2 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord2, "result", "thn__List_Price__c");
        Integer listPriceQuotePackLine3 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord3, "result", "thn__List_Price__c");
        Integer listPriceQuotePackLine4 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord4, "result", "thn__List_Price__c");
        Integer listPriceQuotePackage = JsonParser2.getFieldValueLikeInteger(quotePackageRecord, "result", "thn__List_Price__c");
        Integer sumPricePackLine1 = listPriceQuotePackLine1 + listPriceQuotePackLine2 + listPriceQuotePackLine3 + listPriceQuotePackLine4;
        Assert.assertEquals(listPriceQuotePackage, sumPricePackLine1);
        StringBuilder deleteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId1 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(deleteResult);
        StringBuilder quotePackageRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        Integer listPriceQuotePackage2 = JsonParser2.getFieldValueLikeInteger(quotePackageRecord2, "result", "thn__List_Price__c");
        Integer sumPricePackLine2 = listPriceQuotePackLine2 + listPriceQuotePackLine3 + listPriceQuotePackLine4;
        Assert.assertEquals(listPriceQuotePackage2, sumPricePackLine2);
    }

    @Test(priority = 2, description = "THY-588: Packages - custom price")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-588: Packages - custom price")
    @Story("Case 2: Users should be able to create new ‘Package Lines’ on created Quote.")
    public void packageCustomPrice_addQuotePackageLine() throws InterruptedException, IOException {
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
        StringBuilder productRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='FOOD'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String foodID = JsonParser2.getFieldValue(productRecord1.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='BEVERAGE'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String beverageID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder productRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='EQUIPMENT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String equipmentID = JsonParser2.getFieldValue(productRecord3.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestPackagesCustomPrice2' thn__Hotel__c='" + propertyID + "' thn__Custom_Price__c=true",
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
                "Name='Fish and cheeps' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + foodID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
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
                "Name='Soda' thn__Package__c='" + packageId + "' thn__Type__c='Beverage'" +
                        " thn__Product__c='" + beverageID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
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
                        " thn__Product__c='" + beverageID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=15 thn__VAT_Category__c=1",
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
                "Name='Car' thn__Package__c='" + packageId + "' thn__Type__c='Equipment'" +
                        " thn__Product__c='" + equipmentID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=100 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId4 = JsonParser2.getFieldValue(packageLineResult4.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestPackageCustomPrice2' thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
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
        StringBuilder quotePackageLineRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId1 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        //System.out.println(quotePackageLineRecord1);
        //String quotePackageLineId1 = JsonParser2.getFieldValue(quotePackageResult.toString(), "Id");
        StringBuilder quotePackageLineRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId2 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageLineRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId3 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageLineRecord4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId4 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        Integer listPriceQuotePackLine1 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord1, "result", "thn__List_Price__c");
        Integer listPriceQuotePackLine2 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord2, "result", "thn__List_Price__c");
        Integer listPriceQuotePackLine3 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord3, "result", "thn__List_Price__c");
        Integer listPriceQuotePackLine4 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord4, "result", "thn__List_Price__c");
        Integer listPriceQuotePackage = JsonParser2.getFieldValueLikeInteger(quotePackageRecord, "result", "thn__List_Price__c");
        Integer sumPricePackLine1 = listPriceQuotePackLine1 + listPriceQuotePackLine2 + listPriceQuotePackLine3 + listPriceQuotePackLine4;
        Assert.assertEquals(listPriceQuotePackage, sumPricePackLine1);
        StringBuilder quotePackageLineResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package_Line__c",
                "-v",
                "Name='Meat' thn__Quote_Package__c='" + quotePackageId + "' thn__MYCE_Quote__c='" + myceQuoteID +
                        "' thn__Product__c='" + foodID + "' thn__Type__c='Food' thn__Unit_Price__c=20" +
                        " thn__Start_Time__c='01:00' thn__End_Time__c='02:00'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String addedQuotePackageLineId = JsonParser2.getFieldValue(quotePackageLineResult.toString(), "id");
        StringBuilder quotePackageLineRecord5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + addedQuotePackageLineId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageLineRecord5);
        StringBuilder quotePackageRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageRecord2);
        double unitPriceExclTaxQuotePackage = JsonParser2.getFieldValueLikeDouble(quotePackageRecord2, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackage = JsonParser2.getFieldValueLikeDouble(quotePackageRecord2, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackage = JsonParser2.getFieldValueLikeDouble(quotePackageRecord2, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackage = JsonParser2.getFieldValueLikeDouble(quotePackageRecord2, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine1 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord1, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine1 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord1, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine1 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord1, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine1 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord1, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine2 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord2, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine2 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord2, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine2 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord2, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine2 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord2, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine3 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord3, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine3 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord3, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine3 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord3, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine3 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord3, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine4 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord4, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine4 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord4, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine4 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord4, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine4 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord4, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine5 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord5, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine5 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord5, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine5 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord5, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine5 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord5, "result", "thn__Sales_Price_incl_Tax__c");
        double sumUnitPriceExclTaxQuotePackageLines = unitPriceExclTaxQuotePackageLine1 + unitPriceExclTaxQuotePackageLine2 +unitPriceExclTaxQuotePackageLine3 + unitPriceExclTaxQuotePackageLine4 + unitPriceExclTaxQuotePackageLine5;
        double sumUnitPriceInclTaxQuotePackageLines = unitPriceInclTaxQuotePackageLine1 + unitPriceInclTaxQuotePackageLine2 + unitPriceInclTaxQuotePackageLine3 + unitPriceInclTaxQuotePackageLine4 + unitPriceInclTaxQuotePackageLine5;
        double sumSalesPriceExclTaxQuotePackageLines = salesPriceExclTaxQuotePackageLine1 + salesPriceExclTaxQuotePackageLine2 + salesPriceExclTaxQuotePackageLine3 +salesPriceExclTaxQuotePackageLine4 + salesPriceExclTaxQuotePackageLine5;
        double sumSalesPriceInclTaxQuotePackageLines = salesPriceInclTaxQuotePackageLine1 + salesPriceInclTaxQuotePackageLine2 + salesPriceInclTaxQuotePackageLine3 + salesPriceInclTaxQuotePackageLine4 + salesPriceInclTaxQuotePackageLine5;
        Assert.assertEquals(unitPriceExclTaxQuotePackage, sumUnitPriceExclTaxQuotePackageLines);
        Assert.assertEquals(unitPriceInclTaxQuotePackage, sumUnitPriceInclTaxQuotePackageLines);
        Assert.assertEquals(salesPriceExclTaxQuotePackage, sumSalesPriceExclTaxQuotePackageLines);
        Assert.assertEquals(salesPriceInclTaxQuotePackage, sumSalesPriceInclTaxQuotePackageLines);
    }

    @Test(priority = 3, description = "THY-588: Packages - custom price")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-588: Packages - custom price")
    @Story("Case 3: Add a ‘Discount’ to our ‘Quote Package’. Add more ‘Quote Package Lines’ that have ‘Apply Discount’ set to ‘true’.")
    public void packageCustomPrice_Discount() throws InterruptedException, IOException {
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
        StringBuilder productRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='FOOD'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String foodID = JsonParser2.getFieldValue(productRecord1.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='BEVERAGE'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String beverageID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder productRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='EQUIPMENT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String equipmentID = JsonParser2.getFieldValue(productRecord3.toString(), "Id");
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Package__c",
                "-v",
                "Name='TestPackagesCustomPrice3' thn__Hotel__c='" + propertyID + "' thn__Custom_Price__c=true thn__Discount_Max__c=100",
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
                "Name='Fish and cheeps' thn__Package__c='" + packageId + "' thn__Type__c='Food'" +
                        " thn__Product__c='" + foodID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
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
                "Name='Soda' thn__Package__c='" + packageId + "' thn__Type__c='Beverage'" +
                        " thn__Product__c='" + beverageID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=10 thn__VAT_Category__c=1",
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
                        " thn__Product__c='" + beverageID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=15 thn__VAT_Category__c=1",
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
                "Name='Car' thn__Package__c='" + packageId + "' thn__Type__c='Equipment'" +
                        " thn__Product__c='" + equipmentID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00" +
                        " thn__Unit_Price__c=100 thn__VAT_Category__c=1",
                "-u",
                ORG_USERNAME,
                "--json"});
        String packageLineId4 = JsonParser2.getFieldValue(packageLineResult4.toString(), "id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestPackageCustomPrice3' thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
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
        StringBuilder quotePackageLineRecord1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId1 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        //System.out.println(quotePackageLineRecord1);
        //String quotePackageLineId1 = JsonParser2.getFieldValue(quotePackageResult.toString(), "Id");
        StringBuilder quotePackageLineRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId2 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageLineRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId3 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageLineRecord4 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "thn__Package_Line__c='" + packageLineId4 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        Integer listPriceQuotePackLine1 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord1, "result", "thn__List_Price__c");
        Integer listPriceQuotePackLine2 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord2, "result", "thn__List_Price__c");
        Integer listPriceQuotePackLine3 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord3, "result", "thn__List_Price__c");
        Integer listPriceQuotePackLine4 = JsonParser2.getFieldValueLikeInteger(quotePackageLineRecord4, "result", "thn__List_Price__c");
        Integer listPriceQuotePackage = JsonParser2.getFieldValueLikeInteger(quotePackageRecord, "result", "thn__List_Price__c");
        Integer sumPricePackLine1 = listPriceQuotePackLine1 + listPriceQuotePackLine2 + listPriceQuotePackLine3 + listPriceQuotePackLine4;
        Assert.assertEquals(listPriceQuotePackage, sumPricePackLine1);
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-v",
                "thn__Discount__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageLineResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package_Line__c",
                "-v",
                "Name='Meat' thn__Quote_Package__c='" + quotePackageId + "' thn__MYCE_Quote__c='" + myceQuoteID +
                        "' thn__Product__c='" + foodID + "' thn__Type__c='Food' thn__Unit_Price__c=20 " +
                        "thn__Apply_Discount__c=true thn__Start_Time__c='01:00' thn__End_Time__c='02:00",
                "-u",
                ORG_USERNAME,
                "--json"});
        String addedQuotePackageLineId1 = JsonParser2.getFieldValue(quotePackageLineResult1.toString(), "id");
        //System.out.println(quotePackageLineResult1);
        StringBuilder quotePackageLineResult2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package_Line__c",
                "-v",
                "Name='Bicycle' thn__Quote_Package__c='" + quotePackageId + "' thn__MYCE_Quote__c='" + myceQuoteID +
                        "' thn__Product__c='" + equipmentID + "' thn__Type__c='Equipment' thn__Unit_Price__c=50 " +
                        "thn__Apply_Discount__c=true thn__Start_Time__c='01:00' thn__End_Time__c='02:00",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageLineResult2);
        String addedQuotePackageLineId2 = JsonParser2.getFieldValue(quotePackageLineResult2.toString(), "id");
        StringBuilder quotePackageLineRecord5 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + addedQuotePackageLineId1 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageLineRecord6 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package_Line__c",
                "-w",
                "Id='" + addedQuotePackageLineId2 + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quotePackageRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Package__c",
                "-w",
                "Id='" + quotePackageId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(quotePackageRecord2);
        System.out.println(quotePackageLineRecord6);
        double discountQuotePackageLine5 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord5, "result", "thn__Discount__c");
        double discountQuotePackageLine6 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord6, "result", "thn__Discount__c");
        double discountQuotePackage = JsonParser2.getFieldValueLikeDouble(quotePackageRecord2, "result", "thn__Discount_Amount__c");
        double sumDiscountQuotePackageLines = discountQuotePackageLine5 + discountQuotePackageLine6;
        double unitPriceExclTaxQuotePackage = JsonParser2.getFieldValueLikeDouble(quotePackageRecord2, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackage = JsonParser2.getFieldValueLikeDouble(quotePackageRecord2, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackage = JsonParser2.getFieldValueLikeDouble(quotePackageRecord2, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackage = JsonParser2.getFieldValueLikeDouble(quotePackageRecord2, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine1 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord1, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine1 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord1, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine1 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord1, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine1 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord1, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine2 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord2, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine2 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord2, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine2 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord2, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine2 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord2, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine3 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord3, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine3 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord3, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine3 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord3, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine3 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord3, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine4 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord4, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine4 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord4, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine4 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord4, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine4 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord4, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine5 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord5, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine5 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord5, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine5 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord5, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine5 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord5, "result", "thn__Sales_Price_incl_Tax__c");
        double unitPriceExclTaxQuotePackageLine6 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord6, "result", "thn__Unit_Price_excl_Tax__c");
        double unitPriceInclTaxQuotePackageLine6 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord6, "result", "thn__Unit_Price_incl_Tax__c");
        double salesPriceExclTaxQuotePackageLine6 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord6, "result", "thn__Sales_Price_excl_Tax__c");
        double salesPriceInclTaxQuotePackageLine6 = JsonParser2.getFieldValueLikeDouble(quotePackageLineRecord6, "result", "thn__Sales_Price_incl_Tax__c");
        double sumUnitPriceExclTaxQuotePackageLines = unitPriceExclTaxQuotePackageLine1 + unitPriceExclTaxQuotePackageLine2 +unitPriceExclTaxQuotePackageLine3 + unitPriceExclTaxQuotePackageLine4 + unitPriceExclTaxQuotePackageLine5 + unitPriceExclTaxQuotePackageLine6;
        double sumUnitPriceInclTaxQuotePackageLines = unitPriceInclTaxQuotePackageLine1 + unitPriceInclTaxQuotePackageLine2 + unitPriceInclTaxQuotePackageLine3 + unitPriceInclTaxQuotePackageLine4 + unitPriceInclTaxQuotePackageLine5 + unitPriceInclTaxQuotePackageLine6;
        double sumSalesPriceExclTaxQuotePackageLines = salesPriceExclTaxQuotePackageLine1 + salesPriceExclTaxQuotePackageLine2 + salesPriceExclTaxQuotePackageLine3 +salesPriceExclTaxQuotePackageLine4 + salesPriceExclTaxQuotePackageLine5 + salesPriceExclTaxQuotePackageLine6;
        double sumSalesPriceInclTaxQuotePackageLines = salesPriceInclTaxQuotePackageLine1 + salesPriceInclTaxQuotePackageLine2 + salesPriceInclTaxQuotePackageLine3 + salesPriceInclTaxQuotePackageLine4 + salesPriceInclTaxQuotePackageLine5 + salesPriceInclTaxQuotePackageLine6;
        Assert.assertEquals(unitPriceExclTaxQuotePackage, sumUnitPriceExclTaxQuotePackageLines);
        Assert.assertEquals(unitPriceInclTaxQuotePackage, sumUnitPriceInclTaxQuotePackageLines);
        Assert.assertEquals(salesPriceExclTaxQuotePackage, sumSalesPriceExclTaxQuotePackageLines);
        Assert.assertEquals(salesPriceInclTaxQuotePackage, sumSalesPriceInclTaxQuotePackageLines);
        Assert.assertEquals(discountQuotePackage, sumDiscountQuotePackageLines);
    }

}
