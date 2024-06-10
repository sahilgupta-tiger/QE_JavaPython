from pathlib import Path
from pytest_bdd import scenario, given, parsers, when, then
from selenium.webdriver.chrome.options import Options
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from utilities.dbconnect.sql_server_connect import read_from_sql_file, direct_read_from_sql, convert_value_MK
from tests.resources import objectrepository
from src.pom.pages.loginpowerbi import OpenPowerBIPage
from tests.runner.read_testselection import get_tc_parameters
from utilities.encryption import decode
from tests.settings import *
import pytest
from webdriver_manager.firefox import GeckoDriverManager
from src.pom.pages.executive_summary_dashboard import *

featureFileDir = "../features"
featureFile = "power_bi_dashboard.feature"
BASE_DIR = Path(__file__).resolve().parent
FEATURE_FILE = BASE_DIR.joinpath(featureFileDir).joinpath(featureFile)


@pytest.fixture(autouse=True)
def driver(request):
    global driver
    if browser == "chrome":
        chrome_options = Options()
        chrome_options.add_argument("--incognito")
        chrome_options.add_argument("--start-maximized")
        driver = webdriver.Chrome(options=chrome_options, service=Service(executable_path=chrome_loc))
        driver.implicitly_wait(5)
        driver.get(url)
    elif browser == "healenium":
        nodeURL = "http://localhost:8085"
        chrome_options = Options()
        chrome_options.add_argument("--incognito")
        chrome_options.add_argument("--start-maximized")
        driver = webdriver.Remote(command_executor=nodeURL, options=chrome_options)
        driver.implicitly_wait(5)
        driver.get(url)
    elif browser == "firefox":
        driver = webdriver.Firefox(GeckoDriverManager().install())
    elif browser == "headless":
        chrome_options = Options()
        chrome_options.add_argument("--disable-extensions")
        chrome_options.add_argument("--disable-gpu")
        chrome_options.add_argument("--headless")
        driver = webdriver.Chrome(options=chrome_options, service=Service(executable_path=chrome_loc))
    else:
        print("Driver not supported")
    driver.implicitly_wait(7)

    yield driver
    driver.quit()


@allure.title("TC_001_General Validation")
@allure.severity(allure.severity_level.BLOCKER)
@allure.feature("Validate Dashboard Availability")
@scenario(FEATURE_FILE, "TC_001_General Validation")
def test_report_tc1():
    pass


@allure.title("TC_002_Validation of single value widgets")
@allure.severity(allure.severity_level.NORMAL)
@allure.feature("Validate Single Value Widgets")
@scenario(FEATURE_FILE, "TC_002_Validation of single value widgets")
def test_report_tc2():
    pass


@allure.title("TC_003_Validation of executive summary")
@allure.severity(allure.severity_level.NORMAL)
@allure.feature("Validate Grid Details and Drill Through")
@scenario(FEATURE_FILE, "TC_003_Validation of executive summary")
def test_report_tc3():
    pass


@allure.title("TC_004_Validation of Total spend by sector graph")
@allure.severity(allure.severity_level.MINOR)
@allure.feature("Validate Bar Graph Widgets")
@scenario(FEATURE_FILE, "TC_004_Validation of Total spend by sector graph")
def test_report_tc4():
    pass


@allure.title("TC_005_Validation of Total sensitivity by sector graph")
@allure.severity(allure.severity_level.MINOR)
@allure.feature("Validate Bar Graph Widgets")
@scenario(FEATURE_FILE, "TC_005_Validation of Total sensitivity by sector graph")
def test_report_tc5():
    pass


@allure.title("TC_006_Validation of Total BW vs prior year graph")
@allure.severity(allure.severity_level.MINOR)
@allure.feature("Validate Bar Graph Widgets")
@scenario(FEATURE_FILE, "TC_006_Validation of Total BW vs prior year graph")
def test_report_tc6():
    pass


@allure.title("TC_007_Validation of drilldown")
@allure.severity(allure.severity_level.CRITICAL)
@allure.feature("Validate Grid Details and Drill Through")
@scenario(FEATURE_FILE, "TC_007_Validation of drilldown")
def test_report_tc7():
    pass


@allure.title("TC_008_Validation of slider")
@allure.severity(allure.severity_level.CRITICAL)
@allure.feature("Validate Bar Graph Widgets")
@scenario(FEATURE_FILE, "TC_008_Validation of slider")
def test_report_tc8():
    pass


@allure.title("TC_009_Validation of Sensitivity widget")
@allure.severity(allure.severity_level.NORMAL)
@allure.feature("Validate Single Value Widgets")
@scenario(FEATURE_FILE, "TC_009_Validation of Sensitivity widget")
def test_report_tc9():
    pass


context = {}


