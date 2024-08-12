import os
import time
import allure
from selenium.webdriver import ActionChains
from selenium.webdriver.common.by import By

from tests.resources.saucedemoobjectrepository import *
from .base_page import BasePageElement
from utilities.CustomLogging import getLogger

log = getLogger()


class SauceDemoProductPage(BasePageElement):
    def __init__(self, driver):
        super().__init__(driver)
        self.driver = driver
        self.action = ActionChains(self.driver)

    def click_add_to_cart(self):
        time.sleep(3)
        log.info("Click add to cart")
        with allure.step("Click add to cart"):
            self.click_by_locator(By.XPATH, addToCart)

    def click_go_to_cart(self):
        time.sleep(3)
        log.info("Click go to cart")
        with allure.step("Click go to cart"):
            self.click_by_locator(By.XPATH, goToCart)

    def click_remove_from_cart(self):
        time.sleep(3)
        log.info("Click remove from cart")
        with allure.step("Click remove from cart"):
            self.click_by_locator(By.XPATH, removeFromCart)

    def click_continue_shopping(self):
        time.sleep(3)
        log.info("Click continue shopping")
        with allure.step("Click continue shopping"):
            self.click_by_locator(By.XPATH, continueShopping)

    def get_no_of_items(self):
        listOfItemsInCart = self.find_elements_by_locator(By.XPATH,listOfItems)
        return listOfItemsInCart

    def verify_item(self,requireditem):
        flag = False
        for item in self.get_no_of_items():
            if item.text == requireditem:
                flag = True
            else:
                flag = False
        return flag