package apicoreutils;

import helperfunctions.ExcelDataProvider;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReadApiTestData {
    private static final Logger log = LogManager.getLogger(ExcelDataProvider.class);
    private static final String FEATURE_FILE = "src/test/java/API/resources/features/api-run.feature";

    private static final String DIR = "src/test/java/API/resources/testdata";

    public List<Map<String, Object>> readExcelData() throws IOException {
        List<Map<String, Object>> testData = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        ReadExamples readExamples = new ReadExamples();
        Map<String, String> testDataValues = readExamples.extractTestDataValues(FEATURE_FILE);
        String excelFilePath = testDataValues.get("defineExcelPath");
        String sheetName = testDataValues.get("definesheet");
        File file = new File(Paths.get(DIR, excelFilePath).toString());

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            boolean executeColumnFound = false;

            for (Row row : sheet) {
                Cell firstCell = row.getCell(0);
                if (firstCell != null && "Execute?".equalsIgnoreCase(firstCell.getStringCellValue())) {
                    headers = readHeaderRow(row);
                    executeColumnFound = true;
                } else if (executeColumnFound) { // Skip rows until Execute? column is found
                    values = readDataRow(row);
                    if (!values.isEmpty() && isValidTestCase(values)) {
                        Map<String, Object> dataMap = createDataMap(headers, values);
                        testData.add(dataMap);
                    }
                }
            }
        }

        return testData;
    }

    private boolean isValidTestCase(List<Object> values) {
        String executeValue = values.get(0).toString().toUpperCase();
//        String testTypeValue = values.get(2).toString().toUpperCase();
        //&& testTypeValue.contains(TEST_TYPE.toUpperCase()
        return ("Y".equals(executeValue) || "AUTH".equals(executeValue));
    }


    private List<String> readHeaderRow(Row row) {
        List<String> headers = new ArrayList<>();
        for (Cell cell : row) {
            headers.add(cell.getStringCellValue());
        }
        return headers;
    }

    private List<Object> readDataRow(Row row) {
        List<Object> values = new ArrayList<>();
        for (Cell cell : row) {
            values.add(getCellValue(cell));
        }
        return values;
    }

    private Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        if (CellType.NUMERIC.equals(cellType)) {
            return cell.getNumericCellValue();
        } else if (CellType.STRING.equals(cellType)) {
            return cell.getStringCellValue();
        } else if (CellType.BOOLEAN.equals(cellType)) {
            return cell.getBooleanCellValue();
        } else if (cellType == null || CellType.BLANK.equals(cellType)) {
            return "";
        } else {
            // Handle other cell types if needed
            return null;
        }
    }

    private Map<String, Object> createDataMap(List<String> headers, List<Object> values) {
        Map<String, Object> dataMap = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            Object value = values.get(i);
            dataMap.put(header, value);
        }
        return dataMap;
    }

    /*public Iterator<Object[]> readExcelData(String excelWorkbookPath, String excelSheetName) throws IOException {
        List<Map<String, Object>> testData = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        File file = new File(Paths.get(DIR, excelWorkbookPath).toString());
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(excelSheetName);

            for (Row row : sheet) {
                Cell firstCell = row.getCell(0);
                if (firstCell != null && "Execute?".equalsIgnoreCase(firstCell.getStringCellValue())) {
                    headers = StreamSupport.stream(row.spliterator(), false)
                            .map(cell -> cell != null ? cell.getStringCellValue() : null)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                } else {
                    values.clear();
                    for (Cell cell : row) {
                        values.add(getCellValue(cell));
                    }
                    //&& values.get(2) != null && values.get(2).toString().toUpperCase().contains(TEST_TYPE.toUpperCase())
                    if (values.get(0) != null && ("Y".equalsIgnoreCase(values.get(0).toString()) || "AUTH".equalsIgnoreCase(values.get(0).toString()))
                            ) {
                        if (headers.size() == values.size()) {
                            Map<String, Object> testCaseData = IntStream.range(0, headers.size())
                                    .boxed()
                                    .collect(Collectors.toMap(headers::get, values::get));
                            testData.add(testCaseData);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("The Excel Sheet provided is not correct:");
            throw e;
        }
        System.out.println(testData);

        // Assuming testData is a List<Map<String, Object>>, convert it to Iterator<Object[]>
        return testData.stream()
                .map(map -> map.values().toArray())
                .collect(Collectors.toList())
                .iterator();
    }
    public List<Map<String, Object>> readExcel() {
        List<Map<String, Object>> testData = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        ReadExamples readExamples = new ReadExamples();
        Map<String, String> testDataValues = readExamples.extractTestDataValues(FEATURE_FILE);
        String excelFilePath = testDataValues.get("defineExcelPath");
        String sheetName = testDataValues.get("definesheet");
        File file = new File(Paths.get(DIR, excelFilePath).toString());

        try (FileInputStream fis = new FileInputStream(file)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getLastRowNum();
            int columnCount = 0;
            for (int i = 0; i <= rowCount; i++) {
                String firstColumn = sheet.getRow(i).getCell(0).getStringCellValue();
                if (firstColumn.equalsIgnoreCase("TestCase")) {
                    columnCount = headerIteration(sheet, i, headers);
                } else {
                    if (!values.isEmpty()) {
                        values.clear();
                    }
                    for (int j = 0; j < columnCount; j++) {
                        Cell cell = sheet.getRow(i).getCell(j);
                        CellType cellType = cell.getCellType();
                        readCellData(cellType, cell, values);
                    }
                }
                if (headers.size() == values.size()) {
                    Map<String, Object> dataMap = new HashMap<>();
                    for (int k = 0; k < headers.size(); k++) {
                        String header = headers.get(k);
                        Object value = values.get(k);
                        if (dataMap.containsKey(header)) {
                            log.error("Duplicate key found: " + header + " in row " + (i + 1));
                            // Handle duplicate key scenario, e.g., skip, merge, or replace
                            // For this example, we skip adding this entry
                            dataMap = null;
                            break;
                        }
                        dataMap.put(header, value);
                    }
                    if (dataMap != null) {
                        testData.add(dataMap);
                    }
                }
            }
        } catch (IOException e) {
            log.info("The Excel Sheet provided is not correct:", e);
        }
        return testData;
    }

        private static int headerIteration(XSSFSheet sheet, int row, List<String> headers) {
        if (!headers.isEmpty()) {
            headers.clear();
        }
        int columnCount = sheet.getRow(row).getLastCellNum();
        for (int j = 0; j < columnCount; j++) {
            String cellData = sheet.getRow(row).getCell(j).getStringCellValue();
            headers.add(cellData);
        }
        return columnCount;
    }

    private static void readCellData(CellType cellType, Cell cell, List<Object> values) {
        if (CellType.NUMERIC.equals(cellType)) {
            double numberValue = cell.getNumericCellValue();
            values.add(numberValue);
        } else if (CellType.STRING.equals(cellType)) {
            String cellValue = cell.getStringCellValue();
            if (cellValue.contains("(") && cellValue.contains("char)") || cellValue.contains("(") && cellValue.contains("digit)")) {
                String reqStr = StringUtils.substringBetween(cellValue, "(", ")");
                int reqLength = Integer.parseInt(reqStr.replaceAll("[^0-9]", ""));

                String randomStr;
                if (reqStr.toLowerCase().contains("digit")) {
                    randomStr = String.valueOf(CommonUtils.generateRandomNumber(reqLength));
                } else {
                    randomStr = CommonUtils.generateRandomString(reqLength);
                }
                cellValue = cellValue.replace("(" + reqStr + ")", randomStr);
            }

            values.add(cellValue);
        } else if (CellType.BOOLEAN.equals(cellType)) {
            boolean cellValue = cell.getBooleanCellValue();
            values.add(cellValue);
        } else if (cellType == null || CellType.BLANK.equals(cellType)) {
            values.add("");
        }
    }*/

//    private Object getCellValue(Cell cell) {
//        if (cell == null) {
//            return null;
//        }
//        switch (cell.getCellType()) {
//            case NUMERIC:
//                return cell.getNumericCellValue();
//            case STRING:
//                return cell.getStringCellValue();
//            case BOOLEAN:
//                return cell.getBooleanCellValue();
//            case BLANK:
//                return "";
//            default:
//                return null;
//        }
//    }
}
