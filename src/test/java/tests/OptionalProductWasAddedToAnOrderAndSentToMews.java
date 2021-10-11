package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;
import java.util.List;

public class OptionalProductWasAddedToAnOrderAndSentToMews extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-132: Optional Product was added to an Order and sent to Mews")
    public void logIn() throws InterruptedException, IOException {
        loginPageForScratchOrg.logInOnScratchOrg2(driver, "https://test.salesforce.com/", TB_132_UserName, TB_132_Password);
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:auth:jwt:grant",
                "--clientid",
                TB_132_Key,
                "--jwtkeyfile",
                "/home/user/jdoe/JWT/server.key",
                "--username",
                TB_132_UserName,
                "--instanceurl",
                "https://test.salesforce.com"
        });

    }

    @Test(priority = 2, description = "Add quote product, Optional’ checkbox is set to ‘true’")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-132: Optional Product was added to an Order and sent to Mews")
    public void testOptionalProduct1() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestOptionalProductAuto1'",
                "-u",
                TB_132_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                TB_132_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordWines = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES'",
                "-u",
                TB_132_UserName,
                "--json"});
        String winesID = JsonParser2.getFieldValue(productRecordWines.toString(), "Id");
        System.out.println(winesID);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestOptionalProductAuto1' thn__Stage__c='4 - Closed' thn__Closed_Status__c='Won' thn__Pax__c=10" +
                        " thn__Hotel__c='" + propertyDemoID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                TB_132_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + winesID + "' thn__Optional__c=true",
                "-u",
                TB_132_UserName,
                "--json"});
        System.out.println(quoteProductResult);
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/BatchCreateOrders");
        Object orders = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Order__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                TB_132_UserName,
                "--json"});
        List<String> ordersID= JsonParser2.getFieldValueSoql(orders.toString(), "Id");
        Assert.assertEquals(ordersID.size(), 0);
    }

    @Test(priority = 3, description = "Add quote product, ‘Optional’ checkbox is set to ‘false")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-132: Optional Product was added to an Order and sent to Mews")
    public void testOptionalProduct2() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestOptionalProductAuto2'",
                "-u",
                TB_132_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                TB_132_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordWines = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='WINES'",
                "-u",
                TB_132_UserName,
                "--json"});
        String winesID = JsonParser2.getFieldValue(productRecordWines.toString(), "Id");
        System.out.println(winesID);
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestOptionalProductAuto2' thn__Stage__c='4 - Closed' thn__Closed_Status__c='Won' thn__Pax__c=10" +
                        " thn__Hotel__c='" + propertyDemoID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                TB_132_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteProductResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Product__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + winesID + "' thn__Optional__c=false",
                "-u",
                TB_132_UserName,
                "--json"});
        System.out.println(quoteProductResult);
        String quoteProductId = JsonParser2.getFieldValue(quoteProductResult.toString(), "id");
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/BatchCreateOrders");
        Object orders = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, Name FROM thn__Order__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                TB_132_UserName,
                "--json"});
        List<String> ordersID= JsonParser2.getFieldValueSoql(orders.toString(), "Id");
        List<String> ordersName= JsonParser2.getFieldValueSoql(orders.toString(),  "Name");
        StringBuilder orderLinesRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Order_Line__c",
                "-w",
                "thn__Order__c=" + ordersID.get(0) + "'",
                "-u",
                TB_132_UserName,
                "--json"});
        System.out.println(orderLinesRecord);
        String orderLineID = JsonParser2.getFieldValue(orderLinesRecord.toString(), "Id");
        System.out.println(orderLineID);
        String orderLineProduct = JsonParser2.getFieldValue(orderLinesRecord.toString(), "thn__Quote_Product__c");
        String orderLineName = JsonParser2.getFieldValue(orderLinesRecord.toString(), "Name");
        Assert.assertEquals(ordersID.size(), 1);
        Assert.assertNotNull(orderLineID);
        Assert.assertEquals(orderLineProduct, quoteProductId);
        Assert.assertEquals(orderLineName, "WINES");
        //Assert.assertEquals(ordersName, "[Stay]");
    }

    @Test(priority = 4, description = "Add quote meeting room, Optional’ checkbox is set to ‘true’")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-132: Optional Product was added to an Order and sent to Mews")
    public void testOptionalProduct3() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestOptionalProductAuto3'",
                "-u",
                TB_132_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                TB_132_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordMHD = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                TB_132_UserName,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecordMHD.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestOptionalProductAuto3' thn__Stage__c='4 - Closed' thn__Closed_Status__c='Won' thn__Pax__c=10" +
                        " thn__Hotel__c='" + propertyDemoID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                TB_132_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID +
                        "' thn__Optional__c=true",
                "-u",
                TB_132_UserName,
                "--json"});
        System.out.println(quoteMeetingRoomResult);
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/BatchCreateOrders");
        Object orders = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__Order__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                TB_132_UserName,
                "--json"});
        List<String> ordersID= JsonParser2.getFieldValueSoql(orders.toString(), "Id");
        Assert.assertEquals(ordersID.size(), 0);
    }

    @Test(priority = 5, description = "Add quote meeting room, Optional’ checkbox is set to ‘false’")
    @Severity(SeverityLevel.NORMAL)
    @Story("TB-132: Optional Product was added to an Order and sent to Mews")
    public void testOptionalProduct4() throws InterruptedException, IOException {
        SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                "Name='TestOptionalProductAuto4'",
                "-u",
                TB_132_UserName,
                "--json"});
        StringBuilder propertyDemoRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Hotel__c",
                "-w",
                "Name='Demo'",
                "-u",
                TB_132_UserName,
                "--json"});
        String propertyDemoID = JsonParser2.getFieldValue(propertyDemoRecord.toString(), "Id");
        StringBuilder productRecordMHD = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                "Name='MEETING HALF DAY'",
                "-u",
                TB_132_UserName,
                "--json"});
        String meetingHalfDayID = JsonParser2.getFieldValue(productRecordMHD.toString(), "Id");
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='TestOptionalProductAuto4' thn__Stage__c='4 - Closed' thn__Closed_Status__c='Won' thn__Pax__c=10" +
                        " thn__Hotel__c='" + propertyDemoID + "' thn__Arrival_Date__c=" + date.generateTodayDate2() +
                        " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 3),
                "-u",
                TB_132_UserName,
                "--json"});
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                "thn__MYCE_Quote__c='" + myceQuoteID + "' thn__Product__c='" + meetingHalfDayID +
                        "' thn__Optional__c=false",
                "-u",
                TB_132_UserName,
                "--json"});
        System.out.println(quoteMeetingRoomResult);
        String quoteMeetingRoomId = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/BatchCreateOrders");
        Object orders = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:soql:query",
                "-q",
                "SELECT Id, Name FROM thn__Order__c WHERE thn__MYCE_Quote__c='" + myceQuoteID + "'",
                "-u",
                TB_132_UserName,
                "--json"});
        List<String> ordersID= JsonParser2.getFieldValueSoql(orders.toString(), "Id");
        List<String> ordersName= JsonParser2.getFieldValueSoql(orders.toString(),  "Name");
        StringBuilder orderLinesRecord = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:get",
                "-s",
                "thn__Order_Line__c",
                "-w",
                "thn__Order__c=" + ordersID.get(0) + "'",
                "-u",
                TB_132_UserName,
                "--json"});
        System.out.println(orderLinesRecord);
        String orderLineID = JsonParser2.getFieldValue(orderLinesRecord.toString(), "Id");
        System.out.println(orderLineID);
        String orderLineQuoteMeetingRoom = JsonParser2.getFieldValue(orderLinesRecord.toString(), "thn__Quote_Meetings_Room__c");
        String orderLineName = JsonParser2.getFieldValue(orderLinesRecord.toString(), "Name");
        Assert.assertEquals(ordersID.size(), 1);
        Assert.assertNotNull(orderLineID);
        Assert.assertEquals(orderLineQuoteMeetingRoom, quoteMeetingRoomId);
        Assert.assertEquals(orderLineName, "DEFAULT - MEETING HALF DAY");
    }

}
