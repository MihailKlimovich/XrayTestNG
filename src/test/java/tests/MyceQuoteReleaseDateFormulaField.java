package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class MyceQuoteReleaseDateFormulaField extends BaseTest{


    @Test(priority = 1, description = "Create MYCE Quote record. Result: Release date formula field value == Quote" +
            " arrival date - 1.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-542: Myce Quote - release date formula field")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ReleaseDateAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ReleaseDateAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 10) +
                " RecordTypeId='" + recordTypeID.get(0) + "' thn__Stage__c='4 - Closed' thn__Closed_Status__c='Won'",
                ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        System.out.println(quoteRecord);
        String releaseDateFormula = JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Release_date_formula__c");
        Assert.assertEquals(releaseDateFormula, date.generateTodayDate2_minus(0, 1));
    }

    @Test(priority = 2, description = "Change Quote’s dates using flow. Result: Release date formula field" +
            " value is updated and is equal to the new Quote arrival date - 1.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-542: Myce Quote - release date formula field")
    public void case2() throws InterruptedException, IOException {
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("ReleaseDateAutoTest");
        myceQuotes.editArrivalDate(date.generateDate_plus(0, 5));
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='ReleaseDateAutoTest'", ORG_USERNAME);
        System.out.println(quoteRecord);
        String releaseDateFormula = JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Release_date_formula__c");
        Assert.assertEquals(releaseDateFormula, date.generateTodayDate2_plus(0, 4));

    }

    @Test(priority = 3, description = "Update MYCE Quote’s arrival date manually. Result: Release date formula field" +
            " value is updated and is equal to the new Quote arrival date - 1.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-542: Myce Quote - release date formula field")
    public void case3() throws InterruptedException, IOException {
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("ReleaseDateAutoTest");
        myceQuotes.changeDate(date.generateTodayDate3_plus(0, 3));
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='ReleaseDateAutoTest'", ORG_USERNAME);
        System.out.println(quoteRecord);
        String releaseDateFormula = JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Release_date_formula__c");
        Assert.assertEquals(releaseDateFormula, date.generateTodayDate2_plus(0, 2));
    }

    @Test(priority = 4, description = "Clone MYCE Quote. Result: Release date formula field value on the new Quote" +
            " == Quote arrival date - 1.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-542: Myce Quote - release date formula field")
    public void case4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneReleaseDateAutoTest'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("ReleaseDateAutoTest");
        myceQuotes.cloneMyceQuote("CloneReleaseDateAutoTest", date.generateTodayDate3_plus(0, 7), "5");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneReleaseDateAutoTest'",
                ORG_USERNAME);
        System.out.println(quoteRecord);
        String releaseDateFormula = JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Release_date_formula__c");
        Assert.assertEquals(releaseDateFormula, date.generateTodayDate2_plus(0, 6));
    }

}
