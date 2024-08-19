package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.HIGPages;
import utilities.DriverCall;

public class TagValidationSteps extends BaseClass {

	@Given("Load the Browser and Driver")
	public void load_browser_driver() {
		driverCall.setupBrowser();
		driver = DriverCall.callDriver();
		hp = new HIGPages(driver);
	}

	@When("Load the homepage for HIG website")
	public void open_browser_and_click_url() { hp.getHomePage();	}

	@When("Login HIG with {string} and {string}")
	public void login_with_and(String username, String password) throws InterruptedException {
		hp.clickLoginMenu();
		Thread.sleep(3000);
		hp.enterCredentials(username, password);
		Thread.sleep(3000);
	}

	@When("Load Homepage and Network Payload using collect filter")
	public void start_network_monitoring() { hp.startNetworkMonitoring();	}

	@When("Execute script on console for generating UTAGs")
	public void generate_utags_console() { hp.getUtagData(); 	}

	@Then("Report the tag data")
	public void report_tag_data() { hp.printUtagData(); }

	@Then("Report the payload data")
	public void report_payload_data() { hp.printPayloadData(); }

	@Then("Validate if tag is present with key: {string}")
	public void validate_tag_key_data(String keyValue) { hp.validateTagKey(keyValue); }

	@After
	public void closeBrowser() {
		if(driver != null) { driver.quit(); }
	}

}