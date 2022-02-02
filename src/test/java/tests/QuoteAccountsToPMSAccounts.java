package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteAccountsToPMSAccounts extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-659: Quote accounts to PMS accounts")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Create MYCE Quote, Add the created Account to the Company field on the Quote," +
            " Change the stage of the Quote from ‘1 - Propose’ to ‘3 - Tentative’. Expected result: A PMS Company" +
            " record was created and linked to the Quote. The fields that we filled on the Account record correspond" +
            " to the fields on the PMS Company record. Checkbox 'Is Primary' = True. Property Details Chain Code =" +
            " CMT Chain code (value was taken from CMT). Property Details Code = Property Code (Value taken from" +
            " Property)")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-659: Quote accounts to PMS accounts")
    public void case1() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest'", ORG_USERNAME);
        accounts.deleteAccountSFDX(SFDX, "Name=PMSCompanyAutoTest", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String propertyHotelCode = JsonParser2.getFieldValue(hotelRecord.toString(), "thn__HotelCode__c");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest' thn__Pax__c=3" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String accountId = accounts.createAccountSFDX(SFDX, "Name='PMSCompanyAutoTest' BillingCity='Brest'" +
                " BillingPostalCode='224016' BillingStreet='22 Gorky Street' Fax='+375253254514' thn__IATA__c='IATA'" +
                " Phone='+375253254511' ShippingCity='Zhabinka' ShippingPostalCode='224000'" +
                " ShippingStreet='28 Gorky Street' Sic='SIC Code' SicDesc='SIC Description' Website='brest.com'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Company__c='" + accountId + "'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        StringBuilder accountRecord = accounts.getAccountSFDX(SFDX, "Id='" + accountId + "'", ORG_USERNAME);
        String quotePMSCompany = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Company__c");
        StringBuilder pmsAccountRecord = pmsAccount.
                getPMSAccountSFDX(SFDX, "Id='" + quotePMSCompany + "'", ORG_USERNAME);
        String accountName = JsonParser2.getFieldValue(accountRecord.toString(), "Name");
        String pmsAccountName = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "Name");
        String accountBillingCity = JsonParser2.getFieldValue(accountRecord.toString(), "BillingCity");
        String pmsAccountBillingCity = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__BillingCity__c");
        String accountBillingPostalCode = JsonParser2.getFieldValue(accountRecord.toString(), "BillingPostalCode");
        String pmsAccountBillingPostalCode = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__BillingPostalCode__c");
        String accountBillingStreet = JsonParser2.getFieldValue(accountRecord.toString(), "BillingStreet");
        String pmsAccountBillingStreet = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__BillingStreet__c");
        String accountFax = JsonParser2.getFieldValue(accountRecord.toString(), "Fax");
        String pmsAccountFax = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__Fax__c");
        String accountIATA = JsonParser2.getFieldValue(accountRecord.toString(), "thn__IATA__c");
        String pmsAccountIATA = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__IATANumber__c");
        String accountPhone = JsonParser2.getFieldValue(accountRecord.toString(), "Phone");
        String pmsAccountPhone = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__Phone__c");
        String accountShippingCity = JsonParser2.getFieldValue(accountRecord.toString(), "ShippingCity");
        String pmsAccountShippingCity = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__ShippingCity__c");
        String accountShippingPostalCode = JsonParser2.
                getFieldValue(accountRecord.toString(), "ShippingPostalCode");
        String pmsAccountShippingPostalCode = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__ShippingPostalCode__c");
        String accountShippingStreet = JsonParser2.getFieldValue(accountRecord.toString(), "ShippingStreet");
        String pmsAccountShippingStreet = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__ShippingStreet__c");
        String accountSic = JsonParser2.getFieldValue(accountRecord.toString(), "Sic");
        String pmsAccountSic = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__SicCode__c");
        String accountSicDesc = JsonParser2.getFieldValue(accountRecord.toString(), "SicDesc");
        String pmsAccountSicDesc = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__SicDescription__c");
        String accountWebsite = JsonParser2.getFieldValue(accountRecord.toString(), "Website");
        String pmsAccountWebsite = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__Website__c");
        String pmsAccountIsPrimary = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__Is_Primary__c");
        String pmsAccountPropertyDetailChainCode = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__PropertyDetailsChainCode__c");
        String pmsAccountPropertyDetailCode = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__PropertyDetailsCode__c");
        Assert.assertEquals(pmsAccountName, accountName);
        Assert.assertEquals(pmsAccountBillingCity, accountBillingCity);
        Assert.assertEquals(pmsAccountBillingPostalCode, accountBillingPostalCode);
        Assert.assertEquals(pmsAccountBillingStreet, accountBillingStreet);
        Assert.assertEquals(pmsAccountFax, accountFax);
        Assert.assertEquals(pmsAccountIATA, accountIATA);
        Assert.assertEquals(pmsAccountPhone, accountPhone);
        Assert.assertEquals(pmsAccountShippingCity, accountShippingCity);
        Assert.assertEquals(pmsAccountShippingPostalCode, accountShippingPostalCode);
        Assert.assertEquals(pmsAccountShippingStreet, accountShippingStreet);
        Assert.assertEquals(pmsAccountSic, accountSic);
        Assert.assertEquals(pmsAccountSicDesc, accountSicDesc);
        Assert.assertEquals(pmsAccountWebsite, accountWebsite);
        Assert.assertEquals(pmsAccountIsPrimary, "true");
        Assert.assertEquals(pmsAccountPropertyDetailChainCode, "DEMO_CHAIN");
        Assert.assertEquals(pmsAccountPropertyDetailCode, propertyHotelCode);
    }

    @Test(priority = 3, description = "Remove the linked PMS Company record from the Quote. Change the stage of the" +
            " Quote from ‘3 - Tentative’  to ‘1 - Qualify’. Expected result: The earlier created PMS Company record" +
            " was linked to the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-659: Quote accounts to PMS accounts")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest'", ORG_USERNAME);
        String quotePMSCompany= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Company__c");
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest",
                "thn__PMS_Company__c=''", ORG_USERNAME);
        StringBuilder quoteRecord2 = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest'", ORG_USERNAME);
        String quotePMSCompany2= JsonParser2.getFieldValue(quoteRecord2.toString(), "thn__PMS_Company__c");
        Assert.assertEquals(quotePMSCompany2, null);
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest",
                "thn__Stage__c='1 - Qualify'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest'", ORG_USERNAME);
        String updatedQuotePMSCompany= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__PMS_Company__c");
        Assert.assertEquals(updatedQuotePMSCompany, quotePMSCompany);
    }

    @Test(priority = 4, description = "Remove the linked PMS Company record from the Quote. Add a different PMS" +
            " Company record to the Quote. Change the stage of the Quote from ‘1 - Propose’ to ‘3 - Tentative’." +
            " Expected result: The earlier created PMS Company record was linked to the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-659: Quote accounts to PMS accounts")
    public void case3() throws InterruptedException, IOException {
        pmsAccount.deletePMSAccountSFDX(SFDX, "Name='AutoPMSAccount'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest'", ORG_USERNAME);
        String quotePMSCompany= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Company__c");
        String newPMSAccountID = pmsAccount.createPMSAccountSFDX(SFDX, "Name='AutoPMSAccount'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest",
                "thn__PMS_Company__c='" + newPMSAccountID + "'", ORG_USERNAME);
        StringBuilder quoteRecord2 = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest'", ORG_USERNAME);
        String quotePMSCompany2= JsonParser2.getFieldValue(quoteRecord2.toString(), "thn__PMS_Company__c");
        Assert.assertEquals(quotePMSCompany2, newPMSAccountID);
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest",
                "thn__Stage__c='3 - Tentative'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest'", ORG_USERNAME);
        String updatedQuotePMSCompany= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__PMS_Company__c");
        Assert.assertEquals(updatedQuotePMSCompany, quotePMSCompany);
    }

    @Test(priority = 5, description = "Remove the linked PMS Company and Company record from the Quote. Create an" +
            " Account record with the Name of 110 characters. Change the stage of the Quote from ‘3 - Tentative’  to" +
            " ‘1 - Qualify’. Expected result: The Name of the created PMS Company was shortened to 80 characters. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-659: Quote accounts to PMS accounts")
    public void case4() throws InterruptedException, IOException {
        accounts.deleteAccountSFDX(SFDX, "Name=OneHundreedOneHundreedOneHundreedOneHundreedOneHundreedOne" +
                "HundreedOneHundreedOneHundreedOneHundreedOneHundreed", ORG_USERNAME);
        String accountId = accounts.createAccountSFDX(SFDX, "Name='OneHundreedOneHundreedOneHundreedOneHundreed" +
                "OneHundreedOneHundreedOneHundreedOneHundreedOneHundreedOneHundreed'", ORG_USERNAME);
        StringBuilder accountRecord = accounts.getAccountSFDX(SFDX, "Id='" + accountId + "'", ORG_USERNAME);
        String accountName= JsonParser2.
                getFieldValue(accountRecord.toString(), "Name");
        Assert.assertEquals(accountName.length(), 110);
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest",
                "thn__Company__c='" + accountId + "' thn__PMS_Company__c=''", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest",
                "thn__Stage__c='1 - Qualify'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest'", ORG_USERNAME);
        String quotePMSCompany= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__PMS_Company__c");
        StringBuilder pmsAccountRecord = pmsAccount.
                getPMSAccountSFDX(SFDX, "Id='" + quotePMSCompany + "'", ORG_USERNAME);
        String pmsAccountName = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "Name");
        Assert.assertEquals(pmsAccountName.length(), 80);
    }

    @Test(priority = 6, description = "Create MYCE Quote, Add the created Account to the Agent field on the Quote," +
            " Change the stage of the Quote from ‘1 - Propose’ to ‘3 - Tentative’. Expected result: A PMS Company" +
            " record was created and linked to the Quote. The fields that we filled on the Account record correspond" +
            " to the fields on the PMS Company record. Checkbox 'Is Primary' = True. Property Details Chain Code =" +
            " CMT Chain code (value was taken from CMT). Property Details Code = Property Code (Value taken from" +
            " Property)")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-659: Quote accounts to PMS accounts")
    public void case5() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2'", ORG_USERNAME);
        accounts.deleteAccountSFDX(SFDX, "Name=PMSCompanyAutoTest2", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String propertyHotelCode = JsonParser2.getFieldValue(hotelRecord.toString(), "thn__HotelCode__c");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2' thn__Pax__c=3" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String accountId = accounts.createAccountSFDX(SFDX, "Name='PMSCompanyAutoTest2' BillingCity='Polotsk'" +
                        " BillingPostalCode='211400' BillingStreet='15 Frantsiska Skoriny' Fax='+375253254789' thn__IATA__c='IATA'" +
                        " Phone='+375253254987' ShippingCity='Farinovo' ShippingPostalCode='211654'" +
                        " ShippingStreet='16 Frantsiska Skoriny' Sic='SIC Code' SicDesc='SIC Description'" +
                        " Website='polotsk.com' thn__Type__c='Agent'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Agent__c='" + accountId + "'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        StringBuilder accountRecord = accounts.getAccountSFDX(SFDX, "Id='" + accountId + "'", ORG_USERNAME);
        String quotePMSAgent = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Travel_Agent__c");
        StringBuilder pmsAccountRecord = pmsAccount.
                getPMSAccountSFDX(SFDX, "Id='" + quotePMSAgent + "'", ORG_USERNAME);
        String accountName = JsonParser2.getFieldValue(accountRecord.toString(), "Name");
        String pmsAccountName = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "Name");
        String accountBillingCity = JsonParser2.getFieldValue(accountRecord.toString(), "BillingCity");
        String pmsAccountBillingCity = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__BillingCity__c");
        String accountBillingPostalCode = JsonParser2.getFieldValue(accountRecord.toString(), "BillingPostalCode");
        String pmsAccountBillingPostalCode = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__BillingPostalCode__c");
        String accountBillingStreet = JsonParser2.getFieldValue(accountRecord.toString(), "BillingStreet");
        String pmsAccountBillingStreet = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__BillingStreet__c");
        String accountFax = JsonParser2.getFieldValue(accountRecord.toString(), "Fax");
        String pmsAccountFax = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__Fax__c");
        String accountIATA = JsonParser2.getFieldValue(accountRecord.toString(), "thn__IATA__c");
        String pmsAccountIATA = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__IATANumber__c");
        String accountPhone = JsonParser2.getFieldValue(accountRecord.toString(), "Phone");
        String pmsAccountPhone = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__Phone__c");
        String accountShippingCity = JsonParser2.getFieldValue(accountRecord.toString(), "ShippingCity");
        String pmsAccountShippingCity = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__ShippingCity__c");
        String accountShippingPostalCode = JsonParser2.
                getFieldValue(accountRecord.toString(), "ShippingPostalCode");
        String pmsAccountShippingPostalCode = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__ShippingPostalCode__c");
        String accountShippingStreet = JsonParser2.getFieldValue(accountRecord.toString(), "ShippingStreet");
        String pmsAccountShippingStreet = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__ShippingStreet__c");
        String accountSic = JsonParser2.getFieldValue(accountRecord.toString(), "Sic");
        String pmsAccountSic = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__SicCode__c");
        String accountSicDesc = JsonParser2.getFieldValue(accountRecord.toString(), "SicDesc");
        String pmsAccountSicDesc = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__SicDescription__c");
        String accountWebsite = JsonParser2.getFieldValue(accountRecord.toString(), "Website");
        String pmsAccountWebsite = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__Website__c");
        String pmsAccountIsPrimary = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__Is_Primary__c");
        String pmsAccountPropertyDetailChainCode = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__PropertyDetailsChainCode__c");
        String pmsAccountPropertyDetailCode = JsonParser2.
                getFieldValue(pmsAccountRecord.toString(), "thn__PropertyDetailsCode__c");
        Assert.assertEquals(pmsAccountName, accountName);
        Assert.assertEquals(pmsAccountBillingCity, accountBillingCity);
        Assert.assertEquals(pmsAccountBillingPostalCode, accountBillingPostalCode);
        Assert.assertEquals(pmsAccountBillingStreet, accountBillingStreet);
        Assert.assertEquals(pmsAccountFax, accountFax);
        Assert.assertEquals(pmsAccountIATA, accountIATA);
        Assert.assertEquals(pmsAccountPhone, accountPhone);
        Assert.assertEquals(pmsAccountShippingCity, accountShippingCity);
        Assert.assertEquals(pmsAccountShippingPostalCode, accountShippingPostalCode);
        Assert.assertEquals(pmsAccountShippingStreet, accountShippingStreet);
        Assert.assertEquals(pmsAccountSic, accountSic);
        Assert.assertEquals(pmsAccountSicDesc, accountSicDesc);
        Assert.assertEquals(pmsAccountWebsite, accountWebsite);
        Assert.assertEquals(pmsAccountIsPrimary, "true");
        Assert.assertEquals(pmsAccountPropertyDetailChainCode, "DEMO_CHAIN");
        Assert.assertEquals(pmsAccountPropertyDetailCode, propertyHotelCode);
    }

    @Test(priority = 7, description = "Remove the linked PMS Travel Agent record from the Quote. Change the stage of the" +
            " Quote from ‘3 - Tentative’  to ‘1 - Qualify’. Expected result: The earlier created PMS Company record" +
            " was linked to the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-659: Quote accounts to PMS accounts")
    public void case6() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2'", ORG_USERNAME);
        String quotePMSAgent= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Travel_Agent__c");
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2",
                "thn__PMS_Travel_Agent__c=''", ORG_USERNAME);
        StringBuilder quoteRecord2 = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2'", ORG_USERNAME);
        String quotePMSAgent2= JsonParser2.getFieldValue(quoteRecord2.toString(), "thn__PMS_Travel_Agent__c");
        Assert.assertEquals(quotePMSAgent2, null);
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2",
                "thn__Stage__c='1 - Qualify'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2'", ORG_USERNAME);
        String updatedQuotePMSAgent= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__PMS_Travel_Agent__c");
        Assert.assertEquals(updatedQuotePMSAgent, quotePMSAgent);
    }

    @Test(priority = 8, description = "Remove the linked PMS Travel Agent record from the Quote. Add a different PMS" +
            " Travel Agent record to the Quote. Change the stage of the Quote from ‘1 - Propose’ to ‘3 - Tentative’." +
            " Expected result: The earlier created PMS Travel Agent record was linked to the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-659: Quote accounts to PMS accounts")
    public void case7() throws InterruptedException, IOException {
        pmsAccount.deletePMSAccountSFDX(SFDX, "Name='AutoPMSAccount2'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2'", ORG_USERNAME);
        String quotePMSAgent= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__PMS_Travel_Agent__c");
        String newPMSAccountID = pmsAccount.createPMSAccountSFDX(SFDX, "Name='AutoPMSAccount2'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2",
                "thn__PMS_Travel_Agent__c='" + newPMSAccountID + "'", ORG_USERNAME);
        StringBuilder quoteRecord2 = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2'", ORG_USERNAME);
        String quotePMSAgent2= JsonParser2.getFieldValue(quoteRecord2.toString(), "thn__PMS_Travel_Agent__c");
        Assert.assertEquals(quotePMSAgent2, newPMSAccountID);
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2",
                "thn__Stage__c='3 - Tentative'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2'", ORG_USERNAME);
        String updatedQuotePMSAgent= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__PMS_Travel_Agent__c");
        Assert.assertEquals(updatedQuotePMSAgent, quotePMSAgent);
    }

    @Test(priority = 9, description = "Remove the linked PMS Travel Agent and Agent record from the Quote. Create an" +
            " Account record with the Name of 110 characters. Change the stage of the Quote from ‘3 - Tentative’  to" +
            " ‘1 - Qualify’. Expected result: The Name of the created PMS Travel Agent was shortened to 80 characters.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-659: Quote accounts to PMS accounts")
    public void case8() throws InterruptedException, IOException {
        accounts.deleteAccountSFDX(SFDX, "Name=OneHundreedOneHundreedOneHundreedOneHundreedOneHundreedOne" +
                "HundreedOneHundreedOneHundreedOneHundreedOneHundreed", ORG_USERNAME);
        String accountId = accounts.createAccountSFDX(SFDX, "Name='OneHundreedOneHundreedOneHundreedOneHundreed" +
                "OneHundreedOneHundreedOneHundreedOneHundreedOneHundreedOneHundreed' thn__Type__c='Agent'", ORG_USERNAME);
        StringBuilder accountRecord = accounts.getAccountSFDX(SFDX, "Id='" + accountId + "'", ORG_USERNAME);
        String accountName= JsonParser2.
                getFieldValue(accountRecord.toString(), "Name");
        Assert.assertEquals(accountName.length(), 110);
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2",
                "thn__Agent__c='" + accountId + "' thn__PMS_Travel_Agent__c=''", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2",
                "thn__Stage__c='1 - Qualify'", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteAccountToPMSAccountsAutoTest2'", ORG_USERNAME);
        String quotePMSAgent= JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__PMS_Travel_Agent__c");
        StringBuilder pmsAccountRecord = pmsAccount.
                getPMSAccountSFDX(SFDX, "Id='" + quotePMSAgent + "'", ORG_USERNAME);
        String pmsAccountName = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "Name");
        Assert.assertEquals(pmsAccountName.length(), 80);
    }

}
