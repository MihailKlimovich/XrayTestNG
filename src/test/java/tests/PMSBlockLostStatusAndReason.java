package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class PMSBlockLostStatusAndReason extends BaseTest {


    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-667: PMS Block lost status + reason")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL_THYNK, thynkPackUserName);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PMSBlockLostStatusAutoTest'", thynkPackUserName);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "Name='ESDH'", thynkPackUserName);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", thynkPackUserName);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='PMSBlockLostStatusAutoTest' thn__Pax__c=3" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "' thn__Origin__c='IND'", thynkPackUserName);
        StringBuilder hotelRoomRecord = product.getProductSFDX(SFDX, "Name='Hotel Room' thn__Hotel__c='"
                + propertyID + "'", thynkPackUserName);
        System.out.println(hotelRoomRecord);
        String productHotelRoomID= JsonParser2.getFieldValue(hotelRoomRecord.toString(), "Id");
        StringBuilder roomTypeDeluxeSingleRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Deluxe Single'" +
                " thn__Hotel__c='" + propertyID + "'", thynkPackUserName);
        String roomTypeDeluxeSingleID = JsonParser2.getFieldValue(roomTypeDeluxeSingleRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='TEST RATE EXCLUDE TAX' thn__Hotel__c='"
                + propertyID + "'", thynkPackUserName);
        String rateID = JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + productHotelRoomID + "' thn__Space_Area__c='"
                + roomTypeDeluxeSingleID + "' thn__Rate_Plan__c='" + rateID + "' thn__Type_of_Occupancy__c='Triple'",
                thynkPackUserName);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='"+ quoteID + "'", "thn__SendToPMS__c=true",
                thynkPackUserName);
        StringBuilder pmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                thynkPackUserName);
        String pmsBlockId = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "Id");
        Assert.assertNotNull(pmsBlockId);
    }

}
