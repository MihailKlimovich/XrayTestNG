package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

public class Mews extends BasePage {

    /**Constructor*/
    public Mews(WebDriver driver) {
        super(driver);
    }

    By USERNAME_FIELD = By.xpath("//input[@name='nameOrEmail']");
    By PASSWORD_FIELD = By.xpath("//input[@name='password']");
    By SIGN_IN_BUTTON = By.xpath("//button//span[text()='Sign in']");
    By SEARCH_FIELD = By.xpath("//div[text()='Search within Thynk Hotel Paris']/following-sibling::input");
    By AVG_RATE = By.xpath("//span[text()='Avg. rate (nightly)']/parent::dt/following-sibling::dd[1]//strong//span");
    By TOTAL_AMOUNT = By.xpath("//span[text()='Total amount']/parent::dt/following-sibling::dd[1]//strong//span");
    By RELEASE = By.xpath("//span[text()='Release']/parent::dt/following-sibling::dd[1]//span");
    By DEPARTURE = By.xpath("//span[text()='Departure']/parent::dt/following-sibling::dd[1]//span");
    By STATUS = By.xpath("//span[text()='Status']/parent::dt/following-sibling::dd[1]//span");

    @Step("Log in to Mews")
    public Mews logIn(String login, String password) throws InterruptedException {
        driver.navigate().to("https://app.mews-demo.com/Commander/Home/SignIn");
        click3(USERNAME_FIELD);
        writeText(USERNAME_FIELD, login);
        click3(PASSWORD_FIELD);
        writeText(PASSWORD_FIELD, password);
        click3(SIGN_IN_BUTTON);
        return this;
    }

    @Step("Find record by ID")
    public void findRecordByID(String id, String reservationName) throws InterruptedException {
        click3(SEARCH_FIELD);
        writeText(SEARCH_FIELD, id);
        click3(By.xpath("//div//span[text()='" + reservationName + "']"));
    }

    @Step("Go to main page")
    public Mews goToMainPage() throws InterruptedException {
        driver.navigate().to("https://app.mews-demo.com/Commander/Dashboard/Index");
        return this;
    }

    @Step("Read AVG Rate")
    public String readAvgRate() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(AVG_RATE));
        String rate = readText(AVG_RATE);
        return rate;
    }

    @Step("Read Total Amount")
    public String readTotalAmount() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(TOTAL_AMOUNT));
        String amount = readText(TOTAL_AMOUNT);
        return amount;
    }

    @Step("Read release")
    public String readRelease() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(RELEASE));
        String releaseDate = readText(RELEASE);
        return releaseDate;
    }

    @Step("Read departure")
    public String readDeparture() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(DEPARTURE));
        String departureDate = readText(DEPARTURE);
        return departureDate;
    }

    @Step("Read status")
    public String readStatus() throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(STATUS));
        String status = readText(STATUS);
        return status;
    }

    @Step("Read nights rate")
    public String readNightsRate(String night) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.
                xpath("(//span[text()='Nights']/parent::dt/following-sibling::dd[1]//span/following-sibling::span)[" + night + "]")));
        String rate = readText(By.
                xpath("(//span[text()='Nights']/parent::dt/following-sibling::dd[1]//span/following-sibling::span)[" + night + "]"));
        return rate;
    }





}
