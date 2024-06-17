import com.microsoft.playwright.*;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TC3_ProductView {

@Test
public void Product(){

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

    //Verify the successful login
    assertThat(page).hasURL("https://www.saucedemo.com/inventory.html");

    //Click on one product and verify the product
    page.locator("text=Sauce Labs Backpack").click();
    assertThat(page).hasURL("https://www.saucedemo.com/inventory-item.html?id=4");

    page.close();
    System.out.println("product View successfully");

}

}
