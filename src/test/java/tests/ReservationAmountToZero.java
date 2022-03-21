package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class ReservationAmountToZero extends BaseTest {

    @Test(priority = 1, description = "")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-695: Reservation amount to 0 - Test")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ReservationAmountToZeroAutoTest'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DoNotPriceReservationsTrue.apex");
        rate.deleteRateSFDX(SFDX, "Name='RateReservationAmountToZeroAuto", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Space_Area__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null", ORG_USERNAME);
        List<String> roomTypeID = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ReservationAmountToZeroAutoTest'" +
                " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        String rateId = rate.createRateSFDX(SFDX, "Name='RateReservationAmountToZeroAuto' thn__IsActive__c=true" +
                " thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2() + " thn__Base_Price__c=100 thn__RelativeAdjustment__c=1" +
                " thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1 thn__ExtraUnitAdjustment__c=1" +
                " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1 thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1 thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 3) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1 thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 4) + " thn__Base_Price__c=100" +
                " thn__RelativeAdjustment__c=1 thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1" +
                " thn__ExtraUnitAdjustment__c=1 thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeID.get(0) +
                "' thn__Rate__c='" + rateId + "' thn__Adjustment_excl_Tax__c=1 thn__Adjustment_incl_Tax__c=1" +
                " thn__RelativeValue__c=1 thn__AbsoluteValue__c=1", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" + date.generateTodayDate2()
                + " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "'" +
                " thn__Space_Area__c='" + roomTypeID.get(0) + "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 3) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "'", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 4) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) + "'" +
                " thn__Rate_Plan__c='" + rateId + "'", ORG_USERNAME);

    }

}
