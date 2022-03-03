package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.BasePage;
import tests.BaseTest;

public class MultiDelete extends BasePage {

    /**Constructor*/
    public MultiDelete(WebDriver driver) {
        super(driver);
    }

    By NEXT_BUTTON = By.xpath("//button[@class='slds-button slds-button_brand flow-button__NEXT']");
    By MESSAGE = By.xpath("//lightning-formatted-rich-text[@class='slds-rich-text-editor__output']//span//p");
    By FINISH_BUTTON = By.xpath("//button[@class='slds-button slds-button_brand flow-button__FINISH']");


    @Step("Multi delete records")
    public String multiDeleteRecords()
            throws InterruptedException {
        driver.switchTo().frame(0);
        Thread.sleep(1000);
        click3(NEXT_BUTTON);
        String message = readText(MESSAGE);
        click3(FINISH_BUTTON);
        driver.switchTo().defaultContent();
        return message;
    }
}
