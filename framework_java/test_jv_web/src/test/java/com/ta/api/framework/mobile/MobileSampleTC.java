package com.ta.api.framework.mobile;

import com.ta.api.framework.api.API_CommonMethods;
import com.ta.api.framework.apicoreutils.APICommonActions;
import com.ta.api.framework.apicoreutils.ApiCore;
import com.ta.api.framework.helperfunctions.CommonUtils;
import com.ta.api.framework.mobile.wrappers.MobileGenericWrapper;
import com.ta.api.framework.testbase.TestBaseSetUp;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.Map;


public class MobileSampleTC extends TestBaseSetUp {


    private static final Logger logger = LogManager.getLogger(API_CommonMethods.class);
    protected static SampleMobileCommonActions commonMethods;
    protected static APICommonActions api;
    protected static ApiCore apiCore;
    protected static CommonUtils commonUtils;
    protected static MobileGenericWrapper mobileGenericWrapper;

    public MobileSampleTC() {
        api = new APICommonActions(commonPojo);
        apiCore = new ApiCore(commonPojo);
        commonMethods = new SampleMobileCommonActions(commonPojo);
        commonUtils = new CommonUtils();
        mobileGenericWrapper = new MobileGenericWrapper();
    }

    @Test
    public void sampleMobileTest(Map<String, Object> map) {
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
        }

    }
}
