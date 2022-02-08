package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class CreateQuoteMeetingRoomFromQuoteProduct extends BaseTest {


    @Test(priority = 1, description = "Preconditions: Create a new Myce quote, create QuoteProduct.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-621: Create quote meeting room from quote product")
    public void preconditions() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CreateQuoteMeetingRoomFromQuoteProduct'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='CreateQuoteMeetingRoomFromQuoteProduct'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                beverageID + "'", ORG_USERNAME);
    }

    @Test(priority = 2, description = "Open the ‘Quote Product’ that we added earlier to the Quote. Click the" +
            " ‘Create Resource’ button that we added earlier. Fill in all the fields. Result: A ‘Meeting Room’ was" +
            " created and assigned to the ‘Quote Product’. The Related tab of the ‘Quote Meeting Room’ has the" +
            " related Quote products.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-621: Create quote meeting room from quote product")
    public void case1() throws InterruptedException, IOException {
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder resourceRecrd = resource.getResourceSFDX(SFDX, "Name='DEFAULT' thn__Hotel__c='"
                + propertyID +  "'", ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecrd.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='CreateQuoteMeetingRoomFromQuoteProduct'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CreateQuoteMeetingRoomFromQuoteProduct");
        myceQuotes.openProducts();
        quoteProducts.openRecord_byName("BEVERAGE");
        quoteProducts.clickCreateREsourceButton();
        createResourceForm.createResource("DEFAULT", "DEFAULT", "Custom", "3",
                "151", "Description 123", "English", "France", "Des FS");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(quoteMeetingRoomRecord);
        String quoteProductMeetingRoom= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Service_Area__c");
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "Id");
        String quoteMeetingRoomResource = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        String quoteMeetingRoomFunction = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Function__c");
        String quoteMeetingRoomSetup = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Setup__c");
        String quoteMeetingRoomPax = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Pax__c");
        String quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomDescription = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__DescriptionLong__c");
        String quoteMeetingRoomDescriptionLanquage2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Description_2__c");
        String quoteMeetingRoomDescriptionLanquage3 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Description_Language_3__c");
        String quoteMeetingRoomDescriptionFS = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Description_FS__c");
        Assert.assertEquals(quoteProductMeetingRoom, quoteMeetingRoomId);
        Assert.assertEquals(quoteMeetingRoomResource, resourceId);
        Assert.assertEquals(quoteMeetingRoomSetup, "Custom");
        Assert.assertEquals(quoteMeetingRoomPax, "3");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "151");
        Assert.assertEquals(quoteMeetingRoomDescription, "Description 123");
        Assert.assertEquals(quoteMeetingRoomDescriptionLanquage2, "English");
        Assert.assertEquals(quoteMeetingRoomDescriptionLanquage3, "France");
        Assert.assertEquals(quoteMeetingRoomDescriptionFS, "Des FS");
    }

}
