package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class MeetingRoomSetup extends BaseTest {

    @Test(priority = 1, description = "Create Quote. Create Quote Meeting room linked to resource, Setup == null." +
            " Expected result: Setup is filled with Default setup value on Resource.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-365: Meeting room - Setup")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='MeetingRoomSetupAutoRes'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MeetingRoomSetupAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        String resourceId = resource.createResourceSFDX(SFDX, "Name='MeetingRoomSetupAutoRes' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room' thn__Default_setup__c='Circle'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MeetingRoomSetupAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String meetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId + "'",
                ORG_USERNAME);
        StringBuilder meetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='" + meetingRoomId +
                "'", ORG_USERNAME);
        String meetingRoomSetup = JsonParser2.getFieldValue(meetingRoomRecord.toString(), "thn__Setup__c");
        Assert.assertEquals(meetingRoomSetup, "Circle");
    }

    @Test(priority = 2, description = "Create Quote Meeting room linked to resource, Setup != null. Expected result:" +
            " Setup is filled with value selected while creating Quote meeting room.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-365: Meeting room - Setup")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MeetingRoomSetupAutoTest'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='MeetingRoomSetupAutoRes'",
                ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String meetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                        + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId +
                "' thn__Setup__c='Party'", ORG_USERNAME);
        StringBuilder meetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='" + meetingRoomId +
                "'", ORG_USERNAME);
        String meetingRoomSetup = JsonParser2.getFieldValue(meetingRoomRecord.toString(), "thn__Setup__c");
        Assert.assertEquals(meetingRoomSetup, "Party");
    }

    @Test(priority = 3, description = "Add Package containing Meeting rooms to the Quote. Expected result:" +
            " Setup is filled with Default setup value on Resource.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-365: Meeting room - Setup")
    public void case3() throws InterruptedException, IOException {
        packages.deletePackageSFDX(SFDX, "Name='MeetingRoomSetupPackageAuto", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='MeetingRoomSetupPackageAuto' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='MeetingRoomSetupAutoRes'",
                ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1 thn__Resource__c='" + resourceId +
                "'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MeetingRoomSetupAutoTest'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'" + " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        StringBuilder meetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        String meetingRoomSetup = JsonParser2.getFieldValue(meetingRoomRecord.toString(), "thn__Setup__c");
        Assert.assertEquals(meetingRoomSetup, "Circle");
    }

    @Test(priority = 4, description = "Update Default setup value on Resource; Set Setup on Quote Meeting room to" +
            " null. Expected result: Setup is updated to the new Default setup value on Resource.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-365: Meeting room - Setup")
    public void case4() throws InterruptedException, IOException {
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='MeetingRoomSetupAutoRes'",
                ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MeetingRoomSetupAutoTest'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String meetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                        + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId + "'",
                ORG_USERNAME);
        resource.updateResourceSFDX(SFDX, "Id='" + resourceId + "'", "thn__Default_setup__c='Theater'",
                ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "Id='" + meetingRoomId + "'", "thn__Setup__c=''",
                ORG_USERNAME);
        StringBuilder meetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='" + meetingRoomId +
                "'", ORG_USERNAME);
        String meetingRoomSetup = JsonParser2.getFieldValue(meetingRoomRecord.toString(), "thn__Setup__c");
        Assert.assertEquals(meetingRoomSetup, "Theater");
    }

    @Test(priority = 5, description = "Update Default setup value on Resource; Do not set Setup on Quote Meeting room" +
            " to null. Expected result: Setup is not updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-365: Meeting room - Setup")
    public void case5() throws InterruptedException, IOException {
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='MeetingRoomSetupAutoRes'",
                ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MeetingRoomSetupAutoTest'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String meetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                        + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId + "'",
                ORG_USERNAME);
        resource.updateResourceSFDX(SFDX, "Id='" + resourceId + "'", "thn__Default_setup__c='U-Shape'",
                ORG_USERNAME);
        StringBuilder updatedResourceRecord = resource.getResourceSFDX(SFDX, "Id='" + resourceId + "'",
                ORG_USERNAME);
        String resourceDefaultSetup = JsonParser2.getFieldValue(updatedResourceRecord.toString(), "thn__Default_setup__c");
        StringBuilder meetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX, "Id='" + meetingRoomId +
                "'", ORG_USERNAME);
        String meetingRoomSetup = JsonParser2.getFieldValue(meetingRoomRecord.toString(), "thn__Setup__c");
        Assert.assertEquals(meetingRoomSetup, "Theater");
        Assert.assertNotEquals(meetingRoomSetup, resourceDefaultSetup);
    }

}
