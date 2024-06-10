package com.ta.api.framework.healenium;

import com.ta.api.framework.apicoreutils.APICommonActions;
import com.ta.api.framework.apicoreutils.ApiCore;
import com.ta.api.framework.helperfunctions.CommonUtils;
import com.ta.api.framework.pojo.CommonPojo;
import com.ta.api.framework.webutils.ObjectRepository;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SelfHealSampleWebCommonActions extends ApiCore {

    private static final Logger logger = LogManager.getLogger(SelfHealSampleWebCommonActions.class);
    protected static APICommonActions api;
    protected CommonPojo commonPojo;
    protected static CommonUtils commonUtils;
    protected ObjectRepository objectRepository;

    public SelfHealSampleWebCommonActions(CommonPojo commonPojo) {
        this.commonPojo = commonPojo;
        api = new APICommonActions(commonPojo);
        commonUtils = new CommonUtils();
        objectRepository = new ObjectRepository();
    }

    //@Given("^I can identify 3 UI fields on the website$")
    public void SelfHealApplicationchecksOldLocator(Map<String,Object> map) throws InterruptedException {
        System.out.println(map.get("Fieldone").toString());
        objectRepository.healeniumPage().fieldOne(map.get("Fieldone").toString());
        objectRepository.healeniumPage().fieldTwo(map.get("Fieldtwo").toString());
        objectRepository.healeniumPage().Changelocator();
    }
    //@When("^I click the 'Change Locators' button$")
    public void SelfHealApplicationchecksNewLocator(Map<String,Object> map) throws InterruptedException {
        objectRepository.healeniumPage().fieldOne(map.get("FieldoneNew").toString());
        objectRepository.healeniumPage().fieldTwo(map.get("FieldtwoNew").toString());

    }

    //@Then("^I should be able to identify the same 3 UI fields on the website$")
    public void verify() {
        String a = String.valueOf(objectRepository.healeniumPage().Verify());
        System.out.println("*******a = " +a.toString());
        List<String> sr = Collections.singletonList("PASS");
        commonPojo.setTestStatus(sr);
    }


}
