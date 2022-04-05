package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class CloneMyceQuoteAndQHRPQuantity extends BaseTest{

    @Test(priority = 1, description = "Create a MYCE Quote (today + 2 days, Departure Date = today + 6 days)." +
            " Instantiate a Quote Hotel Room. Change the Quantity of the Quote Hotel Room Prices for the created" +
            " dates. Expected Result: Quantity of the Quote Hotel Room Prices are updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-685: Clone Myce Quote and QHRP.Quantity.")
    public void precondition() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneMyceQuoteAndQHRPQuantityAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='CloneMyceQuoteAndQHRPQuantityAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 6) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) + "'",
                ORG_USERNAME);
        StringBuilder qhrPriceRecord1 = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room_Price__c" +
                " WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "' AND thn__Date__c=" +
                date.generateTodayDate2_plus(0, 3), ORG_USERNAME);
        List<String> qhrPriceID1 = JsonParser2.getFieldValueSoql(qhrPriceRecord1.toString(), "Id");
        StringBuilder qhrPriceRecord2 = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room_Price__c" +
                " WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "' AND thn__Date__c=" +
                date.generateTodayDate2_plus(0, 4), ORG_USERNAME);
        List<String> qhrPriceID2 = JsonParser2.getFieldValueSoql(qhrPriceRecord2.toString(), "Id");
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + qhrPriceID1.get(0) + "'",
                "thn__Quantity__c=10", ORG_USERNAME);
        quoteHotelRoomPrice.updateQuoteHotelRoomPriceSFDX(SFDX, "Id='" + qhrPriceID2.get(0) + "'",
                "thn__Quantity__c=100", ORG_USERNAME);
        StringBuilder qhrPriceRecords = myceQuotes.soql(SFDX, "SELECT thn__Quantity__c FROM" +
                " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                ORG_USERNAME);
        List<Integer> qhrPriceQuantity = JsonParser2.
                getFieldValueSoql2(qhrPriceRecords.toString(), "thn__Quantity__c");
        Assert.assertEquals(qhrPriceQuantity.get(0).intValue(), 5);
        Assert.assertEquals(qhrPriceQuantity.get(1).intValue(), 10);
        Assert.assertEquals(qhrPriceQuantity.get(2).intValue(), 100);
        Assert.assertEquals(qhrPriceQuantity.get(3).intValue(), 5);
    }

    @Test(priority = 2, description = "Use the Clone MYCE Quote button on the Quote. Select a new Arrival Date." +
            " Pax = 10. Keep all Pax = False. Keep rooms Pax = False. Expected Result: New Pax value on the" +
            " MYCE Quote = 10. QHR.Number = 10. QHRP.Quantity = 10 for all dates.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-685: Clone Myce Quote and QHRP.Quantity.")
    public void case1() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneMyceQuoteAndQHRPQuantityAutoTestClone'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("CloneMyceQuoteAndQHRPQuantityAutoTest");
        myceQuotes.cloneMyceQuote_changeDateAndPax("CloneMyceQuoteAndQHRPQuantityAutoTestClone",
                date.generateTodayDate3_plus(0, 3), "10");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX,
                "Name='CloneMyceQuoteAndQHRPQuantityAutoTestClone'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        Integer quotePax= JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Pax__c");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                myceQuoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        Integer quoteHotelRoomPax= JsonParser2.
                getFieldValueLikeInteger(quoteHotelRoomRecord, "result", "thn__Pax__c");
        StringBuilder qhrPriceRecords = myceQuotes.soql(SFDX, "SELECT thn__Quantity__c FROM" +
                        " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                ORG_USERNAME);
        List<Integer> qhrPriceQuantity = JsonParser2.
                getFieldValueSoql2(qhrPriceRecords.toString(), "thn__Quantity__c");
        Assert.assertEquals(quotePax.intValue(), 10);
        Assert.assertEquals(quoteHotelRoomPax.intValue(), 10);
        Assert.assertEquals(qhrPriceQuantity.get(0).intValue(), 10);
        Assert.assertEquals(qhrPriceQuantity.get(1).intValue(), 10);
        Assert.assertEquals(qhrPriceQuantity.get(2).intValue(), 10);
        Assert.assertEquals(qhrPriceQuantity.get(3).intValue(), 10);
    }

    @Test(priority = 3, description = "Use the Clone MYCE Quote button on the original Quote. Select a new" +
            " Arrival Date. Pax = 100. Keep all Pax = true. Keep rooms Pax = False. Expected Result: New Pax value" +
            " on the MYCE Quote = 100. QHR.Number = 5. QHRP.Quantity = 5 for all dates")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-685: Clone Myce Quote and QHRP.Quantity.")
    public void case2() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneMyceQuoteAndQHRPQuantityAutoTestClone2'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("CloneMyceQuoteAndQHRPQuantityAutoTest");
        myceQuotes.cloneMyceQuote_changeDateAndPaxKeepAllPax("CloneMyceQuoteAndQHRPQuantityAutoTestClone2",
                date.generateTodayDate3_plus(0, 3), "100");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX,
                "Name='CloneMyceQuoteAndQHRPQuantityAutoTestClone2'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        Integer quotePax= JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Pax__c");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                myceQuoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        Integer quoteHotelRoomPax= JsonParser2.
                getFieldValueLikeInteger(quoteHotelRoomRecord, "result", "thn__Pax__c");
        StringBuilder qhrPriceRecords = myceQuotes.soql(SFDX, "SELECT thn__Quantity__c FROM" +
                        " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                ORG_USERNAME);
        List<Integer> qhrPriceQuantity = JsonParser2.
                getFieldValueSoql2(qhrPriceRecords.toString(), "thn__Quantity__c");
        Assert.assertEquals(quotePax.intValue(), 100);
        Assert.assertEquals(quoteHotelRoomPax.intValue(), 5);
        Assert.assertEquals(qhrPriceQuantity.get(0).intValue(), 5);
        Assert.assertEquals(qhrPriceQuantity.get(1).intValue(), 5);
        Assert.assertEquals(qhrPriceQuantity.get(2).intValue(), 5);
        Assert.assertEquals(qhrPriceQuantity.get(3).intValue(), 5);
    }

    @Test(priority = 4, description = "Use the Clone MYCE Quote button on the original Quote. Select a new" +
            " Arrival Date. Pax = 60. Keep all Pax = false. Keep rooms Pax = true. Expected Result: New Pax value" +
            " on the MYCE Quote = 60. QHR.Number = 5. QHRP.Quantity = 5 for all dates.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-685: Clone Myce Quote and QHRP.Quantity.")
    public void case3() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneMyceQuoteAndQHRPQuantityAutoTestClone3'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("CloneMyceQuoteAndQHRPQuantityAutoTest");
        myceQuotes.cloneMyceQuote_changeDateAndPaxKeepRoomPax("CloneMyceQuoteAndQHRPQuantityAutoTestClone3",
                date.generateTodayDate3_plus(0, 3), "60");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX,
                "Name='CloneMyceQuoteAndQHRPQuantityAutoTestClone3'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        Integer quotePax= JsonParser2.
                getFieldValueLikeInteger(quoteRecord, "result", "thn__Pax__c");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                myceQuoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID= JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        Integer quoteHotelRoomPax= JsonParser2.
                getFieldValueLikeInteger(quoteHotelRoomRecord, "result", "thn__Pax__c");
        StringBuilder qhrPriceRecords = myceQuotes.soql(SFDX, "SELECT thn__Quantity__c FROM" +
                        " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                ORG_USERNAME);
        List<Integer> qhrPriceQuantity = JsonParser2.
                getFieldValueSoql2(qhrPriceRecords.toString(), "thn__Quantity__c");
        Assert.assertEquals(quotePax.intValue(), 60);
        Assert.assertEquals(quoteHotelRoomPax.intValue(), 5);
        Assert.assertEquals(qhrPriceQuantity.get(0).intValue(), 5);
        Assert.assertEquals(qhrPriceQuantity.get(1).intValue(), 5);
        Assert.assertEquals(qhrPriceQuantity.get(2).intValue(), 5);
        Assert.assertEquals(qhrPriceQuantity.get(3).intValue(), 5);
    }


}
