import re
import pytest
from playwright.sync_api import Playwright, sync_playwright, expect



def test_1_login_success(set_up):
    page = set_up
    page.locator("[data-test=\"username\"]").click()
    page.locator("[data-test=\"username\"]").fill("standard_user")
    page.locator("[data-test=\"password\"]").click()
    page.locator("[data-test=\"password\"]").fill("secret_sauce")
    page.locator("[data-test=\"login-button\"]").click()
    expect(page.locator("[data-test=\"title\"]")).to_be_visible()



def test_2_login_invalid(set_up):
    page = set_up
    page.locator("[data-test=\"username\"]").click()
    page.locator("[data-test=\"username\"]").fill("error_user")
    page.locator("[data-test=\"password\"]").click()
    page.locator("[data-test=\"password\"]").fill("abc")
    page.locator("[data-test=\"login-button\"]").click()
    expect(page.locator("[data-test=\"error\"]")).to_be_visible()




