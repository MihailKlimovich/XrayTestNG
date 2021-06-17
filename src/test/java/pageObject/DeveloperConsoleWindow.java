package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import pages.BasePage;
import tests.BaseTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class DeveloperConsoleWindow extends BasePage {

    /**Constructor*/
    public DeveloperConsoleWindow (WebDriver driver) {
        super(driver);
    }

    /**Variables*/

//Options
    By options = By.xpath("//div[contains(@class,'setupGear')]");

    //Developer Console
    By developerConsole = By.xpath("//span[@class='slds-align-middle'][contains(.,'Developer Console')]");

    //Debug
    By debug = By.xpath("//div[@id='debugMenuEntry']");

    //Execute Anonymous Window
    By executeAnonymousWindow = By.xpath("//div[text()='Open Execute Anonymous Window']");

    //Code space
    By codeSpace = By.xpath("//div[@style='position: relative; outline: none;']");

    //Execute button
    By executeButton = By.xpath("//div[@id='ExecAnon']//span[text()='Execute']");

    By logDeletedOkButton = By.xpath("//span[@class='x-btn-inner'][text()='OK']");

    /**Methods*/

    @Step("Open Developer Console")
    public DeveloperConsoleWindow openDeveloperConsole () throws InterruptedException {
        click(options);
        click(developerConsole);
        return this;
    }

    @Step("Open Execute Anonymous Window")
    public DeveloperConsoleWindow openExecuteAnonymousWindow () throws InterruptedException {
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> itr = handles.iterator();
        String parentWindow = itr.next();
        String newWindow = itr.next();
        driver.switchTo().window(newWindow);
        driver.manage().window().maximize();
        try{
            driver.findElement(logDeletedOkButton).click();
        }
        catch (ElementNotInteractableException | NoSuchElementException e){
            //Action not needed
        }
        click(debug);
        click(executeAnonymousWindow);
        return this;
    }

    @Step("Run Apex Code")
    public DeveloperConsoleWindow runApexCode (String ApexCode) throws InterruptedException {
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> itr = handles.iterator();
        String parentWindow = itr.next();
        String newWindow = itr.next();
        driver.switchTo().window(newWindow);
        ctrlA();
        delete();
        sendText(ApexCode);
        click(executeButton);
        Thread.sleep(3000);
        driver.switchTo().window(parentWindow);
        return this;
    }

    public DeveloperConsoleWindow runApexCodeFromFile (String filePATH) throws InterruptedException, IOException {
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> itr = handles.iterator();
        String parentWindow = itr.next();
        String newWindow = itr.next();
        driver.switchTo().window(newWindow);
        ctrlA();
        delete();
        File file = new File(filePATH);
        String str;
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        while((str=br.readLine())!=null) {
            System.out.println(str);
            sendText(str);
            enter();
        }
        Thread.sleep(2000);
        click(executeButton);
        Thread.sleep(5000);
        try{
            driver.findElement(logDeletedOkButton).click();
        }
        catch (NoSuchElementException | ElementNotInteractableException e){
            //Action not needed
        }
        driver.switchTo().window(parentWindow);
        return this;
    }
}
