package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class QuoteProducts extends BasePage {

    /**Constructor*/
    public QuoteProducts(WebDriver driver) {
        super(driver);
    }


    By NEW_PRODUCT = By.xpath("//h1[text()='Products']/following::div[@title='New']");
    By PRODUCT_FIELD = By.xpath("//slot//label[text()='Product']/following-sibling::div//input");
    By MEETING_ROOM_FIELD = By.xpath("//slot//label[text()='Meeting Room']/following-sibling::div//input");
    By START_DATE_FIELD = By.xpath("//fieldset//legend[text()='Start Date Time']/following-sibling::div//label[text()='Date']/following-sibling::div//input");
    By END_DATE_FIELD = By.xpath("//fieldset//legend[text()='End Date/Time']/following-sibling::div//label[text()='Date']/following-sibling::div//input");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By DATA_ERROR_MESSAGE = By.xpath("//div[@data-error-message]");
    By MESSAGE_ERROR_TEXT = By.xpath("//div[@class='container']//li");
    By CLOSE_WINDOW_BUTTON = By.
            xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");
    By PAX_FIELD = By.xpath("//div//input[@name='thn__Pax__c']");
    By HELP_ERROR_MESSAGE = By.xpath("//div[@data-help-message]");
    By EDIT_BUTTON = By.xpath("//div[text()='Quote Product']/following::button[@name='Edit']");
    By ON_CONSUMPTION_CHECKBOX = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//lightning-input//span[text()='On Consumption']");


    @Step("Fill out the product")
    public void createProduct(String product, String pax, String startDate, String endDate) throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_PRODUCT)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD));
        click2(PRODUCT_FIELD);
        clickInvisibleElement(By.xpath("//span[@title='"+ product +"']"));
        //wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ product +"']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(PAX_FIELD)).click();
        writeText(PAX_FIELD, pax);
        wait1.until(ExpectedConditions.presenceOfElementLocated(START_DATE_FIELD)).click();
        writeText(START_DATE_FIELD, startDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(END_DATE_FIELD)).click();
        writeText(END_DATE_FIELD, endDate);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the product")
    public void createProduct2(String meetingRoom, String product, String startDate, String endDate) throws InterruptedException {

        wait1.until(ExpectedConditions.elementToBeClickable(NEW_PRODUCT)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD));
        click2(PRODUCT_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ product +"']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(START_DATE_FIELD)).click();
        writeText(START_DATE_FIELD, startDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(END_DATE_FIELD)).click();
        writeText(END_DATE_FIELD, endDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(MEETING_ROOM_FIELD)).click();
        writeText(MEETING_ROOM_FIELD, meetingRoom);
        Thread.sleep(2000);
        clear(MEETING_ROOM_FIELD);
        writeText(MEETING_ROOM_FIELD, meetingRoom);
        clickInvisibleElement(By.xpath("//lightning-base-combobox-formatted-text[@title='"+ meetingRoom +"']"));
        //wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//lightning-base-combobox-formatted-text[@title='"+ meetingRoom +"']"))).click();
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Change date of product")
    public void changeDate(String startDate, String endDate){
        wait1.until(ExpectedConditions.elementToBeClickable(EDIT_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(START_DATE_FIELD)).click();
        clear(START_DATE_FIELD);
        writeText(START_DATE_FIELD, startDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(END_DATE_FIELD)).click();
        clear(END_DATE_FIELD);
        writeText(END_DATE_FIELD, endDate);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Change on consumption")
    public void changeOnConsumption(){
        wait1.until(ExpectedConditions.elementToBeClickable(EDIT_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(ON_CONSUMPTION_CHECKBOX)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Read error message at the top of the page")
    public String readErrorMessage2() throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
    }

    @Step("Read data error message")
    public String readErrorMessage3() throws InterruptedException {
        return readRecalculateMessage(DATA_ERROR_MESSAGE);
    }

    @Step("Read help error message")
    public String readHelpErrorMessage() throws InterruptedException {
        return readRecalculateMessage(HELP_ERROR_MESSAGE);
    }

    @Step("Close window")
    public void closeWindow(){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }


}
