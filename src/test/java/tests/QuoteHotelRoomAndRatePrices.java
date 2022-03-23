package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteHotelRoomAndRatePrices extends BaseTest {

    @Test(priority = 1, description = "Default Agile Value > Assign_Rate_Price__c = True, Default Agile Value >" +
            " Use_Cat_Prices__c= True, Default Agile Value > Base_Price__c == Tax Included. Create a MYCE Quote," +
            " Instantiate a Quote hotel Room. Expected result: QHRP.Unit Price incl. Tax =" +
            " Category_Price_c.Price_incl_Tax_c")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-647: Quote hotel room & rate prices")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DAVRatePrice1.apex");
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'", ORG_USERNAME);
        rate.deleteRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'" +
                        " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String rateId = rate.createRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1'" +
                " thn__IsActive__c=true thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" + propertyID + "'",
                ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Base_Price__c=100 thn__RelativeAdjustment__c=1" +
                " thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1 thn__ExtraUnitAdjustment__c=1" +
                " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate__c='" + rateId + "' thn__Adjustment_excl_Tax__c=1 thn__Adjustment_incl_Tax__c=2" +
                " thn__RelativeValue__c=1 thn__AbsoluteValue__c=1", ORG_USERNAME);
        String categoryPriceID = categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        StringBuilder categoryPriceRecord = categoryPrice.
                getCategoryPriceSFDX(SFDX, "Id='" + categoryPriceID + "'", ORG_USERNAME);
        String categoryPricePriceInclTax = JsonParser2.
                getFieldValue(categoryPriceRecord.toString(), "thn__Price_incl_Tax__c");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                        quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                        "' thn__Rate_Plan__c='" + rateId + "'", ORG_USERNAME);
        StringBuilder qhrPriceRecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        String qhrpUnitPriceInclTax = JsonParser2.
                getFieldValue(qhrPriceRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        Assert.assertEquals(qhrpUnitPriceInclTax, categoryPricePriceInclTax);
        Assert.assertEquals(qhrpUnitPriceInclTax, "110");
    }

    @Test(priority = 2, description = "Default Agile Value > Assign_Rate_Price__c = True, Default Agile Value >" +
            " Use_Cat_Prices__c= True, Default Agile Value > Base_Price__c == Tax Excluded. Create a MYCE Quote," +
            " Instantiate a Quote hotel Room. Expected result: QHRP.Unit Price Excl. Tax =" +
            " Category_Price_c.Price_Excl_Tax_c")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-647: Quote hotel room & rate prices")
    public void case2() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DAVRatePrice2.apex");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1",
                ORG_USERNAME);
        String rateID= JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateID + "'", ORG_USERNAME);
        StringBuilder categoryPriceRecord = categoryPrice.
                getCategoryPriceSFDX(SFDX, "thn__Rate__c='" + rateID + "'", ORG_USERNAME);
        String categoryPricePriceExclTax = JsonParser2.
                getFieldValue(categoryPriceRecord.toString(), "thn__Price_excl_Tax__c");
        StringBuilder qhrPriceRecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        String qhrpUnitPriceInclTax = JsonParser2.
                getFieldValue(qhrPriceRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        String qhrpUnitPriceExclTax = JsonParser2.
                getFieldValue(qhrPriceRecord.toString(), "thn__Unit_Price_excl_Tax__c");
        Assert.assertEquals(qhrpUnitPriceExclTax, categoryPricePriceExclTax);
        Assert.assertEquals(qhrpUnitPriceInclTax, "99");
        Assert.assertEquals(qhrpUnitPriceExclTax, "90");
    }

    @Test(priority = 3, description = "Default Agile Value > Assign_Rate_Price__c = True, Default Agile Value >" +
            " Use_Cat_Prices__c= False, Default Agile Value > Base_Price__c == Tax Included. Create a MYCE Quote," +
            " Instantiate a Quote hotel Room. Expected result: QHRP.Unit Price incl. Tax =" +
            " Rate_Price_c.Price_incl_Taxc * (1 + Category_Adjustmentc.RelativeValuec) +" +
            " Category_Adjustmentc.Adjustment_incl_Tax_c")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-647: Quote hotel room & rate prices")
    public void case3() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DAVRatePrice3.apex");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1",
                ORG_USERNAME);
        String rateID= JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateID + "'", ORG_USERNAME);
        StringBuilder ratePriceRecord = ratePrice.getRatePriceSFDX(SFDX, "thn__Rate__c='" + rateID + "'",
                ORG_USERNAME);
        Integer ratePricePriceInclTax = JsonParser2.getFieldValueLikeInteger(ratePriceRecord, "result",
                "thn__Price_incl_Tax__c");
        StringBuilder categoryAdjustmentRecord = categoryAdjustment.
                getCategoryAdjustmentSFDX(SFDX, "thn__Rate__c='" + rateID + "'", ORG_USERNAME);
        Integer categoryAdjustmentRelativeValue = JsonParser2.getFieldValueLikeInteger(categoryAdjustmentRecord,
                "result", "thn__RelativeValue__c");
        Integer categoryAdjustmentInclTax = JsonParser2.getFieldValueLikeInteger(categoryAdjustmentRecord,
                "result", "thn__Adjustment_incl_Tax__c");
        Integer expectedResult =  ratePricePriceInclTax*(1 + categoryAdjustmentRelativeValue) + categoryAdjustmentInclTax;
        StringBuilder qhrPriceRecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        Integer qhrpUnitPriceInclTax = JsonParser2.getFieldValueLikeInteger(qhrPriceRecord,
                "result", "thn__Unit_Price_incl_Tax__c");
        Assert.assertEquals(qhrpUnitPriceInclTax, expectedResult);
    }

    @Test(priority = 4, description = "Default Agile Value > Assign_Rate_Price__c = True, Default Agile Value >" +
            " Use_Cat_Prices__c= False, Default Agile Value > Base_Price__c == Tax Excluded. Create a MYCE Quote," +
            " Instantiate a Quote hotel Room. Expected result: QHRP.Unit Price Excl. Tax =" +
            " Rate_Price_c.Price_Excl_Taxc * (1 + Category_Adjustmentc.RelativeValuec) +" +
            " Category_Adjustmentc.Adjustment_Excl_Tax_c")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-647: Quote hotel room & rate prices")
    public void case4() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DAVRatePrice4.apex");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1",
                ORG_USERNAME);
        String rateID= JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateID + "'", ORG_USERNAME);
        StringBuilder ratePriceRecord = ratePrice.getRatePriceSFDX(SFDX, "thn__Rate__c='" + rateID + "'",
                ORG_USERNAME);
        Integer ratePricePriceExclTax = JsonParser2.getFieldValueLikeInteger(ratePriceRecord, "result",
                "thn__Price_excl_Tax__c");
        StringBuilder categoryAdjustmentRecord = categoryAdjustment.
                getCategoryAdjustmentSFDX(SFDX, "thn__Rate__c='" + rateID + "'", ORG_USERNAME);
        Integer categoryAdjustmentRelativeValue = JsonParser2.getFieldValueLikeInteger(categoryAdjustmentRecord,
                "result", "thn__RelativeValue__c");
        Integer categoryAdjustmentExclTax = JsonParser2.getFieldValueLikeInteger(categoryAdjustmentRecord,
                "result", "thn__Adjustment_excl_Tax__c");
        Integer expectedResult =  ratePricePriceExclTax*(1 + categoryAdjustmentRelativeValue) + categoryAdjustmentExclTax;
        StringBuilder qhrPriceRecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        Integer qhrpUnitPriceExclTax = JsonParser2.getFieldValueLikeInteger(qhrPriceRecord,
                "result", "thn__Unit_Price_excl_Tax__c");
        Assert.assertEquals(qhrpUnitPriceExclTax, expectedResult);
    }

    @Test(priority = 5, description = "Default Agile Value > Assign_Rate_Price__c = False, Default Agile Value >" +
            "  Default Agile Value > Base_Price__c == Tax Included. Create a MYCE Quote," +
            " Instantiate a Quote hotel Room, Set Unit price != Null. Expected result: HRP.Unit Price Incl. Tax = " +
            "qhr.UnitPrice")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-647: Quote hotel room & rate prices")
    public void case5() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DAVRatePrice5.apex");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1",
                ORG_USERNAME);
        String rateID= JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateID + "' thn__Unit_Price__c=500", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='" +
                quoteHotelRoomId + "'", ORG_USERNAME);
        Integer qhrUnitPrice = JsonParser2.getFieldValueLikeInteger(quoteHotelRoomRecord,
                "result", "thn__Unit_Price__c");
        StringBuilder qhrPriceRecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        Integer qhrpUnitPriceInclTax = JsonParser2.getFieldValueLikeInteger(qhrPriceRecord,
                "result", "thn__Unit_Price_incl_Tax__c");
        Assert.assertEquals(qhrpUnitPriceInclTax, qhrUnitPrice);
        Assert.assertEquals(qhrpUnitPriceInclTax.intValue(), 500);
    }

    @Test(priority = 6, description = "Default Agile Value > Assign_Rate_Price__c = False, Default Agile Value >" +
            "  Default Agile Value > Base_Price__c == Tax Included. Create a MYCE Quote," +
            " Instantiate a Quote hotel Room, Set Unit price = Null. Expected result: HQHRP.Unit Price incl. Tax =" +
            " Product_c.Price_Gross_Value_c")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-647: Quote hotel room & rate prices")
    public void case6() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DAVRatePrice5.apex");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1",
                ORG_USERNAME);
        String rateID= JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        Integer productPriceNetValue = JsonParser2.getFieldValueLikeInteger(room1NightRecord,
                "result", "thn__Price_Net_Value__c");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateID + "'", ORG_USERNAME);
        StringBuilder qhrPriceRecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        Integer qhrpUnitPriceExclTax = JsonParser2.getFieldValueLikeInteger(qhrPriceRecord,
                "result", "thn__Unit_Price_excl_Tax__c");
        Assert.assertEquals(qhrpUnitPriceExclTax, productPriceNetValue);
    }

    @Test(priority = 7, description = "Default Agile Value > Assign_Rate_Price__c = True, Default Agile Value >" +
            " Use_Cat_Prices__c= True, Default Agile Value > Base_Price__c == Tax Included.  Instantiate a Quote" +
            " hotel Room. Change rate on the QHR. Expected result: QHRP.Unit Price incl. Tax =" +
            " New Category_Price_c.Price_incl_Tax_c")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-647: Quote hotel room & rate prices")
    public void case7() throws InterruptedException, IOException {
        rate.deleteRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto2", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DAVRatePrice1.apex");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'", ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1",
                ORG_USERNAME);
        String rateID = JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateID + "'", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String rateId2 = rate.createRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto2'" +
                " thn__IsActive__c=true thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" +
                propertyID + "'", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId2 + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Base_Price__c=100 thn__RelativeAdjustment__c=1" +
                " thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1 thn__ExtraUnitAdjustment__c=1" +
                " thn__Price_excl_Tax__c=200 thn__Price_incl_Tax__c=250", ORG_USERNAME);
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate__c='" + rateId2 + "' thn__Adjustment_excl_Tax__c=5 thn__Adjustment_incl_Tax__c=10" +
                " thn__RelativeValue__c=2 thn__AbsoluteValue__c=4", ORG_USERNAME);
        String categoryPriceID2 = categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Price_excl_Tax__c=200" +
                " thn__Price_incl_Tax__c=250 thn__Rate__c='" + rateId2 + "' thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId + "'",
                "thn__Rate_Plan__c='" + rateId2 + "'",  ORG_USERNAME);
        StringBuilder updatedQHRrecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId +
                "'", ORG_USERNAME);
        String updatedQHRratePlan = JsonParser2.getFieldValue(updatedQHRrecord.toString(), "thn__Rate_Plan__c");
        StringBuilder categoryPriceRecord = categoryPrice.
                getCategoryPriceSFDX(SFDX, "Id='" + categoryPriceID2 + "'", ORG_USERNAME);
        String categoryPricePriceInclTax = JsonParser2.
                getFieldValue(categoryPriceRecord.toString(), "thn__Price_incl_Tax__c");
        StringBuilder qhrPriceRecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        String qhrpUnitPriceInclTax = JsonParser2.
                getFieldValue(qhrPriceRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        Assert.assertEquals(updatedQHRratePlan, rateId2);
        Assert.assertEquals(qhrpUnitPriceInclTax, categoryPricePriceInclTax);
        Assert.assertEquals(qhrpUnitPriceInclTax, "250");
    }

    @Test(priority = 8, description = "Default Agile Value > Assign_Rate_Price__c = True, Default Agile Value >" +
            " Use_Cat_Prices__c= True, Default Agile Value > Base_Price__c == Tax Excluded.  Instantiate a Quote" +
            " hotel Room. Change rate on the QHR. Expected result: QHRP.Unit Price excl. Tax =" +
            " New Category_Price_c.Price_excl_Tax_c")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-647: Quote hotel room & rate prices")
    public void case8() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DAVRatePrice2.apex");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'", ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1",
                ORG_USERNAME);
        String rateID = JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder rateRecord2 = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto2",
                ORG_USERNAME);
        String rateID2 = JsonParser2.getFieldValue(rateRecord2.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateID + "'", ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId + "'",
                "thn__Rate_Plan__c='" + rateID2 + "'",  ORG_USERNAME);
        StringBuilder updatedQHRrecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId +
                "'", ORG_USERNAME);
        String updatedQHRratePlan = JsonParser2.getFieldValue(updatedQHRrecord.toString(), "thn__Rate_Plan__c");
        StringBuilder categoryPriceRecord = categoryPrice.
                getCategoryPriceSFDX(SFDX, "thn__Rate__c='" + rateID2 + "'", ORG_USERNAME);
        String categoryPricePriceExclTax = JsonParser2.
                getFieldValue(categoryPriceRecord.toString(), "thn__Price_excl_Tax__c");
        StringBuilder qhrPriceRecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        String qhrpUnitPriceInclTax = JsonParser2.
                getFieldValue(qhrPriceRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        String qhrpUnitPriceExclTax = JsonParser2.
                getFieldValue(qhrPriceRecord.toString(), "thn__Unit_Price_excl_Tax__c");
        Assert.assertEquals(updatedQHRratePlan, rateID2);
        Assert.assertEquals(qhrpUnitPriceExclTax, categoryPricePriceExclTax);
        Assert.assertEquals(qhrpUnitPriceInclTax, "220");
        Assert.assertEquals(qhrpUnitPriceExclTax, "200");
    }

    @Test(priority = 9, description = "Default Agile Value > Assign_Rate_Price__c = True, Default Agile Value >" +
            " Use_Cat_Prices__c= false, Default Agile Value > Base_Price__c == Tax Included.  Instantiate a Quote" +
            " hotel Room. Change rate on the QHR. Expected result: QHRP.Unit Price incl. Tax = New" +
            " Rate_Price_c.Price_incl_Taxc * (1 + New Category_Adjustmentc.RelativeValuec) + New" +
            " Category_Adjustmentc.Adjustment_incl_Tax_cc")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-647: Quote hotel room & rate prices")
    public void case9() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DAVRatePrice3.apex");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'", ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1",
                ORG_USERNAME);
        String rateID = JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder rateRecord2 = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto2",
                ORG_USERNAME);
        String rateID2 = JsonParser2.getFieldValue(rateRecord2.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateID + "'", ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId + "'",
                "thn__Rate_Plan__c='" + rateID2 + "'",  ORG_USERNAME);
        StringBuilder updatedQHRrecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId +
                "'", ORG_USERNAME);
        String updatedQHRratePlan = JsonParser2.getFieldValue(updatedQHRrecord.toString(), "thn__Rate_Plan__c");
        StringBuilder ratePriceRecord = ratePrice.getRatePriceSFDX(SFDX, "thn__Rate__c='" + rateID2 + "'",
                ORG_USERNAME);
        Integer ratePricePriceInclTax = JsonParser2.getFieldValueLikeInteger(ratePriceRecord, "result",
                "thn__Price_incl_Tax__c");
        StringBuilder categoryAdjustmentRecord = categoryAdjustment.
                getCategoryAdjustmentSFDX(SFDX, "thn__Rate__c='" + rateID2 + "'", ORG_USERNAME);
        Integer categoryAdjustmentRelativeValue = JsonParser2.getFieldValueLikeInteger(categoryAdjustmentRecord,
                "result", "thn__RelativeValue__c");
        Integer categoryAdjustmentInclTax = JsonParser2.getFieldValueLikeInteger(categoryAdjustmentRecord,
                "result", "thn__Adjustment_incl_Tax__c");
        Integer expectedResult =  ratePricePriceInclTax*(1 + categoryAdjustmentRelativeValue) + categoryAdjustmentInclTax;
        StringBuilder qhrPriceRecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        Integer qhrpUnitPriceInclTax = JsonParser2.getFieldValueLikeInteger(qhrPriceRecord,
                "result", "thn__Unit_Price_incl_Tax__c");
        Assert.assertEquals(updatedQHRratePlan, rateID2);
        Assert.assertEquals(qhrpUnitPriceInclTax, expectedResult);
    }

    @Test(priority = 10, description = "Default Agile Value > Assign_Rate_Price__c = True, Default Agile Value >" +
            " Use_Cat_Prices__c= false, Default Agile Value > Base_Price__c == Tax Excluded.  Instantiate a Quote" +
            " hotel Room. Change rate on the QHR. Expected result: QHRP.Unit Price excl. Tax = New" +
            " Rate_Price_c.Price_excl_Taxc * (1 + New Category_Adjustmentc.RelativeValuec) + New" +
            " Category_Adjustmentc.Adjustment_excl_Tax_cc")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-647: Quote hotel room & rate prices")
    public void case10() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DAVRatePrice4.apex");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomAndRatePricesAutoTest1'", ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto1",
                ORG_USERNAME);
        String rateID = JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        StringBuilder rateRecord2 = rate.getRateSFDX(SFDX, "Name='RateQuoteHotelRoomAndRatePricesAuto2",
                ORG_USERNAME);
        String rateID2 = JsonParser2.getFieldValue(rateRecord2.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateID + "'", ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId + "'",
                "thn__Rate_Plan__c='" + rateID2 + "'",  ORG_USERNAME);
        StringBuilder updatedQHRrecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId +
                "'", ORG_USERNAME);
        String updatedQHRratePlan = JsonParser2.getFieldValue(updatedQHRrecord.toString(), "thn__Rate_Plan__c");
        StringBuilder ratePriceRecord = ratePrice.getRatePriceSFDX(SFDX, "thn__Rate__c='" + rateID2 + "'",
                ORG_USERNAME);
        Integer ratePricePriceExclTax = JsonParser2.getFieldValueLikeInteger(ratePriceRecord, "result",
                "thn__Price_excl_Tax__c");
        StringBuilder categoryAdjustmentRecord = categoryAdjustment.
                getCategoryAdjustmentSFDX(SFDX, "thn__Rate__c='" + rateID2 + "'", ORG_USERNAME);
        Integer categoryAdjustmentRelativeValue = JsonParser2.getFieldValueLikeInteger(categoryAdjustmentRecord,
                "result", "thn__RelativeValue__c");
        Integer categoryAdjustmentExclTax = JsonParser2.getFieldValueLikeInteger(categoryAdjustmentRecord,
                "result", "thn__Adjustment_excl_Tax__c");
        Integer expectedResult =  ratePricePriceExclTax*(1 + categoryAdjustmentRelativeValue) + categoryAdjustmentExclTax;
        StringBuilder qhrPriceRecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        Integer qhrpUnitPriceExclTax = JsonParser2.getFieldValueLikeInteger(qhrPriceRecord,
                "result", "thn__Unit_Price_excl_Tax__c");
        Assert.assertEquals(updatedQHRratePlan, rateID2);
        Assert.assertEquals(qhrpUnitPriceExclTax, expectedResult);
    }

    @AfterClass
    public void returnSettings() throws InterruptedException, IOException{
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DefaultAgileValueForUnlockedOrg.apex");
    }



}
