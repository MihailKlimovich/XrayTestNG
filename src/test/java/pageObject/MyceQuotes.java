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
    By MESSAGE_TEXT = By.xpath("//div[@class='container']//h2");
    By CLOSE_WINDOW_BUTTON = By.xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");
    By COMMISSION_TYPE_AGENT = By.xpath("//span[@title='Agent']");



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

    @Step("Read error message")
    public String readErrorMessage(WebDriver driver) throws InterruptedException {
        return readRecalculateMessage(MESSAGE_TEXT);
    }

    @Step("Close window")
    public void closeWindow(WebDriver driver){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }






}
