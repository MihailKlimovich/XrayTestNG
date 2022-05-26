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

public class UnableToAddQPwithQPofTypeMeetingRoomToQuoteWhichHasPMSblock extends BaseTest {

    @Test(priority = 1, description = "Create a Package that conains a Meeting Room as a Package Line. Create a" +
            " MYCE Quote. Instantiate a Quote Hotel Room. Set checkbox ‘Send to PMS’ = true. Instantiate the Quote" +
            " Package to the MYCE Quote. Expected Result: Quote Package has been instantiated. No errors are thrown.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-373: Unable to add QP with QPL of type Meeting Room to Quote which has a PMS Block")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB-373AutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='PackageTB373Auto' thn__Hotel__c='" +
                propertyID + "'", ORG_USERNAME);
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB-373AutoTest' thn__Pax__c=3 thn__Hotel__c='" +
                propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2_plus(0, 3) +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 7) + " RecordTypeId='" +
                recordTypeID.get(0) + "'", ORG_USERNAME);
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                + roomTypeQueenID + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__SendToPMS__c=true", ORG_USERNAME);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockID = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        Assert.assertNotNull(pmsBlockID);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "'", ORG_USERNAME);
        System.out.println(quotePackageID);
        StringBuilder quoteMeetingRoomRecord = quoteMeetingRoom.getQuoteMeetingRoomSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        String quoteMeetingRoomID = JsonParser2.getFieldValue(quoteMeetingRoomRecord.toString(), "Id");
        Assert.assertNotNull(quotePackageID);
        Assert.assertNotNull(quoteMeetingRoomID);
    }

}
