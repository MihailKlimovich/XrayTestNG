package tests;

import io.qameta.allure.Description;
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
import pageObject.SfdxCommand;
import utils.Listeners.TestListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Listeners({TestListener.class})

public class MultiEditOnQuotesRelatedList extends BaseTest {

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


    @Test(priority = 1, description = "Preconditions: Creating Packages and Quotes")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-516: Multi edit on quote's related list")
    public void preconditions() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        packages.deletePackageSFDX(SFDX, "Name='MultiEditAutoPackage1", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='MultiEditAutoPackage2", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MultiEditAutoTest1'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MultiEditAutoTest2'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MultiEditAutoTest3'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'",
                ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'",
                ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        String packageID1 = packages.createPackageSFDX(SFDX, "Name='MultiEditAutoPackage1'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID1 +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID1 +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingFullDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID1 + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Diner' thn__Package__c='" + packageID1 + "'" +
                        " thn__Type__c='Food' thn__Product__c='" + productDinerID + "' thn__Start_Time__c=15:00" +
                        " thn__End_Time__c=16:00 thn__Unit_Price__c=30 thn__VAT_Category__c=1 thn__VAT_Category__c=1",
                ORG_USERNAME);
        String packageID2 = packages.createPackageSFDX(SFDX, "Name='MultiEditAutoPackage2'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID2 +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID2 + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID1 = myceQuotes.createQuoteSFDX(SFDX, "Name='MultiEditAutoTest1' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID1 + "'" +
                " thn__Package__c='" + packageID1 + "'", ORG_USERNAME);
        String quoteID2 = myceQuotes.createQuoteSFDX(SFDX, "Name='MultiEditAutoTest2' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID2 + "'" +
                " thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID2 + "'" +
                " thn__Product__c='" + meetingHalfDayID + "'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID2 + "' thn__Product__c='" +
                productDinerID + "'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID2 + "' thn__Product__c='" +
                beverageID + "'", ORG_USERNAME);
        String quoteID3 = myceQuotes.createQuoteSFDX(SFDX, "Name='MultiEditAutoTest3' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID3 + "'" +
                " thn__Package__c='" + packageID2 + "'", ORG_USERNAME);
        quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID3 + "' thn__Product__c='" +
                productDinerID + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID3 + "'" +
                " thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);

    }

