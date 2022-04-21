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

public class PMSBlockCodeDoesntMapTheValueFromCustomFieldOnQuote extends BaseTest {

    @Test(priority = 1, description = "Create a custom field on MYCE Quote object. Specify this field in CMT > Hapi" +
            " Connector: Block Code Fields = thn__MYCE_Quote__c.thn__Reference_c. Create a MYCE QuoteCreate a MYCE" +
            " Quote. Create a MYCE Quote. Refrence == 22-CAN22-0755. Instantiate a Quote Hotel Room. set checkbox" +
            " Send to PMS = true. Expected Result: PMS Block record is created. thn_PMS_Blockc.thnCodec is filled" +
            " from the value of thn__MYCE_Quote__c.thn__Reference__c. thn_PMS_Blockc.thnCode_c = 22-CAN22-0755.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-371: PMS_Block__c.Code__c doesn't map the value from Custom field on Quote")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB-371AutoTest'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/UpdateBlockCodeHapiConnectorDemo.apex");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB-371AutoTest' thn__Pax__c=3 thn__Hotel__c='" +
                propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2_plus(0, 3) +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 7) + " RecordTypeId='" +
                recordTypeID.get(0) + "' Reference__c='22-CAN22-0755'", ORG_USERNAME);
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
        String pmsBlockCode = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__Code__c");
        System.out.println(pmsBlockCode);
        Assert.assertEquals(pmsBlockCode, "22-CAN22-0755");
    }

    @Test(priority = 2, description = "Modify the CMT > Hapi Connector record. Set an invalid value inBlock Code" +
            " Fields. Block Code Fields = Test, a, Test. Instantiate a Quote Hotel Room. set checkbox Send to PMS" +
            " = true. Expected Result: PMS Block record is created, value in thn_PMS_Block.thnCodec is filled with" +
            " ID of the MYCE Quote")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-371: PMS_Block__c.Code__c doesn't map the value from Custom field on Quote")
    public void case2() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB-371AutoTest2'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/UpdateBlockCodeHapiConnectorDemo2.apex");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB-371AutoTest2' thn__Pax__c=3 thn__Hotel__c='" +
                propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2_plus(0, 3) +
                " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 7) + " RecordTypeId='" +
                recordTypeID.get(0) + "' Reference__c='22-CAN22-0755'", ORG_USERNAME);
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
        String pmsBlockCode = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__Code__c");
        System.out.println(pmsBlockCode);
        Assert.assertEquals(pmsBlockCode, quoteID.toUpperCase());
    }

}
