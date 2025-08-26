//package com.ta.api.framework.api.stepdefinitions;
//
//import com.ta.api.framework.helperfunctions.CommonUtils;
//import com.ta.api.framework.pojo.CommonPojo;
//import com.ta.api.framework.reports.CustomHtmlReport;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.testng.ITestContext;
//import org.testng.annotations.DataProvider;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.testng.ITestContext;
//import org.testng.annotations.DataProvider;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//import com.ta.api.framework.pojo.CommonPojo;
//
//public class ExcelDatafetcher extends CustomHtmlReport {
//    public ExcelDatafetcher(){
//        this.commonPojo = new CommonPojo();
//
//    }
//
//    private static final Logger log = LogManager.getLogger(com.ta.api.framework.helperfunctions.ExcelDataProvider.class);
//
//    public static int headerIteration(XSSFSheet sheet, int row, List<String> headers) {
//        if (!headers.isEmpty()) {
//            headers.clear();
//        }
//        int columnCount = sheet.getRow(row).getLastCellNum();
//        for (var j = 0; j < columnCount; j++) {
//            var cellData = sheet.getRow(row).getCell(j).getStringCellValue();
//            headers.add(cellData);
//        }
//        return columnCount;
//    }
//
//    public static void readCellData(CellType cellType, Cell cell, List<Object> values) {
//        if (cellType == CellType.NUMERIC) {
//            double numberValue = cell.getNumericCellValue();
//            values.add(numberValue);
//        } else if (cellType == CellType.STRING) {
//            var cellValue = cell.getStringCellValue();
//            if (cellValue.contains("(") && cellValue.contains("char)") || cellValue.contains("(") && cellValue.contains("digit)")) {
//
//                String reqStr = StringUtils.substringBetween(cellValue, "(", ")");
//                int reqLength = Integer.parseInt(reqStr.replaceAll("[^0-9]", ""));
//
//                String randomStr;
//                if (reqStr.toLowerCase().contains("digit")) {
//                    randomStr = String.valueOf(CommonUtils.generateRandomNumber(reqLength));
//                } else {
//                    randomStr = CommonUtils.generateRandomString(reqLength);
//                }
//                cellValue = cellValue.replace("(" + reqStr + ")", randomStr);
//            }
//
//            values.add(cellValue);
//        } else if (cellType == CellType.BOOLEAN) {
//            var cellValue = cell.getBooleanCellValue();
//            values.add(cellValue);
//        } else if (cellType == null || cellType == CellType.BLANK) {
//            values.add("");
//        }
//    }
//
//
//    public static List<Map<String, Object>> readExcel() {
//        System.out.println();
//        String excelWorkBookPath = "src//test//resources//testdata//api//API_TestData.xlsx";
//        String excelSheetName = "APINEW";
//
//        List<Map<String, Object>> testData = new ArrayList<>();
//        List<String> headers = new ArrayList<>();
//        List<Object> values = new ArrayList<>();
//        /*System.out.println(method);
//        String excelWorkBookPath = context.getCurrentXmlTest().getParameter("excelWorkBook");
//        String excelSheetName = context.getCurrentXmlTest().getParameter("excelSheetName");
//        List<Map<String, Object>> testData = new ArrayList<>();
//        List<String> headers = new ArrayList<>();
//        List<Object> values = new ArrayList<>();
//        String path = Objects.requireNonNull(ExcelDataProvider.class.getClassLoader().getResource(excelWorkBookPath)).getPath();
//        System.out.println(path);
//        path = path.replace("test/testdata", "testdata");
//        path = path.replace("build", "src/test");
//        path = path.replace("/", "\\\\");*/
//        try (var workbook = new XSSFWorkbook(new FileInputStream(excelWorkBookPath))) {
//            XSSFSheet sheet = workbook.getSheet(excelSheetName);
//            int rowCount = sheet.getLastRowNum();
//            var columnCount = 0;
//            for (var i = 0; i <= rowCount; i++) {
//                var firstColumn = sheet.getRow(i).getCell(0).getStringCellValue();
//                if (firstColumn.equalsIgnoreCase("TestCase")) {
//                    columnCount = headerIteration(sheet, i, headers);
//                } else {
//                    if (!values.isEmpty()) {
//                        values.clear();
//                    }
//                    for (var j = 0; j < columnCount; j++) {
//                        Cell cell = sheet.getRow(i).getCell(j);
//                        var cellType = cell.getCellType();
//                        readCellData(cellType, cell, values);
//                    }
//                }
//                if (headers.size() == values.size()) {
//                    testData.add(IntStream.range(0, headers.size()).boxed()
//                            .collect(Collectors.toMap(headers::get, values::get)));
//                }
//            }
//        } catch (IOException e) {
//            log.info("The Excel Sheet provided is not correct:");
//        }
//        return testData;
//    }
//
//    @DataProvider
//    public static Iterator<Object[]> getExcelData() {
//        Collection<Object[]> testData = new ArrayList<>();
//        System.out.println();
//        List<Map<String, Object>> tdList = readExcel();
//        for (Map<String, Object> map : tdList) {
//            if (map.isEmpty()) {
//                log.info("Empty Test Data is provided");
//            } else if (map.get("ExecuteTest").toString().equalsIgnoreCase("No")) {
//                log.info("The Execute Flag is marked as No for the TestCase: {} " );
//            } else if (map.get("ExecuteTest").toString().equalsIgnoreCase("Yes")) {
//                testData.add(new Object[]{map});
//            }
//        }
//        System.out.println("Checking Test Data");
//        System.out.println(testData.iterator());
//        return testData.iterator();
//    }
//    public static Map<String, Object> convertIteratorToMap(Iterator<Object[]> iterator) {
//        Map<String, Object> resultMap = new HashMap<>();
//        System.out.println("Boolean Result: "+ iterator.hasNext());
//        while (iterator.hasNext()) {
//            Object[] testData = iterator.next();
//       // Get the next Object[] from the iterator
//            // Process each Object[] to extract key-value pairs
//            System.out.println("Array Length: "+testData.length);
//            if (testData.length > 0 && testData[0] instanceof Map) {
//                @SuppressWarnings("unchecked") // Suppress the unchecked cast warning
//                Map<String, Object> dataMap = (Map<String, Object>) testData[0];
//                // Now you have your dataMap that is a Map<String, Object>
//                // You can work with dataMap here, for example, print it or pass it to another method
//                System.out.println(dataMap);
//
//                // Example of passing dataMap to another method
//                // makeApiCall(dataMap); // Assuming you have a method that takes Map<String, Object> as parameter
//
//
//            }
//        }
//
//        return resultMap;
//    }
//}

