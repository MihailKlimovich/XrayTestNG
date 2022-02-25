package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.util.Set;

public class CheckAvailabilitiesComponent extends BasePage {

    /**Constructor*/
    public CheckAvailabilitiesComponent(WebDriver driver){
        super(driver);
    }

    By PLUS_BUTTON = By.xpath("//label[text()='Demo']/parent::c-check-availabilities-titles/parent::div/" +
            "descendant::lightning-button//button[text()='+']");
    By PROPERTY_FIELD = By.xpath("//div//label[text()='Property:']/following::input");

    @Step("Change resource")
    public String checkActiveRoomType(String property, String roomType) throws InterruptedException {

        String originalWindow = driver.getWindowHandle();
        final Set<String> oldWindowsSet = driver.getWindowHandles();

        String newWindow = wait1.until(new ExpectedCondition<String>() {
                           public String apply(WebDriver driver) {
                               Set<String> newWindowsSet = driver.getWindowHandles();
                               newWindowsSet.removeAll(oldWindowsSet);
                               return newWindowsSet.size() > 0 ?
                                       newWindowsSet.iterator().next() : null;
                           }
                       }
                );

        driver.switchTo().window(newWindow);

        click3(PROPERTY_FIELD);
        writeText(PROPERTY_FIELD, property);
        click3(By.xpath("//c-check-availabilities-titles//label[text()='" + property + "']"));
        click3(PLUS_BUTTON);
        try {
            if (wait2.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title='" + roomType + "']"))) != null){
                String message = "Room Type is present";
                return message;
            }
        }catch (TimeoutException e) {
        }
        String message = "Room Type is not present";
        return message;
    }

}
