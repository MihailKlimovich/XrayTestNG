package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class AvailabilityLightningComponentShowsInactiveRoomTypes extends BaseTest {

    @Test(priority = 1, description = "Create Room Type where thn__Is_Inactive__c=false. Create Quote, open Quote" +
            " record, click Check Availability button. Expected result: The room type is present in the list for the" +
            " selected property.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-294: Availability Lightning component shows inactive room types.")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        roomType.deleteRoomTypeSFDX(SFDX, "Name='AvailabilityAutoTest'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='AvailabilityShowsInactiveRoomTypesAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        roomType.createResourceSFDX(SFDX, "Name='AvailabilityAutoTest' thn__Hotel__c='" + propertyID +
                "' thn__Quantity__c=25 thn__Is_Inactive__c=false", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='AvailabilityShowsInactiveRoomTypesAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()+
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        StringBuilder roomTypeRecord = roomType.getRoomTypeSFDX(SFDX, "Name='AvailabilityAutoTest'",
                ORG_USERNAME);
        String roomTypeIsInactive = JsonParser2.getFieldValue(roomTypeRecord.toString(), "thn__Is_Inactive__c");
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("AvailabilityShowsInactiveRoomTypesAutoTest");
        myceQuotes.clickCheckkAvailabilitiesButton();
        String message = checkAvailabilitiesComponent.
                checkActiveRoomType("Demo", "AvailabilityAutoTest");
        Assert.assertEquals(roomTypeIsInactive, "false");
        Assert.assertEquals(message, "Room Type is present");
    }

    @Test(priority = 2, description = "Update Room Type: thn__Is_Inactive__c=true. Expected result: The room type" +
            " is not present in the list for the selected property.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-294: Availability Lightning component shows inactive room types.")
    public void case2() throws InterruptedException, IOException {
        roomType.updateRoomTypeSFDX(SFDX, "Name='AvailabilityAutoTest'", "thn__Is_Inactive__c=true",
                ORG_USERNAME);
        StringBuilder roomTypeRecord = roomType.getRoomTypeSFDX(SFDX, "Name='AvailabilityAutoTest'",
                ORG_USERNAME);
        String roomTypeIsInactive = JsonParser2.getFieldValue(roomTypeRecord.toString(), "thn__Is_Inactive__c");
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("AvailabilityShowsInactiveRoomTypesAutoTest");
        myceQuotes.clickCheckkAvailabilitiesButton();
        String message = checkAvailabilitiesComponent.
                checkActiveRoomType("Demo", "AvailabilityAutoTest");
        Assert.assertEquals(roomTypeIsInactive, "true");
        Assert.assertEquals(message, "Room Type is not present");
    }

}
