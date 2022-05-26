package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class Product extends BasePage {

    /**Constructor*/
    public Product(WebDriver driver) {
        super(driver);
    }


    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Get Product SFDX")
    public StringBuilder getProductSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder productRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Product__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return productRecord;
    }

    @Step("Create Product SFDX")
    public String createProductSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder productResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Product__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Product create result:");
        System.out.println(productResult);
        String productID = JsonParser2.getFieldValue(productResult.toString(), "id");
        return productID;
    }

    @Step("Delete Product SFDX")
    public void deleteProductSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder result = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:delete",
                "-s",
                "thn__Product__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        System.out.println(result);
    }

    @Step("Update Product SFDX")
    public void updateProductSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder productUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Product__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(productUpdateResult);
    }

    @Step("Create Combo Product Component SFDX")
    public String createProductComboComponentSFDX(String sfdxPath, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder productComboComponentResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:create",
                "-s",
                "thn__Product_Combo_Component__c",
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println("Product create result:");
        System.out.println(productComboComponentResult);
        String productComboComponentID = JsonParser2.getFieldValue(productComboComponentResult.toString(), "id");
        return productComboComponentID;
    }

    @Step("Get Product SFDX")
    public StringBuilder getProductComboComponentSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder productComboComponentRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Product_Combo_Component__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return productComboComponentRecord;
    }


}
