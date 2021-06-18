package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import pages.HomePageForScratchOrg;

public class AccountsPage extends BasePage {


    private Object WebDriverWait;

    /**Constructor*/

    public AccountsPage(WebDriver driver) {
        super(driver);
    }

    private static final By ACCOUNT_NAME_SELECTOR = By.
            xpath("//span[@class='slds-grid slds-grid--align-spread forceInlineEditCell']");
    private static final By ACCOUNT_SELECTOR = By.
            xpath("//span[@class='slds-grid slds-grid--align-spread forceInlineEditCell']//a[@href]");
    private static final By DETAILS_SELECTOR = By.
            xpath("//ul[@role='tablist']//a[@id='detailTab__item']");
    private static final By EDIT_INDUSTRY = By.
            xpath("//button[@title='Edit Industry']");
    By INDUSTRY_INPUT = By.xpath("//label[text()='Industry']/following-sibling::div//div");
    By FOOD_AND_BEVERAGE = By.xpath("//div//span[@title='Food & Beverage']");
    By SAVE_BUTTON = By.xpath("//button[@name='SaveEdit']");
    private static final By INDUSTRY_NAME_SELECTOR = By.
            xpath("//span[text()='Industry']//following::slot//slot");



    @Step("Get Account Name")
    public String getAccountName(WebDriver driver) {
        WebDriverWait = new WebDriverWait(driver, 30);
        WebElement account_name = wait1.until(ExpectedConditions.presenceOfElementLocated(ACCOUNT_NAME_SELECTOR));
        return account_name.getText();
    }

    @Step("Change Industry")
    public void changeIndustry() throws InterruptedException {
        click(ACCOUNT_SELECTOR);
        click(DETAILS_SELECTOR);
        click(EDIT_INDUSTRY);
        click(INDUSTRY_INPUT);
        click(FOOD_AND_BEVERAGE);
        click(SAVE_BUTTON);
    }

    @Step("Get Account Name")
    public String getIndustryName(WebDriver driver) {
        WebDriverWait = new WebDriverWait(driver, 30);
        WebElement industry_name = wait1.until(ExpectedConditions.presenceOfElementLocated(INDUSTRY_NAME_SELECTOR));
        return industry_name.getText();

    }








}
