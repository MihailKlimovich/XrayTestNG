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

public class PaymentToMews extends BaseTest {


    @Test(priority = 1, description = "Fill guest, Gross value, type, accounting category and notes, check send" +
            " to mews")
    @Severity(SeverityLevel.NORMAL)
    @Story("Payment to Mews")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PaymentToMewsAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='PaymentToMewsAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "' thn__Closed_Status__c='Won' thn__Stage__c='4 - Closed'",
                ORG_USERNAME);
        StringBuilder guestRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Guest__c where thn__Mews_Id__c!=null", ORG_USERNAME);
        System.out.println(guestRecords);
        List<String> guestsId = JsonParser2.getFieldValueSoql(guestRecords.toString(), "Id");
        StringBuilder accoutingCategory = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Accounting_Category__c where thn__Hotel__c='" + propertyID +
                        "' AND thn__Mews_Id__c!=null", ORG_USERNAME);
        System.out.println(accoutingCategory);
        guests.updateGuestSFDX(SFDX, "Id='" + guestsId.get(0) + "'",
                "thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        List<String> accoutingCategoryId = JsonParser2.getFieldValueSoql(accoutingCategory.toString(), "Id");
        String paymentId = payment.createPaymentSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Guest__c='" + guestsId.get(0) + "' thn__Gross_Value__c=500 thn__Type__c='Cash'" +
                " thn__Accounting_Category__c='" + accoutingCategoryId.get(0) + "' thn__Notes__c='"
                + (int) ( Math.random() * 300000 ) + "'", ORG_USERNAME);
        payment.updatePaymentSFDX(SFDX, "Id='" + paymentId + "'", "thn__Send_to_Mews__c=true", ORG_USERNAME);
        StringBuilder paymentRecord = payment.getPaymentSFDX(SFDX, "Id='" + paymentId + "'", ORG_USERNAME);
        String paymentSendToMews = JsonParser2.getFieldValue(paymentRecord.toString(), "thn__Send_to_Mews__c");
        String paymentSyncStatus = JsonParser2.getFieldValue(paymentRecord.toString(), "thn__Sync_Status__c");
        String paymentMewsID = JsonParser2.getFieldValue(paymentRecord.toString(), "thn__Mews_Id__c");
        Assert.assertEquals(paymentSendToMews, "false");
        Assert.assertEquals(paymentSyncStatus, "Sent");
        Assert.assertNotNull(paymentMewsID);
    }



}
