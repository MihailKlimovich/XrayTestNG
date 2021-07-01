package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class MyceQuotes extends BasePage{


    /**Constructor*/
    public MyceQuotes(WebDriver driver) {
        super(driver);
    }

    By NEW_MYCE_QUOTE_BUTTON = By.xpath("//div[@title='New']");
    By QUATE_RADIO_BUTTON = By.xpath("//div//span[text()='Quote']");
    By NEXT_BUTTON = By.xpath("//button//span[text()='Next']");
    By NAME_QUOTE_FIELD = By.xpath("//div//input[@name='Name']");
    By ARRIVAL_DATA_FIELD = By.xpath("//div//input[@name='thn__Arrival_Date__c']");
    By DEPARTURE_DATA_FIELD = By.xpath("//div//input[@name='thn__Departure_Date__c']");
    By PAX_FIELD = By.xpath("//div//input[@name='thn__Pax__c']");
    By COMMISSIONABLE_RADIO_BUTTON = By.xpath("//div//span[text()='Commissionable']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By PROPERTY_FIELD = By.xpath("//label[text()='Property']/following-sibling::div//input");
    By COMMISSION_FIELD = By.xpath("//span//label[text()='Commission to']");
    By COMMISSION_TYPE_NONE = By.xpath("//span[@title='--None--']");
    By COMMISSION_TYPE_COMPANY = By.xpath("//span[@title='Company']");
    By PROPERTY_TYPE_TEST = By.xpath("//div//lightning-base-combobox-formatted-text[@title='test']");
    By PROPERTY_TYPE_DEMO = By.xpath("//div//lightning-base-combobox-formatted-text[@title='Demo']");
    By MESSAGE_TEXT = By.xpath("//div[@class='container']//h2");
    By CLOSE_WINDOW_BUTTON = By.xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");
    By COMMISSION_TYPE_AGENT = By.xpath("//span[@title='Agent']");
    By MESSAGE_ERROR_TEXT = By.xpath("//div[@class='container']//li");
    By COMPANY_FIELD = By.xpath("//label[text()='Company']/following-sibling::div//input");
    By COMPANY_TYPE_AGENT = By.xpath("//lightning-base-combobox-formatted-text[@title='Agent']");
    By AGENT_FIELD = By.xpath("//label[text()='Agent']/following-sibling::div//input");
    By AGENT_TYPE_COMPANY = By.xpath("//lightning-base-combobox-formatted-text[@title='Company']");
    By SEND_TO_MEWS_RADIO_BUTTON = By.xpath("//div//span[text()='Send To Mews']");
    By EDIT_BUTTON = By.xpath("//div//button[@name='Edit']");
    By STAGE_FIELD = By.xpath("//span//label[text()='Stage']");
    By STAGE_TYPE_CLOSED = By.xpath("//span//lightning-base-combobox-item[@data-value='4 - Closed']");
    By IS_CONFIRMED_RADIO_BUTTON = By.
            xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//input[@name='thn__Is_Confirmed__c']");
    By CLOSED_STATUS_FIELD = By.xpath("//span//label[text()='Closed Status']");
    By CLOSED_STATUS_TYPE_CANCELLED = By.xpath("//span[@title='Cancelled']");
    By HOTEL_ROOM = By.xpath("//span[@title = 'Hotel Rooms']");
    By MEETING_ROOM = By.xpath("//span[@title = 'Meeting Rooms']");



    @Step
    public void createNewMyceQuote(WebDriver driver){
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_MYCE_QUOTE_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(QUATE_RADIO_BUTTON)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON)).click();
    }

    @Step("Fill out the quota form when Commission is None")
    public void fillOutTheQuotaForm_whenCommissionIsNone(
            String nameQuote,
            String arrivalDate,
            String departureDate,
            String pax,
            String property
    ) throws InterruptedException {
        writeText(NAME_QUOTE_FIELD, (nameQuote));
        click(ARRIVAL_DATA_FIELD);
        writeText(ARRIVAL_DATA_FIELD, (arrivalDate));
        click(DEPARTURE_DATA_FIELD);
        writeText(DEPARTURE_DATA_FIELD, (departureDate));
        click(PAX_FIELD);
        writeText(PAX_FIELD, (pax));
        wait1.until(ExpectedConditions.elementToBeClickable(COMMISSIONABLE_RADIO_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSION_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSION_TYPE_NONE)).click();
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, (property));
        click(PROPERTY_TYPE_TEST);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the quota form when Commission is Agent")
    public void fillOutTheQuotaForm_whenCommissionIsAgent(
            String nameQuote,
            String arrivalDate,
            String departureDate,
            String pax,
            String property
    ) throws InterruptedException {
        writeText(NAME_QUOTE_FIELD, (nameQuote));
        click(ARRIVAL_DATA_FIELD);
        writeText(ARRIVAL_DATA_FIELD, (arrivalDate));
        click(DEPARTURE_DATA_FIELD);
        writeText(DEPARTURE_DATA_FIELD, (departureDate));
        click(PAX_FIELD);
        writeText(PAX_FIELD, (pax));
        wait1.until(ExpectedConditions.elementToBeClickable(COMMISSIONABLE_RADIO_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSION_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSION_TYPE_AGENT)).click();
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, (property));
        click(PROPERTY_TYPE_TEST);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the quota form when Commission is Company")
    public void fillOutTheQuotaForm_whenCommissionIsCompany(
            String nameQuote,
            String arrivalDate,
            String departureDate,
            String pax,
            String property
    ) throws InterruptedException {
        writeText(NAME_QUOTE_FIELD, (nameQuote));
        click(ARRIVAL_DATA_FIELD);
        writeText(ARRIVAL_DATA_FIELD, (arrivalDate));
        click(DEPARTURE_DATA_FIELD);
        writeText(DEPARTURE_DATA_FIELD, (departureDate));
        click(PAX_FIELD);
        writeText(PAX_FIELD, (pax));
        wait1.until(ExpectedConditions.elementToBeClickable(COMMISSIONABLE_RADIO_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSION_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSION_TYPE_COMPANY)).click();
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, (property));
        click(PROPERTY_TYPE_TEST);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the quota form without commission")
    public void fillOutTheQuotaFormWithoutCommission(
            String nameQuote,
            String arrivalDate,
            String departureDate,
            String pax,
            String property
    ) throws InterruptedException {
        writeText(NAME_QUOTE_FIELD, (nameQuote));
        click(ARRIVAL_DATA_FIELD);
        writeText(ARRIVAL_DATA_FIELD, (arrivalDate));
        click(DEPARTURE_DATA_FIELD);
        writeText(DEPARTURE_DATA_FIELD, (departureDate));
        click(PAX_FIELD);
        writeText(PAX_FIELD, (pax));
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, (property));
        click(PROPERTY_TYPE_TEST);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the quota form when Commission is None")
    public void fillOutTheQuotaForm_whenCompanyIsAgentAndAgentIsCompany(
            String nameQuote,
            String arrivalDate,
            String departureDate,
            String pax,
            String property,
            String company,
            String agent
    ) throws InterruptedException {
        writeText(NAME_QUOTE_FIELD, (nameQuote));
        click(ARRIVAL_DATA_FIELD);
        writeText(ARRIVAL_DATA_FIELD, (arrivalDate));
        click(DEPARTURE_DATA_FIELD);
        writeText(DEPARTURE_DATA_FIELD, (departureDate));
        click(PAX_FIELD);
        writeText(PAX_FIELD, (pax));
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMPANY_FIELD)).click();
        writeText(COMPANY_FIELD, company);
        click(COMPANY_TYPE_AGENT);
        wait1.until(ExpectedConditions.presenceOfElementLocated(AGENT_FIELD)).click();
        writeText(AGENT_FIELD, agent);
        click(AGENT_TYPE_COMPANY);
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, (property));
        click(PROPERTY_TYPE_TEST);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the quota form without commission")
    public void fillOutTheQuotaFormWhereReservationGuestIsNull(
            String nameQuote,
            String arrivalDate,
            String departureDate,
            String pax,
            String property
    ) throws InterruptedException {
        writeText(NAME_QUOTE_FIELD, (nameQuote));
        click(ARRIVAL_DATA_FIELD);
        writeText(ARRIVAL_DATA_FIELD, (arrivalDate));
        click(DEPARTURE_DATA_FIELD);
        writeText(DEPARTURE_DATA_FIELD, (departureDate));
        click(PAX_FIELD);
        writeText(PAX_FIELD, (pax));
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, (property));
        click(PROPERTY_TYPE_TEST);
        scrollToElement(SEND_TO_MEWS_RADIO_BUTTON);
        click(SEND_TO_MEWS_RADIO_BUTTON);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the quota form without commission")
    public void createMyceQuote_happyPath(
            String nameQuote,
            String arrivalDate,
            String departureDate,
            String pax,
            String property
    ) throws InterruptedException {
        writeText(NAME_QUOTE_FIELD, (nameQuote));
        click(ARRIVAL_DATA_FIELD);
        writeText(ARRIVAL_DATA_FIELD, (arrivalDate));
        click(DEPARTURE_DATA_FIELD);
        writeText(DEPARTURE_DATA_FIELD, (departureDate));
        click(PAX_FIELD);
        writeText(PAX_FIELD, (pax));
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, (property));
        click(PROPERTY_TYPE_TEST);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the quota form without commission")
    public void createMyceQuote_happyPath2(
            String nameQuote,
            String arrivalDate,
            String departureDate,
            String pax,
            String property
    ) throws InterruptedException {
        writeText(NAME_QUOTE_FIELD, (nameQuote));
        click(ARRIVAL_DATA_FIELD);
        writeText(ARRIVAL_DATA_FIELD, (arrivalDate));
        click(DEPARTURE_DATA_FIELD);
        writeText(DEPARTURE_DATA_FIELD, (departureDate));
        click(PAX_FIELD);
        writeText(PAX_FIELD, (pax));
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, (property));
        click(PROPERTY_TYPE_DEMO);
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }



    @Step("Click Edit")
    public void clickEdit(WebDriver driver) throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(EDIT_BUTTON)).click();
        Thread.sleep(3000);
    }

    @Step("Change Stage type on Closed")
    public void changeStage(WebDriver driver) throws InterruptedException {
        scrollToElement(STAGE_FIELD);
        click(STAGE_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(STAGE_TYPE_CLOSED)).click();
    }

    @Step("Change Close status type on Cancelled")
    public void changeCloseStatus(WebDriver driver) throws InterruptedException {
        click(CLOSED_STATUS_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(CLOSED_STATUS_TYPE_CANCELLED)).click();
    }

    @Step("Click Save")
    public void clickSave(WebDriver driver){
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Click Is Confirmed radio button")
    public void clickIsConfirmed(WebDriver driver){
        wait1.until(ExpectedConditions.presenceOfElementLocated(IS_CONFIRMED_RADIO_BUTTON)).click();
    }

    @Step("Open Hotel Rooms")
    public void openHotelRooms(WebDriver driver){
        wait1.until(ExpectedConditions.presenceOfElementLocated(HOTEL_ROOM)).click();
    }

    @Step("Open Meeting Rooms")
    public void openMeetingRooms(WebDriver driver){
        wait1.until(ExpectedConditions.presenceOfElementLocated(MEETING_ROOM)).click();
    }



    @Step("Read error message 1")
    public String readErrorMessage(WebDriver driver) throws InterruptedException {
        return readRecalculateMessage(MESSAGE_TEXT);
    }

    @Step("Read error message 2")
    public String readErrorMessage2(WebDriver driver) throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
    }

    @Step("Close window")
    public void closeWindow(WebDriver driver){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    @Step("Refresh page")
    public void refreshPage(WebDriver driver){
        refreshPage(driver);
    }













}
