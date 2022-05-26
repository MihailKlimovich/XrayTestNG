package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class OptionalProducts extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote. Add Quote meeting room, Quote package and Quote product to" +
            " the Quote, set Optional checkboxes to true on each added record. Expected result: Sales price fields" +
            " on added records are set to 0 (including Quote package child records). Unit price and Pax stay as" +
            " stated initially. Potential max revenue currency field on Quote meeting room, Quote Package and" +
            " Quote Product, MYCE Quote are calculated (SUM (unit price * pax * unit) from quote related records" +
            " where optional = true).")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-546: Optional products")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OptionalProductsAutoTest'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='OptionalProductsPackageAuto", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OptionalProductsAutoTest' thn__Pax__c=3" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='OptionalProductsPackageAuto' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        String quoteMeetingRoomID = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'" + " thn__Product__c='" + meetingHalfDayID + "' thn__Optional__c=true", ORG_USERNAME);
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productDinerID + "' thn__Optional__c=true", ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'" + " thn__Package__c='" + packageID + "' thn__Optional__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'", ORG_USERNAME);
        StringBuilder quoteProductPartPackageRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomOptional = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Optional__c");
        String quoteProductOptional = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Optional__c");
        String quotePackageOptional = JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Optional__c");
        String quoteMeetingRoomSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteProductSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quotePackageSalesPriceInclTax= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteProductPartPackageSalesPriceInclTax= JsonParser2.
                getFieldValue(quoteProductPartPackageRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteTotalAmountInclTax= JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        Integer quoteMeetingRoomPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quoteMeetingRoomRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quoteProductPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quotePackagePotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quotePackageRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quoteProductPartPackagePotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quoteProductPartPackageRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValueLikeInteger(quoteMeetingRoomRecord, "result", "thn__Unit_Price__c");
        Integer quoteProductUnitPrice = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result", "thn__Unit_Price__c");
        Integer quotePackageUnitPrice = JsonParser2.
                getFieldValueLikeInteger(quotePackageRecord, "result", "thn__Unit_Price__c");
        Integer quoteProductPartPackageUnitPrice = JsonParser2.
                getFieldValueLikeInteger(quoteProductPartPackageRecord, "result", "thn__Unit_Price__c");
        Integer quoteMeetingRoomPax = JsonParser2.
                getFieldValueLikeInteger(quoteMeetingRoomRecord, "result", "thn__Pax__c");
        Integer quoteProductPax = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result", "thn__Pax__c");
        Integer quotePackagePax = JsonParser2.
                getFieldValueLikeInteger(quotePackageRecord, "result", "thn__Pax__c");
        Integer quoteProductPartPackagePax = JsonParser2.
                getFieldValueLikeInteger(quoteProductPartPackageRecord, "result", "thn__Pax__c");
        Integer quoteProductUnit = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result", "thn__Unit__c");
        Integer quoteProductPartPackageUnit = JsonParser2.
                getFieldValueLikeInteger(quoteProductPartPackageRecord, "result", "thn__Unit__c");
        Integer expectedQuoteMeetingRoomPotentialMaxRevenue = quoteMeetingRoomUnitPrice*quoteMeetingRoomPax;
        Integer expectedQuotePackagePotentialMaxRevenue = quotePackageUnitPrice*quotePackagePax;
        Integer expectedQuoteProductPotentialMaxRevenue = quoteProductUnitPrice*quoteProductPax*quoteProductUnit;
        Integer expectedQuoteProductPartPackagePotentialMaxRevenue =
                quoteProductPartPackageUnitPrice*quoteProductPartPackagePax*quoteProductPartPackageUnit;
        Assert.assertEquals(quoteMeetingRoomOptional, "true");
        Assert.assertEquals(quoteProductOptional, "true");
        Assert.assertEquals(quotePackageOptional, "true");
        Assert.assertEquals(quoteMeetingRoomSalesPriceInclTax, "0");
        Assert.assertEquals(quoteProductSalesPriceInclTax, "0");
        Assert.assertEquals(quotePackageSalesPriceInclTax, "0");
        Assert.assertEquals(quoteProductPartPackageSalesPriceInclTax, "0");
        Assert.assertEquals(quoteTotalAmountInclTax, "0");
        Assert.assertEquals(quoteMeetingRoomPotentialMaxRevenue, expectedQuoteMeetingRoomPotentialMaxRevenue );
        Assert.assertEquals(quotePackagePotentialMaxRevenue, expectedQuotePackagePotentialMaxRevenue );
        Assert.assertEquals(quoteProductPotentialMaxRevenue, expectedQuoteProductPotentialMaxRevenue );
        //Assert.assertEquals
                //(quoteProductPartPackagePotentialMaxRevenue, expectedQuoteProductPartPackagePotentialMaxRevenue );
    }

    @Test(priority = 2, description = "On added Quote meeting room, Quote package and Quote product change Optional" +
            " field to FALSE. Expected result: Sales price fields are calculated. Potential max revenue currency" +
            " field on Quote meeting room, Quote Package and Quote Product, MYCE Quote are empty.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-546: Optional products")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OptionalProductsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder quotePackageRecord = quoteMeetingPackages.getQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quotePackageID= JsonParser2.getFieldValue(quotePackageRecord.toString(), "Id");
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'",
                "thn__Optional__c=false", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Optional__c=false", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                productDinerID + "'", "thn__Optional__c=false", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        System.out.println(updatedQuoteRecord);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(quoteMeetingRoomRecord);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='"
                        + productDinerID + "", ORG_USERNAME);
        System.out.println(quoteProductRecord);
        StringBuilder quotePackageRecord2 = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'", ORG_USERNAME);
        System.out.println(quotePackageRecord2);
        StringBuilder quoteProductPartPackageRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        System.out.println(quoteProductPartPackageRecord);
        String quoteMeetingRoomOptional = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Optional__c");
        String quoteProductOptional = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Optional__c");
        String quotePackageOptional = JsonParser2.
                getFieldValue(quotePackageRecord2.toString(), "thn__Optional__c");
        String quoteMeetingRoomSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteProductSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quotePackageSalesPriceInclTax= JsonParser2.
                getFieldValue(quotePackageRecord2.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteProductPartPackageSalesPriceInclTax= JsonParser2.
                getFieldValue(quoteProductPartPackageRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteTotalAmountInclTax= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        Integer quoteMeetingRoomPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quoteMeetingRoomRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quoteProductPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quotePackagePotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quotePackageRecord2, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quotePotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(updatedQuoteRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Assert.assertEquals(quoteMeetingRoomOptional, "false");
        Assert.assertEquals(quoteProductOptional, "false");
        Assert.assertEquals(quotePackageOptional, "false");
        Assert.assertEquals(quoteMeetingRoomSalesPriceInclTax, "240");
        Assert.assertEquals(quoteProductSalesPriceInclTax, "330");
        Assert.assertEquals(quotePackageSalesPriceInclTax, "30");
        Assert.assertEquals(quoteProductPartPackageSalesPriceInclTax, "30");
        Assert.assertEquals(quoteTotalAmountInclTax, "600");
        Assert.assertEquals(quoteMeetingRoomPotentialMaxRevenue.intValue(), 0);
        Assert.assertEquals(quotePackagePotentialMaxRevenue.intValue(), 0 );
        Assert.assertEquals(quoteProductPotentialMaxRevenue.intValue(), 0);
        Assert.assertEquals(quotePotentialMaxRevenue.intValue(), 0);
    }

    @Test(priority = 3, description = "Create MYCE Quote. Add Quote meeting room, Quote package and Quote product" +
            " to the Quote. Open added records and set Optional checkboxes to true on each added record. Expected" +
            " result: Sales price fields on added records are set to 0 (including Quote package child records)." +
            " Unit price and Pax stay as stated initially. Potential max revenue currency field on Quote meeting" +
            " room, Quote Package and Quote Product, MYCE Quote are calculated (SUM (unit price" +
            " * pax * unit) from quote related records where optional = true).")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-546: Optional products")
    public void case3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OptionalProductsAutoTest2'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='OptionalProductsPackageAuto2", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OptionalProductsAutoTest2' thn__Pax__c=3" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='OptionalProductsPackageAuto2' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        String quoteMeetingRoomID = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'" + " thn__Product__c='" + meetingHalfDayID + "'", ORG_USERNAME);
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productDinerID + "'", ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'" + " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID + "'",
                "thn__Price_per_Person__c=true", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID + "'",
                "thn__Optional__c=true", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "Id='" + quoteProductID + "'",
                "thn__Optional__c=true",ORG_USERNAME);
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'",
                "thn__Optional__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'", ORG_USERNAME);
        StringBuilder quoteProductPartPackageRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomOptional = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Optional__c");
        String quoteProductOptional = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Optional__c");
        String quotePackageOptional = JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Optional__c");
        String quoteMeetingRoomSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteProductSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quotePackageSalesPriceInclTax= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteProductPartPackageSalesPriceInclTax= JsonParser2.
                getFieldValue(quoteProductPartPackageRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteTotalAmountInclTax= JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        Integer quoteMeetingRoomPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quoteMeetingRoomRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quoteProductPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quotePackagePotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quotePackageRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quoteProductPartPackagePotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quoteProductPartPackageRecord, "result",
                        "thn__Potential_max_revenue_field__c");
        Integer quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValueLikeInteger(quoteMeetingRoomRecord, "result", "thn__Unit_Price__c");
        Integer quoteProductUnitPrice = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result", "thn__Unit_Price__c");
        Integer quotePackageUnitPrice = JsonParser2.
                getFieldValueLikeInteger(quotePackageRecord, "result", "thn__Unit_Price__c");
        Integer quoteProductPartPackageUnitPrice = JsonParser2.
                getFieldValueLikeInteger(quoteProductPartPackageRecord, "result", "thn__Unit_Price__c");
        Integer quoteMeetingRoomPax = JsonParser2.
                getFieldValueLikeInteger(quoteMeetingRoomRecord, "result", "thn__Pax__c");
        Integer quoteProductPax = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result", "thn__Pax__c");
        Integer quotePackagePax = JsonParser2.
                getFieldValueLikeInteger(quotePackageRecord, "result", "thn__Pax__c");
        Integer quoteProductUnit = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result", "thn__Unit__c");
        Integer expectedQuoteMeetingRoomPotentialMaxRevenue = quoteMeetingRoomUnitPrice*quoteMeetingRoomPax;
        Integer expectedQuotePackagePotentialMaxRevenue = quotePackageUnitPrice*quotePackagePax;
        Integer expectedQuoteProductPotentialMaxRevenue = quoteProductUnitPrice*quoteProductPax*quoteProductUnit;
        Assert.assertEquals(quoteMeetingRoomOptional, "true");
        Assert.assertEquals(quoteProductOptional, "true");
        Assert.assertEquals(quotePackageOptional, "true");
        Assert.assertEquals(quoteMeetingRoomSalesPriceInclTax, "0");
        Assert.assertEquals(quoteProductSalesPriceInclTax, "0");
        Assert.assertEquals(quotePackageSalesPriceInclTax, "0");
        Assert.assertEquals(quoteProductPartPackageSalesPriceInclTax, "0");
        Assert.assertEquals(quoteTotalAmountInclTax, "0");
        Assert.assertEquals(quoteMeetingRoomPotentialMaxRevenue, expectedQuoteMeetingRoomPotentialMaxRevenue );
        Assert.assertEquals(quotePackagePotentialMaxRevenue, expectedQuotePackagePotentialMaxRevenue );
        Assert.assertEquals(quoteProductPotentialMaxRevenue, expectedQuoteProductPotentialMaxRevenue );

    }

    @Test(priority = 4, description = "On the Quote that have related Optional records update stage to Closed Won." +
            " Make sure Delete optional products in Default Agile Value custom metadata type is set to true. Expected" +
            " result: All Optional products are deleted from the Quote. Potential max revenue currency fields on the" +
            " Quote are cleared.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-546: Optional products")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OptionalProductsAutoTest2'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecords = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder quoteProductRecords = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder quotePackageRecords = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(quoteMeetingRoomRecords);
        String message1 = JsonParser2.getFieldValue2(quoteMeetingRoomRecords.toString(), "message");
        String message2 = JsonParser2.getFieldValue2(quoteProductRecords.toString(), "message");
        String message3 = JsonParser2.getFieldValue2(quotePackageRecords.toString(), "message");
        Assert.assertEquals(message1, "No matching record found");
        Assert.assertEquals(message2, "No matching record found");
        Assert.assertEquals(message3, "No matching record found");
    }

}
