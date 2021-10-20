package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;

public class OperaWriteCorrections extends BaseTest {

    @Test(priority = 1, description = "THY-582: Opera Write - Corrections")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-582: Opera Write - Corrections")
    @Story("PMS Account")
    public void testOperaWrite1() throws InterruptedException, IOException {


        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:auth:jwt:grant",
                "--clientid",
                thynkPackKey,
                "--jwtkeyfile",
                "/home/user/jdoe/JWT/server.key",
                "--username",
                thynkPackUserName,
                "--instanceurl",
                thynkPackDevOrg
        });
        System.out.println(authorise);
        StringBuilder resultDelete = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__PMS_Account__c",
                "-w",
                "Name='TestOperaWriteAuto'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(resultDelete);
        StringBuilder pmsAccountResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__PMS_Account__c",
                "-v",
                "Name='TestOperaWriteAuto' thn__PropertyDetailsCode__c='ESDH' thn__PropertyDetailsChainCode__c='THYNK' thn__Type__c='GROUP'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(pmsAccountResult);
        String pmsAccountId = JsonParser2.getFieldValue(pmsAccountResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__PMS_Account__c",
                "-w",
                "id='" + pmsAccountId + "'",
                "-v",
                "thn__SendToPms__c=true",
                "-u",
                thynkPackUserName,
                "--json"});
        StringBuilder pmsAccountRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Account__c",
                "-w",
                "Id='" + pmsAccountId + "'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(pmsAccountRecord);
        String outboundRequest = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__Outbound_Request__c");
        Assert.assertNotNull(outboundRequest);
    }

    @Test(priority = 2, description = "THY-582: Opera Write - Corrections")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-582: Opera Write - Corrections")
    @Story("Guest")
    public void testOperaWrite2() throws InterruptedException, IOException {
        StringBuilder resultDelete = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='TestOperaWriteAuto'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(resultDelete);

        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='TestOperaWriteAuto' thn__LastName__c='TestOperaWrite' thn__PropertyDetailsCode__c='ESDH'" +
                        " thn__PropertyDetailsChainCode__c='THYNK' thn__Type__c='GUEST' thn__Source__c='Myce'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(guestResult);
        String guestId = JsonParser2.getFieldValue(guestResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Guest__c",
                "-w",
                "id='" + guestId + "'",
                "-v",
                "thn__SendToPms__c=true",
                "-u",
                thynkPackUserName,
                "--json"});
        StringBuilder guestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Guest__c",
                "-w",
                "Id='" + guestId + "'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(guestRecord);
        String outboundRequest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Outbound_Request__c");
        Assert.assertNotNull(outboundRequest);
    }

    @Test(priority = 3, description = "THY-582: Opera Write - Corrections")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-582: Opera Write - Corrections")
    @Story("Reservation")
    public void testOperaWrite3() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='TestOperaWriteAuto2'",
                "-u",
                thynkPackUserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Mews_Service__c",
                "-w",
                "Name='TestService'",
                "-u",
                thynkPackUserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Space_Area__c",
                "-w",
                "Name='TestRoomType'",
                "-u",
                thynkPackUserName,
                "--json"});
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__Rate__c",
                "-w",
                "Name='TestRate'",
                "-u",
                thynkPackUserName,
                "--json"});
        StringBuilder propertyRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "thn__Unique_Id__c='Demo'",
                "-u",
                thynkPackUserName,
                "--json"});
        String propertyID = JsonParser2.getFieldValue(propertyRecord.toString(), "Id");
        StringBuilder mewsServiceResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Mews_Service__c",
                "-v",
                "Name='TestService'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(mewsServiceResult);
        String mewsServiceId = JsonParser2.getFieldValue(mewsServiceResult.toString(), "id");
        StringBuilder roomTypeResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Space_Area__c",
                "-v",
                "Name='TestRoomType' thn__Hotel__c='" + propertyID + "'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(roomTypeResult);
        String roomTypeId = JsonParser2.getFieldValue(roomTypeResult.toString(), "id");
        StringBuilder rateResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Rate__c",
                "-v",
                "Name='TestRate' thn__Hotel__c='" + propertyID + "'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(rateResult);
        String rateId = JsonParser2.getFieldValue(rateResult.toString(), "id");

        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                "thn__FirstName__c='TestOperaWriteAuto2' thn__LastName__c='TestOperaWrite2' " +
                        "thn__PropertyDetailsCode__c='ESDH' thn__PropertyDetailsChainCode__c='THYNK'" +
                        " thn__Type__c='GUEST' thn__Source__c='Myce'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(guestResult);
        String guestId = JsonParser2.getFieldValue(guestResult.toString(), "id");
        StringBuilder reservationResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Reservation__c",
                "-v",
                "thn__Hotel__c='" + propertyID + "' thn__Mews_Service__c='" + mewsServiceId + "' thn__Customer__c='" +
                        guestId + "' thn__AdultCount__c=1 thn__ChildCount__c=1 thn__StartUtc__c=" +
                        date.generateTodayDate2() + " thn__EndUtc__c=" + date.generateTodayDate2_plus(0, 5) +
                        " thn__RequestedCategory__c='" + roomTypeId + "' thn__Pricing_Type__c='Rate pricing' " +
                        "thn__Rate__c='" + rateId + "' thn__PropertyDetailsCode__c='ESDH'" +
                        " thn__PropertyDetailsChainCode__c='THYNK' thn__ActualRoomTypeCode__c='DDBL'" +
                        " thn__ChannelCode__c='OTA' thn__MarketCode__c='TRAN' thn__SourceCode__c='GD'" +
                        " thn__PaymentMethod__c='CA' thn__RateCode__c='RACK' thn__RateAmount__c='100'" +
                        " thn__TotalRateAmount__c='100' thn__RoomNumber__c='555' thn__RoomCount__c='1'" +
                        " thn__RoomTypeCode__c='DDBL'",

                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(reservationResult);
        String reservationId = JsonParser2.getFieldValue(reservationResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__Reservation__c",
                "-w",
                "id='" + reservationId + "'",
                "-v",
                "thn__SendToPms__c=true",
                "-u",
                thynkPackUserName,
                "--json"});
        StringBuilder reservationRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Reservation__c",
                "-w",
                "Id='" + reservationId + "'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(reservationRecord);
        String outboundRequest = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Outbound_Request__c");
        Assert.assertNotNull(outboundRequest);
    }

    @Test(priority = 4, description = "THY-582: Opera Write - Corrections")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-582: Opera Write - Corrections")
    @Story("PMS Block")
    public void testOperaWrite4() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                "Name='TestPMSBlockAuto'",
                "-u",
                thynkPackUserName,
                "--json"});

        StringBuilder pmsBlockResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__PMS_Block__c",
                "-v",
                "Name='TestPMSBlockAuto' thn__PropertyDetailsCode__c='ESDH' thn__PropertyDetailsChainCode__c='THYNK'" +
                        " thn__InventoryBlockType__c='NONELASTIC' thn__Start__c=" + date.generateTodayDate2() +
                        " thn__End__c=" + date.generateTodayDate2_plus(0, 1) + " CurrencyIsoCode='EUR'" +
                        " thn__CurrencyCode__c='USD' thn__PaymentMethodCode__c='CC' thn__SegmentationMarket__c='GROUP'" +
                        " thn__SegmentationSource__c='CD' thn__SegmentationChannel__c='CRO' thn__PMS_Block_Status__c='DEF'" +
                        " thn__Code__c='a1g3X00000Py7rxQAB'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(pmsBlockResult);
        String pmsBlockId = JsonParser2.getFieldValue(pmsBlockResult.toString(), "id");
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:update",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                "id='" + pmsBlockId + "'",
                "-v",
                "thn__SendToPms__c=true",
                "-u",
                thynkPackUserName,
                "--json"});
        StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                "Id='" + pmsBlockId + "'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(pmsBlockRecord);
        String outboundRequest = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__Outbound_Request__c");
        Assert.assertNotNull(outboundRequest);
    }

}

