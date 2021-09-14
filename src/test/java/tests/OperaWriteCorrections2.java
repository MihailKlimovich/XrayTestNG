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

public class OperaWriteCorrections2 extends BaseTest {

    @Test(priority = 1, description = "THY-582: Opera Write - Corrections")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-582: Opera Write - Corrections")
    @Story("PMS Account")
    public void testOperaWrite1() throws InterruptedException, IOException {
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
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
        StringBuilder pmsAccountRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__PMS_Account__c",
                "-w",
                "Name='TestOperaWriteAuto'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(pmsAccountRecord);
        String outboundRequest = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__Outbound_Request__c");
        String hapiId = JsonParser2.getFieldValue(pmsAccountRecord.toString(), "thn__HapiId__c");
        Assert.assertNull(outboundRequest);
        Assert.assertNotNull(hapiId);
    }

    @Test(priority = 2, description = "THY-582: Opera Write - Corrections")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-582: Opera Write - Corrections")
    @Story("Guest")
    public void testOperaWrite2() throws InterruptedException, IOException {
        StringBuilder guestRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='TestOperaWriteAuto'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(guestRecord);
        String outboundRequest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Outbound_Request__c");
        Assert.assertNull(outboundRequest);
    }

    @Test(priority = 3, description = "THY-582: Opera Write - Corrections")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-582: Opera Write - Corrections")
    @Story("Reservation")
    public void testOperaWrite3() throws InterruptedException, IOException {
        StringBuilder guestRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Guest__c",
                "-w",
                "thn__FirstName__c='TestOperaWriteAuto2'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(guestRecord);
        String guestId = JsonParser2.getFieldValue(guestRecord.toString(), "Id");
        StringBuilder reservationRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__Reservation__c",
                "-w",
                "thn__Customer__c='" + guestId + "'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(reservationRecord);
        String outboundRequest = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__Outbound_Request__c");
        String hapiId = JsonParser2.getFieldValue(reservationRecord.toString(), "thn__HapiId__c");
        Assert.assertNull(outboundRequest);
        Assert.assertNotNull(hapiId);
    }

    @Test(priority = 4, description = "THY-582: Opera Write - Corrections")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-582: Opera Write - Corrections")
    @Story("PMS Block")
    public void testOperaWrite4() throws InterruptedException, IOException {
        StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:get",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                "Name='TestPMSBlockAuto'",
                "-u",
                thynkPackUserName,
                "--json"});
        System.out.println(pmsBlockRecord);
        String outboundRequest = JsonParser2.getFieldValue(pmsBlockRecord.toString(), "thn__Outbound_Request__c");
        Assert.assertNull(outboundRequest);
    }

}
