package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class PMSBlockAndMadjustmentsMewsInterface extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote. Add a Hotel Room to the Myce Quote. Go to" +
            "Custom Settings > Block Settings > Manage. Set the ‘Block PMS’ to ‘True’. Go to the MYCE Quote," +
            "Instantiate a Quote Hotel Room and set the ‘Create PMS Block’ to ‘True’. Result: A PMS Block was" +
            "created. The ‘PMS response’ field of the PMS Block is Null. The ‘PMS Status’ of the Block was set to" +
            "‘New’. The ‘PMS Response’ field on the related MAdjustment record is also Null.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-632: PMS block and Madjustments - Mews Interface")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/MewsDefaultValues.apex");
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BlockSettings.apex");
        rate.deleteRateSFDX(SFDX, "Name='PMSBlockAndMadjustmentsRateAutoTest", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PMSBlockAndMadjustmentsAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord1= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID1 = JsonParser2.getFieldValue(hotelRecord1.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='PMSBlockAndMadjustmentsAutoTest' thn__Pax__c=1 thn__Hotel__c='"
                        + propertyID1 + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + "" +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 1) +
                        " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String rateId1 = rate.createRateSFDX(SFDX, "Name='PMSBlockAndMadjustmentsRateAutoTest'" +
                " thn__IsActive__c=true thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" +
                propertyID1 + "'", ORG_USERNAME);
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
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateId1 + "' thn__Property__c='" + propertyID1 + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true" +
                " thn__Stage__c='3 - Tentative'", ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        String pmsResponce = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Response__c");
        String pmsStatus = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Status__c");
        StringBuilder mAdjustmentRecord = mAdjustments.getMAdjustmentSFDX(SFDX, "thn__PMS_Block__c='"
                + pmsBlockID + "'", ORG_USERNAME);
        String mAdjustmentPMSResponce = JsonParser2.
                getFieldValue(mAdjustmentRecord.toString(), "thn__PMS_Response__c");
        Assert.assertEquals(pmsResponce, null);
        Assert.assertEquals(pmsStatus, "New");
        Assert.assertEquals(mAdjustmentPMSResponce, null);
    }

    @Test(priority = 2, description = "Create a MYCE Quote. Add a Hotel Room to the Myce Quote. Go to Custom" +
            " Settings > Block Settings > Manage. Set the ‘Block PMS’ to ‘True’. Go to the MYCE Quote, Instantiate" +
            " a Quote Hotel Room and set the ‘Create PMS Block’ to ‘True’. Result: A PMS Block was created. The" +
            " ‘PMS response’ field of the PMS Block is ‘200 OK’. The ‘PMS Status’ of the Block was set to ‘Send’." +
            " The ‘PMS Response’ field on the related MAdjustment record is also ‘200 OK’.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-632: PMS block and Madjustments - Mews Interface")
    public void case2() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BlockSettings2.apex");
        rate.deleteRateSFDX(SFDX, "Name='PMSBlockAndMadjustmentsRateAutoTest2", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PMSBlockAndMadjustmentsAutoTest2'", ORG_USERNAME);
        StringBuilder hotelRecord1= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID1 = JsonParser2.getFieldValue(hotelRecord1.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='PMSBlockAndMadjustmentsAutoTest2' thn__Pax__c=1 thn__Hotel__c='"
                        + propertyID1 + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + "" +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 1) +
                        " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String rateId1 = rate.createRateSFDX(SFDX, "Name='PMSBlockAndMadjustmentsRateAutoTest2'" +
                " thn__IsActive__c=true thn__IsPublic__c=true thn__getPrices__c=true thn__Hotel__c='" +
                propertyID1 + "'", ORG_USERNAME);
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
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Rate_Plan__c='" + rateId1 + "' thn__Property__c='" + propertyID1 + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true" +
                " thn__Stage__c='3 - Tentative'", ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        String pmsResponce = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Response__c");
        String pmsStatus = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Status__c");
        StringBuilder mAdjustmentRecord = mAdjustments.getMAdjustmentSFDX(SFDX, "thn__PMS_Block__c='"
                + pmsBlockID + "'", ORG_USERNAME);
        String mAdjustmentPMSResponce = JsonParser2.
                getFieldValue(mAdjustmentRecord.toString(), "thn__PMS_Response__c");
        Assert.assertEquals(pmsResponce, "200 OK");
        Assert.assertEquals(pmsStatus, "Send");
        Assert.assertEquals(mAdjustmentPMSResponce, "200 OK");
    }

}
