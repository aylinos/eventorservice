//package com.eventor.selenium;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.time.Duration;
//import java.util.concurrent.TimeUnit;
//
//public class EventsTests extends LaunchBrowser{
//    WebDriver driver;
//
//    public void launchBrowser()
//    {
//        super.launchBrowser_Chrome();
//    }
//
//    public void setDriver()
//    {
//        this.driver = super.getDriver();
//    }
//
//    public void closeBrowser()
//    {
//        super.closeBrowser();
//    }
//
//    public void login()
//    {
//        AuthenticationTest authenticationTest = new AuthenticationTest();
//        authenticationTest.login_validCredentialsProvided(this.driver);
//    }
//
//    public void requestAddEvent()
//    {
//        driver.findElement(By.id("add-event")).click();
//    }
//
//    public void addEvent_validInputsProvided()
//    {
//        ExplicitWait_Id(driver, "title");
////        driver.findElement(By.id("title")).clear();
//        driver.findElement(By.id("title")).sendKeys("test");
////        driver.findElement(By.id("information")).clear();
//        driver.findElement(By.id("information")).sendKeys("Selenium");
//        driver.findElement(By.xpath("//button[contains(text(),'Create')]")).click();
//    }
//
//    public void addEvent_emptyTitleProvided()
//    {
//        ExplicitWait_Id(driver, "title");
//        driver.findElement(By.xpath("//button[contains(text(),'Create')]")).click();
//    }
//
//    public void getListOfEvents()
//    {
//        driver.findElement(By.id("events-link")).click();
//    }
//
//    public void viewEventInfo_selectedByTitle()
//    {
//        WebElement ele = driver.findElement(By.xpath("//li[contains(text(),'test')]"));
//        JavascriptExecutor executor = (JavascriptExecutor)driver;
//        executor.executeScript("arguments[0].click();", ele);
//    }
//
//    public void requestEditEvent()
//    {
//        WebElement ele = driver.findElement(By.xpath("//a[contains(text(),'Edit')]"));
//        JavascriptExecutor executor = (JavascriptExecutor)driver;
//        executor.executeScript("arguments[0].click();", ele);
//    }
//
//    public void editEvent_validInputsProvided()
//    {
//        ExplicitWait_Id(driver, "title");
//        driver.findElement(By.id("title")).clear();
//        driver.findElement(By.id("title")).sendKeys("bla");
//        driver.findElement(By.id("information")).clear();
//        driver.findElement(By.id("information")).sendKeys("Selenium");
//        driver.findElement(By.xpath("//button[contains(text(),'Update')]")).click();
//    }
//
//    public void editEvent_emptyTitleProvided () throws InterruptedException
//    {
//        ExplicitWait_Id(driver, "title");
//        Thread.sleep(2000);
//        driver.findElement(By.id("title")).clear();
//        driver.findElement(By.id("title")).sendKeys("a");
//        driver.findElement(By.id("title")).sendKeys(Keys.BACK_SPACE);
//        Thread.sleep(3000);
//        driver.findElement(By.id("information")).clear();
//        driver.findElement(By.id("information")).sendKeys("Selenium");
//        Thread.sleep(3000);
//        driver.findElement(By.xpath("//button[contains(text(),'Update')]")).click();
//        Thread.sleep(3000);
//    }
//
//    public void deleteEvent()
//    {
//        ExplicitWait_Xpath(driver, "//button[contains(text(),'Delete')]");
//        driver.findElement(By.xpath("//button[contains(text(),'Delete')]")).click();
//    }
//
//    @Test
//    public void workflow_addEvent_validInputsProvided()
//    {
//        launchBrowser();
//        setDriver();
//        login();
//        ExplicitWait_Id(driver, "add-event");
//        requestAddEvent();
//        addEvent_validInputsProvided();
//    }
//
//    @Test
//    public void workflow_addEvent_emptyTitleProvided()
//    {
//        launchBrowser();
//        setDriver();
//        login();
//        ExplicitWait_Id(driver, "add-event");
//        requestAddEvent();
//        addEvent_emptyTitleProvided();
//    }
//
//    @Test
//    public void workflow_getListOfEvents()
//    {
//        launchBrowser();
//        setDriver();
//        login();
//        ExplicitWait_Id(driver, "events-link");
//        getListOfEvents();
//    }
//
//    @Test
//    public void workflow_viewEventInfo_selectedByTitle()
//    {
//        launchBrowser();
//        setDriver();
//        login();
//        ExplicitWait_Id(driver, "events-link");
//        getListOfEvents();
//        ExplicitWait_Xpath(driver, "//li[contains(text(),'test')]");
//        viewEventInfo_selectedByTitle();
//    }
//
//    @Test
//    public void workflow_editEvent_validInputsProvided()
//    {
//        launchBrowser();
//        setDriver();
//        login();
//        ExplicitWait_Id(driver, "events-link");
//        getListOfEvents();
//        ExplicitWait_CssSelector(driver, ".list-group-item:nth-child(1)");
//        viewEventInfo_selectedByTitle();
//        ExplicitWait_Xpath(driver, "//a[contains(text(),'Edit')]");
//        requestEditEvent();
//        editEvent_validInputsProvided();
//    }
//
//    @Test
//    public void workflow_editEvent_emptyTitleProvided() throws InterruptedException
//    {
//        launchBrowser();
//        setDriver();
//        login();
//        ExplicitWait_Id(driver, "events-link");
//        getListOfEvents();
//        ExplicitWait_CssSelector(driver, ".list-group-item:nth-child(1)");
//        viewEventInfo_selectedByTitle();
//        ExplicitWait_Xpath(driver, "//a[contains(text(),'Edit')]");
//        requestEditEvent();
//        editEvent_emptyTitleProvided();
//    }
//
//    @Test
//    public void workflow_deleteEvent()
//    {
//        launchBrowser();
//        setDriver();
//        login();
//        ExplicitWait_Id(driver, "events-link");
//        getListOfEvents();
//        ExplicitWait_CssSelector(driver, ".list-group-item:nth-child(1)");
//        viewEventInfo_selectedByTitle();
//        ExplicitWait_Xpath(driver, "//a[contains(text(),'Edit')]");
//        requestEditEvent();
//        deleteEvent();
//    }
//
//    public static void ExplicitWait_Id(WebDriver driver, String text){
//        (new WebDriverWait(driver, 3)).until(ExpectedConditions
//                .elementToBeClickable(By.id(text)));
//    }
//
//    public static void ExplicitWait_Text(WebDriver driver, String text){
//        (new WebDriverWait(driver, 3)).until(ExpectedConditions
//                .elementToBeClickable(By.linkText(text)));
//    }
//
//    public static void ExplicitWait_Xpath(WebDriver driver, String text){
//        (new WebDriverWait(driver, 3)).until(ExpectedConditions
//                .elementToBeClickable(By.xpath(text)));
//    }
//
//    public static void ExplicitWait_CssSelector(WebDriver driver, String text){
//        (new WebDriverWait(driver, 3)).until(ExpectedConditions
//                .elementToBeClickable(By.cssSelector(text)));
//    }
//}
