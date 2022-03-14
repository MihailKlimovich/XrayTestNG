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

public class QuoteDateRecordsNotGenerated extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote: Arrival Date = today, Departure Date = today + 4 days," +
            " Pax = 1. Instantiate a Quote Product. Change the Stage of the MYCE Quote to ‘2 - Propose’. Five" +
            " FS_Dates records were created for each day for the duration of the MYCE Quote. Change the Departure" +
            " Date of the MYCE Quote. Departure Date = today + 7 days 3 More FS_Dates records were created." +
            " Instantiate another Quote Product. Expected Result: The corresponding FS record was linked to the" +
            " Instantiated Quote product.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-283: Quote - 'Date' records not generated")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteDateRecordsNotGeneratedAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String winesID = JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteDateRecordsNotGeneratedAutoTest'" +
                " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                productDinerID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'","thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        StringBuilder fsDateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__FS_Date__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> fsDateID = JsonParser2.getFieldValueSoql(fsDateRecords.toString(), "Id");
        Assert.assertEquals(fsDateID.size(), 5);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'","thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 7), ORG_USERNAME);
        StringBuilder fsDateRecords2 = myceQuotes.soql(SFDX, "SELECT Id FROM thn__FS_Date__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> fsDateID2 = JsonParser2.getFieldValueSoql(fsDateRecords2.toString(), "Id");
        Assert.assertEquals(fsDateID2.size(), 8);
        String quoteProductId = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + winesID + "' thn__Start_Date_Time__c=" +
                date.generateTodayDate2_plus(0, 7) + "T11:00:00.000+0000 thn__End_Date_Time__c=" +
                date.generateTodayDate2_plus(0, 7) + "T12:00:00.000+0000", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductId + "'", ORG_USERNAME);
        String quoteProductFSDate = JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__FS_Date__c");
        StringBuilder fsDateRecord = myceQuotes.soql(SFDX, "SELECT Id FROM thn__FS_Date__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "' AND thn__Date__c=" +
                date.generateTodayDate2_plus(0 , 7), ORG_USERNAME);
        List<String> fsDateID3 = JsonParser2.getFieldValueSoql(fsDateRecord.toString(), "Id");
        Assert.assertEquals(quoteProductFSDate, fsDateID3.get(0));
    }

}
