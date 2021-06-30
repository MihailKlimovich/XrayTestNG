package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

public class Products extends BasePage {

    /**Constructor*/
    public Products(WebDriver driver) {
        super(driver);
    }

    @Step("Open Packages tab...")
    public Products goToProducts() throws InterruptedException {
        driver.navigate().to("https://connect-java-4747-dev-ed.lightning.force.com/lightning/o/thn__Package__c/list?filterName=Recent");
        return this;
    }

}
