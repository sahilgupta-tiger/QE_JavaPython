import pytest
from playwright.sync_api import Playwright


# Fixture for launching and closing the browser
@pytest.fixture()
def set_up(playwright: Playwright):
    browser = playwright.webkit.launch(headless=False,slow_mo=1000)
    context = browser.new_context()
    context.clear_cookies()
    page = context.new_page()
    page.goto("https://www.saucedemo.com/")
    page.locator("[data-test=\"username\"]").click()
    page.locator("[data-test=\"username\"]").fill("standard_user")
    page.locator("[data-test=\"password\"]").click()
    page.locator("[data-test=\"password\"]").fill("secret_sauce")
    page.locator("[data-test=\"login-button\"]").click()
    yield page
    context.close()
    browser.close()

def python_collection_modifyitems(config,items):
    items.sort(key= lambda  item: item.name)


