package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class PackagLineTypeOfOccupancy extends BaseTest {

    @Test(priority = 1, description = "Create a Package (custom price == true) with a Quote Hotel Room as a Package" +
            " Line. Specify a Type of Occupancy on Quote Package Line. Create a MYCE Quote. Instantiate the created" +
            " package to MYCE Quote. Expected Result: The value in Type of Occupancy field on the Quote Package Line" +
            " is taken from the value of Type of Occupancy on Package Line. Type of Occupancy is also assigned to" +
            " Quote Hotel Room which is part of the Quote Package.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-702: Package line type of occupancy")
    public void case1() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PackageLineTypeOfOccupancyAutoTest", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='PackageTypeOfOccupancyAuto", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='PackageTypeOfOccupancyAuto' thn__Hotel__c='"
                + propertyID + "' thn__Custom_Price__c=true", ORG_USERNAME);
        String packageLineID = packageLine.createPackageLineSFDX(SFDX, "Name='Hotel Room' thn__Package__c='" +
                packageID + "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "'" +
                " thn__Start_Time__c=12:00 thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=0" +
                " thn__Space_Area__c='" + roomTypeQueenID + "' thn__Type_of_Occupancy__c='Triple'", ORG_USERNAME);
        StringBuilder pakageLineRecord = packageLine.getPackageLineSFDX(SFDX, "Id='" + packageLineID + "'",
                ORG_USERNAME);
        String packageLineTypeOfOccupancy = JsonParser2.getFieldValue(pakageLineRecord.
                toString(), "thn__Type_of_Occupancy__c");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='PackageLineTypeOfOccupancyAutoTest'" +
                        " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "'", ORG_USERNAME);
        StringBuilder quotePackageLineRecord = quotePackageLine.getQuotePackageLineSFDX(SFDX,
                "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        String quotePackageLineTypeOfOccupancy = JsonParser2.getFieldValue(quotePackageLineRecord.
                toString(), "thn__Type_of_Occupancy__c");
        StringBuilder quoteHotelRoomRecord  = quoteHotelRoom.
                getQuoteHotelRoomSFDX(SFDX, "thn__Quote_Package__c='" + quotePackageID + "'", ORG_USERNAME);
        String quoteHotelRoomTypeOfOccupancy = JsonParser2.getFieldValue(quoteHotelRoomRecord.
                toString(), "thn__Type_of_Occupancy__c");
        Assert.assertEquals(quotePackageLineTypeOfOccupancy, packageLineTypeOfOccupancy);
        Assert.assertEquals(quoteHotelRoomTypeOfOccupancy, packageLineTypeOfOccupancy);
        Assert.assertEquals(packageLineTypeOfOccupancy, "Triple");
    }

    @Test(priority = 2, description = "Go to the Quote Package Line and change the Type of Occupancy. Expected Result:" +
            " Type of Occupancy is changed to a new value. No erros is thrown.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-702: Package line type of occupancy")
    public void case2() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='PackageLineTypeOfOccupancyAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        quotePackageLine.updateQuotePackageLineSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Type_of_Occupancy__c='Quadruple'", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder quotePackageLineRecord = quotePackageLine.getQuotePackageLineSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quotePackageLineTypeOfOccupancy = JsonParser2.getFieldValue(quotePackageLineRecord.
                toString(), "thn__Type_of_Occupancy__c");
        String quoteHotelRoomTypeOfOccupancy = JsonParser2.getFieldValue(quoteHotelRoomRecord.
                toString(), "thn__Type_of_Occupancy__c");
        Assert.assertEquals(quoteHotelRoomTypeOfOccupancy, "Quadruple");
        Assert.assertEquals(quotePackageLineTypeOfOccupancy, "Quadruple");
    }

    @Test(priority = 3, description = "Create a Package (custom price == false) with a Quote Hotel Room as a Package" +
            " Line. Specify a Type of Occupancy on Quote Package Line. Create a MYCE Quote. Instantiate the created" +
            " package to MYCE Quote. Go to the Quote Package Line and change the Type of Occupancy. Expected Result:" +
            " Type of Occupancy is not changed to a new value. An error is thrown.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-702: Package line type of occupancy")
    public void case3() throws InterruptedException, IOException {
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PackageLineTypeOfOccupancyAutoTest2", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='PackageTypeOfOccupancyAuto2", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder room1NightRecord = product.getProductSFDX(SFDX, "Name='ROOM 1 NIGHT'", ORG_USERNAME);
        String room1NightID = JsonParser2.getFieldValue(room1NightRecord.toString(), "Id");
        StringBuilder roomTypeQueenRecord = roomType.getRoomTypeSFDX(SFDX, "Name='Queen'", ORG_USERNAME);
        String roomTypeQueenID = JsonParser2.getFieldValue(roomTypeQueenRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='PackageTypeOfOccupancyAuto2' thn__Hotel__c='"
                + propertyID + "' thn__Custom_Price__c=false", ORG_USERNAME);
        String packageLineID = packageLine.createPackageLineSFDX(SFDX, "Name='Hotel Room' thn__Package__c='" +
                packageID + "' thn__Type__c='Hotel Room' thn__Product__c='" + room1NightID + "'" +
                " thn__Start_Time__c=12:00 thn__End_Time__c=13:00 thn__Unit_Price__c=100 thn__VAT_Category__c=0" +
                " thn__Space_Area__c='" + roomTypeQueenID + "' thn__Type_of_Occupancy__c='Triple'", ORG_USERNAME);
        StringBuilder pakageLineRecord = packageLine.getPackageLineSFDX(SFDX, "Id='" + packageLineID + "'",
                ORG_USERNAME);
        String packageLineTypeOfOccupancy = JsonParser2.getFieldValue(pakageLineRecord.
                toString(), "thn__Type_of_Occupancy__c");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='PackageLineTypeOfOccupancyAutoTest2'" +
                        " thn__Pax__c=1 thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" +
                        date.generateTodayDate2_plus(0, 2) + " thn__Departure_Date__c=" +
                        date.generateTodayDate2_plus(0, 3) + " RecordTypeId='" + recordTypeID.get(0) + "'",
                ORG_USERNAME);
        String quotePackageID = quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" +
                quoteID + "' thn__Package__c='" + packageID + "'", ORG_USERNAME);
        quotePackageLine.updateQuotePackageLineSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'",
                "thn__Type_of_Occupancy__c='Quadruple'", ORG_USERNAME);
        StringBuilder quoteHotelRoomRecord = quoteHotelRoom.getQuoteHotelRoomSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        StringBuilder quotePackageLineRecord = quotePackageLine.getQuotePackageLineSFDX(SFDX,
                "thn__MYCE_Quote__c='" + quoteID + "'", ORG_USERNAME);
        String quotePackageLineTypeOfOccupancy = JsonParser2.getFieldValue(quotePackageLineRecord.
                toString(), "thn__Type_of_Occupancy__c");
        String quoteHotelRoomTypeOfOccupancy = JsonParser2.getFieldValue(quoteHotelRoomRecord.
                toString(), "thn__Type_of_Occupancy__c");
        Assert.assertEquals(quoteHotelRoomTypeOfOccupancy, "Triple");
        Assert.assertEquals(quotePackageLineTypeOfOccupancy, "Triple");
    }

    @Test(priority = 4, description = "Go to the Packages tab. Open the Package which has a Type of Occupancy" +
            " assigned to it’s Package Line. Clone this Package. Hit the Clone Package button on the Package." +
            " Expected Result: Open the cloned Package. The cloned Package has the Type of Occupancy assigned" +
            " to it’s Package Line.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-702: Package line type of occupancy")
    public void case4() throws InterruptedException, IOException {
        packages.deletePackageSFDX(SFDX, "Name='PackageTypeOfOccupancyAutoClone'", ORG_USERNAME);
        packages.goToPackages();
        packages.openPackageRecord("PackageTypeOfOccupancyAuto");
        packages.clonePackage("PackageTypeOfOccupancyAutoClone");
        StringBuilder originalPackage = packages.
                getPackageSFDX(SFDX, "Name='PackageTypeOfOccupancyAuto'", ORG_USERNAME);
        StringBuilder clonedPackage = packages.
                getPackageSFDX(SFDX, "Name='PackageTypeOfOccupancyAutoClone'", ORG_USERNAME);
        String originalPackageId= JsonParser2.
                getFieldValue(originalPackage.toString(), "Id");
        String clonedPackageId= JsonParser2.
                getFieldValue(clonedPackage.toString(), "Id");
        StringBuilder packageLineOriginal = packageLine.
                getPackageLineSFDX(SFDX, "thn__Package__c='" + originalPackageId + "'", ORG_USERNAME);
        StringBuilder packageLineClone = packageLine.
                getPackageLineSFDX(SFDX, "thn__Package__c='" + clonedPackageId + "'", ORG_USERNAME);
        String packageLineTypeOfOccupancyOriginal = JsonParser2.
                getFieldValue(packageLineOriginal.toString(), "thn__Type_of_Occupancy__c");
        String packageLineTypeOfOccupancyClone = JsonParser2.
                getFieldValue(packageLineClone.toString(), "thn__Type_of_Occupancy__c");
        Assert.assertEquals(packageLineTypeOfOccupancyClone, packageLineTypeOfOccupancyOriginal);
    }

}
