package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class ChangeResource extends BasePage {

    /**Constructor*/
    public ChangeResource(WebDriver driver){
        super(driver);
    }

    By NEW_RESOURCE_FIELD = By.xpath("//label[text()='Resource']/following::input[@type='search']");
    By REMOVE_BUTTON = By.xpath("//label[text()='Resource']/following::button[@title='Remove']");
    By NEXT_BUTTON = By.xpath("//button[@title='Next']");


    @Step("Change resource")
    public void changeResource(String newResource) throws InterruptedException {
        //wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[@title='accessibility title']")));
        //wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
        driver.switchTo().frame(0);
        click3(REMOVE_BUTTON);
        click3(NEW_RESOURCE_FIELD);
        writeText(NEW_RESOURCE_FIELD, newResource);
        click3(By.xpath("//li[@data-name='" + newResource + "']"));
        click4(NEXT_BUTTON);
        Thread.sleep(2000);
        tab();
        enter();
        driver.switchTo().defaultContent();
    }

}
