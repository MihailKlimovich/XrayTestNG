package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;
import java.util.List;

public class MoveProcessesToApex extends BaseTest {


    @Test(priority = 1, description = "Go to the Properties app. Open the Demo Property and select ‘Resort’ = Resort." +
            " Create a new MYCE Quote. For the Property make sure you select Demo. Pax = 6. Result: MYCE Quote was" +
            " created and the Resort field of the MYCE Quote was filled with Resort from Property.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-639: Move processes to apex")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MoveProcessesToApexAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        hotel.updateHotelSFDX(SFDX, "Id='" + propertyID + "'", "thn__Resort__c='Resort'", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MoveProcessesToApexAutoTest' thn__Pax__c=6" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteResort= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Resort__c");
        Assert.assertEquals(quoteResort, "Resort");
    }

    @Test(priority = 2, description = "Add a Quote Hotel Room with Pax = Quote’s Pax. Add a Quote Hotel Room with" +
            " Pax = 3. Add a Quote Product with Pax = Quote’s Pax. Add a Quote Product with Pax = 3. Add a Quote" +
            " Meeting Room with Pax = Quote’s Pax. Add a Quote Meeting Room with Pax = 3. Add a Quote Package with" +
            " Pax = Quote’s Pax. Add a Quote Package with Pax = 3. Change Pax on the Quote to 7. Result: The Pax" +
            " value of the Quote Hotel Room/Quote Product/Quote Meeting Room/Quote Package changed to 7 where it was" +
            " same value as Quote’s pax. The Pax value of the Quote Hotel Room/Quote Product/Quote Meeting Room/Quote" +
            " where Pax=3 did not change.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-639: Move processes to apex")
    public void case2() throws InterruptedException, IOException {
        packages.deletePackageSFDX(SFDX, "Name='MoveProcessesToApexAutoTest1", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='MoveProcessesToApexAutoTest2", ORG_USERNAME);
        StringBuilder roomTypeSingleRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Single'", ORG_USERNAME);
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MoveProcessesToApexAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder room2NightsRecord = product.getProductSFDX(SFDX, "Name='ROOM 2 NIGHTS'", ORG_USERNAME);
        String room2NightsID = JsonParser2.getFieldValue(room2NightsRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String winesID = JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String packageID1 = packages.createPackageSFDX(SFDX, "Name='MoveProcessesToApexAutoTest1'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID1 + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + winesID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        String packageID2 = packages.createPackageSFDX(SFDX, "Name='MoveProcessesToApexAutoTest2'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID2 + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + winesID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeSingleID +
                "'", ORG_USERNAME);
        String quoteHotelRoomId2 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room2NightsID + "' thn__Space_Area__c='" + roomTypeSingleID +
                "' thn__Pax__c=3", ORG_USERNAME);
        String quoteProductId1 = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + beverageID + "'", ORG_USERNAME);
        String quoteProductId2 = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productDinerID + "' thn__Pax__c=3", ORG_USERNAME);
        String quoteMeetingRoomId1 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'" + " thn__Product__c='" + meetingHalfDayID + "'",
                ORG_USERNAME);
        String quoteMeetingRoomId2 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'" + " thn__Product__c='" + meetingFullDayID + "'" +
                        " thn__Pax__c=3", ORG_USERNAME);
        String quotePackageId1 = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'" + " thn__Package__c='" + packageID1 + "'", ORG_USERNAME);
        String quotePackageId2 = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'" + " thn__Package__c='" + packageID2 + "' thn__Pax__c=3", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Pax__c=7", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord1 = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId1 + "'", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord2 = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId2 + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord1 = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductId1 + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord2 = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductId2 + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId1 + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId2 + "'", ORG_USERNAME);
        StringBuilder quotePackageRecord1 = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "Id='" + quotePackageId1 + "'", ORG_USERNAME);
        StringBuilder quotePackageRecord2 = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "Id='" + quotePackageId2 + "'", ORG_USERNAME);
        String quoteHotelRoomPax1 = JsonParser2.getFieldValue(quoteHotelRoomRecord1.toString(), "thn__Pax__c");
        String quoteHotelRoomPax2 = JsonParser2.getFieldValue(quoteHotelRoomRecord2.toString(), "thn__Pax__c");
        String quoteProductPax1 = JsonParser2.getFieldValue(quoteProductRecord1.toString(), "thn__Pax__c");
        String quoteProductPax2 = JsonParser2.getFieldValue(quoteProductRecord2.toString(), "thn__Pax__c");
        String quoteMeetingRoomPax1 = JsonParser2.getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Pax__c");
        String quoteMeetingRoomPax2 = JsonParser2.getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Pax__c");
        String quotePackagePax1 = JsonParser2.getFieldValue(quotePackageRecord1.toString(), "thn__Pax__c");
        String quotePackagePax2 = JsonParser2.getFieldValue(quotePackageRecord2.toString(), "thn__Pax__c");
        Assert.assertEquals(quoteHotelRoomPax1, "7");
        Assert.assertEquals(quoteHotelRoomPax2, "3");
        Assert.assertEquals(quoteProductPax1, "7");
        Assert.assertEquals(quoteProductPax2, "3");
        Assert.assertEquals(quoteMeetingRoomPax1, "7");
        Assert.assertEquals(quoteMeetingRoomPax2, "3");
        Assert.assertEquals(quotePackagePax1, "7");
        Assert.assertEquals(quotePackagePax2, "3");
    }

    @Test(priority = 3, description = "Add a Company Account to the MYCE Quote.  Add a Billing Address to the" +
            " Account. Add an Agent Account to the MYCE Quote. Add a Billing Address to the Account. Select Bill" +
            " to = Agent on the MYCE Quote. Go to the Invoices. Create a new Invoice. Specify our  MYCE Quote." +
            " Add the amount in ‘% to invoice’ field. Result: Invoice record is created. Billing Address is filled" +
            " from the Agent Account that is linked to the Quote because the current bill to on the MYCE Quote is set" +
            " to Agent.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-639: Move processes to apex")
    public void case3() throws InterruptedException, IOException {
        contact.deleteContactSFDX(SFDX, "LastName='ContactCompanyAuto'", ORG_USERNAME);
        contact.deleteContactSFDX(SFDX, "LastName='ContactAgentAuto'", ORG_USERNAME);
        accounts.deleteAccountSFDX(SFDX, "Name='TestAccountCompanyAuto", ORG_USERNAME);
        accounts.deleteAccountSFDX(SFDX, "Name='TestAccountAgentAuto", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MoveProcessesToApexAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String accountId1 = accounts.createAccountSFDX(SFDX, "Name='TestAccountCompanyAuto'" +
                " thn__Type__c='Company' BillingCountry='Netherlands' BillingStreet='Damrak'" +
                " BillingCity='Amsterdam'", ORG_USERNAME);
        String accountId2 = accounts.createAccountSFDX(SFDX, "Name='TestAccountAgentAuto'" +
                " thn__Type__c='Agent' BillingCountry='Belarus' BillingStreet='Frantsiska Skoriny'" +
                " BillingCity='Polack'", ORG_USERNAME);
        String contactId1 = contact.createContactSFDX(SFDX, "LastName='ContactCompanyAuto' AccountId='" +
                accountId1 + "'", ORG_USERNAME);
        String contactId2 = contact.createContactSFDX(SFDX, "LastName='ContactAgentAuto' AccountId='" +
                accountId2 + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Company__c='" + accountId1 +
                "' thn__Company_Contact__c='" + contactId1 + "' thn__Agent__c='" + accountId2 + "'" +
                " thn__Agent_Contact__c='" + contactId2 + "' thn__Bill_to__c='Agent'", ORG_USERNAME);
        String invoiceId =  invoice.createInvoiceSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__to_invoice__c=10 thn__Type__c='Finale'", ORG_USERNAME);
        StringBuilder invoiceRecord = invoice.getInvoiceSFDX(SFDX, "Id='" + invoiceId + "'", ORG_USERNAME);
        String invoiceBillingAddress= JsonParser2.
                getFieldValue(invoiceRecord.toString(), "thn__Billing_Address__c");
        String invoiceAccount= JsonParser2.
                getFieldValue(invoiceRecord.toString(), "thn__Account__c");
        String invoiceContact= JsonParser2.
                getFieldValue(invoiceRecord.toString(), "thn__Contact__c");
        Assert.assertEquals(invoiceBillingAddress, "Frantsiska Skoriny, Polack, Belarus");
        Assert.assertEquals(invoiceAccount, accountId2);
        Assert.assertEquals(invoiceContact, contactId2);
    }

    @Test(priority = 4, description = "Update ‘Bill to’ on MYCE Quote to ‘Company’. Update the ‘% to invoice’ on the" +
            " Invoice to trigger the invoice update. Result: The Billing Address on the Invoice was changed. The" +
            " values are taken from the Company Account. The Account and Contact on the Invoice were changed to the" +
            " Company Account and Company Contact.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-639: Move processes to apex")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MoveProcessesToApexAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder accountCompanyRecord = accounts.
                getAccountSFDX(SFDX, "Name='TestAccountCompanyAuto'", ORG_USERNAME);
        String accountCompanyID= JsonParser2.getFieldValue(accountCompanyRecord.toString(), "Id");
        StringBuilder contactCompanyRecord = contact.
                getContactSFDX(SFDX, "AccountId='" + accountCompanyID + "'", ORG_USERNAME);
        String contactCompanyID= JsonParser2.getFieldValue(contactCompanyRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Bill_to__c='Company'",
                ORG_USERNAME);
        StringBuilder invoiceRecord = invoice.getInvoiceSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String invoiceID= JsonParser2.getFieldValue(invoiceRecord.toString(), "Id");
        invoice.updateInvoiceSFDX(SFDX, "Id='" + invoiceID + "'", "thn__to_invoice__c=11", ORG_USERNAME);
        StringBuilder updatedInvoiceRecord = invoice.getInvoiceSFDX(SFDX, "Id='" + invoiceID + "'", ORG_USERNAME);
        String invoiceBillingAddress= JsonParser2.
                getFieldValue(updatedInvoiceRecord.toString(), "thn__Billing_Address__c");
        String invoiceAccount= JsonParser2.getFieldValue(updatedInvoiceRecord.toString(), "thn__Account__c");
        String invoiceContact= JsonParser2.getFieldValue(updatedInvoiceRecord.toString(), "thn__Contact__c");
        Assert.assertEquals(invoiceBillingAddress, "Damrak, Amsterdam, Netherlands");
        Assert.assertEquals(invoiceAccount, accountCompanyID);
        Assert.assertEquals(invoiceContact, contactCompanyID);
    }

    @Test(priority = 5, description = "Postconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-639: Move processes to apex")
    public void postcondition() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MoveProcessesToApexAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        hotel.updateHotelSFDX(SFDX, "Id='" + propertyID + "'", "thn__Resort__c=''", ORG_USERNAME);
    }


}
