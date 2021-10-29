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
    public void addPermissionSet(String sfdxPath, String apiName, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:user:permset:assign",
                "-n",
                apiName,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

}
