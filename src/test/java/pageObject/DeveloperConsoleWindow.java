package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.zeroturnaround.exec.ProcessExecutor;
import pages.BasePage;
import tests.BaseTest;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;


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

    By queryEditorButton = By.xpath("//button//span[text()='Query Editor']");

    By executeQueryButton = By.xpath("//span[@id = 'queryExecuteButton-btnInnerEl']");

    By queryEditorTextArea = By.xpath("//textarea[@id = 'queryEditorText-inputEl']");

    By queryResult = By.xpath("//tbody//tr[@class='x-grid-row']");




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

    @Step("Run SOQL request")
    public DeveloperConsoleWindow runSoqlRequest (String request) throws InterruptedException {
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> itr = handles.iterator();
        String parentWindow = itr.next();
        String newWindow = itr.next();
        driver.switchTo().window(newWindow);
        click(queryEditorButton);
        click(queryEditorTextArea);
        ctrlA();
        delete();
        sendText(request);
        click(executeQueryButton);
        Thread.sleep(3000);
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

    @Step("Read query result")
    public String readQueryResult() throws InterruptedException {
        return readRecalculateMessage2(queryResult);
    }

    @Step("Run Apex Code")
    public DeveloperConsoleWindow goToParentWindow() throws InterruptedException {
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> itr = handles.iterator();
        String parentWindow = itr.next();
        String newWindow = itr.next();
        driver.switchTo().window(parentWindow);
        return this;
    }
















}
