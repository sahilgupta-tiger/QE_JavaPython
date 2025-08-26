package genericwrappers;

import allurereportGeneration.allureReportGeneration;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.log4testng.Logger;
import reports.ReportManager;
import webutils.LoadProperties;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static database.DBC.getValueFromDatabase;
import static genericwrappers.LocatorTypeIOS.getLocator;

public class GenericWrapper extends ReportManager implements Wrapper {

    public static Properties prop;
    public static allureReportGeneration allure = new allureReportGeneration();

    private static final Logger log = Logger.getLogger(GenericWrapper.class);

    protected WebDriver driver;

    public GenericWrapper(WebDriver driver) {
        this.driver = driver;
    }

    public void validationWithDatabase(Map<String, String> dataDictionary, String expectedValue) {

        String sqlQuery = getSqlQuery(dataDictionary);
        if (sqlQuery != null) {
            String valueFromDatabase = getValueFromDatabase(sqlQuery);

            log.info("Actual Value form database: " + valueFromDatabase); // Debugging print statement
            log.info("Actual and Expected Values are Not Matching: Expected  value to be " + expectedValue + ", and Actual value is " + valueFromDatabase);
            Allure.addAttachment("Resulttest: ", "Actual and Expected Values are Not Matching: Expected  value to be " + expectedValue + ", and Actual value is " + valueFromDatabase);
            assert valueFromDatabase.equals(expectedValue);
        } else {
            throw new RuntimeException("SQL query not found in data dictionary.");
        }
    }

