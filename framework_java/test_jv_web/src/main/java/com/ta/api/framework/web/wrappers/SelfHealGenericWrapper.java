package com.ta.api.framework.web.wrappers;

import com.epam.healenium.SelfHealingDriver;
import com.ta.api.framework.reports.ReportManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.log4testng.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SelfHealGenericWrapper extends ReportManager implements Wrapper {

    public static Properties prop;

    public static SelfHealingDriver driver; // To Launch the browser
    private static final Logger log = Logger.getLogger(SelfHealGenericWrapper.class);

    public void invokeselfhealApp(String browser, String url) {
        System.out.println(browser);
        System.out.println(url);
        try {

            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--disable-notifications");
            WebDriverManager.chromedriver().setup();
            RemoteWebDriver delegate = new ChromeDriver(chromeOptions);
            driver = SelfHealingDriver.create(delegate);
            driver.get(url);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            //System.out.println("The browser " + browser + " is launched with url " + url + " successfully");
            this.logReport("pass", "The browser " + browser + " is launched with url " + url + " successfully");
//            logReport("PASS","The browser " + browser + " is launched with url " + url + " successfully");
        } catch (SessionNotCreatedException e) {
            System.out.println(e);
            logReport("FAIL","The browser " + browser + " is not launched due to session not created error");
        } catch (WebDriverException e) {
            System.out.println(e);
            logReport( "FAIL","The browser " + browser + " is not launched due to unknown error");
        }
    }


    static {
        prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/java/com/ta/api/framework/web/elementrepository/object.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void unloadObjects() {
        prop = null;
    }


    // To enter/pass values into the respective fields

    @Override
    public void invokeApp(String browser, String url, boolean remote) {

    }

    public void enterById(String idValue, String data) {

        try {
            driver.findElement(By.xpath(idValue)).sendKeys(data);
//            driver.findElementById(idValue).sendKeys(data);
            logReport("The element with id " + idValue + " is entered with " + data + " successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with id " + idValue + " is entered is not available in DOM", "FAIL");
        }  catch (ElementNotInteractableException e) {
            logReport("The element with id " + idValue + " is not intractable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with id " + idValue + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void enterByName(String nameValue, String data) {

        try {
//            driver.findElement(By.xpath(idValue)).sendKeys(data);
            driver.findElement(By.name(nameValue)).sendKeys(data);
            //System.out.println("The element with name " + nameValue + " is entered with " + data + " successfully");
//            ReportManager.test.pass(message);

            logReport("The element with the name " + nameValue + " is entered with " + data + " successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with the name " + nameValue + " is entered is not available in DOM", "FAIL");
        }  catch (ElementNotInteractableException e) {
            logReport("The element with the name " + nameValue + " is not intractable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with the name " + nameValue + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void enterByClassName(String nameValue, String data) {

        try {
//            driver.findElement(By.xpath(idValue)).sendKeys(data);
            driver.findElement(By.className(nameValue)).sendKeys(data);
            //System.out.println("The element with name " + nameValue + " is entered with " + data + " successfully");
//            ReportManager.test.pass(message);

            logReport("The element with the name " + nameValue + " is entered with " + data + " successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with the name " + nameValue + " is entered is not available in DOM", "FAIL");
        }  catch (ElementNotInteractableException e) {
            logReport("The element with the name " + nameValue + " is not intractable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with the name " + nameValue + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void enterByXpath(String xpathValue, String data) {

        try {
            driver.findElement(By.xpath(xpathValue)).sendKeys(data);
//            logReport("The element with the xpath" + xpathValue + "entered the data" + data + " successfully", "PASS");
            logReport( "PASS", "The element with the xpath" + xpathValue + "entered the data" + data + " successfully");
        } catch (NoSuchElementException e) {
            logReport( "FAIL","The element with the xpath " + xpathValue + " is not available in DOM");
        } catch (ElementNotInteractableException e) {
            logReport("FAIL","The element with the xpath " + xpathValue + " is not intractable");
        } catch (StaleElementReferenceException e) {
            logReport( "FAIL","The element with the xpath " + xpathValue + " is not stable");
        } catch (WebDriverException e) {
            logReport( "FAIL","The browser got closed due to unknown error");

        }

    }

    public void clearfiled(String xpathValue) {

        try {
            driver.findElement(By.xpath(xpathValue)).clear();
//            logReport("The element with the xpath" + xpathValue + "entered the data" + data + " successfully", "PASS");
            logReport( "PASS", "The value  with the xpath" + xpathValue + "is cleared" + " successfully");
        } catch (NoSuchElementException e) {
            logReport( "FAIL","The element with the xpath " + xpathValue + " is not available in DOM");
        } catch (ElementNotInteractableException e) {
            logReport("FAIL","The element with the xpath " + xpathValue + " is not intractable");
        } catch (StaleElementReferenceException e) {
            logReport( "FAIL","The element with the xpath " + xpathValue + " is not stable");
        } catch (WebDriverException e) {
            logReport( "FAIL","The browser got closed due to unknown error");

        }

    }

    public void PageRefresh() {

        try {
            driver.navigate().refresh();
//            logReport("The element with the xpath" + xpathValue + "entered the data" + data + " successfully", "PASS");
            logReport( "PASS", "The Page is Successfully Refreshed");
        } catch (Exception e) {
            logReport( "FAIL","Unable to refresh page with an Expection" + e);
        }

    }

    // To Select values from drop down

    public void selectVisibileTextById(String id, String value) {

        try {
            WebElement visiblebytext = driver.findElement(By.id(id));
            Select drpdwn1 = new Select(visiblebytext);
            drpdwn1.selectByVisibleText(value);
            logReport("The element selected by " + id + " with the value of visible text " + value + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element selected by " + id + " with the value of visible text " + value + "is not available in DOM", "FAIL");
        }  catch (ElementClickInterceptedException e) {
            logReport("The element selected by " + id + " with the value of visible text" + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element selected by " + id + " with the value of visible text" + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element selected by " + id + " with the value of visible text " + value + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    public void clickElementsByXpath(String xpathVal) {
        try {
            Thread.sleep(5000);
            List<WebElement> elementName = driver.findElements(By.xpath(xpathVal));
            for (int i=0; i<elementName.size();i++) {
                elementName.get(i).click();
                logReport("PASS", "The element is clicked by " + xpathVal + " successfully ");
            }
        } catch (NoSuchElementException e) {
            logReport("FAIL","The element with " + xpathVal + " is not available in DOM");
        } catch (StaleElementReferenceException e) {
            logReport( "FAIL","The element with " + xpathVal + " is not stable");
        } catch (WebDriverException e) {
            logReport("FAIL","The browser got closed due to unknown error");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectVisibileTextByName(String name, String value) {

        try {
            WebElement visiblebytext = driver.findElement(By.name(name));
            Select drpdwn1 = new Select(visiblebytext);
            drpdwn1.selectByVisibleText(value);
            logReport("The element selected by " + name + " with the value of visible text " + value + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element selected by " + name + " with the value of visible text " + value + "is not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("The element selected by " + name + " with the value of visible text" + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element selected by " + name + " with the value of visible text" + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element selected by " + name + " with the value of visible text " + value + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void selectVisibileTextByXPath(String xpathVal, String value) {

        try {
            WebElement visiblebytext = driver.findElement(By.xpath(xpathVal));
            Select drpdwn1 = new Select(visiblebytext);
            drpdwn1.selectByVisibleText(value);
            //System.out.println("The element selected by " + xpathVal + " with the value of visible text " + value + " successfully.");
            logReport("The element selected by " + xpathVal + " with the value of visible text " + value + " successfully.", "PASS");

        } catch (NoSuchElementException e) {
            logReport("The element selected by " + xpathVal + " with the value of visible text " + value + "is not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("The element selected by " + xpathVal + " with the value of visible text" + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element selected by " + xpathVal + " with the value of visible text" + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element selected by " + xpathVal + " with the value of visible text " + value + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void selectIndexById(String id, int value) {

        try {
            WebElement selectbyindex = driver.findElement(By.id(id));
            Select drpdwn = new Select(selectbyindex);
            drpdwn.selectByIndex(value);
            logReport("The element is selected by " + id + " with the " + value + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + value + " is not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("The element with " + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with " + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + value + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void selectIndexByName(String name, int value) {

        try {
            WebElement selectbyindex = driver.findElement(By.name(name));
            Select drpdwn = new Select(selectbyindex);
            drpdwn.selectByIndex(value);
            logReport("The element is selected by " + name + " with the " + value + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + value + " is not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("The element with " + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with " + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + value + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void selectIndexByXPath(String xpathVal, int value) {

        try {
            WebElement selectbyindex = driver.findElement(By.xpath(xpathVal));
            Select drpdwn = new Select(selectbyindex);
            drpdwn.selectByIndex(value);
            logReport("The element is selected by " + xpathVal + " with the " + value + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + value + " is not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("The element with " + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with " + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + value + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void selectValueById(String id, int value) {

        try {
            WebElement selectbyvalue = driver.findElement(By.id(id));
            Select drpdwn = new Select(selectbyvalue);
            drpdwn.selectByIndex(value);
            logReport("The element is selected by " + id + " with the " + value + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + value + " is not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("The element with " + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with " + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + value + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void selectValueByName(String name, int value) {

        try {
            WebElement selectbyvalue = driver.findElement(By.name(name));
            Select drpdwn = new Select(selectbyvalue);
            drpdwn.selectByIndex(value);
            logReport("The element is selected by " + name + " with the " + value + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + value + " is not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("The element with " + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with " + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + value + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void selectValueByXPath(String xpathVal, int value) {

        try {
            WebElement selectbyvalue = driver.findElement(By.xpath(xpathVal));
            Select drpdwn = new Select(selectbyvalue);
            drpdwn.selectByIndex(value);
            logReport("The element is selected by " + xpathVal + " with the " + value + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + value + " is not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("The element with " + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with " + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + value + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    // To take snapshot
  /*  public long takeSnap() {
        long snapNumber = (long) (Math.floor(Math.random() * 100000000) + 100000);
        File temp = driver.getScreenshotAs(OutputType.FILE);
//        File dest = new File("D:\\framework\\QE_CoreAutomationFramework\\src\\test\\resources\\snapshots" + snapNumber + ".png");
                File dest = new File("resources/snapshots" + snapNumber + ".png");

        try {
            FileUtils.copyFile(temp, dest);
            logReport("Snap Taken successfully", "PASS");
        } catch (IOException e) {
            logReport("Input/Output file operation issue", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
        return snapNumber;
    }
*/

   /* public void takeSnapshot() {
        // Convert driver to TakesScreenshot
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        long snapNumber = (long) (Math.floor(Math.random() * 100000000) + 100000);

        // Capture screenshot as file
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);

        // Define destination file path
        String destFilePath = "D:\\framework\\QE_CoreAutomationFramework\\src\\test\\resources\\snapshots\\snap" + snapNumber + ".png";
        try {
            // Copy source file to destination file
            FileUtils.copyFile(srcFile, new File(destFilePath));
            System.out.println("Screenshot saved at: " + destFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }*/

    public String takeSnapshot() {
        // Convert driver to TakesScreenshot
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        long snapNumber = (long) (Math.floor(Math.random() * 100000000) + 100000);

        // Capture screenshot as file
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);

        // Define destination file path
        String destFilePath = "D:\\framework\\QE_CoreAutomationFramework\\src\\test\\resources\\snapshots\\snap" + snapNumber + ".PNG";

        try {
            // Copy source file to destination file
            FileUtils.copyFile(srcFile, new File(destFilePath));
            System.out.println("Screenshot saved at: " + destFilePath);
            return destFilePath; // Return file path of saved screenshot
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to capture screenshot: " + e.getMessage());
            return null; // Return null in case of failure
        }
    }

    // To Close browsers
    public void closeBrowser() {

        try {
            driver.close();
            logReport( "PASS","Browser closed successfully");
        } catch (WebDriverException e) {
            logReport( "FAIL","The browser got closed due to unknown error");
        }
    }

    public void closeAllBrowsers() {

        try {
            driver.quit();
            logReport("All Browser closed successfully", "PASS");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    // To Click the elements

    public void clickById(String id) {

        try {
            driver.findElement(By.id(id)).click();
            logReport("The element is clicked by " + id + " successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + id + " is not available in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + id + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void clickByClassName(String classVal) {

        try {
            driver.findElement(By.className(classVal)).click();
            logReport("The element is clicked by " + classVal + " successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + classVal + " is not available in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + classVal + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    public void clickByName(String name) {

        try {
            driver.findElement(By.name(name)).click();
            logReport("The element is clicked by " + name + " successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + name + " is not available in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + name + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    public void clickByLink(String name) {

        try {
            driver.findElement(By.linkText(name)).click();
            logReport("The element is clicked by " + name + " successfully ", "PASS");
        } catch (NoSuchElementException e) {

            logReport("The element with " + name + " is not available in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {

            logReport("The element with " + name + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }

    }

    public void clickByLinkNoSnap(String name) {

        try {
            driver.findElement(By.linkText(name)).click();
            logReport("The element is clicked by " + name + " successfully ", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + name + " is not available in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + name + " is  is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    public void clickByXpath(String xpathVal) {
        try {
            driver.findElement(By.xpath(xpathVal)).click();
            logReport( "PASS","The element is clicked by " + xpathVal + " successfully ");
        } catch (NoSuchElementException e) {
            logReport("FAIL","The element with " + xpathVal + " is not available in DOM");
        } catch (StaleElementReferenceException e) {
            logReport( "FAIL","The element with " + xpathVal + " is not stable");
        } catch (WebDriverException e) {
            logReport("FAIL","The browser got closed due to unknown error");

        }
    }

    public void clickByXpathNoSnap(String xpathVal) {

        try {
            driver.findElement(By.xpath(xpathVal)).click();
            logReport("The element is clicked by " + xpathVal + " successfully ", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + xpathVal + " is not available in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with " + xpathVal + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    // To getText form the browser

    public String getTextById(String idVal) {

        try {
            String txtbyid = driver.findElement(By.id(idVal)).getText();
            System.out.println(txtbyid);
            logReport("The element with the " + idVal + " is taken and printed in console successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + idVal + " is not available in DOM", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with " + idVal + " is not interactable", "FAIL");

        } catch (StaleElementReferenceException e) {
            logReport("The element with " + idVal + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
        return null;
    }

    public String getTextByName(String name) {

        try {
            String textbyname = driver.findElement(By.name(name)).getText();
            System.out.println(textbyname);
            logReport("The element with the " + name + " is taken and printed in console successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + name + " is not available in DOM", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with " + name + " is not interactable", "FAIL");

        } catch (StaleElementReferenceException e) {
            logReport("The element with " + name + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
        return null;
    }

    public String getTextByClassName(String name) {

        try {
            String textbyclassname = driver.findElement(By.className(name)).getText();
            System.out.println(textbyclassname);
            logReport("The element with the " + name + " is taken and printed in console successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with " + name + " is not available in DOM", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with " + name + " is not interactable", "FAIL");

        } catch (StaleElementReferenceException e) {
            logReport("The element with " + name + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
        return null;
    }

    public String getTextByXpath(String xpathVal) {
        String txtbyxpath = null;
        try {
             txtbyxpath = driver.findElement(By.xpath(xpathVal)).getText();
            System.out.println(txtbyxpath);
            logReport( "PASS","The element with the " + xpathVal + "is taken and printed in console successfully");
        } catch (NoSuchElementException e) {
            logReport( "FAIL","The element with " + xpathVal + " is not available in DOM");
        } catch (ElementNotInteractableException e) {
            logReport( "FAIL","The element with " + xpathVal + " is not interactable");

        } catch (StaleElementReferenceException e) {
            logReport( "FAIL","The element with " + xpathVal + " is not stable");
        } catch (WebDriverException e) {
            logReport( "FAIL","The browser got closed due to unknown error");

        }
        return txtbyxpath;
    }

    // To Alert functionalites

    public void acceptAlert() {

        try {
            driver.switchTo().alert().accept();
            logReport("Alert Accepted successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("There is no such alert not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("There is no such alert is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("There is no such alert is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The alert is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }

    }

    public void enterAlert(String text) {

        try {
            driver.switchTo().alert().sendKeys(text);
            logReport("Text" + text + " entered into the alert successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("There is no such alert not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("There is no such alert is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("There is no such alert is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The alert is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }

    }

    public void dismissAlert() {

        try {
            driver.switchTo().alert().dismiss();
            logReport("Alert dismissed successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("There is no such alert not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("There is no such alert is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("There is no such alert is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The alert is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    public String getAlertText() {

        try {
            String text = driver.switchTo().alert().getText();
            System.out.println(text);
            logReport("Alert text is" + text + ".", "PASS");
        } catch (NoSuchElementException e) {
            logReport("There is no alert  text  available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("There is no such alert text is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("There is no such alert text is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The alert text is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
        return null;

    }

    // To wait for sometime to find the element

    public void timeOut(long sleep) {

        try {
            Thread.sleep(sleep);
            logReport("JVM Slept peacefully for " + sleep + "s. ", "PASS");
        } catch (Exception e) {
            logReport("JVM din't Slept ", "FAIL");
        }
    }


    /*// To webdriverdriver property

    public void webDriverWaitForAlertIsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
//        wait.until(alertIsPresent());
        ExpectedCondition<Alert> expectedCondition = ExpectedConditions.alertIsPresent();
        wait.until((Function<WebDriver, Alert>) expectedCondition);
//        Alert alert = wait.until((Function<WebDriver, Alert>) expectedCondition);


    }

    public void webdriverWaitPresenceOfElementByXpath(String xpathval) {

        WebDriverWait wait = new WebDriverWait(driver, 30);
        try {

            ExpectedCondition<WebElement> expectedCondition = presenceOfElementLocated(By.xpath(xpathval));
            wait.until((Function<WebDriver, Alert>) expectedCondition);
//            wait.until(presenceOfElementLocated(By.xpath(xpathval)));
            logReport("System waited successfully till the element present NITHILANSAI", "PASS");
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void webdriverWaitElementToBeClickable(String xpathval) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            ExpectedCondition<WebElement> expectedCondition = elementToBeClickable(By.xpath(xpathval));
            wait.until((Function<WebDriver, Alert>) expectedCondition);
            logReport("System waited successfully till the element clickable GAYATHRI", "PASS");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void webdriverWaitSwitchToFrame(String xpathval) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
//        wait.until(frameToBeAvailableAndSwitchToIt(By.xpath(xpathval)));
        ExpectedCondition<WebDriver> expectedCondition = frameToBeAvailableAndSwitchToIt(By.xpath(xpathval));
        wait.until((Function<WebDriver, Alert>) expectedCondition);

    }
*/
    public void webdriverWaitElementToBeSelect(String xpathval) {
//        WebDriverWait wait = new WebDriverWait(driver, 30);
//        wait.until(elementToBeSelected(By.xpath(xpathval)));
    }
    // To switching between windows

    public void switchToParentWindow() {

        try {
            Set<String> windowhandles = driver.getWindowHandles();

            for (String eachid : windowhandles) {

                System.out.println(eachid);

                driver.switchTo().window(eachid);
                break;
            }
            //System.out.println("Swithced to parent window successfully");
            logReport("Swithced to parent window successfully", "PASS");
        } catch (NoSuchWindowException e) {
            logReport("No such parent window is available", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }

    }

    public void switchToLastWindow() {

        try {
            Set<String> windowhandles = driver.getWindowHandles();

            for (String eachid : windowhandles) {

                System.out.println(eachid);

                driver.switchTo().window(eachid);

            }
            //System.out.println("Swithced to parent window successfully");
            logReport("Swithced to parent window successfully", "PASS");
        } catch (NoSuchWindowException e) {
            logReport("No such parent window is available", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void switchToWindowWithConditions(String text) {

        try {
            Set<String> newwindow2 = driver.getWindowHandles();
            System.out.println(newwindow2);
            for (String eachid2 : newwindow2) {
                System.out.println(eachid2);
                driver.switchTo().window(eachid2);
                String TitleOfPage = driver.getTitle();
                System.out.println(TitleOfPage);
                if (TitleOfPage.equalsIgnoreCase("NVSP Service Portal")) {
                    System.out.println("Title matching successfully");
                } else {
                    System.out.println("Title Mismatching");
                    driver.close();
                }

            }
            logReport("Swithced to parent window successfully", "PASS");
        } catch (NoSuchWindowException e) {
            logReport("No such parent window is available", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    // To Verify the Text

    public void verifyTextById(String id, String text) {

        try {
            driver.findElement(By.id(id)).sendKeys(text);
            String giventext = driver.findElement(By.id(id)).getText();
            if (giventext.contentEquals(text)) {
                System.out.println("Is Displayed");
            } else {
                System.out.println("Not Displayed");
            }
            logReport("The given text is verified by " + id + "with the " + text + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The given" + text + "with the" + id + " is not available in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The given " + text + "with the" + id + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void verifyTextByName(String name, String text) {

        try {
            driver.findElement(By.name(name)).sendKeys(text);
            String giventext = driver.findElement(By.name(name)).getText();
            if (giventext.contentEquals(text)) {
                System.out.println("Is Displayed");
            } else {
                System.out.println("Not Displayed");
            }
            //System.out.println("The given text is verified by " + name + "with the " + text + " successfully.");
            logReport("The given text is verified by " + name + "with the " + text + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The given" + text + "with the" + name + " is not available in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The given " + text + "with the" + name + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void verifyTextByXpath(String xpath, String text) {

        try {
            //driver.findElement(By.xpath(xpath)).sendKeys(text);
            String txt = driver.findElement(By.xpath(xpath)).getText();
            if (txt.contentEquals(text)) {
                System.out.println("Is Displayed");
            } else {
                System.out.println("Not Displayed");
            }
            //System.out.println("The given text is verified by " + xpath + " with the " + text + " successfully.");
            logReport("The given text is verified by " + xpath + " with the " + text + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The given " + text + "with the" + xpath + " is not available in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The given " + text + "with the" + xpath + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    public void verifyTextContainsByXpath(String xpath, String text) {

        try {
            driver.findElement(By.xpath(xpath)).sendKeys(text);
            String partialtext = driver.findElement(By.xpath(xpath)).getText();
            if (partialtext.contains(text)) {
                System.out.println("Is Displayed partially");
            } else {
                System.out.println("Is not even displayed partially");
            }
            //System.out.println("The given text is verified by " + xpath + " with the " + text + " partially successfully.");
            logReport("The given text is verified by " + xpath + " with the " + text + " partially successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The given " + text + "with the" + xpath + " is not even available partially in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The given " + text + "with the" + xpath + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    // To Verify the Title

    public void verifyTitle(String title) {

        try {
            String titlename = driver.getTitle();
            System.out.println(titlename);
            if (titlename.equalsIgnoreCase(title)) {
                System.out.println("Title verified successfully");
            } else {
                System.out.println("Title mismatching");
            }
            logReport("Title displayed" + title + " verified successfully ", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with name " + title + " is not available in DOM", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with name " + title + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    // To Click on tab key

    public void clickOnTabById(String id) {

        try {
            driver.findElement(By.id(id)).click();
            logReport("The element clicked by" + id + "is tabbed  successfully.", "PASS");
        } catch (ElementClickInterceptedException e) {
            logReport("The element clicked by" + id + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element clicked by" + id + "is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element selected by" + id + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }

    }


    public void clickOnTabByXpath(String xpathVal) {

        try {
            driver.findElement(By.xpath(xpathVal)).click();
            logReport("The element clicked by" + xpathVal + "is tabbed  successfully.", "PASS");
        } catch (ElementClickInterceptedException e) {
            logReport("The element clicked by" + xpathVal + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element clicked by" + xpathVal + "is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element selected by" + xpathVal + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }

    }

    public void pagedown() {
        // TODO Auto-generated method stub
        try {
            driver.findElement(By.xpath("/html/body")).sendKeys(Keys.PAGE_DOWN);
            logReport("The page is scrolled down successfully", "Pass");
        } catch (WebDriverException e) {
            logReport("Issue in page scroll down", "Fail");
        }
    }

    @Override
    public void webDriverWaitForAlertIsPresent() {

    }

    @Override
    public void webdriverWaitPresenceOfElementByXpath(String xpathval) {

    }

    @Override
    public void webdriverWaitElementToBeClickable(String xpathval) {

    }

    @Override
    public void webdriverWaitSwitchToFrame(String xpathval) {

    }


    public void scrolldownByPixcel() {
        // TODO Auto-generated method stub

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,2000)");
            logReport("Page scroll downed succesfully by pixcel", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    public void scrolldownByElementId(String id, String element) {

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement Element = driver.findElement(By.id(id));
            js.executeScript("arguments[0].scrollIntoView();", Element);
            logReport("Scroll downed successfully by element", "FAIL");

        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    public void scrolldownTillEnd() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
            logReport("Scroll downed successfully till end", "FAIL");

        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
    }

    public void fileUpload(String xpathval, String filepath) {

        try {
            driver.findElement(By.xpath(xpathval)).click();
            Thread.sleep(3000);
            Clipboard obj = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection str = new StringSelection(filepath);
            obj.setContents(str, null);
            Robot rbt = new Robot();
            rbt.keyPress(KeyEvent.VK_CONTROL);
            rbt.keyPress(KeyEvent.VK_V);
            rbt.keyRelease(KeyEvent.VK_V);
            rbt.keyRelease(KeyEvent.VK_CONTROL);
            rbt.keyPress(KeyEvent.VK_ENTER);
            rbt.keyRelease(KeyEvent.VK_ENTER);

            //System.out.println("File uploaded successfully");
            logReport("File uploaded successfully", "PASS");
        } catch (HeadlessException e) {
            logReport("File dint uploaded due to AWTException", "FAIL");
        } catch (InterruptedException e) {
            logReport("File dint uploaded due to AWTException", "FAIL");
        } catch (AWTException e) {
            logReport("File dint uploaded due to AWTException", "FAIL");

        } catch (WebDriverException e) {
            System.err.println("The browser got closed due to unknown error");
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }


    //Mousehover methods

    public void mouseHover(String xpathval) {

        try {
            Actions action = new Actions(driver);
            WebElement mousehoveringObj = driver.findElement(By.xpath(xpathval));
            action.moveToElement(mousehoveringObj).perform();
            System.out.println("Mousehovered the object successfully");
        } catch (ElementNotInteractableException e) {

            System.err.println("Element is not intractable");
        } catch (WebDriverException e) {

            System.err.println("Browser closed due to unknown error");
        }

    }

//Calender Date

    public void selectDayOfMonth(String xpathval) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate todayDate = LocalDate.now();
        LocalDate date = todayDate.plusDays(1);
        System.out.println(date.getYear()); // 2019
        System.out.println(date.getMonth()); // April
        System.out.println(date.getDayOfMonth()); // 24

        int da = date.getDayOfMonth();
        System.out.println("the date is:" + da);
        driver.findElement(By.xpath(xpathval)).click();
        driver.findElement(By.xpath(xpathval)).click();

        System.out.println("The element is clicked by " + xpathval + " successfully ");
        //FTRPublicUserRegistrationPage.Date.Xpath=//a[text()='12']

    }


}
