Feature: API Demo Execution

  Background: User is Loaded Data In
    Given Assigning Payload source "api_testselection.xlsx" and "api_testselection"

  @Smoke
  Scenario Outline: Test the endpoints for Reqres Api's
    Given Load Data into Methods "<DataBinding>"
    When Make API call to run
    Then Validate the json response with the expected output

    Examples:
      | DataBinding |
      | Test_0001   |
      | Test_001    |
      | Test_002    |
      | Test_003    |
      | Test_017    |


  @Regression
  Scenario Outline: Test the endpoints for Reqres Api's
    Given Load Data into Methods "<DataBinding>"
    When Make API call to run
    Then Validate the json response with the expected output

    Examples:
      | DataBinding |
      | Test_005    |
      | Test_006    |
      | Test_007    |
      | Test_008    |


  @E2E
  Scenario Outline: Test the endpoints for Reqres Api's
    Given Load Data into Methods "<DataBinding>"
    When Make API call to run
    Then Validate the json response with the expected output

    Examples:
      | DataBinding |
#      | Test_013    |
#      | Test_014    |
#      | Test_015    |
      | Test_022    |