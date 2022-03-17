package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import utils.Listeners.TestListener;

import java.io.IOException;

@Listeners({TestListener.class})

public class ComboProductTaxChange extends BaseTest {

    @Test(priority = 1, description = "Create a combo product, add combo components, on the component side, change the" +
            " VAT category. Expected result: on the combo components, the VAT is updated and the list price including" +
            " tax is recalculated")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-450: Combo product: tax change")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        product.deleteProductSFDX(SFDX, "Name='ComboAutoTest", ORG_USERNAME);
        product.deleteProductSFDX(SFDX, "Name='Coffee", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String coffeProductID = product.createProductSFDX(SFDX, "Name='Coffee' thn__Hotel__c='" + propertyID +
                "' thn__MYCE_Product_Type__c='Beverage' thn__Price_Gross_Value__c='120'" +
                " thn__VAT_Category__c=0", ORG_USERNAME);
        String comboProductId = product.createProductSFDX(SFDX, "Name='ComboAutoTest' thn__Hotel__c='"
                + propertyID + "' thn__MYCE_Product_Type__c='Beverage'", ORG_USERNAME);
        String productComponentId = product.createProductComboComponentSFDX(SFDX, "thn__Combo__c='"
                + comboProductId + "' thn__Component__c='" + coffeProductID + "' thn__Quantity__c=1", ORG_USERNAME);
        product.updateProductSFDX(SFDX, "Id='" + coffeProductID + "'", "thn__VAT_Category__c=2",
                ORG_USERNAME);
        StringBuilder productComponentRecord = product.
                getProductComboComponentSFDX(SFDX, "Id='" + productComponentId + "'", ORG_USERNAME);
        String taxCategoryProductComponent = JsonParser2.
                getFieldValue(productComponentRecord.toString(), "thn__Tax_Category__c");
        String listPriceInclTaxProductComponent = JsonParser2.
                getFieldValue(productComponentRecord.toString(), "thn__List_Price_incl_Tax__c");
        Assert.assertEquals(taxCategoryProductComponent, "2");
        Assert.assertEquals(listPriceInclTaxProductComponent, "144");
    }

}
