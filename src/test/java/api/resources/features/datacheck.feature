Feature: Validate SDC JSON response against MSKS Table

  @ymg1 @smoke
  Scenario: Compare SDC API response with MSKS EXPERIMENT TABLE data using mapping sheet
    Given I have the SDC API JSON response file "sdc_complete_response.json"
#    Given I have the SDC API JSON response file "API_POC.xlsx"
    And I have the mapping sheet file "API_POC.xlsx"
    And I have the MSKS data file "API_POC.xlsx" and sheet "Experiments"
    When I validate the data consistency against "Experiment" table
    Then Check for mismatched Keys and upload the Result to Allure


  @ymg2 @smoke
  Scenario Outline: Compare SDC API response with MSKS MEASUREMENT TABLE data using mapping sheet
    Given I have the SDC API JSON response file "sdc_complete_response.json"
    And I have the mapping sheet file "API_POC.xlsx"
    And I have the MSKS data file "API_POC.xlsx" and sheet "Measurement"
    When I validate the data consistency against "Measurement" table by Index Value, "<topArray>" and "<indexKey>"
    Then Check for mismatched Keys and upload the Result to Allure
    Examples:
      | topArray      | indexKey               |
      | g_measurement | wf12_measurement_index |


  @ymg @smoke
  Scenario Outline: Compare SDC API response with MSKS ANALYSIS COMPARISON TUKEY TABLE data using mapping sheet
    Given I have the SDC API JSON response file "sdc_complete_response.json"
    And I have the mapping sheet file "API_POC.xlsx"
    And I have the MSKS data file "API_POC.xlsx" and sheet "analysiscomparisontukey"
    When I validate the data consistency against "AnalysisComparisonTukey" table by Index Value, "<topArray>" and "<indexKey>"
    Then Check for mismatched Keys and upload the Result to Allure
    Examples:
      | topArray                    | indexKey                    |
      | g_analysis_comparison_tukey | wf12_comparison_tukey_index |

  @ymg44 @smoke
  Scenario Outline: Compare SDC API response with MSKS ANALYSIS LS MEANS TEST TABLE data using mapping sheet
    Given I have the SDC API JSON response file "sdc_complete_response.json"
    And I have the mapping sheet file "API_POC.xlsx"
    And I have the MSKS data file "API_POC.xlsx" and sheet "analysislsmeansttest"
    When I validate the data consistency against "AnalysisLsMeansTtest" table by Index Value, "<topArray>" and "<indexKey>"
    Then Check for mismatched Keys and upload the Result to Allure
    Examples:
      | topArray                  | indexKey                  |
      | g_analysis_ls_means_ttest | wf12_ls_means_ttest_index |

  @ymg55 @smoke
  Scenario Outline: Compare SDC API response with MSKS ANALYSIS LS MEANS TUKEY TABLE data using mapping sheet
    Given I have the SDC API JSON response file "sdc_complete_response.json"
    And I have the mapping sheet file "API_POC.xlsx"
    And I have the MSKS data file "API_POC.xlsx" and sheet "analysislsmeanstukey"
    When I validate the data consistency against "AnalysisLsMeansTukey" table by Index Value, "<topArray>" and "<indexKey>"
    Then Check for mismatched Keys and upload the Result to Allure
    Examples:
      | topArray                  | indexKey                  |
      | g_analysis_ls_means_tukey | wf12_ls_means_tukey_index |

  @ymg66 @smoke
  Scenario Outline: Compare SDC API response with MSKS FORMULATION TABLE data using mapping sheet
    Given I have the SDC API JSON response file "sdc_complete_response.json"
    And I have the mapping sheet file "API_POC.xlsx"
    And I have the MSKS data file "API_POC.xlsx" and sheet "formulations"
    When I validate the data on "Formulations" table by Index Value, "<topArray>", "<topArrayKey>", "<subArray>" and "<indexKey>"
    Then Check for mismatched Keys and upload the Result to Allure
    Examples:
      | topArray      | topArrayKey           | subArray                 | indexKey                          |
      | g_formulation | wf12_formulation_name | g_formulation_ingredient | wf12_formulation_ingredient_index |
