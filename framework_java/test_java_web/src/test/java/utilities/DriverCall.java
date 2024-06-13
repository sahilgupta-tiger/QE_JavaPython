package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverCall {
	
	public static WebDriver driver;


    public void setupBrowser() {
    	ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("incognito");
		chromeOptions.addArguments("start-maximized");
		chromeOptions.addArguments("ignore-certificate-errors");
		chromeOptions.addArguments("disable-infobars");
		System.setProperty("webdriver.chrome.driver",
				"D:/My_Workspaces/GitHub/QE_JavaPython/commonutils/drivers/chromedriver/chromedriver.exe");
		driver=new ChromeDriver(chromeOptions);
    }
    
	public static WebDriver callDriver() {

		return driver;
	}

}

