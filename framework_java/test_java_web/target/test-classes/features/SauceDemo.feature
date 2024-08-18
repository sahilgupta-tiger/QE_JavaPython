Feature: Saucedemo
  
	Background: Below are the steps common for each scenario
		Given Open Browser and click URL

	Scenario Outline: Login with invalid credentials
	When Login with "username" and "pwd"
	Then Verify not able to login
	And Close Browser

	Examples:
	  | username       | pwd                                 |
	  | standard_user  | U0g3UlNLNkZXTkZDTEREUEVDUUJPNVpDNEk=|
	  | uuid_base32hb  | c2VjcmV0X3NhdWNl                    |

	Scenario: Login without credentials
	When Login without username and password
	Then Verify not able to login with error message
	And Close Browser

	Scenario: Add products to cart
		And Login with "standard_user" and "secret_sauce"
		When Add product to cart
		And Go to cart
		Then Verify item in cart
		And Close Browser

	Scenario: Remove products from cart
		And Login with "standard_user" and "secret_sauce"
		And Add product to cart
		And Go to cart
		When Remove products from cart
		Then Verify item not in cart
		And Close Browser

	Scenario: Continue shopping button in cart
		And Login with "standard_user" and "secret_sauce"
		And Add product to cart
		And Go to cart
		When Click Continue shopping button in cart
		Then Verify the page is inventory page
		And Close Browser

	Scenario: Logout to the app
		And User Login with credentials
		When Logout to the application
		Then Verify that user is in logout page
		And Close Browser

	Scenario: Tag Validations for Website
		And User Login with credentials
		When Logout to the application
		Then Verify that user is in logout page
		And Close Browser