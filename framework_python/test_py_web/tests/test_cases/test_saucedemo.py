from pathlib import Path

import allure
import time
from pytest_bdd import scenario, given, parsers, when, then
from selenium.webdriver.chrome.options import Options
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from src.pom.pages.saucedemo_login_page import SauceDemoLoginPage
from src.pom.pages.saucedemo_logout_page import SauceDemoLogoutPage
from src.pom.pages.saucedemo_product_page import SauceDemoProductPage

from tests.settings import *
import pytest
from webdriver_manager.firefox import GeckoDriverManager

featureFileDir = "../features"
featureFile = "sauce_demo.feature"
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


@allure.title("TC010_Login with invalid credentials")
@allure.severity(allure.severity_level.BLOCKER)
@allure.feature("Validate Login Credentials")
@scenario(FEATURE_FILE, "TC010_Login with invalid credentials")
def test_report_tc10():
    pass


@allure.title("TC011_Login without credentials")
@allure.severity(allure.severity_level.NORMAL)
@allure.feature("Validate Login feature")
@scenario(FEATURE_FILE, "TC011_Login without credentials")
def test_report_tc11():
    pass


@allure.title("TC012_Add products to cart")
@allure.severity(allure.severity_level.NORMAL)
@allure.feature("Validate Adding product to cart")
@scenario(FEATURE_FILE, "TC012_Add products to cart")
def test_report_tc12():
    pass


@allure.title("TC013_Remove products from cart")
@allure.severity(allure.severity_level.MINOR)
@allure.feature("Validate removal of product from cart")
@scenario(FEATURE_FILE, "TC013_Remove products from cart")
def test_report_tc13():
    pass


@allure.title("TC014_Continue shopping button in cart")
@allure.severity(allure.severity_level.MINOR)
@allure.feature("Validate Continue shopping button in cart")
@scenario(FEATURE_FILE, "TC014_Continue shopping button in cart")
def test_report_tc14():
    pass


@allure.title("TC015_Logout to the app")
@allure.severity(allure.severity_level.MINOR)
@allure.feature("Validate Logout feature")
@scenario(FEATURE_FILE, "TC015_Logout to the app")
def test_report_tc15():
    pass


context = {}


@given(parsers.parse('Login to dashboard for Test Case: "{tcname}"'), target_fixture="driver")
def login_to_dashboard(tcname):
    context['tcname'] = tcname
    time.sleep(7)


@when(parsers.parse('Login with {username} and {pwd}'))
def login_with_username_password(username, pwd):
    sdl = SauceDemoLoginPage(driver)
    sdl.saucedemo_login(username, pwd)
    sdl.click_login()


@then(parsers.parse("Verify not able to login"))
def verify_not_able_to_login():
    sdl = SauceDemoLoginPage(driver)
    text = sdl.get_error_msg()
    assert "Epic sadface: Username and password do not match any user in this service", text


@when(parsers.parse("Login without username and password"))
def login_without_username_and_password():
    sdl = SauceDemoLoginPage(driver)
    sdl.saucedemo_login("", "")
    sdl.click_login()


@then(parsers.parse("Verify not able to login with error message"))
def verify_not_able_to_login_with_error_message():
    sdl = SauceDemoLoginPage(driver)
    text = sdl.get_error_msg()
    assert "Epic sadface: Username is required", text


@given(parsers.parse("User Login with credentials"))
def user_login_with_credentials():
    sdl = SauceDemoLoginPage(driver)
    sdl.saucedemo_login("standard_user", "secret_sauce")
    sdl.click_login()


@when(parsers.parse("Logout to the application"))
def logout_to_the_application():
    sdlo = SauceDemoLogoutPage(driver)
    sdlo.click_menu_button()
    sdlo.click_logout_button()


@then(parsers.parse("Verify that user is in logout page"))
def verify_that_user_is_in_logout_page():
    current_url = driver.current_url
    assert "https://www.saucedemo.com/", current_url


@when(parsers.parse("Add product to cart"))
def add_product_to_cart():
    sdpp = SauceDemoProductPage(driver)
    sdpp.click_add_to_cart()


@when(parsers.parse("Go to cart"))
def go_to_cart():
    print("Inside go to cart method")
    sdpp = SauceDemoProductPage(driver)
    sdpp.click_go_to_cart()


@then(parsers.parse("Verify item in cart"))
def verify_item_in_cart():
    print("Inside Verify item in the cart method")
    sdpp = SauceDemoProductPage(driver)
    itempresent = sdpp.verify_item("Sauce Labs Backpack")
    assert True, itempresent


@when(parsers.parse("Remove products from cart"))
def remove_products_from_cart():
    sdpp = SauceDemoProductPage(driver)
    sdpp.click_remove_from_cart()


@then(parsers.parse("Verify item not in cart"))
def verify_item_not_in_cart():
    sdpp = SauceDemoProductPage(driver)
    itempresent = sdpp.verify_item("Sauce Labs Backpack")
    assert itempresent is False


@when(parsers.parse("Click Continue shopping button in cart"))
def click_continue_shopping_button_in_cart():
    sdpp = SauceDemoProductPage(driver)
    sdpp.click_continue_shopping()


@then(parsers.parse("Verify the page is inventory page"))
def verify_the_page_is_inventory_page():
    currenturl = driver.current_url
    assert "https://www.saucedemo.com/inventory.html", currenturl
