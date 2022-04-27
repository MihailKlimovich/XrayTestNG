package tests.TB;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.stringtemplate.v4.ST;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import tests.BaseTest;

import java.io.IOException;
import java.util.List;

public class ChangingPackageLinesTimingDoesNotChangeProductTiming extends BaseTest {

    @Test(priority = 1, description = "Create a Package with Packages lines of type Food, Meeting room and Hotel room." +
            " Create a MYCE Quote ( Arrival Date = today date, Departure Date = today date + 5 days). Instantiate a" +
            " Quote Package (Start = today date, End = today date + 3 days. Change the start time to 07:00 on" +
            " the Quote product with Date = today date, Change the End time to 16:00 on the Quote product with" +
            " Date = today + 1 days, Change the Start time and End time on the Quote package line of type" +
            " Food. Expected Result: The start date time and End Date Time fields were updated where these values" +
            " were not changed on the Quote product.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-317: Changing Package lines timing does not change product timing.")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ChangingPackageLinesTimingDoesNotChangeProductTimingAutoTest'",
                ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='ChangingPackageLinesTimingAutoPackage", ORG_USERNAME);
        StringBuilder hotelRecord = hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeSingleRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Single'", ORG_USERNAME);
        String roomTypeSingleID = JsonParser2.getFieldValue(roomTypeSingleRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='ChangingPackageLinesTimingAutoPackage'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Hotel Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1 thn__Space_Area__c='" +
                roomTypeSingleID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Food' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Food' thn__Product__c='" + productDinerID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=10 thn__VAT_Category__c=1", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX,
                "Name='ChangingPackageLinesTimingDoesNotChangeProductTimingAutoTest' thn__Pax__c=1" +
                        " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5) +
                        " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "' thn__Start_Date__c=" + date.generateTodayDate2() +
                " thn__End_Date__c=" + date.generateTodayDate2_plus(0, 3), ORG_USERNAME);
        StringBuilder quoteProductsRecord = myceQuotes.soql(SFDX, "SELECT Id, thn__Start_Date__c," +
                " thn__Start_Time__c, thn__Start_Date_Time__c FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" +
                quoteID + "'", ORG_USERNAME);
        List<String> quoteProductID = JsonParser2.getFieldValueSoql(quoteProductsRecord.toString(), "Id");
        quoteProducts.updateQuoteProducSFDX(SFDX, "Id='" + quoteProductID.get(0) + "'",
                "thn__Start_Time__c=07:00:00.000Z", ORG_USERNAME);
        quoteProducts.updateQuoteProducSFDX(SFDX, "Id='" + quoteProductID.get(1) + "'",
                "thn__End_Time__c=16:00:00.000Z", ORG_USERNAME);
        quotePackageLine.updateQuotePackageLineSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'" +
                " thn__Product__c='" + productDinerID + "'", "thn__Start_Time__c=08:00:00.000Z" +
                " thn__End_Time__c=14:00:00.000Z", ORG_USERNAME);
        StringBuilder updatedQuoteProductsRecord = myceQuotes.soql(SFDX, "SELECT Id, thn__Start_Time__c," +
                " thn__End_Time__c FROM thn__Quote_Product__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        List<String> quoteProductsStartTime = JsonParser2.
                getFieldValueSoql(updatedQuoteProductsRecord.toString(), "thn__Start_Time__c");
        List<String> quoteProductsEndTime = JsonParser2.
                getFieldValueSoql(updatedQuoteProductsRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteProductsStartTime.get(0), "07:00:00.000Z");
        Assert.assertEquals(quoteProductsEndTime.get(0), "14:00:00.000Z");
        Assert.assertEquals(quoteProductsStartTime.get(1), "08:00:00.000Z");
        Assert.assertEquals(quoteProductsEndTime.get(1), "16:00:00.000Z");
        Assert.assertEquals(quoteProductsStartTime.get(2), "08:00:00.000Z");
        Assert.assertEquals(quoteProductsEndTime.get(2), "14:00:00.000Z");
        Assert.assertEquals(quoteProductsStartTime.get(3), "08:00:00.000Z");
        Assert.assertEquals(quoteProductsEndTime.get(3), "14:00:00.000Z");
    }

    @Test(priority = 2, description = "Change the start date time to 04:00 on the Quote Meeting Room with Date =" +
            " today date, Change the End date time to 16:00 on the Quote Meeting Room with Date = today + 1 day," +
            " Change the Start Date time and End Date time on the Quote package line of type Quote Meeting Room." +
            " Expected Result: The start date time and End Date Time fields were updated where these values were not" +
            " changed on the Quote Meeting Room.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-317: Changing Package lines timing does not change product timing.")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX,
                "Name='ChangingPackageLinesTimingDoesNotChangeProductTimingAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackageRecord = quoteMeetingPackages.getQuotePackageSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quotePackageID= JsonParser2.getFieldValue(quotePackageRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder quotemeetingRoomRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Start_Time__c," +
                " thn__End_Time__c FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        List<String> quoteMeetingRoomID = JsonParser2.getFieldValueSoql(quotemeetingRoomRecords.toString(), "Id");
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID.get(0) + "'",
                "thn__Start_Time__c=04:00:00.000Z", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "Id='" + quoteMeetingRoomID.get(1) + "'",
                "thn__End_Time__c=16:00:00.000Z", ORG_USERNAME);
        quotePackageLine.updateQuotePackageLineSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "'", "thn__Start_Time__c=10:00:00.000Z" +
                " thn__End_Time__c=15:00:00.000Z", ORG_USERNAME);
        StringBuilder updatedQuotemeetingRoomRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__Start_Time__c," +
                        " thn__End_Time__c FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        List<String> quoteMeetingRoomStartTime = JsonParser2.
                getFieldValueSoql(updatedQuotemeetingRoomRecords.toString(), "thn__Start_Time__c");
        List<String> quoteMeetingRoomEndTime = JsonParser2.
                getFieldValueSoql(updatedQuotemeetingRoomRecords.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteMeetingRoomStartTime.get(0), "04:00:00.000Z");
        Assert.assertEquals(quoteMeetingRoomEndTime.get(0), "15:00:00.000Z");
        Assert.assertEquals(quoteMeetingRoomStartTime.get(1), "10:00:00.000Z");
        Assert.assertEquals(quoteMeetingRoomEndTime.get(1), "16:00:00.000Z");
        Assert.assertEquals(quoteMeetingRoomStartTime.get(2), "10:00:00.000Z");
        Assert.assertEquals(quoteMeetingRoomEndTime.get(2), "15:00:00.000Z");
        Assert.assertEquals(quoteMeetingRoomStartTime.get(3), "10:00:00.000Z");
        Assert.assertEquals(quoteMeetingRoomEndTime.get(3), "15:00:00.000Z");
    }

    @Test(priority = 3, description = "Change the Start Date Time to 03:45 and End Date Time to 12:30 on the Quote" +
            " Hotel Room. Go to the Quote Package line of type Hotel Room and change the Start Time aтв End Time." +
            " Expected Result: Arrival Date Time and Departure Date were updated on the Quote Hotel Rooms where the" +
            " value wasn’t changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-317: Changing Package lines timing does not change product timing.")
    public void case3() throws InterruptedException, IOException {
        StringBuilder packageRecord = packages.getPackageSFDX(SFDX,
                "Name='ChangingPackageLinesTimingAutoPackage'", ORG_USERNAME);
        String packageID= JsonParser2.getFieldValue(packageRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX,
                "Name='ChangingPackageLinesTimingDoesNotChangeProductTimingAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quotePackageRecord = quoteMeetingPackages.getQuotePackageSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quotePackageID= JsonParser2.getFieldValue(quotePackageRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        String quotePackageID2 = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "' thn__Start_Date__c=" + date.generateTodayDate2() +
                " thn__End_Date__c=" + date.generateTodayDate2_plus(0, 3), ORG_USERNAME);
        StringBuilder quoteHotelRoomRec = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__Quote_Package__c='"
                + quotePackageID + "'", ORG_USERNAME);
        System.out.println(quoteHotelRoomRec);
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'",
                "thn__Arrival_Date_Time__c=" + date.generateTodayDate2() + "T01:45:00.000+0000" +
                        " thn__Departure_Date_Time__c=" + date.generateTodayDate2_plus(0, 3) +
                        "T10:30:00.000+0000", ORG_USERNAME);
        quotePackageLine.updateQuotePackageLineSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'" +
                " thn__Product__c='" + room1NightID + "'", "thn__Start_Time__c=05:00:00.000Z" +
                " thn__End_Time__c=15:00:00.000Z", ORG_USERNAME);
        quotePackageLine.updateQuotePackageLineSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID2 + "'" +
                " thn__Product__c='" + room1NightID + "'", "thn__Start_Time__c=05:00:00.000Z" +
                " thn__End_Time__c=15:00:00.000Z", ORG_USERNAME);
        StringBuilder updatedQuoteHotelRoomRecord1 = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        StringBuilder updatedQuoteHotelRoomRecord2 = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageID2 + "'", ORG_USERNAME);
        String qhrArrivalTime1= JsonParser2.
                getFieldValue(updatedQuoteHotelRoomRecord1.toString(), "thn__Arrival_Time__c");
        String qhrDepartureTime1= JsonParser2.
                getFieldValue(updatedQuoteHotelRoomRecord1.toString(), "thn__Departure_Time__c");
        String qhrArrivalTime2= JsonParser2.
                getFieldValue(updatedQuoteHotelRoomRecord2.toString(), "thn__Arrival_Time__c");
        String qhrDepartureTime2= JsonParser2.
                getFieldValue(updatedQuoteHotelRoomRecord2.toString(), "thn__Departure_Time__c");
        Assert.assertEquals(qhrArrivalTime1, "01:45:00.000Z");
        Assert.assertEquals(qhrDepartureTime1, "10:30:00.000Z");
        Assert.assertEquals(qhrArrivalTime2, "05:00:00.000Z");
        Assert.assertEquals(qhrDepartureTime2, "15                                                  :00:00.000Z");
    }

}
