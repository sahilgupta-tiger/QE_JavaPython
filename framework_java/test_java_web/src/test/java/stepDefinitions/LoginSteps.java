package stepDefinitions;
import pageObjects.*;
import static org.testng.Assert.assertEquals;

import io.cucumber.java.en.*;

public class LoginSteps extends BaseClass {
		
	@When("Login with {string} and {string}")
	public void login_with_and(String username, String pwd) throws InterruptedException {
		lp.inputUsername(username);
		lp.inputPassword(pwd);
		lp.clickLogin();
		Thread.sleep(3000);		
	}
	
	@Then("Verify not able to login")
	public void verify_not_able_to_login() {
		String text = lp.getErrorMessage();
		assertEquals("Epic sadface: Username and password do not match any user in this service", text);
	}

	@When("Login without username and password")
	public void login_without_username_and_password() {
		lp.inputUsername("");
		lp.inputPassword("");
		lp.clickLogin();
	}

	@Then("Verify not able to login with error message")
	public void verify_not_able_to_login_with_error_message() {
		String text = lp.getErrorMessage();
		assertEquals("Epic sadface: Username is required", text);
	}
	
	@Given("Open Browser and click URL")
	public void open_browser_and_click_url() {
		driverCall.setupBrowser();
		driver = driverCall.callDriver();
		lp = new LoginPage(driver);
		driver.get("https://www.saucedemo.com");
		driver.manage().window().maximize();
	}

	@Then("Close Browser")
	public void close_browser() {
		driver.quit();
	}

}