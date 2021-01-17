package com.eventor.selenium;

import org.junit.Before;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LaunchBrowser {
    WebDriver driver;
    
    public void launchBrowser_Chrome()
    {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\HOME\\Documents\\Fontys\\Semester 3\\ITS\\Project\\eventor-service\\eventor\\src\\main\\resources\\chromedriver.exe");
        driver = new ChromeDriver();
//        chromeDriver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.get("http://localhost:8081/");
    }

    public void launchBrowser_Firefox()
    {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\HOME\\Documents\\Fontys\\Semester 3\\ITS\\Project\\eventor-service\\eventor\\src\\main\\resources\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:8081/");
    }

    public void closeBrowser()
    {
        driver.quit();
    }

    public WebDriver getDriver()
    {
        return this.driver;
    }
}
