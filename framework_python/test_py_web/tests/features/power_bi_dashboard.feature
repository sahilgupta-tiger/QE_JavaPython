Feature: Validate the PowerBI dashboard
  Testcases/scenarios for validating the values on PowerBI

  Scenario: TC_001_General Validation
    Given Login to dashboard for Test Case: "tc1"
    When Grab the dashboard name and labels
    Then Validate the generic details of the dashboard matches expected values


  Scenario Outline: TC_002_Validation of single value widgets
    Given Login to dashboard for Test Case: "tc2"
    When Grab widget value from PowerBI: <locator1>
    And Grab widget value from db with sql: <sql1>
    Then Comparison of widget value in PowerBI and db should match: <locator1>,<sql1>
    Examples:
      | locator1            | sql1              |
      | planned_spend_xpath | planned_spend_sql |
      | current_spend_xpath | current_spend_sql |
      | bw_v_prior_xpath    | bw_v_prior_sql    |


  Scenario: TC_003_Validation of executive summary
    Given Login to dashboard for Test Case: "tc3"
    When Grab table grid values from PowerBI
    And Grab table grid values from db with sql: executive_db_sql
    Then Comparison of table grid values in PowerBI and db should match


  Scenario: TC_004_Validation of Total spend by sector graph
    Given Login to dashboard for Test Case: "tc4"
    When Grab graph value from PowerBI with path: tss_xpath
    And Grab corresponding value from db with sql: total_sector_spend
    Then Comparision of graph value in PowerBI and db should match


  Scenario: TC_005_Validation of Total sensitivity by sector graph
    Given Login to dashboard for Test Case: "tc5"
    When Grab graph value from PowerBI with path: sbs_xpath
    And Grab corresponding value from db with sql: total_sensitivity_by_sector
    Then Comparision of graph value in PowerBI and db should match


  Scenario: TC_006_Validation of Total BW vs prior year graph
    Given Login to dashboard for Test Case: "tc6"
    When Grab graph value from PowerBI with path: ybs_xpath
    And Grab corresponding value from db with sql: total_bw_prior_year_sector
    Then Comparision of graph value in PowerBI and db should match


  Scenario Outline: TC_007_Validation of drilldown
    Given Login to dashboard for Test Case: "tc7"
    When Drilldown function is selected for a graph: <locator1>, <locator2>
    And Corresponding drilldown value is fetched from the db: <sql1>
    Then Compare drilldown values from BI and DB
    Examples:
      | locator1            |  locator2          | sql1             |
      | graph_source_xpath  |  table1_xpath      | drillthrough_sql |


  Scenario Outline: TC_008_Validation of slider
    Given Login to dashboard for Test Case: "tc8"
    When Slider mininum & maximum value is selected: <min>, <max>
    And Graph to be validated is located at <locator1> with <sql1>
    Then Comparison of graph value with slider selection should match
    Examples:
      | min         | max        | locator1  | sql1           |
      | 20000000    | 100000000  | tss_xpath | slider_tc_sql  |


  Scenario Outline: TC_009_Validation of Sensitivity widget
    Given Login to dashboard for Test Case: "tc9"
    When Grab widget value from PowerBI: <locator1>
    And Grab Sensitivity widget from db with sql: <sql1>
    Then Comparison of widget value in PowerBI and db should match: <locator1>,<sql1>
    Examples:
      | locator1            | sql1              |
      | sensitivity_xpath   | sensitivity_sql   |
