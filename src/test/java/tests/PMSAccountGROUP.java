package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class PMSAccountGROUP extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote: company != null, agent == null, tage is update to" +
            " 3 - Ternative, Quote.PMS_Account_Group == null. Default agile value: PMS account stage ==" +
            " 3 - Ternative, Create PMS Account Group == true. Expected Result: PMS Group is created: type = GROUP," +
            " account = quote.company, address information = quote.company information. Quote.PMS_Account_Group =" +
            " newly created record.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-708: PMS Account GROUP")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/CreateAccountGroupTrueForDemo.apex");
        accounts.deleteAccountSFDX(SFDX, "Name='PMSAccountGROUPAuto1'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PMSAccountGROUPAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String propertyHotelCode = JsonParser2.getFieldValue(hotelRecord.toString(), "thn__HotelCode__c");
        String propertyPMSdb = JsonParser2.getFieldValue(hotelRecord.toString(), "thn__PMS_DB__c");
        StringBuilder hapiPropertyRecord = hapiProperty.getHapiPropertySFDX(SFDX, "hapi__UniqueId__c='Demo'",
                ORG_USERNAME);
        String hapiPropertyChainCode = JsonParser2.
                getFieldValue(hapiPropertyRecord.toString(), "hapi__ChainCode__c");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String accountId = accounts.createAccountSFDX(SFDX, "Name='PMSAccountGROUPAuto1' BillingCity='Brest'" +
                " BillingPostalCode='224016' BillingStreet='22 Gorky Street' Fax='+375253254514' thn__IATA__c='IATA'" +
                " Phone='+375253254511' ShippingCity='Zhabinka' ShippingPostalCode='224000'" +
                " ShippingStreet='28 Gorky Street' Sic='SIC Code' SicDesc='SIC Description' Website='brest.com'" +
                " thn__Type__c='Company'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='PMSAccountGROUPAutoTest'" +
                " thn__Pax__c=3 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3) + " RecordTypeId='"
                + recordTypeID.get(0) + "' thn__Company__c='" + accountId + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'","thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteName = JsonParser2.getFieldValue(quoteRecord.toString(), "Name");
        String quoteCompany = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Company__c");
        System.out.println(quoteRecord);
        String pmsGroupID =  JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Group__c");
        StringBuilder accountRecord = accounts.getAccountSFDX(SFDX, "Id='" + accountId + "'", ORG_USERNAME);
        String accountBillingCity = JsonParser2.
                getFieldValue(accountRecord.toString(), "BillingCity");
        String accountBillingCountryCode = JsonParser2.
                getFieldValue(accountRecord.toString(), "BillingCountryCode");
        String accountBillingPostalCode = JsonParser2.
                getFieldValue(accountRecord.toString(), "BillingPostalCode");
        String accountBillingStateCode = JsonParser2.
                getFieldValue(accountRecord.toString(), "BillingStateCode");
        String accountBillingStreet = JsonParser2.
                getFieldValue(accountRecord.toString(), "BillingStreet");
        String accountShippingCity = JsonParser2.
                getFieldValue(accountRecord.toString(), "ShippingCity");
        String accountShippingCountryCode = JsonParser2.
                getFieldValue(accountRecord.toString(), "ShippingCountryCode");
        String accountShippingPostalCode = JsonParser2.
                getFieldValue(accountRecord.toString(), "ShippingPostalCode");
        String accountShippingStateCode = JsonParser2.
                getFieldValue(accountRecord.toString(), "ShippingStateCode");
        String accountShippingStreet = JsonParser2.
                getFieldValue(accountRecord.toString(), "ShippingStreet");
        String accountIATA = JsonParser2.
                getFieldValue(accountRecord.toString(), "thn__IATA__c");
        String accountPhone = JsonParser2.
                getFieldValue(accountRecord.toString(), "Phone");
        StringBuilder pmsGroupRecord = pmsAccount.getPMSAccountSFDX(SFDX, "Id='" + pmsGroupID + "'",
                ORG_USERNAME);
        String pmsAcountName = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "Name");
        String pmsAcountPropertyDetailCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__PropertyDetailsCode__c");
        String pmsAcountPropertyDetailChainCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__PropertyDetailsChainCode__c");
        String pmsAcountPMSdb = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__PMS_DB__c");
        String pmsAcountType = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__Type__c");
        String pmsAcountAccount = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__Account__c");
        String pmsAccountBillingCity = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__BillingCity__c");
        String pmsAccountBillingCountryCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__BillingCountryCode__c");
        String pmsAccountBillingPostalCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__BillingPostalCode__c");
        String pmsAccountBillingStateCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__BillingStateCode__c");
        String pmsAccountBillingStreet = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__BillingStreet__c");
        String pmsAccountShippingCity = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__ShippingCity__c");
        String pmsAccountShippingCountryCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__ShippingCountryCode__c");
        String pmsAccountShippingPostalCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__ShippingPostalCode__c");
        String pmsAccountShippingStatelCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__ShippingStateCode__c");
        String pmsAccountShippingStreet = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__ShippingStreet__c");
        String pmsAccountIATA = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__IATANumber__c");
        String pmsAccountPhone = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__Phone__c");
        Assert.assertEquals(pmsAcountName, quoteName + " " + date.generateDate_plus5(0, 0));
        Assert.assertEquals(pmsAcountPropertyDetailCode, propertyHotelCode);
        Assert.assertEquals(pmsAcountPropertyDetailChainCode, hapiPropertyChainCode);
        Assert.assertEquals(pmsAcountPMSdb, propertyPMSdb);
        Assert.assertEquals(pmsAcountType, "GROUP");
        Assert.assertEquals(pmsAcountAccount, quoteCompany);
        Assert.assertEquals(pmsAccountBillingCity, accountBillingCity);
        Assert.assertEquals(pmsAccountBillingCountryCode, accountBillingCountryCode);
        Assert.assertEquals(pmsAccountBillingPostalCode, accountBillingPostalCode);
        Assert.assertEquals(pmsAccountBillingStateCode, accountBillingStateCode);
        Assert.assertEquals(pmsAccountBillingStreet, accountBillingStreet);
        Assert.assertEquals(pmsAccountShippingCity, accountShippingCity);
        Assert.assertEquals(pmsAccountShippingCountryCode, accountShippingCountryCode);
        Assert.assertEquals(pmsAccountShippingPostalCode, accountShippingPostalCode);
        Assert.assertEquals(pmsAccountShippingStatelCode, accountShippingStateCode);
        Assert.assertEquals(pmsAccountShippingStreet, accountShippingStreet);
        Assert.assertEquals(pmsAccountIATA, accountIATA);
        Assert.assertEquals(pmsAccountPhone, accountPhone);
    }

    @Test(priority = 2, description = "Change Quote name and or arrival date. Quote.PMS_Account_Group != null." +
            " Expected Result: No changes to the PMS_Group__c.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-708: PMS Account GROUP")
    public void case2() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PMSAccountGROUPAutoTestChange'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='PMSAccountGROUPAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteName= JsonParser2.getFieldValue(quoteRecord.toString(), "Name");
        String pmsGroupID =  JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Group__c");
        StringBuilder pmsGroupRecord2 = pmsAccount.getPMSAccountSFDX(SFDX, "Id='" + pmsGroupID + "'",
                ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("PMSAccountGROUPAutoTest");
        myceQuotes.changeDate(date.generateTodayDate3_plus(0, 3));
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "Name='PMSAccountGROUPAutoTestChange'",
                ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String updatedQuoteArrivalDate= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Arrival_Date__c");
        String updatedQuoteDepartureDate= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Departure_Date__c");
        String updatedQuoteName= JsonParser2.getFieldValue(updatedQuoteRecord.toString(), "Name");
        String updatedPMSgroup= JsonParser2.getFieldValue(updatedQuoteRecord.toString(), "thn__PMS_Group__c");
        StringBuilder pmsGroupRecord = pmsAccount.getPMSAccountSFDX(SFDX, "Id='" + updatedPMSgroup + "'",
                ORG_USERNAME);
        String pmsAcountName = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "Name");
        Assert.assertEquals(updatedQuoteName, "PMSAccountGROUPAutoTestChange");
        Assert.assertEquals(updatedQuoteArrivalDate, date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(updatedQuoteDepartureDate, date.generateTodayDate2_plus(0, 6));
        Assert.assertEquals(pmsAcountName, quoteName + " " + date.generateDate_plus5(0, 0));
        Assert.assertEquals(pmsGroupID, updatedPMSgroup);
        //Assert.assertEquals(pmsGroupRecord2, pmsGroupRecord);
    }

    @Test(priority = 3, description = "Update stage  to 2 - Propose. Quote.PMS_Account_Group != null." +
            " Expected Result: No changes to the PMS_Group__c")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-708: PMS Account GROUP")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='PMSAccountGROUPAutoTestChange'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String pmsGroupID =  JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Group__c");
        StringBuilder pmsGroupRecord2 = pmsAccount.getPMSAccountSFDX(SFDX, "Id='" + pmsGroupID + "'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteName= JsonParser2.getFieldValue(updatedQuoteRecord.toString(), "Name");
        String updatedStage= JsonParser2.getFieldValue(updatedQuoteRecord.toString(), "thn__Stage__c");
        String updatedPMSgroup= JsonParser2.getFieldValue(updatedQuoteRecord.toString(), "thn__PMS_Group__c");
        StringBuilder pmsGroupRecord = pmsAccount.getPMSAccountSFDX(SFDX, "Id='" + updatedPMSgroup + "'",
                ORG_USERNAME);
        String pmsAcountName = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "Name");
        Assert.assertEquals(updatedStage, "2 - Propose");
        Assert.assertEquals(pmsAcountName, "PMSAccountGROUPAutoTest " + date.generateDate_plus5(0, 0));
        //Assert.assertEquals(pmsGroupRecord2, pmsGroupRecord);
    }

    @Test(priority = 4, description = "Create a MYCE Quote: company != null, agent == null, tage is update to" +
            " 3 - Ternative, Quote.PMS_Account_Group == null. Default agile value: PMS account stage ==" +
            " 3 - Ternative, Create PMS Account Group == false. Expected Result: No PMS_Group__с are created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-708: PMS Account GROUP")
    public void case4() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/CreateAccountGroupFalseForDemo.apex");
        accounts.deleteAccountSFDX(SFDX, "Name='PMSAccountGROUPAuto2'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PMSAccountGROUPAutoTest2'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String accountId = accounts.createAccountSFDX(SFDX, "Name='PMSAccountGROUPAuto2' BillingCity='Brest'" +
                " BillingPostalCode='224016' BillingStreet='22 Gorky Street' Fax='+375253254514' thn__IATA__c='IATA'" +
                " Phone='+375253254511' ShippingCity='Zhabinka' ShippingPostalCode='224000'" +
                " ShippingStreet='28 Gorky Street' Sic='SIC Code' SicDesc='SIC Description' Website='brest.com'" +
                " thn__Type__c='Company'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='PMSAccountGROUPAutoTest2'" +
                " thn__Pax__c=3 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3) + " RecordTypeId='"
                + recordTypeID.get(0) + "' thn__Company__c='" + accountId + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'","thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String pmsGroupID =  JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Group__c");
        Assert.assertEquals(pmsGroupID, null);
    }

    @Test(priority = 5, description = "Create a MYCE Quote: Bill_to == company, company == null, agent !== null," +
            " tage is update to 3 - Ternative, Quote.PMS_Account_Group == null. Default agile value: PMS account" +
            " stage == 3 - Ternative, Create PMS Account Group == true. Expected Result: PMS Group is created:" +
            " type = GROUP, account = null, address information = null, Quote.PMS_Account_Group = newly created record.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-708: PMS Account GROUP")
    public void case5() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/CreateAccountGroupTrueForDemo.apex");
        accounts.deleteAccountSFDX(SFDX, "Name='PMSAccountGROUPAuto3'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PMSAccountGROUPAutoTest3'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String propertyHotelCode = JsonParser2.getFieldValue(hotelRecord.toString(), "thn__HotelCode__c");
        String propertyPMSdb = JsonParser2.getFieldValue(hotelRecord.toString(), "thn__PMS_DB__c");
        StringBuilder hapiPropertyRecord = hapiProperty.getHapiPropertySFDX(SFDX, "hapi__UniqueId__c='Demo'",
                ORG_USERNAME);
        String hapiPropertyChainCode = JsonParser2.
                getFieldValue(hapiPropertyRecord.toString(), "hapi__ChainCode__c");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String accountId = accounts.createAccountSFDX(SFDX, "Name='PMSAccountGROUPAuto3' BillingCity='Brest'" +
                " BillingPostalCode='224016' BillingStreet='22 Gorky Street' Fax='+375253254514' thn__IATA__c='IATA'" +
                " Phone='+375253254511' ShippingCity='Zhabinka' ShippingPostalCode='224000'" +
                " ShippingStreet='28 Gorky Street' Sic='SIC Code' SicDesc='SIC Description' Website='brest.com'" +
                " thn__Type__c='Agent'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='PMSAccountGROUPAutoTest3'" +
                " thn__Pax__c=3 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3) + " RecordTypeId='"
                + recordTypeID.get(0) + "' thn__Agent__c='" + accountId + "' thn__Bill_to__c='Company'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'","thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteName = JsonParser2.getFieldValue(quoteRecord.toString(), "Name");
        String pmsGroupID =  JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Group__c");
        StringBuilder pmsGroupRecord = pmsAccount.getPMSAccountSFDX(SFDX, "Id='" + pmsGroupID + "'",
                ORG_USERNAME);
        String pmsAcountName = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "Name");
        String pmsAcountPropertyDetailCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__PropertyDetailsCode__c");
        String pmsAcountPropertyDetailChainCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__PropertyDetailsChainCode__c");
        String pmsAcountPMSdb = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__PMS_DB__c");
        String pmsAcountType = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__Type__c");
        String pmsAcountAccount = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__Account__c");
        String pmsAccountBillingCity = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__BillingCity__c");
        String pmsAccountBillingCountryCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__BillingCountryCode__c");
        String pmsAccountBillingPostalCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__BillingPostalCode__c");
        String pmsAccountBillingStateCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__BillingStateCode__c");
        String pmsAccountBillingStreet = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__BillingStreet__c");
        String pmsAccountShippingCity = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__ShippingCity__c");
        String pmsAccountShippingCountryCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__ShippingCountryCode__c");
        String pmsAccountShippingPostalCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__ShippingPostalCode__c");
        String pmsAccountShippingStatelCode = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__ShippingStateCode__c");
        String pmsAccountShippingStreet = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__ShippingStreet__c");
        String pmsAccountIATA = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__IATANumber__c");
        String pmsAccountPhone = JsonParser2.
                getFieldValue(pmsGroupRecord.toString(), "thn__Phone__c");
        Assert.assertEquals(pmsAcountName, quoteName + " " + date.generateDate_plus5(0, 0));
        Assert.assertEquals(pmsAcountPropertyDetailCode, propertyHotelCode);
        Assert.assertEquals(pmsAcountPropertyDetailChainCode, hapiPropertyChainCode);
        Assert.assertEquals(pmsAcountPMSdb, propertyPMSdb);
        Assert.assertEquals(pmsAcountType, "GROUP");
        Assert.assertEquals(pmsAcountAccount, null);
        Assert.assertEquals(pmsAccountBillingCity, null);
        Assert.assertEquals(pmsAccountBillingCountryCode, null);
        Assert.assertEquals(pmsAccountBillingPostalCode, null);
        Assert.assertEquals(pmsAccountBillingStateCode, null);
        Assert.assertEquals(pmsAccountBillingStreet, null);
        Assert.assertEquals(pmsAccountShippingCity, null);
        Assert.assertEquals(pmsAccountShippingCountryCode, null);
        Assert.assertEquals(pmsAccountShippingPostalCode, null);
        Assert.assertEquals(pmsAccountShippingStatelCode, null);
        Assert.assertEquals(pmsAccountShippingStreet, null);
        Assert.assertEquals(pmsAccountIATA, null);
        Assert.assertEquals(pmsAccountPhone, null);
    }



}
