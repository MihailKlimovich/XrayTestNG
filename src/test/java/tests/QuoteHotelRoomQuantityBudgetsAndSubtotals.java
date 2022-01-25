package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteHotelRoomQuantityBudgetsAndSubtotals extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-677: Quote hotel room quantity - Budgets & subtotals")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Create MYCE Quote 5 days, Instantiate a Quote Hotel Room. Expected result:" +
            " A Quote Hotel Room was created. Four Quote Hotel Room Prices records were created. Four Quote Budget" +
            " records were created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-677: Quote hotel room quantity - Budgets & subtotals")
    public void case1() throws InterruptedException, IOException {
        rate.deleteRateSFDX(SFDX, "Name='RateAutoTestBudgets", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityBudgetsAndSubtotalsAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='QuoteHotelRoomQuantityBudgetsAndSubtotalsAutoTest' thn__Pax__c=1 thn__Hotel__c='"
                        + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + "" +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 4) +
                        " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String rateId = rate.createRateSFDX(SFDX, "Name='RateAutoTestBudgets' thn__IsActive__c=true" +
                " thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                        date.generateTodayDate2() + " thn__Base_Price__c=100 thn__RelativeAdjustment__c=1" +
                        " thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1 thn__ExtraUnitAdjustment__c=1" +
                        " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1 thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1 thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 3) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1 thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 4) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1 thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate__c='" + rateId + "' thn__Adjustment_excl_Tax__c=1 thn__Adjustment_incl_Tax__c=1" +
                " thn__RelativeValue__c=1 thn__AbsoluteValue__c=1", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" + date.generateTodayDate2()
                + " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "'" +
                " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 3) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 4) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID + "'",
                ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Rate_Plan__c='" + rateId + "'", ORG_USERNAME);
        StringBuilder quoteHotelRoomPrices = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room_Price__c" +
                " WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        List<String> quoteHotelRoomPricesID = JsonParser2.getFieldValueSoql(quoteHotelRoomPrices.toString(), "Id");
        StringBuilder quoteBudgetRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Total_Amount_incl_Tax__c" +
                " FROM thn__Quote_Budget__c WHERE thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteBudgetID = JsonParser2.getFieldValueSoql(quoteBudgetRecords.toString(), "Id");
        List<Integer> quoteBudgetTotalAmountInclTax = JsonParser2.getFieldValueSoql2(quoteBudgetRecords.toString(),
                "thn__Total_Amount_incl_Tax__c");
        Assert.assertEquals(quoteHotelRoomPricesID.size(), 4 );
        Assert.assertEquals(quoteBudgetID.size(), 4);
        Assert.assertEquals(quoteBudgetTotalAmountInclTax.get(0).intValue(), 221);
        Assert.assertEquals(quoteBudgetTotalAmountInclTax.get(1).intValue(), 221);
        Assert.assertEquals(quoteBudgetTotalAmountInclTax.get(2).intValue(), 221);
        Assert.assertEquals(quoteBudgetTotalAmountInclTax.get(3).intValue(), 221);
    }

    @Test(priority = 3, description = "Change the Quantity = 10 on one of Quote Hotel Room Prices record." +
            " Expected result: Sales price excl. Tax and Sales Price incl. Tax changed. uote Budget record updated" +
            " accordingly.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-677: Quote hotel room quantity - Budgets & subtotals")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityBudgetsAndSubtotalsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder quoteHotelRoomPrices = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room_Price__c" +
                " WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'", ORG_USERNAME);
        List<String> quoteHotelRoomPricesID = JsonParser2.getFieldValueSoql(quoteHotelRoomPrices.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + quoteHotelRoomPricesID.get(0) + "'",
                "thn__Quantity__c=10", ORG_USERNAME);
        StringBuilder quoteBudgetRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Total_Amount_incl_Tax__c" +
                " FROM thn__Quote_Budget__c WHERE thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteBudgetID = JsonParser2.getFieldValueSoql(quoteBudgetRecords.toString(), "Id");
        List<Integer> quoteBudgetTotalAmountInclTax = JsonParser2.getFieldValueSoql2(quoteBudgetRecords.toString(),
                "thn__Total_Amount_incl_Tax__c");
        Assert.assertEquals(quoteHotelRoomPricesID.size(), 4 );
        Assert.assertEquals(quoteBudgetID.size(), 4);
        Assert.assertEquals(quoteBudgetTotalAmountInclTax.get(0).intValue(), 2210);
        Assert.assertEquals(quoteBudgetTotalAmountInclTax.get(1).intValue(), 221);
        Assert.assertEquals(quoteBudgetTotalAmountInclTax.get(2).intValue(), 221);
        Assert.assertEquals(quoteBudgetTotalAmountInclTax.get(3).intValue(), 221);
    }

}
