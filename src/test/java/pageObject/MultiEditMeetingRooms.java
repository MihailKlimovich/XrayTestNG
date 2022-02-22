package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

public class MultiEditMeetingRooms extends BasePage {

    /**Constructor*/
    public MultiEditMeetingRooms(WebDriver driver) {
        super(driver);
    }

    By NEW_START_DATE_FIELD = By.xpath("//div//label[text()='Date']/following-sibling::div//input[@name='New_Start_Date_Time']");
    By NEW_END_DATE_FIELD = By.xpath("//div//label[text()='Date']/following-sibling::div//input[@name='New_End_Date_Time']");
    By NEW_START_TIME_FIELD = By.xpath("//lightning-timepicker//input[@name='New_Start_Date_Time']");
    By NEW_END_TIME_FIELD = By.xpath("//lightning-timepicker//input[@name='New_End_Date_Time']");
    By EDIT_SETUP_VALUES_CHECKBOX = By.xpath("//input[@name='Edit_Setup_Values']");
    By NEW_SETUP_PICKLIST= By.xpath("//select[@name='New_Setup']");
    By EDIT_RESERVATION_STATUS_CHECKBOX = By.xpath("//input[@name='Edit_Reservation_Status_Values']");
    By NEW_RESERVATION_STATUS_PICKLIST= By.xpath("//select[@name='New_Reservation_Status']");
    By NEW_FUNCTION_NAME_FIELD = By.xpath("//input[@name='New_Function_Name']");
    By NEW_PAX_FIELD = By.xpath("//input[@name='New_Pax']");
    By NEW_ACTUAL_PAX_FIELD = By.xpath("//input[@name='New_Actual_Pax']");
    By NEW_COMISSION_PERCENTAGE_FIELD = By.xpath("//input[@name='New_Commission_Percentage']");
    By NEW_DISCOUNT_PERCENTAGE_FIELD = By.xpath("//input[@name='New_Discount_Percentage']");
    By NEW_DISCOUNT_AMOUNT_FIELD = By.xpath("//input[@name='New_Discount_Amount']");
    By NEW_UNIT_PRICE_FIELD = By.xpath("//input[@name='New_Unit_Price']");
    By NEXT_BUTTON = By.xpath("//button[@class='slds-button slds-button_brand flow-button__NEXT']");
    By YES_CHOICE_BUTTON = By.xpath("//input[@name='UserChoice' and @value='Yes_Choice' ]");
    By FINISH_BUTTON = By.xpath("//button[@class='slds-button slds-button_brand flow-button__FINISH']");

    @Step("Multi Edit Products (Meeting Rooms are part of Package)")
    public void multiEditMeetingRooms_PartOfPackage
            (String newSetup, String newResStatus, String commissionable, String hideOnOffer,
             String lockResource)
            throws InterruptedException {
        driver.switchTo().frame(0);
        click3(By.xpath("//input[@name='Edit_Setup_Values_0']"));
        click3(By.xpath("//select[@name='New_Setup_0']"));
        click3(By.xpath("//option[text()='" + newSetup + "']"));
        click3(By.xpath("//input[@name='Edit_Reservation_Status_Values_0']"));
        click3(By.xpath("//select[@name='New_Reservation_Status_0']"));
        Thread.sleep(1000);
        click3(By.xpath("//option[text()='" + newResStatus + "']"));
        //click3(By.xpath("//input[@name='New_Function_Name_0']"));
        //writeText(By.xpath("//input[@name='New_Function_Name_0']"), newFuncName);
        click3(By.xpath("//input[@name='Lock_Resource_0' and @value='" + lockResource + "_Choice']"));
        click3(By.xpath("//input[@name='Hide_on_Offer_0' and @value='" + hideOnOffer + "_Choice']"));
        click3(By.xpath("//input[@name='Commissionable_0' and @value='" + commissionable + "_Choice']"));
        click4(NEXT_BUTTON);
        click3(YES_CHOICE_BUTTON);
        click4(NEXT_BUTTON);
        click4(FINISH_BUTTON);
        driver.switchTo().defaultContent();
    }

    @Step("Multi Edit Products (Meeting Rooms are not part of Package)")
    public void multiEditMeetingRooms_notPartOfPackage
            (String newStartDate, String newEndDate, String newSetup, String newResStatus,
             String newPax, String newActualPax, String commissionable, String hideOnOffer, String lockResource,
             String newComPer, String newDisPer, String newDisAmmount, String newUnitPrice )
            throws InterruptedException {
        driver.switchTo().frame(0);
        click3(NEW_START_DATE_FIELD);
        writeText(NEW_START_DATE_FIELD, newStartDate);
        click3(NEW_END_DATE_FIELD);
        writeText(NEW_END_DATE_FIELD, newEndDate);
        click3(EDIT_SETUP_VALUES_CHECKBOX);
        Thread.sleep(1000);
        click3(NEW_SETUP_PICKLIST);
        Thread.sleep(1000);
        click3(By.xpath("//option[text()='" + newSetup + "']"));
        click3(EDIT_RESERVATION_STATUS_CHECKBOX);
        click3(NEW_RESERVATION_STATUS_PICKLIST);
        click3(By.xpath("//option[text()='" + newResStatus + "']"));
        click3(NEW_PAX_FIELD);
        writeText(NEW_PAX_FIELD, newPax);
        click3(NEW_ACTUAL_PAX_FIELD);
        writeText(NEW_ACTUAL_PAX_FIELD, newActualPax);
        click3(By.xpath("//input[@name='Lock_Resource' and @value='" + lockResource + "_Choice' ]"));
        click3(By.xpath("//input[@name='Hide_on_Offer' and @value='" + hideOnOffer + "_Choice' ]"));
        click3(By.xpath("//input[@name='Commissionable' and @value='" + commissionable + "_Choice' ]"));
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
    }



}
