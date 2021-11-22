package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;

public class ErrorManagementNullPriceOnProduct extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Error management: null price on product")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Create a product and leave the price fields empty, Create a quote product/meeting room (depending on the" +
            " type of product you created). Expected result: the price of the record has been set to 0 instead of" +
            " having an error thrown.")
    public void case1() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='NullPriceOnProductAutoTest'", ORG_USERNAME);
        product.deleteProductSFDX(SFDX, "Name='WhiskeyAuto'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String productId = product.createProductSFDX(SFDX, "Name='WhiskeyAuto' thn__Hotel__c='" + propertyID +
                "' thn__MYCE_Product_Type__c='Beverage'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='NullPriceOnProductAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        String quoteProductId = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productId + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "Id='" + quoteProductId +
                "'", ORG_USERNAME);
        String salesPriceInclTaxQuoteProduct = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Sales_Price_incl_Tax__c");
        String salesPriceExclTaxQuoteProduct = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Sales_Price_excl_Tax__c");
        Assert.assertEquals(salesPriceExclTaxQuoteProduct, "0");
        Assert.assertEquals(salesPriceInclTaxQuoteProduct, "0");
    }

}
