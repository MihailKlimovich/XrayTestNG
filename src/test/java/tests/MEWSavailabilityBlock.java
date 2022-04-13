package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class MEWSavailabilityBlock extends BaseTest {

    @Test(priority = 1, description = "Set MewsDefaultValues__c.Create_PMS_Block__c to TRUE. Create MYCE Quote → Fill" +
            " Shoulder start and end date fields (or Specify value for ‘Quote shoulder start' and 'Quote shoulder" +
            " end’ fields in “Default Agile Value” mtd, then on quote save Shoulder fields will be filled" +
            " automatically) → Fill Release date. Add Quote hotel rooms with different rates (Room type == Queen," +
            " Rates: First test rate, Second Rate). Set Create PMS Block to TRUE. Expected result: For each rate," +
            " TH PMS Block is created: When sending to MEWS, thn__StartShoulderDate__c = MyceQuote.ShoulderStartDate," +
            " thn__EndShoulderDate__c = MyceQuote.ShoulderEndDate.  TH PMS Block status == “Send”. TH Quote Hotel" +
            " Rooms are linked to created TH PMS Blocks. Blocks are sent to MEWS: PMS Response field is filled," +
            " PMS Response Date/Time is filled.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-460: MEWS - Availability Block")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest'", ORG_USERNAME);
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
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true", ORG_USERNAME);
        List<String> rateID = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Rate_Plan__c='" + rateID.get(0) + "'", ORG_USERNAME);
        String quoteHotelRoomId2 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Rate_Plan__c='" + rateID.get(1) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true",
                ORG_USERNAME);
        StringBuilder myceQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteShoulderStartDate = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Shoulder_Start_Date__c");
        String quoteShoulderEndDate = JsonParser2.
                getFieldValue(myceQuoteRecord.toString(), "thn__Shoulder_End_Date__c");
        StringBuilder pmsBlockRecord1 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Rate__c='" + rateID.get(0) + "'", ORG_USERNAME);
        StringBuilder pmsBlockRecord2 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Rate__c='" + rateID.get(1) + "'", ORG_USERNAME);
        String pmsBlockResponce1 = JsonParser2.getFieldValue(pmsBlockRecord1.toString(), "thn__PMS_Response__c");
        String pmsBlockResponce2 = JsonParser2.getFieldValue(pmsBlockRecord2.toString(), "thn__PMS_Response__c");
        String pmsBlockPMSstatus1 = JsonParser2.getFieldValue(pmsBlockRecord1.toString(), "thn__PMS_Status__c");
        String pmsBlockPMSstatus2 = JsonParser2.getFieldValue(pmsBlockRecord2.toString(), "thn__PMS_Status__c");
        String pmsShoulderStartDate1 = JsonParser2.
                getFieldValue(pmsBlockRecord1.toString(), "thn__StartShoulderDate__c");
        String pmsShoulderStartDate2 = JsonParser2.
                getFieldValue(pmsBlockRecord2.toString(), "thn__StartShoulderDate__c");
        String pmsShoulderEndDate1 = JsonParser2.
                getFieldValue(pmsBlockRecord1.toString(), "thn__EndShoulderDate__c");
        String pmsShoulderEndDate2 = JsonParser2.
                getFieldValue(pmsBlockRecord2.toString(), "thn__EndShoulderDate__c");
        Assert.assertEquals(pmsShoulderStartDate1, quoteShoulderStartDate);
        Assert.assertEquals(pmsShoulderStartDate2, quoteShoulderStartDate);
        Assert.assertEquals(pmsShoulderEndDate1, quoteShoulderEndDate);
        Assert.assertEquals(pmsShoulderEndDate2, quoteShoulderEndDate);
        Assert.assertEquals(pmsBlockResponce1, "200 OK");
        Assert.assertEquals(pmsBlockResponce2, "200 OK");
        Assert.assertEquals(pmsBlockPMSstatus1, "Send");
        Assert.assertEquals(pmsBlockPMSstatus2, "Send");
    }

    @Test(priority = 2, description = "Create Quote → Add Quote Hotel rooms → Change  Mews State on MYCE Quote so it" +
            " is equal to Create_Adjustment_Status in Default Agile Value mdt → Set Create PMS Block to true." +
            " Expected result: PMS Block and MAdjustment records are created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-460: MEWS - Availability Block")
    public void case2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest2'", ORG_USERNAME);
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
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true", ORG_USERNAME);
        List<String> rateID = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest2' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 3) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Rate_Plan__c='" + rateID.get(0) + "'", ORG_USERNAME);
        String quoteHotelRoomId2 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Rate_Plan__c='" + rateID.get(1) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true",
                ORG_USERNAME);
        StringBuilder pmsBlockRecord1 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Rate__c='" + rateID.get(0) + "'", ORG_USERNAME);
        String pmsBlockID1 = JsonParser2.getFieldValue(pmsBlockRecord1.toString(), "Id");
        StringBuilder pmsBlockRecord2 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Rate__c='" + rateID.get(1) + "'", ORG_USERNAME);
        String pmsBlockID2 = JsonParser2.getFieldValue(pmsBlockRecord2.toString(), "Id");
        StringBuilder mAdjustmentRecord1 = mAdjustments.getMAdjustmentSFDX(SFDX, "thn__PMS_Block__c='" +
                pmsBlockID1 + "'", ORG_USERNAME);
        StringBuilder mAdjustmentRecord2= mAdjustments.getMAdjustmentSFDX(SFDX, "thn__PMS_Block__c='" +
                pmsBlockID2 + "'", ORG_USERNAME);
        StringBuilder qhrRecord1 = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId1 + "'",
                ORG_USERNAME);
        StringBuilder qhrRecord2 = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId2 + "'",
                ORG_USERNAME);
        String qhrArrivalDateTime1 = JsonParser2.getFieldValue(qhrRecord1.toString(), "thn__Arrival_Date_Time__c");
        String qhrArrivalDateTime2 = JsonParser2.getFieldValue(qhrRecord2.toString(), "thn__Arrival_Date_Time__c");
        String qhrDeparturelDateTime1 = JsonParser2.
                getFieldValue(qhrRecord1.toString(), "thn__Departure_Date_Time__c");
        String qhrDeparturelDateTime2 = JsonParser2.
                getFieldValue(qhrRecord2.toString(), "thn__Departure_Date_Time__c");
        String qhrPax1 = JsonParser2.getFieldValue(qhrRecord1.toString(), "thn__Pax__c");
        String qhrPax2 = JsonParser2.getFieldValue(qhrRecord2.toString(), "thn__Pax__c");
        String mAdjustmentQHR1 = JsonParser2.
                getFieldValue(mAdjustmentRecord1.toString(), "thn__Quote_Hotel_Room__c");
        String mAdjustmentQHR2 = JsonParser2.
                getFieldValue(mAdjustmentRecord2.toString(), "thn__Quote_Hotel_Room__c");
        String mAdjustmentStartUTC1 = JsonParser2.
                getFieldValue(mAdjustmentRecord1.toString(), "thn__PMS_Start_UTC__c");
        String mAdjustmentStartUTC2 = JsonParser2.
                getFieldValue(mAdjustmentRecord2.toString(), "thn__PMS_Start_UTC__c");
        String mAdjustmentEndUTC1 = JsonParser2.
                getFieldValue(mAdjustmentRecord1.toString(), "thn__PMS_End_UTC__c");
        String mAdjustmentEndUTC2 = JsonParser2.
                getFieldValue(mAdjustmentRecord2.toString(), "thn__PMS_End_UTC__c");
        String mAdjustmentPMSResponce1 = JsonParser2.
                getFieldValue(mAdjustmentRecord1.toString(), "thn__PMS_Response__c");
        String mAdjustmentPMSResponce2 = JsonParser2.
                getFieldValue(mAdjustmentRecord2.toString(), "thn__PMS_Response__c");
        String mAdjustmentPMSStatus1 = JsonParser2.
                getFieldValue(mAdjustmentRecord1.toString(), "thn__PMS_Status__c");
        String mAdjustmentPMSStatus2 = JsonParser2.
                getFieldValue(mAdjustmentRecord2.toString(), "thn__PMS_Status__c");
        String mAdjustmentPMSQuantity1 = JsonParser2.
                getFieldValue(mAdjustmentRecord1.toString(), "thn__PMS_Quantity__c");
        String mAdjustmentPMSQuantity2 = JsonParser2.
                getFieldValue(mAdjustmentRecord2.toString(), "thn__PMS_Quantity__c");
        Assert.assertEquals(mAdjustmentQHR1, quoteHotelRoomId1);
        Assert.assertEquals(mAdjustmentQHR2, quoteHotelRoomId2);
        Assert.assertEquals(mAdjustmentStartUTC1, qhrArrivalDateTime1);
        Assert.assertEquals(mAdjustmentStartUTC2, qhrArrivalDateTime2);
        Assert.assertEquals(mAdjustmentEndUTC1, qhrDeparturelDateTime1);
        Assert.assertEquals(mAdjustmentEndUTC2, qhrDeparturelDateTime2);
        Assert.assertEquals(mAdjustmentPMSQuantity1, qhrPax1);
        Assert.assertEquals(mAdjustmentPMSQuantity2, qhrPax2);
        Assert.assertEquals(mAdjustmentPMSResponce1, "200 OK");
        Assert.assertEquals(mAdjustmentPMSResponce2, "200 OK");
        Assert.assertEquals(mAdjustmentPMSStatus1, "Send");
        Assert.assertEquals(mAdjustmentPMSStatus2, "Send");
    }

    @Test(priority = 3, description = "Create Quote → Add Quote Hotel rooms → Create Reservations → Make sure" +
            " Status != Canceled && Mews id != null →  Create PMS Blocks → Change State on MYCE Quote so it is" +
            " equal to Create_Adjustment_Status in Default Agile Value mdt. Expected result: MAdjustment records are" +
            " not created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-460: MEWS - Availability Block")
    public void case3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest3'", ORG_USERNAME);
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
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true", ORG_USERNAME);
        List<String> rateID = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest3' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 3) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Rate_Plan__c='" + rateID.get(0) + "'", ORG_USERNAME);
        String quoteHotelRoomId2 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Rate_Plan__c='" + rateID.get(1) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        Thread.sleep(5000);
        StringBuilder reservationRecord1 = reservations.
                getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId1 + "'", ORG_USERNAME);
        StringBuilder reservationRecord2 = reservations.
                getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c='" + quoteHotelRoomId2 + "'", ORG_USERNAME);
        String qhrMewsID1 = JsonParser2.getFieldValue(reservationRecord1.toString(), "thn__Mews_Id__c");
        String qhrMewsID2 = JsonParser2.getFieldValue(reservationRecord2.toString(), "thn__Mews_Id__c");
        Assert.assertNotNull(qhrMewsID1);
        Assert.assertNotNull(qhrMewsID2);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true",
                ORG_USERNAME);
        StringBuilder pmsBlockRecord1 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Rate__c='" + rateID.get(0) + "'", ORG_USERNAME);
        String pmsBlockID1 = JsonParser2.getFieldValue(pmsBlockRecord1.toString(), "Id");
        StringBuilder pmsBlockRecord2 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Rate__c='" + rateID.get(1) + "'", ORG_USERNAME);
        String pmsBlockID2 = JsonParser2.getFieldValue(pmsBlockRecord2.toString(), "Id");
        StringBuilder mAdjustmentRecord1 = mAdjustments.getMAdjustmentSFDX(SFDX, "thn__PMS_Block__c='" +
                pmsBlockID1 + "'", ORG_USERNAME);
        StringBuilder mAdjustmentRecord2= mAdjustments.getMAdjustmentSFDX(SFDX, "thn__PMS_Block__c='" +
                pmsBlockID2 + "'", ORG_USERNAME);
        String message1 = JsonParser2.getFieldValue2(mAdjustmentRecord1.toString(), "message");
        String message2 = JsonParser2.getFieldValue2(mAdjustmentRecord2.toString(), "message");
        Assert.assertEquals(message1, "No matching record found");
        Assert.assertEquals(message2, "No matching record found");
    }

    @Test(priority = 4, description = "Create Quote → Add Quote Hotel rooms →  Change  State on MYCE Quote so it is" +
            " equal to Create_Adjustment_Status in Default Agile Value mdt → Try to create reservations. Expected" +
            " result: Reservations are not created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-460: MEWS - Availability Block")
    public void case4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest4'", ORG_USERNAME);
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
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true", ORG_USERNAME);
        List<String> rateID = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest4' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 3) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        StringBuilder reservationRecords = reservations.
                getReservationSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String message = JsonParser2.getFieldValue2(reservationRecords.toString(), "message");
        Assert.assertEquals(message, "No matching record found");
    }

    @Test(priority = 5, description = "Create Quote → Add Quote Hotel rooms → Change  Mews State on MYCE Quote so it" +
            " is equal to Create_Adjustment_Status in Default Agile Value mdt → Set Create PMS Block to true." +
            " PMS Block and MAdjustment records are created. Update Quote hotel room: Update Arrival/Departure" +
            " date time. Expected result: MAdjustment record: plus new quantity, new start, new end is created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-460: MEWS - Availability Block")
    public void case5() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest5'", ORG_USERNAME);
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
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true", ORG_USERNAME);
        List<String> rateID = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest5' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 5) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Rate_Plan__c='" + rateID.get(0) + "'" +
                " thn__Arrival_Date_Time__c=" + date.generateTodayDate2_plus(0, 2) +
                "T11:00:00.000+0000 thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 4) +
                "T13:30:00.000+0000", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true",
                ORG_USERNAME);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId1 + "'",
                "thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 5) +
                        "T13:30:00.000+0000", ORG_USERNAME);
        StringBuilder pmsBlockRecord1 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Rate__c='" + rateID.get(0) + "'", ORG_USERNAME);
        String pmsBlockID1 = JsonParser2.getFieldValue(pmsBlockRecord1.toString(), "Id");
        StringBuilder mAdustmentRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__PMS_Quantity__c," +
                " thn__PMS_Start_UTC__c, thn__PMS_End_UTC__c FROM thn__MAdjustment__c WHERE thn__PMS_Block__c='" +
                pmsBlockID1 + "'", ORG_USERNAME);
        List<String> mAdjustmentID = JsonParser2.getFieldValueSoql(mAdustmentRecords.toString(), "Id");
        List<Integer> mAdjustmentQuantity = JsonParser2.
                getFieldValueSoql2(mAdustmentRecords.toString(), "thn__PMS_Quantity__c");
        List<String> mAdjustmentStartUTC = JsonParser2.
                getFieldValueSoql(mAdustmentRecords.toString(), "thn__PMS_Start_UTC__c");
        List<String> mAdjustmentEndUTC = JsonParser2.
                getFieldValueSoql(mAdustmentRecords.toString(), "thn__PMS_End_UTC__c");
        Assert.assertEquals(mAdjustmentID.size(), 7);
        Assert.assertEquals(mAdjustmentQuantity.get(0).intValue(), 5);
        Assert.assertEquals(mAdjustmentQuantity.get(1).intValue(), 5);
        Assert.assertEquals(mAdjustmentQuantity.get(2).intValue(), -5);
        Assert.assertEquals(mAdjustmentQuantity.get(3).intValue(), -5);
        Assert.assertEquals(mAdjustmentQuantity.get(4).intValue(), 5);
        Assert.assertEquals(mAdjustmentQuantity.get(5).intValue(), 5);
        Assert.assertEquals(mAdjustmentQuantity.get(6).intValue(), 5);
        Assert.assertEquals(mAdjustmentStartUTC.get(0),
                date.generateTodayDate2_plus(0, 2) + "T11:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(0),
                date.generateTodayDate2_plus(0, 3) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(1),
                date.generateTodayDate2_plus(0, 3) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(1),
                date.generateTodayDate2_plus(0, 4) + "T13:30:00.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(2),
                date.generateTodayDate2_plus(0, 2) + "T11:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(2),
                date.generateTodayDate2_plus(0, 3) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(3),
                date.generateTodayDate2_plus(0, 3) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(3),
                date.generateTodayDate2_plus(0, 4) + "T13:30:00.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(4),
                date.generateTodayDate2_plus(0, 2) + "T11:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(4),
                date.generateTodayDate2_plus(0, 3) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(5),
                date.generateTodayDate2_plus(0, 3) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(5),
                date.generateTodayDate2_plus(0, 4) + "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(6),
                date.generateTodayDate2_plus(0, 4) + "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndUTC.get(6),
                date.generateTodayDate2_plus(0, 5) + "T13:30:00.000+0000");

    }

    @Test(priority = 6, description = "Update MYCE Quote Shoulder Start Date / Shoulder End Date, new dates are" +
            " within the block interval. Expected result: TH PMS Block is not updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-460: MEWS - Availability Block")
    public void case6() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest6'", ORG_USERNAME);
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
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true", ORG_USERNAME);
        List<String> rateID = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest6' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 6) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomId1 = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Rate_Plan__c='" + rateID.get(0) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Shoulder_Start_Date__c="
                + date.generateTodayDate2_plus(0, 3) + " thn__Shoulder_End_Date__c="
                + date.generateTodayDate2_plus(0, 5), ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteShoulderStartDate = JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Shoulder_Start_Date__c");
        String quoteShoulderEndDate = JsonParser2.
                getFieldValue(updatedQuoteRecord.toString(), "thn__Shoulder_End_Date__c");
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Rate__c='" + rateID.get(0) + "'", ORG_USERNAME);
        String pmsBlockResponce = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Response__c");
        String pmsBlockPMSstatus = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__PMS_Status__c");
        String pmsShoulderStartDate = JsonParser2.
                getFieldValue(pmsBlockRecord.toString(), "thn__StartShoulderDate__c");
        String pmsShoulderEndDate = JsonParser2.
                getFieldValue(pmsBlockRecord.toString(), "thn__EndShoulderDate__c");
        Assert.assertEquals(quoteShoulderStartDate, date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(quoteShoulderEndDate, date.generateTodayDate2_plus(0, 5));
        Assert.assertEquals(pmsShoulderStartDate, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(pmsShoulderEndDate, date.generateTodayDate2_plus(0, 8));
        Assert.assertEquals(pmsBlockResponce, "200 OK");
        Assert.assertEquals(pmsBlockPMSstatus, "Send");
    }

    @Test(priority = 7, description = "Update MYCE Quote Shoulder Start Date / Shoulder End Date, new dates are not" +
            " within the block interval. Expected result: On existing Blocks  PMS Block status is set to “Delete”," +
            " Response and Response date/time is filled, External Id is cleared, Delete Block request is sent to" +
            " MEWS. New TH PMS Blocks are created and are sent to MEWS.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-460: MEWS - Availability Block")
    public void case7() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest6'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Shoulder_Start_Date__c="
                + date.generateTodayDate2_plus(0, 3) + " thn__Shoulder_End_Date__c="
                + date.generateTodayDate2_plus(0, 9), ORG_USERNAME);
        StringBuilder pmsBlockRecord1 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__PMS_Status__c='Send'", ORG_USERNAME);
        StringBuilder pmsBlockRecord2 = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__PMS_Status__c='Delete'", ORG_USERNAME);
        String pmsBlockResponce1 = JsonParser2.getFieldValue(pmsBlockRecord1.toString(), "thn__PMS_Response__c");
        String pmsBlockResponce2 = JsonParser2.getFieldValue(pmsBlockRecord2.toString(), "thn__PMS_Response__c");
        String pmsBlockPMSstatus1 = JsonParser2.getFieldValue(pmsBlockRecord1.toString(), "thn__PMS_Status__c");
        String pmsBlockPMSstatus2 = JsonParser2.getFieldValue(pmsBlockRecord2.toString(), "thn__PMS_Status__c");
        String pmsBlockPMSid1 = JsonParser2.getFieldValue(pmsBlockRecord1.toString(), "thn__PMSId__c");
        String pmsBlockPMSid2 = JsonParser2.getFieldValue(pmsBlockRecord2.toString(), "thn__PMSId__c");
        String pmsShoulderStartDate1 = JsonParser2.
                getFieldValue(pmsBlockRecord1.toString(), "thn__StartShoulderDate__c");
        String pmsShoulderStartDate2 = JsonParser2.
                getFieldValue(pmsBlockRecord2.toString(), "thn__StartShoulderDate__c");
        String pmsShoulderEndDate1 = JsonParser2.
                getFieldValue(pmsBlockRecord1.toString(), "thn__EndShoulderDate__c");
        String pmsShoulderEndDate2 = JsonParser2.
                getFieldValue(pmsBlockRecord2.toString(), "thn__EndShoulderDate__c");
        Assert.assertEquals(pmsBlockResponce1, "200 OK");
        Assert.assertEquals(pmsBlockResponce2, "200 OK");
        Assert.assertEquals(pmsBlockPMSstatus1, "Send");
        Assert.assertEquals(pmsBlockPMSstatus2, "Delete");
        Assert.assertNotNull(pmsBlockPMSid1);
        Assert.assertNull(pmsBlockPMSid2);
        Assert.assertEquals(pmsShoulderStartDate1, date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(pmsShoulderEndDate1, date.generateTodayDate2_plus(0, 9));
        Assert.assertEquals(pmsShoulderStartDate2, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(pmsShoulderEndDate2, date.generateTodayDate2_plus(0, 8));
    }

    @Test(priority = 8, description = "On Quote that has Quote hotel rooms, set Send To Mews to true, make sure" +
            " Create PMS Block == true in MewsDefaultValues  custom setting and PMS Block is created. Expected" +
            " result: PMS Block status is changed to status defined in Agile Mews Stage Correspondence mdt," +
            " Mews Status on Quote hotel rooms is updated according to Agile Mews Stage Correspondence mdt," +
            " Reservations are created and are linked to TH PMS Block. If reservation request to Mews is successful," +
            " MAdjustment record is created with Quantity = -number of reservations, Start and End dates are copied" +
            " from Reservation")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-460: MEWS - Availability Block")
    public void case8() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest7'", ORG_USERNAME);
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
        StringBuilder rateRecords = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rate__c WHERE" +
                " thn__Hotel__c='" + propertyID + "' AND thn__Mews_Id__c!=null AND thn__IsPublic__c=true", ORG_USERNAME);
        List<String> rateID = JsonParser2.getFieldValueSoql(rateRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest7' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 6) + " RecordTypeId='" + recordTypeID.get(0) +
                "'", ORG_USERNAME);
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeID.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Rate_Plan__c='" + rateID.get(0) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='3 - Tentative'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Create_PMS_Block__c=true",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        Thread.sleep(5000);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='"
                + quoteHotelRoomId +"'", ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c="
                + quoteHotelRoomId + "'", ORG_USERNAME);
        String qhrMewsState = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Mews_State__c");
        String reservationState = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__State__c");
        String reservationArrival = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__StartUtc__c");
        String reservationDeparture = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__EndUtc__c");
        String pmsBlockStatus = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__Status__c");
        String reservationPMSBlock = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__PMS_Block__c");
        StringBuilder mAdjustmentRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__PMS_Quantity__c," +
                " thn__PMS_Start_UTC__c, thn__PMS_End_UTC__c FROM thn__MAdjustment__c WHERE thn__PMS_Block__c='"
                + pmsBlockID + "'", ORG_USERNAME);
        List<String> mAdjustmentID = JsonParser2.getFieldValueSoql(mAdjustmentRecords.toString(), "Id");
        List<Integer> mAdjustmentQuantity = JsonParser2.
                getFieldValueSoql2(mAdjustmentRecords.toString(), "thn__PMS_Quantity__c");
        List<String> mAdjustmentStartUTC = JsonParser2.
                getFieldValueSoql(mAdjustmentRecords.toString(), "thn__PMS_Start_UTC__c");
        List<String> mAdjustmentEndtUTC = JsonParser2.
                getFieldValueSoql(mAdjustmentRecords.toString(), "thn__PMS_End_UTC__c");
        Assert.assertEquals(qhrMewsState, "Optional");
        Assert.assertEquals(reservationState, "Optional");
        Assert.assertEquals(pmsBlockStatus, "Optional");
        Assert.assertEquals(reservationPMSBlock, pmsBlockID);
        Assert.assertEquals(mAdjustmentID.size(), 5);
        Assert.assertEquals(mAdjustmentStartUTC.get(0), date.generateTodayDate2_plus(0, 2) +
                "T12:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndtUTC.get(0), date.generateTodayDate2_plus(0, 3) +
                "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(1), date.generateTodayDate2_plus(0, 3) +
                "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndtUTC.get(1), date.generateTodayDate2_plus(0, 4) +
                "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(2), date.generateTodayDate2_plus(0, 4) +
                "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndtUTC.get(2), date.generateTodayDate2_plus(0, 5) +
                "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(3), date.generateTodayDate2_plus(0, 5) +
                "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndtUTC.get(3), date.generateTodayDate2_plus(0, 6) +
                "T14:30:00.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(4), reservationArrival);
        Assert.assertEquals(mAdjustmentEndtUTC.get(4), reservationDeparture);
        Assert.assertEquals(mAdjustmentQuantity.get(0).intValue(), 1);
        Assert.assertEquals(mAdjustmentQuantity.get(1).intValue(), 1);
        Assert.assertEquals(mAdjustmentQuantity.get(2).intValue(), 1);
        Assert.assertEquals(mAdjustmentQuantity.get(3).intValue(), 1);
        Assert.assertEquals(mAdjustmentQuantity.get(4).intValue(), -1);
    }

    @Test(priority = 9, description = "Update MYCE Quote’s stage. Reservations have been sent successfully. Expected" +
            " result: TH PMS Block status is changed to status defined in Agile Mews Stage Correspondence mdt, Mews" +
            " Status on Quote hotel rooms and Reservations is updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-460: MEWS - Availability Block")
    public void case9() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest7'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        StringBuilder qhrRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String qhrID = JsonParser2.getFieldValue(qhrRecord.toString(), "Id");
        String qhrMewsState = JsonParser2.getFieldValue(qhrRecord.toString(), "thn__Mews_State__c");
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__Quote_Hotel_Room__c="
                + qhrID + "'", ORG_USERNAME);
        String reservationState = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__State__c");
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockStatus = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__Status__c");
        Assert.assertEquals(qhrMewsState, "Confirmed");
        Assert.assertEquals(reservationState, "Confirmed");
        Assert.assertEquals(pmsBlockStatus, "Confirmed");
    }

    @Test(priority = 10, description = "Cancel Reservation where TH PMS block != null and TH PMS Block != Canceled." +
            " Make sure today < TH PMS Block release date. Expected result: MAdjustment record:  number of cancelled" +
            " reservations, Start, End, and ServiceID is created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-460: MEWS - Availability Block")
    public void case10() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MEWSavailabilityBlockAutoTest7'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        reservations.updateReservationSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__State__c='Canceled' thn__Notes__c='www'", ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        StringBuilder reservationRecord = reservations.getReservationSFDX(SFDX, "thn__MYCE_Quote__c="
                + quoteID + "'", ORG_USERNAME);
        StringBuilder mAdjustmentRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__PMS_Quantity__c," +
                " thn__PMS_Start_UTC__c, thn__PMS_End_UTC__c FROM thn__MAdjustment__c WHERE thn__PMS_Block__c='"
                + pmsBlockID + "'", ORG_USERNAME);
        List<String> mAdjustmentID = JsonParser2.getFieldValueSoql(mAdjustmentRecords.toString(), "Id");
        String reservationArrival = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__StartUtc__c");
        String reservationDeparture = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__EndUtc__c");
        List<Integer> mAdjustmentQuantity = JsonParser2.
                getFieldValueSoql2(mAdjustmentRecords.toString(), "thn__PMS_Quantity__c");
        List<String> mAdjustmentStartUTC = JsonParser2.
                getFieldValueSoql(mAdjustmentRecords.toString(), "thn__PMS_Start_UTC__c");
        List<String> mAdjustmentEndtUTC = JsonParser2.
                getFieldValueSoql(mAdjustmentRecords.toString(), "thn__PMS_End_UTC__c");
        Assert.assertEquals(mAdjustmentID.size(), 6);
        Assert.assertEquals(mAdjustmentStartUTC.get(0), date.generateTodayDate2_plus(0, 2) +
                "T12:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndtUTC.get(0), date.generateTodayDate2_plus(0, 3) +
                "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(1), date.generateTodayDate2_plus(0, 3) +
                "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndtUTC.get(1), date.generateTodayDate2_plus(0, 4) +
                "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(2), date.generateTodayDate2_plus(0, 4) +
                "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndtUTC.get(2), date.generateTodayDate2_plus(0, 5) +
                "T23:59:59.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(3), date.generateTodayDate2_plus(0, 5) +
                "T00:00:00.000+0000");
        Assert.assertEquals(mAdjustmentEndtUTC.get(3), date.generateTodayDate2_plus(0, 6) +
                "T14:30:00.000+0000");
        Assert.assertEquals(mAdjustmentStartUTC.get(4), reservationArrival);
        Assert.assertEquals(mAdjustmentEndtUTC.get(4), reservationDeparture);
        Assert.assertEquals(mAdjustmentStartUTC.get(5), reservationArrival);
        Assert.assertEquals(mAdjustmentEndtUTC.get(5), reservationDeparture);
        Assert.assertEquals(mAdjustmentQuantity.get(0).intValue(), 1);
        Assert.assertEquals(mAdjustmentQuantity.get(1).intValue(), 1);
        Assert.assertEquals(mAdjustmentQuantity.get(2).intValue(), 1);
        Assert.assertEquals(mAdjustmentQuantity.get(3).intValue(), 1);
        Assert.assertEquals(mAdjustmentQuantity.get(4).intValue(), -1);
        Assert.assertEquals(mAdjustmentQuantity.get(5).intValue(), 1);
    }

}
