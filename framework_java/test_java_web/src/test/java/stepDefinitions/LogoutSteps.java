package stepDefinitions;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.en.*;
import pageObjects.*;

import static org.testng.Assert.assertEquals;

public class LogoutSteps extends BaseClass{

	@Given("User Login with credentials")
	public void user_login_with_credentials() {
		driver = driverCall.callDriver();
		lp = new LoginPage(driver);
		lo = new LogoutPage(driver);
		lp.inputUsername("standard_user");
		lp.inputPassword("secret_sauce");
		lp.clickLogin();
	    
	}

	@When("Logout to the application")
	public void logout_to_the_application() {
		lo.clickMenuButton();
		lo.clicLogoutButton();
	}

	@Then("Verify that user is in logout page")
	public void verify_that_user_is_in_logout_page() {
		String currenturl = driver.getCurrentUrl();
		assertEquals("https://www.saucedemo.com/", currenturl);
	}

}
