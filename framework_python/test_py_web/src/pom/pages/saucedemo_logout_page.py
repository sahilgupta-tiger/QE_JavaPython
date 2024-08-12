import os
import time
import allure
from selenium.webdriver import ActionChains
from selenium.webdriver.common.by import By

from tests.resources.saucedemoobjectrepository import *
from .base_page import BasePageElement
from utilities.CustomLogging import getLogger

log = getLogger()


class SauceDemoLogoutPage(BasePageElement):
    def __init__(self, driver):
        super().__init__(driver)
        self.driver = driver
        self.action = ActionChains(self.driver)

    def click_menu_button(self):
        time.sleep(3)
        log.info("Click Menu button")
        with allure.step("Click Menu button"):
            self.click_by_locator(By.ID, menuButton)

    def click_logout_button(self):
        time.sleep(3)
        log.info("Click Logout button")
        with allure.step("Click Logout button"):
            self.click_by_locator(By.XPATH, logoutButton)