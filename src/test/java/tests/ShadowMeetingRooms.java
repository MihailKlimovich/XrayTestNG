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

public class ShadowMeetingRooms extends BaseTest {

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


    @Test(priority = 1, description = "Create a new myce quote and a meeting room, change the meeting room’s resource" +
            " to one with related groupings. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("Shadow Meeting rooms")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        resource.deleteResourceSFDX(SFDX, "Name='ShadowMR1'", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ShadowMR2'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ShadowMeetingRoomsAutoTest'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='Shadow'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        String resourceId1 = resource.createResourceSFDX(SFDX, "Name='ShadowMR1' thn__Hotel__c='" + propertyID +
                "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceId2 = resource.createResourceSFDX(SFDX, "Name='ShadowMR2' thn__Hotel__c='" + propertyID +
                "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        resourceGrouping.createResourceGroupingSFDX(SFDX, "thn__Grouped_Resource__c='" + resourceId1 + "'" +
                " thn__Resource_Group__c='" + resourceId2 + "'", ORG_USERNAME);
        String shadowQuoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='Shadow' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 25), ORG_USERNAME);
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCode("Metadata.DeployContainer mdContainer = new Metadata.DeployContainer();" + "\n" +
                " Metadata.CustomMetadata cmd = new Metadata.CustomMetadata();" + "\n" +
                " cmd.fullName = 'thn__Default_Agile_Value__mdt.thn__Hotel_Demo';" + "\n" +
                " cmd.label = 'Hotel Demo';" + "\n" +
                " Metadata.CustomMetadataValue customField1 = new Metadata.CustomMetadataValue();" + "\n" +
                " customField1.field = 'thn__Shadow_Quote_Id__c';" + "\n" +
                " customField1.value = '" + shadowQuoteID + "';" + "\n" +
                " cmd.values.add(customField1);" + "\n" +
                " mdContainer.addMetadata(cmd);" + "\n" +
                " Id job = Metadata.Operations.enqueueDeployment(mdContainer, null);" + "\n" +
                " System.debug(job);");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ShadowMeetingRoomsAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("ShadowMeetingRoomsAutoTest");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("1");
        quoteMeetingRoom.clickChangeResource();
        changeResource.changeResource("ShadowMR2");
        StringBuilder shadowRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + shadowQuoteID + "'", ORG_USERNAME);
        String shadowMeetingRoomResource = JsonParser2.
                getFieldValue(shadowRecord.toString(), "thn__Resource__c");
        String shadowCheckbox = JsonParser2.getFieldValue(shadowRecord.toString(), "thn__Shadow__c");
        Assert.assertEquals(shadowMeetingRoomResource, resourceId1);
        Assert.assertEquals(shadowCheckbox, "true");
    }

}