    /*
     * Retrieves the SQL query from the data dictionary.
     * @param dataDictionary A map containing key-value pairs, including the SQL query.
     * @return The SQL query string, or null if not found.
     */
    public String getSqlQuery(Map<String, String> dataDictionary) {
        for (Map.Entry<String, String> entry : dataDictionary.entrySet()) {
            if (entry.getKey().toLowerCase().contains("sqlquery")) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * This method is to get text for the mentioned element
     *
     * @param element the web element for which we need to get the text
     */
    public String getText(WebElement element) {
        String text = "";
        try {
            text = element.getText();
            logReport("Successfully retrieved text from the element", "PASS");
        } catch (Exception e) {
            logReport("Failed to get text of the element: " + e.getMessage(), "FAIL");
            e.printStackTrace();
        }
        return text;
    }

    /**
     * This method waits for the page to be fully loaded by checking the document.readyState property.
     * It continuously executes JavaScript in the browser to ensure that the page's readyState is 'complete'.
     * The method will block until the page has finished loading completely.
     */
    public void waitForPageToLoadUsingJS() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Loop until the page's readyState is 'complete'
        boolean isPageLoaded = false;
        while (!isPageLoaded) {
            try{
                isPageLoaded = (boolean) jsExecutor.executeScript("return document.readyState == 'complete';");
            }catch (Exception e){
                logReport(e.getMessage(),"INFO");
            }
        }
    }

    /**
     * This method waits for a specific WebElement to be visible on the page using JavaScript.
     * It executes JavaScript to check if the element is present in the DOM and visible.
     * The method will repeatedly check the presence of the element until it is visible.
     *
     * @param element The WebElement to wait for.
     * @param timeoutInSeconds The maximum time to wait for the element to be visible (in seconds).
     */
    public void waitForElementToBeVisibleUsingJS(WebElement element, int timeoutInSeconds) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        long startTime = System.currentTimeMillis();
        boolean isElementVisible = false;

        // Loop until the element is visible or the timeout is reached
        while ((System.currentTimeMillis() - startTime) < (timeoutInSeconds * 1000)) {
            // Execute JavaScript to check if the element exists and is visible
            isElementVisible = (boolean) jsExecutor.executeScript(
                    "return arguments[0] !== null && arguments[0].offsetWidth > 0 && arguments[0].offsetHeight > 0;",
                    element);

            // If the element is visible, break the loop
            if (isElementVisible) {
                break;
            }

            // Optional: Sleep for a brief time before checking again
            try {
                timeOut(500); // 500ms sleep to avoid excessive CPU usage
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // If the element is not visible after waiting for the timeout, throw an exception
        if (!isElementVisible) {
            log.warn("Element is not visible after " + timeoutInSeconds + " seconds.");
        }
    }


    /**
     * This method is to get text for the mentioned element
     *
     * @param element the web element for which we need to get the text
     */
    public void scrollToElement(WebElement element) {
        String elementText = null;
        try {
            elementText = element.toString();
            Actions actions = new Actions(driver);
            // Scroll to the WebElement
            actions.moveToElement(element).perform();

            log.info("Scrolled to the element successfully: " + elementText);
        } catch (Exception e) {
            addScreenshotToAllureReport();
            log.info("Failed to scroll to the element: " + elementText);
            e.printStackTrace();
        }
    }

    /**
     * This method is to get text for the mentioned element
     *
     * @param element the web element for which we need to get the text
     */
    public void scrollIntoViewByElement(WebElement element) {
        String elementText = null;
        try {
            elementText = element.toString();
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", element);
            log.info("Scrolled to the element successfully: " + elementText);
        } catch (Exception e) {
            addScreenshotToAllureReport();
            log.info("Failed to scroll to the element: " + elementText);
            e.printStackTrace();
        }
    }

    /**
     * This method is to Scroll to specific element
     *
     * @param element the web element for which we need to scroll
     */
    public void scrollToElementUsingAction(WebElement element) {

        String elementText = null;
        try {
            elementText = element.toString();
            Actions actions = new Actions(driver);
            // Scroll to the WebElement
            actions.moveToElement(element).perform();
            log.info("Scrolled to the element successfully: " + elementText);
        } catch (Exception e) {
            addScreenshotToAllureReport();
            log.info("Failed to scroll to the element: " + elementText);
            e.printStackTrace();
        }
    }

    /**
     * This method is to Scroll to specific element
     *
     * @param element the web element for which we need to scroll
     */
    public void scrollToElementUsingJS(WebElement element) {

        String elementText = null;
        try {
            elementText = element.toString();
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
            log.info("Scrolled to the element successfully: " + elementText);
        } catch (Exception e) {
            addScreenshotToAllureReport();
            log.info("Failed to scroll to the element: " + elementText);
            e.printStackTrace();
        }
    }

    /**
     * This method is to click on the web element
     *
     * @param element           the web element which need to be clicked on
     * @param CustomDescription short description about the element
     */
    public void clickByWebelement(WebElement element, String CustomDescription) {
        String elementText = null;
        try{
            elementText = element.toString();
        }catch(Exception e){
            //Catch Class Cast Exception
            logReport(e.getMessage(), "INFO");
        }
        try {
            webdriverWaitElementToBeVisible(element);
            webdriverWaitElementToBeClickable(element);
            element.click();
            Allure.step(CustomDescription);
            logReport("The element with the name " + elementText + " is entered with successfully", "PASS");
        } catch (NoSuchElementException e) {
            addScreenshotToAllureReport();
            allure.logFailure(CustomDescription, e);
            logReport("The element with the name " + elementText + " is entered is not available in DOM", "FAIL");
            logAndFail("NoSuchElementException", e);
        } catch (ElementNotInteractableException e) {
            addScreenshotToAllureReport();
            allure.logFailure(CustomDescription, e);
            logReport("The element with the name " + elementText + " is not intractable", "FAIL");
            logAndFail("ElementNotInteractableException", e);
        } catch (StaleElementReferenceException e) {
            addScreenshotToAllureReport();
            allure.logFailure(CustomDescription, e);
            logReport("The element with the name " + elementText + " is not stable", "FAIL");
            logAndFail("StaleElementReferenceException", e);
        } catch (WebDriverException e) {
            addScreenshotToAllureReport();
            allure.logFailure(CustomDescription, e);
            logReport("The browser got closed due to unknown error", "FAIL");
            logAndFail("WebDriverException", e);
        }
    }


    public void clickElementWithFluentWait(WebElement element, String CustomDescription) {
        String elementText = element.toString();
        int timeout = Integer.parseInt(LoadProperties.prop.getProperty("explicitTimeOut1"));
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class);
        try {
            wait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    try {
                        // Re-fetch the element if it becomes stale
                        element.click();
                        return true;
                    } catch (StaleElementReferenceException e) {
                        log.info("Element became stale. Retrying...");
                        return false;
                    } catch (ElementClickInterceptedException e) {
                        log.info("Element click intercepted. Retrying...");
                        return false;
                    }
                }
            });
            Allure.step(CustomDescription);
            log.info("The element with name " + elementText + " is entered successfully");
            logReport("The element with the name " + elementText + " is entered with successfully", "PASS");
        } catch (NoSuchElementException e) {
            allure.logFailure(CustomDescription, e);
            logReport("The element with the name " + elementText + " is entered is not available in DOM", "FAIL");
            throw e;
        } catch (ElementNotInteractableException e) {
            allure.logFailure(CustomDescription, e);
            logReport("The element with the name " + elementText + " is not intractable", "FAIL");
            throw e;
        } catch (StaleElementReferenceException e) {
            allure.logFailure(CustomDescription, e);
            logReport("The element with the name " + elementText + " is not stable", "FAIL");
            throw e;
        } catch (WebDriverException e) {
            allure.logFailure(CustomDescription, e);
            logReport("The browser got closed due to unknown error", "FAIL");
            throw e;
        }

    }

    public void clickJS(){
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("document.querySelector('a[href=\"/tsc/catalog/baby-toddler-clothing\"]').click()");
    }

    public void clickByJavascript(WebElement element, String customDescription) {
        String elementText = null;
        try {
            // Wait until element is visible and clickable
            webdriverWaitElementToBeVisible(element);
            webdriverWaitElementToBeClickable(element);
            elementText = element.toString();
            // Perform the click using JavaScript
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].click();", element);

            // Log success
            Allure.step(customDescription);
            log.info("The element with name " + elementText + " was clicked successfully using JavaScript");
            logReport("The element with name " + elementText + " was clicked successfully using JavaScript", "PASS");

        } catch (NoSuchElementException e) {
            addScreenshotToAllureReport();
            allure.logFailure(customDescription, e);
            logReport("The element with the name " + elementText + " is not available in DOM", "FAIL");
            throw e;
        } catch (ElementNotInteractableException e) {
            addScreenshotToAllureReport();
            allure.logFailure(customDescription, e);
            logReport("The element with the name " + elementText + " is not interactable", "FAIL");
            throw e;
        } catch (StaleElementReferenceException e) {
            addScreenshotToAllureReport();
            allure.logFailure(customDescription, e);
            logReport("The element with the name " + elementText + " is not stable", "FAIL");
            throw e;

        } catch (WebDriverException e) {
            allure.logFailure(customDescription, e);
            logReport("The browser closed due to an unknown error", "FAIL");
            throw e;
        }
    }


    /**
     * This method is to click on the web element
     *
     * @param element           the web element where data need to be entered
     * @param data              data what need to be entered
     * @param CustomDescription short description about what data is being entered
     */
    public void enterByWebelement(WebElement element, String data, String CustomDescription) {
        String elementText = null;
        try {
            webdriverWaitElementToBeVisible(element);
            webdriverWaitElementToBeClickable(element);
            elementText = element.toString();
            element.clear();
            element.sendKeys(data);
            Allure.step(CustomDescription);
            logReport("The element with id " + elementText + " is entered with " + data + " successfully", "PASS");
        } catch (NoSuchElementException e) {
            addScreenshotToAllureReport();
            allure.logFailure(CustomDescription, e);
            logReport("The element with id " + elementText + " is entered is not available in DOM", "FAIL");
            throw e;
        } catch (ElementNotInteractableException e) {
            addScreenshotToAllureReport();
            allure.logFailure(CustomDescription, e);
            logReport("The element with id " + elementText + " is not intractable", "FAIL");
            throw e;
        } catch (StaleElementReferenceException e) {
            addScreenshotToAllureReport();
            allure.logFailure(CustomDescription, e);
            logReport("The element with id " + elementText + " is not stable", "FAIL");
            throw e;
        } catch (WebDriverException e) {
            addScreenshotToAllureReport();
            allure.logFailure(CustomDescription, e);
            logReport("The browser got closed due to unknown error", "FAIL");
            throw e;
        }
    }

    public void switchToIframeByWebElement(WebElement element) {
        String elementText = null;
        try {
//            webdriverWaitElementToBeVisible(element);
//            webdriverWaitElementToBeClickable(element);
            elementText = element.toString();
            driver.switchTo().frame(element);
        } catch (NoSuchFrameException e) {
            logReport("The element with id " + elementText + " is entered is not available in DOM", "FAIL");
        }
    }

    public void switchToMainWindow() {
        try {
            driver.switchTo().defaultContent();
        } catch (NoSuchFrameException e) {
            logReport("The control switched back to the main window", "FAIL");
        }
    }

    public void closePopupIfPresent(WebElement iFrameElement, WebElement insideIframeElement) {
        try {
            if (iFrameElement.isDisplayed() || iFrameElement.isEnabled()) {
                switchToIframeByWebElement(iFrameElement);
                clickElementWithFluentWait(insideIframeElement, "Closing the Iframe pop-up");
                switchToMainWindow();
            }
        } catch (NoSuchElementException e) {
            // Popup not present, do nothing
        }
    }

    public void waitForPopupAndCloseIfPresent(WebElement iFrameElement, WebElement element2) {
        try {
            closePopupIfPresent(iFrameElement, element2);
        } catch (TimeoutException e) {
            // Popup not present within timeout, do nothing
        }
    }

    public boolean checkIsDisplayed(WebElement element) {
        return checkIsDisplayed(element, "Step is not defined");
    }
    public boolean checkIsDisplayed(WebElement element, String step) {
        boolean status = false;
        String elementText = null;
        try{
            elementText = element.toString();
            status = element.isDisplayed();
        } catch (NoSuchElementException e) {
            logReport("The element " + elementText + " is entered is not available in DOM", "FAIL");
            Allure.step(e.getMessage()+" -> "+step, Status.FAILED);
            return false;
        } catch (Exception e) {
            Allure.step("Exception Occurred in the step -> "+step, Status.FAILED);
            return false;
        }
        if(status){
            Allure.step(step, Status.PASSED);
        }else{
            Allure.step(step, Status.FAILED);
        }
        return status;
    }

    public boolean checkIsEnabled(WebElement element) {
        waitForPageToLoadUsingJS();
        boolean status = false;
        String elementText = null;

        try {
            elementText = element.toString();
        } catch (ClassCastException e) {
            logReport("ClassCastException","Info");
        }
        try {
            if (element.isEnabled()) {
                status = true;
            }
        } catch (NoSuchElementException | TimeoutException e) {
            logReport("The element " + elementText + " is not available in DOM or not enabled", "FAIL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }


    public void enterById(String idValue, String data) {
        try {
            driver.findElement(By.xpath(idValue)).sendKeys(data);
            logReport("The element with id " + idValue + " is entered with " + data + " successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with id " + idValue + " is entered is not available in DOM", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with id " + idValue + " is not intractable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with id " + idValue + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void enterByName(String nameValue, String data) {
        try {
            driver.findElement(By.name(nameValue)).sendKeys(data);
            log.info("The element with name " + nameValue + " is entered with " + data + " successfully");
            ReportManager.test.pass(data);
            logReport("The element with the name " + nameValue + " is entered with " + data + " successfully", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element with the name " + nameValue + " is entered is not available in DOM", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element with the name " + nameValue + " is not intractable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element with the name " + nameValue + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void sendEnter(String locator) {
        driver.findElement(By.xpath(prop.getProperty("HomePage.googlesearch.Xpath"))).sendKeys(Keys.ENTER);
    }

    public void enterByXpath(String xpathValue, String data) {

        try {
            driver.findElement((By.xpath(xpathValue))).clear();
            driver.findElement(By.xpath(xpathValue)).sendKeys(data);
            logReport("PASS", "The element with the xpath" + xpathValue + "entered the data" + data + " successfully");
        } catch (NoSuchElementException e) {
            logReport("FAIL", "The element with the xpath " + xpathValue + " is not available in DOM");
        } catch (ElementNotInteractableException e) {
            logReport("FAIL", "The element with the xpath " + xpathValue + " is not intractable");
        } catch (StaleElementReferenceException e) {
            logReport("FAIL", "The element with the xpath " + xpathValue + " is not stable");
        } catch (WebDriverException e) {
            logReport("FAIL", "The browser got closed due to unknown error");

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
        } catch (ElementClickInterceptedException e) {
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
            List<WebElement> elementName = driver.findElements(By.xpath(xpathVal));
            for (int i = 0; i < elementName.size(); i++) {
                elementName.get(i).click();
                logReport("PASS", "The element is clicked by " + xpathVal + " successfully ");
            }
        } catch (NoSuchElementException e) {
            logReport("FAIL", "The element with " + xpathVal + " is not available in DOM");
        } catch (StaleElementReferenceException e) {
            logReport("FAIL", "The element with " + xpathVal + " is not stable");
        } catch (WebDriverException e) {
            logReport("FAIL", "The browser got closed due to unknown error");
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


    public void selectVisibileTextByWebElement(WebElement element, String value) {

        String elementText = null;
        try {
            webdriverWaitElementToBeVisible(element);
            elementText = element.toString();
            Select drpdwn1 = new Select(element);
            drpdwn1.selectByVisibleText(value);
            logReport("The element selected by " + elementText + " with the value of visible text " + value + " successfully.", "PASS");
        } catch (NoSuchElementException e) {
            logReport("The element selected by " + elementText + " with the value of visible text " + value + "is not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("The element selected by " + elementText + " with the value of visible text" + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element selected by " + elementText + " with the value of visible text" + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element selected by " + elementText + " with the value of visible text " + value + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");
        }
    }

    public void selectByValueByWebElement(WebElement element, String value) {

        String elementText = null;
        try {
            webdriverWaitElementToBeVisible(element);
            elementText = element.toString();
            Select drpdwn1 = new Select(element);
            drpdwn1.selectByValue(value);
            logReport("The element selected by " + elementText + " with the value of visible text " + value + " successfully.", "PASS");

        } catch (NoSuchElementException e) {
            logReport("The element selected by " + elementText + " with the value of visible text " + value + "is not available in DOM", "FAIL");
        } catch (ElementClickInterceptedException e) {
            logReport("The element selected by " + elementText + " with the value of visible text" + value + " is not clickable.", "FAIL");
        } catch (ElementNotInteractableException e) {
            logReport("The element selected by " + elementText + " with the value of visible text" + value + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            logReport("The element selected by " + elementText + " with the value of visible text " + value + " is not stable", "FAIL");
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


    // To Close browsers
    public void closeBrowser() {

        try {
            driver.close();
            //driver.remove();
            logReport("PASS", "Browser closed successfully");
        } catch (WebDriverException e) {
            logReport("FAIL", "The browser got closed due to unknown error");
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


    public void clickByXpath(WebDriver driver, String xpathVal) {
        try {
            driver.findElement(By.xpath(xpathVal)).click();
            logReport("PASS", "The element is clicked by " + xpathVal + " successfully ");
        } catch (NoSuchElementException e) {
            logReport("FAIL", "The element with " + xpathVal + " is not available in DOM");
        } catch (StaleElementReferenceException e) {
            logReport("FAIL", "The element with " + xpathVal + " is not stable");
        } catch (WebDriverException e) {
            logReport("FAIL", "The browser got closed due to unknown error");

        }
    }

    public void clickByXpath(String xpathVal) {
        try {
            driver.findElement(By.xpath(xpathVal)).click();
            logReport("PASS", "The element is clicked by " + xpathVal + " successfully ");
        } catch (NoSuchElementException e) {
            logReport("FAIL", "The element with " + xpathVal + " is not available in DOM");
        } catch (StaleElementReferenceException e) {
            logReport("FAIL", "The element with " + xpathVal + " is not stable");
        } catch (WebDriverException e) {
            logReport("FAIL", "The browser got closed due to unknown error");

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
    public String getTextByWebElement(WebElement element) {

        String elementText = null;
        try {
            elementText = element.toString();
            String text = element.getText();
            log.info("Extracted text is - " +text);
            logReport("The element with the " + elementText + " is taken and printed in console successfully", "PASS");
            return text;
        } catch (NoSuchElementException e) {
            addScreenshotToAllureReport();
            logReport("The element with " + elementText + " is not available in DOM", "FAIL");
        } catch (ElementNotInteractableException e) {
            addScreenshotToAllureReport();
            logReport("The element with " + elementText + " is not interactable", "FAIL");
        } catch (StaleElementReferenceException e) {
            addScreenshotToAllureReport();
            logReport("The element with " + elementText + " is not stable", "FAIL");
        } catch (WebDriverException e) {
            logReport("The browser got closed due to unknown error", "FAIL");

        }
        return null;
    }

    public String getTextById(String idVal) {

        try {
            String txtbyid = driver.findElement(By.id(idVal)).getText();
            log.info("Extracted text is - " +txtbyid);
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
            log.info("Extracted text is - " +textbyname);
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
            log.info("Extracted text is - " +textbyclassname);
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
            log.info("Extracted text is - " +txtbyxpath);
            logReport("PASS", "The element with the " + xpathVal + "is taken and printed in console successfully");
        } catch (NoSuchElementException e) {
            logReport("FAIL", "The element with " + xpathVal + " is not available in DOM");
        } catch (ElementNotInteractableException e) {
            logReport("FAIL", "The element with " + xpathVal + " is not interactable");

        } catch (StaleElementReferenceException e) {
            logReport("FAIL", "The element with " + xpathVal + " is not stable");
        } catch (WebDriverException e) {
            logReport("FAIL", "The browser got closed due to unknown error");

        }
        return txtbyxpath;
    }

    // To Alert functionalities
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
            log.info("Extracted text is - " +text);
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

    // To wait for some time to find the element
    public static void timeOut(long sleep) {
        try {
            Thread.sleep(sleep);
            log.debug("JVM Slept peacefully for " + sleep + "ms. ");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void webdriverWaitElementToBeClickable(WebElement element) {
        try {
            String elementText = element.toString();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(LoadProperties.prop.getProperty("explicitTimeOut1"))));
            ExpectedCondition<WebElement> expectedCondition = ExpectedConditions.elementToBeClickable(element);
            wait.until(expectedCondition);
            logReport("System waited successfully till the element is clickable", "PASS");
        } catch (TimeoutException e) {
            addScreenshotToAllureReport();
            logReport("Element not clickable after " + Integer.parseInt(LoadProperties.prop.getProperty("explicitTimeOut1")) + " seconds: " + e.getMessage(), "FAIL");
            e.printStackTrace();
            // Fail the test immediately
            Assert.fail("Element not clickable after " + Integer.parseInt(LoadProperties.prop.getProperty("explicitTimeOut1")) + " seconds: " + e.getMessage());
        } catch (Exception e) {
            addScreenshotToAllureReport();
            logReport("An error occurred while waiting for the element to be clickable: " + e.getMessage(), "FAIL");
            e.printStackTrace();
            // Fail the test for any other exceptions
            Assert.fail("An error occurred while waiting for the element to be clickable: " + e.getMessage());
        }
    }

    public void webdriverWaitElementToBeVisible(WebElement element) {
        int time = Integer.parseInt(LoadProperties.prop.getProperty("explicitTimeOut1"));
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
            ExpectedCondition<WebElement> expectedCondition = ExpectedConditions.visibilityOf(element);
            wait.until(expectedCondition);
            logReport("System waited successfully till the element is visible", "PASS");
        } catch (TimeoutException e) {
            addScreenshotToAllureReport();
            logReport("Element not visible after " + time + " seconds: " + e.getMessage(), "FAIL");
            e.printStackTrace();
            // Fail the test immediately after logging the screenshot
            logAndFail("Element not visible after " + time + " seconds: " , e);
        } catch (Exception e) {
            addScreenshotToAllureReport();
            logReport("An error occurred while waiting for the element to be visible: " + e.getMessage(), "FAIL");
            e.printStackTrace();
            // Fail the test for any other exceptions
            logAndFail("An error occurred while waiting for the element to be visible: " , e);
        }
    }

    public void webdriverWaitElementNotToBeVisible(WebElement element) {
        try {
            String elementText = element.toString();
            // Initialize WebDriverWait with 60 seconds timeout
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(LoadProperties.prop.getProperty("explicitTimeOut2"))));

            // Wait until the element is not visible using the locator
            wait.until(ExpectedConditions.invisibilityOf(element));

            // Log success if the element becomes invisible within the time frame
            logReport("System waited successfully till the element is not visible", "PASS");
        } catch (TimeoutException e) {
            // Capture screenshot and log the failure
            addScreenshotToAllureReport();
            logReport("Element still visible after " + Integer.parseInt(LoadProperties.prop.getProperty("explicitTimeOut2")) + " seconds: " + e.getMessage(), "FAIL");

            // Print stack trace for debugging
            e.printStackTrace();

            // Fail the test immediately after logging
            Assert.fail("Element still visible after " + Integer.parseInt(LoadProperties.prop.getProperty("explicitTimeOut2")) + " seconds: " + e.getMessage());
        } catch (Exception e) {
            // Capture screenshot and log for any other exceptions
            addScreenshotToAllureReport();
            logReport("An error occurred while waiting for the element to be invisible: " + e.getMessage(), "FAIL");

            // Print stack trace for debugging
            e.printStackTrace();

            // Fail the test for any unexpected exceptions
            Assert.fail("An error occurred while waiting for the element to be invisible: " + e.getMessage());
        }
    }


    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (WebDriverException e) {
            log.info("Error taking screenshot: " + e.getMessage());
            return null;
        }catch(Exception e){
            log.info("Error taking screenshot: " + e.getMessage());
            return null;
        }
    }

    public void addScreenshotToAllureReport() {
        // Take screenshot upon timeout
        byte[] screenshot = takeScreenshot(driver);

        if (screenshot != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = "timeout_" + timestamp + ".png";

            // Attach screenshot to Allure report
            Allure.addAttachment(fileName, new ByteArrayInputStream(screenshot));
        }
    }


    public void webdriverWaitElementToBeEnabled(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(LoadProperties.prop.getProperty("explicitTimeOut1"))));
            ExpectedCondition<Boolean> expectedCondition = new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    return element.isEnabled();
                }
            };
            wait.until(expectedCondition);
            logReport("System waited successfully till the element is enabled", "PASS");

        } catch (Exception e) {
            logReport("An error occurred while waiting for the element to be enabled: " + e.getMessage(), "FAIL");
            e.printStackTrace();
        }
    }

    public void switchToParentWindow() {

        try {
            Set<String> windowhandles = driver.getWindowHandles();

            for (String eachid : windowhandles) {
                driver.switchTo().window(eachid);
                break;
            }
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
                driver.switchTo().window(eachid);

            }
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
            for (String eachid2 : newwindow2) {
                driver.switchTo().window(eachid2);
                String TitleOfPage = driver.getTitle();
                if (TitleOfPage.equalsIgnoreCase("NVSP Service Portal")) {
                    log.info("Title matching successfully");
                } else {
                    log.info("Title Mismatching");
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
                log.info("Is Displayed");
            } else {
                log.info("Not Displayed");
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
                log.info("Is Displayed");
            } else {
                log.info("Not Displayed");
            }
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
            String txt = driver.findElement(By.xpath(xpath)).getText();
            if (txt.contentEquals(text)) {
                log.info("Is Displayed");
            } else {
                log.info("Not Displayed");
            }
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
                log.info("Is Displayed partially");
            } else {
                log.info("Is not even displayed partially");
            }
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
            if (titlename.equalsIgnoreCase(title)) {
                log.info("Title verified successfully");
            } else {
                log.info("Title mismatching");
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

    public void scrollUpByPixel() {
        scrollByPixcel("-4000");
    }

    public void scrolldownByPixcel() {
        scrollByPixcel("2000");
    }

    public void scrollByPixcel(String pixels) {
        // TODO Auto-generated method stub

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0," + pixels + ")");
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

            logReport("File uploaded successfully", "PASS");
        } catch (HeadlessException e) {
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
            log.info("Mousehovered the object successfully");
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
        log.info(date.getYear());
        log.info(date.getMonth());
        log.info(date.getDayOfMonth());

        int da = date.getDayOfMonth();
        log.info("the date is:" + da);
        driver.findElement(By.xpath(xpathval)).click();
        driver.findElement(By.xpath(xpathval)).click();

        log.info("The element is clicked by " + xpathval + " successfully ");
        //FTRPublicUserRegistrationPage.Date.Xpath=//a[text()='12']

    }

    //Web Element is displayed or not

    public boolean elementIsDisplayed(WebElement element, String custom) {
        if (element.isDisplayed() == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean elementIsDisplayed(WebElement element) {
        if (element.isDisplayed() == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean elementExists(String element) {
        boolean present;
        try {
            driver.findElement(By.xpath(element));
            present = true;
        } catch (NoSuchElementException e) {
            present = false;
        }
        return present;
    }

    public WebElement waitForElementToBeAvailable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        return wait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    if (element.isDisplayed() && element.getSize().getWidth() > 0 && element.getSize().getHeight() > 0) {
                        return element;
                    }
                    return null;
                } catch (StaleElementReferenceException | NoSuchElementException e) {
                    return null;
                }
            }
        });
    }

    /**
     * If the element exists but some time may not be interactable because of pop up or other dialog box
     * So we are trying to get the size of the element from the screen. If it really exists it returns the real size of the element on the screen
     * This function helps in a situation where isDisaplyed or other selenium based function fails to work
     *
     * @param element element
     * @return WebElement
     */
    public WebElement waitForElementToBeInteractable(WebElement element) {
        if (waitUntil(() -> (element.getSize().getWidth() > 0 && element.getSize().getHeight() > 0), 10)) {
            return element;
        } else {
            throw new org.openqa.selenium.NoSuchElementException("Either element not available or interactable. Please Check!\n" + element);
        }
    }

    /*This function will wait until the given condition is met. Default, it waits for 1 min*/
    public boolean waitUntil(Supplier<Boolean> condition) {
        return waitUntil(condition, 60);
    }

    /**
     * This function will help us to wait for any given condition to met
     * Wait time can be provided in seconds which will be added to the current system time to calculate overall wait time
     * Every time before checking the condition, function will wait for 0.5s
     * Whenever the condition is met -> loop ends and returns true
     * Whenever given time is reached -> loop ends and returns false
     * Exceptions can be ignored as we are not using this function to perform anything else other than waiting
     *
     * @param condition        any if condition that returns either true or false
     * @param timeoutInSeconds in seconds
     * @return boolean
     */
    public boolean waitUntil(Supplier<Boolean> condition, int timeoutInSeconds) {
        try {
            long startTime = System.currentTimeMillis();
            long endTime = startTime + (timeoutInSeconds * 1000L);
            while (System.currentTimeMillis() < endTime) {
                try {
                    if (condition.get()) {
                        return true;
                    }
                    try {
                        log.info("Wait for the condition to be met...");
                        timeOut(500);  // 500ms sleep to avoid excessive CPU usage
                    } catch (Exception e) {
                        addScreenshotToAllureReport();
                        Thread.currentThread().interrupt();
                        break;
                    }
                } catch (Exception e) {
                    //ignore
                }
            }
        } catch (Exception e) {
            //ignore any exception
        }
        addScreenshotToAllureReport();
        return false;
    }

    /**
     * This function helps to log steps with status of that in allure report based on the string comparisons
     *
     * @param actual
     * @param expected
     * @param step
     * @return boolean
     */
    public boolean verifyExpectedAndActualString(String actual, String expected, String step) {
        if (actual.equals(expected)) {
            Allure.step(step + " is verified successfully", Status.PASSED);
            return true;
        } else {
            addScreenshotToAllureReport();
            Allure.step(step + " -> verification failed!", Status.FAILED);
            return false;
        }
    }

    /**
     * This function helps to log steps with status of that in allure report based on the text from the element and the given expected value
     *
     * @param ele
     * @param expectedTxt
     * @param step
     * @return boolean
     */
    public boolean verifyExpectedAndActualTextOfElement(WebElement ele, String expectedTxt, String step) {
        return verifyExpectedAndActualString(getText(waitForElementToBeInteractable(ele)).trim(), expectedTxt, step);
    }

    /**
     * This function helps to wait until application completes loading
     */
    public void waitTillLoadingCompletesOnCOM() {
        try {
            waitForElementToDisappear(driver.findElement(By.xpath("//div[@class='scLoadingIcon']")));
        } catch (Exception e) {
            //ignore
        }
    }

    /**
     * This function can be used to wait for the element to be disappeared completely from the page
     * Intention is to wait, hence exceptions are not required to be handled
     *
     * @param element web
     */
    public void waitForElementToDisappear(WebElement element) {
        try {
            waitUntil(() -> !element.isDisplayed() || element.getSize().getHeight() == 0 && element.getSize().getWidth() == 0, 10);
        } catch (NoSuchElementException | StaleElementReferenceException ex) {
            // ignore
        }
    }

    private void logAndFail(String exceptionType, Exception e){
        // Attach the exception details to Allure
        Allure.addAttachment("Exception", exceptionType + ": " + e.getMessage());
        // Fail the test and log the error message
        try {
            throw e;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public boolean waitForElementByXpath(String xpath) {

        boolean flag = false;
        while (!flag) {
            WebElement element = driver.findElement(By.xpath(xpath));
            flag = element.isDisplayed();
        }
        return flag;
    }

    public boolean waitForElementToBeDisplayed(WebElement ele){
        return waitForElementToBeDisplayed(ele, 30);
    }

    public boolean waitForElementToBeDisplayed(WebElement ele, int seconds){
        return waitUntil(()->checkIsDisplayed(ele, "Checking element is displayed"), seconds);
    }

    public boolean isElementDisplayed(WebElement element, String elementDesc) {
        try {
            boolean result = element.isDisplayed();
            logReport("Element " + elementDesc + " is displayed", "PASS");
            return result;
        } catch (Exception e) {
            logReport("Element " + elementDesc + " is not displayed", "FAIL");
            return false;
        }
    }

    public String getAttributeValue(WebElement element, String attributeName) {
        if (element != null && attributeName != null) {
            String value = element.getAttribute(attributeName);
            logReport(String.format("Retrieved value of attribute '%s': '%s'", attributeName, value), "INFO");
            return value;
        } else {
            logReport("Element or attribute name cannot be null", "ERROR");
            throw new IllegalArgumentException("Element or attribute name cannot be null");
        }
    }

    public void waitForElement(WebElement element, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            ExpectedCondition<WebElement> expectedCondition = ExpectedConditions.visibilityOf(element);
            wait.until(expectedCondition);
            Thread.sleep(1000);
            logReport("Wait For the Element", "PASS");
        } catch (Exception e) {
        }
    }

    private By getByLocator(LocatorType locatorType, String locatorValue) {
        return switch (locatorType) {
            case ACCESSIBILITY_ID -> AppiumBy.accessibilityId(locatorValue);
            case CONTENT_DESC ->
                    AppiumBy.androidUIAutomator(String.format("new UiSelector().descriptionContains(\"%s\")", locatorValue));
            case RESOURCEID ->
                    AppiumBy.androidUIAutomator(String.format("new UiSelector().resourceId(\"%s\")", locatorValue));
            case TEXT ->
                    AppiumBy.androidUIAutomator(String.format("new UiSelector().textContains(\"%s\")", locatorValue));
            default -> throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        };
    }

    public void waitForElementVisible(LocatorType locatorType, String locatorValue) {
        Duration timeout = Duration.ofSeconds(10);
        Duration pollingInterval = Duration.ofMillis(1500);

        By byLocator = getByLocator(locatorType, locatorValue);

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .withMessage("Element with locatorType " + locatorType + " and value " + locatorValue + " not found within " + timeout + " seconds");

        try {
            wait.until(driver -> {
                try {
                    return ExpectedConditions.visibilityOfElementLocated(byLocator).apply(driver) != null;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            });

            logReport(String.format("Element with locatorType '%s' and value '%s' is visible.", locatorType, locatorValue), "PASS");
        } catch (Exception e) {
            logReport(String.format("Failed to find element with locatorType '%s' and value '%s' after retries. Error: %s", locatorType, locatorValue, e.getMessage()), "FAIL");
            Allure.step("Error occurred while waiting for the element: " + e.getMessage());
        }
    }

    public void waitForElementVisible(LocatorTypeIOS locatorType, String locatorValue) {
        Duration timeout = Duration.ofSeconds(10);
        Duration pollingInterval = Duration.ofMillis(1500);

        By byLocator = getLocator(locatorType, locatorValue);

        // Use FluentWait to handle retries for the element visibility
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .withMessage("Element with locatorType " + locatorType + " and value " + locatorValue + " not found within " + timeout + " seconds");

        try {
            // Wait for the element to be visible
            wait.until(driver -> {
                try {
                    return ExpectedConditions.visibilityOfElementLocated(byLocator).apply(driver) != null;
                } catch (StaleElementReferenceException e) {
                    return null; // Continue waiting if element is stale
                }
            });

            logReport(String.format("Element with locatorType '%s' and value '%s' is visible.", locatorType, locatorValue), "PASS");
        } catch (Exception e) {
            logReport(String.format("Failed to find element with locatorType '%s' and value '%s' after retries. Error: %s", locatorType, locatorValue, e.getMessage()), "FAIL");
            System.out.println("Error occurred while waiting for the element: " + e.getMessage());
            // Optionally, take a screenshot or log additional details here
        }
    }

    public void scrollToElementUntilVisible(WebElement element) {
        int maxScrollAttempts = 12;
        int attempts = 0;
        Dimension size = driver.manage().window().getSize();
        int endY = (int) (size.height * 0.9);
        while (attempts < maxScrollAttempts) {
            try {
                if (element.isDisplayed()) {
                    int locY = element.getLocation().getY();
                    if (locY > endY) {
                        performScrollGesture();
                    }
                    break;
                }
            }
            catch (NoSuchElementException e) {
                performScrollGesture();
            }
            attempts++;
        }
        if (attempts == maxScrollAttempts) {
            throw new RuntimeException("Element not visible after maximum scroll attempts.");
        }
    }

    private void performScrollGesture() {
        Dimension size = driver.manage().window().getSize();
        int startY = (int) (size.height * 0.5);
        int endY = (int) (size.height * 0.2);
        int startX = size.width / 2;
        AndroidDriver androidDriver = (AndroidDriver) driver;
        TouchAction action = new TouchAction(androidDriver);
        action.press(PointOption.point(20, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                .moveTo(PointOption.point(20, endY))
                .release()
                .perform();
    }

    public void performSwipe(String direction, double startX, double startY, double endX, double endY) {
        Dimension screenSize = driver.manage().window().getSize();
        int screenHeight = screenSize.getHeight();
        int screenWidth = screenSize.getWidth();

        Map<String, Object> args = new HashMap<>();
        args.put("direction", direction); // Can be "up", "down", "left", or "right"
        args.put("startX", (int) (startX * screenWidth));  // Start X in percentage (0.0 - 1.0)
        args.put("startY", (int) (startY * screenHeight));  // Start Y in percentage (0.0 - 1.0)
        args.put("endX", (int) (endX * screenWidth));      // End X in percentage (0.0 - 1.0)
        args.put("endY", (int) (endY * screenHeight));      // End Y in percentage (0.0 - 1.0)

        ((JavascriptExecutor) driver).executeScript("mobile: swipe", args);
    }

    public void scrollToElementUsingScrollIOS(WebElement  element) {
        int maxCount = 10;
        for(int i = 0; i < maxCount; i++) {
            try {
                if(element.isDisplayed()){
                    break;
                }
            } catch (Exception e) {}
            if( i % 3 == 0 && i != 0) {
                performSwipe("down", 0.05,0.2,0.05,0.4);
                System.out.println("Swipe down");
            } else {
                performSwipe("up", 0.05, 0.4, 0.05, 0.2);
                System.out.println("Swipe up");
            }
        }
    }

    public void KeyBoardEnterIOS() {
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Return\"]")).click();
    }

    public void CMAswipeUp(int maxSwipes) {
        CMAswipeUp(maxSwipes, 0.5, 0.8);
    }

    // This method is to scroll up
    public void CMAswipeUp(int maxSwipes, double start, double end) {
        try
        {
            Dimension size = driver.manage().window().getSize();
            int startX = (int) (size.width * 0.05);
            int startY = (int) (size.height * start);
            int endY = (int) (size.height * end);
            for (int i = 0; i < maxSwipes; i++) {
                new TouchAction((PerformsTouchActions) driver).longPress(PointOption.point(20, startY)).moveTo(PointOption.point(20, endY)).release().perform();
            }
            logReport("Swiping up completed.","INFO");
        } catch (Exception e) {
            logReport("Failed to perform continuous swipe up: " + e.getMessage(),"FAIL");
        }
    }

    private String buildScrollCommand(LocatorType locatorType, String locatorValue) {
        String selectorMethod = switch (locatorType) {
            case CONTENT_DESC -> "descriptionContains";
            case RESOURCEID -> "resourceId";
            case TEXT -> "textContains";
            case ACCESSIBILITY_ID ->
                    throw new IllegalArgumentException("Accessibility ID should not use UiAutomator command.");
            default -> throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        };

        return String.format("new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().%s(\"%s\"))", selectorMethod, locatorValue);
    }

    public String scrollToElementAndPerformAction(LocatorType locatorType, String locatorValue, ActionType actionType, String... textToSet) {
        logReport(String.format("Started scrolling to element with locatorType '%s' and locatorValue '%s'.", locatorType, locatorValue), "INFO");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {

            String uiScrollableCommand = buildScrollCommand(locatorType, locatorValue);
            logReport(String.format("Generated UiAutomator scroll command: '%s'.", uiScrollableCommand), "INFO");

            By byLocator = AppiumBy.androidUIAutomator(uiScrollableCommand);

            logReport("Waiting for the element to be visible.", "INFO");
            wait.until(ExpectedConditions.visibilityOfElementLocated(byLocator));
            WebElement element = driver.findElement(byLocator);


            switch (actionType) {
                case CLICK:
                    logReport("Performing click action on the element.", "INFO");
                    element.click();
                    break;

                case SET_TEXT:
                    if (textToSet == null || textToSet.length == 0 || textToSet[0].isEmpty()) {
                        throw new IllegalArgumentException("Text to set cannot be null or empty for SET_TEXT action.");
                    }
                    String text = textToSet[0];
                    logReport(String.format("Performing setText action with text: '%s'.", text), "INFO");
                    element.clear();
                    element.sendKeys(text);
                    break;

                case SCROLL:
                    logReport("Successfully scrolled to and viewed the element with locator: " + locatorValue, "INFO");
                    break;

                case GET_TEXT:
                    logReport("Retrieving text from the element.", "INFO");
                    String elementText = element.getText();
                    logReport(String.format("Retrieved text: '%s' from the element with locator: '%s'.", elementText, locatorValue), "INFO");
                    return elementText;

                default:
                    throw new IllegalArgumentException("Unsupported action type: " + actionType);
            }

            logReport(String.format("Successfully performed action: '%s' on the element with locator: '%s'.", actionType, locatorValue), "INFO");
        } catch (Exception e) {
            logReport("Error occurred while scrolling to the element and performing action: " + e.getMessage(), "ERROR");
        }
        return null;
    }

    public void CMAswipeDown( int maxSwipes) {
        CMAswipeDown(maxSwipes, 0.5,0.2);
    }

    // This method is to scroll down with dynamic start and end points
    public void CMAswipeDown( int maxSwipes, double start, double end) {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = (int) (size.width * 0.05);
            int startY = (int) (size.height * start);
            int endY = (int) (size.height * end);
            for (int i = 0; i < maxSwipes; i++) {
                new TouchAction((PerformsTouchActions) driver).longPress(PointOption.point(20, startY)).moveTo(PointOption.point(20, endY)).release().perform();
            }
            logReport("Swiping completed.","INFO");
        } catch (Exception e) {
            logReport("Failed to perform continuous swipe: " + e.getMessage(),"FAIL");
        }
    }

    public void CMAtapbycoordinates(int x,int y) {
        new TouchAction((PerformsTouchActions) driver).tap(PointOption.point(x,y)).release().perform();
    }

    public void pressKey(WebElement element, Keys key) {
        clickByWebelement(element, "Pressed" + key.toString());
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).sendKeys(key).perform();
            logReport("Key " + key + " pressed successfully on element: " + element, "PASS");
        } catch (Exception e) {
            logReport("Error occurred while pressing key on element: " + element + ". Error: " + e.getMessage(), "FAIL");
        }
    }

    public static void swipeDown(WebDriver driver, int swipeCount) {
        swipeDown(driver, swipeCount, 0.8, 0.2);
    }

    public static void swipeDown(WebDriver driver, int swipeCount, double startFrom, double endTO) {
        try {
            // Get the screen size of the device
            Dimension dimension = driver.manage().window().getSize();
            int startX = dimension.width / 2; // Horizontal center of the screen
            int startY = (int) (dimension.height * startFrom); // Start swipe from 80% of the screen
            int endY = (int) (dimension.height * endTO);   // End swipe at 20% of the screen
            // Check if the driver is an instance of AndroidDriver
            if (driver instanceof AndroidDriver) {
                AndroidDriver androidDriver = (AndroidDriver) driver;
                for (int i = 0; i < swipeCount; i++) {
                    // Perform the swipe action
                    TouchAction action = new TouchAction(androidDriver);
                    action.press(PointOption.point(startX, startY)) // Press at start point
                            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500))) // Hold for a moment
                            .moveTo(PointOption.point(startX, endY)) // Move to the end point
                            .release() // Release touch
                            .perform();
                    // Optional: Wait between swipes
                    Thread.sleep(1000);
                }
            } else {
                System.out.println("Unsupported driver type for swipe action.");
            }
        } catch (Exception e) {
            System.out.println("Swipe down failed: " + e.getMessage());
        }
    }

    public static String generateUniqueEmail(String domain) {
        // Generate a unique identifier
        String uniqueId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        // Combine with a domain to create a dummy email
        return "testuser_" + uniqueId + domain;
    }

    public void clickUsingOffset(WebElement webElement, int xOffset, int yOffset){
        new Actions(driver)
                .moveToElement(webElement, xOffset, yOffset)
                .click()
                .perform();
    }

}