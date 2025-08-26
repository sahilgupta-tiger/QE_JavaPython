package helperfunctions;

import com.opencsv.CSVReaderHeaderAware;
import webutils.LoadProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvDataReader {

    public Map<String, CsvRowData> readDataFromCsv(String filePath) {
        Map<String, CsvRowData> dataMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new IOException("CSV file is empty");
            }

            String[] headers = headerLine.split(",", -1);
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                CsvRowData rowData = new CsvRowData();
                Map<String, String> rowDataMap = new HashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    rowDataMap.put(headers[i].trim(), values[i].trim());
                }
                rowData.populateData(values[0].trim(), rowDataMap);
                dataMap.put(rowData.getTestCaseNo(), rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    public CsvRowData rowData(String filepath, String testCaseId) {
        CsvRowData rowData;
        Map<String, CsvRowData> csvRowData = readDataFromCsv(filepath);
        rowData = csvRowData.get(testCaseId);
        if (rowData == null) {
            throw new IllegalArgumentException("No data found for test case: " + testCaseId);
        }
        return rowData;
    }

    public static List<Map<String, String>> readCSVWithHeader(String filePath) throws Exception {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(filePath))) {
            Map<String, String> row;
            while ((row = reader.readMap()) != null) {
                dataList.add(row);
            }
        }
        System.out.println(dataList);
        return dataList;
    }

    public String[] getTestData(String filePath,String testCaseId, String header){
        CsvRowData rowData = rowData(LoadProperties.prop.getProperty(filePath), testCaseId);
        String category = rowData.getValue(header);
        String[] testData = null;
        if (category.contains("|")){
            testData = category.split("\\s*\\|\\s*");

        }
        else {
            testData = new String[1];
            testData[0] = category;
        }
        return testData;
    }
}
