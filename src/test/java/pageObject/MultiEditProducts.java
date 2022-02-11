package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class MultiEditProducts extends BasePage {

    /**Constructor*/
    public MultiEditProducts(WebDriver driver) {
        super(driver);
    }

    By NEW_START_DATE_FIELD = By.xpath("//button[@title='Select a date']/parent::lightning-button-icon/parent::div//" +
            "input[@name='New_Start_Date_Time']");
    By NEW_END_DATE_FIELD = By.xpath("//button[@title='Select a date']/parent::lightning-button-icon/parent::div//" +
            "input[@name='New_End_Date_Time']");
    By NEW_START_TIME_FIELD = By.xpath("//lightning-timepicker//input[@name='New_Start_Date_Time']");
    By NEW_END_TIME_FIELD = By.xpath("//lightning-timepicker//input[@name='New_End_Date_Time']");
    By NEW_MEETING_ROOM_FIELD = By.xpath("//div//label[text()='New Meeting Room']/following::input[1]");
    By NEW_PAX_FIELD = By.xpath("//input[@name='New_Pax']");
    By NEW_ACTUAL_PAX_FIELD = By.xpath("//input[@name='New_Actual_Pax']");
    By NEW_COMISSION_PERCENTAGE_FIELD = By.xpath("//input[@name='New_Commission_Percentage']");
    By NEW_DISCOUNT_PERCENTAGE_FIELD = By.xpath("//input[@name='New_Discount_Percentage']");
    By NEW_DISCOUNT_AMOUNT_FIELD = By.xpath("//input[@name='New_Discount_Amount']");
    By NEW_UNIT_PRICE_FIELD = By.xpath("//input[@name='New_Unit_Price']");
    By NEXT_BUTTON = By.xpath("//button[@class='slds-button slds-button_brand flow-button__NEXT']");
    By YES_CHOICE_BUTTON = By.xpath("//input[@name='UserChoice' and @value='Yes_Choice' ]");
    By FINISH_BUTTON = By.xpath("//button[@class='slds-button slds-button_brand flow-button__FINISH']");


    @Step("Multi Edit Products (Products are part of Package)")
    public void multiEditProducts_partOfPackage
            (String newMeetingRoom, String commissionable, String hideOnOffer, String isOnConsumption)
            throws InterruptedException {
        driver.switchTo().frame(0);
        click3(NEW_MEETING_ROOM_FIELD);
        click3(By.xpath("//div//li[@data-name='" + newMeetingRoom + "']"));
        click3(By.xpath("//input[@name='Commissionable_0' and @value='" + commissionable + "_Choice' ]"));
        click3(By.xpath("//input[@name='Hide_on_Offer_0' and @value='" + hideOnOffer + "_Choice' ]"));
        click3(By.xpath("//input[@name='Is_on_Consumption_0' and @value='" + isOnConsumption + "_Choice' ]"));
        click4(NEXT_BUTTON);
        click3(YES_CHOICE_BUTTON);
        click4(NEXT_BUTTON);
        click4(FINISH_BUTTON);
        driver.switchTo().defaultContent();
    }

    @Step("Multi Edit Products (Products are not part of Package)")
    public void multiEditProducts_notPartOfPackage
            (String newStartDate, String newEndDate, String newMeetingRoom, String newPax, String newActualPax,
             String commissionable, String hideOnOffer, String isOnConsumption, String newComPer, String newDisPer,
             String newDisAmmount, String newUnitPrice ) throws InterruptedException {
        driver.switchTo().frame(0);
        click3(NEW_START_DATE_FIELD);
        writeText(NEW_START_DATE_FIELD, newStartDate);
        click3(NEW_END_DATE_FIELD);
        writeText(NEW_END_DATE_FIELD, newEndDate);
        click3(NEW_MEETING_ROOM_FIELD);
        click3(By.xpath("//div//li[@data-name='" + newMeetingRoom + "']"));
        click3(NEW_PAX_FIELD);
        writeText(NEW_PAX_FIELD, newPax);
        click3(NEW_ACTUAL_PAX_FIELD);
        writeText(NEW_ACTUAL_PAX_FIELD, newActualPax);
        click3(By.xpath("//input[@name='Commissionable' and @value='" + commissionable + "_Choice' ]"));
        click3(By.xpath("//input[@name='Hide_on_Offer' and @value='" + hideOnOffer + "_Choice' ]"));
        click3(By.xpath("//input[@name='Is_on_Consumption' and @value='" + isOnConsumption + "_Choice' ]"));
        click3(NEW_COMISSION_PERCENTAGE_FIELD);
        writeText(NEW_COMISSION_PERCENTAGE_FIELD, newComPer );
        click3(NEW_DISCOUNT_PERCENTAGE_FIELD);
        writeText(NEW_DISCOUNT_PERCENTAGE_FIELD, newDisPer);
        click3(NEW_DISCOUNT_AMOUNT_FIELD);
        writeText(NEW_DISCOUNT_AMOUNT_FIELD, newDisAmmount);
        click3(NEW_UNIT_PRICE_FIELD);
        writeText(NEW_UNIT_PRICE_FIELD, newUnitPrice);
        click4(NEXT_BUTTON);
        click3(YES_CHOICE_BUTTON);
        click4(NEXT_BUTTON);
        click4(FINISH_BUTTON);
        driver.switchTo().defaultContent();
        Thread.sleep(3000);
    }


}