package apicoreutils;

import helperfunctions.CommonUtils;
import io.qameta.allure.Allure;
import pojo.CommonPojo;
import reports.CustomHtmlReport;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExcelDatafetcher extends CustomHtmlReport {
    private static List<Map<String, Object>> testData;
    private static final ReadApiTestData readApiTestData = new ReadApiTestData();
    private static final Logger log = LogManager.getLogger(helperfunctions.ExcelDataProvider.class);
    private static ApiCoreModel apiCoreModel = new ApiCoreModel();
    public ExcelDatafetcher() {
        this.commonPojo = new CommonPojo();
    }

    public static int headerIteration(XSSFSheet sheet, int row, List<String> headers) {
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

    public static void readCellData(CellType cellType, Cell cell, List<Object> values) {
        if (cellType == CellType.NUMERIC) {
            double numberValue = cell.getNumericCellValue();
            values.add(numberValue);
        } else if (cellType == CellType.STRING) {
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
        } else if (cellType == CellType.BOOLEAN) {
            boolean cellValue = cell.getBooleanCellValue();
            values.add(cellValue);
        } else if (cellType == null || cellType == CellType.BLANK) {
            values.add("");
        }
    }

    public static List<Map<String, Object>> readExcel(String Workbook, String Worksheet ) {
        String excelWorkBookPath = "src//test//java//api//resources//testdata//"+Workbook;
        String excelSheetName = Worksheet;

        List<Map<String, Object>> testData = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(excelWorkBookPath)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet(excelSheetName);
            int rowCount = sheet.getLastRowNum();
            int columnCount = 0;
            for (int i = 0; i <= rowCount; i++) {
                String firstColumn = sheet.getRow(i).getCell(0).getStringCellValue();
                if (firstColumn.equalsIgnoreCase("Execute?") ) {
                    columnCount = headerIteration(sheet, i, headers);
                } else if(firstColumn.equalsIgnoreCase("Yes")){
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
                    testData.add(IntStream.range(0, headers.size()).boxed()
                            .collect(Collectors.toMap(headers::get, values::get)));
                    values.clear();
                }
            }
        } catch (IOException e) {
            log.info("The Excel Sheet provided is not correct:");
        }
        return testData;
    }
    public void loadDataIntoMethods_Itr() throws IOException {
        System.out.println("In 1st given step definitions");
        // Placeholder method for Cucumber
        if (testData == null) {
            testData = readApiTestData.readExcelData();
            for (Map<String, Object> dataMap : testData) {
                String testCaseName = (String) dataMap.get("Test_Case_Name");
                Map<String, Object> processedData = apiCoreModel.replaceFetchValues(dataMap);
                Allure.addAttachment("Prerequisite Raw Data for " + testCaseName, processedData.toString());
                System.out.println("Prepared Test Case: " + testCaseName);
            }
        }
    }
    public static List<Map<String, Object>> getTestData() {
        return testData;
    }

    public static Map<String, Object> getExcelData(String dataref,String Workbook, String Worksheet ) {
        Collection<Object[]> testData = new ArrayList<>();
        List<Map<String, Object>> tdList = readExcel(Workbook,Worksheet);
        for (Map<String, Object> map : tdList) {
            if (map.isEmpty()) {
                log.info("Empty Test Data is provided");
            } else if (map.get("Execute?").toString().equalsIgnoreCase("No")) {
//                log.info("The Execute Flag is marked as No for the TestCase:");
            } else if (map.get("Execute?").toString().equalsIgnoreCase("Yes") && map.get("DataBinding").toString().equalsIgnoreCase(dataref)) {
                testData.add(new Object[]{map});
            }
        }
        System.out.println("Checking Test Data");
        System.out.println(testData.iterator());

        return convertIteratorToMap(testData.iterator());
    }
    public static Map<String, Object> convertIteratorToMap(Iterator<Object[]> iterator) {
        Map<String, Object> resultMap = new HashMap<>();
        System.out.println("Boolean Result: " + iterator.hasNext());
        while (iterator.hasNext()) {
            Object[] testData = iterator.next();
            if (testData.length > 0 && testData[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> dataMap = (Map<String, Object>) testData[0];
                System.out.println(dataMap);
                resultMap=dataMap;
            }
        }
        return apiCoreModel.replaceFetchValues(resultMap);
    }
    /* Developed for Multiple Iterations in single @when tag
    @When("Make API call to run")
    public void makeApiCallToRun() throws Exception {
        List<Map<String, Object>> testData = getTestData();
        try {
            for (currentIndex = 0; currentIndex < testData.size(); currentIndex++) {
                Map<String, Object> currentTestData = testData.get(currentIndex);
                executeTestCase(currentTestData);
            }
        } catch (Exception e) {
            throw new RuntimeException("No more data sets available to run." + e);
        }
    }

    private void executeTestCase(Map<String, Object> currentTestData) throws Exception {
        Allure.addAttachment("Prerequisite Raw Data", currentTestData.toString());
        String testCaseName = (String) currentTestData.get("Test_Case_Name");
        Allure.step("Running Test Case: " + testCaseName);
        apiCoreModel.makeApiCall(currentTestData);

    } */
}
