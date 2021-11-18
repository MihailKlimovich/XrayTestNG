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
import java.util.concurrent.TimeUnit;

public class OverbookingAndPriceUpdate extends BaseTest {

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
        //options.addArguments("user-data-dir=/tmp/temp_profile");
        options.addArguments(" --whitelisted-ips=\"\"");
        //options.addArguments("--headless", "window-size=1920,1024", "--no-sandbox");
        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    /*@AfterClass
    public void teardown() {
        driver.close();
        driver.quit();
        if (driver != null) {
            driver = null;
        }
    }*/

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Overbooking & price update")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
    }

    @Test(priority = 2, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("Overbooking & price update")
    public void preconditions() throws InterruptedException, IOException {
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OverbookingChangePriceAutoTest'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest1", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest2", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest3", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest4", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest5", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String resourceID1 = resource.createResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest1'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceID2 = resource.createResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest2'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceID3 = resource.createResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest3'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceID4 = resource.createResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest4'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room' thn__is_Shareable__c=true", ORG_USERNAME);
        String resourceID5 = resource.createResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest5'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room' thn__is_Shareable__c=true" +
                " thn__Break_out_half_day__c=60 thn__Break_out_full_day__c=100", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceID2 + "'" +
                " thn__Resource_Group__c='" + resourceID3 + "'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OverbookingChangePriceAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
    }

    @Test(priority = 3, description = "Create a quote meeting room. Click on change resource to assign a different" +
            " one from the default. Expected result: No Warning (resource isn’t booked yet")
    @Severity(SeverityLevel.NORMAL)
    @Story("Overbooking & price update")
    public void case1() throws InterruptedException, IOException {
        String expectedMessage = "Message not found";
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OverbookingChangePriceAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String quoteMeetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + myceQuoteID + "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        StringBuilder resourceRecord1 = resource.
                getResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest1'", ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecord1.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("OverbookingChangePriceAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("1");
        quoteMeetingRoom.clickChangeResource();
        String message = changeResource.changeResource("OverbookingChangePriceAutoTest1");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId + "'", ORG_USERNAME);
        String quoteMeetingRoomResource = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        Assert.assertEquals(quoteMeetingRoomResource, resourceId);
        System.out.println(message);
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 4, description = "Create a new quote meeting room and then assign the same resource as the" +
            " previous record. Expected result: overbooking message asking for confirmation if you have the permission" +
            " to overbook.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Overbooking & price update")
    public void case2() throws InterruptedException, IOException {
        String expectedMessage = "Do you wish to overbook?";
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OverbookingChangePriceAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String quoteMeetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID + "'", ORG_USERNAME);
        StringBuilder resourceRecord1 = resource.
                getResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest1'", ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecord1.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("OverbookingChangePriceAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("2");
        quoteMeetingRoom.clickChangeResource();
        String message = changeResource.changeResource("OverbookingChangePriceAutoTest1");
        System.out.println(message);
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 5, description = "Update Quote meeting room with conditions: Resource != default resource and" +
            " assigned resource has groupings. Expected result: No overbooking found, changes are saved.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Overbooking & price update")
    public void case3() throws InterruptedException, IOException {
        String expectedMessage = "Message not found";
        StringBuilder resourceRecord1 = resource.
                getResourceSFDX(SFDX, "Name='OverbookingChangePriceAutoTest2'", ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecord1.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OverbookingChangePriceAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("OverbookingChangePriceAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("2");
        quoteMeetingRoom.clickChangeResource();
        String message = changeResource.changeResource("OverbookingChangePriceAutoTest2");
        System.out.println(message);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID + "'",
                ORG_USERNAME);
        String quoteMeetingRoom2Resource = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        Assert.assertEquals(message, expectedMessage);
        Assert.assertEquals(quoteMeetingRoom2Resource, resourceId);
    }

    @Test(priority = 6, description = "Create new meeting room then update: Resource = resource where is" +
            " shareable = true. Expected result: No Warning.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Overbooking & price update")
    public void case4() throws InterruptedException, IOException {
        String expectedMessage = "Message not found";
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OverbookingChangePriceAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteMeetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + myceQuoteID + "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("OverbookingChangePriceAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("3");
        quoteMeetingRoom.clickChangeResource();
        String message = changeResource.changeResource("OverbookingChangePriceAutoTest4");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 7, description = "Create new meeting room then update: Resource = one with shadow from case 3." +
            " Expected result: Warning for overbooking.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Overbooking & price update")
    public void case5() throws InterruptedException, IOException {
        String expectedMessage = "Do you wish to overbook?";
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OverbookingChangePriceAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteMeetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("OverbookingChangePriceAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("4");
        quoteMeetingRoom.clickChangeResource();
        String message = changeResource.changeResource("OverbookingChangePriceAutoTest3");
        Assert.assertEquals(message, expectedMessage);
    }

    @Test(priority = 8, description = "Update meeting room and change resource / start and/or end date/times with" +
            " expected overbooking. Cancel modifications. Expected result: No changes occur on meeting room.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Overbooking & price update")
    public void case6() throws InterruptedException, IOException {
        String expectedMessage = "Do you wish to overbook?";
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OverbookingChangePriceAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteMeetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + myceQuoteID + "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId + "'", ORG_USERNAME);
        String quoteMeetingRoomResource = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        String quoteMeetingRoomStartDate = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Date__c");
        String quoteMeetingRoomEndDate = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Date__c");
        String quoteMeetingRoomStartTime = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Start_Time__c");
        String quoteMeetingRoomEndTime = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__End_Time__c");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("OverbookingChangePriceAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("5");
        quoteMeetingRoom.clickChangeResource();
        String message = changeResource.changeResourceAndDateTime("OverbookingChangePriceAutoTest3",
                date.generateTodayDate2(), "15:00", date.generateTodayDate2(), "15:30", "No" );
        StringBuilder updatedQuoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId + "'", ORG_USERNAME);
        String updatedQuoteMeetingRoomResource = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__Resource__c");
        String updatedQuoteMeetingRoomStartDate = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__Start_Date__c");
        String updatedQuoteMeetingRoomEndDate = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__End_Date__c");
        String updatedQuoteMeetingRoomStartTime = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__Start_Time__c");
        String updatedQuoteMeetingRoomEndTime = JsonParser2.
                getFieldValue(updatedQuoteMeetingRoomRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(message, expectedMessage);
        Assert.assertEquals(updatedQuoteMeetingRoomResource, quoteMeetingRoomResource);
        Assert.assertEquals(updatedQuoteMeetingRoomStartDate, quoteMeetingRoomStartDate);
        Assert.assertEquals(updatedQuoteMeetingRoomEndDate, quoteMeetingRoomEndDate);
        Assert.assertEquals(updatedQuoteMeetingRoomStartTime, quoteMeetingRoomStartTime);
        Assert.assertEquals(updatedQuoteMeetingRoomEndTime, quoteMeetingRoomEndTime);
    }

    @Test(priority = 9, description = "Update meeting room’s resource and check ‘update prices’. Expected result:" +
            " Prices are updated based on new resource’s pricing and value on meeting room (break out/half day)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Overbooking & price update")
    public void case7() throws InterruptedException, IOException {
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='OverbookingChangePriceAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteMeetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + myceQuoteID + "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("OverbookingChangePriceAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("6");
        quoteMeetingRoom.clickChangeResource();


    }

}
