package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class NextFunctionSheet extends BaseTest {

    @Test(priority = 1, description = "Create a Function Sheet record. Set the dates of the records to be earlier than" +
            " today. Run the batch 'NextFunctionSheet'. Check the Property that was linked to the Function Sheet" +
            " record. Expected result: The ‘Next Function Sheet’ field of the Property is Empty after the batch was" +
            " run.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-584: Next Function sheet")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPage.authoriseURL(SFDX, ADMIN_AUTH_URL, ADMIN_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DeleteFunctionSheetDemo.apex");
        functionSheet.deleteFunctionSheetSFDX(SFDX, "Name='FunctionSheetAutoTest1'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String functionSheetID = functionSheet.createFunctionSheetSFDX(SFDX, "Name='FunctionSheetAutoTest1'" +
                " thn__Start_Date__c=" + date.generateTodayDate2_minus(0, 2) + " thn__End_Date__c=" +
                date.generateTodayDate2_minus(0, 1) + " thn__Hotel__c='" + propertyID + "'" , ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchNextFunctionSheet.apex");
        Thread.sleep(15000);
        StringBuilder updatedHotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyNextFunctionSheet = JsonParser2.
                getFieldValue(updatedHotelRecord.toString(), "thn__Next_Function_Sheet__c");
        Assert.assertEquals(propertyNextFunctionSheet, null);
    }

    @Test(priority = 2, description = "Create a Function Sheet record. Set the dates of the records to be later than" +
            " today. Run the batch 'NextFunctionSheet'. Check the Property that was linked to the Function Sheet" +
            " record. Expected result: The ‘Next Function Sheet’ field of the Property was linked with the Function" +
            " Sheet record.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-584: Next Function sheet")
    public void case2() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DeleteFunctionSheetDemo.apex");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String functionSheetID = functionSheet.createFunctionSheetSFDX(SFDX, "Name='FunctionSheetAutoTest2'" +
                " thn__Start_Date__c=" + date.generateTodayDate2_plus(0, 5) + " thn__End_Date__c=" +
                date.generateTodayDate2_plus(0, 6) + " thn__Hotel__c='" + propertyID + "'" , ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchNextFunctionSheet.apex");
        Thread.sleep(15000);
        StringBuilder updatedHotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyNextFunctionSheet = JsonParser2.
                getFieldValue(updatedHotelRecord.toString(), "thn__Next_Function_Sheet__c");
        Assert.assertEquals(propertyNextFunctionSheet, functionSheetID);
    }

    @Test(priority = 3, description = "Create a Function Sheet Day record. Set the Date of the records to be later" +
            " than today. Run the batch 'NextFunctionSheet'.Check the Property that was linked to the Function Sheet" +
            " Day record. Expected result: The ‘Next Function Sheet Day’ field of the Property was linked with the" +
            " Function Sheet Day record.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-584: Next Function sheet")
    public void case3() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DeleteFunctionSheetDayDemo.apex");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String functionSheetDayID = functionSheetDay.createFunctionSheetDaySFDX(SFDX, "Name='FunctionSheetDay1'" +
                " thn__Date__c=" + date.generateTodayDate2_plus(0, 5) + " thn__Hotel__c='" + propertyID +
                "'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchNextFunctionSheet.apex");
        Thread.sleep(15000);
        StringBuilder updatedHotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyNextFunctionSheetDay = JsonParser2.
                getFieldValue(updatedHotelRecord.toString(), "thn__Next_Function_Sheet_Day__c");
        Assert.assertEquals(propertyNextFunctionSheetDay, functionSheetDayID);
    }

    @Test(priority = 4, description = "Create a Function Sheet Day record. Set the dates of the records to be" +
            " earlier than today. Run the batch 'NextFunctionSheet'.Check the Property that was linked to the" +
            " Function Sheet Day record. Expected result: The ‘Next Function Sheet Day’ field of the Property is" +
            " Empty after the batch was run.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-584: Next Function sheet")
    public void case4() throws InterruptedException, IOException {
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/DeleteFunctionSheetDayDemo.apex");
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        String functionSheetDayID = functionSheetDay.createFunctionSheetDaySFDX(SFDX, "Name='FunctionSheetDay2'" +
                " thn__Date__c=" + date.generateTodayDate2_minus(0, 1) + " thn__Hotel__c='" + propertyID +
                "'", ORG_USERNAME);
        user.apexExecute(SFDX, ADMIN_USERNAME, "src/main/Data/BatchNextFunctionSheet.apex");
        Thread.sleep(15000);
        StringBuilder updatedHotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyNextFunctionSheetDay = JsonParser2.
                getFieldValue(updatedHotelRecord.toString(), "thn__Next_Function_Sheet_Day__c");
        Assert.assertEquals(propertyNextFunctionSheetDay, null);
    }

}
