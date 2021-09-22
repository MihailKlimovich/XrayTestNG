package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;

public class HomePageForScratchOrg extends BasePage {

    private Object WebDriverWait;


    /**Constructor*/
    public HomePageForScratchOrg(WebDriver driver) {
        super(driver);
    }

    /**Variables*/
    By homePage = By.xpath("//a[href='/lightning/page/home']");
    By investmentPage = By.xpath("//a[@href='/lightning/o/sfims__Investment__c/home']");
    By fundPage = By.xpath("(//a[@title='Funds'])[1]");
    By applicationPage = By.xpath("(//a[@title='Applications'])[1]");
    By imsSettingsPage = By.xpath("(//a[@title='IMS Settings'])[1]");
    By showMoreAction = By.xpath("//a/span[text()='More']");
    By imsSettingDropDown = By.xpath("//a/span[text()='IMS Settings']");
    private static final By APP_LAUNCHER_BUTTON = By.
            xpath("//div[@class='appLauncher slds-context-bar__icon-action']//button");
    private static final By SEARCH_APP_WINDOW = By.
            xpath("//div[@class='container']//input[@class='slds-input']");
    By MYCE_QUOTE_TAB = By.xpath("//a[@title='MYCE Quotes']");

    /**Page Methods*/
    @Step("Open Home Page...")
    public HomePageForScratchOrg goToHomePage() {
        clickInvisibleElement(homePage);
        return this;
    }



    @Step("Open Investment tab...")
    public HomePageForScratchOrg goToInvestmentPage() {
        clickInvisibleElement(investmentPage);
        return this;
    }

    @Step("Open Fund tab...")
    public HomePageForScratchOrg goToFundPage() throws InterruptedException {
        click(fundPage);
        return this;
    }

    @Step("Open Application tab...")
    public HomePageForScratchOrg goToApplicationPage() throws InterruptedException {
        click(applicationPage);
        return this;
    }

    @Step("Open IMS Settings tab...")
    public HomePageForScratchOrg goToIMSSettingsPage() throws InterruptedException {
        try{
            clickInvisibleElement(showMoreAction);
            Thread.sleep(2000);
            clickInvisibleElement(imsSettingDropDown);
            Thread.sleep(2000);
        }
        catch (Exception e){
            click(imsSettingsPage);
        }
        return this;
    }

    @Step("OpenAppLauncher")
    public void openAppLauncher() throws InterruptedException {
        WebDriverWait = new WebDriverWait(driver, 30);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(APP_LAUNCHER_BUTTON));
        click3(APP_LAUNCHER_BUTTON);



    }

    @Step("SendText")
    public void  sendTextInAppWindow(String text) throws InterruptedException {
        WebDriverWait = new WebDriverWait(driver, 30);
        WebElement searchAppWindow = wait1.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_APP_WINDOW));
        searchAppWindow.findElement(By.xpath("//input[@placeholder='Search apps and items...']")).sendKeys(text);
        Thread.sleep(3000);
        enter();


    }

}
