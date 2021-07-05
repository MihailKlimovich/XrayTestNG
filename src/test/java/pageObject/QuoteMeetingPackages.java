package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class QuoteMeetingPackages extends BasePage {

    /**Constructor*/
    public QuoteMeetingPackages(WebDriver driver) {
        super(driver);
    }

    By NEW_MEETING_PACKAGES = By.xpath("//h1[text()='Meeting Packages']/following::div[@title='New']");
    By PACKAGE_FIELD = By.xpath("//slot//label[text()='Package']/following-sibling::div//input");
    By PACKAGE_TYPE = By.xpath("//span[@title='//span[@title='Package 1']']");
    By START_DATE_FIELD = By.xpath("//slot//label[text()='Start Date']/following-sibling::div//input");
    By END_DATE_FIELD = By.xpath("//slot//label[text()='End Date']/following-sibling::div//input");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");


    @Step("Fill out the meeting room")
    public void createMeetingPackages(String pack, String startDate, String endDate) throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_MEETING_PACKAGES)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PACKAGE_FIELD));
        click2(PACKAGE_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='" + pack + "']"))).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(START_DATE_FIELD)).click();
        writeText(START_DATE_FIELD, startDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(END_DATE_FIELD)).click();
        writeText(END_DATE_FIELD, endDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
        Thread.sleep(5000);
    }

}
