package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class ComplimentaryOnQuotesRelated extends BaseTest {


    @Test(priority = 1, description = "Create MYCE Quote, Add a Quote Product to the Quote, Product = WINES," +
            " Unit price = 100, Discount = 0%. Change the discount to 50% while Complimentary=True. Expected result:" +
            " Discount was reverted to 100%, Unit Price = 0, Discount = 100%, List Price = 100. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String winesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 1) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + winesID + "' thn__Unit_Price__c=100 thn__Discount_Percent__c=0", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "Id='" + quoteProductID + "'",
                "thn__Discount_Percent__c=50 thn__Complimentary__c=true" , ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        String quoteProductDiscount= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        String quoteProductUnitPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String quoteProductListPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__List_price__c");
        Assert.assertEquals(quoteProductDiscount, "100");
        Assert.assertEquals(quoteProductUnitPrice, "0");
        Assert.assertEquals(quoteProductListPrice, "100");
    }

    @Test(priority = 2, description = "On Quote product Set checkbox Complimentary = False. Expected result:" +
            " Unit Price = 100, Discount = 0%, List Price = 100.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=false" , ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteProductDiscount= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        String quoteProductUnitPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String quoteProductListPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__List_price__c");
        Assert.assertEquals(quoteProductDiscount, "0");
        Assert.assertEquals(quoteProductUnitPrice, "100");
        Assert.assertEquals(quoteProductListPrice, "100");
    }

    @Test(priority = 3, description = "Set the Unit Price to 80. Expected result: Unit Price = 80, Discount = 20%," +
            " List Price = 100.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Unit_Price__c=80" , ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteProductDiscount= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        String quoteProductUnitPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String quoteProductListPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__List_price__c");
        Assert.assertEquals(quoteProductDiscount, "20");
        Assert.assertEquals(quoteProductUnitPrice, "80");
        Assert.assertEquals(quoteProductListPrice, "100");
    }

    @Test(priority = 4, description = "Set checkbox Complimentary = True. Expected result: Unit Price = 0," +
            " Discount = 100%, List Price = 100.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=true" , ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteProductDiscount= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        String quoteProductUnitPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String quoteProductListPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__List_price__c");
        Assert.assertEquals(quoteProductDiscount, "100");
        Assert.assertEquals(quoteProductUnitPrice, "0");
        Assert.assertEquals(quoteProductListPrice, "100");
    }

    @Test(priority = 5, description = "Change the Unit price to  50 while Complimentary=True. Expected result:" +
            " Unit Price = 0, Discount = 100%, List Price = 100.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Unit_Price__c=50" , ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteProductDiscount= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        String quoteProductUnitPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String quoteProductListPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__List_price__c");
        Assert.assertEquals(quoteProductDiscount, "100");
        Assert.assertEquals(quoteProductUnitPrice, "0");
        Assert.assertEquals(quoteProductListPrice, "100");
    }

    @Test(priority = 6, description = "On Quote product Set checkbox Complimentary = False. Expected result:" +
            " Unit Price = 100, Discount = 0%, List Price = 100.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case6() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=false" , ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteProductDiscount= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        System.out.println(quoteProductDiscount);
        String quoteProductUnitPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String quoteProductListPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__List_price__c");
        Assert.assertEquals(quoteProductDiscount, "0");
        Assert.assertEquals(quoteProductUnitPrice, "100");
        Assert.assertEquals(quoteProductListPrice, "100");
    }

    @Test(priority = 7, description = "Add a Quote Meeting Room to the Quote. Product = MEETING HALF DAY." +
            " Expected result: A Quote Meeting Room is created,  Unit Price = 240, Discount = Null, List Price = 240," +
            " Resource = DEFAULT.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case7() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomDiscount= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomListPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteMeetingRoomDiscount, null);
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "240");
        Assert.assertEquals(quoteMeetingRoomListPrice, "240");
    }

    @Test(priority = 8, description = "On Quote Meeting Room Set checkbox Complimentary = True. Expected result:" +
            " Unit Price = 0, Discount = 100, List Price = 240.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case8() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomDiscount= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomListPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteMeetingRoomDiscount, "100");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "0");
        Assert.assertEquals(quoteMeetingRoomListPrice, "240");
    }

    @Test(priority = 9, description = "On Quote Meeting Room Set checkbox Complimentary = False. Expected result:" +
            " Unit Price = 0, Discount = 100, List Price = 240.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case9() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=false", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomDiscount= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomListPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteMeetingRoomDiscount, "0");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "240");
        Assert.assertEquals(quoteMeetingRoomListPrice, "240");
    }

    @Test(priority = 10, description = "On Quote Meeting Room Set the Unit Price to 192. Expected result:" +
            " Unit Price = 192, Discount = 20, List Price = 240.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case10() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Unit_Price__c=192", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomDiscount= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomListPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteMeetingRoomDiscount, "20");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "192");
        Assert.assertEquals(quoteMeetingRoomListPrice, "240");
    }

    @Test(priority = 11, description = "On Quote Meeting Room Set checkbox Complimentary = True. Expected result:" +
            " Unit Price = 0, Discount = 100, List Price = 240.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case11() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomDiscount= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        System.out.println(quoteMeetingRoomDiscount);
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomListPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteMeetingRoomDiscount, "100");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "0");
        Assert.assertEquals(quoteMeetingRoomListPrice, "240");
    }

    @Test(priority = 12, description = "On Quote Meeting Room Change the Unit price to  50 while Complimentary=True." +
            " Expected result: Unit Price = 0, Discount = 100, List Price = 240.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case12() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Unit_Price__c=50", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomDiscount= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomListPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteMeetingRoomDiscount, "100");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "0");
        Assert.assertEquals(quoteMeetingRoomListPrice, "240");
    }

    @Test(priority = 13, description = "Change the Resource of the Meeting Room. Expected result: Unit Price and" +
            " Discount did not change. Unit Price = 0, Discount = 100, List Price = 240.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case13() throws InterruptedException, IOException {
        resource.deleteResourceSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String resourceID = resource.createResourceSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room' thn__Half_day_price__c=500" +
                " thn__Full_day_price__c=1000", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Resource__c='" + resourceID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomDiscount= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomListPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteMeetingRoomDiscount, "100");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "0");
        Assert.assertEquals(quoteMeetingRoomListPrice, "240");
    }

    @Test(priority = 14, description = "Add a Quote Hotel Room to the Quote. Product = ROOM 1 NIGHT, Room Type" +
            " = Queen. Expected result: A Quote Hotel Room is created. Unit Price = 90, Discount = Null," +
            " List Price = 90")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case14() throws InterruptedException, IOException {
        rate.deleteRateSFDX(SFDX, "Name='RateAutoTestComplimentary1'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String rateId = rate.createRateSFDX(SFDX, "Name='RateAutoTestComplimentary1' thn__IsActive__c=true" +
                " thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2() + " thn__Base_Price__c=100 thn__RelativeAdjustment__c=1" +
                " thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1 thn__ExtraUnitAdjustment__c=1",
                ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                        date.generateTodayDate2_plus(0, 1) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1", ORG_USERNAME);
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate__c='" + rateId + "' thn__Adjustment_excl_Tax__c=2 thn__Adjustment_incl_Tax__c=4" +
                " thn__RelativeValue__c=90 thn__AbsoluteValue__c=100", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" + date.generateTodayDate2()
                + " thn__Price_excl_Tax__c=110 thn__Price_incl_Tax__c=90 thn__Rate__c='" + rateId + "'" +
                " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Price_excl_Tax__c=110" +
                " thn__Price_incl_Tax__c=90 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID + "'",
                ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Rate_Plan__c='" + rateId + "'", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomDiscount= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomListPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteHotelRoomDiscount, "0");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "90");
        Assert.assertEquals(quoteHotelRoomListPrice, "90");
    }

    @Test(priority = 15, description = "On Quote Hotel Room set checkbox Complimentary = True. Expected result:" +
            " Unit Price = 0, Discount = 100, List Price = 90")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case15() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=true", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomDiscount= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomListPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteHotelRoomDiscount, "100");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "0");
        Assert.assertEquals(quoteHotelRoomListPrice, "90");
    }

    @Test(priority = 16, description = "On Quote Hotel Room set checkbox Complimentary = false. Expected result:" +
            " Unit Price = 90, Discount = 0, List Price = 90")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case16() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=false", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomDiscount= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomListPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteHotelRoomDiscount, "0");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "90");
        Assert.assertEquals(quoteHotelRoomListPrice, "90");
    }

    @Test(priority = 17, description = "Set the Unit Price to 72. Expected result: Unit Price = 72, Discount = 20," +
            " List Price = 90")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case17() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Unit_Price__c=72", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomDiscount= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomListPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteHotelRoomDiscount, "20");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "72");
        Assert.assertEquals(quoteHotelRoomListPrice, "90");
    }

    @Test(priority = 18, description = "On Quote Hotel Room set checkbox Complimentary = true. Expected result:" +
            " Unit Price = 0, Discount = 100, List Price = 90")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case18() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        System.out.println(quoteID);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Complimentary__c=true", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomDiscount= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomListPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteHotelRoomDiscount, "100");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "0");
        Assert.assertEquals(quoteHotelRoomListPrice, "90");
    }

    @Test(priority = 19, description = "Change the discount to 50% while Complimentary=True. Expected result:" +
            " Unit Price = 0, Discount = 100, List Price = 90")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case19() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        System.out.println(quoteID);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Discount_Percent__c=50", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomDiscount= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomListPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteHotelRoomDiscount, "100");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "0");
        Assert.assertEquals(quoteHotelRoomListPrice, "90");
    }

    @Test(priority = 20, description = "Change the Unit price to  50 while Complimentary=True. Expected result:" +
            " Unit Price = 0, Discount = 100, List Price = 90")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case20() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        System.out.println(quoteID);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Unit_Price__c=50", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomDiscount= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomListPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteHotelRoomDiscount, "100");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "0");
        Assert.assertEquals(quoteHotelRoomListPrice, "90");
    }

    @Test(priority = 21, description = "Change the Rate and Room type while Complimentary=True. Expected result:" +
            " Unit Price = 0, Discount = 100, List Price = 90")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-662: Complimentary on quote's related")
    public void case21() throws InterruptedException, IOException {
        rate.deleteRateSFDX(SFDX, "Name='RateAutoTestComplimentary2'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder roomTypeKingRecord = roomType.getRoomTypeSFDX(SFDX, "Name='King'", ORG_USERNAME);
        String roomTypeKingID = JsonParser2.getFieldValue(roomTypeKingRecord.toString(), "Id");
        String rateId = rate.createRateSFDX(SFDX, "Name='RateAutoTestComplimentary2' thn__IsActive__c=true" +
                " thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                        date.generateTodayDate2() + " thn__Base_Price__c=50 thn__RelativeAdjustment__c=1" +
                        " thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1 thn__ExtraUnitAdjustment__c=1",
                ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Base_Price__c=50" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1", ORG_USERNAME);
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeKingID +
                "' thn__Rate__c='" + rateId + "' thn__Adjustment_excl_Tax__c=20 thn__Adjustment_incl_Tax__c=20" +
                " thn__RelativeValue__c=20 thn__AbsoluteValue__c=20", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=20 thn__Date__c=" + date.generateTodayDate2()
                + " thn__Price_excl_Tax__c=20 thn__Price_incl_Tax__c=20 thn__Rate__c='" + rateId + "'" +
                " thn__Space_Area__c='" + roomTypeKingID + "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=20 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Price_excl_Tax__c=20" +
                " thn__Price_incl_Tax__c=20 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeKingID +
                "'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='ComplimentaryOnQuotesRelatedAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");

        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Rate_Plan__c='" + rateId + "' thn__Space_Area__c='" + roomTypeKingID + "'", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomDiscount= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomListPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__List_Price__c");
        Assert.assertEquals(quoteHotelRoomDiscount, "100");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "0");
        Assert.assertEquals(quoteHotelRoomListPrice, "20");
    }


}
