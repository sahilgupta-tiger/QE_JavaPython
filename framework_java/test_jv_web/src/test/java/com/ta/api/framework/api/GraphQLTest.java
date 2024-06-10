package com.ta.api.framework.api;


import com.ta.api.framework.apicoreutils.APICommonActions;
import com.ta.api.framework.apicoreutils.ApiCore;
import com.ta.api.framework.helperfunctions.CommonUtils;
import com.ta.api.framework.helperfunctions.DataBaseUtils;
import com.ta.api.framework.testbase.TestBaseSetUp;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.Map;

public class GraphQLTest extends TestBaseSetUp {
    private static final Logger logger = LogManager.getLogger(GraphQLTest.class);
    protected static API_CommonMethods commonMethods;
    protected static APICommonActions api;
    protected static ApiCore apiCore;
    protected static CommonUtils commonUtils;


    public GraphQLTest() {
        api = new APICommonActions(commonPojo);
        apiCore = new ApiCore(commonPojo);
        commonMethods = new API_CommonMethods(commonPojo);
        commonUtils = new CommonUtils();
    }


    @Test
    public void apiCall(Map<String, Object> map) {
        commonMethods.graphQLAPICall(map);
    }
}
