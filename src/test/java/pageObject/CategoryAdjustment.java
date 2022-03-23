package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class CategoryAdjustment extends BasePage {

    /**Constructor*/

    public CategoryAdjustment(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Create Category Adjustment SFDX")
    public String createCategoryAdjustmentSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder categoryAdjustmentResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Category_Adjustment__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Category Adjustment result:");
        System.out.println(categoryAdjustmentResult);
        String categoryAdjustmentID = JsonParser2.getFieldValue(categoryAdjustmentResult.toString(), "id");
        return categoryAdjustmentID;
    }

    @Step("Get Category Adjustment SFDX")
    public StringBuilder getCategoryAdjustmentSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder categoryAdjustmentRecordRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Category_Adjustment__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return categoryAdjustmentRecordRecord;
    }

}
