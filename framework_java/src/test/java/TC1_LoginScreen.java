import com.microsoft.playwright.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TC1_LoginScreen {

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
public void Login(){

//        Playwright playwright=Playwright.create();
//        BrowserType browserType=playwright.webkit();
//        Browser browser=browserType.launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));
//        BrowserContext browserContext=browser.newContext();
//        Page page=browserContext.newPage();

//    Playwright playwright=Playwright.create();
//    Browser browser=playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));
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

    //Verify the login done successfully
    assertThat(page).hasURL("https://www.saucedemo.com/inventory.html");

    page.close();
    System.out.println("Successfully Login with valid credential");

    playwright.close();

}

}