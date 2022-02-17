package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class CreateResourceForm extends BasePage {

    /**Constructor*/
    public CreateResourceForm(WebDriver driver){
        super(driver);
    }

    By RESOURCE_FIELD = By.xpath("//div//label[text()='Resource']/following-sibling::div//input");
    By FUNCTION_FIELD = By.xpath("//div//label[text()='Function']/following-sibling::div//button");
    By SETUP_FIELD = By.xpath("//div//label[text()='Setup']/following-sibling::div//button");
    By PAX_FIELD = By.xpath("//slot//label[text()='Pax']/following-sibling::div//input");
    By UNIT_PRICE_FIELD = By.xpath("//slot//label[text()='Unit Price']/following-sibling::div//input");
    By DESCRIPTION_FIELD = By.xpath("//label[text()='Description']/following-sibling::div//textarea");
    By DESCRIPTION_LANGUAGE_2_FIELD = By.
            xpath("//label[text()='Description Language 2']/following-sibling::div//textarea");
    By DESCRIPTION_LANGUAGE_3_FIELD = By.
            xpath("//label[text()='Description Language 3']/following-sibling::div//textarea");
    By DESCRIPTION_FS_FIELD = By.xpath("//label[text()='Description FS']/following-sibling::div//textarea");
    By SAVE_BUTTON = By.xpath("//button[text()='Save']");

    @Step("Create Resource")
    public void createResource(String resource, String function, String setup, String pax, String unitPrice,
                               String description, String desLan2, String desLan3, String desFs)
            throws InterruptedException {
        click3(RESOURCE_FIELD);
        click3(By.xpath("//li[@data-name='" + resource + "']"));
        click3(FUNCTION_FIELD);
        click3(By.xpath("//span[@title='" + function + "']"));
        click3(SETUP_FIELD);
        click3(By.xpath("//lightning-base-combobox-item[@data-value='" + setup + "']"));
        click3(PAX_FIELD);
        writeText(PAX_FIELD, pax);
        click3(UNIT_PRICE_FIELD);
        writeText(UNIT_PRICE_FIELD, unitPrice);
        click3(DESCRIPTION_FIELD);
        writeText(DESCRIPTION_FIELD, description);
        click3(DESCRIPTION_LANGUAGE_2_FIELD);
        writeText(DESCRIPTION_LANGUAGE_2_FIELD, desLan2);
        click3(DESCRIPTION_LANGUAGE_3_FIELD);
        writeText(DESCRIPTION_LANGUAGE_3_FIELD, desLan3);
        click3(DESCRIPTION_FS_FIELD);
        writeText(DESCRIPTION_FS_FIELD, desFs);
        click3(SAVE_BUTTON);
        Thread.sleep(3000);
    }

}
