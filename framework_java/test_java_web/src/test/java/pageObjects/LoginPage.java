package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

public class LoginPage {
	
	public WebDriver ldriver;
	
	public LoginPage(WebDriver rdriver){
		ldriver = rdriver;
		PageFactory.initElements(ldriver, this);
	}
	
	@FindBy(id = "user-name")
	WebElement UserName;
	
	@FindBy(id = "password")
	WebElement Password;
	
	@FindBy(id = "login-button")
	WebElement LoginButton;
	
	@FindBy(xpath = "//*[@id=\'login_button_container\']/div/form/div[3]/h3" )
	WebElement ErrorMessage;
	
	public void inputUsername(String username) {
		UserName.sendKeys(username);
	}
	
	public void inputPassword(String pwd) {
		Password.sendKeys(pwd);
	}
	
	public void clickLogin() {
		LoginButton.click();
	}
	public String getErrorMessage() {
		String message = ErrorMessage.getText();
		return message;
	}
}
