package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.io.IOException;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

public class Guests extends BasePage {

    /**Constructor*/
    public Guests(WebDriver driver) {
        super(driver);
    }

    By NEW_GUEST = By.xpath("//span[text()='Guests']/following::div[@title='New']");
    By FIRST_NAME_FIELD = By.xpath("//div//input[@name='thn__FirstName__c']");
    By SEND_TO_MEWS_CHECKBOX = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//lightning-input//span[text()='Send to Mews']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By HELP_ERROR_MESSAGE = By.xpath("//div[@data-help-message]");
    By CLOSE_WINDOW_BUTTON = By.
            xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");

    @Step("Open Guests page")
    public Guests goToGuests() throws InterruptedException {
        driver.navigate().to("https://thynk-test-unlocked-dev-ed.lightning.force.com/lightning/o/thn__Guest__c/list?filterName=Recent");
        try{if(wait2.until(ExpectedConditions.alertIsPresent())!=null){
            Alert alert = wait2.until(alertIsPresent());
            alert.accept();
        }}catch (TimeoutException e){
        }
        return this;
    }

    @Step
    public void clickNew(){
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_GUEST)).click();
    }


    @Step("Fill out the Guest form where Send_to_Mews__c == true")
    public void createGuest(String name) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(FIRST_NAME_FIELD));
        writeText(FIRST_NAME_FIELD, (name));
        wait1.until(ExpectedConditions.presenceOfElementLocated(SEND_TO_MEWS_CHECKBOX)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Read help error message")
    public String readHelpErrorMessage() throws InterruptedException {
        return readRecalculateMessage(HELP_ERROR_MESSAGE);
    }

    @Step("Close window")
    public void closeWindow(){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create Guest SFDX")
    public String createGuestSFDX(String sfdxPath, String value, String userName) throws IOException, InterruptedException {
        StringBuilder guestResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Guest__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Guest create result:");
        System.out.println(guestResult);
        String guestID = JsonParser2.getFieldValue(guestResult.toString(), "id");
        return guestID;
    }

    @Step("Delete Guest SFDX")
    public void deleteGuestSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Guest__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

    @Step("Get Guest SFDX")
    public StringBuilder getGuestSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder guestRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Guest__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return guestRecord;
    }

    @Step("Update Guest SFDX")
    public void updateGuestSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder guestUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Guest__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(guestUpdateResult);
    }
}
