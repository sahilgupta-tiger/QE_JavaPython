#Sample PowerBI report

#Login locators
emailIDpath = '//*[@name="loginfmt"]'
emaiID_id= 'email'
submitBtn = '//*[@id="idSIButton9"]'
passwd = '//*[@name="passwd"]'
extraPopup = '//*[@id="KmsiCheckboxField"]'
newemail = '//*[@id="email"]'
newemailsubmit = '//*[@id="submitBtn"]'

#All constants
nameofreport = "Executive Summary"
header_names = ['Coverage', 'Compliance', 'Executive Summary', 'Raw Commodity Summary', 'Play Book - Coverage', 'Play Book - Forecast', 'Play Book - Compliance', 'Reset']
filter_names = ['Year', 'Commodity Group', 'Commodity', 'Sector', 'Country', 'Country/Region', 'Volume Forecast']

#Generic value locators
reportName = "(//span[@class='textRun'])[1]"
header_eles_xpath=r"//div[@class='content text ui-role-button-text']"
filter_eles_xpath=r"//h3[@class='slicer-header-text']"

#Widget locators
planned_spend_xpath = r"//*[local-name()='svg' and contains(@aria-label,'Planned')]//*[local-name()='text' and @class='value']//*[local-name()='tspan']"
current_spend_xpath = r"//*[local-name()='svg' and contains(@aria-label,'Current')]//*[local-name()='text' and @class='value']//*[local-name()='tspan']"
bw_v_prior_xpath = r"//*[local-name()='svg' and contains(@aria-label,'Prior')]//*[local-name()='text' and @class='value']//*[local-name()='tspan']"
sensitivity_xpath = r"//*[local-name()='svg' and contains(@aria-label,'Sensitivity')]//*[local-name()='text' and @class='value']//*[local-name()='tspan']"

#Table locators
commodityExecutive = "//*[local-name()='div' and @class='pivotTableCellWrap cell-interactive main-cell ']"
rowExecutive = "(//*[local-name()='div' and @class='scrollable-cells-container '])[1]//*[name()='div' and @class='pivotTableCellWrap cell-interactive tablixAlignCenter main-cell ']"
commodity_name_xpath=r"//*[local-name()='div' and @class='pivotTableCellWrap cell-interactive main-cell ']"
total_row_xpath=r"(//*[local-name()='div' and @class='scrollable-cells-container '])["
total_header_xpath=r"]//*[name()='div' and @class='pivotTableCellWrap main-cell ']"
executive_summary=r"(//*[local-name()='div' and @class='mid-viewport']//*[name()='div' and @class='pivotTableCellWrap cell-interactive tablixAlignRight main-cell ' and @column-index="

#Graph locators
tss_xpath=r"(//*[local-name()='svg' and @class='svgScrollable'])[1]//*[name()='text' and @class='label']"
ybs_xpath=r"(//*[local-name()='svg' and @class='svgScrollable'])[2]//*[name()='text' and @class='label']"
sbs_xpath=r"(//*[local-name()='svg' and @class='svgScrollable'])[3]//*[name()='text' and @class='label']"

#Slider locators
slider_min_locator = r"//div[@class='date-slicer-control']/input[contains(@aria-label,'Start')]"
slider_max_locator = r"//div[@class='date-slicer-control']/input[contains(@aria-label,'End')]"

#Drilldown locators
#graph_source_xpath=r"(//*[local-name()='svg' and @class='svgScrollable'])[1]"
graph_source_xpath=r"(//*[local-name()='svg' and @class='svgScrollable'])[1]//*[local-name()='rect' and @class='column setFocusRing']"
multiple_sector=r"(//*[local-name()='svg' and @class='svgScrollable'])[1]//*[local-name()='g' and @class='tick']"
drillthrough_xpath=r"//button[.=' Drill through ']"
drillthrough_sec_xpath=r"//span[normalize-space()='Drill through']"
table1_xpath=r"//div[contains(@aria-label,'B/W Vs Prior Year')]//div[@role='grid']"
row_xpath="(//*[local-name()='div' and @class='interactive-grid innerContainer'])[1]//*[local-name()='div' and @aria-rowindex="
col_xpath="(//*[local-name()='div' and @class='interactive-grid innerContainer'])[2]//*[local-name()='div' and @aria-rowindex="
back_button = r"//div[contains(@aria-label,'Back')]"

