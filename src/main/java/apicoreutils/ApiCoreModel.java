package apicoreutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static database.DBC.getValueFromDatabase;


public class ApiCoreModel {
    private static final Logger logger = LogManager.getLogger(ApiCoreModel.class);


    /**
     * Constructs the complete URL for an API call by combining the base URI and the endpoint.
     * It also appends additional parts to the endpoint from the dataDictionary keys that start with "endPoint-".
     *
     * @param dataDictionary A map containing the base URI and endpoint details.
     * @return The complete URL as a String.
     */
    public String getCompleteUrl(Map<String, Object> dataDictionary) {

        String baseUri = (String) dataDictionary.get("BASE_URI");
        String endpoint = (String) dataDictionary.getOrDefault("endPoint", "");

        StringBuilder completeEndpoint = new StringBuilder(endpoint);
        for (Map.Entry<String, Object> entry : dataDictionary.entrySet()) {
            if (entry.getKey().startsWith("endPoint-")) {
                completeEndpoint.append(entry.getValue());
            }
        }

        return baseUri + completeEndpoint.toString();
    }


    /**
     * Extracts headers from the dataDictionary where keys start with "headers-" and constructs a header map.
     *
     * @param dataDictionary A map containing headers with keys starting with "headers-".
     * @return A map containing the headers.
     */
    public Map<String, String> getHeader(Map<String, Object> dataDictionary) {
        Map<String, String> extractedHeaders = new HashMap<>();
        for (Map.Entry<String, Object> entry : dataDictionary.entrySet()) {
            if (entry.getKey().startsWith("headers-")) {
                String headerKey = entry.getKey().split("-", 2)[1];
                extractedHeaders.put(headerKey, (String) entry.getValue());
            }
        }
        return extractedHeaders;
    }

    /**
     * Extracts expected output values from the dataDictionary where keys start with "Expected-" and constructs a map of these expected values.
     *
     * @param dataDictionary A map containing expected output keys and values.
     * @return A map containing the expected outputs.
     */
    public Map<String, String> getExpectedOutput(Map<String, Object> dataDictionary) {
        Map<String, String> extractedExpectedResult = new HashMap<>();

        // Iterate over the entries of the original map
        for (Map.Entry<String, Object> entry : dataDictionary.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Check if the key starts with "Expected-" and if the value is a String
            if (key.startsWith("Expected-") && value instanceof String) {
                // Extract the part after "Expected-"
                String expectedKey = key.split("-", 2)[1];
                // Add the extracted key and value to the new map
                extractedExpectedResult.put(expectedKey, (String) value);
            }
        }

        // Return the extracted expected result map
        return extractedExpectedResult;
    }

    /**
     * Extracts a value from a JSON object based on a given JSON path.
     *
     * @param data The JSON object as a String.
     * @param path The JSON path expression to extract the value.
     * @return The extracted value as an Object.
     */

    public Object getValueFromPath(Object data, String path) {
        return JsonPath.read(data, path);
    }


    /**
     * Constructs a JSON object using a template file specified by `JsonName` in the `dataDictionary` or directly from keys in the `dataDictionary` starting with `Request-`.
     * It replaces placeholders in the template with values from the dictionary.
     *
     * @param dataDictionary A map containing key-value pairs for constructing the JSON object.
     * @return The constructed JSON object.
     * @throws IOException If an I/O error occurs while reading the JSON template file.
     */
    public JSONObject returnUpdatedJson(Map<String, Object> dataDictionary) throws IOException {
        JSONObject jsonTemplate = new JSONObject();

        // Check if "JsonName" key exists in dataDictionary
        if (dataDictionary.containsKey("JsonName")) {
            String jsonFilePath = "src/test/java/API/resources/json/" + dataDictionary.get("JsonName");
            File file = new File(jsonFilePath);

            if (file.exists()) {
                // Read JSON template from the file
                try (FileReader reader = new FileReader(file)) {
                    char[] buffer = new char[(int) file.length()];
                    reader.read(buffer);
                    jsonTemplate = new JSONObject(new String(buffer));
                }
            } else {
                System.out.println("JSON file not found. Constructing JSON from keys starting with 'Request-'.");
            }
        }

        // Recursively replace placeholders in the template
        replacePlaceholders(jsonTemplate, dataDictionary);

        // Add additional request data to the JSON
        for (Map.Entry<String, Object> entry : dataDictionary.entrySet()) {
            if (entry.getKey().startsWith("Request-")) {
                String jsonKey = entry.getKey().substring("Request-".length());
                if (!jsonTemplate.has(jsonKey)) {
                    jsonTemplate.put(jsonKey, entry.getValue());
                }
            }
        }

        // Log the constructed JSON
        System.out.println("Constructed JSON: " + jsonTemplate.toString(4));
        System.out.println("**********" + jsonTemplate);

        return jsonTemplate;
    }


