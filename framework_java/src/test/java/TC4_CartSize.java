import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TC4_CartSize {

@Test
public void cartSize() throws InterruptedException {

//        Playwright playwright=Playwright.create();
//        BrowserType browserType=playwright.webkit();
//        Browser browser=browserType.launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(2000));
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

    //click the login button
    page.locator("[data-test=\"login-button\"]").click();

    //Verify the login done successfully
    assertThat(page).hasURL("https://www.saucedemo.com/inventory.html");

    Locator cartitem = page.locator("[data-test=\"shopping-cart-badge\"]");
    Assert.assertTrue(cartitem.isHidden());

    System.out.println("Login Successfully with no item in the cart");

    //Add Item to Cart
    page.locator("[data-test=\"add-to-cart-sauce-labs-backpack\"]").click();
    Assert.assertFalse(cartitem.isHidden());
    String cartvalue=page.textContent("[data-test=\"shopping-cart-badge\"]");
    System.out.println(cartvalue+" "+"Item added in the cart");

    /*
    //Open the cart and remove the item from cart
    page.locator("[data-test=\"shopping-cart-link\"]").click();
    assertThat(page).hasURL("https://www.saucedemo.com/cart.html");

    //Click on continue button
    page.locator("[name=\"continue-shopping\"]").click();
    assertThat(page).hasURL("https://www.saucedemo.com/inventory.html");
    System.out.println("Continue button clicked and control move to shopping screen");
    */



    page.close();

}

}

