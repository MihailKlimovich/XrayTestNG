package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class BasePage {
    public WebDriver driver;
    public WebDriverWait wait1;
    public WebDriverWait wait2;
    public Wait waitForTests;
    public SoftAssert softAssert = new SoftAssert();
    public By popupToastMessageContent = By.xpath("//span[@class='toastMessage slds-text-heading--small forceActionsText']");
    public By spinner = By.xpath("//lightning-spinner");


    /**Constructor*/
    public BasePage(WebDriver driver){
        this.driver = driver;
        wait1 = new WebDriverWait(driver, 10);
        wait2 = new WebDriverWait(driver, 1);
        waitForTests = new FluentWait(this.driver)
                .withTimeout(Duration.ofSeconds(2))
                .pollingEvery(Duration.ofSeconds(5));
    }

    /**Navigation Methods*/
    @Step("Refresh Page")
    public void refreshPage() {
        driver.navigate().refresh();
    }


    /** Click Methods*/
    //Click
    public void click(By elementLocation) throws InterruptedException {
        int attempts = 0;
        WebElement element = wait1.until(visibilityOfElementLocated(elementLocation));
        while (attempts < 2) {
            try {
                element.click();
                System.out.println(" Click  "+(elementLocation));
                break;
            }
            catch (JavascriptException | ElementClickInterceptedException e) {
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", element);
                System.out.println("JavascriptException. Action click "+(elementLocation));
                break;
            }
            catch (StaleElementReferenceException e) {
                System.out.println("StaleElementReferenceException "+(elementLocation));
            }
            attempts++;
        }
    }



    //Double click
    public void doubleClick(By elementLocation) {
        Actions action = new Actions(driver);
        action.doubleClick(driver.findElement(elementLocation)).build().perform();
    }

    //Click invisible element
    public void clickInvisibleElement(By elementLocation) {
        WebElement element = driver.findElement(elementLocation);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    //ActionClick
    @Step("Click Action")
    public void actionClick(By elementLocation) {
        WebElement element = driver.findElement((elementLocation));
        Actions action = new Actions(driver);
        action.moveToElement(element).click(element).perform();
    }

    public void checkbox(By elementLocation, By elementLocation2) {
        WebElement element = driver.findElement((elementLocation));
        WebElement element2 = driver.findElement((elementLocation2));
        if (element2.isDisplayed()){
            element.click();
        }
        else{
            System.out.println("CheckBox "+(elementLocation)+" is selected");
        }
    }
    public void click2(By elementLocation) throws InterruptedException {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        int attempts = 0;
        while (attempts < 2) {
            try {
                waitForTests.until(visibilityOfElementLocated(elementLocation));
                driver.findElement(elementLocation).click();
                System.out.println(" Click  "+(elementLocation));
                break;
            }
            catch (Exception e){
                System.out.println("Element is not available to click : "+elementLocation);
                e.printStackTrace();
            }
            attempts++;
        }
    }


    /** Buttons  Methods*/
    @Step("Press Backspace")
    public void backspace(By elementLocation) {
        try {
            driver.findElement(elementLocation).sendKeys(Keys.BACK_SPACE);
        } catch (ElementNotInteractableException e){
            ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('type',arguments[1]);", driver.findElement(elementLocation), "text");
            driver.findElement(elementLocation).sendKeys(Keys.BACK_SPACE);
        }
    }

    @Step("Press TAB")
    public void tab() {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.TAB).perform();
    }

    //Ctrl +A
    @Step("Press CTRL+A")
    public void ctrlA() {
        Actions action = new Actions(driver);
        action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0061')).perform();
        action.keyUp(Keys.CONTROL).perform();
    }

    @Step("Press ENTER")
    public void enter() {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ENTER).perform();
    }

    @Step("Press DELETE")
    public void delete() {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.DELETE).build().perform();
    }

    @Step("Press ARROW DOWN")
    public void down() {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ARROW_DOWN).perform();
    }

    @Step("Press ESC")
    public void escape() {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ESCAPE).perform();
    }

    /** Text  Methods*/
    //Send text
    public void sendText(String apexText) {
        Actions action = new Actions(driver);
        action.sendKeys(String.valueOf(apexText)).perform();
    }

    //Select element
    public void selectElement(By elementLocation, String Value) {
        WebElement select = driver.findElement(elementLocation);
        new Select(select).selectByValue(Value);
    }

    //Write Text
    public void writeText(By elementLocation, String text2) {
        try {
            driver.findElement(elementLocation).sendKeys(text2);
            System.out.println("write  "+text2+" to "+(elementLocation));
        }
        catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('type',arguments[1]);", driver.findElement(elementLocation), "text");
            driver.findElement(elementLocation).sendKeys(text2);
            System.out.println("write  "+text2+" to "+(elementLocation));
        }
    }

    //Write Number
    public void writeNumber(By elementLocation, String text2) {
        try {
            driver.findElement(elementLocation).sendKeys(text2);
            System.out.println("write  "+text2+" to "+(elementLocation));
        }
        catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('type',arguments[1]);", driver.findElement(elementLocation), "number");
            driver.findElement(elementLocation).sendKeys(text2);
            System.out.println("write  "+text2+" to "+(elementLocation));
        }
    }

    //Read Text
    public String readText(By elementLocation){
        return driver.findElement(elementLocation).getText();
    }

    /**
     * Method contains div//span[@class='slds-checkbox_on'] and div//span[@class='slds-checkbox_off'] parts for toggle
     * @param xpathInit is used as initial part for get On/Off values for corresponding Toggle
     * @return true if toggle is set On; false - if toggel is set Off
     */
    public Boolean readToggle(String xpathInit) {
        By elementOn = By.xpath(xpathInit+"div//span[@class='slds-checkbox_on']");
        By elementOff = By.xpath(xpathInit+"div//span[@class='slds-checkbox_off']");
        if(driver.findElement(elementOn).isDisplayed()) {
            System.out.println("Active");
            return true;
        }
        else {
            if(driver.findElement(elementOff).isDisplayed()) {
                System.out.println("Inactive");
                return false;
            }
        }
        return false;
    }

    @Step("Read Checkbox {elementLocation}")
    public Boolean readCheckbox(By elementLocation){
        System.out.println("Checkbox:"+ elementLocation + "is " +driver.findElement(elementLocation));
        return driver.findElement(elementLocation).isSelected();
    }

    public String readAppMessage(By elementLocation){
        System.out.println("Popup: "+driver.findElement(elementLocation).getText());
        return driver.findElement(elementLocation).getText();
    }

    public String readRecalculateMessage(By elementLocation){
        wait2.until(visibilityOfElementLocated(elementLocation));
        return driver.findElement(elementLocation).getText();
    }

    /** Elements  Methods*/
    //Wait Visible
    public void waitVisibility(By by) {
        wait1.until(visibilityOfElementLocated(by));
    }

    //Clear Field
    public void clear(By elementLocation) {
        driver.findElement(elementLocation).clear();

    }

    //Attribute element
    public String readAttribute(By elementLocation, String attribute) {
        return driver.findElement(elementLocation).getAttribute(attribute);
    }

    //Scrolling
    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",element);
    }

    public void scrollToElement2(By elementLocation, By elementLocation2) {
        try {
            WebElement element = driver.findElement((elementLocation));
            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);
        }
        catch (NoSuchElementException e) {
            WebElement element2 = driver.findElement((elementLocation2));
            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element2);
        }
    }



    /** Popup Methods*/
    public Boolean isDisplayed(By elementLocation) {
        try {
            WebElement popup = wait1.until(visibilityOfElementLocated(elementLocation));
            return popup.isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }
    public Boolean checkPopUpContent(String popupMessage) {
        if(popupMessage.contains("must be") || popupMessage.contains("should be")) return false;
        else if(popupMessage.contains("success") || popupMessage.contains("added") || popupMessage.contains("completed") || popupMessage.contains("deleted") || popupMessage.contains("saved")) return true;
        return false;
    }

    //Switch to
    public void switchTo(By elementLocation) {
        driver.switchTo().frame(driver.findElement(elementLocation));
    }

    //Close popup alert window
    public void safeAlertDismiss() {
        try {
            driver.close();
        } catch (NoAlertPresentException e) { }
    }

    /** Assert Methods*/
    //AssertEquals
    public void assertValue(String Text, By elementLocation, String Message) {
        Assert.assertEquals(driver.findElement(elementLocation).getText(), (Text), Message);
    }

    public void assertValueExpectation(String Text, By elementLocation, Boolean expectedResult, String Message) {
        Boolean actual = false;
        if (driver.findElement(elementLocation).getText().equalsIgnoreCase(Text)) { actual = true; }
        Assert.assertEquals(actual, expectedResult, Message);
    }

    public void assertValueByPartText(String Text, By elementLocation, String Message) {
        String s = driver.findElement(elementLocation).getText();
        Message += " contains "+ Text + "; Actual value: " + s;
        Assert.assertTrue(s.contains(Text), Message);
    }

    public void assertValueByPartTextFalse(String Text, String actualValue, String Message) {
        Assert.assertFalse(actualValue.contains(Text), Message);
    }

    @Step("Assert contains")
    public void assertValueByPartText2(String Text, By elementLocation, By elementLocation2, String Message) {
        String s;
        try {
            s = driver.findElement(elementLocation).getText();
            Message += " contains "+ Text + "; Actual value: " + s;
            Assert.assertTrue(s.contains(Text), Message);
        }
        catch (NoSuchElementException e) {
            s = driver.findElement(elementLocation2).getText();
            Message += " contains "+ Text + "; Actual value: " + s;
            Assert.assertTrue(s.contains(Text), Message);
        }
    }

    public void assertPopup(Boolean expected) {
        wait1.until(visibilityOfElementLocated(popupToastMessageContent));
        Assert.assertEquals(checkPopUpContent(readAppMessage(popupToastMessageContent)), expected, "Wrong message type");
    }

    public void assertBoolean(Boolean actual, Boolean expected) {
        Assert.assertEquals(actual, expected, "Wrong value: ");
    }

    //SoftAssert
    public void softAsserts(String Text, By elementLocation, String Message) {
        softAssert.assertEquals(driver.findElement(elementLocation).getText(), (Text), Message);
    }

    public void softAsserts2(String Text, By elementLocation,  By elementLocation2, String Message) {
        try {
            softAssert.assertEquals(driver.findElement(elementLocation).getText(), (Text), Message);
        }
        catch (NoSuchElementException e) {
            softAssert.assertEquals(driver.findElement(elementLocation2).getText(), (Text), Message);
        }
    }

    public void softAssertAll() {
        softAssert.assertAll();
    }

    public void attributeAssert(CharSequence Text, By elementLocation, String Message) {
        softAssert.assertEquals(driver.findElement(elementLocation).getAttribute("max").contains(Text),(Text), Message);
    }

    //Change Attribute
    public void setAttribute(By locator, String attribute, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('" + attribute + "',arguments[1]);", driver.findElement(locator), value);
    }

    //Is Present
    public  Boolean isPresent(By elementLocation) {
        Boolean isPresent = driver.findElements(elementLocation).size() > 0;
        return isPresent;
    }

    public void presentAssert(By elementLocation, String Message) {
        Assert.assertTrue(isDisplayed(elementLocation), Message);
    }



}
