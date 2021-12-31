package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.io.IOException;

public class PackageLine extends BasePage {

    public PackageLine(WebDriver driver) {
        super(driver);
    }

    By NAME_FIELD = By.xpath("//div//input[@name='Name']");
    By TYPE_FIELD = By.xpath("//span//label[text()='Type']");
    By TYPE_HOTEL_ROOM = By.xpath("//span[@title='Hotel Room']");
    By PRODUCT_FIELD = By.xpath("//span//label[text()='Product']");
    By PRODUCT_TYPE = By.xpath("//span[@title='ROOM 1 NIGHT']");
    By START_TIME_FIELD = By.xpath("//label[text()='Start Time']/following-sibling::div//input");
    By END_TIME_FIELD = By.xpath("//label[text()='End Time']/following-sibling::div//input");
    By UNIT_PRICE_FIELD = By.xpath("//span//label[text()='Unit Price']/following-sibling::div//input");
    By VAT_CATEGORY_FIELD = By.xpath("//span//label[text()='VAT Category']");
    By VAT_CATEGORY_TYPE = By.xpath("//div//span[@title='1']");
    By SAVE_BUTTON = By.xpath("//div[@class='isModal inlinePanel oneRecordActionWrapper']//button[@name='SaveEdit']");
    By NEW_PACKAGE_LINE_BUTTON = By.xpath("//span[text()='Package Lines']/following::button[@name='New']");
    By MESSAGE_ERROR_TEXT = By.xpath("//div[@class='container']//li");
    By CLOSE_WINDOW_BUTTON = By.
            xpath("//div[@class='modal-container slds-modal__container']//button[@title='Close this window']");
    By APPLIED_DAY_FIELD = By.xpath("//label[text()='Applied Day']/following-sibling::div//input");
    By APPLY_DISCOUNT_RADIO_BUTTON = By.xpath("//force-record-layout-section//span[text()='Apply Discount']");
    By HELP_ERROR_MESSAGE = By.xpath("//div[@data-help-message]");



