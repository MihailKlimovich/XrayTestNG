package tests.TB;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import tests.BaseTest;

import java.io.IOException;
import java.util.List;

public class TotalTaxIsNotCalculatedCorrectlyOnOneQuote extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote. Instantiate a Quote Package, Quote products (Subtotals" +
            " are calculated, Total Amount excl. Tax, Total Tax, Total Amount incl. Tax fields on MYCE Quote are" +
            " calculated.) Add a Combo Product. Set checkbox ‘Optional’ = True on Quote product that is combo." +
            " Expected result: The Subtotals are calculated correctly , Total Amount excl. Tax, Total Tax," +
            " Total Amount incl. Tax fields on MYCE Quote are calculated correctly. The optional product in not" +
            " included in the calculations.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-277: Total tax is not calculated correctly on one Quote.")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TotalTaxIsNotCalculatedCorrectlyOnOneQuoteAutoTest'", ORG_USERNAME);
        product.deleteProductSFDX(SFDX, "Name='ComboProductAutoTest", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='TB277AutoTest", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='TB277AutoTest' thn__Hotel__c='" + propertyID +
                "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingFullDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beverage' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Food' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Food' thn__Product__c='" + productDinerID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=35 thn__VAT_Category__c=1", ORG_USERNAME);
        String comboProductId = product.createProductSFDX(SFDX, "Name='ComboProductAutoTest' thn__Hotel__c='"
                + propertyID + "' thn__MYCE_Product_Type__c='Beverage'", ORG_USERNAME);
        String productComponentId1 = product.createProductComboComponentSFDX(SFDX, "thn__Combo__c='"
                + comboProductId + "' thn__Component__c='" + productDinerID + "' thn__Quantity__c=1", ORG_USERNAME);
        String productComponentId2 = product.createProductComboComponentSFDX(SFDX, "thn__Combo__c='"
                + comboProductId + "' thn__Component__c='" + beverageID + "' thn__Quantity__c=1", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TotalTaxIsNotCalculatedCorrectlyOnOneQuoteAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" +
                recordTypeID.get(0) + "' thn__Closed_Status__c='Won'", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        StringBuilder myceQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteTotalAmmountInclTax = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        String quoteTotalAmmountExclTax = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Total_Amount_excl_Tax__c");
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                comboProductId + "' thn__Optional__c=true", ORG_USERNAME);
        StringBuilder updatedMyceQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String updatedQuoteTotalAmmountInclTax = JsonParser2.
                getFieldValue(updatedMyceQuoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        String updatedQuoteTotalAmmountExclTax = JsonParser2.
                getFieldValue(updatedMyceQuoteRecord.toString(), "thn__Total_Amount_excl_Tax__c");
        Assert.assertEquals(updatedQuoteTotalAmmountExclTax, quoteTotalAmmountExclTax);
        Assert.assertEquals(updatedQuoteTotalAmmountInclTax, quoteTotalAmmountInclTax);
    }

}
