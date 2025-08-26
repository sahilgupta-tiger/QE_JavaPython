package helperfunctions;

import java.util.HashMap;
import java.util.Map;

public class CsvRowData {
    private String testCaseNo;
    private Map<String, String> data;

    public CsvRowData() {
        this.data = new HashMap<>();
    }

    public void populateData(String testCaseNo, Map<String, String> data) {
        this.testCaseNo = testCaseNo;
        this.data.putAll(data);
    }

    public String getTestCaseNo() {
        return testCaseNo;
    }

    public String getValue(String header) {
        return data.get(header);
    }

    public Map<String, String> getAllData() {
        return data;
    }
}
