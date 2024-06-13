package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverCall {
	
	public static WebDriver driver;
	
    public void setupBrowser() {
    	ChromeOptions options = new ChromeOptions();
    	options.addArguments("--incognito");
		System.setProperty("webdriver.chrome.driver","D:/Project/QE_JavaPython/commonutils/drivers/chromedriver/chromedriver.exe");
		driver=new ChromeDriver(options); 
    }
    
	public static WebDriver callDriver() {

		return driver;
	}

}

