package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.io.IOException;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

public class ResourceGrouping extends BasePage {


    /**Constructor*/
    public ResourceGrouping(WebDriver driver) {
        super(driver);
    }

    By NEW_RESOURCE_GROUPING_BUTTON = By.xpath("//span[text()='Resource Groupings']/following::div[@title='New']");
    By GROUPED_RESOURCE_FIELD = By.xpath("//label[text()='Grouped Resource']/following-sibling::div//input");
    By RESOURCE_GROUP_FIELD = By.xpath("//label[text()='Resource Group']/following-sibling::div//input");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By MESSAGE_ERROR_TEXT = By.xpath("//div[@class='container']//li");
    By CLOSE_WINDOW_BUTTON = By.
            xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");


    @Step("Open Resource Grouping tab...")
    public ResourceGrouping goToResourceGrouping() throws InterruptedException {
        driver.navigate().to("https://agility-efficiency-64-dev-ed.lightning.force.com/lightning/o/thn__Resource_Grouping__c/list?filterName=Recent");
        try{if(wait2.until(ExpectedConditions.alertIsPresent())!=null){
            Alert alert = wait2.until(alertIsPresent());
            alert.accept();
        }}catch (TimeoutException e){
        }
        return this;
    }

    @Step
    public void clickNew(){
        wait1.until(ExpectedConditions.elementToBeClickable(NEW_RESOURCE_GROUPING_BUTTON));
        clickInvisibleElement(NEW_RESOURCE_GROUPING_BUTTON);
    }

    @Step
    public void createResourceGrouping(String resource1, String resource2) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(GROUPED_RESOURCE_FIELD));
        click3(GROUPED_RESOURCE_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ resource1 +"']")));
        click3(By.xpath("//span[@title='"+ resource1 +"']"));
        wait1.until(ExpectedConditions.presenceOfElementLocated(RESOURCE_GROUP_FIELD));
        click3(RESOURCE_GROUP_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.
                xpath("//lightning-grouped-combobox//label[text()='Resource Group']/following::span[@title='" + resource2 + "']")));
        click3(By.xpath("//lightning-grouped-combobox//label[text()='Resource Group']/following::span[@title='" + resource2 + "']"));
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Close window")
    public void closeWindow(){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    @Step("Read error message 2")
    public String readErrorMessage2() throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////
    @Step("Create Resource Grouping SFDX")
    public String createResourceGroupingSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder resourceGroupingResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Resource_Grouping__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Quote create result:");
        System.out.println(resourceGroupingResult);
        String resourceID = JsonParser2.getFieldValue(resourceGroupingResult.toString(), "id");
        return resourceID;
    }
}
