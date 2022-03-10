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

public class UnableToChangeMeetingRoomOnProduct extends BaseTest {

    @Test(priority = 1, description = "Create 3 resources: Parent Resource with 2 child resources, One of the child" +
            " resources should be grouped with another child resource. Create a MYCE Quote where Arrival date =" +
            " today + 1 day, Departure date = today + 3 days. Instantiate a Meeting room with grouped resource where" +
            " Start Date/Time = today + 2 days 07:00, End Date/Time = today + 2 days 02/04/2022 23:30. Instantiate" +
            " a Meeting room with another grouped resource where Start Date/Time =today + 1 day 12:00, End Date/Time" +
            " = today + 1 day 14:30. Instantiate a Quote Product where Start Date Time = today + 2 days 12:00," +
            " End Date Time = today + 2 days 12:15, Meeting room = second created meeting room. Start Date/Time and" +
            " End Date/Time of the second Quote meeting room are updated and = Start Date/Time and End Date/Time of" +
            " he Quote Product. Change the resource of the second Quote meeting room on the same resource like on" +
            " the first Quote meeting room. Expected result: Resource is updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-311: Unable to change Meeting room on Product")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='KIT C&E AUTO'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='Marmeren Hal AUTO'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='Conference & Events AUTO'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='UnableToChangeMeetingRoomOnProductAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES' thn__Hotel__c='" +
                propertyID + "'", ORG_USERNAME);
        String winesID = JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceID1 = resource.createResourceSFDX(SFDX, "Name='KIT C&E AUTO' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceID2 = resource.createResourceSFDX(SFDX, "Name='Marmeren Hal AUTO' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room' thn__Parent_Resource__c='" + resourceID1 + "'" +
                " thn__Bookable__c=true", ORG_USERNAME);
        String resourceID3 = resource.createResourceSFDX(SFDX, "Name='Conference & Events AUTO' thn__Hotel__c='"
                        + propertyID + "' thn__Type__c='Meeting Room' thn__Parent_Resource__c='" + resourceID1 + "'" +
                " thn__Bookable__c=true thn__Group_Resource__c='" + resourceID1 + "'", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='UnableToChangeMeetingRoomOnProductAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 3) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quotemeetingRoomId1 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + meetingFullDayID + "' thn__Property__c='" + propertyID +
                "' thn__Resource__c='" + resourceID3 + "' thn__Start_Date_Time__c='" +
                date.generateTodayDate2_plus(0, 2) + "T06:00:00.000+0000' thn__End_Date_Time__c='" +
                date.generateTodayDate2_plus(0, 2) + "T22:30:00.000+0000'", ORG_USERNAME);
        String quotemeetingRoomId2 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + meetingFullDayID + "' thn__Property__c='" + propertyID +
                "' thn__Resource__c='" + resourceID2 + "' thn__Start_Date_Time__c='" +
                date.generateTodayDate2_plus(0, 1) + "T11:00:00.000+0000' thn__End_Date_Time__c='" +
                date.generateTodayDate2_plus(0, 1) + "T13:30:00.000+0000'", ORG_USERNAME);
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + winesID + "' thn__Start_Date_Time__c='" +
                date.generateTodayDate2_plus(0, 2) + "T11:00:00.000+0000' thn__End_Date_Time__c='" +
                date.generateTodayDate2_plus(0, 2) + "T11:15:00.000+0000' thn__Service_Area__c='" +
                quotemeetingRoomId2 + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='" +
                quotemeetingRoomId2 + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'",
                ORG_USERNAME);
        String quoteMeetingRoomStartDateTime = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Date_Time__c");
        String quoteMeetingRoomEndDateTime = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Date_Time__c");
        String quoteProductStartDateTime = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Date_Time__c");
        String quoteProductEndDateTime = JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__End_Date_Time__c");
        Assert.assertEquals(quoteMeetingRoomStartDateTime, quoteProductStartDateTime);
        Assert.assertEquals(quoteMeetingRoomEndDateTime, quoteProductEndDateTime);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "Id='" + quotemeetingRoomId2 + "'",
                "thn__Resource__c='" + resourceID3 + "'", ORG_USERNAME);
        StringBuilder updatedQuoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='" +
                quotemeetingRoomId2 + "'",  ORG_USERNAME);
        String quoteMeetingRoom2Resource = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__Resource__c");
        Assert.assertEquals(quoteMeetingRoom2Resource, resourceID3);
    }
}
