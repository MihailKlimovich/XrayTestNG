package tests;

import dates.Dates;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pageObject.*;
import pages.HomePageForPackageOrg;
import pages.HomePageForScratchOrg;
import pages.LoginPageForPackageOrg;
import pages.LoginPageForScratchOrg;

import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected Dates date;
    protected WebDriver driver;
    protected HomePageForPackageOrg homePage;
    protected HomePageForScratchOrg homePageForScratchOrg;
    protected LoginPageForPackageOrg loginPage;
    protected LoginPageForScratchOrg loginPageForScratchOrg;
    protected AccountPage account;
    protected AccountsPage accounts;
    protected DeveloperConsoleWindow developerConsoleWindow;
    protected CreditNoteLine creditNoteLine;
    protected MyceQuotes myceQuotes;
    protected Packages packages;
    protected PackageLine packageLine;

    public WebDriver getDriver() {
        return driver;
    }

    public static final String testDataExcelFileName = "testdata.xlsx";

    public String SCRATCHORGUSERNAME = "test-wixjue9le3v0@example.com";
    public String SCRATCHORGPASSWORD = "mEA3VqI_*OUSw";

    @BeforeClass
    public void classLevelSetup(){
        ChromeOptions options= new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-cache");
        options.addArguments("--disk-cache-size=1");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-data-dir=/tmp/temp_profile");
        options.addArguments(" --whitelisted-ips=\"\"");
        options.addArguments("--headless", "window-size=1920,1024", "--no-sandbox");
        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @BeforeMethod
    public void methodLevelSetup(){
        date = new Dates();
        homePage = new HomePageForPackageOrg();
        homePageForScratchOrg = new HomePageForScratchOrg(driver);
        loginPage = new LoginPageForPackageOrg();
        loginPageForScratchOrg = new LoginPageForScratchOrg();
        developerConsoleWindow = new DeveloperConsoleWindow(driver);
        account = new AccountPage(driver);
        accounts = new AccountsPage(driver);
        creditNoteLine = new CreditNoteLine(driver);
        myceQuotes = new MyceQuotes(driver);
        packages = new Packages(driver);
        packageLine = new PackageLine(driver);
    }

    @AfterClass
    public void teardown(){
        driver.close();
        driver.quit();
        if (driver != null) {
            driver = null;
        }
   }

    //Successfully set the password "PWBgjUWd26#s|" for user test-unbuxpim9tgh@example.com.
    //Successfully set the password "vt*P7[VrGp&]P" for user test-6epicstbdzhd@example.com.



}
