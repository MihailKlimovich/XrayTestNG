package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

public class Items extends BasePage {

    /**Constructor*/
    public Items(WebDriver driver) {
        super(driver);
    }

    By NEW_ITEM_BUTTON = By.xpath("//span[text()='Items']/following::div[@title='New']");
    By RESERVATION_FIELD = By.xpath("//div//input[@placeholder='Search Reservations...']");
    By RESERVATION_TYPE = By.xpath("//label[text()='Reservation']/following-sibling::div//span[@title='Demo']");
    By PRODUCT_FIELD = By.xpath("//slot//label[text()='Product']/following-sibling::div//input");
    By MEWS_ID_FIELD = By.xpath("//slot//label[text()='Mews Id']/following-sibling::div//input");
    By SEND_TO_MEWS_CHECKBOX = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//lightning-input//span[text()='Send to Mews']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By CLOSE_WINDOW_BUTTON = By.
            xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");
    By HELP_ERROR_MESSAGE = By.xpath("//div[@data-help-message]");

    @Step("Open Items page")
    public Items goToItems() {
        try {
            URL baseUrl = new URL(driver.getCurrentUrl());
            String url = "https://" + baseUrl.getAuthority() + "/lightning/o/thn__Item__c/list?filterName=Recent";
            System.out.println(url);
            driver.navigate().to(url);
        } catch (Exception ex){ex.printStackTrace();
        }
        try{if(wait2.until(ExpectedConditions.alertIsPresent())!=null){
            Alert alert = wait2.until(alertIsPresent());
            alert.accept();
        }}catch (TimeoutException e){
        }
        return this;
    }

    @Step
    public void clickNew(){
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_ITEM_BUTTON)).click();
    }

    @Step("Fill out the Item form where Send_to_Mews__c == true")
    public void createItem(String product, String mewsId) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(RESERVATION_FIELD)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(RESERVATION_TYPE));
        clickInvisibleElement(RESERVATION_TYPE);
        //click2(RESERVATION_TYPE);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD)).click();
        writeText(PRODUCT_FIELD, product);
        Thread.sleep(2000);
        down();
        enter();
        wait1.until(ExpectedConditions.presenceOfElementLocated(SEND_TO_MEWS_CHECKBOX)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(MEWS_ID_FIELD)).click();
        writeText(MEWS_ID_FIELD, mewsId );
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Close window")
    public void closeWindow(){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    @Step("Read help error message")
    public String readHelpErrorMessage() throws InterruptedException {
        return readRecalculateMessage(HELP_ERROR_MESSAGE);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Delete Item SFDX")
    public void deleteItemSFDX(String sfdxPath, String where, String userName) throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Item__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }
}
