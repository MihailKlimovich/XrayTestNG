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
    protected QuoteHotelRoom quoteHotelRoom;
    protected QuoteMeetingRoom quoteMeetingRoom;
    protected QuoteMeetingPackages quoteMeetingPackages;
    protected QuoteProducts quoteProducts;
    protected Guests guests;
    protected Items items;
    protected Reservations reservations;
    protected ResourceGrouping resourceGrouping;
    protected SfdxCommand sfdxCommand;
    protected JsonParser jsonParser;
    protected JsonParser2 jsonParser2;
    protected Requests requests;
    protected ConvertWindow convertWindow;
    protected Hotel hotel;
    protected Product product;
    protected RoomType roomType;
    protected Resource resource;
    protected User user;
    protected Order order;

    public WebDriver getDriver() {
        return driver;
    }

    public static final String testDataExcelFileName = "testdata.xlsx";


    public String ORG_USERNAME = System.getenv("JAVAUSERNAME");
    public String ORG_PASSWORD = System.getenv("JAVAPASSWORD");
    public String ORG_URL = System.getenv("SF_URL");
    public String CONSUMER_KEY = System.getenv("SF_CONSUMER_KEY");
    public String SFDX = System.getenv("SFDX");
    public String SERVER_KEY_PATH = System.getenv("SERVER_KEY_PATH");
    public String SFDX_AUTH_URL = System.getenv("AUTH_URL");


    /*public String ORG_USERNAME = "cs1@com.postpart";
    public String ORG_PASSWORD = "Std2021!65";
    public String ORG_URL = "https://test.salesforce.com";
    public String CONSUMER_KEY = "3MVG9sSN_PMn8tjQ_i1zH6SFiiRiWU8a6A0ccIA8pgi6PtKmTcevqRVhFkbTw9WjqjcylgNcp6WfR7tCRz1Z5";
    public String SFDX = "/home/user/sfdx/bin/sfdx";
    public String SERVER_KEY_PATH = "/home/user/salesforceauthotest/jdoe/JWT/server.key";
    public String SFDX_AUTH_URL = "force://PlatformCLI::5Aep861KhtojOqEEpdH0TQSE9vuVzdljKpf1MnxJ39U2zMud8kQ1z6hlDIRT73KpEekvlhTMdXkadgGyID.Mu1f@thdemo-dev-ed.my.salesforce.com";*/

    /*public String ORG_USERNAME = "qa-thy626@succraft.com";
    public String ORG_PASSWORD = "hellohello11";
    public String ORG_URL = "https://test.salesforce.com";
    public String CONSUMER_KEY = "";
    public String SFDX = "/home/user/sfdx/bin/sfdx";
    public String SERVER_KEY_PATH = "/home/user/salesforceauthotest/jdoe/JWT/server.key";
    public String SFDX_AUTH_URL = "force://PlatformCLI::5Aep861xBpRqBedp8vzzGbigTH4C1HLTaldn0ZQWDzst2bGneG1Jej_gy659erJpIRCmfjSkmPiMj51qffSufGp@platform-site-757-dev-ed.cs160.my.salesforce.com";*/

    final protected String thynkPackDevOrg = "https://thynkpack-dev-ed.my.salesforce.com/";
    final protected String thynkPackUserName = "rostislav.orel@succraft.com";
    final protected String thynkPackKey = "3MVG91BJr_0ZDQ4ta_ZwN1EEnfj.OQSJWOBWMPFXclJ22A8oaKqM9KTLdsoSupXX0idQnMIdsI3IweGbsJx6t";
    final protected String thynkPackPassword = "Rost1508";



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
        loginPage = new LoginPageForPackageOrg(driver);
        loginPageForScratchOrg = new LoginPageForScratchOrg();
        developerConsoleWindow = new DeveloperConsoleWindow(driver);
        account = new AccountPage(driver);
        accounts = new AccountsPage(driver);
        creditNoteLine = new CreditNoteLine(driver);
        myceQuotes = new MyceQuotes(driver);
        packages = new Packages(driver);
        packageLine = new PackageLine(driver);
        quoteHotelRoom = new QuoteHotelRoom(driver);
        quoteMeetingRoom = new QuoteMeetingRoom(driver);
        quoteMeetingPackages = new QuoteMeetingPackages(driver);
        quoteProducts = new QuoteProducts(driver);
        guests = new Guests(driver);
        items = new Items(driver);
        reservations = new Reservations(driver);
        resourceGrouping = new ResourceGrouping(driver);
        sfdxCommand = new SfdxCommand(driver);
        jsonParser2 = new JsonParser2(driver);
        requests = new Requests(driver);
        convertWindow = new ConvertWindow(driver);
        hotel = new Hotel(driver);
        product = new Product(driver);
        roomType = new RoomType(driver);
        resource = new Resource(driver);
        user = new User(driver);
        order = new Order(driver);
    }

    @AfterClass
    public void teardown(){
        driver.close();
        driver.quit();
        if (driver != null) {
            driver = null;
        }
   }





}
