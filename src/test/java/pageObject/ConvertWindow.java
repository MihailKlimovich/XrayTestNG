package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class ConvertWindow extends BasePage {

    /**Constructor*/
    public ConvertWindow(WebDriver driver){
        super(driver);
    }


    By MOVE_SELECTION_TO_CHOSEN_BUTTON =By.xpath("//div[@id='wrapper-body']//button[@title='Move selection to Chosen']");
    By FROM_DATE_FIELD= By.xpath("//label//span[text()='From Date']/following::div//input");
    By TO_DATE_FIELD= By.xpath("//label//span[text()='To Date']/following::div//input");
    By PAX_FIELD= By.xpath("//label//span[text()='Pax']/following::input");
    By SAVE_BUTTON= By.xpath("//div[@id='wrapper-body']//footer//button//span[text()='Save']");





    @Step("Fill form")
    public void fillConvertForm(String property, String pax, String fromDate, String toDate ) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='wrapper-body']//span[@title='" + property + "']")));
        click(By.xpath("//div[@id='wrapper-body']//span[@title='" + property + "']"));
        wait1.until(ExpectedConditions.presenceOfElementLocated(MOVE_SELECTION_TO_CHOSEN_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(FROM_DATE_FIELD)).click();
        writeText(FROM_DATE_FIELD, fromDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(TO_DATE_FIELD)).click();
        writeText(TO_DATE_FIELD, toDate);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PAX_FIELD)).click();
        writeText(PAX_FIELD, pax);
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }

}
