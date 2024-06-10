package com.ta.api.framework.api;


import com.ta.api.framework.apicoreutils.APICommonActions;
import com.ta.api.framework.apicoreutils.ApiCore;
import com.ta.api.framework.commonmethods.GetTokenApi;
import com.ta.api.framework.graphql.GraphqlQuery;
import com.ta.api.framework.helperfunctions.CommonUtils;
import com.ta.api.framework.helperfunctions.DataBaseUtils;
import com.ta.api.framework.pojo.CommonPojo;
import com.ta.api.framework.pojo.ServiceRequest;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.ITestContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class API_CommonMethods {
    protected static final Logger logger = Logger.getLogger(API_CommonMethods.class);
    protected static ApiCore apiCore;
    protected static APICommonActions api;
    protected CommonPojo commonPojo;

    public API_CommonMethods(CommonPojo commonPojo) {
        this.commonPojo = commonPojo;
        api = new APICommonActions(commonPojo);
        apiCore = new ApiCore(commonPojo);
    }

    String id = null;
    JSONArray jsonArraypayload = null;
    JSONObject jsonObjectpayload = null;
    Map<String, Object> jsonheader = new HashMap<>();

    public Response makeApiCall(Map<String, Object> map) throws Exception {
        Response response = null;
        String httpMethodName = map.get("httpMethod").toString();
        switch (httpMethodName) {
            case "GET":
                response = getRequestList(map);
                logger.info(response);
                break;
            case "POST":
                response = createRequest(map);
                logger.info(response);
                break;
            case "PUT":
                updateRequest(map, id);
                response = getRequest(map, id);
                break;
            case "DELETE":
                response = createRequest(map);
                id = jsonExtractSpecificKey(map, response);
                deleteRequest(map, id);
                response = getRequest(map, id);
                logger.info(response);
                break;

        }
        return response;
    }


    public Map<String, Object> getJsonHeaders(Map<String, Object> map) {

        Map<String, Object> constructHeaders = new HashMap<>();
        map.entrySet().forEach(e -> {
            String key = e.getKey();
            boolean authorizationFlag = key.contains("Authorization");
            if (key.toLowerCase().startsWith("headers-") && !authorizationFlag) {
                constructHeaders.put(key.substring(key.indexOf('-') + 1), e.getValue());
            } else if (key.toLowerCase().startsWith("headers-") && authorizationFlag) {
                constructHeaders.put(key.substring(key.indexOf('-') + 1), e.getValue() + GetTokenApi.token);
            }
        });
        System.out.println(constructHeaders);
        return constructHeaders;
    }

    public static JSONArray iteratePayload(Map<String, Object> map) {
        JSONArray payloadArr = new JSONArray();
        for (int i = 1; i <= Integer.parseInt(map.get("itr").toString()); i++) {
            JSONObject payload = getPayloadAsJsonObject(map);
            payloadArr.put(payload);
        }
        System.out.println(payloadArr);
        logger.info("Constructed payload " + payloadArr);
        return payloadArr;
    }

    public static JSONObject getPayloadAsJsonObject(Map<String, Object> map) {
        JSONObject payload = new JSONObject();
        map.entrySet().forEach(e -> {

            String key = e.getKey();
            String value = (String) e.getValue();

            boolean mobileFlag = key.toLowerCase().contains("phone") || key.toLowerCase().contains("cell") || key.toLowerCase().contains("mobile");
            if (key.toLowerCase().startsWith("payload-") && !mobileFlag) {
                if (Integer.parseInt(map.get("itr").toString()) > 1) {
                    payload.put(key.substring(key.indexOf('-') + 1), e.getValue() + CommonUtils.generateRandomString(2));
                    if (value.toLowerCase().contains("@")) {
                        String[] arr = value.split("@", 2);
                        String email = arr[0] + CommonUtils.generateRandomString(4) + "@" + arr[1];
                        payload.put(key.substring(key.indexOf('-') + 1), email);
                    }
                } else {
                    payload.put(key.substring(key.indexOf('-') + 1), e.getValue());
                }

            } else if (key.toLowerCase().startsWith("payload-") && mobileFlag) {
                if (Integer.parseInt(map.get("itr").toString()) > 1) {
                    int num = (int) CommonUtils.generateRandomNumber(5);
                    String value1 = ((String) e.getValue()).substring(0, 5) + num;
                    payload.put(key.substring(key.indexOf('-') + 1), value1);
                } else {
                    payload.put(key.substring(key.indexOf('-') + 1), e.getValue());
                }
            }
        });
        logger.info("Request Payload --> " + payload);
        return payload;
    }

    public Response createRequest(Map<String, Object> map) throws IOException {
        ServiceRequest serviceRequest = new ServiceRequest();
        jsonheader = getJsonHeaders(map);
        serviceRequest.setHeaders(jsonheader);
        serviceRequest.setEndpoint(String.valueOf(map.get("endPoint")));
        serviceRequest.setHttpMethod("POST");
        serviceRequest.setUri(String.valueOf(map.get("uri")));
        api.setServiceRequest(serviceRequest);
        Response response = null;
        String jsonTemplate = null;
        if (map.containsKey("jsonFileName")) {
            jsonTemplate = new String(Files.readAllBytes(Paths.get("src/test/resources/Json/" + map.get("jsonFileName").toString() + ".json")), StandardCharsets.UTF_8);

            if (jsonTemplate.contains("$")) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    if (key.startsWith("$") && key.endsWith("$")) {
                        jsonTemplate = jsonTemplate.replace(key, map.get(key) + CommonUtils.generateRandomString(3));
                    }
                }
            }
            serviceRequest.setPayload(jsonTemplate);
        } else {
            int i = Integer.parseInt(map.get("itr").toString());

            if (i > 1) {
                jsonArraypayload = iteratePayload(map);
                serviceRequest.setPayload(jsonArraypayload.toString());
            } else {
                jsonObjectpayload = getPayloadAsJsonObject(map);
                serviceRequest.setPayload(jsonObjectpayload.toString());
            }
        }
        api.setServiceRequest(serviceRequest);
        try {
            response = api.sendCustomRequest(map.get("ExpectedStatusCode").toString());
        } catch (Exception e) {
            logger.info("Failed to create request");
        }
        return response;
    }

    public Response getRequestList(Map<String, Object> map) {
        ServiceRequest serviceRequest = new ServiceRequest();
        jsonheader = getJsonHeaders(map);
        serviceRequest.setHeaders(jsonheader);
        serviceRequest.setEndpoint(String.valueOf(map.get("endPoint")));
        serviceRequest.setHttpMethod("GET");
        serviceRequest.setUri(String.valueOf(map.get("uri")));
        api.setServiceRequest(serviceRequest);
        Response response = null;
        try {
            response = api.sendCustomRequest(map.get("ExpectedStatusCode").toString());
        } catch (Exception e) {
            logger.info("Failed to get request list");
        }
        return response;
    }

    public Response getRequest(Map<String, Object> map, String id) {
        ServiceRequest serviceRequest = new ServiceRequest();
        jsonheader = getJsonHeaders(map);
        serviceRequest.setHeaders(jsonheader);
        String endPoint = String.valueOf(map.get("endPoint"));
        endPoint = endPoint + "/" + id;
        System.out.println(endPoint);
        serviceRequest.setEndpoint(endPoint);
        serviceRequest.setHttpMethod("GET");
        serviceRequest.setUri(String.valueOf(map.get("uri")));
        api.setServiceRequest(serviceRequest);
        Response response = null;
        try {
            response = api.sendCustomRequest("200");
        } catch (Exception e) {
            logger.info("Failed to get request");
        }
        return response;
    }

    public Response updateRequest(Map<String, Object> map, String id) {

        ServiceRequest serviceRequest = new ServiceRequest();

        jsonheader = getJsonHeaders(map);
        serviceRequest.setHeaders(jsonheader);
        String endPoint = String.valueOf(map.get("endPoint"));
        endPoint = endPoint + "/" + id;
        System.out.println(endPoint);
        serviceRequest.setEndpoint(endPoint);
        serviceRequest.setHttpMethod("PUT");
        serviceRequest.setUri(String.valueOf(map.get("uri")));
        JSONArray payload = iteratePayload(map);
        serviceRequest.setPayload(payload.toString());
        api.setServiceRequest(serviceRequest);
        Response response = null;
        try {
            response = api.sendCustomRequest(map.get("ExpectedStatusCode").toString());

        } catch (Exception e) {
            logger.info("Failed to update request");
        }
        return response;
    }

    public void deleteRequest(Map<String, Object> map, String id) {
        ServiceRequest serviceRequest = new ServiceRequest();
        jsonheader = getJsonHeaders(map);
        serviceRequest.setHeaders(jsonheader);
        String endPoint = String.valueOf(map.get("endPoint"));
        endPoint = endPoint + id;
        System.out.println(endPoint);
        serviceRequest.setEndpoint(endPoint);
        serviceRequest.setHttpMethod("DELETE");
        serviceRequest.setUri(String.valueOf(map.get("uri")));
        api.setServiceRequest(serviceRequest);
        try {
            Response response = api.sendCustomRequest("200");
        } catch (Exception e) {
            logger.info("Failed to delete request");
        }
    }


    public String jsonExtractSpecificKey(Map<String, Object> map, Response response) {
        id = response.jsonPath().getString(map.get("expectedKeyPath").toString());
        logger.info("Expected Values are: " + id);
        System.out.println("expectedValue");
        return id;
    }

    public List<String> jsonExtractList(Map<String, Object> map, Response response) {
        String[] arr = map.get("variableName").toString().split(",");
        List<String> responseList = new ArrayList<>();
        for (String str : arr) {
            responseList.add(response.jsonPath().getString(str));
        }
        return responseList;
    }

    public Map<String, Object> jsonExtractMap(Response response) {
        Map<String, Object> responseMap = response.jsonPath().getMap("");
        System.out.println(responseMap);
        return responseMap;
    }

    public void assertSpecificValue(Map<String, Object> map, Response response) {
        String requestData = null;
        String responseData = null;
        requestData = jsonObjectpayload.getString(map.get("requestData").toString());
        responseData = response.jsonPath().getString("tourist_email");
        System.out.println(requestData);
        System.out.println(responseData);
        apiCore.customAssertEquals(requestData, responseData);
    }

    public void assertListValues(Map<String, Object> map, Response response) {
        String requestData = null;
        String responseData = null;
        List<String> lst = requestList(map);
        List<String> lst1 = jsonExtractList(map, response);
        System.out.println(lst);
        System.out.println(lst1);

        System.out.println(lst.get(0));
        System.out.println(lst1.get(0));
        for (int i = 0; i <= lst.size(); i++) {
            apiCore.customAssertEquals(lst.get(i), lst1.get(i));
        }
    }


    public String constructGraphqlQuery(Map<String, Object> testDataMap, String query) {
//        String constructedQuery = query;
        if (query.contains("{")) {
            for (Map.Entry<String, Object> e : testDataMap.entrySet()) {
                if (e.getKey().startsWith("query-")) {
                    String key = e.getKey().replace("query-", "$");
                    query = query.replace(key, e.getValue().toString());
                }
            }
        } else {

            System.out.println(query);
            String path = Objects.requireNonNull(getClass().getClassLoader().getResource(query).getPath());
            System.out.println(path);
            query = CommonUtils.readFile(path);
            for (Map.Entry<String, Object> e : testDataMap.entrySet()) {
                if (e.getKey().startsWith("query-")) {
                    String key = e.getKey().replace("query-", "$");
                    query = query.replace(key, e.getValue().toString());

                }
            }

        }
        return query;
    }

    public String constructGraphqlQuery(Map<String, Object> tsetDataMap, String query, String requiredValue) {
        System.out.println(query);
        String a = "$" + tsetDataMap.get("requiredKey").toString();
        System.out.println(a);
        if (query.contains("$" + tsetDataMap.get("requiredKey").toString())) {
            query = query.replace("$" + tsetDataMap.get("requiredKey").toString(), requiredValue.toString());

        }
        return query;
    }

    public Response graphQLAPICall(Map<String, Object> map) {
        Response response = null;
        String httpMethodName = map.get("httpMethodValidation").toString().toUpperCase();
        switch (httpMethodName) {
            case "GET":
                response = graphQlApi(map, "GETquery");
                logger.info(response);
                break;
            case "POST":
                response = graphQlApi(map, "POSTquery");
                logger.info(response);
                break;
            case "POST-GET":
                response = graphQlApi(map, "POSTquery");
                String requiredValue = response.getBody().jsonPath().getString(map.get("jsonPath").toString());
                response = graphQlApi(map, "GETquery", requiredValue);
                break;
        }
        return response;


    }


    public Response graphQlApi(Map<String, Object> map, String queryType) {
        GraphqlQuery query = new GraphqlQuery();
        String constructedQuery = constructGraphqlQuery(map, map.get(queryType).toString());
        System.out.println(constructedQuery);
        query.setQuery(constructedQuery);
        ServiceRequest serviceRequest = new ServiceRequest();
//        Map<String, Object> jsonheader = commonMethods.getJsonHeaders(map);
//        serviceRequest.setHeaders(jsonheader);
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik9FWTJSVGM1UlVOR05qSXhSRUV5TURJNFFUWXdNekZETWtReU1EQXdSVUV4UVVRM05EazFNQSJ9.eyJodHRwczovL2hhc3VyYS5pby9qd3QvY2xhaW1zIjp7IngtaGFzdXJhLWRlZmF1bHQtcm9sZSI6InVzZXIiLCJ4LWhhc3VyYS1hbGxvd2VkLXJvbGVzIjpbInVzZXIiXSwieC1oYXN1cmEtdXNlci1pZCI6ImF1dGgwfDY0MjJkMzg0ZGY4YTcyYzI0ZWY2NDNiMCJ9LCJuaWNrbmFtZSI6ImdheWF0aHJpMjJrNiIsIm5hbWUiOiJnYXlhdGhyaTIyazZAZ21haWwuY29tIiwicGljdHVyZSI6Imh0dHBzOi8vcy5ncmF2YXRhci5jb20vYXZhdGFyLzZiZDk4YmY2MTZjODFkMTgxNzFjOWNiZGExM2RkNTQ1P3M9NDgwJnI9cGcmZD1odHRwcyUzQSUyRiUyRmNkbi5hdXRoMC5jb20lMkZhdmF0YXJzJTJGZ2EucG5nIiwidXBkYXRlZF9hdCI6IjIwMjMtMDQtMjBUMDU6Mzk6MDcuMjc5WiIsImlzcyI6Imh0dHBzOi8vZ3JhcGhxbC10dXRvcmlhbHMuYXV0aDAuY29tLyIsImF1ZCI6IlAzOHFuRm8xbEZBUUpyemt1bi0td0V6cWxqVk5HY1dXIiwiaWF0IjoxNjgxOTY5MTQ4LCJleHAiOjE2ODIwMDUxNDgsInN1YiI6ImF1dGgwfDY0MjJkMzg0ZGY4YTcyYzI0ZWY2NDNiMCIsImF0X2hhc2giOiJ1YnVUVzN5TWxOaGsyei1GQW5oa2Z3Iiwic2lkIjoiWlpEX0NVeTIxbDhiZnZfVnZON0FrWi04Snk3RGZ0eGYiLCJub25jZSI6ImJYb1RBZ35sNzJDUG9ZbWZRWF9JakMudktTNGN2TEVlIn0.IE_lHGf0ajmfeKoKXzh-UU9c2Sddifxh4Rko_mmUK2FZ3dB7yTxCBVSVHR-IHPNqMxqZLqA3VFMXYRXeQ7GxPi7BSdylwpCnUzfMWiDMuv-T34uv1u-JfTCpe6WyoVV757A0H-Ip6-dslBTMiGfgh_ONcSLwBGF0IyXM9RVGeUxkxc503QQCGUOSX7F06ueb1a0-hccbWQQoCP8xUuBYWXY8Z8mSocMJ2QvbWcfQQoMxzbGRDh0C8hsNuCKzbvXqomngyyPnUqJxqH6tqj5F5by1CnFIeiAG2cpw0d6tEz_uC8K5DQQPXSTbvrDIHAtbNc579TGaTlYavL2xAkxCtg");
        headers.put("Content-Type", "application/json");
        serviceRequest.setHeaders(headers);
        serviceRequest.setEndpoint(String.valueOf(map.get("endPoint")));
        serviceRequest.setHttpMethod("POST");
        serviceRequest.setUri(String.valueOf(map.get("uri")));
        serviceRequest.setQuery(query);
        api.setServiceRequest(serviceRequest);
        Response response = api.sendCustomRequest("200");
        return response;
    }

    public Response graphQlApi(Map<String, Object> map, String queryType, String requiredValue) {
        GraphqlQuery query = new GraphqlQuery();
        String constructedQuery = null;
        if (requiredValue.length() > 0) {
            constructedQuery = constructGraphqlQuery(map, map.get(queryType).toString(), requiredValue);
        } else {
            constructedQuery = constructGraphqlQuery(map, map.get(queryType).toString());
        }
        query.setQuery(constructedQuery);
        ServiceRequest serviceRequest = new ServiceRequest();
//        Map<String, Object> jsonheader = commonMethods.getJsonHeaders(map);
//        serviceRequest.setHeaders(jsonheader);
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik9FWTJSVGM1UlVOR05qSXhSRUV5TURJNFFUWXdNekZETWtReU1EQXdSVUV4UVVRM05EazFNQSJ9.eyJodHRwczovL2hhc3VyYS5pby9qd3QvY2xhaW1zIjp7IngtaGFzdXJhLWRlZmF1bHQtcm9sZSI6InVzZXIiLCJ4LWhhc3VyYS1hbGxvd2VkLXJvbGVzIjpbInVzZXIiXSwieC1oYXN1cmEtdXNlci1pZCI6ImF1dGgwfDY0MjJkMzg0ZGY4YTcyYzI0ZWY2NDNiMCJ9LCJuaWNrbmFtZSI6ImdheWF0aHJpMjJrNiIsIm5hbWUiOiJnYXlhdGhyaTIyazZAZ21haWwuY29tIiwicGljdHVyZSI6Imh0dHBzOi8vcy5ncmF2YXRhci5jb20vYXZhdGFyLzZiZDk4YmY2MTZjODFkMTgxNzFjOWNiZGExM2RkNTQ1P3M9NDgwJnI9cGcmZD1odHRwcyUzQSUyRiUyRmNkbi5hdXRoMC5jb20lMkZhdmF0YXJzJTJGZ2EucG5nIiwidXBkYXRlZF9hdCI6IjIwMjMtMDMtMjhUMTE6NDY6MTMuODc4WiIsImlzcyI6Imh0dHBzOi8vZ3JhcGhxbC10dXRvcmlhbHMuYXV0aDAuY29tLyIsImF1ZCI6IlAzOHFuRm8xbEZBUUpyemt1bi0td0V6cWxqVk5HY1dXIiwiaWF0IjoxNjgwMDAzOTc1LCJleHAiOjE2ODAwMzk5NzUsInN1YiI6ImF1dGgwfDY0MjJkMzg0ZGY4YTcyYzI0ZWY2NDNiMCIsImF0X2hhc2giOiI3V3AzVndrR0Z3Y0Q4TzZ6VWU0bGx3Iiwic2lkIjoiT09yTnNMT0pZRlkzZmFRVmZBN2M1MjBDSXhrRmdVMWMiLCJub25jZSI6IlE4SUpmYU13dmR2aURLLUpJczVpbzF4eWVxb0RvR0J4In0.IVRZ-zZG81BZDL6pPB-9kuCMarxnjpbbYnBchDWEwlScs-vJeRegeNwzr-Oh4wjx2W9dm4dKaFte2h2lFAOy77QcGw7L7HqPmENrkcDe0FuFpIhj1nxHP3zajynzRZzZRa_M5YnvO1kuSBTUGYTCNJtP7BeHPhpQ1rsjOKRcNcp_MLqyHATNl6pRdiHd-ZH1UGbyyktrOXPLspmn2s1O4HgMciem6beFH5F804u7smpZ6jDQyWVid0Qjsk2CaiT4x0C3Bz3XCZeZAoOUOeTdIkJuZNeIqKerRP8lEdfrQtTWjx-hpItNn-kTEmtNmdZbPtW9Bv_mMW0tMLgI9WtlUw");
        headers.put("Content-Type", "application/json");
        serviceRequest.setHeaders(headers);
        serviceRequest.setEndpoint(String.valueOf(map.get("endPoint")));
        serviceRequest.setHttpMethod("POST");
        serviceRequest.setUri(String.valueOf(map.get("uri")));
        serviceRequest.setQuery(query);
        api.setServiceRequest(serviceRequest);
        Response response = api.sendCustomRequest("200");
        return response;
    }

    public List<String> requestList(Map<String, Object> map) {
        String[] arr = map.get("expectedKeyPath").toString().split(",");
        List<String> requestList = new ArrayList<>();
        for (String str : arr) {
            requestList.add(jsonObjectpayload.getString(str));
        }
        return requestList;
    }

    public void dbValidationE(Map<String, Object> map) {
        DataBaseUtils dataBaseUtils = new DataBaseUtils();
        String  dataFromDB = dataBaseUtils.connecttoDBString(map.get("SqlQuery").toString(),map.get("SqlColumnValue").toString());
        System.out.println(dataFromDB);
        logger.info("Created record is available in database");
        apiCore.customAssertEquals(map.get("expectedData").toString(), dataFromDB);
    }

    public void dbValidation(Map<String, Object> map) {
        DataBaseUtils dataBaseUtils = new DataBaseUtils();
        List<String> columnNames = Arrays.asList(map.get("SqlColumnValue").toString().split(","));
        Map<String, String> dataFromDB = dataBaseUtils.connecttoDBList(map.get("SqlQuery").toString(), columnNames);
        logger.info("Created record is available in database");
        HashMap<String, String> expectedMap = new HashMap<>();
        Arrays.stream(map.get("expectedData").toString().split(","))
                .map(pair -> pair.split("="))
                .forEach(keyValue -> expectedMap.put(keyValue[0], keyValue[1]));
        apiCore.customAssertEquals(expectedMap, dataFromDB);
    }
}