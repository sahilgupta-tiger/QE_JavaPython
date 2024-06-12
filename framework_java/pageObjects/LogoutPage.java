package pageObjects;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

public class LogoutPage {

	public WebDriver ldriver;
	
	public LogoutPage(WebDriver rdriver){
		ldriver = rdriver;
		PageFactory.initElements(ldriver, this);
	}
	
	@FindBy(id = "react-burger-menu-btn")
	WebElement MenuButton;
	
	@FindBy(xpath = "//a[@id='logout_sidebar_link']")
	WebElement LogoutButton;
	
	public void clickMenuButton() {
		MenuButton.click();
	}
	
	public void clicLogoutButton() {
		ldriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		LogoutButton.click();
	}
}
