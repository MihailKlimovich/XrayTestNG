package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteHotelRoomAssignMewsState extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote. Instantiate a Quote Hotel Room. Change the stage of the" +
            " Quote to ‘2 - Propose’. Expected result: Reservation guest is created, sent to mews and linked to the" +
            " quote. Mews state is updated on the Quote Hotel Room. Reservations are created have their state update" +
            " and sent to mews. Mews Group Id is updated on the MYCE Quote." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-524: Quote Hotel room: assign mews state")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteHotelRoomAssignMewsStateAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteHotelRoomAssignMewsStateAutoTest'" +
                " thn__Pax__c=100 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0  ,2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 3) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Pax__c=1", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'",
                ORG_USERNAME);
        Thread.sleep(5000);
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String reservationGuestID = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Reservation_Guest__c");
        String mewsGroupID = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Mews_Group_Id__c");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId + "'", ORG_USERNAME);
        String qhrMewsState = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Mews_State__c");
        StringBuilder reservationsRecords = myceQuotes.soql(SFDX, "SELECT Id, thn__State__c FROM" +
                " thn__Reservation__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomId + "'", ORG_USERNAME);
        List<String> reservationsID = JsonParser2.
                getFieldValueSoql(reservationsRecords.toString(), "Id");
        List<String> reservationsState = JsonParser2.
                getFieldValueSoql(reservationsRecords.toString(), "thn__State__c");
        Assert.assertNotNull(reservationGuestID);
        Assert.assertNotNull(mewsGroupID);
        Assert.assertEquals(reservationsID.size(), 1);
        Assert.assertEquals(qhrMewsState, "Optional");
        Assert.assertEquals(reservationsState.get(0), "Optional");
    }

    @Test(priority = 2, description = "Add Quote hotel room. Expected result: Mews state is updated on the added" +
            " Quote Hotel room." )
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-524: Quote Hotel room: assign mews state")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='QuoteHotelRoomAssignMewsStateAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        String quoteHotelRoomId = quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Product__c='" + room1NightID + "' thn__Space_Area__c='" + roomTypesId.get(0) +
                "' thn__Property__c='" + propertyID + "' thn__Pax__c=1", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomId + "'", ORG_USERNAME);
        String qhrMewsState = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Mews_State__c");
        Assert.assertEquals(qhrMewsState, "Optional");
    }

}
