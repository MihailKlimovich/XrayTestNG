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

public class ErrorInChangingTimesInMeetingRooms extends BaseTest {

    @Test(priority = 1, description = "Create 3 resources: Parent Resource with 2 child resources, One of the child" +
            " resources should be grouped with another child resource. Create a MYCE Quote where Arrival date =" +
            " today + 1 day, Departure date = today + 3 days. Instantiate a Meeting room with grouped resource where" +
            " Start Date/Time = today + 2 days 07:00, End Date/Time = today + 2 days 02/04/2022 23:30. Instantiate" +
            " a Meeting room with another grouped resource where Start Date/Time =today + 1 day 12:00, End Date/Time" +
            " = today + 1 day 14:30. Change the Start Date/Time and End Date/Time on the second Quote Meeting Room" +
            " like in the first Quote Meeting Room. Expected result: Start Date/Time and End Date/Time on the" +
            " first Quote Meeting Room = Start Date/Time and End Date/Time on the second Quote Meeting Room.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-299: Error 'Snag' Error in changing times in meeting rooms")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='KIT C&E AUTO2'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='Marmeren Hal AUTO2'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='Conference & Events AUTO2'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ErrorInChangingTimesInMeetingRoomsAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceID1 = resource.createResourceSFDX(SFDX, "Name='KIT C&E AUTO2' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceID2 = resource.createResourceSFDX(SFDX, "Name='Marmeren Hal AUTO2' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room' thn__Parent_Resource__c='" + resourceID1 + "'" +
                " thn__Bookable__c=true", ORG_USERNAME);
        String resourceID3 = resource.createResourceSFDX(SFDX, "Name='Conference & Events AUTO2' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room' thn__Parent_Resource__c='" + resourceID1 + "'" +
                " thn__Bookable__c=true thn__Group_Resource__c='" + resourceID1 + "'", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ErrorInChangingTimesInMeetingRoomsAutoTest'" +
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
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "Id='" + quotemeetingRoomId2 + "'",
                "thn__Start_Date_Time__c='" + date.generateTodayDate2_plus(0, 2) +
                        "T06:00:00.000+0000' thn__End_Date_Time__c='"  + date.generateTodayDate2_plus(0, 2)
                        + "T22:30:00.000+0000'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='" +
                quotemeetingRoomId1 + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='" +
                quotemeetingRoomId2 + "'", ORG_USERNAME);
        String quoteMeetingRoomStartDateTime1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Start_Date_Time__c");
        String quoteMeetingRoomEndDateTime1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__End_Date_Time__c");
        String quoteMeetingRoomStartDateTime2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Start_Date_Time__c");
        String quoteMeetingRoomEndDateTime2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__End_Date_Time__c");
        Assert.assertEquals(quoteMeetingRoomStartDateTime2, quoteMeetingRoomStartDateTime1);
        Assert.assertEquals(quoteMeetingRoomEndDateTime2, quoteMeetingRoomEndDateTime1);
    }

}
