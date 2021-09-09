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

public class TestInWork extends BaseTest{

    @Test(priority = 1, description = "THY-516: Multi edit on quote's related list")
    @Severity(SeverityLevel.NORMAL)
    @Description("THY-516: Multi edit on quote's related list")
    @Story("THY-516: Multi edit on quote's related list")
    public void RequestAgent_case1() throws InterruptedException, IOException {
        /*StringBuilder authorise = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:auth:jwt:grant",
                "--clientid",
                key,
                "--jwtkeyfile",
                "/home/user/jdoe/JWT/server.key",
                "--username",
                ALIAS,
                "--instanceurl",
                "https://test.salesforce.com"
        });
        System.out.println(authorise);
        Object byPassRecord = SfdxCommand.runLinuxCommand1(new String[]{"/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:soql:query",
                "-q",
                "SELECT Id FROM thn__bypass__c",
                "-u",
                ALIAS,
                "--json"});
        System.out.println(byPassRecord);*/


    }
}
