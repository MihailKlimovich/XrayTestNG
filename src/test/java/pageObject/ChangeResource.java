package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

public class ChangeResource extends BasePage {

    /**Constructor*/
    public ChangeResource(WebDriver driver){
        super(driver);
    }

    By NEW_RESOURCE_FIELD = By.xpath("//label[text()='Resource']/following::input[@type='search']");
    By REMOVE_BUTTON = By.xpath("//body//label[text()='Resource']/following::button[@title='Remove']");
    By NEXT_BUTTON = By.xpath("//button[@class='slds-button slds-button_brand flow-button__NEXT']");
    By FINISH_BUTTON = By.xpath("//button[@class='slds-button slds-button_brand flow-button__FINISH']");
    By CONFIRMATION_MESSAGE = By.xpath("//flowruntime-display-text-lwc//span//p[text()='Do you wish to overbook?']");
    By COMLIMENTARY_MESSAGE = By.xpath("//p[text()='Cannot update prices because assigned MYCE Quote is complimentary.']");
    By YES_RADIO_BUTTON = By.xpath("//span//input[@value='Yes']");
    By NO_RADIO_BUTTON = By.xpath("//span//input[@value='No']");
    By START_DATE_FIELD = By.xpath("//div//label[text()='Date']/following::lightning-button-icon/parent::div//input[@name='Start_Date_Time']");
    By END_DATE_FIELD = By.xpath("//div//label[text()='Date']/following::lightning-button-icon/parent::div//input[@name='End_Date_Time']");
    By START_TIME_FIELD = By.xpath("//lightning-timepicker//input[@name='Start_Date_Time']");
    By END_TIME_FIELD = By.xpath("//lightning-timepicker//input[@name='End_Date_Time']");
    By UPDATE_PRICES_CHECKBOX = By.xpath("//input[@name='Update_Prices']");
    By BREAK_OUT_CHECKBOX = By.xpath("//input[@name='Break_Out']");
    By HALF_DAY_CHECKBOX = By.xpath("//input[@name='Half_Day']");
    By FRAME = By.xpath("//div//iframe");
    By FRAME2 = By.xpath("//div//iframe/following-sibling::force-form-auto-post//form");


    @Step("Change resource")
    public String changeResource(String newResource) throws InterruptedException {
        //waitForTests.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(FRAME));
        Thread.sleep(5000);
        driver.switchTo().frame(0);
        Thread.sleep(5000);
        wait1.until(ExpectedConditions.elementToBeClickable(REMOVE_BUTTON));
        click(REMOVE_BUTTON);
        click3(NEW_RESOURCE_FIELD);
        writeText(NEW_RESOURCE_FIELD, newResource);
        click3(By.xpath("//li[@data-name='" + newResource + "']"));
        click4(NEXT_BUTTON);
        try {
            if (wait2.until(ExpectedConditions.presenceOfElementLocated(CONFIRMATION_MESSAGE)) != null){
                String confirmationMessage = readText(CONFIRMATION_MESSAGE);
                click3(YES_RADIO_BUTTON);
                click4(NEXT_BUTTON);
                click4(FINISH_BUTTON);
                driver.switchTo().defaultContent();
                return confirmationMessage;
            }
        }catch (TimeoutException e) {

            click4(FINISH_BUTTON);
            driver.switchTo().defaultContent();
        }
        String message = "Message not found";
        return message;
    }

    @Step("Change resource and update price")
    public String changeResourceAndUpdatePrice(String newResource) throws InterruptedException {
        Thread.sleep(5000);
        driver.switchTo().frame(0);
        Thread.sleep(5000);
        click3(REMOVE_BUTTON);
        click3(NEW_RESOURCE_FIELD);
        writeText(NEW_RESOURCE_FIELD, newResource);
        click3(By.xpath("//li[@data-name='" + newResource + "']"));
        click3(UPDATE_PRICES_CHECKBOX);
        click3(BREAK_OUT_CHECKBOX);
        click3(HALF_DAY_CHECKBOX);
        click4(NEXT_BUTTON);
        try {
            if (wait2.until(ExpectedConditions.presenceOfElementLocated(CONFIRMATION_MESSAGE)) != null){
                String confirmationMessage = readText(CONFIRMATION_MESSAGE);
                click3(YES_RADIO_BUTTON);
                click4(NEXT_BUTTON);
                click4(FINISH_BUTTON);
                driver.switchTo().defaultContent();
                return confirmationMessage;
            }
        }catch (TimeoutException e) {

            click4(FINISH_BUTTON);
            driver.switchTo().defaultContent();
        }
        String message = "Message not found";
        return message;
    }

    @Step("Change resource and update price on complimentary Quote")
    public String changeResourceAndUpdatePrice_complimentary(String newResource) throws InterruptedException {
        Thread.sleep(5000);
        driver.switchTo().frame(0);
        Thread.sleep(5000);
        click3(REMOVE_BUTTON);
        click3(NEW_RESOURCE_FIELD);
        writeText(NEW_RESOURCE_FIELD, newResource);
        click3(By.xpath("//li[@data-name='" + newResource + "']"));
        click3(UPDATE_PRICES_CHECKBOX);
        click4(NEXT_BUTTON);
        try {
            if (wait2.until(ExpectedConditions.presenceOfElementLocated(COMLIMENTARY_MESSAGE)) != null){
                String confirmationMessage = readText(COMLIMENTARY_MESSAGE);
                click4(FINISH_BUTTON);
                driver.switchTo().defaultContent();
                return confirmationMessage;
            }
        }catch (TimeoutException e) {
            click4(FINISH_BUTTON);
            driver.switchTo().defaultContent();
        }
        String message = "Message not found";
        return message;
    }

    @Step("Change resource and start/end time")
    public String changeResourceAndDateTime(String newResource, String startDate, String startTime, String endDate,
                                            String endTime, String confirm) throws InterruptedException {
        Thread.sleep(5000);
        driver.switchTo().frame(0);
        Thread.sleep(5000);
        click3(REMOVE_BUTTON);
        click3(NEW_RESOURCE_FIELD);
        writeText(NEW_RESOURCE_FIELD, newResource);
        click3(By.xpath("//li[@data-name='" + newResource + "']"));
        click3(START_DATE_FIELD);
        writeText(START_DATE_FIELD, startDate);
        click3(START_TIME_FIELD);
        writeText(START_TIME_FIELD, startTime);
        click3(END_DATE_FIELD);
        writeText(END_DATE_FIELD, endDate);
        click3(END_TIME_FIELD);
        writeText(END_TIME_FIELD, endTime);
        click4(NEXT_BUTTON);
        try {
            if (wait2.until(ExpectedConditions.presenceOfElementLocated(CONFIRMATION_MESSAGE)) != null){
                String confirmationMessage = readText(CONFIRMATION_MESSAGE);
                click3(By.xpath("//span//input[@value='" + confirm + "']"));
                click4(NEXT_BUTTON);
                click4(FINISH_BUTTON);
                driver.switchTo().defaultContent();
                return confirmationMessage;
            }
        }catch (TimeoutException e) {

            click4(FINISH_BUTTON);
            driver.switchTo().defaultContent();
        }
        String message = "Message not found";
        return message;
    }



}
