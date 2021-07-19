package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class QuoteHotelRoom extends BasePage {

    /**Constructor*/
    public QuoteHotelRoom(WebDriver driver) {
        super(driver);
    }

    By PRODUCT_FIELD = By.xpath("//slot//label[text()='Product']/following-sibling::div//input");
    By PRODUCT_TYPE_ROOM1NIGHT = By.xpath("//span[@title='ROOM 1 NIGHT']");
    By ROOM_TYPE_FIELD = By.xpath("//slot//label[text()='Room Type']/following-sibling::div//input");
    By ROOM_TYPE_SINGLE = By.xpath("//span[@title='Single']");
    By NUMBER_FIELD = By.xpath("//div//input[@name='thn__Pax__c']");
    By ARRIVAL_DATE_FIELD = By.
            xpath("//fieldset//legend[text()='Arrival Date Time']/following-sibling::div//label[text()='Date']/following-sibling::div//input");
    By ARRIVAL_TIME_FIELD = By.
            xpath("//fieldset//legend[text()='Arrival Date Time']/following-sibling::div//label[text()='Time']/following-sibling::div//input");
    By DEPARTURE_DATE_FIELD = By.
            xpath("//fieldset//legend[text()='Departure Date Time']/following-sibling::div//label[text()='Date']/following-sibling::div//input");
    By DEPARTURE_TIME_FIELD = By.
            xpath("//fieldset//legend[text()='Departure Date Time']/following-sibling::div//label[text()='Time']/following-sibling::div//input");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By NEW_HOTEL_ROOM = By.xpath("//span[text()='Hotel Rooms']/following::div[@title='New']");
    By DATA_ERROR_MESSAGE = By.xpath("//div[@data-error-message]");
    By MESSAGE_ERROR_TEXT = By.xpath("//div[@class='container']//li");
    By CLOSE_WINDOW_BUTTON = By.xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");
    By HELP_ERROR_MESSAGE = By.xpath("//div[@data-help-message]");


    @Step("Fill out the hotel room")
    public void createHotelRoom(
            String arrivalDate,
            String arrivalTime,
            String departureDate,
            String departureTime
    ) throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_HOTEL_ROOM)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD)).click();
        Thread.sleep(1000);
        clickInvisibleElement(PRODUCT_TYPE_ROOM1NIGHT);
        //wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_TYPE_ROOM1NIGHT)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(ROOM_TYPE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(ROOM_TYPE_SINGLE)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(ARRIVAL_DATE_FIELD)).click();
        writeText(ARRIVAL_DATE_FIELD, arrivalDate);
        click2(ARRIVAL_TIME_FIELD);
        clear(ARRIVAL_TIME_FIELD);
        writeText(ARRIVAL_TIME_FIELD, arrivalTime);
        wait1.until(ExpectedConditions.presenceOfElementLocated(DEPARTURE_DATE_FIELD)).click();
        writeText(DEPARTURE_DATE_FIELD, departureDate);
        click2(DEPARTURE_TIME_FIELD);
        clear(DEPARTURE_TIME_FIELD);
        writeText(DEPARTURE_TIME_FIELD, departureTime);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }

    @Step("Fill out the hotel room")
    public void createHotelRoom2(
            String pax
    ) throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_HOTEL_ROOM)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_TYPE_ROOM1NIGHT)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(ROOM_TYPE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(ROOM_TYPE_SINGLE)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(NUMBER_FIELD)).click();
        writeText(NUMBER_FIELD, pax);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }

    @Step("Read error message 2")
    public String readErrorMessage2(WebDriver driver) throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
    }

    @Step("Read error message 3")
    public String readErrorMessage3(WebDriver driver) throws InterruptedException {
        return readRecalculateMessage(DATA_ERROR_MESSAGE);
    }

    @Step("Close window")
    public void closeWindow(WebDriver driver){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    @Step("Read help error message")
    public String readHelpErrorMessage() throws InterruptedException {
        return readRecalculateMessage(HELP_ERROR_MESSAGE);
    }




}
