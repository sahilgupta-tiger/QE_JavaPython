package pageObjects;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ProductsPage {
	public WebDriver ldriver;
	
	public ProductsPage(WebDriver rdriver){
		ldriver = rdriver;
		PageFactory.initElements(ldriver, this);
		
	}
	
	@FindBy(xpath = "//*[@id='add-to-cart-sauce-labs-backpack']")
	WebElement AddtoCart;
	
	@FindBy(xpath = "//*[@id='shopping_cart_container']/a")
	WebElement GotoCart;
	
	@FindBy(xpath = "//div[@class='cart_item']/div/a/div")
	List<WebElement> Item;
	
	@FindBy(xpath = "//*/div/button[contains(@id,'remove')]")
	WebElement RemoveFromCart;

	@FindBy(xpath = "//button[@id='continue-shopping']")
	WebElement ContinueShopping;
	
	public void clickAddtoCart() {
		AddtoCart.click();
	}
	
	public void clickGotoCart() {
		try 
		{
			//WebDriverWait wait = new WebDriverWait(ldriver,Duration.ofSeconds(40));
			//WebElement GotoCart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='shopping_cart_container']/a")));
			//ldriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
			GotoCart.click();
		}
		catch(Exception e)
		{
			//System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	public int getNoOfItems() {
		return(Item.size());
	}
	
	public boolean verifyItems(String itemrequrired) {
		boolean flag = false;
		for(WebElement i:Item) {
			String text = i.getText();
			if(text.equals(itemrequrired)) {
				flag=true;
			}
			else {
				flag= false;
			}
		}
		return flag;
	}
	
	public void clickRemoveFromCart() {
		try 
		{
			ldriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			RemoveFromCart.click();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	public void clickContinueShopping() {
		ContinueShopping.click();
	}

}
