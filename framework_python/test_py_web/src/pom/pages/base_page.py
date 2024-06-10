import pyodbc
from selenium.webdriver import ActionChains, Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium import webdriver
from selenium.common.exceptions import ElementNotVisibleException,NoSuchElementException,StaleElementReferenceException
from utilities.CustomLogging import getLogger
import pandas as pd

log = getLogger()

class BasePageElement:
    """Base page class that is initialized on every page object class."""

    def __init__(self, driver):
        self.driver = driver
        self.wait = WebDriverWait(self.driver, 10)
        self.web = webdriver.Chrome

    def open_page(self, url):
        self.driver.get(url)

    def find_element_EC(self, locator):
        return self.wait.until(EC.presence_of_element_located(locator))

    def find_element_by_xpath(self, locator):
        try:
            return self.driver.find_element('xpath', locator)
        except ElementNotVisibleException as E:
            print(E)
            log.info(print(E))
        except NoSuchElementException as E:
            print(E)
            #log.info(print(E))
        except StaleElementReferenceException as E:
            print(E)
            log.info(print(E))

    def find_element_by_locator(self, locator1, locator):
        try:
            return self.driver.find_element( locator1, locator)
        except ElementNotVisibleException as E:
            print(E)
            log.info(print(E))
        except NoSuchElementException as E:
            print(E)
            log.info(print(E))
        except StaleElementReferenceException as E:
            print(E)
            log.info(print(E))

    def find_elements_by_locator(self, locator1, locator2):
        return self.driver.find_elements(locator1, locator2)

    def find_multiple_elements(self, locator):
        return self.wait.until(EC.presence_of_all_elements_located(locator))

    def is_displayed(self, locator1, locator2):
        try:
            return self.find_element_by_locator(locator1, locator2).is_displayed()
        except ElementNotVisibleException as E:
            print(E)
            log.info(print(E))

    def click_by_locator(self, locator1, locator2):
        try:
            return self.find_element_by_locator(locator1,locator2).click()
        except ElementNotVisibleException as E:
            print(E)
            log.info(print(E))

    def get_count_elements(self, locator):
        return len(self.find_multiple_elements(locator))

    def click(self, by_locator: object) -> object:
        self.wait.until(EC.presence_of_element_located(by_locator)).click()

    def send_keys(self, locator1, locator2, text):
        self.find_element_by_locator(locator1, locator2).send_keys(text)

    def get_title(self):
        return self.driver.title

    def get_attribute(self, locator, val):
        return self.find_element_by_xpath(locator).get_attribute(val)

    def read_from_sql_file(sfpath):
        log.info("Read SQL file and Execute SQL on Snowflake")
        #with allure.step("Read SQL file and Execute SQL on Snowflake"):
        fd = open(sfpath, "r+")
        wholesql = fd.read()

        # Replacing the Filter values in SQL statement


        #if wholesql.find("--$filter$") != -1:
            #wholesql = wholesql.replace("--$filter$", {str(tc_params[0])})


    def connectTosqlTest(self, sql):
        conn = pyodbc.connect('Driver={SQL Server};'
                              'Server=qe-vm1;'
                              'Database=SamplePowerBI;'
                              'Trusted_Connection=yes;')
        print(conn)
        df = pd.read_sql_query(sql, conn)
        return df

    def control_key_down(self):
        ActionChains(self.driver).key_down(Keys.CONTROL).perform()

    def control_key_up(self):
        ActionChains(self.driver).key_up(Keys.CONTROL).perform()

    def scroll_into_view(self, element):
        self.driver.implicitly_wait(2)
        return self.driver.execute_script("arguments[0].scrollIntoView()", element)

    def get_text_by_xpath(self, locator1, locator):
        try:
            return self.driver.find_element( locator1, locator).text
        except ElementNotVisibleException as E:
            print(E)
            log.info(print(E))
        except NoSuchElementException as E:
            print(E)
            log.info(print(E))
        except StaleElementReferenceException as E:
            print(E)
            log.info(print(E))

