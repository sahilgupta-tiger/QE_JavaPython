import os
import time
import allure
from selenium.webdriver.common.by import By
from .base_page import BasePageElement
from utilities.CustomLogging import getLogger
from tests.resources.objectrepository import *

log = getLogger()


class OpenPowerBIPage(BasePageElement):
    """Base page class that is initialized on every page object class."""

    def __init__(self, driver):
        super().__init__(driver)
        self.driver = driver


    def power_BI_login(self, emailID, password):
        time.sleep(3)
        if len(self.find_elements_by_locator(By.XPATH,emailIDpath))>0:
            log.info("Login into power bi using id")
            with allure.step("Login into power bi using id"):
                self.send_keys(By.XPATH, emailIDpath, emailID)
                self.click_by_locator(By.XPATH, submitBtn)

        else:
            log.info("Login into power bi using id")
            with allure.step("Login into power bi using id"):
                self.send_keys(By.XPATH, newemail, emailID)
                self.click_by_locator(By.XPATH, newemailsubmit)

        time.sleep(5)
        log.info("Login into power bi using encrypted password")
        with allure.step("Login into power bi using encrypted password"):
            self.send_keys(By.XPATH, passwd, password)
            self.click_by_locator(By.XPATH, submitBtn)
        time.sleep(3)
        log.info("Clicked the extra pop up in login screen")
        with allure.step("Clicked the extra pop up in login screen"):
            if len(self.find_elements_by_locator(By.XPATH, extraPopup)) > 0:
                self.click_by_locator(By.XPATH, submitBtn)


    def test(self, emailID):
        time.sleep(3)
        self.find_element_by_locator(By.XPATH, emailID)
