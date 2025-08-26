package listeners;

import com.google.common.io.Files;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AllureTestNgListener implements ITestListener {
    boolean errorFlag = false;
    byte[] snapshot;

    private static final String REPORT_DIR = "target/allure-results";

    @Override
    public void onStart(ITestContext context) {
        // No implementation needed
    }

    @Override
    public void onFinish(ITestContext context) {
        // No implementation needed
    }

    @Override
    public void onTestStart(ITestResult result) {
        String runId = UUID.randomUUID().toString(); // Generate unique ID for each run
        String runDir = REPORT_DIR + File.separator + runId;
        System.setProperty("allure.results.directory", runDir);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // No implementation needed
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestContext context = result.getTestContext();
        WebDriver driver = (WebDriver) context.getAttribute("WebDriver");

        if (driver != null) {
            try {
                // Capture screenshot
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

                // Ensure Allure is running the test before attaching screenshot
                if (Allure.getLifecycle().getCurrentTestCaseOrStep().isPresent()) {
                    Allure.getLifecycle().addAttachment("Screenshot on Failure", "image/png", "png", new ByteArrayInputStream(screenshot));
                } else {
                    System.out.println("No active test case or step found in Allure lifecycle.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("WebDriver instance was null, could not capture screenshot.");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // No implementation needed
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // No implementation needed
    }

    public static byte[] screenshot(WebDriver driver) {
        try {
            File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            return Files.toByteArray(screen);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean ValidateAssert(String expected_text, String actual_text, WebDriver driver) {
        try {
            assertEquals(expected_text, actual_text);
        } catch (AssertionError e) {
            errorFlag = true;
            snapshot = screenshot(driver);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);
            // Create the file name with the timestamp
            String fileName = "snapshot_" + timestamp + ".png";
            System.out.println("Snapshot filename: " + fileName);
            Allure.addAttachment(fileName, new ByteArrayInputStream(snapshot));
        }
        return errorFlag;
    }


    public static void captureAndAttachScreenshot(String description, WebDriver driver) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = "failure_screenshot_" + timestamp + ".png";

            // Attach screenshot to Allure
            Allure.addAttachment(fileName, new ByteArrayInputStream(screenshot));

            System.out.println("Screenshot captured and attached to Allure report: " + fileName);
        } catch (WebDriverException e) {
            e.printStackTrace();
        }
    }

}
