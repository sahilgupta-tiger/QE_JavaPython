package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.remote.RemoteWebDriver;
import webutils.LoadProperties;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import static java.time.temporal.ChronoUnit.SECONDS;

public class DriverCall {

	public static WebDriver driver;

    public void setupBrowser() {
		RemoteWebDriver rdriver;
		try {
			ChromeOptions chromeOptions = new ChromeOptions();
			//chromeOptions.setBinary(LoadProperties.prop.getProperty("chrome.binary"));
			chromeOptions.addArguments("--no-sandbox");
			chromeOptions.addArguments("--incognito");
			chromeOptions.addArguments("--start-maximized");
			chromeOptions.addArguments("--ignore-certificate-errors");
			String selGrid = LoadProperties.prop.getProperty("use.selenium.grid");
			String selURL = LoadProperties.prop.getProperty("grid.url");
			if (selGrid.equalsIgnoreCase("yes")) {
				rdriver = new RemoteWebDriver(new URI(selURL).toURL(), chromeOptions);
				driver  = rdriver;
			} else {
				//WebDriverManager.chromedriver().setup();
				System.setProperty("webdriver.chrome.driver", LoadProperties.prop.getProperty("chrome.driver.path"));
				driver = new ChromeDriver(chromeOptions);
			}
		} catch (MalformedURLException | URISyntaxException m) {
			System.err.println(m.getMessage());
		}
        driver.manage().timeouts().implicitlyWait(Duration.of(10, SECONDS));
    }
    
	public static WebDriver callDriver() { return driver; 	}

}

