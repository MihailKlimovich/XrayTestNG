package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPageForScratchOrg {

    private static final String USERNAME = "test-6epicstbdzhd@example.com";
    private static final String PASSWORD = "vt*P7[VrGp&]P";
    private static final String URL_LOGIN_PAGE = "https://test.salesforce.com/";
    private static final By USER_NAME_SELECTOR = By.
            xpath("//div[@id='theloginform']//input[@class='input r4 wide mb16 mt8 username']");
    private static final By PASSWORD_SELECTOR = By.
            xpath("//div[@id='theloginform']//input[@class='input r4 wide mb16 mt8 password']");
    private static final By LOGIN_BUTTON_SELECTOR = By.
            xpath("//div[@id='theloginform']//input[@class='button r4 wide primary']");
    private static final By IGNORE_PHONE_SELECTOR = By.xpath("");



    public void open(WebDriver driver) {
        driver.get(URL_LOGIN_PAGE);
    }

    public void signUpUserName(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement userNameField = wait.until(ExpectedConditions.presenceOfElementLocated(USER_NAME_SELECTOR));
        userNameField.click();
        userNameField.sendKeys(USERNAME);
    }

    public void signUpPassword(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver,30 );
        WebElement passwordNameField = wait.until(ExpectedConditions.presenceOfElementLocated(PASSWORD_SELECTOR));
        passwordNameField.click();
        passwordNameField.sendKeys(PASSWORD);
    }

    public void clickLogIn(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement logInButton = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON_SELECTOR));
        logInButton.click();
    }

    public void logInOnScratchOrg(WebDriver driver){
        driver.get(URL_LOGIN_PAGE);
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement userNameField = wait.until(ExpectedConditions.presenceOfElementLocated(USER_NAME_SELECTOR));
        userNameField.click();
        userNameField.sendKeys(USERNAME);
        WebElement passwordNameField = wait.until(ExpectedConditions.presenceOfElementLocated(PASSWORD_SELECTOR));
        passwordNameField.click();
        passwordNameField.sendKeys(PASSWORD);
        WebElement logInButton = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON_SELECTOR));
        logInButton.click();
    }


}
