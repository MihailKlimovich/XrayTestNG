package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import utils.Listeners.TestListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Listeners({TestListener.class})

public class DefaultSetupTesting extends BaseTest {

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


    @Test(priority = 1, description = "Preconditions: Create a new Myce quote, create Resource records with a setup.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-365: Default Setup testing")
    public void preconditions() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME, ADMIN_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='DefaultSetupAutoTesting'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='DefaultSetupAutoTest'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='DefaultSetupAutoTest2'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='DefaultSetupAutoTest3'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='DefaultSetupAutoTest' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room' thn__Default_setup__c='Circle'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='DefaultSetupAutoTest2' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room' thn__Default_setup__c='Cabaret'", ORG_USERNAME);
        String resourceId3 = resource.createResourceSFDX(SFDX, "Name='DefaultSetupAutoTest3' thn__Hotel__c='"
                + propertyID + "' thn__Type__c='Meeting Room' thn__Default_setup__c='Theater'", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='DefaultSetupAutoTesting' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
    }

    @Test(priority = 2, description = "Create a quote meeting room without setup and assign a resource. Expected" +
            " result: meeting room setup is filled with resource’s default setup")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-365: Default Setup testing")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='DefaultSetupAutoTesting'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='DefaultSetupAutoTest'",
                ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String quoteMeetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'" + " thn__Product__c='" + meetingFullDayID + "'",
                ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId + "'", ORG_USERNAME);
        String defaultSetupResource = JsonParser2.
                getFieldValue(resourceRecord.toString(), "thn__Default_setup__c");
        String setupQuoteMeetingRoom = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Setup__c");
        Assert.assertEquals(setupQuoteMeetingRoom, defaultSetupResource);
    }

    @Test(priority = 3, description = "Change the assigned resource on the meeting room. Expected" +
            " result: no change occurs on meeting room’s setup")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-365: Default Setup testing")
    public void case2() throws InterruptedException, IOException {
        StringBuilder resourceRecord1 = resource.
                getResourceSFDX(SFDX, "Name='DefaultSetupAutoTest'", ORG_USERNAME);
        String resourceId1 = JsonParser2.getFieldValue(resourceRecord1.toString(), "Id");
        StringBuilder resourceRecord2 = resource.
                getResourceSFDX(SFDX, "Name='DefaultSetupAutoTest2'", ORG_USERNAME);
        String resourceId2 = JsonParser2.getFieldValue(resourceRecord2.toString(), "Id");
        String defaultSetupResource1 = JsonParser2.
                getFieldValue(resourceRecord1.toString(), "thn__Default_setup__c");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='DefaultSetupAutoTesting'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("DefaultSetupAutoTesting");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("1");
        quoteMeetingRoom.clickChangeResource();
        changeResource.changeResource("DefaultSetupAutoTest2");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        String setupQuoteMeetingRoom = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Setup__c");
        String resourceQuoteMeetingRoom = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        Assert.assertEquals(resourceQuoteMeetingRoom, resourceId2);
        Assert.assertEquals(setupQuoteMeetingRoom, defaultSetupResource1);
    }

    @Test(priority = 4, description = "Create a meeting room with a setup and assign a resource. Expected" +
            " result: no change occurs on meeting room’s setup")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-365: Default Setup testing")
    public void case3() throws InterruptedException, IOException {
        StringBuilder resourceRecord = resource.
                getResourceSFDX(SFDX, "Name='DefaultSetupAutoTest'", ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='DefaultSetupAutoTesting'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.
                getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String quoteMeetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                        + quoteID + "'" + " thn__Product__c='" + meetingHalfDayID + "' thn__Setup__c='Party'" +
                " thn__Resource__c='" + resourceId + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId + "'", ORG_USERNAME);
        String defaultSetupResource = JsonParser2.
                getFieldValue(resourceRecord.toString(), "thn__Default_setup__c");
        String setupQuoteMeetingRoom = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Setup__c");
        String resourceQuoteMeetingRoom = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        Assert.assertEquals(resourceQuoteMeetingRoom, resourceId);
        Assert.assertNotEquals(setupQuoteMeetingRoom, defaultSetupResource);
    }

    @Test(priority = 5, description = "Create a meeting room without setup and without resource. Expected" +
            " result: no change occurs on meeting room’s setup")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-365: Default Setup testing")
    public void case4() throws InterruptedException, IOException {
        StringBuilder meetingHalfDayRecord = product.
                getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.
                getResourceSFDX(SFDX, "Name='DefaultSetupAutoTest3'", ORG_USERNAME);
        String resourceId = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        String defaultSetupResource = JsonParser2.
                getFieldValue(resourceRecord.toString(), "thn__Default_setup__c");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='DefaultSetupAutoTesting'",
                ORG_USERNAME);
        String quoteID = JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteMeetingRoomId = quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'" + " thn__Product__c='" + meetingHalfDayID + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("DefaultSetupAutoTesting");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("3");
        quoteMeetingRoom.clickChangeResource();
        changeResource.changeResource("DefaultSetupAutoTest3");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId + "'", ORG_USERNAME);
        String setupQuoteMeetingRoom = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Setup__c");
        String resourceQuoteMeetingRoom = JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        Assert.assertEquals(resourceQuoteMeetingRoom, resourceId);
        Assert.assertEquals(setupQuoteMeetingRoom, defaultSetupResource);
    }

}
