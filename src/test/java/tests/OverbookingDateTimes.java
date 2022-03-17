package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class OverbookingDateTimes extends BaseTest{


    @Test(priority = 1, description = "Preconditions: Create a Meeting Room and assign a Resource to it that is" +
            " NOT Default, Create another one with the same Resource but different (date) times.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-629: Overbooking - date times")
    public void preconditions() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME, ADMIN_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OverbookingDateTimesAutoTest'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='OverbookingDateTimesAutoTest", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceID = resource.createResourceSFDX(SFDX, "Name='OverbookingDateTimesAutoTest'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OverbookingDateTimesAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("OverbookingDateTimesAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.createQuoteMeetingRoom("OverbookingDateTimesAutoTest", "MEETING HALF DAY",
                date.generateTodayDate(), date.generateTodayDate(), "15:00", "16:00");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("OverbookingDateTimesAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.createQuoteMeetingRoom("OverbookingDateTimesAutoTest", "MEETING FULL DAY",
                date.generateTodayDate(), date.generateTodayDate(), "18:00", "19:00");
    }

    @Test(priority = 2, description = "Change the (date)times on the layout for second Quote Meeting Room to at least" +
            " overlap the date time of  the first record. Result: Overbooking and shadow processes are triggered." +
            " Overbooking message is toasted.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-629: Overbooking - date times")
    public void case1() throws InterruptedException, IOException {
        String expectedMessage = "Quote Meeting Room is overbooked.";
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("OverbookingDateTimesAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.openRecordByName("OverbookingDateTimesAutoTest - MEETING FULL DAY");
        quoteMeetingRoom.editTime( "07:00", "19:00");
        String message = quoteMeetingRoom.readToastMessage();
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 3, description = "Remove the ‘Overbooking user’ permission set from the current user. Go to" +
            " the 2nd Quote Meeting Room record that we created and try changing the Date time. Result: Error is" +
            " thrown with the message: ‘You don’t have the permission to overbook resources. Default meeting room " +
            "has been assigned.'")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-629: Overbooking - date times")
    public void case2() throws InterruptedException, IOException {
        String expectedMessage = "Something went wrong. Please contact support@thynk.cloud.";
        myceQuotes.goToMyceQuotes();
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/RemoveOverbookingPermissionSet.apex");
        myceQuotes.openMyceQoteRecord("OverbookingDateTimesAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.openRecordByName("OverbookingDateTimesAutoTest - MEETING FULL DAY");
        quoteMeetingRoom.editTime( "06:45", "19:00");
        String message = quoteMeetingRoom.readErrorMessage2();
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 4, description = "Postconditions: Add the ‘Overbooking user’ permission set from the" +
            " current user.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-629: Overbooking - date times")
    public void postconditions() throws InterruptedException, IOException {
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME, ADMIN_USERNAME);
    }

}
