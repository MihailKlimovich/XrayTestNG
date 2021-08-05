package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import pages.BasePage;
import tests.BaseTest;

import java.io.*;
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


    public static void main(String[] args) throws InterruptedException, IOException {
        String[] command = { "/home/minsk-sc/sfdx/bin/sfdx", "force:data:record:create",  "-s thn__MYCE_Quote__c", "-v \"Name='SFDXTEST556'\"",  "-u THYNK-VR", "--json" };
        String cmd = "/home/minsk-sc/sfdx/bin/sfdx force:data:record:create -s thn__MYCE_Quote__c -v \"Name='SFDXTEST556' thn__Commissionable__c=true\" -u THYNK-VR --json";
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(command);
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        while ((line = buf.readLine()) != null) {
            System.out.println(line);
        }
    }

     /*public StringBuilder  runLinuxCommand(String cmd) throws InterruptedException, IOException {
        StringBuilder strB = new StringBuilder();
         System.out.println(cmd);
        Runtime run = Runtime.getRuntime();
        String [] d = {"/home/minsk-sc/sfdx/bin/sfdx", "force:data:soql:query", "-q 'SELECT thn__Commissionable__c, thn__Commission_to__c FROM thn__MYCE_Quote__c where Name=\'Test22\''", "-u test-qcud2ypdbztz@example.com", "--json"};
        Process pr = run.exec( d,null);
        pr.waitFor();
         System.out.println(pr);
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        while ((line = buf.readLine()) != null) {
            strB.append(line);
            strB.append("\n");
        }
        buf.close();
        return strB;
    }

    public StringBuilder  runLinuxCommand2(String cmd) throws InterruptedException, IOException {
        StringBuilder strB = new StringBuilder();
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        while ((line = buf.readLine()) != null) {
            strB.append(line);
            strB.append("\n");
        }
        buf.close();
        return strB;
    }*/












}
