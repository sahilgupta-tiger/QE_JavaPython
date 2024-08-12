import os
import time
import allure
from selenium.webdriver import ActionChains
from selenium.webdriver.common.by import By

from tests.resources.saucedemoobjectrepository import *
from .base_page import BasePageElement
from utilities.CustomLogging import getLogger

log = getLogger()


class SauceDemoLoginPage(BasePageElement):

    def __init__(self, driver):
        super().__init__(driver)
        self.driver = driver
        self.action = ActionChains(self.driver)

    def saucedemo_login(self, username, password):
        time.sleep(3)
        log.info("Login into sauce demo using id and password")
        with allure.step("Login into sauce demo using id and password"):
            self.send_keys(By.ID, userName, username)
            self.send_keys(By.ID, passWord, password)

    def click_login(self):
        time.sleep(3)
        log.info("Click login button")
        with allure.step("Click login button"):
            self.click_by_locator(By.ID, loginButton)

    def get_error_msg(self):
        time.sleep(3)
        log.info("Get error message after login failure")
        with allure.step("Get error message after login failure"):
            error_message = self.get_text_by_xpath(By.XPATH, errorMessage)
            return error_message

