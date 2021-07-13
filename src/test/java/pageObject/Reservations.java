package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class Reservations extends BasePage {

    /**Constructor*/
    public Reservations(WebDriver driver) {
        super(driver);
    }

    By NEW_RESERVATION_BUTTON = By.xpath("//span[text()='Reservations']/following::div[@title='New']");
    By PROPERTY_FIELD = By.xpath("//label[text()='Property']/following-sibling::div//input");
    By MEWS_SERVICE_FIELD = By.xpath("//label[text()='Mews Service']/following-sibling::div//input");
    By CUSTOMER_FIELD = By.xpath("//label[text()='Customer']/following-sibling::div//input");
    By ARRIVAL_FIELD = By.xpath
            ("//fieldset//legend[text()='Arrival']/following-sibling::div//label[text()='Date']/following-sibling::div//input");
    By DEPARTURE_FIELD = By.xpath
            ("//fieldset//legend[text()='Departure']/following-sibling::div//label[text()='Date']/following-sibling::div//input");
    By ADULT_COUNT_FIELD = By.xpath("//label[text()='Adult Count']/following-sibling::div//input");
    By CHILD_COUNT_FIELD = By.xpath("//label[text()='Child Count']/following-sibling::div//input");
    By REQUEST_ROOM_FIELD = By.xpath("//label[text()='Requested Room Type']/following-sibling::div//input");
    By PRICING_TYPE_FIELD = By.xpath("//label[text()='Pricing Type']/following-sibling::div//input");
    By RATE_FIELD = By.xpath("//label[text()='Rate']/following-sibling::div//input");
    By UPDATE_PRICE_CHECKBOX = By.xpath
            ("//div[@class='isModal inlinePanel oneRecordActionWrapper']//lightning-input//span[text()='Update Price']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By CLOSE_WINDOW_BUTTON = By.
            xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");
    By HELP_ERROR_MESSAGE = By.xpath("//div[@data-help-message]");
    By EDIT_BUTTON = By.xpath("//div[text()='Reservation']/following::button[@name='Edit']");
    By MEWS_ID__FIELD = By.xpath("//label[text()='Mews Id']/following-sibling::div//input");
    By STATE__FIELD = By.xpath("//label[text()='State']/following-sibling::div//input");

    @Step
    public void clickNew(){
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_RESERVATION_BUTTON)).click();
    }

    @Step
    public void clickEdit(){
        wait1.until(ExpectedConditions.elementToBeClickable(EDIT_BUTTON)).click();
    }

    @Step("Fill out the reservation form where Update Price == true")
    public void createReservation(String property,
                                  String mewsService,
                                  String arrivalDate,
                                  String departureDate,
                                  String adultCount,
                                  String childCount,
                                  String roomType,
                                  String pricingType,
                                  String rate) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ property+"']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(MEWS_SERVICE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ mewsService +"']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(CUSTOMER_FIELD)).click();
        Thread.sleep(1000);
        enter();
        scrollToElement(ARRIVAL_FIELD);
        writeText(ARRIVAL_FIELD, arrivalDate);
        writeText(DEPARTURE_FIELD, departureDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(ADULT_COUNT_FIELD)).click();
        writeText(ADULT_COUNT_FIELD, adultCount);
        writeText(CHILD_COUNT_FIELD, childCount);
        wait1.until(ExpectedConditions.presenceOfElementLocated(CHILD_COUNT_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(REQUEST_ROOM_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ roomType +"']"))).click();
        scrollToElement(PRICING_TYPE_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRICING_TYPE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ pricingType +"']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(RATE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ rate +"']"))).click();
        scrollToElement(UPDATE_PRICE_CHECKBOX);
        wait1.until(ExpectedConditions.presenceOfElementLocated(UPDATE_PRICE_CHECKBOX)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the reservation form where Update Price == false")
    public void createReservation2(String property,
                                   String mewsService,
                                   String arrivalDate,
                                   String departureDate,
                                   String adultCount,
                                   String childCount,
                                   String roomType,
                                   String pricingType,
                                   String rate,
                                   String mewsId) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ property+"']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(MEWS_SERVICE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ mewsService +"']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(CUSTOMER_FIELD)).click();
        Thread.sleep(1000);
        enter();
        scrollToElement(ARRIVAL_FIELD);
        writeText(ARRIVAL_FIELD, arrivalDate);
        writeText(DEPARTURE_FIELD, departureDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(ADULT_COUNT_FIELD)).click();
        writeText(ADULT_COUNT_FIELD, adultCount);
        writeText(CHILD_COUNT_FIELD, childCount);
        wait1.until(ExpectedConditions.presenceOfElementLocated(CHILD_COUNT_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(REQUEST_ROOM_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ roomType +"']"))).click();
        scrollToElement(PRICING_TYPE_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRICING_TYPE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ pricingType +"']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(RATE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ rate +"']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(MEWS_ID__FIELD)).click();
        writeText(MEWS_ID__FIELD, mewsId);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Change state")
    public void changeState(String state){
        wait1.until(ExpectedConditions.presenceOfElementLocated(STATE__FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ state +"']"))).click();
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();

    }

    @Step("Close window")
    public void closeWindow(){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    @Step("Read help error message")
    public String readHelpErrorMessage() throws InterruptedException {
        return readRecalculateMessage(HELP_ERROR_MESSAGE);
    }





}
