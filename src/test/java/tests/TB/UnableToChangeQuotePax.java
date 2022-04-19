package tests.TB;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;
import tests.BaseTest;

import java.io.IOException;
import java.util.List;

public class UnableToChangeQuotePax extends BaseTest {

    @Test(priority = 1, description = "Create a Multi-days package. For the Package Lines Create: Package Line 1:" +
            " Type = Beverage, Applied day = Null, Package Line 2: Type = Meeting Room, Applied day = Null, Package" +
            " Line 3: Type = Food, Applied day = 1, Package Line 4: Type = Activity, Applied day = 2. Package Line 1:" +
            " Type = Activity, Applied day = 2. Instantiate the created Package to the Quote. Expected Result: Quote" +
            " Package is instantiated for the duration of 2 days. Quote product of type Beverage is created for 2" +
            " days of the package. Quote product of type Food is created for 1st day of the package. Quote product" +
            " of type Activity is created for the 2nd day of the package. Quote Meeting Room is created for 2 days of" +
            " the Package.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-336: Unable to change Quote pax")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "User",
                "-w",
                "Username='" + ORG_USERNAME + "'",
                "-v",
                "thn__ByPassVR__c=true",
                "-u",
                ORG_USERNAME,
                "--json"});
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='UnableToChangeQuotePaxAutoTest'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='TB-336 AutoPackage", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder activityRecord = product.getProductSFDX(SFDX, "Name='ACTIVITY'", ORG_USERNAME);
        String productActivityID= JsonParser2.getFieldValue(activityRecord.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='TB-336 AutoPackage'" +
                " thn__Hotel__c='" + propertyID + "' thn__Multi_Days__c=true", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Beverage' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Beverage' thn__Product__c='" + beverageID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Food' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Food' thn__Product__c='" + productDinerID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1 thn__AppliedDay__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Activity' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Activity' thn__Product__c='" + productActivityID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1 thn__AppliedDay__c=2", ORG_USERNAME);
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='UnableToChangeQuotePaxAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        StringBuilder quoteProductBeverage = myceQuotes.soql(SFDX, "SELECT Id, thn__Start_Date__c," +
                        " thn__End_Date__c FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" + quoteID + "' AND" +
                " thn__Type__c='Beverage'", ORG_USERNAME);
        StringBuilder quoteProductFood = myceQuotes.soql(SFDX, "SELECT Id, thn__Start_Date__c," +
                " thn__End_Date__c FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" + quoteID + "' AND" +
                " thn__Type__c='Food'", ORG_USERNAME);
        StringBuilder quoteProductActivity = myceQuotes.soql(SFDX, "SELECT Id, thn__Start_Date__c," +
                " thn__End_Date__c FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" + quoteID + "' AND" +
                " thn__Type__c='Activity'", ORG_USERNAME);
        StringBuilder quoteMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id, thn__Start_Date__c," +
                " thn__End_Date__c FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "' AND" +
                " thn__Type__c='Meeting Room'", ORG_USERNAME);
        List<String> qpBeverageID = JsonParser2.getFieldValueSoql(quoteProductBeverage.toString(), "Id");
        List<String> qpFoodID = JsonParser2.getFieldValueSoql(quoteProductFood.toString(), "Id");
        List<String> qpActivityID = JsonParser2.getFieldValueSoql(quoteProductActivity.toString(), "Id");
        List<String> qmrID = JsonParser2.getFieldValueSoql(quoteMeetingRoom.toString(), "Id");
        List<String> qpBeverageStartDate = JsonParser2.
                getFieldValueSoql(quoteProductBeverage.toString(), "thn__Start_Date__c");
        List<String> qpFoodStartDate = JsonParser2.
                getFieldValueSoql(quoteProductFood.toString(), "thn__Start_Date__c");
        List<String> qpActivityStartDate = JsonParser2.
                getFieldValueSoql(quoteProductActivity.toString(), "thn__Start_Date__c");
        List<String> qmrStartDate = JsonParser2.
                getFieldValueSoql(quoteMeetingRoom.toString(), "thn__Start_Date__c");
        List<String> qpBeverageEndDate = JsonParser2.
                getFieldValueSoql(quoteProductBeverage.toString(), "thn__End_Date__c");
        List<String> qpFoodEndDate = JsonParser2.
                getFieldValueSoql(quoteProductFood.toString(), "thn__End_Date__c");
        List<String> qpActivityEndDate = JsonParser2.
                getFieldValueSoql(quoteProductActivity.toString(), "thn__End_Date__c");
        List<String> qmrEndDate = JsonParser2.
                getFieldValueSoql(quoteMeetingRoom.toString(), "thn__End_Date__c");
        Assert.assertEquals(qpBeverageID.size(), 2);
        Assert.assertEquals(qpFoodID.size(), 1);
        Assert.assertEquals(qpActivityID.size(), 1);
        Assert.assertEquals(qmrID.size(), 2);
        Assert.assertEquals(qpBeverageStartDate.get(0), date.generateTodayDate2());
        Assert.assertEquals(qpBeverageStartDate.get(1), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(qpFoodStartDate.get(0), date.generateTodayDate2());
        Assert.assertEquals(qpActivityStartDate.get(0), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(qmrStartDate.get(0), date.generateTodayDate2());
        Assert.assertEquals(qmrStartDate.get(1), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(qpBeverageEndDate.get(0), date.generateTodayDate2());
        Assert.assertEquals(qpBeverageEndDate.get(1), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(qpFoodEndDate.get(0), date.generateTodayDate2());
        Assert.assertEquals(qpActivityEndDate.get(0), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(qmrEndDate.get(0), date.generateTodayDate2());
        Assert.assertEquals(qmrEndDate.get(1), date.generateTodayDate2_plus(0, 1));
    }

    @Test(priority = 2, description = "Change the Applied day field on the Quote Package Line of type Meeting Room." +
            " Applied day = 0. Expected Result: Two Meeting Rooms are Deleted and a new Meeting Room is created." +
            " New Meeting Room is created for the day before the the Quote package starts.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-336: Unable to change Quote pax")
    public void case2() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "User",
                "-w",
                "Username='" + ORG_USERNAME + "'",
                "-v",
                "thn__ByPassVR__c=false",
                "-u",
                ORG_USERNAME,
                "--json"});
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX,
                "Name='UnableToChangeQuotePaxAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "' AND thn__Type__c='Meeting Room'", ORG_USERNAME);
        List<String> qmrID = JsonParser2.getFieldValueSoql(quoteMeetingRooms.toString(), "Id");
        StringBuilder quotePackageLineMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Package_Line__c WHERE thn__MYCE_Quote__c='" + quoteID + "' AND" +
                " thn__Type__c='Meeting Room'", ORG_USERNAME);
        List<String> packageLineQMRID = JsonParser2.
                getFieldValueSoql(quotePackageLineMeetingRoom.toString(), "Id");
        quotePackageLine.updateQuotePackageLineSFDX(SFDX, "Id='" + packageLineQMRID.get(0) + "'",
                "thn__Applied_Day__c=0", ORG_USERNAME);
        StringBuilder newQuoteMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id, thn__Start_Date__c," +
                " thn__End_Date__c FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "' AND" +
                " thn__Type__c='Meeting Room'", ORG_USERNAME);
        List<String> newQMRID = JsonParser2.getFieldValueSoql(newQuoteMeetingRoom.toString(), "Id");
        List<String> newQMRstartDate = JsonParser2.
                getFieldValueSoql(newQuoteMeetingRoom.toString(), "thn__Start_Date__c");
        List<String> newQMRendDate = JsonParser2.
                getFieldValueSoql(newQuoteMeetingRoom.toString(), "thn__End_Date__c");
        Assert.assertEquals(newQMRID.size(), 1);
        Assert.assertNotEquals(newQMRID, qmrID.get(0));
        Assert.assertNotEquals(newQMRID, qmrID.get(1));
        Assert.assertEquals(newQMRstartDate.get(0), date.generateTodayDate2_minus(0, 1));
        Assert.assertEquals(newQMRendDate.get(0), date.generateTodayDate2_minus(0, 1));
    }

    @Test(priority = 3, description = "Change the Applied day field on the Quote Package Line of type Meeting Room." +
            " Applied day = 2. Expected Result: Meeting room is deleted and new one is created. New Meeting Room is" +
            " created for the 2nd day of the Quote package.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-336: Unable to change Quote pax")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX,
                "Name='UnableToChangeQuotePaxAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Quote_Meeting_Room__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "' AND thn__Type__c='Meeting Room'", ORG_USERNAME);
        List<String> qmrID = JsonParser2.getFieldValueSoql(quoteMeetingRooms.toString(), "Id");
        StringBuilder quotePackageLineMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id FROM" +
                " thn__Quote_Package_Line__c WHERE thn__MYCE_Quote__c='" + quoteID + "' AND" +
                " thn__Type__c='Meeting Room'", ORG_USERNAME);
        List<String> packageLineQMRID = JsonParser2.
                getFieldValueSoql(quotePackageLineMeetingRoom.toString(), "Id");
        quotePackageLine.updateQuotePackageLineSFDX(SFDX, "Id='" + packageLineQMRID.get(0) + "'",
                "thn__Applied_Day__c=2", ORG_USERNAME);
        StringBuilder newQuoteMeetingRoom = myceQuotes.soql(SFDX, "SELECT Id, thn__Start_Date__c," +
                " thn__End_Date__c FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "' AND" +
                " thn__Type__c='Meeting Room'", ORG_USERNAME);
        List<String> newQMRID = JsonParser2.getFieldValueSoql(newQuoteMeetingRoom.toString(), "Id");
        List<String> newQMRstartDate = JsonParser2.
                getFieldValueSoql(newQuoteMeetingRoom.toString(), "thn__Start_Date__c");
        List<String> newQMRendDate = JsonParser2.
                getFieldValueSoql(newQuoteMeetingRoom.toString(), "thn__End_Date__c");
        Assert.assertEquals(newQMRID.size(), 1);
        Assert.assertNotEquals(newQMRID, qmrID.get(0));
        Assert.assertEquals(newQMRstartDate.get(0), date.generateTodayDate2_plus(0, 1));
        Assert.assertEquals(newQMRendDate.get(0), date.generateTodayDate2_plus(0, 1));
    }

    @Test(priority = 4, description = "Change the Pax on the MYCE quote. Pax = 100. Expected Result: Quote pax and" +
            " it’s related is changed. No errors are thrown.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-336: Unable to change Quote pax")
    public void case4() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX,
                "Name='UnableToChangeQuotePaxAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Pax__c=100", ORG_USERNAME);
        StringBuilder updatedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quotePax= JsonParser2.getFieldValue(updatedQuoteRecord.toString(), "thn__Pax__c");
        StringBuilder quotePackageLineRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Pax__c FROM" +
                " thn__Quote_Package_Line__c WHERE thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> qplID = JsonParser2.getFieldValueSoql(quotePackageLineRecords.toString(), "Id");
        List<Integer> qplPax = JsonParser2.getFieldValueSoql2(quotePackageLineRecords.toString(), "thn__Pax__c");
        Assert.assertEquals(quotePax, "100");
        Assert.assertEquals(qplID.size(), 4);
        Assert.assertEquals(qplPax.get(0).intValue(), 100);
        Assert.assertEquals(qplPax.get(1).intValue(), 100);
        Assert.assertEquals(qplPax.get(2).intValue(), 100);
        Assert.assertEquals(qplPax.get(3).intValue(), 100);
    }
}
