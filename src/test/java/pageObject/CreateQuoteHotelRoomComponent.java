package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class CreateQuoteHotelRoomComponent extends BasePage {

    /**Constructor*/
    public CreateQuoteHotelRoomComponent(WebDriver driver){
        super(driver);
    }

    By PRODUCT_FIELD = By.xpath("//slot//label[text()='Product']/following-sibling::div//input");
    By ROOM_TYPE_FIELD = By.xpath("//slot//label[text()='Room Type']/following-sibling::div//input");
    By DISCOUNTABLE_CHECKBOX = By.xpath("//span[text()='Discountable']");
    By PAX_FIELD = By.xpath("//div//input[@name='thn__Pax__c']");
    By ARRIVAL_DATE_FIELD = By.
            xpath("//fieldset//legend[text()='Arrival Date Time']/following-sibling::div//label[text()='Date']/following-sibling::div//input");
    By ARRIVAL_TIME_FIELD = By.
            xpath("//fieldset//legend[text()='Arrival Date Time']/following-sibling::div//label[text()='Time']/following-sibling::div//input");
    By DEPARTURE_DATE_FIELD = By.
            xpath("//fieldset//legend[text()='Departure Date Time']/following-sibling::div//label[text()='Date']/following-sibling::div//input");
    By DEPARTURE_TIME_FIELD = By.
            xpath("//fieldset//legend[text()='Departure Date Time']/following-sibling::div//label[text()='Time']/following-sibling::div//input");
    By DAY_NUMBER_FIELD = By.xpath("//slot//label[text()='Day Number']/following-sibling::div//input");
    By OVERBOOKING_MESSAGE_FIELD = By.xpath("//slot//label[text()='Overbooking message']/following-sibling::div//input");
    By SAVE_BUTTON = By.xpath("//button[text()='Save']");
    By CONFIRMATION_MESSAGE = By.xpath("//flowruntime-display-text-lwc//span//p[text()='Do you wish to overbook?']");
    By YES_RADIO_BUTTON = By.xpath("//span//input[@value='YesChoice']");
    By NEXT_BUTTON = By.xpath("//button[@title='Next']");


    @Step("Create New Quote Hotel Room")
    public void createNewQuoteHotelRoom
            (String product, String roomType, String pax, String arrivalDate, String arrivalTime, String departureDate,
             String departureTime, String dayNumber) throws InterruptedException {
        driver.switchTo().frame(0);
        click3(PRODUCT_FIELD);
        writeText(PRODUCT_FIELD, product);
        click3(PRODUCT_FIELD);
        click3(By.xpath("//li[@data-name='" + product + "']"));
        click3(ROOM_TYPE_FIELD);
        writeText(ROOM_TYPE_FIELD, roomType);
        click3(By.xpath("//li[@data-name='" + roomType + "']"));
        click3(PAX_FIELD);
        writeText(PAX_FIELD, pax);
        click3(ARRIVAL_DATE_FIELD);
        writeText(ARRIVAL_DATE_FIELD, arrivalDate);
        click3(ARRIVAL_TIME_FIELD);
        writeText(ARRIVAL_TIME_FIELD, arrivalTime);
        click3(DEPARTURE_DATE_FIELD);
        writeText(DEPARTURE_DATE_FIELD, departureDate);
        click3(DEPARTURE_TIME_FIELD);
        writeText(DEPARTURE_TIME_FIELD, departureTime);
        click3(DAY_NUMBER_FIELD);
        writeText(DAY_NUMBER_FIELD, dayNumber);
        click4(SAVE_BUTTON);
        Thread.sleep(5000);
        try {
            if (wait2.until(ExpectedConditions.presenceOfElementLocated(CONFIRMATION_MESSAGE)) != null){
                click3(YES_RADIO_BUTTON);
                click4(NEXT_BUTTON);
                driver.switchTo().defaultContent();
            }
        }catch (TimeoutException e) {
            System.out.println("No overbooking message");
            driver.switchTo().defaultContent();
        }


    }


}
