package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteAnalyticsProcess extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote. Instantiate a Quote Hotel Room. Expected Result: A" +
            " Quote Hotel Room Price record is created in the related list of the Quote Hotel Room. At the same a" +
            " Quote Analytics record is created and filled according to: QAnalytics.Total_Excl__c = " +
            "qprice.thn__Sales_Price_excl_Tax__c; QAnalytics.Total_Incl__c = qprice.thn__Sales_Price_incl_Tax__c;" +
            " QAnalytics.Package_Number__c = qprice.thn__Quote_Hotel_Room__r.thn__Package_Number__c;QAnalytics.Pax__c" +
            " = qprice.thn__Quantity__c; QAnalytics.VAT__c = qprice.thn__VAT_Rate__c.format();" +
            " QAnalytics.Unit_Price_excl__c = qprice.thn__Unit_Price_excl_Tax__c; QAnalytics.Unit_Price_incl__c" +
            " = qprice.thn__Unit_Price_incl_Tax__c; QAnalytics.MYCE_Quote__c =" +
            " qprice.thn__Quote_Hotel_Room__r.thn__MYCE_Quote__c; QAnalytics.Property__c =" +
            " qprice.thn__Quote_Hotel_Room__r.thn__Property__r.name.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String propertyName = JsonParser2.getFieldValue(hotelRecord.toString(), "Name");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest'" +
                " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 3) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 6) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                        quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                        "' thn__Arrival_Date_Time__c=" + date.generateTodayDate2_plus(0, 3) +
                        "T12:00:00.000+0000 thn__Departure_Date_Time__c=" +
                        date.generateTodayDate2_plus(0, 4) + "T12:00:00.000+0000 thn__Unit_Price__c=400",
                ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID + "'", ORG_USERNAME);
        String qhrPackageNumber = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Package_Number__c");
        StringBuilder quoteHotelRoomPriceRecord = quoteHotelRoomPrice.
                getQuoteHotelRoomPriceSFDX(SFDX, "thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                        ORG_USERNAME);
        String quoteHotelRoomPriceID = JsonParser2.getFieldValue(quoteHotelRoomPriceRecord.toString(), "Id");
        String qhrpSalesPriceExclTax = JsonParser2.
                getFieldValue(quoteHotelRoomPriceRecord.toString(), "thn__Sales_Price_excl_Tax__c");
        String qhrpQuantity = JsonParser2.
                getFieldValue(quoteHotelRoomPriceRecord.toString(), "thn__Quantity__c");
        String qhrpSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteHotelRoomPriceRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String qhrpSalesPriceVatRate = JsonParser2.
                getFieldValue(quoteHotelRoomPriceRecord.toString(), "thn__VAT_Rate__c");
        String qhrpSalesPriceUnitePriceExclTax = JsonParser2.
                getFieldValue(quoteHotelRoomPriceRecord.toString(), "thn__Unit_Price_excl_Tax__c");
        String qhrpSalesPriceUnitePriceInclTax = JsonParser2.
                getFieldValue(quoteHotelRoomPriceRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        StringBuilder quoteAnalyticsRecord = quoteAnalytics.
                getQuoteAnalyticsSFDX(SFDX, "thn__Quote_Hotel_Room_Price__c='" + quoteHotelRoomPriceID + "'",
                        ORG_USERNAME);
        String quoteAnalyticsID = JsonParser2.getFieldValue(quoteAnalyticsRecord.toString(), "Id");
        String quoteAnalyticsTotalExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Excl__c");
        String quoteAnalyticsPackageNumber = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Package_Number__c");
        String quoteAnalyticsPax = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Pax__c");
        String quoteAnalyticsTotalIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Incl__c");
        String quoteAnalyticsVat = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__VAT__c");
        String quoteAnalyticsUnitPriceExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_excl__c");
        String quoteAnalyticsUnitPriceIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_incl__c");
        String quoteAnalyticsMyceQuote = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__MYCE_Quote__c");
        String quoteAnalyticsProperty = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Property__c");
        Assert.assertNotNull(quoteAnalyticsID);
        Assert.assertEquals(quoteAnalyticsTotalExcl, qhrpSalesPriceExclTax);
        Assert.assertEquals(quoteAnalyticsPackageNumber, qhrPackageNumber);
        Assert.assertEquals(quoteAnalyticsPax, qhrpQuantity);
        Assert.assertEquals(quoteAnalyticsTotalIncl, qhrpSalesPriceInclTax);
        Assert.assertEquals(quoteAnalyticsVat, qhrpSalesPriceVatRate);
        Assert.assertEquals(quoteAnalyticsUnitPriceExcl, qhrpSalesPriceUnitePriceExclTax);
        Assert.assertEquals(quoteAnalyticsUnitPriceIncl, qhrpSalesPriceUnitePriceInclTax);
        Assert.assertEquals(quoteAnalyticsMyceQuote, quoteID);
        Assert.assertEquals(quoteAnalyticsProperty, propertyName);
    }

    @Test(priority = 2, description = "Update the Unit Price on the Quote Hotel Room. Expected Result: Quote Hotel" +
            " Room Price is update on the related list of the Quote Hotel Room. At the same time Quote Analytics" +
            " record for the QHRP is updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Unit_Price__c=99", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder quoteHotelRoomPriceRecord = quoteHotelRoomPrice.
                getQuoteHotelRoomPriceSFDX(SFDX, "thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                        ORG_USERNAME);
        String quoteHotelRoomPriceID = JsonParser2.getFieldValue(quoteHotelRoomPriceRecord.toString(), "Id");
        String qhrpSalesPriceUnitePriceExclTax = JsonParser2.
                getFieldValue(quoteHotelRoomPriceRecord.toString(), "thn__Unit_Price_excl_Tax__c");
        String qhrpSalesPriceUnitePriceInclTax = JsonParser2.
                getFieldValue(quoteHotelRoomPriceRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        String qhrpSalesPriceExclTax = JsonParser2.
                getFieldValue(quoteHotelRoomPriceRecord.toString(), "thn__Sales_Price_excl_Tax__c");
        String qhrpSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteHotelRoomPriceRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        StringBuilder quoteAnalyticsRecord = quoteAnalytics.
                getQuoteAnalyticsSFDX(SFDX, "thn__Quote_Hotel_Room_Price__c='" + quoteHotelRoomPriceID + "'",
                        ORG_USERNAME);
        String quoteAnalyticsUnitPriceExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_excl__c");
        String quoteAnalyticsUnitPriceIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_incl__c");
        String quoteAnalyticsTotalExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Excl__c");
        String quoteAnalyticsTotalIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Incl__c");
        Assert.assertEquals(qhrpSalesPriceUnitePriceExclTax, "90");
        Assert.assertEquals(qhrpSalesPriceUnitePriceInclTax, "99");
        Assert.assertEquals(qhrpSalesPriceExclTax, "90");
        Assert.assertEquals(qhrpSalesPriceInclTax, "99");
        Assert.assertEquals(qhrpSalesPriceUnitePriceExclTax, quoteAnalyticsUnitPriceExcl);
        Assert.assertEquals(qhrpSalesPriceUnitePriceInclTax, quoteAnalyticsUnitPriceIncl);
        Assert.assertEquals(qhrpSalesPriceExclTax, quoteAnalyticsTotalExcl);
        Assert.assertEquals(qhrpSalesPriceInclTax, quoteAnalyticsTotalIncl);
    }

    @Test(priority = 3, description = "Delete the Created Quote Hotel Room. Expected Result: Quote Analytics record" +
            " for the related QHRP is deleted.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder quoteHotelRoomPriceRecord = quoteHotelRoomPrice.
                getQuoteHotelRoomPriceSFDX(SFDX, "thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                        ORG_USERNAME);
        String quoteHotelRoomPriceID = JsonParser2.getFieldValue(quoteHotelRoomPriceRecord.toString(), "Id");
        quoteHotelRoom.deleteQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder quoteAnalyseRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Analytics__c WHERE" +
                " thn__Quote_Hotel_Room_Price__c='" + quoteHotelRoomPriceID + "'", ORG_USERNAME);
        System.out.println(quoteAnalyseRecords);
        List<String> quoteAnalyseID = JsonParser2.getFieldValueSoql(quoteAnalyseRecords.toString(), "Id");
        Assert.assertEquals(quoteAnalyseID.size(), 0);
    }

    @Test(priority = 4, description = "Instantiate a Quote Product. Expected Result: Quote Analytics record is" +
            " created for the instantiated Quote Product with following mapping: QAnalytics.[ParentObject]  =" +
            " [currentRecord].id; QAnalytics.Hide_on_Offer__c = [currentRecord].thn__Hide_on_Offer__c;" +
            " QAnalytics.Name = [currentRecord].Name; QAnalytics.Package_Number__c = [currentRecord]." +
            "thn__Package_Number__c; QAnalytics.Pax__c = [currentRecord].thn__Pax__c; QAnalytics.Total_Excl__c" +
            " = [currentRecord].thn__Sales_Price_excl_Tax__c; QAnalytics.Total_Incl__c = [currentRecord]." +
            "thn__Sales_Price_incl_Tax__c; QAnalytics.VAT__c = [currentRecord].thn__VAT__c.format();" +
            " QAnalytics.Unit_Price_excl__c = [currentRecord].thn__Unit_Price_excl_Tax__c; QAnalytics." +
            "Unit_Price_incl__c = [currentRecord].thn__Unit_Price_incl_Tax__c; QAnalytics.MYCE_Quote__c =" +
            " [currentRecord].thn__MYCE_Quote__c; QAnalytics.Property__c = [currentRecord].thn__Property__c.Name.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest2'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String propertyName = JsonParser2.getFieldValue(hotelRecord.toString(), "Name");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String productWinesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest2'" +
                        " thn__Pax__c=100 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 6) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productWinesID + "' thn__Hide_on_Offer__c=false thn__Pax__c=3" +
                " thn__Unit_Price__c=200 thn__VAT__c=20", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'",
                ORG_USERNAME);
        String quoteProductHideOnOffer = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Hide_on_Offer__c");
        String quoteProductName = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "Name");
        String quoteProductPackageNumber = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Package_Number__c");
        String quoteProductPax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Pax__c");
        String quoteProductSalesPriceExclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Sales_Price_excl_Tax__c");
        String quoteProductSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteProductVat = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__VAT__c");
        String quoteProductUnitPriceExclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price_excl_Tax__c");
        String quoteProductUnitPriceInclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        StringBuilder quoteAnalyticsRecord = quoteAnalytics.
                getQuoteAnalyticsSFDX(SFDX, "thn__Quote_Product__c='" + quoteProductID + "'", ORG_USERNAME);
        String quoteAnalyticsHideOnOffer = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Hide_on_Offer__c");
        String quoteAnalyticsName = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "Name");
        String quoteAnalyticsPackageNumber = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Package_Number__c");
        String quoteAnalyticsPax = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Pax__c");
        String quoteAnalyticsTotalExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Excl__c");
        String quoteAnalyticsTotalIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Incl__c");
        String quoteAnalyticsVat = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__VAT__c");
        String quoteAnalyticsUnitPriceExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_excl__c");
        String quoteAnalyticsUnitPriceIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_incl__c");
        String quoteAnalyticsMyceQuote = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__MYCE_Quote__c");
        String quoteAnalyticsProperty = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Property__c");
        Assert.assertEquals(quoteAnalyticsHideOnOffer, quoteProductHideOnOffer);
        Assert.assertEquals(quoteAnalyticsName, quoteProductName);
        Assert.assertEquals(quoteAnalyticsPackageNumber, quoteProductPackageNumber);
        Assert.assertEquals(quoteAnalyticsPackageNumber, quoteProductPackageNumber);
        Assert.assertEquals(quoteAnalyticsPax, quoteProductPax);
        Assert.assertEquals(quoteAnalyticsTotalExcl, quoteProductSalesPriceExclTax);
        Assert.assertEquals(quoteAnalyticsTotalIncl, quoteProductSalesPriceInclTax);
        Assert.assertEquals(quoteAnalyticsVat, quoteProductVat);
        Assert.assertEquals(quoteAnalyticsUnitPriceExcl, quoteProductUnitPriceExclTax);
        Assert.assertEquals(quoteAnalyticsUnitPriceIncl, quoteProductUnitPriceInclTax);
        Assert.assertEquals(quoteAnalyticsMyceQuote, quoteID);
        Assert.assertEquals(quoteAnalyticsProperty, propertyName);
    }

    @Test(priority = 5, description = "Update thn__Hide_on_Offer__c, Name, thn__Pax__c, thn__VAT__c," +
            " thn__Unit_Price_excl_Tax__c. Expected Result: Quote Analytics record as been updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest2'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__VAT__c=10", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Hide_on_Offer__c=true Name='Red whine' thn__Pax__c=5 thn__Unit_Price__c=99", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        System.out.println(quoteProductRecord);
        String quoteProductID= JsonParser2.getFieldValue(quoteProductRecord.toString(), "Id");
        String quoteProductHideOnOffer= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Hide_on_Offer__c");
        String quoteProductName= JsonParser2.getFieldValue(quoteProductRecord.toString(), "Name");
        String quoteProductPax= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Pax__c");
        String quoteProductVat= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__VAT__c");
        String quoteProductUnitPrice= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String quoteProductSalesPriceExclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Sales_Price_excl_Tax__c");
        String quoteProductSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteProductUnitPriceExclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price_excl_Tax__c");
        String quoteProductUnitPriceInclTax = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        StringBuilder quoteAnalyticsRecord = quoteAnalytics.
                getQuoteAnalyticsSFDX(SFDX, "thn__Quote_Product__c='" + quoteProductID + "'", ORG_USERNAME);
        String quoteAnalyticsHideOnOffer = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Hide_on_Offer__c");
        String quoteAnalyticsName = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "Name");
        String quoteAnalyticsPax = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Pax__c");
        String quoteAnalyticsTotalExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Excl__c");
        String quoteAnalyticsTotalIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Incl__c");
        String quoteAnalyticsVat = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__VAT__c");
        String quoteAnalyticsUnitPriceExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_excl__c");
        String quoteAnalyticsUnitPriceIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_incl__c");
        Assert.assertEquals(quoteProductHideOnOffer, "true");
        Assert.assertEquals(quoteProductName, "Red whine");
        Assert.assertEquals(quoteProductPax, "5");
        Assert.assertEquals(quoteProductVat, "10");
        Assert.assertEquals(quoteProductUnitPrice, "99");
        Assert.assertEquals(quoteProductSalesPriceExclTax, "412.5");
        Assert.assertEquals(quoteProductSalesPriceInclTax, "495");
        Assert.assertEquals(quoteProductUnitPriceExclTax, "82.5");
        Assert.assertEquals(quoteProductUnitPriceInclTax, "99");
        Assert.assertEquals(quoteAnalyticsHideOnOffer, quoteProductHideOnOffer);
        Assert.assertEquals(quoteAnalyticsName, quoteProductName);
        Assert.assertEquals(quoteAnalyticsPax, quoteProductPax);
        Assert.assertEquals(quoteAnalyticsTotalExcl, quoteProductSalesPriceExclTax);
        Assert.assertEquals(quoteAnalyticsTotalIncl, quoteProductSalesPriceInclTax);
        Assert.assertEquals(quoteAnalyticsVat, quoteProductVat);
        Assert.assertEquals(quoteAnalyticsUnitPriceExcl, quoteProductUnitPriceExclTax);
        Assert.assertEquals(quoteAnalyticsUnitPriceIncl, quoteProductUnitPriceInclTax);
    }

    @Test(priority = 6, description = "Delete the created Quote Product. Expected Result: The related Quote Analytics" +
            " record is deleted.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case6() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest2'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteProductID= JsonParser2.getFieldValue(quoteProductRecord.toString(), "Id");
        quoteProducts.deleteQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder quoteAnalyseRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Analytics__c WHERE" +
                " thn__Quote_Product__c='" + quoteProductID + "'", ORG_USERNAME);
        System.out.println(quoteAnalyseRecords);
        List<String> quoteAnalyseID = JsonParser2.getFieldValueSoql(quoteAnalyseRecords.toString(), "Id");
        Assert.assertEquals(quoteAnalyseID.size(), 0);
    }

    @Test(priority = 7, description = "Instantiate a Quote Meetings Room. Expected Result: Quote Analytics record" +
            " is created for the instantiated Quote Meetings room with following mapping: QAnalytics.[ParentObject]" +
            "  = [currentRecord].id; QAnalytics.Hide_on_Offer__c = [currentRecord].thn__Hide_on_Offer__c;" +
            " QAnalytics.Name = [currentRecord].Name; QAnalytics.Package_Number__c = [currentRecord]." +
            "thn__Package_Number__c; QAnalytics.Pax__c = [currentRecord].thn__Pax__c; QAnalytics.Total_Excl__c =" +
            " [currentRecord].thn__Sales_Price_excl_Tax__c; QAnalytics.Total_Incl__c = [currentRecord]." +
            "thn__Sales_Price_incl_Tax__c; QAnalytics.VAT__c = [currentRecord].thn__VAT__c.format(); QAnalytics." +
            "Unit_Price_excl__c = [currentRecord].thn__Unit_Price_excl_Tax__c; QAnalytics.Unit_Price_incl__c =" +
            " [currentRecord].thn__Unit_Price_incl_Tax__c; QAnalytics.MYCE_Quote__c = [currentRecord]." +
            "thn__MYCE_Quote__c; QAnalytics.Property__c = [currentRecord].thn__Property__c.Name.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case7() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest3'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String propertyName = JsonParser2.getFieldValue(hotelRecord.toString(), "Name");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest3'" +
                        " thn__Pax__c=100 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 6) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quoteMeetingRoomID = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Pax__c=3 thn__Unit_Price__c=100" +
                " thn__VAT__c=20 thn__Price_per_Person__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='" +
                quoteMeetingRoomID + "'", ORG_USERNAME);
        String quoteMeetingRoomHideOnOffer = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Hide_on_Offer__c");
        String quoteMeetingRoomName = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "Name");
        String quoteMeetingRoomPackageNumber = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Package_Number__c");
        String quoteMeetingRoomPax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Pax__c");
        String quoteMeetingRoomSalesPriceExclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Sales_Price_excl_Tax__c");
        String quoteMeetingRoomSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteMeetingRoomVat = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__VAT__c");
        String quoteMeetingRoomUnitPriceExclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price_excl_Tax__c");
        String quoteMeetingRoomUnitPriceInclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        StringBuilder quoteAnalyticsRecord = quoteAnalytics.
                getQuoteAnalyticsSFDX(SFDX, "thn__Quote_Meetings_Room__c='" + quoteMeetingRoomID + "'",
                        ORG_USERNAME);
        String quoteAnalyticsHideOnOffer = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Hide_on_Offer__c");
        String quoteAnalyticsName = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "Name");
        String quoteAnalyticsPackageNumber = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Package_Number__c");
        String quoteAnalyticsPax = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Pax__c");
        String quoteAnalyticsTotalExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Excl__c");
        String quoteAnalyticsTotalIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Incl__c");
        String quoteAnalyticsVat = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__VAT__c");
        String quoteAnalyticsUnitPriceExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_excl__c");
        String quoteAnalyticsUnitPriceIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_incl__c");
        String quoteAnalyticsMyceQuote = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__MYCE_Quote__c");
        String quoteAnalyticsProperty = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Property__c");
        Assert.assertEquals(quoteAnalyticsHideOnOffer, quoteMeetingRoomHideOnOffer);
        Assert.assertEquals(quoteAnalyticsName, quoteMeetingRoomName);
        Assert.assertEquals(quoteAnalyticsPackageNumber, quoteMeetingRoomPackageNumber);
        Assert.assertEquals(quoteAnalyticsPax, quoteMeetingRoomPax);
        Assert.assertEquals(quoteAnalyticsTotalExcl, quoteMeetingRoomSalesPriceExclTax);
        Assert.assertEquals(quoteAnalyticsTotalIncl, quoteMeetingRoomSalesPriceInclTax);
        Assert.assertEquals(quoteAnalyticsVat, quoteMeetingRoomVat);
        Assert.assertEquals(quoteAnalyticsUnitPriceExcl, quoteMeetingRoomUnitPriceExclTax);
        Assert.assertEquals(quoteAnalyticsUnitPriceIncl, quoteMeetingRoomUnitPriceInclTax);
        Assert.assertEquals(quoteAnalyticsMyceQuote, quoteID);
        Assert.assertEquals(quoteAnalyticsProperty, propertyName);
    }

    @Test(priority = 8, description = "Update thn__Hide_on_Offer__c, Name, thn__Pax__c, thn__VAT__c," +
            " thn_Unit_Price_c on Quote Meetings Room. Expected Result: The related Quote Analytics record is updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case8() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest3'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__VAT__c=10", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Hide_on_Offer__c=true Name='UpdatedQMR' thn__Pax__c=5 thn__Unit_Price__c=99", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomID= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "Id");
        String quoteMeetingRoomHideOnOffer= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Hide_on_Offer__c");
        String quoteMeetingRoomName= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "Name");
        String quoteMeetingRoomPax= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Pax__c");
        String quoteMeetingRoomVat= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__VAT__c");
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomSalesPriceExclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Sales_Price_excl_Tax__c");
        String quoteMeetingRoomSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteMeetingRoomUnitPriceExclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price_excl_Tax__c");
        String quoteMeetingRoomUnitPriceInclTax = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price_incl_Tax__c");
        StringBuilder quoteAnalyticsRecord = quoteAnalytics.getQuoteAnalyticsSFDX(SFDX,
                "thn__Quote_Meetings_Room__c='" + quoteMeetingRoomID + "'", ORG_USERNAME);
        String quoteAnalyticsHideOnOffer = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Hide_on_Offer__c");
        String quoteAnalyticsName = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "Name");
        String quoteAnalyticsPax = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Pax__c");
        String quoteAnalyticsTotalExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Excl__c");
        String quoteAnalyticsTotalIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Total_Incl__c");
        String quoteAnalyticsVat = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__VAT__c");
        String quoteAnalyticsUnitPriceExcl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_excl__c");
        String quoteAnalyticsUnitPriceIncl = JsonParser2.
                getFieldValue(quoteAnalyticsRecord.toString(), "thn__Unit_Price_incl__c");
        Assert.assertEquals(quoteMeetingRoomHideOnOffer, "true");
        Assert.assertEquals(quoteMeetingRoomName, "UpdatedQMR");
        Assert.assertEquals(quoteMeetingRoomPax, "5");
        Assert.assertEquals(quoteMeetingRoomVat, "10");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "99");
        Assert.assertEquals(quoteMeetingRoomSalesPriceExclTax, "412.5");
        Assert.assertEquals(quoteMeetingRoomSalesPriceInclTax, "495");
        Assert.assertEquals(quoteMeetingRoomUnitPriceExclTax, "82.5");
        Assert.assertEquals(quoteMeetingRoomUnitPriceInclTax, "99");
        Assert.assertEquals(quoteAnalyticsHideOnOffer, quoteMeetingRoomHideOnOffer);
        Assert.assertEquals(quoteAnalyticsName, quoteMeetingRoomName);
        Assert.assertEquals(quoteAnalyticsPax, quoteMeetingRoomPax);
        Assert.assertEquals(quoteAnalyticsTotalExcl, quoteMeetingRoomSalesPriceExclTax);
        Assert.assertEquals(quoteAnalyticsTotalIncl, quoteMeetingRoomSalesPriceInclTax);
        Assert.assertEquals(quoteAnalyticsVat, quoteMeetingRoomVat);
        Assert.assertEquals(quoteAnalyticsUnitPriceExcl, quoteMeetingRoomUnitPriceExclTax);
        Assert.assertEquals(quoteAnalyticsUnitPriceIncl, quoteMeetingRoomUnitPriceInclTax);
    }

    @Test(priority = 9, description = "Delete the created Quote Meetings Room. Expected Result: The related Quote" +
            " Analytics record is deleted.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case9() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest3'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomID= JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "Id");
        quoteMeetingRoom.deleteQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder quoteAnalyseRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Analytics__c WHERE" +
                " thn__Quote_Meetings_Room__c='" + quoteMeetingRoomID + "'", ORG_USERNAME);
        System.out.println(quoteAnalyseRecords);
        List<String> quoteAnalyseID = JsonParser2.getFieldValueSoql(quoteAnalyseRecords.toString(), "Id");
        Assert.assertEquals(quoteAnalyseID.size(), 0);
    }

    @Test(priority = 10, description = "Create a Package. Add a Meeting Room, Hotel room and Quote Product of" +
            " different types as Package Lines. Instantiate this Package to the MYCE Quote. Expected Result:" +
            " Quote Analytics records are created for the related of the Quote Package.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case10() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest4'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='CloneAutoTest' thn__Hotel__c='" + propertyID +
                "'", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest4'" +
                        " thn__Pax__c=100 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 6) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Hotel Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1", ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "' thn__Pax__c=50", ORG_USERNAME);
        StringBuilder quoteAnalyticRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Pax__c FROM" +
                " thn__Quote_Analytics__c WHERE thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteAnalyticID = JsonParser2.
                getFieldValueSoql(quoteAnalyticRecords.toString(), "Id");
        List<Integer> quoteAnalyticPax = JsonParser2.
                getFieldValueSoql2(quoteAnalyticRecords.toString(), "thn__Pax__c");
        Assert.assertEquals(quoteAnalyticID.size(), 4);
        Assert.assertEquals(quoteAnalyticPax.get(0).intValue(), 50);
        Assert.assertEquals(quoteAnalyticPax.get(1).intValue(), 50);
        Assert.assertEquals(quoteAnalyticPax.get(2).intValue(), 50);
        Assert.assertEquals(quoteAnalyticPax.get(3).intValue(), 50);
    }

    @Test(priority = 11, description = "Update Pax on the Quote Package. Expected Result: The related Quote Analytics" +
            " records are updated with new values.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case11() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest4'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Pax__c=35", ORG_USERNAME);
        StringBuilder quoteAnalyticRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Pax__c FROM" +
                " thn__Quote_Analytics__c WHERE thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteAnalyticID = JsonParser2.
                getFieldValueSoql(quoteAnalyticRecords.toString(), "Id");
        List<Integer> quoteAnalyticPax = JsonParser2.
                getFieldValueSoql2(quoteAnalyticRecords.toString(), "thn__Pax__c");
        Assert.assertEquals(quoteAnalyticID.size(), 4);
        Assert.assertEquals(quoteAnalyticPax.get(0).intValue(), 35);
        Assert.assertEquals(quoteAnalyticPax.get(1).intValue(), 35);
        Assert.assertEquals(quoteAnalyticPax.get(2).intValue(), 35);
        Assert.assertEquals(quoteAnalyticPax.get(3).intValue(), 35);
    }

    @Test(priority = 12, description = "Delete the created Quote Package. Expected Result: The related Quote" +
            " Analytics records are deleted.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-706: Quote Analytics")
    public void case12() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAnalyticsAutoTest4'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingPackages.deleteQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder quoteAnalyticRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Pax__c FROM" +
                " thn__Quote_Analytics__c WHERE thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteAnalyticID = JsonParser2.
                getFieldValueSoql(quoteAnalyticRecords.toString(), "Id");
        Assert.assertEquals(quoteAnalyticID.size(), 0);
    }


}
