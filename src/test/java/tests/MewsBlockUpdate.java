package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class MewsBlockUpdate extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote. Arrival Date = today + 2 days. Departure Date = today + 6 days." +
            " Release Date = today + 1 day. Pax = 5. Instantiate a Quote Hotel Room . Set checkbox ‘Create" +
            " PMS Block’ = True on MYCE Quote. Change the stage of the Quote to ‘3 - Tentative’. Expected result:" +
            " PMS block is created. MAdjustment records are created on PMS Block.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-666: Mews Block update")
    public void precondition() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MewsBlockUpdateAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        System.out.println(guests);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='MewsBlockUpdateAutoTest' thn__Pax__c=5 thn__Hotel__c='"
                        + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 6) + "" +
                        " RecordTypeId='" + recordTypeID.get(0) + "' thn__Release_Date__c=" +
                        date.generateTodayDate2_plus(0, 1), ORG_USERNAME);
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true AND" +
                " Name='Bar'", ORG_USERNAME);
        List<String> rateId = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) +
                "' thn__Rate_Plan__c='" + rateId.get(0) + "' thn__Property__c='" + propertyID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        String pmsResponce = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Response__c");
        String pmsStatus = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Status__c");
        StringBuilder mAdjustmentReocords = myceQuotes.soql(SFDX, "SELECT Id, thn__PMS_Start_UTC__c," +
                " thn__PMS_End_UTC__c, thn__PMS_Quantity__c FROM thn__MAdjustment__c WHERE thn__PMS_Block__c='" +
                pmsBlockID + "'", ORG_USERNAME);
        List<String> mAdjustmentStartUTC = JsonParser2.
                getFieldValueSoql(mAdjustmentReocords.toString(), "thn__PMS_Start_UTC__c");
        List<String> mAdjustmentEndUTC = JsonParser2.
                getFieldValueSoql(mAdjustmentReocords.toString(), "thn__PMS_End_UTC__c");
        /*List<String> mAdjustmentPMSResponce = JsonParser2.
               getFieldValueSoql(mAdjustmentReocords.toString(), "thn__PMS_Response__c");*/
        Assert.assertEquals(pmsResponce, "200 OK");
        Assert.assertEquals(pmsStatus, "Send");
        Assert.assertEquals(mAdjustmentStartUTC.get(0),
                date.generateTodayDate2_plus(0, 2) + "T12:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(0),
                date.generateTodayDate2_plus(0, 3) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(1),
                date.generateTodayDate2_plus(0, 3) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(1),
                date.generateTodayDate2_plus(0, 4) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(2),
                date.generateTodayDate2_plus(0, 4) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(2),
                date.generateTodayDate2_plus(0, 5) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(3),
                date.generateTodayDate2_plus(0, 5) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(3),
                date.generateTodayDate2_plus(0, 6) + "T14:30:00.000+0000");
        /*Assert.assertEquals(mAdjustmentPMSResponce.get(0), "200 OK");
        Assert.assertEquals(mAdjustmentPMSResponce.get(1), "200 OK");
        Assert.assertEquals(mAdjustmentPMSResponce.get(2), "200 OK");
        Assert.assertEquals(mAdjustmentPMSResponce.get(3), "200 OK");*/
    }

    @Test(priority = 2, description = "Delete the Quote Hotel Room. Expected result: The related MAdjustments of" +
            " the PMS Block were deleted. The PMS Status on the PMS block changed to ‘Delete’. PMS Status = Delete.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-666: Mews Block update")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='MewsBlockUpdateAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteHotelRoom.deleteQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        StringBuilder mAdjustmentRecord = mAdjustments.getMAdjustmentSFDX(SFDX, "thn__PMS_Block__c='"
                + pmsBlockID + "'", ORG_USERNAME);
        System.out.println(mAdjustmentRecord);
        String message = JsonParser2.getFieldValue2(mAdjustmentRecord.toString(), "message");
        System.out.println(mAdjustmentRecord);
        String pmsStatus = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Status__c");
        Assert.assertEquals(message, "No matching record found");
        Assert.assertEquals(pmsStatus, "Delete");
    }

    @Test(priority = 3, description = "Instantiate a Quote Hotel Room. Change the Stage of the MYCE Quote to" +
            " '1 - Qualify. Set checkbox ‘Create PMS Block’ = True on MYCE Quote. Change the stage of the Quote to" +
            " ‘3 - Tentative’. Change the stage of the Quote to ‘4 - Closed' Closed Status ‘Lost’." +
            "  Expected result: PMS block is created. MAdjustment record is created on PMS Block. PMS Status on" +
            " the PMS Block was changed to Delete.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-666: Mews Block update")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='MewsBlockUpdateAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        pmsBlock.deletePMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=false",
                ORG_USERNAME);
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true AND" +
                " Name='Bar'", ORG_USERNAME);
        List<String> rateId = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        System.out.println(guests);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) +
                "' thn__Rate_Plan__c='" + rateId.get(0) + "' thn__Property__c='" + propertyID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'","thn__Stage__c='1 - Qualify'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        String pmsResponce = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Response__c");
        String pmsStatus = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Status__c");
        StringBuilder mAdjustmentReocords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__MAdjustment__c WHERE" +
                " thn__PMS_Block__c='" + pmsBlockID + "'", ORG_USERNAME);
        List<String> mAdjustmentId = JsonParser2.
                getFieldValueSoql(mAdjustmentReocords.toString(), "Id");
        Assert.assertEquals(mAdjustmentId.size(), 4);
        Assert.assertEquals(pmsResponce, "200 OK");
        Assert.assertEquals(pmsStatus, "Send");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Lost'", ORG_USERNAME);
        StringBuilder updatedPMSblockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "'", ORG_USERNAME);
        String updatedPMSstatus = JsonParser2.getFieldValue(updatedPMSblockRecord.toString(), "thn__PMS_Status__c");
        Assert.assertEquals(updatedPMSstatus, "Delete");
    }

    @Test(priority = 4, description = "Create MYCE Quote. Arrival Date = today + 2 days. Departure Date = today + 6 days." +
            " Release Date = today + 1 day. Pax = 5. Instantiate a Quote Hotel Room . Set checkbox ‘Create" +
            " PMS Block’ = True on MYCE Quote. Change the stage of the Quote to ‘3 - Tentative’. Update the Shoulder" +
            " End Date on the MYCE Quote. Expected result: PMS block is created. MAdjustment records is created" +
            " on PMS Block. PMS Block Name on the PMS Block was changed to Updated + Name.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-666: Mews Block update")
    public void case3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MewsBlockUpdateAutoTest2'", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        System.out.println(guests);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='MewsBlockUpdateAutoTest2' thn__Pax__c=5 thn__Hotel__c='"
                        + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2_plus(0, 2) +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 6) + "" +
                        " RecordTypeId='" + recordTypeID.get(0) + "' thn__Release_Date__c=" +
                        date.generateTodayDate2_plus(0, 1), ORG_USERNAME);
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true AND" +
                " Name='Bar'", ORG_USERNAME);
        List<String> rateId = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) +
                "' thn__Rate_Plan__c='" + rateId.get(0) + "' thn__Property__c='" + propertyID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'",
                "thn__Shoulder_End_Date__c=" + date.generateTodayDate2_plus(0, 11), ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        String pmsResponce = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Response__c");
        String pmsBlockName = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Name");
        StringBuilder mAdjustmentReocords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__MAdjustment__c WHERE" +
                " thn__PMS_Block__c='" + pmsBlockID + "'", ORG_USERNAME);
        List<String> mAdjustmentId = JsonParser2.
                getFieldValueSoql(mAdjustmentReocords.toString(), "Id");
        Assert.assertEquals(mAdjustmentId.size(), 8);
        Assert.assertEquals(pmsResponce, "200 OK");
        Assert.assertEquals(pmsBlockName, "Updated MewsBlockUpdateAutoTest2");
    }

}
