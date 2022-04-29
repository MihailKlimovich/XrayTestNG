package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class GroupBookingPackageLines extends BaseTest {

    @Test(priority = 1, description = "Create Availability records. Create a MYCE Quote. Arrival Date = today + 2" +
            " days, Departure Date = today + 7 days. Instantiate a Quote Hotel Room via Group Booking Component." +
            " For the Room Type select the Room Type for which you created Availability records. Type of Occupancy" +
            " = Double. Expected result: On the Group Booking Component the created hotel Room is displayed. First" +
            " two letter of the Type of Occupancy are displayed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-712: Group booking - package lines")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='GroupBookingPackageLinesAutoTest'", ORG_USERNAME);
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
                " thn__Number_of_Availabilities__c=100 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 1)
                + " thn__Number_of_Availabilities__c=100 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 2)
                + " thn__Number_of_Availabilities__c=100 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 3)
                + " thn__Number_of_Availabilities__c=100 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 4)
                + " thn__Number_of_Availabilities__c=100 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 5)
                + " thn__Number_of_Availabilities__c=100 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 6)
                + " thn__Number_of_Availabilities__c=100 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        availability.createAvailabilitySFDX(SFDX, "thn__Date__c=" + date.generateTodayDate2_plus(0, 7)
                + " thn__Number_of_Availabilities__c=100 thn__Inventory__c=30 thn__Space_Area__c='" + roomTypeQueenID +
                "'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='GroupBookingPackageLinesAutoTest'" +
                " thn__Pax__c=5 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                date.generateTodayDate2_plus(0, 7) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        myceQuotes.goToMyceQuotes().openMyceQoteRecord("GroupBookingPackageLinesAutoTest");
        myceQuotes.clickGroupBookingTab();
        groupBookingComponent.clickNewButton();
        groupBookingComponent.
                cteateQuoteHotelRoom("DEFAULT", "Double", "1", "Queen");
        /*groupBookingComponent.
                changePricePerDayAndQuantity("1", "1", "150", "10");
        groupBookingComponent.
                changePricePerDayAndQuantity("3", "2", "160", "8");*/
        groupBookingComponent.clickSaveButton();
        String quoteHotelRoomName = groupBookingComponent.readQuoteHotelRoomName("1");
        Assert.assertEquals(quoteHotelRoomName, "Queen / DO");
    }

    @Test(priority = 2, description = "Create a Package that Contains five Hotel Room as a Package Line. Each" +
            " Package Line should Have different Type of Occupancy: Single, Double, Tripple, Twin, Quadrupple." +
            " Instantiate a Quote Package that we created. Expected result: Group Booking component is updated" +
            " with the instantiated Quote Hotel Rooms. Quote Hotel Rooms are instantiated for the duration of" +
            " the Quote Package. Type of Occupancy is represented by the first two letters after Room type. Delete" +
            " Button isn’t active for the Quote Hotel Rooms that are part of the package. Quote Package color is" +
            " displayed before the Room Type instead of the Clone button. The values (check, quantity, price) are" +
            " read only.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-712: Group booking - package lines")
    public void case2() throws InterruptedException, IOException {
        packages.deletePackageSFDX(SFDX, "Name='GroupBookingPackageLinesAutoPackage", ORG_USERNAME);
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='GroupBookingPackageLinesAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder rateRecord = rate.getRateSFDX(SFDX, "Name='DEFAULT' thn__Hotel__c='" + propertyID + "'",
                ORG_USERNAME);
        String rateID= JsonParser2.getFieldValue(rateRecord.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='GroupBookingPackageLinesAutoPackage'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Single' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1 thn__Space_Area__c='" +
                roomTypeQueenID + "' thn__Type_of_Occupancy__c='Single' thn__Rate__c='" + rateID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Double' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1 thn__Space_Area__c='" +
                roomTypeQueenID + "' thn__Type_of_Occupancy__c='Double' thn__Rate__c='" + rateID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Triple' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1 thn__Space_Area__c='" +
                roomTypeQueenID + "' thn__Type_of_Occupancy__c='Triple' thn__Rate__c='" + rateID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Twin' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1 thn__Space_Area__c='" +
                roomTypeQueenID + "' thn__Type_of_Occupancy__c='Twin' thn__Rate__c='" + rateID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Quadruple' thn__Package__c='" + packageID +
                "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1 thn__Space_Area__c='" +
                roomTypeQueenID + "' thn__Type_of_Occupancy__c='Quadruple' thn__Rate__c='" + rateID + "'", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Package__c='" + packageID + "' thn__Start_Date__c=" +
                date.generateTodayDate2_plus(0, 2)  + " thn__End_Date__c=" +
                date.generateTodayDate2_plus(0, 5) , ORG_USERNAME);

    }

}