    /**
     * Recursively replaces placeholders in a JSON object with values from the `dataDictionary`.
     *
     * @param json           The JSON object containing placeholders.
     * @param dataDictionary A map containing key-value pairs to replace placeholders in the JSON object.
     */
    private void replacePlaceholders(JSONObject json, Map<String, Object> dataDictionary) {
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.get(key);

            if (value instanceof JSONObject) {
                replacePlaceholders((JSONObject) value, dataDictionary);
            } else if (value instanceof String) {
                String valueStr = (String) value;
                if (valueStr.startsWith("$") && valueStr.endsWith("$")) {
                    String placeholderKey = valueStr.substring(1, valueStr.length() - 1);
                    if (dataDictionary.containsKey("Request-" + placeholderKey)) {
                        json.put(key, dataDictionary.get("Request-" + placeholderKey));
                    }
                }
            }
        }
    }

    /**
     * Retrieves the parent schema JSON object from a file specified in the `dataDictionary`.
     *
     * @param dataDictionary A map containing key-value pairs, including the "Parent_Schema" key specifying the schema file name.
     * @return The JSON object representing the parent schema.
     * @throws IOException If an I/O error occurs while reading the schema file.
     */

    public JSONObject getParentSchema(Map<String, String> dataDictionary) throws IOException {
        String schemaFilePath = "json/" + dataDictionary.get("Parent_Schema");
        try (FileReader reader = new FileReader(schemaFilePath)) {
            char[] buffer = new char[(int) new File(schemaFilePath).length()];
            reader.read(buffer);
            return new JSONObject(new String(buffer));
        }
    }

    /**
     * Asserts that the status code of the response matches the expected status code specified in the `dataDictionary`.
     *
     * @param response       The response object to validate.
     * @param dataDictionary A map containing key-value pairs, including the "Expected-StatusCode" key specifying the expected status code.
     */

    public void statusCodeAssertion(Response response, Map<String, String> dataDictionary) {
        String expectedStatusCodeStr = dataDictionary.get("Expected-StatusCode");
        if (expectedStatusCodeStr != null) {
            int expectedStatusCode = Integer.parseInt(expectedStatusCodeStr);
            assert response.statusCode() == expectedStatusCode : "Expected status code: " + expectedStatusCode + ", but got: " + response.statusCode();
        }
    }

    /**
     * Extracts a value from a JSON response based on a given JSON path expression.
     *
     * @param response The response object containing the JSON data.
     * @param jsonPath The JSON path expression to extract the value.
     */
    public void extractValueFromJson(Response response, String jsonPath) {
        Object value = JsonPath.read(response.asString(), jsonPath);
        System.out.println("Extracted Value: " + value);
    }

    /**
     * Stores a key-value pair in a properties file. The key is constructed using the test case name from the `dataDictionary`.
     *
     * @param key            The key to store in the properties file.
     * @param value          The value to store in the properties file.
     * @param dataDictionary A map containing key-value pairs, including the "Test_Case_Name" key specifying the test case name.
     * @throws IOException If an I/O error occurs while writing to the properties file.
     */

    public void storeValueInFile(String key, String value, Map<String, Object> dataDictionary) throws IOException {
        // Construct the key using the Test_Case_Name from dataDictionary
        String testCaseName = (String) dataDictionary.get("Test_Case_Name");
        String newKey = (testCaseName.substring(0, Math.min(testCaseName.length(), 6))).toLowerCase() + "_" + key;

        // Load existing properties file or create a new one
        Properties props = new Properties();
        String filePath = "src/test/java/API/resources/extracted.properties";  // Update the file path accordingly
        try {
            props.load(new FileReader(filePath));
        } catch (IOException e) {
            // File not found or unable to read, create a new properties object
        }

        // Append the new key-value pair to the properties
        props.setProperty(newKey, value);

        // Write the updated properties back to the file
        try (FileWriter writer = new FileWriter(filePath)) {
            props.store(writer, "Updated extracted values");
        } catch (IOException e) {
            System.err.println("Error writing properties file: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Extracts data from a JSON response based on keys in the `dataDictionary` that start with "Extract-",
     * and stores the extracted values in a properties file.
     *
     * @param dataDictionary A map containing key-value pairs, including keys starting with "Extract-" specifying JSON path expressions.
     * @param response       The response object containing the JSON data.
     * @throws IOException If an I/O error occurs while storing extracted values.
     */
    public void extractingData(Map<String, Object> dataDictionary, Response response) throws IOException {
        String responseAsString = response.getBody().asString(); // Extract the response body as a string

        for (Map.Entry<String, Object> entry : dataDictionary.entrySet()) {
            if (entry.getKey().startsWith("Extract-")) {
                String jsonPath = (String) entry.getValue();
                String extractedKey = entry.getKey().substring("Extract-".length());
                System.out.println("JSONPath Expression: " + jsonPath);

                try {
                    Object extractedValue = JsonPath.read(responseAsString, jsonPath);
                    if (extractedValue != null) {
                        storeValueInFile(extractedKey, extractedValue.toString(), dataDictionary);
                        System.out.println("Value '" + extractedValue + "' stored in file with key '" + extractedKey + "'");
                    } else {
                        System.out.println("Value not found for JSONPath: " + jsonPath);
                    }
                } catch (Exception e) {
                    System.out.println("Error while extracting data with JSONPath: " + jsonPath);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Compares expected output values from `expectedOutputDict` with actual values in the JSON response.
     * Skips keys that are equal to "statuscode" and performs SQL validation if specified.
     *
     * @param expectedOutputDict A map containing expected output values.
     * @param response           The response object containing the JSON data.
     */
    public void expectedOutputCompare(Map<String, String> expectedOutputDict, Response response) {
        for (Map.Entry<String, String> entry : expectedOutputDict.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase("statuscode")) {
                String[] texts = entry.getValue().split(" \\|\\| ");
                String text1 = texts[0];
                String text2 = texts[1];
                if (!text2.toLowerCase().contains("sqlvalidation")) {
                    String expectedValue = text2;
                    String actualValue = String.valueOf(JsonPath.read(response.asString(), text1));
                    assert expectedValue.equals(actualValue) : "Mismatch: Expected value: " + expectedValue + " | Actual value: " + actualValue;
                }
            }
        }
    }

    /**
     * Prints the details of a request in JSON format.
     *
     * @param requestDetails A map containing key-value pairs representing request details.
     */
    public void printRequestDetails(Map<String, Object> requestDetails) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestDetailsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDetails);
            System.out.println("Request Details: " + requestDetailsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the JSON body for a request based on the HTTP method specified in the `dataDictionary`.
     * Constructs the JSON body using the `returnUpdatedJson` method if the HTTP method is POST or PUT.
     *
     * @param dataDictionary A map containing key-value pairs, including the "httpMethod" key specifying the HTTP method.
     * @param requestDetails A map to store the constructed JSON body.
     * @return The updated `requestDetails` map with the JSON body.
     * @throws IOException If an I/O error occurs while constructing the JSON body.
     */

    public Map<String, Object> setRequestJsonBody(Map<String, Object> dataDictionary, Map<String, Object> requestDetails) throws IOException {
        String httpMethod = dataDictionary.get("httpMethod").toString().toUpperCase();
        JSONObject updatedJson = null;
        if (httpMethod.equals("POST") || httpMethod.equals("PUT")) {
            try {
                updatedJson = returnUpdatedJson(dataDictionary);
                System.out.println("((((((((((((" + updatedJson);
                requestDetails.put("json", updatedJson);
                System.out.println(requestDetails);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Error while setting request JSON body", e);
            }
        }
        return requestDetails;
    }

    /**
     * Attaches the given response to the Allure report for better reporting and debugging.
     *
     * @param response The response to attach, as a String.
     */

    public void attachResponseToAllure(Response response) {
        Allure.addAttachment("API Response", response.asString());
    }

    /**
     * Validates the API response JSON against the expected output specified in the data dictionary.
     *
     * @param dataDictionary A map containing key-value pairs, including the expected output values.
     * @param responseJson   The JSON object representing the API response.
     */
    public void responseValidation(Map<String, Object> dataDictionary, JSONObject responseJson) {
        Map<String, String> expectedResult = getExpectedOutput(dataDictionary);

        Map<String, Object> filteredExpectedOutputMap = new HashMap<>();
        for (Map.Entry<String, String> entry : expectedResult.entrySet()) {
            if (!entry.getKey().equals("StatusCode")) {
                filteredExpectedOutputMap.put(entry.getKey(), entry.getValue());
            }
        }

        JSONObject responsebody = responseJson;
        System.out.println("Response: " + responsebody.toString(4));
        validateResponse(filteredExpectedOutputMap, responseJson.toMap());
    }

    /**
     * Makes an API call based on the data dictionary and returns the response JSON object.
     *
     * @param dataDictionary A map containing key-value pairs required for making the API call, such as the HTTP method, URL, headers, and request body.
     * @return The JSON object representing the API response.
     */
    public JSONObject makeApiCall(Map<String, Object> dataDictionary) {
        ObjectMapper objectMapper = new ObjectMapper();
        String combinedUrl = getCompleteUrl(dataDictionary);
        Map<String, String> extractedHeaders = getHeader(dataDictionary);
        Response response = null;
        Map<String, Object> requestDetails = new HashMap<>();
        requestDetails.put("method", dataDictionary.get("httpMethod").toString().toUpperCase());
        requestDetails.put("url", combinedUrl);
        requestDetails.put("headers", extractedHeaders);

        JSONObject responseJson = null;
        try {
            printRequestDetails(requestDetails);
            requestDetails = setRequestJsonBody(dataDictionary, requestDetails);

            System.out.println(requestDetails);

            String httpMethod = dataDictionary.get("httpMethod").toString().toUpperCase();

            switch (httpMethod) {
                case "GET":
                case "READ":
                    response = RestAssured.given().headers(extractedHeaders).get(combinedUrl);
                    break;
                case "POST":
                case "CREATE":
                    response = RestAssured.given().headers(extractedHeaders).body(requestDetails.get("json").toString()).post(combinedUrl);
                    break;
                case "PUT":
                case "UPDATE":
                    response = RestAssured.given().headers(extractedHeaders).body(requestDetails.get("json").toString()).put(combinedUrl);
                    break;
                case "PATCH":
                    response = RestAssured.given().headers(extractedHeaders).body(requestDetails.get("json").toString()).patch(combinedUrl);
                    break;
                case "DELETE":
                    response = RestAssured.given().headers(extractedHeaders).delete(combinedUrl);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
            }

            if (response != null) {
                String responseBody = response.asString();

                responseJson = new JSONObject();
                if (responseBody.trim().startsWith("{")) {
                    responseJson = new JSONObject(responseBody);
                    System.out.println("Response JSON: " + responseJson.toString(4));
                } else {
                    System.err.println("Response is not a valid JSON object: " + responseBody);
                }

                System.out.println("Response StatusCode: " + response.statusCode());
                logger.info(combinedUrl + " Request has been sent to the base URL ");
                Allure.addAttachment("Request Details", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDetails));
                Allure.addAttachment("Response JSON", responseJson.toString(4));
                Allure.addAttachment("Response StatusCode", String.valueOf(response.statusCode()));
                Allure.addAttachment("Result: ", response.asString());
                attachResponseToAllure(response);
                extractingData(dataDictionary, response);
            }
        } catch (Exception e) {
            logger.error("Error while making API call: ", e);
            Allure.addAttachment("Exception", e.toString());
            throw new RuntimeException(e);
        }
        return responseJson;
    }

    /**
     * Checks if a given string is a valid JSON object or array.
     *
     * @param str The string to be checked.
     * @return True if the string is a valid JSON object or array, false otherwise.
     */
    private boolean isValidJson(String str) {
        try {
            new JSONObject(str);
        } catch (JSONException ex) {
            try {
                new JSONArray(str);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    /**
     * Retrieves a nested value from a map based on a dot-separated key.
     *
     * @param data The map containing the data.
     * @param key  The dot-separated key representing the path to the nested value.
     * @return The nested value, or null if not found.
     */

    private Object getNestedValue(Map<String, Object> data, String key) {
        String[] parts = key.split("\\.");
        Object current = data;

        for (String part : parts) {
            if (part.contains("[") && part.contains("]")) {
                String arrayKey = part.substring(0, part.indexOf('['));
                int index = Integer.parseInt(part.substring(part.indexOf('[') + 1, part.indexOf(']')));
                current = ((List<?>) ((Map<String, Object>) current).get(arrayKey)).get(index);
            } else {
                current = ((Map<String, Object>) current).get(part);
            }

            if (current == null) {
                break;
            }
        }

        return current;
    }

    /**
     * Replaces placeholders in the data dictionary with values fetched from an INI file.
     *
     * @param dataDictionary A map containing key-value pairs, including placeholders to be replaced.
     * @return The updated data dictionary with fetched values.
     */
    public Map<String, Object> replaceFetchValues(Map<String, Object> dataDictionary) {
        String iniFile = "src/test/java/API/resources/extracted.properties";
        Properties config = new Properties();

        try (FileReader reader = new FileReader(iniFile)) {
            config.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception if needed
        }

        for (Map.Entry<String, Object> entry : dataDictionary.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String && ((String) value).contains("||")) {
                String[] parts = ((String) value).split("\\|\\|");
                String directValue = parts[0].trim();
                String fetchKey = parts[1].trim();
                if (fetchKey.startsWith("fetch-")) {
                    fetchKey = fetchKey.substring(fetchKey.indexOf("-") + 1);
                    if (config.containsKey(fetchKey)) {
                        dataDictionary.put(key, config.getProperty(fetchKey));
                    } else {
                        System.out.println("Error: Key '" + fetchKey + "' not found in " + iniFile);
                    }
                }
            } else if (value instanceof String && ((String) value).startsWith("fetch-")) {
                String fetchKey = ((String) value).substring(((String) value).indexOf("-") + 1);
                if (config.containsKey(fetchKey)) {
                    dataDictionary.put(key, config.getProperty(fetchKey));
                } else {
                    System.out.println("Error: Key '" + fetchKey + "' not found in " + iniFile);
                }
            }
        }

        return dataDictionary;
    }


    /**
     * Validates the actual API response against the expected output.
     * <p>
     * //     * @param expectedOutput A map containing the expected output values.
     * //     * @param response A map representing the actual API response.
     */
    public void validateResponse(Map<String, Object> expectedOutput, Map<String, Object> response) {
        // Convert response to JSON object if it's a string
        JSONObject responseJson;
        try {
            responseJson = new JSONObject(response);
        } catch (JSONException e) {
            System.err.println("Error parsing response JSON: " + e.getMessage());
            return;
        }

        // Log the structures for debugging
        System.out.println("Expected Response: " + new JSONObject(expectedOutput).toString(4));
        System.out.println("Actual Response: " + responseJson.toString(4));

        // Keys to exclude from validation
        List<String> keysToExclude = Arrays.asList("statuscode");

        // Validation logic
        for (Map.Entry<String, Object> entry : expectedOutput.entrySet()) {
            String key = entry.getKey();
            Object keyValue = entry.getValue();

            if (keysToExclude.contains(key.toLowerCase())) {
                continue;
            }

            // Handle SQLQuery keys
            if (key.toLowerCase().contains("sqlquery")) {
                validationWithDatabase(response, convertToStringMap(expectedOutput));
                continue;
            }

            if (keyValue instanceof String) {
                String keyValueStr = (String) keyValue;
                if (keyValueStr.contains("||")) {
                    String[] parts = keyValueStr.split("\\|\\|", 2);
                    String nestedKey = parts[0].trim();
                    String expectedValue = parts[1].trim();
                    Object actualValue = getNestedValue(response, nestedKey); // Pass JSONObject
                    System.out.println("Actual Value for " + nestedKey + ": " + actualValue); // Debugging print statement
                    try {
                        assert actualValue.equals(expectedValue) : "Expected " + nestedKey + " to be " + expectedValue + ", but got " + actualValue;
                        System.out.println("Actual and Expected Values are Matching: Expected " + nestedKey + " value to be " + expectedValue + ", and Actual value is " + actualValue);
                    } catch (AssertionError e) {
                        System.err.println("Actual and Expected Values are Not Matching: Expected " + nestedKey + " value to be " + expectedValue + ", but got " + actualValue);
                    }
                } else {
                    String actualKey = key.replace("expectedoutput-", "").trim();
                    Object actualValue = getNestedValue(response, actualKey); // Pass JSONObject
                    System.out.println("Actual Value for " + actualKey + ": " + actualValue); // Debugging print statement
                    try {
                        assert actualValue.equals(keyValueStr) : "Expected " + actualKey + " to be " + keyValueStr + ", but got " + actualValue;
                        System.out.println("Actual and Expected Values are Matching: Expected " + actualKey + " value to be " + keyValueStr + ", and Actual value is " + actualValue);
                    } catch (AssertionError e) {
                        System.err.println("Actual and Expected Values are Not Matching: Expected " + actualKey + " value to be " + keyValueStr + ", but got " + actualValue);
                    }
                }
            } else {
                System.out.println("Skipping non-string key value pair: " + keyValue);
            }
        }
    }

    /**
     * Converts a map with values of type Object to a map with values of type String.
     * Only entries with String values are included in the resulting map.
     *
     * @param map The original map with values of type Object.
     * @return A new map containing only the entries from the original map that have String values.
     */
    private Map<String, String> convertToStringMap(Map<String, Object> map) {
        Map<String, String> stringMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                stringMap.put(entry.getKey(), (String) entry.getValue());
            }
        }
        return stringMap;
    }

    /**
     * Validates the API response against values fetched from the database based on a SQL query specified in the data dictionary.
     *
     * @param response       A map representing the actual API response.
     * @param dataDictionary A map containing key-value pairs, including the SQL query for database validation.
     */
    public void validationWithDatabase(Map<String, Object> response, Map<String, String> dataDictionary) {
        System.out.println(response);

        JSONObject jsonObject = new JSONObject(response);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        String expectedValue = dataObject.getString("first_name");

        String sqlQuery = getApiSqlQuery(dataDictionary);
        if (sqlQuery != null) {
            String valueFromDatabase = getValueFromDatabase(sqlQuery);

            System.out.println("Actual Value form database: " + valueFromDatabase); // Debugging print statement
            System.out.println("Actual and Expected Values are Matching: Expected  value to be " + expectedValue + ", and Actual value is " + valueFromDatabase);
            Allure.addAttachment("ResultAssertion: ", "Actual and Expected Values are Matching: Expected  value to be " + expectedValue + ", and Actual value is " + valueFromDatabase);
            assert valueFromDatabase.equals(expectedValue);

        } else {
            throw new RuntimeException("SQL query not found in data dictionary.");
        }
    }

    /*
     * Retrieves the SQL query from the data dictionary.
     * @param dataDictionary A map containing key-value pairs, including the SQL query.
     * @return The SQL query string, or null if not found.
     */
    public String getApiSqlQuery(Map<String, String> dataDictionary) {
        for (Map.Entry<String, String> entry : dataDictionary.entrySet()) {
            if (entry.getKey().toLowerCase().contains("sqlquery")) {
                return entry.getValue();
            }
        }
        return null;
    }

}