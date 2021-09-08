package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class MultiEditProducts extends BasePage {

    /**Constructor*/
    public MultiEditProducts(WebDriver driver) {
        super(driver);
    }

    By NEW_MEETING_ROOM_FIELD = By.xpath("//label[text()='New Meeting Room']");
    By COMMISSIONABLE_YES_RADIO_BUTTON = By.xpath("//span[text()='Commissionable']/following::span[@class='slds-radio_faux'][1]");
    By HIDE_ON_OFFER_YES_RADIO_BUTTON = By.xpath("//span[text()='Hide on Offer']/following::span[@class='slds-radio_faux'][1]");
    By IS_ON_CONSUMPTION_YES_RADIO_BUTTON = By.xpath("//span[text()='Is on Consumption?']/following::span[@class='slds-radio_faux'][1]");
    By NEXT_BUTTON = By.xpath("//button[@title='Next']");
    By DO_YOU_WISH_RADIO_BUTTON = By.xpath("//p[text()='Do you wish to edit all selected items?']/following::span[@class='slds-radio_faux'][1]");
    By DEFAULT_MEETING_FULL_DAY = By.xpath("//div//li[@data-name='DEFAULT - MEETING FULL DAY']");


    @Step("Edit products")
    public  void editProducts(){
        //wait1.until(ExpectedConditions.presenceOfElementLocated(NEW_MEETING_ROOM_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(COMMISSIONABLE_YES_RADIO_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(HIDE_ON_OFFER_YES_RADIO_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(IS_ON_CONSUMPTION_YES_RADIO_BUTTON)).click();
        clickInvisibleElement(DEFAULT_MEETING_FULL_DAY);
        wait1.until(ExpectedConditions.presenceOfElementLocated(NEXT_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(DO_YOU_WISH_RADIO_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(NEXT_BUTTON)).click();
    }



}
