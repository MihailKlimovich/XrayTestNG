package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginPageForPackageOrg extends BasePage{

    /**Constructor*/
    public LoginPageForPackageOrg(WebDriver driver) {
        super(driver);
    }

    /**Variables*/
    String baseURL = "https://singlifypackagetest.my.salesforce.com";
    String URL = "https://singlifypackagetest.my.salesforce.com/packaging/installPackage.apexp?p0=";
    public String PACKAGE = System.getenv("PACKAGEVERSIONID");
    String PACKAGEURL =URL+PACKAGE;

    /**Web Elements*/
    By userNameId = By.id("username");
    By passwordId = By.id("password");
    By loginButtonId = By.id("Login");

    By ignorePhone = By.xpath("(//p/a)[2]");
    By iconWaffle = By.xpath("//div[@class='slds-icon-waffle']");
    By IMS = By.xpath("//p[text()='IMS']");

    By user = By.xpath("//img[@title='User']");
    By logOut = By.xpath("//a[text()='Log Out']");

    By upgrade = By.xpath("//span[text()='Upgrade']");
    By done = By.xpath("//span[text()='Done']");

    /**Page Methods*/

    public LoginPageForPackageOrg loginToSalesforce(String username, String password) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                driver.get(baseURL);
                writeText(userNameId, username);
                writeText(passwordId, password);
                click(loginButtonId);
                Thread.sleep(4000);
                driver.findElement(iconWaffle).click();
                break;
            }
            catch (Exception e) {

            }
            attempts++;
        }
        return null;
    }

    @Step("Login to SF2")
    public LoginPageForPackageOrg loginToSalesforce2(String username, String password) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                driver.get(baseURL);
                writeText(userNameId, username);
                writeText(passwordId, password);
                click(loginButtonId);
                Thread.sleep(4000);
                break;
            }
            catch (Exception e) {

            }
            attempts++;
        }
        return null;
    }

    //Log Out
    @Step("Log Out")
    public LoginPageForPackageOrg logOut() {
        clickInvisibleElement(user);
        clickInvisibleElement(logOut);
        return null;
    }

    @Step("Install Package")
    public LoginPageForPackageOrg installPackage() throws InterruptedException {

        if (PACKAGEURL=="https://singlifypackagetest.my.salesforce.com/packaging/installPackage.apexp?p0=0") {
            System.out.println(PACKAGEURL);
            System.out.println("New version isn't ready!");
        }
        else{
            try{
                System.out.println(PACKAGEURL);
                driver.get(PACKAGEURL);
                click(upgrade);
                Thread.sleep(120000);
                click(done);
                Thread.sleep(60000);
            }
            catch (NoSuchElementException e) {
                System.out.println("Please check a package version");
            }
        }
        return null;
    }


        public void authoriseURL(String sfdxPath, String authURL, String userName) throws IOException, InterruptedException {
            System.out.println("I try!!!!!!!!!!!!!!!!!!!!!!");
            File createFile = new File("**/SFDX_URL.txt");
            createFile.createNewFile();
            PrintWriter pw = new PrintWriter(createFile);
            pw.write(authURL);
            pw.flush();
            pw.close();
            StringBuilder authorise = BasePage.runLinuxCommand1(new String[]{
                    sfdxPath,
                    "auth:sfdxurl:store",
                    "-f",
                    "**/SFDX_URL.txt",
                    "-a",
                    userName
            });
            System.out.println(authorise);
            if(createFile.delete()){
                System.out.println("**/SFDX_URL.txt file deleted");
            }else System.out.println("**/SFDX_URL.txt file not found");
        }








}


