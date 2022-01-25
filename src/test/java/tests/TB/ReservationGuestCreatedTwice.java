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

public class ReservationGuestCreatedTwice extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-261: Reservation Guest created twice")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-261: Reservation Guest created twice")
    public void case1() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='ReservationGuestCreatedTwiceAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='ReservationGuestCreatedTwiceAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" +quoteID + "'", "thn__Stage__c='3 - Tentative'" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" +quoteID + "'", "thn__Stage__c='2 - Propose'", ORG_USERNAME);
        StringBuilder reservationGuests = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Guest__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> reservationGuestID = JsonParser2.getFieldValueSoql(reservationGuests.toString(), "Id");
        Assert.assertEquals(reservationGuestID.size(), 1);
    }

}
