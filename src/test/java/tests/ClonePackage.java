package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import utils.Listeners.TestListener;

import java.io.IOException;

@Listeners({TestListener.class})

public class ClonePackage extends BaseTest {

    @Test(priority = 1, description = "Create new Package, package lines. To test all the possibilities," +
            " create one of type food and one of type meeting room. Link the first package line to the meeting room," +
            " click on Clone Package. Expected result: the package and its related lines have been cloned, the package" +
            " line of type food is linked to the cloned line of type meeting room")
    @Severity(SeverityLevel.NORMAL)
    @Story("THY-424: Clone Package testing")
    public void case1() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        packages.deletePackageSFDX(SFDX, "Name='ClonePackageAutoTest", ORG_USERNAME);
        packages.deletePackageSFDX(SFDX, "Name='ClonePackageAutoTest clone'", ORG_USERNAME);
        StringBuilder hotelRecord= hotel.getHotelSFDX(SFDX, "thn__Unique_Id__c='Demo'", ORG_USERNAME);
        String propertyID = JsonParser2.getFieldValue(hotelRecord.toString(), "Id");
        StringBuilder resourceRecord = resource.getResourceSFDX(SFDX, "Name='DEFAULT' thn__Hotel__c='"
                + propertyID + "'", ORG_USERNAME);
        String resourceID = JsonParser2.getFieldValue(resourceRecord.toString(), "Id");
        StringBuilder dinerRecord = product.getProductSFDX(SFDX, "Name='DINER'", ORG_USERNAME);
        String productDinerID= JsonParser2.getFieldValue(dinerRecord.toString(), "Id");
        StringBuilder meetingFullDayRecord = product.getProductSFDX(SFDX, "Name='MEETING FULL DAY'", ORG_USERNAME);
        String meetingFullDayID = JsonParser2.getFieldValue(meetingFullDayRecord.toString(), "Id");
        String packageID = packages.createPackageSFDX(SFDX, "Name='ClonePackageAutoTest' thn__Hotel__c='" +
                propertyID + "' thn__Active__c=true thn__Custom_Price__c=true thn__Multi_Days__c=true" +
                " thn__Discount_Max__c=10 thn__Description__c='Package for auto testing'", ORG_USERNAME);
        String plID1 = packageLine.createPackageLineSFDX(SFDX, "Name='Diner' thn__Package__c='" + packageID + "'" +
                " thn__Type__c='Food' thn__Product__c='" + productDinerID + "' thn__Start_Time__c=15:00" +
                " thn__End_Time__c=16:00 thn__Unit_Price__c=50 thn__VAT_Category__c=1 thn__AppliedDay__c=1" +
                " thn__Apply_Discount__c=true thn__Resource__c='" + resourceID + "' thn__Function__c='DEFAULT'",
                ORG_USERNAME);
        String plID2 = packageLine.createPackageLineSFDX(SFDX, "Name='Meeting Room' thn__Package__c='"
                + packageID + "'  thn__Type__c='Meeting Room' thn__Product__c='" + meetingFullDayID + "'" +
                " thn__Start_Time__c=12:00 thn__End_Time__c=18:00 thn__Unit_Price__c=100 thn__VAT_Category__c=1" +
                " thn__AppliedDay__c=1 thn__Apply_Discount__c=true thn__Resource__c='" + resourceID + "'" +
                " thn__Function__c='DEFAULT'", ORG_USERNAME);
        packageLine.updatePackageLineSFDX(SFDX, "Id='" + plID1 + "'",
                "thn__Service_Area__c='" + plID2 + "'", ORG_USERNAME);
        packages.goToPackages();
        //homePageForScratchOrg.openAppLauncher();
        //homePageForScratchOrg.sendTextInAppWindow("Packages");
        packages.openPackageRecord("ClonePackageAutoTest");
        packages.clonePackage("ClonePackageAutoTest clone");
        StringBuilder originalPackage = packages.getPackageSFDX(SFDX, "Name='ClonePackageAutoTest'", ORG_USERNAME);
        StringBuilder clonedPackage = packages.getPackageSFDX(SFDX, "Name='ClonePackageAutoTest clone'", ORG_USERNAME);
        String originalPackageId= JsonParser2.
                getFieldValue(originalPackage.toString(), "Id");
        String clonedPackageId= JsonParser2.
                getFieldValue(clonedPackage.toString(), "Id");
        String originalPackageProperty= JsonParser2.
                getFieldValue(originalPackage.toString(), "thn__Hotel__c");
        String clonedPackageProperty= JsonParser2.
                getFieldValue(clonedPackage.toString(), "thn__Hotel__c");
        String originalPackageActiveCheckbox= JsonParser2.
                getFieldValue(originalPackage.toString(), "thn__Active__c");
        String clonedPackageActiveCheckbox= JsonParser2.
                getFieldValue(clonedPackage.toString(), "thn__Active__c");
        String originalPackageCustomPriceCheckbox= JsonParser2.
                getFieldValue(originalPackage.toString(), "thn__Custom_Price__c");
        String clonedPackageCustomPriceCheckbox= JsonParser2.
                getFieldValue(clonedPackage.toString(), "thn__Custom_Price__c");
        String originalPackageMultiDaysCheckbox= JsonParser2.
                getFieldValue(originalPackage.toString(), "thn__Multi_Days__c");
        String clonedPackageMultiDaysCheckbox= JsonParser2.
                getFieldValue(clonedPackage.toString(), "thn__Multi_Days__c");
        String originalPackageDiscountMax = JsonParser2.
                getFieldValue(originalPackage.toString(), "thn__Discount_Max__c");
        String clonedPackageDiscountMax = JsonParser2.
                getFieldValue(clonedPackage.toString(), "thn__Discount_Max__c");
        String originalPackageDescription = JsonParser2.
                getFieldValue(originalPackage.toString(), "thn__Description__c");
        String clonedPackageDescription = JsonParser2.
                getFieldValue(clonedPackage.toString(), "thn__Description__c");
        StringBuilder originalPL1 = packageLine.getPackageLineSFDX(SFDX, "Id='" + plID1 + "'", ORG_USERNAME);
        StringBuilder originalPL2 = packageLine.getPackageLineSFDX(SFDX, "Id='" + plID2 + "'", ORG_USERNAME);
        StringBuilder clonedlPL1 = packageLine.getPackageLineSFDX(SFDX, "thn__Package__c='" + clonedPackageId +
                "' Name='Diner'", ORG_USERNAME);
        StringBuilder clonedPL2 = packageLine.getPackageLineSFDX(SFDX, "thn__Package__c='" + clonedPackageId +
                "' Name='Meeting Room'", ORG_USERNAME);
        String originalPL1Product= JsonParser2.
                getFieldValue(originalPL1.toString(), "thn__Product__c");
        String originalPL2Product= JsonParser2.
                getFieldValue(originalPL2.toString(), "thn__Product__c");
        String clonedPL1Product= JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__Product__c");
        String clonedPL2Product= JsonParser2.
                getFieldValue(clonedPL2.toString(), "thn__Product__c");
        String originalPL1Resource= JsonParser2.
                getFieldValue(originalPL1.toString(), "thn__Resource__c");
        String originalPL2Resource= JsonParser2.
                getFieldValue(originalPL2.toString(), "thn__Resource__c");
        String clonedPL1Resource= JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__Resource__c");
        String clonedPL2Resource= JsonParser2.
                getFieldValue(clonedPL2.toString(), "thn__Resource__c");
        String originalPL1AppliedDay= JsonParser2.
                getFieldValue(originalPL1.toString(), "thn__AppliedDay__c");
        String originalPL2AppliedDay= JsonParser2.
                getFieldValue(originalPL2.toString(), "thn__AppliedDay__c");
        String clonedPL1AppliedDay= JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__AppliedDay__c");
        String clonedPL2AppliedDay= JsonParser2.
                getFieldValue(clonedPL2.toString(), "thn__AppliedDay__c");
        String originalPL1Type= JsonParser2.
                getFieldValue(originalPL1.toString(), "thn__Type__c");
        String originalPL2Type= JsonParser2.
                getFieldValue(originalPL2.toString(), "thn__Type__c");
        String clonedPL1Type= JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__Type__c");
        String clonedPL2Type= JsonParser2.
                getFieldValue(clonedPL2.toString(), "thn__Type__c");
        String originalPL1Function= JsonParser2.
                getFieldValue(originalPL1.toString(), "thn__Function__c");
        String originalPL2Function= JsonParser2.
                getFieldValue(originalPL2.toString(), "thn__Function__c");
        String clonedPL1Function= JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__Function__c");
        String clonedPL2Function= JsonParser2.
                getFieldValue(clonedPL2.toString(), "thn__Function__c");
        String originalPL1StartTime= JsonParser2.
                getFieldValue(originalPL1.toString(), "thn__Start_Time__c");
        String originalPL2StartTime= JsonParser2.
                getFieldValue(originalPL2.toString(), "thn__Start_Time__c");
        String clonedPL1StartTime= JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__Start_Time__c");
        String clonedPL2StartTime= JsonParser2.
                getFieldValue(clonedPL2.toString(), "thn__Start_Time__c");
        String originalPL1EndTime= JsonParser2.
                getFieldValue(originalPL1.toString(), "thn__End_Time__c");
        String originalPL2EndTime= JsonParser2.
                getFieldValue(originalPL2.toString(), "thn__End_Time__c");
        String clonedPL1EndTime= JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__End_Time__c");
        String clonedPL2EndTime= JsonParser2.
                getFieldValue(clonedPL2.toString(), "thn__End_Time__c");
        String originalPL1UnitPrice= JsonParser2.
                getFieldValue(originalPL1.toString(), "thn__Unit_Price__c");
        String originalPL2UnitPrice= JsonParser2.
                getFieldValue(originalPL2.toString(), "thn__Unit_Price__c");
        String clonedPL1UnitPrice= JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__Unit_Price__c");
        String clonedPL2UnitPrice= JsonParser2.
                getFieldValue(clonedPL2.toString(), "thn__Unit_Price__c");
        String originalPL1VATCategory= JsonParser2.
                getFieldValue(originalPL1.toString(), "thn__VAT_Category__c");
        String originalPL2VATCategory= JsonParser2.
                getFieldValue(originalPL2.toString(), "thn__VAT_Category__c");
        String clonedPL1VATCategory= JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__VAT_Category__c");
        String clonedPL2VATCategory= JsonParser2.
                getFieldValue(clonedPL2.toString(), "thn__VAT_Category__c");
        String originalPL1ApplyDiscount = JsonParser2.
                getFieldValue(originalPL1.toString(), "thn__Apply_Discount__c");
        String originalPL2ApplyDiscount = JsonParser2.
                getFieldValue(originalPL2.toString(), "thn__Apply_Discount__c");
        String clonedPL1ApplyDiscount = JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__Apply_Discount__c");
        String clonedPL2ApplyDiscount = JsonParser2.
                getFieldValue(clonedPL2.toString(), "thn__Apply_Discount__c");
        String clonedPL2Id = JsonParser2.
                getFieldValue(clonedPL2.toString(), "Id");
        String clonedPL1MeetingRoom = JsonParser2.
                getFieldValue(clonedlPL1.toString(), "thn__Service_Area__c");
        Assert.assertEquals(originalPackageProperty, clonedPackageProperty);
        Assert.assertEquals(originalPackageActiveCheckbox, clonedPackageActiveCheckbox);
        Assert.assertEquals(originalPackageCustomPriceCheckbox, clonedPackageCustomPriceCheckbox);
        Assert.assertEquals(originalPackageMultiDaysCheckbox, clonedPackageMultiDaysCheckbox);
        Assert.assertEquals(originalPackageDiscountMax, clonedPackageDiscountMax);
        Assert.assertEquals(originalPackageDescription, clonedPackageDescription);
        Assert.assertEquals(originalPL1Product, clonedPL1Product);
        Assert.assertEquals(originalPL2Product, clonedPL2Product);
        Assert.assertEquals(originalPL1Resource, clonedPL1Resource);
        Assert.assertEquals(originalPL2Resource, clonedPL2Resource);
        Assert.assertEquals(originalPL1AppliedDay, clonedPL1AppliedDay);
        Assert.assertEquals(originalPL2AppliedDay, clonedPL2AppliedDay);
        Assert.assertEquals(originalPL1Type, clonedPL1Type);
        Assert.assertEquals(originalPL2Type, clonedPL2Type);
        Assert.assertEquals(originalPL1Function, clonedPL1Function);
        Assert.assertEquals(originalPL2Function, clonedPL2Function);
        Assert.assertEquals(originalPL1StartTime, clonedPL1StartTime);
        Assert.assertEquals(originalPL2StartTime, clonedPL2StartTime);
        Assert.assertEquals(originalPL1EndTime, clonedPL1EndTime);
        Assert.assertEquals(originalPL2EndTime, clonedPL2EndTime);
        Assert.assertEquals(originalPL1UnitPrice, clonedPL1UnitPrice);
        Assert.assertEquals(originalPL2UnitPrice, clonedPL2UnitPrice);
        Assert.assertEquals(originalPL1VATCategory, clonedPL1VATCategory);
        Assert.assertEquals(originalPL2VATCategory, clonedPL2VATCategory);
        Assert.assertEquals(originalPL1ApplyDiscount, clonedPL1ApplyDiscount);
        Assert.assertEquals(originalPL2ApplyDiscount, clonedPL2ApplyDiscount);
        Assert.assertEquals(clonedPL1MeetingRoom, clonedPL2Id);
    }

}
