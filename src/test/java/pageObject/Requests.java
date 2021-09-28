package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;
import tests.BaseTest;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

public class Requests extends BasePage {

    /**Constructor*/
    public Requests(WebDriver driver) {
        super(driver);
    }

    By CONVERT_BUTTON = By.xpath("//button[@name='thn__Request__c.thn__Convert']");

    @Step("Open request record")
    public Requests openRequestRecord(String id){
        https://efficiency-business-1136-dev-ed.lightning.force.com/lightning/r/thn__Request__c/a1K1j000004xFxbEAE/view
        driver.navigate().to("https://ability-dream-3253-dev-ed.lightning.force.com/lightning/r/thn__Request__c/" + id + "/view");
        try{if(wait2.until(ExpectedConditions.alertIsPresent())!=null){
            Alert alert = wait2.until(alertIsPresent());
            alert.accept();
        }}catch (TimeoutException e){
        }
        return this;
    }

    @Step("Click Convert button")
    public void clickConvert() throws InterruptedException {
        waitForTests.until(ExpectedConditions.elementToBeClickable(CONVERT_BUTTON));
        click2(CONVERT_BUTTON);
    }


}
