package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class User extends BasePage {

    /**Constructor*/
    public User(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Add Permission Set")
    public void addPermissionSet(String sfdxPath, String apiName, String forUser, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:user:permset:assign",
                "-n",
                apiName,
                "-o",
                forUser,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

    @Step("Apex execute")
    public void apexExecute(String sfdxPath, String userName, String path)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:apex:execute",
                "-u",
                userName,
                "-f",
                path,
                "--json"});
        System.out.println(result);
    }

}
