package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class PackageLineRatePlan extends BaseTest{

    @Test(priority = 1, description = "Create a Package with a Quote Hotel Room as a Package Line. Specify a Rate" +
            " on Quote Package Line. Create a MYCE Quote. Instantiate the created package to MYCE Quote. Expected" +
            " Result: The value in Rate field on the Quote Package Line is taken from the value of Rate on Package" +
            " Line. Rate Plan is also assigned to Quote Hotel Room which is part of the Quote Package.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-683: Package line rate plan")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PackageLineRatePlanAutoTest", ORG_USERNAME);
        rate.deleteRateSFDX(SFDX, "Name='RatePackageLineAutoTest", ORG_USERNAME);
        rate.deleteRateSFDX(SFDX, "Name='RatePackageLineAutoTest2", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='PackageRatePlanAutoTest", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String rateId = rate.createRateSFDX(SFDX, "Name='RatePackageLineAutoTest'" +
                        " thn__IsActive__c=true thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Base_Price__c=100 thn__RelativeAdjustment__c=1" +
                " thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1 thn__ExtraUnitAdjustment__c=1" +
                " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate__c='" + rateId + "' thn__Adjustment_excl_Tax__c=1 thn__Adjustment_incl_Tax__c=2" +
                " thn__RelativeValue__c=1 thn__AbsoluteValue__c=1", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId + "' thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        String rateId2 = rate.createRateSFDX(SFDX, "Name='RatePackageLineAutoTest2'" +
                " thn__IsActive__c=true thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        ratePrice.createRatePriceSFDX(SFDX, "thn__Rate__c='" + rateId2 + "' thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Base_Price__c=100 thn__RelativeAdjustment__c=1" +
                " thn__AbsoluteAdjustment__c=1 thn__EmptyUnitAdjustment__c=1 thn__ExtraUnitAdjustment__c=1" +
                " thn__Price_excl_Tax__c=90 thn__Price_incl_Tax__c=110", ORG_USERNAME);
        categoryAdjustment.createCategoryAdjustmentSFDX(SFDX, "thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate__c='" + rateId2 + "' thn__Adjustment_excl_Tax__c=1 thn__Adjustment_incl_Tax__c=2" +
                " thn__RelativeValue__c=1 thn__AbsoluteValue__c=1", ORG_USERNAME);
        categoryPrice.createCategoryPriceSFDX(SFDX, "thn__Price__c=100 thn__Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Price_excl_Tax__c=90" +
                " thn__Price_incl_Tax__c=110 thn__Rate__c='" + rateId2 + "' thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='PackageRatePlanAutoTest' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Hotel Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Rate__c='" + rateId + "' thn__Unit_Price__c=100 thn__VAT_Category__c=0",
                ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='PackageLineRatePlanAutoTest'" +
                        " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        StringBuilder quotePackageLineRecord = quotePackageLine.
                getQuotePackageLineSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String qplRate = JsonParser2.getFieldValue(quotePackageLineRecord.toString(), "thn__Rate__c");
        StringBuilder qhrRecord = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String qprRatePlan = JsonParser2.getFieldValue(qhrRecord.toString(), "thn__Rate_Plan__c");
        Assert.assertEquals(qplRate, rateId);
        Assert.assertEquals(qprRatePlan, rateId);
    }

    @Test(priority = 2, description = "Change the Rate Plan on the Quote Hotel Room. Go to the Quote Package Line" +
            " and trigger an update: Change the Name, Start time or VAT category. Expected Result: Rate on Quote" +
            " Hotel Room that is part of the package did not change..")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-683: Package line rate plan")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='PackageLineRatePlanAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder rateRecord2 = rate.getRateSFDX(SFDX, "Name='RatePackageLineAutoTest2",
                ORG_USERNAME);
        String rateID2= JsonParser2.getFieldValue(rateRecord2.toString(), "Id");
        StringBuilder quotePackageLineRecord = quotePackageLine.
                getQuotePackageLineSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String qplEndTime = JsonParser2.getFieldValue(quotePackageLineRecord.toString(), "thn__End_Time__c");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Rate_Plan__c='" + rateID2 + "'", ORG_USERNAME);
        quotePackageLine.updateQuotePackageLineSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__End_Time__c=22:15", ORG_USERNAME);
        StringBuilder updatedQuotePackageLineRecord = quotePackageLine.
                getQuotePackageLineSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder qhrRecord = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String updateQplEndTime = JsonParser2.getFieldValue(updatedQuotePackageLineRecord.toString(), "thn__End_Time__c");
        String qprRatePlan = JsonParser2.getFieldValue(qhrRecord.toString(), "thn__Rate_Plan__c");
        Assert.assertNotEquals(updateQplEndTime, qplEndTime);
        Assert.assertEquals(qprRatePlan, rateID2);
    }

}
