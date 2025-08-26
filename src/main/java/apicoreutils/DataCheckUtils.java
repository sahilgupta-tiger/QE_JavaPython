package apicoreutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DataCheckUtils {

    public static List<Map<String, String>> readExcelAsList(String filePath, String sheetName) throws Exception {
        List<Map<String, String>> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("src/test/java/API/resources/testdata/" + filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();

            // Header
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            // Data rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowMap.put(headers.get(i), cell.toString().trim());
                }
                data.add(rowMap);
            }
        }
        return data;
    }

    public static JSONObject convertExcelToJson(String filePath, String sheetName) {
        JSONObject jsonObject = new JSONObject();

        try (FileInputStream fis = new FileInputStream("src/test/java/API/resources/testdata/" + filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {  // skip header row
                Row row = sheet.getRow(i);

                if (row != null) {
                    Cell nameCell = row.getCell(1);  // Column1.name
                    Cell valueCell = row.getCell(2); // Column1.value

                    if (nameCell != null && valueCell != null) {
                        String key = nameCell.getStringCellValue().trim();
                        String value = "";

                        if (valueCell.getCellType() == CellType.STRING) {
                            value = valueCell.getStringCellValue().trim();
                        } else if (valueCell.getCellType() == CellType.NUMERIC) {
                            value = String.valueOf(valueCell.getNumericCellValue());
                        }

                        if (!key.isEmpty() && !value.isEmpty()) {
                            jsonObject.put(key, value);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static void ConvertLikeActualJSON(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream("src/test/java/API/resources/testdata/" + filePath);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);

        ObjectMapper mapper = new ObjectMapper();
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode rootNode = factory.objectNode();

        // Stack to maintain current group hierarchy
        Deque<Object> groupStack = new ArrayDeque<>();
        groupStack.push(rootNode);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // skip header row

            Cell keyCell = row.getCell(1);
            Cell valCell = row.getCell(2);
            if (keyCell == null) continue;

            String key = keyCell.getStringCellValue().trim();
            String value = (valCell != null) ? valCell.toString().trim() : "";

            if ("group".equalsIgnoreCase(key)) {
                // New group starts
                String groupName = value;
                Object top = groupStack.peek();

                if (top instanceof ObjectNode) {
                    ObjectNode parent = (ObjectNode) top;

                    if (parent.has(groupName)) {
                        // Already exists -> make/append to array
                        if (parent.get(groupName).isArray()) {
                            ObjectNode newGroup = factory.objectNode();
                            ((ArrayNode) parent.get(groupName)).add(newGroup);
                            groupStack.push(newGroup);
                        } else {
                            // Convert existing object to array
                            ObjectNode existing = (ObjectNode) parent.get(groupName);
                            ArrayNode arr = factory.arrayNode();
                            arr.add(existing);
                            ObjectNode newGroup = factory.objectNode();
                            arr.add(newGroup);
                            parent.set(groupName, arr);
                            groupStack.push(newGroup);
                        }
                    } else {
                        // First time this group is seen
                        ObjectNode newGroup = factory.objectNode();
                        parent.set(groupName, newGroup);
                        groupStack.push(newGroup);
                    }
                }
            } else {
                // Normal key-value pair
                Object top = groupStack.peek();
                if (top instanceof ObjectNode) {
                    ((ObjectNode) top).put(key, value);
                }
            }
        }

        workbook.close();

        // Convert to JSON String
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println(jsonString);
    }


    public static JSONObject convertExcelToJsonNew(String filePath, String sheetName) {
        JSONObject root = new JSONObject();

        try (FileInputStream fis = new FileInputStream("src/test/java/API/resources/testdata/" + filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            String currentGroup = null;
            Map<String, Object> groupObject = null;
            Map<String, List<Map<String, Object>>> arrayGroups = new HashMap<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // assuming row0 = headers
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell keyCell = row.getCell(1);   // Column1.name
                Cell valueCell = row.getCell(2); // Column1.value
//                System.out.println(keyCell + " &&&&&&& " + valueCell);
                if (keyCell == null) continue; // skip empty rows

                String key = keyCell.getStringCellValue().trim();
                String value = getCellValueAsString(valueCell);
//                System.out.println(key + " &&&&&&& " + value);

                if ("group".equalsIgnoreCase(key)) {
                    // new group starts
                    currentGroup = value;
                    groupObject = new HashMap<>();

                    if (root.has(currentGroup)) {
                        // ensure array
                        Object existing = root.get(currentGroup);
                        JSONArray arr;
                        if (existing instanceof JSONObject) {
                            arr = new JSONArray();
                            arr.put(existing);
                        } else {
                            arr = (JSONArray) existing;
                        }
                        arr.put(new JSONObject(groupObject));
                        root.put(currentGroup, arr);
                    } else {
                        root.put(currentGroup, new JSONObject(groupObject));
                        arrayGroups.put(currentGroup, new ArrayList<>());
                    }

                } else if (currentGroup != null) {
                    // inside group
                    Object grp = root.get(currentGroup);
                    if (grp instanceof JSONObject) {
                        ((JSONObject) grp).put(key, value);
                    } else if (grp instanceof JSONArray) {
                        List<Map<String, Object>> list = arrayGroups.get(currentGroup);
                        if (list.isEmpty() || list.get(list.size() - 1).containsKey(key)) {
                            list.add(new HashMap<>()); // start new row if duplicate keys
                        }
                        list.get(list.size() - 1).put(key, value);

                        JSONArray arr = new JSONArray();
                        for (Map<String, Object> obj : list) {
                            arr.put(new JSONObject(obj));
                        }
                        root.put(currentGroup, arr);
                    }
                }
            }

            System.out.println(root);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }

//    private static String getCellValueAsString(Cell cell) {
//        if (cell == null) return "";
//        switch (cell.getCellType()) {
//            case STRING: return cell.getStringCellValue();
//            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
//            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
//            default: return "";
//        }
//    }

    public static String findKey(JSONObject json, String key) {
        if (json.has(key)) {
            return json.optString(key);
        }
        for (String k : json.keySet()) {
            Object val = json.get(k);
            if (val instanceof JSONObject) {
                String result = findKey((JSONObject) val, key);
                if (!result.equals("Not Found In JSON Response")) return result;
            } else if (val instanceof JSONArray) {
                for (Object obj : (JSONArray) val) {
                    if (obj instanceof JSONObject) {
                        String result = findKey((JSONObject) obj, key);
                        if (!result.equals("Not Found In JSON Response")) return result;
                    }
                }
            }
        }
        return "Not Found In JSON Response";
    }

    public static JSONObject findCorrectObjectByIndex(JSONObject root, int index, String topArray, String indexKey) {
        JSONArray measurements = root.optJSONArray(topArray);
        if (measurements != null) {
            for (int i = 0; i < measurements.length(); i++) {
                JSONObject obj = measurements.optJSONObject(i);
                if (obj != null && obj.optInt(indexKey, -1) == index) {
                    return obj; // ✅ Found the correct object
                }
            }
        }
        return null; // ❌ Not found
    }

    public static JSONObject getFormulationByName(JSONObject rootJson, String formulationName,String topArrayName, String arrayKeyName) {
        JSONArray formulations = rootJson.optJSONArray(topArrayName);
        if (formulations != null) {
            for (int i = 0; i < formulations.length(); i++) {
                JSONObject formulation = formulations.getJSONObject(i);
                if (formulation.optString(arrayKeyName).equalsIgnoreCase(formulationName)) {
                    return formulation; // found matching formulation
                }
            }
        }
        return null; // not found
    }

    public static JSONObject getIngredientByIndex(JSONObject formulation, int index,String subArrayName, String indexKeyName) {
        JSONArray ingredients = formulation.optJSONArray(subArrayName);
        if (ingredients != null) {
            for (int i = 0; i < ingredients.length(); i++) {
                JSONObject ingredient = ingredients.getJSONObject(i);
                if (ingredient.optInt(indexKeyName) == index) {
                    return ingredient; // found matching ingredient
                }
            }
        }
        return null; // not found
    }

    public static String formatNumberString(String numStr) {
        try {
            double d = Double.parseDouble(numStr);
            // Check if d is whole number (integer)
            if (d == Math.floor(d)) {
                return String.valueOf((int) d);
            } else {
                return numStr;
            }
        } catch (NumberFormatException e) {
            // Not a number, return original string
            return numStr;
        }
    }



}
