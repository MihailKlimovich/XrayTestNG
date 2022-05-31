package tests.TB;

import com.xpandit.testng.annotations.Xray;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import tests.BaseTest;

import java.io.IOException;
import java.util.List;

public class CloneSelection extends BaseTest {

    @Test(priority = 1, description = "Create a Package with two Products having lookup to different meeting rooms." +
            " Create a MYCE Quote. Instantiate the created Package to the Quote. Clone the Quote Package using" +
            " the Clone Select component. Expected Result: Quote Package is clone. thn__Quote_Package__c." +
            "thn__Clone__c = true. The cloned package has the linked Quote package lines stored.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-388: Clone selection.")
    @Xray(requirement = "TTP-263", test="TTP-284")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB388AutoTest'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='TB388AutoTPackage", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='TB388AutoTPackage' thn__Hotel__c='" +
                propertyID + "'", ORG_USERNAME);
        String meetingRoomPackage1 = packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room Half Day'" +
                " thn__Package__c='" + packageID + "' thn__Type__c='Meeting Room' thn__Product__c='" +
                meetingHalfDayID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00 thn__Unit_Price__c=20 " +
                "thn__VAT_Category__c=1", ORG_USERNAME);
        String meetingRoomPackage2 = packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room Full Day'" +
                " thn__Package__c='" + packageID + "' thn__Type__c='Meeting Room' thn__Product__c='" +
                meetingFullDayID + "' thn__Start_Time__c=12:00 thn__End_Time__c=13:00 thn__Unit_Price__c=20" +
                " thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1 thn__Service_Area__c='" +
                meetingRoomPackage1 + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Diner' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Food' thn__Product__c='" + productDinerID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1 thn__Service_Area__c='" +
                meetingRoomPackage2 + "'", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB388AutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("TB388AutoTest");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate3_plus(0 , 0), "Quote Package");
        StringBuilder clonedQuotePackage = quoteMeetingPackages.getQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Clone__c=true", ORG_USERNAME);
        String clonePackageID = JsonParser2.getFieldValue(clonedQuotePackage.toString(), "Id");
        StringBuilder clonedQuoteMeetingRoom1 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + clonePackageID + "' thn__Product__c='" + meetingHalfDayID + "'",
                ORG_USERNAME);
        String cloneCheckboxQuoteMeetingRoom1= JsonParser2.
                getFieldValue(clonedQuoteMeetingRoom1.toString(), "thn__Clone__c");
        String quoteMeetingRoom1ID= JsonParser2.
                getFieldValue(clonedQuoteMeetingRoom1.toString(), "Id");
        StringBuilder clonedQuoteMeetingRoom2 = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + clonePackageID + "' thn__Product__c='" + meetingFullDayID + "'",
                ORG_USERNAME);
        String cloneCheckboxQuoteMeetingRoom2= JsonParser2.
                getFieldValue(clonedQuoteMeetingRoom2.toString(), "thn__Clone__c");
        String quoteMeetingRoom2ID= JsonParser2.
                getFieldValue(clonedQuoteMeetingRoom2.toString(), "Id");
        StringBuilder clonedQuoteProductBeverage = quoteProducts.getQuoteProductSFDX(SFDX,
                "thn__Quote_Package__c='" + clonePackageID + "' thn__Product__c='" + beverageID + "'",
                ORG_USERNAME);
        String cloneCheckboxQuoteProductBeverage= JsonParser2.
                getFieldValue(clonedQuoteProductBeverage.toString(), "thn__Clone__c");
        String quoteProductBeverageMeetingRoom= JsonParser2.
                getFieldValue(clonedQuoteProductBeverage.toString(), "thn__Service_Area__c");
        StringBuilder clonedQuoteProductDiner = quoteProducts.getQuoteProductSFDX(SFDX,
                "thn__Quote_Package__c='" + clonePackageID + "' thn__Product__c='" + productDinerID + "'",
                ORG_USERNAME);
        String cloneCheckboxQuoteProductDiner= JsonParser2.
                getFieldValue(clonedQuoteProductDiner.toString(), "thn__Clone__c");
        String quoteProductDinerMeetingRoom= JsonParser2.
                getFieldValue(clonedQuoteProductDiner.toString(), "thn__Service_Area__c");
        Assert.assertEquals(cloneCheckboxQuoteMeetingRoom1, "true");
        Assert.assertEquals(cloneCheckboxQuoteMeetingRoom2, "true");
        Assert.assertEquals(cloneCheckboxQuoteProductBeverage, "true");
        Assert.assertEquals(cloneCheckboxQuoteProductDiner, "true");
        Assert.assertEquals(quoteProductBeverageMeetingRoom, quoteMeetingRoom1ID);
        Assert.assertEquals(quoteProductDinerMeetingRoom, quoteMeetingRoom2ID);

    }

}
