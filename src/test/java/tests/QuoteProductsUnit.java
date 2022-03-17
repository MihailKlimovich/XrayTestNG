package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteProductsUnit extends BaseTest {


    @Test(priority = 1, description = "Create Package, Create package line, type == Food, Unit == 5, Unit Price" +
            " == 100. Result: Total price is 500 (Unit price (100) * Unit (5)). List Price on Package is changed" +
            " to 500.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        packages.deletePackageSFDX(SFDX, "Name='QuoteProductsUnitAutoTest", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='QuoteProductsUnitAutoTest' thn__Hotel__c='" +
                propertyID + "' thn__Active__c=true thn__Custom_Price__c=true thn__Discount_Max__c=1000", ORG_USERNAME);
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        String plID1 = packageLine.createPackageLineSFDX(SFDX, "Name='Food' thn__Package__c='" + packageID + "'" +
                        " thn__Type__c='Food' thn__Product__c='" + productDinerID + "' thn__Start_Time__c=15:00" +
                        " thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__Unit__c=5 thn__VAT_Category__c=0" +
                        " thn__Apply_Discount__c=true",
                ORG_USERNAME);
        StringBuilder packageRecord = packages.
                getPackageSFDX(SFDX, "Id='" + packageID + "'", ORG_USERNAME );
        String packagePrice = JsonParser2.getFieldValue(packageRecord.toString(), "thn__List_Price_new__c");
        Assert.assertEquals(packagePrice, "500");
    }

    @Test(priority = 2, description = "Create Package line, type == Beverage, Unit == null, Unit Price ==100. Result:" +
            " Unit is set to “1”. Total price is 100 (Unit price (100) * Unit (1)). List Price on Package is changed" +
            " to 600.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case2() throws InterruptedException, IOException {
        StringBuilder packageRecord = packages.getPackageSFDX(SFDX, "Name='QuoteProductsUnitAutoTest",
                ORG_USERNAME);
        System.out.println(packageRecord);
        String packageId = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        String packageLineId = packageLine.createPackageLineSFDX(SFDX, "Name='Beverage' thn__Package__c='"
                + packageId + "' thn__Type__c='Beverage' thn__Product__c='" + beverageID + "'" +
                " thn__Start_Time__c=15:00 thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__VAT_Category__c=0" +
                        " thn__Apply_Discount__c=true",
                ORG_USERNAME);
        StringBuilder packageLineRecord = packageLine.
                getPackageLineSFDX(SFDX, "Id='" + packageLineId + "'", ORG_USERNAME);
        String packageLineUnit = JsonParser2.getFieldValue(packageLineRecord.toString(), "thn__Unit__c");
        StringBuilder updatedPackageRecord = packages.
                getPackageSFDX(SFDX, "Id='" + packageId + "'", ORG_USERNAME );
        String packagePrice = JsonParser2.getFieldValue(updatedPackageRecord.toString(), "thn__List_Price_new__c");
        Assert.assertEquals(packageLineUnit, "1");
        Assert.assertEquals(packagePrice, "600");
    }

    @Test(priority = 3, description = " Create Package line, type == Equipment, Unit == -5, Unit price == 100." +
            " Result: Unit is set to “1”. Total price is 100 (Unit price (100) * Unit (1)). List Price on Package" +
            " is changed to 700.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case3() throws InterruptedException, IOException {
        StringBuilder packageRecord = packages.getPackageSFDX(SFDX, "Name='QuoteProductsUnitAutoTest",
                ORG_USERNAME);
        System.out.println(packageRecord);
        String packageId = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder equipmentRecord = product.getProductSFDX(SFDX, "Name='EQUIPMENT'", ORG_USERNAME);
        String equipmentID = JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        String packageLineId = packageLine.createPackageLineSFDX(SFDX, "Name='Equipment' thn__Package__c='"
                + packageId + "' thn__Type__c='Equipment' thn__Product__c='" + equipmentID + "'" +
                " thn__Start_Time__c=15:00 thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__Unit__c=-5" +
                " thn__VAT_Category__c=0 thn__Apply_Discount__c=true", ORG_USERNAME);
        StringBuilder packageLineRecord = packageLine.
                getPackageLineSFDX(SFDX, "Id='" + packageLineId + "'", ORG_USERNAME);
        String packageLineUnit = JsonParser2.getFieldValue(packageLineRecord.toString(), "thn__Unit__c");
        StringBuilder updatedPackageRecord = packages.
                getPackageSFDX(SFDX, "Id='" + packageId + "'", ORG_USERNAME );
        String packagePrice = JsonParser2.getFieldValue(updatedPackageRecord.toString(), "thn__List_Price_new__c");
        Assert.assertEquals(packageLineUnit, "1");
        Assert.assertEquals(packagePrice, "700");
    }

    @Test(priority = 4, description = "Delete Package line, Equipment. Result: List Price is updated to 600.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case4() throws InterruptedException, IOException {
        StringBuilder packageRecord = packages.getPackageSFDX(SFDX, "Name='QuoteProductsUnitAutoTest",
                ORG_USERNAME);
        String packageId = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder equipmentRecord = product.getProductSFDX(SFDX, "Name='EQUIPMENT'", ORG_USERNAME);
        String equipmentID = JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        packageLine.deletePackageLineSFDX(SFDX, "thn__Package__c='" + packageId + "' thn__Product__c='"
                + equipmentID + "'", ORG_USERNAME);
        StringBuilder updatedPackageRecord = packages.
                getPackageSFDX(SFDX, "Id='" + packageId + "'", ORG_USERNAME );
        String packagePrice = JsonParser2.getFieldValue(updatedPackageRecord.toString(), "thn__List_Price_new__c");
        Assert.assertEquals(packagePrice, "600");
    }

    @Test(priority = 5, description = "Update Package line Unit, set Unit on Beverage to 10. Result: Total price" +
            " is 1000 (Unit price (100) * Unit (10). List Price on Package is changed to 1500.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case5() throws InterruptedException, IOException {
        StringBuilder packageRecord = packages.getPackageSFDX(SFDX, "Name='QuoteProductsUnitAutoTest",
                ORG_USERNAME);
        String packageId = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        packageLine.updatePackageLineSFDX(SFDX, "thn__Package__c='" + packageId + "' thn__Product__c='"
                + beverageID + "'", "thn__Unit__c=10", ORG_USERNAME);
        StringBuilder updatedPackageRecord = packages.
                getPackageSFDX(SFDX, "Id='" + packageId + "'", ORG_USERNAME );
        String packagePrice = JsonParser2.getFieldValue(updatedPackageRecord.toString(), "thn__List_Price_new__c");
        Assert.assertEquals(packagePrice, "1500");
    }

    @Test(priority = 6, description = " Create Package line, product == Combo, Unit ==  5, Unit Price == 150." +
            " Result: Unit is set to “1”. Total price is 150 (Unit price (150) * Unit (1)). List Price on Package" +
            " is changed to 1650.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case6() throws InterruptedException, IOException {
        product.deleteProductSFDX(SFDX, "Name='ComboAutoTest2", ORG_USERNAME);
        product.deleteProductSFDX(SFDX, "Name='Absinthe", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder packageRecord = packages.getPackageSFDX(SFDX, "Name='QuoteProductsUnitAutoTest",
                ORG_USERNAME);
        String packageId = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        String whiskeyProductID = product.createProductSFDX(SFDX, "Name='Absinthe' thn__Hotel__c='" + propertyID +
                "' thn__MYCE_Product_Type__c='Beverage' thn__Price_Gross_Value__c='100' thn__Price_Net_Value__c='110'" +
                " thn__VAT_Category__c=0", ORG_USERNAME);
        String comboProductId = product.createProductSFDX(SFDX, "Name='ComboAutoTest2' thn__Hotel__c='"
                + propertyID + "' thn__MYCE_Product_Type__c='Beverage'", ORG_USERNAME);
        String productComponentId = product.createProductComboComponentSFDX(SFDX, "thn__Combo__c='"
                + comboProductId + "' thn__Component__c='" + whiskeyProductID + "' thn__Quantity__c=1", ORG_USERNAME);
        product.updateProductSFDX(SFDX, "Id='" + comboProductId + "'",
                "thn__MYCE_Product_Type__c='Beverage'", ORG_USERNAME);
        String packageLineId = packageLine.createPackageLineSFDX(SFDX, "Name='Combo' thn__Package__c='"
                + packageId + "' thn__Type__c='Beverage' thn__Product__c='" + comboProductId + "'" +
                " thn__Start_Time__c=15:00 thn__End_Time__c=16:00 thn__Unit_Price__c=150 thn__Unit__c=5" +
                " thn__VAT_Category__c=0 thn__Apply_Discount__c=true", ORG_USERNAME);
        StringBuilder packageLineRecord = packageLine.
                getPackageLineSFDX(SFDX, "Id='" + packageLineId + "'", ORG_USERNAME);
        String packageLineUnit = JsonParser2.getFieldValue(packageLineRecord.toString(), "thn__Unit__c");
        StringBuilder updatedPackageRecord = packages.
                getPackageSFDX(SFDX, "Id='" + packageId + "'", ORG_USERNAME );
        String packagePrice = JsonParser2.getFieldValue(updatedPackageRecord.toString(), "thn__List_Price_new__c");
        Assert.assertEquals(packagePrice, "1650");
        Assert.assertEquals(packageLineUnit, "1");
    }

    @Test(priority = 7, description = "Create Package line, type == Hotel room, Unit == 5,Unit Price == 100. Result:" +
            " Unit is set to “1”. Total price is 100 (Unit price (100) * Unit (1)). List Price on Package is changed" +
            " to 1750.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case7() throws InterruptedException, IOException {
        StringBuilder packageRecord = packages.getPackageSFDX(SFDX, "Name='QuoteProductsUnitAutoTest",
                ORG_USERNAME);
        String packageId = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        String packageLineId = packageLine.createPackageLineSFDX(SFDX, "Name='Room' thn__Package__c='"
                + packageId + "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "'" +
                " thn__Start_Time__c=15:00 thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__Unit__c=5" +
                " thn__VAT_Category__c=0 thn__Apply_Discount__c=true", ORG_USERNAME);
        StringBuilder packageLineRecord = packageLine.
                getPackageLineSFDX(SFDX, "Id='" + packageLineId + "'", ORG_USERNAME);
        String packageLineUnit = JsonParser2.getFieldValue(packageLineRecord.toString(), "thn__Unit__c");
        StringBuilder updatedPackageRecord = packages.
                getPackageSFDX(SFDX, "Id='" + packageId + "'", ORG_USERNAME );
        String packagePrice = JsonParser2.getFieldValue(updatedPackageRecord.toString(), "thn__List_Price_new__c");
        Assert.assertEquals(packagePrice, "1750");
        Assert.assertEquals(packageLineUnit, "1");
    }

    @Test(priority = 8, description = "Create Package line, type == Meeting room, Unit == 5, Unit Price == 100." +
            " Result: Unit is set to “1”. Total price is 100 (Unit price (100) * Unit (1)). List" +
            " Price on Package is changed to 1750.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case8() throws InterruptedException, IOException {
        StringBuilder packageRecord = packages.getPackageSFDX(SFDX, "Name='QuoteProductsUnitAutoTest",
                ORG_USERNAME);
        String packageId = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String packageLineId = packageLine.createPackageLineSFDX(SFDX, "Name='Meeting' thn__Package__c='"
                + packageId + "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "'" +
                " thn__Start_Time__c=15:00 thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__Unit__c=5" +
                " thn__VAT_Category__c=0 thn__Apply_Discount__c=true", ORG_USERNAME);
        StringBuilder packageLineRecord = packageLine.
                getPackageLineSFDX(SFDX, "Id='" + packageLineId + "'", ORG_USERNAME);
        String packageLineUnit = JsonParser2.getFieldValue(packageLineRecord.toString(), "thn__Unit__c");
        StringBuilder updatedPackageRecord = packages.
                getPackageSFDX(SFDX, "Id='" + packageId + "'", ORG_USERNAME );
        String packagePrice = JsonParser2.getFieldValue(updatedPackageRecord.toString(), "thn__List_Price_new__c");
        Assert.assertEquals(packagePrice, "1850");
        Assert.assertEquals(packageLineUnit, "1");
    }

    @Test(priority = 9, description = "Create MYCE Quote, specify “1” for Pax, Create Quote package, leave Pax," +
            " Discounts empty (package created above). Result: Quote package, Quote package lines are copied to the" +
            " Quote with price values.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case9() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        StringBuilder packageRecord = packages.getPackageSFDX(SFDX, "Name='QuoteProductsUnitAutoTest",
                ORG_USERNAME);
        String packageId = JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Package__c='" + packageId + "'", ORG_USERNAME);
        StringBuilder myceQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteTotalAmountInclTax = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        Assert.assertEquals(quoteTotalAmountInclTax, "1850");
    }

    @Test(priority = 10, description = "Update Pax on Quote package. Result: Hotel Rooms Amount, Meeting Rooms" +
            " Amount, Products Amount, Total Amount excl. Tax, Total Amount excl. Tax fields on the Quote are" +
            " recalculated according to the new Pax value.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case10() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        Integer hotelRoomsAmmount = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Hotel_Rooms_Amount__c");
        Integer meetingRoomsAmmount = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Meeting_Rooms_Amount__c");
        Integer productsAmmount = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Products_Amount__c");
        Integer totalAmmountExclTax = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Total_Amount_excl_Tax__c");
        Integer totalAmmountInclclTax = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Total_Amount_incl_Tax__c");
        Integer expectedhotelRoomsAmmount = hotelRoomsAmmount*3;
        Integer expectedmeetingRoomsAmmount = meetingRoomsAmmount*3;
        Integer expectedproductsAmmount = productsAmmount*3;
        Integer expectedtotalAmmountExclTax = totalAmmountExclTax*3;
        Integer expectedtotalAmmountInclclTax = totalAmmountInclclTax*3;
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Pax__c=5", ORG_USERNAME);
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "'", "thn__Pax__c=3", ORG_USERNAME);
        StringBuilder updatedQuote = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        Integer updatedHotelRoomsAmmount = JsonParser2.
                getFieldValueLikeInteger(updatedQuote, "result", "thn__Hotel_Rooms_Amount__c");
        Integer updatedMeetingRoomsAmmount = JsonParser2.
                getFieldValueLikeInteger(updatedQuote, "result", "thn__Meeting_Rooms_Amount__c");
        Integer updatedProductsAmmount = JsonParser2.
                getFieldValueLikeInteger(updatedQuote, "result", "thn__Products_Amount__c");
        Integer updatedTotalAmmountExclTax = JsonParser2.
                getFieldValueLikeInteger(updatedQuote, "result", "thn__Total_Amount_excl_Tax__c");
        Integer updatedTotalAmmountInclclTax = JsonParser2.
                getFieldValueLikeInteger(updatedQuote, "result", "thn__Total_Amount_incl_Tax__c");
        Assert.assertEquals(updatedHotelRoomsAmmount, expectedhotelRoomsAmmount);
        Assert.assertEquals(updatedMeetingRoomsAmmount, expectedmeetingRoomsAmmount);
        Assert.assertEquals(updatedProductsAmmount, expectedproductsAmmount);
        Assert.assertEquals(updatedTotalAmmountExclTax, expectedtotalAmmountExclTax);
        Assert.assertEquals(updatedTotalAmmountInclclTax, expectedtotalAmmountInclclTax);
    }

    @Test(priority = 11, description = "Add Quote product to the Quote, Product == Equipment, Pax == 1, Unit == 5." +
            " Result: Quote product record is created: List price is 300 (Unit Price incl. Tax (60) * Unit (5))." +
            " Products Amount, Total Amount excl. Tax, Total Tax, Total Amount incl. Tax fields on the Quote are" +
            " recalculated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case11() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        Integer totalAmmountInclclTax = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Total_Amount_incl_Tax__c");
        StringBuilder equipmentRecord = product.getProductSFDX(SFDX, "Name='EQUIPMENT'", ORG_USERNAME);
        String equipmentID = JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        String quoteProductId = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + equipmentID + "' thn__Pax__c=1 thn__Unit__c=5", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "Id='" + quoteProductId +
                "'", ORG_USERNAME);
        String quoteProductListPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__List_price__c");
        Integer expectedtotalAmmountInclclTax = totalAmmountInclclTax + 300;
        StringBuilder updatedQuote = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        Integer updatedTotalAmmountInclclTax = JsonParser2.
                getFieldValueLikeInteger(updatedQuote, "result", "thn__Total_Amount_incl_Tax__c");
        Assert.assertEquals(quoteProductListPrice, "300");
        Assert.assertEquals(updatedTotalAmmountInclclTax, expectedtotalAmmountInclclTax);
    }

    @Test(priority = 12, description = "Update Unit on added Quote product to 10. Result: Quote product record is" +
            " updated: List price is 600 (Unit Price incl. Tax (60) * Unit (10)). Products Amount, Total Amount" +
            " excl. Tax, Total Tax, Total Amount incl. Tax fields on the Quote are recalculated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case12() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        Integer totalAmmountInclclTax = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Total_Amount_incl_Tax__c");
        StringBuilder equipmentRecord = product.getProductSFDX(SFDX, "Name='EQUIPMENT'", ORG_USERNAME);
        String equipmentID = JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='"
                + equipmentID + "'", "thn__Unit__c=10", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + equipmentID + "'", ORG_USERNAME);
        String quoteProductListPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__List_price__c");
        Integer expectedtotalAmmountInclclTax = totalAmmountInclclTax + 300;
        StringBuilder updatedQuote = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        Integer updatedTotalAmmountInclclTax = JsonParser2.
                getFieldValueLikeInteger(updatedQuote, "result", "thn__Total_Amount_incl_Tax__c");
        Assert.assertEquals(quoteProductListPrice, "600");
        Assert.assertEquals(updatedTotalAmmountInclclTax, expectedtotalAmmountInclclTax);
    }

    @Test(priority = 13, description = "Update Pax on added Quote product to 5. Result: Quote product record is" +
            " updated: Sales Price excl. Tax, Sales Price incl. Tax. fields. Products Amount, Total Amount excl." +
            " Tax, Total Tax, Total Amount incl. Tax fields on the Quote are recalculated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case13() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        Integer totalAmmountInclclTax = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Total_Amount_incl_Tax__c");
        StringBuilder equipmentRecord = product.getProductSFDX(SFDX, "Name='EQUIPMENT'", ORG_USERNAME);
        String equipmentID = JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='"
                + equipmentID + "'", "thn__Pax__c=5", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + equipmentID + "'", ORG_USERNAME);
        String quoteProductListPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__List_price__c");
        Integer expectedtotalAmmountInclclTax = totalAmmountInclclTax + 2400;
        StringBuilder updatedQuote = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        Integer updatedTotalAmmountInclclTax = JsonParser2.
                getFieldValueLikeInteger(updatedQuote, "result", "thn__Total_Amount_incl_Tax__c");
        Assert.assertEquals(quoteProductListPrice, "600");
        Assert.assertEquals(updatedTotalAmmountInclclTax, expectedtotalAmmountInclclTax);
    }

    @Test(priority = 14, description = "Change Discount on created Equipment record. Result: Quote product record" +
            " is updated: Unit Price incl. Tax, Unit Price excl. Tax, Sales Price excl. Tax, Sales Price incl. Tax." +
            " Discount Amount fields. Products Amount, Total Amount excl. Tax, Total Tax, Total Amount incl. Tax" +
            " fields on the Quote are recalculated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case14() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        Integer totalAmmountInclclTax = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Total_Amount_incl_Tax__c");
        StringBuilder equipmentRecord = product.getProductSFDX(SFDX, "Name='EQUIPMENT'", ORG_USERNAME);
        String equipmentID = JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='"
                + equipmentID + "'", "thn__Discount_Percent__c=10", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + equipmentID + "'", ORG_USERNAME);
        String quoteProductSalesPriceInclTax= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteProductDiscountAmmount= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Discount__c");
        Integer expectedtotalAmmountInclclTax = totalAmmountInclclTax - 300;
        StringBuilder updatedQuote = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        Integer updatedTotalAmmountInclclTax = JsonParser2.
                getFieldValueLikeInteger(updatedQuote, "result", "thn__Total_Amount_incl_Tax__c");
        Assert.assertEquals(quoteProductSalesPriceInclTax, "2700");
        Assert.assertEquals(quoteProductDiscountAmmount, "6");
        Assert.assertEquals(updatedTotalAmmountInclclTax, expectedtotalAmmountInclclTax);
    }

    @Test(priority = 15, description = "Change Discount on Quote package. Result: Discount Amount," +
            " Discount Max fields on Quote package are recalculated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case15() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        double totalAmmountInclclTax = JsonParser2.
                getFieldValueLikeDouble(quoteRecord, "result", "thn__Total_Amount_incl_Tax__c");
        StringBuilder quotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        double quotePackageSalesPriceInclTax = JsonParser2.
                getFieldValueLikeDouble(quotePackageRecord, "result", "thn__Sales_Price_incl_Tax__c");
        System.out.println(quotePackageSalesPriceInclTax);
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Discount__c=10", ORG_USERNAME);
        StringBuilder updatedQuotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quotePackageDiscountAmmount= JsonParser2.
                getFieldValue(updatedQuotePackageRecord.toString(), "thn__Discount_Amount__c");
        String quotePackageDiscountMax= JsonParser2.
                getFieldValue(updatedQuotePackageRecord.toString(), "thn__Discount_Max__c");
        StringBuilder updatedQuote = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        double expectedTotalAmountInclTax = totalAmmountInclclTax - (quotePackageSalesPriceInclTax/100*10);
        double updatedTotalAmmountInclclTax = JsonParser2.
                getFieldValueLikeDouble(updatedQuote, "result", "thn__Total_Amount_incl_Tax__c");
        Assert.assertEquals(quotePackageDiscountAmmount, "185");
        Assert.assertEquals(quotePackageDiscountMax, "815");
        Assert.assertEquals(updatedTotalAmmountInclclTax, expectedTotalAmountInclTax);
    }

    @Test(priority = 16, description = "Quote budget record is created once Quote Product / Quote meeting room /" +
            " Quote hotel room is added to the Quote for each Product type with Total Amount incl. Tax," +
            " Total Amount excl. Tax fields.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-543: Quote products - unit")
    public void case16() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductsUnitAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteTotalAmountExclTax= JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Total_Amount_excl_Tax__c");
        String quoteTotalAmountInclTax= JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        StringBuilder quoreBudgetRecord = quoteBudget.getQuoteBudgetSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteBudgetTotalAmountExclTax= JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Total_Amount_excl_Tax__c");
        String quoteBudgetTotalAmountInclTax= JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        Assert.assertEquals(quoteBudgetTotalAmountExclTax, quoteTotalAmountExclTax);
        Assert.assertEquals(quoteBudgetTotalAmountInclTax, quoteTotalAmountInclTax);
    }




}
