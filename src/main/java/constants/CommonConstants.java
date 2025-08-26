package constants;

import helperfunctions.CommonUtils;
import reports.ReportManager;
import org.testng.ITestContext;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonConstants {

    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";
    public static final boolean TRUE_BOOLEAN = true;
    public static final String NULL;
    public static final String CONSOLIDATE_SUITES_REPORTS = "consolidate_suites_reports";
    public static final String EXECUTION_LOG = "EXECUTION_LOG";
    public static final String USER_PROPERTY_FILE_PATH = "src/test/resources/Configuration/Executionconfig.properties";
    public static final List<String> API_HEADER_VALUES;
    public static final String ASSERTION = "Assertion : ";
    public static final String XLSX = ".xlsx";
    public static final String XLSM = ".xlsm";
    public static final String YAML = ".yaml";
    public static final String JSON = ".json";
    public static final String JAVA = ".java";
    public static final String HTML = ".html";
    public static final File CUSTOM_HTML_REPORT;
    public static final File API_HTML_REPORT;
    public static final List<String> HEADER_VALUES;
    public static final String BUILD_NUMBER = "buildNumber";

    static {
        NULL = null;
        HEADER_VALUES = Collections.unmodifiableList((List<? extends String>) Arrays.asList("", "", "", "", "", ""));
        API_HEADER_VALUES = Collections.unmodifiableList((List<? extends String>) Arrays.asList("ApiTestCase", "Test Scenario", "HTTPMethod : EndPoint", "Expected StatusCode", "Actual StatusCode", "Response Time(Sec)", "Test Status", "Comments"));
        CUSTOM_HTML_REPORT = CommonUtils.createOrReadFile(null, "", ReportManager.resultDirectory + "/TestDataCustomRpt.html");
        API_HTML_REPORT = CommonUtils.createOrReadFile(null, "", ReportManager.resultDirectory + "/ApiHtmlReport.html");
    }

    ITestContext context;

    private CommonConstants() {
    }
}
