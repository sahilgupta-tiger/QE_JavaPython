import re
from playwright.sync_api import Playwright, sync_playwright, expect


def test_3_productview(set_up):
    page = set_up
    page.locator("[data-test=\"item-4-title-link\"]").click()
    #Validating the product details on the product page
    expect(page.locator("[data-test=\"item-sauce-labs-backpack-img\"]")).to_be_visible()
    expect(page.locator("[data-test=\"inventory-item-name\"]")).to_be_visible()
    expect(page.locator("[data-test=\"inventory-item-desc\"]")).to_be_visible()
    page.locator("[data-test=\"back-to-products\"]").click()
    #Navigating back to products page
    expect(page.locator("[data-test=\"title\"]")).to_be_visible()

def test_4_cartsize(set_up):
    page = set_up
    page.locator("[data-test=\"add-to-cart-sauce-labs-bike-light\"]").click()
    page.locator("[data-test=\"add-to-cart-sauce-labs-backpack\"]").click()
    # Checking the cart size
    expect(page.locator("[data-test=\"shopping-cart-link\"]")).to_have_text('2')

    page.locator("[data-test=\"shopping-cart-link\"]").click()
    #checking for items in cart
    expect(page.locator("[data-test=\"item-0-title-link\"]")).to_be_visible()
    expect(page.locator("[data-test=\"item-4-title-link\"]")).to_be_visible()

def test_5_removeitem(set_up):
    page = set_up
    page.locator("[data-test=\"add-to-cart-sauce-labs-bolt-t-shirt\"]").click()
    page.locator("[data-test=\"add-to-cart-sauce-labs-fleece-jacket\"]").click()
    page.locator("[data-test=\"shopping-cart-link\"]").click()
    expect(page.locator("[data-test=\"item-1-title-link\"]")).to_be_visible()
    expect(page.locator("[data-test=\"item-5-title-link\"]")).to_be_visible()
    page.locator("[data-test=\"remove-sauce-labs-bolt-t-shirt\"]").click()
    #Verify removed item is not available
    expect(page.locator("[data-test=\"item-1-title-link\"]")).not_to_be_visible()

def test_6_checkout(set_up):
    page = set_up
    page.locator("[data-test=\"add-to-cart-sauce-labs-backpack\"]").click()
    page.locator("[data-test=\"add-to-cart-sauce-labs-bike-light\"]").click()
    page.locator("[data-test=\"shopping-cart-link\"]").click()
    page.locator("[data-test=\"checkout\"]").click()
    page.locator("[data-test=\"firstName\"]").click()
    page.locator("[data-test=\"firstName\"]").fill("abc")
    page.locator("[data-test=\"lastName\"]").click()
    page.locator("[data-test=\"lastName\"]").fill("abc")
    page.locator("[data-test=\"postalCode\"]").click()
    page.locator("[data-test=\"postalCode\"]").fill("10000")
    page.locator("[data-test=\"continue\"]").click()
    page.locator("[data-test=\"finish\"]").click()
    expect(page.locator("[data-test=\"complete-header\"]")).to_be_visible()

def test_7_continueshopping(set_up):
        page = set_up
        page.locator("[data-test=\"add-to-cart-sauce-labs-onesie\"]").click()
        page.locator("[data-test=\"shopping-cart-link\"]").click()
        page.locator("[data-test=\"remove-sauce-labs-onesie\"]").click()
        page.locator("[data-test=\"continue-shopping\"]").click()
        expect(page.locator("[data-test=\"title\"]")).to_be_visible()

