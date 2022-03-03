package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.io.IOException;

public class QuoteMeetingRoom extends BasePage {

    /**Constructor*/
    public QuoteMeetingRoom(WebDriver driver) {
        super(driver);
    }

    By NEW_MEETING_ROOM = By.xpath("//span[text()='Meeting Rooms']/following::div[@title='New']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By SAVE_EDIT_BUTTON = By.xpath("//button[@name='SaveEdit']");
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
    By END_DATE_FIELD = By.xpath("//div//label[text()='Date']/following::input[@name='thn__End_Date_Time__c'][1]");
    By START_DATE_FIELD = By.xpath("//div//label[text()='Date']/following::input[@name='thn__Start_Date_Time__c'][1]");
    By START_TIME_FIELD = By.xpath("//label[text()='Time']/following::input[@name='thn__Start_Date_Time__c']");
    By END_TIME_FIELD = By.xpath("//label[text()='Time']/following::input[@name='thn__End_Date_Time__c'][2]");
    By MYCE_QUOTE_NAME = By.xpath("//div//span[@id = 'window']");
    By MULTI_DELETE_BUTTON = By.xpath("//a[@title='Multidelete']");
    By MULTIEDIT_BUTTON = By.xpath("//h1[text()='Meeting Rooms']/following::div[@title='Multiedit']");
    By CHANGE_RESOURCE_BUTTON = By.xpath("//div[@title='Change Resource']");
    By TOAST_MESSSAGE = By.xpath("//span[@class='toastMessage slds-text-heading--small forceActionsText'][1]");
    By EDIT_START_DAYE_TIME_BUTTON = By.xpath("//button[@title='Edit Start Date/Time']");









    @Step("Click Multiedit")
    public void clickMultiedit() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(MULTIEDIT_BUTTON)).click();
        Thread.sleep(3000);
    }

    @Step("Click Edit")
    public void clickEdit() throws InterruptedException {
        click3(EDIT_BUTTON);
    }



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

    @Step("Create quote meeting room")
    public void createQuoteMeetingRoom(String resource, String product,  String startDate, String endDate,
                                       String startTime, String endTime) throws InterruptedException {
        click3(NEW_MEETING_ROOM);
        click3(RESOURCE_FIELD);
        click3(By.xpath("//span[@title='" + resource + "']"));
        click3(PRODUCT_FIELD);
        click3(By.xpath("//span[@title='" + product + "']"));
        click3(START_DATE_FIELD);
        clear(START_DATE_FIELD);
        writeText(START_DATE_FIELD, startDate);
        click3(END_DATE_FIELD);
        clear(END_DATE_FIELD);
        writeText(END_DATE_FIELD, endDate);
        click3(START_TIME_FIELD);
        clear(START_TIME_FIELD);
        writeText(START_TIME_FIELD, startTime);
        click3(END_TIME_FIELD);
        clear(END_TIME_FIELD);
        writeText(END_TIME_FIELD, endTime);
        click4(SAVE_BUTTON);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1//div[text()='Quote Meetings Room']")));
    }

    @Step("Create quote meeting room 2")
    public void createQuoteMeetingRoom2(String product,  String startDate, String endDate,
                                       String startTime, String endTime) throws InterruptedException {
        click3(NEW_MEETING_ROOM);
        click3(PRODUCT_FIELD);
        click3(By.xpath("//span[@title='" + product + "']"));
        click3(START_DATE_FIELD);
        clear(START_DATE_FIELD);
        writeText(START_DATE_FIELD, startDate);
        click3(END_DATE_FIELD);
        clear(END_DATE_FIELD);
        writeText(END_DATE_FIELD, endDate);
        click3(START_TIME_FIELD);
        clear(START_TIME_FIELD);
        writeText(START_TIME_FIELD, startTime);
        click3(END_TIME_FIELD);
        clear(END_TIME_FIELD);
        writeText(END_TIME_FIELD, endTime);
        click4(SAVE_BUTTON);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1//div[text()='Quote Meetings Room']")));
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

    @Step("Edit date")
    public void editDateTime(String startDate, String endDate, String startTime, String endTime)
            throws InterruptedException {
        click3(START_DATE_FIELD);
        clear(START_DATE_FIELD);
        writeText(START_DATE_FIELD, startDate);
        click3(END_DATE_FIELD);
        clear(END_DATE_FIELD);
        writeText(END_DATE_FIELD, endDate);
        click3(START_TIME_FIELD);
        clear(START_TIME_FIELD);
        writeText(START_TIME_FIELD, startTime);
        click3(END_TIME_FIELD);
        clear(END_TIME_FIELD);
        writeText(END_TIME_FIELD, endTime);
        click4(SAVE_BUTTON);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1//div[text()='Quote Meetings Room']")));
    }

    @Step("Edit start/end time")
    public void editTime(String startTime, String endTime)
            throws InterruptedException {
        click3(EDIT_START_DAYE_TIME_BUTTON);
        click3(START_TIME_FIELD);
        clear(START_TIME_FIELD);
        writeText(START_TIME_FIELD, startTime);
        click3(END_TIME_FIELD);
        clear(END_TIME_FIELD);
        writeText(END_TIME_FIELD, endTime);
        click4(SAVE_EDIT_BUTTON);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1//div[text()='Quote Meetings Room']")));
    }

    @Step("Open record bu number")
    public void openRecordByName(String recordName) throws InterruptedException {
        click3(By.xpath("//a[@title='" + recordName + "']"));
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

    @Step("Click 'Change resource'")
    public void clickChangeResource() throws InterruptedException {
        click4(CHANGE_RESOURCE_BUTTON);
        Thread.sleep(5000);
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

    @Step("Read toast message")
    public String readToastMessage() throws InterruptedException {
        return readRecalculateMessage(TOAST_MESSSAGE);
    }

    @Step("Close window")
    public void closeWindow(){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    @Step("Return to MYCE Quote")
    public void clickQuoteName(String nameQuote){
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.
                xpath("//div[@force-highlights2_highlights2]//span[text()='"+ nameQuote +"']"))).click();
    }

    @Step("Select item")
    public  void selectItem(String itemNumber) throws InterruptedException {
        refreshPage();
        By SELECT_ITEM_CHECKBOX = By.
                xpath("//span[text()='Select item " + itemNumber + "']/preceding-sibling::span");
        wait1.until(ExpectedConditions.presenceOfElementLocated(SELECT_ITEM_CHECKBOX));
        click2(SELECT_ITEM_CHECKBOX);
    }

    @Step("Select all items")
    public  void selectItems(String numberOfElements) throws InterruptedException {
        refreshPage();
        By SELECT_ALL_ITEMS_CHECKBOX = By.
                xpath("//span[text()='Select " + numberOfElements + " items']/preceding-sibling::span");
        click3(SELECT_ALL_ITEMS_CHECKBOX);
    }

    @Step("Click multi delete button")
    public  void clickMultiDeleteButton() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(MULTI_DELETE_BUTTON));
        click3(MULTI_DELETE_BUTTON);
        Thread.sleep(3000);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Create Quote Meeting Room SFDX")
    public String createQuoteMeetingRoomSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder quoteMeetingRoomResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Quote meeting room create result:");
        System.out.println(quoteMeetingRoomResult);
        String quoteMeetingRoomID = JsonParser2.getFieldValue(quoteMeetingRoomResult.toString(), "id");
        return quoteMeetingRoomID;
    }

    @Step("Get Quote Meeting Room SFDX")
    public StringBuilder getQuoteMeetingRoomSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder quoteMeetingRoomRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return quoteMeetingRoomRecord;
    }

    @Step("Update Quote Meeting Room SFDX")
    public void updateQuoteMeetingRoomSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder quoteMeetingRoomUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(quoteMeetingRoomUpdateResult);
    }

    @Step("Delete Quote Meeting Room SFDX")
    public void deleteQuoteMeetingRoomSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Quote_Meeting_Room__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }







}
