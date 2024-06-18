import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TC2_LoginScreen_InvalidCredential {

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
public void Login2() {

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
    page.locator("[data-test=\"password\"]").fill("Change@123");

    //click on login button
    page.locator("[data-test=\"login-button\"]").click();

    //Verify the login screen with error message
    assertThat(page).hasURL("https://www.saucedemo.com/");

    //Verify the error message
    String AErrorMessage=page.textContent("[data-test=\"error\"]");
    String EErrorMessage="Epic sadface: Username and password do not match any user in this service";
    Assert.assertEquals(AErrorMessage,EErrorMessage);

    page.close();
    System.out.println("Error message displayed with invalid credential");

}

}
