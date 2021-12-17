package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.io.IOException;

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
    By SELECT_ALL_ITEMS_CHECKBOX = By.xpath("//span[text()='Select 2 items']");
    By MULTI_DELETE_BUTTON = By.xpath("//a[@title='Multidelete']");


    @Step("Fill out the hotel room")
    public void createHotelRoom(
            String arrivalDate,
            String arrivalTime,
            String departureDate,
            String departureTime
    ) throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_HOTEL_ROOM)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD));
        click2(PRODUCT_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_TYPE_ROOM1NIGHT));
        click2(PRODUCT_TYPE_ROOM1NIGHT);
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
    public String readErrorMessage2() throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
    }

    @Step("Read error message 3")
    public String readErrorMessage3() throws InterruptedException {
        return readRecalculateMessage(DATA_ERROR_MESSAGE);
    }

    @Step("Click multi delete button")
    public  void clickMultiDeleteButton() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(MULTI_DELETE_BUTTON));
        click3(MULTI_DELETE_BUTTON);
        Thread.sleep(3000);
    }

    @Step("Close window")
    public void closeWindow(){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    @Step("Read help error message")
    public String readHelpErrorMessage() throws InterruptedException {
        return readRecalculateMessage(HELP_ERROR_MESSAGE);
    }

    @Step("Select all items")
    public  void selectItems(String numberOfElements) throws InterruptedException {
        refreshPage();
        By SELECT_ALL_ITEMS_CHECKBOX = By.xpath("//span[text()='Select " + numberOfElements + " items']/preceding-sibling::span");
        wait1.until(ExpectedConditions.presenceOfElementLocated(SELECT_ALL_ITEMS_CHECKBOX));
        click3(SELECT_ALL_ITEMS_CHECKBOX);
    }

    @Step("Select item number")
    public  void selectItemNumber(String numberOfElements) throws InterruptedException {
        refreshPage();
        By SELECT_NUMBer_ITEM_CHECKBOX = By.xpath("//span[text()='Select item "+ numberOfElements + "']/preceding-sibling::span");
        wait1.until(ExpectedConditions.presenceOfElementLocated(SELECT_NUMBer_ITEM_CHECKBOX));
        click3(SELECT_NUMBer_ITEM_CHECKBOX);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Create Quote Hotel Room SFDX")
    public String createQuoteHotelRoomSFDX(String sfdxPath, String value, String userName) throws IOException, InterruptedException {
        StringBuilder quoteHotelRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Quote hotel room create result:");
        System.out.println(quoteHotelRoomResult);
        String quoteHotelRoomID = JsonParser2.getFieldValue(quoteHotelRoomResult.toString(), "id");
        return quoteHotelRoomID;
    }

    @Step("Get Quote Hotel Room SFDX")
    public StringBuilder getQuoteHotelRoomSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder quoteHotelRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return quoteHotelRoomRecord;
    }

    @Step("Delete Quote Hotel Room SFDX")
    public StringBuilder deleteQuoteHotelRoomSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
        return result;
    }

    @Step("Update Quote Hotel Room SFDX")
    public void updateQuoteHotelRoomSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder quoteHotelRoomUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(quoteHotelRoomUpdateResult);
    }





}
