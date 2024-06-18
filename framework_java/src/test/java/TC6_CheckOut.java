import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TC6_CheckOut {
    Browser browser;
    Page page;
    Playwright playwright;

    @BeforeMethod
    @Parameters({"browser"})
    public void setup(String browsername){

        BrowserType browserType;
        playwright=Playwright.create();
        switch (browsername){
            case "firefox":
                browserType=playwright.firefox();
                break;
            case "webkit":
                browserType=playwright.webkit();
                break;
            default:
                throw new IllegalArgumentException("Please provide valid browser name");

        }

        browser=browserType.launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));
        page=browser.newPage();

    }


@Test
public void Checkout() {

//        Playwright playwright=Playwright.create();
//        BrowserType browserType=playwright.webkit();
//        Browser browser=browserType.launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));
//        BrowserContext browserContext=browser.newContext();
//        Page page=browserContext.newPage();

//    Playwright playwright=Playwright.create();
//    Browser browser=playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));
//    BrowserContext context=browser.newContext(new Browser.NewContextOptions().setViewportSize(1000,1080));
//    Page page=context.newPage();

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

    //Add Item to Cart and verify item cart size
    page.locator("[data-test=\"add-to-cart-sauce-labs-backpack\"]").click();
    page.locator("[data-test=\"add-to-cart-sauce-labs-bike-light\"]").click();
    String Cartsize=page.textContent("[class=\"shopping_cart_badge\"]");
    Assert.assertEquals("2",Cartsize);
    System.out.println(Cartsize+" "+"Item Added to the cart");

    //Move to shopping cart page
    page.locator("[data-test=\"shopping-cart-link\"]").click();
    assertThat(page).hasURL("https://www.saucedemo.com/cart.html");
    String Cartlabel=page.textContent("[data-test=\"title\"]");
    System.out.println(Cartlabel+" "+"Page opened");

    //Checkout button clicked
    page.locator("[data-test=\"checkout\"]").click();
    String CheckoutLabel=page.textContent("[data-test=\"title\"]");
    Assert.assertEquals(CheckoutLabel,"Checkout: Your Information");
    System.out.println(CheckoutLabel);

    //Fill the user information
    page.locator("[data-test=\"firstName\"]").fill("DANE");
    page.locator("[data-test=\"lastName\"]").fill("TIE");
    page.locator("[data-test=\"postalCode\"]").fill("123456");
    page.locator("[data-test=\"continue\"]").click();

    //Verify the Checkout done successfully
    assertThat(page).hasURL("https://www.saucedemo.com/checkout-step-two.html");
    page.locator("[name=\"finish\"]").click();

    //Verify the Checkout done successfully
    //assertThat(page).hasURL("https://www.saucedemo.com/checkout-complete.html");
    Locator TKmessage = page.locator("[data-test=\"complete-header\"]");
    Assert.assertTrue(TKmessage.isVisible());

    //close the browser
    page.close();
    System.out.println("Successfully placed the Order");

}

}

