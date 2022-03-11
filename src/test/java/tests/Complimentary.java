package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
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

public class Complimentary extends BaseTest {

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

    @Test(priority = 1, description = "Create MYCE Quote, Add Quote hotel rooms, Quote meeting rooms, Quote prodycts" +
            " and Quote meeting packages, where unit price != 0. Set Complimentary on MYCE Quote to TRUE. Expected" +
            " result:Unit price of all related records and prices on the Quote are set to 0. Complimentary" +
            " checkboxes on related records not linked to the Quote package are set to TRUE. Complimentary" +
            " checkboxes on Quote packages are set to true. Discount on all related records is set to 100%.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-540: Myce Quote - Complimentary")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary1'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='MyceQuoteComplimentaryPackage1", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String winesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary1' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='MyceQuoteComplimentaryPackage1'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1", ORG_USERNAME);
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + winesID + "' thn__Unit_Price__c=100 thn__Discount_Percent__c=0", ORG_USERNAME);
        String quoteMeetingRoomId =quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'" + " thn__Product__c='" + meetingHalfDayID + "' thn__Unit_Price__c=100", ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Unit_Price__c=100", ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'" + " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Complimentary__c=true", ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'",ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId + "'", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID + "'", ORG_USERNAME);
        StringBuilder quoteProductFromPackageRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        String quotePackageComplimentary= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Complimentary__c");
        String quotePackageUnitPrice= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price__c");
        String quotePackageDiscount= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Discount__c");
        String quoteProductComplimentary= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Complimentary__c");
        String quoteProductUnitPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String quoteProductDiscount= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        String quoteMeetingRoomComplimentary= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Complimentary__c");
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomDiscount= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteHotelRoomComplimentary= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Complimentary__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomDiscount= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteProductPackageComplimentary= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Complimentary__c");
        String quoteProductPackageUnitPrice= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Unit_Price__c");
        String quoteProductPackageDiscount= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Discount_Percent__c");
        Assert.assertEquals(quotePackageComplimentary, "true");
        Assert.assertEquals(quoteProductComplimentary, "true");
        Assert.assertEquals(quoteMeetingRoomComplimentary, "true");
        Assert.assertEquals(quoteHotelRoomComplimentary, "true");
        Assert.assertEquals(quoteProductPackageComplimentary, "true");
        Assert.assertEquals(quotePackageUnitPrice, "0");
        Assert.assertEquals(quoteProductUnitPrice, "0");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "0");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "0");
        Assert.assertEquals(quoteProductPackageUnitPrice, "0");
        Assert.assertEquals(quotePackageDiscount, "100");
        Assert.assertEquals(quoteProductDiscount, "100");
        Assert.assertEquals(quoteMeetingRoomDiscount, "100");
        Assert.assertEquals(quoteHotelRoomDiscount, "100");
        Assert.assertEquals(quoteProductPackageDiscount, "100");
    }

    @Test(priority = 2, description = "On created Quote set Complimentary to FALSE back. Expected result: Unit price" +
            " on all related records becomes equal to list price of each record. Prices on MYCE Quote record are" +
            " recalculated. Complimentary checkboxes on all related records are set to FALSE. Discount on all related" +
            " records is set to 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-540: Myce Quote - Complimentary")
    public void case2() throws InterruptedException, IOException {
        myceQuotes.updateQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary1'", "thn__Complimentary__c=false",
                ORG_USERNAME);
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String winesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary1'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String myceQuoteTotalAmmountInclTax= JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        StringBuilder quotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'",ORG_USERNAME);
        String quotePackageID= JsonParser2.getFieldValue(quotePackageRecord.toString(), "Id");
        StringBuilder quoteProductRecord = quoteProducts.getQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='"
                + myceQuoteID + "' thn__Product__c='" + winesID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'", ORG_USERNAME);
        StringBuilder quoteProductFromPackageRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        String quotePackageComplimentary= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Complimentary__c");
        String quotePackageUnitPrice= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price__c");
        String quotePackageDiscount= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Discount__c");
        String quoteProductComplimentary= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Complimentary__c");
        String quoteProductUnitPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String quoteProductDiscount= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Discount_Percent__c");
        String quoteMeetingRoomComplimentary= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Complimentary__c");
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomDiscount= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteHotelRoomComplimentary= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Complimentary__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomDiscount= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Discount_Percent__c");
        String quoteProductPackageComplimentary= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Complimentary__c");
        String quoteProductPackageUnitPrice= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Unit_Price__c");
        String quoteProductPackageDiscount= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Discount_Percent__c");
        Assert.assertEquals(quotePackageComplimentary, "false");
        Assert.assertEquals(quoteProductComplimentary, "false");
        Assert.assertEquals(quoteMeetingRoomComplimentary, "false");
        Assert.assertEquals(quoteHotelRoomComplimentary, "false");
        Assert.assertEquals(quoteProductPackageComplimentary, "false");
        Assert.assertEquals(quotePackageUnitPrice, "100");
        Assert.assertEquals(quoteProductUnitPrice, "100");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "100");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "100");
        Assert.assertEquals(quoteProductPackageUnitPrice, "100");
        Assert.assertEquals(quotePackageDiscount, "0");
        Assert.assertEquals(quoteProductDiscount, "0");
        Assert.assertEquals(quoteMeetingRoomDiscount, "0");
        Assert.assertEquals(quoteHotelRoomDiscount, "0");
        Assert.assertEquals(quoteProductPackageDiscount, "0");
        Assert.assertEquals(myceQuoteTotalAmmountInclTax, "500");
    }

    @Test(priority = 3, description = "On MYCE Quote check the Complimentary checkbox. Add Quote hotel rooms," +
            " Quote meeting rooms, Quote prodycts and Quote meeting packages. Expected result: Added records" +
            " prices are set to 0. Prices on MYCE Quote record are 0.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-540: Myce Quote - Complimentary")
    public void case3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary2'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='MyceQuoteComplimentaryPackage2", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String winesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary2' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "' thn__Complimentary__c=true", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='MyceQuoteComplimentaryPackage2'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1", ORG_USERNAME);
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + winesID + "' thn__Unit_Price__c=100 thn__Discount_Percent__c=0", ORG_USERNAME);
        String quoteMeetingRoomId =quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'" + " thn__Product__c='" + meetingHalfDayID + "' thn__Unit_Price__c=100", ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypeQueenID +
                "' thn__Unit_Price__c=100", ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'" + " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String myceQuoteTotalAmmountInclTax= JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        StringBuilder quotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'",ORG_USERNAME);
        StringBuilder quoteProductRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomId + "'", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID + "'", ORG_USERNAME);
        StringBuilder quoteProductFromPackageRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        String quotePackageUnitPrice= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price__c");
        String quoteProductUnitPrice= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteHotelRoomUnitPrice= JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteProductPackageUnitPrice= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Unit_Price__c");
        Assert.assertEquals(quotePackageUnitPrice, "0");
        Assert.assertEquals(quoteProductUnitPrice, "0");
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "0");
        Assert.assertEquals(quoteHotelRoomUnitPrice, "0");
        Assert.assertEquals(quoteProductPackageUnitPrice, "0");
        Assert.assertEquals(myceQuoteTotalAmmountInclTax, "0");
    }

    @Test(priority = 4, description = "Add Quote package where discount is specified to the MYCE Quote." +
            " Set Complimentary on MYCE Quote to true. Expected result: Unit price of all related records and" +
            " prices on the Quote are set to 0. Discounts on related records is changed to 100%.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-540: Myce Quote - Complimentary")
    public void case4() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary3'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='MyceQuoteComplimentaryPackage3", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary3' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='MyceQuoteComplimentaryPackage3'" +
                " thn__Hotel__c='" + propertyID + "' thn__Discount_Max__c=100", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1 thn__Apply_Discount__c=true",
                ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'" + " thn__Package__c='" + packageID + "' thn__Discount__c=10", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Complimentary__c=true", ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'",ORG_USERNAME);
        StringBuilder quoteProductFromPackageRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        String quotePackageComplimentary= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Complimentary__c");
        String quotePackageUnitPrice= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price__c");
        String quotePackageDiscount= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Discount__c");
        String quoteProductPackageComplimentary= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Complimentary__c");
        String quoteProductPackageUnitPrice= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Unit_Price__c");
        String quoteProductPackageDiscount= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Discount_Percent__c");
        Assert.assertEquals(quotePackageComplimentary, "true");
        Assert.assertEquals(quoteProductPackageComplimentary, "true");
        Assert.assertEquals(quotePackageUnitPrice, "0");
        Assert.assertEquals(quoteProductPackageUnitPrice, "0");
        Assert.assertEquals(quotePackageDiscount, "100");
        Assert.assertEquals(quoteProductPackageDiscount, "100");
    }

    @Test(priority = 5, description = "Set Complimentary on Quote back to FALSE. Expected result:Unit price on all" +
            " related records become equal to list price of each record. Discounts on related records are set to 0%." +
            " Prices on MYCE Quote record are recalculated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-540: Myce Quote - Complimentary")
    public void case5() throws InterruptedException, IOException {
        myceQuotes.updateQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary3'", "thn__Complimentary__c=false",
                ORG_USERNAME);
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary3'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String myceQuoteTotalAmmountInclTax= JsonParser2.
                getFieldValue(quoteRecord.toString(), "thn__Total_Amount_incl_Tax__c");
        StringBuilder quotePackageRecord = quoteMeetingPackages.
                getQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + myceQuoteID + "'",ORG_USERNAME);
        String quotePackageID= JsonParser2.getFieldValue(quotePackageRecord.toString(), "Id");

        StringBuilder quoteProductFromPackageRecord = quoteProducts.
                getQuoteProductSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        String quotePackageComplimentary= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Complimentary__c");
        String quotePackageUnitPrice= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Unit_Price__c");
        String quotePackageDiscount= JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Discount__c");

        String quoteProductPackageComplimentary= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Complimentary__c");
        String quoteProductPackageUnitPrice= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Unit_Price__c");
        String quoteProductPackageDiscount= JsonParser2.
                getFieldValue(quoteProductFromPackageRecord.toString(), "thn__Discount_Percent__c");
        Assert.assertEquals(quotePackageComplimentary, "false");
        Assert.assertEquals(quoteProductPackageComplimentary, "false");
        Assert.assertEquals(quotePackageUnitPrice, "100");
        Assert.assertEquals(quoteProductPackageUnitPrice, "100");
        Assert.assertEquals(quotePackageDiscount, "0");
        Assert.assertEquals(quoteProductPackageDiscount, "0");
        Assert.assertEquals(myceQuoteTotalAmmountInclTax, "100");
    }

    @Test(priority = 6, description = "Add Quote meeting room to the Quote (can be part of the Quote package). Select" +
            " added Quote meeting room. Press Change resource button. In the flow change resource and check Update" +
            " prices checkbox. Expected result: Cannot update prices because assigned MYCE Quote is complimentary.”" +
            " message is displayed. Resource on Quote meeting room is changed. Prices are not changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-540: Myce Quote - Complimentary")
    public void case6() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary4'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='MyceQuoteComplimentaryPackage4", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='MyceQuoteComplimentaryResource", ORG_USERNAME);
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='MyceQuoteComplimentary4' thn__Pax__c=1" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "' thn__Complimentary__c=true", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='MyceQuoteComplimentaryPackage4'" +
                " thn__Hotel__c='" + propertyID + "' thn__Discount_Max__c=100", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1", ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "'" + " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        String resourceID1 = resource.createResourceSFDX(SFDX, "Name='MyceQuoteComplimentaryResource'" +
                " thn__Hotel__c='" + propertyID + "' thn__Type__c='Meeting Room' thn__Full_day_price__c=500", ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("MyceQuoteComplimentary4");
        myceQuotes.openMeetingRooms();
        quoteMeetingRoom.selectItem("1");
        quoteMeetingRoom.clickChangeResource();
        String message = changeResource.
                changeResourceAndUpdatePrice_complimentary("MyceQuoteComplimentaryResource");
        String expectedMessage = "Cannot update prices because assigned MYCE Quote is complimentary.";
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.
                getQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +"'", ORG_USERNAME);
        String quoteMeetingRoomUnitPrice= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Unit_Price__c");
        String quoteMeetingRoomResource= JsonParser2.
                getFieldValue(quoteMeetingRoomRecord.toString(), "thn__Resource__c");
        Assert.assertEquals(message, expectedMessage);
        Assert.assertEquals(quoteMeetingRoomUnitPrice, "0");
        Assert.assertEquals(quoteMeetingRoomResource, resourceID1);
    }


}
