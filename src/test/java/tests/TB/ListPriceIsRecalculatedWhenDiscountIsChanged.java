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

public class ListPriceIsRecalculatedWhenDiscountIsChanged extends BaseTest {

    @Test(priority = 1, description = "Create a Package which is: Multi Days = True, Custom Price = True, Discount" +
            " max = 100 and it has package lines: Package line 1: unit price = 100, Apply Discount = true, Applied" +
            " Day = 1; Package line 2: unit price = 50, Apply Discount = true, Applied Day = 2; Package line 3:" +
            " unit price = 200, Apply Discount = false, Applied Day = 3. Create a MYCE Quote. Instantiate the created" +
            " Quote Package. Change the discount on the Quote package to 10%. Expected result: List price of the" +
            " Quote Package is not being recalculated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-253: List price is recalculated when discount is changed")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ListPriceIsRecalculatedWhenDiscountIsChangedAutoTest'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='ListPriceIsRecalculatedPackageAutoTest", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder foodRecord = product.getProductSFDX(SFDX, "Name='FOOD'", ORG_USERNAME);
        String productFoodID= JsonParser2.getFieldValue(foodRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='ListPriceIsRecalculatedWhenDiscountIsChangedAutoTest' thn__Pax__c=5 thn__Hotel__c='" +
                        propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c="
                        + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='" + recordTypeID.get(0) +
                        "'", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='ListPriceIsRecalculatedPackageAutoTest'" +
                " thn__Hotel__c='" + propertyID + "' thn__Multi_Days__c=true thn__Custom_Price__c=true" +
                " thn__Discount_Max__c=100", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__VAT_Category__c=0 thn__Apply_Discount__c=true" +
                " thn__AppliedDay__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Diner' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Food' thn__Product__c='" + productDinerID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=50 thn__VAT_Category__c=0 thn__Apply_Discount__c=true" +
                " thn__AppliedDay__c=2", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Food' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Food' thn__Product__c='" + productFoodID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=200 thn__VAT_Category__c=0 thn__Apply_Discount__c=false" +
                " thn__AppliedDay__c=3", ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "'", ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.getQuotePackageSFDX(SFDX, "Id='" +
                quotePackageID + "'", ORG_USERNAME);
        String quotePackageListPrice = JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__List_Price__c");
        String quotePackageDiscount = JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Discount__c");
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'",
                "thn__Discount__c=10", ORG_USERNAME);
        StringBuilder updatedQuotePackageRecord = quoteMeetingPackages.getQuotePackageSFDX(SFDX, "Id='" +
                quotePackageID + "'", ORG_USERNAME);
        String updatedQuotePackageListPrice = JsonParser2.
                getFieldValue(updatedQuotePackageRecord.toString(), "thn__List_Price__c");
        String updatedQuotePackageDiscount = JsonParser2.
                getFieldValue(updatedQuotePackageRecord.toString(), "thn__Discount__c");
        Assert.assertEquals(quotePackageDiscount, "0");
        Assert.assertEquals(updatedQuotePackageDiscount, "10");
        Assert.assertEquals(updatedQuotePackageListPrice, quotePackageListPrice);
    }

}
