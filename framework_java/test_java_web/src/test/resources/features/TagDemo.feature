Feature: Tag Validations Demo

	Background: Pre-Requisite for automated execution
		Given Load the Browser and Driver

	@smoke
	Scenario: Monitor Network Payload for Home Page
		When Load Homepage and Network Payload using collect filter
		Then Report the payload data

	@smoke2
	Scenario: Extract the UTAG Data for Home Page
		When Load the homepage for HIG website
		And Execute script on console for generating UTAGs
		Then Report the tag data

	@smoke3
	Scenario Outline: Extract the UTAG Data for Multiple Pages
		When Load the homepage for HIG website
		And Execute script on console for generating UTAGs
		And Report the tag data
		And Login HIG with "username" and "pwd"
		And Execute script on console for generating UTAGs
		Then Report the tag data

		Examples:
			| username       | pwd			|
			| standard_user  | secret_sauce	|