package com.ta.api.framework.healenium;

import com.ta.api.framework.api.API_CommonMethods;
import com.ta.api.framework.apicoreutils.APICommonActions;
import com.ta.api.framework.apicoreutils.ApiCore;
import com.ta.api.framework.helperfunctions.CommonUtils;
import com.ta.api.framework.testbase.TestBaseSetUp;
import com.ta.api.framework.web.SampleWebCommonActions;
import com.ta.api.framework.web.wrappers.GenericWrapper;
import com.ta.api.framework.web.wrappers.SelfHealGenericWrapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import java.util.Map;


public class SelfHealWebSampleTC extends TestBaseSetUp {


    private static final Logger logger = LogManager.getLogger(API_CommonMethods.class);
    protected static SelfHealSampleWebCommonActions commonMethods;
    protected static APICommonActions api;
    protected static ApiCore apiCore;
    protected static CommonUtils commonUtils;
    protected static SelfHealGenericWrapper selfhealgenericWrapper;

    public SelfHealWebSampleTC() {
        api = new APICommonActions(commonPojo);
        apiCore = new ApiCore(commonPojo);
        commonMethods = new SelfHealSampleWebCommonActions(commonPojo);
        commonUtils = new CommonUtils();
        selfhealgenericWrapper = new SelfHealGenericWrapper();
    }

    @Test
    public void sampleWebTest(Map<String, Object> map) {
        System.out.println("hello test");
        try {
            commonMethods.SelfHealApplicationchecksOldLocator(map);
            commonMethods.SelfHealApplicationchecksNewLocator(map);
            commonMethods.verify();
        } catch (Exception e) {
            e.printStackTrace();
            commonMethods.objectRepository.selfHealGenericWrapper().closeBrowser();
        }  finally {
            commonMethods.objectRepository.selfHealGenericWrapper().closeBrowser();
        }

    }
}
