import time
import allure
from selenium.webdriver import Keys
from selenium.webdriver.common.by import By
from tests.resources.objectrepository import *
from .base_page import BasePageElement
from selenium.webdriver.common.action_chains import ActionChains
from utilities.CustomLogging import getLogger

log = getLogger()


class SummaryReportPage(BasePageElement):
    """Base page class that is initialized on every page object class."""

    def __init__(self, driver):
        super().__init__(driver)
        self.driver = driver
        self.action = ActionChains(self.driver)


    def reset_Filter_Report(self):
        time.sleep(15)
        log.info("Reset Filter")
        with allure.step("Reset Filter"):
            ele = self.find_element_by_locator(By.XPATH, resetFilter)
            self.action.click(ele).perform()
            time.sleep(2)

    def selection_of_filters(self, tc_params):
        self.reset_Filter_Report()
        year_params=[]
        commodity_group_params=[]
        commodity_params=[]
        sector_params=[]
        country_params=[]
        coun_region_params=[]
        list_params_dropdown=[]
        for i in range(0, len(tc_params)):
            if tc_params[i].find("|") != -1:
                if i == 0:
                    year_params = tc_params[i].split("|")
                    list_params_dropdown.append(year_params)
                elif i == 1:
                    commodity_group_params = tc_params[i].split("|")
                    list_params_dropdown.append(commodity_group_params)
                elif i == 2:
                    commodity_params = tc_params[i].split("|")
                    list_params_dropdown.append(commodity_params)
                elif i == 3:
                    sector_params = tc_params[i].split("|")
                    list_params_dropdown.append(sector_params)
                elif i == 4:
                    country_params = tc_params[i].split("|")
                    list_params_dropdown.append(country_params)
                elif i == 5:
                    coun_region_params = tc_params[i].split("|")
                    list_params_dropdown.append(coun_region_params)
            else:
                list_params_dropdown.append(tc_params[i])

        for j in range(0, len(tc_params)):
            time.sleep(2)
            dd_ele = f"{dropdown}[{str(j + 1)}]"
            self.click_by_locator(By.XPATH, dd_ele)
            if tc_params[j] != "All":
                time.sleep(1)
                if isinstance(list_params_dropdown[j], list):
                    self.control_key_down()
                    for val in list_params_dropdown[j]:

                        ele_xpath = f"{fil_dropdown[j]}[@title='{str(val)}']"
                        all_elements=self.find_elements_by_locator(By.XPATH, fil_dropdown[j])
                        log.info(f">>> For filter option '{str(val)}' there are '"
                                 f"{str(len(all_elements))}' items in dropdown")
                        if not bool(self.find_element_by_xpath(ele_xpath)):
                            for a, b in enumerate(all_elements):
                                self.scroll_into_view(b)
                                if bool(self.find_element_by_xpath(ele_xpath)):
                                    another_list = self.find_elements_by_locator(By.XPATH, fil_dropdown[j])
                                    for z in range(a, a+3):
                                        self.scroll_into_view(another_list[z])
                                        time.sleep(1)
                                    break

                        self.click_by_locator(By.XPATH, ele_xpath)
                        time.sleep(2)
                    self.control_key_up()
                    time.sleep(2)
                else:
                    ele = f"{fil_dropdown[j]}[@title='{str(tc_params[j])}']"
                    all_elements = self.find_elements_by_locator(By.XPATH, fil_dropdown[j])
                    log.info(f">>> For filter option '{str(tc_params[j])}' there are '"
                             f"{str(len(all_elements))}' items in dropdown")
                    if not bool(self.find_element_by_xpath(ele)):
                        for a, b in enumerate(all_elements):
                            self.scroll_into_view(b)
                            if bool(self.find_element_by_xpath(ele)):
                                another_list = self.find_elements_by_locator(By.XPATH, fil_dropdown[j])
                                for z in range(a, a+3):
                                    self.scroll_into_view(another_list[z])
                                    time.sleep(1)
                                break
                    time.sleep(2)
                    self.click_by_locator(By.XPATH, ele)
                    time.sleep(3)
            # click to close the dropdown window
            self.click_by_locator(By.XPATH, dd_ele)

    def grab_generic_values(self):
        time.sleep(3)
        log.info("Validate availability of the dashboard")
        with allure.step("Validate availability of the dashboard"):
            nameinpowerbi = self.find_element_by_locator(By.XPATH, reportName).text
        with allure.step("Verify availability of the headers"):
            ls_headers=[]
            header_elements = self.find_elements_by_locator(By.XPATH, header_eles_xpath)
            for i in range(0,len(header_elements)):
                ls_headers.append(header_elements[i].text)
        with allure.step("Verify availability of the filters"):
            ls_filters = []
            filter_elements = self.find_elements_by_locator(By.XPATH, filter_eles_xpath)
            for i in range(0,len(filter_elements)):
                ls_filters.append(filter_elements[i].text)
        return nameinpowerbi, ls_headers, ls_filters

    def grab_executive_summary_bi(self):
        log.info("Grab values from Executive Summary Table Grid")
        #All commodities
        len_all_ele=len(self.find_elements_by_locator(By.XPATH, commodityExecutive))
        #Total number of headers
        header_count = len(self.find_elements_by_locator(By.XPATH, rowExecutive))
        #All commodity names
        commodity_names=self.find_elements_by_locator(By.XPATH, commodity_name_xpath)
        cm=[]
        for i in commodity_names:
            cm.append(i.text)
        #Getting last row Total values
        total_row_values=[]
        final_row_header=self.find_element_by_locator(By.XPATH,total_row_xpath+str(len_all_ele+2)+total_header_xpath).text
        final_row_ele=self.find_elements_by_locator(By.XPATH, total_row_xpath+str(len_all_ele+2)+total_ind_ele_xpath)
        for j in final_row_ele:
            total_row_values.append(j.text)
        total_row_values.insert(0,final_row_header)
        ls_all_elements=[]
        for i in range(1, len_all_ele+1):
            for j in range(1, header_count):
                ele=self.find_element_by_locator(By.XPATH, executive_summary+str(j)+"])[" + str(i) + "]")
                ls_all_elements.append(ele.text)
        sublists= [ls_all_elements[i:i+header_count-1] for i in range(0,len(ls_all_elements), header_count-1)]
        final_bi_list=[]
        for i in range(len(cm)):
            final_bi_list.append([cm[i]] + sublists[i])
        final_bi_list.append(total_row_values)
        empty_val=""
        final_bi_list=[[empty_val if x == " " else x for x in sub_list] for sub_list in final_bi_list]
        return final_bi_list

    def drillthrough_elements(self, graph_xpath, table_xpath):
        time.sleep(4)
        dd_values_table1=[]
        dd_values_table2=[]
        j=0
        if (len(self.find_elements_by_locator(By.XPATH, graph_xpath)) > 1):
            source_eles = self.find_elements_by_locator(By.XPATH, graph_xpath)
            print(len(source_eles))
            for i in range(0, len(source_eles)):
                print(source_eles[i])
                table1, table2=self.drillthrough(source_eles[i], table_xpath)
                print(table1)
                print(table2)
                print(j)
                dd_values_table1.append(table1)
                dd_values_table2.append(table2)
                time.sleep(2)
                self.find_element_by_locator(By.XPATH,back_button).click()
                time.sleep(7)
                self.find_element_by_locator(By.TAG_NAME,'body').send_keys(Keys.COMMAND + 'r')
                time.sleep(7)
                j=j+1

        return dd_values_table1, dd_values_table2

    def drillthrough(self, graph_xpath, table_xpath):
        # element
        time.sleep(4)
        source = self.find_element_by_xpath(graph_xpath)
        # action chain object
        self.action.context_click(source).perform()
        time.sleep(2)
        self.find_element_by_xpath(drillthrough_xpath).click()
        time.sleep(2)
        self.find_element_by_xpath(drillthrough_sec_xpath).click()
        time.sleep(10)

        row_cnt = self.find_element_by_xpath(table_xpath).get_attribute("aria-rowcount")
        col_cnt = self.find_element_by_xpath(table_xpath).get_attribute("aria-colcount")
        print("Row Count: " + row_cnt + " Col Count: " + col_cnt)
        firstrow = []
        secondrow = []
        for i in range(2, int(row_cnt) + 1):
            tup_first_table = []
            tup_second_table = []
            for j in range(1, int(col_cnt) + 1):
                xpath = row_xpath + str(i) + "]//*[local-name()='div' and @aria-colindex=" + str(j) + "]"
                tup_first_table.append(self.find_element_by_xpath(xpath).text)
                #firstrow.append(self.find_element_by_xpath(xpath).text)
                xpath_2 = col_xpath + str(i) + "]//*[local-name()='div' and @aria-colindex=" + str(j) + "]"
                tup_second_table.append(self.find_element_by_xpath(xpath_2).text)
                #secondrow.append(self.find_element_by_xpath(xpath_2).text)
            firstrow.append(tup_first_table)
            secondrow.append(tup_second_table)
        return firstrow, secondrow

    def bar_graph_value(self, graph_val_xpath):
        time.sleep(4)
        log.info("Grab data from PowerBI dashboard")
        with allure.step("Get the data of Total spend by sector graph from PowerBI"):
            tss_elements=self.find_elements_by_locator(By.XPATH,graph_val_xpath)
            tss_values=[]
            for i in tss_elements:
                tss_values.append(i.text)
            log.info(tss_values)
            return tss_values

    def widget_value(self, widget_xpath):
        time.sleep(2)
        log.info("Validation of widget values")
        with allure.step("Get the value of the widget from the PowerBI dashboard"):
            log.info("Value grabbed from the PowerBI are below=")
            widget_val=self.get_text_by_xpath(By.XPATH, widget_xpath)
            log.info(widget_val)
            return widget_val

    def slider(self, slider_min_xpath, slider_max_xpath, min, max):
        time.sleep(2)
        log.info("Selecting the slider")
        min = int(min)
        max = int(max)
        with allure.step("Select slider"):
            slider_min_ele=self.find_element_by_locator(By.XPATH, slider_min_xpath)
            slider_min_ele.send_keys(Keys.CONTROL, "a")
            slider_min_ele.send_keys(Keys.DELETE)
            time.sleep(2)
            slider_min_ele.send_keys(min)
            time.sleep(2)
            slider_min_ele.send_keys(Keys.TAB)
            time.sleep(2)
            slider_max_ele=self.find_element_by_locator(By.XPATH, slider_max_xpath)
            slider_max_ele.send_keys(Keys.CONTROL, "a")
            slider_max_ele.send_keys(Keys.DELETE)
            time.sleep(2)
            slider_max_ele.send_keys(max)
            time.sleep(2)
            slider_max_ele.send_keys(Keys.RETURN)
            time.sleep(2)

    def slider_2(self):
        ele=self.find_element_by_locator(By.XPATH,"//*[@id='pvExplorationHost]/div/div/exploration/div/explore-canvas/div/div[2]/div/div[2]/div[2]/visual-container-repeat/visual-container[31]/transform/div/div[3]/div/div/visual-modern/div/div/div[2]/div/div[2]/div")
        action=ActionChains(self)
        action.click_and_hold(ele).move_by_offset(250,0).release().perform()


