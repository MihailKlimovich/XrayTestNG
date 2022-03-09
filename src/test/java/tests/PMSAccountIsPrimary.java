package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class PMSAccountIsPrimary extends BaseTest {

    @Test(priority = 1, description = "Create Account. Create two PMS Account for created Account where" +
            " thn__PMS_DB__c=1. Expected result: Checkbox Is Primary on first PMS Account = true, on second = false.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-668: PMS account is primary")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        accounts.deleteAccountSFDX(SFDX, "Name='PMSAccountIsPrimaryAutoTest1'", ORG_USERNAME);
        pmsAccount.deletePMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest1", ORG_USERNAME);
        pmsAccount.deletePMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest2", ORG_USERNAME);
        String accountId = accounts.createAccountSFDX(SFDX, "Name='PMSAccountIsPrimaryAutoTest1'", ORG_USERNAME);
        String pmsAccountID1 = pmsAccount.createPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest1'" +
                " thn__PMS_DB__c=1 thn__Account__c='"  + accountId + "'", ORG_USERNAME);
        String pmsAccountID2= pmsAccount.createPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest2'" +
                " thn__PMS_DB__c=1 thn__Account__c='"  + accountId + "'", ORG_USERNAME);
        StringBuilder pmsAccountRecord1 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Id='" + pmsAccountID1 + "'", ORG_USERNAME);
        StringBuilder pmsAccountRecord2 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Id='" + pmsAccountID2 + "'", ORG_USERNAME);
        String pmsAccountIsPrimary1 = JsonParser2.getFieldValue(pmsAccountRecord1.toString(), "thn__Is_Primary__c");
        String pmsAccountIsPrimary2 = JsonParser2.getFieldValue(pmsAccountRecord2.toString(), "thn__Is_Primary__c");
        Assert.assertEquals(pmsAccountIsPrimary1, "true");
        Assert.assertEquals(pmsAccountIsPrimary2, "false");
    }

    @Test(priority = 2, description = "Change the value Is Primary to true in the second PMS Account. Expected" +
            " result: Checkbox Is Primary on first PMS Account = false, on second = true.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-668: PMS account is primary")
    public void case2() throws InterruptedException, IOException {
        pmsAccount.updatePMSaccountSFDX(SFDX, "Name='IsPrimaryAutoTest2'", "thn__Is_Primary__c=true",
                ORG_USERNAME);
        StringBuilder pmsAccountRecord1 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest1'", ORG_USERNAME);
        StringBuilder pmsAccountRecord2 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest2'", ORG_USERNAME);
        String pmsAccountIsPrimary1 = JsonParser2.getFieldValue(pmsAccountRecord1.toString(), "thn__Is_Primary__c");
        String pmsAccountIsPrimary2 = JsonParser2.getFieldValue(pmsAccountRecord2.toString(), "thn__Is_Primary__c");
        Assert.assertEquals(pmsAccountIsPrimary1, "false");
        Assert.assertEquals(pmsAccountIsPrimary2, "true");
    }

    @Test(priority = 3, description = "Create new PMS Account for same Account, thn__PMS_DB__c=2. Expected" +
            " result: Checkbox Is Primary on new PMS Account = true, on first and second PMS Account not changed.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-668: PMS account is primary")
    public void case3() throws InterruptedException, IOException {
        pmsAccount.deletePMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest3", ORG_USERNAME);
        StringBuilder accountRecord = accounts.
                getAccountSFDX(SFDX, "Name='PMSAccountIsPrimaryAutoTest1'", ORG_USERNAME);
        String accountId = JsonParser2.getFieldValue(accountRecord.toString(), "Id");
        String pmsAccountID3 = pmsAccount.createPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest3'" +
                " thn__PMS_DB__c=2 thn__Account__c='"  + accountId + "'", ORG_USERNAME);
        StringBuilder pmsAccountRecord1 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest1'", ORG_USERNAME);
        StringBuilder pmsAccountRecord2 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest2'", ORG_USERNAME);
        StringBuilder pmsAccountRecord3 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Id='" + pmsAccountID3 + "'", ORG_USERNAME);
        String pmsAccountIsPrimary1 = JsonParser2.getFieldValue(pmsAccountRecord1.toString(), "thn__Is_Primary__c");
        String pmsAccountIsPrimary2 = JsonParser2.getFieldValue(pmsAccountRecord2.toString(), "thn__Is_Primary__c");
        String pmsAccountIsPrimary3 = JsonParser2.getFieldValue(pmsAccountRecord3.toString(), "thn__Is_Primary__c");
        Assert.assertEquals(pmsAccountIsPrimary1, "false");
        Assert.assertEquals(pmsAccountIsPrimary2, "true");
        Assert.assertEquals(pmsAccountIsPrimary3, "true");
    }

    @Test(priority = 4, description = "Create new PMS Account for same Account, thn__PMS_DB__c=null. Expected" +
            " result: Checkbox Is Primary on new PMS Account = false.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-668: PMS account is primary")
    public void case4() throws InterruptedException, IOException {
        pmsAccount.deletePMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest4", ORG_USERNAME);
        StringBuilder accountRecord = accounts.
                getAccountSFDX(SFDX, "Name='PMSAccountIsPrimaryAutoTest1'", ORG_USERNAME);
        String accountId = JsonParser2.getFieldValue(accountRecord.toString(), "Id");
        String pmsAccountID4 = pmsAccount.createPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest4'" +
                " thn__Account__c='"  + accountId + "'", ORG_USERNAME);
        StringBuilder pmsAccountRecord1 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest1'", ORG_USERNAME);
        StringBuilder pmsAccountRecord2 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest2'", ORG_USERNAME);
        StringBuilder pmsAccountRecord3 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Name='IsPrimaryAutoTest3'", ORG_USERNAME);
        StringBuilder pmsAccountRecord4 = pmsAccount.
                getPMSAccountSFDX(SFDX, "Id='" + pmsAccountID4 + "'", ORG_USERNAME);
        String pmsAccountIsPrimary1 = JsonParser2.getFieldValue(pmsAccountRecord1.toString(), "thn__Is_Primary__c");
        String pmsAccountIsPrimary2 = JsonParser2.getFieldValue(pmsAccountRecord2.toString(), "thn__Is_Primary__c");
        String pmsAccountIsPrimary3 = JsonParser2.getFieldValue(pmsAccountRecord3.toString(), "thn__Is_Primary__c");
        String pmsAccountIsPrimary4 = JsonParser2.getFieldValue(pmsAccountRecord4.toString(), "thn__Is_Primary__c");
        Assert.assertEquals(pmsAccountIsPrimary1, "false");
        Assert.assertEquals(pmsAccountIsPrimary2, "true");
        Assert.assertEquals(pmsAccountIsPrimary3, "true");
        Assert.assertEquals(pmsAccountIsPrimary4, "false");
    }

}
