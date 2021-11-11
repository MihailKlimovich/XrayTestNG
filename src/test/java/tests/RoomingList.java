package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class RoomingList extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Rooming list testing")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        //developerConsoleWindow.openDeveloperConsole();
        //developerConsoleWindow.openExecuteAnonymousWindow();
        //developerConsoleWindow.runApexCodeFromFile("src/main/Data/TemplateConfig");
    }

    @Test(priority = 2, description = "Create MYCE Quote, specify Reservation Guest, Add Quote hotel rooms, Send Quote" +
            " to Mews to have Reservations created")
    @Severity(SeverityLevel.NORMAL)
    @Story("Rooming list testing")
    public void preconditions() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='RoomingListAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room2NightsRecord = product.getProductSFDX(SFDX, "Name='ROOM 2 NIGHTS'", ORG_USERNAME);
        String room2NightsID = JsonParser2.getFieldValue(room2NightsRecord.toString(), "Id");
        StringBuilder roomTypeRecords = myceQuotes.
                soql(SFDX, "SELECT Id from thn__Space_Area__c where thn__Mews_Id__c!=null AND thn__Hotel__c='" +
                        propertyID + "'", ORG_USERNAME);
        System.out.println(guests);
        List<String> roomTypesId = JsonParser2.getFieldValueSoql(roomTypeRecords.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='RoomingListAutoTest' thn__Pax__c=3" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2), ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'" +
                " thn__SendToMews__c=true", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='1 - Qualify'" +
                " thn__SendToMews__c=false", ORG_USERNAME);
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                room2NightsID + "' thn__Space_Area__c='" + roomTypesId.get(0) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'" +
                " thn__SendToMews__c=true", ORG_USERNAME);
    }

    @Test(priority = 3, description = "Set the checkbox ‘Generate Rooming List’ on the Quote to ‘True’. Result:" +
            " ‘Rooming List’ records were created for each ‘Adult Count’ of the 'Reservation' on the Quote.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Rooming list testing")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='RoomingListAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'",
                "thn__Generate_Rooming_List__c=true", ORG_USERNAME);
        StringBuilder roomingList = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Rooming_List__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> roomingListID = JsonParser2.getFieldValueSoql(roomingList.toString(), "Id");
        Assert.assertEquals(roomingListID.size(), 3);
    }

    @Test(priority = 4, description = "")
    @Severity(SeverityLevel.NORMAL)
    @Story("Rooming list testing")
    public void case2() throws InterruptedException, IOException {
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("RoomingListAutoTest");
        myceQuotes.goToFiles();
        files.clickDownload();
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 1, 0, "Rost");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 1, 1, "Oryol");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 1, 2, "451899");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 1, 5, "Belarus");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 2, 0, "John");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 2, 1, "Doe");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 2, 2, "551454");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 2, 5, "American");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 3, 0, "Vito");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 3, 1, "Corleone");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 3, 2, "578977");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 3, 5, "Italian");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("RoomingListAutoTest");
        myceQuotes.uploadFile("/home/user/Downloads/Rooming List RoomingListAutoTest.xlsx");
    }

}
