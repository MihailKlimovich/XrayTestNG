package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class PMSBlock extends BasePage {

    /**Constructor*/
    public PMSBlock(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Get PMS Block SFDX")
    public StringBuilder getPMSBlockSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder pmsBlockRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return pmsBlockRecord;
    }

    @Step("Delete PMS Block SFDX")
    public void deletePMSBlockSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__PMS_Block__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

}
