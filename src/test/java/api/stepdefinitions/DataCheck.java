package api.stepdefinitions;

import io.cucumber.java.en.*;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static apicoreutils.DataCheckUtils.*;

public class DataCheck {

    private JSONObject sdcJson;
    private List<Map<String, String>> mappingSheet;
    private List<Map<String, String>> msksDataRows;
    private Map<Integer, List<String>> rowMismatches = new LinkedHashMap<>();

    // Buckets
    private Map<Integer, List<String>> matched = new HashMap<>();
    private Map<Integer, List<String>> mismatched = new HashMap<>();
    private Map<Integer, List<String>> notFoundInJson = new HashMap<>();
    private Map<Integer, List<String>> notFoundInMsks = new HashMap<>();

    @Given("I have the SDC API JSON response file {string}")
    public void loadJsonResponse(String filePath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get("src/test/java/API/resources/json/"+filePath)));
        sdcJson = new JSONObject(content);
    }

    @Given("I have the mapping sheet file {string}")
    public void loadMappingSheet(String filePath) throws Exception {
        mappingSheet = readExcelAsList(filePath, "Mapping Sheet"); // assume "Mapping" is sheet name
    }

    @Given("I have the MSKS data file {string} and sheet {string}")
    public void loadMsksData(String filePath,String sheetName) throws Exception {
        msksDataRows = readExcelAsList(filePath, sheetName);
    }

    @When("I validate the data consistency against {string} table")
    public void validateData(String tableName) {
        int rowNum = 1;

        for (Map<String, String> msksRow : msksDataRows) {
            List<String> rowMatches = new ArrayList<>();
            List<String> rowMismatches = new ArrayList<>();
            List<String> rowNotFoundJson = new ArrayList<>();
            List<String> rowNotFoundMsks = new ArrayList<>();

            for (Map<String, String> mapping : mappingSheet) {
                if (mapping.get("Table in MSKS").contains(tableName)) {
                    String msksField = mapping.get("Field in MSKS");
                    String sdcField = mapping.get("Field in SDC");

                    // Get values
                    String msksValue = msksRow.getOrDefault(msksField.replace(" ", ""), "Not Found on Table");
                    msksValue = formatNumberString(msksValue);
                    String jsonValue = findKey(sdcJson, sdcField);
                    jsonValue = JsonValuePrecision(jsonValue);


                    // Categorization
                    if ("Not Found In JSON Response".equals(jsonValue)) {
                        rowNotFoundJson.add(sdcField + " - (Expected in JSON but not found)");
                    }
                    if ("Not Found on Table".equals(msksValue)) {
                        rowNotFoundMsks.add(msksField + " - (Expected in MSKS but not found)");
                    }
                    if (msksValue.equals(jsonValue)) {
                        rowMatches.add("Matched: " + msksField +" = " + sdcField + " ➡ " + jsonValue);
                    } else {
                        rowMismatches.add("Mismatched: " + msksField + " ➡️ JSON= " + jsonValue + " |❌| MSKS= " + msksValue);
                    }
                }
            }

            // Save to buckets
            if (!rowMatches.isEmpty()) matched.put(rowNum, rowMatches);
            if (!rowMismatches.isEmpty()) mismatched.put(rowNum, rowMismatches);
            if (!rowNotFoundJson.isEmpty()) notFoundInJson.put(rowNum, rowNotFoundJson);
            if (!rowNotFoundMsks.isEmpty()) notFoundInMsks.put(rowNum, rowNotFoundMsks);

            Allure.step("Comparison completed for row "+ rowNum +" from the "+tableName +" Table");

            rowNum++;

        }
        printValidationSummary();
    }


    @When("I validate the data consistency against {string} table by Index Value, {string} and {string}")
    public void validate_the_data_consistency(String tableName,String topArray, String indexKey) {
        int rowNum = 1;

        for (Map<String, String> msksRow : msksDataRows) {
            List<String> rowMatches = new ArrayList<>();
            List<String> rowMismatches = new ArrayList<>();
            List<String> rowNotFoundJson = new ArrayList<>();
            List<String> rowNotFoundMsks = new ArrayList<>();

            String indexValue = formatNumberString(msksRow.getOrDefault("Index",""));

            for (Map<String, String> mapping : mappingSheet) {
                if (mapping.get("Table in MSKS").contains(tableName)) {
                    String msksField = mapping.get("Field in MSKS");
                    String sdcField = mapping.get("Field in SDC");

                    String msksValue = msksRow.getOrDefault(msksField.replace(" ", ""), "Not Found on Table");
                    msksValue = formatNumberString(msksValue);

                    JSONObject measurement = findCorrectObjectByIndex(sdcJson, Integer.parseInt(indexValue),topArray,indexKey);

                    String jsonValue = null;
                    if (measurement != null) {
                        // ✅ Reusing the recursive findKey within this object
                        jsonValue = findKey(measurement, sdcField);
                        jsonValue = JsonValuePrecision(jsonValue);
                    } else {
                        System.out.println("Index " + indexValue + " not found in "+topArray);
                    }

                    // Categorization
                    if ("Not Found In JSON Response".equals(jsonValue)) {
                        rowNotFoundJson.add(sdcField + " - (Expected in JSON but not found)");
                    }
                    if ("Not Found on Table".equals(msksValue)) {
                        rowNotFoundMsks.add(msksField + " - (Expected in MSKS but not found)");
                    }
                   try {
                       if (msksValue.equals(jsonValue)) {
                           rowMatches.add("Matched: " + msksField +" = " + sdcField + " ➡ " + jsonValue);
                       } else {
                           rowMismatches.add("Mismatched: " + msksField + " ➡️ JSON= " + jsonValue + " |❌| MSKS= " + msksValue);
                       }
                   }catch (NullPointerException ignored){

                   }

                }
            }

            // Save to buckets
            if (!rowMatches.isEmpty()) matched.put(rowNum, rowMatches);
            if (!rowMismatches.isEmpty()) mismatched.put(rowNum, rowMismatches);
            if (!rowNotFoundJson.isEmpty()) notFoundInJson.put(rowNum, rowNotFoundJson);
            if (!rowNotFoundMsks.isEmpty()) notFoundInMsks.put(rowNum, rowNotFoundMsks);

            Allure.step("Comparison completed for row "+ rowNum +" from the "+tableName +" Table");

            rowNum++;
        }
        printValidationSummary();
    }


    @When("I validate the data on {string} table by Index Value, {string}, {string}, {string} and {string}")
    public void validate_the_data_consistency_formulation(String tableName,String topArray, String arrayKey,String subArray, String indexKey) {
        int rowNum = 1;

        for (Map<String, String> msksRow : msksDataRows) {
            List<String> rowMatches = new ArrayList<>();
            List<String> rowMismatches = new ArrayList<>();
            List<String> rowNotFoundJson = new ArrayList<>();
            List<String> rowNotFoundMsks = new ArrayList<>();

            String indexValue = formatNumberString(msksRow.getOrDefault("Index",""));
            String formulationName = msksRow.getOrDefault("Formulationname","");


            for (Map<String, String> mapping : mappingSheet) {
                if (mapping.get("Table in MSKS").contains(tableName)) {
                    String msksField = mapping.get("Field in MSKS");
                    String sdcField = mapping.get("Field in SDC");

                    //Getting the value from DB table
                    String msksValue = msksRow.getOrDefault(msksField.replace(" ", ""), "Not Found on Table");
                    msksValue = formatNumberString(msksValue);


                    JSONObject formulation = getFormulationByName(sdcJson, formulationName,topArray,arrayKey);
                    String jsonValue = null;
                    if (formulation != null) {
                        JSONObject ingredient = getIngredientByIndex(formulation, Integer.parseInt(indexValue),subArray,indexKey);

                        if (ingredient != null) {
                            jsonValue = findKey(ingredient, sdcField);
                            jsonValue = JsonValuePrecision(jsonValue);
                            if(jsonValue.contains("Not Found")) {
                                jsonValue = findKey(formulation, sdcField);
                                jsonValue = JsonValuePrecision(jsonValue);
                            }
                        } else {
                            System.out.println("Ingredient with index "+indexValue+"  not found!");
                        }
                    } else {
                        System.out.println("Formulation not found!");
                    }


                    if ("Not Found In JSON Response".equals(jsonValue)) {
                        rowNotFoundJson.add(sdcField + " - (Expected in JSON but not found)");
                    }
                    if ("Not Found on Table".equals(msksValue)) {
                        rowNotFoundMsks.add(msksField + " - (Expected in MSKS but not found)");
                    }
                    try {
                        if (msksValue.equals(jsonValue)) {
                            rowMatches.add("Matched: " + msksField +" = " + sdcField + " ➡ " + jsonValue);
                        } else {
                            rowMismatches.add("Mismatched: " + msksField + " ➡️ JSON= " + jsonValue + " |❌| MSKS= " + msksValue);
                        }
                    }catch (NullPointerException ignored){

                    }

                }
            }

            // Save to buckets
            if (!rowMatches.isEmpty()) matched.put(rowNum, rowMatches);
            if (!rowMismatches.isEmpty()) mismatched.put(rowNum, rowMismatches);
            if (!rowNotFoundJson.isEmpty()) notFoundInJson.put(rowNum, rowNotFoundJson);
            if (!rowNotFoundMsks.isEmpty()) notFoundInMsks.put(rowNum, rowNotFoundMsks);

            Allure.step("Comparison completed for row "+ rowNum +" from the "+tableName +" Table");

            rowNum++;
        }
        printValidationSummary();
    }

    @Then("Check for mismatched Keys and upload the Result to Allure")
    public void assertMatches() {
        updateResultToAllure();
        if (!mismatched.isEmpty()) {
            StringBuilder sb = new StringBuilder("Validation failed:\n");
            for (Map.Entry<Integer, List<String>> entry : mismatched.entrySet()) {
                sb.append("MSKS Row ").append(entry.getKey()).append(" mismatches:\n");
                for (String msg : entry.getValue()) {
                    sb.append("   ").append(msg).append("\n");
                }
            }
            throw new AssertionError(sb.toString());
        }
    }

    public void printValidationSummary() {
        System.out.println("\n========= VALIDATION SUMMARY =========");

        int totalMatched = matched.values().stream().mapToInt(List::size).sum();
        int totalMismatched = mismatched.values().stream().mapToInt(List::size).sum();
        int totalNotFoundInJson = notFoundInJson.values().stream().mapToInt(List::size).sum();
        int totalNotFoundInMsks = notFoundInMsks.values().stream().mapToInt(List::size).sum();

        int totalValidated = totalMatched + totalMismatched;

        System.out.println("\nTotally we validated " + totalValidated + " key-value pairs, out of which "
                + totalMatched + " are Matched ✅, "
                + totalMismatched + " are MisMatched ❌, "
                + totalNotFoundInJson + " Not Found in JSON 🚫, and "
                + totalNotFoundInMsks + " Not Found in MSKS Table 🚫");

        System.out.println("\n✅ MATCHED KEYS");
        matched.forEach((row, list) -> {
            System.out.println(" Row " + row + ":");
            list.forEach(item -> System.out.println("   " + item));
        });

        System.out.println("\n❌ MISMATCHED KEYS");
        mismatched.forEach((row, list) -> {
            System.out.println(" Row " + row + ":");
            list.forEach(item -> System.out.println("   " + item));
        });

        System.out.println("\n🚫 NOT FOUND IN JSON");
        notFoundInJson.forEach((row, list) -> {
            System.out.println(" Row " + row + ":");
            list.forEach(item -> System.out.println("   " + item));
        });

        System.out.println("\n🚫 NOT FOUND IN MSKS TABLE");
        notFoundInMsks.forEach((row, list) -> {
            System.out.println(" Row " + row + ":");
            list.forEach(item -> System.out.println("   " + item));
        });

        System.out.println("\n========= END SUMMARY =========");
    }

    public void updateResultToAllure(){
        StringBuilder report = new StringBuilder();
        report.append("========= VALIDATION SUMMARY REPORT =========\n\n");

        // Totals
        int totalMatched = matched.values().stream().mapToInt(List::size).sum();
        int totalMismatched = mismatched.values().stream().mapToInt(List::size).sum();
        int totalNotFoundInJson = notFoundInJson.values().stream().mapToInt(List::size).sum();
        int totalNotFoundInMsks = notFoundInMsks.values().stream().mapToInt(List::size).sum();
        int totalValidated = totalMatched + totalMismatched;

        report.append("===== Summary =====\n");
        report.append("Total Key-Value Pairs validated: " + totalValidated + "\n");
        report.append("Total Rows Validated: " + msksDataRows.size() + "\n");
        report.append("✅ Matched: " + totalMatched + "\n");
        report.append("❌ Mismatched: " + totalMismatched + "\n");
        report.append("🚫 Keys Not Found in JSON: " + totalNotFoundInJson + "\n");
        report.append("🚫 Columns Not Found in MSKS: " + totalNotFoundInMsks + "\n\n");

        report.append("✅ MATCHED KEYS\n");
        matched.forEach((row, list) -> {
            report.append("Row " + row + ":\n");
            list.forEach(item -> report.append("   " + item + "\n"));
        });

        report.append("\n❌ MISMATCHED KEYS\n");
        mismatched.forEach((row, list) -> {
            report.append("Row " + row + ":\n");
            list.forEach(item -> report.append("   " + item + "\n"));
        });

        /*report.append("\n🚫 NOT FOUND IN JSON\n");
        notFoundInJson.forEach((row, list) -> {
            report.append("Row " + row + ":\n");
            list.forEach(item -> report.append("   " + item + "\n"));
        });

        report.append("\n🚫 NOT FOUND IN MSKS TABLE\n");
        notFoundInMsks.forEach((row, list) -> {
            report.append("Row " + row + ":\n");
            list.forEach(item -> report.append("   " + item + "\n"));
        });*/

        Map<Integer, List<String>> combinedMap = new TreeMap<>();

        // Add all from notFoundInJson
        notFoundInJson.forEach((row, list) -> {
            combinedMap.putIfAbsent(row, new ArrayList<>());
            combinedMap.get(row).addAll(list);
        });

        // Add all from notFoundInMsks
        notFoundInMsks.forEach((row, list) -> {
            combinedMap.putIfAbsent(row, new ArrayList<>());
            combinedMap.get(row).addAll(list);
        });

        report.append("\n🚫 KEYS NOT FOUND FROM BOTH MSKS & JSON\n");
        combinedMap.forEach((row, list) -> {
            report.append("Row " + row + ":\n");
            list.forEach(item -> report.append("   " + item + "\n"));
        });


        // Add into Allure
        Allure.addAttachment("Validation Report", report.toString());

    }


}

