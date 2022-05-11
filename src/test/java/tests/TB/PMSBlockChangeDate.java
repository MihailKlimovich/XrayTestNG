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

public class PMSBlockChangeDate extends BaseTest {

    @Test(priority = 1, description = "In CMT > Hapi Connector > Block Code Fields = Start__c,Name. Create a" +
            " MYCE Quote. Instantiate a Qutoe Hotel Room. Instantiate a Qutoe Hotel Room. Pms Block is created." +
            " Code field on the block is formed from Date field and Name of the Qutoe.thn_Code_c = quoteArrivalDate" +
            " + 000000TB356. Change the date of the Quote via the Change Date flow. Expected Result: Date on the" +
            " MYCE Quote and it’s related is changed. Code field on the PMS Block isn’t changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-356: PMS Block - change date")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/UpdateBlockCodeHapiConnectorDemo3.apex");
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TB-356AutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='TB-356AutoTest' thn__Pax__c=3 thn__Hotel__c='" +
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
        String pmsBlockCode = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__Code__c");
        System.out.println(pmsBlockCode);
        Assert.assertEquals(pmsBlockCode, date.generateDate_plus6(0, 3) + "000000TB356A");
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("TB-356AutoTest");
        myceQuotes.changeDate(date.generateTodayDate3_plus(0, 5));
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Id='" + quoteID + "'", ORG_USERNAME);
        String quoteArrivalDate = JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Arrival_Date__c");
        StringBuilder updatedPmsBlockRecord = pmsBlock.getPMSBlockSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "'", ORG_USERNAME);
        String updatedPmsBlockCode = JsonParser2.getFieldValue(updatedPmsBlockRecord.toString(), "thn__Code__c");
        Assert.assertEquals(quoteArrivalDate, date.generateTodayDate2_plus(0, 5));
        Assert.assertEquals(updatedPmsBlockCode, pmsBlockCode);
    }

}
