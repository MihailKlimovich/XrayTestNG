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

public class QuoteHotelRoomQuantity extends BaseTest {

    @Test(priority = 1, description = "Create a MYCE Quote, Open the Group Booking Component and Instantiate a Quote" +
            " Hotel Room. Expected Result: A Quote Hotel Room is created. Number on the Quote Hotel room equals to" +
            " the Highest value of Quantity on the Quote Hotel Room Prices.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-376: Quote hotel room - quantity")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB-376'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB-376'" +
                " thn__Pax__c=100 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 10) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("TB-376");
        myceQuotes.clickGroupBookingTab();
        groupBookingComponent.clickNewButton();
        groupBookingComponent.
                cteateQuoteHotelRoom("BAR", "Double", "1", "Queen");
        groupBookingComponent.
                changePricePerDayAndQuantity("1", "1", "150", "10");
        groupBookingComponent.
                changePricePerDayAndQuantity("3", "2", "160", "8");
        groupBookingComponent.clickSaveButton();
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        String quoteHotelRoonPax = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Pax__c");
        StringBuilder quoteHotelRoomPrices = myceQuotes.soql(SFDX, "SELECT Id, thn__Quantity__c FROM" +
                        " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                ORG_USERNAME);
        List<Integer> qhrpQuantity = JsonParser2.
                getFieldValueSoql2(quoteHotelRoomPrices.toString(), "thn__Quantity__c");
        Assert.assertEquals(quoteHotelRoonPax, "10");
        Assert.assertEquals(qhrpQuantity.get(0).intValue(), 10);
        Assert.assertEquals(qhrpQuantity.get(1).intValue(), 8);
    }

    @Test(priority = 2, description = "Change the value of the Quantity on the first day to 9. Expected Result:" +
            " Quote Hotel room Number is updated to 9.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-376: Quote hotel room - quantity")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='TB-376'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("TB-376");
        myceQuotes.clickGroupBookingTab();
        groupBookingComponent.changeQuantity("1", "9" );
        groupBookingComponent.clickSaveButton();
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        String quoteHotelRoonPax = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Pax__c");
        StringBuilder quoteHotelRoomPrices = myceQuotes.soql(SFDX, "SELECT Id, thn__Quantity__c FROM" +
                        " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                ORG_USERNAME);
        List<Integer> qhrpQuantity = JsonParser2.
                getFieldValueSoql2(quoteHotelRoomPrices.toString(), "thn__Quantity__c");
        Assert.assertEquals(quoteHotelRoonPax, "9");
        Assert.assertEquals(qhrpQuantity.get(0).intValue(), 9);
        Assert.assertEquals(qhrpQuantity.get(1).intValue(), 8);
    }

    @Test(priority = 3, description = "Change the value of the Quantity on the second day to 15. Expected Result:" +
            " Quote Hotel room Number is updated to 15.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-376: Quote hotel room - quantity")
    public void case3() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='TB-376'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("TB-376");
        myceQuotes.clickGroupBookingTab();
        groupBookingComponent.changeQuantity("2", "15" );
        groupBookingComponent.clickSaveButton();
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "'", ORG_USERNAME);
        String quoteHotelRoomID = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "Id");
        String quoteHotelRoonPax = JsonParser2.getFieldValue(quoteHotelRoomRecord.toString(), "thn__Pax__c");
        StringBuilder quoteHotelRoomPrices = myceQuotes.soql(SFDX, "SELECT Id, thn__Quantity__c FROM" +
                        " thn__Quote_Hotel_Room_Price__c WHERE thn__Quote_Hotel_Room__c='" + quoteHotelRoomID + "'",
                ORG_USERNAME);
        List<Integer> qhrpQuantity = JsonParser2.
                getFieldValueSoql2(quoteHotelRoomPrices.toString(), "thn__Quantity__c");
        Assert.assertEquals(quoteHotelRoonPax, "15");
        Assert.assertEquals(qhrpQuantity.get(0).intValue(), 9);
        Assert.assertEquals(qhrpQuantity.get(1).intValue(), 15);
    }

}
