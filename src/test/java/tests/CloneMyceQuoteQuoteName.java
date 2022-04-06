package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class CloneMyceQuoteQuoteName extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote. On MYCE Quote record press Clone MYCE Quote button." +
            " In opened modal leave the Name field empty. Press Save button. Expected result: A new MYCE Quote" +
            " record is created. The Name of the Quote is converted arrival date.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-541: Clone Myce quote. Quote Name.")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneMyceQuoteQuoteNameAutoTest'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='" + date.generateTodayDate2_plus(0, 5) + "'",
                ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='CloneMyceQuoteQuoteNameAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("CloneMyceQuoteQuoteNameAutoTest");
        String quoteName = myceQuotes.cloneMyceQuote_readName("", date.generateTodayDate3_plus(0, 5),
                "5");
        Assert.assertEquals(quoteName, date.generateTodayDate2_plus(0, 5));
    }

    @Test(priority = 2, description = "On MYCE Quote record press Clone MYCE Quote button. In opened modal fill Name" +
            " field. Press Save button. Expected result: A new MYCE Quote record is created. The name of the Quote" +
            " is the value entered in the Name field in the Clone MYCE Quote modal.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-541: Clone Myce quote. Quote Name.")
    public void case2() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Clone555'",
                ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='CloneMyceQuoteQuoteNameAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("CloneMyceQuoteQuoteNameAutoTest");
        String quoteName = myceQuotes.cloneMyceQuote_readName("Clone555", date.generateTodayDate3_plus(0, 5),
                "5");
        Assert.assertEquals(quoteName, "Clone555");
    }

}
