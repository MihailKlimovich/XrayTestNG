package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.IOException;

public class QuoteHotelRoomPrice extends BasePage {

    /**Constructor*/
    public QuoteHotelRoomPrice(WebDriver driver) {
        super(driver);
    }

    //////////////////////////////   SFDX COMMANDS   ////////////////////////////////////

    @Step("Get Quote Hotel Room Price SFDX")
    public StringBuilder getQuoteHotelRoomPriceSFDX(String sfdxPath, String where, String userName)
            throws IOException, InterruptedException {
        StringBuilder quoteHotelRoomPriceRecord = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:get",
                "-s",
                "thn__Quote_Hotel_Room_Price__c",
                "-w",
                where,
                "-u",
                userName,
                "--json"});
        return quoteHotelRoomPriceRecord;
    }

    @Step("Update Quote Hotel Room Price SFDX")
    public StringBuilder updateQuoteHotelRoomPriceSFDX(String sfdxPath, String where, String value, String userName)
            throws IOException, InterruptedException {
        StringBuilder quoteHotelRoomPriceUpdateResult = SfdxCommand.runLinuxCommand1(new String[]{
                sfdxPath,
                "force:data:record:update",
                "-s",
                "thn__Quote_Hotel_Room_Price__c",
                "-w",
                where,
                "-v",
                value,
                "-u",
                userName,
                "--json"});
        System.out.println(quoteHotelRoomPriceUpdateResult);
        return quoteHotelRoomPriceUpdateResult;
    }


}
