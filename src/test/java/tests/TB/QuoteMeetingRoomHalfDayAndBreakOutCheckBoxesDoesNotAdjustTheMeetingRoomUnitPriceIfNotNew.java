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

public class QuoteMeetingRoomHalfDayAndBreakOutCheckBoxesDoesNotAdjustTheMeetingRoomUnitPriceIfNotNew extends BaseTest {

    @Test(priority = 1, description = "Create quote, create Quote Meeting room, Link a resource to the created" +
        " Quote Meeting Room. Expected result: Unit price on Quote meeting Room = Full day prise on resource.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-309: Quote Meeting Room 'Half Day' and 'Break Out' check boxes does not adjust the meeting room" +
            " Unit price if not new")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB309Autotest'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='TB309AutoResource'", ORG_USERNAME);
        StringBuilder hotelRecord1= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord1.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='TB309Autotest' thn__Pax__c=1 thn__Hotel__c='"
                        + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + "" +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3) +
                        " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String resourceID = resource.createResourceSFDX(SFDX, "Name='TB309AutoResource'" +
                " thn__Type__c='Meeting Room' thn__Break_out_full_day__c=75 thn__Break_out_half_day__c=25" +
                " thn__Full_day_price__c=100 thn__Half_day_price__c=50 thn__Hotel__c='" + propertyID + "'",
                ORG_USERNAME);
        String quoteMeetingRoomID = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceID + "'",
                ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID + "'", ORG_USERNAME);
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Id='" + resourceID + "'", ORG_USERNAME);
        String quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String resourceFullDayPrice = JsonParser2.
                getFieldValue(resourceRecord.toString(), "thn__Full_day_price__c");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, resourceFullDayPrice);
    }

    @Test(priority = 2, description = "Change the value of the checkbox Hald Day to true on Quote Meeting Room." +
            " Expected result: Unit price on Quote meeting Room = Half day prise on resource.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-309: Quote Meeting Room 'Half Day' and 'Break Out' check boxes does not adjust the meeting room" +
            " Unit price if not new")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='TB309Autotest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='TB309AutoResource'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Half_day__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String resourceHalfDayPrice = JsonParser2.
                getFieldValue(resourceRecord.toString(), "thn__Half_day_price__c");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, resourceHalfDayPrice);
    }

    @Test(priority = 3, description = "Change the value of the checkbox Hald Day to false and value of the checkbox" +
            " Break Out to true on Quote Meeting Room. Expected result: Unit price on Quote meeting Room = Break" +
            " out full day on resource.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-309: Quote Meeting Room 'Half Day' and 'Break Out' check boxes does not adjust the meeting room" +
            " Unit price if not new")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='TB309Autotest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='TB309AutoResource'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Half_day__c=false thn__Break_out__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String resourceBreakOutFullDay = JsonParser2.
                getFieldValue(resourceRecord.toString(), "thn__Break_out_full_day__c");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, resourceBreakOutFullDay);
    }

    @Test(priority = 4, description = "Change the value of the checkbox Hald Day to true and value of the checkbox" +
            " Break Out to true on Quote Meeting Room. Expected result: Unit price on Quote meeting Room = Break out" +
            " half day on resource.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-309: Quote Meeting Room 'Half Day' and 'Break Out' check boxes does not adjust the meeting room" +
            " Unit price if not new")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='TB309Autotest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='TB309AutoResource'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Half_day__c=true thn__Break_out__c=true", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String resourceBreakOutFullDay = JsonParser2.
                getFieldValue(resourceRecord.toString(), "thn__Break_out_half_day__c");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, resourceBreakOutFullDay);
    }

    @Test(priority = 5, description = "Change the value of the checkbox Hald Day to false and value of the checkbox" +
            " Break Out to false on Quote Meeting Room. Expected result: Unit price on Quote meeting Room = Full day" +
            " prise on resource.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-309: Quote Meeting Room 'Half Day' and 'Break Out' check boxes does not adjust the meeting room" +
            " Unit price if not new")
    public void case5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='TB309Autotest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='TB309AutoResource'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Half_day__c=false thn__Break_out__c=false", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quoteMeetingRoomUnitPrice = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String resourceBreakOutFullDay = JsonParser2.
                getFieldValue(resourceRecord.toString(), "thn__Full_day_price__c");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, resourceBreakOutFullDay);
    }



}