@given(parsers.parse('Login to dashboard for Test Case: "{tcname}"'), target_fixture="driver")
def login_to_dashboard(tcname):
    context['tcname'] = tcname
    pbiobj=OpenPowerBIPage(driver)
    pbiobj.power_BI_login(emailID, decode(encryptedPasscode))
    tc_params = get_tc_parameters(tcname)
    context['tcparams'] = tc_params
    srep = SummaryReportPage(driver)
    if tcname != "tc1":
        srep.selection_of_filters(tc_params)
    time.sleep(7)


@when(parsers.parse('Grab the dashboard name and labels'))
def grab_values():
    srep = SummaryReportPage(driver)
    dashboard_name, dashboard_headers, available_filters=srep.grab_generic_values()
    context['powerbi']=[dashboard_name, dashboard_headers, available_filters]


@then(parsers.parse('Validate the generic details of the dashboard matches expected values'))
def compare_values():
    powerbivalues=context['powerbi']
    with allure.step("Compare dashboard name, header and available filter names matches with expected values"):
        if (powerbivalues[0]==nameofreport) and \
           (powerbivalues[1]==header_names) and \
           (powerbivalues[2]==filter_names):
            print(powerbivalues[0]+"="+nameofreport)
            print(powerbivalues[1]," = ",header_names)
            print(powerbivalues[2]," = ", filter_names)
            assert True, "All values from PowerBI are matching with expected values."
        else:
            if (powerbivalues[0] != nameofreport):
                print(powerbivalues[0]+" == "+nameofreport)
            if (powerbivalues[1]!=header_names):
                print(powerbivalues[1])
                print(" == ")
                print(header_names)
            if (powerbivalues[2]!=filter_names):
                print(powerbivalues[2])
                print(" == ")
                print(filter_names)

            assert False, "There is a mismatch in values from PowerBI. Please check comparison in report"


@when(parsers.parse('Grab widget value from PowerBI: {locator1}'))
def widget_value(locator1):
    params=context['tcparams']
    xpath1=getattr(objectrepository, locator1)
    srep = SummaryReportPage(driver)
    planned_spend=srep.widget_value(xpath1)
    context['powerbi']=planned_spend


@when(parsers.parse('Grab widget value from db with sql: {sql1}'))
def widget_db_value(sql1):
    tcname = context['tcname']
    params = context['tcparams']
    sqlpath = getattr(objectrepository, sql1)
    with allure.step("Get values from DB by running the sql file"):
        sqlvalues = read_from_sql_file(sqlpath, params, tcname)
        context['db'] = sqlvalues


@then(parsers.parse('Comparison of widget value in PowerBI and db should match: {locator1},{sql1}'))
def compare_widgets_value(locator1, sql1):
    powerbivalue=context['powerbi']
    if type(powerbivalue) == list:
        powerbivalue.sort()
    print("Value from PowerBI for " + locator1 + ":")
    print(powerbivalue)
    sqldbvalue = context['db']
    print("Value from DB for " + sql1 + ":")
    print(sqldbvalue)
    context.clear()
    with allure.step("Comparison of widget value in PowerBI and db"):
        if powerbivalue == sqldbvalue:
            assert True, ("Both PowerBI" +locator1 +" and " +sql1+" DB values are matching as required.")
        else:
            assert False, ("Values from PowerBI" +locator1 +" and " +sql1+" are mismatching. Please check output in logs.")


@when(parsers.parse('Grab table grid values from PowerBI'))
def grab_table_values():
    srep = SummaryReportPage(driver)
    with allure.step("Grab table grid values from PowerBI"):
        table_values = srep.grab_executive_summary_bi()
        context['powerbi'] = table_values


@when(parsers.parse('Grab table grid values from db with sql: {sql1}'))
def grab_table_db_values(sql1):
    params = context['tcparams']
    sqlpath = getattr(objectrepository, sql1)
    with allure.step("Grab table grid values from db with sql"):
        sqldb_val = direct_read_from_sql(sqlpath, params)
        sqldb_val = [list(t) for t in sqldb_val]
        sqldb_val = [['' if x is None else x for x in sub_list] for sub_list in sqldb_val]
        context['db'] = sqldb_val


@then(parsers.parse('Comparison of table grid values in PowerBI and db should match'))
def compare_table_value():
    grid_bi_values = context['powerbi']
    grid_bi_values.sort()
    grid_db_values = context['db']
    grid_db_values.sort()
    for i in range(0, len(grid_db_values)):

        if grid_bi_values[i] == grid_db_values[i]:
            print("Row" + str(i))
            print(grid_bi_values[i])
            print(":: matches ::")
            print(grid_db_values[i])
        else:
            print("Row" + str(i))
            print(grid_bi_values[i])
            print(">-> NOT Matched <-<")
            print(grid_db_values[i])

    if grid_bi_values == grid_db_values:
        assert True, " Values are matching"
    else:
        assert False, "There is a mismatch in values from Power Bi bar graph and db." \
                           " Please check comparison in report."


