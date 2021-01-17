package com.eventor.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AuthenticationTest extends LaunchBrowser{
    WebDriver driver;

    public void launchBrowser()
    {
        super.launchBrowser_Chrome();
    }

    public void setDriver()
    {
        this.driver = super.getDriver();
    }

    public void closeBrowser()
    {
        super.closeBrowser();
    }

    public void login_validCredentialsProvided(WebDriver driver)
    {
        driver.findElement(By.id("immerge-item")).click();
        driver.findElement(By.id("input-username")).sendKeys("victor");
        driver.findElement(By.id("input-password")).sendKeys("a12347");
        driver.findElement(By.id("LogIn-button")).submit();
    }

    public void login_invalidUsernameProvided(WebDriver driver)
    {
        driver.findElement(By.id("immerge-item")).click();
        driver.findElement(By.id("input-username")).sendKeys("victorr");
        driver.findElement(By.id("input-password")).sendKeys("a12347");
        driver.findElement(By.id("LogIn-button")).submit();
    }

    public void login_wrongPasswordProvided(WebDriver driver)
    {
        driver.findElement(By.id("immerge-item")).click();
        driver.findElement(By.id("input-username")).sendKeys("victor");
        driver.findElement(By.id("input-password")).sendKeys("a1234");
        driver.findElement(By.id("LogIn-button")).submit();
    }

    public void login_noInputProvided(WebDriver driver)
    {
        driver.findElement(By.id("immerge-item")).click();
        driver.findElement(By.id("input-username")).clear();
        driver.findElement(By.id("input-password")).clear();
        driver.findElement(By.id("LogIn-button")).submit();
    }

    @Test
    public void workflow_login_validCredentialsProvided()
    {
        launchBrowser();
        setDriver();
        login_validCredentialsProvided(this.driver);
        closeBrowser();
    }
//set up tear down
    @Test
    public void workflow_login_invalidUsernameProvided()
    {
        launchBrowser();
        setDriver();
        login_invalidUsernameProvided(this.driver);
        closeBrowser();
    }

    @Test
    public void workflow_login_wrongPasswordProvided()
    {
        launchBrowser();
        setDriver();
        login_wrongPasswordProvided(this.driver);
        closeBrowser();
    }

    @Test
    public void workflow_login_noInputProvided()
    {
        launchBrowser();
        setDriver();
        login_noInputProvided(this.driver);
        closeBrowser();
    }
}
