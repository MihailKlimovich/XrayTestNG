package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteHotelRoomQuantityPerNight extends BaseTest {


    @Test(priority = 1, description = "Create MYCE Quote where Pax = 5 and Departure Date = Arrival Date + 2," +
            " Instantiate a Quote Hotel Room. Expected result: A Quote Hotel Room was created. Two Quote Hotel" +
            " Room Prices records were created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-591: Quote hotel room - quantity per night")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        rate.deleteRateSFDX(SFDX, "Name='QuoteHotelRoomQuantityPerNightAuto", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityPerNightAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord1= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord1.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityPerNightAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        String rateId = rate.createRateSFDX(SFDX, "Name='QuoteHotelRoomQuantityPerNightAuto'" +
                " thn__IsActive__c=true thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" + propertyID +
                "'", ORG_USERNAME);
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
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate__c='" + rateId + "' thn__Adjustment_excl_Tax__c=1 thn__Adjustment_incl_Tax__c=1" +
                " thn__RelativeValue__c=20 thn__AbsoluteValue__c=1", ORG_USERNAME);
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
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateId + "' thn__Property__c='" + propertyID + "'", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='"
                + quoteHotelRoomId + "'", ORG_USERNAME);
        String quoteHotelRoomUnitPrice = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomSalesPriceInclTax = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        StringBuilder quoteHotelRoomPrices = myceQuotes.soql(SFDX, "SELECT Id, thn__Sales_Price_incl_Tax__c," +
                " thn__Quantity__c FROM thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='"
                + quoteHotelRoomId + "'", ORG_USERNAME);
        List<String> quoteHotelRoomPricesID = JsonParser2.
                getFieldValueSoql(quoteHotelRoomPrices.toString(), "Id");
        List<Integer> quoteHotelRoomPricesSalesPriceInclTax = JsonParser2.
                getFieldValueSoql2(quoteHotelRoomPrices.toString(), "thn__Sales_Price_incl_Tax__c");
        List<Integer> quoteHotelRoomPricesQuantity = JsonParser2.getFieldValueSoql2(quoteHotelRoomPrices.toString(),
                "thn__Quantity__c");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "110");
        Assert.assertEquals(quoteHotelRoomSalesPriceInclTax, "1100");
        Assert.assertEquals(quoteHotelRoomPricesID.size(), 2);
        Assert.assertEquals(quoteHotelRoomPricesSalesPriceInclTax.get(0).intValue(), 550);
        Assert.assertEquals(quoteHotelRoomPricesSalesPriceInclTax.get(1).intValue(), 550);
        Assert.assertEquals(quoteHotelRoomPricesQuantity.get(0).intValue(), 5);
        Assert.assertEquals(quoteHotelRoomPricesQuantity.get(1).intValue(), 5);
    }

    @Test(priority = 2, description = "Change the Quantity = 7 on the thirst quote hotel room price. Expected result:" +
            " Sales Price  on Quote Hotel Room Price and Sales Price on Quote Hotel Room are changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-591: Quote hotel room - quantity per night")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityPerNightAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder quoteHotelRoomPrices = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                ORG_USERNAME);
        List<String> quoteHotelRoomPricesID = JsonParser2.
                getFieldValueSoql(quoteHotelRoomPrices.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + quoteHotelRoomPricesID.get(0) + "'",
                "thn__Quantity__c=7", ORG_USERNAME);
        StringBuilder updatedQuoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomSalesPriceInclTax = JsonParser2.
                getFieldValue(updatedQuoteHotelRoomRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        StringBuilder quoteHotelRoomPrice1 = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX, "Id='" +
                quoteHotelRoomPricesID.get(0) + "'", ORG_USERNAME );
        StringBuilder quoteHotelRoomPrice2 = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX, "Id='" +
                quoteHotelRoomPricesID.get(1) + "'", ORG_USERNAME );
        String quoteHotelRoomPriceQuantity1= JsonParser2.
                getFieldValue(quoteHotelRoomPrice1.toString(), "thn__Quantity__c");
        String quoteHotelRoomPriceQuantity2= JsonParser2.
                getFieldValue(quoteHotelRoomPrice2.toString(), "thn__Quantity__c");
        String quoteHotelRoomPriceSalesPriceInclTax1= JsonParser2.
                getFieldValue(quoteHotelRoomPrice1.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteHotelRoomPriceSalesPriceInclTax2= JsonParser2.
                getFieldValue(quoteHotelRoomPrice2.toString(), "thn__Sales_Price_incl_Tax__c");
        Assert.assertEquals(quoteHotelRoomSalesPriceInclTax, "1320");
        Assert.assertEquals(quoteHotelRoomPriceQuantity1, "7");
        Assert.assertEquals(quoteHotelRoomPriceQuantity2, "5");
        Assert.assertEquals(quoteHotelRoomPriceSalesPriceInclTax1, "770");
        Assert.assertEquals(quoteHotelRoomPriceSalesPriceInclTax2, "550");
    }

    @Test(priority = 3, description = "Change the Number = 3 on Quote Hotel Room record. Expected result:The" +
            " Quantity should change if the value of the QHR.Number matched the value of QHRP.Quantity." +
            " Prices also updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-591: Quote hotel room - quantity per night")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityPerNightAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Pax__c=3", ORG_USERNAME);
        StringBuilder updatedQuoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomSalesPriceInclTax = JsonParser2.
                getFieldValue(updatedQuoteHotelRoomRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String quoteHotelRoomID= JsonParser2.getFieldValue(updatedQuoteHotelRoomRecord.toString(), "Id");

        StringBuilder quoteHotelRoomPrice1 = myceQuotes.soql(SFDX, "SELECT thn__Sales_Price_incl_Tax__c FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'" +
                " AND thn__Quantity__c=7", ORG_USERNAME);
        StringBuilder quoteHotelRoomPrice2 = myceQuotes.soql(SFDX, "SELECT thn__Sales_Price_incl_Tax__c FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "' AND" +
                " thn__Quantity__c=3", ORG_USERNAME);
        List<Integer> quoteHotelRoomPriceSalesPriceInclTax1= JsonParser2.
                getFieldValueSoql2(quoteHotelRoomPrice1.toString(), "thn__Sales_Price_incl_Tax__c");
        List<Integer> quoteHotelRoomPriceSalesPriceInclTax2= JsonParser2.
                getFieldValueSoql2(quoteHotelRoomPrice2.toString(), "thn__Sales_Price_incl_Tax__c");
        Assert.assertEquals(quoteHotelRoomSalesPriceInclTax, "1100");
        Assert.assertEquals(quoteHotelRoomPriceSalesPriceInclTax1.get(0).intValue(), 770);
        Assert.assertEquals(quoteHotelRoomPriceSalesPriceInclTax2.get(0).intValue(), 330);
    }

    @Test(priority = 4, description = "Create Quote, create a Package and add a Hotel Room as Package Line." +
            " Instantiate the created Package to the Quote. Change the Quantity on the Quote Hotel Room price." +
            " Expected result: An error is thrown and Quantity isn’t changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-591: Quote hotel room - quantity per night")
    public void case4() throws InterruptedException, IOException {
        packages.deletePackageSFDX(SFDX, "Name='QuoteHotelRoomQuantityPackageAuto", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityPerNightAutoTest2'", ORG_USERNAME);
        StringBuilder hotelRecord1= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord1.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteHotelRoomQuantityPerNightAutoTest2'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='QuoteHotelRoomQuantityPackageAuto'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Hotel Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=0 thn__Space_Area__c='"
                + roomTypeQueenID + "'", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        StringBuilder updateQuoteHotelRoomPriceResult = quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomID +"'", "thn__Quantity__c=3", ORG_USERNAME);
        System.out.println(updateQuoteHotelRoomPriceResult);
        String message = JsonParser2.getFieldValue2(updateQuoteHotelRoomPriceResult.toString(), "message");
        StringBuilder quoteHotelRoomPricerecord = quoteHotelRoomPrice.getQuoteHotelRoomPriceSFDX(SFDX,
                "thn__Quote_Hotel_Room__c='" + quoteHotelRoomID +"'", ORG_USERNAME);
        String quoteHotelRoomPriceQuantity= JsonParser2.
                getFieldValue(quoteHotelRoomPricerecord.toString(), "thn__Quantity__c");
        Assert.assertEquals(message, "Quantity cannot be changed for packaged hotel rooms");
        Assert.assertEquals(quoteHotelRoomPriceQuantity, "5");
    }

}
