package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ResourceGroupingTesting extends BaseTest {

    @BeforeClass
    public void classLevelSetup() {
        ChromeOptions options= new ChromeOptions();
        options.addArguments("--disable-cache");
        options.addArguments("--disk-cache-size=1");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-data-dir=/tmp/temp_profile");
        options.addArguments(" --whitelisted-ips=\"\"");
        options.addArguments("--headless", "window-size=1920,1024", "--no-sandbox");
        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @AfterClass
    public void teardown() {
        driver.close();
        driver.quit();
        if (driver != null) {
            driver = null;
        }
    }


    @Test(priority = 1, description = "Clone MYCE Quote that has Meeting room with Resource Grouping. User has" +
            " Overbooking permission. Result: New Myce Quote with Quote meeting room with shadows is created. New" +
            " meeting room has Waiting list and Overbooking level 2. On Meeting room that was cloned Waiting list" +
            " and overbooking level = 1")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME, ADMIN_USERNAME);
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
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
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
        myceQuotes.cloneMyceQuote("CloneResourceGroupingAutoTest1",
                date.generateTodayDate3_plus(0, 0));
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
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 1);
        Assert.assertEquals(overbookingLevelOriginalQMR, "1");
        Assert.assertEquals(waitingListOriginalQMR, "1");
        Assert.assertEquals(overbookingLevelClonedQMR, "2");
        Assert.assertEquals(waitingListClonedQMR, "2");
    }

    @Test(priority = 2, description = "Clone MYCE Quote that has Meeting room with Resource Grouping. User hasn’t" +
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
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
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
        myceQuotes.cloneMyceQuote("CloneResourceGroupingAutoTest2",
                date.generateTodayDate3_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest2'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder clonedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME );
        String resourceClonedQMR = JsonParser2.
                getFieldValue(clonedQuoteMeetingRoomRecord.toString(), "thn__Resource__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
        Assert.assertEquals(resourceClonedQMR, defaultResourceID);

    }

    @Test(priority = 3, description = "Clone MYCE Quote that has Meeting room with Default Resource. Result: New" +
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
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest3' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + defaultResourceID + "'",
                ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest3");
        myceQuotes.cloneMyceQuote("CloneResourceGroupingAutoTest3",
                date.generateTodayDate3_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest3'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder clonedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME );
        String resourceClonedQMR = JsonParser2.
                getFieldValue(clonedQuoteMeetingRoomRecord.toString(), "thn__Resource__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
        Assert.assertEquals(resourceClonedQMR, defaultResourceID);
    }

    @Test(priority = 4, description = "Clone MYCE Quote that has Meeting room with Shareable resource. Result:" +
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
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest4' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest4");
        myceQuotes.cloneMyceQuote("CloneResourceGroupingAutoTest4",
                date.generateTodayDate3_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest4'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder clonedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME );
        String resourceClonedQMR = JsonParser2.
                getFieldValue(clonedQuoteMeetingRoomRecord.toString(), "thn__Resource__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
        Assert.assertEquals(resourceClonedQMR, resourceId1);
    }

    @Test(priority = 5, description = "Clone MYCE Quote in status Closed Lost that has Meeting room with" +
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
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
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
        myceQuotes.cloneMyceQuote("CloneResourceGroupingAutoTest5",
                date.generateTodayDate3_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneResourceGroupingAutoTest5'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder clonedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + clonedQuoteID + "'", ORG_USERNAME );
        String resourceClonedQMR = JsonParser2.
                getFieldValue(clonedQuoteMeetingRoomRecord.toString(), "thn__Resource__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
        Assert.assertEquals(resourceClonedQMR, resourceId1);
    }

    @Test(priority = 6, description = "Clone Quote meeting room with Resource Grouping. Result: New Quote meeting" +
            " room with shadows is created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case6() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME, ADMIN_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest6'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping8'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping9'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
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
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(quoteMeetingRoomsID.size(), 2);
        Assert.assertEquals(shadowMeetingRoomsID.size(), 2);
    }

    @Test(priority = 7, description = "Clone Quote meeting room with Default Resource. Result: New Quote meeting" +
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
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest7' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + defaultResourceID + "'",
                ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest7");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0, 0), "Quote Meetings Room");
        StringBuilder quoteMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoom.toString(), "Id");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(quoteMeetingRoomsID.size(), 2);
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
    }

    @Test(priority = 8, description = "Clone Meeting room with Shareable resource. Result: New Quote meeting room" +
            " with shareable resource without shadows and overbooking is created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case8() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest8'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping10'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping10' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room' thn__is_Shareable__c=true", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest8' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest8");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0, 0), "Quote Meetings Room");
        StringBuilder quoteMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoom.toString(), "Id");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(quoteMeetingRoomsID.size(), 2);
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
    }

    @Test(priority = 9, description = "Clone Meeting room with Group Resource on Quote in status Closed Lost." +
            " Result: New Quote meeting room without shadows and overbooking is created.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case9() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest9'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping11'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping12'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping11' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping12' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest9' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Lost'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest9");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0, 0), "Quote Meetings Room");
        StringBuilder quoteMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoom.toString(), "Id");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(quoteMeetingRoomsID.size(), 2);
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
    }

    @Test(priority = 10, description = "Create Myce Quote. Change Quote’s status to Closed Lost. Add Quote Meeting" +
            " rooms. Change Resource on added rooms (with the flow). Result: Shadows are not created, rooms are" +
            " not overbooked.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case10() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest10'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping13'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping13' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest10' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Lost'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest10");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("2");
        quoteMeetingRoom.clickChangeResource();
        changeResource.changeResource("ResourceGrouping13");
        StringBuilder quoteMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> quoteMeetingRoomsID = JsonParser2.getFieldValueSoql(quoteMeetingRoom.toString(), "Id");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(quoteMeetingRoomsID.size(), 2);
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
    }

    @Test(priority = 11, description = "Change Quote’s status to Closed Lost. Quote has Meeting rooms with shadows" +
            " and overbooking. Result: All Shadows are deleted, Overbooking level and Waiting list are cleared" +
            " (no Overbooking)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case11() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest11'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping14'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping15'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping14' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping15' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest11' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quoteMeetingRoomId1 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'",
                ORG_USERNAME);
        String quoteMeetingRoomId2 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest11");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("2");
        quoteMeetingRoom.clickChangeResource();
        changeResource.changeResource("ResourceGrouping14");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId1 + "'", ORG_USERNAME );
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId2 + "'", ORG_USERNAME );
        Assert.assertEquals(shadowMeetingRoomsID.size(), 2);
        String overbookingLevelQMR1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Overbooking_Level__c");
        String waitingListQMR1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Waiting_List__c");
        String overbookingLevelQMR2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Overbooking_Level__c");
        String waitingListQMR2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Waiting_List__c");
        Assert.assertEquals(overbookingLevelQMR1, "1");
        Assert.assertEquals(waitingListQMR1, "1");
        Assert.assertEquals(overbookingLevelQMR2, "2");
        Assert.assertEquals(waitingListQMR2, "2");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Lost'", ORG_USERNAME);
        StringBuilder shadowMeetingRooms2 = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> shadowMeetingRoomsID2 = JsonParser2.getFieldValueSoql(shadowMeetingRooms2.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID2.size(), 0);
        StringBuilder updatedQuoteMeetingRoomRecord1 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId1 + "'", ORG_USERNAME );
        StringBuilder updatedQuoteMeetingRoomRecord2 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId2 + "'", ORG_USERNAME );
        String updatedOverbookingLevelQMR1 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord1.toString(), "thn__Overbooking_Level__c");
        String updatedWaitingListQMR1 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord1.toString(), "thn__Waiting_List__c");
        String updatedOverbookingLevelQMR2 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord2.toString(), "thn__Overbooking_Level__c");
        String updatedWaitingListQMR2 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord2.toString(), "thn__Waiting_List__c");
        Assert.assertEquals(updatedOverbookingLevelQMR1, null);
        Assert.assertEquals(updatedWaitingListQMR1, null);
        Assert.assertEquals(updatedOverbookingLevelQMR2, null);
        Assert.assertEquals(updatedWaitingListQMR2, null);
    }

    @Test(priority = 12, description = "Change Quote’s status to Closed Won from Closed Lost. User has Overbooking" +
            " permission. Result: Shadows are created, rooms are overbooked (Overbooking level and Waiting list" +
            " is specified)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case12() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest12'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping16'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping17'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping16' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping17' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest12' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quoteMeetingRoomId1 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'",
                ORG_USERNAME);
        String quoteMeetingRoomId2 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("ResourceGroupingAutoTest12");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("2");
        quoteMeetingRoom.clickChangeResource();
        changeResource.changeResource("ResourceGrouping16");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId1 + "'", ORG_USERNAME );
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId2 + "'", ORG_USERNAME );
        Assert.assertEquals(shadowMeetingRoomsID.size(), 2);
        String overbookingLevelQMR1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Overbooking_Level__c");
        String waitingListQMR1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Waiting_List__c");
        String overbookingLevelQMR2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Overbooking_Level__c");
        String waitingListQMR2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Waiting_List__c");
        Assert.assertEquals(overbookingLevelQMR1, "1");
        Assert.assertEquals(waitingListQMR1, "1");
        Assert.assertEquals(overbookingLevelQMR2, "2");
        Assert.assertEquals(waitingListQMR2, "2");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Lost'", ORG_USERNAME);
        StringBuilder shadowMeetingRooms2 = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> shadowMeetingRoomsID2 = JsonParser2.getFieldValueSoql(shadowMeetingRooms2.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID2.size(), 0);
        StringBuilder updatedQuoteMeetingRoomRecord1 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId1 + "'", ORG_USERNAME );
        StringBuilder updatedQuoteMeetingRoomRecord2 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId2 + "'", ORG_USERNAME );
        String updatedOverbookingLevelQMR1 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord1.toString(), "thn__Overbooking_Level__c");
        String updatedWaitingListQMR1 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord1.toString(), "thn__Waiting_List__c");
        String updatedOverbookingLevelQMR2 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord2.toString(), "thn__Overbooking_Level__c");
        String updatedWaitingListQMR2 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord2.toString(), "thn__Waiting_List__c");
        Assert.assertEquals(updatedOverbookingLevelQMR1, null);
        Assert.assertEquals(updatedWaitingListQMR1, null);
        Assert.assertEquals(updatedOverbookingLevelQMR2, null);
        Assert.assertEquals(updatedWaitingListQMR2, null);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        StringBuilder shadowMeetingRooms3 = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> shadowMeetingRoomsID3 = JsonParser2.getFieldValueSoql(shadowMeetingRooms3.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID3.size(), 2);
        StringBuilder updatedQuoteMeetingRoomRecord11 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId1 + "'", ORG_USERNAME );
        StringBuilder updatedQuoteMeetingRoomRecord22 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId2 + "'", ORG_USERNAME );
        String updatedOverbookingLevelQMR11 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord11.toString(), "thn__Overbooking_Level__c");
        String updatedWaitingListQMR11 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord11.toString(), "thn__Waiting_List__c");
        String updatedOverbookingLevelQMR22 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord22.toString(), "thn__Overbooking_Level__c");
        String updatedWaitingListQMR22 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord22.toString(), "thn__Waiting_List__c");
        Assert.assertEquals(updatedOverbookingLevelQMR11, "1");
        Assert.assertEquals(updatedWaitingListQMR11, "1");
        Assert.assertEquals(updatedOverbookingLevelQMR22, "2");
        Assert.assertEquals(updatedWaitingListQMR22, "2");
    }

    @Test(priority = 13, description = "Change Quote’s status to Closed Won from Closed Lost. User doesn’t have" +
            " Overbooking permission. Result: Meeting rooms' Resource is changed to Default. Shadows are not" +
            " created, rooms are not overbooked.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case13() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/RemoveOverbookingPermissionSet");
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest13'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping18'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping19'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping18' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping19' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        StringBuilder defaultResourceRecord = resource.getResourceSFDX(SFDX, "Name='DEFAULT'", ORG_USERNAME);
        String defaultResourceID = JsonParser2.getFieldValue(defaultResourceRecord.toString(), "Id");
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest13' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quoteMeetingRoomId1 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 + "'",
                ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Lost'", ORG_USERNAME);
        String quoteMeetingRoomId2 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                        + quoteID + "' thn__Product__c='" + meetingFullDayID + "' thn__Resource__c='" + resourceId1 +
                "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        StringBuilder updatedQuoteMeetingRoomRecord1 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId1 + "'", ORG_USERNAME );
        StringBuilder updatedQuoteMeetingRoomRecord2 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId2 + "'", ORG_USERNAME );
        String resourceQMR1 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord1.toString(), "thn__Resource__c");
        String resourceQMR2 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord2.toString(), "thn__Resource__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 0);
        Assert.assertEquals(resourceQMR1,defaultResourceID);
        Assert.assertEquals(resourceQMR2, defaultResourceID);
    }

    @Test(priority = 14, description = "Delete Meeting room in case there are multiple overbooked rooms and Shadows." +
            " Result: Shadows linked to deleted room are deleted. Waiting list on third room is changed from 3 to 2.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Resource grouping")
    public void case14() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME, ADMIN_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest14'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping20'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceGrouping21'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping20' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ResourceGrouping21' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ResourceGroupingAutoTest14' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quoteMeetingRoomId1 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                        + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 +
                "'", ORG_USERNAME);
        String quoteMeetingRoomId2 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                        + quoteID + "' thn__Product__c='" + meetingHalfDayID + "' thn__Resource__c='" + resourceId1 +
                "'", ORG_USERNAME);
        String quoteMeetingRoomId3 = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                        + quoteID + "' thn__Product__c='" + meetingFullDayID + "' thn__Resource__c='" + resourceId1 +
                "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId1 + "'", ORG_USERNAME );
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId2 + "'", ORG_USERNAME );
        StringBuilder quoteMeetingRoomRecord3 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId3 + "'", ORG_USERNAME );
        String overbookingLevelQMR1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Overbooking_Level__c");
        String waitingListQMR1 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Waiting_List__c");
        String overbookingLevelQMR2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Overbooking_Level__c");
        String waitingListQMR2 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Waiting_List__c");
        String overbookingLevelQMR3 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord3.toString(), "thn__Overbooking_Level__c");
        String waitingListQMR3 = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord3.toString(), "thn__Waiting_List__c");
        StringBuilder shadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c" +
                " WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> shadowMeetingRoomsID = JsonParser2.getFieldValueSoql(shadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(overbookingLevelQMR1, "1");
        Assert.assertEquals(waitingListQMR1, "1");
        Assert.assertEquals(overbookingLevelQMR2, "2");
        Assert.assertEquals(waitingListQMR2, "2");
        Assert.assertEquals(overbookingLevelQMR3, "3");
        Assert.assertEquals(waitingListQMR3, "3");
        Assert.assertEquals(shadowMeetingRoomsID.size(), 3);
        quoteMeetingRoom.deleteQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId2 + "'", ORG_USERNAME);
        StringBuilder updatedQuoteMeetingRoomRecord1 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId1 + "'", ORG_USERNAME );
        StringBuilder updatedQuoteMeetingRoomRecord3 = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId3 + "'", ORG_USERNAME );
        String updatedOverbookingLevelQMR1 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord1.toString(), "thn__Overbooking_Level__c");
        String updatedWaitingListQMR1 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord1.toString(), "thn__Waiting_List__c");
        String updatedOverbookingLevelQMR3 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord3.toString(), "thn__Overbooking_Level__c");
        String updatedWaitingListQMR3 = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord3.toString(), "thn__Waiting_List__c");
        StringBuilder updatedShadowMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Meeting_Room__c WHERE thn__ShadowRoomQuote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> updatedShadowMeetingRoomsID = JsonParser2.
                getFieldValueSoql(updatedShadowMeetingRooms.toString(), "Id");
        Assert.assertEquals(updatedOverbookingLevelQMR1, "1");
        Assert.assertEquals(updatedWaitingListQMR1, "1");
        Assert.assertEquals(updatedOverbookingLevelQMR3, "3");
        Assert.assertEquals(updatedWaitingListQMR3, "2");
        Assert.assertEquals(updatedShadowMeetingRoomsID.size(), 2);
    }


}
