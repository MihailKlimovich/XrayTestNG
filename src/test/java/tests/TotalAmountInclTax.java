package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;
import utils.Listeners.TestListener;

import java.io.IOException;

@Listeners({TestListener.class})

public class TotalAmountInclTax extends BaseTest {

    @Test(priority = 1, description = "Create MYCE Quote. Add Quote hotel rooms, Quote Products, Quote meeting rooms." +
            " Result: thn__Total_Amount_incl_Tax__c field equals the thn__Total_incl_Tax__c field which is the sum of" +
            " thn_Total_Product_incl_Tax__c + thn_Total_Meeting_Room_incl_Tax__c + thn_Total_Hotel_Room_incl_Tax__c")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-523 Total_amount_incl_Tax__c")
    @Story("THY-523: Total_amount_incl_Tax__c")
    public void totalAmountInclTaxTest() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='TotalAmountInclTaxAutoTest'", ORG_USERNAME);
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='ROOM 1 NIGHT'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productID = JsonParser2.getFieldValue(productRecord.toString(), "Id");
        StringBuilder roomTypeRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='Queen'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(roomTypeRecord);
        String roomTypeID = JsonParser2.getFieldValue(roomTypeRecord.toString(), "Id");
        StringBuilder productRecord2 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES' thn__Hotel__c='" + propertyID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String productWinesID = JsonParser2.getFieldValue(productRecord2.toString(), "Id");
        StringBuilder productRecord3 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                ORG_USERNAME,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecord3.toString(), "Id");
        System.out.println(meetingHalfDayID);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TotalAmountInclTaxAutoTest' thn__Pax__c=10 thn__Hotel__c='" + propertyID + "'" +
                        " thn__Arrival_Date__c=" + date.generateTodayDate2() + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3),
                "-u",
                ORG_USERNAME,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productID + "'" +
                        " thn__Space_Area__c='" + roomTypeID + "' thn__Unit_Price__c=10" ,
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteHotelRoomId = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + productWinesID +
                        "' thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID + "'" +
                        " thn__Unit_Price__c=10",
                "-u",
                ORG_USERNAME,
                "--json"});
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        StringBuilder myceQuoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Id='" + myceQuoteID + "'",
                "-u",
                ORG_USERNAME,
                "--json"});
        System.out.println(myceQuoteRecord);
        Integer totalHotelRoomInclTax = JsonParser2.
                getFieldValueLikeInteger(myceQuoteRecord, "result", "thn__Total_Hotel_Room_incl_Tax__c");
        Integer totalProductInclTax = JsonParser2.
                getFieldValueLikeInteger(myceQuoteRecord, "result", "thn__Total_Product_incl_Tax__c");
        Integer totalMeetingRoomInclTax = JsonParser2.
                getFieldValueLikeInteger(myceQuoteRecord, "result", "thn__Total_Meeting_Room_incl_Tax__c");
        Integer sum = totalHotelRoomInclTax + totalProductInclTax + totalMeetingRoomInclTax;
        Integer totalAmountInclTax = JsonParser2.
                getFieldValueLikeInteger(myceQuoteRecord, "result", "thn__Total_Amount_incl_Tax__c");
        Integer totalInclTax = JsonParser2.
                getFieldValueLikeInteger(myceQuoteRecord, "result", "thn__Total_incl_Tax__c");
        Assert.assertEquals(totalInclTax, sum );
        Assert.assertEquals(totalInclTax, totalAmountInclTax);
    }

}
