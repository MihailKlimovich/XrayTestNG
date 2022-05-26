package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class CancellationOfOrders extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote. Instantiate a Quote Product. Change the Stage of the" +
            " Quote to ‘Closed’ and Status to ‘Won’. Run the Batch to create orders. Open the created Order and make" +
            " sure that Mews id!=null. Change the stage of the Quote to Closed - Canceled or any of the the lost" +
            " ones (custom settings stages - lost statuses). Expected Result: Duplicate order is created with" +
            " negative values and sent to Mews.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-644: Cancellation of Orders")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CancellationOfOrdersAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='CancellationOfOrdersAutoTest' thn__Pax__c=5" +
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
        Thread.sleep(15000);
        StringBuilder orderRecord = order.getOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String orderID = JsonParser2.getFieldValue(orderRecord.toString(), "Id");
        String totalOrder = JsonParser2.getFieldValue(orderRecord.toString(), "thn__Total_Order__c");
        String mewsIdOrder = JsonParser2.getFieldValue(orderRecord.toString(), "thn__Mews_Id__c");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Cancelled'", ORG_USERNAME);
        StringBuilder newOrderRecord = myceQuotes.soql(SFDX, "SELECT Id, thn__Total_Order__c FROM thn__Order__c" +
                " WHERE thn__MYCE_Quote__c='" + quoteID + "' AND Id!='" + orderID + "'", ORG_USERNAME);
        List<Integer> newOrderTotalOrder = JsonParser2.
                getFieldValueSoql2(newOrderRecord.toString(), "thn__Total_Order__c");
        System.out.println(newOrderRecord);
        Assert.assertNotNull(mewsIdOrder);
        Assert.assertEquals(totalOrder, "300");
        Assert.assertEquals(newOrderTotalOrder.get(0).intValue(), -300);


    }

}
