package pageObjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;
import webutils.LoadProperties;
import java.time.Duration;
import static java.time.temporal.ChronoUnit.SECONDS;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v127.emulation.Emulation;
import org.openqa.selenium.devtools.v127.log.Log;
import org.openqa.selenium.devtools.v127.network.Network;
import org.openqa.selenium.devtools.v127.network.model.Request;
import java.util.ArrayList;
import java.util.Optional;

public class HIGPages {

	public ChromeDriver ldriver;
	public DevTools devTool;
	public ArrayList<Request> requests;

	public HIGPages(ChromeDriver rdriver){
		ldriver = rdriver;
		PageFactory.initElements(ldriver, this);
	}

	@Step("{0}")
	public void logToReport(String message) {
		Reporter.log(message);
		System.out.println(message);
	}

	public void loadDevToolSession() {
		devTool = ldriver.getDevTools(); // Create devTool instance
		devTool.createSession();
	}

	public void startNetworkMonitoring(String filter) {
		loadDevToolSession();
        getNetworkPayload(filter);
		getHomePage();
		printNetworkPayload();
	}

	public void getHomePage() {
		ldriver.get(LoadProperties.prop.getProperty("web.url"));
		ldriver.manage().timeouts().implicitlyWait(Duration.of(20, SECONDS));
		logToReport("::: HIG Homepage Loaded :::");
	}

	@FindBy(xpath = "//button[@class='navbar-toggle account-toggle']")
	WebElement AccountFromMenu;

	@FindBy(id = "//span[text()='Log In']/parent::a")
	WebElement LoginFromMenu;

	@FindBy(xpath = "//input[@title='username']")
	WebElement InputUsername;

	@FindBy(xpath = "//huk-button[@id='loadButton']")
	WebElement ContinueLogin;

	@FindBy(xpath = "//a[@id='logout_sidebar_link']")
	WebElement LogoutButton;
	
	public void clickLoginMenu() {
		AccountFromMenu.click();
		LoginFromMenu.click();
		logToReport("::: HIG Account Login Page Loaded :::");
	}

	public void enterCredentials(String usr, String pwd) {
		//Switch between tabs using Ctrl + \t
		ldriver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"\t");
		InputUsername.sendKeys(usr);
		ContinueLogin.click();
		logToReport("::: Attempted to Login using username = " + usr + " :::");
	}
	
	public void clicLogoutButton() {
		ldriver.manage().timeouts().implicitlyWait(Duration.of(10, SECONDS));
		LogoutButton.click();
	}

	public void getNetworkPayload(String filter) {
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
			logToReport(url);
			// Filter or process requests as needed to extract payload data
			if (url.contains(filter)) {
				requests.add(request.getRequest());
				logToReport("Found collect data with RequestID: " + request.getRequestId());
			}
		});
	}

	public void printNetworkPayload() {
		int cnt = 1;
		for(Request r:requests){
			String collectUrl = r.getUrl();
			String[] collectArray = collectUrl.split("&");
			logToReport("\n::::: Collect filtered Payload-" + cnt + " from Network :::::\n");
			for (String str : collectArray) {
				logToReport(str);
			}
			cnt++;
		}
	}

	public String getUtagData() {
		// Execute JavaScript to access utag data
		Object utagData = ((JavascriptExecutor) ldriver).executeScript("return utag.data;");
		String tagData = utagData.toString();
		tagData = tagData.substring(1, tagData.length() - 1);
		return tagData;
	}

	public void printUtagData(String utagValue) {
		String[] array = utagValue.split(", ");
		logToReport("\n::::: UTAG DATA from Console :::::\n");
		for (String str : array) {
			logToReport(str);
		}
	}
}
