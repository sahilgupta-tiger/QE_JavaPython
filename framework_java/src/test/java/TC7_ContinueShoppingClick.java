import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TC7_ContinueShoppingClick {

@Test
public void ContinueClick() throws InterruptedException {

//        Playwright playwright=Playwright.create();
//        BrowserType browserType=playwright.webkit();
//        Browser browser=browserType.launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));
//        BrowserContext browserContext=browser.newContext();
//        Page page=browserContext.newPage();

    Playwright playwright=Playwright.create();
    Browser browser=playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));
    BrowserContext context=browser.newContext(new Browser.NewContextOptions().setViewportSize(1000,1080));
    Page page=context.newPage();

    //Go to https://www.saucedemo.com/
    page.navigate("https://www.saucedemo.com/");

    //Fill the UserName
    page.locator("[data-test=\"username\"]").fill("standard_user");

    //Fill the Password
    page.locator("[data-test=\"password\"]").fill("secret_sauce");

    //click on login button
    page.locator("[data-test=\"login-button\"]").click();

    //Verify the successfully login
    assertThat(page).hasURL("https://www.saucedemo.com/inventory.html");


    //Add Item to Cart
    page.locator("[data-test=\"add-to-cart-sauce-labs-backpack\"]").click();

    //Verify the cart size
    String Cartsize=page.textContent("[class=\"shopping_cart_badge\"]");
    Assert.assertEquals("1",Cartsize);
    System.out.println("One Item added to the cart");

    //Open the cart and remove the item from cart
    page.locator("[data-test=\"shopping-cart-link\"]").click();
    assertThat(page).hasURL("https://www.saucedemo.com/cart.html");
    Locator removeButton = page.locator("[name=\"remove-sauce-labs-backpack\"]");
    removeButton.click();
    Assert.assertTrue(removeButton.isHidden());
    System.out.println("Item removed from the cart");

    //Click on continue button
    page.locator("[name=\"continue-shopping\"]").click();
    assertThat(page).hasURL("https://www.saucedemo.com/inventory.html");
    System.out.println("Continue button clicked and control move to shopping screen");

    //close the browser
    page.close();


}

}

