package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class UpdateOrders extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update Orders")
    public void logIn() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/UpdateGenerateXDaysBefore");
    }

    @Test(priority = 2, description = "Preconditions: Create a quote with products and meeting rooms. Click the" +
            " ‘ProdMod’ Button the the Quote that we created. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update Orders")
    public void preconditions() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest2'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest3'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " thn__Closed_Status__c='Won' thn__Stage__c='4 - Closed'", ORG_USERNAME);
        String quoteMeetingRoomID = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID + "'", ORG_USERNAME);
        String unitPriceQMR = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + productDinerID + "'", ORG_USERNAME);

        String quoteID2 = myceQuotes.createQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest2' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " thn__Closed_Status__c='Won' thn__Stage__c='4 - Closed'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID2 + "' thn__Product__c='" +
                productDinerID + "'", ORG_USERNAME);
        String quoteID3 = myceQuotes.createQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest3' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " thn__Closed_Status__c='Won' thn__Stage__c='4 - Closed'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID3 + "' thn__Product__c='" +
                productDinerID + "'", ORG_USERNAME);

        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        String unitPriceQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        //myceQuotes.goToMyceQuotes();
        //myceQuotes.openMyceQoteRecord("UpdateOrdersAutoTest");
        //myceQuotes.updateOrder();
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/BatchCreateOrders");
        StringBuilder orderRecord = order.getOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String orderID = JsonParser2.getFieldValue(orderRecord.toString(), "Id");
        String orderConsumptionDate = JsonParser2.getFieldValue(orderRecord.toString(), "thn__ConsumptionUtc__c");
        StringBuilder orderLineRecord1 = order.getOrderLineSFDX(SFDX, "thn__Order__c='" + orderID + "'" +
                " thn__Quote_Meetings_Room__c='"+ quoteMeetingRoomID + "'", ORG_USERNAME);
        String GrossValue1 = JsonParser2.getFieldValue(orderLineRecord1.toString(), "thn__GrossValue__c");
        StringBuilder orderLineRecord2 = order.getOrderLineSFDX(SFDX, "thn__Order__c='" + orderID + "'" +
                " thn__Quote_Product__c='"+ quoteProductID + "'", ORG_USERNAME);
        String GrossValue2 = JsonParser2.getFieldValue(orderLineRecord2.toString(), "thn__GrossValue__c");
        Assert.assertEquals(orderConsumptionDate, date.generateTodayDate2());
        Assert.assertEquals(GrossValue1, unitPriceQMR);
        Assert.assertEquals(GrossValue2, unitPriceQuoteProduct);
    }

    @Test(priority = 3, description = "Edit the ‘MEWS Id’ field of our created Order, click the ‘ProdMod’. Result: No" +
            " new orders were created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update Orders")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder orderRecord = order.getOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String orderID = JsonParser2.getFieldValue(orderRecord.toString(), "Id");
        order.updateOrderSFDX(SFDX, "Id='" + orderID + "'", "thn__Mews_Id__c='" +
                (int) ( Math.random() * 300000 ) + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("UpdateOrdersAutoTest");
        myceQuotes.updateOrder();
        StringBuilder orders = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Order__c where thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> ordersId = JsonParser2.getFieldValueSoql(orders.toString(), "Id");
        Assert.assertEquals(ordersId.size(), 1);
    }

    @Test(priority = 4, description = "Change the ‘Pax’ on quote Product. Result:a duplicate ‘Order’ was crated, A new" +
            " 'Order’ was created and an ‘Order Line’ related record for our ‘Quote Product’ with the new changed pax. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update Orders")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder orderRecord = order.getOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String orderID = JsonParser2.getFieldValue(orderRecord.toString(), "Id");
        order.updateOrderSFDX(SFDX, "Id='" + orderID + "'", "thn__Mews_Id__c='" +
                (int) ( Math.random() * 300000 ) + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", "thn__Pax__c=3",
                ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("UpdateOrdersAutoTest");
        myceQuotes.updateOrder();
        StringBuilder orders = myceQuotes.
                soql(SFDX, "SELECT Id, Name, thn__Mews_Id__c, thn__Status__c from thn__Order__c where" +
                        " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(orders);
        List<String> ordersId = JsonParser2.getFieldValueSoql(orders.toString(), "Id");
        StringBuilder oldOrderRecord = order.getOrderSFDX(SFDX, "Id='" + ordersId.get(0) + "'", ORG_USERNAME);
        StringBuilder duplicateOrderRecord = order.getOrderSFDX(SFDX, "Id='" + ordersId.get(1) + "'", ORG_USERNAME);
        String oldOrderStatus = JsonParser2.getFieldValue(oldOrderRecord.toString(), "thn__Status__c");
        String duplicateOrderName = JsonParser2.getFieldValue(duplicateOrderRecord.toString(), "Name");
        StringBuilder orderLineRecord = order.getOrderLineSFDX(SFDX, "thn__Order__c='" + ordersId.get(2) + "'" +
                " Name='DINER'", ORG_USERNAME);
        String paxNewOrderLine = JsonParser2.getFieldValue(orderLineRecord.toString(), "thn__Quantity__c");
        Assert.assertEquals(ordersId.size(), 3);
        Assert.assertEquals(oldOrderStatus, "Cancelled");
        Assert.assertEquals(duplicateOrderName, "Stay (Duplicate)");
        Assert.assertEquals(paxNewOrderLine, "3");
    }

    @Test(priority = 5, description = "Change the ‘Start Date Time’ on quote Product. Result:a duplicate ‘Order’ was" +
            " crated, A new 'Order’ was created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update Orders")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest2'", ORG_USERNAME);
        System.out.println(quoteRecord);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder orderRecord = order.getOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String orderID = JsonParser2.getFieldValue(orderRecord.toString(), "Id");
        order.updateOrderSFDX(SFDX, "Id='" + orderID + "'", "thn__Mews_Id__c='" +
                (int) ( Math.random() * 300000 ) + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Start_Date_Time__c=" + date.generateTodayDate2_minus(0, 1) +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2_minus(0, 1), ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("UpdateOrdersAutoTest2");
        myceQuotes.updateOrder();
        StringBuilder orders = myceQuotes.
                soql(SFDX, "SELECT Id, Name, thn__Mews_Id__c, thn__Status__c from thn__Order__c where" +
                        " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(orders);
        List<String> ordersId = JsonParser2.getFieldValueSoql(orders.toString(), "Id");
        StringBuilder oldOrderRecord = order.getOrderSFDX(SFDX, "Id='" + ordersId.get(0) + "'", ORG_USERNAME);
        StringBuilder duplicateOrderRecord = order.getOrderSFDX(SFDX, "Id='" + ordersId.get(1) + "'", ORG_USERNAME);
        String oldOrderStatus = JsonParser2.getFieldValue(oldOrderRecord.toString(), "thn__Status__c");
        String duplicateOrderName = JsonParser2.getFieldValue(duplicateOrderRecord.toString(), "Name");
        Assert.assertEquals(ordersId.size(), 3);
        Assert.assertEquals(oldOrderStatus, "Cancelled");
        Assert.assertEquals(duplicateOrderName, "Stay (Duplicate)");
    }

    @Test(priority = 6, description = "Change the ‘Unit price’ on quote Product. Result:a duplicate ‘Order’ was" +
            " crated, A new 'Order’ was created and an ‘Order Line’ related record for our ‘Quote Product’ with the new" +
            " changed 'Gross Value'. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update Orders")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest3'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder orderRecord = order.getOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String orderID = JsonParser2.getFieldValue(orderRecord.toString(), "Id");
        order.updateOrderSFDX(SFDX, "Id='" + orderID + "'", "thn__Mews_Id__c='" +
                (int) ( Math.random() * 300000 ) + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Unit_Price__c=55", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("UpdateOrdersAutoTest3");
        myceQuotes.updateOrder();
        StringBuilder orders = myceQuotes.
                soql(SFDX, "SELECT Id, Name, thn__Mews_Id__c, thn__Status__c from thn__Order__c where" +
                        " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(orders);
        List<String> ordersId = JsonParser2.getFieldValueSoql(orders.toString(), "Id");
        StringBuilder oldOrderRecord = order.getOrderSFDX(SFDX, "Id='" + ordersId.get(0) + "'", ORG_USERNAME);
        StringBuilder duplicateOrderRecord = order.getOrderSFDX(SFDX, "Id='" + ordersId.get(1) + "'", ORG_USERNAME);
        String oldOrderStatus = JsonParser2.getFieldValue(oldOrderRecord.toString(), "thn__Status__c");
        String duplicateOrderName = JsonParser2.getFieldValue(duplicateOrderRecord.toString(), "Name");
        StringBuilder orderLineRecord = order.getOrderLineSFDX(SFDX, "thn__Order__c='" + ordersId.get(2) + "'" +
                " Name='DINER'", ORG_USERNAME);
        String grossValueNewOrderLine = JsonParser2.getFieldValue(orderLineRecord.toString(), "thn__GrossValue__c");
        Assert.assertEquals(ordersId.size(), 3);
        Assert.assertEquals(oldOrderStatus, "Cancelled");
        Assert.assertEquals(duplicateOrderName, "Stay (Duplicate)");
        Assert.assertEquals(grossValueNewOrderLine, "55");
    }




    /*@Test(priority = 3, description = "Run Batch 'Create orders'")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update Orders")
    public void runBatch() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='UpdateOrdersAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCode("List<MYCE_Quote__c> quotes =  [SELECT Id,name, No_VAT__c," +
                " Reservation_Guest__c, Hotel__r.Mews_Default_Service_Id__c, Arrival_Date__c, (SELECT Id, Name," +
                " Unit_Price_incl_Tax__c, Pax__c, VAT_Category__c, Start_Date_Time__c, Product__c, Product__r.Service__c, " +
                "Product__r.Mews_Id__c, Product__r.Service__r.Name, Product__r.Accounting_Category__c," +
                " Product__r.Accounting_Category__r.Mews_Id__c, Product__r.No_Order__c, Property__c," +
                " Price_per_Person__c, Product__r.POS_Id__c, Unit_Price_excl_Tax__c, LastModifiedDate FROM" +
                " Quote_Meeting_Rooms__r WHERE Product__c != null AND Product__r.No_Order__c = FALSE AND" +
                " Shadow__c = false AND Optional__c = false), (SELECT Id, Name, Unit_Price_incl_Tax__c, Pax__c," +
                " VAT_Category__c, Start_Date_Time__c, Product__c, Product__r.Service__c, Combo__c," +
                " Product__r.Mews_Id__c, Product__r.Service__r.Name, Product__r.Accounting_Category__c," +
                " Product__r.Accounting_Category__r.Mews_Id__c, Product__r.No_Order__c, Property__c," +
                " Product__r.POS_Id__c, Unit_Price_excl_Tax__c, Unit__c, LastModifiedDate FROM Quote_Products__r WHERE" +
                " Product__c != null AND Product__r.No_Order__c = FALSE AND Optional__c = false ), ( SELECT Id, Name," +
                " ConsumptionUtc__c, Guest__c, Mews_Error_Message__c, Mews_Id__c, Mews_Service__c, Send_to_Mews__c FROM" +
                " Orders__r ) FROM Myce_Quote__c WHERE Id = '" + quoteID + "'];" +
                " BatchCreateOrdersHelper.createOrders(quotes, false);");
    }*/

}
