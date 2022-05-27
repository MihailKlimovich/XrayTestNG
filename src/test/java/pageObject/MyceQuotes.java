package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;


public class MyceQuotes extends BasePage {


    /**Constructor*/
    public MyceQuotes(WebDriver driver) {
        super(driver);
    }

    By NAME_PAGE = By.xpath("//div[text()='MYCE Quote']");
    By NEW_MYCE_QUOTE_BUTTON = By.xpath("//div[@class='windowViewMode-maximized oneContent active lafPageHost']//a[@title='New']");
    By MYCE_QUOTE_TAB = By.xpath("//span[text()='MYCE Quotes']");
    By QUATE_RADIO_BUTTON = By.xpath("//div//span[text()='Quote']");
    By NEXT_BUTTON = By.xpath("//button//span[text()='Next']");
    By NAME_QUOTE_FIELD = By.xpath("//div//input[@name='Name']");
    By ARRIVAL_DATA_FIELD = By.xpath("//div//input[@name='thn__Arrival_Date__c']");
    By DEPARTURE_DATA_FIELD = By.xpath("//div//input[@name='thn__Departure_Date__c']");
    By PAX_FIELD = By.xpath("//div//input[@name='thn__Pax__c']");
    By COMMISSIONABLE_RADIO_BUTTON = By.xpath("//div//span[text()='Commissionable']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By SAVE_EDIT_BUTTON = By.xpath("//button[@name='SaveEdit']");
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
    By COMPANY_TYPE_AGENT = By.xpath("//lightning-base-combobox-formatted-text[@title='Test Agent']");
    By AGENT_FIELD = By.xpath("//label[text()='Agent']/following-sibling::div//input");
    By AGENT_TYPE_COMPANY = By.xpath("//lightning-base-combobox-formatted-text[@title='Test Company']");
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
    By MEETING_PACKAGES = By.xpath("//span[@title = 'Meeting Packages']");
    By PRODUCTS = By.xpath("//span[@title = 'Products']");
    By HELP_ERROR_MESSAGE = By.xpath("//div[@data-help-message]");
    By DATA_ERROR_MESSAGE = By.xpath("//div[@data-error-message]");
    By QUOTE_NAME = By.xpath("//slot[@slot='primaryField']//lightning-formatted-text");
    By CLONE_MYCE_QUOTE_BUTTON = By.xpath("//div//runtime_platform_actions-action-renderer[@title='Clone MYCE Quote']");
    By DROP_DOWN_BUTTON = By.xpath("//records-lwc-highlights-panel//lightning-button-menu[@class='menu-button-item slds-dropdown-trigger slds-dropdown-trigger_click']//button");
    By KEEP_ALL_PAX_CHECKBOX = By.xpath("//span//input[@name='Keep all Pax']/following-sibling::label//span[@class='slds-checkbox_faux']");
    By KEEP_ROOMS_PAX_CHECKBOX = By.xpath("//input[@name='Keep rooms Pax']/following-sibling::label//span[@class='slds-checkbox_faux']");
    By SAVE_BUTTON_FOR_CLONE = By.xpath("//footer//button[text()='Save']");
    By CHANGE_DATE_BUTTON = By.xpath("//button[text()='Change Date']");
    By NEW_ARRIVAL_DATE_FIELD = By.xpath("//input[@name='New_arrival_date']");
    By NEXT_BUTTON_CHANGE_DATE_WINDOW = By.xpath("//button[@class='slds-button slds-button_brand flow-button__NEXT']");
    By FINISH_BUTTON_CHANGE_DATE_WINDOW = By.xpath("//button[@class='slds-button slds-button_brand flow-button__FINISH']");
    By UPDATE_ORDER_BUTTON = By.xpath("//button[@name='thn__MYCE_Quote__c.Update_Order']");
    By CLONE_TO_DATE_FIELD = By.xpath("//thn-clone-multi-select-component//input[@name='cloneTo']");
    By CREATE_CLONE_BUTTON = By.xpath("//thn-clone-multi-select-component//button[text()='Create']");
    By CLONE_QUOTE_ARRIVAL_DAY_FIELD = By.xpath("//div//input[@name='Arrival Date']");
    By SERIE_TAB = By.xpath("//li//a[@data-label='Serie']");
    By DOCUMENTS_TAB = By.xpath("//li//a[@data-label='Documents']");
    By FILES = By.xpath("//span[@title = 'Files']");
    By ROOMING_LIST_TAB = By.xpath("//li//a[@data-label='Rooming List']");
    By EDIT_ARRIVAL_DATE_BUTTON = By.xpath("//button[@title='Edit Arrival Date']");
    By CLONE_PAX_FIELD = By.xpath("//input[@name='Pax']");
    By CHECK_AVAILABILITIES_BUTTON = By.xpath("//button[@name='thn__MYCE_Quote__c.Check_Avalabilities']");
    By QUOTE_NAME_FIELD = By.xpath("//span[text()='Name']/parent::div/following::div//span//slot//lightning-formatted-text");
    By GROUP_BOOKING_TAB = By.xpath("//li//a[@data-label='Group Booking']");




    @Step("Open MYCE Quote record")
    public void openMyceQoteRecord(String name) throws InterruptedException {
        waitForTests.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//a[@title='" + name + "']")));
        //wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//a[@title='" + name + "']")));
        click(By.xpath("//table//a[@title='" + name + "']"));
    }

    @Step("Open Myce Quote page")
    public MyceQuotes goToMyceQuotes() throws MalformedURLException {
        try {
        URL baseUrl = new URL(driver.getCurrentUrl());
        String url = "https://" + baseUrl.getAuthority() + "/lightning/o/thn__MYCE_Quote__c/list?filterName=Recent";
        System.out.println(url);
        driver.navigate().to(url);
        } catch (Exception ex){ex.printStackTrace();
        }
        try {
            if (wait2.until(ExpectedConditions.alertIsPresent()) != null) {
                Alert alert = wait2.until(alertIsPresent());
                alert.accept();
            }
        } catch (TimeoutException e) {
        }
        return this;
    }

    @Step("Open Files page")
    public void goToFiles() throws InterruptedException {
        click3(DOCUMENTS_TAB);
        click3(FILES);
    }

    @Step("Click MYCE Quote tab")
    public void clickMyceQuoteTab() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(MYCE_QUOTE_TAB));
        Thread.sleep(1000);
        click3(MYCE_QUOTE_TAB);
    }

    @Step("Click Сheck Availabilities button")
    public void clickCheckkAvailabilitiesButton() throws InterruptedException {
        click4(CHECK_AVAILABILITIES_BUTTON);
    }


    @Step("Clone Myce Quote")
    public void cloneMyceQuote(String name, String date, String pax) throws InterruptedException {
        //waitForTests.until(ExpectedConditions.presenceOfElementLocated(CLONE_MYCE_QUOTE_BUTTON));
        click4(CLONE_MYCE_QUOTE_BUTTON);
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_QUOTE_FIELD)).click();
        writeText(NAME_QUOTE_FIELD, name);
        click3(CLONE_QUOTE_ARRIVAL_DAY_FIELD);
        Thread.sleep(1000);
        clear(CLONE_QUOTE_ARRIVAL_DAY_FIELD);
        writeText(CLONE_QUOTE_ARRIVAL_DAY_FIELD, date);
        click3(CLONE_PAX_FIELD);
        clear(CLONE_PAX_FIELD);
        writeText(CLONE_PAX_FIELD, pax);
        wait1.until(ExpectedConditions.presenceOfElementLocated(KEEP_ALL_PAX_CHECKBOX));
        Thread.sleep(1000);
        click3(KEEP_ALL_PAX_CHECKBOX);
        wait1.until(ExpectedConditions.presenceOfElementLocated(KEEP_ROOMS_PAX_CHECKBOX));
        click3(KEEP_ROOMS_PAX_CHECKBOX);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON_FOR_CLONE)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//slot[@name='primaryField']//lightning-formatted-text[text()='" + name + "']")));
        Thread.sleep(5000);
    }

    @Step("Clone Myce Quote and read name")
    public String cloneMyceQuote_readName(String name, String date, String pax) throws InterruptedException {
        //waitForTests.until(ExpectedConditions.presenceOfElementLocated(CLONE_MYCE_QUOTE_BUTTON));
        click4(CLONE_MYCE_QUOTE_BUTTON);
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_QUOTE_FIELD)).click();
        writeText(NAME_QUOTE_FIELD, name);
        click3(CLONE_QUOTE_ARRIVAL_DAY_FIELD);
        Thread.sleep(1000);
        clear(CLONE_QUOTE_ARRIVAL_DAY_FIELD);
        writeText(CLONE_QUOTE_ARRIVAL_DAY_FIELD, date);
        click3(CLONE_PAX_FIELD);
        clear(CLONE_PAX_FIELD);
        writeText(CLONE_PAX_FIELD, pax);
        wait1.until(ExpectedConditions.presenceOfElementLocated(KEEP_ALL_PAX_CHECKBOX));
        Thread.sleep(1000);
        click3(KEEP_ALL_PAX_CHECKBOX);
        wait1.until(ExpectedConditions.presenceOfElementLocated(KEEP_ROOMS_PAX_CHECKBOX));
        click3(KEEP_ROOMS_PAX_CHECKBOX);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON_FOR_CLONE)).click();
        Thread.sleep(5000);
        refreshPage();
        return readRecalculateMessage(QUOTE_NAME_FIELD);
    }

    @Step("Clone Myce Quote. Change date and pax. Keep all pax = true")
    public void cloneMyceQuote_changeDateAndPaxKeepAllPax(String name, String date, String pax) throws InterruptedException {
        //waitForTests.until(ExpectedConditions.presenceOfElementLocated(CLONE_MYCE_QUOTE_BUTTON));
        click4(CLONE_MYCE_QUOTE_BUTTON);
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_QUOTE_FIELD)).click();
        writeText(NAME_QUOTE_FIELD, name);
        click3(CLONE_QUOTE_ARRIVAL_DAY_FIELD);
        clear(CLONE_QUOTE_ARRIVAL_DAY_FIELD);
        writeText(CLONE_QUOTE_ARRIVAL_DAY_FIELD, date);
        click3(CLONE_PAX_FIELD);
        clear(CLONE_PAX_FIELD);
        writeText(CLONE_PAX_FIELD, pax);
        click3(KEEP_ALL_PAX_CHECKBOX);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON_FOR_CLONE)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//slot[@name='primaryField']//lightning-formatted-text[text()='" + name + "']")));
        Thread.sleep(5000);
    }

    @Step("Clone Myce Quote. Change date and pax. Keep room pax = true")
    public void cloneMyceQuote_changeDateAndPaxKeepRoomPax(String name, String date, String pax) throws InterruptedException {
        //waitForTests.until(ExpectedConditions.presenceOfElementLocated(CLONE_MYCE_QUOTE_BUTTON));
        click4(CLONE_MYCE_QUOTE_BUTTON);
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_QUOTE_FIELD)).click();
        writeText(NAME_QUOTE_FIELD, name);
        click3(CLONE_QUOTE_ARRIVAL_DAY_FIELD);
        clear(CLONE_QUOTE_ARRIVAL_DAY_FIELD);
        writeText(CLONE_QUOTE_ARRIVAL_DAY_FIELD, date);
        click3(CLONE_PAX_FIELD);
        clear(CLONE_PAX_FIELD);
        writeText(CLONE_PAX_FIELD, pax);
        click3(KEEP_ROOMS_PAX_CHECKBOX);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON_FOR_CLONE)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//slot[@name='primaryField']//lightning-formatted-text[text()='" + name + "']")));
        Thread.sleep(5000);
    }

    @Step("Clone Myce Quote. Change date and pax. ")
    public void cloneMyceQuote_changeDateAndPax(String name, String date, String pax) throws InterruptedException {
        //waitForTests.until(ExpectedConditions.presenceOfElementLocated(CLONE_MYCE_QUOTE_BUTTON));
        click4(CLONE_MYCE_QUOTE_BUTTON);
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_QUOTE_FIELD)).click();
        writeText(NAME_QUOTE_FIELD, name);
        click3(CLONE_QUOTE_ARRIVAL_DAY_FIELD);
        clear(CLONE_QUOTE_ARRIVAL_DAY_FIELD);
        writeText(CLONE_QUOTE_ARRIVAL_DAY_FIELD, date);
        click3(CLONE_PAX_FIELD);
        clear(CLONE_PAX_FIELD);
        writeText(CLONE_PAX_FIELD, pax);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON_FOR_CLONE)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//slot[@name='primaryField']//lightning-formatted-text[text()='" + name + "']")));
        Thread.sleep(5000);
    }

    @Step("Clone related record")
    public void cloneRelatedRecord(String date, String nameRecord) throws InterruptedException {
        waitForTests.until(ExpectedConditions.presenceOfElementLocated(SERIE_TAB));
        Thread.sleep(1000);
        click(SERIE_TAB);
        wait1.until(ExpectedConditions.presenceOfElementLocated(CLONE_TO_DATE_FIELD));
        click(CLONE_TO_DATE_FIELD);
        ctrlA();
        delete();
        Thread.sleep(1000);
        writeText(CLONE_TO_DATE_FIELD, date);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.
                xpath("//div//label[text()='Components to clone']/following::select//option[text()='" + nameRecord + "']"))).click();
        Thread.sleep(1000);
        wait1.until(ExpectedConditions.elementToBeClickable(CREATE_CLONE_BUTTON)).click();
        Thread.sleep(10000);
    }

    @Step
    public void createNewMyceQuote() {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NEW_MYCE_QUOTE_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(QUATE_RADIO_BUTTON)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON)).click();
    }

    @Step("Change date")
    public void changeDate(String date) throws InterruptedException {
        click4(CHANGE_DATE_BUTTON);
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(NEW_ARRIVAL_DATE_FIELD)).click();
        clear(NEW_ARRIVAL_DATE_FIELD);
        writeText(NEW_ARRIVAL_DATE_FIELD, date);
        wait1.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON_CHANGE_DATE_WINDOW)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[text()='All the dates of the quote will be updated. Click Next to confirm.']")));
        wait1.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON_CHANGE_DATE_WINDOW)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[text()='Quote date has been updated.']")));
        wait1.until(ExpectedConditions.elementToBeClickable(FINISH_BUTTON_CHANGE_DATE_WINDOW)).click();
        Thread.sleep(2000);
    }

    @Step("Edit arrival date")
    public void editArrivalDate(String date) throws InterruptedException {
        click3(EDIT_ARRIVAL_DATE_BUTTON);
        click3(ARRIVAL_DATA_FIELD);
        Thread.sleep(1000);
        clear(ARRIVAL_DATA_FIELD);
        writeText(ARRIVAL_DATA_FIELD, date);
        click4(SAVE_EDIT_BUTTON);
    }

    @Step("Fill out the quota form when Commission is None")
    public void fillOutTheQuotaForm_whenCommissionIsNone(
            String nameQuote,
            String arrivalDate,
            String departureDate,
            String pax,
            String property
    ) throws InterruptedException {
        Thread.sleep(500);
        writeText(NAME_QUOTE_FIELD, (nameQuote));
        click(ARRIVAL_DATA_FIELD);
        Thread.sleep(500);
        writeText(ARRIVAL_DATA_FIELD, (arrivalDate));
        click(DEPARTURE_DATA_FIELD);
        Thread.sleep(500);
        writeText(DEPARTURE_DATA_FIELD, (departureDate));
        click(PAX_FIELD);
        Thread.sleep(500);
        writeText(PAX_FIELD, (pax));
        wait1.until(ExpectedConditions.elementToBeClickable(COMMISSIONABLE_RADIO_BUTTON)).click();
        Thread.sleep(500);
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSION_FIELD)).click();
        Thread.sleep(500);
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSION_TYPE_NONE)).click();
        Thread.sleep(500);
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        Thread.sleep(500);
        writeText(PROPERTY_FIELD, (property));
        Thread.sleep(500);
        click(PROPERTY_TYPE_DEMO);
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
        Thread.sleep(500);
        writeText(NAME_QUOTE_FIELD, (nameQuote));
        click(ARRIVAL_DATA_FIELD);
        Thread.sleep(500);
        writeText(ARRIVAL_DATA_FIELD, (arrivalDate));
        click(DEPARTURE_DATA_FIELD);
        Thread.sleep(500);
        writeText(DEPARTURE_DATA_FIELD, (departureDate));
        click(PAX_FIELD);
        Thread.sleep(500);
        writeText(PAX_FIELD, (pax));
        wait1.until(ExpectedConditions.elementToBeClickable(COMMISSIONABLE_RADIO_BUTTON)).click();
        Thread.sleep(500);
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSION_FIELD)).click();
        Thread.sleep(500);
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSION_TYPE_AGENT)).click();
        Thread.sleep(500);
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        Thread.sleep(500);
        writeText(PROPERTY_FIELD, (property));
        Thread.sleep(500);
        click(PROPERTY_TYPE_DEMO);
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
        click(PROPERTY_TYPE_DEMO);
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
        click(PROPERTY_TYPE_DEMO);
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
        click2(COMPANY_TYPE_AGENT);
        wait1.until(ExpectedConditions.presenceOfElementLocated(AGENT_FIELD)).click();
        writeText(AGENT_FIELD, agent);
        click2(AGENT_TYPE_COMPANY);
        scrollToElement(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, (property));
        click(PROPERTY_TYPE_DEMO);
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
        click(PROPERTY_TYPE_DEMO);
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
        click(PROPERTY_TYPE_DEMO);
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
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_QUOTE_FIELD));
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
        Thread.sleep(2000);
        refreshPage();
    }


    @Step("Click Edit")
    public void clickEdit() throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(EDIT_BUTTON)).click();
        Thread.sleep(3000);
    }

    @Step("Click Group Booking Tab")
    public void clickGroupBookingTab() throws IOException, InterruptedException {
        click3(GROUP_BOOKING_TAB);
    }

    @Step("Change Stage type on Closed")
    public void changeStage() throws InterruptedException {
        scrollToElement(STAGE_FIELD);
        click(STAGE_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(STAGE_TYPE_CLOSED)).click();
    }

    @Step("Change Close status type on Cancelled")
    public void changeCloseStatus() throws InterruptedException {
        click(CLOSED_STATUS_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(CLOSED_STATUS_TYPE_CANCELLED)).click();
    }

    @Step("Click Save")
    public void clickSave() {
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Click Is Confirmed radio button")
    public void clickIsConfirmed() {
        wait1.until(ExpectedConditions.presenceOfElementLocated(IS_CONFIRMED_RADIO_BUTTON)).click();
    }


    @Step("Open Hotel Rooms")
    public void openHotelRooms() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_PAGE));
        scrollToElement(HOTEL_ROOM);
        clickInvisibleElement(HOTEL_ROOM);
    }

    @Step("Open Meeting Rooms")
    public void openMeetingRooms() throws InterruptedException {
        scrollDownToTheEnd();
        scrollToElement(MEETING_ROOM);
        wait1.until(ExpectedConditions.presenceOfElementLocated(MEETING_ROOM));
        clickInvisibleElement(MEETING_ROOM);
    }

    @Step("Open Meeting Packages")
    public void openMeetingPackages() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_PAGE));
        scrollDownToTheEnd();
        scrollToElement(MEETING_PACKAGES);
        wait1.until(ExpectedConditions.presenceOfElementLocated(MEETING_PACKAGES));
        clickInvisibleElement(MEETING_PACKAGES);
    }

    @Step("Open quote Product")
    public void openProducts() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_PAGE));
        scrollDownToTheEnd();
        scrollToElement(PRODUCTS);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCTS));
        clickInvisibleElement(PRODUCTS);
    }

    @Step("Open first product")
    public void openProduct(String productName) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.
                xpath("//a[@title='" + productName + "']"))).click();
        Thread.sleep(2000);
    }

    @Step("Read error message 1")
    public String readErrorMessage() throws InterruptedException {
        return readRecalculateMessage(MESSAGE_TEXT);
    }

    @Step("Read error message 2")
    public String readErrorMessage2() throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
    }

    @Step("Read data error message")
    public String readDataErrorMessage() throws InterruptedException {
        return readRecalculateMessage(DATA_ERROR_MESSAGE);
    }

    @Step("Read help error message")
    public String readHelpErrorMessage() throws InterruptedException {
        return readRecalculateMessage(HELP_ERROR_MESSAGE);
    }

    @Step("Read help error message")
    public String readQuoteName() throws InterruptedException {
        return readRecalculateMessage(QUOTE_NAME);
    }


    @Step("Close window")
    public void closeWindow() throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
        Thread.sleep(1000);
    }

    @Step("Refresh page")
    public void refreshPage(WebDriver driver) {
        refreshPage(driver);
    }

    @Step("Update order")
    public void updateOrder() throws InterruptedException {
        click4(UPDATE_ORDER_BUTTON);
        //wait1.until(ExpectedConditions.elementToBeClickable(UPDATE_ORDER_BUTTON));
        //Thread.sleep(2000);
        //click3(UPDATE_ORDER_BUTTON);
        Thread.sleep(3000);
    }

    @Step("Upload rooming list")
    public void uploadFile(String filePath) throws IOException, InterruptedException {
        refreshPage();
        int attempts = 0;
        while (attempts < 4) {
            try {
                click3(ROOMING_LIST_TAB);
                Thread.sleep(3000);
                //wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//slot/child::input"))).sendKeys("" + filePath + "");
                WebElement addFile = driver.findElement(By.xpath("//slot/child::input"));
                addFile.sendKeys(filePath);
                Thread.sleep(3000);
                wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='Success']")));
                break;
        /*By fileInput = By.cssSelector("input[type=file]");
        driver.findElement(fileInput).sendKeys(filePath);*/
            }
            catch (Exception e){
                refreshPage();
            }
            attempts++;
        }
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create Quote SFDX")
    public String createQuoteSFDX(String sfdxPath, String value, String userName) throws IOException, InterruptedException {
        StringBuilder myseQuoteResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Quote create result:");
        System.out.println(myseQuoteResult);
        String myceQuoteID = JsonParser2.getFieldValue(myseQuoteResult.toString(), "id");
        return myceQuoteID;
    }

    @Step("Delete Quote SFDX")
    public void deleteQuoteSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

    @Step("Get Quote SFDX")
    public StringBuilder getQuoteSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder quoteRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return quoteRecord;
    }

    @Step("Update Quote SFDX")
    public StringBuilder updateQuoteSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder quoteUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__MYCE_Quote__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(quoteUpdateResult);
        return quoteUpdateResult;
    }

    @Step("SOQL")
    public StringBuilder soql(String sfdxPath, String soql, String userName) throws IOException, InterruptedException {
        StringBuilder jsonObject = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:soql:query",
                "-q",
                soql,
                "-u",
                userName,
                "--json"});
        return jsonObject;
    }

    @Step("SOQL delete")
    public void soqlDeleteRecords(String sfdxPath, String soql, String object,  String where, String userName) throws IOException, InterruptedException {
        StringBuilder jsonObject = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:soql:query",
                "-q",
                soql,
                "-u",
                userName,
                "--json"});
        List<String> recordsID = JsonParser2.getFieldValueSoql(jsonObject.toString(), "Id");
        int quantity = recordsID.size();
        int index = 0;
        while (quantity > 0) {
            StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                    sfdxPath,
                    "force:data:record:delete",
                    "-s",
                    object,
                    "-w",
                    where,
                    "-u",
                    userName,
                    "--json"});
            System.out.println(result);
            quantity--;
            index++;
        }

    }




















}
