Feature: Saucedemo

	Scenario Outline: TC010_Login with invalid credentials
		Given Login to dashboard for Test Case: "tc10"
		When Login with <username> and <pwd>
		Then Verify not able to login

	Examples:
	  | username       | pwd                                 |
	  | standard_user  | U0g3UlNLNkZXTkZDTEREUEVDUUJPNVpDNEk=|
	  | uuid_base32hb  | c2VjcmV0X3NhdWNl                    |

	Scenario: TC011_Login without credentials
		Given Login to dashboard for Test Case: "tc11"
		When Login without username and password
		Then Verify not able to login with error message

	Scenario: TC012_Add products to cart
		Given Login to dashboard for Test Case: "tc12"
		And User Login with credentials
		When Add product to cart
		And Go to cart
		Then Verify item in cart

	Scenario: TC013_Remove products from cart
		Given Login to dashboard for Test Case: "tc13"
		And User Login with credentials
		When Add product to cart
		And Go to cart
		And Remove products from cart
		Then Verify item not in cart

	Scenario: TC014_Continue shopping button in cart
		Given Login to dashboard for Test Case: "tc14"
		And User Login with credentials
		When Add product to cart
		And Go to cart
		And Click Continue shopping button in cart
		Then Verify the page is inventory page

	Scenario: TC015_Logout to the app
		Given Login to dashboard for Test Case: "tc15"
		And User Login with credentials
		When Logout to the application
		Then Verify that user is in logout page