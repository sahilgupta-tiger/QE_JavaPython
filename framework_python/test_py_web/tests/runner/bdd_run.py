import os
import sys
import pytest
from tests.runner.read_testselection import *

allure_path = f"{os.getcwd()}/utilities/helper/allure-2.24.1/bin"
os.environ["PATH"] = allure_path + os.pathsep + os.environ["PATH"]
allure_home = f"{os.getcwd()}/utilities/helper/allure-2.24.1"
os.environ["ALLURE_HOME"] = allure_home
var = os.getenv("testcases")
if var is not None:
    scenarioexecute = get_execute_tc_args(var)
else:
    scenarioexecute = get_scenario_read_csv()

__dir = os.path.dirname(os.path.realpath(__file__))
XML_DIR = os.path.join((os.path.dirname(__dir)), "tests_results")
xml_report_path = os.path.join(XML_DIR, "integration.xml")
report_dir = os.path.join(os.path.dirname(__dir), "tests_results", "reports")
allure_report = os.path.join(report_dir, "allure_results")
test_dirs = os.path.join(__dir, "../test_cases")
use_allure_bdd = "-p no:allure_pytest"
no_of_executors = f"-n {get_parallel_executors_csv(scenarioexecute)}"

parallel_test_args = [
    "--junitxml=" + xml_report_path,
    "--alluredir=" + allure_report,
    test_dirs, scenarioexecute, no_of_executors
    ]

res = pytest.main(parallel_test_args)

sys.exit(res)
