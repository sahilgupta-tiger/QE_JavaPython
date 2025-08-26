package mobileutils;

import helperfunctions.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojo.CommonPojo;
import reports.CustomHtmlReport;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PosExcelDatafetcher extends CustomHtmlReport {
    private static final Logger log =
            LogManager.getLogger(helperfunctions.ExcelDataProvider.class);

    public PosExcelDatafetcher() {
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

    public static void readCellData(CellType cellType, Cell cell,
                                    List<Object> values) {
        if (cellType == CellType.NUMERIC) {
            double numberValue = cell.getNumericCellValue();
            values.add(numberValue);
        } else if (cellType == CellType.STRING) {
            String cellValue = cell.getStringCellValue();
            if (cellValue.contains("(") && cellValue.contains("char)")
                    || cellValue.contains("(") && cellValue.contains("digit)")) {

                String reqStr =
                        StringUtils.substringBetween(cellValue, "(", ")");
                int reqLength =
                        Integer.parseInt(reqStr.replaceAll("[^0-9]", ""));

                String randomStr;
                if (reqStr.toLowerCase().contains("digit")) {
                    randomStr =
                            String.valueOf(CommonUtils.generateRandomNumber(reqLength));
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

    public static List<Map<String, Object>> readExcel(String Workbook,String Worksheet) {
        String excelWorkBookPath = "src//test//java//posmobile//resources//testdata//" + Workbook;
        String excelSheetName = Worksheet;

        List<Map<String, Object>> testData = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(excelWorkBookPath)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet(excelSheetName);
            int rowCount = sheet.getLastRowNum();
            System.out.println("Rowcount: "+rowCount);
            int columnCount = 0;
            for (int i = 0; i <= rowCount; i++) {
                String firstColumn =
                        sheet.getRow(i).getCell(0).getStringCellValue();
                if (firstColumn.equalsIgnoreCase("Execute"))  {
                    columnCount = headerIteration(sheet, i, headers);
                  //  System.out.println("columnCount: "+columnCount);
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
                            .collect(Collectors.toMap(headers::get,
                                    values::get)));
                    values.clear();
                }
            }
        } catch (IOException e) {
            log.info("The Excel Sheet provided is not correct:");
        }
        return testData;
    }


    public static Map<String, String> getExcelData(String dataref,String Workbook,String Worksheet) {
        Collection<Object[]> testData = new ArrayList<>();

        List<Map<String, Object>> tdList = readExcel(Workbook,Worksheet);
        for (Map<String, Object> map : tdList) {
            if (map.isEmpty()) {
                log.info("Empty Test Data is provided");
            } else if (map.get("Execute").toString().equalsIgnoreCase("No")) {
                log.info("The Execute Flag is marked as No for the TestCase:");
            } else if
            (map.get("Execute").toString().equalsIgnoreCase("Yes") && map.get("TestCaseID").toString().equalsIgnoreCase(dataref)) {
                testData.add(new Object[]{map});
            }
        }
       // System.out.println("Checking Test Data");
        //System.out.println(testData.iterator());

        return convertIteratorToMap(testData.iterator());
    }
    public static Map<String, String>
    convertIteratorToMap(Iterator<Object[]> iterator) {
        Map<String, String> resultMap = new HashMap<>();
        System.out.println("Boolean Result: " + iterator.hasNext());
        while (iterator.hasNext()) {
            Object[] testData = iterator.next();
            if (testData.length > 0 && testData[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> dataMap = (Map<String, Object>) testData[0];
                System.out.println(dataMap);

                // Convert the Map<String, Object> to Map<String, String>
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    resultMap.put(key, value != null ? value.toString() : null);
                }
            }
        }
        return resultMap;
    }
}