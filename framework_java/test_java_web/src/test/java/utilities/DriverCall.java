package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DriverCall {
	
	public static WebDriver driver;


    public void setupBrowser() {
    	ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("incognito");
		chromeOptions.addArguments("start-maximized");
		chromeOptions.addArguments("ignore-certificate-errors");
		chromeOptions.addArguments("disable-infobars");

		File CurrentDirFile = new File(System.getProperty("user.dir"));
		String RequiredDir = CurrentDirFile.getParentFile().getParent();
		Path filepath = Paths.get(RequiredDir,"commonutils","drivers","chromedriver","chromedriver.exe");
		System.setProperty("webdriver.chrome.driver",filepath.toString());
		driver=new ChromeDriver(chromeOptions);
    }
    
	public static WebDriver callDriver() {

		return driver;
	}

}

