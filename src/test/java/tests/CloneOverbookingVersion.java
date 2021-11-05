package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CloneOverbookingVersion extends BaseTest{


    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection - overbooking version")
    public void logIn() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
    }

    @Test(priority = 2, description = "Preconditions")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection - overbooking version")
    public void preconditions() throws InterruptedException, IOException {
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/RemoveOverbookingPermissionSet");
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTest'", ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTest2'", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='CloneOverbookingPackageAutoTest", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceOverbookingAuto1", ORG_USERNAME);
        resource.deleteResourceSFDX(SFDX, "Name='ResourceOverbookingAuto2", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String resourceID1 = resource.createResourceSFDX(SFDX, "Name='ResourceOverbookingAuto1' thn__Hotel__c='" +
                propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String resourceID2 = resource.createResourceSFDX(SFDX, "Name='ResourceOverbookingAuto2' thn__Hotel__c='" +
                propertyID + "' thn__Type__c='Meeting Room'", ORG_USERNAME);
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        String packageID = packages.createPackageSFDX(SFDX, "Name='CloneOverbookingPackageAutoTest'" +
                " thn__Hotel__c='" + propertyID + "'", ORG_USERNAME);
        packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='" + packageID +
                "' thn__Type__c='Meeting Room' thn__Product__c='" + meetingHalfDayID + "' thn__Start_Time__c=12:00" +
                " thn__End_Time__c=13:00 thn__Unit_Price__c=20 thn__VAT_Category__c=1", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingHalfDayID + "'", "thn__Resource__c='" +
                resourceID1 + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID + "'" +
                " thn__Product__c='" + meetingFullDayID + "'", "thn__Resource__c='" +
                resourceID2 + "'", ORG_USERNAME);
        String quoteID2 = myceQuotes.createQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTest2' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 2) + "" +
                " thn__Closed_Status__c='Won'", ORG_USERNAME);
        quoteMeetingPackages.createQuotePackageSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID2 + "'" +
                " thn__Package__c='" + packageID + "'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID2 + "'" +
                " thn__Product__c='" + meetingHalfDayID + "'", "thn__Resource__c='" +
                resourceID1 + "'", ORG_USERNAME);
        quoteMeetingRoom.createQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID2 + "'" +
                " thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        quoteMeetingRoom.updateQuoteMeetingRoomSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID2 + "'" +
                " thn__Product__c='" + meetingFullDayID + "'", "thn__Resource__c='" +
                resourceID2 + "'", ORG_USERNAME);
    }

    @Test(priority = 3, description = "Clone MYCE Quote, specify Arrival Date so it is equal to Arrival Date of the" +
            " Quote being cloned (User hasn’t overbooking permission)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection - overbooking version")
    public void сloneQuote1() throws InterruptedException, IOException {
        String expectedMessage =
                "You don’t have the permission to overbook resources. Default meeting room has been assigned.";
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTestClone'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneOverbookingAutoTest");
        myceQuotes.cloneMyceQuote("CloneOverbookingAutoTestClone", date.generateTodayDate_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTestClone'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder defaultResource = myceQuotes.soql(SFDX, "select thn__Resource_Id__c from" +
                "  thn__Default_Agile_Value__mdt where Label='Hotel Demo'", ORG_USERNAME);
        List<String> defaultResourceID = JsonParser2.
                getFieldValueSoql(defaultResource.toString(), "thn__Resource_Id__c");
        StringBuilder quoteMeetingRooms = myceQuotes.soql(SFDX, "SELECT thn__Resource__c," +
                " thn__Overbooking_message__c  FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" +
                clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(quoteMeetingRooms);
        List<String> quoteMeetingRoomsResource = JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "thn__Resource__c");
        List<String> quoteMeetingRoomsOverbookingMessage = JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "thn__Overbooking_message__c");
        Assert.assertEquals(quoteMeetingRoomsResource.get(0), defaultResourceID.get(0));
        Assert.assertEquals(quoteMeetingRoomsResource.get(1), defaultResourceID.get(0));
        Assert.assertEquals(quoteMeetingRoomsOverbookingMessage.get(0), expectedMessage);
        Assert.assertEquals(quoteMeetingRoomsOverbookingMessage.get(1), expectedMessage);
    }

    @Test(priority = 4, description = "Clone Quote package, specify  Date to clone so it is equal to Start date of the" +
            " Quote Package being cloned (User hasn’t overbooking permission)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection - overbooking version")
    public void сloneQuotePackage1() throws InterruptedException, IOException {
        String expectedMessage =
                "You don’t have the permission to overbook resources. Default meeting room has been assigned.";
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneOverbookingAutoTest");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate_plus(0 , 0), "Quote Package");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder defaultResource = myceQuotes.soql(SFDX, "select thn__Resource_Id__c from" +
                "  thn__Default_Agile_Value__mdt where Label='Hotel Demo'", ORG_USERNAME);
        List<String> defaultResourceID = JsonParser2.
                getFieldValueSoql(defaultResource.toString(), "thn__Resource_Id__c");
        StringBuilder quoteMeetingRooms = myceQuotes.soql(SFDX, "SELECT thn__Resource__c," +
                " thn__Overbooking_message__c FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + myceQuoteID +
                "' and thn__Package_Number__c='2'", ORG_USERNAME);
        System.out.println(quoteMeetingRooms);
        List<String> quoteMeetingRoomsResource = JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "thn__Resource__c");
        List<String> quoteMeetingRoomsOverbookingMessage = JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "thn__Overbooking_message__c");
        Assert.assertEquals(quoteMeetingRoomsResource.get(0), defaultResourceID.get(0));
        Assert.assertEquals(quoteMeetingRoomsOverbookingMessage.get(0), expectedMessage);
    }

    @Test(priority = 5, description = " Clone Quote meeting room (User hasn’t overbooking permission)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection - overbooking version")
    public void сloneQuoteMeetingRoom1() throws InterruptedException, IOException {
        String expectedMessage =
                "You don’t have the permission to overbook resources. Default meeting room has been assigned.";
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneOverbookingAutoTest");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate_plus(0 , 0), "Quote Meetings Room");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTest'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder defaultResource = myceQuotes.soql(SFDX, "select thn__Resource_Id__c from" +
                "  thn__Default_Agile_Value__mdt where Label='Hotel Demo'", ORG_USERNAME);
        List<String> defaultResourceID = JsonParser2.
                getFieldValueSoql(defaultResource.toString(), "thn__Resource_Id__c");
        StringBuilder quoteMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id, thn__Overbooking_message__c FROM" +
                " thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "' and thn__Resource__c='" +
                defaultResourceID.get(0) + "' and thn__Product__c='" + meetingFullDayID + "'", ORG_USERNAME);
        System.out.println(quoteMeetingRooms);
        List<String> cloneQuoteMeetingRoomsId = JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "Id");
        List<String> quoteMeetingRoomsOverbookingMessage = JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "thn__Overbooking_message__c");
        Assert.assertEquals(cloneQuoteMeetingRoomsId.size(), 1);
        Assert.assertEquals(quoteMeetingRoomsOverbookingMessage.get(0), expectedMessage);
    }

    @Test(priority = 6, description = "Clone MYCE Quote, specify Arrival Date so it is equal to Arrival Date of the" +
            " Quote being cloned (User has overbooking permission)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection - overbooking version")
    public void сloneQuote2() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        user.addPermissionSet(SFDX, "Overbooking_User", ORG_USERNAME);
        String expectedMessage = "Quote Meeting Room is overbooked.";
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTestClone2'", ORG_USERNAME);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneOverbookingAutoTest2");
        myceQuotes.cloneMyceQuote("CloneOverbookingAutoTestClone2", date.generateTodayDate_plus(0, 0));
        StringBuilder clonedQuoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTestClone2'",
                ORG_USERNAME);
        String clonedQuoteID= JsonParser2.getFieldValue(clonedQuoteRecord.toString(), "Id");
        StringBuilder defaultResource = myceQuotes.soql(SFDX, "select thn__Resource_Id__c from" +
                "  thn__Default_Agile_Value__mdt where Label='Hotel Demo'", ORG_USERNAME);
        List<String> defaultResourceID = JsonParser2.
                getFieldValueSoql(defaultResource.toString(), "thn__Resource_Id__c");
        StringBuilder quoteMeetingRooms = myceQuotes.soql(SFDX, "SELECT thn__Resource__c," +
                " thn__Overbooking_message__c  FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" +
                clonedQuoteID + "'", ORG_USERNAME);
        System.out.println(quoteMeetingRooms);
        List<String> quoteMeetingRoomsResource = JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "thn__Resource__c");
        List<String> quoteMeetingRoomsOverbookingMessage = JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "thn__Overbooking_message__c");
        Assert.assertNotEquals(quoteMeetingRoomsResource.get(0), defaultResourceID.get(0));
        Assert.assertNotEquals(quoteMeetingRoomsResource.get(1), defaultResourceID.get(0));
        Assert.assertEquals(quoteMeetingRoomsOverbookingMessage.get(0), expectedMessage);
        Assert.assertEquals(quoteMeetingRoomsOverbookingMessage.get(1), expectedMessage);
    }

    @Test(priority = 7, description = "Clone Quote package, specify  Date to clone so it is equal to Start date of the" +
            " Quote Package being cloned (User has overbooking permission)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection - overbooking version")
    public void сloneQuotePackage2() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneOverbookingAutoTest2");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate_plus(0 , 0), "Quote Package");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTest2'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder meetingHalfDayRecord = product.getProductSFDX(SFDX, "Name='MEETING HALF DAY'", ORG_USERNAME);
        String meetingHalfDayID = JsonParser2.getFieldValue(meetingHalfDayRecord.toString(), "Id");
        StringBuilder quoteMeetingRooms = myceQuotes.soql(SFDX, "SELECT thn__Resource__c," +
                " thn__Overbooking_message__c FROM thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + myceQuoteID +
                "' and thn__Product__c='" + meetingHalfDayID + "'", ORG_USERNAME);
        System.out.println(quoteMeetingRooms);
        List<String> quoteMeetingRoomsResource = JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "thn__Resource__c");
        Assert.assertEquals(quoteMeetingRoomsResource.get(0), quoteMeetingRoomsResource.get(1));
    }

    @Test(priority = 8, description = " Clone Quote meeting room (User has overbooking permission)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Clone Myce quote and clone selection - overbooking version")
    public void сloneQuoteMeetingRoom2() throws InterruptedException, IOException {
        //loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        myceQuotes.goToMyceQuotes();
        myceQuotes.openMyceQoteRecord("CloneOverbookingAutoTest2");
        myceQuotes.cloneRelatedRecord(date.generateTodayDate_plus(0 , 0), "Quote Meetings Room");
        StringBuilder quoteRecord = myceQuotes.getQuoteSFDX(SFDX, "Name='CloneOverbookingAutoTest2'", ORG_USERNAME);
        String myceQuoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        StringBuilder quoteMeetingRooms = myceQuotes.soql(SFDX, "SELECT Id, thn__Resource__c FROM" +
                " thn__Quote_Meeting_Room__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "' and thn__Product__c='" +
                meetingFullDayID + "'", ORG_USERNAME);
        System.out.println(quoteMeetingRooms);
        List<String> cloneQuoteMeetingRoomsId = JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "Id");
        List<String> quoteMeetingRoomsResource= JsonParser2.
                getFieldValueSoql(quoteMeetingRooms.toString(), "thn__Resource__c");
        Assert.assertEquals(cloneQuoteMeetingRoomsId.size(), 2);
        Assert.assertEquals(quoteMeetingRoomsResource.get(0), quoteMeetingRoomsResource.get(1));
    }






}
