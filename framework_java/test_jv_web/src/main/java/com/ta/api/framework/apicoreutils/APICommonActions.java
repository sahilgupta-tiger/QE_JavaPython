package com.ta.api.framework.apicoreutils;

import com.ta.api.framework.pojo.CommonPojo;
import io.restassured.response.Response;
import org.testng.log4testng.Logger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * This class will have the Common Methods related to Web Services
 *
 * @author gayathri
 */
public class APICommonActions extends ApiCore {

    private static final Logger log = Logger.getLogger(APICommonActions.class);

    public APICommonActions(CommonPojo commonPojo) {
        super(commonPojo);
    }

    /**
     * This method is to copy the key and value from one map to another map
     *
     * @param sourceMap      the source map
     * @param destinationMap the destination map
     * @param key            the key to copy from one map to another
     */
    public static void addDataToMapWithKey(Map<String, Object> sourceMap, Map<String, Object> destinationMap, String key) {
        sourceMap.entrySet().stream().filter(entry -> (entry.getKey()).equals(key))
                .forEach(entry -> {
                    destinationMap.put(entry.getKey(), entry.getValue());
                    log.info("Added Keys to Map: " + entry.getKey() + entry.getValue());
                });
    }

    /**
     * This method will execute the API and will returns the response
     * Need to Pass the Request Parameters from the Method level
     * Need to Use this method when we have Customized Request
     *
     * @param statusCode the expected statusCode
     * @return returns the response from the API
     */

    public synchronized Response sendCustomRequest(String statusCode) {
        Response response = this.customRequest();
        this.responseLogger(response, statusCode);
        return response;
    }

    public synchronized Response sendCustomGraphQLRequest(Map<String,Object> map, String queryFromTD,String statusCode) throws IOException {
       Response response = this.graphqlMethod(map,queryFromTD);
        this.responseLogger(response,statusCode);
        return response;
    }



    /**
     * This method will execute the API and will returns the Particular Value from the Response
     * Need to Pass the Request Parameters from the Method level
     * Need to Use this method when we have Customized Request
     *
     * @param statusCode      the expected statusCode
     * @param expectedKeyPath the path of the key for which the value should return
     * @return returns the value of the expected key from the response
     */
    public synchronized String getCustomResponse(String statusCode, String expectedKeyPath) {
        Response response = this.customRequest();
        this.responseLogger(response, statusCode);
        Object expectedValue = "";
        try {
            List<String> expectedList = response.jsonPath().getList(expectedKeyPath);
            log.info("Expected Values are: " + expectedList);

            Random rand = SecureRandom.getInstanceStrong();
            expectedValue = expectedList.get(rand.nextInt(expectedList.size()));
        } catch (ClassCastException | NoSuchAlgorithmException e) {
            expectedValue = response.jsonPath().getString(expectedKeyPath);
        }
        return String.valueOf(expectedValue);
    }

    /**
     * This method will execute the API and will returns the response
     * Need to Pass the Request Parameters from the Test Data Sheet as defined
     * Need to Use this method when we have Static Request Not the Customized one
     *
     * @param map    the test data map
     * @param apiURI the API URI
     * @return returns the response from the API
     */
    public synchronized Response sendRequest(final Map<String, Object> map, String apiURI) {
        final Response response = this.sendInternalRequest(map, apiURI);
        this.responseLogger(response, this.mapKeyFinder(map, "StatusCode"));
        return response;
    }

    /**
     * This method will execute the API and will returns the response
     * Also it will validate the Contracts from the Response (Expected and Actual Keys)
     * Need to Pass the Request Parameters and Expected Contracts from the Test Data Sheet as defined
     * Need to Use this method when we have Static Request Not the Customized one
     *
     * @param map                     the test data map
     * @param apiURI                  the API URI
     * @param expectedContractsColumn -- specify the expected contracts column
     * @return returns the response from the API
     */
    public synchronized Response validateContracts(final Map<String, Object> map, String apiURI, String expectedContractsColumn) {
        final Response response = this.sendInternalRequest(map, apiURI);
        this.contractValidation(map, response.getBody().asString(), expectedContractsColumn);
//		this.responseValidation(map, response);
        return response;
    }

    /**
     * This method will execute the API and will returns the response
     * Also it will validate the Contracts from the Response (Expected and Actual Keys)
     * Need to Pass Expected Contracts from the Test Data Sheet as defined
     *
     * @param map                     the test data map
     * @param response                the API response
     * @param expectedContractsColumn -- the expected contracts column in test data
     * @return returns the response from the API
     */
    public synchronized Response validateCustomContracts(final Map<String, Object> map, Response response, String expectedContractsColumn) {
        this.contractValidation(map, response.getBody().asString(), expectedContractsColumn);
        return response;
    }

    /**
     * A function that should read the "Expected" columns from the Test Data
     * and compare with the response coming up from the web service.
     * The locators for the elements in API needs to pass from the ExecutionConfig.Properties file
     *
     * @param map      the test data map
     * @param response the API response
     */
    public void assertfromTestData(Map<String, Object> map, Response response) {

        Locale locale = new Locale("EN", "INDIA");
        map.entrySet().stream().filter(entry -> entry.getKey().startsWith("Expected")).forEach(entry -> {
            String element = entry.getKey().replace("Expected", "");
            String locator = getProperty(element.toLowerCase(locale));

            String actResp = String.valueOf(response.jsonPath().getList(locator).get(0));
            if (null != entry.getValue() && !entry.getValue().toString().isEmpty()) {
                this.logReport("info", "Performing Assertion for the Element: " + element);
                this.customAssertEquals(actResp, String.valueOf(entry.getValue()));
            }
        });
    }
}
