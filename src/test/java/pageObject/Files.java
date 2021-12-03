package pageObject;

import io.qameta.allure.Step;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

import java.io.*;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class Files extends BasePage {

    /**Constructor*/
    public Files(WebDriver driver) {
        super(driver);
    }

    By SHOW_MORE_ACTION= By.xpath("//div//h1[@title='Files']/following::span[text()='Show Actions']");
    By DOWNLOAD_BUTTON= By.xpath("//li//a[@title='Download']");

    @Step("Click Download")
    public void clickDownload(String fileName) throws InterruptedException {
        refreshPage();
        int attempts = 0;
        while (attempts < 4) {
            try {
                wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@title='" + fileName + "']")));
                click3(By.xpath("//span[text()='"+ fileName + "']/ancestor::th/following::span[text()='Show Actions']/parent::span"));
                click3(DOWNLOAD_BUTTON);
                Thread.sleep(3000);
                break;
            }
            catch (Exception e){
                refreshPage();
            }
            attempts++;
        }
    }

    @Step("Delete dile")
    public void deleteFile(String filePath) throws InterruptedException {
        File file = new File(filePath);
        if(file.delete()){
            System.out.println(filePath + " файл удален");
        }else System.out.println("Файла " + filePath + " не обнаружено");
    }



    @Step("Update xls")
    public void updateXLS(String fileName, int numberRow, int numderCell, String value) throws IOException {
        File file = new File("/home/user/Downloads/" + fileName);
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFCell cell = sheet.getRow(numberRow).getCell(numderCell);
        cell.setCellValue(value);
        inputStream.close();
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
    }



}
