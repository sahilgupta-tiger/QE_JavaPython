package stepDefinitions;
import utilities.DriverCall;
import io.cucumber.java.en.*;
import pageObjects.*;

import static org.testng.Assert.assertEquals;

public class ProductSteps extends BaseClass{
	
	
	@When("Add product to cart")
	public void add_product_to_cart() {
		driver = driverCall.callDriver();
		lp = new LoginPage(driver);
		lo = new LogoutPage(driver);
		Pp = new ProductsPage(driver);
		Pp.clickAddtoCart();
	}
	
	@When("Go to cart")
	public void go_to_cart() {
		Pp.clickGotoCart();
	}

	@Then("Verify item in cart")
	public void verify_item_in_cart() {
		boolean text = Pp.verifyItems("Sauce Labs Backpack");
		assert text;
	}
	
	@When("Remove products from cart")
	public void remove_products_from_cart() {
		Pp.clickRemoveFromCart();
	}
	

	@Then("Verify item not in cart")
	public void verify_item_not_in_cart() {
		boolean text = Pp.verifyItems("Sauce Labs Backpack");
		assert !text;
	}

	@When("Click Continue shopping button in cart")
	public void click_continue_shopping_button_in_cart() {
			Pp.clickContinueShopping();
	}


	@Then("Verify the page is inventory page")
	public void verify_the_page_is_inventory_page() {
		String currenturl = driver.getCurrentUrl();
		assertEquals("https://www.saucedemo.com/inventory.html", currenturl);

	}

}
