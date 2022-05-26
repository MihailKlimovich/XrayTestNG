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

public class IncorrectEndDateOnQPafterStartDateIsChangedOnQP extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote. Instantiate a Quote Package. Start Date = today + 1 day," +
            " End Date = today + 3 days. Change the Start Date of the Instantiated Quote package: Start Date = today" +
            " + 2 days. Expected result: The Start Date of the Quote Package was change but the End Date is not" +
            " changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-256: Incorrect End Date on QP after Start Date is Changed on QP")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB-256'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='PackageTB256", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB-256' thn__Pax__c=5 thn__Hotel__c='" +
                        propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 10) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='PackageTB256' thn__Hotel__c='" + propertyID +
                "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "' thn__Start_Date__c=" +
                date.generateTodayDate2_plus(0, 1) + " thn__End_Date__c=" +
                date.generateTodayDate2_plus(0, 3), ORG_USERNAME);
        quoteMeetingPackages.updateQuotePackageSFDX(SFDX, "Id='" + quotePackageID + "'",
                "thn__Start_Date__c='" + date.generateTodayDate2_plus(0, 2), ORG_USERNAME);
        StringBuilder quotePackageRecord = quoteMeetingPackages.getQuotePackageSFDX(SFDX, "Id='" +
                quotePackageID + "'", ORG_USERNAME);
        String quotePackageStartDate = JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__Start_Date__c");
        String quotePackageEndDate = JsonParser2.
                getFieldValue(quotePackageRecord.toString(), "thn__End_Date__c");
        Assert.assertEquals(quotePackageStartDate, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(quotePackageEndDate, date.generateTodayDate2_plus(0, 3));
    }

}
