import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.*;

public class Testing {

    public static JSONArray convertExcelToJson(String excelPath, String sheetName) {
        JSONArray jsonArray = new JSONArray();

        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();

            // Read header row (keys)
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValue(cell));
            }

            // Iterate through each remaining row
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                JSONObject rowObject = new JSONObject();

                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String header = headers.get(i);
                    String value = getCellValue(cell);
                    rowObject.put(header, value);
                }

                jsonArray.put(rowObject);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    private static String getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> (DateUtil.isCellDateFormatted(cell))
                    ? cell.getDateCellValue().toString()
                    : String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK, _NONE, ERROR -> "";
        };
    }

    public static void main(String[] args) {
        String filePath = "src/test/java/api/resources/testdata/SDC_API_Response.xlsx";
        JSONArray json = convertExcelToJson(filePath, "API Data");
        System.out.println(json.toString(4));
    }
}
