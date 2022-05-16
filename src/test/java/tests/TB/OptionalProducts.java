package tests.TB;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import tests.BaseTest;

import java.io.IOException;
import java.util.List;

public class OptionalProducts extends BaseTest {

    @Test(priority = 1, description = "Create a Package that has a Product and Meeting Room as Package lines. Create" +
            " a MYCE Quote. Instantiate Created Quote Package and tag it as Optional == True. Instantiate another" +
            " Quote Package and tag it as Optional == False. Set the Stage of the Quote to ‘4 - Closed’ ‘Won’. Set" +
            " the Stage of the Quote to ‘4 - Closed’ ‘Won’. Expected result: Orders are create only for the Quote" +
            " Product and Quote Meeting Room which parent Quote package isn’t tagged as Optional == True.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-419: Optional Products")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB-419AutoTest'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DeleteOptionalProductFalseForDemo.apex");
        packages.deletePackageSFDX(SFDX, "Name='TB-419AutoPackage", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='TB-419AutoPackage'" +
                " thn__Hotel__c='" + propertyID + "' thn__Multi_Days__c=true thn__Custom_Price__c=true" +
                " thn__Discount_Max__c=100", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beer' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=100 thn__VAT_Category__c=0 thn__Apply_Discount__c=true" +
                " thn__AppliedDay__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='TB-419AutoTest' thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 2) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "'", ORG_USERNAME);
        String quotePackageID2 = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "' thn__Optional__c=true", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='4 - Closed'" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchCreateOrders");
        Thread.sleep(5000);
        StringBuilder orderRecord = order.getOrderSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String orderID = JsonParser2.getFieldValue(orderRecord.toString(), "Id");
        StringBuilder orderLine = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Order_Line__c WHERE" +
                " thn__Order__c='" + orderID + "'", ORG_USERNAME);
        List<String> orderLineID = JsonParser2.getFieldValueSoql(orderLine.toString(), "Id");
        Assert.assertEquals(orderLineID.size(), 2);
    }

}
