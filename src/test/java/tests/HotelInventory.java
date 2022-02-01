package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HotelInventory extends BaseTest {

    @BeforeClass
    public void classLevelSetup() {
        ChromeOptions options= new ChromeOptions();
        options.addArguments("--disable-cache");
        options.addArguments("--disk-cache-size=1");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-data-dir=/tmp/temp_profile");
        options.addArguments(" --whitelisted-ips=\"\"");
        options.addArguments("--headless", "window-size=1920,1024", "--no-sandbox");
        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-624: Hotel Inventory")
    public void logIn() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Create a MYCE Quote, create Availability records")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-624: Hotel Inventory")
    public void precondition() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='HotelInventoryAutoTest'", ORG_USERNAME);
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder avalabilityRecord = myceQuotes.soql(SFDX, "SELECT Id FROM thn__Availability__c WHERE" +
                        " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<String> avalabilityID = JsonParser2.getFieldValueSoql(avalabilityRecord.toString(), "Id");
        int quantity = avalabilityID.size();
        int index = 0;
        while (quantity > 0) {
            availability.deleteAvailabilitySFDX(SFDX, "Id='" + avalabilityID.get(index) + "'", ORG_USERNAME);
            quantity--;
            index++;
        }
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2() + "" +
                " thn__Number_of_Availabilities__c=19 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 1)
                + " thn__Number_of_Availabilities__c=25 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 2)
                + " thn__Number_of_Availabilities__c=15 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 3)
                + " thn__Number_of_Availabilities__c=25 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 4)
                + " thn__Number_of_Availabilities__c=15 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 5)
                + " thn__Number_of_Availabilities__c=15 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 6)
                + " thn__Number_of_Availabilities__c=25 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 7)
                + " thn__Number_of_Availabilities__c=15 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='HotelInventoryAutoTest' thn__Pax__c=500" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3) + " RecordTypeId='"
                + recordTypeID.get(0) + "'", ORG_USERNAME);

    }

    @Test(priority = 3, description = "Open the new Create quote hotel room component via the New (overbooking)" +
            " button: Product = Room 1 Night, Room Type = Queen, Pax = 10, Day number = Null, Arrival Date Time =" +
            " Null, Departure Date Time = Null. Expected result: A Quote Hotel Room was instantiated. Remaining" +
            " Inventory on the Availabilities records was updated.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-624: Hotel Inventory")
    public void case1() throws InterruptedException, IOException {
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("HotelInventoryAutoTest");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.clickNewOverbookingButton();
        createQuoteHotelRoomComponent.createNewQuoteHotelRoom("ROOM 1 NIGHT", "Queen", "10",
                "", "", "", "", "");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='HotelInventoryAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "thn__MYCE_Quote__c="
                + myceQuoteID + "'", ORG_USERNAME);
        System.out.println(quoteHotelRoomRecord);
        String productQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Product__c");
        String roomTypeQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Space_Area__c");
        String numberQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Pax__c");
        String arrivalDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Arrival_Date__c");
        String departureDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Departure_Date__c");
        String dayNumberDateQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Day_Number__c");
        String overbookingStatusQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Overbooking_status__c");
        StringBuilder avalabilityRecord1 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2() + " AND thn__Space_Area__c='"
                + roomTypeQueenID + "'", ORG_USERNAME);
        System.out.println(avalabilityRecord1);
        List<Integer> remainingInvventory1= JsonParser2.
                getFieldValueSoql2(avalabilityRecord1.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord2 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 1) + " AND" +
                " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory2= JsonParser2.
                getFieldValueSoql2(avalabilityRecord2.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord3 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory3= JsonParser2.
                getFieldValueSoql2(avalabilityRecord3.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord4 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 3) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory4= JsonParser2.
                getFieldValueSoql2(avalabilityRecord4.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord5 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 4) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory5= JsonParser2.
                getFieldValueSoql2(avalabilityRecord5.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord6 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 5) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory6= JsonParser2.
                getFieldValueSoql2(avalabilityRecord6.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord7 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 6) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory7= JsonParser2.
                getFieldValueSoql2(avalabilityRecord7.toString(), "thn__Remaining_Inventory__c");
        Assert.assertEquals(productQuoteHotelRoom, room1NightID);
        Assert.assertEquals(roomTypeQuoteHotelRoom, roomTypeQueenID);
        Assert.assertEquals(numberQuoteHotelRoom, "10");
        Assert.assertEquals(arrivalDateQuoteHotelRoom, date.generateTodayDate2());
        Assert.assertEquals(departureDateQuoteHotelRoom, date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(dayNumberDateQuoteHotelRoom, null);
        Assert.assertEquals(overbookingStatusQuoteHotelRoom, null);
        Assert.assertEquals(remainingInvventory1.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory2.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory3.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory4.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory5.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory6.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory7.get(0).intValue(), 30);
    }

    @Test(priority = 4, description = "Instantiate another Quote Hotel Room to the MYCE Quote via the New" +
            " (overbooking) button: Product = Room 1 Night, Room Type = Single, Pax = 100, Day number = Null," +
            " Arrival Date Time = today, Departure Date Time = today + 2. Since the remaining inventory is less" +
            " than booked rooms we get the Dialogue that the Quote hotel Room is in conflict whether we wish to" +
            " Overbook, Select Yes. Expected result: A Quote Hotel Room was instantiated with: Overbooking Status" +
            " = Awaiting approval. Remaining Inventory on the Availabilities records was updated. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-624: Hotel Inventory")
    public void case2() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("HotelInventoryAutoTest");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.clickNewOverbookingButton();
        createQuoteHotelRoomComponent.createNewQuoteHotelRoom("ROOM 1 NIGHT", "Queen", "100",
                date.generateTodayDate3_plus(0, 0), "",
                date.generateTodayDate3_plus(0, 1),"", "");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='HotelInventoryAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomSOQL = myceQuotes.
                soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" +
                        quoteID + "' AND thn__Pax__c=100", ORG_USERNAME);
        System.out.println(quoteHotelRoomSOQL);
        List<String> quoteHotelRoomID = JsonParser2.getFieldValueSoql(quoteHotelRoomSOQL.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='" +
                quoteHotelRoomID.get(0) + "'", ORG_USERNAME);
        String overbookingStatusQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Overbooking_status__c");
        StringBuilder avalabilityRecord1 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2() + " AND thn__Space_Area__c='"
                + roomTypeQueenID + "'", ORG_USERNAME);
        System.out.println(avalabilityRecord1);
        List<Integer> remainingInvventory1= JsonParser2.
                getFieldValueSoql2(avalabilityRecord1.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord2 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 1) + " AND" +
                " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory2= JsonParser2.
                getFieldValueSoql2(avalabilityRecord2.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord3 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory3= JsonParser2.
                getFieldValueSoql2(avalabilityRecord3.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord4 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 3) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory4= JsonParser2.
                getFieldValueSoql2(avalabilityRecord4.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord5 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 4) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory5= JsonParser2.
                getFieldValueSoql2(avalabilityRecord5.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord6 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 5) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory6= JsonParser2.
                getFieldValueSoql2(avalabilityRecord6.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord7 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 6) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory7= JsonParser2.
                getFieldValueSoql2(avalabilityRecord7.toString(), "thn__Remaining_Inventory__c");
        Assert.assertEquals(overbookingStatusQuoteHotelRoom, "Awaiting approval");
        Assert.assertEquals(remainingInvventory1.get(0).intValue(), -80);
        Assert.assertEquals(remainingInvventory2.get(0).intValue(), -80);
        Assert.assertEquals(remainingInvventory3.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory4.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory5.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory6.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory7.get(0).intValue(), 30);
    }

    @Test(priority = 5, description = "Instantiate another Quote Hotel Room to the MYCE Quote via the New" +
            " (overbooking) button: Product = Room 1 Night, Room Type = Single, Pax = 100, Day number = 1," +
            " Arrival Date Time = null, Departure Date Time = null. Since the remaining inventory is less" +
            " than booked rooms we get the Dialogue that the Quote hotel Room is in conflict whether we wish to" +
            " Overbook, Select Yes. Expected result: A Quote Hotel Room was instantiated with: Overbooking Status" +
            " = Awaiting approval. Remaining Inventory on the Availabilities records was updated. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-624: Hotel Inventory")
    public void case3() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("HotelInventoryAutoTest");
        myceQuotes.openHotelRooms();
        quoteHotelRoom.clickNewOverbookingButton();
        createQuoteHotelRoomComponent.createNewQuoteHotelRoom("ROOM 1 NIGHT", "Queen", "100",
                "", "", "","", "1");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='HotelInventoryAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder quoteHotelRoomSOQL = myceQuotes.
                soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" +
                        quoteID + "' AND thn__Day_Number__c=1", ORG_USERNAME);
        System.out.println(quoteHotelRoomSOQL);
        List<String> quoteHotelRoomID = JsonParser2.getFieldValueSoql(quoteHotelRoomSOQL.toString(), "Id");
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX, "Id='" +
                quoteHotelRoomID.get(0) + "'", ORG_USERNAME);
        String overbookingStatusQuoteHotelRoom = JsonParser2.
                getFieldValue(quoteHotelRoomRecord.toString(), "thn__Overbooking_status__c");
        StringBuilder avalabilityRecord1 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2() + " AND thn__Space_Area__c='"
                + roomTypeQueenID + "'", ORG_USERNAME);
        System.out.println(avalabilityRecord1);
        List<Integer> remainingInvventory1= JsonParser2.
                getFieldValueSoql2(avalabilityRecord1.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord2 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 1) + " AND" +
                " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory2= JsonParser2.
                getFieldValueSoql2(avalabilityRecord2.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord3 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory3= JsonParser2.
                getFieldValueSoql2(avalabilityRecord3.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord4 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 3) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory4= JsonParser2.
                getFieldValueSoql2(avalabilityRecord4.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord5 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 4) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory5= JsonParser2.
                getFieldValueSoql2(avalabilityRecord5.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord6 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 5) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory6= JsonParser2.
                getFieldValueSoql2(avalabilityRecord6.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord7 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 6) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory7= JsonParser2.
                getFieldValueSoql2(avalabilityRecord7.toString(), "thn__Remaining_Inventory__c");
        Assert.assertEquals(overbookingStatusQuoteHotelRoom, "Awaiting approval");
        Assert.assertEquals(remainingInvventory1.get(0).intValue(), -180);
        Assert.assertEquals(remainingInvventory2.get(0).intValue(), -180);
        Assert.assertEquals(remainingInvventory3.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory4.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory5.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory6.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory7.get(0).intValue(), 30);
    }

    @Test(priority = 6, description = "Use the change dates of the MYCE Quote via the Change date flow button on the" +
            " MYCE Quote. Change the date of the MYCE Quote to today + 3 days. Expected result: MYCE Quote dates were" +
            " updated. Availabilities were updated as well.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-624: Hotel Inventory")
    public void case4() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("HotelInventoryAutoTest");
        myceQuotes.changeDate(date.generateTodayDate3_plus(0, 3));
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='HotelInventoryAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteArrivalDate= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Arrival_Date__c");
        String quoteDepartureDate= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Departure_Date__c");
        StringBuilder avalabilityRecord1 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2() + " AND thn__Space_Area__c='"
                + roomTypeQueenID + "'", ORG_USERNAME);
        System.out.println(avalabilityRecord1);
        List<Integer> remainingInvventory1= JsonParser2.
                getFieldValueSoql2(avalabilityRecord1.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord2 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 1) + " AND" +
                " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory2= JsonParser2.
                getFieldValueSoql2(avalabilityRecord2.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord3 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory3= JsonParser2.
                getFieldValueSoql2(avalabilityRecord3.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord4 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 3) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory4= JsonParser2.
                getFieldValueSoql2(avalabilityRecord4.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord5 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 4) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory5= JsonParser2.
                getFieldValueSoql2(avalabilityRecord5.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord6 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 5) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory6= JsonParser2.
                getFieldValueSoql2(avalabilityRecord6.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord7 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 6) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory7= JsonParser2.
                getFieldValueSoql2(avalabilityRecord7.toString(), "thn__Remaining_Inventory__c");
        Assert.assertEquals(quoteArrivalDate, date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(quoteDepartureDate, date.generateTodayDate2_plus(0, 6));
        Assert.assertEquals(remainingInvventory1.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory2.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory3.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory4.get(0).intValue(), -180);
        Assert.assertEquals(remainingInvventory5.get(0).intValue(), -180);
        Assert.assertEquals(remainingInvventory6.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory7.get(0).intValue(), 20);
    }

    @Test(priority = 7, description = "Open the last created Quote Hotel room which has Day Number = 1. Change the" +
            " Mews State to the state that is specified in Custom Settings > Stages > Hotel Room cancelled stage." +
            " Mews State = Canceled. Expected result: The Availabilities for the dates of the Cancelled Hotel Room" +
            " got updated")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-624: Hotel Inventory")
    public void case5() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='HotelInventoryAutoTest'",
                ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder quoteHotelRoomSOQL = myceQuotes.
                soql(SFDX, "SELECT Id FROM thn__Quote_Hotel_Room__c WHERE thn__MYCE_Quote__c='" +
                        quoteID + "' AND thn__Day_Number__c=1", ORG_USERNAME);
        System.out.println(quoteHotelRoomSOQL);
        List<String> quoteHotelRoomID = JsonParser2.getFieldValueSoql(quoteHotelRoomSOQL.toString(), "Id");
        quoteHotelRoom.updateQuoteHotelRoomSFDX(SFDX, "Id='" + quoteHotelRoomID.get(0) + "'",
                "thn__Mews_State__c='Canceled'", ORG_USERNAME);
        StringBuilder avalabilityRecord1 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2() + " AND thn__Space_Area__c='"
                + roomTypeQueenID + "'", ORG_USERNAME);
        System.out.println(avalabilityRecord1);
        List<Integer> remainingInvventory1= JsonParser2.
                getFieldValueSoql2(avalabilityRecord1.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord2 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 1) + " AND" +
                " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory2= JsonParser2.
                getFieldValueSoql2(avalabilityRecord2.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord3 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory3= JsonParser2.
                getFieldValueSoql2(avalabilityRecord3.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord4 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 3) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory4= JsonParser2.
                getFieldValueSoql2(avalabilityRecord4.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord5 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 4) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory5= JsonParser2.
                getFieldValueSoql2(avalabilityRecord5.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord6 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 5) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory6= JsonParser2.
                getFieldValueSoql2(avalabilityRecord6.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord7 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 6) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory7= JsonParser2.
                getFieldValueSoql2(avalabilityRecord7.toString(), "thn__Remaining_Inventory__c");
        Assert.assertEquals(remainingInvventory1.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory2.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory3.get(0).intValue(), 30);
        Assert.assertEquals(remainingInvventory4.get(0).intValue(), -80);
        Assert.assertEquals(remainingInvventory5.get(0).intValue(), -80);
        Assert.assertEquals(remainingInvventory6.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory7.get(0).intValue(), 20);
    }

    @Test(priority = 8, description = "Clone the MYCE Quote via the Clone MYCE Quote button on the Quote." +
            " Arrival Date = today date. Pax = 10. Expected result: Arrival Date = today  date, Departure Date" +
            " = today date + 3. Availabilities were updated as well.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-624: Hotel Inventory")
    public void case6() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneHotelInventoryAutoTest'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("HotelInventoryAutoTest");
        myceQuotes.cloneMyceQuote_changeDateAndPax("CloneHotelInventoryAutoTest",
                date.generateTodayDate3_plus(0, 0), "10");
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneHotelInventoryAutoTest'",
                ORG_USERNAME);
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        String quoteArrivalDate= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "thn__Arrival_Date__c");
        String quoteDepartureDate= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "thn__Departure_Date__c");
        StringBuilder avalabilityRecord1 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2() + " AND thn__Space_Area__c='"
                + roomTypeQueenID + "'", ORG_USERNAME);
        System.out.println(avalabilityRecord1);
        List<Integer> remainingInvventory1= JsonParser2.
                getFieldValueSoql2(avalabilityRecord1.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord2 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 1) + " AND" +
                " thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory2= JsonParser2.
                getFieldValueSoql2(avalabilityRecord2.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord3 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory3= JsonParser2.
                getFieldValueSoql2(avalabilityRecord3.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord4 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 3) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory4= JsonParser2.
                getFieldValueSoql2(avalabilityRecord4.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord5 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 4) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory5= JsonParser2.
                getFieldValueSoql2(avalabilityRecord5.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord6 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 5) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory6= JsonParser2.
                getFieldValueSoql2(avalabilityRecord6.toString(), "thn__Remaining_Inventory__c");
        StringBuilder avalabilityRecord7 = myceQuotes.soql(SFDX, "SELECT thn__Remaining_Inventory__c FROM" +
                " thn__Availability__c WHERE thn__Date__c=" + date.generateTodayDate2_plus(0, 6) + "" +
                " AND thn__Space_Area__c='" + roomTypeQueenID + "'", ORG_USERNAME);
        List<Integer> remainingInvventory7= JsonParser2.
                getFieldValueSoql2(avalabilityRecord7.toString(), "thn__Remaining_Inventory__c");
        Assert.assertEquals(quoteArrivalDate, date.generateTodayDate2_plus(0, 0));
        Assert.assertEquals(quoteDepartureDate, date.generateTodayDate2_plus(0, 3));
        Assert.assertEquals(remainingInvventory1.get(0).intValue(), 0);
        Assert.assertEquals(remainingInvventory2.get(0).intValue(), 0);
        Assert.assertEquals(remainingInvventory3.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory4.get(0).intValue(), -90);
        Assert.assertEquals(remainingInvventory5.get(0).intValue(), -80);
        Assert.assertEquals(remainingInvventory6.get(0).intValue(), 20);
        Assert.assertEquals(remainingInvventory7.get(0).intValue(), 20);
    }




}
