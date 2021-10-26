package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Product extends BasePage {

    /**Constructor*/
    public Product(WebDriver driver) {
        super(driver);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Get Product SFDX")
    public StringBuilder getProductSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return productRecord;
    }
}
