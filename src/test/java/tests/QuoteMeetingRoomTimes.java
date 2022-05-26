package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteMeetingRoomTimes extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote (Arrival Date = today + 2 days, Departure Date = today +" +
            " 9 days. Instantiate a Quote Meeting Room (Start Date/Time = today + 2 days 08:00, End Date/Time =" +
            " today + 2 days 17:00). Instantiate a Quote product: meeting room != null, impact timing == false," +
            " Specify New time for Start Date Time and End Date/time fields. Expected Result: Meeting Room times are" +
            " not updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-709: Quote meeting room times")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteMeetingRoomTimesAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder foodRecord = product.getProductSFDX(SFDX, "Name='FOOD'", ORG_USERNAME);
        String productFoodID= JsonParser2.getFieldValue(foodRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteMeetingRoomTimesAutoTest'" +
                        " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0  ,2) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 9) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quoteMeetinRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Date_Time__c='" +
                date.generateTodayDate2_plus(0, 2) + "T08:00:00.000+0000' thn__End_Date_Time__c='" +
                date.generateTodayDate2_plus(0, 2) + "T17:00:00.000+0000'", ORG_USERNAME);
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productFoodID + "' thn__Service_Area__c='" + quoteMeetinRoomId + "'" +
                " thn__Impact_Timing__c=false thn__Start_Date_Time__c=" +
                date.generateTodayDate2_plus(0, 2) + "T12:00:00.000+0000 thn__End_Date_Time__c=" +
                date.generateTodayDate2_plus(0, 2) + "T13:30:00.000+0000" , ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetinRoomId + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        String qmrStartDateTime = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Date_Time__c");
        String qmrEndDateTime = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Date_Time__c");
        String quoteProductStartDateTime = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Date_Time__c");
        String quoteProductEndDateTime = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__End_Date_Time__c");
        Assert.assertEquals(quoteProductStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T12:00:00.000+0000");
        Assert.assertEquals(quoteProductEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T13:30:00.000+0000");
        Assert.assertEquals(qmrStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T08:00:00.000+0000");
        Assert.assertEquals(qmrEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T17:00:00.000+0000");
    }

    @Test(priority = 2, description = "Go the Quote Product that we created and updated the Start Date Time and" +
            " End Date/Time fields (meeting room != null, impact timing == false) Expected Result: Meeting Room" +
            " times are not updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-709: Quote meeting room times")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='QuoteMeetingRoomTimesAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 2) +
                        "T10:30:00.000+0000 thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 2) +
                        "T14:00:00.000+0000", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String qmrStartDateTime = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Date_Time__c");
        String qmrEndDateTime = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Date_Time__c");
        String quoteProductStartDateTime = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Date_Time__c");
        String quoteProductEndDateTime = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__End_Date_Time__c");
        Assert.assertEquals(quoteProductStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T10:30:00.000+0000");
        Assert.assertEquals(quoteProductEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T14:00:00.000+0000");
        Assert.assertEquals(qmrStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T08:00:00.000+0000");
        Assert.assertEquals(qmrEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T17:00:00.000+0000");
    }

    @Test(priority = 3, description = "Instantiate a Quote product (meeting room != null, impact timing == true," +
            " Specify New time for Start Date Time and End Date/time fields). Expected Result: Meeting Room times" +
            " are updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-709: Quote meeting room times")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='QuoteMeetingRoomTimesAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetinRoomId = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "Id");
        StringBuilder foodRecord = product.getProductSFDX(SFDX, "Name='FOOD'", ORG_USERNAME);
        String productFoodID= JsonParser2.getFieldValue(foodRecord.toString(), "Id");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String productWinesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        String quoteProductWinesID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productWinesID + "' thn__Service_Area__c='" + quoteMeetinRoomId + "'" +
                " thn__Impact_Timing__c=true thn__Start_Date_Time__c=" +
                date.generateTodayDate2_plus(0, 2) + "T22:00:00.000+0000 thn__End_Date_Time__c=" +
                date.generateTodayDate2_plus(0, 2) + "T23:00:00.000+0000" , ORG_USERNAME);
        StringBuilder quoteProductFoodRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + productFoodID + "'", ORG_USERNAME);
        String quoteProductFoodStartDateTime = JsonParser2.
                getFieldValue(quoteProductFoodRecord.toString(), "thn__Start_Date_Time__c");
        String quoteProductFoodEndDateTime = JsonParser2.
                getFieldValue(quoteProductFoodRecord.toString(), "thn__End_Date_Time__c");
        StringBuilder quoteProductWinesRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + productWinesID + "'", ORG_USERNAME);
        String quoteProductWinesStartDateTime = JsonParser2.
                getFieldValue(quoteProductWinesRecord.toString(), "thn__Start_Date_Time__c");
        String quoteProductWinesEndDateTime = JsonParser2.
                getFieldValue(quoteProductWinesRecord.toString(), "thn__End_Date_Time__c");
        StringBuilder updatedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String qmrStartDateTime = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__Start_Date_Time__c");
        String qmrEndDateTime = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__End_Date_Time__c");
        Assert.assertEquals(quoteProductFoodStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T10:30:00.000+0000");
        Assert.assertEquals(quoteProductFoodEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T14:00:00.000+0000");
        Assert.assertEquals(quoteProductWinesStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T22:00:00.000+0000");
        Assert.assertEquals(quoteProductWinesEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T23:00:00.000+0000");
        Assert.assertEquals(qmrStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T22:00:00.000+0000");
        Assert.assertEquals(qmrEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T23:00:00.000+0000");
    }

    @Test(priority = 4, description = "Go the Quote Product that we created and updated the Start Date Time and" +
            " End Date/Time fields (meeting room != null, impact timing == true). Expected Result: Quote meeting" +
            " room start and end date/time are updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-709: Quote meeting room times")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='QuoteMeetingRoomTimesAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder foodRecord = product.getProductSFDX(SFDX, "Name='FOOD'", ORG_USERNAME);
        String productFoodID= JsonParser2.getFieldValue(foodRecord.toString(), "Id");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String productWinesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                productWinesID + "'", "thn__Start_Date_Time__c=" + date.generateTodayDate2_plus(0, 2)
                + "T22:30:00.000+0000 thn__End_Date_Time__c=" + date.generateTodayDate2_plus(0, 2) +
                "T23:30:00.000+0000", ORG_USERNAME);
        StringBuilder quoteProductFoodRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + productFoodID + "'", ORG_USERNAME);
        String quoteProductFoodStartDateTime = JsonParser2.
                getFieldValue(quoteProductFoodRecord.toString(), "thn__Start_Date_Time__c");
        String quoteProductFoodEndDateTime = JsonParser2.
                getFieldValue(quoteProductFoodRecord.toString(), "thn__End_Date_Time__c");
        StringBuilder quoteProductWinesRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + productWinesID + "'", ORG_USERNAME);
        String quoteProductWinesStartDateTime = JsonParser2.
                getFieldValue(quoteProductWinesRecord.toString(), "thn__Start_Date_Time__c");
        String quoteProductWinesEndDateTime = JsonParser2.
                getFieldValue(quoteProductWinesRecord.toString(), "thn__End_Date_Time__c");
        StringBuilder updatedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String qmrStartDateTime = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__Start_Date_Time__c");
        String qmrEndDateTime = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__End_Date_Time__c");
        Assert.assertEquals(quoteProductFoodStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T10:30:00.000+0000");
        Assert.assertEquals(quoteProductFoodEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T14:00:00.000+0000");
        Assert.assertEquals(quoteProductWinesStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T22:30:00.000+0000");
        Assert.assertEquals(quoteProductWinesEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T23:30:00.000+0000");
        Assert.assertEquals(qmrStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T22:30:00.000+0000");
        Assert.assertEquals(qmrEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T23:30:00.000+0000");
    }

    @Test(priority = 5, description = "Update Quote meeting room start and/or end date time (related quote product" +
            " 1 impact timing == true, related quote product 2 impact timing == false). Expected Result: related" +
            " quote product 1 start and end date/time are updated, related quote product 2 start and end date/time" +
            " are not updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-709: Quote meeting room times")
    public void case5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='QuoteMeetingRoomTimesAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder foodRecord = product.getProductSFDX(SFDX, "Name='FOOD'", ORG_USERNAME);
        String productFoodID= JsonParser2.getFieldValue(foodRecord.toString(), "Id");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String productWinesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Start_Date_Time__c='" + date.generateTodayDate2_plus(0, 2) +
                        "T01:00:00.000+0000' thn__End_Date_Time__c='" + date.generateTodayDate2_plus(0, 2) +
                        "T02:15:00.000+0000'", ORG_USERNAME);
        StringBuilder quoteProductFoodRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + productFoodID + "'", ORG_USERNAME);
        String quoteProductFoodStartDateTime = JsonParser2.
                getFieldValue(quoteProductFoodRecord.toString(), "thn__Start_Date_Time__c");
        String quoteProductFoodEndDateTime = JsonParser2.
                getFieldValue(quoteProductFoodRecord.toString(), "thn__End_Date_Time__c");
        StringBuilder quoteProductWinesRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + productWinesID + "'", ORG_USERNAME);
        String quoteProductWinesStartDateTime = JsonParser2.
                getFieldValue(quoteProductWinesRecord.toString(), "thn__Start_Date_Time__c");
        String quoteProductWinesEndDateTime = JsonParser2.
                getFieldValue(quoteProductWinesRecord.toString(), "thn__End_Date_Time__c");
        StringBuilder updatedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String qmrStartDateTime = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__Start_Date_Time__c");
        String qmrEndDateTime = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__End_Date_Time__c");
        Assert.assertEquals(qmrStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T01:00:00.000+0000");
        Assert.assertEquals(qmrEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T02:15:00.000+0000");
        Assert.assertEquals(quoteProductFoodStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T10:30:00.000+0000");
        Assert.assertEquals(quoteProductFoodEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T14:00:00.000+0000");
        Assert.assertEquals(quoteProductWinesStartDateTime,
                date.generateTodayDate2_plus(0, 2) + "T01:00:00.000+0000");
        Assert.assertEquals(quoteProductWinesEndDateTime,
                date.generateTodayDate2_plus(0, 2) + "T02:15:00.000+0000");
    }

}
