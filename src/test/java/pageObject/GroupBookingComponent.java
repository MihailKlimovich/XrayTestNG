package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

public class GroupBookingComponent extends BasePage {

    /**Constructor*/
    public GroupBookingComponent(WebDriver driver) {
        super(driver);
    }

    By SAVE_BUTTON = By.xpath("//div[@thn-groupbooking_groupbooking]//button[text()='Save']");
    By NEW_BUTTON = By.xpath("//div[@thn-groupbooking_groupbooking]//button[text()='New']");
    By RATE_PICKLIST = By.xpath("//button[@aria-label='Hotel Rooms, Select an Option']");
    By OCCUPANCY_TYPE_PICKLIST = By.xpath("//button[@aria-label='Occupancy Type, Select an Option']");
    By NUMBER_OF_ROOMS_FIELD = By.xpath("//lightning-input[@data-field='thn__Pax__c']//input");
    By ROOM_TYPE_PICKLIST = By.xpath("//input[@placeholder='Select an Option']");
    By FILTER_VALUES = By.xpath("//input[@placeholder='Filter values..']");
    By ADD_RATE_BUTTON = By.xpath("//button[text()='Add Rate']");


    @Step("Change the price per day")
    public void changePricePerDay(String dayNumber, String rate) throws InterruptedException {
        click3(By.xpath("(//c-day-price-table//lightning-input[@data-field='thn__Unit_Price_incl_Tax__c']//div//input)[" + dayNumber + "]"));
        clear(By.xpath("(//c-day-price-table//lightning-input[@data-field='thn__Unit_Price_incl_Tax__c']//div//input)[" + dayNumber + "]"));
        writeText(By.xpath("(//c-day-price-table//lightning-input[@data-field='thn__Unit_Price_incl_Tax__c']//div//input)[" + dayNumber + "]"), rate);
    }

    @Step("Change the price per day and quantity")
    public void changePricePerDayAndQuantity(String numberOfElement, String dayNumber, String rate, String quantity)
            throws InterruptedException {
        click3(By.xpath("(//div[@thn-groupbooking_groupbooking]//c-day-price-row//span[@class='slds-checkbox_faux'])[" + numberOfElement + "]"));
        click3(By.xpath("(//c-day-price-table//input[@data-field='thn__Quantity__c'])[" + dayNumber + "]"));
        clear(By.xpath("(//c-day-price-table//input[@data-field='thn__Quantity__c'])[" + dayNumber + "]"));
        writeText(By.xpath("(//c-day-price-table//input[@data-field='thn__Quantity__c'])[" + dayNumber + "]"), quantity);
        click3(By.xpath("(//c-day-price-table//input[@data-field='thn__Unit_Price_incl_Tax__c'])[" + dayNumber + "]"));
        clear(By.xpath("(//c-day-price-table//input[@data-field='thn__Unit_Price_incl_Tax__c'])[" + dayNumber + "]"));
        writeText(By.xpath("(//c-day-price-table//input[@data-field='thn__Unit_Price_incl_Tax__c'])[" + dayNumber + "]"), rate);
    }

    @Step("Change quantity")
    public void changeQuantity(String dayNumber, String quantity)
            throws InterruptedException {
        click3(By.xpath("(//c-day-price-table//input[@data-field='thn__Quantity__c'])[" + dayNumber + "]"));
        clear(By.xpath("(//c-day-price-table//input[@data-field='thn__Quantity__c'])[" + dayNumber + "]"));
        writeText(By.xpath("(//c-day-price-table//input[@data-field='thn__Quantity__c'])[" + dayNumber + "]"), quantity);

    }



    @Step("Click Save button")
    public void clickSaveButton() throws InterruptedException {
        click4(SAVE_BUTTON);
        Thread.sleep(3000);
    }

    @Step("Click New button")
    public void clickNewButton() throws InterruptedException {
        click4(NEW_BUTTON);
    }

    @Step("Create quote hotel room")
    public void cteateQuoteHotelRoom(String rate, String occupancyType, String numberOfRooms, String roomTypes)
            throws InterruptedException {
        click3(RATE_PICKLIST);
        click3(By.xpath("//span[@title='" + rate + "']"));
        click3(OCCUPANCY_TYPE_PICKLIST);
        click3(By.xpath("//span[@title='" + occupancyType + "']"));
        click3(NUMBER_OF_ROOMS_FIELD);
        clear(NUMBER_OF_ROOMS_FIELD);
        writeText(NUMBER_OF_ROOMS_FIELD, numberOfRooms);
        click3(ROOM_TYPE_PICKLIST);
        click3(FILTER_VALUES);
        writeText(FILTER_VALUES, roomTypes);
        click3(By.xpath("//span[@title='" + roomTypes + "']"));
        click4(ADD_RATE_BUTTON);
    }

    @Step("Read Quote Hotel Room Name")
    public String readQuoteHotelRoomName(String numberOfRooms) throws InterruptedException {
        return readRecalculateMessage(By.xpath("(//span[@class='qhrDisplayName'])[" + numberOfRooms + "]"));
    }

    @Step("Find room by Name")
    public boolean findRoomByName(String name) throws InterruptedException {
        boolean displaydElement = isDisplayed(By.xpath("//span[@class='qhrDisplayName'][text()='"+ name + "']"));
        return displaydElement;
    }

    @Step("Read colour by number of element")
    public String readColour_ByNumberOfElement(String numberOfElement) throws InterruptedException {
        WebElement t = driver.findElement(By.
                xpath("(//lightning-icon[@class='slds-icon-utility-stop clone-button pointer-auto slds-icon_container'])[" + numberOfElement + "]"));
        String s = t.getAttribute("style");
        return s;
    }

    @Step("Read checkbox on quote hotel room")
    public Boolean checkCheckboxActivity(String numberOfElement) throws InterruptedException {
        Boolean c = readCheckbox(By.xpath("(//div[@thn-groupbooking_groupbooking]//c-day-price-row//input[@type='checkbox'])[" + numberOfElement + "]"));
        return c;
    }



    /*@Step("Check that the checkbox is not interactive")
    public Boolean checkCheckbox_isNotIntaractive(String numberOfElement) throws InterruptedException {
        WebElement t = driver.findElement(By.
                xpath("(//div[@thn-groupbooking_groupbooking]//c-day-price-row//span[@class='slds-checkbox_faux'])[" + numberOfElement + "]"));
        Boolean s = t.
        return s;
    }*/


}
