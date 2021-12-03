package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class MeetingRoomAndResourcePricing extends BaseTest{

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Meeting room and resource pricing")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("Meeting room and resource pricing")
    public void preconditions() throws InterruptedException, IOException {
        resource.deleteResourceSFDX(SFDX, "Name='ResourcePricingAutoTest'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourcePricingAutoTest2'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourcePricingAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String resourceID = resource.createResourceSFDX(SFDX, "Name='ResourcePricingAutoTest' thn__Hotel__c='" +
                propertyID + "' thn__Type__c='Meeting Room' thn__Break_out_half_day__c=50 thn__Half_day_price__c=150" +
                " thn__Break_out_full_day__c=90 thn__Full_day_price__c=260", ORG_USERNAME);
        String resourceID2 = resource.createResourceSFDX(SFDX, "Name='ResourcePricingAutoTest2' thn__Hotel__c='" +
                propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID1 = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourcePricingAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
    }

    @Test(priority = 3, description = "Create Quote meeting room with conditions: Resource != default resource," +
            " Resource price !=null, Shadow == false, Half day == true, Break out == true. Expected result:" +
            " Unit price = resource Break out half day")
    @Severity(SeverityLevel.NORMAL)
    @Story("Meeting room and resource pricing")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='ResourcePricingAutoTest'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='ResourcePricingAutoTest'",
                ORG_USERNAME);
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String resourceID = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        String meetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceID + "'" +
                " thn__Shadow__c=false thn__Half_day__c=true thn__Break_out__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='"
                + meetingRoomId + "'", ORG_USERNAME);
        String meetingRoomUnitPrice = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(),
                "thn__Unit_Price__c");
        String resourceBreakOutHalfDay = JsonParser2.getFieldValue(resourceRecord.toString(),
                "thn__Break_out_half_day__c");
        Assert.assertEquals(meetingRoomUnitPrice, resourceBreakOutHalfDay);
    }

    @Test(priority = 4, description = "Create Quote meeting room with conditions: Resource != default resource," +
            " Resource price !=null, Shadow == false, Half day == true, Break out == false. Expected result:" +
            " Unit price = resource Half day price")
    @Severity(SeverityLevel.NORMAL)
    @Story("Meeting room and resource pricing")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='ResourcePricingAutoTest'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='ResourcePricingAutoTest'",
                ORG_USERNAME);
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceID = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        String meetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingFullDayID + "' thn__Resource__c='" + resourceID + "'" +
                " thn__Shadow__c=false thn__Half_day__c=true thn__Break_out__c=false", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='"
                + meetingRoomId + "'", ORG_USERNAME);
        String meetingRoomUnitPrice = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(),
                "thn__Unit_Price__c");
        String resourceHalfDayPrice = JsonParser2.getFieldValue(resourceRecord.toString(),
                "thn__Half_day_price__c");
        Assert.assertEquals(meetingRoomUnitPrice, resourceHalfDayPrice);
    }

    @Test(priority = 5, description = "Create Quote meeting room with conditions: Resource != default resource," +
            " Resource price !=null, Shadow == false, Half day == false, Break out == true. Expected result:" +
            " Unit price = resource Break out half day")
    @Severity(SeverityLevel.NORMAL)
    @Story("Meeting room and resource pricing")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='ResourcePricingAutoTest'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='ResourcePricingAutoTest'",
                ORG_USERNAME);
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String resourceID = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        String meetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceID + "'" +
                " thn__Shadow__c=false thn__Half_day__c=false thn__Break_out__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='"
                + meetingRoomId + "'", ORG_USERNAME);
        String meetingRoomUnitPrice = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(),
                "thn__Unit_Price__c");
        String resourceBreakOutFullDay = JsonParser2.getFieldValue(resourceRecord.toString(),
                "thn__Break_out_full_day__c");
        Assert.assertEquals(meetingRoomUnitPrice, resourceBreakOutFullDay);
    }

    @Test(priority = 6, description = "Create Quote meeting room with conditions: Resource != default resource," +
            " Resource price !=null, Shadow == false, Half day == false, Break out == false. Expected result:" +
            " Unit price = resource Half day price")
    @Severity(SeverityLevel.NORMAL)
    @Story("Meeting room and resource pricing")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='ResourcePricingAutoTest'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='ResourcePricingAutoTest'",
                ORG_USERNAME);
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceID = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        String meetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingFullDayID + "' thn__Resource__c='" + resourceID + "'" +
                " thn__Shadow__c=false thn__Half_day__c=false thn__Break_out__c=false", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='"
                + meetingRoomId + "'", ORG_USERNAME);
        String meetingRoomUnitPrice = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(),
                "thn__Unit_Price__c");
        String resourceFullDayPrice = JsonParser2.getFieldValue(resourceRecord.toString(),
                "thn__Full_day_price__c");
        Assert.assertEquals(meetingRoomUnitPrice, resourceFullDayPrice);
    }

    @Test(priority = 7, description = "Remove prices from the Resource used while creating Quote meeting rooms." +
            " Repeat steps above specifying Unit price . Unit price is as specified while creation")
    @Severity(SeverityLevel.NORMAL)
    @Story("Meeting room and resource pricing")
    public void case5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='ResourcePricingAutoTest'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='ResourcePricingAutoTest2'",
                ORG_USERNAME);
        String resourceID = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String meetingRoomId1 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceID + "'" +
                " thn__Shadow__c=false thn__Half_day__c=true thn__Break_out__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='"
                + meetingRoomId1 + "'", ORG_USERNAME);
        String meetingRoomId2 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingFullDayID + "' thn__Resource__c='" + resourceID + "'" +
                " thn__Shadow__c=false thn__Half_day__c=true thn__Break_out__c=false", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='"
                + meetingRoomId2 + "'", ORG_USERNAME);
        String meetingRoomId3 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceID + "'" +
                " thn__Shadow__c=false thn__Half_day__c=false thn__Break_out__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord3 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='"
                + meetingRoomId3 + "'", ORG_USERNAME);
        String meetingRoomId4 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingFullDayID + "' thn__Resource__c='" + resourceID + "'" +
                " thn__Shadow__c=false thn__Half_day__c=false thn__Break_out__c=false", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord4 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='"
                + meetingRoomId4 + "'", ORG_USERNAME);
        String meetingRoomUnitPrice1 = JsonParser2.getFieldValue(quoteMeetingRoomRecord1.toString(),
                "thn__Unit_Price__c");
        String meetingRoomUnitPrice2 = JsonParser2.getFieldValue(quoteMeetingRoomRecord2.toString(),
                "thn__Unit_Price__c");
        String meetingRoomUnitPrice3 = JsonParser2.getFieldValue(quoteMeetingRoomRecord3.toString(),
                "thn__Unit_Price__c");
        String meetingRoomUnitPrice4 = JsonParser2.getFieldValue(quoteMeetingRoomRecord4.toString(),
                "thn__Unit_Price__c");
        Assert.assertEquals(meetingRoomUnitPrice1, "240");
        Assert.assertEquals(meetingRoomUnitPrice2, "420");
        Assert.assertEquals(meetingRoomUnitPrice3, "240");
        Assert.assertEquals(meetingRoomUnitPrice4, "420");
    }

}