    @Step("Click new Package Line")
    public void clickNewPackageLine() throws InterruptedException {
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(NEW_PACKAGE_LINE_BUTTON));
        click2(NEW_PACKAGE_LINE_BUTTON);
    }

    @Step("Fill out the package line form where AppliedDay==null")
    public void createPackageLine_whereAppliedDateIsEmpty(
            String name,
            String start,
            String end,
            String unitPrice
    ) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_FIELD));
        writeText(NAME_FIELD, (name));
        wait1.until(ExpectedConditions.presenceOfElementLocated(TYPE_FIELD));
        click3(TYPE_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(TYPE_HOTEL_ROOM));
        click3(TYPE_HOTEL_ROOM);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD));
        click3(PRODUCT_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_TYPE));
        click3(PRODUCT_TYPE);
        wait1.until(ExpectedConditions.presenceOfElementLocated(START_TIME_FIELD)).click();
        writeText(START_TIME_FIELD, start);
        wait1.until(ExpectedConditions.presenceOfElementLocated(END_TIME_FIELD)).click();
        writeText(END_TIME_FIELD, end);
        wait1.until(ExpectedConditions.presenceOfElementLocated(UNIT_PRICE_FIELD));
        click2(UNIT_PRICE_FIELD);
        writeText(UNIT_PRICE_FIELD, unitPrice);
        wait1.until(ExpectedConditions.presenceOfElementLocated(VAT_CATEGORY_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(VAT_CATEGORY_TYPE)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the package line form where AppliedDay is not null")
    public void createPackageLine_whereAppliedDateIsNotEmpty(
            String name,
            String start,
            String end,
            String appliedDay,
            String unitPrice
    ) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_FIELD));
        writeText(NAME_FIELD, (name));
        wait1.until(ExpectedConditions.presenceOfElementLocated(TYPE_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(TYPE_HOTEL_ROOM));
        click2(TYPE_HOTEL_ROOM);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD));
        click2(PRODUCT_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_TYPE));
        click2(PRODUCT_TYPE);
        wait1.until(ExpectedConditions.presenceOfElementLocated(APPLIED_DAY_FIELD)).click();
        writeText(APPLIED_DAY_FIELD, appliedDay);
        wait1.until(ExpectedConditions.presenceOfElementLocated(START_TIME_FIELD)).click();
        writeText(START_TIME_FIELD, start);
        wait1.until(ExpectedConditions.presenceOfElementLocated(END_TIME_FIELD)).click();
        writeText(END_TIME_FIELD, end);
        wait1.until(ExpectedConditions.presenceOfElementLocated(UNIT_PRICE_FIELD));
        click2(UNIT_PRICE_FIELD);
        writeText(UNIT_PRICE_FIELD, unitPrice);
        wait1.until(ExpectedConditions.presenceOfElementLocated(VAT_CATEGORY_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(VAT_CATEGORY_TYPE)).click();
        wait1.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }

    @Step("Fill out the package line form where thn__Apply_Discount__c == TRUE")
    public void createPackageLine_applyDiscountIsTrue(
            String name,
            String type,
            String product,
            String start,
            String end,
            String unitPrice
    ) throws InterruptedException {
        wait1.until(ExpectedConditions.presenceOfElementLocated(NAME_FIELD));
        writeText(NAME_FIELD, (name));
        wait1.until(ExpectedConditions.presenceOfElementLocated(TYPE_FIELD));
        click3(TYPE_FIELD);
        wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='"+ type + "']"))).click();
        Thread.sleep(2000);
        wait1.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_FIELD)).click();
        click3(PRODUCT_FIELD);
        //clickInvisibleElement(By.xpath("//span[@title='" + product + "']"));
        //Thread.sleep(2000);
        waitForTests.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='" + product + "']")));
        //wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='" + product + "']")));
        click3(By.xpath("//span[@title='" + product + "']"));
        wait1.until(ExpectedConditions.presenceOfElementLocated(APPLY_DISCOUNT_RADIO_BUTTON)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(START_TIME_FIELD)).click();
        writeText(START_TIME_FIELD, start);
        wait1.until(ExpectedConditions.presenceOfElementLocated(END_TIME_FIELD)).click();
        writeText(END_TIME_FIELD, end);
        wait1.until(ExpectedConditions.presenceOfElementLocated(UNIT_PRICE_FIELD));
        click2(UNIT_PRICE_FIELD);
        writeText(UNIT_PRICE_FIELD, unitPrice);
        wait1.until(ExpectedConditions.presenceOfElementLocated(VAT_CATEGORY_FIELD)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(VAT_CATEGORY_TYPE)).click();
        wait1.until(ExpectedConditions.presenceOfElementLocated(SAVE_BUTTON)).click();
    }

    @Step("Read error message 2")
    public String readErrorMessage2() throws InterruptedException {
        return readRecalculateMessage(MESSAGE_ERROR_TEXT);
    }

    @Step("Close window")
    public void closeWindow(){
        wait1.until(ExpectedConditions.elementToBeClickable(CLOSE_WINDOW_BUTTON)).click();
    }

    @Step("Read help error message")
    public String readHelpErrorMessage() throws InterruptedException {
        return readRecalculateMessage(HELP_ERROR_MESSAGE);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Create Package Line")
    public String createPackageLineSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder packageResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Package_Line__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        String packageLineId = JsonParser2.getFieldValue(packageResult.toString(), "id");
        return packageLineId;
    }

    @Step("Get Package Line SFDX")
    public StringBuilder getPackageLineSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder packageLineRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Package_Line__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return packageLineRecord;
    }

    @Step("Update Package Line SFDX")
    public void updatePackageLineSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder packageLineUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Package_Line__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(packageLineUpdateResult);
    }

    @Step("Delete Package Line SFDX")
    public void deletePackageLineSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Package_Line__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

}
