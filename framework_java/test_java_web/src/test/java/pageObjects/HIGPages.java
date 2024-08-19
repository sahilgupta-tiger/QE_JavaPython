package pageObjects;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.Reporter;
import webutils.LoadProperties;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import static java.time.temporal.ChronoUnit.SECONDS;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v127.network.Network;
import org.openqa.selenium.devtools.v127.network.model.Request;
import java.util.ArrayList;
import java.util.Optional;

public class HIGPages {

	public ChromeDriver ldriver;
	public DevTools devTool;
	public ArrayList<Request> requests;
	public String tagValue;

	public HIGPages(ChromeDriver rdriver){
		ldriver = rdriver;
		PageFactory.initElements(ldriver, this);
		clearLogFile();
	}

	@Step("{0}")
	public void logToReport(String message) {
		Reporter.log(message);
	}

	public void clearLogFile() {
		try {
			Files.newBufferedWriter(Paths.get("target/logs.txt"),
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public void logToFile(String message) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter("target/logs.txt", true)))) {
            out.println(message);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
	}

	public void getHomePage() {
		ldriver.get(LoadProperties.prop.getProperty("web.url"));
		ldriver.manage().timeouts().implicitlyWait(Duration.of(20, SECONDS));
		logToReport("::: HIG Homepage Loaded :::");
	}

	@FindBy(xpath = "(//div[@data-dl-cat=\"My Account\"])[1]")
	WebElement AccountFromMenu;

	@FindBy(xpath = "//span[text()='Log In']/parent::a/parent::div")
	WebElement LoginFromMenu;

	@FindBy(xpath = "//input[@title='username']")
	WebElement InputUsername;

	@FindBy(id = "UsernamePassword")
	WebElement ContinueLogin;

	@FindBy(xpath = "//a[@id='logout_sidebar_link']")
	WebElement LogoutButton;
	
	public void clickLoginMenu() {
		AccountFromMenu.click();
		ldriver.manage().timeouts().implicitlyWait(Duration.of(5, SECONDS));
		LoginFromMenu.click();
		ldriver.manage().timeouts().implicitlyWait(Duration.of(20, SECONDS));
		logToReport("::: HIG Account Login Page Loaded :::");
	}

	public void enterCredentials(String usr, String pwd) {
		String clickl = Keys.chord(Keys.CONTROL,Keys.TAB);
		ldriver.findElement(By.cssSelector("body")).sendKeys(clickl);
		ldriver.navigate().to("https://account.thehartford.com/customer/login");
		//InputUsername.sendKeys(usr);
		ContinueLogin.submit();
		ldriver.manage().timeouts().implicitlyWait(Duration.of(10, SECONDS));
		logToReport("::: Attempted to Login on the page :::");
	}
	
	public void clicLogoutButton() {
		ldriver.manage().timeouts().implicitlyWait(Duration.of(10, SECONDS));
		LogoutButton.click();
	}

	public void startNetworkMonitoring() {
		try {
			getNetworkPayload();
			getHomePage();
			Thread.sleep(15000);
			printNetworkPayload();
		} catch (Exception e) { e.printStackTrace(); }
	}

	public void getNetworkPayload() {
		devTool = ldriver.getDevTools(); // Create devTool instance
		devTool.createSession();
		requests = new ArrayList<>();
		// Enable network tracking
		devTool.send(Network.enable(
				Optional.empty(),
				Optional.empty(),
				Optional.empty()
		));

		// Add a listener for network requests
		devTool.addListener(Network.requestWillBeSent(), request -> {
			String url = request.getRequest().getUrl();
			logToFile(url);
			// Filter or process requests as needed to extract payload data
			if (url.contains("g/collect")) {
				requests.add(request.getRequest());
				logToFile("Found collect data with RequestID: " + request.getRequestId());
			}
		});
	}

	public void printNetworkPayload() {
		int cnt = 1;
		for(Request r:requests){
			String collectUrl = r.getUrl();
			String[] collectArray = collectUrl.split("&");
			logToReport("\n::::: Display Payload-" + cnt + " from Network :::::\n");
			for (String str : collectArray) {
				logToReport(str);
			}
			cnt++;
		}
	}

	public void getUtagData() {
		// Execute JavaScript to access utag data
		Object utagData = ((JavascriptExecutor) ldriver).executeScript("return utag.data;");
		String tagData = utagData.toString();
		tagData = tagData.substring(1, tagData.length() - 1);
		tagValue = tagData;
	}

	public void printUtagData() {
		String[] array = tagValue.split(", ");
		logToReport("\n::::: UTAG DATA for current page from Console :::::\n");
		for (String str : array) {
			logToReport(str);
		}
	}

	public void validateTagKey(String keyValue) {
		if (tagValue.contains(keyValue)) {
			logToReport("SUCCESS: Tag Key Value = " + keyValue + " found in UTAG data");
		} else {
			Assert.fail("FAILURE: Tag Key Value = " + keyValue + " is not available in UTAG data.");
		}
	}

	public void printPayloadData() {
		try {
			String content = Files.readString(Path.of("target/logs.txt"));
			Allure.addAttachment("Network Payload Data", "text/*", content);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
