package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;

public class AccountPage extends BasePage {

    private Object WebDriverWait;

    /**Constructor*/
    public AccountPage(WebDriver driver){
        super(driver);
    }

    /**Variables*/

    //New Account button
    By newAccountButton = By.xpath("//span[text()='Accounts']/ancestor::div[@role='banner']/descendant::a[@title='New']");

    //New Account Next button
    By nextButton = By.xpath("(//h2[text()='New Account']/following::span[text()='Next'])[1]");

    //Account Name field
    By accountNameField = By.xpath("(//label/span[text()='Account Name']/following::input)[1]");

    //Save button
    By saveAccountButton = By.xpath("//button[@title='Save']");

    private static final By DETAILS_ACCOUNT_SELECTOR = By.
            xpath("//a[@id='detailTab__item']");


    /**Methods*/
    @Step("Create new Account")
    public void createAccount(String newAccountName) throws InterruptedException{
        Thread.sleep(2000);
        click(newAccountButton);
        click(nextButton);
        writeText(accountNameField, newAccountName);
        Thread.sleep(4000);
    }

    @Step("Click Enter Button")
    public void clickEnterButton(){
        enter();
    }

    @Step("FindNesAccount")
    public void findLastAccount(){

    }

    @Step("Open Details")
    public void openDetails(WebDriver driver){
        WebDriverWait = new WebDriverWait(driver, 30);
        WebElement details = wait1.until(ExpectedConditions.presenceOfElementLocated(DETAILS_ACCOUNT_SELECTOR));
        details.click();
    }







}
