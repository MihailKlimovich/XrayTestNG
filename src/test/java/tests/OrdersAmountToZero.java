package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class OrdersAmountToZero extends BaseTest {

    @Test(priority = 1, description = "In CMT > DAV > Do not price orders == True. Create a MYCE Quote. Instantiate" +
            " a Quote Product. Change the Stage of the MYCE Quote to ‘4 - Closed’ and Closed Status 'Won'. Wait for" +
            " the BatchCreateOrders to be ran via the Global scheduling. Wait for the BatchOrders to be ran via the" +
            " Global scheduling. Order is created and sent to Mews. On the Mews side the Price of the order is 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-696: Orders amount to 0")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OrdersAmountToZeroAutoTest'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DoNotPriceOrdersTrueForDemo.apex");
        mews.logIn("thynk@mews.li", "sample");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OrdersAmountToZeroAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + "" +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                beverageID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchCreateOrders");
        Thread.sleep(10000);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchOrders.apex");
        Thread.sleep(10000);
        StringBuilder orderRecord = order.getOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String orderID = JsonParser2.getFieldValue(orderRecord.toString(), "Id");
        String orderDoNotPriceOrder = JsonParser2.
                getFieldValue(orderRecord.toString(), "thn__Do_not_price_order__c");
        String orderCustomer = JsonParser2.getFieldValue(orderRecord.toString(), "thn__Guest__c");
        StringBuilder guestRecord = guests.getGuestSFDX(SFDX, "Id='" + orderCustomer + "'", ORG_USERNAME);
        String guestMewsID = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Mews_Id__c");
        mews.goToMainPage().findRecordByID(guestMewsID, "OrdersAmountToZeroAutoTest");
        mews.clickDashBoardTab();
        String totalAmount = mews.readTotalAmountOrders();
        Assert.assertEquals(orderDoNotPriceOrder, "true");
        Assert.assertEquals(totalAmount, "€0.00");
    }

    @Test(priority = 2, description = "In CMT > DAV > Do not price orders == True. Create a MYCE Quote. Instantiate" +
            " a Quote Product. Change the Stage of the MYCE Quote to ‘4 - Closed’ and Closed Status 'Won'. Wait for" +
            " the BatchCreateOrders to be ran via the Global scheduling. Wait for the BatchOrders to be ran via the" +
            " Global scheduling. Order is created and sent to Mews. On the Mews side the Price of the order is 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-696: Orders amount to 0")
    public void case2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OrdersAmountToZeroAutoTest2'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DoNotPriceOrdersFalseForDemo.apex");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OrdersAmountToZeroAutoTest2' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + "" +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                beverageID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchCreateOrders");
        Thread.sleep(10000);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchOrders.apex");
        Thread.sleep(10000);
        StringBuilder orderRecord = order.getOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String orderID = JsonParser2.getFieldValue(orderRecord.toString(), "Id");
        String orderDoNotPriceOrder = JsonParser2.
                getFieldValue(orderRecord.toString(), "thn__Do_not_price_order__c");
        String orderCustomer = JsonParser2.getFieldValue(orderRecord.toString(), "thn__Guest__c");
        StringBuilder guestRecord = guests.getGuestSFDX(SFDX, "Id='" + orderCustomer + "'", ORG_USERNAME);
        String guestMewsID = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Mews_Id__c");
        mews.goToMainPage().findRecordByID(guestMewsID, "OrdersAmountToZeroAutoTest2");
        mews.clickDashBoardTab();
        String totalAmount = mews.readTotalAmountOrders();
        Assert.assertEquals(orderDoNotPriceOrder, "false");
        Assert.assertEquals(totalAmount, "€300.00");
    }

}
