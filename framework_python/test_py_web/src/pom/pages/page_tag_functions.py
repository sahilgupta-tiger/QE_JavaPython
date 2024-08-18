import time
import allure
from selenium.webdriver import Keys
from selenium.webdriver.common.by import By
from tests.resources.objectrepository import *
from .base_page import BasePageElement
from selenium.webdriver.common.action_chains import ActionChains
from utilities.CustomLogging import getLogger
import selenium.devtools.DevTools

log = getLogger()


class PageTagFunctions(BasePageElement):
    """Base page class that is initialized on every page object class."""

    def __init__(self, driver):
        super().__init__(driver)
        self.driver = driver
        self.action = ActionChains(self.driver)


    def console_output(self):
        time.sleep(7)
        log.info("Starting the Chrome DevTools session")


    def network_output(self):
        time.sleep(7)
        log.info("Starting the Chrome DevTools session")

