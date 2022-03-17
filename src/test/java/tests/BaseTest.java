package tests;

import dates.Dates;
import org.junit.Rule;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pageObject.*;
import pageObject.RoomingList;
import pages.HomePageForPackageOrg;
import pages.HomePageForScratchOrg;
import pages.LoginPageForPackageOrg;
import pages.LoginPageForScratchOrg;
import java.lang.String;

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
    protected Deposit deposit;
    protected Payment payment;
    protected ChangeResource changeResource;
    protected Files files;
    protected RoomingList roomingList;
    protected Contact contact;
    protected MultiEditProducts multiEditProducts;
    protected MultiEditMeetingRooms multiEditMeetingRooms;
    protected Invoice invoice;
    protected MultiDelete multiDelete;
    protected CreateResourceForm createResourceForm;
    protected QuotePackageLine quotePackageLine;
    protected QuoteBudget quoteBudget;
    protected Rate rate;
    protected RatePrice ratePrice;
    protected CategoryAdjustment categoryAdjustment;
    protected CategoryPrice categoryPrice;
    protected PMSAccount pmsAccount;
    protected PMSBlock pmsBlock;
    protected QuoteHotelRoomPrice quoteHotelRoomPrice;
    protected CreateQuoteHotelRoomComponent createQuoteHotelRoomComponent;
    protected Availability availability;
    protected MAdjustments mAdjustments;
    protected CheckAvailabilitiesComponent checkAvailabilitiesComponent;



    public WebDriver getDriver() {
        return driver;
    }
    public static final String testDataExcelFileName = "testdata.xlsx";


    public String ORG_USERNAME = System.getenv("JAVAUSERNAME");
    public String ORG_PASSWORD = System.getenv("JAVAPASSWORD");
    public String ORG_URL = System.getenv("SF_URL");
    public String SFDX = System.getenv("SFDX");
    public String SFDX_AUTH_URL = System.getenv("AUTH_URL");
    public String ADMIN_USERNAME = System.getenv("ADMINUSERNAME");
    public String ADMIN_PASSWORD = System.getenv("ADMINPASSWORD");
    public String ADMIN_AUTH_URL = System.getenv("ADMIN_AUTH_URL");






    final protected String thynkPackDevOrg = "https://thynkpack-dev-ed.my.salesforce.com/";
    final protected String thynkPackUserName = "rostislav.orel@succraft.com";
    final protected String thynkPackPassword = "Rost1508";
    final protected String thynkPackKey = "3MVG91BJr_0ZDQ4ta_ZwN1EEnfj.OQSJWOBWMPFXclJ22A8oaKqM9KTLdsoSupXX0idQnMIdsI3IweGbsJx6t";
    public String SFDX_AUTH_URL_THYNK = "force://PlatformCLI::5Aep861KhtojOqEEpf1C.laFhN16Pmut38yhtiYoOyrXXUeg8QGP2hZVxy39KizY65ljaqxviEldYCEYtpb1Gi1@thynkpack-dev-ed.my.salesforce.com";








    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        public Statement apply(Statement base, Description description) {
            if (description.getMethodName().toLowerCase().contains("thisTestTakesLonger")) {
                return new FailOnTimeout(base, 600000); //10 minutes = 600000
            } else {
                return new FailOnTimeout(base, 300000); //5 minutes = 300000
            }
        }
    };

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
        deposit = new Deposit(driver);
        payment = new Payment(driver);
        changeResource = new ChangeResource(driver);
        files = new Files(driver);
        roomingList = new RoomingList(driver);
        contact = new Contact(driver);
        multiEditProducts = new MultiEditProducts(driver);
        multiEditMeetingRooms = new MultiEditMeetingRooms(driver);
        invoice = new Invoice(driver);
        multiDelete = new MultiDelete(driver);
        createResourceForm = new CreateResourceForm(driver);
        quotePackageLine = new QuotePackageLine(driver);
        quoteBudget = new QuoteBudget(driver);
        rate = new Rate(driver);
        ratePrice = new RatePrice(driver);
        categoryAdjustment = new CategoryAdjustment(driver);
        categoryPrice = new CategoryPrice(driver);
        pmsAccount = new PMSAccount(driver);
        pmsBlock = new PMSBlock(driver);
        quoteHotelRoomPrice = new QuoteHotelRoomPrice(driver);
        createQuoteHotelRoomComponent = new CreateQuoteHotelRoomComponent(driver);
        availability = new Availability(driver);
        mAdjustments = new MAdjustments(driver);
        checkAvailabilitiesComponent = new CheckAvailabilitiesComponent(driver);
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
