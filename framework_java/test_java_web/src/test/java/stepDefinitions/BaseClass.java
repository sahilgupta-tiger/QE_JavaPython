package stepDefinitions;

import org.openqa.selenium.WebDriver;
import pageObjects.*;
import utilities.DriverCall;


public class BaseClass {
	
	public DriverCall driverCall = new DriverCall();
	static WebDriver driver;
	static LoginPage lp;
	static LogoutPage lo;
	static ProductsPage Pp;
	static HIGPages hp;

}
