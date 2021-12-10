package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageObject.JsonParser2;

import java.io.IOException;
import java.util.List;

public class QuoteProductCreation extends BaseTest {

    @Test(priority = 1, description = "LogIn")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-619: Quote product creation")
    public void logIn() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
    }

    @Test(priority = 2, description = "Preconditions: Create a new MYCE Quote, Current Default Start hour and Default" +
            " End hour on the org.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-619: Quote product creation")
    public void preconditions() throws InterruptedException, IOException {
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCode(" Metadata.DeployContainer mdContainer = new Metadata.DeployContainer();\n" +
                "   Metadata.CustomMetadata cmd = new Metadata.CustomMetadata();\n" +
                "   cmd.fullName = 'thn__Default_Agile_Value__mdt.thn__Hotel_Demo';\n" +
                "   cmd.label = 'Hotel Demo';\n" +
                "   Metadata.CustomMetadataValue customField1 = new Metadata.CustomMetadataValue();\n" +
                "     customField1.field = 'thn__Default_Start_hour__c';\n" +
                "     customField1.value = '8';\n" +
                "     cmd.values.add(customField1);\n" +
                "     Metadata.CustomMetadataValue customField2 = new Metadata.CustomMetadataValue();\n" +
                "      customField2.field = 'thn__Default_End_hour__c';\n" +
                "      customField2.value = '9';\n" +
                "      cmd.values.add(customField2);\n" +
                "  mdContainer.addMetadata(cmd);\n" +
                "    Id job = Metadata.Operations.enqueueDeployment(mdContainer, null);\n" +
                "    System.debug(job);");
        myceQuotes.deleteQuoteSFDX(SFDX, "Name='QuoteProductCreationAutoTest'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder recordTypes = myceQuotes.soql(SFDX, "SELECT Id FROM RecordType WHERE" +
                " SobjectType='thn__MYCE_Quote__c' AND Name='Quote'", ORG_USERNAME);
        System.out.println(recordTypes);
        List<String> recordTypeID = JsonParser2.getFieldValueSoql(recordTypes.toString(), "Id");
        String quoteID = myceQuotes.createQuoteSFDX(SFDX, "Name='QuoteProductCreationAutoTest' thn__Pax__c=5" +
                " thn__Hotel__c='" + propertyID + "' thn__Arrival_Date__c=" + date.generateTodayDate2()
                + " thn__Departure_Date__c=" + date.generateTodayDate2_plus(0, 5) + "" +
                " RecordTypeId='" + recordTypeID.get(0) + "'", ORG_USERNAME);
    }

    @Test(priority = 3, description = "Add a product to the Quote. Leave the ‘Day Number’, ‘Start Time', 'End Time’" +
            " fields empty. Result: A product was added to the Quote. The ‘Start Date’, ‘End Date’ fields" +
            " were filled with values from ‘Arrival Date’ on the Quote. The ‘Start Time', 'End Time’ fields were" +
            " filled with values from ‘Start Time', 'End Time’ fields of the Product. ")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-619: Quote product creation")
    public void case1() throws InterruptedException, IOException {
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductCreationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        StringBuilder beverageRecord = product.getProductSFDX(SFDX, "Name='BEVERAGE'", ORG_USERNAME);
        String beverageID = JsonParser2.getFieldValue(beverageRecord.toString(), "Id");
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + beverageID + "'", ORG_USERNAME);
        StringBuilder quoteProductRecord= quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        System.out.println(quoteProductRecord);
        String quoteArrivalDay= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Arrival_Date__c");
        String quoteProductStartDate= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        String productStartTime= JsonParser2.getFieldValue(beverageRecord.toString(), "thn__Start_Time__c");
        String productEndTime= JsonParser2.getFieldValue(beverageRecord.toString(), "thn__End_Time__c");
        String quoteProductStartTime= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Time__c");
        String quoteProductEndTime= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteProductStartDate, quoteArrivalDay);
        Assert.assertEquals(quoteProductEndDate, quoteArrivalDay);
        Assert.assertEquals(quoteProductStartTime, productStartTime);
        Assert.assertEquals(quoteProductEndTime, productEndTime);
    }

    @Test(priority = 4, description = "Add a product to the Quote. Leave the ‘Day Number’, 'End Time’ fields empty." +
            " Chose a value for the ‘Start Time' field.  (E.g. 11:00). Result: A product was added to the Quote. The" +
            " ‘Start Date’, ‘End Date’ fields were filled with values from ‘Arrival Date’ on the Quote." +
            " The ‘Start Time’ was set to 11:00 as we specified above. The ‘End Time’ was filled ‘Start time’ +1 hour" +
            " (end time = start time +1 if default end time < start time).")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-619: Quote product creation")
    public void case2() throws InterruptedException, IOException {
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductCreationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productDinerID + "' thn__Start_Time__c='11:00:00.000Z'", ORG_USERNAME);
        StringBuilder quoteProductRecord= quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        String quoteArrivalDay= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Arrival_Date__c");
        String quoteProductStartDate= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        String quoteProductStartTime= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Time__c");
        String quoteProductEndTime= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteProductStartDate, quoteArrivalDay);
        Assert.assertEquals(quoteProductEndDate, quoteArrivalDay);
        Assert.assertEquals(quoteProductStartTime, "11:00:00.000Z");
        Assert.assertEquals(quoteProductEndTime, "12:00:00.000Z");
    }

    @Test(priority = 5, description = "Add a product to the Quote. Leave the ‘Day Number’, 'End Time’ fields empty." +
            " Chose a value for the ‘Start Time' field (E.g. 07:00). Result: A product was added to the Quote. The" +
            " ‘Start Date’, ‘End Date’ fields were filled with values from ‘Arrival Date’ on the Quote." +
            " The ‘Start Time’ was set to 07:00 as we specified above. The ‘End Time’ was filled from the  Default" +
            " End hour from the Custom Metadata > Default Agile values")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-619: Quote product creation")
    public void case3() throws InterruptedException, IOException {
        StringBuilder winesRecord = product.getProductSFDX(SFDX, "Name='WINES'", ORG_USERNAME);
        String productWinesID= JsonParser2.getFieldValue(winesRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductCreationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productWinesID + "' thn__Start_Time__c='07:00:00.000Z'", ORG_USERNAME);
        StringBuilder quoteProductRecord= quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        String quoteArrivalDay= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Arrival_Date__c");
        String quoteProductStartDate= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        String quoteProductStartTime= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Time__c");
        String quoteProductEndTime= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteProductStartDate, quoteArrivalDay);
        Assert.assertEquals(quoteProductEndDate, quoteArrivalDay);
        Assert.assertEquals(quoteProductStartTime, "07:00:00.000Z");
        Assert.assertEquals(quoteProductEndTime, "09:00:00.000Z");
    }

    @Test(priority = 6, description = "Add a product to the Quote. Leave the ‘Day Number’, 'Start Time’ fields empty." +
            " Chose a value for the 'End Time’ field (E.g. 11:00). Result: A product was added to the Quote. The" +
            " ‘Start Date’, ‘End Date’ fields were filled with values from ‘Arrival Date’ on the Quote." +
            " The ‘End Time’ was set to 11:00 as we specified above. The ‘Start Time’ was filled from the  Default" +
            " Start hour from the Custom Metadata > Default Agile values.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-619: Quote product creation")
    public void case4() throws InterruptedException, IOException {
        StringBuilder activityRecord = product.getProductSFDX(SFDX, "Name='ACTIVITY'", ORG_USERNAME);
        String productActivityID= JsonParser2.getFieldValue(activityRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductCreationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productActivityID + "' thn__End_Time__c='11:00:00.000Z'", ORG_USERNAME);
        StringBuilder quoteProductRecord= quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        String quoteArrivalDay= JsonParser2.getFieldValue(quoteRecord.toString(), "thn__Arrival_Date__c");
        String quoteProductStartDate= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        String quoteProductStartTime= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Time__c");
        String quoteProductEndTime= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteProductStartDate, quoteArrivalDay);
        Assert.assertEquals(quoteProductEndDate, quoteArrivalDay);
        Assert.assertEquals(quoteProductStartTime, "08:00:00.000Z");
        Assert.assertEquals(quoteProductEndTime, "11:00:00.000Z");
    }

    @Test(priority = 7, description = "Add a product to the Quote. Leave the ‘Start Time' field empty. Set the value" +
            " in the ‘Day Number’ to 3. Chose a value for the 'End Time’ field (E.g. 13:15). Result: A product was" +
            " added to the Quote. The ‘Start Date’, ‘End Date’ fields were filled with values from" +
            " (‘Arrival Date’ + 2)on the Quote. The ‘End Time’ was set to 13:15 as we specified above. The ‘Start" +
            " Time’ was filled from the  Default Start hour from the Custom Metadata > Default Agile values.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-619: Quote product creation")
    public void case5() throws InterruptedException, IOException {
        StringBuilder equipmentRecord = product.getProductSFDX(SFDX, "Name='EQUIPMENT'", ORG_USERNAME);
        String productEquipmentID= JsonParser2.getFieldValue(equipmentRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductCreationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productEquipmentID + "' thn__End_Time__c='13:15:00.000Z' thn__Day_Number__c=3",
                ORG_USERNAME);
        StringBuilder quoteProductRecord= quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        String quoteProductStartDate= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        String quoteProductStartTime= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Time__c");
        String quoteProductEndTime= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteProductStartDate, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(quoteProductEndDate, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(quoteProductStartTime, "08:00:00.000Z");
        Assert.assertEquals(quoteProductEndTime, "13:15:00.000Z");
    }

    @Test(priority = 8, description = "Add a product to the Quote. Leave the ‘End Time' field empty. Set the value" +
            " in the ‘Day Number’ to 3. Chose a value for the 'Start Time’ field (E.g. 07:00). Result: A product was" +
            " added to the Quote. The ‘Start Date’, ‘End Date’ fields were filled with values from " +
            "(‘Arrival Date’ + 2)on the Quote. The ‘Start Time’ was set to 07:00 as we specified above. The " +
            "‘End Time’ was filled from the  Default Start hour from the Custom Metadata > Default Agile values.")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-619: Quote product creation")
    public void case6() throws InterruptedException, IOException {
        StringBuilder foodRecord = product.getProductSFDX(SFDX, "Name='FOOD'", ORG_USERNAME);
        String productFoodID= JsonParser2.getFieldValue(foodRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductCreationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                        "' thn__Product__c='" + productFoodID + "' thn__Start_Time__c='07:00:00.000Z'" +
                " thn__Day_Number__c=3", ORG_USERNAME);
        StringBuilder quoteProductRecord= quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        String quoteProductStartDate= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        String quoteProductStartTime= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Time__c");
        String quoteProductEndTime= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteProductStartDate, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(quoteProductEndDate, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(quoteProductStartTime, "07:00:00.000Z");
        Assert.assertEquals(quoteProductEndTime, "09:00:00.000Z");
    }

    @Test(priority = 9, description = "Add a product to the Quote. Leave the ‘End Time' field empty. Set the value" +
            " in the ‘Day Number’ to 3. Chose a value for the 'Start Time’ field (E.g. 14:30). Result: A product was" +
            " added to the Quote. The ‘Start Date’, ‘End Date’ fields were filled with values from" +
            " (‘Arrival Date’ + 2)on the Quote. The ‘Start Time’ was set to 14:30 as we specified above. The" +
            " ‘End Time’ was with ‘Start Time’ + 1 (end time = start time +1 if default end time < start time). ")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-619: Quote product creation")
    public void case7() throws InterruptedException, IOException {
        StringBuilder foodRecord = product.getProductSFDX(SFDX, "Name='FOOD'", ORG_USERNAME);
        String productFoodID= JsonParser2.getFieldValue(foodRecord.toString(), "Id");
        StringBuilder quoteRecord = myceQuotes.
                getQuoteSFDX(SFDX, "Name='QuoteProductCreationAutoTest'", ORG_USERNAME);
        String quoteID= JsonParser2.getFieldValue(quoteRecord.toString(), "Id");
        String quoteProductID = quoteProducts.createQuoteProductSFDX(SFDX, "thn__MYCE_Quote__c='" + quoteID +
                "' thn__Product__c='" + productFoodID + "' thn__Start_Time__c='14:30:00.000Z'" +
                " thn__Day_Number__c=3", ORG_USERNAME);
        StringBuilder quoteProductRecord= quoteProducts.
                getQuoteProductSFDX(SFDX, "Id='" + quoteProductID + "'", ORG_USERNAME);
        String quoteProductStartDate= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Date__c");
        String quoteProductEndDate= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Date__c");
        String quoteProductStartTime= JsonParser2.
                getFieldValue(quoteProductRecord.toString(), "thn__Start_Time__c");
        String quoteProductEndTime= JsonParser2.getFieldValue(quoteProductRecord.toString(), "thn__End_Time__c");
        Assert.assertEquals(quoteProductStartDate, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(quoteProductEndDate, date.generateTodayDate2_plus(0, 2));
        Assert.assertEquals(quoteProductStartTime, "14:30:00.000Z");
        Assert.assertEquals(quoteProductEndTime, "15:30:00.000Z");
    }

}
