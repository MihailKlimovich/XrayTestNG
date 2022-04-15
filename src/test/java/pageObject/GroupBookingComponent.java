package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class GroupBookingComponent extends BasePage {

    /**Constructor*/
    public GroupBookingComponent(WebDriver driver) {
        super(driver);
    }

    By SAVE_BUTTON = By.xpath("//div[@thn-groupbooking_groupbooking]//button[text()='Save']");


    @Step("Change the price per day")
    public void changePricePerDay(String dayNumber, String rate) throws InterruptedException {
        click3(By.xpath("(//c-day-price-table//lightning-input[@data-field='thn__Unit_Price_incl_Tax__c']//div//input)[" + dayNumber + "]"));
        clear(By.xpath("(//c-day-price-table//lightning-input[@data-field='thn__Unit_Price_incl_Tax__c']//div//input)[" + dayNumber + "]"));
        writeText(By.xpath("(//c-day-price-table//lightning-input[@data-field='thn__Unit_Price_incl_Tax__c']//div//input)[" + dayNumber + "]"), rate);
    }

    @Step("Click Save button")
    public void clickSaveButton() throws InterruptedException {
        click4(SAVE_BUTTON);
    }


}
