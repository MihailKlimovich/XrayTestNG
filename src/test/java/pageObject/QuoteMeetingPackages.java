package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

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



    @Step("Fill out the meeting room")
    public void createMeetingPackages(String pack, String pax, String startDate, String endDate, String unitPrice) throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_MEETING_PACKAGES)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PACKAGE_FIELD));
        click2(PACKAGE_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='" + pack + "']"))).click();
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
    public String readErrorMessage2(WebDriver driver) throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
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


}
