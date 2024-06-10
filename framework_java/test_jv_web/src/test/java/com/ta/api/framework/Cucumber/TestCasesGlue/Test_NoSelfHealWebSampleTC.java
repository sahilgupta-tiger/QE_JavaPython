package com.ta.api.framework.Cucumber.TestCasesGlue;

import com.ta.api.framework.api.API_CommonMethods;
import com.ta.api.framework.apicoreutils.APICommonActions;
import com.ta.api.framework.apicoreutils.ApiCore;
import com.ta.api.framework.healenium.NoSelfHealSampleWebCommonActions;
import com.ta.api.framework.helperfunctions.CommonUtils;
import com.ta.api.framework.testbase.TestBaseSetUp;
import com.ta.api.framework.web.wrappers.GenericWrapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.Map;


public class Test_NoSelfHealWebSampleTC extends TestBaseSetUp {

    private static final Logger logger = LogManager.getLogger(API_CommonMethods.class);
    protected static NoSelfHealSampleWebCommonActions commonMethods;
    protected static APICommonActions api;
    protected static ApiCore apiCore;
    protected static CommonUtils commonUtils;
    protected static GenericWrapper genericWrapper;

    public Test_NoSelfHealWebSampleTC() {
        api = new APICommonActions(commonPojo);
        apiCore = new ApiCore(commonPojo);
        commonMethods = new NoSelfHealSampleWebCommonActions(commonPojo);
        commonUtils = new CommonUtils();
        genericWrapper = new GenericWrapper();
    }

    @Test
    public void sampleWebTest(Map<String, Object> map) {
        System.out.println("hello test");
        try {
            this.identify_ui_elements(map);
            this.click_submit_button(map);
            this.verify_same_ui_elements(map);
        } catch (Exception e) {
            e.printStackTrace();
            commonMethods.objectRepository.genericWrapper().closeBrowser();
        }  finally {
            commonMethods.objectRepository.genericWrapper().closeBrowser();
        }

    }

    @Given("I can identify three UI fields on the website")
    public void identify_ui_elements(Map<String, Object> map) throws InterruptedException {
        commonMethods.HealeniumApplicationchecksOldLocator(map);
    }

    @When("I click the Change Locators button")
    public void click_submit_button(Map<String, Object> map) throws InterruptedException {
        commonMethods.HealeniumApplicationchecksNewLocator(map);
    }

    @Then("I should be able to identify the same three UI fields on the website")
    public void verify_same_ui_elements(Map<String, Object> map) throws InterruptedException {
        commonMethods.verify();
    }
}
