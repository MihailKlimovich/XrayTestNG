package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class MyceQuoteComplimentaryChangePriceCalculationWithHandleUnitField extends BaseTest{


    @Test(priority = 1, description = "Preconditions: create a new ‘MYCE Quote’. Add a ‘Meeting Package’ to it.  Add" +
            " some  ‘Meeting Rooms’.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Myce Quote - Complimentary. Change price calculation with handle Unit field.")
    public void preconditions() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID1 = myceQuotes.createQuoteSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        String packageID1 = packages.createPackageSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest'" +
                " thn__Hotel__c='" + propertyID + "' thn__Custom_Price__c=true thn__Discount_Max__c=60", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID1 +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID1 + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Diner' thn__Package__c='" + packageID1 + "'" +
                " thn__Type__c='Food' thn__Product__c='" + productDinerID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=30 thn__VAT_Category__c=1 thn__VAT_Category__c=1",
                ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID1 + "'" +
                " thn__Package__c='" + packageID1 + "'", ORG_USERNAME);
    }

    @Test(priority = 2, description = "Set the ‘Complimentary’ checkbox on the Quote Package as ‘True’. Result:" +
            " The ‘Meeting Rooms Amount’, ‘Products Amount’, ‘Total Tax’ and ‘Total Amount incl. Tax’ fields  on" +
            " the ‘MYCE Quote’ were set to 0. The Pricing fields ‘Unit Price’, ‘Unit Price incl. Tax’," +
            " ‘Unit Price excl. Tax’, ‘Sales Price incl. Tax’, ‘Sales Price excl. Tax’ on ‘Quote Package’ were also" +
            " set to 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Myce Quote - Complimentary. Change price calculation with handle Unit field.")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=true", ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.getQuotePackageSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Id='" +quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomAmount= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Meeting_Rooms_Amount__c");
        String quoteTotalTax= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Total_Tax__c");
        String quoteTotalAmountInclTax= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        String packageUnitPrice= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price__c");
        String packageUnitPriceInclTax= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        String packageUnitPriceExclTax= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price_excl_Tax__c");
        String packageSalesPriceInclTax= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String packageSalesPriceExclTax= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Sales_Price_excl_Tax__c");
        Assert.assertEquals(quoteMeetingRoomAmount, "0");
        Assert.assertEquals(quoteTotalTax, "0");
        Assert.assertEquals(quoteTotalAmountInclTax, "0");
        Assert.assertEquals(packageUnitPrice, "0");
        Assert.assertEquals(packageUnitPriceInclTax, "0");
        Assert.assertEquals(packageUnitPriceExclTax, "0");
        Assert.assertEquals(packageSalesPriceInclTax, "0");
        Assert.assertEquals(packageSalesPriceExclTax, "0");
    }

    @Test(priority = 3, description = "Set the ‘Complimentary’ checkbox on the Quote Package as ‘false’. Set the" +
            " ‘Discount’ to 20%. On the Quote record ‘Complimentary’ checkbox == true.  Result: Discount should" +
            " change to 100% and ‘Unit Price incl. Tax’, ‘Unit Price excl. Tax’, ‘Sales Price incl. Tax’," +
            " ‘Sales Price excl. Tax’ fields should set to 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Myce Quote - Complimentary. Change price calculation with handle Unit field.")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=false thn__Discount__c=20", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id=" + quoteID + "'", "thn__Complimentary__c=true",
                ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.getQuotePackageSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String packageUnitPriceInclTax= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        String packageUnitPriceExclTax= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price_excl_Tax__c");
        String packageSalesPriceInclTax= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String packageSalesPriceExclTax= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Sales_Price_excl_Tax__c");
        String packageDiscount= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Discount__c");
        Assert.assertEquals(packageUnitPriceInclTax, "0");
        Assert.assertEquals(packageUnitPriceExclTax, "0");
        Assert.assertEquals(packageSalesPriceInclTax, "0");
        Assert.assertEquals(packageSalesPriceExclTax, "0");
        Assert.assertEquals(packageDiscount, "100");
    }

    @Test(priority = 4, description = "Try to set the ‘Complimentary’ checkbox to ‘False’  on the ‘Quote Package’" +
            " when it’s set to ‘True’ on the ‘Quote page’.  Result: ‘Complimentary’ checkbox can’t be unchecked on the" +
            " ‘Quote package’ if it is checked on the ‘Quote’ page.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Myce Quote - Complimentary. Change price calculation with handle Unit field.")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=false", ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.getQuotePackageSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String packageComplimentary= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Complimentary__c");
        Assert.assertEquals(packageComplimentary, "true");
    }

    @Test(priority = 5, description = "Set the ‘Complimentary’ checkbox on the Quote to ‘True’. Add a new ‘Quote" +
            " Product’. Try changing the ‘Discount’ on the created product to 20%. Result: The ‘Discount’ is being" +
            " reverted to 100%.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Myce Quote - Complimentary. Change price calculation with handle Unit field.")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id=" + quoteID + "'", "thn__Complimentary__c=true",
                ORG_USERNAME);
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String winesID = JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + winesID + "'", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "Id='" + quoteProductID + "'",
                "thn__Discount_Percent__c=20", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        String productDiscount= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        Assert.assertEquals(productDiscount, "100");
    }

    @Test(priority = 6, description = "Set the ‘Complimentary’ checkbox on the Quote to ‘True’.Add a new ‘Quote" +
            " Meetings Room’. Try changing the ‘Discount’ on the created product to 20%. Result: The ‘Discount’" +
            " is being reverted to 100%.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Myce Quote - Complimentary. Change price calculation with handle Unit field.")
    public void case5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id=" + quoteID + "'", "thn__Complimentary__c=true",
                ORG_USERNAME);
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String quoteMeetingRoomID = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID + "'",
                "thn__Discount_Percent__c=20", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID + "'", ORG_USERNAME);
        String productDiscount= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        Assert.assertEquals(productDiscount, "100");
    }

    @Test(priority = 7, description = "Set the ‘Complimentary’ checkbox on the Quote to ‘True’.Go to the ‘Quote" +
            " Meetings Room’ and set the ‘Complimentary’ checkbox to ‘False’. Result: When the changes in the record" +
            " are saved The ‘Complimentary’ checkbox is set back to ‘True’.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Myce Quote - Complimentary. Change price calculation with handle Unit field.")
    public void case6() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id=" + quoteID + "'", "thn__Complimentary__c=true",
                ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingFullDayID + "'", "thn__Complimentary__c=false", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        String quoteMeetingRoomComplimentary = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Complimentary__c");
        Assert.assertEquals(quoteMeetingRoomComplimentary, "true");
    }

    @Test(priority = 8, description = "Set the ‘Complimentary’ checkbox on the Quote to ‘True’.Go to the ‘Quote" +
            " Product’ and set the ‘Complimentary’ checkbox to ‘False’. Result: When the changes in the record" +
            " are saved The ‘Complimentary’ checkbox is set back to ‘True’.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Myce Quote - Complimentary. Change price calculation with handle Unit field.")
    public void case7() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryChangePriceCalculationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id=" + quoteID + "'", "thn__Complimentary__c=true",
                ORG_USERNAME);
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String winesID = JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + winesID + "'", "thn__Complimentary__c=false", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + winesID + "'", ORG_USERNAME);
        String quoteProductComplimentary = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Complimentary__c");
        Assert.assertEquals(quoteProductComplimentary, "true");
    }

}
