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
import java.util.List;

@Listeners({TestListener.class})

public class MyceQuoteAndDeposits extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Myce quote and deposits")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Preconditions: Create a new ‘MYCE Quote’, create a couple  new Deposits with" +
            " various ‘Deposit numbers’. The deposit which number is lower is going to be used first.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update Orders")
    public void preconditions() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteAndDepositAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteAndDepositAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " thn__Closed_Status__c='Won' thn__Stage__c='4 - Closed'", ORG_USERNAME);
        deposit.createDepositSFDX(SFDX,"thn__MYCE_Quote__c='" + quoteID + "' thn__Amount__c=1000 thn__Date__c=" +
                date.generateTodayDate2() + " thn__Payment_Method__c='Cash' thn__Deposit_number__c=1", ORG_USERNAME);
        deposit.createDepositSFDX(SFDX,"thn__MYCE_Quote__c='" + quoteID + "' thn__Amount__c=1500 thn__Date__c=" +
                date.generateTodayDate2() + " thn__Payment_Method__c='Cash' thn__Deposit_number__c=2", ORG_USERNAME);
    }

    @Test(priority = 3, description = "Preconditions: Create Payments on the ‘MYCE Quote’, the ‘Gross Value' = 1500’.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update Orders")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAndDepositAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String guestID = guests.
                createGuestSFDX(SFDX, "thn__FirstName__c='John' thn__LastName__c='Doe'", ORG_USERNAME);
        payment.createPaymentSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Guest__c='" + guestID +
                "' thn__Gross_Value__c=1500 thn__Type__c='Cash'", ORG_USERNAME);
        StringBuilder deposits1 = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Deposit__c where" +
                        " thn__MYCE_Quote__c='" + quoteID + "' AND thn__Deposit_number__c=1", ORG_USERNAME);
        System.out.println(deposits1);
        List<String> depositId1 = JsonParser2.getFieldValueSoql(deposits1.toString(), "Id");
        StringBuilder deposits2 = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Deposit__c where" +
                        " thn__MYCE_Quote__c='" + quoteID + "' AND thn__Deposit_number__c=2", ORG_USERNAME);
        System.out.println(deposits1);
        List<String> depositId2 = JsonParser2.getFieldValueSoql(deposits2.toString(), "Id");
        StringBuilder depositRecord1 = deposit.
                getDepositSFDX(SFDX, "Id='" + depositId1.get(0) + "'", ORG_USERNAME);
        System.out.println(depositRecord1);
        StringBuilder depositRecord2 = deposit.
                getDepositSFDX(SFDX, "Id='" + depositId2.get(0) + "'", ORG_USERNAME);
        String depositPersentPaid1 = JsonParser2.
                getFieldValue(depositRecord1.toString(), "thn__Paid_percentage__c");
        String depositPersentPaid2 = JsonParser2.
                getFieldValue(depositRecord2.toString(), "thn__Paid_percentage__c");
        String depositStatus1 = JsonParser2.getFieldValue(depositRecord1.toString(), "thn__Status__c");
        String depositStatus2 = JsonParser2.getFieldValue(depositRecord2.toString(), "thn__Status__c");
        String depositTotalPaid1 = JsonParser2.getFieldValue(depositRecord1.toString(), "thn__Total_paid__c");
        String depositTotalPaid2 = JsonParser2.getFieldValue(depositRecord2.toString(), "thn__Total_paid__c");
        Assert.assertEquals(depositPersentPaid1, "100");
        Assert.assertEquals(depositPersentPaid2, "33.33");
        Assert.assertEquals(depositStatus1, "Paid");
        Assert.assertEquals(depositStatus2, null);
        Assert.assertEquals(depositTotalPaid1, "1000");
        Assert.assertEquals(depositTotalPaid2, "500");
    }

    @Test(priority = 4, description = "Preconditions: Create Payments on the ‘MYCE Quote’, the ‘Gross Value' = 1500’.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update Orders")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAndDepositAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String guestID = guests.
                createGuestSFDX(SFDX, "thn__FirstName__c='John2' thn__LastName__c='Doe2'", ORG_USERNAME);
        payment.createPaymentSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Guest__c='" + guestID +
                "' thn__Gross_Value__c=2000 thn__Type__c='Cash'", ORG_USERNAME);
        StringBuilder deposits1 = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Deposit__c where" +
                        " thn__MYCE_Quote__c='" + quoteID + "' AND thn__Deposit_number__c=1", ORG_USERNAME);
        System.out.println(deposits1);
        List<String> depositId1 = JsonParser2.getFieldValueSoql(deposits1.toString(), "Id");
        StringBuilder deposits2 = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Deposit__c where" +
                        " thn__MYCE_Quote__c='" + quoteID + "' AND thn__Deposit_number__c=2", ORG_USERNAME);
        System.out.println(deposits1);
        List<String> depositId2 = JsonParser2.getFieldValueSoql(deposits2.toString(), "Id");
        StringBuilder depositRecord1 = deposit.
                getDepositSFDX(SFDX, "Id='" + depositId1.get(0) + "'", ORG_USERNAME);
        System.out.println(depositRecord1);
        StringBuilder depositRecord2 = deposit.
                getDepositSFDX(SFDX, "Id='" + depositId2.get(0) + "'", ORG_USERNAME);
        String depositPersentPaid1 = JsonParser2.
                getFieldValue(depositRecord1.toString(), "thn__Paid_percentage__c");
        String depositPersentPaid2 = JsonParser2.
                getFieldValue(depositRecord2.toString(), "thn__Paid_percentage__c");
        String depositStatus1 = JsonParser2.getFieldValue(depositRecord1.toString(), "thn__Status__c");
        String depositStatus2 = JsonParser2.getFieldValue(depositRecord2.toString(), "thn__Status__c");
        String depositTotalPaid1 = JsonParser2.getFieldValue(depositRecord1.toString(), "thn__Total_paid__c");
        String depositTotalPaid2 = JsonParser2.getFieldValue(depositRecord2.toString(), "thn__Total_paid__c");
        Assert.assertEquals(depositPersentPaid1, "100");
        Assert.assertEquals(depositPersentPaid2, "166.67");
        Assert.assertEquals(depositStatus1, "Paid");
        Assert.assertEquals(depositStatus2, "Paid");
        Assert.assertEquals(depositTotalPaid1, "1000");
        Assert.assertEquals(depositTotalPaid2, "2500");
    }

}
