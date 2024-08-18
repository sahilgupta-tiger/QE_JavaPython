from pathlib import Path
import allure
import time
from pytest_bdd import scenario, given, parsers, when, then
from selenium.webdriver.chrome.options import Options
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from src.pom.pages.page_tag_functions import PageTagFunctions
from tests.settings import *
import pytest
from webdriver_manager.firefox import GeckoDriverManager

featureFileDir = "../features"
featureFile = "tag_validation.feature"
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
        driver.get(hartford_url)
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


@allure.title("TC020_Grab Tag values from browser")
@allure.severity(allure.severity_level.BLOCKER)
@allure.feature("Tag Validation on Browser")
@scenario(FEATURE_FILE, "TC020_Grab Tag values from browser")
def test_report_tc20():
    pass


context = {}


@given(parsers.parse('Login to dashboard for Test Case: "{tcname}"'), target_fixture="driver")
def login_to_dashboard(tcname):
    context['tcname'] = tcname
    time.sleep(7)


@when(parsers.parse('Grab tag values from Console'))
def tag_values_console():
    tagobj = PageTagFunctions(driver)
    tagobj.console_output()


@when(parsers.parse("Grab tag values from Network"))
def tag_values_network():
    tagobj = PageTagFunctions(driver)




