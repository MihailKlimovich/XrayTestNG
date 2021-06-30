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

    By NEW_CREDIT_NOTE_LINE_BUTTON = By.xpath("//div[@class='windowViewMode-maximized oneContent active lafPageHost']//a[@title='New']");
    By CREDIT_NOTE_FIELD = By.xpath("//div[@class='slds-form']//label[text()='Credit Note']");
    By NEW_CREDIT_NOTE_BUTTON = By.xpath("//label[text()='Credit Note']/following-sibling::div//span[@title='New Credit Note']");
    By SAVE_BUTTON_NEW_CREDIT_NOTE = By.xpath("//div[@class='modal-footer slds-modal__footer']//span[text()='Save']");
    By VAT_FIELD = By.xpath("//div[@class='slds-form']//label[text()='VAT %']/following-sibling::div//input");
    By VAT_CATEGORY = By.xpath("//div[@class='slds-form']//label[text()='VAT Category']");
    By VAT_CATEGORY_TYPE = By.xpath("//span[@title='0']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By MESSAGE_ERROR_TEXT = By.xpath("//div[@class='container']//li");
    By CLOSE_WINDOW_BUTTON = By.xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");


    @Step ("Click new Credit note line")
    public void clickNewCreditNoteLineButton(WebDriver driver) throws InterruptedException {
        Thread.sleep(3000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(NEW_CREDIT_NOTE_LINE_BUTTON));
        click2(NEW_CREDIT_NOTE_LINE_BUTTON);
    }

    @Step ("Fill out the new credit note line where Invoice Line == null & Amount == null & Quantity == null")
    public void fillOutNewCreditNoteLineForm(WebDriver driver, String vat) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(CREDIT_NOTE_FIELD)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_CREDIT_NOTE_BUTTON)).click();
         wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON_NEW_CREDIT_NOTE)).click();
         wait1.until(ExpectedConditions.presenceOfElementLocated(VAT_FIELD));
         click2(VAT_FIELD);
         writeText(VAT_FIELD, vat);
         wait1.until(ExpectedConditions.presenceOfElementLocated(VAT_CATEGORY)).click();
         wait1.until(ExpectedConditions.presenceOfElementLocated(VAT_CATEGORY_TYPE)).click();
         wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Read error message 2")
    public String readErrorMessage2(WebDriver driver) throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
    }

    @Step("Close window")
    public void closeWindow(WebDriver driver){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }






}
