package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;

public class CreditNoteLine extends BasePage {



    /**Constructor*/
    public CreditNoteLine(WebDriver driver){
        super(driver);
    }

    By NEW_CREDIT_NOTE_BUTTON = By.xpath("//div[@title='New']");


    @Step
    public void createNewCreditNoteLine(WebDriver driver){
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_CREDIT_NOTE_BUTTON)).click();
    }






}
