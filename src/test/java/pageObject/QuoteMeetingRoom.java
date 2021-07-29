package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class QuoteMeetingRoom extends BasePage {

    /**Constructor*/
    public QuoteMeetingRoom(WebDriver driver) {
        super(driver);
    }

    By NEW_MEETING_ROOM = By.xpath("//span[text()='Meeting Rooms']/following::div[@title='New']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By PRODUCT_FIELD = By.xpath("//slot//label[text()='Product']/following-sibling::div//input");
    By PRODUCT_TYPE_ROOM1NIGHT = By.xpath("//span[@title='MEETING HALF DAY']");
    By SETUP_FIELD = By.xpath("//slot//label[text()='Setup']/following-sibling::div//input");
    By SETUP_TYPE_BUFFET = By.xpath("//span[@title='Buffet']");
    By SETUP_TYPE_CABARET = By.xpath("//span[@title='Cabaret']");
    By SETUP_TYPE_CIRCLE = By.xpath("//span[@title='Circle']");
    By SETUP_TYPE_CLASSROOM = By.xpath("//span[@title='Classroom']");
    By SETUP_TYPE_CUSTOM = By.xpath("//span[@title='Custom']");
    By SETUP_TYPE_DINNER = By.xpath("//span[@title='Dinner']");
    By SETUP_TYPE_PARTY = By.xpath("//span[@title='Party']");
    By SETUP_TYPE_SQUARE = By.xpath("//span[@title='Square']");
    By SETUP_TYPE_THEATER = By.xpath("//span[@title='Theater']");
    By SETUP_TYPE_USHAPE = By.xpath("//span[@title='U-Shape']");
    By RESOURCE_FIELD = By.xpath("//slot//label[text()='Resource']/following-sibling::div//input");
    By RESOURCE_TYPE_TESTRES = By.xpath("//span[@title='TestRes']");
    By MESSAGE_ERROR_TEXT = By.xpath("//div[@class='container']//li");
    By CLOSE_WINDOW_BUTTON = By.
            xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");
    By PAX_FIELD = By.xpath("//div//input[@name='thn__Pax__c']");
    By EDIT_BUTTON = By.xpath("//div[text()='Quote Meetings Room']/following::button[@name='Edit']");
    By LOCK_RESOURCE_CHECKBOX = By.
            xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//div//input[@name = 'thn__Lock_Resource__c']");
    By END_DATE_FIELD = By.xpath("//div//label[text()='Date']/following::input[@name='thn__End_Date_Time__c']");
    By MYCE_QUOTE_NAME = By.xpath("//div//span[@id = 'window']");






    @Step("Fill out the meeting room")
    public void createMeetingRoom(String pax) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NEW_MEETING_ROOM)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_TYPE_ROOM1NIGHT)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(RESOURCE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(RESOURCE_TYPE_TESTRES)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(SETUP_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(SETUP_TYPE_BUFFET)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(PAX_FIELD)).click();
        writeText(PAX_FIELD, pax);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }

    @Step("Fill out the meeting room")
    public void createMeetingRoom2(String roomType) throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_MEETING_ROOM)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD)).click();
        click3(PRODUCT_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='" + roomType +"']")));
        click3(By.xpath("//span[@title='" + roomType +"']"));
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }

    @Step("Edit date")
    public void editDate(String record, String endDate){
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + record + "']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(EDIT_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(END_DATE_FIELD)).click();
        clear(END_DATE_FIELD);
        writeText(END_DATE_FIELD, endDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }

    @Step("Change resource when Lock Resource is true")
    public void  changeResource() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(EDIT_BUTTON));
        click2(EDIT_BUTTON);
        wait1.until(ExpectedConditions.presenceOfElementLocated(LOCK_RESOURCE_CHECKBOX));
        click3(LOCK_RESOURCE_CHECKBOX);
        //clickInvisibleElement(RESOURCE_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(RESOURCE_FIELD));
        click2(RESOURCE_FIELD);
        //clickInvisibleElement(RESOURCE_FIELD);
        delete();
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }

    public void changeSetupType(String type) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(SETUP_FIELD)).click();
        clickInvisibleElement(By.xpath("//span[@title='" + type + "']"));
        //wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='" + type + "']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();

    }

    @Step("Read error message 2")
    public String readErrorMessage2() throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
    }

    @Step("Close window")
    public void closeWindow(){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    @Step("Return to MYCE Quote")
    public void clickQuoteName(String nameQuote){
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@force-highlights2_highlights2]//span[text()='"+ nameQuote +"']"))).click();
    }






}
