Feature: Tag Validations Demo

	Background: Pre-Requisite for automated execution
		Given Load the Browser and Driver

	@smoke
	Scenario: Monitor Network Payload for Home Page
		When Load Homepage and Network Payload using filter : "g/collect"
		Then Report the payload data

	@smoke1
	Scenario Outline: Extract the UTAG Data for Home Page
		When Load the homepage for desired website : "<application>"
		And Execute script on console for generating UTAGs
		Then Report the tag data
		And Validate if tag is present with key: "tealium_visitor_id"

		Examples:
			| application			|
			| www.thehartford.com	|
			| www.newbalance.com	|

	@smoke3
	Scenario Outline: Extract the UTAG Data for Multiple Pages
		When Load the homepage for desired website : "www.thehartford.com"
		And Execute script on console for generating UTAGs
		And Report the tag data
		And Login HIG with "<username>" and "<pwd>"
		And Execute script on console for generating UTAGs
		Then Report the tag data

		Examples:
			| username       | pwd			|
			| standard_user  | secret_sauce	|