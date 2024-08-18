package stepDefinitions;

import org.openqa.selenium.chrome.ChromeDriver;
import pageObjects.*;
import utilities.DriverCall;


public class BaseClass {
	
	public DriverCall driverCall = new DriverCall();
	static ChromeDriver driver;
	static LoginPage lp;
	static LogoutPage lo;
	static ProductsPage Pp;
	static HIGPages hp;

}
