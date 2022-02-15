package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class AllowResourceSelectionWhenAddingPackage extends BaseTest {

    @Test(priority = 1, description = "User has Overbooking permission. Create MYCE Quote. Add Quote meeting package" +
            " that contains Meeting room package line with “Master meeting space” set to TRUE. Select Resource and" +
            " Save the Quote meeting package. Result: Master meeting space on Quote package line is set to TRUE." +
            " Master meeting space on Quote meeting room is set to TRUE. Resource on Quote meeting room is the" +
            " resource selected while creating the Quote meeting package.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-528: Allow resource selection when adding package.")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME, ADMIN_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='AllowResourceSelectionAutoTest'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='AllowResourceSelectionPackageAutoTest", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='AllowResourceSelectionAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String resourceID = resource.createResourceSFDX(SFDX, "Name='AllowResourceSelectionAutoTest'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='AllowResourceSelectionPackageAutoTest'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1" +
                " thn__Master_meeting_space__c=true", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='AllowResourceSelectionAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quotePackageId = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Package__c='" + packageID + "' thn__Resource__c='" + resourceID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageId + "'", ORG_USERNAME);
        String qmrMasterMeetingSpace = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Master_meeting_space__c");
        String qmrResource = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        Assert.assertEquals(qmrMasterMeetingSpace, "true");
        Assert.assertEquals(qmrResource, resourceID);
    }

    @Test(priority = 2, description = "User has Overbooking permission. Add Quote meeting package that contains" +
            " Meeting room package line with “Master meeting space” set to FALSE. Select Resource and Save the Quot" +
            "e meeting package. Result: Master meeting space on Quote package line is FALSE. Master meeting space on" +
            " Quote meeting room is FALSE. Resource on Quote meeting room is DEFAULT.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-528: Allow resource selection when adding package.")
    public void case2() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='AllowResourceSelectionAutoTest2'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='AllowResourceSelectionPackageAutoTest2", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='AllowResourceSelectionAutoTest2'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String resourceID = resource.createResourceSFDX(SFDX, "Name='AllowResourceSelectionAutoTest2'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='AllowResourceSelectionPackageAutoTest2'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1" +
                " thn__Master_meeting_space__c=false", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='AllowResourceSelectionAutoTest2' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quotePackageId = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Package__c='" + packageID + "' thn__Resource__c='" + resourceID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageId + "'", ORG_USERNAME);
        String qmrMasterMeetingSpace = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Master_meeting_space__c");
        String qmrResource = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        Assert.assertEquals(qmrMasterMeetingSpace, "false");
        Assert.assertNotEquals(qmrResource, resourceID);
    }

    @Test(priority = 3, description = "User has Overbooking permission. Add Quote meeting package that contains" +
            " Meeting room package line with “Master meeting space” set to FALSE and Meeting room package line with" +
            " “Master meeting space” set to TRUE. Select Resource and Save the Quote meeting package. Result: Master" +
            " meeting space on Quote package line is TRUE / FALSE (see cases 1 and 2 results). Resource on Quote" +
            " meeting room where Master meeting space is TRUE is the resource selected while creating the Quote" +
            " meeting package. Resource on Quote meeting room where Master meeting space is FALSE is DEFAULT.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-528: Allow resource selection when adding package.")
    public void case3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='AllowResourceSelectionAutoTest3'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='AllowResourceSelectionPackageAutoTest3", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='AllowResourceSelectionAutoTest3'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String resourceID = resource.createResourceSFDX(SFDX, "Name='AllowResourceSelectionAutoTest3'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='AllowResourceSelectionPackageAutoTest3'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1" +
                " thn__Master_meeting_space__c=false", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingFullDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1" +
                " thn__Master_meeting_space__c=true", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='AllowResourceSelectionAutoTest3' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quotePackageId = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Package__c='" + packageID + "' thn__Resource__c='" + resourceID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageId + "' thn__Product__c='" + meetingHalfDayID + "'",
                ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageId + "' thn__Product__c='" + meetingFullDayID + "'",
                ORG_USERNAME);
        String qmrMasterMeetingSpace1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Master_meeting_space__c");
        String qmrResource1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Resource__c");
        String qmrMasterMeetingSpace2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Master_meeting_space__c");
        String qmrResource2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Resource__c");
        Assert.assertEquals(qmrMasterMeetingSpace1, "false");
        Assert.assertNotEquals(qmrResource1, resourceID);
        Assert.assertEquals(qmrMasterMeetingSpace2, "true");
        Assert.assertEquals(qmrResource2, resourceID);
    }

    @Test(priority = 4, description = "User has Overbooking permission. Add Quote meeting package that contains" +
            " Meeting room package line with “Master meeting space” set to TRUE. Leave Resource empty and Save the" +
            " Quote meeting package. Result: Master meeting space on Quote package line is set to TRUE. Master" +
            " meeting space on Quote meeting room is set to TRUE. Resource on Quote meeting room is DEFAULT." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-528: Allow resource selection when adding package.")
    public void case4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='AllowResourceSelectionAutoTest4'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='AllowResourceSelectionPackageAutoTest4", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='DEFAULT' thn__Hotel__c='" +
                propertyID + "'", ORG_USERNAME);
        String resourceDefaultID = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='AllowResourceSelectionPackageAutoTest4'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1" +
                " thn__Master_meeting_space__c=true", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='AllowResourceSelectionAutoTest4' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quotePackageId = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Package__c='" + packageID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageId + "' thn__Product__c='" + meetingHalfDayID + "'",
                ORG_USERNAME);
        String qmrMasterMeetingSpace = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Master_meeting_space__c");
        String qmrResource = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        Assert.assertEquals(qmrMasterMeetingSpace, "true");
        Assert.assertEquals(qmrResource, resourceDefaultID);
    }

    @Test(priority = 5, description = "User has Overbooking permission. Add Quote meeting package that contains" +
            " multiple Meeting room package lines with “Master meeting space” set to TRUE. Select Resource and Save" +
            " the Quote meeting package. Result: Master meeting space on Quote package lines is set to TRUE. Master" +
            " meeting space on Quote meeting rooms is set to TRUE. All meeting rooms are assigned to resource." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-528: Allow resource selection when adding package.")
    public void case5() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='AllowResourceSelectionAutoTest5'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='AllowResourceSelectionPackageAutoTest5", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='AllowResourceSelectionAutoTest4'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String resourceID = resource.createResourceSFDX(SFDX, "Name='AllowResourceSelectionAutoTest4'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='AllowResourceSelectionPackageAutoTest5'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1" +
                " thn__Master_meeting_space__c=true", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingFullDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1" +
                " thn__Master_meeting_space__c=true", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='AllowResourceSelectionAutoTest5' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quotePackageId = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Package__c='" + packageID + "' thn__Resource__c='" + resourceID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageId + "' thn__Product__c='" + meetingHalfDayID + "'",
                ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageId + "' thn__Product__c='" + meetingFullDayID + "'",
                ORG_USERNAME);
        String qmrMasterMeetingSpace1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Master_meeting_space__c");
        String qmrResource1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Resource__c");
        String qmrMasterMeetingSpace2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Master_meeting_space__c");
        String qmrResource2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Resource__c");
        Assert.assertEquals(qmrMasterMeetingSpace1, "true");
        Assert.assertEquals(qmrResource1, resourceID);
        Assert.assertEquals(qmrMasterMeetingSpace2, "true");
        Assert.assertEquals(qmrResource2, resourceID);
    }

}