    @Test(priority = 2, description = "Quote products are part of the Quote Package")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-516: Multi edit on quote's related list")
    public void case1() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='MultiEditAutoTest1'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' Name='DEFAULT - MEETING HALF DAY'", ORG_USERNAME);
        String quoteMeetingRoomID= JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("MultiEditAutoTest1");
        myceQuotes.openProducts();
        quoteProducts.selectAllItems("2");
        quoteProducts.clickMultiedit();
        multiEditProducts.multiEditProducts_partOfPackage("DEFAULT - MEETING HALF DAY",
                "Yes", "Yes", "Yes");
        StringBuilder quoteProductRecord1 = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +  "' Name='DINER'", ORG_USERNAME);
        StringBuilder quoteProductRecord2 = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +  "' Name='BEVERAGE'", ORG_USERNAME);
        String productMeetingRoom1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Service_Area__c");
        String productMeetingRoom2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Service_Area__c");
        String productCommissionable1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Commissionable__c");
        String productCommissionable2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Commissionable__c");
        String productHideOnOffer1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Hide_on_Offer__c");
        String productHideOnOffer2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Hide_on_Offer__c");
        String productIsOnConsumption1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__On_Consumption__c");
        String productIsOnConsumption2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__On_Consumption__c");
        Assert.assertEquals(productMeetingRoom1, quoteMeetingRoomID);
        Assert.assertEquals(productMeetingRoom2, quoteMeetingRoomID);
        Assert.assertEquals(productCommissionable1, "true");
        Assert.assertEquals(productCommissionable2, "true");
        Assert.assertEquals(productHideOnOffer1, "true");
        Assert.assertEquals(productHideOnOffer2, "true");
        Assert.assertEquals(productIsOnConsumption1, "false");
        Assert.assertEquals(productIsOnConsumption2, "false");
    }

    @Test(priority = 3, description = "Meeting rooms are part of the Quote Package")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-516: Multi edit on quote's related list")
    public void case2() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='MultiEditAutoTest1'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("MultiEditAutoTest1");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItems("2");
        quoteMeetingRoom.clickMultiedit();
        multiEditMeetingRooms.multiEditMeetingRooms_PartOfPackage("Party", "Confirmed",
                "Yes", "Yes", "Yes");
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' Name='DEFAULT - MEETING HALF DAY'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' Name='DEFAULT - MEETING FULL DAY'", ORG_USERNAME);
        String quoteMeetingRoomSetup1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Setup__c");
        String quoteMeetingRoomSetup2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Setup__c");
        String quoteMeetingRoomReservationStatus1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Reservation_Status__c");
        String quoteMeetingRoomReservationStatus2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Reservation_Status__c");
        String quoteMeetingRoomCommissionable1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Commissionable__c");
        String quoteMeetingRoomCommissionable2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Commissionable__c");
        String quoteMeetingRoomHideOnOffer1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Hide_on_Offer__c");
        String quoteMeetingRoomHideOnOffer2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Hide_on_Offer__c");
        String quoteMeetingRoomLockResource1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Lock_Resource__c");
        String quoteMeetingRoomLockResource2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Lock_Resource__c");
        Assert.assertEquals(quoteMeetingRoomSetup1, "Party");
        Assert.assertEquals(quoteMeetingRoomSetup2, "Party");
        Assert.assertEquals(quoteMeetingRoomReservationStatus1, "Confirmed");
        Assert.assertEquals(quoteMeetingRoomReservationStatus2, "Confirmed");
        Assert.assertEquals(quoteMeetingRoomCommissionable1, "true");
        Assert.assertEquals(quoteMeetingRoomCommissionable2, "true");
        Assert.assertEquals(quoteMeetingRoomHideOnOffer1, "true");
        Assert.assertEquals(quoteMeetingRoomHideOnOffer2, "true");
        Assert.assertEquals(quoteMeetingRoomLockResource1, "true");
        Assert.assertEquals(quoteMeetingRoomLockResource2, "true");
    }

    @Test(priority = 4, description = "Quote products are not part of the Quote Package")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-516: Multi edit on quote's related list")
    public void case3() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='MultiEditAutoTest2'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' Name='DEFAULT - MEETING FULL DAY'", ORG_USERNAME);
        String quoteMeetingRoomID= JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("MultiEditAutoTest2");
        myceQuotes.openProducts();
        quoteProducts.selectAllItems("2");
        quoteProducts.clickMultiedit();
        multiEditProducts.multiEditProducts_notPartOfPackage(date.generateTodayDate3_plus(0, 0),
                date.generateTodayDate3_plus(0, 1), "DEFAULT - MEETING FULL DAY",
                "3", "2", "Yes", "Yes", "No",
                "5", "10", "", "");
        StringBuilder quoteProductRecord1 = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +  "' Name='DINER'", ORG_USERNAME);
        StringBuilder quoteProductRecord2 = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +  "' Name='BEVERAGE'", ORG_USERNAME);
        String productMeetingRoom1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Service_Area__c");
        String productMeetingRoom2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Service_Area__c");
        String productCommissionable1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Commissionable__c");
        String productCommissionable2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Commissionable__c");
        String productHideOnOffer1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Hide_on_Offer__c");
        String productHideOnOffer2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Hide_on_Offer__c");
        String productIsOnConsumption1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__On_Consumption__c");
        String productIsOnConsumption2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__On_Consumption__c");
        String productStartDate1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Start_Date__c");
        String productStartDate2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Start_Date__c");
        String productEndDate1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__End_Date__c");
        String productEndDate2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__End_Date__c");
        String productPax1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Pax__c");
        String productPax2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Pax__c");
        String productActualPax1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Actual_Pax__c");
        String productActualPax2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Actual_Pax__c");
        String productCommissionPercentage1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Commission_Percentage__c");
        String productCommissionPercentage2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Commission_Percentage__c");
        String productDiscountPercent1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Discount_Percent__c");
        String productDiscountPercent2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Discount_Percent__c");
        String productDiscountAmount1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Discount__c");
        String productDiscountAmount2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Discount__c");
        String productUnitPrice1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Unit_Price__c");
        String productUnitPrice2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Unit_Price__c");
        Assert.assertEquals(productMeetingRoom1, quoteMeetingRoomID);
        Assert.assertEquals(productMeetingRoom2, quoteMeetingRoomID);
        Assert.assertEquals(productCommissionable1, "true");
        Assert.assertEquals(productCommissionable2, "true");
        Assert.assertEquals(productHideOnOffer1, "true");
        Assert.assertEquals(productHideOnOffer2, "true");
        Assert.assertEquals(productIsOnConsumption1, "false");
        Assert.assertEquals(productIsOnConsumption2, "false");
        Assert.assertEquals(productStartDate1, date.generateTodayDate2_plus(0, 0));
        Assert.assertEquals(productStartDate2, date.generateTodayDate2_plus(0, 0));
        Assert.assertEquals(productEndDate1, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(productEndDate2, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(productPax1, "3");
        Assert.assertEquals(productPax2, "3");
        Assert.assertEquals(productActualPax1, "2");
        Assert.assertEquals(productActualPax2, "2");
        Assert.assertEquals(productCommissionPercentage1, "5");
        Assert.assertEquals(productCommissionPercentage2, "5");
        Assert.assertEquals(productDiscountPercent1, "10");
        Assert.assertEquals(productDiscountPercent2, "10");
        Assert.assertEquals(productDiscountAmount1, "11");
        Assert.assertEquals(productDiscountAmount2, "6");
        Assert.assertEquals(productUnitPrice1, "99");
        Assert.assertEquals(productUnitPrice2, "54");
    }

    @Test(priority = 5, description = "Meeting rooms are not part of the Quote Package")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-516: Multi edit on quote's related list")
    public void case4() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='MultiEditAutoTest2'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("MultiEditAutoTest2");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItems("2");
        quoteMeetingRoom.clickMultiedit();
        multiEditMeetingRooms.multiEditMeetingRooms_notPartOfPackage(date.generateTodayDate3_plus(0, 1),
                date.generateTodayDate3_plus(0, 2), "Classroom", "Confirmed",
                 "3", "2", "Yes", "Yes",
                "Yes", "5", "10", "", "");
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' Name='DEFAULT - MEETING HALF DAY'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' Name='DEFAULT - MEETING FULL DAY'", ORG_USERNAME);
        String quoteMeetingRoomSetup1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Setup__c");
        String quoteMeetingRoomSetup2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Setup__c");
        String quoteMeetingRoomReservationStatus1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Reservation_Status__c");
        String quoteMeetingRoomReservationStatus2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Reservation_Status__c");
        String quoteMeetingRoomCommissionable1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Commissionable__c");
        String quoteMeetingRoomCommissionable2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Commissionable__c");
        String quoteMeetingRoomHideOnOffer1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Hide_on_Offer__c");
        String quoteMeetingRoomHideOnOffer2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Hide_on_Offer__c");
        String quoteMeetingRoomLockResource1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Lock_Resource__c");
        String quoteMeetingRoomLockResource2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Lock_Resource__c");
        String quoteMeetingRoomStartDate1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Start_Date__c");
        String quoteMeetingRoomStartDate2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Start_Date__c");
        String quoteMeetingRoomEndDate1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__End_Date__c");
        String quoteMeetingRoomEndDate2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__End_Date__c");
        String quoteMeetingRoomPax1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Pax__c");
        String quoteMeetingRoomPax2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Pax__c");
        String quoteMeetingRoomActualPax1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Actual_Pax__c");
        String quoteMeetingRoomActualPax2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Actual_Pax__c");
        String quoteMeetingRoomCommissionPercentage1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Commission_Percentage__c");
        String quoteMeetingRoomCommissionPercentage2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Commission_Percentage__c");
        String quoteMeetingRoomDiscountPercent1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Discount_Percent__c");
        String quoteMeetingRoomDiscountPercent2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Discount_Percent__c");
        String quoteMeetingRoomDiscountAmount1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Discount__c");
        String quoteMeetingRoomDiscountAmount2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Discount__c");
        String quoteMeetingRoomUnitPrice1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomUnitPrice2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Unit_Price__c");
        Assert.assertEquals(quoteMeetingRoomSetup1, "Classroom");
        Assert.assertEquals(quoteMeetingRoomSetup2, "Classroom");
        Assert.assertEquals(quoteMeetingRoomReservationStatus1, "Confirmed");
        Assert.assertEquals(quoteMeetingRoomReservationStatus2, "Confirmed");
        Assert.assertEquals(quoteMeetingRoomCommissionable1, "true");
        Assert.assertEquals(quoteMeetingRoomCommissionable2, "true");
        Assert.assertEquals(quoteMeetingRoomHideOnOffer1, "true");
        Assert.assertEquals(quoteMeetingRoomHideOnOffer2, "true");
        Assert.assertEquals(quoteMeetingRoomLockResource1, "true");
        Assert.assertEquals(quoteMeetingRoomLockResource2, "true");
        Assert.assertEquals(quoteMeetingRoomStartDate1, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteMeetingRoomStartDate2, date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(quoteMeetingRoomEndDate1, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(quoteMeetingRoomEndDate2, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(quoteMeetingRoomPax1, "3");
        Assert.assertEquals(quoteMeetingRoomPax2, "3");
        Assert.assertEquals(quoteMeetingRoomActualPax1, "2");
        Assert.assertEquals(quoteMeetingRoomActualPax2, "2");
        Assert.assertEquals(quoteMeetingRoomCommissionPercentage1, "5");
        Assert.assertEquals(quoteMeetingRoomCommissionPercentage2, "5");
        Assert.assertEquals(quoteMeetingRoomDiscountPercent1, "10");
        Assert.assertEquals(quoteMeetingRoomDiscountPercent2, "10");
        Assert.assertEquals(quoteMeetingRoomDiscountAmount1, "24");
        Assert.assertEquals(quoteMeetingRoomDiscountAmount2, "42");
        Assert.assertEquals(quoteMeetingRoomUnitPrice1, "216");
        Assert.assertEquals(quoteMeetingRoomUnitPrice2, "378");
    }

    @Test(priority = 6, description = "Quote products are  partially related to the Quote package")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-516: Multi edit on quote's related list")
    public void case5() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='MultiEditAutoTest3'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' Name='DEFAULT - MEETING HALF DAY'", ORG_USERNAME);
        String quoteMeetingRoomID= JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("MultiEditAutoTest3");
        myceQuotes.openProducts();
        quoteProducts.selectAllItems("2");
        quoteProducts.clickMultiedit();
        multiEditProducts.multiEditProducts_partOfPackage("DEFAULT - MEETING HALF DAY",
                "Yes", "Yes", "Yes");
        StringBuilder quoteProductRecord1 = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +  "' Name='DINER'", ORG_USERNAME);
        StringBuilder quoteProductRecord2 = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +  "' Name='BEVERAGE'", ORG_USERNAME);
        String productMeetingRoom1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Service_Area__c");
        String productMeetingRoom2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Service_Area__c");
        String productCommissionable1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Commissionable__c");
        String productCommissionable2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Commissionable__c");
        String productHideOnOffer1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__Hide_on_Offer__c");
        String productHideOnOffer2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__Hide_on_Offer__c");
        String productIsOnConsumption1= JsonParser2.
                getFieldValue(quoteProductRecord1.toString(), "thn__On_Consumption__c");
        String productIsOnConsumption2= JsonParser2.
                getFieldValue(quoteProductRecord2.toString(), "thn__On_Consumption__c");
        Assert.assertEquals(productMeetingRoom1, quoteMeetingRoomID);
        Assert.assertEquals(productMeetingRoom2, quoteMeetingRoomID);
        Assert.assertEquals(productCommissionable1, "true");
        Assert.assertEquals(productCommissionable2, "true");
        Assert.assertEquals(productHideOnOffer1, "true");
        Assert.assertEquals(productHideOnOffer2, "true");
        Assert.assertEquals(productIsOnConsumption1, "true");
        Assert.assertEquals(productIsOnConsumption2, "false");
    }

    @Test(priority = 7, description = "Meeting rooms are partially related to the Quote package")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-516: Multi edit on quote's related listT")
    public void case6() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='MultiEditAutoTest3'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("MultiEditAutoTest3");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItems("2");
        quoteMeetingRoom.clickMultiedit();
        multiEditMeetingRooms.multiEditMeetingRooms_PartOfPackage("Party", "Confirmed",
                 "Yes", "Yes", "Yes");
        StringBuilder quoteMeetingRoomRecord1 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' Name='DEFAULT - MEETING HALF DAY'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord2 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "' Name='DEFAULT - MEETING FULL DAY'", ORG_USERNAME);
        String quoteMeetingRoomSetup1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Setup__c");
        String quoteMeetingRoomSetup2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Setup__c");
        String quoteMeetingRoomReservationStatus1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Reservation_Status__c");
        String quoteMeetingRoomReservationStatus2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Reservation_Status__c");
        String quoteMeetingRoomFunctionName1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Function_Name__c");
        String quoteMeetingRoomFunctionName2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Function_Name__c");
        String quoteMeetingRoomCommissionable1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Commissionable__c");
        String quoteMeetingRoomCommissionable2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Commissionable__c");
        String quoteMeetingRoomHideOnOffer1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Hide_on_Offer__c");
        String quoteMeetingRoomHideOnOffer2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Hide_on_Offer__c");
        String quoteMeetingRoomLockResource1= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord1.toString(), "thn__Lock_Resource__c");
        String quoteMeetingRoomLockResource2= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord2.toString(), "thn__Lock_Resource__c");
        Assert.assertEquals(quoteMeetingRoomSetup1, "Party");
        Assert.assertEquals(quoteMeetingRoomSetup2, "Party");
        Assert.assertEquals(quoteMeetingRoomReservationStatus1, "Confirmed");
        Assert.assertEquals(quoteMeetingRoomReservationStatus2, "Confirmed");
        Assert.assertEquals(quoteMeetingRoomCommissionable1, "true");
        Assert.assertEquals(quoteMeetingRoomCommissionable2, "true");
        Assert.assertEquals(quoteMeetingRoomHideOnOffer1, "true");
        Assert.assertEquals(quoteMeetingRoomHideOnOffer2, "true");
        Assert.assertEquals(quoteMeetingRoomLockResource1, "true");
        Assert.assertEquals(quoteMeetingRoomLockResource2, "true");
    }
}