#Dashboard locators
resetFilter = "//*[text()='Reset']"
dropdown = "(//*[local-name()='div' and @class='slicer-restatement'])"
yearDropDown = "(//*[local-name()='div' and @class='slicer-restatement'])[1]"
commodityGroup = "(//*[local-name()='div' and @class='slicer-restatement'])[2]"
commodityFilter = "(//*[local-name()='div' and @class='slicer-restatement'])[3]"
sectorFilter = "(//*[local-name()='div' and @class='slicer-restatement'])[4]"
countryFilter = "(//*[local-name()='div' and @class='slicer-restatement'])[5]"
countryRegionFilter = "(//*[local-name()='div' and @class='slicer-restatement'])[6]"
totalSpendBySectorCol = "(//*[local-name()='g' and @class='x axis hideLinesOnAxis setFocusRing'])[1]//*[name()='g' and @class='tick']"
SensitivityBySpend = "(//*[local-name()='g' and @class='x axis hideLinesOnAxis setFocusRing'])[3]//*[name()='g' and @class='tick']"
SpendByPriorYear = "(//*[local-name()='g' and @class='x axis hideLinesOnAxis setFocusRing'])[2]//*[name()='g' and @class='tick']"
total_ind_ele_xpath=r"]//*[name()='div' and @class='pivotTableCellWrap tablixAlignRight main-cell ']"
newXpath = "(//*[local-name()='g' and @class='x axis hideLinesOnAxis setFocusRing'])[1]//*[name()='g' and @class='tick'][1]"
ReportValues = "//*[@class='value']"
tts_graph_x_axis=r"(//*[local-name()='g' and @class='x axis hideLinesOnAxis setFocusRing'])[1]//*[name()='g' and @class='tick']"
tts_graph_y_axis=r"(//*[local-name()='svg' and @class='mainGraphicsContext setFocusRing'])[1]//*[name()='rect' and @class='column setFocusRing']"
tss_graph_x_axis=r"(//*[local-name()='g' and @class='x axis hideLinesOnAxis setFocusRing'])[3]//*[name()='g' and @class='tick']"
tss_graph_y_axis=r"(//*[local-name()='svg' and @class='mainGraphicsContext setFocusRing'])[3]//*[name()='rect' and @class='column setFocusRing']"
fil_dropdown = []
fil_dropdown.insert(0, r"//div[@class='slicerBody' and @aria-label='Date Year']//div")
fil_dropdown.insert(1, r"//div[@class='slicerBody' and @aria-label='Commodity Group']//div")
fil_dropdown.insert(2, r"//div[@class='slicerBody' and @aria-label='Commodity']//div")
fil_dropdown.insert(3, r"//div[@class='slicerBody' and @aria-label='Sector']//div")
fil_dropdown.insert(4, r"//div[@class='slicerBody' and @aria-label='Country']//div")
fil_dropdown.insert(5, r"//div[@class='slicerBody' and @aria-label='Country/Region']//div")

#SQL file locators
total_sector_spend = "total_spend_by_sector.sql"
total_sensitivity_by_sector = "total_sensitivity_by_sector.sql"
total_bw_prior_year_sector = "total_bw_prior_year_by_sector.sql"
planned_spend_sql = "planned_spend.sql"
current_spend_sql = "current_spend.sql"
bw_v_prior_sql = "bw_v_prior.sql"
sensitivity_sql = "sensitivity.sql"
drillthrough_sql = "graph_drillthrough.sql"
executive_db_sql = "executive_db.sql"
sample_table_sql = "sample_table.sql"
slider_tc_sql = "slider_tc.sql"