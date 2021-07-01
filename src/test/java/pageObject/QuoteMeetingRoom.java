package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class QuoteMeetingRoom extends BasePage {

    /**Constructor*/
    public QuoteMeetingRoom(WebDriver driver) {
        super(driver);
    }

    By NEW_MEETING_ROOM = By.xpath("//span[text()='Meeting Rooms']/following::div[@title='New']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By PRODUCT_FIELD = By.xpath("//slot//label[text()='Product']/following-sibling::div//input");
    By PRODUCT_TYPE_ROOM1NIGHT = By.xpath("//span[@title='MEETING HALF DAY']");
    By SETUP_FIELD = By.xpath("//slot//label[text()='Setup']/following-sibling::div//input");
    By SETUP_TYPE_BUFFET = By.xpath("//span[@title='Buffet']");
    By SETUP_TYPE_CABARET = By.xpath("//span[@title='Cabaret']");
    By SETUP_TYPE_CIRCLE = By.xpath("//span[@title='Circle']");
    By SETUP_TYPE_CLASSROOM = By.xpath("//span[@title='Classroom']");
    By SETUP_TYPE_CUSTOM = By.xpath("//span[@title='Custom']");
    By SETUP_TYPE_DINNER = By.xpath("//span[@title='Dinner']");
    By SETUP_TYPE_PARTY = By.xpath("//span[@title='Party']");
    By SETUP_TYPE_SQUARE = By.xpath("//span[@title='Square']");
    By SETUP_TYPE_THEATER = By.xpath("//span[@title='Theater']");
    By SETUP_TYPE_USHAPE = By.xpath("//span[@title='U-Shape']");
    By RESOURCE_FIELD = By.xpath("//slot//label[text()='Resource']/following-sibling::div//input");
    By RESOURCE_TYPE_TESTRES = By.xpath("//span[@title='TestRes']");

    @Step("Fill out the meeting room")
    public void createMeetingRoom() throws InterruptedException {
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_MEETING_ROOM)).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_TYPE_ROOM1NIGHT)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(RESOURCE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(RESOURCE_TYPE_TESTRES)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(SETUP_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(SETUP_TYPE_BUFFET)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }






}
