package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;
import utils.Listeners.TestListener;

import java.io.IOException;

@Listeners({TestListener.class})

public class OnConsumption extends BaseTest {

    @Test(priority = 1, description = "Add Quote product on MYCE Quote. Set 5 for Pax, On consumption == true." +
            " Result: Pax on Quote product is set to 0")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-570: On Consumption")
    public void testOnConsumption1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test On Consumption 1'", ORG_USERNAME);
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test On Consumption 1' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=5 thn__On_Consumption__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});

        String onConsumption = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__On_Consumption__c");
        String paxQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Pax__c");
        Assert.assertEquals(onConsumption, "true");
        Assert.assertEquals(paxQuoteProduct, "0");
    }

    @Test(priority = 2, description = "Add Quote product on MYCE Quote. Do not fill Pax field," +
            " On consumption == true. Result: Pax on Quote product is set to 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-570: On Consumption")
    public void testOnConsumption2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test On Consumption 2'", ORG_USERNAME);
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test On Consumption 2' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__On_Consumption__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String onConsumption = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__On_Consumption__c");
        String paxQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Pax__c");
        Assert.assertEquals(onConsumption, "true");
        Assert.assertEquals(paxQuoteProduct, "0");
    }

    @Test(priority = 3, description = "Edit Pax on Quote product, On consumption == true." +
            " Result: Pax value is not changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-570: On Consumption")
    public void testOnConsumption3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test On Consumption 3'", ORG_USERNAME);
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test On Consumption 3' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__On_Consumption__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
               "-v",
               "thn__Pax__c=3",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String onConsumption = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__On_Consumption__c");
        String paxQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Pax__c");
        Assert.assertEquals(onConsumption, "true");
        Assert.assertEquals(paxQuoteProduct, "0");
    }

    @Test(priority = 4, description = "Set On consumption to true on existing Quote product. Result: Pax on Quote" +
            " product is set to 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-570: On Consumption")
    public void testOnConsumption4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test On Consumption 4'", ORG_USERNAME);
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='Test On Consumption 4' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Pax__c=5",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
                "-v",
                "thn__On_Consumption__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteProductRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Quote_Product__c",
                "-w",
                "Id='" + quoteProductId + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String onConsumption = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__On_Consumption__c");
        String paxQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Pax__c");
        Assert.assertEquals(onConsumption, "true");
        Assert.assertEquals(paxQuoteProduct, "0");
    }

    @Test(priority = 5, description = "Add Quote product on MYCE Quote. Always on consumption == true on Product." +
            " Result: Pax on Quote product is set to 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-570: On Consumption")
    public void testOnConsumption5() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test On Consumption 5'", ORG_USERNAME);
        product.deleteProductSFDX(SFDX, "Name='GinAuto'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String productId = product.createProductSFDX(SFDX, "Name='GinAuto' thn__Hotel__c='" + propertyID +
                "' thn__MYCE_Product_Type__c='Beverage' thn__Always_On_Consumption__c=true", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='Test On Consumption 5' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2), ORG_USERNAME);
        String quoteProductId = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productId + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "Id='" + quoteProductId +
                "'", ORG_USERNAME);
        String paxQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Pax__c");
        Assert.assertEquals(paxQuoteProduct, "0");
    }

    @Test(priority = 6, description = "Edit Pax on Quote product where 'Always on consumption' == true on Product." +
            " Result: Pax value is not changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-570: On Consumption")
    public void testOnConsumption6() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Test On Consumption 6'", ORG_USERNAME);
        product.deleteProductSFDX(SFDX, "Name='VodkaAuto'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String productId = product.createProductSFDX(SFDX, "Name='VodkaAuto' thn__Hotel__c='" + propertyID +
                "' thn__MYCE_Product_Type__c='Beverage' thn__Always_On_Consumption__c=true", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='Test On Consumption 6' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2), ORG_USERNAME);
        String quoteProductId = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productId + "'", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "Id='" + quoteProductId + "'", "thn__Pax__c=3",
                ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "Id='" + quoteProductId +
                "'", ORG_USERNAME);
        String paxQuoteProduct = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__Pax__c");
        Assert.assertEquals(paxQuoteProduct, "0");
    }
}
