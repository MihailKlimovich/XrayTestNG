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

public class QuoteBudgetMultipropertyQuote extends BaseTest{



    @Test(priority = 1, description = "Create quote with property == property 1. Add products on property 1 and" +
            " property 2. Expected result: Quote budgets created for each property")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-264: Quote budget - multiproperty quote")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        rate.deleteRateSFDX(SFDX, "Name='QuoteBudgetMultipropertyQuoteAutoTest1", ORG_USERNAME);
        rate.deleteRateSFDX(SFDX, "Name='QuoteBudgetMultipropertyQuoteAutoTest2", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteBudgetMultipropertyQuoteAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord1= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID1 = JsonParser2.getFieldValue(hotelRecord1.toString(), "Id");
        StringBuilder hotelRecord2= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo2'", ORG_USERNAME);
        String propertyID2 = JsonParser2.getFieldValue(hotelRecord2.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomDemo2Record = product.getProductSFDX(SFDX, "Name='Room'", ORG_USERNAME);
        String roomDemo2ID = JsonParser2.getFieldValue(roomDemo2Record.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='QuoteBudgetMultipropertyQuoteAutoTest' thn__Pax__c=1 thn__Hotel__c='"
                        + propertyID1 + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + "" +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 1) +
                        " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String rateId1 = rate.createRateSFDX(SFDX, "Name='QuoteBudgetMultipropertyQuoteAutoTest1' thn__IsActive__c=true" +
                " thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" + propertyID1 + "'", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId1 + "' thn__Date__c=" +
                date.generateTodayDate2() + " thn__Base_Price__c=100 thn__RelativeAdjustment__c=1" +
                " thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1 thn__ExtraUnitAdjustment__c=1" +
                " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId1 + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1 thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate__c='" + rateId1 + "' thn__Adjustment_excl_Tax__c=1 thn__Adjustment_incl_Tax__c=1" +
                " thn__RelativeValue__c=1 thn__AbsoluteValue__c=1", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" + date.generateTodayDate2()
                + " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId1 + "'" +
                " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId1 + "' thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        StringBuilder roomTypeDemo2 = roomType.getRoomTypeSFDX(SFDX, "Name='Demo2'", ORG_USERNAME);
        String roomTypeDemo2ID = JsonParser2.getFieldValue(roomTypeDemo2.toString(), "Id");
        String rateId2 = rate.createRateSFDX(SFDX, "Name='QuoteBudgetMultipropertyQuoteAutoTest2' thn__IsActive__c=true" +
                " thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" + propertyID2 + "'", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId2 + "' thn__Date__c=" +
                date.generateTodayDate2() + " thn__Base_Price__c=100 thn__RelativeAdjustment__c=1" +
                " thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1 thn__ExtraUnitAdjustment__c=1" +
                " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId2 + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1 thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeDemo2ID +
                "' thn__Rate__c='" + rateId2 + "' thn__Adjustment_excl_Tax__c=1 thn__Adjustment_incl_Tax__c=1" +
                " thn__RelativeValue__c=1 thn__AbsoluteValue__c=1", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" + date.generateTodayDate2()
                + " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId2 + "'" +
                " thn__Space_Area__c='" + roomTypeDemo2ID + "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId2 + "' thn__Space_Area__c='" + roomTypeDemo2ID +
                "'", ORG_USERNAME);
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                        + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateId1 + "' thn__Property__c='" + propertyID1 + "'", ORG_USERNAME);
        String quoteHotelRoomId2 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + roomDemo2ID + "' thn__Space_Area__c='" + roomTypeDemo2ID +
                "' thn__Rate_Plan__c='" + rateId2 + "' thn__Property__c='" + propertyID2 + "'", ORG_USERNAME);
        StringBuilder quoteBudgetProperty1Records = myceQuotes.soql(SFDX, "SELECT Id, thn__Total_Amount_incl_Tax__c" +
                " FROM thn__Quote_Budget__c WHERE thn__MYCE_Quote__c='" + quoteID + "' AND thn__Property__c='" + propertyID1 + "'", ORG_USERNAME);
        List<String> quoteBudgetID1 = JsonParser2.getFieldValueSoql(quoteBudgetProperty1Records.toString(), "Id");
        StringBuilder quoteBudgetProperty2Records = myceQuotes.soql(SFDX, "SELECT Id, thn__Total_Amount_incl_Tax__c" +
                " FROM thn__Quote_Budget__c WHERE thn__MYCE_Quote__c='" + quoteID + "' AND thn__Property__c='" + propertyID2 + "'", ORG_USERNAME);
        List<String> quoteBudgetID2 = JsonParser2.getFieldValueSoql(quoteBudgetProperty2Records.toString(), "Id");
        Assert.assertEquals(quoteBudgetID1.size(), 1);
        Assert.assertEquals(quoteBudgetID2.size(), 1);
    }

}
