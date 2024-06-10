package com.ta.api.framework.web;

import com.ta.api.framework.api.API_CommonMethods;
import com.ta.api.framework.apicoreutils.APICommonActions;
import com.ta.api.framework.apicoreutils.ApiCore;
import com.ta.api.framework.helperfunctions.CommonUtils;
import com.ta.api.framework.testbase.TestBaseSetUp;
import com.ta.api.framework.web.wrappers.GenericWrapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.Map;


public class WebSampleTC extends TestBaseSetUp {


    private static final Logger logger = LogManager.getLogger(API_CommonMethods.class);
    protected static SampleWebCommonActions commonMethods;
    protected static APICommonActions api;
    protected static ApiCore apiCore;
    protected static CommonUtils commonUtils;
    protected static GenericWrapper genericWrapper;

    public WebSampleTC() {
        api = new APICommonActions(commonPojo);
        apiCore = new ApiCore(commonPojo);
        commonMethods = new SampleWebCommonActions(commonPojo);
        commonUtils = new CommonUtils();
        genericWrapper = new GenericWrapper();
    }

    @Test
    public void sampleWebTest(Map<String, Object> map) {
        System.out.println("hello test");
        try {
            commonMethods.LogintoApplication(map);
            commonMethods.SelectTheProduct(map);
            commonMethods.Checkout(map);
            commonMethods.ProvideShippingDetails(map);
            commonMethods.OverviewofOrder(map);
            commonMethods.MovebacktoHomeScreen(map);
            commonMethods.verify();
        } catch (Exception e) {
            e.printStackTrace();
            commonMethods.objectRepository.genericWrapper().closeBrowser();
        }  finally {
            commonMethods.objectRepository.genericWrapper().closeBrowser();
        }

    }
}
