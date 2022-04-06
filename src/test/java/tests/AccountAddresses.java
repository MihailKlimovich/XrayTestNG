package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class AccountAddresses extends BaseTest {

    @Test(priority = 1, description = "Create a new Account. Fill in Account Name and the Billing address field." +
            " Expected Result: A new account is created. Copy Billing address checkbox is set to false. Billing" +
            " Address is copied to Shipping address.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-660: Account addresses.")
    public void case1() throws InterruptedException, IOException {
        accounts.deleteAccountSFDX(SFDX, "Name='AccountAddressesAutoTest'", ORG_USERNAME);
        String accountID = accounts.createAccountSFDX(SFDX, "Name='AccountAddressesAutoTest'" +
                " BillingCountry='Netherlands' BillingStreet='Damrak' BillingCity='Amsterdam' BillingPostalCode=545123",
                ORG_USERNAME);
        StringBuilder accountRecord = accounts.getAccountSFDX(SFDX, "Id='" + accountID + "'", ORG_USERNAME);
        System.out.println(accountRecord);
        String accountCoppyBillindAddress = JsonParser2.
                getFieldValue(accountRecord.toString(), "thn__Copy_Billing_address__c");
        String accountShipppingStreet = JsonParser2.
                getFieldValue(accountRecord.toString(), "ShippingStreet");
        String accountShipppingCity = JsonParser2.
                getFieldValue(accountRecord.toString(), "ShippingCity");
        String accountShipppingPostalCode = JsonParser2.
                getFieldValue(accountRecord.toString(), "ShippingPostalCode");
        Assert.assertEquals(accountCoppyBillindAddress, "false");
        Assert.assertEquals(accountShipppingStreet, "Damrak");
        Assert.assertEquals(accountShipppingCity, "Amsterdam");
        Assert.assertEquals(accountShipppingPostalCode, "545123");
    }

}
