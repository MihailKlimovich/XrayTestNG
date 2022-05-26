package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteProductMeetingRoomInteractions extends BaseTest{


    @Test(priority = 1, description = "Create a MYCE Quote. Add a Meeting Room to the Quote. Add a couple products to" +
            " the Quote. When adding new products make sure that ‘Start Time’ and ‘End Time’ fields of the products" +
            " that we add varies. Add a product to the Quote that doesn’t have a ‘Meeting Room’ selected" +
            " (service_area__c == null).")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-620: Quote product / meeting room interactions")
    public void preconditions() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteProductMeetingRoomInteractionsAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String productWinesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        StringBuilder activityRecord = product.getProductSFDX(SFDX, "Name='ACTIVITY'", ORG_USERNAME);
        String productActivityID= JsonParser2.getFieldValue(activityRecord.toString(), "Id");
        StringBuilder equipmentRecord = product.getProductSFDX(SFDX, "Name='EQUIPMENT'", ORG_USERNAME);
        String productEquipmentID= JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteProductMeetingRoomInteractionsAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quoteMeetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Date_Time__c=" +
                date.generateTodayDate2() + "T00:45:00.000Z thn__End_Date_Time__c=" + date.generateTodayDate2() +
                "T18:00:00.000Z" , ORG_USERNAME);
        /*homePageForScratchOrg.openAppLauncher();
        homePageForScratchOrg.sendTextInAppWindow("MYCE Quotes");
        myceQuotes.openMyceQoteRecord("QuoteProductMeetingRoomInteractionsAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.createQuoteMeetingRoom2("MEETING HALF DAY", date.generateTodayDate(), date.generateTodayDate(),
         "00:45", "23:00");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
         + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomId= JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "Id");*/
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='"
               + beverageID + "' thn__Service_Area__c='" + quoteMeetingRoomId + "' thn__Start_Date_Time__c=" +
                date.generateTodayDate2() + "T12:15:00.000Z thn__End_Date_Time__c=" + date.generateTodayDate2() +
                "T18:00:00.000Z", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='"
                + productDinerID + "' thn__Service_Area__c='" + quoteMeetingRoomId + "'" +
                " thn__Start_Date_Time__c=" + date.generateTodayDate2() + "T07:00:00.000Z thn__End_Date_Time__c=" +
                date.generateTodayDate2() + "T08:00:00.000Z", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='"
                + productWinesID + "' thn__Service_Area__c='" + quoteMeetingRoomId + "'" +
                " thn__Start_Date_Time__c=" + date.generateTodayDate2() + "T02:15:00.000Z thn__End_Date_Time__c=" +
                date.generateTodayDate2() + "T09:30:00.000Z", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='"
                + productActivityID + "' thn__Service_Area__c='" + quoteMeetingRoomId + "'" +
                " thn__Start_Date_Time__c=" + date.generateTodayDate2() + "T00:45:00.000Z thn__End_Date_Time__c=" +
                date.generateTodayDate2() + "T11:15:00.000Z", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='"
                + productEquipmentID + "' thn__Start_Date_Time__c=" + date.generateTodayDate2() + "T06:15:00.000Z" +
                        " thn__End_Date_Time__c=" + date.generateTodayDate2() + "T18:00:00.000Z", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "Id='" + quoteMeetingRoomId + "'", ORG_USERNAME);
        String quoteMeetingRoomStartTime= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Time__c");
        String quoteMeetingRoomEndTime= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteMeetingRoomStartTime, "00:45:00.000Z");
        Assert.assertEquals(quoteMeetingRoomEndTime, "18:00:00.000Z");
    }

    @Test(priority = 2, description = "Change the ‘Start Date/Time’ field of the ‘Quote Meetings Room’ so the value" +
            " is less than the minimum value of ‘Start Date/Time’ field of the ‘Quote Product’. Result: the" +
            " ‘Start Date/Time’ fields of the related ‘Quote Products’ did not change.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-620: Quote product / meeting room interactions")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductMeetingRoomInteractionsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Start_Date_Time__c=" + date.generateTodayDate2() + "T00:30:00.000Z", ORG_USERNAME);
        StringBuilder quoteProductBeverage = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='BEVERAGE'", ORG_USERNAME);
        String baverageStartTime= JsonParser2.getFieldValue(quoteProductBeverage.toString(), "thn__Start_Time__c");
        StringBuilder quoteProductDiner = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='DINER'", ORG_USERNAME);
        String dinerStartTime= JsonParser2.getFieldValue(quoteProductDiner.toString(), "thn__Start_Time__c");
        StringBuilder quoteProductWines = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='WINES'", ORG_USERNAME);
        String winesStartTime= JsonParser2.getFieldValue(quoteProductWines.toString(), "thn__Start_Time__c");
        StringBuilder quoteProductActivity = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='ACTIVITY'", ORG_USERNAME);
        String activityStartTime= JsonParser2.getFieldValue(quoteProductActivity.toString(), "thn__Start_Time__c");
        StringBuilder quoteProductEquipment = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='EQUIPMENT'", ORG_USERNAME);
        String equipmentStartTime= JsonParser2.
                getFieldValue(quoteProductEquipment.toString(), "thn__Start_Time__c");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomStartTime= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Time__c");
        Assert.assertEquals(quoteMeetingRoomStartTime, "00:30:00.000Z");
        Assert.assertEquals(baverageStartTime, "12:15:00.000Z");
        Assert.assertEquals(dinerStartTime, "07:00:00.000Z");
        Assert.assertEquals(winesStartTime, "02:15:00.000Z");
        Assert.assertEquals(activityStartTime, "00:45:00.000Z");
        Assert.assertEquals(equipmentStartTime, "06:15:00.000Z");
    }

    @Test(priority = 3, description = "Change the ‘Start Date/Time’ field of the ‘Quote Meetings Room’ so the value" +
            " is more than the minimum value of ‘Start Date/Time’ field of the ‘Quote Product’. Result: The ‘Start" +
            " Date/Time’ fields of the related ‘Quote Products’ which ‘Start Date/Time’ values were lower than the" +
            " new value of the Meeting room have had their ‘Start Date/Time’ field values changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-620: Quote product / meeting room interactions")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductMeetingRoomInteractionsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Start_Date_Time__c=" + date.generateTodayDate2() + "T07:15:00.000Z", ORG_USERNAME);
        StringBuilder quoteProductBeverage = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='BEVERAGE'", ORG_USERNAME);
        String baverageStartTime= JsonParser2.getFieldValue(quoteProductBeverage.toString(), "thn__Start_Time__c");
        StringBuilder quoteProductDiner = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='DINER'", ORG_USERNAME);
        String dinerStartTime= JsonParser2.getFieldValue(quoteProductDiner.toString(), "thn__Start_Time__c");
        StringBuilder quoteProductWines = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='WINES'", ORG_USERNAME);
        String winesStartTime= JsonParser2.getFieldValue(quoteProductWines.toString(), "thn__Start_Time__c");
        StringBuilder quoteProductActivity = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='ACTIVITY'", ORG_USERNAME);
        String activityStartTime= JsonParser2.getFieldValue(quoteProductActivity.toString(), "thn__Start_Time__c");
        StringBuilder quoteProductEquipment = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='EQUIPMENT'", ORG_USERNAME);
        String equipmentStartTime= JsonParser2.
                getFieldValue(quoteProductEquipment.toString(), "thn__Start_Time__c");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomStartTime= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Time__c");
        Assert.assertEquals(quoteMeetingRoomStartTime, "07:15:00.000Z");
        Assert.assertEquals(baverageStartTime, "12:15:00.000Z");
        Assert.assertEquals(dinerStartTime, "07:15:00.000Z");
        Assert.assertEquals(winesStartTime, "07:15:00.000Z");
        Assert.assertEquals(activityStartTime, "07:15:00.000Z");
        Assert.assertEquals(equipmentStartTime, "06:15:00.000Z");
    }

    @Test(priority = 4, description = "Change the ‘End Date/Time’ field of the ‘Quote Meetings Room’ so the value" +
            " is more than the maximum value of ‘End Date/Time’ field of the ‘Quote Product’. Result: The" +
            " ‘End Date/Time’ fields of the related ‘Quote Products’ did not change.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-620: Quote product / meeting room interactions")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductMeetingRoomInteractionsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__End_Date_Time__c=" + date.generateTodayDate2() + "T18:15:00.000Z", ORG_USERNAME);
        StringBuilder quoteProductBeverage = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='BEVERAGE'", ORG_USERNAME);
        String baverageEndTime= JsonParser2.getFieldValue(quoteProductBeverage.toString(), "thn__End_Time__c");
        StringBuilder quoteProductDiner = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='DINER'", ORG_USERNAME);
        String dinerEndTime= JsonParser2.getFieldValue(quoteProductDiner.toString(), "thn__End_Time__c");
        StringBuilder quoteProductWines = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='WINES'", ORG_USERNAME);
        String winesEndTime= JsonParser2.getFieldValue(quoteProductWines.toString(), "thn__End_Time__c");
        StringBuilder quoteProductActivity = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='ACTIVITY'", ORG_USERNAME);
        String activityEndTime= JsonParser2.getFieldValue(quoteProductActivity.toString(), "thn__End_Time__c");
        StringBuilder quoteProductEquipment = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='EQUIPMENT'", ORG_USERNAME);
        String equipmentEndTime= JsonParser2.getFieldValue(quoteProductEquipment.toString(), "thn__End_Time__c");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomEndTime= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteMeetingRoomEndTime, "18:15:00.000Z");
        Assert.assertEquals(baverageEndTime, "18:00:00.000Z");
        Assert.assertEquals(dinerEndTime, "08:00:00.000Z");
        Assert.assertEquals(winesEndTime, "09:30:00.000Z");
        Assert.assertEquals(activityEndTime, "11:15:00.000Z");
        Assert.assertEquals(equipmentEndTime, "18:00:00.000Z");
    }

    @Test(priority = 5, description = "Change the ‘End Date/Time’ field of the ‘Quote Meetings Room’ so the value is" +
            " less than the maximum value of ‘End Date/Time’ field of the ‘Quote Product’. Result: The" +
            " ‘End Date/Time’ fields of the related ‘Quote Products’ which ‘End Date/Time’ values were lower than" +
            " the new value of the Meeting room have had their ‘End Date/Time’ field values changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-620: Quote product / meeting room interactions")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductMeetingRoomInteractionsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__End_Date_Time__c=" + date.generateTodayDate2() + "T17:45:00.000Z", ORG_USERNAME);
        StringBuilder quoteProductBeverage = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='BEVERAGE'", ORG_USERNAME);
        String baverageEndTime= JsonParser2.getFieldValue(quoteProductBeverage.toString(), "thn__End_Time__c");
        StringBuilder quoteProductDiner = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='DINER'", ORG_USERNAME);
        String dinerEndTime= JsonParser2.getFieldValue(quoteProductDiner.toString(), "thn__End_Time__c");
        StringBuilder quoteProductWines = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='WINES'", ORG_USERNAME);
        String winesEndTime= JsonParser2.getFieldValue(quoteProductWines.toString(), "thn__End_Time__c");
        StringBuilder quoteProductActivity = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='ACTIVITY'", ORG_USERNAME);
        String activityEndTime= JsonParser2.getFieldValue(quoteProductActivity.toString(), "thn__End_Time__c");
        StringBuilder quoteProductEquipment = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='EQUIPMENT'", ORG_USERNAME);
        String equipmentEndTime= JsonParser2.getFieldValue(quoteProductEquipment.toString(), "thn__End_Time__c");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomEndTime= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteMeetingRoomEndTime, "17:45:00.000Z");
        Assert.assertEquals(baverageEndTime, "17:45:00.000Z");
        Assert.assertEquals(dinerEndTime, "08:00:00.000Z");
        Assert.assertEquals(winesEndTime, "09:30:00.000Z");
        Assert.assertEquals(activityEndTime, "11:15:00.000Z");
        Assert.assertEquals(equipmentEndTime, "18:00:00.000Z");
    }

    @Test(priority = 6, description = "Change the ‘Start Date/Time’ field of the ‘Quote Product’ so the value is" +
            " less than the minimum value of ‘Start Date/Time’ field of the ‘Quote Meetings Room’. Result: The" +
            " ‘Start Date/Time’ field of the ‘Quote Meetings Room’ has had its ‘Start Date/Time’ field value changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-620: Quote product / meeting room interactions")
    public void case5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductMeetingRoomInteractionsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' Name='DINER'",
                "thn__Start_Date_Time__c=" + date.generateTodayDate2() + "T06:30:00.000Z", ORG_USERNAME);
        StringBuilder quoteProductDiner = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='DINER'", ORG_USERNAME);
        String dinerStartTime= JsonParser2.getFieldValue(quoteProductDiner.toString(), "thn__Start_Time__c");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomStartTime= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Time__c");
        Assert.assertEquals(dinerStartTime, "06:30:00.000Z");
        Assert.assertEquals(quoteMeetingRoomStartTime, "06:30:00.000Z");
    }

    @Test(priority = 7, description = "Change the ‘Start Date/Time’ field of the ‘Quote Product’ so the value is" +
            " more than the minimum value of ‘Start Date/Time’ field of the ‘Quote Meetings Room’. Result: The" +
            " ‘Start Date/Time’ field of the ‘Quote Meetings Room’ has had its ‘Start Date/Time’ field value changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-620: Quote product / meeting room interactions")
    public void case6() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductMeetingRoomInteractionsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' Name='DINER'",
                "thn__Start_Date_Time__c=" + date.generateTodayDate2() + "T06:45:00.000Z", ORG_USERNAME);
        StringBuilder quoteProductDiner = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='DINER'", ORG_USERNAME);
        String dinerStartTime= JsonParser2.getFieldValue(quoteProductDiner.toString(), "thn__Start_Time__c");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomStartTime= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Time__c");
        Assert.assertEquals(dinerStartTime, "06:45:00.000Z");
        Assert.assertEquals(quoteMeetingRoomStartTime, "06:45:00.000Z");
    }

    @Test(priority = 8, description = "Change the ‘End Date/Time’ field of the ‘Quote Product’ so the value is more" +
            " than the maximum value of ‘End Date/Time’ field of the ‘Quote Meetings Room’. Result: The" +
            " ‘End Date/Time’ field of the ‘Quote Meetings Room’ has had its ‘End Date/Time’ field value changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-620: Quote product / meeting room interactions")
    public void case7() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductMeetingRoomInteractionsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' Name='BEVERAGE'",
                "thn__End_Date_Time__c=" + date.generateTodayDate2() + "T18:30:00.000Z", ORG_USERNAME);
        StringBuilder quoteProductBeverage = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='BEVERAGE'", ORG_USERNAME);
        String baverageEndTime= JsonParser2.getFieldValue(quoteProductBeverage.toString(), "thn__End_Time__c");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomEndTime= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(baverageEndTime, "18:30:00.000Z");
        Assert.assertEquals(quoteMeetingRoomEndTime, "18:30:00.000Z");
    }

    @Test(priority = 9, description = "Change the ‘End Date/Time’ field of the ‘Quote Product’ so the value is less" +
            " than the maximum value of ‘End Date/Time’ field of the ‘Quote Meetings Room’. Result: The" +
            " ‘End Date/Time’ field of the ‘Quote Meetings Room’ has had its ‘End Date/Time’ field value changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-620: Quote product / meeting room interactions")
    public void case8() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductMeetingRoomInteractionsAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' Name='BEVERAGE'",
                "thn__End_Date_Time__c=" + date.generateTodayDate2() + "T18:15:00.000Z", ORG_USERNAME);
        StringBuilder quoteProductBeverage = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' Name='BEVERAGE'", ORG_USERNAME);
        String baverageEndTime= JsonParser2.getFieldValue(quoteProductBeverage.toString(), "thn__End_Time__c");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomEndTime= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(baverageEndTime, "18:15:00.000Z");
        Assert.assertEquals(quoteMeetingRoomEndTime, "18:15:00.000Z");
    }
}
