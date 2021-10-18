package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import pageObject.SfdxCommand;

import java.io.IOException;

public class GuestManagementTesting extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest Management testing")
    public void logIn() throws InterruptedException, IOException {
        StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:auth:jwt:grant",
                "--clientid",
                CONSUMER_KEY,
                "--jwtkeyfile",
                SERVER_KEY_PATH,
                "--username",
                ORG_USERNAME,
                "--instanceurl",
                ORG_URL
        });
        System.out.println(authorise);
    }

    @Test(priority = 2, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("Create a new myce quote and fill values: company and contact, agent and contact, reservation guest name and" +
            " set 'Bill to' to Company.")
    public void precondition() throws InterruptedException, IOException {
        StringBuilder accountResult1 = SfdxCommand.runLinuxCommand1(new String[]{
                SFDX,
                "force:data:record:create",
                "-s",
                "Account",
                "-v",
                "Name='TestAccountWithCommission' thn__Comm_Activity__c=15 thn__Beverage__c=15 thn__Comm_Equipment__c=15" +
                        " thn__Food__c=15 thn__Room__c=15 thn__Meeting_Room__c=15 thn__Other__c=15 thn__Comm_Packages__c=15",
                "-u",
                ORG_USERNAME,
                "--json"});
        String accountId1 = JsonParser2.getFieldValue(accountResult1.toString(), "id");

    }

}
