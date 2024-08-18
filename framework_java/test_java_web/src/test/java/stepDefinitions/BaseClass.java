package stepDefinitions;

import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.devtools.v127.network.model.Request;
import pageObjects.*;
import utilities.DriverCall;

import java.util.ArrayList;

public class BaseClass {
	
	public DriverCall driverCall = new DriverCall();
	static ChromeDriver driver;
	static LoginPage lp;
	static LogoutPage lo;
	static ProductsPage Pp;
	static HIGPages hp;
	static String tagData;

}