@when(parsers.parse('Grab graph value from PowerBI with path: {locator1}'))
def grab_graph_values(locator1):
    params=context['tcparams']
    xpath1 = getattr(objectrepository, locator1)
    srep = SummaryReportPage(driver)
    with allure.step("Grab values from PowerBI"):
        graph_bi_values = srep.bar_graph_value(xpath1)
        context['powerbi']=graph_bi_values


@when(parsers.parse('Grab corresponding value from db with sql: {sql1}'))
def grab_db_graph_values(sql1):
    tcname = context['tcname']
    params = context['tcparams']
    sqlpath1=getattr(objectrepository, sql1)
    with allure.step("Grab the graph values from DB"):
        val_db=read_from_sql_file(sqlpath1, params, tcname)
        log.info(val_db)
        context['db']=val_db


@then(parsers.parse('Comparision of graph value in PowerBI and db should match'))
def compare_graph_values():
    powerbivalues = context['powerbi']
    if type(powerbivalues) == list:
        powerbivalues.sort()
    print("Values from powerbi:")
    print(powerbivalues)
    dbvalues = context['db']
    print("Values from db:")
    print(dbvalues)
    context.clear()
    with allure.step("Comparison of graph value in PowerBI and db should match"):
        if powerbivalues == dbvalues:
            results_text = "All values in Power Bi bar graph and db are matching."
            assert True, results_text
        else:
            results_text = "There is a mismatch in values from Power Bi bar graph and db." \
                           " Please check comparison in report."
            assert False, results_text


@when(parsers.parse('Drilldown function is selected for a graph: {locator1}, {locator2}'))
def grab_drilldown(locator1, locator2):
    params = context['tcparams']
    xpath1 = getattr(objectrepository, locator1)
    xpath2 = getattr(objectrepository, locator2)
    srep = SummaryReportPage(driver)
    drilldown_values = []
    with allure.step("Grab values from PowerBI"):
        firsttable, secondtable = srep.drillthrough(xpath1, xpath2)
        firsttable.sort()
        secondtable.sort()
        drilldown_values.append(firsttable)
        drilldown_values.append(secondtable)
        print("Values from powerbi:")
        print(drilldown_values)
        context['powerbi'] = drilldown_values


@when(parsers.parse('Corresponding drilldown value is fetched from the db: {sql1}'))
def grab_drilldown_sql(sql1):
    tcname = context['tcname']
    params = context['tcparams']
    sql_path = getattr(objectrepository, sql1)
    db_values = direct_read_from_sql(sql_path, params)
    db_values = [list(t) for t in db_values]
    print("Values from db:")
    print(db_values)
    context['db'] = db_values


@then(parsers.parse('Compare drilldown values from BI and DB'))
def validate_drilldown():
    with allure.step('Compare drilldown values from BI and DB'):
        drilldown_BI = context['powerbi']
        drilldown_DB = context['db']
        assert drilldown_BI[0] == drilldown_DB and drilldown_BI[1] == drilldown_DB, \
            "Values are not matching. Please check output logs in report."


@when(parsers.parse('Slider mininum & maximum value is selected: {min}, {max}'))
def select_slider(min, max):
    time.sleep(5)
    srep = SummaryReportPage(driver)
    srep.slider(slider_min_locator, slider_max_locator, min, max)


@when(parsers.parse('Graph to be validated is located at {locator1} with {sql1}'))
def get_bi_sql_values(locator1, sql1):
    params = context['tcparams']
    tcname = context['tcname']
    xpath1 = getattr(objectrepository, locator1)
    sql1 = getattr(objectrepository, sql1)
    srep = SummaryReportPage(driver)
    with allure.step("Grab values from PowerBI"):
        power_bi_val = srep.bar_graph_value(xpath1)
        power_bi_val.sort()
        print("value from powerbi")
        print(power_bi_val)
        context['powerbi'] = power_bi_val

    with allure.step("Grab values from DB"):
        db_val = direct_read_from_sql(sql1, params)
        db_ls=[]
        for i in db_val:
            db_ls.append(i[1])
        db_ls.sort()
        print("Value from DB")
        print(db_ls)
        context['db'] = db_ls


@then(parsers.parse('Comparison of graph value with slider selection should match'))
def check_slider_values():
    with allure.step('Compare slider values from BI and DB'):
        slider_BI = context['powerbi']
        slider_DB = context['db']
        assert slider_BI == slider_DB, \
            "Values are not matching. Please check output logs in report."


@when(parsers.parse('Grab Sensitivity widget from db with sql: {sql1}'))
def sensitivity_widget_db(sql1):
    params = context['tcparams']
    sqlpath = getattr(objectrepository, sql1)
    with allure.step("Get values from DB by running the sql file"):
        sqlvalues = direct_read_from_sql(sqlpath, params)
        if len(sqlvalues) == 1:
            actualvalue = convert_value_MK(int(sqlvalues[0]))
            context['db'] = actualvalue
        else:
            context['db'] = sqlvalues
