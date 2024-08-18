package utilities;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import static java.time.temporal.ChronoUnit.SECONDS;

public class DriverCall {
	
	public static ChromeDriver driver;

    public void setupBrowser() {
    	ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("incognito");
		chromeOptions.addArguments("start-maximized");
		chromeOptions.addArguments("ignore-certificate-errors");
		chromeOptions.addArguments("disable-infobars");

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().implicitlyWait(Duration.of(10, SECONDS));
    }
    
	public static ChromeDriver callDriver() { return driver; 	}

}

