package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class OperaQuoteAndPMSBlockMapping extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote and fill in the Source and Market Segment (Source = Walk" +
            " in, Market Segment = Leisure). Add a Quote Hotel Room. Set ‘Send to PMS’ = True on MYCE Quote. Expected" +
            " Result: PMS Block is created. Segmentation Source and Segmentation Market have been filled on the block" +
            " from the values on the Quote." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY- 679: Test Opera - Quote and PMS Block mapping")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='OperaQuoteAndPMSBlockMappingAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='OperaQuoteAndPMSBlockMappingAutoTest'" +
                " thn__Pax__c=100 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 4) + " RecordTypeId='" +
                recordTypeID.get(0) + "' thn__Source__c='Walk in' thn__Market_Segment__c='Leisure'", ORG_USERNAME);
        String quoteHotelRoomID = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='"
                + roomTypeQueenID + "' thn__Type_of_Occupancy__c='Triple' thn__Pax__c=5", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__SendToPMS__c='true'", ORG_USERNAME);
        StringBuilder pmsBlockrecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                ORG_USERNAME);
        String pmsBlockId = JsonParser2.getFieldValue(pmsBlockrecord.toString(), "Id");
        Assert.assertNotNull(pmsBlockId);
        String pmsBlockSegmentationSource = JsonParser2.
                getFieldValue(pmsBlockrecord.toString(), "thn__SegmentationSource__c");
        String pmsBlockSegmentationMarket = JsonParser2.
                getFieldValue(pmsBlockrecord.toString(), "thn__SegmentationMarket__c");
        Assert.assertEquals(pmsBlockSegmentationSource, "Walk in");
        Assert.assertEquals(pmsBlockSegmentationMarket, "Leisure");
    }

}
