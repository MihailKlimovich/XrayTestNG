package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class Packages extends BasePage {

    /**Constructor*/
    public Packages(WebDriver driver) {
        super(driver);
    }

    By NEW_PACKAGE = By.xpath("//span[text()='Packages']/following::div[@title='New']");
    By NAME_FIELD = By.xpath("//div//input[@name='Name']");
    By PROPERTY_FIELD = By.xpath("//label[text()='Property']/following-sibling::div//input");
    By PROPERTY_TYPE_DEMO = By.xpath("//div//lightning-base-combobox-formatted-text[@title='Demo']");
    By PROPERTY_TYPE_TEST = By.xpath("//div//lightning-base-combobox-formatted-text[@title='Test']");
    By MULTI_DAYS_RADIO_BUTTON = By.xpath("//div//span[text()='Multi Days']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");



    @Step("Open Packages tab...")
    public Packages goToPackages() throws InterruptedException {
        driver.navigate().to("https://connect-java-4747-dev-ed.lightning.force.com/lightning/o/thn__Package__c/list?filterName=Recent");
        return this;
    }

    @Step("Click new Package")
    public void clickNewPackage(WebDriver driver) throws InterruptedException {
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(NEW_PACKAGE));
        click2(NEW_PACKAGE);
    }

    @Step("Fill out the package form where Multi_Days__c == true")
    public void createPackage_happyPath(
            String name,
            String property
    ) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_FIELD));
        writeText(NAME_FIELD, (name));
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, property);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(PROPERTY_TYPE_DEMO)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(MULTI_DAYS_RADIO_BUTTON)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the package form where Multi_Days__c == false")
    public void createPackage_happyPath2(
            String name,
            String property
    ) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_FIELD));
        writeText(NAME_FIELD, (name));
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, property);
        click2(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(PROPERTY_TYPE_DEMO)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the package form where Multi_Days__c == true")
    public void createPackage_happyPath3(
            String name,
            String property
    ) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_FIELD));
        writeText(NAME_FIELD, (name));
        wait1.until(ExpectedConditions.presenceOfElementLocated(PROPERTY_FIELD)).click();
        writeText(PROPERTY_FIELD, property);
        click2(PROPERTY_FIELD);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(PROPERTY_TYPE_TEST)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }


}
