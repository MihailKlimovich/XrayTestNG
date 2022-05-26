package tests.TB;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import tests.BaseTest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OptionalQPandQMRDiscount extends BaseTest {

    @BeforeClass
    public void classLevelSetup() {
        ChromeOptions options= new ChromeOptions();
        options.addArguments("--disable-cache");
        options.addArguments("--disk-cache-size=1");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-data-dir=/tmp/temp_profile");
        options.addArguments(" --whitelisted-ips=\"\"");
        options.addArguments("--headless", "window-size=1920,1024", "--no-sandbox");
        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test(priority = 1, description = "Create a MYCE Quote. Quote Pax = 5. Instantiate a Quote Product (Unit Price" +
            " = EUR 55,00). On Quote Product set Optional == True. Expected result: On qp: Sales Price incl Tax = 0," +
            " Sales Price excl Tax  = 0, Potential Max Revenue Field = 275. On MYCE Quote: Potential Max Revenue" +
            " Field = 275, Potential Max Revenue Product = 275.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-424: Optional QP/QMR Discount issue")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + beverageID + "' thn__Unit_Price__c=55", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "Id='" + quoteProductID + "'", "thn__Optional__c=true",
                ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        Integer quoteProductSalesPriceExclTax = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result", "thn__Sales_Price_excl_Tax__c");
        Integer quoteProductSalesPriceInclTax = JsonParser2
                .getFieldValueLikeInteger(quoteProductRecord, "result", "thn__Sales_Price_incl_Tax__c");
        Integer quoteProductPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quoteProductRecord, "result", "thn__Potential_max_revenue_field__c");
        Integer quotePotentialMaxRevenueField = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Potential_max_revenue_field__c");
        Integer quotePotentialMaxRevenueProduct = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Potential_max_revenue_Product__c");
        Assert.assertEquals(quoteProductSalesPriceExclTax.intValue(), 0);
        Assert.assertEquals(quoteProductSalesPriceInclTax.intValue(), 0);
        Assert.assertEquals(quoteProductPotentialMaxRevenue.intValue(), 275);
        Assert.assertEquals(quotePotentialMaxRevenueField.intValue(), 275);
        Assert.assertEquals(quotePotentialMaxRevenueProduct.intValue(), 275);
    }

    @Test(priority = 2, description = "On Quote Product set Optional == True and Discount = 10%. Expected result:" +
            " Unit Price and Potential Max Revenue field updated on the Quote Product. Discount = 10%, Unit Price" +
            " = 49.5, Potential max revenue field = 247.50. On MYCE Quote Potential Max Revenue Field and Potential" +
            " Max Revenue Product are updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-424: Optional QP/QMR Discount issue")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "thn__Optional__c=true thn__Discount_Percent__c=10", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" +
                myceQuoteID + "'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Id='" + myceQuoteID + "'", ORG_USERNAME);
        Double quoteProductUnitPrice = JsonParser2.
                getFieldValueLikeDouble(quoteProductRecord, "result", "thn__Unit_Price__c");
        Double quoteProductDiscount = JsonParser2.
                getFieldValueLikeDouble(quoteProductRecord, "result", "thn__Discount_Percent__c");
        Double quoteProductSalesPriceExclTax = JsonParser2.
                getFieldValueLikeDouble(quoteProductRecord, "result", "thn__Sales_Price_excl_Tax__c");
        Double quoteProductSalesPriceInclTax = JsonParser2
                .getFieldValueLikeDouble(quoteProductRecord, "result", "thn__Sales_Price_incl_Tax__c");
        Double quoteProductPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeDouble(quoteProductRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueField = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueProduct = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_Product__c");
        Assert.assertEquals(quoteProductUnitPrice.doubleValue(), 49.5);
        Assert.assertEquals(quoteProductDiscount.doubleValue(), 10);
        Assert.assertEquals(quoteProductSalesPriceExclTax.doubleValue(), 0);
        Assert.assertEquals(quoteProductSalesPriceInclTax.doubleValue(), 0);
        Assert.assertEquals(quoteProductPotentialMaxRevenue.doubleValue(), 247.5);
        Assert.assertEquals(quotePotentialMaxRevenueField.doubleValue(), 247.5);
        Assert.assertEquals(quotePotentialMaxRevenueProduct.doubleValue(), 247.5);
    }

    @Test(priority = 3, description = "On Quote Product set Optional == True and Complimentary == True. Expected" +
            " result: Unit Price and Potential Max Revenue field updated on the Quote Product. Unit Price = 0," +
            " Potential max revenue field = 0,  Discount = 100%. On MYCE Quote Potential Max Revenue Field and" +
            " Potential Max Revenue Product are updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-424: Optional QP/QMR Discount issue")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "thn__Optional__c=true thn__Complimentary__c=true", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" +
                myceQuoteID + "'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Id='" + myceQuoteID + "'", ORG_USERNAME);
        Double quoteProductUnitPrice = JsonParser2.
                getFieldValueLikeDouble(quoteProductRecord, "result", "thn__Unit_Price__c");
        Double quoteProductDiscount = JsonParser2.
                getFieldValueLikeDouble(quoteProductRecord, "result", "thn__Discount_Percent__c");
        Double quoteProductSalesPriceExclTax = JsonParser2.
                getFieldValueLikeDouble(quoteProductRecord, "result", "thn__Sales_Price_excl_Tax__c");
        Double quoteProductSalesPriceInclTax = JsonParser2
                .getFieldValueLikeDouble(quoteProductRecord, "result", "thn__Sales_Price_incl_Tax__c");
        Double quoteProductPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeDouble(quoteProductRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueField = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueProduct = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_Product__c");
        Assert.assertEquals(quoteProductUnitPrice.doubleValue(), 0);
        Assert.assertEquals(quoteProductDiscount.doubleValue(), 100);
        Assert.assertEquals(quoteProductSalesPriceExclTax.doubleValue(), 0);
        Assert.assertEquals(quoteProductSalesPriceInclTax.doubleValue(), 0);
        Assert.assertEquals(quoteProductPotentialMaxRevenue.doubleValue(), 0);
        Assert.assertEquals(quotePotentialMaxRevenueField.doubleValue(), 0);
        Assert.assertEquals(quotePotentialMaxRevenueProduct.doubleValue(), 0);
    }

    @Test(priority = 4, description = "Instantiate a Quote meetings Room (Unit price = 420). Set Optional == True." +
            " Expected result: On qmr: Sales Price incl Tax = 0, Sales Price excl Tax  = 0, Potential Max Revenue" +
            " Field = 420. On MYCE Quote: Potential Max Revenue Field = 420, Potential Max Revenue MR = 420.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-424: Optional QP/QMR Discount issue")
    public void case4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest2'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest2' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quoteMeetingRoomID = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'" + " thn__Product__c='" + meetingHalfDayID + "' thn__Unit_Price__c=420", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID + "'",
                "thn__Optional__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='" +
                quoteMeetingRoomID + "'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        Integer quoteMeetingRoomSalesPriceExclTax = JsonParser2.
                getFieldValueLikeInteger(quoteMeetingRoomRecord, "result", "thn__Sales_Price_excl_Tax__c");
        Integer quoteMeetingRoomSalesPriceInclTax = JsonParser2
                .getFieldValueLikeInteger(quoteMeetingRoomRecord, "result", "thn__Sales_Price_incl_Tax__c");
        Integer quoteMeetingRoomPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quoteMeetingRoomRecord, "result", "thn__Potential_max_revenue_field__c");
        Integer quotePotentialMaxRevenueField = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Potential_max_revenue_field__c");
        Integer quotePotentialMaxRevenueMR = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Potential_max_revenue_MR__c");
        Assert.assertEquals(quoteMeetingRoomSalesPriceExclTax.intValue(), 0);
        Assert.assertEquals(quoteMeetingRoomSalesPriceInclTax.intValue(), 0);
        Assert.assertEquals(quoteMeetingRoomPotentialMaxRevenue.intValue(), 420);
        Assert.assertEquals(quotePotentialMaxRevenueField.intValue(), 420);
        Assert.assertEquals(quotePotentialMaxRevenueMR.intValue(), 420);
    }

    @Test(priority = 5, description = "On Quote Meeting Room set Optional == True and Discount = 10%. Expected" +
            " result: Unit Price and Potential Max Revenue field updated on the QMR. Discount = 10%," +
            " Unit Price = 378, Potential max revenue field = 378. On MYCE Quote Potential Max Revenue Field" +
            " and Potential Max Revenue MR are updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-424: Optional QP/QMR Discount issue")
    public void case5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest2'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "thn__Optional__c=true thn__Discount_Percent__c=10", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + myceQuoteID + "'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Id='" + myceQuoteID + "'", ORG_USERNAME);
        Double quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Unit_Price__c");
        Double quoteMeetingRoomDiscount = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Discount_Percent__c");
        Double quoteMeetingRoomSalesPriceExclTax = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Sales_Price_excl_Tax__c");
        Double quoteMeetingRoomSalesPriceInclTax = JsonParser2
                .getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Sales_Price_incl_Tax__c");
        Double quoteMeetingRoomPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueField = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueMR = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_MR__c");
        Assert.assertEquals(quoteMeetingRoomUnitPrice.doubleValue(), 378);
        Assert.assertEquals(quoteMeetingRoomDiscount.doubleValue(), 10);
        Assert.assertEquals(quoteMeetingRoomSalesPriceExclTax.doubleValue(), 0);
        Assert.assertEquals(quoteMeetingRoomSalesPriceInclTax.doubleValue(), 0);
        Assert.assertEquals(quoteMeetingRoomPotentialMaxRevenue.doubleValue(), 378);
        Assert.assertEquals(quotePotentialMaxRevenueField.doubleValue(), 378);
        Assert.assertEquals(quotePotentialMaxRevenueMR.doubleValue(), 378);
    }

    @Test(priority = 6, description = "On Quote Meeting Room set Price Per Person == True. Expected" +
            " result: Unit Price and Potential Max Revenue field updated on the QMR. Discount = 10%," +
            " Unit Price = 378, Potential max revenue field = 1890. On MYCE Quote Potential Max Revenue Field" +
            " and Potential Max Revenue MR are updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-424: Optional QP/QMR Discount issue")
    public void case6() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest2'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "thn__Price_per_Person__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + myceQuoteID + "'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Id='" + myceQuoteID + "'", ORG_USERNAME);
        Double quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Unit_Price__c");
        Double quoteMeetingRoomDiscount = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Discount_Percent__c");
        Double quoteMeetingRoomSalesPriceExclTax = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Sales_Price_excl_Tax__c");
        Double quoteMeetingRoomSalesPriceInclTax = JsonParser2
                .getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Sales_Price_incl_Tax__c");
        Double quoteMeetingRoomPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueField = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueMR = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_MR__c");
        Assert.assertEquals(quoteMeetingRoomUnitPrice.doubleValue(), 378);
        Assert.assertEquals(quoteMeetingRoomDiscount.doubleValue(), 10);
        Assert.assertEquals(quoteMeetingRoomSalesPriceExclTax.doubleValue(), 0);
        Assert.assertEquals(quoteMeetingRoomSalesPriceInclTax.doubleValue(), 0);
        Assert.assertEquals(quoteMeetingRoomPotentialMaxRevenue.doubleValue(), 1890);
        Assert.assertEquals(quotePotentialMaxRevenueField.doubleValue(), 1890);
        Assert.assertEquals(quotePotentialMaxRevenueMR.doubleValue(), 1890);
    }

    @Test(priority = 7, description = "Change the Resource of the QMR. On QMR set Break Out == True, Half Day ==" +
            " True, Price_per_Person = false, thn__Discount_Percent__c = 0. Expected result: Unit Price and" +
            " Potential Max Revenue field updated on the Quote Product. Unit Price = 35, Potential max revenue" +
            " field = 35. On MYCE Quote Potential Max Revenue Field and Potential Max Revenue MR are updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-424: Optional QP/QMR Discount issue")
    public void case7() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        resource.deleteResourceSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoRes'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest2'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "thn__Price_per_Person__c=false thn__Discount_Percent__c=0", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        resource.createResourceSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoRes' thn__Hotel__c='" + propertyID +
                "' thn__Type__c='Meeting Room' thn__Bookable__c=true thn__Full_day_price__c=100" +
                " thn__Half_day_price__c=50 thn__Break_out_full_day__c=70 thn__Break_out_half_day__c=35", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("OptionalQPandQMRDiscountAutoTest2");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("1");
        quoteMeetingRoom.clickChangeResource();
        changeResource.changeResourceAndUpdatePrice("OptionalQPandQMRDiscountAutoRes");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + myceQuoteID + "'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Id='" + myceQuoteID + "'", ORG_USERNAME);
        Double quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Unit_Price__c");
        Double quoteMeetingRoomDiscount = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Discount_Percent__c");
        Double quoteMeetingRoomSalesPriceExclTax = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Sales_Price_excl_Tax__c");
        Double quoteMeetingRoomSalesPriceInclTax = JsonParser2
                .getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Sales_Price_incl_Tax__c");
        Double quoteMeetingRoomPotentialMaxRevenue = JsonParser2.
                getFieldValueLikeDouble(quoteMeetingRoomRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueField = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueMR = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_MR__c");
        Assert.assertEquals(quoteMeetingRoomUnitPrice.doubleValue(), 35);
        Assert.assertEquals(quoteMeetingRoomDiscount.doubleValue(), 0);
        Assert.assertEquals(quoteMeetingRoomSalesPriceExclTax.doubleValue(), 0);
        Assert.assertEquals(quoteMeetingRoomSalesPriceInclTax.doubleValue(), 0);
        Assert.assertEquals(quoteMeetingRoomPotentialMaxRevenue.doubleValue(), 35);
        Assert.assertEquals(quotePotentialMaxRevenueField.doubleValue(), 35);
        Assert.assertEquals(quotePotentialMaxRevenueMR.doubleValue(), 35);
    }

    @Test(priority = 8, description = "Instantiate a Quote Package: Optional == True. Expected result: On qp: Sales" +
            " Price incl Tax = 0, Sales Price excl Tax  = 0, Potential Max Revenue Field = 200. On MYCE Quote:" +
            " Potential Max Revenue Field = 200, Potential Max Revenue Package = 200.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-424: Optional QP/QMR Discount issue")
    public void case8() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest3'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoPackage", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest3' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoPackage'" +
                " thn__Hotel__c='" + propertyID + "' thn__Discount_Max__c=200 thn__Custom_Price__c=true", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=0 thn__Apply_Discount__c=true",
                ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__VAT_Category__c=0 thn__Apply_Discount__c=true",
                ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "' thn__Pax__c=1", ORG_USERNAME);
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'",
                "thn__Optional__c=true", ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        Integer quotePackageSalesPriceExclTax = JsonParser2.
                getFieldValueLikeInteger(quotePackageRecord, "result", "thn__Sales_Price_excl_Tax__c");
        Integer quotePackageSalesPriceInclTax = JsonParser2
                .getFieldValueLikeInteger(quotePackageRecord, "result", "thn__Sales_Price_incl_Tax__c");
        Integer quotePackagePotentialMaxRevenue = JsonParser2.
                getFieldValueLikeInteger(quotePackageRecord, "result", "thn__Potential_max_revenue_field__c");
        Integer quotePotentialMaxRevenueField = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Potential_max_revenue_field__c");
        Integer quotePotentialMaxRevenuePackage = JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Potential_max_revenue_Package__c");
        Assert.assertEquals(quotePackageSalesPriceExclTax.intValue(), 0);
        Assert.assertEquals(quotePackageSalesPriceInclTax.intValue(), 0);
        Assert.assertEquals(quotePackagePotentialMaxRevenue.intValue(), 200);
        Assert.assertEquals(quotePotentialMaxRevenueField.intValue(), 200);
        Assert.assertEquals(quotePotentialMaxRevenuePackage.intValue(), 200);
    }

    @Test(priority = 9, description = "Change Discount = 10% of the Quote Package. Expected result:" +
            " Unit Price and Potential Max Revenue field updated on the Quote Package. Discount = 10%, Unit Price" +
            " = 180, Potential max revenue field = 180. On MYCE Quote Potential Max Revenue Field and Potential" +
            " Max Revenue Product are updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-424: Optional QP/QMR Discount issue")
    public void case9() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OptionalQPandQMRDiscountAutoTest3'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "thn__Discount__c=10", ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Id='" + myceQuoteID + "'", ORG_USERNAME);
        Double quotePackageUnitPrice = JsonParser2.
                getFieldValueLikeDouble(quotePackageRecord, "result", "thn__Unit_Price__c");
        Double quotePackageDiscount = JsonParser2.
                getFieldValueLikeDouble(quotePackageRecord, "result", "thn__Discount__c");
        Double qquotePackagetSalesPriceExclTax = JsonParser2.
                getFieldValueLikeDouble(quotePackageRecord, "result", "thn__Sales_Price_excl_Tax__c");
        Double quotePackageSalesPriceInclTax = JsonParser2
                .getFieldValueLikeDouble(quotePackageRecord, "result", "thn__Sales_Price_incl_Tax__c");
        Double quotePackagePotentialMaxRevenue = JsonParser2.
                getFieldValueLikeDouble(quotePackageRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenueField = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_field__c");
        Double quotePotentialMaxRevenuePackage = JsonParser2.
                getFieldValueLikeDouble(updatedQuoteRecord, "result", "thn__Potential_max_revenue_Package__c");
        Assert.assertEquals(quotePackageUnitPrice.doubleValue(), 180);
        Assert.assertEquals(quotePackageDiscount.doubleValue(), 10);
        Assert.assertEquals(qquotePackagetSalesPriceExclTax.doubleValue(), 0);
        Assert.assertEquals(quotePackageSalesPriceInclTax.doubleValue(), 0);
        Assert.assertEquals(quotePackagePotentialMaxRevenue.doubleValue(), 180);
        Assert.assertEquals(quotePotentialMaxRevenueField.doubleValue(), 180);
        Assert.assertEquals(quotePotentialMaxRevenuePackage.doubleValue(), 180);
    }

}
