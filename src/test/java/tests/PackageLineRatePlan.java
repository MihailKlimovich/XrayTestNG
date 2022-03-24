package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class PackageLineRatePlan extends BaseTest{

    @Test(priority = 1, description = "Fill guest, Gross value, type, accounting category and notes, check send" +
            " to mews")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-683: Package line rate plan - Test")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='PackageLineRatePlanAutoTest", ORG_USERNAME);

    }

}
