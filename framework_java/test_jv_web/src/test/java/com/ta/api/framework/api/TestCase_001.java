package com.ta.api.framework.api;

import com.ta.api.framework.apicoreutils.APICommonActions;
import com.ta.api.framework.apicoreutils.ApiCore;
import com.ta.api.framework.helperfunctions.CommonUtils;
import com.ta.api.framework.testbase.TestBaseSetUp;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class TestCase_001 extends TestBaseSetUp {
    private static final Logger logger = LogManager.getLogger(API_CommonMethods.class);
    protected static API_CommonMethods commonMethods;
    protected static APICommonActions api;
    protected static ApiCore apiCore;
    protected static CommonUtils commonUtils;

    public TestCase_001() {
        api = new APICommonActions(commonPojo);
        apiCore = new ApiCore(commonPojo);
        commonMethods = new API_CommonMethods(commonPojo);
        commonUtils = new CommonUtils();
    }

    @Test
    public void makeApiCall(Map<String, Object> map) throws Exception {
        commonMethods.makeApiCall(map);
    }
}

