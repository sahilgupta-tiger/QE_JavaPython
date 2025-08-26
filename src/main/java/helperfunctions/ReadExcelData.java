package helperfunctions;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ReadExcelData {

    public HashMap<String, Object> readExcel(String filePath, String fileName, String sheetName) throws IOException {

        HashMap<String, Object> map = new HashMap<String, Object>();

        File file = new File(filePath + "\\" + fileName);
        FileInputStream inputStream = new FileInputStream(file);
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        Workbook typeOfWorkbook = null;
        if(fileExtensionName.equals(".xlsx")){
            typeOfWorkbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtensionName.equals(".xls")){
            typeOfWorkbook = new HSSFWorkbook(inputStream);
        }

        Sheet testSheet = typeOfWorkbook.getSheet(sheetName);
        int rowCount = testSheet.getLastRowNum()-testSheet.getFirstRowNum();

        for (int i = 0; i < rowCount+1; i++) {
            String key = null;
            List<Object> values = new ArrayList<>();
            Row row = testSheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = testSheet.getRow(i).getCell(j);
                if(cell != null){
                    CellType cellType = cell.getCellType();
                    if (cellType == CellType.NUMERIC)
                    {
                        values.add(row.getCell(j).getNumericCellValue());
                    }
                    else if (cellType == CellType.STRING) {
                        String temp = row.getCell(j).getStringCellValue();
                        values.add(temp);
                        if(temp.startsWith("web_fr") || temp.startsWith("Test_")){
                            key = temp;
                        }
                    }
                    else if (cellType == CellType.BOOLEAN) {
                        values.add(row.getCell(j).getStringCellValue());
                    }
                    else if (cellType == null || cellType == CellType.BLANK) {
                        values.add(row.getCell(j).getBooleanCellValue());
                    }
                }
            }
            map.put(key,values);
        }
        return map;
    }
}
