package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;

public class ShadowMeetingRooms extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Shadow Meeting rooms")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Create a new myce quote and a meeting room, change the meeting room’s resource" +
            " to one with related groupings. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("Shadow Meeting rooms")
    public void case1() throws InterruptedException, IOException {
        resource.deleteResourceSFDX(SFDX, "Name='ShadowMR1'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ShadowMR2'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ShadowMeetingRoomsAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ShadowMR1' thn__Hotel__c='" + propertyID +
                "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ShadowMR2' thn__Hotel__c='" + propertyID +
                "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ShadowMeetingRoomsAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Resource__c='" + resourceId1 + "'", ORG_USERNAME);
    }

}
