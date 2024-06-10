package com.ta.api.framework.apicoreutils;

import com.ta.api.framework.graphql.GraphqlQuery;
import com.ta.api.framework.pojo.CommonPojo;
import com.ta.api.framework.pojo.ServiceRequest;
import com.ta.api.framework.reports.ReportManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.collections.CollectionUtils;
import org.testng.log4testng.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * This class will have all the API Common Methods at the core level
 *
 * @author gayathri
 */
public class ApiCore extends ReportManager {

    private static final Logger log = Logger.getLogger(ApiCore.class);
    private static final String GREEN_COLOR = "green";
    private static final String RED_COLOR = "red";
    private static final String REQUEST_LINK_TEXT = "Click to open Request Body";
    private static final String RESPONSE_LINK_TEXT = "Click to open API Response";

    private static final String CONTENT_TYPE_PROTOBUF = "application/x-protobuf";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_XML = "application/xml";

    private ServiceRequest serviceRequest;

    public ApiCore(CommonPojo commonPojo) {
        super(commonPojo);
        this.commonPojo = commonPojo;
    }

    public ApiCore() {
    }

    private static Set<String> findKeysOfJsonArray(JSONArray jsonArr, Set<String> keys) {
        Set<String> keysFromArr = new LinkedHashSet<>();
        if (jsonArr != null && jsonArr.length() != 0) {
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObjIn = jsonArr.getJSONObject(i);
                keysFromArr = findKeysOfJsonObject(jsonObjIn, keys);
            }
        }
        return keysFromArr;
    }

    private static Set<String> findKeysOfJsonObject(JSONObject jsonObj, Set<String> keys) {

        Iterator<String> itr = jsonObj.keys();
        Set<String> keysFromObj = addtoSet(itr);
        keys.addAll(keysFromObj);

        itr = jsonObj.keys();
        while (itr.hasNext()) {
            String itrStr = itr.next();
            JSONObject jsonObj1 = null;
            JSONArray jsonArr = null;
            if (jsonObj.get(itrStr).getClass() == JSONObject.class) {
                jsonObj1 = jsonObj.getJSONObject(itrStr);
                findKeysOfJsonObject(jsonObj1, keys);
            } else if (jsonObj.get(itrStr).getClass() == JSONArray.class) {
                jsonArr = jsonObj.getJSONArray(itrStr);
                keys.addAll(findKeysOfJsonArray(jsonArr, keys));
            } else if (jsonObj.get(itrStr).getClass() == String.class) {
                log.info(itrStr + "-" + jsonObj.get(itrStr));
            }
        }
        return keys;
    }

    private static Set<String> addtoSet(Iterator<String> iter) {
        Set<String> set = new LinkedHashSet<>();
        while (iter.hasNext()) {
            set.add(iter.next());
        }
        return set;
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    /**
     * It will capture all the Request Parameters defined as Arguments for the WebService.
     * All the Request Parameters are logged into the Extent Report.
     *
     * @return returns the response from the API
     */
    protected synchronized Response customRequest() {
        Response response;
        RestAssured.baseURI = getServiceRequest().getUri();
        log.info("Picked URI: " + getServiceRequest().getUri());
        if (Thread.currentThread().getStackTrace()[4].getMethodName() != null) {
            this.logReport("info", "Executing API Test: <b>" + Thread.currentThread().getStackTrace()[3].getMethodName() + "</b>" + "  and  <b>" + Thread.currentThread().getStackTrace()[4].getMethodName() + "</b>");
        } else {
            this.logReport("info", "Executing API Test: <b>" + Thread.currentThread().getStackTrace()[3].getMethodName() + "</b>");
        }

        this.logReport("info", "Picked URI: <b>" + getServiceRequest().getUri() + "</b>");

        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification requestSpecification = RestAssured.given().contentType(ContentType.XML).request().urlEncodingEnabled(false);

        if (CollectionUtils.hasElements(getServiceRequest().getHeaders())) {
            this.logReport("info", "Request Headers are :<b>" + getServiceRequest().getHeaders());
            requestSpecification.headers(getServiceRequest().getHeaders());
        }


        if (CollectionUtils.hasElements(getServiceRequest().getPathparams())) {
            this.logReport("info", "Path params are :<b>" + getServiceRequest().getPathparams());
            requestSpecification.pathParams(getServiceRequest().getPathparams());
        }

        if (CollectionUtils.hasElements(getServiceRequest().getQueryparams())) {
            this.logReport("info", "Query params are :<b>" + getServiceRequest().getQueryparams());
            requestSpecification.queryParams(getServiceRequest().getQueryparams());
        }

        if (CollectionUtils.hasElements(getServiceRequest().getFormparams())) {
            this.logReport("info", "Form params are :<b>" + getServiceRequest().getFormparams());
            requestSpecification.formParams(getServiceRequest().getFormparams());
        }

        if (CollectionUtils.hasElements(getServiceRequest().getCookies())) {
            this.logReport("info", "Cookies are :<b>" + getServiceRequest().getCookies());
            requestSpecification.cookies(getServiceRequest().getCookies());
        }
        if (getServiceRequest().getProtoRequest() != null) {
            this.logReport("info", prettyPrintToString(GREEN_COLOR, REQUEST_LINK_TEXT, getServiceRequest().getProtoRequest()));
            requestSpecification.body(getServiceRequest().getProtoRequest().toByteArray());
        }
        if (getServiceRequest().getPayload() != null) {
            this.logReport("info", prettyPrintToString(GREEN_COLOR, REQUEST_LINK_TEXT, getServiceRequest().getPayload()));
            requestSpecification.body(getServiceRequest().getPayload());
        }

        if (getServiceRequest().getQuery() != null) {
            this.logReport("info", prettyPrintToString(GREEN_COLOR, REQUEST_LINK_TEXT, getServiceRequest().getQuery()));
            requestSpecification.body(getServiceRequest().getQuery());
        }

        System.out.println(getServiceRequest().getHttpMethod());
        System.out.println(getServiceRequest().getEndpoint());

        System.out.println(requestSpecification);

        response = this.executeAPIMethod(getServiceRequest().getHttpMethod(), getServiceRequest().getEndpoint(), requestSpecification);
        return response;
    }

    /**
     * This method will execute the API Request for all the HTTP Methods
     *
     * @param httpMethod           the httpMethod
     * @param endpoint             the end Point
     * @param requestSpecification
     * @return returns the response from the API
     */
    private Response executeAPIMethod(String httpMethod, String endpoint, RequestSpecification requestSpecification) {
        Response response = null;
        log.info("Executing HTTP method: " + httpMethod + " and EndPoint is :" + endpoint);
        this.logReport("info", "Executing HTTP method: <b>" + httpMethod + "</b> and EndPoint is :<b>" + endpoint + "</b>");
        double respTime;
        this.commonPojo.rptHttpMethod.add(httpMethod);
        this.commonPojo.rptEndpoint.add(endpoint);
        this.commonPojo.setHttpMethod(this.commonPojo.rptHttpMethod);
        this.commonPojo.setEndpoint(this.commonPojo.rptEndpoint);

        switch (httpMethod) {
            case "GET":
                response = ((ValidatableResponse) ((Response) requestSpecification.get(endpoint)).then()).extract().response();
                respTime = response.getTimeIn(TimeUnit.MILLISECONDS) / 1000.0;

                this.commonPojo.rptResponseTime.add(respTime);
                this.commonPojo.setResponseTime(this.commonPojo.rptResponseTime);

                this.commonPojo.rptActStatusCode.add(String.valueOf(response.getStatusCode()));
                this.commonPojo.setActStatusCode(this.commonPojo.rptActStatusCode);
                break;

            case "POST":
                response = ((ValidatableResponse) ((Response) requestSpecification.post(endpoint)).then()).extract().response();
                System.out.println(response.asString());
                respTime = response.getTimeIn(TimeUnit.MILLISECONDS) / 1000.0;

                this.commonPojo.rptResponseTime.add(respTime);
                this.commonPojo.setResponseTime(this.commonPojo.rptResponseTime);

                this.commonPojo.rptActStatusCode.add(String.valueOf(response.getStatusCode()));
                this.commonPojo.setActStatusCode(this.commonPojo.rptActStatusCode);
                break;

            case "PUT":
                response = ((ValidatableResponse) ((Response) requestSpecification.put(endpoint)).then()).extract().response();
                respTime = response.getTimeIn(TimeUnit.MILLISECONDS) / 1000.0;

                this.commonPojo.rptResponseTime.add(respTime);
                this.commonPojo.setResponseTime(this.commonPojo.rptResponseTime);

                this.commonPojo.rptActStatusCode.add(String.valueOf(response.getStatusCode()));
                this.commonPojo.setActStatusCode(this.commonPojo.rptActStatusCode);
                break;

            case "PATCH":
                response = ((ValidatableResponse) ((Response) requestSpecification.patch(endpoint)).then()).extract().response();
                respTime = response.getTimeIn(TimeUnit.MILLISECONDS) / 1000.0;

                this.commonPojo.rptResponseTime.add(respTime);
                this.commonPojo.setResponseTime(this.commonPojo.rptResponseTime);

                this.commonPojo.rptActStatusCode.add(String.valueOf(response.getStatusCode()));
                this.commonPojo.setActStatusCode(this.commonPojo.rptActStatusCode);
                break;

            case "DELETE":
                response = ((ValidatableResponse) ((Response) requestSpecification.delete(endpoint)).then()).extract().response();
                respTime = response.getTimeIn(TimeUnit.MILLISECONDS) / 1000.0;

                this.commonPojo.rptResponseTime.add(respTime);
                this.commonPojo.setResponseTime(this.commonPojo.rptResponseTime);

                this.commonPojo.rptActStatusCode.add(String.valueOf(response.getStatusCode()));
                this.commonPojo.setActStatusCode(this.commonPojo.rptActStatusCode);
                break;

            default:
                log.error((Object) ("Method " + httpMethod + " is not yet implemented"));
                this.logReport("FAIL", "Method " + httpMethod + " is not yet implemented");
                break;

        }
        return response;
    }

    /**
     * This method will logs the response from the API Request to the Extent Reports
     * Also, It will Asserts the Expected and Actual Status Codes
     *
     * @param resp       the API response
     * @param statusCode the expected StatusCode
     */
    protected void responseLogger(Response resp, String statusCode) {

        this.commonPojo.rptExpStatusCode.add(statusCode);
        this.commonPojo.setExpStatusCode(this.commonPojo.rptExpStatusCode);

        if ("api".equalsIgnoreCase(this.commonPojo.getSuiteTestType())) {
            log.info("Actual Status Code: " + resp.getStatusCode());
            this.customAssertEquals(String.valueOf(resp.getStatusCode()), String.valueOf(statusCode));
            final String reqContentType = getServiceRequest().getHeaders().get("Content-Type").toString();
            if (!reqContentType.equalsIgnoreCase(CONTENT_TYPE_PROTOBUF)) {
                log.info("Response JSON : " + resp.asString());
                this.logStatus("info", resp.getBody().prettyPrint());
            }
        }
    }


    /**
     * This method helps to log the content in pretty print format to the Extent Reports
     * Also, logs the response under an hyperlink which will be useful for huge amount of data
     *
     * @param status  the status of the test step
     * @param message the message to display under the hyperlink
     */
    public synchronized void logStatus(String status, String message) {

        if (status.equalsIgnoreCase("PASS")) {
            logReport("pass", prettyPrintToString(GREEN_COLOR, RESPONSE_LINK_TEXT, message));
        } else if (status.equalsIgnoreCase("FAIL")) {
            logReport("fail", prettyPrintToString(RED_COLOR, RESPONSE_LINK_TEXT, message));
        } else if (status.equalsIgnoreCase("INFO")) {
            logReport("info", prettyPrintToString(GREEN_COLOR, RESPONSE_LINK_TEXT, message));
        } else {
            logger.error("Report Initialization is Failed");
        }
    }

    /**
     * This method will validate the Expected and Actual Statements and logs to the Reports
     * The Expected is equal to Actual - logs in GREEN color
     * The Expected is NOT equal to Actual - logs in RED color
     *
     * @param actual   the Actual value in string format
     * @param expected the expected value in string format
     */
    public void customAssertEquals(String actual, String expected) {
        log.info("Asserting Equals...");
        log.info("****************Actual -> " + actual);
        log.info("**************Expected -> " + expected);

        if (expected.equals(actual)) {
            this.logReport("PASS", "Actual Text: " + this.badge3 + actual + this.badge4 + "</br>Expected Exact Text: " + this.badge3 + expected + this.badge4);

            this.commonPojo.rptTestStatus.add("PASS");
            this.commonPojo.setTestStatus(this.commonPojo.rptTestStatus);

        } else {
            ReportManager.sAssert.assertEquals(actual, expected);
            this.logReport("FAIL", "Actual Text: " + this.badge3 + actual + this.badge4 + "</br>Expected Exact Text: " + this.badge3 + expected + this.badge4);

            this.commonPojo.rptTestStatus.add("FAIL");
            this.commonPojo.setTestStatus(this.commonPojo.rptTestStatus);

            this.commonPojo.rptComments.add(this.badge2 + "Exp Text is: " + expected + "<br/>" + "Act Text is: " + actual + this.badge5);
            this.commonPojo.setComments(this.commonPojo.rptComments);
        }
    }

    public void customAssertEquals(Map<String, String> actualMap, Map<String, String> expectedMap) {
        log.info("Asserting Maps...");
        log.info("****************Actual Map -> " + actualMap);
        log.info("**************Expected Map -> " + expectedMap);

        if (expectedMap.equals(actualMap)) {
            this.logReport("PASS", "Actual Map: " + this.badge3 + actualMap + this.badge4 + "</br>Expected Map: " + this.badge3 + expectedMap + this.badge4);

            this.commonPojo.rptTestStatus.add("PASS");
            this.commonPojo.setTestStatus(this.commonPojo.rptTestStatus);

        } else {
            ReportManager.sAssert.assertEquals(actualMap, expectedMap);
            this.logReport("FAIL", "Actual Map: " + this.badge3 + actualMap + this.badge4 + "</br>Expected Map: " + this.badge3 + expectedMap + this.badge4);

            this.commonPojo.rptTestStatus.add("FAIL");
            this.commonPojo.setTestStatus(this.commonPojo.rptTestStatus);

            this.commonPojo.rptComments.add(this.badge2 + "Expected Map is: " + expectedMap + "<br/>" + "Actual Map is: " + actualMap + this.badge5);
            this.commonPojo.setComments(this.commonPojo.rptComments);
        }
    }


    /**
     * This method will returns the Value if the Parameterized Key present in the Map
     * Else returns the Null Value.
     * The Test Case will be Stopped here and will be proceed further
     *
     * @param map     the test data map
     * @param keyName the key for which the value should return from map
     * @return returns the value of the key from the input map
     */
    public String mapKeyFinder(final Map<String, Object> map, final String keyName) {
        final Object keyName2 = map.get(keyName);
        String returnValue = "";
        if (keyName2 != null) {
            returnValue = String.valueOf(keyName2);
        } else {
            logReport("FAIL", "Key: " + keyName + " Not Found in Data Provider");
            Assert.fail("Key: " + keyName + " Not Found in Data Provider");
        }
        return returnValue;
    }

    /**
     * It will capture all the Request Parameters defined in the Test Data with the Specified Format.
     * All the Request Parameters are logged into the Extent Report.
     *
     * @param map    the test data map
     * @param apiURI the API URI
     * @return returns the response from the API
     */
    protected synchronized Response sendInternalRequest(final Map<String, Object> map, String apiURI) {
        Response response = null;
        if (apiURI != null && !apiURI.isEmpty()) {
            RestAssured.baseURI = apiURI;
            ApiCore.log.info((Object) ("Picked URI from SUITE: " + apiURI));
            this.logReport("info", "Picked URI from SUITE: <b>" + apiURI + "</b>");

        }
        if (Thread.currentThread().getStackTrace()[4].getMethodName() != null) {
            this.logReport("info", "Executing API: <b>" + Thread.currentThread().getStackTrace()[3].getMethodName() + "  and  " + Thread.currentThread().getStackTrace()[4].getMethodName() + "</b>");
        } else {
            this.logReport("info", "Executing API: <b>" + Thread.currentThread().getStackTrace()[3].getMethodName() + "</b>");
        }

        RestAssured.useRelaxedHTTPSValidation();
        final RequestSpecification requestSpecification = RestAssured.given().contentType(ContentType.XML).request().urlEncodingEnabled(false);
        final Map<String, Object> headers = new HashMap<>();
        final Map<String, Object> cookies = new HashMap<>();
        final Map<String, Object> queryparams = new HashMap<>();
        final Map<String, Object> formparams = new HashMap<>();
        final Map<String, Object> pathparams = new HashMap<>();

        map.entrySet().stream().filter(entry -> entry.getKey().contains("HDR#")).forEach(entry -> {
            String key2 = entry.getKey().replace("HDR#", "");
            ApiCore.log.info((Object) (key2 + "  " + entry.getValue()));
            headers.put(key2, entry.getValue());
        });


        map.entrySet().stream().filter(entry -> entry.getKey().contains("CK#")).forEach(entry -> {
            String key3 = entry.getKey().replace("CK#", "");
            ApiCore.log.info((Object) (key3 + "  " + entry.getValue()));
            cookies.put(key3, entry.getValue());
        });


        map.entrySet().stream().filter(entry -> entry.getKey().contains("QP#")).forEach(entry -> {
            String key4 = entry.getKey().replace("QP#", "");
            ApiCore.log.info((Object) (key4 + "  " + entry.getValue()));
            queryparams.put(key4, entry.getValue());
        });


        map.entrySet().stream().filter(entry -> entry.getKey().contains("FP#")).forEach(entry -> {
            String key5 = entry.getKey().replace("FP#", "");
            ApiCore.log.info((Object) (key5 + "  " + entry.getValue()));
            formparams.put(key5, entry.getValue());
        });


        map.entrySet().stream().filter(entry -> entry.getKey().contains("PP#")).forEach(entry -> {
            String key6 = entry.getKey().replace("PP#", "");
            ApiCore.log.info((Object) (key6 + "  " + entry.getValue()));
            pathparams.put(key6, entry.getValue());
        });

        if (CollectionUtils.hasElements(headers)) {
            this.logReport("info", "Request Headers are :<b>" + headers);
            requestSpecification.headers(headers);
        }

        if (CollectionUtils.hasElements(pathparams)) {
            this.logReport("info", "Path params are :<b>" + pathparams);
            requestSpecification.pathParams(pathparams);
        }

        if (CollectionUtils.hasElements(queryparams)) {
            this.logReport("info", "Query params are :<b>" + queryparams);
            requestSpecification.queryParams(queryparams);
        }

        if (CollectionUtils.hasElements(formparams)) {
            this.logReport("info", "Form params are :<b>" + formparams);
            requestSpecification.formParams(formparams);
        }

        if (CollectionUtils.hasElements(cookies)) {
            this.logReport("info", "Cookies are :<b>" + cookies);
            requestSpecification.cookies(cookies);
        }
        Object payload = map.get("Payload");
        try {
            if (payload != null) {
                this.logReport("info", prettyPrintToString(GREEN_COLOR, REQUEST_LINK_TEXT, payload));
                requestSpecification.body(payload);
            } else {
                this.logReport("FAIL", "Please Pass RequestBody as \"Payload\" from the TestData");
            }
        } catch (NullPointerException e) {
            log.info(ExceptionUtils.getStackTrace(e));
        }

        response = this.executeAPIMethod(this.mapKeyFinder(map, "HttpMethod"), this.mapKeyFinder(map, "EndPoint"), requestSpecification);
        this.responseLogger(response, String.valueOf(map.get("StatusCode")));
        return response;
    }

    /**
     * This method helps to validate the response by passing expected values and locators from test data
     * User has to pass the test data header in the format EXP#{elementLocator} and expected value as value
     *
     * @param map      the test data map
     * @param response the API response
     */
    public void responseValidation(Map<String, Object> map, Response response) {

        map.entrySet().stream().filter(entry -> entry.getKey().contains("EXP#")).forEach(entry -> {
            String locator = entry.getKey().replace("EXP#", "");
            ApiCore.log.info((Object) (locator + "  " + entry.getValue()));
            String expectedValue = String.valueOf(entry.getValue());
            String actualValue = response.jsonPath().getString(locator);

            ApiCore.log.info("***Asserting the Expected Response***");
            this.customAssertEquals(actualValue, expectedValue);
        });
    }

    /**
     * This method will extract the contracts from JsonString
     *
     * @param response -- the API response
     * @return returns the keys from the response
     */
    private Set<String> contractsfromString(String response) {

        ApiCore.log.info("....Collecting the Actual Keys....");
        JSONObject jsonObjResp = new JSONObject(response);
        Set<String> actualKeys = new LinkedHashSet<>();
        findKeysOfJsonObject(jsonObjResp, actualKeys);
        return actualKeys;
    }

    /**
     * This method will validate the Contracts in the Response.
     * Need to pass the Expected Contracts from the Test Data separated by Comma
     *
     * @param tdMap                   the test data map
     * @param response                -- the API response in json string format
     * @param expectedContractsColumn -- the expected contracts column in test data
     */
    public void contractValidation(final Map<String, Object> tdMap, String response, String expectedContractsColumn) {

        List<String> actualContractList = new ArrayList<>(contractsfromString(response));
        this.logReport("info", "<b> Actual Keys from the response:</b> " + actualContractList);
        ApiCore.log.info(".....Asserting Contracts....");
        String expKeys = tdMap.get(expectedContractsColumn).toString();
        List<String> expectedContractlist = Arrays.asList(expKeys.split(",")).stream().map(String::trim).collect(Collectors.toList());

        this.logReport("info", "<b> Expected Keys from the test data:</b> " + expectedContractlist);
        this.logReport("info", "Validating Contracts....");
        for (String actualKey : actualContractList) {
            boolean verifyKeys = expectedContractlist.contains(actualKey);
            if (!verifyKeys) {
                this.logReport("Fail", actualKey + " Key is Not there in Response");
            }
        }
    }

    /**
     * This method will formats the input and returns the pretty print string
     *
     * @param Color    the color
     * @param linkText the link text
     * @param message  the message to convert to the pretty print string
     * @return the String in pretty print format
     */
    private String prettyPrintToString(final String Color, final String linkText, final Object message) {

        final String reqContentType = getServiceRequest().getHeaders().get("Content-Type").toString();
        String data = "";

        if (reqContentType.equalsIgnoreCase(CONTENT_TYPE_JSON)) {
            data = prettyJsonFormat(message);
        } else if (reqContentType.equalsIgnoreCase(CONTENT_TYPE_XML)) {
            data = prettyXMLformat(message.toString(), 2);
        } else if (reqContentType.equalsIgnoreCase(CONTENT_TYPE_PROTOBUF)) {
            data = message.toString();
        } else {
            data = message.toString();
        }
        return "<details><summary><font color=\"" + Color + "\"><b>" + linkText + "</b></font></summary><p><pre>" + data + "</pre></script></p></details>";
    }


    public Response graphqlMethod(Map<String, Object> map, String queryFromTD) throws IOException {

        GraphqlQuery query = new GraphqlQuery();
        query.setQuery(queryFromTD);
        RestAssured.baseURI = map.get("uri").toString();
        RequestSpecification requestSpec = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik9FWTJSVGM1UlVOR05qSXhSRUV5TURJNFFUWXdNekZETWtReU1EQXdSVUV4UVVRM05EazFNQSJ9.eyJodHRwczovL2hhc3VyYS5pby9qd3QvY2xhaW1zIjp7IngtaGFzdXJhLWRlZmF1bHQtcm9sZSI6InVzZXIiLCJ4LWhhc3VyYS1hbGxvd2VkLXJvbGVzIjpbInVzZXIiXSwieC1oYXN1cmEtdXNlci1pZCI6ImF1dGgwfDY0MTg5NjhmMDMxYzJmOTQ5OTNiZWVlMCJ9LCJuaWNrbmFtZSI6ImdheWF0cmhpMjJrNiIsIm5hbWUiOiJnYXlhdHJoaTIyazZAZ21haWwuY29tIiwicGljdHVyZSI6Imh0dHBzOi8vcy5ncmF2YXRhci5jb20vYXZhdGFyL2JiMzJkNjMwOTE2MDQxNTAyMjY1MWZmZDk1OWY0MDgxP3M9NDgwJnI9cGcmZD1odHRwcyUzQSUyRiUyRmNkbi5hdXRoMC5jb20lMkZhdmF0YXJzJTJGZ2EucG5nIiwidXBkYXRlZF9hdCI6IjIwMjMtMDMtMjBUMTc6MjM6MjkuODQyWiIsImlzcyI6Imh0dHBzOi8vZ3JhcGhxbC10dXRvcmlhbHMuYXV0aDAuY29tLyIsImF1ZCI6IlAzOHFuRm8xbEZBUUpyemt1bi0td0V6cWxqVk5HY1dXIiwiaWF0IjoxNjc5NDYzNzM4LCJleHAiOjE2Nzk0OTk3MzgsInN1YiI6ImF1dGgwfDY0MTg5NjhmMDMxYzJmOTQ5OTNiZWVlMCIsImF0X2hhc2giOiJGSFVRQTROT2Qya1NfcGVHOU0yVGJBIiwic2lkIjoiWjNaaE04UThuUXV3MEh6MWJlZWRPY1ladmQzVFNrNlciLCJub25jZSI6IjZoRG1sT1NRQTFHQnZsS09Jdy45VGFUZGo4UDhBOFBmIn0.pDy0q2727p1C0BVUWB-4jb7V6IjJmYksb56pgNT8qrGtJZEn6nwoRrD-1o8X7e2OFkIXS6yHi9uasD3I1CBnYaW5WRjdhKIV1ZKrEG3qsJXpFMyHOQjFKqL48fOCCWaU4qKJ-p9CIOk3UhgFizfhLCbpN_t5b1BTEB6KsANblfMlmZG_GgyAMBO-TtamI8Tk5BFNHLAUW3Kee-SlR1RCf4Y4j3B5kMLwz1DVY8lZJUW-LGjikoDHX-L43UIFI4CkDbDSocRsDus2nqf8VAKQYn-SRx9gIyruRtnRayT1DZDFL7_AZ48da2A9bvf2-QagYAR05kxzD_uBdxkXtNdLWw")
                .body(query);

        Response response = requestSpec.post(map.get("endpoint").toString());
        return response;
    }

    /*public okhttp3.Response graphqlMethod(Map<String, Object> map, String queryFromTD) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("query", queryFromTD);
        String jsonQuery = mapper.writeValueAsString(json);
        System.out.println(jsonQuery);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, jsonQuery);
        Request request = new Request.Builder()
                .url(map.get("uri").toString())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik9FWTJSVGM1UlVOR05qSXhSRUV5TURJNFFUWXdNekZETWtReU1EQXdSVUV4UVVRM05EazFNQSJ9.eyJodHRwczovL2hhc3VyYS5pby9qd3QvY2xhaW1zIjp7IngtaGFzdXJhLWRlZmF1bHQtcm9sZSI6InVzZXIiLCJ4LWhhc3VyYS1hbGxvd2VkLXJvbGVzIjpbInVzZXIiXSwieC1oYXN1cmEtdXNlci1pZCI6ImF1dGgwfDY0MTg5NjhmMDMxYzJmOTQ5OTNiZWVlMCJ9LCJuaWNrbmFtZSI6ImdheWF0cmhpMjJrNiIsIm5hbWUiOiJnYXlhdHJoaTIyazZAZ21haWwuY29tIiwicGljdHVyZSI6Imh0dHBzOi8vcy5ncmF2YXRhci5jb20vYXZhdGFyL2JiMzJkNjMwOTE2MDQxNTAyMjY1MWZmZDk1OWY0MDgxP3M9NDgwJnI9cGcmZD1odHRwcyUzQSUyRiUyRmNkbi5hdXRoMC5jb20lMkZhdmF0YXJzJTJGZ2EucG5nIiwidXBkYXRlZF9hdCI6IjIwMjMtMDMtMjBUMTc6MjM6MjkuODQyWiIsImlzcyI6Imh0dHBzOi8vZ3JhcGhxbC10dXRvcmlhbHMuYXV0aDAuY29tLyIsImF1ZCI6IlAzOHFuRm8xbEZBUUpyemt1bi0td0V6cWxqVk5HY1dXIiwiaWF0IjoxNjc5MzMzMDExLCJleHAiOjE2NzkzNjkwMTEsInN1YiI6ImF1dGgwfDY0MTg5NjhmMDMxYzJmOTQ5OTNiZWVlMCIsImF0X2hhc2giOiJvLXN6aV93UTBSYjZkSkFRTXhtR1NRIiwic2lkIjoiWjNaaE04UThuUXV3MEh6MWJlZWRPY1ladmQzVFNrNlciLCJub25jZSI6IkJNdWNqVjE1SDMzamU0SVh3a0ZIbkdhWmpsa1FUUWJQIn0.FCs6lJl0Y2-eeXbMpoIlotxqmfDUUwcAhye8bIFP_yol3a6C3xOC8QJ1UshcrVuBAe5Crkf-nnF1lZ17aSG4ILx1pe1inHaTaVkxHPMrdnuXsJY9_Ho6yEZN0BmZfXQKqrvMkx6zZovLErag1BmE-ya9m1_ECH9yqvJ-2Eai0nDUw10U536OUhukSYcxo8sepQU0yrZPL0Gnu90-xxcg7FZ6r5RmUWXQ6ULvyGZhkOlVIfIXLkgpjX7VuhUMhTIEb8ie8vA2o8T7z9x1JwXRYmkRkx3bj9-NpWVhLN6ncE1Fg5OuVQ4OakB1fW2slcUCMvHIG95nlEjmeVxLpI0i9g")

                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();

        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response);
        return response;
    }
*/


}


