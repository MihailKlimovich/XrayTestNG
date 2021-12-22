package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class ResourceGroupingTesting extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
    }

    @Test(priority = 2, description = "Clone MYCE Quote that has Meeting room with Resource Grouping. User has" +
            " Overbooking permission. Result: New Myce Quote with Quote meeting room with shadows is created. New" +
            " meeting room has Waiting list and Overbooking level 2. On Meeting room that was cloned Waiting list" +
            " and overbooking level = 1")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case1() throws InterruptedException, IOException {
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest1'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest1'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping1'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping2'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping1' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping2' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest1' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest1");
        myceQuotes.cloneMyceQuote("CloneResourceGroupingAutoTest1", date.generateTodayDate3_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest1'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME );
        StringBuilder clonedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME );
        String overbookingLevelOriginalQMR = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Overbooking_Level__c");
        String waitingListOriginalQMR = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Waiting_List__c");
        String overbookingLevelClonedQMR = JsonParser2.
                getFieldValue(clonedQuoteMeetingRoomRecord.toString(), "thn__Overbooking_Level__c");
        String waitingListClonedQMR = JsonParser2.
                getFieldValue(clonedQuoteMeetingRoomRecord.toString(), "thn__Waiting_List__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 1);
        Assert.assertEquals(overbookingLevelOriginalQMR, "1");
        Assert.assertEquals(waitingListOriginalQMR, "1");
        Assert.assertEquals(overbookingLevelClonedQMR, "2");
        Assert.assertEquals(waitingListClonedQMR, "2");
    }

    @Test(priority = 3, description = "Clone MYCE Quote that has Meeting room with Resource Grouping. User hasn’t" +
            " Overbooking permission. Result: New Myce Quote with Quote meeting room with Default resource is" +
            " created. The Meeting room that was cloned is not changed. Shadows are not created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case2() throws InterruptedException, IOException {
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/RemoveOverbookingPermissionSet");
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest2'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest2'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping3'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping4'", ORG_USERNAME);
        StringBuilder defaultResourceRecord = resource.getResourceSFDX(SFDX, "Name='DEFAULT'", ORG_USERNAME);
        String defaultResourceID = JsonParser2.getFieldValue(defaultResourceRecord.toString(), "Id");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping3' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping4' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest2' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest2");
        myceQuotes.cloneMyceQuote("CloneResourceGroupingAutoTest2", date.generateTodayDate3_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest2'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder clonedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME );
        String resourceClonedQMR = JsonParser2.
                getFieldValue(clonedQuoteMeetingRoomRecord.toString(), "thn__Resource__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
        Assert.assertEquals(resourceClonedQMR, defaultResourceID);

    }

    @Test(priority = 4, description = "Clone MYCE Quote that has Meeting room with Default Resource. Result: New" +
            " Myce Quote with Quote meeting room without shadows and overbooking is created")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest3'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest3'", ORG_USERNAME);
        StringBuilder defaultResourceRecord = resource.getResourceSFDX(SFDX, "Name='DEFAULT'", ORG_USERNAME);
        String defaultResourceID = JsonParser2.getFieldValue(defaultResourceRecord.toString(), "Id");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest3' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + defaultResourceID + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest3");
        myceQuotes.cloneMyceQuote("CloneResourceGroupingAutoTest3", date.generateTodayDate3_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest3'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder clonedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME );
        String resourceClonedQMR = JsonParser2.
                getFieldValue(clonedQuoteMeetingRoomRecord.toString(), "thn__Resource__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
        Assert.assertEquals(resourceClonedQMR, defaultResourceID);
    }

    @Test(priority = 5, description = "Clone MYCE Quote that has Meeting room with Shareable resource. Result:" +
            " New Myce Quote with Quote meeting room with shareable resource without shadows and overbooking" +
            " is created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest4'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest4'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping5'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping5' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room' thn__is_Shareable__c=true", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest4' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest4");
        myceQuotes.cloneMyceQuote("CloneResourceGroupingAutoTest4", date.generateTodayDate3_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest4'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder clonedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME );
        String resourceClonedQMR = JsonParser2.
                getFieldValue(clonedQuoteMeetingRoomRecord.toString(), "thn__Resource__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
        Assert.assertEquals(resourceClonedQMR, resourceId1);
    }

    @Test(priority = 6, description = "Clone MYCE Quote in status Closed Lost that has Meeting room with" +
            " Group Resource. Result: New Myce Quote with Quote meeting room without shadows and overbooking" +
            " is created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case5() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest5'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest5'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping6'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping7'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping6' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping7' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest5' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Lost'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest5");
        myceQuotes.cloneMyceQuote("CloneResourceGroupingAutoTest5", date.generateTodayDate3_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest5'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder clonedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME );
        String resourceClonedQMR = JsonParser2.
                getFieldValue(clonedQuoteMeetingRoomRecord.toString(), "thn__Resource__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
        Assert.assertEquals(resourceClonedQMR, resourceId1);
    }

    @Test(priority = 7, description = "Clone Quote meeting room with Resource Grouping. Result: New Quote meeting" +
            " room with shadows is created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case6() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest6'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping8'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping9'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping8' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping9' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest6' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest6");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0, 0), "Quote Meetings Room");
        StringBuilder quoteMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoom.toString(), "Id");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(quoteMeetingRoomsID.size(), 2);
        Assert.assertEquals(shadowMeetingRoomsID.size(), 2);
    }

    @Test(priority = 8, description = "Clone Quote meeting room with Default Resource. Result: New Quote meeting" +
            " room without shadows and overbooking is created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case7() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest7'", ORG_USERNAME);
        StringBuilder defaultResourceRecord = resource.getResourceSFDX(SFDX, "Name='DEFAULT'", ORG_USERNAME);
        String defaultResourceID = JsonParser2.getFieldValue(defaultResourceRecord.toString(), "Id");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest7' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + defaultResourceID + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest7");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0, 0), "Quote Meetings Room");
        StringBuilder quoteMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoom.toString(), "Id");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(quoteMeetingRoomsID.size(), 2);
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
    }

    @Test(priority = 9, description = "Clone Meeting room with Shareable resource. Result: New Quote meeting room" +
            " with shareable resource without shadows and overbooking is created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case8() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);

    }

}
