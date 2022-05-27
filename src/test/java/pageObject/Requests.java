package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;
import tests.BaseTest;

import java.net.MalformedURLException;
import java.net.URL;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

public class Requests extends BasePage {

    /**Constructor*/
    public Requests(WebDriver driver) {
        super(driver);
    }

    By CONVERT_BUTTON = By.xpath("//button[@name='thn__Request__c.thn__Convert']");

    @Step("Open request record")
    public Requests openRequestRecord(String id) throws MalformedURLException {
        try {
            URL baseUrl = new URL(driver.getCurrentUrl());
            String url = "https://" + baseUrl.getAuthority() + "/lightning/r/thn__Request__c/" + id + "/view";
            System.out.println(url);
            driver.navigate().to(url);
        } catch (Exception ex){ex.printStackTrace();
        }
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
