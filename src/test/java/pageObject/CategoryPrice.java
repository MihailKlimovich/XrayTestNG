package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class CategoryPrice extends BasePage{

    /**Constructor*/

    public CategoryPrice(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Create Category Price SFDX")
    public String createCategoryPriceSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder categoryPriceResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Category_Price__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Category Price result:");
        System.out.println(categoryPriceResult);
        String categoryPriceID = JsonParser2.getFieldValue(categoryPriceResult.toString(), "id");
        return categoryPriceID;
    }

    @Step("Get Category Price SFDX")
    public StringBuilder getCategoryPriceSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder categoryPriceRecordRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Category_Price__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return categoryPriceRecordRecord;
    }



}
