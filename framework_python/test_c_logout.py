import re
from playwright.sync_api import Playwright, sync_playwright, expect


def test_8_logout_success(set_up):
    page = set_up
    expect(page.locator("[data-test=\"title\"]")).to_be_visible()
    page.get_by_role("button", name="Open Menu").click()
    page.locator("[data-test=\"logout-sidebar-link\"]").click()
    expect(page.locator("[data-test=\"login-button\"]")).to_be_enabled()

    # ---------------------
