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
        String guestID = guests.createGuestSFDX(SFDX, "thn__LastName__c='RoomingListAutoTest' thn__Hotel__c='"
                + propertyID + "' thn__Send_to_Mews__c=true", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='RoomingListAutoTest' thn__Pax__c=3" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2), ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'",
                "thn__Reservation_Guest__c='" + guestID + "'", ORG_USERNAME);
        quoteHotelRoom.createQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "' thn__Product__c='" +
                room2NightsID + "' thn__Space_Area__c='" + roomTypesId.get(1) + "'", ORG_USERNAME);
        myceQuotes.updateQuoteSFDX(SFDX, "Id='" + quoteID + "'", "thn__Stage__c='2 - Propose'" +
                " thn__SendToMews__c=true", ORG_USERNAME);
        StringBuilder reservations = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Reservation__c WHERE" +
                " thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        List<String> reservationsID = JsonParser2.getFieldValueSoql(reservations.toString(), "Id");
        Assert.assertEquals(reservationsID.size(), 3);

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

    @Test(priority = 4, description = "Complete the file and upload it using the upload component on quote. Expected" +
            " result: when phone or email are completed, guest is created ")
    @Severity(SeverityLevel.NORMAL)
    @Story("Rooming list testing")
    public void case2() throws InterruptedException, IOException {
        files.deleteFile("/home/user/Downloads/Rooming List RoomingListAutoTest.xlsx");
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("RoomingListAutoTest");
        myceQuotes.goToFiles();
        files.clickDownload("Rooming List RoomingListAutoTest");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 1, 0, "Werty");
        files.updateXLS("Rooming List RoomingListAutoTest.xlsx", 1, 1, "Kukin");
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
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='RoomingListAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder roomingListRecord1 = roomingList.getRoomungListSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Last_Name__c='Kukin'", ORG_USERNAME);
        StringBuilder roomingListRecord2 = roomingList.getRoomungListSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Last_Name__c='Doe'", ORG_USERNAME);
        StringBuilder roomingListRecord3 = roomingList.getRoomungListSFDX(SFDX, "thn__MYCE_Quote__c='"
                + quoteID + "' thn__Last_Name__c='Corleone'", ORG_USERNAME);
        String phone1 = JsonParser2.getFieldValue(roomingListRecord1.toString(), "thn__Phone__c");
        String phone2 = JsonParser2.getFieldValue(roomingListRecord2.toString(), "thn__Phone__c");
        String phone3 = JsonParser2.getFieldValue(roomingListRecord3.toString(), "thn__Phone__c");
        String nationality1 = JsonParser2.getFieldValue(roomingListRecord1.toString(), "thn__Nationality__c");
        String nationality2 = JsonParser2.getFieldValue(roomingListRecord2.toString(), "thn__Nationality__c");
        String nationality3 = JsonParser2.getFieldValue(roomingListRecord3.toString(), "thn__Nationality__c");
        String guest1 = JsonParser2.getFieldValue(roomingListRecord1.toString(), "thn__Guest__c");
        String guest2 = JsonParser2.getFieldValue(roomingListRecord2.toString(), "thn__Guest__c");
        String guest3 = JsonParser2.getFieldValue(roomingListRecord3.toString(), "thn__Guest__c");
        Assert.assertEquals(phone1, "451899");
        Assert.assertEquals(phone2, "551454");
        Assert.assertEquals(phone3, "578977");
        Assert.assertEquals(nationality1, "Belarus");
        Assert.assertEquals(nationality2, "American");
        Assert.assertEquals(nationality3, "Italian");
        Assert.assertNotNull(guest1);
        Assert.assertNotNull(guest2);
        Assert.assertNotNull(guest3);
    }

}
