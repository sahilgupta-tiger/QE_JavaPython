package helperfunctions;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExcelDataProvider {

    private static final Logger log = LogManager.getLogger(ExcelDataProvider.class);

    private ExcelDataProvider() {
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
            Boolean cellValue = cell.getBooleanCellValue();
            values.add(cellValue);
        } else if (cellType == null || cellType == CellType.BLANK) {
            values.add("");
        }
    }


    private static List<Map<String, Object>> readExcel(Method method, ITestContext context) {
        System.out.println(method);
        String excelWorkBookPath = context.getCurrentXmlTest().getParameter("excelWorkBook");
        String excelSheetName = context.getCurrentXmlTest().getParameter("excelSheetName");

        List<Map<String, Object>> testData = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelWorkBookPath))) {
            XSSFSheet sheet = workbook.getSheet(excelSheetName);
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
                    testData.add(IntStream.range(0, headers.size()).boxed()
                            .collect(Collectors.toMap(headers::get, values::get)));
                }
            }
        } catch (IOException e) {
            log.info("The Excel Sheet provided is not correct:");
        }
        return testData;
    }

    @DataProvider
    public static Iterator<Object[]> getExcelData(Method method, ITestContext context) {
        Collection<Object[]> testData = new ArrayList<>();
        System.out.println(method);
        String somename = method.getName();
        System.out.println(somename);
        List<Map<String, Object>> tdList = readExcel(method,context);
        for (Map<String, Object> map : tdList) {
            if (map.isEmpty()) {
                log.info("Empty Test Data is provided");
            } else if (map.get("ExecuteTest").toString().equalsIgnoreCase("No")) {
                log.info("The Execute Flag is marked as No for the TestCase: {} " + method.getName());
            } else if (map.get("ExecuteTest").toString().equalsIgnoreCase("Yes") && (map.get("TestCase").toString().equalsIgnoreCase(method.getName()))) {
                testData.add(new Object[]{map});
            }
        }
        return testData.iterator();
    }
}
