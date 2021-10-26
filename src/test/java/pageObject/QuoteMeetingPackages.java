package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.io.IOException;

public class QuoteMeetingPackages extends BasePage {

    /**Constructor*/
    public QuoteMeetingPackages(WebDriver driver) {
        super(driver);
    }

    By NEW_MEETING_PACKAGES = By.xpath("//h1[text()='Meeting Packages']/following::div[@title='New']");
    By PACKAGE_FIELD = By.xpath("//slot//label[text()='Package']/following-sibling::div//input");
    By PACKAGE_TYPE = By.xpath("//span[@title='//span[@title='Package 1']']");
    By START_DATE_FIELD = By.xpath("//slot//label[text()='Start Date']/following-sibling::div//input");
    By END_DATE_FIELD = By.xpath("//slot//label[text()='End Date']/following-sibling::div//input");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By MESSAGE_ERROR_TEXT = By.xpath("//div[@class='container']//li");
    By CLOSE_WINDOW_BUTTON = By.
            xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");
    By PAX_FIELD = By.xpath("//div//input[@name='thn__Pax__c']");
    By UNIT_PRICE_FIELD = By.xpath("//slot//label[text()='Unit Price']/following-sibling::div//input");
    By EDIT_BUTTON = By.xpath("//div[text()='Quote Package']/following::button[@name='Edit']");
    By DISCOUNT_FIELD = By.xpath("//slot//label[text()='Discount']/following-sibling::div//input");
    By HELP_ERROR_MESSAGE = By.xpath("//div[@data-help-message]");
    By MULTI_DELETE_BUTTON = By.xpath("//a[@title='Multidelete']");



    @Step("Fill out the meeting room")
    public void createMeetingPackages(String pack, String pax, String startDate, String endDate, String unitPrice) throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_MEETING_PACKAGES)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PACKAGE_FIELD));
        click2(PACKAGE_FIELD);
        writeText(PACKAGE_FIELD, pack);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//lightning-base-combobox-formatted-text[@title='" + pack + "']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(PAX_FIELD)).click();
        writeText(PAX_FIELD, pax);
        wait1.until(ExpectedConditions.presenceOfElementLocated(START_DATE_FIELD)).click();
        writeText(START_DATE_FIELD, startDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(END_DATE_FIELD)).click();
        writeText(END_DATE_FIELD, endDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(UNIT_PRICE_FIELD)).click();
        writeText(UNIT_PRICE_FIELD, unitPrice);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
        Thread.sleep(5000);
    }

    @Step("Read error message 2")
    public String readErrorMessage2() throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
    }

    @Step("Read help error message")
    public String readHelpErrorMessage() throws InterruptedException {
        return readRecalculateMessage(HELP_ERROR_MESSAGE);
    }

    @Step("Change date ")
    public void changeDate(String startDate, String endDate) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(START_DATE_FIELD)).click();
        clear(START_DATE_FIELD);
        writeText(START_DATE_FIELD, startDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(END_DATE_FIELD)).click();
        clear(END_DATE_FIELD);
        writeText(END_DATE_FIELD, endDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }

    @Step
    public void changeDiscount(String discount){
        scrollToElement(DISCOUNT_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(DISCOUNT_FIELD)).click();
        clear(DISCOUNT_FIELD);
        writeText(DISCOUNT_FIELD, discount);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }

    @Step("Return to MYCE Quote")
    public void clickQuoteName(String nameQuote) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@force-highlights2_highlights2]//span[text()='"+ nameQuote +"']"))).click();
        refreshPage();
        Thread.sleep(2000);
    }

    @Step("Click Edit")
    public void clickEdit(){
        wait1.until(ExpectedConditions.presenceOfElementLocated(EDIT_BUTTON)).click();
    }

    @Step("Close window")
    public void closeWindow(){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    @Step("Click multi delete button")
    public  void multiDeleteRecords() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(MULTI_DELETE_BUTTON));
        click3(MULTI_DELETE_BUTTON);
        Thread.sleep(5000);
        down();
        down();
        tab();
        enter();
        Thread.sleep(10000);
        enter();
    }

    @Step("Select item number")
    public  void selectItemNumber(String numberOfElements) throws InterruptedException {
        refreshPage();
        By SELECT_NUMBer_ITEM_CHECKBOX = By.xpath("//span[text()='Select item "+ numberOfElements + "']/preceding-sibling::span");
        wait1.until(ExpectedConditions.presenceOfElementLocated(SELECT_NUMBer_ITEM_CHECKBOX));
        click3(SELECT_NUMBer_ITEM_CHECKBOX);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Create Quote Package SFDX")
    public String createQuotePackageSFDX(String sfdxPath, String value, String userName) throws IOException, InterruptedException {
        StringBuilder quotePackageResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Quote_Package__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(quotePackageResult);
        String quotePackageID = JsonParser2.getFieldValue(quotePackageResult.toString(), "id");
        return quotePackageID;
    }


}
