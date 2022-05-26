package tests.TB;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import tests.BaseTest;

import java.io.IOException;
import java.util.List;

public class OrdersWithNegativeRevenueWereNotCreated extends BaseTest {

    @Test(priority = 1, description = "In CMT > DAV > Generate days before = 0. Create a MYCE Quote. Instantiated" +
            " a Quote Product (Start date time = today). Generated orders via the executed batch BatchCreateOrders." +
            " Run to batch to send the orders to Mews. Expected result: Order is sent to mews. MewsId!=null.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-403: Orders with negative revenue were not created, this resulted in discrepancy between Thynk and Mews")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/GenerateDaysBefore0ForDemo.apex");
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB403AutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='TB403AutoTest' thn__Pax__c=20 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 5) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                beverageID + "' thn__Pax__c=5", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchCreateOrders");
        Thread.sleep(10000);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchOrders.apex");
        Thread.sleep(15000);
        order.updateOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", "thn__Mews_Id__c='" + Math.random() + "'",
                ORG_USERNAME);
        StringBuilder orderRecord = order.getOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String orderID = JsonParser2.getFieldValue(orderRecord.toString(), "Id");
        String mewsIdOrder = JsonParser2.getFieldValue(orderRecord.toString(), "thn__Mews_Id__c");
        Assert.assertNotNull(orderID);
        Assert.assertNotNull(mewsIdOrder);
    }

    @Test(priority = 2, description = "Go to the Quote Product and change the pax (Pax = 10). Press the Product" +
            " Modification (order update) on the MYCE Quote. Expected result: The initial order with pax = 5 has" +
            " been cancelled. Status = Cancelled. One duplicate order with pax = 5 with Negative value is created." +
            " One more order is created with new pax value pax = 10.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-403: Orders with negative revenue were not created, this resulted in discrepancy between Thynk and Mews")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='TB403AutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", "thn__Pax__c=10",
                ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("TB403AutoTest");
        myceQuotes.updateOrder();
        StringBuilder orderRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Status__c, thn__Total_Order__c," +
                " thn__isDuplicate__c FROM thn__Order__c WHERE thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(orderRecords);
        List<String> ordersID = JsonParser2.getFieldValueSoql(orderRecords.toString(), "Id");
        Assert.assertEquals(ordersID.size(), 3);
        StringBuilder orderRecord1 = order.getOrderSFDX(SFDX, "Id='" + ordersID.get(0) + "'", ORG_USERNAME);
        StringBuilder orderRecord2 = order.getOrderSFDX(SFDX, "Id='" + ordersID.get(1) + "'", ORG_USERNAME);
        StringBuilder orderRecord3 = order.getOrderSFDX(SFDX, "Id='" + ordersID.get(2) + "'", ORG_USERNAME);
        String order1Status= JsonParser2.getFieldValue(orderRecord1.toString(), "thn__Status__c");
        String order2Status= JsonParser2.getFieldValue(orderRecord2.toString(), "thn__Status__c");
        String order3Status= JsonParser2.getFieldValue(orderRecord3.toString(), "thn__Status__c");
        String order1TotalOrder= JsonParser2.getFieldValue(orderRecord1.toString(), "thn__Total_Order__c");
        String order2TotalOrder= JsonParser2.getFieldValue(orderRecord2.toString(), "thn__Total_Order__c");
        String order3TotalOrder= JsonParser2.getFieldValue(orderRecord3.toString(), "thn__Total_Order__c");
        String order1isDuplicate= JsonParser2.getFieldValue(orderRecord1.toString(), "thn__isDuplicate__c");
        String order2isDuplicate= JsonParser2.getFieldValue(orderRecord2.toString(), "thn__isDuplicate__c");
        String order3isDuplicate= JsonParser2.getFieldValue(orderRecord3.toString(), "thn__isDuplicate__c");
        Assert.assertEquals(order1Status, "Cancelled");
        Assert.assertEquals(order2Status, null);
        Assert.assertEquals(order3Status, null);
        Assert.assertEquals(order1TotalOrder, "300");
        Assert.assertEquals(order2TotalOrder, "-300");
        Assert.assertEquals(order3TotalOrder, "600");
        Assert.assertEquals(order1isDuplicate, "false");
        Assert.assertEquals(order2isDuplicate, "true");
        Assert.assertEquals(order3isDuplicate, "false");
    }

    @Test(priority = 3, description = "Instantiate a Quote product with Start date = Today +1. Change the settings" +
            " > CMT > DAV > Generate days before = 1. Generated orders via the executed batch BatchCreateOrders. Run" +
            " to batch to send the orders to Mews. Expected result: Order is sent to mews. MewsId!=null.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-403: Orders with negative revenue were not created, this resulted in discrepancy between Thynk and Mews")
    public void case3() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/GenerateDaysBefore1ForDemo.apex");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='TB403AutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                productDinerID + "' thn__Pax__c=5 thn__Start_Date_Time__c=" +
                date.generateTodayDate2_plus(0, 1) + "T02:15:00.000Z thn__End_Date_Time__c=" +
                date.generateTodayDate2_plus(0, 1) + "T09:30:00.000Z", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchCreateOrders");
        Thread.sleep(10000);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchOrders.apex");
        Thread.sleep(15000);
        StringBuilder orderRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Status__c, thn__Total_Order__c," +
                " thn__isDuplicate__c FROM thn__Order__c WHERE thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(orderRecords);
        List<String> ordersID = JsonParser2.getFieldValueSoql(orderRecords.toString(), "Id");
        order.updateOrderSFDX(SFDX, "Id='" + ordersID.get(4) + "'", "thn__Mews_Id__c='" + Math.random() +
                "'", ORG_USERNAME);
        Assert.assertEquals(ordersID.size(), 4);
    }

    @Test(priority = 4, description = "Press the Product Modification (order update) on the MYCE Quote. Expected" +
            " result: No new orders are created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-403: Orders with negative revenue were not created, this resulted in discrepancy between Thynk and Mews")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='TB403AutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", "thn__Pax__c=10",
                ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("TB403AutoTest");
        myceQuotes.updateOrder();
        StringBuilder orderRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Status__c, thn__Total_Order__c," +
                " thn__isDuplicate__c FROM thn__Order__c WHERE thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(orderRecords);
        List<String> ordersID = JsonParser2.getFieldValueSoql(orderRecords.toString(), "Id");
        Assert.assertEquals(ordersID.size(), 4);
    }

}
