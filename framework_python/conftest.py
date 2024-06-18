import pytest
from playwright.sync_api import sync_playwright

@pytest.fixture(params=[ "firefox", "webkit"])
def set_up(request):
    browser_name = request.param
    playwright = sync_playwright().start()
    browser = playwright[browser_name].launch(headless=True)
    context = browser.new_context()
    context.clear_cookies()
    page = context.new_page()
    page.goto("https://www.saucedemo.com/")

    yield page
    context.close()
    browser.close()
    playwright.stop()

def python_collection_modifyitems(config,items):
    items.sort(key= lambda  item: item.name)

@pytest.fixture(params=["chromium", "firefox", "webkit"])
def browser(playwright_instance, request):
    browser = playwright_instance[request.param].launch(headless=False)
    yield browser
    browser.close()


